package com.example.product;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ActivityPopUp extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//remove title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_popup);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		//popup 에 글씨들 설정하기
		((TextView)findViewById(R.id.stock_name)).setText(extras.getString("stock_name"));
		((TextView)findViewById(R.id.signal_name)).setText(extras.getString("signal_name"));
		
		
        // 이 부분이 바로 화면을 깨우는 부분 되시겠다.
        // 화면이 잠겨있을 때 보여주기
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
	            // 키잠금 해제하기
	            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
	            // 화면 켜기
	            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	    
	    
	    //일회성이기 떄문에 리시버는 onCreate 에서 등록한다.
	    registerReceiver(mReceiver, filter); 
	}
	
	
	//인텐트를 다시 받기 위한 함수.
	@Override
	protected void onNewIntent(Intent intent) {
	       super.onNewIntent(intent);
	       setIntent(intent);
	       Bundle extras = intent.getExtras();

	       //popup 에 글씨들 설정하기
	       ((TextView)findViewById(R.id.stock_name)).setText(extras.getString("stock_name"));
	       ((TextView)findViewById(R.id.signal_name)).setText(extras.getString("signal_name"));
	}
	
	public void button_onclick(View v) {
		switch(v.getId()) {
			case R.id.close_bt :
				finish();
				break;
			case R.id.open_bt :
                Intent intent= new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 그리고 호출한다.
                this.startActivity(intent);
                finish();
				break;
			default :
				break;
		}
	}
	
	// 화면이 꺼졌을 때, 액티비티 종료 시켜줌.
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				finish(); //액티비티 종료코드 작성
			}
		}
	};
	IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
	

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
