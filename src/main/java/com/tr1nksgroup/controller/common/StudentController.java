package com.tr1nksgroup.controller.common;

import com.tr1nksgroup.model.engines.StudentEngine;
import com.tr1nksgroup.model.models.filters.FilterModel;
import com.tr1nksgroup.model.models.person.student.StudentModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/c/student"})
public class StudentController implements CommonController {
    private static final String VIEW_NAME = VIEW_BASE + "student";
    static final String UPLOAD_ERROR_FLAG_MODEL_NAME = "uploadError";
    private static final String STUDENT_MODEL_NAME = "studentModel";
    private static final String STUDENT_FILTER_MODEL_NAME = "studentFilterModel";
    @Resource
    private StudentEngine studentEngine;

    @GetMapping
    public String get(Model model) {
//        model.addAttribute(STUDENT_MODEL_NAME, studentEngine.getTest());//fixme for debug only
        model.addAttribute(STUDENT_FILTER_MODEL_NAME, studentEngine.getFilterModel());//fixme add all filters
        return VIEW_NAME;
    }


    @PostMapping(path = "filter")
    public String postBudget(Model model, @ModelAttribute(STUDENT_FILTER_MODEL_NAME) FilterModel filterModel) {
        model.addAttribute(STUDENT_MODEL_NAME, studentEngine.getStudentsByFilters(filterModel));
        return VIEW_NAME;
    }

    @PostMapping(path = "budget/{action}")
    public String postBudget(@PathVariable("action") String action,Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        studentEngine.budgetSetRem(studentModel,action);
        return VIEW_NAME;
    }

    @PostMapping(path = "imagine/{action}")
    public String postImagine(@PathVariable("action") String action, Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        studentEngine.imagineSetRem(studentModel,action);
        return VIEW_NAME;
    }

    @PostMapping(path = "office/{action}")
    public String postOffice(@PathVariable("action") String action, Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        studentEngine.officeSetRem(studentModel,action);
        return VIEW_NAME;
    }

    @PostMapping(path = "email")
    public String postEmail(Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        //todo send email
        return VIEW_NAME;
    }

    @PostMapping(path = "archives")
    public String postArchives(Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        //todo get archives
        return VIEW_NAME;
    }

    @PostMapping(path = "edit/{action}")
    public String postEdit(@PathVariable("action") String action, Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        //todo get archives
        return VIEW_NAME;
    }

    @PostMapping(path = "upload/repeat")
    public String post(Model model, @ModelAttribute(STUDENT_MODEL_NAME) StudentModel studentModel) {
        model.addAttribute(UPLOAD_ERROR_FLAG_MODEL_NAME, !studentEngine.uploadRepeat(studentModel));
        return VIEW_NAME;
    }
}
