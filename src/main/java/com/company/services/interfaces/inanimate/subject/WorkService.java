package com.company.services.interfaces.inanimate.subject;

import com.company.domain.inanimate.subject.Subject;
import com.company.domain.inanimate.subject.Work;
import com.company.domain.people.Student;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class WorkService
        extends CrudService<Work, Long>
        implements Common<Work> {
}
