package com.extrade.usermanagement.utilities;

import java.security.SecureRandom;

public class RandomGenerator {
    private final static String[] ALPHA_NUMERIC_SEQUENCE = new String[]{"C", "1", "B", "a", "D", "9", "d", "E", "0", "@", "X", "e", "z", "w", "b", "W", "4", "p", "Q", "P", "q", "s", "T", "S", "8", "t", "k", "2", "1", "3", "v"};
    private final static String[] NUMBER_SEQUENCE = new String[]{"9", "5", "1", "0", "8", "7", "2", "6", "3", "4"};

    public static String randomAlphaNumericSpecialCharsSequence(final int length) {
        final StringBuilder builder = new StringBuilder();
        final SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            builder.append(ALPHA_NUMERIC_SEQUENCE[random.nextInt(31)]);
        }
        return builder.toString();
    }

    public static String randomNumericSequence(final int length) {
        final StringBuilder builder = new StringBuilder();
        final SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            builder.append(NUMBER_SEQUENCE[random.nextInt(10)]);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomNumericSequence(6));
    }
}
