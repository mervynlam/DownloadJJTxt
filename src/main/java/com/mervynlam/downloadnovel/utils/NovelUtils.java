package com.mervynlam.downloadnovel.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONPath;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mervynlam.downloadnovel.entity.Chapter;
import com.mervynlam.downloadnovel.entity.Novel;
import org.jsoup.select.Elements;

public class NovelUtils {
	
	private final static String FILE_ENCODING = "UTF-8";	//文件编码
	private static Map<String, String> cookies = null;

	public static Novel getNovelInfo(Novel novel) {
		getCookies();
		System.out.println("-------------------------------------");
		System.out.println("正在获取文章信息");
		String bookUrl = novel.getUrl();
		try {
			Response response = Jsoup.connect(bookUrl)
					.timeout(5000)
					.cookies(cookies)
					.userAgent(ApiUtils.USER_AGENT).execute();
			String json = response.body();
			String novelName = (String) JSONPath.read(json, "$.novelName");
			String authorName = (String) JSONPath.read(json, "$.authorName");
			String novelCover = (String) JSONPath.read(json, "$.novelCover");
			String novelIntro = (String) JSONPath.read(json, "$.novelIntro");
			novel.setTitle(novelName);
			novel.setAuthor(authorName);
			novel.setCoverUrl(novelCover);
			novel.setIntro(StringUtils.replaceAll(StringEscapeUtils.unescapeHtml4(novelIntro)));
		} catch (IOException e1) {
			System.out.println("获取文章信息失败，url:"+bookUrl);
			e1.printStackTrace();
		}
		System.out.println("-------------------------------------");
		System.out.println("正在获取章节信息");
		String tocUrl = novel.getTocUrl();
		try {
			Response response = Jsoup.connect(tocUrl)
					.timeout(5000)
					.cookies(cookies)
					.userAgent(ApiUtils.USER_AGENT).execute();
			String json = response.body();
			List<Map<String, Object>> chapterListMap = (List<Map<String, Object>>) JSONPath.read(json, "$.chapterlist");
			List<Chapter> chapterList = new ArrayList<Chapter>();
			for (Map<String, Object> map : chapterListMap) {
				String chapterid = (String) map.get("chapterid");
				String chaptername = (String) map.get("chaptername");
				Integer isvip = (Integer) map.get("isvip");
				String url = ApiUtils.getChapterUrl(novel.getNovelId(), chapterid, isvip);
				Chapter chapter = new Chapter(chapterid, chaptername, url, isvip);
				String content = getChapterContent(chapter);
				chapter.setContent(content);
				chapterList.add(chapter);
			}
			novel.setChapters(chapterList);
		} catch (IOException e1) {
			System.out.println("获取章节失败，url:"+tocUrl);
			e1.printStackTrace();
		}
		return novel;
	}
	
	public static String getChapterContent(Chapter chapter) {
		getCookies();
		System.out.println("正在获取第"+chapter.getChapterNum()+"章 "+chapter.getChapterTitle());
		Document doc = null;
		String content = null;
		try {
			doc = Jsoup.connect(chapter.getUrl())
					.timeout(5000)
					.cookies(cookies)
					.userAgent(ApiUtils.USER_AGENT)
					.get();
			Elements elem = doc.select(".b.module li");
			String text1 = StringUtils.replaceAll(StringEscapeUtils.unescapeHtml4(elem.eq(0).first().html()));
			String text2 = StringUtils.replaceAll(StringEscapeUtils.unescapeHtml4(elem.eq(1).first().html()));
			if (text1.contains("作者有话要说")) {
				content = text2 + "\n";
				content += text1 + "\n";
			} else{
				content = text1 + "\n";
				if (text2.contains("作者有话要说")) {
					content += text2 + "\n";
				}
			}
		} catch (IOException e) {
			System.out.println("章节获取失败");
			e.printStackTrace();
		} catch (NullPointerException e) {
			return "未购买";
		}
		return content;
	}
	
	public static void outputFile(Novel novel) {
		System.out.println("-------------------------------------");
		System.out.println("输出文件");
		//文件名：作品名 - 作者.txt
		String fileName = novel.getTitle() + " - " + novel.getAuthor() + ".txt";
		File file = new File(fileName);
		//文件不存在则创建文件
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//输出到文件
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), FILE_ENCODING));

			System.out.println("正在输出文案");
			bw.write("文案\n");
			bw.write(novel.getIntro());
			bw.newLine();
			bw.newLine();

			List<Chapter> chapterList = novel.getChapters();
			for (Chapter chapter : chapterList) {
				System.out.println("正在输出第"+chapter.getChapterNum()+"章 "+chapter.getChapterTitle());
				bw.write("#第"+chapter.getChapterNum()+"章 "+ chapter.getChapterTitle() +"\n");
				if (chapter.getContent() != null) {
					bw.write(chapter.getContent());
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch(Exception e) {
			System.out.println("输出失败");
			e.printStackTrace();
		}
		System.out.println("输出完成");
		System.out.println("-------------------------------------");
	}
	
	public static void downloadCover(Novel novel) {
		System.out.println("-------------------------------------");
		System.out.println("正在下载封面");
		try {
			Response response = Jsoup.connect(novel.getCoverUrl()).ignoreContentType(true).execute();
			String format = novel.getCoverUrl().substring(novel.getCoverUrl().lastIndexOf("."));
			String fileName = novel.getTitle() + " - " + novel.getAuthor() + ".jpg";
			File file = new File(fileName);
			//文件不存在则创建文件
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(response.bodyAsBytes());
			out.close();
			System.out.println("封面下载完成");
		} catch(Exception e) {
			System.out.println("封面下载失败，url:"+novel.getCoverUrl());
			e.printStackTrace();
		}
	}

	private static Map<String, String> getCookies() {
		if(cookies == null) {
			cookies = CookiesUtils.getCookiesFromFile();
		}
		return cookies;
	}
}
