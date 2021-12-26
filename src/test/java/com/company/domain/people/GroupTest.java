package com.company.domain.people;

import com.company.domain.inanimate.Group;
import com.company.exceptoins.EmptyListException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    int year = 3;
    String studentName = "Lan'Koma";
    Student student = new Student(studentName);
    Group obj = new Group("HH-11", year);
//    Student student = new Student(studentName, obj, obj.getSubjects());

    @Test
    void addStudent_AddOne_True() {
        assertTrue(obj.addStudent(student));
    }

    @Test
    void addStudent_AddTwoEqualOnes_False() {
        obj.addStudent(student);
        assertFalse(obj.addStudent(student));
    }

    @Test
    void getStudent_EmptyList_Null() {
        assertThrows(EmptyListException.class, ()->obj.getStudent(0));
    }

    @Test
    void getStudent_WrongNumber_Null() {
        obj.addStudent(student);
        assertThrows(IndexOutOfBoundsException.class, ()->obj.getStudent(4));
    }

    @Test
    void getStudent_GetStudentFromList_NotNull() throws EmptyListException {
        obj.addStudent(student);
        assertNotNull(obj.getStudent(0));
    }

//    @Test
//    void showStudentsList_GetEmptyList_False() {
//        assertFalse(obj.showStudentsList());
//    }
//
//    @Test
//    void showStudentsList_GetNotEmptyList_True() {
//        obj.addStudent(studentName);
//        assertTrue(obj.showStudentsList());
//    }
}