package com.company.services.interfaces.inanimate;

import com.company.domain.inanimate.Group;
import com.company.domain.people.Student;
import com.company.domain.inanimate.subject.Work;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

public abstract class GroupService
        extends CrudService<Group, Long>
        implements Common<Group> {
    public abstract List<Student> getStudentsFromGroup(long groupId);
}
