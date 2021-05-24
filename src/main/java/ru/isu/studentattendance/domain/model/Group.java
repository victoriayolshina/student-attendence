package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "students")
@Table(name = "groups", uniqueConstraints = {
        @UniqueConstraint(name = "idx_code_year", columnNames = {"code", "year"})
})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer code;

    private Integer year;

    @OneToMany(targetEntity = Student.class, mappedBy = "group", fetch = FetchType.EAGER)
    @OrderBy("fullname ASC")
    private List<Student> students;

    public String getFullName() {
        return String.format("%d %d - %s", getCode(), getYear(), getName());
    }
}
