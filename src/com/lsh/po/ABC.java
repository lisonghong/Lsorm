package com.lsh.po;

import java.sql.*;
import java.util.*;
public class ABC {

	private String name;
	private Double numa;
	private Integer id;


	public String getName(){
		return name;
	}
	public Double getNuma(){
		return numa;
	}
	public Integer getId(){
		return id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setNuma(Double numa){
		this.numa=numa;
	}
	public void setId(Integer id){
		this.id=id;
	}
}
