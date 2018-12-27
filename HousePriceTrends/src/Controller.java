import java.awt.FlowLayout;
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
//		String filepath;
		
		final JFrame frame = new JFrame("House prices");
		
		JTextArea textArea = new JTextArea("Enter the filepath for the database you want to use eg. C:/Documents/mydatabase.db");
		final JTextField textField = new JTextField(40);
		JButton loadButton = new JButton("Load");
		
		frame.setLayout(new FlowLayout());
		frame.setSize(700,150);
		frame.setLocation(300,300);
		
		frame.add(textArea);
		frame.add(textField);
		frame.add(loadButton);
		
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String filepath;
				
				do {
					filepath = textField.getText();
					if(filepath.length() != 0) {
						frame.dispose();
						new SessionQuery(filepath);
					}
				} while(filepath.length() == 0);
			}
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
		frame.setVisible(true);
	}
}
