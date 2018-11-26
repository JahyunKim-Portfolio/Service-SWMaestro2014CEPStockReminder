package com.example.product;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;

public class ActivityCondition extends FragmentActivity {
	FragmentManager fm; //onCreate에서 초기화
	
	static ArrayList<predefined_condition_type> pred_cond_list;
	static ArrayList<condition_type> cond_list;
	static ArrayList<condition_type> cond_list_jaemu;
	static ArrayList<condition_type> cond_list_sise;
	static ArrayList<condition_type> cond_list_kisool;
	static ArrayList<condition_type> cond_list_pattern;

	static MyArrayAdapter_PRED_COND pred_cond_adapter;
	static MyArrayAdapter_COND cond_adapter_jaemu;
	static MyArrayAdapter_COND cond_adapter_sise;
	static MyArrayAdapter_COND cond_adapter_kisool;
	static MyArrayAdapter_COND cond_adapter_pattern;
	
	//전달만 한다.
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		//do something
		//Toast.makeText(this, "onActivity_result", 0).show();
		if(requestCode == 0) {
			if(resultCode == RESULT_OK) {
				//Toast.makeText(this, "onActivity_result result OK", 0).show();
				
				setResult(resultCode, data);
				finish();
			}
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);		
		
		
		
		/* 프래그먼트 관리 */
		fm = getSupportFragmentManager();
		/* 부모의 id로 fragment를 찾기, fragment가 없으면 추가 */
		
		
		if(fm.findFragmentById(R.id.frame) == null) {
			FragmentEasyCondition fg_easy = new FragmentEasyCondition();
			fm.beginTransaction().add(R.id.frame, fg_easy, "fragment_easy_condition").commit();
		}
		
		

		if(pred_cond_list == null) {
			if(MyDataBase.pred_cond_list != null) {
				pred_cond_list = MyDataBase.pred_cond_list;
			}
			else {
				pred_cond_list = new ArrayList<predefined_condition_type>();
				MyDataBase.getAvailable_predefined_conditions(
						new Response.Listener<available_predefined_conditions>() {
							@Override
							public void onResponse(available_predefined_conditions response) {
								pred_cond_list.addAll(response.available_predefined_conditions);
								// 데이터 베이스에 있는 자료도 업데이트
								MyDataBase.pred_cond_list = response.available_predefined_conditions;
								if(pred_cond_adapter != null) {
									pred_cond_adapter.notifyDataSetChanged();				
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
		}
		if(cond_list == null) {
			if(MyDataBase.cond_list != null) {
				cond_list = MyDataBase.cond_list;
			}
			else {
				cond_list = new ArrayList<condition_type>();
				MyDataBase.getAvailable_conditions(
						new Response.Listener<available_conditions>() {
							@Override
							public void onResponse(available_conditions response) {
								cond_list.addAll(response.available_conditions);
								// 데이터 베이스에 있는 자료도 업데이트
								MyDataBase.cond_list = cond_list;
							}
						}, 
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("product", "error!" + error.getMessage());
							}
						});
			}
			cond_list_jaemu = new ArrayList<condition_type>();
			cond_list_sise = new ArrayList<condition_type>();
			cond_list_kisool = new ArrayList<condition_type>();
			cond_list_pattern = new ArrayList<condition_type>();
			
			for(int i=0; i<cond_list.size(); ++i) {							
				condition_type data = cond_list.get(i);
				if(data.category.equals("재무분석")) {
					cond_list_jaemu.add(data);
				}
				else if(data.category.equals("시세분석")) {
					cond_list_sise.add(data);
				}
				else if(data.category.equals("기술분석")) {
					cond_list_kisool.add(data);
				}
				else if(data.category.equals("패턴분석")) {
					cond_list_pattern.add(data);
				}
			}
			if(cond_adapter_jaemu != null) {
				cond_adapter_jaemu.notifyDataSetChanged();
			}
			if(cond_adapter_sise != null) {
				cond_adapter_sise.notifyDataSetChanged();
			}
			if(cond_adapter_kisool != null) {
				cond_adapter_kisool.notifyDataSetChanged();
			}
			if(cond_adapter_pattern != null) {
				cond_adapter_pattern.notifyDataSetChanged();
			}	
		}
		
	}
	
	public void tabButtonOnClick(View view) {
		
		Fragment fragment = fm.findFragmentById(R.id.frame);
		
		//여기로 진입하면 에러임
		if(fragment == null) {
			FragmentEasyCondition fg_easy = new FragmentEasyCondition();	
			fm.beginTransaction().add(R.id.frame, fg_easy, "fragment_easy_condtion").commit();
			return;
		}
		
		switch(view.getId()) {
			case R.id.easy_condition_bt :
				if(fragment.getTag() == "fagment_easy_condition") {
					//do nothing
				}
				else {
					/*다른 fragment로 변환 */
					/* fragment를 매번 새로 생성하는데 만들어놓고 Visible만 바꿔서 성능을 향상시키는 것도 고려 */
					FragmentEasyCondition fg_easy = new FragmentEasyCondition();
					fm.beginTransaction().replace(R.id.frame, fg_easy, "fragment_easy_condtion").commit();
				}
				
				break;
				
			case R.id.detail_condition_bt :
				if(fragment.getTag() == "fragment_detail_condition") {
					//do nothing
				}
				else {
					/*다른 fragment로 변환 */
					/* fragment를 매번 새로 생성하는데 만들어놓고 Visible만 바꿔서 성능을 향상시키는 것도 고려 */
					
					FragmentDetailCondition fg_detail = new FragmentDetailCondition();
					fm.beginTransaction().replace(R.id.frame, fg_detail, "fragment_detail_condition").commit();
				}
				break;
		
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class MyArrayAdapter_PRED_COND extends ArrayAdapter<predefined_condition_type> {
		private ArrayList<predefined_condition_type> items;
		private Context context;
		private int resource;

		public MyArrayAdapter_PRED_COND(Context context, int viewResourceId, ArrayList<predefined_condition_type> items) {
			super(context, viewResourceId, items);
			this.items = items;
			this.context = context;
			resource = viewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			RelativeLayout v = (RelativeLayout)convertView;
			ViewHolder vh;
			if (v == null) {
				v = (RelativeLayout)View.inflate(context, resource, null);
				vh = new ViewHolder();
				
				vh.image= (ImageView)v.findViewById(R.id.image);	
				vh.type_color = (View)v.findViewById(R.id.type_color);
				vh.cond_name= (TextView)v.findViewById(R.id.condition_name);
				vh.rank = (TextView)v.findViewById(R.id.rank);
				vh.detail = (TextView)v.findViewById(R.id.detail);
				vh.user_cnt = (TextView)v.findViewById(R.id.people_cnt);
				vh.love_cnt = (TextView)v.findViewById(R.id.love_cnt);
				
				v.setTag(vh);
			}
			else {
				vh = (ViewHolder)v.getTag();
			}
			predefined_condition_type data = items.get(position);
			
			/*이미지 변경 코드 작성할 것 */
			
			//이미지 밑에 색을 변경
			//pred_cond는 항상 condition_type_easy이다.
			vh.type_color.setBackgroundResource(R.color.condition_type_easy);
			/*
			if(data.cond_type == ConditionAbstract.EASY) {
				vh.type_color.setBackgroundResource(R.color.condition_type_easy);
			}
			else if(data.cond_type == ConditionAbstract.HARD) {
				vh.type_color.setBackgroundResource(R.color.condition_type_hard);
			}
			*/
			
			// 조건 이름 설정
			vh.cond_name.setText(data.name);
			
			// 조건 구성 설정
			String str = "";
			ArrayList<predefined_condition_parameter_type> param_list = data.parameter_types;
			for(int i=0; i<param_list.size(); ++i) {
				if(i == param_list.size() - 1) {
					str += param_list.get(i).name;
				}
				else {
					str += param_list.get(i).name + ", ";
				}
			}
			vh.detail.setText(str);
			
			// 순위 설정
			vh.rank.setText(Integer.toString(position + 1));
			
			// 사람 숫자 설정
			vh.user_cnt.setText(Integer.toString(data.number_of_users_add_me));
			
			// 사랑 숫자 설정
			vh.love_cnt.setText(Integer.toString(data.number_of_users_like_me));

			
			return v;
		}
	}

	public static class MyArrayAdapter_COND extends ArrayAdapter<condition_type> {
		private ArrayList<condition_type> items;
		private Context context;
		private int resource;

		public MyArrayAdapter_COND(Context context, int viewResourceId, ArrayList<condition_type> items) {
			super(context, viewResourceId, items);
			this.items = items;
			this.context = context;
			resource = viewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			RelativeLayout v = (RelativeLayout)convertView;
			ViewHolder vh;
			if (v == null) {
				v = (RelativeLayout)View.inflate(context, resource, null);
				vh = new ViewHolder();

				vh.image= (ImageView)v.findViewById(R.id.image);	
				vh.type_color = (View)v.findViewById(R.id.type_color);
				vh.cond_name= (TextView)v.findViewById(R.id.condition_name);
				vh.rank = (TextView)v.findViewById(R.id.rank);
				vh.detail = (TextView)v.findViewById(R.id.detail);
				vh.user_cnt = (TextView)v.findViewById(R.id.people_cnt);
				vh.love_cnt = (TextView)v.findViewById(R.id.love_cnt);

				v.setTag(vh);
			}
			else {
				vh = (ViewHolder)v.getTag();
			}
			condition_type data = items.get(position);

			/*이미지 변경 코드 작성할 것 */

			//이미지 밑에 색을 변경
			//cond는 항상 condition_type_hard이다.
			vh.type_color.setBackgroundResource(R.color.condition_type_hard);
			/*
				if(data.cond_type == ConditionAbstract.EASY) {
					vh.type_color.setBackgroundResource(R.color.condition_type_easy);
				}
				else if(data.cond_type == ConditionAbstract.HARD) {
					vh.type_color.setBackgroundResource(R.color.condition_type_hard);
				}
			 */

			// 조건 이름 설정
			vh.cond_name.setText(data.name);

			// 조건 구성 설정
			vh.detail.setText(data.description_of_parameters);

			// 순위 설정
			vh.rank.setText(Integer.toString(position + 1));

			// 사람 숫자 설정
			vh.user_cnt.setText(Integer.toString(data.number_of_users_add_me));

			// 사랑 숫자 설정
			vh.love_cnt.setText(Integer.toString(data.number_of_users_like_me));


			return v;
		}
	}
	
	public static class ViewHolder {		
		ImageView image;
		View type_color;
		TextView cond_name;
		TextView rank;
		TextView detail;
		TextView user_cnt;
		TextView love_cnt;
	}

	
	/***********************************************
	 * 
	 * 간단조건 Fragment
	 * @author Namhoon
	 *
	 ***********************************************/
	public static class FragmentEasyCondition extends ListFragment {
		
		
		
		/*아래 주석은 나중에 검토 하자 아이템을 클릭하였을때 어떻게 할 것인가 하는 부분임 */
		
		/* Item 이 클릭되었을 때, 새로운 Activity를 띄운다(예정)
		 * 이 동작을 Fragment에서 구현하지 않고 정석적인 방법으로 Activity에 Listener 구현을 강제하여 
		 * Activity 차원에서 다양한 처리를 할 수 있게 한다.
		 * 
		 * 하지만 복잡하므로 그냥 이 Fragment에서 구현
		 */		
		/*
		OnItemClickedListener mHost;
		
		public interface OnItemClickedListener {
			public void onItemClicked(int index);
		}
		
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			
			try {
				mHost = (OnItemClickedListener)activity;				
			} catch (ClassCastException e) {
				throw new ClassCastException("activity must implement OnItemClickedListener");
			}
		}		
		*/
		

		
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			/*for test *?
			 * 
			 */

			if(pred_cond_list != null) {
			predefined_condition_type data = new predefined_condition_type();
			data.id = 1;
			data.description = "testDescription 입니다.";
			data.name = "소프트웨어마에스트로";
			data.number_of_users_add_me = 100;
			data.number_of_users_like_me = 887;
			data.parameter_types = new ArrayList<predefined_condition_parameter_type>();
			ArrayList<predefined_condition_parameter_type> param = data.parameter_types;
			predefined_condition_parameter_type t = new predefined_condition_parameter_type();
			t.order = 1;
			t.name = "인자조건";
			t.desc = "인자조건의 description 입니다.";
			param.add(t);
			pred_cond_list.add(data);
			}

			


			setListAdapter(pred_cond_adapter = new MyArrayAdapter_PRED_COND(getActivity(), R.layout.activity_condition_item, pred_cond_list));
			
			// divider 세팅 부분. 주석처리 가능
			ListView mListView = getListView();
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setDivider(new ColorDrawable(Color.LTGRAY));
			mListView.setDividerHeight(1);		
		}
		
		public void onListItemClick(ListView l, View v, int position, long id) {			
			//mHost.onItemChecked(position);
			/*
			l.setItemChecked(position, true);
			Intent intent = new Intent(getActivity(), ActivityStockDetail.class);
			startActivity(intent);
			*/
			//Toast.makeText(getActivity(), position+" is clicked", 0).show();
			

			Intent intent = new Intent(getActivity(), OptionDetail.class);
			
			//인텐트에 클릭된 신호의 조건이름 정보를 전달
			
			predefined_condition_type data = pred_cond_list.get(position);
			
			
			Gson gson = new Gson();
			String data_string = gson.toJson(data);
			
			intent.putExtra("type", "easy");
			intent.putExtra("predefined_condition_type", data_string);
			getActivity().startActivityForResult(intent, 0);
			//Toast.makeText(getActivity(), position+" is clicked", 0).show();		
		}
		
		
		//Activity 단위로 바꿈.
		//주석처리
		/*
		//전달만 한다.
		@Override
		public void onActivityResult (int requestCode, int resultCode, Intent data) {
			//do something
			
			if(requestCode == 0) {
				if(resultCode == RESULT_OK) {
					getActivity().setResult(resultCode, data);
					getActivity().finish();
				}
			}
		}
		*/
	}
	
	
	
	
	
	/***********************************************
	 * 
	 * 일반조건 Fragment
	 * @author Namhoon
	 *
	 ***********************************************/
	
	public static class FragmentDetailCondition extends Fragment {
		private ViewPager mPager;
		private MyPagerAdapter mPagerAdapter;
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.view_pager, container, false);
			
			mPager = (ViewPager)root.findViewById(R.id.pager);
			mPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
			mPager.setAdapter(mPagerAdapter);

	        TabPageIndicator mIndicator = (TabPageIndicator)root.findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
			
			return root;
		}
		
	/*********************************************************************************************************************
	//
//					뷰페이저 어댑터
	//
	*********************************************************************************************************************/
		private class MyPagerAdapter extends FragmentStatePagerAdapter {
			private static final int NUM_PAGES = 4;
			private final FragmentManager fm;

			public MyPagerAdapter(FragmentManager fm) {
				super(fm);
				this.fm = fm;
			}
			@Override
			public int getCount() {
				return NUM_PAGES;
			}

			@Override
			public Fragment getItem(int position) {
				switch(position) {
					case 0 :
						return new FragmentDetailConditionList((String)getPageTitle(position));
					case 1 :
						return new FragmentDetailConditionList((String)getPageTitle(position));
					case 2 :
						return new FragmentDetailConditionList((String)getPageTitle(position));
					case 3 :
						return new FragmentDetailConditionList((String)getPageTitle(position));				
					default :
						return null;
				}
			}
			
			@Override
			public CharSequence getPageTitle(int position) {
				switch(position) {
					case 0 :
						return "재무";
					case 1 :
						return "시세";
					case 2 :
						return "기술";
					case 3 :
						return "패턴";
					default :
						return "";
				}
			}
			
		}

	/*********************************************************************************************************************
	//
//					일반조건 - List
	//
	*********************************************************************************************************************/	
		/* 전체 */
		public static class FragmentDetailConditionList extends ListFragment {
			String myCategory;
			
			/* Item 이 클릭되었을 때, 새로운 Activity를 띄운다(예정)
			 * 이 동작을 Fragment에서 구현하지 않고 정석적인 방법으로 Activity에 Listener 구현을 강제하여 
			 * Activity 차원에서 다양한 처리를 할 수 있게 한다.
			 * 
			 * 하지만 복잡하므로 그냥 이 Fragment에서 구현
			 */		
			/*
			OnItemClickedListener mHost;
			
			public interface OnItemClickedListener {
				public void onItemClicked(int index);
			}
			
			public void onAttach(Activity activity) {
				super.onAttach(activity);
				
				try {
					mHost = (OnItemClickedListener)activity;				
				} catch (ClassCastException e) {
					throw new ClassCastException("activity must implement OnItemClickedListener");
				}
			}		
			*/
			
			public FragmentDetailConditionList() {
				super();
			}
			
			public FragmentDetailConditionList(String pageTitle) {
				super();
				myCategory = pageTitle;			
			}
			
			
			public void onActivityCreated(Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				

				
				if(myCategory == "재무") {
					/* 테스트 코드 */
					if(cond_list_jaemu != null) {
						condition_type data = new condition_type();
						data.id = 1;
						data.description = "testDescription 입니다.";
						data.name = "소프트웨어마에스트로";
						data.description_of_parameters = "A봉전 B 어쩌고 C 는 D";
						data.category = "재무";
						data.number_of_users_add_me = 220;
						data.number_of_users_like_me = 111;
						data.parameter_types = new ArrayList<condition_parameter_type>();
						ArrayList<condition_parameter_type> param = data.parameter_types;					
						for(int i=0; i<3; ++i) {
							condition_parameter_type t = new condition_parameter_type();						
							t.order = i+1;
							t.type = "타입"+i;
							t.initial_value = "기본값"+i;
							t.candidate_values = "후보값"+i;
							param.add(t);
						}
						cond_list_jaemu.add(data);
					}
					
					
					setListAdapter(cond_adapter_jaemu = new MyArrayAdapter_COND(getActivity(), R.layout.activity_condition_item, cond_list_jaemu));
				}
				else if(myCategory == "시세") {
					setListAdapter(cond_adapter_sise = new MyArrayAdapter_COND(getActivity(), R.layout.activity_condition_item, cond_list_sise));
				}
				else if(myCategory == "기술") {
					setListAdapter(cond_adapter_kisool = new MyArrayAdapter_COND(getActivity(), R.layout.activity_condition_item, cond_list_kisool));
				}
				else if(myCategory == "패턴") {
					setListAdapter(cond_adapter_pattern = new MyArrayAdapter_COND(getActivity(), R.layout.activity_condition_item, cond_list_pattern));
				}
				else {
					//default
					setListAdapter(cond_adapter_jaemu = new MyArrayAdapter_COND(getActivity(), R.layout.activity_condition_item, cond_list_jaemu));
				}

				// divider 세팅 부분. 주석처리 가능
				ListView mListView = getListView();
				mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				mListView.setDivider(new ColorDrawable(Color.LTGRAY));
				mListView.setDividerHeight(1);		
			}
			
			public void onListItemClick(ListView l, View v, int position, long id) {			
				//mHost.onItemChecked(position);
				/*
				l.setItemChecked(position, true);
				Intent intent = new Intent(getActivity(), ActivityStockDetail.class);
				startActivity(intent);
				*/
				
				//Toast.makeText(getActivity(), position+" is clicked", 0).show();
				


				//Toast.makeText(getActivity(), position+" is clicked", 0).show();		
				

				Intent intent = new Intent(getActivity(), OptionDetail.class);
				
				condition_type data;				
				//인텐트에 클릭된 신호의 조건이름 정보를 전달
				if(myCategory == "재무") {
					data = cond_list_jaemu.get(position);
				}
				else if(myCategory == "시세") {
					data = cond_list_sise.get(position);
				}
				else if(myCategory == "기술") {
					data = cond_list_kisool.get(position);
				}
				else if(myCategory == "패턴") {
					data = cond_list_pattern.get(position);
				}
				else {
					data = cond_list_jaemu.get(position);
				}
				
				Gson gson = new Gson();
				String data_string = gson.toJson(data);
				
				intent.putExtra("type", "hard");
				intent.putExtra("condition_type", data_string);
				getActivity().startActivityForResult(intent, 0);
				
			}
			
			// Activity 단위로 바꾸었다.
			//따라서 주석처리
			/*
			//전달만 한다.
			@Override
			public void onActivityResult (int requestCode, int resultCode, Intent data) {
				//do something
				Toast.makeText(getActivity(), "onActivity_result", 0).show();
				if(requestCode == 0) {
					if(resultCode == RESULT_OK) {
						Toast.makeText(getActivity(), "onActivity_result result OK", 0).show();
						getActivity().setResult(resultCode, data);
						getActivity().finish();
					}
				}
			}
			*/
		}
	}
}
