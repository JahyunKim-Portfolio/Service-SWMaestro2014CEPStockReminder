package com.example.product;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product.R;

public class SearchDetail extends FragmentActivity {
	ExpandableListView lv;
	TextView searchName;
	EditText txtOptionGroupName;
	ArrayList<Option> option = new ArrayList<Option>();
	ArrayList<OptionData> data = new ArrayList<OptionData>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_detail);
		txtOptionGroupName = (EditText)findViewById(R.id.txtOptionGroupName);
		searchName = (TextView) findViewById(R.id.searchName);
        
		lv = (ExpandableListView)findViewById(R.id.optionList);
		final OptionExpandableCustomAdapter optionCostum = new OptionExpandableCustomAdapter(this,option,data);
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
		searchName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchDetail.this, SearchByEvent.class);
				startActivityForResult(intent,1);
				// TODO Auto-generated method stub
			}
		});
		
		FragmentManager fm = getSupportFragmentManager();
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
					
					//Toast.makeText(SearchDetail.this, "cancle_btn clicked", 0).show();
					
					finish();
					break;
				case R.id.admit_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_total();
					my_adapter.notifyDataSetChanged();
					*/
					
					//Toast.makeText(SearchDetail.this, "admit_btn clicked", 0).show();
					
					
					/*
					//서버에 신호 추가 요청
					post_condition_factor factor = new post_condition_factor();
	
					factor.applicable_range = 0;
					factor.stock_item_id = 0;
					factor.search_conditions = new ArrayList<search_condition>();
					for(int i=0; i<cond_list.size(); ++i) {
						condition_type data = cond_list.get(i);
						search_condition sc = new search_condition();
						sc.condition_type_id = data.id;
						sc.search_condition_parameters = new ArrayList<search_condition_parameter>();
						for(int j=0; j<data.parameter_types.size(); ++j) {
							condition_parameter_type dd = data.parameter_types.get(j);
							search_condition_parameter param = new search_condition_parameter();
							param.order = dd.order;
							param.type = dd.type; 
							param.value = dd.initial_value;
							sc.search_condition_parameters.add(param);
						}
						factor.search_conditions.add(sc);
					}
					factor.search_predefined_conditions = new ArrayList<search_predefined_condition>();
					for(int i=0; i<pred_cond_list.size(); ++i) {
						predefined_condition_type data = pred_cond_list.get(i);
						search_predefined_condition spc = new search_predefined_condition();
						spc.predefined_condition_type_id = data.id;
						factor.search_predefined_conditions.add(spc);
					}
					*/
					
					
					
					
					
					break;
				default :
					//do nothing
					break;
				}
			}		
		};
		bottom_menu.setButtonClickListener(bt_listener);
		
		//for(int i=0;i<optionCostum.getGroupCount();i++) lv.expandGroup(i);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, requestCode, data);
		if(requestCode==1 && resultCode==RESULT_OK){
			searchName.setText(data.getStringExtra("stock_name"));
		}
	}
}
