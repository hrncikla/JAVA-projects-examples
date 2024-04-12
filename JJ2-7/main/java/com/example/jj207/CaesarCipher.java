package com.example.jj207;

/**
 * The CaesarCipher class provides methods for encrypting and decrypting text using the Caesar cipher algorithm.
 */
public class CaesarCipher {
    private static final int ALPHABET_SIZE = 26;
    private static final char FIRST_LETTER = 'A';

    /**
     * Encrypts the given text.
     *
     * @param text  The text to be encrypted.
     * @param shift The number of positions to shift each letter in the alphabet.
     * @return The encrypted text.
     */
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                currentChar = (char) ((((int) currentChar + shift - FIRST_LETTER) % ALPHABET_SIZE + ALPHABET_SIZE) % ALPHABET_SIZE + FIRST_LETTER);
            }
            result.append(currentChar);
        }
        return result.toString();
    }

    /**
     * Decrypts the given text.
     *
     * @param text  The text to be decrypted.
     * @param shift The number of positions to shift each letter in the alphabet.
     * @return The decrypted text.
     */
    public static String decrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                currentChar = (char) ((((int) currentChar - FIRST_LETTER - shift + ALPHABET_SIZE) % ALPHABET_SIZE + ALPHABET_SIZE) % ALPHABET_SIZE + FIRST_LETTER);
            }
            result.append(currentChar);
        }
        return result.toString();
    }
}
