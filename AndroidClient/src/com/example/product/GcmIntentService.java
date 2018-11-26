/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.product;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	Log.e(TAG, "GCM_MESSAGE_TYPE_SEND_ERROR");
            	//do nothing           	
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            	Log.e(TAG, "GCM_MESSAGE_TYPE_DELETED");
            	//do nothing         	
                //sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	
            	//항상 어느경우에나 Notification은 보여준다.           	
                // This loop represents the service doing some work.
            	/*
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                */
      	
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());                
                //test용 sendNotification
                sendNotification(extras);
                
                Log.i(TAG, "Received: " + extras.toString());
                
                
                
                
                
            	//스크린이 꺼져 있으면
            	if(!isScreenOn(this)) {
            		
            		Log.i(TAG, "screen is off");

                    //test
                    Intent popupIntent = new Intent(this, ActivityPopUp.class)
                    .putExtras(extras)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // 그리고 호출한다.
                    this.startActivity(popupIntent);
 
            	}
            	//스크린이 켜져 있으면
            	else {
            		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            		List<RunningTaskInfo> runList = am.getRunningTasks(10);
            		ComponentName name = runList.get(0).topActivity;
            		String className = name.getClassName();
            		boolean isAppRunning = false;

            		
            		Log.i(TAG, "className ==" + className);
            		//클래스 이름이 AcitivityPopUp 일때
            		if(className.equals("com.example.product.ActivityPopUp")) {
            			isAppRunning = true;
            		}
            		
            		//팝업이 실행중일때
            		if(isAppRunning == true) {
                		Log.i(TAG, "screen is off, but popup is on");

                        //test
                        Intent popupIntent = new Intent(this, ActivityPopUp.class)
                        .putExtras(extras)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // 그리고 호출한다.
                        this.startActivity(popupIntent);
            			// 앱이 실행중일 경우 로직 구현
            		}
            		//팝업 실행중이 아닐 때
            		else {

            			// 앱이 실행중이 아닐 때 로직 구현

            		}
            	}
            	
            	
            	
            	
            	
            	
            	
                
                
                
                
                
                
                
                
                
                
                
                
                
                
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle extras) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        
        //largeicon 크기에 꼭 맞게 사진 크기를 조절하는 코드
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mylogo_whitebackground);
        Resources res = getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        largeIcon = Bitmap.createScaledBitmap(largeIcon, width, height, false); 

        //나중에는 신호 이름에 맞춰서 큰로고 변경해주기
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.mylogo_whitebackground)
        .setLargeIcon(largeIcon)
        .setTicker(extras.getString("stock_name") + " - " + extras.getString("signal_name"))
        .setContentTitle(extras.getString("stock_name"))       
        //.setStyle(new NotificationCompat.BigTextStyle() //메시지가 짧으므로 BigTextStyle을 쓸 필요가 없다.
        //.bigText(msg))
        //.setWhen() //일단 디펄트값으로 현재 시간이 입력되게끔 하자.
        //새로 들어온 시그널 숫자를 보여주는데 일단 5라고 하자.
        .setNumber(5)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .setAutoCancel(true)
        .setContentText(extras.getString("signal_name"));


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    public static boolean isScreenOn(Context context) {  	
    	return ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn(); 
    }
    
    
}
