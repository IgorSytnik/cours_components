package com.company.services.interfaces.inanimate.subject;

import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.people.Student;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.Date;
import java.util.Map;

public abstract class ListHasStudentsService
        extends CrudService<ListHasStudents, Long>
        implements Common<ListHasStudents> {
//    void addAttestations(Map<ListHasStudents, Boolean> mapAttest);
    public abstract void addFirstAttest(long listHasStudentsId, boolean attest);
    public abstract void addSecondAttest(long listHasStudentsId, boolean attest);
}
