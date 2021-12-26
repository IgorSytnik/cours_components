package com.company.domain.inanimate;

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
@Table(name = "specialties")
@ToString(exclude = {"groups"})
public class Specialty /*extends ClassWithName*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Transient
//    @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
    private final List<Group> groups = new ArrayList<>();

    public Specialty(String name) {
        this.name = name;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        Specialty that = (Specialty) o;
//        return Objects.equals(name, that.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
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
//        if (obj instanceof Specialty) {
//            Specialty anobj = (Specialty)obj;
//            return this.name.equals(anobj.name);
//        }
//        return false;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specialty)) return false;
        Specialty specialty = (Specialty) o;
        return id == specialty.id &&
                name.equals(specialty.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Specialty)) return false;
//        Specialty specialty = (Specialty) o;
//        return id == specialty.id &&
//                name.equals(specialty.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name);
//    }
}
