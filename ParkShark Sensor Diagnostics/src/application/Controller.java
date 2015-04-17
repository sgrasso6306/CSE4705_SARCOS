package application;

import java.io.File;
import java.io.IOException;

public class Controller {
	public static final int STATE_READY = 0;
	public static final int STATE_CONNECTED = 1;
	public static final int STATE_CONN_RECORDING = 2;
	private int _state;
	UI_2 _interface;
	EthernetModule _ethernet;
	ExcelModule _excelModule;
	
	public Controller() throws IOException {
		_interface = new UI_2(this);
		_ethernet = new EthernetModule(this);
		_state = STATE_READY;
		
	}
	
	public int getState() {
		return _state;
	}
	public void setState(int state) {
		_state = state;
	}
	
	public boolean waitForConnection(int timeout) {
		_ethernet.setReadPermissive(true);
		return _ethernet.waitForConnection(timeout);
	}
	
	public DataDisplay createDataDisplay(String title) {
		return new DataDisplay(this, title);
	}

	public void updateDisplay(DataDisplay d, DataPoint data) {
		//System.out.println(_points);
		d.addPoints(data._x,data._y,data._z);			
	}
	
	public EthernetModule getEthMod() {
		return _ethernet;
	}
	public ExcelModule getExcelMod() {
		return _excelModule;
	}
	public void initExcelModule(File path, int mode) {				// init a new excel module when recording or loading
		_excelModule = new ExcelModule(this, path, mode);
	}
}
