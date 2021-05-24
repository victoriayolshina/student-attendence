package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentStatistics {
    private LessonAttendance.Status status;
    private Long count;

    public StudentStatistics(LessonAttendance.Status status, Long count) {
        this.status = status;
        this.count = count;
    }
}
