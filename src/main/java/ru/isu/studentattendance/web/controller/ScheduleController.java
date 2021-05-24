package ru.isu.studentattendance.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.isu.studentattendance.domain.model.AutoUser;
import ru.isu.studentattendance.domain.model.CurrentSchedule;
import ru.isu.studentattendance.domain.model.Group;
import ru.isu.studentattendance.ScheduleService;
import ru.isu.studentattendance.domain.repository.GroupRepository;
import ru.isu.studentattendance.domain.repository.SubjectRepository;

import java.time.LocalDate;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @RequestMapping("/{groupId}")
    public String getSchedule(@ModelAttribute(name = "date") String sDate, @PathVariable("groupId") Group group, Model model) {
        LocalDate date;
        if (sDate.isEmpty()) date = LocalDate.now();
        else                 date = LocalDate.parse(sDate);

        model.addAttribute("schedule", scheduleService.getWeekSchedule(group, date));
        model.addAttribute("group", group);

        model.addAttribute("prevWeek", date.minusDays(7));
        model.addAttribute("nextWeek", scheduleService.dateBelongsToCurrentWeek(date) ? null : date.plusDays(7));

        return "/schedule/curator";
    }

    @RequestMapping("/")
    public String getScheduleList(@ModelAttribute(name = "date") String sDate, Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(AuthorityUtils.createAuthorityList("ROLE_ADMIN").get(0));

        if (!isAdmin) {
            Group group = ((AutoUser) auth.getPrincipal()).getGroup();

            LocalDate date;
            if (sDate.isEmpty()) date = LocalDate.now();
            else                 date = LocalDate.parse(sDate);

            CurrentSchedule schedule = scheduleService.getWeekSchedule(group, date);
            model.addAttribute("schedule", schedule);
            model.addAttribute("group", group);
            model.addAttribute("prevWeek", date.minusDays(7));
            model.addAttribute("nextWeek", scheduleService.dateBelongsToCurrentWeek(date) ? null : date.plusDays(7));

            return "/schedule/teamhead";
        }

        model.addAttribute("groups", groupRepository.findAll());

        return "/schedule/list";
    }

    @RequestMapping("/{groupId}/edit")
    public String editSchedule(@PathVariable("groupId") Group group, Model model) {
        model.addAttribute("schedule", scheduleService.getGroupSchedule(group));
        model.addAttribute("group", group);
        model.addAttribute("subjects", subjectRepository.findAll());

        return "/schedule/edit";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveSchedule(@ModelAttribute ScheduleService.ScheduleFormatted schedule, Model model) {

        scheduleService.save(schedule);
        return "redirect:/schedule/";
    }
}
