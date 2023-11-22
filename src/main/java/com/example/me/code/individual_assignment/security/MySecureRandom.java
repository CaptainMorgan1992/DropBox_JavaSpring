package com.example.me.code.individual_assignment.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MySecureRandom {

    private String fetchSecureRandom(){
        try {
            byte[] randomBytes = generateSecureRandomBytes(32);
            String secureSecret = bytesToHex(randomBytes);
            return secureSecret;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] generateSecureRandomBytes(int length) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2*bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return hexStringBuilder.toString();
    }
}
