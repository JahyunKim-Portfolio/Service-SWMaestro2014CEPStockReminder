package com.example.product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


// 데이터 베이스 코드는 전부 Static으로 선언하여 어디서든지 접근할 수 있게 한다.
public class MyDataBase {
/*
	// 받은 시그널 정보
	static ArrayList<signal_result> signal_list_all;
	static ArrayList<signal_result> signal_list_total;
	static ArrayList<signal_result> signal_list_indiv;
	static ArrayList<signal_result> signal_list_alarm;
	
	// 받은 시그널 종목별
	static ArrayList<signal_results_of_stock_item> signal_by_stock_list_all;
	static ArrayList<signal_results_of_stock_item> signal_by_stock_list_total;
	static ArrayList<signal_results_of_stock_item> signal_by_stock_list_indiv;
	static ArrayList<signal_results_of_stock_item> signal_by_stock_list_alarm;
	
	// 받은 시그널 정보 신호별
	static ArrayList<signal_results_signal_condition> signal_by_cond_list_all;
	static ArrayList<signal_results_signal_condition> signal_by_cond_list_total;
	static ArrayList<signal_results_signal_condition> signal_by_cond_list_indiv;
	static ArrayList<signal_results_signal_condition> signal_by_cond_list_alarm;

	*/
	
	//설정된 신호 목록 (전체)
	static ArrayList<user_signal_condition> my_condition_total;

	//선택가능한 신호 목록
	static ArrayList<predefined_condition_type> pred_cond_list;
	static ArrayList<condition_type> cond_list;
	
	
	//전체 종목 이름과 코드 정보 < 검색용 >
	static ArrayList<stock_item> stock_item_list;
	
	
	//내종목
	
	
	static Context my_context; // application context 이다.
	static RequestQueue queue;
	
	static String device_id;
	
	static int limit = 10;
	static String URL = "http://14.63.165.145:5000/";
	static String users;

	//이 코드는 Application 에서 onCreate할떄 실행
	
	
	// 초기화
	public static void initialize(Context context) {	
		my_context = context;
		queue = Volley.newRequestQueue(my_context);
		TelephonyManager tm =(TelephonyManager)my_context.getSystemService(Context.TELEPHONY_SERVICE);
		device_id = tm.getDeviceId();
		users = "users/" + device_id +"/" ;
	}
	
	// 전체 대상 시그널 리스트를 받아옴
	public static void updateSignal_list(int group, int before_after, int last_position, 
					Response.Listener<signal_results> resp_listener, 
					Response.ErrorListener resp_error_listener) {		
		
		String my_url = URL;	
		my_url += users +"signals?first_filter=";
        String first_filter = "total";
        my_url += first_filter;
        my_url +="&second_filter=";
        String second_filter;
        switch(group) {
        	case 0 :
        		second_filter = "total";
        		break;
        	case 1 :
        		second_filter = "to_all_stock_items";
        		break;
        	case 2 :
        		second_filter = "to_specific_stock_item";
        		break;
        	case 3 :
        		second_filter = "alarm";
        		break;
        	default :
        		second_filter = "total";
        		Log.e("product", "method updateSignal_list parameter group has invalid value");
        		break;        		
        }    
        my_url += second_filter;

        switch(before_after) {
	        case -1 : // default 첫 시작할 때 최신 갯수 받아올때,
	        	my_url += "&limit="+limit;
	        	break;
	        case 0 : //before
	        	my_url += "&limit="+limit+"&before="+last_position;
	        	break;
	        case 1 : //after
	        	my_url += "&after="+last_position;
	        	break;
        }


        GsonRequest<signal_results> myReq = new GsonRequest<signal_results>(
                my_url,
                signal_results.class,
                null,
                resp_listener,
                resp_error_listener
                );
        queue.add(myReq);
	}
	
	// 신호별 시그널 리스트를 받아옴
	public static void updateSignal_by_cond_list(int group,
			Response.Listener<signal_results_signal_conditions> resp_listener, 
			Response.ErrorListener resp_error_listener) {		

		String my_url = URL;	
		my_url += users +"signals?first_filter=";
		String first_filter = "signal";
		my_url += first_filter;
		my_url +="&second_filter=";
		String second_filter;
		switch(group) {
		case 0 :
			second_filter = "total";
			break;
		case 1 :
			second_filter = "to_all_stock_items";
			break;
		case 2 :
			second_filter = "to_specific_stock_item";
			break;
		case 3 :
			second_filter = "alarm";
			break;
		default :
			second_filter = "total";
			Log.e("product", "method updateSignal_list parameter group has invalid value");
			break;        		
		}    
		my_url += second_filter;


		GsonRequest<signal_results_signal_conditions> myReq = new GsonRequest<signal_results_signal_conditions>(
				my_url,
				signal_results_signal_conditions.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);
	}
	
	// 종목별 시그널 리스트를 받아옴
	public static void updateSignal_by_stock_list(int group,
			Response.Listener<signal_results_of_stock_items> resp_listener, 
			Response.ErrorListener resp_error_listener) {		

		String my_url = URL;	
		my_url += users +"signals?first_filter=";
		String first_filter = "stock_item";
		my_url += first_filter;
		my_url +="&second_filter=";
		String second_filter;
		switch(group) {
		case 0 :
			second_filter = "total";
			break;
		case 1 :
			second_filter = "to_all_stock_items";
			break;
		case 2 :
			second_filter = "to_specific_stock_item";
			break;
		case 3 :
			second_filter = "alarm";
			break;
		default :
			second_filter = "total";
			Log.e("product", "method updateSignal_list parameter group has invalid value");
			break;        		
		}    
		my_url += second_filter;


		GsonRequest<signal_results_of_stock_items> myReq = new GsonRequest<signal_results_of_stock_items>(
				my_url,
				signal_results_of_stock_items.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);
	}
	
	// 기본 조건들의 리스트를 받아옴
	public static void getAvailable_predefined_conditions(
			Response.Listener<available_predefined_conditions> resp_listener, 
			Response.ErrorListener resp_error_listener) {
		String my_url = URL+users+"available_predefined_conditions";
		//for example http://14.63.165.145:5000/21354321654/available_predefined_conditions"

		GsonRequest<available_predefined_conditions> myReq = new GsonRequest<available_predefined_conditions>(
				my_url,
				available_predefined_conditions.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);
	}
	
	// 고급 조건들의 리스트를 받아옴
	public static void getAvailable_conditions(
			Response.Listener<available_conditions> resp_listener, 
			Response.ErrorListener resp_error_listener) {
		String my_url = URL+users+"available_conditions";
		//for example http://14.63.165.145:5000/21354321654/available_conditions"
		
		GsonRequest<available_conditions> myReq = new GsonRequest<available_conditions>(
				my_url,
				available_conditions.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);		
	}
	
	
	// 전체를 대상으로 설정한 조건들을 받아오기
	public static void getSignal_conditions(
			Response.Listener<signal_conditions> resp_listener, 
			Response.ErrorListener resp_error_listener) 
	{		
		String my_url = URL+users+"conditions";
		GsonRequest<signal_conditions> myReq = new GsonRequest<signal_conditions>(
				my_url,
				signal_conditions.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);			
	}
	
	// 종목 상세 페이지 받아오기
	public static void getStock_detail(int stock_item_id,
			Response.Listener<stock_detail> resp_listener, 
			Response.ErrorListener resp_error_listener) 
	{
		String my_url = URL+users+"stocks/"+stock_item_id;	
		GsonRequest<stock_detail> myReq = new GsonRequest<stock_detail>(
				my_url,
				stock_detail.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);			
	}
	
	// 신호 상세 페이지 받아오기
	public static void getSignal_condition_detail(int user_signal_condition_id,
			Response.Listener<signal_condition_detail> resp_listener, 
			Response.ErrorListener resp_error_listener) 
	{
		String my_url = URL+users+"conditions/"+user_signal_condition_id;	
		GsonRequest<signal_condition_detail> myReq = new GsonRequest<signal_condition_detail>(
				my_url,
				signal_condition_detail.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);			
	}
	
	// 특정 종목 대상 시그널 리스트를 받아옴
	public static void getStockSignal_list(int group, int before_after, int last_position, int id, 
					Response.Listener<signal_results> resp_listener, 
					Response.ErrorListener resp_error_listener) {		
		
		String my_url = URL;	
		my_url += users +"signals?first_filter=";
        String first_filter = "stock_item";
        my_url += first_filter;
        my_url +="&second_filter=";
        String second_filter;
        switch(group) {
        	case 0 :
        		second_filter = "total";
        		break;
        	case 1 :
        		second_filter = "to_all_stock_items";
        		break;
        	case 2 :
        		second_filter = "to_specific_stock_item";
        		break;
        	case 3 :
        		second_filter = "alarm";
        		break;
        	default :
        		second_filter = "total";
        		Log.e("product", "method updateSignal_list parameter group has invalid value");
        		break;        		
        }    
        my_url += second_filter;

        switch(before_after) {
	        case -1 : // default 첫 시작할 때 최신 갯수 받아올때,
	        	my_url += "&limit="+limit;
	        	break;
	        case 0 : //before
	        	my_url += "&limit="+limit+"&before="+last_position;
	        	break;
	        case 1 : //after
	        	my_url += "&after="+last_position;
	        	break;
        }
        my_url += "&id=" + id;

        GsonRequest<signal_results> myReq = new GsonRequest<signal_results>(
                my_url,
                signal_results.class,
                null,
                resp_listener,
                resp_error_listener
                );
        queue.add(myReq);
	}

	
	// 특정 조건 대상 시그널 리스트를 받아옴
	public static void getCondSignal_list(int group, int before_after, int last_position, int id, 
					Response.Listener<signal_results> resp_listener, 
					Response.ErrorListener resp_error_listener) {		
		
		String my_url = URL;	
		my_url += users +"signals?first_filter=";
        String first_filter = "stock_item";
        my_url += first_filter;
        my_url +="&second_filter=";
        String second_filter;
        switch(group) {
        	case 0 :
        		second_filter = "total";
        		break;
        	case 1 :
        		second_filter = "to_all_stock_items";
        		break;
        	case 2 :
        		second_filter = "to_specific_stock_item";
        		break;
        	case 3 :
        		second_filter = "alarm";
        		break;
        	default :
        		second_filter = "total";
        		Log.e("product", "method updateSignal_list parameter group has invalid value");
        		break;        		
        }    
        my_url += second_filter;

        switch(before_after) {
	        case -1 : // default 첫 시작할 때 최신 갯수 받아올때,
	        	my_url += "&limit="+limit;
	        	break;
	        case 0 : //before
	        	my_url += "&limit="+limit+"&before="+last_position;
	        	break;
	        case 1 : //after
	        	my_url += "&after="+last_position;
	        	break;
        }
        my_url += "&id=" + id;

        GsonRequest<signal_results> myReq = new GsonRequest<signal_results>(
                my_url,
                signal_results.class,
                null,
                resp_listener,
                resp_error_listener
                );
        queue.add(myReq);
	}
	
	
	// 내종목
	public static void getMy_Stock(int group, 
			Response.Listener<signal_results_of_stock_items> resp_listener, 
			Response.ErrorListener resp_error_listener) {		

		String my_url = URL;	
		my_url += users +"signals?option=my_stocks";
		
		String second_filter = "";
		if(group == 0) { //all
			second_filter = "total";
		}
		else if(group == 3) { // alarm
			second_filter = "alarm";
		}
		
		my_url += "&second_filter="+second_filter;

		GsonRequest<signal_results_of_stock_items> myReq = new GsonRequest<signal_results_of_stock_items>(
				my_url,
				signal_results_of_stock_items.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);
	}
	
	// 주식 종목 목록 얻기
	public static void getStock_list(
			Response.Listener<stock_items> resp_listener, 
			Response.ErrorListener resp_error_listener) {		

		String my_url = URL + "stocks";	

		GsonRequest<stock_items> myReq = new GsonRequest<stock_items>(
				my_url,
				stock_items.class,
				null,
				resp_listener,
				resp_error_listener
				);
		queue.add(myReq);
	}
	

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
// POST, PUT, DELETE 는 response처리를 일단 로그만 작성하므로, 같은 response listener
// 를 쓰기로 하자.
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 디바이스 등록
	// MainActivity 에 sendRegistrationIdToBackend() 에 있다.
	
	//알람 ON/OFF 설정 변경 보내기
	public static void putAlarmChange(int condition_id, int alarm) 
	{
		String my_url = URL + users + "conditions/" + condition_id;	
		
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("alarm", "" + alarm);
		JsonObjectRequest myReq = new JsonObjectRequest(
		        Request.Method.PUT,
		        my_url,
		        new JSONObject(jsonParams),
		 
		        new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {
			            // response
			            Log.d("debug", "success" + response.toString());
		            }
		        },
		        new Response.ErrorListener() {
		            @Override
		            public void onErrorResponse(VolleyError error) {
			             // error
			             Log.d("debug", "error!  " + error.getMessage());
		            }
		        }) {
		 
		    @Override
		    public Map<String, String> getHeaders() throws AuthFailureError {
		        HashMap<String, String> headers = new HashMap<String, String>();
		        headers.put("Content-Type", "application/json; charset=utf-8");
		        headers.put("User-agent", "My useragent");
		        return headers;
		    }
		};
		queue.add(myReq);				
	}
	
	public static void deleteCondition(int condition_id) 
	{
		String my_url = URL + users + "conditions/" + condition_id;	
		StringRequest dr = new StringRequest(Request.Method.DELETE, my_url, 
			    new Response.Listener<String>() 
			    {
			        @Override
			        public void onResponse(String response) {
			            // response
			            // response
			            Log.d("debug", "success" + response.toString());
			        }
			    }, 
			    new Response.ErrorListener() 
			    {
			         @Override
			         public void onErrorResponse(VolleyError error) {
			             // error.
			             Log.d("debug", "error!  " + error.getMessage());            
			       }
			    }
			);
			queue.add(dr);
	}
	
	public static void postSearch_factor(JSONObject object,
			Response.Listener<JSONObject> resp_listener, 
			Response.ErrorListener resp_error_listener
			) 
	{
		String my_url = URL + users + "search";
		JsonObjectRequest myReq = new JsonObjectRequest(
		        Request.Method.POST,
		        my_url,
		        object,
		        resp_listener,
				resp_error_listener) 
		{
		 
		    @Override
		    public Map<String, String> getHeaders() throws AuthFailureError {
		        HashMap<String, String> headers = new HashMap<String, String>();
		        headers.put("Content-Type", "application/json; charset=utf-8");
		        headers.put("User-agent", "My useragent");
		        return headers;
		    }
		};
		queue.add(myReq);
	}
	
	public static void postCondition_factor(JSONObject object) 
	{
		String my_url = URL + users + "search";
		JsonObjectRequest myReq = new JsonObjectRequest(
		        Request.Method.POST,
		        my_url,
		        object,
		        new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {
			            // response
			            Log.d("debug", "success" + response.toString());
		            }
		        },
		        new Response.ErrorListener() {
		            @Override
		            public void onErrorResponse(VolleyError error) {
			             // error
			             Log.d("debug", "error!  " + error.getMessage());
		            }
		        }) 
		{
		 
		    @Override
		    public Map<String, String> getHeaders() throws AuthFailureError {
		        HashMap<String, String> headers = new HashMap<String, String>();
		        headers.put("Content-Type", "application/json; charset=utf-8");
		        headers.put("User-agent", "My useragent");
		        return headers;
		    }
		};
		queue.add(myReq);
	}
}






/******************************
 * 
 * 각좀 클래스 정의
 * 
 ******************************/

class Flag {
	//for type
	static final int TYPE_SIGNAL = 0;
	static final int TYPE_INFO = 1;
	//for signal_type
	static final int TYPE_SIGNAL_TOTAL = 0;
	static final int TYPE_SIGNAL_INDIV = 1;
	//for is_new;
	static final int OLD = 0;
	static final int NEW = 1;
	//for condition_type
	static final int EASY = 1;
	static final int HARD = 0;
	static final int CUSTOM = 2;
	//for is_alarm
	static final int IS_NOT_ALARM = 0;
	static final int IS_ALARM = 1;
	
	static final int TOTAL = 0;
	static final int INDIV = 1;
	
	static final int JAEMU = 0;
	static final int SISE = 1;
	static final int KISOOL = 2;
	static final int PATTERN = 3;
}




/* 서버에서 준 데이터 명세 */

// get 할때

class signal_results {
	ArrayList<signal_result> signal_results;
}

// 하나의 시그널 결과 정보
class signal_result {
	int signal_id;
	String date;
	int user_signal_condition_id;
	String user_signal_condition_name;
	int level; // 0 고급조건, 1 기본, 2 CUSTOM
	int applicable_range; // 0 전체종목, 1 특정종목
	int new_signal; // 0 과거 시그널 1 새로운 시그널
	String stock_item_name;
	int stock_item_id;
	int price;
	int entered; // 0 이탈 1 진입
	int alarm; // 0 알람 OFF 1 알람 ON
}

//추가된 시그널 조건 목록 얻기
class signal_conditions {
	ArrayList<user_signal_condition> signal_conditions;
}


// 사용자가 추가한 시그널 조건 정보
class user_signal_condition {
	int id;
	String name;
	int alarm; // 0 알람 OFF 1 알람 ON
	int level; // 0 고급조건, 1 기본조건, 2 CUSTOM
}

//first_filter 가 stock_item 인 경우.
class signal_results_of_stock_items {
	ArrayList<signal_results_of_stock_item> signal_results_of_stock_items;
}

//종목 별 시그널 결과 정보
class signal_results_of_stock_item {
	int stock_item_id;
	String stock_item_name;
	int price;
	int price_gap_of_previous_closing_price;
	double price_rate_of_previous_closing_price;
	int number_of_signals_to_all_stock_items;
	int number_of_signals_to_specific_stock_item;
	ArrayList<signal_result> signal_results;	
}

//first_filter 가 signal인 경우
class signal_results_signal_conditions {
	ArrayList<signal_results_signal_condition> signal_results_signal_conditions;
}

//시그널 조건 별 시그널 결과 정보
class signal_results_signal_condition {
	int user_signal_condition_id;
	String user_signal_condition_name;
	int applicable_range; // 0 전체종목 1 특정 종목
	int number_of_signals;
	int alarm; // 0 알람이 설정되지 않은 경우, 1 알람이 설정된 경우
	int level;
	ArrayList<signal_result> signal_results;
}

//시그널 조건 상세
class signal_condition_detail {
	String user_signal_condition_name;
	int level; // 0 일반조건 1 간단조건 2 조건 2개 이상
	ArrayList<String> included_signal_condition_names;
}

//이용 가능한 기본 조건 list
class available_predefined_conditions {
	ArrayList<predefined_condition_type> available_predefined_conditions;
}

//이용 가능한 기본 조건 
class predefined_condition_type {
	int id;
	String description;
	String name;
	int number_of_users_add_me;
	int number_of_users_like_me;
	ArrayList<predefined_condition_parameter_type> parameter_types;
}

//기본 조건 인자 타입 정보
class predefined_condition_parameter_type {
	int order;
	String name;
	String desc;
}

class available_conditions {
	ArrayList<condition_type> available_conditions;
}
//일반 조건
class condition_type {
	int id;
	String name;
	String description;
	String description_of_parameters;
	String category;
	int number_of_users_add_me;
	int number_of_users_like_me;
	ArrayList<condition_parameter_type> parameter_types;
}

//일반 조건 인자 타입 정보
class condition_parameter_type {
	int order;
	String type;
	String initial_value;
	String candidate_values;
}

//종목 상세
class stock_detail {
	String stock_item_name;
	int price;
	int price_gap_of_previous_closing_price;
	double price_rate_of_previous_closing_price;
	int high_price;
	int low_price;
	int volume;
	int average_volume;
	ArrayList<user_signal_condition> user_signal_conditions;
}

class stock_items {
	ArrayList<stock_item> stock_items;
}

//주식 종목 정보(목록 리스팅 용)
class stock_item {
	int id;
	String name;
	String code;
	String short_code;
}

//검색 결과 목록
class search_results {
	ArrayList<search_result> search_results;
}

//검색 결과
class search_result {
	int stock_item_id;
	String stock_name;
	int price;
	int price_gap_of_previous_closing_price;
	double price_rate_of_previous_closing_price;
}






/////////////////////////////////////////////////////////////////////////////////////
// 이 아래는 POST로 보낼때 필요한 CLASS 들 이다.
/////////////////////////////////////////////////////////////////////////////////////

//시그널 조건 추가
class post_condition_factor {
	String name;
	int level;
	int applicable_range;
	int stock_item_id;
	ArrayList<signal_condition> signal_conditions;
	ArrayList<signal_predefined_condition> signal_predefined_conditions;
}

class signal_condition {
	int condition_type_id;
	ArrayList<signal_condition_parameter> signal_condition_parameters;
}

class signal_predefined_condition {
	int predefined_condition_type_id;
}

class signal_condition_parameter {
	int order;
	String type;
	String value;
}



//상세 조건 검색
class post_search_factor {
	int applicable_range;
	int stock_item_id;
	ArrayList<search_condition> search_conditions;
	ArrayList<search_predefined_condition> search_predefined_conditions;
}

class search_condition {
	int condition_type_id;
	ArrayList<search_condition_parameter> search_condition_parameters;
}

class search_predefined_condition {
	int predefined_condition_type_id;
}

class search_condition_parameter {
	int order;
	String type;
	String value;
}

