import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {	
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();
		
		int result = jfc.showOpenDialog(new JFrame());
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			new SessionQuery(file.getAbsolutePath());
		}
	}
}
