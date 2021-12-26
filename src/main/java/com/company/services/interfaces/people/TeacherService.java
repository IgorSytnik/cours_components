package com.company.services.interfaces.people;

import com.company.domain.people.Teacher;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class TeacherService
        extends CrudService<Teacher, Long>
        implements Common<Teacher> {
}
