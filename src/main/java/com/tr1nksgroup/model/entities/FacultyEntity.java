package com.tr1nksgroup.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * факультет
 * 6.[04].051.010.17.01
 */
@Entity
@Table(name = "faculty")
public class FacultyEntity implements Serializable {
    /**
     * id сущности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    /**
     * шифр факультета
     */
    @Basic
    @Column(name = "faculty_id", unique = true, nullable = false, length = 3)
    private int facultyId;
    /**
     * название факультета
     */
    @Basic
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
    /**
     * название факультета
     */
    @Basic
    @Column(name = "abbr", unique = true, nullable = false, length = 5)
    private String abbr;
    /**
     * кафедры факультета
     */
    @OneToMany(mappedBy = "facultyEntity")
    private List<CathedraEntity> cathedraEntities;
    /**
     * группы факультета
     */
    @OneToMany(mappedBy = "facultyEntity")
    private List<GroupEntity> groupEntities;

    public FacultyEntity(int facultyId, String name, String abbr) {
        this.facultyId = facultyId;
        this.name = name;
        this.abbr = abbr;
    }

    private FacultyEntity() {
    }

    //region get-set

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public List<GroupEntity> getGroupEntities() {
        return groupEntities;
    }

    public void setGroupEntities(List<GroupEntity> groupEntities) {
        this.groupEntities = groupEntities;
    }

    public List<CathedraEntity> getCathedraEntities() {
        return cathedraEntities;
    }

    public void setCathedraEntities(List<CathedraEntity> cathedraEntities) {
        this.cathedraEntities = cathedraEntities;
    }
    //endregion
}
