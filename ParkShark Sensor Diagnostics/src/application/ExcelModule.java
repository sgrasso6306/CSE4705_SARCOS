package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelModule {
	public static final int MODE_NEW_FILE = 0;
	public static final int MODE_OPEN_FILE = 1;
	Controller _controller;
	Workbook _wb; // xlsx workbook
	File _filePath;
	ArrayList<Sheet> _allSheets;
	Sheet _currentSheet;
	String _currentSheetName;
	ArrayList<DataPoint> _dataBuffer;
	
	public ExcelModule(Controller c, File path, int mode) {
		_controller = c;
		_filePath = path;
		_allSheets = new ArrayList<Sheet>();
		
		if (mode == MODE_NEW_FILE) {
			_wb = new XSSFWorkbook();
			_currentSheetName = PopupWindowFactory.textEntryPop("Name of data sheet?", "New Data Sheet");
			_currentSheet = createNewDataSheet(_currentSheetName);	// create a new sheet, will be added to _allSheets
		}
		else {
			loadWorkbook();
		}	

	}
	
	public String addSheet() {
		_currentSheetName = PopupWindowFactory.textEntryPop("Name of data sheet?", "New Data Sheet");
		_currentSheet = createNewDataSheet(_currentSheetName);	// create a new sheet, will be added to _allSheets
		return _currentSheetName;
	}
	
	
	
	public void saveWorkbook() {
		// save and close the workbook
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(_filePath);
			_wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// autosize the columns
	    for (int x=0; x<10; x++) {
	    	_currentSheet.autoSizeColumn(x);
	    }
	}
	
	public void loadWorkbook() {
		try {
	        FileInputStream fis = new FileInputStream(_filePath);
	        _wb = new XSSFWorkbook(fis);
	        for (int i=0; i<_wb.getNumberOfSheets(); i++) {
	        	_allSheets.add(_wb.getSheetAt(i));
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addDataToBuffer(DataPoint d) {
		if (_dataBuffer == null) {
			_dataBuffer = new ArrayList<DataPoint>(1000);
		}
		_dataBuffer.add(d);
	}
	
	public void populateBufferFromSheet() {
		_dataBuffer = new ArrayList<DataPoint>(1000);
		int r = 1;
		Row row = _currentSheet.getRow(r);
		while (row != null) {
			float x = Float.parseFloat(row.getCell(2).getStringCellValue()); 
			float y = Float.parseFloat(row.getCell(3).getStringCellValue());
			float z = Float.parseFloat(row.getCell(4).getStringCellValue());
			int xAvg = Integer.parseInt(row.getCell(5).getStringCellValue());
			int dAbs = Integer.parseInt(row.getCell(6).getStringCellValue());
			int dS = Integer.parseInt(row.getCell(7).getStringCellValue());
			int cFlag = Integer.parseInt(row.getCell(8).getStringCellValue());
			_dataBuffer.add(new DataPoint(x,y,z,xAvg,dAbs,dS,cFlag));
			r++;
			row = _currentSheet.getRow(r);
		}
	}
	
	public void writeDataToSheet() {
		int i = 1;
		for (DataPoint d : _dataBuffer) {
			addValueToCell(_currentSheet,i,1,new String(i+""));
			addValueToCell(_currentSheet,i,2,new String(d._x+""));
			addValueToCell(_currentSheet,i,3,new String(d._y+""));
			addValueToCell(_currentSheet,i,4,new String(d._z+""));
			addValueToCell(_currentSheet,i,5,new String(d._xAvg+""));
			addValueToCell(_currentSheet,i,6,new String(d._dAbs+""));
			addValueToCell(_currentSheet,i,7,new String(d._dS+""));
			addValueToCell(_currentSheet,i,8,new String(d._cFlag+""));
			i++;
		}
	}
	
	
	public Sheet createNewDataSheet(String title) {
		// create sheet	
		Sheet sheet = _wb.createSheet(title);
				
		// Create header row
		Row headerRow = sheet.createRow((short)0);
		// Create and populate header cells
		Cell tempCell = headerRow.createCell(1);
		tempCell.setCellValue("Frame");
		tempCell = headerRow.createCell(2);
		tempCell.setCellValue("X");
		tempCell = headerRow.createCell(3);
		tempCell.setCellValue("Y");
		tempCell = headerRow.createCell(4);
		tempCell.setCellValue("Z");	
		tempCell = headerRow.createCell(5);
		tempCell.setCellValue("xAvg");	
		tempCell = headerRow.createCell(6);
		tempCell.setCellValue("dAbs");	
		tempCell = headerRow.createCell(7);
		tempCell.setCellValue("dS");
		tempCell = headerRow.createCell(8);
		tempCell.setCellValue("cFlag");
		
		_allSheets.add(sheet);
		return sheet;
	}
	
	public void addValueToCell(Sheet sheet, int r, int c, String s) {
		Row row = sheet.getRow(r);
		if (row == null) {
			row = sheet.createRow(r);
		}
		Cell cell = row.createCell(c);
		cell.setCellValue(s);
	}
	public String getCurrentSheetName() {
		return _currentSheet.getSheetName();
	}
	public ArrayList<Sheet> getAllSheets() {
		return _allSheets;
	}
	public void setCurrentSheet(String sheetName) {
		for (Sheet s : _allSheets) {
			if (s.getSheetName().equals(sheetName)) {
				_currentSheet = s;
			}
		}
	}
	public ArrayList<DataPoint> getDataBuffer() {
		return _dataBuffer;
	}
	
}
