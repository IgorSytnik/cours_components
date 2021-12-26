package com.company.services.interfaces.inanimate.subject;

import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.GradeDate;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

public abstract class GradeService
        extends CrudService<Grade, Long>
        implements Common<Grade> {
}
