package com.company.services.interfaces.inanimate.subject;

import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class SubjectService
        extends CrudService<Subject, Long>
        implements Common<Subject> {
}
