import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Filepath extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private String filepath;
	private JTextArea textArea = new JTextArea("Enter the filepath for the database you want to use eg. C:/Documents/mydatabase");
	private JTextField textField = new JTextField(40);
	private JButton button = new JButton("Load");
	
	public Filepath() {
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(700,350);
		setLocation(300,300);
		
		add(textArea);
		add(textField);
		add(button);
		
		button.addActionListener(this);
		
		setVisible(true);
	}
	
	public void setFilepath(String _filepath) {
		filepath = _filepath;
	}
	
	public String getFilepath() {
		return filepath;
	}
	
//	public void welcomeDisplay() {
//		JFrame frame = new JFrame("House prices");

//		frame.setLayout(new GridBagLayout());
//		GridBagConstraints constraints = new GridBagConstraints();
//		
//		constraints.fill = GridBagConstraints.HORIZONTAL;
//		constraints.ipady =10;
//		constraints.gridx = 0;
//		constraints.gridy = 0;
//		constraints.gridwidth = 4;
//		frame.add(textArea, constraints);
//
//		constraints.fill = GridBagConstraints.VERTICAL;
//		constraints.ipadx = 0;
//		constraints.gridx = 0;
//		constraints.gridy = 1;
//		constraints.gridwidth = 4;
//		frame.add(textField, constraints);
//	
//		constraints.fill = GridBagConstraints.VERTICAL;
//		constraints.ipady = 0;
//		constraints.gridx = 3;
//		constraints.gridy = 2;
//		constraints.gridwidth = 1;
//		constraints.anchor = GridBagConstraints.PAGE_END;
//		frame.add(button, constraints);
//	}
	
	public void actionPerformed(ActionEvent event) {
		filepath = textField.getText();
		Connection connection = null;
		if(filepath.length() > 0) {
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
				this.dispose();
				System.out.println("It worked");
			}
			
			catch(SQLException sqlex) {
				sqlex.printStackTrace();
			}
		}
	}
}
