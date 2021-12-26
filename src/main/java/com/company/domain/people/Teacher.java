package com.company.domain.people;

import com.company.domain.hei.Department;
import com.company.domain.inanimate.GroupsSubjects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Table(name = "teachers")
@Getter
@Setter
@ToString(exclude = {"department", "groupsSubjects"})
public class Teacher /*extends ClassWithName*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "position", nullable = false)
    private AcademicPosition position;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<GroupsSubjects> groupsSubjects = new ArrayList<>();

    public Teacher(String name, AcademicPosition position, Department department) {
        this.name = name;
        this.position = position;
        this.department = department;
    }

//    /**
//     * Example:
//     * David Newberg, position: Aspirant */
//    @Override
//    public String toString() {
//        return name + ", position: " + position;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                name.equals(teacher.name) &&
                position == teacher.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    //    @Override
//    public int hashCode() {
//        return name.hashCode() + position.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Teacher) {
//            Teacher anobj = (Teacher)obj;
//            return this.name.equals(anobj.name)
//                    & this.position.equals(anobj.position);
//        }
//        return false;
//    }
}
