package com.company.services.interfaces.hei;

import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.services.interfaces.Common;
import org.vaadin.artur.helpers.CrudService;

import java.util.Date;

public abstract class DepartmentService
        extends CrudService<Department, Long>
        implements Common<Department> {
    public abstract void addFirstAttestDates(long departmentId, Date beg, Date end);
    public abstract void addSecondAttestDates(long departmentId, Date beg, Date end);
}
