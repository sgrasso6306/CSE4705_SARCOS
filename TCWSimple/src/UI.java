import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Controller _controller;
	private JPanel _realtimePanel;
	private JButton _startAMButton, _endAMButton, _enterButton, _addButton;
	private JTextField _startHourInput,_startMinuteInput,_endHourInput,_endMinuteInput;
	private OutputPanel _outputPanel;
	
	UI(Controller c) {
		super("Time Card Clock");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_controller = c;
		
    	try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Unable to set LookAndFeel");
        }
    	
    	_realtimePanel = getPanel();
    	_realtimePanel.setOpaque(true);
    	
    
        //Create and set up the content pane.
        JPanel newContentPane = new JPanel();
        newContentPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        newContentPane.setOpaque(true); //content panes must be opaque
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
        newContentPane.add(_realtimePanel, gc);
        
        setContentPane(newContentPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    	
	}
	
	
    public void actionPerformed(ActionEvent e) {
        if ("START_AM_PM".equals(e.getActionCommand())) {
        	if (_startAMButton.getText().equals("AM")) {
        		_startAMButton.setText("PM");
        		_controller.setStartTimeMode(Controller.PM_TIME);
        	}
        	else {
        		_startAMButton.setText("AM");
        		_controller.setStartTimeMode(Controller.AM_TIME);
        	}
        }
        else if ("END_AM_PM".equals(e.getActionCommand())) {
        	if (_endAMButton.getText().equals("AM")) {
        		_endAMButton.setText("PM");
        		_controller.setEndTimeMode(Controller.PM_TIME);
        	}
        	else {
        		_endAMButton.setText("AM");
        		_controller.setEndTimeMode(Controller.AM_TIME);
        	}
        }
        else if ("ENTER".equals(e.getActionCommand())) {
        	System.out.println("Enter");
        	_controller.handleEnterAction();
        }
        else if ("ADD".equals(e.getActionCommand())) {
        	System.out.println("Add");
        }
        else {
        	System.out.println(e.getActionCommand());
        	System.exit(0);
        }
    }
    
    
    
	public JPanel getPanel() {
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		topPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		//	Top panel components
		JPanel startTimePanel = new JPanel();
		startTimePanel.setLayout(new GridBagLayout());		
		TitledBorder startTitle;
		startTitle = BorderFactory.createTitledBorder("Start Time");
		startTimePanel.setBorder(startTitle);
		JPanel endTimePanel = new JPanel();
		endTimePanel.setLayout(new GridBagLayout());		
		TitledBorder endTitle;
		endTitle = BorderFactory.createTitledBorder("End Time");
		endTimePanel.setBorder(endTitle);
		JPanel enterPanel = new JPanel();
		enterPanel.setLayout(new GridBagLayout());		
		TitledBorder enterTitle;
		enterTitle = BorderFactory.createTitledBorder("Controls");
		enterPanel.setBorder(enterTitle);

		
		//	Bottom panel components
		_outputPanel = new OutputPanel();
		
		
		
		
		
		//	Start Time Panel
		gc.insets = new Insets(0,0,0,0);			// top,left,bottom,right
		_startHourInput = new JTextField("",2);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		startTimePanel.add(_startHourInput,gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		startTimePanel.add(new JLabel(":"),gc);
		
		_startMinuteInput = new JTextField("",2);
		gc.gridx = 2;
		gc.gridy = 0;
		startTimePanel.add(_startMinuteInput,gc);

		_startAMButton = new JButton("AM");
		_startAMButton.addActionListener(this);
		_startAMButton.setActionCommand("START_AM_PM");
		gc.gridx = 3;
		gc.gridy = 0;
		startTimePanel.add(_startAMButton,gc);
		
		
		
		
		//	End Time Panel
		gc.insets = new Insets(0,0,0,0);			// top,left,bottom,right
		_endHourInput = new JTextField("",2);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		endTimePanel.add(_endHourInput,gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		endTimePanel.add(new JLabel(":"),gc);
		
		_endMinuteInput = new JTextField("",2);
		gc.gridx = 2;
		gc.gridy = 0;
		endTimePanel.add(_endMinuteInput,gc);

		_endAMButton = new JButton("AM");
		_endAMButton.addActionListener(this);
		_endAMButton.setActionCommand("END_AM_PM");
		gc.gridx = 3;
		gc.gridy = 0;
		endTimePanel.add(_endAMButton,gc);
		
		
		
		
		//	Enter Panel
		gc.insets = new Insets(0,0,0,0);			// top,left,bottom,right
		_enterButton = new JButton("=");
		_enterButton.addActionListener(this);
		_enterButton.setActionCommand("ENTER");
		gc.gridx = 0;
		gc.gridy = 0;
		enterPanel.add(_enterButton,gc);
		
		_addButton = new JButton("+");
		_addButton.addActionListener(this);
		_addButton.setActionCommand("ADD");
		gc.gridx = 1;
		gc.gridy = 0;
		enterPanel.add(_addButton,gc);

		
		
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		topPanel.add(startTimePanel,gc);
		gc.gridx = 1;
		gc.gridy = 0;
		topPanel.add(endTimePanel,gc);	
		gc.gridx = 2;
		gc.gridy = 0;
		topPanel.add(enterPanel,gc);	
		
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		bottomPanel.add(_outputPanel,gc);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());	
		gc.gridx = 0;
		gc.gridy = 0;
		panel.add(topPanel,gc);
		gc.gridx = 0;
		gc.gridy = 1;
		panel.add(bottomPanel,gc);
		
		return panel;
	}


	public class OutputPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		private JTextArea outputText;
		private JScrollPane scrollPane;
		private JButton clearButton;
		
		//JTextField tf = new JTextField(12);
        //outputFrame.add(tf, BorderLayout.NORTH);
		
		
				
		public OutputPanel() {
			this.setLayout(new GridBagLayout());
	        GridBagConstraints gc = new GridBagConstraints();
	        
			outputText = new JTextArea(12,37);
			scrollPane = new JScrollPane(outputText);
			//clearButton = new JButton("Clear Output Display");
			//clearButton.addActionListener(this);
			
			outputText.setEditable(false);
			
			
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			//add(clearButton, gc);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			//gc.gridy = 1;
			add(scrollPane, gc);
		}
		
		public void printText(String s) {
			outputText.append(s);
			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		}
		public void printlnText(String s) {
			outputText.append(s);
			outputText.append("\n");
			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		}
		
		public void clearText() {
			outputText.setText("");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// only clear button, so laziness.
			clearText();
		}
        
        
	}

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public int getStartHour() {
		if (isInteger(_startHourInput.getText())) {
			int sHour = Integer.parseInt(_startHourInput.getText());
			if ((sHour > 0) && (sHour <= 12)) {
				return sHour;
			}
		}
		PopupWindowFactory.messagePop("Invalid Parameter", "Start hour invalid!", JOptionPane.WARNING_MESSAGE);
		return -1;
	}
	public int getStartMin() {
		if (isInteger(_startMinuteInput.getText())) {
			int sMin = Integer.parseInt(_startMinuteInput.getText());
			if ((sMin > 0) && (sMin <= 12)) {
				return sMin;
			}
		}
		PopupWindowFactory.messagePop("Invalid Parameter", "Start minute invalid!", JOptionPane.WARNING_MESSAGE);
		return -1;
	}
	public int getEndHour() {
		if (isInteger(_endHourInput.getText())) {
			int eHour = Integer.parseInt(_endHourInput.getText());
			if ((eHour > 0) && (eHour <= 12)) {
				return eHour;
			}
		}
		PopupWindowFactory.messagePop("Invalid Parameter", "End hour invalid!", JOptionPane.WARNING_MESSAGE);
		return -1;
	}
	public int getEndMin() {
		if (isInteger(_endMinuteInput.getText())) {
			int eMin = Integer.parseInt(_endMinuteInput.getText());
			if ((eMin > 0) && (eMin <= 12)) {
				return eMin;
			}
		}
		PopupWindowFactory.messagePop("Invalid Parameter", "End minute invalid!", JOptionPane.WARNING_MESSAGE);
		return -1;
	}	
}
