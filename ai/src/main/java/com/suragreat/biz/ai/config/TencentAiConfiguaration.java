package com.suragreat.biz.ai.config;

import com.suragreat.base.db.util.DecryptDataSourceTool;
import com.suragreat.biz.ai.sdk.tencent.ImageEffectApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("tencentai")
public class TencentAiConfiguaration {
    private final static String KEY_TXT = "n_N6,uo!tZnWbGg@";
    private final static String APP_ID = "appId";
    private final static String API_KEY = "apiKey";
    private final static String SECRET_KEY = "secretKey";
    private Map<String, String> faceMerge = new HashMap<>();

    public static void main(String[] a) {
        System.out.println(DecryptDataSourceTool.encryptAES("1106628622", KEY_TXT));
        System.out.println(DecryptDataSourceTool.encryptAES("bHHt4QbzMVEULBVY", KEY_TXT));
    }

    public Map<String, String> getFaceMerge() {
        return faceMerge;
    }

    public void setFaceMerge(Map<String, String> faceMerge) {
        this.faceMerge = faceMerge;
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
    public ImageEffectApi imageEffectApi() {
        Map<String, String> tmp = new HashMap<>();
        decryptMap(faceMerge, tmp);
        return new ImageEffectApi(tmp.get(APP_ID), tmp.get(API_KEY));
    }
}
