package ru.isu.studentattendance.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "lesson_number")
    private Integer lessonNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public Schedule(Group group, int dayOfWeek, int lessonNumber) {
        this.group = group;
        this.dayOfWeek = dayOfWeek;
        this.lessonNumber = lessonNumber;
    }
}
