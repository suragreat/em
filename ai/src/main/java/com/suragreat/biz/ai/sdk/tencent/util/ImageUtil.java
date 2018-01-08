package com.suragreat.biz.ai.sdk.tencent.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Base64;

public class ImageUtil {
    public static void main(String[] args) throws IOException {
        String strImg = toBase64("d:/tmp/0.jpg");
        System.out.println(strImg);
        fromBase64(strImg, "d:/tmp/222.jpg");
    }

    public static String toBase64(String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath);
        return toBase64(in);
    }

    public static void fromBase64(String base64Image, String savePath) throws IOException {

        OutputStream out = new FileOutputStream(savePath);
        fromBase64(base64Image, out);
        out.flush();
        out.close();

    }

    public static void fromBase64(String base64Image, OutputStream out) throws IOException {
        if (base64Image == null || out == null)
            return;

        byte[] b = Base64.getDecoder().decode(base64Image);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        out.write(b);
    }

    public static String toBase64(InputStream inputStream) throws IOException {
        return Base64.getEncoder().encodeToString(toByteArray(inputStream));
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                //write(byte[] b, int off, int len)
                //将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte 数组输出流。
                outSteam.write(buffer, 0, len);
            }
            return outSteam.toByteArray();
        } finally {
            IOUtils.closeQuietly(outSteam);
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static  byte[] toByteArray(String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath);
        return toByteArray(in);
    }
}
