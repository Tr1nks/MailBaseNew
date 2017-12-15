package com.tr1nksgroup.model.entities;

import javax.persistence.*;
import java.util.List;

/**
 * академ группа
 */
@Entity
@Table(name = "academ_group")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "select_by_cipher",
                procedureName = "academ_group.select_by_cipher",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_studylevel_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_faculty_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_specialization_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_speciality_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_year", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "in_num", type = Integer.class)
                })
})
public class GroupEntity extends AbstrEntity {
    /**
     * уровень обучения
     * [6].04.051.010.17.01
     */
    @Basic
    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "id", nullable = false)
    private StudyLevelEntity studyLevelEntity;
    /**
     * факультет к которому предлежит группа
     * 6.[04].051.010.17.01
     */
    @Basic
    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id", nullable = false)
    private FacultyEntity facultyEntity;
    /**
     * специализация группы
     * 6.04.051.[010].17.01
     */
    @Basic
    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id", nullable = false)
    private SpecializationEntity specializationEntity;
    /**
     * год поступления
     * 6.04.051.010.[17].01
     */
    @Basic
    @Column(name = "year", nullable = false, length = 3)
    private int year;
    /**
     * номер группы
     * 6.04.051.010.17.[01]
     */
    @Basic
    @Column(name = "num", nullable = false, length = 3)
    private int num;
    /**
     * студенты группы
     */
    @OneToMany(mappedBy = "group")
    private List<StudentEntity> students;

    public GroupEntity(StudyLevelEntity studyLevelEntity, FacultyEntity facultyEntity, SpecializationEntity specializationEntity, Integer year, Integer num) {
        this.studyLevelEntity = studyLevelEntity;
        this.facultyEntity = facultyEntity;
        this.specializationEntity = specializationEntity;
        this.year = year;
        this.num = num;
    }


    //region get-set

    public StudyLevelEntity getStudyLevelEntity() {
        return studyLevelEntity;
    }

    public void setStudyLevelEntity(StudyLevelEntity studyLevelEntity) {
        this.studyLevelEntity = studyLevelEntity;
    }

    public FacultyEntity getFacultyEntity() {
        return facultyEntity;
    }

    public void setFacultyEntity(FacultyEntity facultyEntity) {
        this.facultyEntity = facultyEntity;
    }

    public SpecializationEntity getSpecializationEntity() {
        return specializationEntity;
    }

    public void setSpecializationEntity(SpecializationEntity specializationEntity) {
        this.specializationEntity = specializationEntity;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(List<StudentEntity> students) {
        this.students = students;
    }
    //endregion
}
