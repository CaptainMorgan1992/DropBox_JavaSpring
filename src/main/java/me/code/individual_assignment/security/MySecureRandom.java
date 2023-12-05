package me.code.individual_assignment.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * MySecureRandom class generates a secure random string using SHA1PRNG algorithm.
 */
public class MySecureRandom {

    /**
     * Fetches a secure random string.
     *
     * @return A secure random string.
     */
    static String fetchSecureRandom() {
        try {
            byte[] randomBytes = generateSecureRandomBytes(32);
            String secureSecret = bytesToHex(randomBytes);
            return secureSecret;
        } catch (NoSuchAlgorithmException e) {
            // Handle NoSuchAlgorithmException by printing the stack trace.
            e.printStackTrace();
        }
        // Return null in case of an exception.
        return null;
    }

    /**
     * Generates secure random bytes using the SHA1PRNG algorithm.
     *
     * @param length The length of the random byte array.
     * @return An array of secure random bytes.
     * @throws NoSuchAlgorithmException If the specified algorithm (SHA1PRNG) is not available.
     */
    private static byte[] generateSecureRandomBytes(int length) throws NoSuchAlgorithmException {
        // Create a SecureRandom instance using the SHA1PRNG algorithm.
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        // Generate random bytes with the specified length.
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * Converts a byte array to its hexadecimal representation.
     *
     * @param bytes The byte array to be converted.
     * @return A hexadecimal string representing the input byte array.
     */
    private static String bytesToHex(byte[] bytes) {
        // Create a StringBuilder to build the hexadecimal string.
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        // Iterate through each byte in the array and append its hexadecimal representation.
        for (byte b : bytes) {
            hexStringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        // Return the final hexadecimal string.
        return hexStringBuilder.toString();
    }
}
