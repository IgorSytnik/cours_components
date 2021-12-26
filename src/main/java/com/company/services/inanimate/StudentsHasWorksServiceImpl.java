package com.company.services.inanimate;

import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.StudentsHasWorks;
import com.company.repository.dao.inanimate.StudentsHasWorksRepository;
import com.company.services.interfaces.inanimate.StudentsHasWorksService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;

@Service
public class StudentsHasWorksServiceImpl extends StudentsHasWorksService {

    @Getter
    @Autowired
    StudentsHasWorksRepository repository;

    @Override
    public StudentsHasWorks make(StudentsHasWorks ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<StudentsHasWorks> makeMany(Collection<StudentsHasWorks> collection) {
        Collection<StudentsHasWorks> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public Collection<StudentsHasWorks> getAll() {
        return repository.findAll();
    }

    @Override
    public StudentsHasWorks findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find student's work with id " + id));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
