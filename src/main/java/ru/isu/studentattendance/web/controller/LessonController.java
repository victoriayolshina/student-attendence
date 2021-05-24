package ru.isu.studentattendance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isu.studentattendance.ScheduleService;
import ru.isu.studentattendance.domain.model.Lesson;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    @Autowired
    ScheduleService scheduleService;

    @ResponseBody
    @RequestMapping(value = "/{lessonId}/delete", method = RequestMethod.POST)
    public String deleteLesson(@PathVariable("lessonId") Lesson lesson) {
        scheduleService.deleteLesson(lesson);

        return "success";
    }
}
