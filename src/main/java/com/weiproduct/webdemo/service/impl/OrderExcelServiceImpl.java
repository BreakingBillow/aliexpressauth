package com.weiproduct.webdemo.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.weiproduct.webdemo.common.BaseExcel;
import com.weiproduct.webdemo.model.Order;
import com.weiproduct.webdemo.service.ExcelService;

public class OrderExcelServiceImpl extends BaseExcel implements ExcelService {

	private List<Order> orderList;
	
	public OrderExcelServiceImpl(HashMap parameters) {
		super(parameters);
		
		this.orderList = (List<Order>) parameters.get("orderList");
	}
	
	
	public boolean GenerateReport(HSSFWorkbook document) throws Exception {
		// TODO Auto-generated method stub
		
		
		try {
			this.initFont(document);
			
			HSSFSheet sheet = document.createSheet("Order List");
			int []columnWidths = {8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000};
			
			String []columnHeaders = {
					"Order Id",
					"Order Status",
					"Amount",
					"Currency Code",
					"Issue Status",
					"Frozen Status",
					"Fund Status",
					"BuyerId",
					"GMT Order Create time",
					"GMT Order Pay Time",
					"Payment Type"									
				};
			
			for(int i=0; i<columnWidths.length; i++){			
				sheet.setColumnWidth(i, columnWidths[i]);
			}
			
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(25);
			HSSFCell cell = null;
			
			for(int i=0; i<columnHeaders.length; i++){
				cell = row.createCell(i);
				cell.setCellStyle(headerCellStyle);
				cell.setCellValue(columnHeaders[i]);
			}
			
			for(Order ol : orderList) {
				row = sheet.createRow(rowIndex++);	
				int colIndex = 0;
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getOrderId());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getOrderStatus());
				
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getSymbol() + ol.getAmount());
				
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getCurrencyCode());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getIssueStatus());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getFrozenStatus());
				
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getFundStatus());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getBuyerLoginId());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getGmtCreate());
				
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getGmtPayTime());
				
				cell = row.createCell(colIndex++);
				cell.setCellStyle(normalCellStyle);
				cell.setCellValue(ol.getPaymentType());
								
			}
			
			
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public List<Order> getOrderList() {
		return orderList;
	}


	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	
	

}
