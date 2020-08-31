package com.jakeporter.WellbeingTrackerAPI.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author jake
 */
public class PasswordEncoder {
    public static void main(String[] args) {
        String clearTxtPw = "thisIsapa55word";
        // BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPw = encoder.encode(clearTxtPw);
        System.out.println(hashedPw);
    }
}
