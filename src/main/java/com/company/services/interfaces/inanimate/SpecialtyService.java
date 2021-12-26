package com.company.services.interfaces.inanimate;

import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class SpecialtyService
        extends CrudService<Specialty, Long>
        implements Common<Specialty> {
}
