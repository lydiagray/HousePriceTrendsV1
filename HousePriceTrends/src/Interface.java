import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class Interface {
	public static void welcomeFrame() {
		JFrame frame = new JFrame("House prices");
		
		JTextArea textArea = new JTextArea("Enter the filepath for the database you want to use eg. C:/Documents/mydatabase");
		JTextField textField = new JTextField(40);
		JButton button = new JButton("Load");
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipady =100;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		frame.add(textArea, constraints);
		
		constraints.ipady = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		frame.add(button, constraints);
		
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.ipadx = 60;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.PAGE_END;
		frame.add(textField, constraints);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
