import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.sqlite.*;

@SuppressWarnings("serial")
public class SessionQuery extends JFrame implements ActionListener{
	private Connection connection = null;
	private Font header = new Font("Arial", Font.BOLD, 16);
	
	private Vector<String> columnHeaders = new Vector<>();
	
	private String filepath;
	private JTextArea instructions = new JTextArea("Please input the postcode you would like to search for. You must enter a minimum of 1 characters eg. S, SY16 or SY16 4BN", 2, 1);
	private JTextField postcodeField = new JTextField(30);
	private JButton search = new JButton("Search");
	private JButton newSearch = new JButton(new AbstractAction("New search") {
		@Override
		public void actionPerformed(ActionEvent event) {
			remove(panel2);
			add(panel1);
			postcodeField.setText("");
			setSize(700,150);
			revalidate();
			repaint();
		}
	});
	
	private GridBagConstraints panel1Constraints = new GridBagConstraints();
	private GridBagConstraints panel2ConstraintsSearch = new GridBagConstraints();
	private GridBagConstraints panel2ConstraintsTable = new GridBagConstraints();

	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	
	public SessionQuery(String _filepath) {
		filepath = _filepath;
		DocumentListener textListener = new TextListener();
		
		columnHeaders.add("Sale Price (£)");
		columnHeaders.add("Sale Date");
		columnHeaders.add("Postcode");
		columnHeaders.add("House Number/Name");
		columnHeaders.add("Street");
		columnHeaders.add("Locality");
		columnHeaders.add("Town");
		columnHeaders.add("District");
		columnHeaders.add("County");
		
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(1200,150);
		setLocation(300,200);
		
		panel1.setLayout(new GridBagLayout());
		panel2.setLayout(new GridBagLayout());
		
		panel1Constraints.fill = GridBagConstraints.NONE;
		panel1Constraints.gridx = 0;
		panel1Constraints.gridy = 0;
		panel1Constraints.gridwidth = 4;
		instructions.setSize(700, 25);
		instructions.setMargin(new Insets(10,10,10,10));
		instructions.setFont(header);
		panel1.add(instructions, panel1Constraints);
		
		panel1Constraints.ipady =10;
		panel1Constraints.gridx = 0;
		panel1Constraints.gridy = 1;
		panel1Constraints.gridwidth = 2;
		postcodeField.setSize(350, 40);
		panel1.add(postcodeField, panel1Constraints);
		
		panel1Constraints.ipady =10;
		panel1Constraints.gridx = 2;
		panel1Constraints.gridy = 1;
		panel1Constraints.gridwidth = 2;
		panel1Constraints.anchor = GridBagConstraints.PAGE_END;
		search.setSize(350, 40);
		panel1.add(search, panel1Constraints);
		
		panel2ConstraintsSearch.fill = GridBagConstraints.NONE;
		panel2ConstraintsSearch.gridx = 2;
		panel2ConstraintsSearch.gridy = 0;
		panel2ConstraintsSearch.gridwidth = 1;
		
		panel2ConstraintsTable.gridx = 0;
		panel2ConstraintsTable.gridy = 1;
		panel2ConstraintsTable.gridwidth = 5;
		panel2ConstraintsTable.anchor = GridBagConstraints.PAGE_END;
		
		add(panel1);
		postcodeField.requestFocusInWindow();
		postcodeField.getDocument().addDocumentListener(textListener);
		
		search.setEnabled(false);
		search.addActionListener(this);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			// connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
			connection = DriverManager.getConnection("jdbc:sqlite:C:/code/HousePriceTrendsV1/month-house-prices.db");
			String postcode = postcodeField.getText();
			Statement statement = connection.createStatement();
			String query = "SELECT * FROM sales WHERE postcode LIKE '" + postcode + "%';";
			ResultSet results = statement.executeQuery(query);
			
			JTable table = new JTable(populateModel(results));
			table.moveColumn(3, 0);
			table.moveColumn(4, 1);
			table.moveColumn(5, 2);
			table.moveColumn(6, 3);
			table.moveColumn(7, 4);
			table.moveColumn(8, 5);
			table.moveColumn(8, 6);
			table.moveColumn(8, 7);
			
			table.setSize(1000, 400);
			
			JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			panel2.add(scrollPane, panel2ConstraintsTable);
			panel2.add(newSearch, panel2ConstraintsSearch);
			remove(panel1);
			add(panel2);


			setSize(1100, 650);
			panel2.setSize(1000,400);
			revalidate();
			repaint();
		}
		catch(SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}
	
	public DefaultTableModel populateModel(ResultSet results) {	
		Vector<Vector<Object>> updatedData = new Vector<Vector<Object>>();
		try {
			while (results.next()) {
				
				Vector<Object> vector = new Vector<Object>();
				
				for(int column = 1; column <= 16; column++) {
					if(column == 2 || column == 4 || (column >= 10 && column <= 14)) {
						vector.add(results.getObject(column));
					}
					else if(column == 8) {
						if(results.getString(9) == "") {
							vector.add(results.getObject(8));
						}
						else {
							String propIdentifier;
							propIdentifier = results.getString("saon") + " " + results.getString("paon");
							vector.add(propIdentifier);
						}
					}
					else if(column == 3) {						
						String shortDate = results.getString(3).substring(0, 10);
						vector.add(shortDate);
					}
				}
			updatedData.add(vector);
			}
		}
		catch (SQLException e) {
			
		}
		DefaultTableModel model = new DefaultTableModel(updatedData, columnHeaders);
		return model;
	}
	
	public void checkFieldsNotEmpty() {
		if(postcodeField.getText().isEmpty()) {
			search.setEnabled(false);
			return;
		}
		search.setEnabled(true);
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
