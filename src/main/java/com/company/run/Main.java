package com.company.run;

import com.company.context.HEIConfig;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Faculty;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HEIConfig.class);
        final ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println("Active profile: " + environment.getProperty("spring.profiles.active"));

        final ModeratorController moderatorController = context.getBean(ModeratorController.class);

        System.out.println();
        moderatorController.deleteAllFaculties();

        moderatorController.makeFaculty("TEF1");
        moderatorController.makeFaculty("TEF2");
        moderatorController.makeFaculty("TEF3");

        for (Faculty faculty : moderatorController.getAllFaculties()) {
            System.out.println(faculty);
        }

//        System.out.println();
//
//        facultyController.makeDepartment("APEPS");
//
//        for (Department department :
//                departmentController.getAllDepartments()) {
//            System.out.println(faculty);
//        }

//        final DepartmentController departmentController = context.getBean(DepartmentController.class);
//        departmentController.makeGroup("TV-81", 3);
//        for (Group g :
//                departmentController.getAllGroups()) {
//            System.out.println(g.getName());
//        }
//        departmentController.makeStudent("Goga Pulli", );
//        departmentController.makeStudent("Nash si-Iperr");
//        for (Student s :
//             departmentController.getAllStudents()) {
//            System.out.println(s.getName());
//        }
//        StudentController studentController = context.getBean(StudentController.class);
//        TeacherController teacherController = context.getBean(TeacherController.class);

    }
}
