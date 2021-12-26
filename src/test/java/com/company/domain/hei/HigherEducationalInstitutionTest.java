package com.company.domain.hei;

import com.company.exceptoins.EmptyListException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HigherEducationalInstitutionTest {

    // for keyboard input
//    String name = "My string";
//    String expected = "My@@ string\n" +
//            "My string.\n" +
//            "My string\n" +
//            "My string\n" +
//            "n\n" +
//            "My string\n" +
//            "y\n";
//    InputStream sysInBackup = System.in; // backup System.in to restore it later
//    ByteArrayInputStream in = new ByteArrayInputStream(expected.getBytes());

    // helping objects
    Faculty faculty = new Faculty("HPF");
    HigherEducationalInstitution obj = new HigherEducationalInstitution("Tim Shaq");

    @Test
    void addFaculty_AddOne_True() {
        assertTrue(obj.addFaculty(faculty));
    }

    @Test
    void addFaculty_AddTwoEqualOnes_False() {
        obj.addFaculty(faculty);
        assertFalse(obj.addFaculty(faculty));
    }

    @Test
    void getFaculty_EmptyList_EmptyListException() {
        assertThrows(EmptyListException.class, ()->obj.getFaculty(0));
    }

    @Test
    void getFaculty_WrongNumber_IndexOutOfBoundsException() {
        obj.addFaculty(faculty);
        assertThrows(IndexOutOfBoundsException.class, ()->obj.getFaculty(4));
    }

    @Test
    void getFaculty_GetFacultyFromList_NotNull() throws EmptyListException {
        obj.addFaculty(faculty);
        assertNotNull(obj.getFaculty(0));
    }

    @Test
    void getFacList_GetEmptyList_False() {
        assertFalse(obj.showFacList());
    }

    @Test
    void getFacList_GetNotEmptyList_True() {
        obj.addFaculty(faculty);
        assertTrue(obj.showFacList());
    }

}