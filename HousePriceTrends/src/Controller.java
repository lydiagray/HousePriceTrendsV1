import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller {	
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Databases", "db");
		jfc.setFileFilter(filter);
		
		int result = jfc.showOpenDialog(new JFrame());
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			new SessionQuery(file.getAbsolutePath());
		}
	}
}
