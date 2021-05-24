package ru.isu.studentattendance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.isu.studentattendance.domain.model.AutoUser;
import ru.isu.studentattendance.domain.repository.AutoUserRepository;
import ru.isu.studentattendance.domain.repository.GroupRepository;

@Controller
public class HomeController {

    @Autowired
    private AutoUserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @RequestMapping(value="/")
    public String home(){
        return "redirect:/schedule/";
    }

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String goLogin(){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute AutoUser user) {
        if (user.getGroup() == null)
            user.setRole("ROLE_ADMIN");
        else
            user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String goRegister(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "register";
    }

}
