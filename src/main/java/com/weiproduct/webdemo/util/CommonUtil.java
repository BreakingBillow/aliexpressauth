package com.weiproduct.webdemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;

/**
 * ͨ�ù����࣬������ǩ�����ߡ�urlƴװ�Լ�httpResponse�Ľ���
 */
public final class CommonUtil {

    /**
     * ����http�����response
     * @param method
     * @return ������
     * @throws IOException
     */
//    public static String parserResponse(PostMethod method) throws IOException{
//        StringBuffer contentBuffer = new StringBuffer();
//        InputStream in = method.getResponseBodyAsStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in, method.getResponseCharSet()));
//        String inputLine = null;
//        while((inputLine = reader.readLine()) != null)
//        {
//            contentBuffer.append(inputLine);
//            contentBuffer.append("/n");
//        }
//        //ȥ����β�Ļ��з�
//        contentBuffer.delete(contentBuffer.length() - 2, contentBuffer.length());
//        in.close();
//        return contentBuffer.toString();
//    }
	
	public static String parserResponse(HttpResponse response) throws IOException{
		StringBuffer contentBuffer = new StringBuffer();
		InputStream in = response.getEntity().getContent();
		
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String inputLine = null;
      while((inputLine = reader.readLine()) != null)
      {
          contentBuffer.append(inputLine);
          contentBuffer.append("/n");
      }
      //ȥ����β�Ļ��з�
      contentBuffer.delete(contentBuffer.length() - 2, contentBuffer.length());
      in.close();
      return contentBuffer.toString();
		
	}
	
    
	public static String getAopTimestamp() {
		Date date= new Date();
		Timestamp ts =  new Timestamp(date.getTime());
		
		String result = String.valueOf(ts.getTime());
		
		return result;
	}
	
	public static String getFormatTimestamp(String timestamp) throws ParseException  {	
		
		Date date = new Date();
		DateFormat original = new SimpleDateFormat("yyyyMMddHHmmss");		
		date = original.parse(timestamp);
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");			
		return sdf.format(date);
		
	}
	
	
    /**
     * ��urlPath���������ͬʱ��Ϊǩ�����ӽ���ǩ��
     * @param urlPath protocol/version/namespace/name/appKey
     * @param params api����ĸ�������ֵ��
     * @param appSecretKey appǩ����Կ
     * @return
     */
    public static String signatureWithParamsAndUrlPath(String urlPath, Map<String, String> params, String appSecretKey){
        List<String> paramValueList = new ArrayList<String>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramValueList.add(entry.getKey() + entry.getValue());
            }
        }
        final String[] datas = new String[1 + paramValueList.size()];
        datas[0] = urlPath;
        Collections.sort(paramValueList);
        for (int i = 0; i < paramValueList.size(); i++) {
            datas[i+1] = paramValueList.get(i);
        }
        byte[] signature = SecurityUtil.hmacSha1(datas, StringUtil.toBytes(appSecretKey));
        return StringUtil.encodeHexStr(signature);
    }
    
    /**
     * 
     * �������������Ϊǩ�����ӽ���ǩ��
     * @param params api����ĸ�������ֵ��
     * @param appSecretKey
     * @return
     */
    public static String signatureWithParamsOnly(Map<String, String> params, String appSecretKey){
        List<String> paramValueList = new ArrayList<String>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramValueList.add(entry.getKey() + entry.getValue());
            }
        }
        Collections.sort(paramValueList);
        String[] datas = new String[paramValueList.size()];
        paramValueList.toArray(datas);
        byte[] signature = SecurityUtil.hmacSha1(datas, StringUtil.toBytes(appSecretKey));
        return StringUtil.encodeHexStr(signature);
    }
    
    /**
     * ����apiǩ����urlPath����protocol/version/namespace/name/appKey
     * @param apiNamespace
     * @param apiName
     * @param apiVersion
     * @param protocol
     * @param appKey
     * @return
     */
    public static String buildInvokeUrlPath(String apiNamespace, String apiName, int apiVersion, String protocol, String appKey) {
        String url = protocol + "/" + apiVersion + "/" + apiNamespace + "/" + apiName + "/" + appKey;
        return url;
    }
    
    /**
     * ��ȡ������url
     * @param url ����uri
     * @param params �������
     * @return
     */
    public static String getWholeUrl(String url, Map<String, String> params){
        if(url == null){
            return null;
        }
        if(params == null){
            return url;
        }
        Set<Map.Entry<String, String>> set = params.entrySet();
        if(set.size() <= 0){
            return url;
        }
        url += "?";
        Iterator<Map.Entry<String, String>> it = set.iterator();
        if(it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String param = entry.getKey() + "=" + entry.getValue();
            url += param;
        }
        while(it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String param = entry.getKey() + "=" + entry.getValue();
            url += "&" + param;
        }
        return url;
    }
    
    private CommonUtil(){
    }
}
