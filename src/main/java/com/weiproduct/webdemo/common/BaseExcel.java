package com.weiproduct.webdemo.common;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class BaseExcel {

	protected HashMap parameters = null;
	
	protected HSSFFont headerFont = null;
	protected HSSFFont normalFont = null;

	protected HSSFCellStyle headerCellStyle = null;
	protected HSSFCellStyle normalCellStyle = null;
	
	protected SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");	
	
	public BaseExcel() {
		
	}
	
	public BaseExcel(HashMap parameters) {
		this.parameters = parameters;
	}
	
	
	public void initFont(HSSFWorkbook document){
		headerFont = document.createFont();
		headerFont.setColor(HSSFFont.COLOR_NORMAL);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("ARIAL");
		headerFont.setFontHeightInPoints((short) 12);

		normalFont = document.createFont();
		normalFont.setColor(HSSFFont.COLOR_NORMAL);
		normalFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		normalFont.setFontName("ARIAL");
		normalFont.setFontHeightInPoints((short) 10);

		headerCellStyle = document.createCellStyle();
		headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerCellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT()
				.getIndex());
		headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerCellStyle.setFont(headerFont);

		normalCellStyle = document.createCellStyle();
		normalCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		normalCellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT()
				.getIndex());
		normalCellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		normalCellStyle.setFont(normalFont);
	}
	
	
	
	
}
