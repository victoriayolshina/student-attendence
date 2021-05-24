package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CurrentDaySchedule {
    private List<Lesson> lessons;

    private String dayOfWeek;
}
