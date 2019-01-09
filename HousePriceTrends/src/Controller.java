import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is the main controller class which executes a JFileChooser and the instatiates an instance of the SessionQuery class upon successful filepath selection
 * @author Lydia Gray 17106282
 *
 */

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
