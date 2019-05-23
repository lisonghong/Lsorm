package com.lsh.po;

import java.sql.*;
import java.util.*;
public class UserS {

	private String UserPwd;
	private String UserName;
	private String Corporate_name;


	public String getUserPwd(){
		return UserPwd;
	}
	public String getUserName(){
		return UserName;
	}
	public String getCorporate_name(){
		return Corporate_name;
	}
	public void setUserPwd(String UserPwd){
		this.UserPwd=UserPwd;
	}
	public void setUserName(String UserName){
		this.UserName=UserName;
	}
	public void setCorporate_name(String Corporate_name){
		this.Corporate_name=Corporate_name;
	}
}
