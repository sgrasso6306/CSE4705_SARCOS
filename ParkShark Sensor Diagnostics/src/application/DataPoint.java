package application;

public class DataPoint {
	public float _x,_y,_z;
	public int _xAvg, _dAbs, _dS, _cFlag;
	
	public DataPoint(float x, float y, float z, int xAvg, int dAbs, int dS, int cFlag) {
		_x = x;
		_y = y;
		_z = z;
		_xAvg = xAvg;
		_dAbs = dAbs;
		_dS = dS;
		_cFlag = cFlag;
	}
	
}
