package com.tr1nksgroup.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * кафедра
 */
@Entity
@Table(name = "cathedra")
public class CathedraEntity implements Serializable {
    /**
     * id сущности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    /**
     * факультет к которому предлежит кафедра
     */
    @Basic
    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id", nullable = false)
    private FacultyEntity facultyEntity;
    /**
     * название кафедры
     */
    @Basic
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    /**
     * аббревиатура названия кафедры
     */
    @Basic
    @Column(name = "abbr", unique = true, nullable = false, length = 7)
    private String abbr;
    /**
     * преподаватели кафедры
     */
    @OneToMany(mappedBy = "cathedra")
    private List<TeacherEntity> teacherEntities;

    public CathedraEntity(FacultyEntity facultyEntity, String name, String abbr) {
        this.facultyEntity = facultyEntity;
        this.name = name;
        this.abbr = abbr;
    }

    private CathedraEntity() {
    }

    //region get-set

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FacultyEntity getFacultyEntity() {
        return facultyEntity;
    }

    public void setFacultyEntity(FacultyEntity facultyEntity) {
        this.facultyEntity = facultyEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getAbbr() {
        return abbr;
    }

    public List<TeacherEntity> getTeacherEntities() {
        return teacherEntities;
    }

    public void setTeacherEntities(List<TeacherEntity> teacherEntities) {
        this.teacherEntities = teacherEntities;
    }


    //endregion
}
