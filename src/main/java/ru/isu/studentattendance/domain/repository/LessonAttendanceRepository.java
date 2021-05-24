package ru.isu.studentattendance.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.*;

import java.util.List;

@Repository
public interface LessonAttendanceRepository extends CrudRepository<LessonAttendance, Long> {
    List<LessonAttendance> findByLesson(Lesson lesson);
    List<LessonAttendance> findByLessonIn(List<Lesson> lessons);

    List<LessonAttendance> findByLessonOrderById(Lesson lesson);

    @Query("select new ru.isu.studentattendance.domain.model.StudentStatistics(la.status, count(la.status)) " +
            "from LessonAttendance la left join Lesson lesson on la.lesson = lesson " +
            "where la.student = ?1 and lesson.lessonAttendanceIsFinished = true group by la.status")
    List<StudentStatistics> getStudentAttendance(Student student);

    @Query("select new ru.isu.studentattendance.domain.model.GroupStatistics(s.group, " +
            "count(case when la.status = 'ATTENDED' then 1 else null end), " +
            "count(case when la.status = 'WAS_LATE' then 1 else null end), " +
            "count(case when la.status = 'ABSENT' then 1 else null end), " +
            "count(case when la.status = 'WAS_SICK' then 1 else null end)) " +
            "from Lesson l join LessonAttendance la on l = la.lesson inner join Student s on s = la.student " +
            "where l.lessonAttendanceIsFinished = true group by s.group")
    List<GroupStatistics> getGroupAttendance();
}
