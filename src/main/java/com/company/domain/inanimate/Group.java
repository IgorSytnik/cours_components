package com.company.domain.inanimate;

import com.company.domain.hei.Department;
import com.company.domain.people.Student;
import com.company.exceptoins.EmptyListException;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
//"groups" is a reserved keyword in mysql
@Table(name = "groups_table", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "year", "department_id"})
)
@ToString(exclude = {"department", "specialty", "students", "groupsSubjects"})
public class Group /*extends ClassWithName*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "year", nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "group"/*, fetch = FetchType.LAZY*/)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GroupsSubjects> groupsSubjects = new ArrayList<>();

    public Group(String name, int year, Department department, Specialty specialty) {
        this.name = name;
        this.year = year;
        this.department = department;
        this.specialty = specialty;
    }

    // TODO: 22.05.2021 shouldn't be here
    public Group(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public boolean addStudent(@NonNull Student student) {
//        Student student = new Student(studentName, name, subjects);
        if(students.contains(student)) {
            return false;
        } else {
            students.add(student);
            student.setGroup(this);
            return true;
        }
    }

    public Student getStudent(int i) throws EmptyListException {
        if (students.isEmpty()) {
            throw new EmptyListException(students.toString());
        } else if (i < 0 || i > students.size()) {
            throw new IndexOutOfBoundsException(i);
        }
        return students.get(i);
    }

//    @Override
//    public String toString() {
//        return name + ", year: " + year;
//    }

//    @Override
//    public int hashCode() {
//        return name.hashCode() + year + students.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Group) {
//            Group anobj = (Group)obj;
//            return this.year == anobj.year
//                    & this.name.equals(anobj.name)
//                    & this.students.equals(anobj.students);
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return id == group.id &&
                year == group.year &&
                name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year);
    }
}
