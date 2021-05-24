package ru.isu.studentattendance.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.Group;
import ru.isu.studentattendance.domain.model.Schedule;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    @Query(value = "select s from Schedule s where s.group = ?1 order by s.dayOfWeek asc, s.lessonNumber asc")
    List<Schedule> findByGroup(Group group);
}
