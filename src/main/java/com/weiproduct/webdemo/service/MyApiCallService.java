package com.weiproduct.webdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.weiproduct.webdemo.util.CommonUtil;

/**
 * api调用的服务类
 */
public class MyApiCallService {

	private static HttpResponse response;
	
    /**
     * 调用api测试
     * @param urlHead 请求的url到openapi的部分，如http://gw.open.1688.com/openapi/
     * @param urlPath protocol/version/namespace/name/appKey
     * @param appSecretKey 测试的app密钥，如果为空表示不需要签名
     * @param params api请求参数map。如果api需要用户授权访问，那么必须完成授权流程，params中必须包含access_token参数
     * @return json格式的调用结果
     */
    public static String callApiTest(String urlHead, String urlPath, String appSecretKey, Map<String, String> params){
        final HttpClient httpClient = new DefaultHttpClient();
        String result = "";
                
        final HttpPost httpPost = new HttpPost(urlHead + urlPath);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        
        if(params != null) {
        	for (Map.Entry<String, String> entry : params.entrySet()) {
        		nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        	}
        }
        
        if(appSecretKey != null){
        	nvps.add(new BasicNameValuePair("_aop_signature", CommonUtil.signatureWithParamsAndUrlPath(urlPath, params, appSecretKey)));	 
        }
        
        try {        
        	httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));       
        	response = httpClient.execute(httpPost);
        	
        	result = CommonUtil.parserResponse(response);
        	        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
        	httpClient.getConnectionManager().shutdown();

        }
        return result;
    }
    
    
}
