package com.company.domain.inanimate.subject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "grades", uniqueConstraints =
    @UniqueConstraint(columnNames = {"grade_dates_id", "list_has_students_id"})
)
@ToString(exclude = {"gradeDate", "listHasStudents"})
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "grade", nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_dates_id", nullable = false)
    private GradeDate gradeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_has_students_id", nullable = false)
    private ListHasStudents listHasStudents;

    public Grade(int grade, GradeDate gradeDate, ListHasStudents listHasStudents) {
        this.grade = grade;
        this.gradeDate = gradeDate;
        this.listHasStudents = listHasStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade1 = (Grade) o;
        return id == grade1.id &&
                grade == grade1.grade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade);
    }
}
