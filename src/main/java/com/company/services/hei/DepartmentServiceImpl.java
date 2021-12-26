package com.company.services.hei;

import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.subject.GradeDate;
import com.company.repository.dao.hei.DepartmentRepository;
import com.company.services.interfaces.hei.DepartmentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl extends DepartmentService {

    @Getter
    @Autowired
    private DepartmentRepository repository;

    @Override
    public Department make(Department ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Department> makeMany(Collection<Department> collection) {
        Collection<Department> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Department> getAll() {
        return repository.findAll();
    }

    @Override
    public Department findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find department with id " + id));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void addFirstAttestDates(long departmentId, Date beg, Date end) {
        Department department = repository.getOne(departmentId);
        department.setFirstAttestationBeg(beg);
        department.setFirstAttestationEnd(end);
        repository.saveAndFlush(department);
    }

    @Override
    public void addSecondAttestDates(long departmentId, Date beg, Date end) {
        Department department = repository.getOne(departmentId);
        department.setSecondAttestationBeg(beg);
        department.setSecondAttestationEnd(end);
        repository.saveAndFlush(department);
    }


    /*public List<Group> findGroupsByYear(int year) {
        throw new NotImplementedException("");
    }
    public int countStudentsByYear(int year) {
        throw new NotImplementedException("");
    }
    public Group maxStudentsInGroup() {
        throw new NotImplementedException("");
    }
    public double avgNumberOfStudents() {
        throw new NotImplementedException("");
    }
    public Map<Boolean, List<Group>> splitGroupsByYear(int year) {
        throw new NotImplementedException("");
    }
    public List<Teacher> findTeachersByPosition(AcademicPosition position) {
        throw new NotImplementedException("");
    }
    public Map<Boolean, List<Teacher>> splitTeachersByPosition(AcademicPosition position) {
        throw new NotImplementedException("");
    }*/
}
