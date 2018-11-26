package com.example.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BottomMenu {
	
	// ALL, 전체, 개별, 알람 4개 메뉴가 있는 Fragment
	public static class BottomMenu_1 extends Fragment {
		Button.OnClickListener listener;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.bottom_menu_1, container, false);
			
			if(listener != null) {
				((Button)root.findViewById(R.id.all_btn)).setOnClickListener(listener);
				((Button)root.findViewById(R.id.total_btn)).setOnClickListener(listener);
				((Button)root.findViewById(R.id.indiv_btn)).setOnClickListener(listener);
				((Button)root.findViewById(R.id.alarm_btn)).setOnClickListener(listener);
			}
			return root;			
		}
		
		public void setButtonClickListener(Button.OnClickListener listener) {
			this.listener = listener;			
		}
		
	}
	
	
	//ALL, 알람 2개 버튼만 있음
	public static class BottomMenu_2 extends Fragment {
		
		Button.OnClickListener listener;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.bottom_menu_2, container, false);
			
			if(listener != null) {
				((Button)root.findViewById(R.id.all_btn)).setOnClickListener(listener);
				((Button)root.findViewById(R.id.alarm_btn)).setOnClickListener(listener);
			}
			
			return root;			
		}
		
		
		
		public void setButtonClickListener(Button.OnClickListener listener) {
			this.listener = listener;			
		}
	}
	
	//ALL, 알람 2개 버튼만 있음
	public static class BottomMenu_3 extends Fragment {
		
		Button.OnClickListener listener;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.bottom_menu_3, container, false);
			
			if(listener != null) {
				((Button)root.findViewById(R.id.cancle_btn)).setOnClickListener(listener);
				((Button)root.findViewById(R.id.admit_btn)).setOnClickListener(listener);
			}
			
			return root;			
		}
		
		
		
		public void setButtonClickListener(Button.OnClickListener listener) {
			this.listener = listener;			
		}
	}

}
