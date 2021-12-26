package com.company.services.hei;

import com.company.domain.hei.Faculty;
import com.company.repository.dao.hei.FacultyRepository;
import com.company.services.interfaces.hei.FacultyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyServiceImpl extends FacultyService {

    @Getter
    @Autowired
    private FacultyRepository repository;

    @Override
    public Faculty findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find faculty with id " + id));
    }

    @Override
    public Faculty make(Faculty ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<Faculty> makeMany(Collection<Faculty> collection) {
        Collection<Faculty> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<Faculty> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
