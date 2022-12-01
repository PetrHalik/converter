package com.petrhalik.converter.testutils;

public final class TestUtils {

    public static  String normalizeEOL(String str) {
        String newStr = str.replaceAll("\\r\\n", "\n");
        return newStr.replaceAll("\\r", "\n");
    }
}
