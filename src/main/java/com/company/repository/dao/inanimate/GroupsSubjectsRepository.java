package com.company.repository.dao.inanimate;

import com.company.domain.inanimate.GroupsSubjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsSubjectsRepository extends JpaRepository<GroupsSubjects, Long> {
}
