package com.company.controllers.hei;

import com.company.domain.people.*;
import com.company.repository.dao.people.*;
import com.company.services.interfaces.people.*;
import com.company.services.people.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DepartmentControllerTest {

//    GroupService groupService = Mockito.mock(GroupService.class);
//    StudentService studentService = Mockito.mock(StudentService.class);
//    SubjectService subjectService = Mockito.mock(SubjectService.class);
//    Student student = Mockito.mock(Student.class);
//
//    StudentRepository studentRepository =  Mockito.mock(StudentRepository.class);
//    SubjectRepository subjectRepository =  Mockito.mock(SubjectRepository.class);
//    GroupRepository groupRepository =  Mockito.mock(GroupRepository.class);
//    StudentService studentService1 = new StudentServiceImpl(studentRepository);
//    SubjectService subjectService1 = new SubjectServiceImpl(subjectRepository);


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void giveGrades() {
//        //GIVEN
//        Subject subject = new Subject("bruh", 12L);
//        Map minimap = Collections
//                .singletonMap("2021-03-16", 4);
//
//        SubjectAttest subjectAttest =  Mockito.mock(SubjectAttest.class);
//
//        HashMap<Subject, SubjectAttest> subjectMap = new HashMap<>();
//        subjectMap.put(subject, subjectAttest);
//        Map grades = Collections.singletonMap(student, minimap);
//
//        DepartmentController departmentController =
//                new DepartmentController(groupService, studentService1, subjectService1);
//
//        Mockito.when(studentRepository.update(student)).thenReturn(student);
//        Mockito.when(student.getSubjects()).thenReturn(subjectMap);
//        Mockito.when(subjectRepository.findById(subject.getId())).thenReturn(subject);
//
//        //WHEN
//        departmentController.giveGrades(grades, subject.getId());
//
//        //THEN
//        Mockito.verify(subjectRepository, Mockito.times(1))
//                .findById(subject.getId());
//        Mockito.verify(studentRepository, Mockito.times(1))
//                .update(Mockito.any());

    }

    @Test
    void giveAttestations() {
//        //GIVEN
//        Subject subject = new Subject("bruh", 12L);
//
//        SubjectAttest subjectAttest =  Mockito.mock(SubjectAttest.class);
//
//        HashMap<Subject, SubjectAttest> subjectMap = new HashMap<>();
//        subjectMap.put(subject, subjectAttest);
//        Map attestations = Collections.singletonMap(student, true);
//
//        DepartmentController departmentController =
//                new DepartmentController(groupService, studentService1, subjectService1);
//
//        Mockito.when(studentRepository.save(student)).thenReturn(student);
//        Mockito.when(student.getSubjects()).thenReturn(subjectMap);
//        Mockito.when(subjectRepository.findById(subject.getId())).thenReturn(subject);
//
//        //WHEN
//        departmentController.giveAttestations(attestations, subject.getId());
//
//        //THEN
//        Mockito.verify(subjectRepository, Mockito.times(1))
//                .findById(subject.getId());
//        Mockito.verify(studentRepository, Mockito.times(1))
//                .update(Mockito.any());
    }

    @Test
    void getGrates() {
//        //GIVEN
//        Subject subject = new Subject("bruh");
//        Group group = new Group("XX-00", 3);
//        Student student = new Student("man");
//        List students = Collections.singletonList(group);
//        Mockito.when(groupRepository.getStudentsFromGroup(group.getId())).thenReturn(groups);
//        Mockito.when(student.getSubjects()).thenReturn(subjectMap);
//        //WHEN
//
//        //THEN
    }

    @Test
    void getAttestations() {
    }
}