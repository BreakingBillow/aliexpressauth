package com.weiproduct.webdemo.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelService {
	public boolean GenerateReport(HSSFWorkbook document)throws Exception;
}
