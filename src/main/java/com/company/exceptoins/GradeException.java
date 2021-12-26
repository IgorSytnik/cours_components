package com.company.exceptoins;

public class GradeException extends RuntimeException {
    public GradeException(String subjectName) {
        super("Can't add grade to subject \""+ subjectName +"\"");
    }
}
