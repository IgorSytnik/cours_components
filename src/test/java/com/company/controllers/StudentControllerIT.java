package com.company.controllers;

import com.company.context.TestHEIConfig;
import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.StudentsHasWorks;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.inanimate.subject.Work;
import com.company.domain.people.AcademicPosition;
import com.company.domain.people.Student;
import com.company.domain.people.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerIT {

    static AnnotationConfigApplicationContext context;
    static ModeratorController moderatorController;
    static TeacherController teacherController;
    static StudentController studentController;
    final String name1 = "Reto'sii-re kl.";
    final String name2 = "Uutda Em";
    final int year1 = 3;
    final int year2 = 1;
    static java.util.Date date1;
    static java.util.Date date2;

    // TODO: 25.05.2021 Test fixtures 
    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(TestHEIConfig.class);
        moderatorController = context.getBean(ModeratorController.class);
        teacherController = context.getBean(TeacherController.class);
        studentController = context.getBean(StudentController.class);

        date1 = java.sql.Date.valueOf(LocalDate.of
                (2021, Month.JUNE, 7));
        date2 = java.sql.Date.valueOf(LocalDate.of
                (2021, Month.JUNE, 20));
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void getAllWorks() {
//        /*GIVEN*/
//        Specialty specialty = moderatorController.makeSpecialty(name2);
//        Faculty faculty = moderatorController.makeFaculty(name1);
//        Department department = moderatorController.makeDepartment(name1, faculty.getId());
//        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
//        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
//        Subject subject = moderatorController.makeSubject(name1);
//        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
//        GroupsSubjects groupsSubjects =
//                moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());
//
//        Student student = moderatorController.makeStudent(name1, group1.getId());
//
//        List<Map.Entry<String, Date>> list = new LinkedList<>();
//        list.add(Map.entry(name1, date1));
//        list.add(Map.entry(name2, date2));
//        List<Work> workList = (List<Work>) teacherController.makeWorks(list, groupsSubjects.getId());
//
//        /*WHEN*/
//        List<Work> actual =
//                new ArrayList<>(studentController.getAllWorks(groupsSubjects.getId()));
//
//        /*THEN*/
//        for (Work work: workList) {
//            assertTrue(actual.stream()
//                    .anyMatch(e -> e.getId() == work.getId()));
//        }
//
//        studentController.deleteAllDoneWorks();
//        teacherController.deleteAllWorks();
//        moderatorController.deleteAllGroupsSubjects();
//        moderatorController.deleteAllSubjects();
//        moderatorController.deleteAllStudents();
//        moderatorController.deleteAllGroups();
//        moderatorController.deleteAllSpecialties();
//        moderatorController.deleteAllTeachers();
//        moderatorController.deleteAllDepartments();
//        moderatorController.deleteAllFaculties();
//    }

    @Test
    void getWorksToDoBySubject() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects =
                moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());

        Student student = moderatorController.makeStudent(name1, group1.getId());

        List<Map.Entry<String, Date>> list = new LinkedList<>();
        list.add(Map.entry(name1, date1));
        list.add(Map.entry(name2, date2));
        List<Work> workList = (List<Work>) teacherController.makeWorks(list, groupsSubjects.getId());

        /*WHEN*/
        List<Work> actual =
                new ArrayList<>(studentController.getWorksToDoBySubject(student.getId(), groupsSubjects.getId()));

        /*THEN*/
        for (Work work: workList) {
            assertTrue(actual.stream()
                    .anyMatch(e -> e.getId() == work.getId()));
        }

        studentController.deleteAllDoneWorks();
        teacherController.deleteAllWorks();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void getWorksToDo() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects =
                moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());

        Student student = moderatorController.makeStudent(name1, group1.getId());

        List<Map.Entry<String, Date>> list = new LinkedList<>();
        list.add(Map.entry(name1, date1));
        list.add(Map.entry(name2, date2));
        List<Work> workList = (List<Work>) teacherController.makeWorks(list, groupsSubjects.getId());

        /*WHEN*/
        List<Work> actual = new ArrayList<>(studentController.getWorksToDo(student.getId()));

        /*THEN*/
        for (Work work: workList) {
            assertTrue(actual.stream()
                        .anyMatch(e -> e.getId() == work.getId()));
        }

        studentController.deleteAllDoneWorks();
        teacherController.deleteAllWorks();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void handOverWork() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects =
                moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());

        Student student = moderatorController.makeStudent(name1, group1.getId());

        List<Map.Entry<String, Date>> list = new LinkedList<>();
        list.add(Map.entry(name1, date1));
        list.add(Map.entry(name2, date2));
        List<Work> workList = (List<Work>) teacherController.makeWorks(list, groupsSubjects.getId());

        /*WHEN*/
        for (Work work : workList) {
            studentController.handOverWork(student.getId(), work.getId(), name2);
        }

        /*THEN*/
        List<StudentsHasWorks> actual = new ArrayList<>(studentController.getDoneWorks(student.getId()));
        for (StudentsHasWorks studentsHasWorks: actual) {
            assertEquals(
                    student,
                    studentsHasWorks.getPrimaryKey().getStudent());
            assertEquals(
                    name2,
                    studentsHasWorks.getWorkLink());
        }

        studentController.deleteAllDoneWorks();
        teacherController.deleteAllWorks();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void getDoneWorksBySubject() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects =
                moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());

        Student student = moderatorController.makeStudent(name1, group1.getId());

        List<Map.Entry<String, Date>> list = new LinkedList<>();
        list.add(Map.entry(name1, date1));
        list.add(Map.entry(name2, date2));
        List<Work> workList = (List<Work>) teacherController.makeWorks(list, groupsSubjects.getId());

        for (Work work : workList) {
            studentController.handOverWork(student.getId(), work.getId(), name2);
        }

        /*WHEN*/
        List<StudentsHasWorks> actual =
                new ArrayList<>(studentController.getDoneWorksBySubject(student.getId(), groupsSubjects.getId()));

        /*THEN*/
        for (StudentsHasWorks studentsHasWorks: actual) {
            assertEquals(
                    student,
                    studentsHasWorks.getPrimaryKey().getStudent());
            assertEquals(
                    name2,
                    studentsHasWorks.getWorkLink());
        }

        studentController.deleteAllDoneWorks();
        teacherController.deleteAllWorks();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

}