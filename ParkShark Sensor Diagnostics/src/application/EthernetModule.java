package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class EthernetModule {
	Controller _controller;
	ServerSocket _s; // Server socket (java is the server)
	Socket _sc; // Client socket (arduino)
	BufferedReader _b;
	boolean _connected;
	boolean _readPermissive;
	

	public EthernetModule(Controller c) throws IOException {
		_controller = c;
		_connected = false;
		_readPermissive = false;
	}
	
	public boolean waitForConnection(int timeout) {
		
		try {
			// create server socket
			_s = new ServerSocket(8887);
			System.out.println("Waiting for client connection");

			// wait for incoming connection from arduino client for timeout parameter (milliseconds)
			_s.setSoTimeout(timeout); 
			_sc = _s.accept();
			System.out.println("Accepted a client connection");

			
			if (!_sc.isClosed() && !_s.isClosed()) {

				System.out.println("Connected!");
				return true;
			}
			else {
				_sc.close();
				_s.close();
				System.out.println("Connection wait timeout");
				return false;
			}
			
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}
	
	public void readLiveData(DataDisplay d) {		
		_b = null;
		String message = null;

		try {
			_b = new BufferedReader(new InputStreamReader(_sc.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
		while (!_sc.isClosed()) {
				try {
					message = _b.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (message != null) {
					String[] tokens = message.split("[,]");
					float x = Integer.parseInt(tokens[0]);
					float y = Integer.parseInt(tokens[1]);
					float z = Integer.parseInt(tokens[2]);
					int xAvg = Integer.parseInt(tokens[3]);
					int dAbs = Integer.parseInt(tokens[4]);
					int dS = Integer.parseInt(tokens[5]);
					final int cFlag = Integer.parseInt(tokens[6]);
					if (cFlag != 0) {
			            Thread detect = new Thread() {
			                @Override
			                public void run() {    		
								String dir = null;
								if (cFlag == 1) {
									dir = "Car passed left to right!";
								}
								else {
									dir = "Car passed right to left";
								}
								PopupWindowFactory.messagePop("Detection!", dir, JOptionPane.INFORMATION_MESSAGE);
			                }
			             };
			             detect.start(); 
					}
					DataPoint data = new DataPoint(x,y,z,xAvg,dAbs,dS,cFlag);
					if (d.getFrame().isVisible()) {
						_controller.updateDisplay(d, data);
					}
					if (_controller.getState() == Controller.STATE_CONN_RECORDING) {
						ExcelModule em = _controller.getExcelMod();
						em.addDataToBuffer(data);
					}
					
					//System.out.println("Deriv: "+deriv);
				}
		}
		try {
			_b.close();
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*	public void readTest(int seconds) {
		long millis = seconds*1000;

		long endTime = System.currentTimeMillis() + millis;
		long currentTime = System.currentTimeMillis();
		
		while (currentTime <= endTime) {
			
			_controller.updateDisplay("500,500,500");
			
			currentTime = System.currentTimeMillis();
		}

	}
	*/
	public void readMessage() {
		_b = null;
		try {
			_b = new BufferedReader(new InputStreamReader(_sc.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("obtained client's input stream");

		while (!_sc.isClosed() && _readPermissive) {
			int test = 0;
			try {
				test = _b.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(test);
			String format = "%0" + 8 + "d";
			String binOut = String.format(format, new Integer(Integer.toString(test,2)));
			System.out.println(binOut);
		}
		try {
			_b.close();
			_sc.close();
			_s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void setReadPermissive(boolean b) {
		_readPermissive = b;
	}
	
	public boolean disconnect() {
		try {
			if (_sc != null) {
				_sc.close();
			}
			if (_s != null) {
				_s.close();
			}
			if (_b != null) {
				_b.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (_sc.isClosed() && _s.isClosed()) {
			return true;
		}
		else {
			return false;
		}
	}
}
