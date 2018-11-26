package com.example.product;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

public class ActivitySearchResult extends FragmentActivity  {
	
	//1회성이기 때문에 static 으로 하지 않는다.
	ArrayList<search_result> results = new ArrayList<search_result>();
	MyArrayAdapter adapter;
	private ListView listview;
	
	ArrayList<predefined_condition_type> pred_cond_list = new ArrayList<predefined_condition_type>();
	ArrayList<condition_type> cond_list = new ArrayList<condition_type>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		
		
		
		
		/* 테스트 용 */
		search_result result = new search_result();
		result.stock_item_id = 1;
		result.stock_name = "소마전자";
		result.price = 5222341;
		result.price_gap_of_previous_closing_price = -3030;
		result.price_rate_of_previous_closing_price = -7.52;
		
		results.add(result);

		
		
		listview = (ListView)findViewById(R.id.list);
		adapter = new MyArrayAdapter(this, R.layout.activity_search_result_item, results);
		listview.setAdapter(adapter);
		

		

		//Intent를 받음
		
		
		
		
		
		
		
		

		//리스트뷰 클릭할떄 리스너
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				
				//mHost.onItemChecked(position);
				listview.setItemChecked(position, true);
				Intent intent = new Intent(ActivitySearchResult.this, ActivityStockDetail.class);
				
				//인텐트에 클릭된 신호의 주식이름 정보를 전달
				TextView tv = (TextView)v.findViewById(R.id.stock_name);		
				intent.putExtra("stock_name", tv.getText());
				setResult(RESULT_OK);
				startActivity(intent);
				finish();
				//Toast.makeText(getActivity(), position+" is clicked", 0).show();				
			}
			
		});
		
		
		
		//서버에 검색 결과 요청
		post_search_factor factor = new post_search_factor();
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
		
		
		Gson gson = new Gson();
		String data_string = gson.toJson(factor);
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(data_string);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyDataBase.postSearch_factor(jobj, 
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// response
						Log.d("postSearch_factor", "success" + response.toString());
						
						String data_str = response.toString();
						Gson gson = new Gson();
						search_results search_results = gson.fromJson(data_str, search_results.class);
						results.addAll(search_results.search_results);
						adapter.notifyDataSetChanged();					
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						Log.d("postSearch_factor", "error!  " + error.getMessage());
					}
				});
				
		
		
		
		
		//검색 조건 표시하기
		//원래 cps 값은 String이 아니라 level 값이 같이 들어있는 class 이어야 한다.
		//level 값은 default 로 하고 1차 구현을 진행한다.
		
		//ArrayList<String> cps = signal_detail.included_signal_condition_names; // 포함조건
		
		LinearLayout cond_linear = (LinearLayout)findViewById(R.id.cond_linear);
		
		// 업데이트 되었으니 child를 전부 지워준다.
		// 혹시 모르니 일단 지워줌.
		cond_linear.removeAllViews();

		// 조건 목록을 넣어줌
		for(int i=0; i<pred_cond_list.size(); ++i) {
			predefined_condition_type data = pred_cond_list.get(i);
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
			
			//pred_cond_list 는 무조건 easy 이다.
			/*
			if(data.cond_type == Flag.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.cond_type == Flag.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			*/
			cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			
			
			//조건 이름 변경
			cond_name.setText(data.name);
		}
		
		for(int i=0; i<cond_list.size(); ++i) {
			condition_type data = cond_list.get(i);
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
			
			//cond_list 는 무조건 hard 이다.
			/*
			if(data.cond_type == Flag.EASY) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_easy));
			}
			else if(data.cond_type == Flag.HARD) {
				cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			}
			*/
			cond_type.setBackgroundColor(getResources().getColor(R.color.condition_type_hard));
			
			
			//조건 이름 변경
			cond_name.setText(data.name);
		}
	
	}
	
	

	/* ****************************
	 * 어댑터
	 * 
	*****************************/
	
	class MyArrayAdapter extends ArrayAdapter<search_result> {
		private ArrayList<search_result> items;
		private Context context;
		private int resource;

		public MyArrayAdapter(Context context, int viewResourceId, ArrayList<search_result> items) {
			super(context, viewResourceId, items);
			this.items = items;
			this.context = context;
			resource = viewResourceId;
		}

		//그냥 가져다 씀. childView는 신경쓰지 말자.
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ChildViewHolder childViewHolder;

			if(v == null){
	            childViewHolder = new ChildViewHolder();
	            v = View.inflate(context, resource, null);
	            
	            childViewHolder.stock_name = (TextView)v.findViewById(R.id.stock_name);
	            childViewHolder.price = (TextView)v.findViewById(R.id.price);
	            childViewHolder.price_diff = (TextView)v.findViewById(R.id.diff);
	            childViewHolder.price_diff_percent = (TextView)v.findViewById(R.id.diff_percent);

	            v.setTag(childViewHolder);
	            
				/* 버튼 리스너 등록할 것 */
	        }else{
	            childViewHolder = (ChildViewHolder)v.getTag();
	        }
			
			search_result data = items.get(position);
	
			
			childViewHolder.stock_name.setText(data.stock_name);	
			NumberFormat nf = NumberFormat.getNumberInstance();
			childViewHolder.price.setText(nf.format(data.price));
			childViewHolder.price_diff.setText(nf.format(data.price_gap_of_previous_closing_price));
			childViewHolder.price_diff_percent.setText("( " + data.price_rate_of_previous_closing_price + "% )");

			
			/* 전일 종가 대비 변동량 색 변경 */
			if(data.price_gap_of_previous_closing_price < 0) {
				childViewHolder.price_diff.setTextColor(getResources().getColor(R.color.blue));
				childViewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.blue));
			}
			else if(data.price_gap_of_previous_closing_price > 0) {
				childViewHolder.price_diff.setTextColor(getResources().getColor(R.color.red));
				childViewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.red));
			}
			else {
				childViewHolder.price_diff.setTextColor(getResources().getColor(R.color.black));
				childViewHolder.price_diff_percent.setTextColor(getResources().getColor(R.color.black));
			}

			return v;

		}
		public class ChildViewHolder {
			TextView stock_name;
			TextView price;
			TextView price_diff;
			TextView price_diff_percent;
		}
	}
}
