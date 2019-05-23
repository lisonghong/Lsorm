package com.lsh.po;

import java.sql.*;
import java.util.*;
public class Corporate_name {

	private Integer Corporate_ID;
	private String Name;


	public Integer getCorporate_ID(){
		return Corporate_ID;
	}
	public String getName(){
		return Name;
	}
	public void setCorporate_ID(Integer Corporate_ID){
		this.Corporate_ID=Corporate_ID;
	}
	public void setName(String Name){
		this.Name=Name;
	}
}
