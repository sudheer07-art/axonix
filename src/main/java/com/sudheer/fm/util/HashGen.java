package com.sudheer.fm.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGen {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("teacher123 -> " + encoder.encode("teacher123"));
        System.out.println("student123 -> " + encoder.encode("student123"));
    }
}
