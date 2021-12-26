package com.company.domain.hei;

import com.company.exceptoins.AttestationException;
import com.company.exceptoins.EmptyListException;
import com.company.domain.people.AcademicPosition;
import com.company.domain.inanimate.Group;
import com.company.domain.people.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Entity
@Getter
@ToString(exclude = {"faculty", "groups", "teachers"})
@Table(name = "departments", uniqueConstraints =
@UniqueConstraint(columnNames = {"name"})
)
@Setter
public class Department extends Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @ManyToOne(/*fetch = FetchType.EAGER*/)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "attestation_first_date_beg")
    @Temporal(TemporalType.DATE)
    private Date FirstAttestationBeg;

    @Column(name = "attestation_first_date_end")
    @Temporal(TemporalType.DATE)
    private Date FirstAttestationEnd;

    @Column(name = "attestation_second_date_beg")
    @Temporal(TemporalType.DATE)
    private Date SecondAttestationBeg;

    @Column(name = "attestation_second_date_end")
    @Temporal(TemporalType.DATE)
    private Date SecondAttestationEnd;

    @Transient
//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private final List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private final List<Teacher> teachers = new ArrayList<>();

    public Department(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
    }

    private boolean lookUp(final String obName) {
        return groups.stream().anyMatch(o -> o.getName().equals(obName));
    }

    public boolean addGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("teacher must not be null");
        }
        if(lookUp(group.getName())) {
            return false;
        } else {
            groups.add(group);
            return true;
        }
    }

    public boolean addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("teacher must not be null");
        }
        if(teachers.contains(teacher)) {
            return false;
        } else {
            teachers.add(teacher);
            return true;
        }
    }

    /** group list lambdas */
    public List<Group> findGroupsByYear(int year) {
        return groups.stream()
                .filter(g -> g.getYear() == year)
                .collect(Collectors.toList());
    }

    public int countStudentsByYear(int year) {
        return groups.stream()
                .filter(g -> g.getYear() == year)
                .mapToInt(g -> g.getStudents().size())
                .sum();
    }

    public Group maxStudentsInGroup() throws EmptyListException {
        return groups.stream()
                .max(Comparator.comparing(g -> g.getStudents().size()))
                .orElseThrow(() ->
                        new EmptyListException("The List of groups is empty"));
    }

    public double avgNumberOfStudents() throws EmptyListException {
        return groups.stream()
                .mapToInt(g -> g.getStudents().size())
                .average()
                .orElseThrow(() ->
                        new EmptyListException("The List of groups is empty"));
    }

    public Map<Boolean, List<Group>> splitGroupsByYear(int year) {
        return groups.stream().
                collect(Collectors.partitioningBy(g -> g.getYear() == year));
    }

    /*teacher list lambdas*/
    public List<Teacher> findTeachersByPosition(AcademicPosition position) {
        return teachers.stream()
                .filter(t -> t.getPosition().equals(position))
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Teacher>> splitTeachersByPosition(AcademicPosition position) {
        return teachers.stream().
                collect(Collectors.partitioningBy(t -> t.getPosition().equals(position)));
    }

//    /**
//     * @return copy of teacher list
//     * */
//    public List<Teacher> getTeachersList() {
//        return List.copyOf(teachers);
//    }
//
//    /**
//     * @return copy of group list
//     * */
//    public List<Group> getGroupsList() {
//        return List.copyOf(groups);
//    }

//    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this.hashCode() != obj.hashCode()) {
//            return false;
//        }
//        if (obj instanceof Department) {
//            Department anobj = (Department)obj;
//            return this.name.equals(anobj.name);
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return id == that.id &&
                name.equals(that.name) &&
                Objects.equals(FirstAttestationBeg, that.FirstAttestationBeg) &&
                Objects.equals(FirstAttestationEnd, that.FirstAttestationEnd) &&
                Objects.equals(SecondAttestationBeg, that.SecondAttestationBeg) &&
                Objects.equals(SecondAttestationEnd, that.SecondAttestationEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, FirstAttestationBeg, FirstAttestationEnd,
                SecondAttestationBeg, SecondAttestationEnd);
    }
}

