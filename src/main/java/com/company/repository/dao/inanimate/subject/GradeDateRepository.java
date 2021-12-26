package com.company.repository.dao.inanimate.subject;

import com.company.domain.inanimate.subject.GradeDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeDateRepository extends JpaRepository<GradeDate, Long> {
}
