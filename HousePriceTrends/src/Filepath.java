import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Filepath extends JFrame implements ActionListener{
	private String filepath;
	private JTextArea instructions = new JTextArea("Enter the filepath for the database you want to use eg. C:/Documents/mydatabase");
	private JTextField filepathField = new JTextField("C:/code/HousePriceTrendsV1/month-house-prices.db", 40);
	private JButton load = new JButton("Load");
	
	public Filepath() {
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(700,150);
		setLocation(300,300);
		
		DocumentListener textListener = new TextListener();

		instructions.setEditable(false);
		filepathField.requestFocusInWindow();
		
		add(instructions);
		add(filepathField);
		add(load);
		
		filepathField.getDocument().addDocumentListener(textListener);
		
		load.addActionListener(this);
		load.setEnabled(false);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		dispose();
		new SessionQuery(filepath);
	}
	
	public void checkFieldsNotEmpty() {
		if(filepathField.getText().isEmpty()) {
			load.setEnabled(false);
			return;
		}
		load.setEnabled(true);
	}
	
	private class TextListener implements DocumentListener{
		public void changedUpdate(DocumentEvent event) {
			checkFieldsNotEmpty();
		}
		
		public void insertUpdate(DocumentEvent event) {
			checkFieldsNotEmpty();
		}
		
		public void removeUpdate(DocumentEvent event) {
			checkFieldsNotEmpty();
		}
	}
}
