package com.example.me.code.individual_assignment.security;

import java.security.NoSuchAlgorithmException;

public class SecureRandom {

    private String fetchSecureRandom(){
        try {
            byte[] randomBytes = generateSecureRandomBytes(32);
            String secureSecret = bytesToHex(randomBytes);
            return secureSecret;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
