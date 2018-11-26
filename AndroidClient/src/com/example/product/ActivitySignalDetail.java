package com.example.product;

import java.util.ArrayList;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.product.ActivityStockDetail.MyArrayAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySignalDetail extends FragmentActivity {
	signal_condition_detail signal_detail;  //static 이면 안됨. 매번 새로운 정보를 받아야 한다.
	
	// 받은 시그널 정보
	static ArrayList<signal_result> signal_list_all;
	static ArrayList<signal_result> signal_list_alarm;
	
	static MyArrayAdapter adapter_all;
	static MyArrayAdapter adapter_alarm;
	static MyArrayAdapter current_adapter;
	
	private PullToRefreshListView ptrlistview;
	private ListView listview;
	
	public void notifySignal_detailChanged() {

		//원래 cps 값은 String이 아니라 level 값이 같이 들어있는 class 이어야 한다.
		//level 값은 default 로 하고 1차 구현을 진행한다.
		
		ArrayList<String> cps = signal_detail.included_signal_condition_names; // 포함조건
		
		LinearLayout cond_linear = (LinearLayout)findViewById(R.id.cond_linear);
		
		// 업데이트 되었으니 child를 전부 지워준다.
		cond_linear.removeAllViews();

		// 동적으로 포함조건 목록을 넣어줌
		for(int i=0; i<cps.size(); ++i) {
			String data = cps.get(i);
			LinearLayout horizon_linear;
			RelativeLayout rel_root;
			if(i % 2 == 0) {
				horizon_linear = (LinearLayout)View.inflate(this, R.layout.simple_horizontal_linear_with_two_itmes, null);
				cond_linear.addView(horizon_linear);
				rel_root = (RelativeLayout)horizon_linear.findViewById(R.id.first_rel);
			}
			else {
				horizon_linear = (LinearLayout)cond_linear.getChildAt(i/2);
				rel_root = (RelativeLayout)horizon_linear.findViewById(R.id.second_rel);
			}
			RelativeLayout v = (RelativeLayout)View.inflate(this, R.layout.simple_condition, rel_root);
			
			ImageView image = (ImageView)v.findViewById(R.id.image);
			View cond_type = (View)v.findViewById(R.id.type_color);
			TextView cond_name = (TextView)v.findViewById(R.id.condition_name);
			
			// 이미지 교체 코드 작성 
			
			//타입 색 변경 코드 작성
			//이미지 아래 VIew 색을 변경 
			//1차 구현에서는 미구현
			/*
			if(data.cond_type == SimpleCond.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.cond_type == SimpleCond.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			*/
			
			//조건 이름 변경
			cond_name.setText(data);
		}	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signal_detail);
		
		// pull to refresh 등록
		ptrlistview = (PullToRefreshListView)findViewById(R.id.ptr_list);
		listview = ptrlistview.getRefreshableView();
		
		
		//타이틀 설정
		Intent intent = getIntent();
		String signal_name = intent.getStringExtra("signal_name");
		final int signal_id = intent.getIntExtra("signal_id", 1); //default 값은 1
		if(signal_name != null) {
			setTitle(signal_name);
			// title에도 등록하고, 맨위 textview 에도 등록 할 것.
			TextView t = (TextView)findViewById(R.id.signal_name);
			t.setText(signal_name);
		}
		/*
		// 포함조건 동적으로 추가
		// 시그널 디테일 서버에서 받아오기
		signaldetail = new SignalDetail();
		signaldetail.compose_signal.add(new SimpleCond());
		signaldetail.compose_signal.add(new SimpleCond());
		signaldetail.compose_signal.add(new SimpleCond());
		
		// 해당 조건에 대한 시그널을 서버에서 받아오기 
		list = new ArrayList<SignalOfSignal>();
		for(int i=0; i<11; ++i) {
			list.add(new SignalOfSignal());
		}
		*/
		
		// 해당 신호에 대해 정보를 받아옴
		MyDataBase.getSignal_condition_detail(signal_id, 
				new Response.Listener<signal_condition_detail>() {
					@Override
					public void onResponse(signal_condition_detail response) {
						signal_detail = response;
						notifySignal_detailChanged();
					}
				}, 
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("product", "error!" + error.getMessage());
					}
				});
		
		//static 변수들 초기화 하고 데이터 받아오기. 신호정보
		if(signal_list_all == null) {
			signal_list_all = new ArrayList<signal_result>();
			MyDataBase.getCondSignal_list(0, -1, -1, signal_id,
					new Response.Listener<signal_results>() {
						@Override
						public void onResponse(signal_results response) {
							signal_list_all.addAll(response.signal_results);
							if(adapter_all != null) {
								adapter_all.notifyDataSetChanged();				
							}
						}
					}, 
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("product", "error!" + error.getMessage());
						}
					});
		}
		if(signal_list_alarm == null) {
			signal_list_alarm = new ArrayList<signal_result>();
			MyDataBase.getCondSignal_list(3, -1, -1, signal_id, 
					new Response.Listener<signal_results>() {
						@Override
						public void onResponse(signal_results response) {
							signal_list_alarm.addAll(response.signal_results);
							if(adapter_alarm != null) {
								adapter_alarm.notifyDataSetChanged();				
							}
						}
					}, 
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("product", "error!" + error.getMessage());
						}
					});
		}		

		
		
		
		
		// 서버에서 받아온 값이 없을때, default 값.
		if(signal_detail == null) {
			signal_detail = new signal_condition_detail();
			signal_detail.user_signal_condition_name = "DEFAULT";
			signal_detail.level = 0;
			signal_detail.included_signal_condition_names = new ArrayList<String>();
		}
		
		notifySignal_detailChanged(); // signal_detail 기본값을 출력
		
		//어댑터 등록
		adapter_all = new MyArrayAdapter(this, R.layout.activity_signal_detail_item, signal_list_all);
		adapter_alarm = new MyArrayAdapter(this, R.layout.activity_signal_detail_item, signal_list_alarm);
		
		
		// 기본적으로 제일 처음에는 all 이 보여진다.
		listview.setAdapter(adapter_all);
		current_adapter = adapter_all;

		
		
		// Set a listener to be invoked when the list should be refreshed.
		ptrlistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(ActivitySignalDetail.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				
				
				

				if(current_adapter == adapter_all) {
					if(signal_list_all.isEmpty()) {
						MyDataBase.getCondSignal_list(0, -1, -1, signal_id,
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_all.addAll(response.signal_results);
										if(adapter_all != null) {
											adapter_all.notifyDataSetChanged();		
											refreshView.onRefreshComplete();
										}
									}
								}, 
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										Log.e("product", "error!" + error.getMessage());
										refreshView.onRefreshComplete();
									}
								});
					}
					else {
						int pos = signal_list_all.get(0).signal_id;
						MyDataBase.getCondSignal_list(0, 1, pos, signal_id, //all, after, pos 
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_all.addAll(0, response.signal_results);
										if(adapter_all != null) {
											adapter_all.notifyDataSetChanged();				
										}
										// Call onRefreshComplete when the list has been refreshed.
										refreshView.onRefreshComplete();
									}
								}, 
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										Log.e("product", "error!" + error.getMessage());
										refreshView.onRefreshComplete();
									}
								});
					}
				}
				else if(current_adapter == adapter_alarm) {
					if(signal_list_alarm.isEmpty()) {
						MyDataBase.getCondSignal_list(3, -1, -1, signal_id,
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_alarm.addAll(response.signal_results);
										if(adapter_alarm != null) {
											adapter_alarm.notifyDataSetChanged();				
										}
										refreshView.onRefreshComplete();
									}
								}, 
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										Log.e("product", "error!" + error.getMessage());
										refreshView.onRefreshComplete();
									}
								});
						
					}
					else {
						int pos = signal_list_alarm.get(0).signal_id;
						MyDataBase.getCondSignal_list(3, 1, pos, signal_id, //alarm, after, pos 
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_alarm.addAll(0, response.signal_results);
										if(adapter_alarm != null) {
											adapter_alarm.notifyDataSetChanged();				
										}
										// Call onRefreshComplete when the list has been refreshed.
										refreshView.onRefreshComplete();
									}
								}, 
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										Log.e("product", "error!" + error.getMessage());
										refreshView.onRefreshComplete();
									}
								});
					}
				}
				//new GetDataTask().execute();				
			}
		});
	
		
		// Add an end-of-list listener
		ptrlistview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(ActivitySignalDetail.this, "End of List!", Toast.LENGTH_SHORT).show();
				if(current_adapter == adapter_all) {
					final int last_idx = signal_list_all.size()-1;
					int pos = signal_list_all.get(last_idx).signal_id;
					MyDataBase.getCondSignal_list(0, 0, pos, signal_id, //all, before, pos 
							new Response.Listener<signal_results>() {
								@Override
								public void onResponse(signal_results response) {
									signal_list_all.addAll(response.signal_results);
									if(adapter_all != null) {
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
				}
				else if(current_adapter == adapter_alarm) {
					final int last_idx = signal_list_alarm.size()-1;
					int pos = signal_list_alarm.get(last_idx).signal_id;
					MyDataBase.getCondSignal_list(3, 0, pos, signal_id, //alarm, before, pos 
							new Response.Listener<signal_results>() {
								@Override
								public void onResponse(signal_results response) {
									signal_list_alarm.addAll(response.signal_results);
									if(adapter_alarm != null) {
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
				}		
			}
		});
		
		
		
		/*
		ArrayList<SimpleCond> cps = signaldetail.compose_signal; // 포함조건
		// MyAdapter는 ActivityStockDetail 에 있음
		//어댑터 등록		
		lv.setAdapter(new MyAdapter(this, R.layout.activity_signal_detail_item, list));
		
		LinearLayout cond_linear = (LinearLayout)findViewById(R.id.cond_linear);
		
		// 여기로 진입하면 안됨.
		if(cond_linear.getChildCount() != 0) {
			//차일드가 있으면 안됨.
			Toast.makeText(this, "경고! 포함조건 linear에 차일드가 있음!", 0).show();
			cond_linear.removeAllViews();
		}
		
		// 동적으로 포함조건 목록을 넣어줌
		for(int i=0; i<cps.size(); ++i) {
			SimpleCond data = cps.get(i);
			LinearLayout horizon_linear;
			RelativeLayout rel_root;
			if(i % 2 == 0) {
				horizon_linear = (LinearLayout)View.inflate(this, R.layout.simple_horizontal_linear_with_two_itmes, null);
				cond_linear.addView(horizon_linear);
				rel_root = (RelativeLayout)horizon_linear.findViewById(R.id.first_rel);
			}
			else {
				horizon_linear = (LinearLayout)cond_linear.getChildAt(i/2);
				rel_root = (RelativeLayout)horizon_linear.findViewById(R.id.second_rel);
			}
			RelativeLayout v = (RelativeLayout)View.inflate(this, R.layout.simple_condition, rel_root);
			
			ImageView image = (ImageView)v.findViewById(R.id.image);
			View cond_type = (View)v.findViewById(R.id.type_color);
			TextView cond_name = (TextView)v.findViewById(R.id.condition_name);
			
			// 이미지 교체 코드 작성 
			
			//타입 색 변경 코드 작성
			//이미지 아래 VIew 색을 변경
			if(data.cond_type == SimpleCond.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.cond_type == SimpleCond.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			
			//조건 이름 변경
			cond_name.setText(data.cond_name);			
		}	
*/
		
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
					
					listview.setAdapter(adapter_all);
					current_adapter = adapter_all;
					break;
				case R.id.alarm_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_alarm();
					my_adapter.notifyDataSetChanged();
					*/
					
					listview.setAdapter(adapter_alarm);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_stock_detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	
		int id = item.getItemId();
		if (id == R.id.action_my_settings) {
			Toast.makeText(this, "신호별로 종목을 설정하는 페이지는 차후 버전에서 구현", 0).show();

			//설정 메뉴로 진입
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	/* ****************************
	 * 어댑터
	 * 
	*****************************/
	
	class MyArrayAdapter extends ArrayAdapter<signal_result> {
		private ArrayList<signal_result> items;
		private Context context;
		private int resource;

		public MyArrayAdapter(Context context, int viewResourceId, ArrayList<signal_result> items) {
			super(context, viewResourceId, items);
			this.items = items;
			this.context = context;
			resource = viewResourceId;
		}

		//FragmentSignal 에서 신호-신호별 exp 어댑터의 childView 부분.
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ChildViewHolder childViewHolder;

			if(v == null){
	            childViewHolder = new ChildViewHolder();
	            v = View.inflate(context, resource, null);
	            
	            childViewHolder.stock_name = (TextView)v.findViewById(R.id.stock_name);
	            childViewHolder.price = (TextView)v.findViewById(R.id.price);
	            childViewHolder.date = (TextView)v.findViewById(R.id.date);
	            childViewHolder.alarm_bt = (ImageButton)v.findViewById(R.id.alarm_bt);
	            v.setTag(childViewHolder);
	            
				ImageButton bt = (ImageButton)v.findViewById(R.id.alarm_bt);
				bt.setFocusable(false);
				/* 버튼 리스너 등록할 것 */
	        }else{
	            childViewHolder = (ChildViewHolder)v.getTag();
	        }
			
			final signal_result data = items.get(position);
	
			//주식명을 변경
			childViewHolder.stock_name.setText(data.stock_item_name);
			
			//주식 가격을 변경
			childViewHolder.price.setText("" + data.price);
			
			//날짜를 설정
			childViewHolder.date.setText(data.date);
			
			
			//change alarm_bt image
			if(data.alarm == Flag.IS_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm_clicked);
			}
			else if(data.alarm == Flag.IS_NOT_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm);
			}
			
			
			//리스너 등록은 if ( v== null) 에서 하는게 아니라 밖에서 함.
			childViewHolder.alarm_bt.setOnClickListener(new ImageButton.OnClickListener() {
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
					MyDataBase.putAlarmChange(data.user_signal_condition_id, data.alarm);
				}						
			});	
			
			return v;

		}
		public class ChildViewHolder {
			TextView stock_name;
			TextView price;
			TextView date;
			ImageButton alarm_bt;
		}
	}
	
	//test용 데이터 받아오는 클래스
	// 지금은 4초 딜레이만 있다. 
	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			//mListItems.addFirst("Added after refresh...");
			//mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			ptrlistview.onRefreshComplete();

			super.onPostExecute(v);
		}
	}
}

