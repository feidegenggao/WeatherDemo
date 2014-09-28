package com.lxf.weatherdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.lxf.weatherdemo.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_layout);
	}

}
