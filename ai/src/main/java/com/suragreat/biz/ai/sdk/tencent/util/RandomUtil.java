package com.suragreat.biz.ai.sdk.tencent.util;

import java.util.Random;

public class RandomUtil {
    private static char[] randomStrChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', '0'};

    private static Random r = new Random();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        while (length-- > 0) {
            sb.append(randomStrChar[r.nextInt(randomStrChar.length)]);
        }
        return sb.toString();
    }
}
