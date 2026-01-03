package com.sudheer.fm.controller;

import com.sudheer.fm.entity.User;
import com.sudheer.fm.repository.UserRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AdminController(UserRepository userRepo,
                           PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    // ================= ADMIN CHECK (UI VISIBILITY) =================
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/check")
    @ResponseBody
    public String checkAdmin() {
        return "OK";
    }

    // ================= BLOCK DIRECT ACCESS TO TEACHER REGISTER PAGE =================
    @GetMapping("/register-teacher")
    public String redirectTeacherRegister() {
        return "redirect:/login.html";
    }

    // ================= CREATE TEACHER (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/register-teacher")
    public String registerTeacher(@RequestParam String username,
                                  @RequestParam String password) {

        if (userRepo.findByUsername(username).isPresent()) {
            return "redirect:/register.html?error=true";
        }

        User teacher = new User();
        teacher.setUsername(username);
        teacher.setPassword(encoder.encode(password));
        teacher.setRole("ROLE_TEACHER");

        userRepo.save(teacher);

        return "redirect:/register.html?success=true";
    }
}
