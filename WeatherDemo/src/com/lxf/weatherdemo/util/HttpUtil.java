package com.lxf.weatherdemo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	protected static final int CONNECT_TIMEOUT = 8 * 1000;
	protected static final int READ_TIMEOUT = 8 * 1000;
	protected static final String REQUEST_METHOD_GET = "GET";

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();

					connection.setRequestMethod(REQUEST_METHOD_GET);
					connection.setConnectTimeout(CONNECT_TIMEOUT);
					connection.setReadTimeout(READ_TIMEOUT);

					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder builder = new StringBuilder();

					String line = null;
					while (null != (line = reader.readLine())) {
						builder.append(line);
					}

					if (null != listener) {
						listener.onFinish(builder.toString());
					}

				} catch (Exception e) {
					e.printStackTrace();
					if (null != listener) {
						listener.onError(e);
					}
				} finally {
					if (null != connection) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
