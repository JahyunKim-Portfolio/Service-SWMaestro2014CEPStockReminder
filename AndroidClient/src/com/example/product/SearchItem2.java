package com.example.product;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.viewpagerindicator.TabPageIndicator;

public class SearchItem2 extends FragmentActivity{
	
	static Activity activity;
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);
		
		activity = this;
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);		

        TabPageIndicator mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);	
	}
	
	
	/*********************************************************************************************************************
	//
//					뷰페이저 어댑터
	//
	*********************************************************************************************************************/
		private class MyPagerAdapter extends FragmentStatePagerAdapter {
			private static final int NUM_PAGES = 2;
			private final FragmentManager fm;

			public MyPagerAdapter(FragmentManager fm) {
				super(fm);
				this.fm = fm;
			}
			@Override
			public int getCount() {
				return NUM_PAGES;
			}

			@Override
			public Fragment getItem(int position) {
				switch(position) {
					case 0 :
						return new FragmentTab1(getApplicationContext(), (Activity)SearchItem2.this);
					case 1 :
						return new FragmentTab2(getApplicationContext());		
					default :
						return null;
				}
			}
			
			@Override
			public CharSequence getPageTitle(int position) {
				switch(position) {
					case 0 :
						return "종목으로 검색";
					case 1 :
						return "조건으로 검색";
					default :
						return "";
				}
			}
			
		}
	
	
	
	
}
