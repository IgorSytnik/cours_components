package com.company.services.inanimate.subject;

import com.company.domain.inanimate.subject.Subject;
import com.company.domain.inanimate.subject.Work;
import com.company.repository.dao.inanimate.subject.WorkRepository;
import com.company.services.interfaces.inanimate.subject.WorkService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;

@Service
public class WorkServiceImpl extends WorkService {

    @Getter
    @Autowired
    private WorkRepository repository;

    @Override
    public Work make(Work ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Work> makeMany(Collection<Work> collection) {
        Collection<Work> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Work> getAll() {
        return repository.findAll();
    }

    @Override
    public Work findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find work with id " + id));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
