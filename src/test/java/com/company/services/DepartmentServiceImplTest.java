package com.company.services;

class DepartmentServiceImplTest {


//    /*  lambdas */
//
//    /**
//     * findGroupsByYear*/
//    @Test
//    void findGroupsByYear_FindByTwoDifferentYears_Equals() {
//        assertEquals(4, Lambda1.findGroupsByYear(4).size());
//        assertEquals(3, Lambda1.findGroupsByYear(3).size());
//    }
//
//    @Test
//    void findGroupsByYear_FindNonExistingGroups_Equals() {
//        assertEquals(0, Lambda1.findGroupsByYear(1).size());
//    }
//
//    @Test
//    void findGroupsByYear_SearchesInTheEmptyList_Equals() {
//        assertEquals(0, Lambda2.findGroupsByYear(4).size());
//    }
//
//    /**
//     * countStudentsByYear*/
//
//    @Test
//    void countStudentsByYear_CountByTwoDifferentYears_Equals() {
//        assertEquals(4, Lambda1.countStudentsByYear(4));
//        assertEquals(3, Lambda1.countStudentsByYear(3));
//    }
//
//    @Test
//    void countStudentsByYear_CountByNonExistingGroups_Equals() {
//        assertEquals(0, Lambda1.countStudentsByYear(1));
//    }
//
//    @Test
//    void countStudentsByYear_CountInAnEmptyList_Equals(){
//        assertEquals(0, Lambda2.countStudentsByYear(4));
//    }
//
//    /**
//     * maxStudentsInGroup*/
//
//    @Test
//    void maxStudentsInGroup_FindMaxInAListWithGroups_Equals() throws EmptyListException {
//        assertEquals(gTest3, Lambda1.maxStudentsInGroup());
//    }
//
//    @Test
//    void maxStudentsInGroup_FindMaxInAnEmptyList_Equals() {
//        assertThrows(EmptyListException.class, Lambda2::maxStudentsInGroup);
//    }
//
//    /**
//     * avgNumberOfStudents*/
//
//    @Test
//    void avgNumberOfStudents_FindAvgInAListWithGroups_Equals() throws EmptyListException {
//        assertEquals(1.0, Lambda1.avgNumberOfStudents());
//    }
//
//    @Test
//    void avgNumberOfStudents_FindAvgInAnEmptyList_Equals() {
//        assertThrows(EmptyListException.class, Lambda2::avgNumberOfStudents);
//    }
//
//    /**
//     * splitGroupsByYear*/
//
//    @Test
//    void splitGroupsByYear_SplitByTwoExistingYears_Equals() {
//        assertEquals(4, Lambda1.splitGroupsByYear(4).get(true).size());
//        assertEquals(3, Lambda1.splitGroupsByYear(4).get(false).size());
//    }
//
//    @Test
//    void splitGroupsByYear_SplitByNonExistingYear_Equals() {
//        assertEquals(0, Lambda1.splitGroupsByYear(1).get(true).size());
//    }
//
//    @Test
//    void splitGroupsByYear_SplitAnEmptyList_Equals() {
//        assertEquals(0, Lambda2.splitGroupsByYear(4).get(true).size());
//        assertEquals(0, Lambda2.splitGroupsByYear(4).get(false).size());
//    }
//
//    /**
//     * findTeachersByPosition*/
//
//    @Test
//    void findTeachersByPosition_FindByTwoDifferentPositions_Equals() {
//        assertEquals(4, Lambda1.findTeachersByPosition(AcademicPosition.ASPIRANT).size());
//        assertEquals(3, Lambda1.findTeachersByPosition(AcademicPosition.DOCTORAL_STUDENT).size());
//    }
//
//    @Test
//    void findTeachersByPosition_FindNonExistingGroups_Equals() {
//        assertEquals(0, Lambda1.findTeachersByPosition(AcademicPosition.LECTURER).size());
//    }
//
//    @Test
//    void findTeachersByPosition_SearchesInTheEmptyList_Equals() {
//        assertEquals(0, Lambda2.findTeachersByPosition(AcademicPosition.ASPIRANT).size());
//    }
//
//    /**
//     * splitTeachersByPosition*/
//
//    @Test
//    void splitTeachersByPosition_SplitByTwoExistingPositions_Equals() {
//        assertEquals(4, Lambda1.splitTeachersByPosition(AcademicPosition.ASPIRANT).get(true).size());
//        assertEquals(5, Lambda1.splitTeachersByPosition(AcademicPosition.ASPIRANT).get(false).size());
//    }
//
//    @Test
//    void splitTeachersByPosition_SplitByNonExistingPosition_Equals() {
//        assertEquals(0, Lambda1.splitTeachersByPosition(AcademicPosition.LECTURER).get(true).size());
//    }
//
//    @Test
//    void splitTeachersByPosition_SplitAnEmptyList_Equals() {
//        assertEquals(0, Lambda2.splitTeachersByPosition(AcademicPosition.ASPIRANT).get(true).size());
//        assertEquals(0, Lambda2.splitTeachersByPosition(AcademicPosition.ASPIRANT).get(false).size());
//    }
}