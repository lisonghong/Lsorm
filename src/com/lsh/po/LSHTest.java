package com.lsh.po;

import java.sql.*;
import java.util.*;
public class LSHTest {

	private String UserPwd;
	private Integer UserID;


	public String getUserPwd(){
		return UserPwd;
	}
	public Integer getUserID(){
		return UserID;
	}
	public void setUserPwd(String UserPwd){
		this.UserPwd=UserPwd;
	}
	public void setUserID(Integer UserID){
		this.UserID=UserID;
	}
}
