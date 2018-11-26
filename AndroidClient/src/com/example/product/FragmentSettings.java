package com.example.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentSettings extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_settings, container, false);
		
		Button my_stock_bt = (Button)root.findViewById(R.id.show_my_stock_button);
		my_stock_bt.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ActivityMyStock.class);
				startActivity(intent);				
			}
		});
		
		final Button push_bt = (Button)root.findViewById(R.id.set_push_button);
		push_bt.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/*서버에 신호를 보내기 */
				if(push_bt.getText().equals("ON")) {
					push_bt.setText("OFF");
				}
				else {
					push_bt.setText("ON");
				}
			}
		});
		
		final Button delete_bt = (Button)root.findViewById(R.id.delete_signal_button);
		delete_bt.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/*서버에 신호를 보내기 */
				Toast.makeText(getActivity(), "삭제 완료!", 0).show();						
			}
		});
		
		final Button reset_bt = (Button)root.findViewById(R.id.reset_signal_setting_button);
		reset_bt.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/*서버에 신호를 보내기 */
				Toast.makeText(getActivity(), "초기화 완료!", 0).show();						
			}
		});
		return root;
	}
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	*/
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_add_alarm :
				// 전체 신호 추가 Activity
				/*
				Intent intent = new Intent(getActivity(), StockDetail.class);
				startActivity(intent);
				*/		
				
				return true;
			case R.id.action_settings :
				//do s.th.
				
				return true;
			default :
				Toast.makeText(getActivity(), "영문이 바보", 0).show();
				return super.onOptionsItemSelected(item);
		}
	}
	
	

}
