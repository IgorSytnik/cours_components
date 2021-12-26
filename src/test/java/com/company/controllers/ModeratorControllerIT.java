package com.company.controllers;

import com.company.context.TestHEIConfig;
import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.people.AcademicPosition;
import com.company.domain.people.Student;
import com.company.domain.people.Teacher;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ModeratorControllerIT {

    static AnnotationConfigApplicationContext context;
    static ModeratorController moderatorController;
    final String name1 = "Reto'sii-re kl.";
    final String name2 = "Uutda Em";
    final String name3 = "Uutda22 Em";
    final int year1 = 3;
    final int year2 = 1;
    final java.util.Date date1 = Date.valueOf(LocalDate.of(2021, Month.MAY, 26));
    final java.util.Date date2 = Date.valueOf(LocalDate.of(2021, Month.JUNE, 6));
    final java.util.Date date3 = Date.valueOf(LocalDate.of(2021, Month.JUNE, 7));
    final java.util.Date date4 = Date.valueOf(LocalDate.of(2021, Month.JUNE, 20));

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(TestHEIConfig.class);
        moderatorController = context.getBean(ModeratorController.class);
    }

    @AfterAll
    static void tearDown() {
    }

    @AfterEach
    void afterEach() {
//        moderatorController.deleteAll();
    }

    /*make*/
    @Test
    void makeFaculty_makingTwoFaculties_Equals() {
        /*GIVEN*/
        List<String> expected = new ArrayList<>();
        expected.add(name1);
        expected.add(name2);

        /*WHEN*/
        moderatorController.makeFaculty(name1);
        moderatorController.makeFaculty(name2);

        /*THEN*/
        List<Faculty> actual = new ArrayList<>(moderatorController.getAllFaculties());
        assertEquals(
                new HashSet<>(expected),
                actual.stream()
                        .map(Faculty::getName)
                        .collect(Collectors.toCollection(HashSet::new))
        );

        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeSpecialty_makingTwoSpecialties_Equals() {
        /*GIVEN*/
        List<String> expected = new ArrayList<>();
        expected.add(name1);
        expected.add(name2);

        /*WHEN*/
        moderatorController.makeSpecialty(name1);
        moderatorController.makeSpecialty(name2);

        /*THEN*/
        List<Specialty> actual = new ArrayList<>(moderatorController.getAllSpecialties());
        assertEquals(
                new HashSet<>(expected),
                actual.stream()
                        .map(Specialty::getName)
                        .collect(Collectors.toCollection(HashSet::new))
        );

        moderatorController.deleteAllSpecialties();
    }

    @Test
    void makeSubject_makingTwoSubjects_Equals() {
        /*GIVEN*/
        List<String> expected = new ArrayList<>();
        expected.add(name1);
        expected.add(name2);

        /*WHEN*/
        moderatorController.makeSubject(name1);
        moderatorController.makeSubject(name2);

        /*THEN*/
        List<Subject> actual = new ArrayList<>(moderatorController.getAllSubjects());
        assertEquals(
                new HashSet<>(expected),
                actual.stream()
                        .map(Subject::getName)
                        .collect(Collectors.toCollection(HashSet::new))
        );

        moderatorController.deleteAllSubjects();
    }

    @Test
    void makeDepartment_makingTwoDepartments_Equals() {
        /*GIVEN*/
        Faculty faculty = moderatorController.makeFaculty(name1);
        moderatorController.makeFaculty(name2);

        /*WHEN*/
        moderatorController.makeDepartment(name1, faculty.getId());
        moderatorController.makeDepartment(name2, faculty.getId());

        /*THEN*/
        List<Department> actual = new ArrayList<>(moderatorController.getAllDepartments());
        for (Department department: actual) {
            assertEquals(
                    faculty,
                    department.getFaculty());
        }

        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeTeacher_makingTwoTeachers_Equals() {
        /*GIVEN*/
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        moderatorController.makeDepartment(name2, faculty.getId());

        /*WHEN*/
        moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        moderatorController.makeTeacher(name2, AcademicPosition.ASPIRANT, department.getId());

        /*THEN*/
        List<Teacher> actual = new ArrayList<>(moderatorController.getAllTeachers());
        for (Teacher teacher: actual) {
            assertEquals(
                    department,
                    teacher.getDepartment());
            assertEquals(
                    department.getFaculty(),
                    teacher.getDepartment().getFaculty());
        }

        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeGroup_makingTwoGroups_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        moderatorController.makeDepartment(name2, faculty.getId());

        /*WHEN*/
        moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());

        /*THEN*/
        List<Group> actual = new ArrayList<>(moderatorController.getAllGroups());
        for (Group group: actual) {
            assertEquals(
                    specialty,
                    group.getSpecialty());
            assertEquals(
                    faculty,
                    group.getDepartment().getFaculty());
            assertEquals(
                    department,
                    group.getDepartment());
        }

        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeGroupSubject_makingTwoGroupSubjects_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year1, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        moderatorController.makeSubject(name2);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        moderatorController.makeTeacher(name2, AcademicPosition.ASPIRANT, department.getId());

        /*WHEN*/
        moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());
        moderatorController.makeGroupSubject(group2.getId(), subject.getId(), teacher.getId());

        /*THEN*/
        List<GroupsSubjects> actual = new ArrayList<>(moderatorController.getAllGroupsSubjects());
        for (GroupsSubjects groupsSubjects: actual) {
            assertEquals(
                    subject,
                    groupsSubjects.getSubject());
            assertEquals(
                    teacher,
                    groupsSubjects.getTeacher());
        }

        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeStudent_makingTwoStudents_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name3);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        moderatorController.makeGroup(name2, year1, department.getId(), specialty.getId());

        /*WHEN*/
        moderatorController.makeStudent(name1, group.getId());
        moderatorController.makeStudent(name2, group.getId());

        /*THEN*/
        List<Student> actual = new ArrayList<>(moderatorController.getAllStudents());
        for (Student student: actual) {
            assertEquals(
                    group,
                    student.getGroup());
        }

        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeListHasStudents_makingTwoListHasStudents_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name3);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year1, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());

        Student student1 = moderatorController.makeStudent(name1, group1.getId());
        Student student2 = moderatorController.makeStudent(name2, group2.getId());

        GroupsSubjects groupSubject1 = moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());
        GroupsSubjects groupSubject2 = moderatorController.makeGroupSubject(group2.getId(), subject.getId(), teacher.getId());

        /*WHEN*/
        ListHasStudents listHasStudents1 = moderatorController.makeListHasStudents(groupSubject1.getId(), student1.getId());
        ListHasStudents listHasStudents2 = moderatorController.makeListHasStudents(groupSubject2.getId(), student2.getId());

        /*THEN*/
        List<ListHasStudents> actual = new ArrayList<>(moderatorController.getAllListHasStudents());
        assertTrue(actual.contains(listHasStudents1));
        assertTrue(actual.contains(listHasStudents2));

        moderatorController.deleteAllListHasStudents();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    /*delete*/
    @Test
    void deleteAllFaculties__True() {
        /*GIVEN*/
        moderatorController.makeFaculty(name1);

        /*WHEN*/
        moderatorController.deleteAllFaculties();

        /*THEN*/
        List<Faculty> actual = new ArrayList<>(moderatorController.getAllFaculties());
        assertTrue(actual.isEmpty());
    }

    @Test
    void addAttestTerms_givingTwoAttestationTerms_Equals() {
        /*GIVEN*/
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());


        /*WHEN*/
        Map.Entry<java.util.Date, java.util.Date> mapEntry1 = new AbstractMap.SimpleEntry<>(date1, date2);
        Map.Entry<java.util.Date, java.util.Date> mapEntry2 = new AbstractMap.SimpleEntry<>(date3, date4);
        Map<Boolean, Map.Entry<java.util.Date, java.util.Date>> mapAttestDates = new HashMap<>();
        mapAttestDates.put(true, mapEntry1);
        mapAttestDates.put(false, mapEntry2);
        Map<Long, Map<Boolean, Map.Entry<java.util.Date, java.util.Date>>> mapAttestToDep = new HashMap<>();
        mapAttestToDep.put(department.getId(), mapAttestDates);
        moderatorController.addAttestTerms(mapAttestToDep);

        /*THEN*/
        List<Department> actual = new ArrayList<>(moderatorController.getAllDepartments());
        Optional<Department> optionalDepartment = actual.stream().findAny();
        assertTrue(optionalDepartment.isPresent());
        assertEquals(0, optionalDepartment.get().getFirstAttestationBeg().compareTo(date1));
        assertEquals(0, optionalDepartment.get().getFirstAttestationEnd().compareTo(date2));
        assertEquals(0, optionalDepartment.get().getSecondAttestationBeg().compareTo(date3));
        assertEquals(0, optionalDepartment.get().getSecondAttestationEnd().compareTo(date4));

        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }
}