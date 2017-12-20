package com.tr1nksgroup.model.models.person.student;

import com.tr1nksgroup.model.entities.StudentEntity;
import com.tr1nksgroup.model.models.enums.person.TableColumnIndexes;
import com.tr1nksgroup.model.models.enums.person.TableRowStyleClass;

public class StudentEntityWrapper {
    private Boolean checked = false;
    private Boolean readonly = true;
    private StudentEntity studentEntity;
    private TableRowStyleClass rowStyle = TableRowStyleClass.DEFAULT;
    private Integer cellIndex;
    private TableRowStyleClass cellStyle = TableRowStyleClass.DEFAULT;
    private String message;

    public StudentEntityWrapper(StudentEntity studentEntity, Integer cellIndex, String message, TableRowStyleClass rowStyle, TableRowStyleClass cellStyle) {
        this(studentEntity, rowStyle);
        this.cellIndex = cellIndex;
        this.message = message;
        this.cellStyle = cellStyle;
    }

    public StudentEntityWrapper(StudentEntity studentEntity, TableRowStyleClass rowStyle) {
        this(studentEntity);
        this.rowStyle = rowStyle;
    }

    public StudentEntityWrapper(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    public StudentEntityWrapper() {
    }

    //region get-set
    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    public TableRowStyleClass getRowStyle() {
        return rowStyle;
    }

    public void setRowStyle(TableRowStyleClass rowStyle) {
        this.rowStyle = rowStyle;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TableRowStyleClass getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(TableRowStyleClass cellStyle) {
        this.cellStyle = cellStyle;
    }

    public void setCellMessageAndStyle(TableColumnIndexes cellIndex, String message, TableRowStyleClass cellStyle) {
        this.cellIndex = cellIndex.index;
        this.message = message;
        this.cellStyle = cellStyle;
    }

    public void setCellMessageAndStyleAndRowStyle(TableColumnIndexes cellIndex, String message, TableRowStyleClass cellStyle, TableRowStyleClass rowStyle) {
        this.setCellMessageAndStyle(cellIndex, message, cellStyle);
        this.rowStyle = rowStyle;
    }
    //endregion
}
