package com.mervynlam.downloadnovel.utils;

public class StringUtils {

    private final static String[] newLineRegex = {"<\\s*br\\s*/*\\s*>"};
    private final static String[] emptyStrRegex = {"@无限好文，尽在晋江文学城"
                                                        ,"<hr size=\\\"1\\\">"
                                                        ,"<input(.+?)>"};

    public static String replaceAll(String str) {
        for (String regex : newLineRegex) {
            str = str.replaceAll(regex, "\n");
        }
        for (String regex : emptyStrRegex) {
            str = str.replaceAll(regex, "");
        }
        return str;
    }
}
