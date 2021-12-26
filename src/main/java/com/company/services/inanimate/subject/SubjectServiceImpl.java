package com.company.services.inanimate.subject;

import com.company.domain.hei.Department;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.repository.dao.inanimate.subject.SubjectRepository;
import com.company.services.interfaces.inanimate.subject.SubjectService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;

@Service
public class SubjectServiceImpl extends SubjectService {

    @Getter
    @Autowired
    private SubjectRepository repository;

    @Override
    public Subject findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find subject with id " + id));
    }

    @Override
    public Subject make(Subject ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Subject> makeMany(Collection<Subject> collection) {
        Collection<Subject> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Subject> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
