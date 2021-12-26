package com.company.domain.inanimate.subject;

import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.people.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "list_has_students", uniqueConstraints =
    @UniqueConstraint(columnNames = {"groups_subjects_Id", "students_id"})
)
@ToString(exclude = {"groupsSubjects", "student", "grades"})
public class ListHasStudents {

//    @EmbeddedId
//    private ListHasStudentsId primaryKey;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(/*fetch = FetchType.EAGER*/)
    @JoinColumn(name = "groups_subjects_Id", nullable = false)
    private GroupsSubjects groupsSubjects;

    @ManyToOne(/*fetch = FetchType.EAGER*/)
    @JoinColumn(name = "students_id", nullable = false)
    private Student student;

    @Column(name = "attestation_first", columnDefinition = "TINYINT(1)")
    private Boolean attest1;

    @Column(name = "attestation_second", columnDefinition = "TINYINT(1)")
    private Boolean attest2;

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "listHasStudents", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Grade> grades;

    public ListHasStudents(GroupsSubjects groupsSubjects, Student student) {
        this.groupsSubjects = groupsSubjects;
        this.student = student;
    }

//    public void addAttest(boolean attest) {
//        Date dateNow = new Date(System.currentTimeMillis());
//
//        Date firstB = getGroupsSubjects().getGroup().getDepartment().getFirstAttestationBeg();
//        Date firstE = getGroupsSubjects().getGroup().getDepartment().getFirstAttestationEnd();
//        Date secondB = getGroupsSubjects().getGroup().getDepartment().getSecondAttestationBeg();
//        Date secondE = getGroupsSubjects().getGroup().getDepartment().getSecondAttestationEnd();
//
//
//        if (attest1 == null && compareDates(dateNow, firstB, firstE)) {
//            attest1 = attest;
//        } else if (attest2 == null && compareDates(dateNow, secondB, secondE)) {
//            attest2 = attest;
//        }
//    }
//
//    private boolean compareDates(Date now, Date beg, Date end) {
//        if (beg == null || end == null) {
//            return false;
//        } else {
//            return beg.compareTo(now) * now.compareTo(end) >= 0;
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListHasStudents)) return false;
        ListHasStudents that = (ListHasStudents) o;
        return id == that.id &&
                Objects.equals(attest1, that.attest1) &&
                Objects.equals(attest2, that.attest2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attest1, attest2);
    }

    // inner class defined for primary key(composite keys)
    /*@Embeddable
    @Getter
    public static class ListHasStudentsId {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumns({
                @JoinColumn(name = "subject_list_groups_id", referencedColumnName = "groups_id"),
                @JoinColumn(name = "subject_list_subjects_id", referencedColumnName = "subjects_id")
        })
        private GroupsSubjects groupsSubjects;

//        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//        @JoinColumn(name = "students_id")
        @Column(name = "students_id")
        private Student student;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass())
                return false;

            ListHasStudentsId that = (ListHasStudentsId) o;
            return Objects.equals(groupsSubjects, that.groupsSubjects) &&
                    Objects.equals(student, that.student);
        }

        @Override
        public int hashCode() {
            return Objects.hash(groupsSubjects, student);
        }
    }*/
}
