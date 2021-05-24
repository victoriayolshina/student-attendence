package ru.isu.studentattendance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.isu.studentattendance.domain.model.*;
import ru.isu.studentattendance.domain.repository.LessonAttendanceRepository;
import ru.isu.studentattendance.domain.repository.LessonRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class LessonAttendanceController {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonAttendanceRepository lessonAttendanceRepository;

    @RequestMapping("/{lessonId}/edit")
    public String editLessonAttendance(@PathVariable("lessonId") Lesson lesson, Model model, Authentication auth) {
        Group group = ((AutoUser) auth.getPrincipal()).getGroup();

        if (group == null || lesson == null || lesson.getGroup().getId() != group.getId()) {
            return "redirect:/accessDenied";
        }
        if (lesson.getLessonAttendanceIsFinished()) {
            return "redirect:/accessDenied";
        }

        LessonAttendanceList list = new LessonAttendanceList();
        list.setList(lessonAttendanceRepository.findByLessonOrderById(lesson));

        model.addAttribute("attendance", list);
        model.addAttribute("lesson", lesson);

        return "lessonAttendance/edit";
    }

    @RequestMapping("/{lessonId}/show")
    public String getLessonAttendance(@PathVariable("lessonId") Lesson lesson, Model model, Authentication auth) {
        model.addAttribute("attendance", lessonAttendanceRepository.findByLessonOrderById(lesson));

        Group group = ((AutoUser) auth.getPrincipal()).getGroup();

        if (lesson == null || group != null && lesson.getGroup().getId() != group.getId()) {
            return "redirect:/accessDenied";
        }

        model.addAttribute("userBoundToOneGroup", group != null);
        model.addAttribute("lesson", lesson);
        return "lessonAttendance/show";
    }

    @RequestMapping(value = "/{lessonId}/save", method = RequestMethod.POST)
    public String saveLessonAttendance(
            @PathVariable("lessonId") Lesson lesson,
            @Valid @ModelAttribute LessonAttendanceList lessonAttendanceList,
            BindingResult errors,
            Model model, Authentication auth) {

        Group group = ((AutoUser) auth.getPrincipal()).getGroup();

        if (group == null || lesson == null || lesson.getGroup().getId() != group.getId()) {
            return "redirect:/accessDenied";
        }
        if (lesson.getLessonAttendanceIsFinished()) {
            return "redirect:/accessDenied";
        }

        List<LessonAttendance> list = lessonAttendanceList.getList();
        lessonAttendanceRepository.save(list);

        if (!lessonAttendanceList.isFinal()) {
            return "redirect:/schedule/";
        }

        for (LessonAttendance la : list) {
            if (la.getStatus() == LessonAttendance.Status.NOT_SELECTED) {
                model.addAttribute("attendance", lessonAttendanceList);
                model.addAttribute("lesson", lesson);

                errors.rejectValue("isFinal", "attendance.isFinal", "Select status for all students");
                return "lessonAttendance/edit";
            }
        }

        lesson.setLessonAttendanceIsFinished(true);
        lessonRepository.save(lesson);

        return "redirect:/schedule/";
    }

    @RequestMapping("/current")
    public String getCurrentLesson(Model model, Authentication auth) {
        Group group = ((AutoUser) auth.getPrincipal()).getGroup();

        if (group == null) {
            return "redirect:/schedule/";
        }

        LocalDateTime now = LocalDateTime.now();
        List<Lesson> lessons = lessonRepository.findByGroupAndDatetimeBetween(group, now.minusMinutes(90), now);

        if (lessons.isEmpty())
            return "redirect:/schedule/";
        Lesson lesson = lessons.get(0);
        return "redirect:/attendance/" + lesson.getId() + (lesson.getLessonAttendanceIsFinished() ? "/show" : "/edit");
    }
}
