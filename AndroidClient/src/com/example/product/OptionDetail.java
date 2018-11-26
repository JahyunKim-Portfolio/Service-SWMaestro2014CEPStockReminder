package com.example.product;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class OptionDetail extends ActionBarActivity {
	ImageView imgLikeNum, imgLikePlus;
	TextView txtOptionName, txtOptionIntroduct, txtOptionDetail, txtLikeNum,
			txtUseNum, txtOptionInform;
	View informView;
	Button btnSubmit;
	ListView lvParam;
	int type = 0;
	
	
	predefined_condition_type pred_cond_data = new predefined_condition_type();
	condition_type cond_data = new condition_type();
	
	public void onClickMethod(View v) {
		setResult(RESULT_OK, getIntent());
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		
		
		predefined_condition_type predefined_condition_type = null;
		condition_type condition_type = null;
		
		//Intent로 부터 데이터 받아오기
		if(intent.getStringExtra("type").equals("easy")) {
			type = 0;
			String data_string = intent.getStringExtra("predefined_condition_type");
					
			Gson gson = new Gson();
			pred_cond_data = gson.fromJson(data_string, predefined_condition_type.class);
			
			predefined_condition_type = pred_cond_data;
				
		}
		else if(intent.getStringExtra("type").equals("hard")) {
			type = 1;
			String data_string = intent.getStringExtra("condition_type");
			
			Gson gson = new Gson();
			cond_data = gson.fromJson(data_string, condition_type.class);
			
			condition_type = cond_data;
		}
		
		
		/*
		predefined_condition_type predefined_condition_type = new predefined_condition_type();
		predefined_condition_type.parameter_types = new ArrayList<predefined_condition_parameter_type>();
		condition_type condition_type = new condition_type();
		condition_type.parameter_types = new ArrayList<condition_parameter_type>();
		*/
		String optionName = intent.getStringExtra("name");
		type = type+0;
		if (type == 0) {
			/*
			predefined_condition_type.name="성장주";
			predefined_condition_type.description="디테일 내용";
			predefined_condition_type.id=123;
			predefined_condition_type.number_of_users_add_me=101;
			predefined_condition_type.number_of_users_like_me=22;
			predefined_condition_parameter_type tt = new predefined_condition_parameter_type();
			tt.order = 1;
			tt.name = "매출액 증가";
			tt.desc = "최근 3년 평균 증감율 50% 이상";
			predefined_condition_type.parameter_types.add(tt);
			predefined_condition_parameter_type aa = new predefined_condition_parameter_type();
			aa.order = 2;
			aa.name = "매출액";
			aa.desc = "최근 결산액 400억 이상";
			predefined_condition_type.parameter_types.add(aa);
			*/
			
			setContentView(R.layout.activity_option_detail);
			
			imgLikeNum = (ImageView) findViewById(R.id.imgLikeNum);
			imgLikePlus = (ImageView) findViewById(R.id.imgLike);
			txtOptionName = (TextView) findViewById(R.id.txtOptionName);
			txtOptionIntroduct = (TextView) findViewById(R.id.txtInform);
			txtLikeNum = (TextView) findViewById(R.id.txtLikeNum);
			txtUseNum = (TextView) findViewById(R.id.txtUseNum);
			txtOptionDetail = (TextView) findViewById(R.id.txtDetail);
			txtOptionName.setText(predefined_condition_type.name);
			txtOptionIntroduct.setText(predefined_condition_type.description);
			int len = predefined_condition_type.parameter_types.size();
			String str="";
			for(int i=0;i<len;i++){
				if(i!=0) str+="\n";
				str+=predefined_condition_type.parameter_types.get(i).name+"\n\t";
				str+="●"+predefined_condition_type.parameter_types.get(i).desc;
			}
			txtOptionDetail.setText(str);
			txtLikeNum.setText(predefined_condition_type.number_of_users_like_me+"");
			txtUseNum.setText(predefined_condition_type.number_of_users_add_me+"");
		}else{
			LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			setContentView(R.layout.activity_option_detail2);
			
			/*
			condition_type.name = "성장주";
			condition_type.id = 11;
			condition_type.description = "성장주에 대한 자세한 설명";
			condition_type.description_of_parameters = "A증가 B감소";
			condition_type.category="시세";
			condition_type.number_of_users_add_me=1111;
			condition_type.number_of_users_like_me=313;
			condition_parameter_type aa = new condition_parameter_type();
			aa.order = 1;
			aa.type = "int";
			condition_type.parameter_types.add(aa);
			*/
			
			imgLikeNum = (ImageView) findViewById(R.id.imgLikeNum);
			imgLikePlus = (ImageView) findViewById(R.id.imgLike);
			txtOptionName = (TextView) findViewById(R.id.txtOptionName);
			txtOptionInform = (TextView) findViewById(R.id.txtInform);
			txtOptionDetail = (TextView) findViewById(R.id.txtOptionDetail);
			txtLikeNum = (TextView) findViewById(R.id.txtLikeNum);
			txtUseNum = (TextView) findViewById(R.id.txtUseNum);
			LinearLayout inLayout = (LinearLayout) findViewById(R.id.llChild);
			
			txtOptionName.setText(condition_type.name);
			txtOptionInform.setText(condition_type.description);
			txtOptionDetail.setText(condition_type.description_of_parameters);
			txtLikeNum.setText(condition_type.number_of_users_like_me+"");
			txtUseNum.setText(condition_type.number_of_users_add_me+"");
			
			
			for (int i = 0; i < (condition_type.parameter_types.size()+1)/2; i++) {
				LinearLayout route = (LinearLayout) infalInflater.inflate(
						R.layout.expandablelistview_child2_child, null);
				EditText txtParamName1 = (EditText) route.findViewById(R.id.txtParamName1);
				EditText txtParamName2 = (EditText) route.findViewById(R.id.txtParamName2);
				TextView txtParam1 = (TextView) route.findViewById(R.id.txtParam1);
				TextView txtParam2 = (TextView) route.findViewById(R.id.txtParam2);
				txtParam1.setText((char)('A'+2*i)+"");
				txtParam2.setText((char)('A'+2*i)+"");
				txtParamName1.setText("");
				txtParamName2.setText("");
				txtParamName1.setHint("인자 값");
				txtParamName2.setHint("인자 값");
				txtParamName1.setFocusable(false);
				txtParamName2.setFocusable(false);
				txtParamName1.setEnabled(false);
				txtParamName2.setEnabled(false);
				if(i==(condition_type.parameter_types.size()+1)/2-1 && condition_type.parameter_types.size()%2!=0){
					txtParam2.setVisibility(View.INVISIBLE);
					txtParamName2.setVisibility(View.INVISIBLE);
				}
				inLayout.addView(route);
			}
		}
		imgLikePlus.setTag("2");
		imgLikePlus.setImageResource(R.drawable.gray_heart);
		imgLikePlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (imgLikePlus.getTag() == "2") {
					imgLikePlus.setImageResource(R.drawable.red_heart);
					int num = Integer.parseInt(txtLikeNum.getText()
							.toString());
					num += 1;
					imgLikePlus.setTag("1");
					txtLikeNum.setText("" + (num));
				} else {
					imgLikePlus.setImageResource(R.drawable.gray_heart);
					int num = Integer.parseInt(txtLikeNum.getText()
							.toString());
					num -= 1;
					imgLikePlus.setTag("2");
					txtLikeNum.setText("" + (num));
				}

				// TODO Auto-generated method stub

			}
		});
	}
}
