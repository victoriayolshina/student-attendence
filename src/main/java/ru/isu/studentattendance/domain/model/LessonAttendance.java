package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "lesson_attendance")
public class LessonAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.EAGER)
    private Student student;

    public enum Status {
        NOT_SELECTED("Not selected"),
        ATTENDED("Attended"),
        WAS_LATE("Was late"),
        ABSENT("Absent"),
        WAS_SICK("Was sick");

        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'NOT_SELECTED'")
    private Status status = Status.NOT_SELECTED;

    private String description;

    public LessonAttendance(Lesson lesson, Student student) {
        this.lesson = lesson;
        this.student = student;
    }
}
