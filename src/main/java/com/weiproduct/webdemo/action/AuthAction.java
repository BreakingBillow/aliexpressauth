package com.weiproduct.webdemo.action;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.weiproduct.webdemo.common.ExcelFactory;
import com.weiproduct.webdemo.model.Constant;
import com.weiproduct.webdemo.model.Order;
import com.weiproduct.webdemo.service.ExcelService;
import com.weiproduct.webdemo.service.MyApiCallService;
import com.weiproduct.webdemo.util.CommonUtil;

public class AuthAction extends ActionSupport implements SessionAware{
		
	private String getCodeForWebResult;
	private String code;
	
	private Map<String, Object> session;
	
	private String refreshToken;
	private String accessToken;
	
	private ArrayList<Order> orderList;
	private HashMap parameters = new HashMap();
	
	private InputStream inputStream;
	
	public String doAuth() throws Exception {

		return INPUT;
	}
	
	public String doRequestCode() throws Exception {
		
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("site", Constant.SITE);
        params2.put("client_id", Constant.APP_KEY);
        params2.put("redirect_uri", Constant.REDIRECT_URLI_CODE);
        params2.put("state", Constant.STATE);

		
        getCodeForWebResult = getClientAuthUrl(Constant.HOST, params2, Constant.APP_SIGNATURE);

		return SUCCESS;
	}

	public String doRequestToken() throws Exception {

        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("client_id", Constant.APP_KEY);
        params1.put("redirect_uri", Constant.REDIRECT_URLI_TOKEN);
        params1.put("client_secret", Constant.APP_SIGNATURE);
        params1.put("code", code);
        String getTokenResult = getToken(Constant.HOST, params1, true);
        System.out.println("用临时令牌换取授权令牌的返回结果：" + getTokenResult);
        
        if(getTokenResult != null) {
            //JSONObject jsonObject = JSONObject.fromObject(getTokenResult);
        	JSONObject jsonObject = new JSONObject(getTokenResult);
            System.out.println("refreshToken:" + jsonObject.get("refresh_token"));
            System.out.println("accessToken:" + jsonObject.get("access_token"));
            
            refreshToken = (String) jsonObject.get("refresh_token");
            accessToken = (String) jsonObject.get("access_token");
            
            session.put("refreshToken", jsonObject.get("refresh_token"));
            session.put("accessToken", jsonObject.get("access_token"));           
        }
                
		return SUCCESS;
	}
	
	
	public String doFinish() throws Exception {
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("client_id", Constant.APP_KEY);
		params1.put("client_secret", Constant.APP_SIGNATURE);
		
		params1.put("_aop_timestamp", CommonUtil.getAopTimestamp());		
		params1.put("access_token", (String) session.get("accessToken"));
		params1.put("page", "1");
		
		
        String getResult = getCategory(Constant.HOST, params1, true);
        System.out.println("用临时令牌换取授权令牌的返回结果：" + getResult);
				
		return SUCCESS;
	}
	
	public String doListOrder() throws Exception {
		orderList = new ArrayList();
		
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("client_id", Constant.APP_KEY);
		params1.put("client_secret", Constant.APP_SIGNATURE);
		
		params1.put("_aop_timestamp", CommonUtil.getAopTimestamp());		
		params1.put("access_token", (String) session.get("accessToken"));
		
		for (int j = 1; j < 50; j++ ){
			params1.put("page", String.valueOf(j));
			params1.put("pageSize", "50");
			
	        String getResult = getOrderList(Constant.HOST, params1, true);
	        System.out.println("用临时令牌换取授权令牌的返回结果：" + getResult);        
	        
	        if(getResult != null) {
	        	
	        	JSONObject jsonObject = new JSONObject(getResult);        	
	        	JSONArray jsonArray = new JSONArray();
	        	
	        	jsonArray =  jsonObject.getJSONArray("orderList");
	        		        	
	        	for(int i =0; i < jsonArray.length(); i++) {
	        		Order tempOrder = new Order();
	        		
	        		tempOrder.setOrderId(String.valueOf(jsonArray.getJSONObject(i).getLong("orderId")));
	        		tempOrder.setIssueStatus(jsonArray.getJSONObject(i).getString("issueStatus"));
	        		tempOrder.setFrozenStatus(jsonArray.getJSONObject(i).getString("frozenStatus"));
	        		
	        		if(!jsonArray.getJSONObject(i).isNull("buyerLoginId")) {
	        			tempOrder.setBuyerLoginId(jsonArray.getJSONObject(i).getString("buyerLoginId"));
	        		} else {
	        			tempOrder.setBuyerLoginId("");
	        		}
	        		
	        		tempOrder.setGmtCreate(CommonUtil.getFormatTimestamp(jsonArray.getJSONObject(i).getString("gmtCreate")));
	        		
	        		if(!jsonArray.getJSONObject(i).isNull("paymentType")) {
	        			tempOrder.setPaymentType(jsonArray.getJSONObject(i).getString("paymentType"));
	        		} else {
	        			tempOrder.setPaymentType("");
	        		}
	        		
	        		tempOrder.setOrderStatus(jsonArray.getJSONObject(i).getString("orderStatus"));
	        		
	        		if(!jsonArray.getJSONObject(i).isNull("gmtPayTime")) {
	        			tempOrder.setGmtPayTime(CommonUtil.getFormatTimestamp(jsonArray.getJSONObject(i).getString("gmtPayTime")));
	        		} else {
	        			tempOrder.setGmtPayTime("");
	        		}
	        		
	        		
	        		tempOrder.setFundStatus(jsonArray.getJSONObject(i).getString("fundStatus"));
	        		
	        		
	        		if(!jsonArray.getJSONObject(i).isNull("timeoutLeftTime")) {
	        			tempOrder.setTimeoutLeftTime(String.valueOf(jsonArray.getJSONObject(i).getLong("timeoutLeftTime")));
	        		} else {
	        			tempOrder.setTimeoutLeftTime("");
	        		}
	        			
	        		tempOrder.setBizType(jsonArray.getJSONObject(i).getString("bizType"));
	        		
	        		//pay amount        		
	        		JSONObject amountObject  = jsonArray.getJSONObject(i).getJSONObject("payAmount");
	        		
	        		tempOrder.setAmount(Float.parseFloat(String.valueOf(amountObject.getDouble("amount"))));
	        		tempOrder.setCurrencyCode(amountObject.getString("currencyCode"));
	        		
	        		JSONObject currencyObject = amountObject.getJSONObject("currency");        		
	        		tempOrder.setSymbol(currencyObject.getString("symbol"));
	        		
	        		orderList.add(tempOrder);        		
	        	}
	        }
	        	
    		ExcelService excel = null;
    		HSSFWorkbook document = new HSSFWorkbook(); 	
        	
    		parameters.put("orderList", orderList);
    		
    		excel = ExcelFactory.createOrderExcel(parameters);
    		
    		excel.GenerateReport(document);
        	
    		try {
    			ByteArrayOutputStream ba = new ByteArrayOutputStream();
    			document.write(ba);
    			inputStream = new DataInputStream(new ByteArrayInputStream(ba
    					.toByteArray()));
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}            	
        }
         				
		return SUCCESS;
	}
	
	
    public static String getOrderList(String host, Map<String, String> params, boolean needRefreshToken){
        String urlHead = "http://" + host + "/openapi/";
        String namespace = "aliexpress.open";
        String name = "api.findOrderListQuery";
        int version = 1;
        String protocol = "param2";
        if(params != null){
            if(params.get("client_id") == null ){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "authorization_code");
            params.put("need_refresh_token", Boolean.toString(needRefreshToken));
            String appKey = params.get("client_id");
            String appSecrect = params.get("client_secret");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = MyApiCallService.callApiTest(urlHead, urlPath, appSecrect, params);
            
            return result;
        }
        return null;
    }
	
	
    /**
     * 返回客户端和Web端授权时获取临时令牌code的url
     * @param host 请求的主机名，包括域名和端口
     * @param params 请求参数map，包括client_id,site,redirect_uri以及可选的state、scope和view
     * @param appSecretKey app签名密钥
     * @return 请求的完整url，用户在浏览器中打开此url然后输入自己的用户名密码进行授权，之后就会得到code
     */
    public static String getClientAuthUrl(String host, Map<String, String> params, String appSecretKey){
        String url = "http://" + host + "/auth/authorize.htm";
        if(params == null){
            return null;
        }
        if(params.get("client_id") == null || params.get("site") == null
                || params.get("redirect_uri") == null){
            System.out.println("params is invalid, lack neccessary key!");
            return null;
        }
        String signature = CommonUtil.signatureWithParamsOnly(params, appSecretKey);
        params.put("_aop_signature", signature);
        return CommonUtil.getWholeUrl(url, params);
    }

    
    public static String getCategory(String host, Map<String, String> params, boolean needRefreshToken){
        String urlHead = "http://" + host + "/openapi/";
        String namespace = "aliexpress.open";
        String name = "api.getWsProductGroup";
        int version = 1;
        String protocol = "param2";
        if(params != null){
            if(params.get("client_id") == null ){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "authorization_code");
            params.put("need_refresh_token", Boolean.toString(needRefreshToken));
            String appKey = params.get("client_id");
            String appSecrect = params.get("client_secret");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = MyApiCallService.callApiTest(urlHead, urlPath, appSecrect, params);
            
            return result;
        }
        return null;
    }
    
    
	
    /**
     * 通过临时令牌换取授权令牌
     * @param host 请求的主机名，包括域名和端口
     * @param params 请求参数，必填client_id、client_secret、redirect_uri和code，scope和view可选
     * @param needRefreshToken 是否需要返回refreshToken
     * @return getToken请求的json串
     */
    public static String getToken(String host, Map<String, String> params, boolean needRefreshToken){
        String urlHead = "https://" + host + "/openapi/";
        String namespace = "system.oauth2";
        String name = "getToken";
        int version = 1;
        String protocol = "http";
        if(params != null){
            if(params.get("client_id") == null || params.get("client_secret") == null
                    || params.get("redirect_uri") == null || params.get("code") == null){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "authorization_code");
            params.put("need_refresh_token", Boolean.toString(needRefreshToken));
            String appKey = params.get("client_id");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = MyApiCallService.callApiTest(urlHead, urlPath, null, params);
            return result;
        }
        return null;
    }
    
    /**
     * 通过长时令牌换取授权令牌
     * @param host 请求的主机名，包括域名和端口
     * @param params 请求参数，必填client_id、client_secret、redirect_uri和refresh_token，scope和view可选
     * @return
     */
    public static String refreshToken(String host, Map<String, String> params){
        String urlHead = "https://" + host + "/openapi/";
        String namespace = "system.oauth2";
        String name = "getToken";
        int version = 1;
        String protocol = "param2";
        if(params != null){
            if(params.get("client_id") == null || params.get("client_secret") == null
                    || params.get("redirect_uri") == null || params.get("refresh_token") == null){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "refresh_token");
            String appKey = params.get("client_id");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = MyApiCallService.callApiTest(urlHead, urlPath, null, params);
            return result;
        }
        return null;
    }

    

	public String getGetCodeForWebResult() {
		return getCodeForWebResult;
	}

	public void setGetCodeForWebResult(String getCodeForWebResult) {
		this.getCodeForWebResult = getCodeForWebResult;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}

	public HashMap getParameters() {
		return parameters;
	}

	public void setParameters(HashMap parameters) {
		this.parameters = parameters;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}	
	
	
	
}
