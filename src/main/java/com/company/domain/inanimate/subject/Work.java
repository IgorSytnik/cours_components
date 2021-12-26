package com.company.domain.inanimate.subject;

import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.StudentsHasWorks;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "works")
@ToString(exclude = {"groupsSubjects", "StudentsHasWorksList"})
public class Work /*extends ClassWithName*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "term", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date term;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groups_subjects_Id", nullable = false)
    private GroupsSubjects groupsSubjects;

    @OneToMany(mappedBy = "primaryKey.work", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<StudentsHasWorks> StudentsHasWorksList = new ArrayList<>();

    public Work(String name, Date term, GroupsSubjects groupsSubjects) {
        this.name = name;
        this.term = term;
        this.groupsSubjects = groupsSubjects;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        Work that = (Work) o;
//        return Objects.equals(term, that.term) &&
//                Objects.equals(name, that.name) /*&&
//                Objects.equals(subject, that.subject) &&
//                Objects.equals(group, that.group)*/;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(term, name/*, subject, group*/);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Work)) return false;
        Work work = (Work) o;
        return id == work.id &&
                name.equals(work.name) &&
                term.equals(work.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, term);
    }
}
