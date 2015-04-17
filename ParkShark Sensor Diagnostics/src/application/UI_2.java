package application;


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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.poi.ss.usermodel.Sheet;

public class UI_2 extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Controller _controller;
	private DataDisplay _rtDisplay, _histDisplay;
	private JPanel _realtimePanel, _historicalPanel;
	private JButton _connectButton, _viewLiveButton, _recButton, _loadButton, _playButton;
	private JTextField _openDataName;
	private JComboBox<String> _sheetSelect;
	private JSlider _playbackSpeed;
	private int _playbackDelay;
	
	UI_2(Controller c) throws IOException {
		super("ParkShark Sensor Diagnostics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_controller = c;
		_rtDisplay = null;
		_histDisplay = null;
		_sheetSelect = null;
		_playbackDelay = 10;
		
    	try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Unable to set LookAndFeel");
        }
    	
    	_realtimePanel = getRTPanel();
    	_realtimePanel.setOpaque(true);
    	
    	_historicalPanel = getHistPanel();
    	_historicalPanel.setOpaque(true);
    
        //Create and set up the content pane.
        JPanel newContentPane = new JPanel();
        newContentPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        newContentPane.setOpaque(true); //content panes must be opaque
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
        newContentPane.add(_realtimePanel, gc);
        
        gc.insets = new Insets(0,10,0,7);
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(5,1));
		gc.fill = GridBagConstraints.VERTICAL;
		gc.gridx = 1;
		gc.gridy = 0;
		newContentPane.add(sep, gc);
        
		gc.insets = new Insets(0,0,0,0);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 2;
		gc.gridy = 0;
        newContentPane.add(_historicalPanel, gc);
        newContentPane.setOpaque(true); //content panes must be opaque;
        
        setContentPane(newContentPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    	
	}
	
	
    public void actionPerformed(ActionEvent e) {
        if ("Connect".equals(e.getActionCommand())) {
        	
            Thread connect = new Thread() {
                @Override
                public void run() {    		
                	if (_controller.waitForConnection(0)) {
            			_recButton.setEnabled(false);
            			_viewLiveButton.setEnabled(true);
            			_connectButton.setEnabled(false);
            			_controller.setState(Controller.STATE_CONNECTED);
            		}
                }
             };
             connect.start();      	
        } 
        else if("ViewLive".equals(e.getActionCommand())) {      	
			final EthernetModule eth = _controller.getEthMod();	
			_rtDisplay = _controller.createDataDisplay("Live Data");
			_rtDisplay.init();
			new DataDisplayParam(_rtDisplay, "Real Time Data Bounds");  
			final DataDisplay dataDisp = _rtDisplay;
			_recButton.setEnabled(true);
		
	            Thread readData = new Thread() {
	                @Override
	                public void run() {
	                	eth.readLiveData(dataDisp);
	                	try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	                }
	             };
	             readData.start();		
        }
        else if("Rec".equals(e.getActionCommand())) {
        	
        	// if connected but not recording
        	if (_controller.getState() == Controller.STATE_CONNECTED) {
        		if (_controller.getExcelMod() == null) {
        			File path = PopupWindowFactory.fileSelectorPop("New Data Log", "Create Log", "data.xlsx", PopupWindowFactory.XLSX_FILE_FILTER);
        			_controller.initExcelModule(path, ExcelModule.MODE_NEW_FILE);
        			_controller.setState(Controller.STATE_CONN_RECORDING);
        			_recButton.setText("Stop Recording");
        			updateDataName(path.getName());
        			addSheetToDrop(_controller.getExcelMod().getCurrentSheetName()); 
        		}
        		else {
        			int useSaved = PopupWindowFactory.yesNoCancelPop("New Data Log", "Data log already open", "Add new sheet to open log", "Save open log and create new log", "Cancel");
        			if (useSaved == 0) {
        				// add new sheet to log
        				File path = _controller.getExcelMod()._filePath;
        				clearDrop();
        				_controller.initExcelModule(path, ExcelModule.MODE_OPEN_FILE);
        				ExcelModule em = _controller.getExcelMod();
        				addSheetsToDrop(em.getAllSheets());
        				em.addSheet();
        				_controller.setState(Controller.STATE_CONN_RECORDING);
        				_recButton.setText("Stop Recording");
        				updateDataName(path.getName());
        				addSheetToDrop(_controller.getExcelMod().getCurrentSheetName());        	
        			}
        			else if (useSaved == 1) {
        				// save open sheet and create new excel module
        				File path = PopupWindowFactory.fileSelectorPop("New Data Log", "Create Log", "data.xlsx", PopupWindowFactory.XLSX_FILE_FILTER);
        				_controller.initExcelModule(path, ExcelModule.MODE_NEW_FILE);
        				_controller.setState(Controller.STATE_CONN_RECORDING);
        				_recButton.setText("Stop Recording");
        				updateDataName(path.getName());
        				clearDrop();
        				addSheetToDrop(_controller.getExcelMod().getCurrentSheetName()); 
        			}
        			else {
        				// do nothing
        			}
        		}
        	}
        	// if recording in progress
        	else {
        		if (_controller.getState() == Controller.STATE_CONN_RECORDING) {
        			_controller.setState(Controller.STATE_CONNECTED);
        			ExcelModule em = _controller.getExcelMod();
        			em.writeDataToSheet();
        			em.saveWorkbook();
        			_recButton.setText("Start Recording");
        			_playButton.setEnabled(true);
        		}
        	}
        	// if not started state,
        		// record _points to memory
        		// change button text to "Stop Recording"
        	// if started state,
        		// stop recording, create excel doc
        		// change button text to "Start Recording"
        	
        }
        else if("Load".equals(e.getActionCommand())) {
			File path = PopupWindowFactory.fileSelectorPop("Load Saved Data Log", "Open Log", "", PopupWindowFactory.XLSX_FILE_FILTER);
			if (path.exists()) {
				_controller.initExcelModule(path, ExcelModule.MODE_OPEN_FILE);
				ExcelModule em = _controller.getExcelMod();	
				updateDataName(path.getName());
				clearDrop();
				addSheetsToDrop(em.getAllSheets());
				_playButton.setEnabled(true);
			} 
        }
        else if("Play".equals(e.getActionCommand())) {
        	String selectedSheetName = (String)_sheetSelect.getSelectedItem();
        	_controller.getExcelMod().setCurrentSheet(selectedSheetName);
        	_controller.getExcelMod().populateBufferFromSheet();
        	if (_histDisplay == null) {
    			_histDisplay = _controller.createDataDisplay("Recorded Data Playback");
    			_histDisplay.init();   
    			new DataDisplayParam(_histDisplay, "Recorded Data Bounds");    		
        	}
        	_histDisplay.show();
			final DataDisplay dataDisp = _histDisplay;
			
			Thread playData = new Thread() {
				@Override
				public void run() {
					_playButton.setEnabled(false);
					ArrayList<DataPoint> dataPoints = _controller.getExcelMod().getDataBuffer();
					for (DataPoint d : dataPoints) {
						_controller.updateDisplay(dataDisp, d);
						try {
							Thread.sleep(_playbackDelay);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					_playButton.setEnabled(true);
				}
			};
			playData.start();		
        }
        else {
        	System.exit(0);
        }
        
        

    }
	@Override
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        _playbackDelay = 60 - (int)source.getValue();
	    }
		
	}
    
    public void disconnectButtonHandle() {
		_recButton.setEnabled(false);
		_viewLiveButton.setEnabled(false);
		_connectButton.setEnabled(true);
    }
    
	public JPanel getRTPanel() {
		JPanel rtPanel = new JPanel();
		
		rtPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		
		
		_connectButton = new JButton("Wait for Arduino Connection");
		_connectButton.addActionListener(this);
		_connectButton.setActionCommand("Connect");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		rtPanel.add(_connectButton,gc);		
		
		_viewLiveButton = new JButton("Monitor Live Data");
		_viewLiveButton.addActionListener(this);
		_viewLiveButton.setActionCommand("ViewLive");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 1;
		_viewLiveButton.setEnabled(false);
		rtPanel.add(_viewLiveButton,gc);
		
		_recButton = new JButton("Start Recording");
		_recButton.addActionListener(this);
		_recButton.setActionCommand("Rec");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 2;
		_recButton.setEnabled(false);
		rtPanel.add(_recButton,gc);

		
		return rtPanel;
	}
	
	public JPanel getHistPanel() {
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel histPanel = new JPanel();
		
		topPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new BorderLayout());
		histPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		
		
		_loadButton = new JButton("Load an existing data log");
		_loadButton.addActionListener(this);
		_loadButton.setActionCommand("Load");
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		topPanel.add(_loadButton,gc);		
		
		gc.insets = new Insets(0,5,0,1);
		_openDataName = new JTextField("Data: ",25);
		_openDataName.setEditable(false);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 0;
		topPanel.add(_openDataName,gc);	
		
		_sheetSelect = new JComboBox<String>();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 1;
		topPanel.add(_sheetSelect,gc);
		
		gc.insets = new Insets(0,0,0,0);
		_playButton = new JButton("Play data");
		_playButton.addActionListener(this);
		_playButton.setActionCommand("Play");
		_playButton.setEnabled(false);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 1;
		topPanel.add(_playButton,gc);
		
		_playbackSpeed = new JSlider(JSlider.HORIZONTAL,0,60,50);
		_playbackSpeed.setMajorTickSpacing(1);
		Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("Slow") );
		labelTable.put( new Integer( 50 ), new JLabel("Actual") );
		labelTable.put( new Integer( 60 ), new JLabel("Fast") );
		_playbackSpeed.setLabelTable( labelTable );
		_playbackSpeed.setPaintLabels(true);
		_playbackSpeed.setPaintTicks(false);
		_playbackSpeed.addChangeListener(this);
		bottomPanel.add(_playbackSpeed);
		
		
		gc.gridx = 0;
		gc.gridy = 0;
		histPanel.add(topPanel,gc);
		gc.gridx = 0;
		gc.gridy = 1;
		histPanel.add(bottomPanel,gc);
		
		
		return histPanel;		
	}

	
	public void updateDataName(String s) {
		_openDataName.setText("Data: "+s);
	}

	public void addSheetToDrop(String sheetName) {
		_sheetSelect.addItem(sheetName);
		_sheetSelect.setSelectedItem(sheetName);
	}
	public void addSheetsToDrop(ArrayList<Sheet> l) {
		for (Sheet s : l) {
			addSheetToDrop(s.getSheetName());
		}
	}
	public void clearDrop() {
		//_sheetSelect.removeAll();
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) _sheetSelect.getModel();
		model.removeAllElements();
	}



		
	
}
