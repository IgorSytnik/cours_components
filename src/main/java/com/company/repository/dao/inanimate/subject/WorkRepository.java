package com.company.repository.dao.inanimate.subject;

import com.company.domain.inanimate.subject.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
}
