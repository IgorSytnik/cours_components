package com.company.controllers;

import com.company.domain.inanimate.StudentsHasWorks;
import com.company.domain.inanimate.subject.Work;
import com.company.services.interfaces.inanimate.GroupsSubjectsService;
import com.company.services.interfaces.inanimate.StudentsHasWorksService;
import com.company.services.interfaces.people.StudentService;
import com.company.services.interfaces.inanimate.subject.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class StudentController {

    private final StudentService studentService;
    private final WorkService workService;
    private final GroupsSubjectsService groupsSubjectsService;
    private final StudentsHasWorksService studentsHasWorksService;

    public List<Work> getAllWorks(long groupsSubjectsId) {
        return groupsSubjectsService.findById(groupsSubjectsId).getWorks();
    }

    public List<Work> getWorksToDoBySubject(long studentId, long groupsSubjectsId) {
        List<Work> workList = new LinkedList<>(groupsSubjectsService.findById(groupsSubjectsId).getWorks());
        workList.removeAll(getDoneWorks(studentId).stream()
            .map(studentsHasWorks -> studentsHasWorks.getPrimaryKey().getWork())
            .collect(Collectors.toList()));
        return workList;
    }

    public List<Work> getWorksToDo(long studentId) {
        return studentService.findById(studentId)
                .getGroup().getGroupsSubjects().stream()
                    .flatMap(groupsSubjects -> groupsSubjects.getWorks().stream())
                    .collect(Collectors.toList());
    }

//    @Transactional
    public List<StudentsHasWorks> getDoneWorks(long studentId) {
        return studentService.findById(studentId).getStudentsHasWorksList();
    }

    public List<StudentsHasWorks> getDoneWorksBySubject(long studentId, long groupsSubjectsId) {
        return studentService.findById(studentId).getStudentsHasWorksList().stream()
                .filter(w -> w.getPrimaryKey().getWork().getGroupsSubjects().getId() == groupsSubjectsId)
                .collect(Collectors.toList());
    }

    public StudentsHasWorks handOverWork(long studentId, long workId, String file) {
        return studentsHasWorksService.make(new StudentsHasWorks(
                new Date(System.currentTimeMillis()),
                file,
                studentService.findById(studentId),
                workService.findById(workId)));
    }

    public void deleteAllDoneWorks() {
        studentsHasWorksService.deleteAll();
    }
}