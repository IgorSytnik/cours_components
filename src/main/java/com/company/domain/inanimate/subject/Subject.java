package com.company.domain.inanimate.subject;

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
@Getter
@Setter
@Entity
@Table(name = "subjects", uniqueConstraints =
@UniqueConstraint(columnNames = {"name"})
)
@ToString(exclude = {"groupsSubjects"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "subject",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupsSubjects> groupsSubjects = new ArrayList<>();

    public Subject(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return id == subject.id &&
                name.equals(subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
