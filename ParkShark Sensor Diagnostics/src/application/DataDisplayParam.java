package application;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DataDisplayParam extends JFrame implements ActionListener {
	DataDisplay _display;
	private JTextField _xLow, _xHigh, _yLow, _yHigh, _zLow, _zHigh;
	private JButton _setBoundsButton;
	
	DataDisplayParam(DataDisplay d, String title) {
		super(title);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		_display = d;
		
		JPanel panel = new JPanel();
		JPanel contentPane = new JPanel();
		panel.setOpaque(true);
		
		panel.setLayout(new GridBagLayout());
		contentPane.setLayout(new BorderLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.insets = new Insets(0,10,5,7);
        JLabel label = new JLabel("Bounds");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
        panel.add(label, gc);
        
        label = new JLabel("Low");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 0;
        panel.add(label, gc);
        
        label = new JLabel("High");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 2;
		gc.gridy = 0;
        panel.add(label, gc);
        
        label = new JLabel("X:");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 1;
        panel.add(label, gc);
        
        label = new JLabel("Y:");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 2;
        panel.add(label, gc);
        
        label = new JLabel("Z:");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 3;
        panel.add(label, gc);
        
        _xLow = new JTextField("0");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 1;
        panel.add(_xLow, gc);
        
        _xHigh = new JTextField("500");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 2;
		gc.gridy = 1;
        panel.add(_xHigh, gc);
        
        _yLow = new JTextField("-700");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 2;
        panel.add(_yLow, gc);
        
        _yHigh = new JTextField("-200");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 2;
		gc.gridy = 2;
        panel.add(_yHigh, gc);
        
        _zLow = new JTextField("200");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 3;
        panel.add(_zLow, gc);
        
        _zHigh = new JTextField("700");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 2;
		gc.gridy = 3;
        panel.add(_zHigh, gc);
        
        contentPane.add(panel, BorderLayout.NORTH);
        
        _setBoundsButton = new JButton("Set Data Display Bounds");
        _setBoundsButton.addActionListener(this);
        _setBoundsButton.setActionCommand("SetBounds");
        
        contentPane.add(_setBoundsButton, BorderLayout.SOUTH);	

        
        
		setContentPane(contentPane);
		pack();
		Rectangle dataGraphBounds = _display.getFrame().getBounds();
		setLocation(dataGraphBounds.x + dataGraphBounds.width +20, dataGraphBounds.y + dataGraphBounds.height +20);
		//setLocationByPlatform(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	     	if ("SetBounds".equals(e.getActionCommand())) {
	     		if (validateBounds()) {
	     			_display.setBounds(Integer.parseInt(_xLow.getText()), Integer.parseInt(_xHigh.getText()), 
	     					Integer.parseInt(_yLow.getText()), Integer.parseInt(_yHigh.getText()),
	     					Integer.parseInt(_zLow.getText()), Integer.parseInt(_zHigh.getText()));
	     		}
	        } 
	}
	
	public boolean validateBounds() {
	    try { 
	        Integer.parseInt(_xLow.getText()); 
	        Integer.parseInt(_xHigh.getText());
	        Integer.parseInt(_yLow.getText()); 
	        Integer.parseInt(_yHigh.getText());
	        Integer.parseInt(_zLow.getText()); 
	        Integer.parseInt(_zHigh.getText());
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		return true;
	}
}
