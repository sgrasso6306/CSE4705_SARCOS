package application;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class DataDisplay extends AbstractAnalysis {
	Controller _controller;
	String _title;
	JFrame frame;
	boolean init;
	Point p = null;
    Point p1 = null;
    Point p2 = null;
    Point p3 = null;
    Point[] _points = new Point[8];
    Color[]   _colors = new Color[8];
    Scatter scatter;
	
	public DataDisplay(Controller c, String title) {
		_controller = c;
		_title = title;
	}
		
	public void init(){
        
        Coord3d[] dummyCoord = new Coord3d[1];
        Color[] dummyColor = new Color[1];
        dummyCoord[0] = new Coord3d(0,0,0);
        dummyColor[0] = new Color(0, 0, 0, 0.0f);
        //dummyCoord[0] = null;
        //dummyColor[0] = null;
        //scatter = new Scatter(dummyCoord, dummyColor);
        scatter = new Scatter();
        setBounds(0,500,-700,-200,200,700);
        
        
        //chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        //chart = AWTChartComponentFactory.chart(Quality.Nicest, "newt");
        chart = AWTChartComponentFactory.chart(Quality.Nicest, "awt");
        
        
        chart.getScene().add(scatter);
        //chart.addController(null);
        chart.addMouseController();
        // Embed into Swing.
        frame = new JFrame(_title);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        //panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());						// window size
        panel.setPreferredSize(new Dimension(400,400));
        //panel.setBorder(new MatteBorder(5, 5, 5, 5, java.awt.Color.BLACK));
        if (chart.getCanvas() instanceof Canvas) {
            //System.out.println("Canvas "+chart.getCanvas());
            panel.add((Canvas)chart.getCanvas());
        } 
        else if (chart.getCanvas() instanceof Panel) {
            //System.out.println("Panel "+chart.getCanvas());
            panel.add((Panel) chart.getCanvas());
        } 
        else {
            //System.err.println("Other "+chart.getCanvas());
            panel.add((JPanel)chart.getCanvas());
        }
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
	
	public void addPoints(float x, float y, float z) {
        //p3 = p2;
        //p2 = p1;
        p1 = p;
        p = new Point(new Coord3d(x, y, z));
        //float distance = (float) (Math.sqrt((x/100)*(x/100)+(y/100)*(y/100)+(z/100)*(z/100)));
        p.setWidth(20f);
        //p.setColor(new Color(27, 20, 227, 0.8f));
        p.setColor(new Color(0, 0, 0, 1f));
        if (chart.getScene() != null) {
        	chart.getScene().getGraph().add(p);
        }
        if (chart.getScene() != null) {
        	chart.getScene().getGraph().remove(p1);
        }
        
	}
	public void setBounds(int xLow, int xHigh, int yLow, int yHigh, int zLow, int zHigh) {
        float x;
        float y;
        float z;
        
        Coord3d[] boundCoords = new Coord3d[8];
        Color[] boundColors = new Color[8];
       
        x=xLow;
        y=yLow;
        z=zLow;
        boundCoords[0] = new Coord3d(x, y, z);
        boundColors[0] = new Color(x, y, z, 0.0f);
     
        x=xLow;
        y=yLow;
        z=zHigh;
        boundCoords[1] = new Coord3d(x, y, z);
        boundColors[1] = new Color(x, y, z, 0.0f);
       
        x=xLow;
        y=yHigh;
        z=zLow;
        boundCoords[2] = new Coord3d(x, y, z);
        boundColors[2] = new Color(x, y, z, 0.0f);
        
        x=xHigh;
        y=yLow;
        z=zLow;
        boundCoords[3] = new Coord3d(x, y, z);
        boundColors[3] = new Color(x, y, z, 0.0f);
        
        x=xHigh;
        y=yHigh;
        z=zHigh;
        boundCoords[4] = new Coord3d(x, y, z);
        boundColors[4] = new Color(x, y, z, 0.0f);
        
        x=xLow;
        y=yHigh;
        z=zHigh;
        boundCoords[5] = new Coord3d(x, y, z);
        boundColors[5] = new Color(x, y, z, 0.0f);
        
        x=xHigh;
        y=yLow;
        z=zHigh;
        boundCoords[6] = new Coord3d(x, y, z);
        boundColors[6] = new Color(x, y, z, 0.0f);
        
        x=xHigh;
        y=yHigh;
        z=zLow;
        boundCoords[7] = new Coord3d(x, y, z);
        boundColors[7] = new Color(x, y, z, 0.0f);
        
        scatter.setData(boundCoords);
        scatter.setColors(boundColors);
        
	}
	
	/*
	public void setBounds(int xLow, int xHigh, int yLow, int yHigh, int zLow, int zHigh) {
        float x;
        float y;
        float z;  
        if (chart.getScene() != null && _points.length == 8) {
        	for (int i=0; i<8; i++) {
        		chart.getScene().getGraph().remove(_points[i]);
        	}
        }
        x=xLow;
        y=yLow;
        z=zLow;
        _points[0] = new Point(new Coord3d(x, y, z));
        _colors[0] = new Color(x, y, z, 0.0f);
        x=xLow;
        y=yLow;
        z=zHigh;
        _points[1] = new Point(new Coord3d(x, y, z));
        _colors[1] = new Color(x, y, z, 0.0f);
        x=xLow;
        y=yHigh;
        z=zLow;
        _points[2] = new Point(new Coord3d(x, y, z));
        _colors[2] = new Color(x, y, z, 0.0f);
        x=xHigh;
        y=yLow;
        z=zLow;
        _points[3] = new Point(new Coord3d(x, y, z));
        _colors[3] = new Color(x, y, z, 0.0f);
        x=xHigh;
        y=yHigh;
        z=zHigh;
        _points[4] = new Point(new Coord3d(x, y, z));
        _colors[4] = new Color(x, y, z, 0.0f);
        x=xLow;
        y=yHigh;
        z=zHigh;
        _points[5] = new Point(new Coord3d(x, y, z));
        _colors[5] = new Color(x, y, z, 0.0f);
        x=xHigh;
        y=yLow;
        z=zHigh;
        _points[6] = new Point(new Coord3d(x, y, z));
        _colors[6] = new Color(x, y, z, 0.0f);
        x=xHigh;
        y=yHigh;
        z=zLow;
        _points[7] = new Point(new Coord3d(x, y, z));
        _colors[7] = new Color(x, y, z, 0.0f);
        
        
    	for (int i=0; i<8; i++) {
    		chart.getScene().getGraph().add(_points[i]);
    	}
        
	}
	*/
	
	public JFrame getFrame() {
		return frame;
	}
	
	public boolean isInit() {
		return init;
	}

	public void show() {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
	}
}
