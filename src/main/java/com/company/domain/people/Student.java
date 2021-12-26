package com.company.domain.people;

import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.StudentsHasWorks;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "students")
@ToString(exclude = {"group", "StudentsHasWorksList", "listHasStudentsList"})
public class Student /*extends ClassWithName*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "primaryKey.student", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<StudentsHasWorks> StudentsHasWorksList;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ListHasStudents> listHasStudentsList;

    public Student(String name, Group group, List<Subject> subjectList) {
        this.name = name;
        this.group = group;
    }
    public Student(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    /*public Student(String name, String groupName, List<Subject> subjectList) {
            this.name = name;
            this.groupName = groupName;
    //        for (Subject subject:
    //                subjectList) {
    //            subjects.put(subject, new SubjectAttest());
    //        }
        }*/
    public Student(String name) {
        this.name = name;
    }

//    public boolean handOverWork(Work work, String file) {
//        StudentsHasWorks studentsHasWorks = new StudentsHasWorks();
//        studentsHasWorks.setDeliveryDate(new Date(System.currentTimeMillis()));
//        studentsHasWorks.setWorkLink(file);
//        studentsHasWorks.getPrimaryKey().setStudent(this);
//        studentsHasWorks.getPrimaryKey().setWork(work);
//        return StudentsHasWorksList.add(studentsHasWorks);
//    }

//    @Override
//    public String toString() {
//        return name + ", group: " + group;
//    }
//
//    @Override
//    public int hashCode() {
//        return name.hashCode() + group.hashCode() + subjects.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Student) {
//            Student anobj = (Student)obj;
//            return this.name.equals(anobj.name)
//                    & this.group.equals(anobj.group)
//                    & this.subjects.equals(anobj.subjects);
//        }
//        return false;
//    }
//    @Override
//    public String toString() {
//        return name + ", group: " + group;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id &&
                name.equals(student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    //    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Student) {
//            Student anobj = (Student)obj;
//            return this.name.equals(anobj.name)
//                    & this.group.equals(anobj.group)
////                    & this.subjects.equals(anobj.subjects)
//                    ;
//        }
//        return false;
//    }
}
