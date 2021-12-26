package com.company.repository.dao.hei;

import com.company.domain.hei.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
//    @Override
//    public Department findById(long id) {
//        return null;
//    }
//
//    @Override
//    public List<Department> findAll() {
//        return null;
//    }
//
//    @Override
//    public Department save(Department department) {
//        return null;
//    }
//
//    @Override
//    public Department update(Department department) {
//        return null;
//    }
//
//    @Override
//    public Department delete(Department department) {
//        return null;
//    }
}
