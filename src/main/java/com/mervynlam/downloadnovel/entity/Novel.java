package com.mervynlam.downloadnovel.entity;

import java.util.List;

import com.mervynlam.downloadnovel.utils.ApiUtils;

public class Novel {
	String title;		//书名
	String author;		//作者
	List<Chapter> chapters;//章节
	String novelId;	//小说id
	String url;		//小说url
	String coverUrl;	//封面url
	String tocUrl;	//封面url
	String intro;		//文案

	public String getTocUrl() {
		return tocUrl;
	}

	public void setTocUrl(String tocUrl) {
		this.tocUrl = tocUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Novel(String novelId) {
		this.novelId = novelId;
		this.url = ApiUtils.BOOK_URL+novelId;
		this.tocUrl = ApiUtils.TOC_URL+novelId;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public String getNovelId() {
		return novelId;
	}

	public void setNovelId(String novelId) {
		this.novelId = novelId;
	}
	
}
