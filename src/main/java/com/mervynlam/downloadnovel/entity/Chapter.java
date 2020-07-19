package com.mervynlam.downloadnovel.entity;

public class Chapter {
	String chapterNum;		//章节号
	String chapterTitle;		//章节名
	String url;	//章节地址
	boolean isVip;	//是否vip
	String content;	//小说内容

	public Chapter(String chapterNum, String chapterTitle, String url, Integer isVip) {
		this.chapterNum = chapterNum;
		this.chapterTitle = chapterTitle;
		this.url = url;
		if (0 == isVip) {
			this.isVip = false;
		} else {
			this.isVip = true;
		}
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getChapterNum() {
		return chapterNum;
	}
	public void setChapterNum(String chapterNum) {
		this.chapterNum = chapterNum;
	}
	public String getChapterTitle() {
		return chapterTitle;
	}
	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isVip() {
		return isVip;
	}
	public void setVip(Integer isVip) {
		if (0 == isVip) {
			this.isVip = false;
		} else {
			this.isVip = true;
		}
	}
}
