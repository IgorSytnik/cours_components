package com.company.services.interfaces.inanimate;

import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.subject.Work;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

public abstract class GroupsSubjectsService
        extends CrudService<GroupsSubjects, Long>
        implements Common<GroupsSubjects> {
    public abstract Collection<Work> getWorks(long id);
}
