package com.company.services.inanimate.subject;

import com.company.domain.hei.Department;
import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.people.Student;
import com.company.repository.dao.inanimate.GroupRepository;
import com.company.repository.dao.inanimate.subject.ListHasStudentsRepository;
import com.company.services.interfaces.inanimate.subject.ListHasStudentsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ListHasStudentsServiceImpl extends ListHasStudentsService {

    @Getter
    @Autowired
    private ListHasStudentsRepository repository;

    @Override
    public ListHasStudents make(ListHasStudents ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<ListHasStudents> makeMany(Collection<ListHasStudents> collection) {
        Collection<ListHasStudents> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<ListHasStudents> getAll() {
        return repository.findAll();
    }

    @Override
    public ListHasStudents findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find student list with id " + id));
    }

    @Override
    public void addFirstAttest(long listHasStudentsId, boolean attest) {
        ListHasStudents listHasStudents = repository.getOne(listHasStudentsId);
        listHasStudents.setAttest1(attest);
        repository.saveAndFlush(listHasStudents);
    }

    @Override
    public void addSecondAttest(long listHasStudentsId, boolean attest) {
        ListHasStudents listHasStudents = repository.getOne(listHasStudentsId);
        listHasStudents.setAttest2(attest);
        repository.saveAndFlush(listHasStudents);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
