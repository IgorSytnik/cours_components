package com.company.controllers;

import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.subject.*;
import com.company.domain.people.Student;
import com.company.domain.people.Teacher;
import com.company.services.interfaces.inanimate.GroupsSubjectsService;
import com.company.services.interfaces.inanimate.subject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class TeacherController {

    private final GradeDateService gradeDateService;
    private final GroupsSubjectsService groupsSubjectsService;
    private final GradeService gradeService;
    private final ListHasStudentsService listHasStudentsService;
    private final WorkService workService;

    /**
     * @param dateMap
     * Map: long groupsSubjectsId, Date gradeDate
     * @return
     */
    public Collection<GradeDate> makeGradeDates(Map<Long, List<Date>> dateMap) {
        List<GradeDate> gradeDateList = new LinkedList<>();
        dateMap.forEach((groupsSubjectsId, gradeDate) -> gradeDate.forEach(d ->
                        gradeDateList.add(new GradeDate(d, groupsSubjectsService.findById(groupsSubjectsId))))
        );
        return gradeDateService.makeMany(gradeDateList);
    }

    /**
     * @param workMap
     * Map: String name, Date term
     * @param groupsSubjectsId
     * @return
     */
    public Collection<Work> makeWorks(List<Map.Entry<String, Date>> workMap, long groupsSubjectsId) {
        GroupsSubjects groupsSubjects = groupsSubjectsService.findById(groupsSubjectsId);
        return workService.makeMany(workMap.stream()
                .collect(
                        LinkedList::new,
                        (list, entry) -> list.add(new Work(
                                entry.getKey(),
                                entry.getValue(),
                                groupsSubjects)),
                        LinkedList::addAll));
    }

    /**
     * @param mapAttest
     * Map: long listHasStudentsId, boolean true=first or false=second attest, boolean attest
     */
    @Transactional
    public void giveAttestations(Map<Long, Map.Entry<Boolean, Boolean>> mapAttest) {
        mapAttest.forEach((k,v) -> {
            Date dateNow = new Date(System.currentTimeMillis());
            ListHasStudents listHasStudents = listHasStudentsService.findById(k);
            if (v.getKey()) {
                Date beg = listHasStudents.getGroupsSubjects().getGroup().getDepartment().getFirstAttestationBeg();
                Date end = listHasStudents.getGroupsSubjects().getGroup().getDepartment().getFirstAttestationEnd();
                if (compareDates(dateNow, beg, end)) {
                    listHasStudentsService.addFirstAttest(k, v.getValue());
                } else {
                    throw new RuntimeException("It's not attestation period right now");
                }
            } else {
                Date beg = listHasStudents.getGroupsSubjects().getGroup().getDepartment().getSecondAttestationBeg();
                Date end = listHasStudents.getGroupsSubjects().getGroup().getDepartment().getSecondAttestationEnd();
                if (compareDates(dateNow, beg, end)) {
                    listHasStudentsService.addSecondAttest(k, v.getValue());
                } else {
                    throw new RuntimeException("It's not attestation period right now");
                }
            }
        });
    }

    private boolean compareDates(Date now, Date beg, Date end) {
        if (beg == null || end == null) {
            return false;
        } else {
            return beg.compareTo(now) * now.compareTo(end) >= 0;
        }
    }

    /**
     * @param mapGrades
     * Map: long listHasStudentsId, long gradeDateId, int grade
     * @return
     */
    public Collection<Grade> giveGrades(Map<Long, Map<Long, Integer>> mapGrades) {
        List<Grade> gradeList = new LinkedList<>();
        mapGrades.forEach((listHasStudentsId, v) -> v.forEach((gradeDateId, grade) ->
                gradeList.add(new Grade(grade,
                        gradeDateService.findById(gradeDateId),
                        listHasStudentsService.findById(listHasStudentsId))))
        );
        return gradeService.makeMany(gradeList);
    }

//    @Transactional
    public Collection<ListHasStudents> getStudentList(long groupsSubjectsId) {
        return groupsSubjectsService.findById(groupsSubjectsId).getListHasStudentsList();
    }

    public Collection<GradeDate> getGradeDatesList(long groupsSubjectsId) {
        return groupsSubjectsService.findById(groupsSubjectsId).getGradeDateList();
    }

    public List<GradeDate> getAllGradeDates() {
        return (List<GradeDate>) gradeDateService.getAll();
    }

    public List<Work> getAllWorks() {
        return (List<Work>) workService.getAll();
    }

    public List<Grade> getAllGrades() {
        return (List<Grade>) gradeService.getAll();
    }

    public void deleteAllGradeDates() {
        gradeDateService.deleteAll();
    }

    public void deleteAllWorks() {
        workService.deleteAll();
    }

    public void deleteAllGrades() {
        gradeService.deleteAll();
    }

    /*updaters*/
//    @Put
    public GradeDate updateGradeDate(GradeDate gradeDate) {
        return gradeDateService.update(gradeDate);
    }

    public Work updateWork(Work work) {
        return workService.update(work);
    }

    public Grade updateGrade(Grade grade) {
        return gradeService.update(grade);
    }

    /*getters*/

    public Optional<GradeDate> getGradeDate(long id) {
        return gradeDateService.get(id);
    }

    public Optional<Work> getWork(long id) {
        return workService.get(id);
    }

    public Optional<Grade> getGrade(long id) {
        return gradeService.get(id);
    }

    /*deleters*/

    public void deleteGradeDate(long id) {
        gradeDateService.delete(id);
    }

    public void deleteWork(long id) {
        workService.delete(id);
    }

    public void deleteGrade(long id) {
        gradeService.delete(id);
    }
}
