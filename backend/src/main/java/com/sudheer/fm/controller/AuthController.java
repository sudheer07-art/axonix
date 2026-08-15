
package com.sudheer.fm.controller;

import com.sudheer.fm.entity.User;
import com.sudheer.fm.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= AUTH STATUS =================
    @GetMapping("/status")
    public Map<String, String> authStatus(Authentication authentication) {

        Map<String, String> res = new HashMap<>();

        if (authentication == null) {
            res.put("role", "NOT_LOGGED_IN");
            return res;
        }

        res.put("username", authentication.getName());

        for (GrantedAuthority a : authentication.getAuthorities()) {
            res.put("role", a.getAuthority());
            break;
        }

        return res;
    }

    // ================= STUDENT REGISTER =================
    @PostMapping("/register-student")
    public ResponseEntity<String> registerStudent(
            @RequestParam String username,
            @RequestParam String password
    ) {

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_STUDENT");

        userRepository.save(user);

        return ResponseEntity.ok("STUDENT_CREATED");
    }

    // ================= TEACHER REGISTER =================
    @PostMapping("/register-teacher")
    public ResponseEntity<String> registerTeacher(
            @RequestParam String username,
            @RequestParam String password
    ) {

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_TEACHER");

        userRepository.save(user);

        return ResponseEntity.ok("TEACHER_CREATED");
    }
}