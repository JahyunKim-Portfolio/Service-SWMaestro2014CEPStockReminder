package com.example.product;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

public class ActivityStockDetailSettings extends Activity{
	ArrayList<user_signal_condition> list_total;
	ArrayList<user_signal_condition> list_indiv;
	
	public void myNotifyDataSetChanged_total() {
		// 이 코드를 데이터 셋이 바뀔 때마다 수행		
		LinearLayout total_linear = (LinearLayout)findViewById(R.id.linear_1);
		
		total_linear.removeAllViews();
		
		for(int i=0; i<list_total.size(); ++i) {
			final user_signal_condition data = list_total.get(i);
			RelativeLayout layout;
			layout = (RelativeLayout)View.inflate(this, R.layout.activity_stock_detail_settings_item, null);
			total_linear.addView(layout);
			
			TextView cond_name = (TextView)layout.findViewById(R.id.signal);
			ImageView image = (ImageView)layout.findViewById(R.id.image);
			View cond_type = (View)layout.findViewById(R.id.type_color);
			ImageButton alarm_bt = (ImageButton)layout.findViewById(R.id.alarm_bt);
			ImageButton delete_bt = (ImageButton)layout.findViewById(R.id.delete_bt);
		
			cond_name.setText(data.name);
			/* cond_name 에 따라서 이미지를 변경하는 코드를 작성할 것. */
		
		
			//이미지 아래 VIew 색을 변경
			if(data.level == Flag.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.level == Flag.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			else if(data.level == Flag.CUSTOM) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_custom));
			}
		
			//change alarm_bt image
			if(data.alarm == Flag.IS_ALARM){
				alarm_bt.setImageResource(R.drawable.push_alarm_clicked);
			}
			else if(data.alarm == Flag.IS_NOT_ALARM){
				alarm_bt.setImageResource(R.drawable.push_alarm);
			}
			
			//리스너 등록은 if ( v== null) 에서 하는게 아니라 밖에서 함.
			alarm_bt.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					/* 알람 버튼이 눌리면 이미지를 바꾸고, 서버에 알람버튼을 눌렀다고 알림 */
					if(data.alarm == Flag.IS_ALARM){
						data.alarm = Flag.IS_NOT_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm);
					}
					else if(data.alarm == Flag.IS_NOT_ALARM){
						data.alarm = Flag.IS_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm_clicked);
					}
					MyDataBase.putAlarmChange(data.id, data.alarm);
					
					// 서버에 알리는 코드를 작성
				}						
			});	

			
			final int position = i;
		

			delete_bt.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {							
					//Toast.makeText(ActivityStockDetailSettings.this, ""+position+" 번째 아이템을 삭제", 0).show();						
					user_signal_condition data = list_total.get(position);
					MyDataBase.deleteCondition(data.id);
					
					list_total.remove(position);
					myNotifyDataSetChanged_total();
					// 삭제 헀다는 정보를 서버에 보내야 함 
				}
			});
		}		
	}
	
	
	
	
	
	public void myNotifyDataSetChanged_indiv() {
		// 이 코드를 데이터 셋이 바뀔 때마다 수행		

		LinearLayout indiv_linear = (LinearLayout)findViewById(R.id.linear_2);	
		indiv_linear.removeAllViews();
		
		for(int i=0; i<list_indiv.size(); ++i) {
			final user_signal_condition data = list_indiv.get(i);
			RelativeLayout layout;
			layout = (RelativeLayout)View.inflate(this, R.layout.activity_stock_detail_settings_item, null);	
			indiv_linear.addView(layout);

			TextView cond_name = (TextView)layout.findViewById(R.id.signal);
			ImageView image = (ImageView)layout.findViewById(R.id.image);
			View cond_type = (View)layout.findViewById(R.id.type_color);
			ImageButton alarm_bt = (ImageButton)layout.findViewById(R.id.alarm_bt);
			ImageButton delete_bt = (ImageButton)layout.findViewById(R.id.delete_bt);
		
			cond_name.setText(data.name);
			/* cond_name 에 따라서 이미지를 변경하는 코드를 작성할 것. */
		
		
			//이미지 아래 VIew 색을 변경
			if(data.level == Flag.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.level == Flag.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			else if(data.level == Flag.CUSTOM) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_custom));
			}
		
			//change alarm_bt image
			if(data.alarm == Flag.IS_ALARM){
				alarm_bt.setImageResource(R.drawable.push_alarm_clicked);
			}
			else if(data.alarm == Flag.IS_NOT_ALARM){
				alarm_bt.setImageResource(R.drawable.push_alarm);
			}
			
			//리스너 등록은 if ( v== null) 에서 하는게 아니라 밖에서 함.
			alarm_bt.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					/* 알람 버튼이 눌리면 이미지를 바꾸고, 서버에 알람버튼을 눌렀다고 알림 */
					if(data.alarm == Flag.IS_ALARM){
						data.alarm = Flag.IS_NOT_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm);
					}
					else if(data.alarm == Flag.IS_NOT_ALARM){
						data.alarm = Flag.IS_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm_clicked);
					}
					
					// 서버에 알리는 코드를 작성
					MyDataBase.putAlarmChange(data.id, data.alarm);
				}						
			});	

			
			final int position = i;
		

			delete_bt.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {							
					//Toast.makeText(ActivityStockDetailSettings.this, ""+position+" 번째 아이템을 삭제", 0).show();						
					
					user_signal_condition data = list_indiv.get(position);
					MyDataBase.deleteCondition(data.id);
					
					list_indiv.remove(position);
					myNotifyDataSetChanged_indiv();
					// 삭제 헀다는 정보를 서버에 보내야 함 
				}
			});
		}		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_detail_settings);
		
		
		//타이틀 설정
		Intent intent = getIntent();
		String stock_detail_json_string = intent.getStringExtra("stock_detail");
		
		Gson gson = new Gson();
		stock_detail stock_detail = gson.fromJson(stock_detail_json_string, stock_detail.class);
		
		if(stock_detail.stock_item_name != null) {
			setTitle(stock_detail.stock_item_name);			
		}
		
		//리스트 초기화
		if(list_total == null) {
			list_total = new ArrayList<user_signal_condition>();
		}
		else {
			list_total.clear();
		}
		//리스트 초기화
		if(list_indiv == null) {
			list_indiv = new ArrayList<user_signal_condition>();
		}
		else {
			list_indiv.clear();
		}
		
		// list 값을 업데이트 (전체)
		if(MyDataBase.my_condition_total == null) {		
			//전체 신호를 업데이트
			MyDataBase.getSignal_conditions(	
					new Response.Listener<signal_conditions>() {
						@Override
						public void onResponse(signal_conditions response) {
							list_total.addAll(response.signal_conditions);
							MyDataBase.my_condition_total = response.signal_conditions;
							myNotifyDataSetChanged_total();
						}
					}, 
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("product", "error!" + error.getMessage());
						}
					});
		}
		else {
			list_total.addAll(MyDataBase.my_condition_total);
		}
		// list 값을 업데이트 (개별)
		list_indiv.addAll(stock_detail.user_signal_conditions);
		myNotifyDataSetChanged_indiv();

		/*
		//테스트 코드 
		if(list == null) {
			//서버로부터 데이터를 받아옴 
			list = new ArrayList<SettedCond>();
			list.add(new SettedCond());
			list.add(new SettedCond());
			list.add(new SettedCond());
			for(int i=0; i<3; ++i) {
				SettedCond data = new SettedCond();
				data.signal_type = SettedCond.INDIV;
				list.add(data);
			}
		}
		 */
		
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_stock_detail_settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	
		int id = item.getItemId();
		if (id == R.id.action_add) {			
			// 추가함
			Intent intent = new Intent(this, ActivityCondition.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}