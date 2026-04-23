//////////package com.sudheer.fm.controller;
//////////
//////////import com.sudheer.fm.entity.User;
//////////import com.sudheer.fm.repository.UserRepository;
//////////import org.springframework.http.ResponseEntity;
//////////import org.springframework.security.crypto.password.PasswordEncoder;
//////////import org.springframework.stereotype.Controller;
//////////import org.springframework.web.bind.annotation.PostMapping;
//////////import org.springframework.web.bind.annotation.RequestMapping;
//////////import org.springframework.web.bind.annotation.RequestParam;
//////////import org.springframework.web.bind.annotation.ResponseBody;
//////////
//////////@Controller
//////////@RequestMapping("/auth")
//////////public class AuthController {
//////////
//////////    private final UserRepository userRepository;
//////////    private final PasswordEncoder passwordEncoder;
//////////
//////////    public AuthController(UserRepository userRepository,
//////////                          PasswordEncoder passwordEncoder) {
//////////        this.userRepository = userRepository;
//////////        this.passwordEncoder = passwordEncoder;
//////////    }
//////////
//////////    // ================= STUDENT REGISTRATION (AJAX) =================
//////////    @PostMapping("/register-student")
//////////    @ResponseBody
//////////    public ResponseEntity<String> registerStudent(
//////////            @RequestParam String username,
//////////            @RequestParam String password
//////////    ) {
//////////
//////////        // check existing user
//////////        if (userRepository.existsByUsername(username)) {
//////////            return ResponseEntity
//////////                    .badRequest()
//////////                    .body("User already exists");
//////////        }
//////////
//////////        // create student
//////////        User user = new User();
//////////        user.setUsername(username);
//////////        user.setPassword(passwordEncoder.encode(password));
//////////        user.setRole("ROLE_STUDENT");
//////////
//////////        userRepository.save(user);
//////////
//////////        // IMPORTANT: no redirect
//////////        return ResponseEntity.ok("SUCCESS");
//////////    }
//////////}
////////package com.sudheer.fm.controller;
////////
////////import com.sudheer.fm.entity.User;
////////import com.sudheer.fm.repository.UserRepository;
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.security.core.Authentication;   // ✅ CORRECT IMPORT
////////import org.springframework.security.core.GrantedAuthority;
////////import org.springframework.security.crypto.password.PasswordEncoder;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.util.HashMap;
////////import java.util.Map;
////////
////////@RestController
////////@RequestMapping("/auth")
////////public class AuthController {
////////
////////    private final UserRepository userRepository;
////////    private final PasswordEncoder passwordEncoder;
////////
////////    public AuthController(UserRepository userRepository,
////////                          PasswordEncoder passwordEncoder) {
////////        this.userRepository = userRepository;
////////        this.passwordEncoder = passwordEncoder;
////////    }
////////
////////    // ================= CHECK LOGIN STATUS =================
////////    @GetMapping("/status")
////////    public String authStatus(Authentication authentication) {
////////
////////        if (authentication == null) {
////////            return "NOT_LOGGED_IN";
////////        }
////////
////////        for (GrantedAuthority authority : authentication.getAuthorities()) {
////////            return authority.getAuthority(); // ROLE_STUDENT or ROLE_TEACHER
////////        }
////////
////////        return "NOT_LOGGED_IN";
////////    }
////////
////////    // ================= STUDENT REGISTRATION =================
////////    @PostMapping("/register-student")
////////    public ResponseEntity<String> registerStudent(
////////            @RequestParam String username,
////////            @RequestParam String password
////////    ) {
////////
////////        if (userRepository.existsByUsername(username)) {
////////            return ResponseEntity.badRequest().body("User already exists");
////////        }
////////
////////        User user = new User();
////////        user.setUsername(username);
////////        user.setPassword(passwordEncoder.encode(password));
////////        user.setRole("ROLE_STUDENT");
////////
////////        userRepository.save(user);
////////
////////        return ResponseEntity.ok("SUCCESS");
////////    }
////////    @GetMapping("/auth/me")
////////    public Map<String, String> currentUser(Authentication authentication) {
////////
////////        Map<String, String> map = new HashMap<>();
////////
////////        if (authentication == null) {
////////            map.put("status", "NOT_LOGGED_IN");
////////            return map;
////////        }
////////
////////        map.put("status", "LOGGED_IN");
////////        map.put("username", authentication.getName());
////////
////////        authentication.getAuthorities()
////////                .forEach(a -> map.put("role", a.getAuthority()));
////////
////////        return map;
////////    }
////////}
//////package com.sudheer.fm.controller;
//////
//////import com.sudheer.fm.entity.User;
//////import com.sudheer.fm.repository.UserRepository;
//////import org.springframework.beans.factory.annotation.Value;
//////import org.springframework.http.ResponseEntity;
//////import org.springframework.security.core.Authentication;
//////import org.springframework.security.core.GrantedAuthority;
//////import org.springframework.security.crypto.password.PasswordEncoder;
//////import org.springframework.web.bind.annotation.*;
//////
//////import java.util.HashMap;
//////import java.util.Map;
//////
//////@RestController
//////@RequestMapping("/auth")
//////
//////public class AuthController {
//////
//////    private final UserRepository userRepository;
//////    private final PasswordEncoder passwordEncoder;
//////
//////    public AuthController(UserRepository userRepository,
//////                          PasswordEncoder passwordEncoder) {
//////        this.userRepository = userRepository;
//////        this.passwordEncoder = passwordEncoder;
//////    }
//////
//////    // 🔍 AUTH STATUS FOR UI (dashboard, role checks)
//////    @GetMapping("/status")
//////    public Map<String, String> authStatus(Authentication authentication) {
//////
//////        Map<String, String> res = new HashMap<>();
//////
//////        if (authentication == null) {
//////            res.put("role", "NOT_LOGGED_IN");
//////            return res;
//////        }
//////
//////        res.put("username", authentication.getName());
//////
//////        for (GrantedAuthority a : authentication.getAuthorities()) {
//////            res.put("role", a.getAuthority());
//////            break;
//////        }
//////
//////        return res;
//////    }
//////
//////    // 🎓 STUDENT REGISTER
//////    @PostMapping("/register-student")
//////    public ResponseEntity<String> registerStudent(
//////            @RequestParam String username,
//////            @RequestParam String password
//////    ) {
//////
//////        if (userRepository.existsByUsername(username)) {
//////            return ResponseEntity.badRequest().body("User already exists");
//////        }
//////
//////        User user = new User();
//////        user.setUsername(username);
//////        user.setPassword(passwordEncoder.encode(password));
//////        user.setRole("ROLE_STUDENT");
//////
//////        userRepository.save(user);
//////
//////        return ResponseEntity.ok("SUCCESS");
//////    }
//////}
////package com.sudheer.fm.controller;
////
////import com.sudheer.fm.entity.User;
////import com.sudheer.fm.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.HashMap;
////import java.util.Map;
////
////@RestController
////@RequestMapping("/auth")
////public class AuthController {
////
////    private final UserRepository userRepository;
////    private final PasswordEncoder passwordEncoder;
////
////    // 🔐 Teacher passcode from application.properties
////    @Value("${teacher.passcode}")
////    private String teacherPasscode;
////
////    public AuthController(UserRepository userRepository,
////                          PasswordEncoder passwordEncoder) {
////        this.userRepository = userRepository;
////        this.passwordEncoder = passwordEncoder;
////    }
////
////    // ================= AUTH STATUS (USED BY UI) =================
////    @GetMapping("/status")
////    public Map<String, String> authStatus(Authentication authentication) {
////
////        Map<String, String> res = new HashMap<>();
////
////        if (authentication == null) {
////            res.put("role", "NOT_LOGGED_IN");
////            return res;
////        }
////
////        res.put("username", authentication.getName());
////
////        for (GrantedAuthority a : authentication.getAuthorities()) {
////            res.put("role", a.getAuthority()); // ROLE_STUDENT / ROLE_TEACHER
////            break;
////        }
////
////        return res;
////    }
////
////    // ================= STUDENT REGISTRATION =================
////    @PostMapping("/register-student")
////    public ResponseEntity<String> registerStudent(
////            @RequestParam String username,
////            @RequestParam String password
////    ) {
////
////        if (userRepository.existsByUsername(username)) {
////            return ResponseEntity.badRequest().body("User already exists");
////        }
////
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword(passwordEncoder.encode(password));
////        user.setRole("ROLE_STUDENT");
////
////        userRepository.save(user);
////
////        return ResponseEntity.ok("SUCCESS");
////    }
////    @Value("${teacher.passcode}")
////    private String teacherPasscode;
////
////    @PostMapping("/register-teacher")
////    public ResponseEntity<String> registerTeacher(
////            @RequestParam String username,
////            @RequestParam String password,
////            @RequestParam String passcode
////    ) {
////
////        if (!teacherPasscode.equals(passcode)) {
////            return ResponseEntity.status(403).body("Invalid teacher passcode");
////        }
////
////        if (userRepository.existsByUsername(username)) {
////            return ResponseEntity.badRequest().body("User already exists");
////        }
////
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword(passwordEncoder.encode(password));
////        user.setRole("ROLE_TEACHER");
////
////        userRepository.save(user);
////
////        return ResponseEntity.ok("TEACHER_CREATED");
////    }
////
////    // ================= TEACHER LOGIN WITH PASSCODE =================
////    @PostMapping("/teacher-login")
////    public ResponseEntity<String> teacherLogin(
////            @RequestParam String username,
////            @RequestParam String password,
////            @RequestParam String passcode
////    ) {
////
////        User user = userRepository.findByUsername(username)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        // Password validation
////        if (!passwordEncoder.matches(password, user.getPassword())) {
////            return ResponseEntity.status(401).body("Invalid password");
////        }
////
////        // Teacher passcode validation
////        if (!passcode.equals(teacherPasscode)) {
////            return ResponseEntity.status(403).body("Invalid teacher passcode");
////        }
////
////        // Assign TEACHER role
////        user.setRole("ROLE_TEACHER");
////        userRepository.save(user);
////
////        return ResponseEntity.ok("TEACHER_LOGIN_SUCCESS");
////    }
////}
//package com.sudheer.fm.controller;
//
//import com.sudheer.fm.entity.User;
//import com.sudheer.fm.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    // 🔐 Teacher passcode (from application.properties / Render env)
//    @Value("${teacher.passcode}")
//    private String teacherPasscode;
//
//    public AuthController(UserRepository userRepository,
//                          PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // ================= AUTH STATUS (USED BY UI) =================
//    @GetMapping("/status")
//    public Map<String, String> authStatus(Authentication authentication) {
//
//        Map<String, String> res = new HashMap<>();
//
//        if (authentication == null) {
//            res.put("role", "NOT_LOGGED_IN");
//            return res;
//        }
//
//        res.put("username", authentication.getName());
//
//        for (GrantedAuthority a : authentication.getAuthorities()) {
//            res.put("role", a.getAuthority()); // ROLE_STUDENT / ROLE_TEACHER
//            break;
//        }
//
//        return res;
//    }
//
//    // ================= STUDENT REGISTRATION =================
//    @PostMapping("/register-student")
//    public ResponseEntity<String> registerStudent(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//
//        if (userRepository.existsByUsername(username)) {
//            return ResponseEntity.badRequest().body("User already exists");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_STUDENT");
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok("STUDENT_CREATED");
//    }
//
//    // ================= TEACHER REGISTRATION =================
//    @PostMapping("/register-teacher")
//    public ResponseEntity<String> registerTeacher(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String passcode
//    ) {
//
//        // 🔒 Passcode validation
//        if (!teacherPasscode.equals(passcode)) {
//            return ResponseEntity.status(403).body("Invalid teacher passcode");
//        }
//
//        if (userRepository.existsByUsername(username)) {
//            return ResponseEntity.badRequest().body("User already exists");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_TEACHER");
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok("TEACHER_CREATED");
//    }
//}
//package com.sudheer.fm.controller;
//
//import com.sudheer.fm.entity.User;
//import com.sudheer.fm.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${teacher.passcode}")
//    private String teacherPasscode;
//
//    public AuthController(UserRepository userRepository,
//                          PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // ================= CHECK LOGIN STATUS =================
//    @GetMapping("/status")
//    public Map<String, String> authStatus(Authentication authentication) {
//
//        Map<String, String> map = new HashMap<>();
//
//        if (authentication == null) {
//            map.put("role", "NOT_LOGGED_IN");
//            return map;
//        }
//
//        map.put("username", authentication.getName());
//
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            map.put("role", authority.getAuthority());
//            break;
//        }
//
//        return map;
//    }
//
//    // ================= STUDENT REGISTER =================
//    @PostMapping("/register-student")
//    public ResponseEntity<String> registerStudent(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//
//        if (userRepository.existsByUsername(username)) {
//            return ResponseEntity.badRequest().body("User already exists");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_STUDENT");
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok("STUDENT_CREATED");
//    }
//
//    // ================= TEACHER REGISTER =================
//    @PostMapping("/register-teacher")
//    public ResponseEntity<String> registerTeacher(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String passcode
//    ) {
//
//        if (!teacherPasscode.equals(passcode)) {
//            return ResponseEntity.status(403).body("Invalid Teacher Passcode");
//        }
//
//        if (userRepository.existsByUsername(username)) {
//            return ResponseEntity.badRequest().body("User already exists");
//        }
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole("ROLE_TEACHER");
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok("TEACHER_CREATED");
//    }
//
//    // ================= VALIDATE TEACHER LOGIN PASSCODE =================
//    @PostMapping("/teacher-login")
//    public ResponseEntity<String> teacherLogin(
//            @RequestParam String passcode
//    ) {
//
//        if (!teacherPasscode.equals(passcode)) {
//            return ResponseEntity.status(403).body("Invalid Passcode");
//        }
//
//        return ResponseEntity.ok("PASSCODE_OK");
//    }
//}
package com.sudheer.fm.controller;

import com.sudheer.fm.entity.User;
import com.sudheer.fm.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    // STUDENT SIGNUP
    @PostMapping("/register-student")
    public ResponseEntity<String> registerStudent(
            @RequestParam String username,
            @RequestParam String password) {

        if(userRepository.existsByUsername(username)){
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_STUDENT");

        userRepository.save(user);

        return ResponseEntity.ok("Student Created");
    }

    // TEACHER SIGNUP
    @PostMapping("/register-teacher")
    public ResponseEntity<String> registerTeacher(
            @RequestParam String username,
            @RequestParam String password) {

        if(userRepository.existsByUsername(username)){
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_TEACHER");

        userRepository.save(user);

        return ResponseEntity.ok("Teacher Created");
    }
}