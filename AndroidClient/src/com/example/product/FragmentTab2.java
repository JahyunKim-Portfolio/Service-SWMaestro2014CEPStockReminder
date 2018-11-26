package com.example.product;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

public class FragmentTab2 extends Fragment{
	Context mContext;
	ExpandableListView lv;
	ArrayList<Option> option = new ArrayList<Option>();
	ArrayList<OptionData> data = new ArrayList<OptionData>();
	ArrayList<predefined_condition_type> pred_cond_list = new ArrayList<predefined_condition_type>();
	ArrayList<condition_type> cond_list = new ArrayList<condition_type>();
	
	
	private Menu myMenu;
	
	
	public FragmentTab2(Context context){
		mContext = context;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_by_option, container, false);
        
        option.clear();
        
		lv = (ExpandableListView)rootView.findViewById(R.id.optionList);
		final OptionExpandableCustomAdapter optionCostum = new OptionExpandableCustomAdapter(container.getContext(),option,data);
		lv.setAdapter(optionCostum);
		
        
        // optionIntroduct = new makeOption();
        // makeoption_activity(include layout) 생성
        // 아래 과정들 전부 삭제
		option.add(new Option("성장주","origin_01","장주는 기업의 수익구조가 지속적으로 향상되고 있어 장기간에 걸쳐 주가가 큰 폭으로 상승하고 있는 주식입니다.","매출액 증가율\n\t최근 3년 평균 증감률 50% 이상\n매출액\n\t최근 결산 400억원 이상",0));
		option.add(new Option("골든 크로스","origin_01","장주는 기업의 수익구조가 지속적으로 향상되고 있어 장기간에 걸쳐 주가가 큰 폭으로 상승하고 있는 주식입니다.","매출액 증가율\n\t최근 3년 평균 증감률 50% 이상\n매출액\n\t최근 결산 400억원 이상",1));
		for(int i=0;i<option.size();i++){
			String[] str = new String[4];
			for(int j=0;j<2;j++){
				str[j*2]="입력 값" + (char)('A'+j*2);
				str[j*2+1]="입력 값" + (char)('A'+j*2+1);
			}
			data.add(new OptionData(str));
		}
		optionCostum.notifyDataSetChanged();
		
		FragmentManager fm = getFragmentManager();
		BottomMenu.BottomMenu_3 bottom_menu;
		if((bottom_menu = (BottomMenu.BottomMenu_3)fm.findFragmentById(R.id.bottom_menu)) == null) {
			bottom_menu = new BottomMenu.BottomMenu_3();
			fm.beginTransaction().add(R.id.bottom_menu, bottom_menu, "bottom_menu").commit();
		}

		//클릭 리스너 등록		
		Button.OnClickListener bt_listener = new Button.OnClickListener() {						
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.cancle_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_all();
					my_adapter.notifyDataSetChanged();
					*/
					
					//Toast.makeText(getActivity(), "cancle_btn clicked", 0).show();
					
					
					getActivity().finish();
					break;
				case R.id.admit_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_total();
					my_adapter.notifyDataSetChanged();
					*/
					
					//Toast.makeText(getActivity(), "admit_btn clicked", 0).show();
					Intent intent = new Intent(getActivity(), ActivitySearchResult.class);
					
					
					
					//데이터 넣기
					
					
					startActivityForResult(intent, 1);
					
					
					break;
				default :
					//do nothing
					break;
				}
			}		
		};
		bottom_menu.setButtonClickListener(bt_listener);
        return rootView;
    }
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	/*
	@Override
	public void onDestroy() {
		myMenu.clear();	
		super.onDestroy();				
	}
	*/
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragmenttab2_menu, menu);
		myMenu=menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_add :
				Intent intent = new Intent(getActivity(), ActivityCondition.class);
				
				startActivityForResult(intent, 0);
	
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	//어플 내에서 FragmentTab2 가 result를 받는 경우는 한가지 이다.
	//하지만 호출된 activity가 setresult를 하지 않아도 아래 함수가 호출되므로
	//Case를 나눠서 오류가 없게끔 한다.
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent) {
		//do something
		//조건 값을 받아와서 option으로 바꿔서 화면에 표시해준다.
		if(requestCode == 0) {
			if(resultCode == Activity.RESULT_CANCELED) {
				
			}
			else if(resultCode == Activity.RESULT_OK) {
				//Toast.makeText(getActivity(), "RESULT_OK", 0).show();
				
				//Intent로 부터 데이터 받아오기
				//기본조건일때
				if(intent.getStringExtra("type").equals("easy")) {
					
					//type = 0;
					String data_string = intent.getStringExtra("predefined_condition_type");
							
					Gson gson = new Gson();
					predefined_condition_type pred_cond_data = gson.fromJson(data_string, predefined_condition_type.class);
					
					pred_cond_list.add(pred_cond_data);
					
					
					
					//////////////////////////////////////
					////////////////////////////////////////
					// 들어온 데이터를 Option으로 변환하는 곳
					///////////////////////////////////////
					////////////////////////////////////
					
						
				}
				//고급조건일때
				else if(intent.getStringExtra("type").equals("hard")) {
					//type = 1;
					String data_string = intent.getStringExtra("condition_type");
					
					Gson gson = new Gson();
					condition_type cond_data = gson.fromJson(data_string, condition_type.class);
					
					cond_list.add(cond_data);
					
					
					
					//////////////////////////////////////
					////////////////////////////////////////
					// 들어온 데이터를 Option으로 변환하는 곳
					///////////////////////////////////////
					////////////////////////////////////
				}
				
				
			}
		}
		if(requestCode == 1) {
			if(resultCode == Activity.RESULT_OK) {
				getActivity().finish();
			}
		}
	}
}
