package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonAttendanceList {
    private List<LessonAttendance> list;

    private boolean isFinal;

    public boolean isFinal() {
        return isFinal;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean value) {
        isFinal = value;
    }
}
