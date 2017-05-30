package com.mobileguard.domain;

import java.util.Date;

public class Comment{
	private String commId;
	private String commContent;
	private long commTime;
	private int praiseTime;
	public Comment(){}
	public Comment(String commId, String commContent, long commTime, int praiseTime) {
		this.commId = commId;
		this.commContent = commContent;
		this.commTime = commTime;
		this.praiseTime = praiseTime;
	}
	public String getCommId() {
		return commId;
	}
	public void setCommId(String commId) {
		this.commId = commId;
	}
	public String getCommContent() {
		return commContent;
	}
	public void setCommContent(String commContent) {
		this.commContent = commContent;
	}
	public long getCommTime() {
		return commTime;
	}
	public void setCommTime(long commTime) {
		this.commTime = commTime;
	}
	public int getPraiseTime() {
		return praiseTime;
	}
	public void setPraiseTime(int praiseTime) {
		this.praiseTime = praiseTime;
	}
	@Override
	public String toString() {
		return "Comment [commId=" + commId + ", commContent=" + commContent + ", commTime=" + commTime + ", praiseTime="
				+ praiseTime + "]";
	}	
}
