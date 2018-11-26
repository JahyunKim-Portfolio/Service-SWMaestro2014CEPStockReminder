package com.example.product;

public class OptionData {
	String[] parameter;
	public OptionData(String[] params) {
		parameter = params;
		// TODO Auto-generated constructor stub
	}
	public String getParam(int x){
		return parameter[x];
	}
	public void setParam(int x,String str){
		parameter[x] = str;
	}
}
