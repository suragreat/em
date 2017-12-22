package com.suragreat.em.biz;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test implements Runnable {
    static CloseableHttpClient client = HttpClientBuilder.create().build();

    public static void main(String[] a) {
        ExecutorService es = Executors.newCachedThreadPool();
        waitTime();
        for (int i = 0; i < 1; i++) {
            Test t = new Test();
            es.execute(t);
        }
        es.shutdown();

        //try {
        //    client.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    private static void waitTime() {
        Date date = new Date();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        while (hour <10 && minute<=59 && second<=50){
             hour = date.getHours();
             minute = date.getMinutes();
             second = date.getSeconds();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        String uri = "https://free.aliyun.com/json/gainTicket.json?trialType=0&umidToken=Yea6926d90e2f73dfcfae7de1e25b6871&collina=099%23KAFEtYEiE2QEIETLEEEEE6twScJFS6thSXJoV60qS9l4a6V1ScJYD6VhDc95G6NTJGFETRpCD67nE7EKt3mS1I%2Bdt6xJ6GFE19iA57il%2F3iYRzqTEE9St3llsyFgN7FET3iSET1lE7EFNIaHF24TEHREDILmiwuqw4G6qX7VqESRvfP%2BbxEGaPgMnhsDqipcvH4f9ef%2FIHvkaHoZbHUamdeIZhxQI%2BCoiGZ6cuMbftP2h7Yss8KYnH4IiwL%2FoZ7fB0CygGFEAyRRDI4ScblcL4wsDwqV1Gbt1OLj%2FRz63Me6r0MncYZc95r8GspB1twc1Wy3bLXp1nZRbo5ec7GpaiZdCRMZYMscwXvo8Kbl3BdEQtE28EEt9as%2BDg%2By3yll3XwmbzAR%2FLtl%2FuWDIdl3aquYSR4f9BDRiOJ5Hsw6wTE3rzxAPGDtYMW4C0W2lvdl3Xafhg8u3YEtQ6F2hGwE37CWnOIdiYrRwp9ibL4RCaDus6dyq4ep%2Bbk6%2FWEbaa%2F3FpiTvLYRiUW6rPbZO7FEp3iSlllP%2F3iSt377mXZdtU%2FStTTmsyantliSFHBP%2F3Mrt377mXZdt%2BW%2FE7Eht%2FMFE6rcJ7FE13iSEJ4WriczTGFET%2FiDlllP%2FTdTEExCbPi5z7FETRRCD679E7EFbOR5DRbTEEaSd55FPyaSt3irB3d9E7EFbOR5DRbTETiSt1oIOsaSt3ik67wPD%2FxNtTnl81qTEEySt9llau7%3D";
        String cookie = "l=Aisr/CMF9KRdHkTeL2IuC57WO0UXTz8H; cnz=+6zbEWcbGjECATKAX2WaM8zv; UM_distinctid=15f22d0ecde46d-06ad2349297aa2-3b3e5906-100200-15f22d0ecdf750; cna=Pc5XEaDxCFcCAWVfgDIaCah9; JSESSIONID=41666WC1-GOJPLQFAMXW0RJZQ8XLR1-Y0ELQ1AJ-DP32; free_aliyun_com_tmp0=eNrz4A12DQ729PeL9%2FV3cfUxiKrOTLFSMjE0MzMLdzbUdff3CvAJdHP0jQg3CPKKCrSI8Aky1I00cPUJNHT00nUJMDZS0kkusTI0NTQwtzQyMDc1NjbQSUxGE8itsDKojQIAMEAcKA%3D%3D; aliyun_lang=zh; _uab_collina=151079232376399920148123; channel=Z%2FzJ%2BQR2jiqqTfD%2FsIeTDw%3D%3D; login_aliyunid=\"surag****\"; login_aliyunid_ticket=_oApof_BNTwUhTOoNC1ZBeeMfKJzxdnb95hYssNIZor6q7SCxRtgmGCbifG2Cd4ZWazmBdHI6sgXZqg4XFWQfyKpeu*0vCmV8s*MT5tJl3_1$$wCD*Uf7yJ0B*36DNtl0qo5U7ELcT9ojwpmupKwc6ah0; login_aliyunid_csrf=_csrf_tk_1321211140990379; login_aliyunid_pk=1485103385937174; hssid=1Iy16Xy6sjS4lTPCRrXXMrg1; hsite=6; aliyun_country=CN; aliyun_site=CN; aliyun_choice=CN; _ga=GA1.2.237155857.1492073827; _gid=GA1.2.1269652342.1511140940; _gat=1; _umdata=2FB0BDB3C12E491D610145B5BE6107494B1538B6C348EF4CFD52402299CD8BDFA40AFE92EDCAFFBCCD43AD3E795C914C71669C0B236E3F15925F0B86348F4EC1; isg=AhoasyL3FAdurZpWaWeZROE3a8A2us4-e879HCSTs61xl7vRDdskNb6hE1Xw";

        HttpUriRequest req = new HttpGet(uri);
        req.setHeader("authority", "free.aliyun.com");
        req.setHeader("cookie", cookie);
        req.setHeader("referer", "https://free.aliyun.com/ntms/free/experience/getTrial.html?spm=5176.7973419.726407.45.45e9f067kQkFtt");
        req.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        req.setHeader("x-requested-with", "XMLHttpRequest");
        for (int i = 0; i < 500; i++) {
            try {
                CloseableHttpResponse resp = client.execute(req);
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[4096];
                InputStream input = resp.getEntity().getContent();
                for (int n; (n = input.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                System.out.println(out.toString());
                if (out.toString().contains("\"success\":true")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
