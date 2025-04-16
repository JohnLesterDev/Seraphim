/*
 * This file is part of Seraphim - Universal Secure Vault Overseer.
 * 
 * Seraphim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Seraphim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Seraphim.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2025 JohnLesterDev
 */

package dev.johnlester.seraphim.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Utility class for generating random salt strings and hashing input strings
 * using the SHA-256 hashing algorithm.
 * 
 * @author JohnLesterDev
 */
public class SecuredUtils {
    
    /**
     * Returns the SHA-256 hash of the input string as a hexadecimal string.
     *
     * @param input the string to hash
     * @return the SHA-256 hash of the input string
     * @throws NoSuchAlgorithmException if the SHA-256 hashing algorithm is not found
     */
    public static String hashSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }


    
    /**
     * Generates a random salt string with a length of 16 bytes, or 32 characters when
     * represented as a hexadecimal string.
     *
     * @return a random salt string
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        StringBuilder hexString = new StringBuilder();
        for (byte b : salt) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * Checks the strength of a given PIN number.
     * 
     * A PIN with the following properties is deemed UNACCEPTABLE:
     * - The PIN consists of the same digit repeated 6 times.
     * - The PIN consists of 3 or more consecutive digits.
     * 
     * A PIN with the following properties is deemed WEAK:
     * - The PIN consists of 2 or more pairs of the same digit.
     * 
     * A PIN with the following properties is deemed MEDIUM:
     * - The PIN consists of 2 or more of the same digit.
     * 
     * A PIN with no restrictions is deemed STRONG.
     * 
     * @param pin the PIN number to check
     * @return the strength of the given PIN
     * @throws IllegalArgumentException if the PIN is not exactly 6 digits
     */
    public static SecuredStrength getPinStrength(String pin) {
        if (pin == null || pin.length() != 6) {
            throw new IllegalArgumentException("PIN must be exactly 6 digits.");
        }
    
        if (pin.matches("(\\d)\\1{5}")) {
            return SecuredStrength.UNACCEPTABLE;
        }

        int sequenceCount = 1;
        for (int i = 0; i < pin.length() - 1; i++) {
            int current = Character.getNumericValue(pin.charAt(i));
            int next = Character.getNumericValue(pin.charAt(i + 1));
            
            if (next == current + 1 || next == current - 1) {
                sequenceCount++;
                if (sequenceCount >= 3) {
                    return SecuredStrength.UNACCEPTABLE;
                }
            } else {
                sequenceCount = 1;
            }
        }
    
        if (pin.matches(".*(\\d)\\1{2}.*")) {
            return SecuredStrength.WEAK;
        }
    
        if (pin.matches(".*(\\d)\\1{1}.*")) {
            return SecuredStrength.MEDIUM;
        }
    
        return SecuredStrength.STRONG;
    }

    /**
     * Returns the SHA-256 hash of the input string concatenated with the provided salt.
     *
     * @param input the string to hash
     * @param salt the salt to append to the input before hashing
     * @return the SHA-256 hash of the input string with the salt
     * @throws NoSuchAlgorithmException if the SHA-256 hashing algorithm is not found
     */
    public static String hashWithSalt(String input, String salt) throws NoSuchAlgorithmException {
        return hashSHA256(input + salt);
    }
    
}
