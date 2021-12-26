package com.company.services.interfaces.hei;

import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.Group;
import com.company.domain.people.Student;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

public abstract class FacultyService
        extends CrudService<Faculty, Long>
        implements Common<Faculty> {
}
