package com.weiproduct.webdemo.common;

import java.util.HashMap;

import com.weiproduct.webdemo.service.ExcelService;
import com.weiproduct.webdemo.service.impl.OrderExcelServiceImpl;

public class ExcelFactory {

	public static ExcelService createOrderExcel(HashMap parameters) {
		return new OrderExcelServiceImpl(parameters);
	}
	
}
