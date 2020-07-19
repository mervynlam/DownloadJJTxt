package com.mervynlam.downloadnovel.core;

import java.util.Map;
import java.util.Scanner;

import com.mervynlam.downloadnovel.entity.Novel;
import com.mervynlam.downloadnovel.utils.CookiesUtils;
import com.mervynlam.downloadnovel.utils.NovelUtils;

public class App {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
	    //检查cookies
        boolean cookiesStatus = CookiesUtils.checkCookies();
        if (!cookiesStatus) {
			System.out.println("请输入cookies字符串");
			String cookiesStr = sc.nextLine();
			Map<String, String> cookiesMap = CookiesUtils.generateCookies(cookiesStr);
			CookiesUtils.setCookiesToFile(cookiesMap);
			cookiesStatus = CookiesUtils.checkCookies();
			if (!cookiesStatus)
				return;
        }

        //输入小说id
		System.out.println("请输入小说id");
		String novelId = sc.next();

		//小说实体
		Novel novel = new Novel(novelId);
		novel = NovelUtils.getNovelInfo(novel);
		NovelUtils.outputFile(novel);
		NovelUtils.downloadCover(novel);
	}

}
