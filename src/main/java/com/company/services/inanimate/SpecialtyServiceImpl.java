package com.company.services.inanimate;

import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.repository.dao.inanimate.SpecialtyRepository;
import com.company.services.interfaces.inanimate.SpecialtyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;

@Service
public class SpecialtyServiceImpl extends SpecialtyService {

    @Getter
    @Autowired
    SpecialtyRepository repository;

    @Override
    public Specialty make(Specialty ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Specialty> makeMany(Collection<Specialty> collection) {
        Collection<Specialty> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Specialty> getAll() {
        return repository.findAll();
    }

    @Override
    public Specialty findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find specialty with id " + id));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
