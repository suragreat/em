package com.suragreat.base.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;


public class DecryptDataSourceTool {

    private static Logger logger = LoggerFactory.getLogger(DecryptDataSourceTool.class);
    private static String wsEntryPath = "service/UcsRemoteService";
    public final static String PROP_ENCRYPTED = "encrypted";
    private final static String KEY_TXT = "n_N6,uo!tZnvbygu";

    @SuppressWarnings({"unchecked"})
    public static DataSource buildDataSource(Properties props) throws Exception {
        String encrypted = props.getProperty(PROP_ENCRYPTED);
        DataSourceBuilder builder = DataSourceBuilder.create();
        // DataSourceBuilder.properties is private, so use reflect to modify it
        Field propsField = DataSourceBuilder.class.getDeclaredField("properties");
        propsField.setAccessible(true);
        Map<String, String> builderProps = (Map<String, String>) propsField.get(builder);
        if ("AES".equals(encrypted)) {
            decryptAesConfigs(props);
        }
        for (Object key : props.keySet()) {
            String strValue = props.get(key).toString();
            builderProps.put(key.toString(), strValue);
        }
        return builder.build();
    }

    private static String getContent(InputStream is) {
        String rt = "";
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = is.read(buf)) != -1) {
                rt += new String(buf, 0, i);
            }
        } catch (Exception e) {
        } finally {
        }
        return rt;
    }

    public static void decryptAesConfigs(Properties properties) {
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        properties.put("url", decryptAES(url, KEY_TXT));
        properties.put("username", decryptAES(username, KEY_TXT));
        properties.put("password", decryptAES(password, KEY_TXT));
    }

    public static String encryptAES(String encryptedHexTxt, String keyTxt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encryptedBytes = encryptedHexTxt.getBytes("utf-8");
            byte[] original = cipher.doFinal(encryptedBytes);
            return bytesToHexString(original);
        } catch (Exception ex) {
            logger.error("ERROR: decrypt failed. error=" + ex.getMessage());
            return null;
        }
    }

    public static String decryptAES(String encryptedHexTxt, String keyTxt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] encryptedBytes = hexStringToBytes(encryptedHexTxt);
            byte[] original = cipher.doFinal(encryptedBytes);
            return new String(original);
        } catch (Exception ex) {
            logger.error("ERROR: decrypt failed. error=" + ex.getMessage());
            return null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] a){
        System.out.println(encryptAES("jdbc:mysql://rm-uf6i8311rt4t65310o.mysql.rds.aliyuncs.com:3306/em?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE", KEY_TXT));
        System.out.println(encryptAES("em", KEY_TXT));
        System.out.println(encryptAES("empwd0", KEY_TXT));
    }
}
