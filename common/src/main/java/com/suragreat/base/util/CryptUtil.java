package com.suragreat.base.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

public final class CryptUtil {
    private CryptUtil() {
    }
    
    public static String md5(String plainText, String salt) {
        if (StringUtils.isEmpty(plainText)){
            return "";
        }
        String toEnc = plainText;
        if (StringUtils.isNotEmpty(salt)){
            toEnc +=salt;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            throw new IllegalArgumentException("No such algorithm [MD5]");
        }
        try {
            byte[] digest = md.digest(toEnc.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(digest);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
    }
    
    public static void main(String[] args) {
       System.out.println(md5("FALSE", "abcdefg"));
    }
}
