package com.company.repository.dao.inanimate.subject;

import com.company.domain.inanimate.subject.ListHasStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListHasStudentsRepository extends JpaRepository<ListHasStudents, Long> {
}
