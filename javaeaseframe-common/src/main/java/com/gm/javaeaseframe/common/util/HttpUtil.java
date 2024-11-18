package com.gm.javaeaseframe.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求实用类
 * 
 *
 */
public final class HttpUtil {
	/** 日志记录对象性*/
	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
	/** get请求 */
	public static final String HTTP_GET = "GET";
	/** post请求 */
	public static final String HTTP_POST = "POST";
	/** HTTP请求默认超时时间 */
	private static final int DEFAULT_TIMEOUT = 5000;
	/** HTTP默认字符编码集 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	/** HTTP header definitions */
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_CONTENT_LEN  = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_EXPECT_DIRECTIVE = "Expect";
    public static final String HEADER_CONN_DIRECTIVE = "Connection";
    public static final String HEADER_TARGET_HOST = "Host";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_DATE_HEADER = "Date";
    public static final String HEADER_SERVER_HEADER = "Server";
	
	private static TrustManager myX509TrustManager = new X509TrustManager() { 
	    @Override 
	    public X509Certificate[] getAcceptedIssuers() { 
	        return null; 
	    } 

	    @Override 
	    public void checkServerTrusted(X509Certificate[] chain, String authType) 
	    throws CertificateException { 
	    } 

	    @Override 
	    public void checkClientTrusted(X509Certificate[] chain, String authType) 
	    throws CertificateException { 
	    } 
	};
	/**
	 * 执行Get请求
	 * @param requestUrl	请求地址
	 * @param params		参数
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String requestUrl, Map<String,String> params) throws Exception
	{
		return doGet(requestUrl, null, params);
	}
	/**
	 * 执行Get请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String requestUrl, Map<String,String> header, Map<String,String> params) throws Exception
	{
		return doGet(requestUrl, header, params, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行Get请求
	 * @param requestUrl	请求地址
	 * @param params		参数
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String requestUrl, Map<String,String> params, int timeout) throws Exception
	{
		return doGet(requestUrl, null, params, timeout);
	}
	/**
	 * 执行Get请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String requestUrl, Map<String,String> header, Map<String,String> params, int timeout) throws Exception
	{
		return doGet(requestUrl, header, params, DEFAULT_CHARSET, timeout);
	}
	/**
	 * 执行Get请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String requestUrl, Map<String,String> header, Map<String,String> params, String charset, int timeout) throws Exception
	{
		String paramsStr = buildParamsForGet(params, charset);
		requestUrl = buildUrlForGet(requestUrl, paramsStr);
		return httpRequest(requestUrl, HTTP_GET, header, "", charset, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param params		请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> params) throws Exception
	{
		return doPost(requestUrl, null, params);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> header, Map<String,String> params) throws Exception
	{
		return doPost(requestUrl, header, params, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param params		参数
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> params, int timeout) throws Exception
	{
		return doPost(requestUrl, null, params, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> header, Map<String,String> params, int timeout) throws Exception
	{
		return doPost(requestUrl, header, params, DEFAULT_CHARSET, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param params		参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> params, String charset, int timeout) throws Exception
	{
		return doPost(requestUrl, null, params, charset, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> header, Map<String,String> params, String charset, int timeout) throws Exception
	{
		String paramsStr = buildParamsForForm(params);
		return doPost(requestUrl, header, paramsStr, charset, timeout);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param params		请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, Map<String,String> params) throws Exception
	{
		return doPostAtUrl(requestUrl, params, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param params		请求参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, Map<String,String> params, String charset, int timeout) throws Exception
	{
		return doPostAtUrl(requestUrl, null, params, charset, timeout);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param params		请求参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, Map<String,String> header, Map<String,String> params, String charset, int timeout) throws Exception
	{
		String paramsStr = buildParamsForGet(params, charset);
		return doPostAtUrl(requestUrl, header, paramsStr, charset, timeout);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param data			请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, String data) throws Exception
	{
		return doPostAtUrl(requestUrl, null, data);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param data			请求参数
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, Map<String,String> header, String data) throws Exception
	{
		return doPostAtUrl(requestUrl, header, data, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行Post请求(将请求参数放到URL中提交)
	 * @param requestUrl	请求地址
	 * @param header		请求头信息
	 * @param data			请求参数
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPostAtUrl(String requestUrl, Map<String,String> header, String data, String charset, int timeout) throws Exception
	{
		requestUrl = buildUrlForGet(requestUrl, data);
		return doPost(requestUrl, header, "", charset, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param data			请求数据
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, String data) throws Exception
	{
		return doPost(requestUrl, null, data);
	}
	public static String doPost(String requestUrl, Map<String,String> header, String data) throws Exception
	{
		return doPost(requestUrl, header, data, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param data			请求数据
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, String data, int timeout) throws Exception
	{
		return doPost(requestUrl, null, data, DEFAULT_CHARSET, timeout);
	}
	/**
	 * 执行Post请求
	 * @param requestUrl	请求地址
	 * @param data			请求数据
	 * @param contentType	请求内容类型
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestUrl, Map<String,String> header, String data, String charset, int timeout) throws Exception
	{
		return httpRequest(requestUrl, HTTP_POST, header, data, charset, timeout);
	}
	/**
	 * 执行HTTP请求
	 * @param requestUrl	请求地址
	 * @param requestMethod	请求类型（POST、GET）
	 * @param params		请求数据
	 * @return
	 * @throws Exception
	 */
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> params) throws Exception
	{
	    return httpRequest(requestUrl, requestMethod, null, params);
	}
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> header, Map<String,String> params) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, header, params, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行HTTP请求
	 * @param requestUrl	请求地址
	 * @param requestMethod	请求类型（POST、GET）
	 * @param params		请求数据
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> params, int timeout) throws Exception
	{
	    return httpRequest(requestUrl, requestMethod, null, params, timeout);
	}
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> header, Map<String,String> params, int timeout) throws Exception
	{
		String outStr = "";
		if(requestMethod.equalsIgnoreCase(HTTP_GET))
		{
			outStr = buildParamsForGet(params, DEFAULT_CHARSET);
			requestUrl = buildUrlForGet(requestUrl,outStr);
		}
		else
		{
			outStr = buildParamsForForm(params);
		}
		return httpRequest(requestUrl, requestMethod, header, outStr, timeout);
	}
	/**
	 * 执行HTTP请求
	 * @param requestUrl	请求地址
	 * @param requestMethod	请求类型（POST、GET）
	 * @param data			请求数据
	 * @return
	 * @throws Exception
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String data) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, null, data);
	}
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> header, String data) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, header, data, DEFAULT_TIMEOUT);
	}
	/**
	 * 执行HTTP请求
	 * @param requestUrl	请求地址
	 * @param requestMethod	请求类型（POST、GET）
	 * @param data			请求数据
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String data, int timeout) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, null, data, DEFAULT_CHARSET, timeout);
	}
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> header, String data, int timeout) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, header, data, DEFAULT_CHARSET, timeout);
	}
	public static String httpRequest(String requestUrl, String requestMethod, String data, String charset, int timeout) throws Exception
	{
		return httpRequest(requestUrl, requestMethod, null, data, charset, timeout);
	}
	/**
	 * 执行HTTP请求
	 * @param requestUrl	请求地址
	 * @param requestMethod	请求类型（POST、GET）
	 * @param header		请求头信息
	 * @param data			请求数据
	 * @param charset		字符编码集
	 * @param timeout		请求超时间，单位：毫秒，0：表示不超时
	 * @return
	 * @throws Exception
	 */
	public static String httpRequest(String requestUrl, String requestMethod, Map<String,String> header, String data, String charset, int timeout) throws Exception
	{
		CloseableHttpClient httpClient = null;
		try
		{
			if(StringUtils.isEmpty(charset))
			{
				charset = DEFAULT_CHARSET;
			}
			HttpClientBuilder httpBuilder = HttpClientBuilder.create();
			httpClient = httpBuilder.build();
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(timeout)//设置连接超时时间，单位毫秒
			        .setConnectionRequestTimeout(timeout)//设置从connect Manager获取Connection 超时时间，单位毫秒
			        .setSocketTimeout(timeout).build();//请求获取数据的超时时间，单位毫秒
			HttpUriRequest request = null;
			if(HTTP_POST.equalsIgnoreCase(requestMethod))
			{
				HttpPost post = new HttpPost(requestUrl);
				post.setConfig(requestConfig);
				if(data != null && data.trim().length() > 0)
				{
					post.setEntity(new StringEntity(data, charset));
				}
				request = post;
			}
			else
			{
				if(data != null && data.trim().length() > 0)
				{
					requestUrl = buildUrlForGet(requestUrl, data);
				}
				HttpGet get = new HttpGet(requestUrl);
				get.setConfig(requestConfig);
				request = get;
			}
//			if(StringUtils.isNotEmpty(contentType))
//			{
//				request.addHeader(HTTP.CONTENT_TYPE, ContentType.create(contentType, charset).toString());
//			}
			if(header != null)
			{
				for(Map.Entry<String, String> entry : header.entrySet())
				{
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			log.debug("请求开始，请求url:" + requestUrl);
			HttpResponse httpResponse = httpClient.execute(request);
			 //获取响应消息实体  
	        HttpEntity entity = httpResponse.getEntity();  
	        //响应状态  
	        log.debug("status:" + httpResponse.getStatusLine());  
	        //判断响应实体是否为空  
	        if (entity != null) {
	        	String content = EntityUtils.toString(entity, charset);
	            log.debug("response content:" + content);  
	            return content;
	        }  
			return null;
		}
		finally
		{
			if(httpClient != null)
			{
				try
				{
					httpClient.close();
				}catch(Exception e){}
			}
		}
	}

	public static byte[] httpRequestForBuffer(String requestUrl, String requestMethod, Map<String, String> header, String data,
			int timeout) throws Exception {
		CloseableHttpClient httpClient = null;
		try {
			HttpClientBuilder httpBuilder = HttpClientBuilder.create();
			httpClient = httpBuilder.build();
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)// 设置连接超时时间，单位毫秒
					.setConnectionRequestTimeout(timeout)// 设置从connect Manager获取Connection 超时时间，单位毫秒
					.setSocketTimeout(timeout).build();// 请求获取数据的超时时间，单位毫秒
			HttpUriRequest request = null;
			if (HTTP_POST.equalsIgnoreCase(requestMethod)) {
				HttpPost post = new HttpPost(requestUrl);
				post.setConfig(requestConfig);
				if (data != null && data.trim().length() > 0) {
					post.setEntity(new StringEntity(data, DEFAULT_CHARSET));
				}
				request = post;
			} else {
				if (data != null && data.trim().length() > 0) {
					requestUrl = buildUrlForGet(requestUrl, data);
				}
				HttpGet get = new HttpGet(requestUrl);
				get.setConfig(requestConfig);
				request = get;
			}
			// if(StringUtils.isNotEmpty(contentType))
			// {
			// request.addHeader(HTTP.CONTENT_TYPE, ContentType.create(contentType,
			// charset).toString());
			// }
			if (header != null) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
			log.debug("请求开始，请求url:" + requestUrl);
			HttpResponse httpResponse = httpClient.execute(request);
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();
			// 响应状态
			log.debug("status:" + httpResponse.getStatusLine());
			// 判断响应实体是否为空
			if (entity != null) {
				InputStream inputStream = entity.getContent();
	             ByteArrayOutputStream out = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int len = -1;
	            while ((len = inputStream.read(buffer)) != -1) {
	                out.write(buffer, 0, len);
	            }
	            return out.toByteArray();
			}
			return null;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
				}
			}
		}
	}
	/**
	 * 执行http请求(Java源版)，不建议使用
	 * @param requestUrl
	 * @param requestMethod
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String httpRequestForJava(String requestUrl,String requestMethod,String data) throws Exception{
		String ret = null;
		HttpURLConnection httpUrlConn = null;
		InputStream is = null;
		BufferedReader bReader = null;
		InputStreamReader isr = null;
	    try {
	    	log.debug("请求开始，请求url:" + requestUrl);
	        URL url = new URL(requestUrl);
	        if(requestUrl.startsWith("https://")){
	        	SSLContext sslcontext = SSLContext.getInstance("TLS"); 
	            sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
	        	httpUrlConn = (HttpsURLConnection)url.openConnection();
	        	((HttpsURLConnection)httpUrlConn).setSSLSocketFactory(sslcontext.getSocketFactory());
	        	
	        }else{
	        	httpUrlConn=(HttpURLConnection)url.openConnection();
	        }
	        //设定连接超时时间
	        httpUrlConn.setConnectTimeout(30*1000);
	        //设置读取超时时间
	        httpUrlConn.setReadTimeout(30*1000);
	        //设置是否向httpUrlConnection输出
	        httpUrlConn.setDoOutput(true);
	        //设置是否从httpUrlConnection读入，默认情况下是true;
	        httpUrlConn.setDoInput(true);
	        
	        httpUrlConn.setUseCaches(false);
	        //设置请求方式（GET/POST）
	        httpUrlConn.setRequestMethod(requestMethod);
  
	        if(data != null && data.length() > 0){
	        	httpUrlConn.getOutputStream().write(data.getBytes(DEFAULT_CHARSET));
	        }
	        
	        // 将返回的输入流转换成字符串
	        is = httpUrlConn.getInputStream();
	        isr = new InputStreamReader(is, DEFAULT_CHARSET);
	        bReader = new BufferedReader(isr);
	        String str = null;
	        StringBuffer buffer = new StringBuffer();
	        while ((str = bReader.readLine()) != null) {
	            buffer.append(str).append("\n");
	        }
	        ret = buffer.length() > 0 ? buffer.substring(0, buffer.length() - 1) : buffer.toString();
	        log.debug("请求完成");
	    }finally{
	    	if(bReader != null){
	    		try{
	    			bReader.close();
	    			bReader = null;
	    		}catch(Exception e){
	    			
	    		}
	    	}
	    	if(isr != null){
	    		try{
	    			isr.close();
	    			isr = null;
	    		}catch(Exception e){
	    			
	    		}
	    	}
	    	if(is != null){
	    		try{
	    			is.close();
	    		    is = null;
	    		}catch(Exception e){
	    			
	    		}
	    	}
	    	if(httpUrlConn != null){
	    		try{
	    			httpUrlConn.disconnect();
	    			httpUrlConn = null;
	    		}catch(Exception e){
	    			
	    		}
	    	}
	    }
	    return ret;
	}
	/**
	 * 根据map获取请求参数串
	 * @param params
	 * @return
	 */
	public static String buildParamsForGet(Map<String,String> params, String charset){
		StringBuilder sb = new StringBuilder("");
		if(params != null){
		    if (StringUtils.isEmpty(charset)) {
		        charset = DEFAULT_CHARSET;
		    }
			Iterator<Map.Entry<String, String>> ite = params.entrySet().iterator();
			while(ite.hasNext()){
				Map.Entry<String, String> entry = ite.next();
				if(entry.getValue() != null){
					String value = entry.getValue();
					try
					{
						value = URLEncoder.encode(value, charset);
					}
					catch(Exception e)
					{
						log.debug("请求参数URLEncoder异常", e);
					}
					sb.append("&").append(entry.getKey()).append("=").append(value);
				}
			} 
		}
		if(sb.length() > 1){
			return sb.toString().substring(1);
		}else{
			return "";
		}
	}
	public static String buildParamsForForm(Map<String,String> params){
		StringBuilder sb = new StringBuilder("");
		if(params != null){
			Iterator<Map.Entry<String, String>> ite = params.entrySet().iterator();
			while(ite.hasNext()){
				Map.Entry<String, String> entry = ite.next();
				if(entry.getValue() != null){
					String value = entry.getValue();
					sb.append("&").append(entry.getKey()).append("=").append(value);
				}
			} 
		}
		if(sb.length() > 1){
			return sb.toString().substring(1);
		}else{
			return "";
		}
	}
	/**
	 * 合成请求url
	 * @param requestUri
	 * @param params
	 * @return
	 */
	public static String buildUrlForGet(String requestUri, String params){
		String retuUrl = requestUri;
		if(params.length() > 0){
			if(requestUri.indexOf("?") > 0){//已经存在参数
				retuUrl += "&" + params;
			}else{
				retuUrl += "?" + params;
			}
		}
		return retuUrl;
	}
	
	/**
	 * 接受HTTP的Multipart格式请求
	 * @param url
	 * @param entityMap
	 * @return
	 * @throws Exception
	 */
	public static String httpRequestMultipart(String url, Map<String, Object> entityMap) throws Exception 
	{
		HttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try{
			HttpClientBuilder httpBuilder = HttpClientBuilder.create();
			httpClient = httpBuilder.build();
//			RequestConfig requestConfig = RequestConfig.custom()  
//			        .setConnectTimeout(timeout)//设置连接超时时间，单位毫秒
//			        .setConnectionRequestTimeout(1000)//设置从connect Manager获取Connection 超时时间，单位毫秒
//			        .setSocketTimeout(timeout).build();//请求获取数据的超时时间，单位毫秒
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if(entityMap != null)
			{
				for(Map.Entry<String, Object> entry : entityMap.entrySet())
				{
					String key = entry.getKey();
					Object value = entry.getValue();
					if(value == null)
					{
						continue;
					}
					if(value instanceof String)
					{
						builder.addTextBody(key, value.toString());
					}
					else if(value instanceof File)
					{
						builder.addBinaryBody(key, (File)value);
					}
					else if(value instanceof InputStream)
					{
						builder.addBinaryBody(key, (InputStream)value);
					}
					else if(value instanceof byte[])
					{
						builder.addBinaryBody(key, (byte[])value);
					}
				}
			}
			httpPost.setEntity(builder.build());
	        response = httpClient.execute(httpPost);
	        return processMultipartResponse(response);
		}catch(Exception e){
			log.error("请求出错", e);
			throw e;
		}
		finally
		{
			if(httpClient != null)
			{
				try
				{
					httpClient.close();
				}catch(Exception e){}
			}
		}
	}
	
	/**
	 * 解析HTTP的Multipart格式的响应
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String processMultipartResponse(HttpResponse response) throws Exception {  
		StringBuffer buffer = new StringBuffer();
		
        HttpEntity entity = response.getEntity();  
        BufferedReader is = new BufferedReader(new InputStreamReader(entity.getContent(),DEFAULT_CHARSET));  
        String str = null;  
        while((str = is.readLine()) != null){  
        	buffer.append(str);  
        }
        return buffer.toString();
    }
	
	/**
	 * 设置cookie
	 * @param param
	 * @param value
	 */
	public static void setCookieValue(HttpServletRequest request, HttpServletResponse response, String param, String value) {
		setCookieValue(request, response, param, value, 24 * 60 * 60 * 30);
	}
	/**
	 * 设置cookie
	 * @param request
	 * @param response
	 * @param param
	 * @param value
	 * @param cookieMaxAge
	 */
	public static void setCookieValue(HttpServletRequest request, HttpServletResponse response, String param, String value, int cookieMaxAge) {
		String host = request.getServerName();
		setCookieValue(response, param, value, host, cookieMaxAge);
	}
	public static void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue, String cookieDomain, int cookieMaxAge)
	{
		setCookieValue(response, cookieName, cookieValue, cookieDomain, cookieMaxAge, true);
	}
	/**
	 * 设置cookie
	 * @param response
	 * @param cookieName	cookie名称
	 * @param cookieValue	cookie值
	 * @param cookieDomain	cookie有效的域名
	 * @param cookieMaxAge	cookie的有效期，单位：秒，大于等于0：则使用时间有效期，否则与session有效期相同
	 * @param httpOnly		cookie是否允许页面读取
	 * 
	 */
	public static void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue, String cookieDomain, int cookieMaxAge, boolean httpOnly)
	{
		try {
			Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue,DEFAULT_CHARSET));
			cookie.setPath("/");// cookie有效路径是网站根目录
			cookie.setDomain(cookieDomain);
			if(cookieMaxAge >= 0)
			{
				cookie.setMaxAge(cookieMaxAge);
			}
			cookie.setHttpOnly(httpOnly);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			log.debug("设置Cookie异常-->" + e.getMessage());
		}
	}
	
	/**
	 * 获取coolie
	 * @param param
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String param) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (param.equals(cookie.getName())) {
				try {
					return URLDecoder.decode(cookie.getValue(), DEFAULT_CHARSET);
				} catch (UnsupportedEncodingException e) {
					log.debug("转换Cookie值异常-->" + e.getMessage());
				}
			}
		}
		return null;
	}
	
	/**
	 * 删除cookie
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
		String host = request.getServerName(); 
		Cookie[] cs = request.getCookies();
		if (cs != null && cs.length > 0) {
			for (int i = 0; i < cs.length; i++) {
				Cookie cookie = cs[i];
				Cookie cookies = new Cookie(cookie.getName(), null);
				cookies.setMaxAge(0);
				cookies.setPath("/");
				cookies.setDomain(host);
				response.addCookie(cookies);
			}
		}
	}
	
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		String host = request.getServerName(); 
		Cookie[] cs = request.getCookies();
		if (cs != null && cs.length > 0) {
			for (int i = 0; i < cs.length; i++) {
				Cookie cookie = cs[i];
				if(name.equals(cookie.getName())) {
					Cookie cookienew = new Cookie(cookie.getName(), null);
					cookienew.setMaxAge(0);
					cookienew.setPath("/");
					cookienew.setDomain(host);
					response.addCookie(cookienew);
				}
			}
		}
	}
	
	/**
	 * 获取发送请求的源IP地址
	 * @param request
	 * @return
	 */
	public static String getRequestIP(HttpServletRequest request) 
	{
		try
		{
			String ip = request.getHeader("x-forwarded-for");
			if(StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip))
	        {
	        	//多次反向代理后会有多个ip值，第一个ip才是真实ip
				int index = ip.indexOf(",");
				if (index != -1)
				{
					return ip.substring(0, index);
				}
				else
				{
					return ip;
				}
	        }
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getRemoteAddr();
			}
			return ip;
		}
		catch(Throwable e)
		{
			log.debug("获取请求IP地址异常-->" + e.getMessage());
			return "";
		}
	}
	/**
	 * 是否是ajax提交请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxSubmit(HttpServletRequest request)
	{
		try
		{
			String accept = request.getHeaders("Accept").nextElement().toString();
			if(accept.indexOf("application/json") != -1)
			{
				return true;
			}
		}
		catch(Exception e){
			log.debug("请求类型识别(普通/Ajax)异常-->" + e.getMessage());
		}
		return false;
	}
}
