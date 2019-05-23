package com.lsh.po;

import java.sql.*;
import java.util.*;
public class NewS {

	private String UserName;
	private String Corporate_name;
	private String Text;
	private java.sql.Timestamp time;
	private String title;
	private Integer news_ID;
	private String describes;


	public String getUserName(){
		return UserName;
	}
	public String getCorporate_name(){
		return Corporate_name;
	}
	public String getText(){
		return Text;
	}
	public java.sql.Timestamp getTime(){
		return time;
	}
	public String getTitle(){
		return title;
	}
	public Integer getNews_ID(){
		return news_ID;
	}
	public String getDescribes(){
		return describes;
	}
	public void setUserName(String UserName){
		this.UserName=UserName;
	}
	public void setCorporate_name(String Corporate_name){
		this.Corporate_name=Corporate_name;
	}
	public void setText(String Text){
		this.Text=Text;
	}
	public void setTime(java.sql.Timestamp time){
		this.time=time;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setNews_ID(Integer news_ID){
		this.news_ID=news_ID;
	}
	public void setDescribes(String describes){
		this.describes=describes;
	}
}
