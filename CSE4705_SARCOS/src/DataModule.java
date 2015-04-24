import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*************************************************************************************************************************************************
 *
 * @author Steve Grasso
 * 
 * USE EXAMPLE: 
 * 
 * DataModule dataModule = new DataModule();
 * dataModule.init();													// prompted to select xlsx file containing training data
 * 
 * ArrayList<Observation> trainSetObs = getTrainSetObs();				// each observation in the list contains its own features and target
 * Observation obs = trainSetObs.get(0);								// the features and target can be accessed from an individual observation
 * float[] features = obs.getFeatures();
 * float target = obs.getTarget();
 * 
 * ArrayList<float[]> features = dataModule.getTrainSetFeatures();		// can access the matrix of features from all observations directly
 * ArrayList<Float> targets = dataModule.getTrainSetTargets();			// can access the vector of all targets directly
 *
 */



public class DataModule {
	private Workbook _trainSetWB;
	private Sheet _trainSetSheet;	
	private File _trainSetPath;
	private ArrayList<Observation> _trainSetObs;

	
	
	public DataModule() {

	}
	
	public ArrayList<Observation> getTrainSetObs() {
		return _trainSetObs;
	}
	
	public ArrayList<float[]> getTrainSetFeatures() {
		ArrayList<float[]> allFeatures = new ArrayList<float[]>(_trainSetObs.size());
		
		for (Observation o : _trainSetObs) {
			allFeatures.add(o.getFeatures());
		}
		
		return allFeatures;
	}
	
	public ArrayList<Float> getTrainSetTargets() {
		ArrayList<Float> allTargets = new ArrayList<Float>(_trainSetObs.size());
		
		for (Observation o : _trainSetObs) {
			allTargets.add(o.getTarget());
		}
		
		return allTargets;
	}
	
	public void init() {
		PopupWindowFactory.setLF();
		_trainSetPath = PopupWindowFactory.fileSelectorPop("Select Training Set", "Load", "TrainingSet.xlsx", PopupWindowFactory.XLSX_FILE_FILTER);
		if (_trainSetPath == null || !_trainSetPath.exists()) {
			System.exit(0);
		}
		loadWorkbook();
		loadTrainingData();
		
		/*
		int i=0;
		for (Observation o : _trainSetObs) {
			i++;
			System.out.println("Obs: "+i);
			System.out.println("      Target: "+o.getTarget());
			System.out.print("      Features: ");
			for (int j=0; j<21; j++) {
				System.out.print(o.getFeatures()[j]);
			}
			System.out.println();
		}
		*/
	}
	
	public void loadTrainingData() {
		_trainSetObs = new ArrayList<Observation>();
		int r = 1;
		Row row = _trainSetSheet.getRow(r);
		while (row != null) {
			
			// get target
			float target = new Float(row.getCell(1).getNumericCellValue()); 
			
			// get all features
			float[] features = new float[21];
			for (int i=0; i<21; i++) {
				features[i] = new Float(row.getCell(i+2).getNumericCellValue());
			}
			
			// add observation
			_trainSetObs.add(new Observation(target,features));
			r++;
			row = _trainSetSheet.getRow(r);
		}
	}
	
	public void loadWorkbook() {
		try {
	        FileInputStream fis = new FileInputStream(_trainSetPath);
	        _trainSetWB = new XSSFWorkbook(fis);
	        _trainSetSheet = _trainSetWB.getSheetAt(0);
	        fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static class PopupWindowFactory {
		public static final int XLSX_FILE_FILTER = 0;
		
		public static void setLF() {
	    	try{
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        }catch(Exception e){
	            System.out.println("Unable to set LookAndFeel");
	        }
		}
		
		// JOptionPane.INFORMATION_MESSAGE, WARNING_MESSAGE
		public static void messagePop(String title, String message, int messageType) {
			JOptionPane.showMessageDialog(null, message, title, messageType);
		}
		
		public static String dropSelectPop(String[] selections, String message, String title) {
			JDialog.setDefaultLookAndFeelDecorated(true);
		    Object[] selectionValues = selections;
		    Object selection = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, selectionValues, null);
		    if (selection == null) {return null;}
		    return selection.toString();
		}
		
		public static String textEntryPop(String message, String title) {
			String result = JOptionPane.showInputDialog(null,message,title,JOptionPane.PLAIN_MESSAGE);
			return result;
		}
		
		// returns 0,1,2 for yes, no, cancel respectively.
		public static int yesNoCancelPop(String title, String message, String yesButton, String noButton, String cancelButton) {
			//Custom button text
			Object[] options = {yesButton, noButton, cancelButton};
			int n = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			    options,
			    options[2]);
			
			return n;
		}
		
		public static int intEntryPop(String message, String title) {
			String r = JOptionPane.showInputDialog(null,message,title,JOptionPane.PLAIN_MESSAGE);
			int result = -1;
			while (r != null) {
				if (isInteger(r)) {
					result = Integer.parseInt(r);
					break;
				}
				else {
					messagePop("Nope", "input an integer value", JOptionPane.WARNING_MESSAGE);
					r = JOptionPane.showInputDialog(null,message,title,JOptionPane.PLAIN_MESSAGE);
				}			
			}
			return result;
		}
		
		public static File fileSelectorPop(String title, String buttonText, String defaultText, int fileFilter) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.setDialogTitle(title);
			chooser.setCurrentDirectory(null);
			chooser.setSelectedFile(new File(defaultText));
			if (fileFilter == XLSX_FILE_FILTER) {
				chooser.setFileFilter(new FileNameExtensionFilter("XLSX file", "xlsx","XLSX"));
			}
			
			chooser.showDialog(null, buttonText);
			return chooser.getSelectedFile();
		}
		
		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    return true;
		}
		

	}
	
}
