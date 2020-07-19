package com.mervynlam.downloadnovel.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class LoginUtils {

    public static final String LOGIN_URL = "https://wap.jjwxc.net/login/wapLogin";

    public static void login() {
        System.out.println("-------------------------------------");
        System.out.println("登陆");
        try {
            Scanner sc = new Scanner(System.in);
            String text = null;
            Connection.Response response;
            do {
                if (text != null) {
                    System.out.println("账户密码错误");
                }
                System.out.print("用户名:");
                String loginname = sc.next();
                System.out.print("密码:");
                String loginpass = sc.next();
                response = Jsoup.connect(LOGIN_URL)
                        .referrer("https://wap.jjwxc.net/my/login?login_mode=jjwxc")
                        .data("loginname", loginname)
                        .data("loginpass", loginpass)
                        .data("cookietime", "1")
                        .data("login_mode", "jjwxc")
                        .userAgent(ApiUtils.USER_AGENT)
                        .execute();
                Document doc = response.parse();
                text = doc.select(".b.module h2").first().text();
                System.out.println(text);
            } while (text.contains("操作"));

            CookiesUtils.setCookiesToFile(response.cookies());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("登录失败");
        }
    }

}
