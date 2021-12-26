package com.company.domain.user;

import com.company.domain.hei.Faculty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@ToString(/*exclude = {"faculty", "groups", "teachers"}*/)
@Table(name = "moderators", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name"})
)
@Setter
public class Moderator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Moderator)) return false;
        Moderator moderator = (Moderator) o;
        return id == moderator.id &&
                name.equals(moderator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
