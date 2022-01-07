package com.raxware.healthCheck.utils;

public class FilterUtils {
    private FilterUtils() {}

    public static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPHA_UPPER = ALPHA_LOWER.toUpperCase();

    public static String filter(String input, String allowed) {
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if(allowed.indexOf(c) != -1)
                builder.append(c);
        }
        return builder.toString();
    }
}
