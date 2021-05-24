package ru.isu.studentattendance.domain.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Subject subject;

    private LocalDateTime datetime;

    private int lessonNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    /*@OneToMany(targetEntity = LessonAttendance.class, mappedBy = "lesson", fetch = FetchType.LAZY)
    @OrderBy("fullname ASC")
    private List<LessonAttendance> attendance;*/

    @NotNull
    @ColumnDefault("0")
    private boolean lessonAttendanceIsFinished = false;

    public boolean getLessonAttendanceIsFinished() {
        return lessonAttendanceIsFinished;
    }

    public String getFormattedDate() {
        return datetime.getDayOfWeek().toString();
    }

    public String getFormattedTime() {
        return datetime.toLocalTime().toString();
    }
}
