package com.example.course.controller;


import com.example.course.dao.MemberRepository;
import com.example.course.dao.RolesRepository;
import com.example.course.entity.Member;
import com.example.course.entity.Roles;
import com.example.course.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RegistrationService registrationService;

    private final RolesRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public RegistrationController(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @GetMapping("/registerForm")
    public String showRegistrationForm(Model model){
        model.addAttribute("members",new Member());
        return "registration-form";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("members")Member member, @RequestParam("role")String role,
                                      @RequestParam("username")String username,
                                      @RequestParam("email")String email
                                      ){
        String encoded = passwordEncoder.encode(member.getPw());
        System.out.println(member.getPw());

        System.out.println("Encoded password: " + encoded);
        member.setPw(encoded);
        member.setActive(true);

        memberRepository.save(member);

        Roles roles = new Roles();
        roles.setMember(member);
        roles.setRole("ROLE_" + role.toUpperCase());

        rolesRepository.saveAndFlush(roles);


        registrationService.registerNewMember(member,"ROLE_"+role.toUpperCase(),username,email);


        return "redirect:/login?registered";
    }


}
