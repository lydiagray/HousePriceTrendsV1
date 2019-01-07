import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class SessionQuery extends JFrame implements ActionListener{
	private Connection connection = null;
	
	private String[] columnHeadersArray = {"Sale Price (£)", "Sale Date", "Postcode", "House Number/Name", "Street", "Locality", "Town", "District", "County"};
	private Vector<String> columnHeaders = new Vector<String>(Arrays.asList(columnHeadersArray));
	private DocumentListener textListener = new TextListener();
	
	private String filepath;
	private JTextField instructions = new JTextField("Please input the postcode you would like to search for. You must enter a minimum of 1 character eg. S, SY16 or SY16 4BN.");
	private JTextField postcodeField = new JTextField(30);
	private JTextField processing = new JTextField("The search may take up to 30 seconds, please be patient.");
	private JTextField noResults = new JTextField("The postcode you entered did not return any results. Please check it and try again.");
	private JCheckBox propertyTypeCheckbox = new JCheckBox("Exclude non-residential properties");
	private JButton search = new JButton("Search");
	private JButton newSearch = new JButton(new AbstractAction("New search") {
		@Override
		public void actionPerformed(ActionEvent event) {
			clearSearchResults();
		}
	});

	private JPanel panel1 = new JPanel();
	private JPanel panel2, panel3;
	
	public SessionQuery(String _filepath) {
		filepath = _filepath;	
		pageSetup();
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
			String postcode = postcodeField.getText();
			String sanitisedPostcode = postcode.replaceAll("[^a-zA-Z\\d:]", "");
			String query;
			if(propertyTypeCheckbox.isSelected()) {
				query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%' AND prop_type <> 'O';";
			}
			else {
				query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%';";
			}
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			
			if(results.next()) {
				createTable(results);
			}
			else {
				panel1.add(noResults);
				setSize(1100,250);
			}		
		}
		catch(SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}
	
	private void pageSetup() {
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1100,210);
		setLocation(350,100);
		
		panel1.setLayout(new GridLayout(0,1,0,5));
		
		instructions.setEditable(false);
		instructions.setHorizontalAlignment(JTextField.CENTER);
		postcodeField.setHorizontalAlignment(JTextField.CENTER);
		search.setEnabled(false);
		search.addActionListener(this);
		processing.setEditable(false);
		processing.setForeground(Color.red);
		processing.setHorizontalAlignment(JTextField.CENTER);
		noResults.setEditable(false);
		noResults.setForeground(Color.red);
		noResults.setHorizontalAlignment(JTextField.CENTER);
		
		panel1.add(instructions);
		panel1.add(postcodeField);
		panel1.add(propertyTypeCheckbox);
		panel1.add(search);
		panel1.add(processing);
		panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(panel1);
		postcodeField.getDocument().addDocumentListener(textListener);
	
		setVisible(true);
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
					// Checks whether SAON exists
					else if(column == 8) {
						// Ignores SAON if present
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
	
	private void createTable(ResultSet results) {
		JTable table = new JTable(populateModel(results));
		table.moveColumn(3, 0);
		table.moveColumn(4, 1);
		table.moveColumn(5, 2);
		table.moveColumn(6, 3);
		table.moveColumn(7, 4);
		table.moveColumn(8, 5);
		table.moveColumn(8, 6);
		table.moveColumn(8, 7);
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		panel1.remove(processing);
		remove(panel1);
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		panel2.add(newSearch);
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(0,1));
		panel3.add(scrollPane);
		panel3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(panel2, BorderLayout.NORTH);
		add(panel3, BorderLayout.CENTER);

		setSize(1100, 850);
		revalidate();
		repaint();
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
	
	private void clearSearchResults() {
		search.setText("Search");
		remove(panel2);
		remove(panel3);
		panel1.remove(noResults);
		add(panel1);
		postcodeField.setText("");
		propertyTypeCheckbox.setSelected(false);
		setSize(1100,210);
		revalidate();
		repaint();
	}
}
