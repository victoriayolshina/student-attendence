package ru.isu.studentattendance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.studentattendance.domain.model.*;
import ru.isu.studentattendance.domain.repository.LessonAttendanceRepository;
import ru.isu.studentattendance.domain.repository.LessonRepository;
import ru.isu.studentattendance.domain.repository.ScheduleRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonAttendanceRepository lessonAttendanceRepository;

    private static final String[] lessonsTime = {
            "08:30",
            "10:10",
            "11:50",
            "13:50",
            "15:30",
            "17:10"};

    private final int workingDaysNumber = 6;

    public static LocalTime getLessonTime(int number) {
        return LocalTime.parse(lessonsTime[number - 1]);
    }

    public static String getDayOfWeekName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1 : return "Monday";
            case 2 : return "Tuesday";
            case 3 : return "Wednesday";
            case 4 : return "Thursday";
            case 5 : return "Friday";
            case 6 : return "Saturday";
            case 7 : return "Sunday";
        }
        return "none";
    }


    private List<LessonAttendance> createLessonsAttendance(List<Lesson> lessons) {
        Group group = lessons.get(0).getGroup();
        List<Student> students = group.getStudents();

        List<LessonAttendance> attendanceList = new LinkedList<>();

        for (Lesson lesson : lessons) {
            for (Student student : students) {
                attendanceList.add(new LessonAttendance(lesson, student));
            }
        }

        lessonAttendanceRepository.save(attendanceList);
        return attendanceList;
    }

    public boolean dateBelongsToCurrentWeek(LocalDate date) {
        LocalDate monday = LocalDate.now().minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);
        return monday.equals(date) || sunday.equals(date) || (monday.isBefore(date) && sunday.isAfter(date));
    }

    private List<Lesson> createWeekLessons(Group group, LocalDate date) {
        LocalDate dateMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);
        List<Schedule> weekSchedule = scheduleRepository.findByGroup(group);

        List<Lesson> lessons = new LinkedList<>();
        for (Schedule s : weekSchedule) {
            Lesson lesson = new Lesson();
            lesson.setGroup(group);
            lesson.setSubject(s.getSubject());
            LocalDateTime datetime = dateMonday.plusDays(s.getDayOfWeek() - 1).atTime(getLessonTime(s.getLessonNumber()));
            lesson.setDatetime(datetime);
            lesson.setLessonNumber(s.getLessonNumber());
            lessons.add(lesson);
        }

        lessonRepository.save(lessons);
        if (!lessons.isEmpty())
            createLessonsAttendance(lessons);

        return lessons;
    }

    public List<Lesson> getWeekLessons(Group group, LocalDate date) {
        LocalDate dateMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);

        List<Lesson> lessons = lessonRepository.findByGroupAndDatetimeBetween(
                group,
                dateMonday.atTime(0, 0),
                dateMonday.plusDays(6).atTime(23, 59));

        if (lessons.isEmpty() && dateBelongsToCurrentWeek(date)) {
            return createWeekLessons(group, date);
        }

        return lessons;
    }

    public CurrentSchedule getWeekSchedule(Group group, LocalDate date) {
        List<Lesson> lessons = getWeekLessons(group, date);

        LocalDate dateMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);

        if (lessons.isEmpty()) {
            return new CurrentSchedule(null, dateMonday);
        }

        List<List<Lesson>> scheduleWeek = new LinkedList<>();
        Iterator<Lesson> iter = lessons.iterator();

        List<Lesson> tempDayLessons = new LinkedList<>();
        Lesson tempLesson = iter.next();
        LocalDate tempDay = dateMonday;
        while (true) {
            if (!tempLesson.getDatetime().toLocalDate().equals(tempDay)) {
                scheduleWeek.add(tempDayLessons);
                tempDayLessons = new LinkedList<>();
                //add lesson;
                tempDay = tempDay.plusDays(1);
                continue;
            }

            if (tempLesson.getLessonNumber() != tempDayLessons.size() + 1) {
                tempDayLessons.add(null);
                continue;
            }

            tempDayLessons.add(tempLesson);
            if (!iter.hasNext()) {
                scheduleWeek.add(tempDayLessons);
                break;
            }
            tempLesson = iter.next();
        }

        return new CurrentSchedule(scheduleWeek, dateMonday);
    }

    @Getter
    @Setter
    @ToString
    public static class ScheduleFormatted {
        List<List<Schedule>> list;

        public ScheduleFormatted() {
            list = new LinkedList<>();
        }
    }

    public ScheduleFormatted getGroupSchedule(Group group) {
        List<Schedule> schedule = scheduleRepository.findByGroup(group);

        ScheduleFormatted result = new ScheduleFormatted();

        for (int dayOfWeek = 1; dayOfWeek <= workingDaysNumber; dayOfWeek++) {
            List<Schedule> dayTemp = new LinkedList<Schedule>();
            for (int lessonNumber = 1; lessonNumber <= lessonsTime.length; lessonNumber++) {

                dayTemp.add(new Schedule(group, dayOfWeek, lessonNumber));
            }
            result.getList().add(dayTemp);
        }

        for (Schedule s : schedule) {
            result.getList().get(s.getDayOfWeek() - 1).set(s.getLessonNumber() - 1, s);
        }

        return result;
    }


    public void save(ScheduleFormatted schedule) {

        for (List<Schedule> dayLessons : schedule.getList()) {
            for (Schedule s : dayLessons) {
                if (s.getSubject() != null)
                    scheduleRepository.save(s);
                else if (s.getId() != null)
                    scheduleRepository.delete(s);
            }
        }
    }

    public void deleteLesson(Lesson lesson) {
        lessonAttendanceRepository.delete(lessonAttendanceRepository.findByLesson(lesson));
        lessonRepository.delete(lesson);
    }
}
