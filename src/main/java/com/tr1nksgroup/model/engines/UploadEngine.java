package com.tr1nksgroup.model.engines;

import com.tr1nksgroup.model.components.LoginPasswordUtil;
import com.tr1nksgroup.model.entities.GroupEntity;
import com.tr1nksgroup.model.entities.SpecialityEntity;
import com.tr1nksgroup.model.entities.SpecializationEntity;
import com.tr1nksgroup.model.entities.StudentEntity;
import com.tr1nksgroup.model.models.MyModel;
import com.tr1nksgroup.model.models.enums.person.TableColumnIndexes;
import com.tr1nksgroup.model.models.enums.person.TableRowStyleClass;
import com.tr1nksgroup.model.models.enums.upload.PersonEnum;
import com.tr1nksgroup.model.models.enums.upload.UploadFileMaskEnum;
import com.tr1nksgroup.model.models.person.student.StudentEntityTableWrapper;
import com.tr1nksgroup.model.models.person.student.StudentModel;
import com.tr1nksgroup.model.models.person.teacher.TeacherModel;
import com.tr1nksgroup.model.models.upload.UploadModel;
import com.tr1nksgroup.model.services.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UploadEngine {
    private static final String FILE_STORAGE_PATH = "./fileStorage/";
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final Pattern patternInnerGroupChiper = Pattern.compile("^0");
    private static final Pattern patternOuterGroupChiper = Pattern.compile("\\.0");
    @Resource
    private StudentService studentService;
    @Resource
    private GroupService groupService;
    @Resource
    private FacultyService facultyService;
    @Resource
    private StudyLevelService studyLevelService;
    @Resource
    private SpecialityService specialityService;
    @Resource
    private SpecializationService specializationService;
    @Resource
    private WorkSessionService workSessionService;
    @Resource
    private LoginPasswordUtil loginPasswordUtil;

    /**
     * загрузить файл
     *
     * @param file файл
     * @return данные страницы загрузки на сервер с предпросмотром по фильтрам по умолчанию
     */
    public UploadModel uploadFile(MultipartFile file) {
        if (!new File(FILE_STORAGE_PATH).exists()) {
            new File(FILE_STORAGE_PATH).mkdir();
        }
        String fn = dateFormat.format(new Date()) + "----" + file.getOriginalFilename();
        int add = 1;
        while (new File(FILE_STORAGE_PATH + fn).exists()) {
            fn = fn.replaceFirst("--\\d*--", "--" + add + "--");
            add++;
        }//todo refactor filenames on dirs with date
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(FILE_STORAGE_PATH + fn)))) {
            stream.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        UploadModel uploadModel = new UploadModel();
        uploadModel.setFilename(fn);
        return uploadModel;
    }

    /**
     * пере-заполнить с учетом даных фильтров
     *
     * @param uploadModel пере-заполненные данные страницы
     */
    public void refillWithNewFilterData(UploadModel uploadModel) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_STORAGE_PATH + uploadModel.getFilename()), uploadModel.getCodepage().getValue()))) {
            String buf;
            byte i = 0;
            while (i < 2 && (buf = reader.readLine()) != null) {
                uploadModel.getFileStrings()[i] = buf.split(uploadModel.getDelimiter().getValue());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * парсить из файла
     *
     * @param uploadModel данные страницы
     * @return данные для страницы с загруженными персонами
     */
    public MyModel parseFromFile(UploadModel uploadModel) {
        MyModel myModel = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_STORAGE_PATH + uploadModel.getFilename()), uploadModel.getCodepage().getValue()))) {
            if (PersonEnum.STUDENT.equals(uploadModel.getPerson())) {
                myModel = parseStudents(reader, uploadModel.getDelimiter().getValue(), uploadModel.getMask());
            } else if (PersonEnum.TEACHER.equals(uploadModel.getPerson())) {
                myModel = new TeacherModel();//stub fixme
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myModel;
    }

    /**
     * парсить студентов из файла
     *
     * @param reader    читатель файла
     * @param delimiter символ-разделитель в строке
     * @param mask      маска положения столбцов
     * @return данные страницы студенты
     * @throws IOException ошибки чтения reader-ом
     */
    private StudentModel parseStudents(BufferedReader reader, String delimiter, UploadFileMaskEnum[] mask) throws IOException {
        StudentModel studentModel = new StudentModel();
        List<Long> ids = new ArrayList<>();
        String buf;
        while ((buf = reader.readLine()) != null) {
            if (buf.toLowerCase().contains("имя") || buf.toLowerCase().contains("name")) {
                continue;
            }
            String[] arr = parseArrOnMask(buf.split(delimiter), mask);
            arr[4] = patternOuterGroupChiper.matcher((patternInnerGroupChiper.matcher(arr[4]).replaceAll(""))).replaceAll(".");
            String[] groupCipherArray = arr[4].split("\\.");
            GroupEntity groupEntity = groupService.getByStudyLevelEntityAndFacultyEntityAndSpecializationEntityAndYearAndNum(
                    studyLevelService.getByLevelId(Integer.parseInt(groupCipherArray[0])),
                    facultyService.getByFacultyId(Integer.parseInt(groupCipherArray[1])),
                    specializationService.getBySpecializationIdAndSpecialityEntity(Integer.parseInt(groupCipherArray[3]),
                            specialityService.getBySpecialityId(Integer.parseInt(groupCipherArray[2]))),
                    Integer.parseInt(groupCipherArray[4]),
                    Integer.parseInt(groupCipherArray[5]));
            if (null == groupEntity) {
                SpecializationEntity specialization = getCreateSpecialization(Integer.parseInt(groupCipherArray[3]), Integer.parseInt(groupCipherArray[2]));
                groupEntity = new GroupEntity(
                        studyLevelService.getByLevelId(Integer.parseInt(groupCipherArray[0])),
                        facultyService.getByFacultyId(Integer.parseInt(groupCipherArray[1])),
                        specialization,
                        Integer.valueOf(groupCipherArray[4]),
                        Integer.valueOf(groupCipherArray[5])
                );
                groupService.save(groupEntity);
            }
            StudentEntity student = new StudentEntity(arr[0], arr[1], arr[2], arr[3], groupEntity, loginPasswordUtil.createLogin(arr[0], arr[1]), loginPasswordUtil.generatePassword(8), parseTrueFlag(arr[5]));
            StudentEntityTableWrapper wrapper = new StudentEntityTableWrapper();
            if (studentService.testCode(arr[3])) {
                student = studentService.save(student);
                wrapper.setStudentEntity(student);
                wrapper.setRowStyle(TableRowStyleClass.SUCCESS);
            } else {
                wrapper.setStudentEntity(student);
                wrapper.setChecked(true);
                wrapper.setReadonly(false);
                wrapper.setCellMessageAndStyleAndRowStyle(TableColumnIndexes.CODE, "Такой код ЕДБО уже есть в Базе Данных", TableRowStyleClass.DANGER, TableRowStyleClass.WARNING);
                studentModel.setShowHiddenColumns(true);
            }
            ids.add(wrapper.getStudentEntity().getId());
            studentModel.getStudentEntityTableWrappers().add(wrapper);
        }
        return studentModel;
    }

    private SpecializationEntity getCreateSpecialization(int specializationId, int specialityId) {
        SpecializationEntity specializationEntity = specializationService.getBySpecializationIdAndSpecialityEntity(specializationId,
                specialityService.getBySpecialityId(specialityId));
        if (specializationEntity == null) {
            SpecialityEntity specialityEntity = specialityService.getBySpecialityId(specialityId);
            if (null == specialityEntity) {
                specialityEntity = new SpecialityEntity(specialityId, "STUB-" + specialityId);
                specialityService.save(specialityEntity);
            }
            specializationEntity = new SpecializationEntity(specializationId, "STUB-" + specializationId + "-" + specialityEntity.getName(), specialityEntity);
            specializationService.save(specializationEntity);
        }
        return specializationEntity;
    }

    /**
     * разложить массив в правильном порядке по маске
     *
     * @param inp  входной массив
     * @param mask маска
     * @return массив  в порядке : {фамилия, имя, отчество, код, группа, бюджет}
     */
    private String[] parseArrOnMask(String[] inp, UploadFileMaskEnum[] mask) {
        int i = 0;
        String[] out = new String[6];
        while (i < mask.length && i < inp.length) {
            switch (mask[i]) {
                case SURNAME:
                    out[0] = inp[i];
                    break;
                case NAME:
                    out[1] = inp[i];
                    break;
                case PATRONYMIC:
                    out[2] = inp[i];
                    break;
                case CODE:
                    out[3] = inp[i];
                    break;
                case GROUP_OR_CATHEDRA:
                    out[4] = inp[i];
                    break;
                case BUDGET_OR_RATE:
                    out[5] = inp[i];
                    break;
                default:
                    break;
            }
            i++;
        }
        return out;
    }

    /**
     * преобразование во флаг о наличии
     *
     * @param s строка для преобразования
     * @return true если строка соответствует "true","да","+","yes","1"  и false в остальных случаях
     */
    private boolean parseTrueFlag(String s) {
        return s.equals("true") || s.equals("да") || s.equals("+") || s.equals("yes") || s.equals("1");
    }

}
