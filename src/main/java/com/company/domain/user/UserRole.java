package com.company.domain.user;

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
@Table(name = "user_roles", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name"})
)
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return id == userRole.id &&
                name.equals(userRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
