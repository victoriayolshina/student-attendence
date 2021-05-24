package ru.isu.studentattendance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class GroupStatistics {
    private Group group;
    private Long attended;
    private Long wasLate;
    private Long absent;
    private Long wasSick;

    public GroupStatistics(Group group, Long attended, Long wasLate, Long absent, Long wasSick) {
        this.group = group;
        this.attended = attended;
        this.wasLate = wasLate;
        this.absent = absent;
        this.wasSick = wasSick;
    }
}
