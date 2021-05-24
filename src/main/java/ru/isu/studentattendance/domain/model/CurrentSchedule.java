package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CurrentSchedule {
    private List<List<Lesson>> lessons;

    private LocalDate dateMonday;

    public CurrentSchedule(List<List<Lesson>> lessons, LocalDate dateMonday) {
        this.lessons = lessons;
        this.dateMonday = dateMonday;
    }
}
