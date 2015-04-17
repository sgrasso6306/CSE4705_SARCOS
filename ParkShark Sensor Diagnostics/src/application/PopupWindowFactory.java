package application;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PopupWindowFactory {
	public static final int XLSX_FILE_FILTER = 0;

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
	
	
	
	
	
	
	// Utility method. If there are more such methods I'll make a utility class
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}
