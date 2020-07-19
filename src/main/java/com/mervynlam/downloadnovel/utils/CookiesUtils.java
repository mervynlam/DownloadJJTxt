package com.mervynlam.downloadnovel.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CookiesUtils {

    private static Map<String, String> cookies = null;

    private static final String COOKIES_FILE_NAME = "cookies.txt";

    private static final String MY_FAV_URL = "https://wap.jjwxc.net/my/fav?r=1";

    public static boolean checkCookies() {
        System.out.println("-------------------------------------");
        System.out.println("检查cookies状态");
        Map<String, String> cookies = getCookiesFromFile();
        if (cookies == null) {
            System.out.println("无已存储cookies");
            return false;
        }
        try {
            Document doc = Jsoup.connect(MY_FAV_URL)
                    .userAgent(ApiUtils.USER_AGENT)
                    .cookies(cookies)
                    .get();
            Elements elem = doc.select(".b.module li");
            String text = elem.eq(0).first().text();
            if (text.contains("邮箱")) {
                System.out.println("cookies失效");
                return false;
            } else {
                System.out.println("cookies有效");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, String> getCookiesFromFile() {
        System.out.println("-------------------------------------");
        System.out.println("读取cookies");
        if (cookies != null) {
            return cookies;
        }
        File cookiesFile = new File(COOKIES_FILE_NAME);
        if (!cookiesFile.exists()) {
            return null;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(COOKIES_FILE_NAME)));) {
            String line = null;
            StringBuilder cookiesSb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                cookiesSb.append(line);
            }
            JSONObject json = JSONObject.parseObject(cookiesSb.toString());
            cookies = JSONObject.toJavaObject(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取cookies失败");
        }
        return cookies;
    }

    public static void setCookiesToFile(Map<String, String> cookiesMap) {
        System.out.println("-------------------------------------");
        System.out.println("保存cookies");

        if (cookies != null) {
            cookies.clear();
        } else {
            cookies = new HashMap<String, String>();
        }
        cookies.putAll(cookiesMap);

        Map<String, Object> mapObject = new HashMap<String, Object>();
        mapObject.putAll(cookiesMap);
        JSONObject json = new JSONObject(mapObject);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(COOKIES_FILE_NAME)));) {
            bufferedWriter.write(json.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("保存cookies失败");
        }
    }

    public static Map<String, String> generateCookies(String cookiesStr) {
        Map<String, String> cookiesMap = new HashMap<String, String>();
        String[] mapStr = cookiesStr.split(";");
        for (String str : mapStr) {
            String[] cookiesKV = str.split("=");
            cookiesMap.put(cookiesKV[0].trim(), cookiesKV[1].trim());
        }
        return cookiesMap;
    }
}
