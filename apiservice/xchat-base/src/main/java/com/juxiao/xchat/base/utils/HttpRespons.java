package com.juxiao.xchat.base.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


public class HttpRespons {
	  String urlString;     
      
	    int defaultPort;     
	      
	    String file;     
	      
	    String host;     
	      
	    String path;     
	      
	    int port;     
	      
	    String protocol;     
	      
	    String query;     
	      
	    String ref;     
	      
	    String userInfo;     
	      
	    String contentEncoding;     
	      
	    String content;     
	      
	    String contentType;     
	      
	    int code;     
	      
	    String message;     
	      
	    String method;     
	      
	    int connectTimeout;     
	      
	    int readTimeout;     
	      
	    Vector<String> contentCollection;     
	      
	    public String getContent() {     
	        return content;     
	    }     
	      
	    public String getContentType() {     
	        return contentType;     
	    }     
	      
	    public int getCode() {     
	        return code;     
	    }     
	      
	    public String getMessage() {     
	        return message;     
	    }     
	      
	    public Vector<String> getContentCollection() {     
	        return contentCollection;     
	    }     
	      
	    public String getContentEncoding() {     
	        return contentEncoding;     
	    }     
	      
	    public String getMethod() {     
	        return method;     
	    }     
	      
	    public int getConnectTimeout() {     
	        return connectTimeout;     
	    }     
	      
	    public int getReadTimeout() {     
	        return readTimeout;     
	    }     
	      
	    public String getUrlString() {     
	        return urlString;     
	    }     
	      
	    public int getDefaultPort() {     
	        return defaultPort;     
	    }     
	      
	    public String getFile() {     
	        return file;     
	    }     
	      
	    public String getHost() {     
	        return host;     
	    }     
	      
	    public String getPath() {     
	        return path;     
	    }     
	      
	    public int getPort() {     
	        return port;     
	    }     
	      
	    public String getProtocol() {     
	        return protocol;     
	    }     
	      
	    public String getQuery() {     
	        return query;     
	    }     
	      
	    public String getRef() {     
	        return ref;     
	    }     
	      
	    public String getUserInfo() {     
	        return userInfo;     
	    } 
	    
	    
	    public static String post(String params,String requestUrl) throws IOException {  
	        // TODO Auto-generated method stub  
//	        try {       
	           //HttpRequester request = new HttpRequester();  
	           // request.setDefaultContentEncoding("utf-8"); 	
	            byte[] requestBytes = params.getBytes("utf-8"); // 将参数转为二进制流
	                   HttpClient httpClient = new HttpClient(); // 客户端实例化
	                   PostMethod postMethod = new PostMethod(requestUrl);
	                   //设置请求头Authorization
//	                   postMethod.setRequestHeader("Authorization", "Basic " + authorization);
	                   // 设置请求头  Content-Type
	                   postMethod.setRequestHeader("Content-Type", "application/json");
	                   InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,requestBytes.length);
	                   RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,
	                             requestBytes.length, "application/json; charset=utf-8"); // 请求体
	                   postMethod.setRequestEntity(requestEntity);
	                   httpClient.executeMethod(postMethod);// 执行请求
	                   InputStream soapResponseStream = postMethod.getResponseBodyAsStream();// 获取返回的流
	                   byte[] datas = null;
	                    try {
	                        datas = readInputStream(soapResponseStream);// 从输入流中读取数据
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                     }
	                    String result = new String(datas, "UTF-8");// 将二进制流转为String
	                     // 打印返回结果
	                     // System.out.println(result);
	             
	                     return result;   
	    }  
	    
	    
	    /**
	          * 从输入流中读取数据
	          * 
	          * @param inStream
	          * @return
	          * @throws Exception
	    */
	    public static byte[] readInputStream(InputStream inStream) throws Exception {
	    	         ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    	         byte[] buffer = new byte[1024];
	    	         int len = 0;
	    	         while ((len = inStream.read(buffer)) != -1) {
	    	            outStream.write(buffer, 0, len);
	    	         }
	    	         byte[] data = outStream.toByteArray();
	    	         outStream.close();
	    	         inStream.close();
	    	        return data;
	    	     }
}
