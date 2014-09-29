package com.lxf.weatherdemo.util;

public interface HttpCallbackListener {
	public void onFinish(String string);
	
	public void onError(Exception e);
}
