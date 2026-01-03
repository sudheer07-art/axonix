//package com.sudheer.fm.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final CustomLoginSuccessHandler customLoginSuccessHandler;
//
//    public SecurityConfig(CustomLoginSuccessHandler customLoginSuccessHandler) {
//        this.customLoginSuccessHandler = customLoginSuccessHandler;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //   @Bean
////public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////
////    http
////        .csrf(csrf -> csrf.disable())
////        .authorizeHttpRequests(auth -> auth
////            .requestMatchers(
////                "/register.html",
////                "/auth/**",
////                "/css/**",
////                "/js/**",
////                "/images/**"
////            ).permitAll()
////            .anyRequest().authenticated()
////        )
////        .formLogin(login -> login
////            .loginPage("/register.html")
////            .loginProcessingUrl("/login")
////
////            // 🔥 KEY FIX — STAY ON SAME PAGE
////            .defaultSuccessUrl("/register.html", false)
////
////            .permitAll()
////        )
////        .logout(logout -> logout
////            .logoutUrl("/logout")
////            .logoutSuccessUrl("/register.html")
////        );
////
////    return http.build();
////}
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/register.html",
//                                "/auth/**",
//                                "/css/**",
//                                "/js/**",
//                                "/images/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/register.html")
//                        .loginProcessingUrl("/login")
//
//                        // 🔥 KEY FIX — STAY ON SAME PAGE
//                        .defaultSuccessUrl("/register.html", false)
//
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/register.html")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                );
//
//        return http.build();
//    }
//}
package com.sudheer.fm.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/aisummary.html",
                                "/ai/**",
                                "/register.html",
                                "/test.html",
                                "/assets/**",
                                "/universitypdf.html",
                                "/auth/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 🔐 LOGIN — NO REDIRECT
                .formLogin(login -> login
                        .loginPage("/register.html")
                        .loginProcessingUrl("/login")
                        .successHandler((req, res, auth) -> {
                            res.setStatus(HttpServletResponse.SC_OK);
                        })
                        .failureHandler((req, res, ex) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .permitAll()
                )

                // 🚪 LOGOUT — NO REDIRECT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/register.html")
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
