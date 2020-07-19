package com.mervynlam.downloadnovel.utils;

public class ApiUtils {
    public static final String BOOK_URL = "http://android.jjwxc.net/androidapi/novelbasicinfo?novelId=";
    public static final String TOC_URL = "http://app-cdn.jjwxc.net/androidapi/chapterList?more=0&whole=1&novelId=";
    public static final String CHAPTER_URL_NORMAL = "https://wap.jjwxc.net/book2/";
    public static final String CHAPTER_URL_VIP = "https://wap.jjwxc.net/vip/";
    public final static String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E230 Safari/601.1";

    public static String getChapterUrl(String novelId, String chapterId, Integer isvip) {
        if (0 == isvip) {
            return CHAPTER_URL_NORMAL + novelId + "/" + chapterId + "_" + isvip;
        } else{
            return CHAPTER_URL_VIP + novelId + "/" + chapterId + "_" + isvip;
        }
    }
}
