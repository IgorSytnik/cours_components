package com.company.services.interfaces.inanimate.subject;

import com.company.domain.hei.Department;
import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.GradeDate;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class GradeDateService
        extends CrudService<GradeDate, Long>
        implements Common<GradeDate> {
}
