package com.tr1nksgroup.model.models.upload;

import com.tr1nksgroup.model.models.MyModel;
import com.tr1nksgroup.model.models.enums.upload.CodePagesEnum;
import com.tr1nksgroup.model.models.enums.upload.DelimiterEnum;
import com.tr1nksgroup.model.models.enums.upload.PersonEnum;
import com.tr1nksgroup.model.models.enums.upload.UploadFileMaskEnum;

/**
 * модель для страницы upload
 */
public class UploadModel implements MyModel {
    /**
     * кодировка
     */
    private CodePagesEnum codepage = CodePagesEnum.UTF8;
    /**
     * символ-разделитель
     */
    private DelimiterEnum delimiter = DelimiterEnum.SEMICOLON;
    /**
     * тип персоны
     */
    private PersonEnum person = PersonEnum.STUDENT;
    /**
     * имя файла на сервере
     */
    private String filename;
    /**
     * строки предпросмотра
     */
    private String[][] fileStrings = new String[2][6];
    /**
     * маска столбцов файла
     */
    private UploadFileMaskEnum[] mask = new UploadFileMaskEnum[]{UploadFileMaskEnum.SURNAME, UploadFileMaskEnum.NAME, UploadFileMaskEnum.PATRONYMIC, UploadFileMaskEnum.CODE, UploadFileMaskEnum.GROUP_OR_CATHEDRA, UploadFileMaskEnum.BUDGET_OR_RATE};


    //region get-set
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public CodePagesEnum getCodepage() {
        return codepage;
    }

    public void setCodepage(CodePagesEnum codepage) {
        this.codepage = codepage;
    }

    public DelimiterEnum getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(DelimiterEnum delimiter) {
        this.delimiter = delimiter;
    }

    public PersonEnum getPerson() {
        return person;
    }

    public void setPerson(PersonEnum person) {
        this.person = person;
    }

    public String[][] getFileStrings() {
        return fileStrings;
    }

    public void setFileStrings(String[][] fileStrings) {
        this.fileStrings = fileStrings;
    }

    public UploadFileMaskEnum[] getMask() {
        return mask;
    }

    public void setMask(UploadFileMaskEnum[] mask) {
        this.mask = mask;
    }
    //endregion
}
