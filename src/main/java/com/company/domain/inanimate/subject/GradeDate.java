package com.company.domain.inanimate.subject;

import com.company.domain.inanimate.GroupsSubjects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "grade_dates", uniqueConstraints =
        @UniqueConstraint(columnNames = {"groups_subjects_id", "date"})
)
@ToString(exclude = {"groupsSubjects", "grades"})
public class GradeDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "subject_list_groups_id", referencedColumnName = "groups_id"),
//            @JoinColumn(name = "subject_list_subjects_id", referencedColumnName = "subjects_id")
//    })
//    private GroupsSubjects groupsSubjects;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groups_subjects_Id", nullable = false)
    private GroupsSubjects groupsSubjects;

    @OneToMany(mappedBy = "gradeDate", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    public GradeDate(Date date, GroupsSubjects groupsSubjects) {
        this.date = date;
        this.groupsSubjects = groupsSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeDate)) return false;
        GradeDate gradeDate = (GradeDate) o;
        return id == gradeDate.id &&
                date.equals(gradeDate.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
