package com.company.domain.inanimate;

import com.company.domain.inanimate.subject.GradeDate;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.inanimate.subject.Work;
import com.company.domain.people.Teacher;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Table(name = "groups_subjects", uniqueConstraints =
    @UniqueConstraint(columnNames = {"groups_id", "subjects_id"})
)
//@AssociationOverrides({
//        @AssociationOverride(name = "primaryKey.group",
//                joinColumns = @JoinColumn(name = "groups_id")),
//        @AssociationOverride(name = "primaryKey.subject",
//                joinColumns = @JoinColumn(name = "subjects_id"))
//})
@Getter
@Setter
@ToString(exclude = {"group", "subject", "teacher", "gradeDateList", "listHasStudentsList", "works"})
public class GroupsSubjects {

//    @EmbeddedId
//    private GroupsSubjectId primaryKey;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groups_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subjects_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teachers_id", nullable = false)
    private Teacher teacher;

    @OneToMany(/*fetch = FetchType.LAZY, */mappedBy = "groupsSubjects", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GradeDate> gradeDateList = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "groupsSubjects", orphanRemoval = true)
    private List<ListHasStudents> listHasStudentsList = new ArrayList<>();

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "groupsSubjects", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Work> works = new ArrayList<>();

    public GroupsSubjects(Group group, Subject subject, Teacher teacher) {
        this.group = group;
        this.subject = subject;
        this.teacher = teacher;
    }

    public boolean addWork(@NonNull Work work) {
        if(works.contains(work)) {
            return false;
        } else {
            works.add(work);
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupsSubjects)) return false;
        GroupsSubjects that = (GroupsSubjects) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        GroupsSubjects that = (GroupsSubjects) o;
//        return Objects.equals(teacher, that.teacher);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(teacher);
//    }

    // inner class defined for primary key(composite keys)
    /*@Embeddable
    @Getter
    public static class GroupsSubjectId {

//        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//        @JoinColumn(name = "groups_id")
        @Column(name = "groups_id")
        private Group group;
//        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//        @JoinColumn(name = "subjects_id")
        @Column(name = "subjects_id")
        private Subject subject;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass())
                return false;

            GroupsSubjectId that = (GroupsSubjectId) o;
            return Objects.equals(group, that.group) &&
                    Objects.equals(subject, that.subject);
        }

        @Override
        public int hashCode() {
            return Objects.hash(group, subject);
        }
    }*/
}
