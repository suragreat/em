package com.suragreat.biz.ai.config;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.nlp.AipNlp;
import com.suragreat.base.db.util.DecryptDataSourceTool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("baiduai")
public class BaiduAiConfiguaration {
    private final static String KEY_TXT = "n_N6,uo!tZnWbGg@";
    private final static String APP_ID = "appId";
    private final static String API_KEY = "apiKey";
    private final static String SECRET_KEY = "secretKey";
    private Map<String, String> aipImageClassify = new HashMap<>();

    public static void main(String[] a) {
        System.out.println(DecryptDataSourceTool.encryptAES("10470766", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("aefDR3GGzGEpTUggtkYSyDiO", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("fFlbS7y3fXvF39s2Nz45CfZFKYdof7dK", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("wx700ea0db4d682e72", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("f3a959106814ba4055ae2192b66a1903", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("token4d5FdGf9HYf", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("aesErfv6HUJ3gyYi", KEY_TXT));
    }

    public Map<String, String> getAipImageClassify() {
        return aipImageClassify;
    }

    public void setAipImageClassify(Map<String, String> aipImageClassify) {
        this.aipImageClassify = aipImageClassify;
    }

    private void decryptMap(Map<String, String> source, Map<String, String> destination) {
        if (source != null) {
            source.forEach((k, v) -> {
                        destination.put(k, DecryptDataSourceTool.decryptAES(v, KEY_TXT));
                    }
            );
        }
    }

    @Bean
    public AipNlp aipNlp() {
        Map<String, String> tmp = new HashMap<>();
        decryptMap(aipImageClassify, tmp);
        return new AipNlp(tmp.get(APP_ID), tmp.get(API_KEY), tmp.get(SECRET_KEY));
    }

    @Bean
    public AipImageClassify aipImageClassify()  {
        Map<String, String> tmp = new HashMap<>();
        decryptMap(aipImageClassify, tmp);
        return new AipImageClassify(tmp.get(APP_ID), tmp.get(API_KEY), tmp.get(SECRET_KEY));
    }
}
