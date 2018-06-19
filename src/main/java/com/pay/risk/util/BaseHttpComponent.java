package com.pay.risk.util;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 基础HTTP访问封装
 * @author jiang.li
 *
 */
public class BaseHttpComponent {
	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * 执行GET方法
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static String executeGet(URI uri) throws IOException{
		RequestConfig config = RequestConfig.custom()
						.setSocketTimeout(5000)
						.setConnectTimeout(50)
						.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
						.build();
		HttpGet get = new HttpGet(uri);
		get.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				throw new IOException("Response status " + response.getStatusLine().getStatusCode());
			}
			HttpEntity entity = response.getEntity();
			if(entity == null){
				throw new IOException("Response is null");
			}
			return EntityUtils.toString(entity, Charset.forName("UTF-8"));
		} finally {
			if(response != null){
				response.close();
			}
		}
	}

}
