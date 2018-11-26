package com.example.product;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ActivityStockDetail extends FragmentActivity{
	static stock_detail stock_detail;
	
	// 받은 시그널 정보
	static ArrayList<signal_result> signal_list_all;
	static ArrayList<signal_result> signal_list_total;
	static ArrayList<signal_result> signal_list_indiv;
	static ArrayList<signal_result> signal_list_alarm;
	
	static MyArrayAdapter adapter_all;
	static MyArrayAdapter adapter_total;
	static MyArrayAdapter adapter_indiv;
	static MyArrayAdapter adapter_alarm;
	static MyArrayAdapter current_adapter;
	
	
	private PullToRefreshListView ptrlistview;
	private ListView listview;
	
	public void notifyStock_detailChanged() {
		// 받아온 데이터를 화면에 띄움
		//가격 정보
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		TextView price = (TextView)findViewById(R.id.price);
		price.setText(nf.format(stock_detail.price));
		
		
		//price_diff 에 따라 값을 설정		
		//price_diff 값에 쉼표가 들어가면  parse가 안된다. 주의!
		TextView price_diff = (TextView)findViewById(R.id.price_diff);
		TextView price_diff_percent = (TextView)findViewById(R.id.price_diff_percent);
		if(stock_detail.price_gap_of_previous_closing_price > 0) {
			price_diff.setTextColor(getResources().getColor(R.color.red));
			price_diff_percent.setTextColor(getResources().getColor(R.color.red));	
		}
		else if(stock_detail.price_gap_of_previous_closing_price < 0) {
			price_diff.setTextColor(getResources().getColor(R.color.blue));
			price_diff_percent.setTextColor(getResources().getColor(R.color.blue));	
		}
		else {
			price_diff.setTextColor(getResources().getColor(R.color.black));
			price_diff_percent.setTextColor(getResources().getColor(R.color.black));	
		}		
		price_diff.setText(nf.format(stock_detail.price_gap_of_previous_closing_price));
		price_diff_percent.setText("( " + stock_detail.price_rate_of_previous_closing_price + "% )");
		
		//고가 저가 설정
		TextView high_price = (TextView)findViewById(R.id.highest_price);
		TextView low_price = (TextView)findViewById(R.id.lowest_price);
		
		if(stock_detail.high_price == stock_detail.low_price) {
			high_price.setTextColor(getResources().getColor(R.color.black));
			low_price.setTextColor(getResources().getColor(R.color.black));
		}
		else {
			high_price.setTextColor(getResources().getColor(R.color.red));
			low_price.setTextColor(getResources().getColor(R.color.blue));
		}
		high_price.setText("" + stock_detail.high_price);
		low_price.setText("" + stock_detail.low_price);
		
		//거래량 설정		
		TextView trading_volume = (TextView)findViewById(R.id.trade_volume);
		TextView trading_volume_avr = (TextView)findViewById(R.id.trade_volume_average);
		trading_volume.setText("" + stock_detail.volume);
		trading_volume_avr.setText("" + stock_detail.average_volume);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_detail);
		
		// pull to refresh 등록
		ptrlistview = (PullToRefreshListView)findViewById(R.id.ptr_list);
		listview = ptrlistview.getRefreshableView();
				
		//타이틀 설정
		Intent intent = getIntent();
		String stock_name = intent.getStringExtra("stock_name");
		final int stock_item_id = intent.getIntExtra("stock_item_id", 1); //1 은 default 값이다.
		if(stock_name != null) {
			setTitle(stock_name);			
		}
		
		
		// 해당 주식 종목에 대해 정보를 받아옴
		MyDataBase.getStock_detail(stock_item_id, 
				new Response.Listener<stock_detail>() {
					@Override
					public void onResponse(stock_detail response) {
						stock_detail = response;
						notifyStock_detailChanged();
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
			MyDataBase.getStockSignal_list(0, -1, -1, stock_item_id,
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
		if(signal_list_total == null) {
			signal_list_total = new ArrayList<signal_result>();
			MyDataBase.getStockSignal_list(1, -1, -1, stock_item_id,
					new Response.Listener<signal_results>() {
						@Override
						public void onResponse(signal_results response) {
							signal_list_total.addAll(response.signal_results);
							if(adapter_total != null) {
								adapter_total.notifyDataSetChanged();				
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
		if(signal_list_indiv == null) {
			signal_list_indiv = new ArrayList<signal_result>();
			MyDataBase.getStockSignal_list(2, -1, -1, stock_item_id, 
					new Response.Listener<signal_results>() {
						@Override
						public void onResponse(signal_results response) {
							signal_list_indiv.addAll(response.signal_results);
							if(adapter_indiv != null) {
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
			MyDataBase.getStockSignal_list(3, -1, -1, stock_item_id, 
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
		if(stock_detail == null) {
			stock_detail = new stock_detail();
			stock_detail.stock_item_name = "DEFAULT";
			stock_detail.price = 0;
			stock_detail.price_gap_of_previous_closing_price = 0;
			stock_detail.price_rate_of_previous_closing_price = 0;
			stock_detail.high_price = 0;
			stock_detail.low_price = 0;
			stock_detail.volume = 0;
			stock_detail.average_volume = 0;
			stock_detail.user_signal_conditions = new ArrayList<user_signal_condition>();
		}
		
		notifyStock_detailChanged(); // stock_detail 기본값을 출력
		
		//어댑터 등록
		adapter_all = new MyArrayAdapter(this, R.layout.activity_stock_detail_item, signal_list_all);
		adapter_total = new MyArrayAdapter(this, R.layout.activity_stock_detail_item, signal_list_total);
		adapter_indiv = new MyArrayAdapter(this, R.layout.activity_stock_detail_item, signal_list_indiv);
		adapter_alarm = new MyArrayAdapter(this, R.layout.activity_stock_detail_item, signal_list_alarm);
		
		// 기본적으로 제일 처음에는 all 이 보여진다.
		listview.setAdapter(adapter_all);
		current_adapter = adapter_all;


		
		
		//아래의 코드는 myNotifyDataSetChanged() method 로 wrapping하여 옮겨놓았다.
		/*
		// 받아온 데이터를 화면에 띄움

		//가격 정보
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		TextView price = (TextView)findViewById(R.id.price);
		price.setText(nf.format(stock_detail.price));
		
		
		//price_diff 에 따라 값을 설정		
		//price_diff 값에 쉼표가 들어가면  parse가 안된다. 주의!
		TextView price_diff = (TextView)findViewById(R.id.price_diff);
		TextView price_diff_percent = (TextView)findViewById(R.id.price_diff_percent);
		if(stock_detail.price_gap_of_previous_closing_price > 0) {
			price_diff.setTextColor(getResources().getColor(R.color.red));
			price_diff_percent.setTextColor(getResources().getColor(R.color.red));	
		}
		else if(stock_detail.price_gap_of_previous_closing_price < 0) {
			price_diff.setTextColor(getResources().getColor(R.color.blue));
			price_diff_percent.setTextColor(getResources().getColor(R.color.blue));	
		}
		else {
			price_diff.setTextColor(getResources().getColor(R.color.black));
			price_diff_percent.setTextColor(getResources().getColor(R.color.black));	
		}		
		price_diff.setText(nf.format(stock_detail.price_gap_of_previous_closing_price));
		price_diff_percent.setText("( " + stock_detail.price_rate_of_previous_closing_price + "% )");
		
		//고가 저가 설정
		TextView high_price = (TextView)findViewById(R.id.highest_price);
		TextView low_price = (TextView)findViewById(R.id.lowest_price);
		
		if(stock_detail.high_price == stock_detail.low_price) {
			high_price.setTextColor(getResources().getColor(R.color.black));
			low_price.setTextColor(getResources().getColor(R.color.black));
		}
		else {
			high_price.setTextColor(getResources().getColor(R.color.red));
			low_price.setTextColor(getResources().getColor(R.color.blue));
		}
		high_price.setText("" + stock_detail.high_price);
		low_price.setText("" + stock_detail.low_price);
		
		//거래량 설정		
		TextView trading_volume = (TextView)findViewById(R.id.trade_volume);
		TextView trading_volume_avr = (TextView)findViewById(R.id.trade_volume_average);
		trading_volume.setText("" + stock_detail.volume);
		trading_volume_avr.setText("" + stock_detail.average_volume);
		
		*/

				

		
		// Set a listener to be invoked when the list should be refreshed.
		ptrlistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(ActivityStockDetail.this.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.

				if(current_adapter == adapter_all) {
					if(signal_list_all.isEmpty()) {
						MyDataBase.getStockSignal_list(0, -1, -1, stock_item_id,
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
						MyDataBase.getStockSignal_list(0, 1, pos, stock_item_id, //all, after, pos 
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
				else if(current_adapter == adapter_total) {
					if(signal_list_total.isEmpty()) {
						MyDataBase.getStockSignal_list(1, -1, -1, stock_item_id,
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_total.addAll(response.signal_results);
										if(adapter_total != null) {
											adapter_total.notifyDataSetChanged();	
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
						int pos = signal_list_total.get(0).signal_id;
						MyDataBase.getStockSignal_list(1, 1, pos, stock_item_id,//total, after, pos 
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_total.addAll(0, response.signal_results);
										if(adapter_total != null) {
											adapter_total.notifyDataSetChanged();				
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
				else if(current_adapter == adapter_indiv) {
					if(signal_list_indiv.isEmpty()) {
						MyDataBase.getStockSignal_list(2, -1, -1, stock_item_id,
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_indiv.addAll(response.signal_results);
										if(adapter_indiv != null) {
											adapter_all.notifyDataSetChanged();				
										}
										refreshView.onRefreshComplete();
									}
								}, 
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										Log.e("product", "error!" + error.getMessage());
										refreshView.onRefreshComplete();}
								});
					}
					else {
						int pos = signal_list_indiv.get(0).signal_id;
						MyDataBase.getStockSignal_list(2, 1, pos, stock_item_id, //indiv, after, pos 
								new Response.Listener<signal_results>() {
									@Override
									public void onResponse(signal_results response) {
										signal_list_indiv.addAll(0, response.signal_results);
										if(adapter_indiv != null) {
											adapter_indiv.notifyDataSetChanged();				
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
						MyDataBase.getStockSignal_list(3, -1, -1, stock_item_id,
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
						MyDataBase.getStockSignal_list(3, 1, pos, stock_item_id, //alarm, after, pos 
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
				//Toast.makeText(ActivityStockDetail.this, "End of List!", Toast.LENGTH_SHORT).show();
				
				if(current_adapter == adapter_all) {
					final int last_idx = signal_list_all.size()-1;
					int pos = signal_list_all.get(last_idx).signal_id;
					MyDataBase.getStockSignal_list(0, 0, pos, stock_item_id, //all, before, pos 
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
				else if(current_adapter == adapter_total) {
					final int last_idx = signal_list_total.size()-1;
					int pos = signal_list_total.get(last_idx).signal_id;
					MyDataBase.getStockSignal_list(1, 0, pos, stock_item_id, //total, before, pos 
							new Response.Listener<signal_results>() {
								@Override
								public void onResponse(signal_results response) {
									signal_list_total.addAll(response.signal_results);
									if(adapter_total != null) {
										adapter_total.notifyDataSetChanged();				
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
				else if(current_adapter == adapter_indiv) {
					final int last_idx = signal_list_indiv.size()-1;
					int pos = signal_list_indiv.get(last_idx).signal_id;
					MyDataBase.getStockSignal_list(2, 0, pos, stock_item_id,//indiv, before, pos 
							new Response.Listener<signal_results>() {
								@Override
								public void onResponse(signal_results response) {
									signal_list_indiv.addAll(response.signal_results);
									if(adapter_indiv != null) {
										adapter_indiv.notifyDataSetChanged();				
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
					MyDataBase.getStockSignal_list(3, 0, pos, stock_item_id, //alarm, before, pos 
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
		
		
		/* 하단 메뉴 설정 */
		FragmentManager fm=getSupportFragmentManager();
		BottomMenu.BottomMenu_1 bottom_menu;
		if((bottom_menu = (BottomMenu.BottomMenu_1)fm.findFragmentById(R.id.bottom_menu)) == null) {
			bottom_menu = new BottomMenu.BottomMenu_1();
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
				case R.id.total_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_total();
					my_adapter.notifyDataSetChanged();
					*/
					
					listview.setAdapter(adapter_total);
					current_adapter = adapter_total;
					break;
				case R.id.indiv_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_indiv();
					my_adapter.notifyDataSetChanged();
					*/
					//어댑터를 변경 
					listview.setAdapter(adapter_indiv);
					current_adapter = adapter_indiv;
					break;
				case R.id.alarm_btn :
					/*
					received_signal = MyDataBase.getReceivedSignalList_alarm();
					my_adapter.notifyDataSetChanged();
					*/
					
					//어댑터를 변경 
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
	
	/*
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
	*/
	
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
			
			Gson gson = new Gson();
			String stock_detail_json_string = gson.toJson(stock_detail);
			
			Intent intent = new Intent(this, ActivityStockDetailSettings.class);		
			//인텐트에 주식의 이름을 전달		
			intent.putExtra("stock_detail", stock_detail_json_string);
			//설정 메뉴로 진입
			startActivity(intent);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



/***************************************
 * 
 * @author NamHoon
 * 		어댑터
 *
 ****************************************/


	/* FragmentSignal의 종목별 어댑터의 ChildView 하는 부분과 동일 따라서 복붙 */
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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ChildViewHolder childViewHolder;

			if(v == null){
	            childViewHolder = new ChildViewHolder();
	            v = View.inflate(context, resource, null);
	            childViewHolder.image = (ImageView)v.findViewById(R.id.image);
	            childViewHolder.cond_type = (View)v.findViewById(R.id.type_color);
	            childViewHolder.signal_name = (TextView)v.findViewById(R.id.signal);
	            childViewHolder.signal_type = (TextView)v.findViewById(R.id.signal_type);
	            childViewHolder.date = (TextView)v.findViewById(R.id.date);
	            childViewHolder.alarm_bt = (ImageButton)v.findViewById(R.id.alarm_bt);
	            v.setTag(childViewHolder);
	            
				ImageButton bt = (ImageButton)v.findViewById(R.id.alarm_bt);
				bt.setFocusable(false);
	        }else{
	            childViewHolder = (ChildViewHolder)v.getTag();
	        }
			
			signal_result data = items.get(position);
			
			
			//리스너 등록은 if ( v== null) 에서 하는게 아니라 밖에서 함.
			childViewHolder.alarm_bt.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					/* 알람 버튼이 눌리면 이미지를 바꾸고, 서버에 알람버튼을 눌렀다고 알림 */
					signal_result data = items.get(position);
					if(data.alarm == Flag.IS_ALARM){
						data.alarm = Flag.IS_NOT_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm);
						MyDataBase.putAlarmChange(data.user_signal_condition_id, data.alarm);
					}
					else if(data.alarm == Flag.IS_NOT_ALARM){
						data.alarm = Flag.IS_ALARM;
						((ImageView)v).setImageResource(R.drawable.push_alarm_clicked);
						MyDataBase.putAlarmChange(data.user_signal_condition_id, data.alarm);
					}
					
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
			if(data.applicable_range == Flag.TOTAL) {
				childViewHolder.signal_type.setText("전체");
				childViewHolder.signal_type.setBackgroundColor(getResources().getColor(R.color.signal_type_total));
			}
			else if(data.applicable_range == Flag.INDIV) {
				childViewHolder.signal_type.setText("개별");
				childViewHolder.signal_type.setBackgroundColor(getResources().getColor(R.color.signal_type_indiv));
			}		
			
			//날짜를 설정
			childViewHolder.date.setText(data.date);
			
			

			
			//change alarm_bt image
			if(data.alarm == Flag.IS_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm_clicked);
			}
			else if(data.alarm == Flag.IS_NOT_ALARM){
				childViewHolder.alarm_bt.setImageResource(R.drawable.push_alarm);
			}

			
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
	}
}