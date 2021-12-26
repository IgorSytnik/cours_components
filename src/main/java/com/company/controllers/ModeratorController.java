package com.company.controllers;

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
import com.company.services.interfaces.hei.DepartmentService;
import com.company.services.interfaces.hei.FacultyService;
import com.company.services.interfaces.inanimate.GroupService;
import com.company.services.interfaces.inanimate.GroupsSubjectsService;
import com.company.services.interfaces.inanimate.SpecialtyService;
import com.company.services.interfaces.inanimate.subject.ListHasStudentsService;
import com.company.services.interfaces.inanimate.subject.SubjectService;
import com.company.services.interfaces.people.StudentService;
import com.company.services.interfaces.people.TeacherService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Controller
public class ModeratorController {

    private final GroupService groupService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final SpecialtyService specialtyService;
    private final GroupsSubjectsService groupsSubjectsService;
    private final ListHasStudentsService listHasStudentsService;

    /*makers*/
    public Faculty makeFaculty(String name) {
        return facultyService.make(new Faculty(name));
    }

    @Transactional
    public Specialty makeSpecialty(String name) {
        return specialtyService.make(new Specialty(name));
    }

    public Subject makeSubject(String name) {
        return subjectService.make(new Subject(name));
    }

    public Department makeDepartment(String name, long facultyId) {
        return departmentService.make(new Department(name, facultyService.findById(facultyId)));
    }

    public Teacher makeTeacher(String name, AcademicPosition position, long departmentId) {
        return teacherService.make(new Teacher(name, position, departmentService.findById(departmentId)));
    }

    public Group makeGroup(String name, int year, long departmentId, long specialtyId) {
        return groupService.make(new Group(name, year,
                departmentService.findById(departmentId),
                specialtyService.findById(specialtyId)));
    }

    public GroupsSubjects makeGroupSubject(long groupId, long subjectId, long teacherId) {
        return groupsSubjectsService.make(new GroupsSubjects(
                groupService.findById(groupId),
                subjectService.findById(subjectId),
                teacherService.findById(teacherId)
        ));
    }

    public Student makeStudent(String name, long groupId) {
        return studentService.make(new Student( name,
                groupService.findById(groupId)
        ));
    }

    public ListHasStudents makeListHasStudents(long groupSubjId, long studentId) {

        GroupsSubjects groupsSubjects = groupsSubjectsService.findById(groupSubjId);
        Student student = studentService.findById(studentId);

        if (!groupsSubjects.getGroup().equals(student.getGroup()))
            throw new RuntimeException("Student's and subject's groups don't match");

        return listHasStudentsService.make(new ListHasStudents(
                groupsSubjects,
                student
        ));
    }

    /*get lists*/
    public List<Faculty> getAllFaculties() {
        return (List<Faculty>) facultyService.getAll();
    }

    public List<Specialty> getAllSpecialties() {
        return (List<Specialty>) specialtyService.getAll();
    }

    public List<Subject> getAllSubjects() {
        return (List<Subject>) subjectService.getAll();
    }

    public List<Department> getAllDepartments() {
        return (List<Department>) departmentService.getAll();
    }

    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) teacherService.getAll();
    }

    public List<Group> getAllGroups() {
        return (List<Group>) groupService.getAll();
    }

    public List<GroupsSubjects> getAllGroupsSubjects() {
        return (List<GroupsSubjects>) groupsSubjectsService.getAll();
    }

    public List<Student> getAllStudents() {
        return (List<Student>) studentService.getAll();
    }

    public List<ListHasStudents> getAllListHasStudents() {
        return (List<ListHasStudents>) listHasStudentsService.getAll();
    }

    /**
     * @param mapAttestDates
     * Map: long departmentId, boolean true=first or false=second attest, Date beginning, Date ending
     */
    @Transactional
    public void addAttestTerms(Map<Long, Map<Boolean, Map.Entry<Date, Date>>> mapAttestDates) {
        mapAttestDates.forEach((k,v) ->
                v.forEach((bool, entry) -> {
                    if (bool)
                        departmentService.addFirstAttestDates(k, entry.getKey(), entry.getValue());
                    else
                        departmentService.addSecondAttestDates(k, entry.getKey(), entry.getValue());
                })
        );
    }

    /*deleters*/

    public void deleteFaculty(long id) {
        facultyService.delete(id);
    }

    public void deleteSubject(long id) {
        subjectService.delete(id);
    }

    public void deleteSpecialty(long id) {
        specialtyService.delete(id);
    }

    public void deleteDepartment(long id) {
        departmentService.delete(id);
    }

    public void deleteTeacher(long id) {
        teacherService.delete(id);
    }

    public void deleteGroup(long id) {
        groupService.delete(id);
    }

    public void deleteStudent(long id) {
        studentService.delete(id);
    }

    public void deleteGroupsSubject(long id) {
        groupsSubjectsService.delete(id);
    }

    public void deleteListHasStudent(long id) {
        listHasStudentsService.delete(id);
    }

    /*delete all*/

    public void deleteAll() {
        listHasStudentsService.deleteAll();
        studentService.deleteAll();
        groupsSubjectsService.deleteAll();
        teacherService.deleteAll();
        groupService.deleteAll();
        subjectService.deleteAll();
        specialtyService.deleteAll();
        departmentService.deleteAll();
        facultyService.deleteAll();
    }

    public void deleteAllFaculties() {
        facultyService.deleteAll();
    }

    public void deleteAllSubjects() {
        subjectService.deleteAll();
    }

    public void deleteAllSpecialties() {
        specialtyService.deleteAll();
    }

    public void deleteAllDepartments() {
        departmentService.deleteAll();
    }

    public void deleteAllTeachers() {
        teacherService.deleteAll();
    }

    public void deleteAllGroups() {
        groupService.deleteAll();
    }

    public void deleteAllStudents() {
        studentService.deleteAll();
    }

    public void deleteAllGroupsSubjects() {
        groupsSubjectsService.deleteAll();
    }

    public void deleteAllListHasStudents() {
        listHasStudentsService.deleteAll();
    }

    /*updaters*/
//    @Put
    public Faculty updateFaculty(Faculty faculty) {
        return facultyService.update(faculty);
    }

    public Subject updateSubject(Subject subject) {
        return subjectService.update(subject);
    }

    public Specialty updateSpecialty(Specialty specialty) {
        return specialtyService.update(specialty);
    }

    public Department updateDepartment(Department department) {
        return departmentService.update(department);
    }

    public Teacher updateTeacher(Teacher teacher) {
        return teacherService.update(teacher);
    }

    public Group updateGroup(Group group) {
        return groupService.update(group);
    }

    public Student updateStudent(Student student) {
        Student student1 = studentService.update(student);
        // Also makes list for the student
        for (GroupsSubjects groupsSubjects : getAllGroupsSubjects()) {
            ListHasStudents listHasStudents = new ListHasStudents(groupsSubjects, student1);
            updateListHasStudents(listHasStudents);
        }
        return student1;
    }

    public GroupsSubjects updateGroupsSubjects(GroupsSubjects groupsSubjects) {
        GroupsSubjects groupsSubjects1 = groupsSubjectsService.update(groupsSubjects);
        // Also makes list for the groupsSubject
        for (Student student : getAllStudents()) {
            ListHasStudents listHasStudents = new ListHasStudents(groupsSubjects1, student);
            updateListHasStudents(listHasStudents);
        }
        return groupsSubjects1;
    }

    public ListHasStudents updateListHasStudents(ListHasStudents listHasStudents) {
        return listHasStudentsService.update(listHasStudents);
    }

    /*getters*/

    public Optional<Faculty> getFaculty(long id) {
        return facultyService.get(id);
    }

    public Optional<Subject> getSubject(long id) {
        return subjectService.get(id);
    }

    public Optional<Specialty> getSpecialty(long id) {
        return specialtyService.get(id);
    }

    public Optional<Department> getDepartment(long id) {
        return departmentService.get(id);
    }

    public Optional<Teacher> getTeacher(long id) {
        return teacherService.get(id);
    }

    public Optional<Group> getGroup(long id) {
        return groupService.get(id);
    }

    public Optional<Student> getStudent(long id) {
        return studentService.get(id);
    }

    public Optional<GroupsSubjects> getGroupsSubject(long id) {
        return groupsSubjectsService.get(id);
    }

    public Optional<ListHasStudents> getListHasStudent(long id) {
        return listHasStudentsService.get(id);
    }
}
