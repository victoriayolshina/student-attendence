package ru.isu.studentattendance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.isu.studentattendance.domain.model.*;
import ru.isu.studentattendance.domain.repository.GroupRepository;
import ru.isu.studentattendance.domain.repository.LessonAttendanceRepository;
import ru.isu.studentattendance.domain.repository.StudentRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private LessonAttendanceRepository lessonAttendanceRepository;

    @RequestMapping(value = "/")
    public String getStudents(Model model) {
        List<List<Student>> students = new LinkedList<>();

        for (Group group : groupRepository.findAll()) {
            List<Student> s = group.getStudents();
            if (s.isEmpty())
                s.add(new Student(group, "no students"));
            students.add(s);
        }

        model.addAttribute("students", students);
        return "students/list";
    }

    @RequestMapping("/{studentId}")
    public String getStudent(@PathVariable("studentId") Student student, Model model, Authentication auth) {
        model.addAttribute("student", student);

        List<StudentStatistics> statList = lessonAttendanceRepository.getStudentAttendance(student);
        for (LessonAttendance.Status status : LessonAttendance.Status.values()) {
            if (status == LessonAttendance.Status.NOT_SELECTED) continue;
            boolean f = false;
            for (StudentStatistics s : statList)
                if (s.getStatus() == status)
                    f = true;
            if (!f)
                statList.add(new StudentStatistics(status, 0L));
        }

        model.addAttribute("attendance", statList);

        return "students/show";
    }

    @RequestMapping(value = "/create")
    public String createStudent(@ModelAttribute Group group, Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("student", new Student(group, ""));
        model.addAttribute("formType", "Create");
        return "students/form";
    }

    @RequestMapping(value = "/{studentId}/edit")
    public String editStudent(@PathVariable("studentId") Student student, Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("student", student);
        model.addAttribute("formType", "Edit");
        return "students/form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveStudent(@Valid @ModelAttribute Student student, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("student", student);
            model.addAttribute("formType", "*");
            return "students/form";
        }
        studentRepository.save(student);
        return "redirect:/students/";
    }

    @RequestMapping("/statistics")
    public String getGroupStatistics(Model model) {
        List<GroupStatistics> stat = lessonAttendanceRepository.getGroupAttendance();

        model.addAttribute("attendance", stat);
        model.addAttribute("date", LocalDate.now());

        return("/students/statistics");
    }
}

