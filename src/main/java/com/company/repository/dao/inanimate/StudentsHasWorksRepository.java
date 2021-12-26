package com.company.repository.dao.inanimate;

import com.company.domain.inanimate.StudentsHasWorks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsHasWorksRepository extends JpaRepository<StudentsHasWorks, Long> {
}
