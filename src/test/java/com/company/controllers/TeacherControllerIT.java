package com.company.controllers;

import com.company.context.TestHEIConfig;
import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.subject.*;
import com.company.domain.people.AcademicPosition;
import com.company.domain.people.Student;
import com.company.domain.people.Teacher;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherControllerIT {

    static AnnotationConfigApplicationContext context;
    static ModeratorController moderatorController;
    static TeacherController teacherController;
    final String name1 = "Reto'sii-re kl.";
    final String name2 = "Uutda Em";
    final int year1 = 3;
    final int year2 = 1;
    final int[] grade1 = new int[] {3,5};
    static java.util.Date date1;
    static java.util.Date date2;
    static java.util.Date date3;
    static java.util.Date date4;

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(TestHEIConfig.class);
        moderatorController = context.getBean(ModeratorController.class);
        teacherController = context.getBean(TeacherController.class);

        final LocalDate dateNow = LocalDate.now();
        final LocalDate dateYesterday = dateNow.plus(-1, ChronoUnit.DAYS);
        final LocalDate dateTomorrow = dateNow.plus(1, ChronoUnit.DAYS);
        date1 = java.sql.Date.valueOf(LocalDate.of
                (dateYesterday.getYear(), dateYesterday.getMonth(), dateYesterday.getDayOfMonth()));
        date2 = java.sql.Date.valueOf(LocalDate.of
                (dateTomorrow.getYear(), dateTomorrow.getMonth(), dateTomorrow.getDayOfMonth()));
        date3 = java.sql.Date.valueOf(LocalDate.of
                (2021, Month.JUNE, 7));
        date4 = java.sql.Date.valueOf(LocalDate.of
                (2021, Month.JUNE, 20));
    }

    @AfterAll
    static void tearDown() {
    }

    @AfterEach
    void afterEach() {
//        moderatorController.deleteAll();
    }

    @Test
    void makeGradeDates_makingTwoGradeDates_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects = moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());
        moderatorController.makeGroupSubject(group2.getId(), subject.getId(), teacher.getId());

        /*WHEN*/
        List<java.util.Date> list = new LinkedList<>();
        list.add(date1);
        list.add(date2);
        Map<Long, List<java.util.Date>> dateMap = new HashMap<>();
        dateMap.put(groupsSubjects.getId(), list);
        teacherController.makeGradeDates(dateMap);

        /*THEN*/
        List<GradeDate> actual = new ArrayList<>(teacherController.getAllGradeDates());
        for (GradeDate gradeDate: actual) {
            assertEquals(
                    groupsSubjects,
                    gradeDate.getGroupsSubjects());
        }

        teacherController.deleteAllGradeDates();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void makeWorks_makingTwoWorks_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group1 = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Group group2 = moderatorController.makeGroup(name2, year2, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects = moderatorController.makeGroupSubject(group1.getId(), subject.getId(), teacher.getId());
        moderatorController.makeGroupSubject(group2.getId(), subject.getId(), teacher.getId());

        /*WHEN*/
        List<Map.Entry<String, java.util.Date>> list = new LinkedList<>();
        list.add(Map.entry(name1, date1));
        list.add(Map.entry(name2, date2));
        teacherController.makeWorks(list, groupsSubjects.getId());

        /*THEN*/
        List<Work> actual = new ArrayList<>(teacherController.getAllWorks());
        for (Work work: actual) {
            assertEquals(
                    groupsSubjects,
                    work.getGroupsSubjects());
        }

        teacherController.deleteAllWorks();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void giveAttestations_givingFirstAttestations_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());

        Student student1 = moderatorController.makeStudent(name1, group.getId());
        Student student2 = moderatorController.makeStudent(name2, group.getId());

        GroupsSubjects groupSubject = moderatorController.makeGroupSubject(group.getId(), subject.getId(), teacher.getId());

        ListHasStudents listHasStudents1 = moderatorController.makeListHasStudents(groupSubject.getId(), student1.getId());
        ListHasStudents listHasStudents2 = moderatorController.makeListHasStudents(groupSubject.getId(), student2.getId());

        Map.Entry<java.util.Date, java.util.Date> mapEntry1 = new AbstractMap.SimpleEntry<>(date1, date2);
        Map<Boolean, Map.Entry<java.util.Date, java.util.Date>> mapAttestDates = new HashMap<>();
        mapAttestDates.put(true, mapEntry1);
        Map<Long, Map<Boolean, Map.Entry<java.util.Date, java.util.Date>>> mapAttestToDep = new HashMap<>();
        mapAttestToDep.put(department.getId(), mapAttestDates);
        moderatorController.addAttestTerms(mapAttestToDep);

        /*WHEN*/
        Map.Entry<Boolean, Boolean> mapEntryTeacher1 = new AbstractMap.SimpleEntry<>(true, false);
        Map.Entry<Boolean, Boolean> mapEntryTeacher2 = new AbstractMap.SimpleEntry<>(true, true);
        Map<Long, Map.Entry<Boolean, Boolean>> mapAttest = new HashMap<>();
        mapAttest.put(listHasStudents1.getId(), mapEntryTeacher1);
        mapAttest.put(listHasStudents2.getId(), mapEntryTeacher2);
        teacherController.giveAttestations(mapAttest);

        /*THEN*/
        List<ListHasStudents> actual = new ArrayList<>(teacherController.getStudentList(groupSubject.getId()));
        Optional<ListHasStudents> optionalListHasStudents1 = actual.stream()
                .filter(e -> e.getId() == listHasStudents1.getId()).findAny();
        Optional<ListHasStudents> optionalListHasStudents2 = actual.stream()
                .filter(e -> e.getId() == listHasStudents2.getId()).findAny();
        assertTrue(optionalListHasStudents1.isPresent());
        assertTrue(optionalListHasStudents2.isPresent());
        assertFalse(optionalListHasStudents1.get().getAttest1());
        assertTrue(optionalListHasStudents2.get().getAttest1());

        moderatorController.deleteAllListHasStudents();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void giveAttestations_givingSecondAttestations_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());

        Student student1 = moderatorController.makeStudent(name1, group.getId());
        Student student2 = moderatorController.makeStudent(name2, group.getId());

        GroupsSubjects groupSubject = moderatorController.makeGroupSubject(group.getId(), subject.getId(), teacher.getId());

        ListHasStudents listHasStudents1 = moderatorController.makeListHasStudents(groupSubject.getId(), student1.getId());
        ListHasStudents listHasStudents2 = moderatorController.makeListHasStudents(groupSubject.getId(), student2.getId());

        Map.Entry<java.util.Date, java.util.Date> mapEntry2 = new AbstractMap.SimpleEntry<>(date1, date2);
        Map<Boolean, Map.Entry<java.util.Date, java.util.Date>> mapAttestDates = new HashMap<>();
        mapAttestDates.put(false, mapEntry2);
        Map<Long, Map<Boolean, Map.Entry<java.util.Date, java.util.Date>>> mapAttestToDep = new HashMap<>();
        mapAttestToDep.put(department.getId(), mapAttestDates);
        moderatorController.addAttestTerms(mapAttestToDep);

        /*WHEN*/
        Map.Entry<Boolean, Boolean> mapEntryTeacher1 = new AbstractMap.SimpleEntry<>(false, false);
        Map.Entry<Boolean, Boolean> mapEntryTeacher2 = new AbstractMap.SimpleEntry<>(false, true);
        Map<Long, Map.Entry<Boolean, Boolean>> mapAttest = new HashMap<>();
        mapAttest.put(listHasStudents1.getId(), mapEntryTeacher1);
        mapAttest.put(listHasStudents2.getId(), mapEntryTeacher2);
        teacherController.giveAttestations(mapAttest);

        /*THEN*/
        List<ListHasStudents> actual = new ArrayList<>(teacherController.getStudentList(groupSubject.getId()));
        Optional<ListHasStudents> optionalListHasStudents1 = actual.stream()
                .filter(e -> e.getId() == listHasStudents1.getId()).findAny();
        Optional<ListHasStudents> optionalListHasStudents2 = actual.stream()
                .filter(e -> e.getId() == listHasStudents2.getId()).findAny();
        assertTrue(optionalListHasStudents1.isPresent());
        assertTrue(optionalListHasStudents2.isPresent());
        assertFalse(optionalListHasStudents1.get().getAttest2());
        assertTrue(optionalListHasStudents2.get().getAttest2());

        moderatorController.deleteAllListHasStudents();
        moderatorController.deleteAllStudents();
        moderatorController.deleteAllGroupsSubjects();
        moderatorController.deleteAllSubjects();
        moderatorController.deleteAllGroups();
        moderatorController.deleteAllSpecialties();
        moderatorController.deleteAllTeachers();
        moderatorController.deleteAllDepartments();
        moderatorController.deleteAllFaculties();
    }

    @Test
    void giveGrades_makingTwoGradeDates_Equals() {
        /*GIVEN*/
        Specialty specialty = moderatorController.makeSpecialty(name2);
        Faculty faculty = moderatorController.makeFaculty(name1);
        Department department = moderatorController.makeDepartment(name1, faculty.getId());
        Group group = moderatorController.makeGroup(name1, year1, department.getId(), specialty.getId());
        Subject subject = moderatorController.makeSubject(name1);
        Teacher teacher = moderatorController.makeTeacher(name1, AcademicPosition.ASPIRANT, department.getId());
        GroupsSubjects groupsSubjects = moderatorController.makeGroupSubject(group.getId(), subject.getId(), teacher.getId());
        Student student = moderatorController.makeStudent(name1, group.getId());
        ListHasStudents listHasStudents = moderatorController.makeListHasStudents(groupsSubjects.getId(), student.getId());

        List<java.util.Date> list = new LinkedList<>();
        list.add(date1);
        list.add(date2);
        Map<Long, List<java.util.Date>> dateMap = new HashMap<>();
        dateMap.put(groupsSubjects.getId(), list);
        List<GradeDate> gradeDateList = (List<GradeDate>) teacherController.makeGradeDates(dateMap);

        /*WHEN*/
        Map<Long, Integer> mapGrades = new HashMap<>();
        mapGrades.put(gradeDateList.get(0).getId(), grade1[0]);
        mapGrades.put(gradeDateList.get(1).getId(), grade1[1]);
        Map<Long, Map<Long, Integer>> mapMap = new HashMap<>();
        mapMap.put(listHasStudents.getId(), mapGrades);
        teacherController.giveGrades(mapMap);

        /*THEN*/
        List<Grade> actual = new ArrayList<>(teacherController.getAllGrades());
        Optional<Grade> optionalListHasStudents1 = actual.stream()
                .filter(e -> e.getGradeDate().getId() == gradeDateList.get(0).getId()).findAny();
        Optional<Grade> optionalListHasStudents2 = actual.stream()
                .filter(e -> e.getGradeDate().getId() == gradeDateList.get(1).getId()).findAny();
        assertTrue(optionalListHasStudents1.isPresent());
        assertTrue(optionalListHasStudents2.isPresent());
        assertEquals(grade1[0], optionalListHasStudents1.get().getGrade());
        assertEquals(grade1[1], optionalListHasStudents2.get().getGrade());

        teacherController.deleteAllGrades();
        teacherController.deleteAllGradeDates();
        moderatorController.deleteAllListHasStudents();
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
    void getStudentList() {
    }

    @Test
    void getGradeDatesList() {
    }
}