package com.company.repository.dao.people;

import com.company.domain.people.Student;
import com.company.domain.people.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
