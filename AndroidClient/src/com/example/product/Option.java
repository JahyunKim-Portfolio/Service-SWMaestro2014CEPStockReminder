package com.example.product;

import java.util.ArrayList;

public class Option {
	String name, body;
	String detail;
	String introduct;
	boolean status;
	int type;
	
	public Option(String names, String bodies, String introducts, String details, int types){
		detail = details;
		name = names;
		body = bodies;
		introduct = introducts;
		type = types;
		status = false;
	}
	
	public Option(String introducts, int types){
		detail = new String();
		name = new String();
		body = new String();
		detail = new String();
		introduct = introducts;
		type = types;
		status = false;
	}
	
	public Option(String names, String bodies, String introducts, int types){   // 이름, 조건명, 설명, 일반 혹은 상세
		detail = new String();
		name = names;
		body = bodies;
		introduct = introducts;
		type = types;
		status = false;
	}
	
	public Boolean getStatus(){
		return status;
	}
	
	public void setStatus(Boolean type){
		status = type;
	}
	
	public String getName(){
		return name;
	}
	public String getbody(){
		return body;
	}
	public String getIntroduct(){
		return introduct;
	}
	public String getDetail(){
		return detail;
	}
	public int getType(){
		return type;
	}

}
