package com.company.repository.dao.inanimate.subject;

import com.company.domain.inanimate.subject.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
