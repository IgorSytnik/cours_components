package com.company.domain.hei;

import com.company.exceptoins.EmptyListException;
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
@Getter
@Setter
@Table(name = "faculties", uniqueConstraints =
@UniqueConstraint(columnNames = {"name"})
)
@ToString(exclude = {"departments"})
public class Faculty extends Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<Department> departments = new ArrayList<>();

    public Faculty(String name) {
        this.name = name;
    }

    private boolean lookUp(final String obName) {
        return departments.stream().anyMatch(o -> o.getName().equals(obName));
    }

    public boolean addDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("department must not be null");
        }
        if(lookUp(department.getName())) {
            return false;
        } else {
            departments.add(department);
            return true;
        }
    }

    public boolean showDepartmentList() {
        return showList(departments, "departments");
    }

    public Department getDepartment(int i) throws EmptyListException {
        return (Department) getOne(departments, "departments", i);
    }

//    @Override
//    public String toString() {
//        return id + " " + name;
//    }

//    @Override
//        public int hashCode() {
//        return name.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Faculty) {
//            Faculty anobj = (Faculty)obj;
//            return this.name.equals(anobj.name);
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;
        Faculty faculty = (Faculty) o;
        return id == faculty.id &&
                name.equals(faculty.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
