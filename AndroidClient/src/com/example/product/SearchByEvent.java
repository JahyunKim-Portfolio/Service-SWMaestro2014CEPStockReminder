package com.example.product;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.product.R;

public class SearchByEvent extends Activity {
	ListView lvSearchByEvent;
	EditText txtSearchEventName;
	ArrayList<String> eventName = new ArrayList<String>();
	ArrayList<String> eventNum = new ArrayList<String>();
	
	SearchByEventCustomAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_event);
		
		
		

		//서버로부터 데이터 받아오는 부분
		if(MyDataBase.stock_item_list == null) {
			MyDataBase.getStock_list(
					new Response.Listener<stock_items>() {
						@Override
						public void onResponse(stock_items response) {
							if(response != null && response.stock_items != null) {							
								MyDataBase.stock_item_list = response.stock_items;
								ArrayList<stock_item> list = response.stock_items;
								eventName.clear();
								eventNum.clear();
								for(int i=0; i<list.size(); ++i) {
									eventName.add(list.get(i).name);
									eventNum.add(list.get(i).short_code);
								}							
								if(adapter != null) {
									adapter.notifyDataSetChanged();				
								}
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
		else {
			ArrayList<stock_item> list = MyDataBase.stock_item_list;
			eventName.clear();
			eventNum.clear();
			for(int i=0; i<list.size(); ++i) {
				eventName.add(list.get(i).name);
				eventNum.add(list.get(i).short_code);
			}							
			if(adapter != null) {
				adapter.notifyDataSetChanged();				
			}
		}
		
		
		
		
		
		
		
		
		eventName.clear();


		eventNum.add("A1111");
		eventNum.add("A1112");
		eventNum.add("A1113");
		eventName.add("Samsung");
		eventName.add("Sambo");
		eventName.add("Apple");
		

		adapter = new SearchByEventCustomAdapter((Activity)this,this, eventNum, eventName);
		
		lvSearchByEvent = (ListView)findViewById(R.id.lvSearchByEvent);
		lvSearchByEvent.setAdapter(adapter);
		
		
		//리스트뷰 클릭할떄 리스너
		lvSearchByEvent.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				
				
				String name = (String)((TextView)v.findViewById(R.id.txtEventName)).getText();
				int stock_item_id = 0;
				
				// if 구문으로 감싼 부분은 테스트 용으로 놔둔 것으로
				// 나중에 서버 연동이 정상적으로 된다면 지워주자.
				if(MyDataBase.stock_item_list != null) {
					ArrayList<stock_item> tlist = MyDataBase.stock_item_list;
					for(int i=0; i<tlist.size(); ++i) {
						stock_item data = tlist.get(i);
						if(data.name.equals(name)) {
							stock_item_id = data.id;
							break;
						}
					}
				}
				
				Intent intent = new Intent();		
				intent.putExtra("stock_name", name);
				intent.putExtra("stock_item_id", stock_item_id);		
				setResult(RESULT_OK, intent);
				// TODO Auto-generated method stub
				finish();	
			}
			
		});
		
		
		//divder가 위아래 에도 표시되게 하기 위해 빈 header와 footer를 삽입	
		lvSearchByEvent.addFooterView(new View(this), null, true);
		lvSearchByEvent.addHeaderView(new View(this), null, true);

		txtSearchEventName = (EditText)findViewById(R.id.txtSearchByEvent);
		txtSearchEventName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String text = txtSearchEventName.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.Filter(text);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
}
