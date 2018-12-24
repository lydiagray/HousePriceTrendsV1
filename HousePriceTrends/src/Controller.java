import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.sqlite.*;

public class Controller {	
	public static void main(String[] args) {
		// Create the object class for each run
		// Set the user input as the url property on button click
		// Create mock search screen
//		SessionQuery session = new SessionQuery();
//		Interface.welcomeFrame();
//		Connection connection = null;
//		
//		try {
//			connection = DriverManager.getConnection(session.getDatabasePath());
//			System.out.println("It worked");
//		}
//		
//		catch(SQLException sqlex) {
//			sqlex.printStackTrace();
//		}
		Filepath filepath = new Filepath();
		do {
		System.out.println(filepath.getFilepath());
		} while (filepath.getFilepath().length() == 0);
	}
}
