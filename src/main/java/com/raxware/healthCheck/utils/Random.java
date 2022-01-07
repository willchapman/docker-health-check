package com.raxware.healthCheck.utils;

import java.security.SecureRandom;

/**
 * Because not every connection needs a separate SecureRandom object
 */
public class Random {
    private static final SecureRandom secureRandom = new SecureRandom();

    private Random() {}

    public static int randomInt(int low, int high) {
        return secureRandom.nextInt(high-low) + low;
    }

}
