package com.company.services.people;

import com.company.domain.inanimate.StudentsHasWorks;
import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.people.Student;
import com.company.repository.dao.people.StudentRepository;
import com.company.services.interfaces.people.StudentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends StudentService {

    @Getter
    @Autowired
    private StudentRepository repository;

    @Override
    public Student make(Student ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Student> makeMany(Collection<Student> collection) {
        Collection<Student> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Student findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find student with id " + id));
    }

    @Override
    public List<Grade> getGrades(Student student, Subject subject) {
        return student.getListHasStudentsList().stream()
                .filter(ob -> ob.getGroupsSubjects().
                        getSubject().
                        equals(subject))
                .flatMap(ob -> ob.getGrades().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<ListHasStudents> getAttestations(Student student, Subject subject) {
        return student.getListHasStudentsList().stream()
                .filter(ob -> ob.getGroupsSubjects()
                        .getSubject()
                        .equals(subject))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
