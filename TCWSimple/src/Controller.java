
public class Controller {
	private UI _gui;
	public static final int AM_TIME = 0;
	public static final int PM_TIME = 1;
	private int _startTimeMode,_endTimeMode;
	
	public Controller() {
		_gui = new UI(this);
		_startTimeMode = AM_TIME;
		_endTimeMode = AM_TIME;
	}
	
	public void setStartTimeMode(int mode) {
		if (mode == AM_TIME) {
			_startTimeMode = AM_TIME;
		}
		else {
			_startTimeMode = PM_TIME;
		}
	}
	
	public void setEndTimeMode(int mode) {
		if (mode == AM_TIME) {
			_endTimeMode = AM_TIME;
		}
		else {
			_endTimeMode = PM_TIME;
		}
	}

	public void handleEnterAction() {
		// TODO Auto-generated method stub
		
	}
	
}
