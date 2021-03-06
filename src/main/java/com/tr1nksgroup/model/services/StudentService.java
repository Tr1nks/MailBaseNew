package com.tr1nksgroup.model.services;

import com.tr1nksgroup.model.entities.GroupEntity;
import com.tr1nksgroup.model.entities.StudentEntity;

import java.util.List;

public interface StudentService {
    boolean testEmail(String s);

    boolean testCode(String s);

    StudentEntity save(StudentEntity student);

    List<StudentEntity> getAll();

    List<StudentEntity> getAllByFacultyIds(List<Long> facultyIds);

    List<StudentEntity> getAllByGroupIds(List<Long> groupIds);

    List<StudentEntity> getAllByGroupYears(List<Integer> years);

    StudentEntity getById(long id);

    StudentEntity getBySurnameAndNameAndGroup(String surname, String name, GroupEntity groupEntity);
}
