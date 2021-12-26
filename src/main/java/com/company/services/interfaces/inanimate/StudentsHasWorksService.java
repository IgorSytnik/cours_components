package com.company.services.interfaces.inanimate;

import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.StudentsHasWorks;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

public abstract class StudentsHasWorksService
        extends CrudService<StudentsHasWorks, Long>
        implements Common<StudentsHasWorks> {
}
