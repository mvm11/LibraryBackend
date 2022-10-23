package com.iud.library;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderGenerator {

    public static void main(String[] args) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Admin: " + passwordEncoder.encode("admin123"));
        System.out.println("Teacher: " + passwordEncoder.encode("teacher123"));
        System.out.println("Student: " + passwordEncoder.encode("student123"));

    }
}
