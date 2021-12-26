package com.company.services.inanimate.subject;

import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.subject.GradeDate;
import com.company.repository.dao.inanimate.subject.GradeDateRepository;
import com.company.repository.dao.inanimate.subject.GradeRepository;
import com.company.services.interfaces.inanimate.subject.GradeDateService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service
public class GradeDateServiceImpl extends GradeDateService {

    @Getter
    @Autowired
    private GradeDateRepository repository;

    @Override
    public GradeDate make(GradeDate ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<GradeDate> makeMany(Collection<GradeDate> collection) {
        Collection<GradeDate> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public Collection<GradeDate> getAll() {
        return repository.findAll();
    }

    @Override
    public GradeDate findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find grade date with id " + id));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
