
package com.example.product;

import java.lang.reflect.Field;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class MyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		setDefaultFont(this, "MONOSPACE", "NanumBarunGothic.ttf");
		MyDataBase.initialize(this);
	}
	
	
	
	
	//전체적인 font 바꾸기
	//참고사이트
	//http://stackoverflow.com/questions/2711858/is-it-possible-to-set-font-for-entire-application?rq=1
	//http://app.e-mirim.hs.kr/xe/board_BTqO41/8684
    public static void setDefaultFont(Context context,
            String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
            final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

