package ru.isu.studentattendance.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.isu.studentattendance.domain.model.Group;
import ru.isu.studentattendance.domain.model.Lesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson, Long> {
    @Query(value = "select l from Lesson l where l.group = ?1 and l.datetime between ?2 and ?3 order by l.datetime asc")
    List<Lesson> findByGroupAndDatetimeBetween(Group group, LocalDateTime startDate, LocalDateTime endDate);
}
