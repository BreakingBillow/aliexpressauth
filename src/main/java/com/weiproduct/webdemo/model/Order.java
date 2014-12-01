package com.weiproduct.webdemo.model;

public class Order {
	
	private String orderId;
	private String issueStatus;
	private String frozenStatus;
	private String buyerLoginId;
	private String gmtCreate;
	private String paymentType;
	private String orderStatus;
	private String gmtPayTime;
	private String fundStatus;
	private String timeoutLeftTime;
	private String bizType;
	
	//pay amount	
	private float amount;
	private String currencyCode;
	private String symbol;
		
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public String getFrozenStatus() {
		return frozenStatus;
	}
	public void setFrozenStatus(String frozenStatus) {
		this.frozenStatus = frozenStatus;
	}
	public String getBuyerLoginId() {
		return buyerLoginId;
	}
	public void setBuyerLoginId(String buyerLoginId) {
		this.buyerLoginId = buyerLoginId;
	}
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getGmtPayTime() {
		return gmtPayTime;
	}
	public void setGmtPayTime(String gmtPayTime) {
		this.gmtPayTime = gmtPayTime;
	}
	public String getFundStatus() {
		return fundStatus;
	}
	public void setFundStatus(String fundStatus) {
		this.fundStatus = fundStatus;
	}
	public String getTimeoutLeftTime() {
		return timeoutLeftTime;
	}
	public void setTimeoutLeftTime(String timeoutLeftTime) {
		this.timeoutLeftTime = timeoutLeftTime;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
