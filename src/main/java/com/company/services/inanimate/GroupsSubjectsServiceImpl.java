package com.company.services.inanimate;

import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.subject.Work;
import com.company.repository.dao.inanimate.GroupsSubjectsRepository;
import com.company.services.interfaces.inanimate.GroupsSubjectsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Collection;
import java.util.List;

@Service
public class GroupsSubjectsServiceImpl extends GroupsSubjectsService {

    @Getter
    @Autowired
    GroupsSubjectsRepository repository;

    @Override
    public GroupsSubjects make(GroupsSubjects ob) {
        return repository.saveAndFlush(ob);
    }

    @Override
    public Collection<GroupsSubjects> makeMany(Collection<GroupsSubjects> collection) {
        Collection<GroupsSubjects> collection1 = repository.saveAll(collection);
        repository.flush();
        return collection1;
    }

    @Override
    public List<GroupsSubjects> getAll() {
        return repository.findAll();
    }

    @Override
    public GroupsSubjects findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find group-subject with id " + id));
    }

    @Override
    public List<Work> getWorks(long id) {
        return findById(id).getWorks();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
