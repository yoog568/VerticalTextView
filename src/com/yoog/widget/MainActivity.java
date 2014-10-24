package com.yoog.widget;

import com.yoog.widget.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		VerticalTextView mTextView = new VerticalTextView(this);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
