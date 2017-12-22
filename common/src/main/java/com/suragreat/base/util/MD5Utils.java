package com.suragreat.base.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Utils {
    private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    private MD5Utils() {
    }

    private static ThreadLocal<MessageDigest> messageDigestThreadLocal = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsaex) {
                logger.error("Failed to initialize MessageDigest ", nsaex);
            }
            return null;
        }
    };

    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messageDigestThreadLocal.get().update(byteBuffer);
        return Hex.encodeHexString(messageDigestThreadLocal.get().digest());
    }

    public static String getMD5(String URLName) throws IOException {

        URL url = new URL(URLName);
        InputStream inputStream = new BufferedInputStream(url.openStream());
        byte[] bytes = new byte[1024];
        int len = 0;
        MessageDigest messagedigest = messageDigestThreadLocal.get();
        while ((len = inputStream.read(bytes)) > 0) {
            messagedigest.update(bytes, 0, len);
        }
        String name = Hex.encodeHexString(messagedigest.digest());
        inputStream.close();

        return name;
    }

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();

        File big = new File("d:/tmp/0.jpg");

        String md5 = getFileMD5String(big);

        long end = System.currentTimeMillis();
        System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");

        System.out.print(getMD5("http://mmbiz.qpic.cn/mmbiz_jpg/YE6zDC5FzEKHNaqiciaFxKiaPia0mjXBrJIvTRl5VajM3Diblcqz8IQNj0SOR4Z4RAkCKyRI4x1rSHkUhMgl8huxDFw/0"));
    }

}
