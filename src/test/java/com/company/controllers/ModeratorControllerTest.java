package com.company.controllers;

import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.services.interfaces.hei.DepartmentService;
import com.company.services.interfaces.hei.FacultyService;
import com.company.services.interfaces.inanimate.GroupService;
import com.company.services.interfaces.inanimate.GroupsSubjectsService;
import com.company.services.interfaces.inanimate.SpecialtyService;
import com.company.services.interfaces.inanimate.subject.ListHasStudentsService;
import com.company.services.interfaces.inanimate.subject.SubjectService;
import com.company.services.interfaces.people.StudentService;
import com.company.services.interfaces.people.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModeratorControllerTest {

    GroupService groupService = Mockito.mock(GroupService.class);
    StudentService studentService = Mockito.mock(StudentService.class);
    SubjectService subjectService = Mockito.mock(SubjectService.class);
    FacultyService facultyService = Mockito.mock(FacultyService.class);
    DepartmentService departmentService = Mockito.mock(DepartmentService.class);
    TeacherService teacherService = Mockito.mock(TeacherService.class);
    SpecialtyService specialtyService = Mockito.mock(SpecialtyService.class);
    GroupsSubjectsService groupsSubjectsService = Mockito.mock(GroupsSubjectsService.class);
    ListHasStudentsService listHasStudentsService = Mockito.mock(ListHasStudentsService.class);

    ModeratorController moderatorController = new ModeratorController(
            groupService,studentService,subjectService,facultyService,departmentService,teacherService,
            specialtyService,groupsSubjectsService,listHasStudentsService);


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void makeFaculty_makingAFaculty_Equals() {
//        /*GIVEN*/
//        final String name = "FHP";
//        final Faculty expected = new Faculty(name);
//        Mockito.doReturn(expected).when(facultyService).make(expected);
//
//        /*WHEN*/
//        final Faculty actual = moderatorController.makeFaculty(name);
//
//        /*THEN*/
//        assertEquals(expected, actual);
//    }

//    @Test
//    void getAllFaculties() {
//        /*GIVEN*/
//        final String name1 = "FHP";
//        final String name2 = "TEF";
//        final List<Faculty> expected = new ArrayList<Faculty>();
//        expected.add(new Faculty(name1));
//        expected.add(new Faculty(name2));
//        Mockito.doReturn(expected).when(facultyService).make(expected);
//
//        /*WHEN*/
//        final Faculty actual = moderatorController.makeFaculty(name);
//
//        /*THEN*/
//        assertEquals(expected, actual);
//    }

    @Test
    void deleteAllFaculties() {
    }

    @Test
    void makeSpecialty() {
    }

    @Test
    void makeSubject() {
    }

    @Test
    void makeDepartment() {
    }

    @Test
    void makeTeacher() {
    }

    @Test
    void makeGroup() {
    }

    @Test
    void makeGroupSubject() {
    }

    @Test
    void makeStudent() {
    }

    @Test
    void makeListHasStudents() {
    }

    @Test
    void getAllSpecialties() {
    }

    @Test
    void getAllSubjects() {
    }

    @Test
    void getAllDepartments() {
    }

    @Test
    void getAllTeachers() {
    }

    @Test
    void getAllGroups() {
    }

    @Test
    void getAllGroupsSubjects() {
    }

    @Test
    void getAllStudents() {
    }

    @Test
    void getAllListHasStudents() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void deleteAllSubjects() {
    }

    @Test
    void deleteAllSpecialties() {
    }

    @Test
    void deleteAllDepartments() {
    }

    @Test
    void deleteAllTeachers() {
    }

    @Test
    void deleteAllGroups() {
    }

    @Test
    void deleteAllStudents() {
    }

    @Test
    void deleteAllGroupsSubjects() {
    }

    @Test
    void deleteAllListHasStudents() {
    }
}