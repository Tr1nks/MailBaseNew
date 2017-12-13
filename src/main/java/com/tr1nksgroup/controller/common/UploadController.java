package com.tr1nksgroup.controller.common;

import com.tr1nksgroup.model.engines.UploadEngine;
import com.tr1nksgroup.model.models.UploadModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Controller
@RequestMapping({"/c/upload"})
public class UploadController implements AbstrCommonController {
    private static final String VIEW_NAME = VIEW_BASE + "upload";
    private static final String MODEL_NAME = "uploadModel";
    private static final String STUDENT_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Группа;Бюджет\nИванов;Иван;Иванович;co32432de;6.1.11.11.11.1;true";
    private static final String TEACHER_SAMPLE_STR = "Фамилия;Имя;Отчество;Код;Кафедра;Ставка\nПетров;Петр;Петрович;co98765de;ИС;1,5";
    @Resource
    UploadEngine uploadEngine;

    @GetMapping
    public String get() {
        return VIEW_NAME;
    }

    /**
     * GET mapping обработчик для получения примера файла
     *
     * @param person   тип получаемого файла (студент преподаватель)
     * @param response ответ сервера для копирования файла
     * @return имя представления
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
        UploadModel uploadModel = null;
        if (!file.isEmpty()) {
            uploadModel = uploadEngine.uploadFile(file);
        }
        return new ModelAndView(VIEW_NAME, MODEL_NAME, "ew");
    }
}