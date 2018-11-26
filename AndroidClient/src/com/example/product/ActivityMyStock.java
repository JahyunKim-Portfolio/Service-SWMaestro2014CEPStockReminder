package com.example.product;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ActivityMyStock extends FragmentActivity{
	static ArrayList<signal_results_of_stock_item> list_all;
	static ArrayList<signal_results_of_stock_item> list_alarm;
	
	static MyExpAdapter adapter_all;
	static MyExpAdapter adapter_alarm;
	static MyExpAdapter current_adapter;
	
	
	private ExpandableListView mExpListView;	
	FragmentManager fm; //onCreate에서 초기화

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_expandablelistview);
		
		mExpListView = (ExpandableListView)findViewById(R.id.explist);
		
		/* 임시로 데이터 만들기 */
		/*
		for(int i=0; i<3; ++i) {

			SignalSortByStock data_t = new SignalSortByStock();

			data_t.total_cnt = 0;
			data_t.indiv_cnt = 12;
			data_t.stock_name = "소마제철소";
			data_t.price = "1,154,000";
			data_t.price_diff = "-98711";
			data_t.price_diff_percent="(-3.12%)";
			
			for(int j=0; j<3; ++j) {

				SignalOfStock stock = new SignalOfStock();
				stock.signal_type = SignalOfStock.INDIV;
				data_t.list.add(stock);

			}


			list.add(data_t);

	
		}
		*/
		
		if(list_all == null) {
			list_all = new ArrayList<signal_results_of_stock_item>();
			list_alarm = new ArrayList<signal_results_of_stock_item>();
		}
		
		adapter_all = new MyExpAdapter(this, list_all);
		adapter_alarm = new MyExpAdapter(this, list_alarm);
		
		mExpListView.setAdapter(adapter_all);
		current_adapter = adapter_all;
		
		
		mExpListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				/*
				TextView signal = (TextView)v.findViewById(R.id.signal);
				TextView signal_type = (TextView)v.findViewById(R.id.signal_type);
				
				Toast.makeText(getActivity(), "chlid position = " + childPosition +"\n"+
													"signal Top = " + signal.getTop() + "\n" +
													"signal Bottom = " + signal.getBottom() + "\n" +
													"signal Baseline = " + signal.getBaseline() + "\n" +
													"signal_type Top = " + signal_type.getTop() + "\n" +
													"signal_type Bottom = " + signal_type.getBottom() + "\n" +
													"signal_type Baseline = " + signal_type.getBaseline() + "\n", Toast.LENGTH_LONG).show();
				*/
				
				Intent intent = new Intent(ActivityMyStock.this, ActivitySignalDetail.class);
				
				//인텐트에 클릭된 신호의 주식이름 정보를 전달
				String signal_name = (String)((TextView)v.findViewById(R.id.signal)).getText();
				intent.putExtra("signal_name", signal_name);
				startActivity(intent);					
				
				return true;
			}
		});
		
		
		
		
		
		
		
		/*Bottom Menu 에 대한 버튼 클릭 리스너를 등록하자. */
		
		FragmentManager fm=getSupportFragmentManager();	
		BottomMenu.BottomMenu_2 bottom_menu = null;
		if(fm.findFragmentById(R.id.bottom_menu) == null) {
			bottom_menu = new BottomMenu.BottomMenu_2();
			fm.beginTransaction().add(R.id.bottom_menu, bottom_menu, "bottom_menu").commit();
		}

		//클릭 리스너 등록		
		Button.OnClickListener bt_listener = new Button.OnClickListener() {						
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.all_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_all();
					my_adapter.notifyDataSetChanged();
					*/
					
					mExpListView.setAdapter(adapter_all);
					current_adapter = adapter_all;
					break;
				case R.id.alarm_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_alarm();
					my_adapter.notifyDataSetChanged();
					*/
					
					mExpListView.setAdapter(adapter_alarm);
					current_adapter = adapter_alarm;
					break;
				default :
					//do nothing
					break;
				}
			}		
		};
		bottom_menu.setButtonClickListener(bt_listener);
	}
	
	
	@Override
	public void onResume() {
		// 각 리스트를 전부 업데이트
		MyDataBase.getMy_Stock(0,		 // group 0 means all		
				new Response.Listener<signal_results_of_stock_items>() {
					@Override
					public void onResponse(signal_results_of_stock_items response) {
						list_all.clear();
						list_all.addAll(response.signal_results_of_stock_items);
						if(current_adapter == adapter_all) {
							adapter_all.notifyDataSetChanged();				
						}
						// Call onRefreshComplete when the list has been refreshed.
					}
				}, 
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("product", "error!" + error.getMessage());
					}
				});

		MyDataBase.getMy_Stock(3,		 // group 3 means alarm		
				new Response.Listener<signal_results_of_stock_items>() {
					@Override
					public void onResponse(signal_results_of_stock_items response) {
						list_alarm.clear();
						list_alarm.addAll(response.signal_results_of_stock_items);
						if(current_adapter == adapter_alarm) {
							adapter_alarm.notifyDataSetChanged();				
						}
						// Call onRefreshComplete when the list has been refreshed.
					}
				}, 
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("product", "error!" + error.getMessage());
					}
				});
		
		super.onResume();
	}
	
	//FragmentSIgnal 에서 종목별 보여주는 부분의 어댑터와 동일하다.
	//일부 코드만 수정
	class MyExpAdapter extends BaseExpandableListAdapter {
		//private int GROUP_CNT = 3;
		//private int CHILD_CNT = 5;
		private int CHILD_CNT_MAX = 5;
		
		Context context;
		ArrayList<signal_results_of_stock_item> list;
		
		/* 뷰홀더 코드 */
		//private ViewHoler viewHolder = null;
		
		public MyExpAdapter(Context context, ArrayList<signal_results_of_stock_item> list) { //부모그룹이랑 차일드 그룹을 받아야 함.
			super();
			this.context = context;
			this.list=list;
		}
		
		@Override
		public String getGroup(int groupPosition) {
			return "Parent";
		}
		
		@Override
		public int getGroupCount() {
			return list.size();
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View v = convertView;
			GroupViewHolder viewHolder;
			if(v == null){
	            viewHolder = new GroupViewHolder();
	            v = View.inflate(ActivityMyStock.this, R.layout.activity_my_stock_item_parent, null);		            
	            
	            viewHolder.stock_name = (TextView) v.findViewById(R.id.stock_name);
	            viewHolder.total_cnt = (TextView)v.findViewById(R.id.total_cnt);         
	            viewHolder.indiv_cnt = (TextView)v.findViewById(R.id.indiv_cnt);
	            viewHolder.price = (TextView)v.findViewById(R.id.price);
	            viewHolder.price_diff = (TextView)v.findViewById(R.id.diff);
	            viewHolder.price_diff_percent = (TextView)v.findViewById(R.id.diff_percent);

	            v.setTag(viewHolder);
	        }else{
	            viewHolder = (GroupViewHolder)v.getTag();
	        }								
			signal_results_of_stock_item groupData = list.get(groupPosition);
			viewHolder.stock_name.setText(groupData.stock_item_name);
			viewHolder.total_cnt.setText(""+groupData.number_of_signals_to_all_stock_items);
			viewHolder.indiv_cnt.setText(""+groupData.number_of_signals_to_specific_stock_item);
			NumberFormat nf = NumberFormat.getNumberInstance();
			viewHolder.price.setText(nf.format(groupData.price));
			viewHolder.price_diff.setText(nf.format(groupData.price_gap_of_previous_closing_price));
			viewHolder.price_diff_percent.setText("( " + groupData.price_rate_of_previous_closing_price + "% )");
			
            /* button에 listener를 등록
             * v == null 일때 뷰 홀더를 설정하는 부분에서 코드를 넣을 수도 있다.
             * 위 방법이 더 효율적이지만 일단 아래와 같이 구현.
             *  
             *  */		

			ImageButton bt = (ImageButton)v.findViewById(R.id.go_bt);
			
			bt.setFocusable(false);
			bt.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {							
					Intent intent = new Intent(ActivityMyStock.this, ActivityStockDetail.class);					
					
					//인텐트에 클릭된 신호의 주식이름 정보를 전달
					signal_results_of_stock_item groupdata = list.get(groupPosition);			
					intent.putExtra("stock_name", groupdata.stock_item_name);						
					
					startActivity(intent);
				}
			});

			
			
			
			/* 전일 종가 대비 변동량 색 변경 */
			if(groupData.price_gap_of_previous_closing_price < 0) {
				viewHolder.price_diff.setTextColor(getResources().getColor(R.color.blue));
				viewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.blue));
			}
			else if(groupData.price_gap_of_previous_closing_price > 0) {
				viewHolder.price_diff.setTextColor(getResources().getColor(R.color.red));
				viewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.red));
			}
			else {
				viewHolder.price_diff.setTextColor(getResources().getColor(R.color.black));
				viewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.black));
			}
			

			// total_cnt 는 항상 안보이게 한다.
			viewHolder.total_cnt.setVisibility(View.GONE);
			
			/* 숫자가 0보다 클때만 보이게 */
			if(groupData.number_of_signals_to_specific_stock_item > 0 ) {
				viewHolder.indiv_cnt.setVisibility(View.VISIBLE);
			}
			else {
				viewHolder.indiv_cnt.setVisibility(View.GONE);
			}

			return v;
		}
		
		public class GroupViewHolder {			
			TextView stock_name;
			TextView total_cnt;
			TextView indiv_cnt;
			TextView price;
			TextView price_diff;
			TextView price_diff_percent;
		}
		
		@Override
		public String getChild(int groupPosition, int childPosition) {
			return "child";
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			if(list.get(groupPosition).signal_results.size() < CHILD_CNT_MAX) {
				return list.get(groupPosition).signal_results.size();
			}
			else {
				return CHILD_CNT_MAX;
			}
	    }
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		
		@Override
		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			View v = convertView;
			ChildViewHolder childViewHolder;

			if(v == null){
	            childViewHolder = new ChildViewHolder();
	            v = View.inflate(ActivityMyStock.this, R.layout.activity_my_stock_item_child, null);
	            childViewHolder.image = (ImageView)v.findViewById(R.id.image);
	            childViewHolder.cond_type = (View)v.findViewById(R.id.type_color);
	            childViewHolder.signal_name = (TextView)v.findViewById(R.id.signal);
	            childViewHolder.signal_type = (TextView)v.findViewById(R.id.signal_type);
	            childViewHolder.date = (TextView)v.findViewById(R.id.date);
	            childViewHolder.alarm_bt = (ImageButton)v.findViewById(R.id.alarm_bt);
	            v.setTag(childViewHolder);
	            
				ImageButton bt = (ImageButton)v.findViewById(R.id.alarm_bt);
				bt.setFocusable(false);
				/* 버튼 리스너 등록할 것 */
	        }else{
	            childViewHolder = (ChildViewHolder)v.getTag();
	        }
			
			signal_result data = list.get(groupPosition).signal_results.get(childPosition);
			
			//리스너 등록은 if ( v== null) 에서 하는게 아니라 밖에서 함.
			childViewHolder.alarm_bt.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					/* 알람 버튼이 눌리면 이미지를 바꾸고, 서버에 알람버튼을 눌렀다고 알림 */
					signal_result data = list.get(groupPosition).signal_results.get(childPosition);
					if(data.alarm == Flag.IS_ALARM){
						data.alarm = Flag.IS_NOT_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm);
					}
					else if(data.alarm == Flag.IS_NOT_ALARM){
						data.alarm = Flag.IS_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm_clicked);
					}
					MyDataBase.putAlarmChange(data.user_signal_condition_id, data.alarm);
					
					// 서버에 알리는 코드를 작성
				}						
			});	
			
			
			childViewHolder.signal_name.setText(data.user_signal_condition_name);
			/* 이미지 교체 작업 코드 작성할 것*/			
			
			
			
			//이미지 밑에 색을 변경
			if(data.level == Flag.EASY) {
				childViewHolder.cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.level == Flag.HARD) {
				childViewHolder.cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			else if(data.level == Flag.CUSTOM) {
				childViewHolder.cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_custom));
			}
			
			//전체/개별 태그를 설정
			// MyStock은 무조건 개별 이다. 차후 변경 가능
			/*
			if(data.signal_type == SignalOfStock.TOTAL) {
				childViewHolder.signal_type.setText("전체");
				childViewHolder.signal_type.setBackgroundColor(getResources().getColor(R.color.signal_type_total));
			}
			else if(data.signal_type == SignalOfStock.INDIV) {
				childViewHolder.signal_type.setText("개별");
				childViewHolder.signal_type.setBackgroundColor(getResources().getColor(R.color.signal_type_indiv));
			}		
			*/
			childViewHolder.signal_type.setText("개별");
			childViewHolder.signal_type.setBackgroundColor(getResources().getColor(R.color.signal_type_indiv));
			
			//날짜를 설정
			childViewHolder.date.setText(data.date);
			
			//change alarm_bt image
			if(data.alarm == Flag.IS_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm_clicked);
			}
			else if(data.alarm == Flag.IS_NOT_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm);
			}

			
			
			//종목별 코드와 다른점
			// 날짜를 안보이게 한다.
			childViewHolder.date.setVisibility(View.GONE);			

			
			return v;
		}
		
		public class ChildViewHolder {		
			ImageView image;
			TextView signal_name;
			View cond_type;
			TextView signal_type;
			TextView date;
			ImageButton alarm_bt;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
