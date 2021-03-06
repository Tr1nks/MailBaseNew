package com.tr1nksgroup.controller.common;

import com.tr1nksgroup.model.engines.UploadEngine;
import com.tr1nksgroup.model.models.MyModel;
import com.tr1nksgroup.model.models.person.student.StudentModel;
import com.tr1nksgroup.model.models.upload.UploadModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Principal;

@Controller
@RequestMapping({"/c/upload"})
public class UploadController implements CommonController {
    private static final String VIEW_NAME = VIEW_BASE + "upload";
    private static final String MODEL_NAME = "uploadModel";
    private static final String STUDENT_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Группа;Бюджет\nИванов;Иван;Иванович;co32432de;6.04.51.1.17.1;true";
    private static final String TEACHER_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Кафедра;Ставка\nПетров;Петр;Петрович;co98765de;ИС;1,5";
    @Resource
    private UploadEngine uploadEngine;
    @Resource
    private StudentController studentController;

    @GetMapping
    public String get() {
        return VIEW_NAME;
    }

    /**
     * GET mapping обработчик для получения примера файла
     *
     * @param person   тип получаемого файла (студент преподаватель)
     * @param response ответ сервера для копирования файла
     */
    @GetMapping(path = "sample/{person}Sample.csv")
    public void getSample(@PathVariable("person") String person, HttpServletResponse response) {
        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream())) {
            response.setContentType("text/csv");
            if ("student".equals(person.toLowerCase())) {
                writer.write(STUDENT_SAMPLE_STR);
            } else if ("teacher".equals(person.toLowerCase())) {
                writer.write(TEACHER_SAMPLE_STR);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST mapping обработчик загрузки файла на сервер
     *
     * @param file файл
     * @return имя представления и модель с данными для отображения и настройки фильтров
     */
    @PostMapping(path = "file")
    public ModelAndView postFile(@RequestParam("file") MultipartFile file) {
        UploadModel uploadModel;
        if (!file.isEmpty()) {
            uploadModel = uploadEngine.uploadFile(file);
            return postTest(uploadModel);
        } else {
            return new ModelAndView(REDIRECT + "/c/upload?fileIsEmpty");
        }
    }

    /**
     * POST mapping обработчик проверки фильтров
     *
     * @param uploadModel данные страницы
     * @return имя представления и данные страницы с учетом фильтров
     */
    @PostMapping(path = "test")
    public ModelAndView postTest(@ModelAttribute(MODEL_NAME) UploadModel uploadModel) {
        uploadEngine.refillWithNewFilterData(uploadModel);
        return new ModelAndView(VIEW_NAME, MODEL_NAME, uploadModel);
    }

    /**
     * POST mapping обработчик парсинга файла
     *
     * @param uploadModel данные страницы
     * @return имя представления на которое будет перенамправлене и данные страницы этого представления
     */
    @PostMapping(path = "process")
    public String postProcess(@ModelAttribute(MODEL_NAME) UploadModel uploadModel, Principal principal, Model model) {
        MyModel parsedModel = uploadEngine.parseFromFile(uploadModel);
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal.getName().getClass().getName());
        if (parsedModel instanceof StudentModel) {
            model.addAttribute("studentModel", parsedModel);
            if (((StudentModel) parsedModel).getShowHiddenColumns()) {
                model.addAttribute(StudentController.UPLOAD_ERROR_FLAG_MODEL_NAME, true);
                return "common/student";
            } else {
                return "common/student";
            }
        } else {
            return "/error";//fixme error here, teacher should be
        }
    }
}
