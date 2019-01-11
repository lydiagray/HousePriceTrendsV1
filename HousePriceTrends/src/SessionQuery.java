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

/**
 * This class implements a Session Query user interface which allows a user to query a specified database with a self defined postcode and filter option.
 * @author Lydia Gray 17106282
 *
 */

@SuppressWarnings("serial")
public class SessionQuery extends JFrame implements ActionListener{
	private Connection connection = null;
	private DocumentListener textListener = new TextListener();
	private int resultCount = 0;
	
	private String[] columnHeadersArray = {"Sale Price (£)", "Sale Date", "Postcode", "House Number/Name", "Street", "Locality", "Town", "District", "County"};
	private Vector<String> columnHeaders = new Vector<String>(Arrays.asList(columnHeadersArray));
	private JTable table = new JTable();
	
	private String filepath;
	
	private JLabel instructions = new JLabel("Please input the postcode you would like to search for. You must enter a minimum of 1 character eg. S, SY16 or SY16 4BN");
	private JTextField postcodeField = new JTextField(30);
	private JLabel processing = new JLabel("The search may take up to 15 seconds, please be patient.");
	private JLabel noResults = new JLabel("The postcode you entered did not return any results. Please check it and try again.");
	private JCheckBox propertyTypeCheckbox = new JCheckBox("Exclude non-residential properties");
	private JLabel lowerLimitLabel = new JLabel("Minimum sale price (optional) £");
	private JTextField lowerPriceLimit = new JTextField(20);
	private JLabel upperLimitLabel = new JLabel("Maximum sale price (optional) £");
	private JTextField upperPriceLimit = new JTextField(20);
	private JButton search = new JButton("Search");
	private JButton refineSearch = new JButton(new AbstractAction("Refine search") {
		@Override
		public void actionPerformed(ActionEvent event) {
			clearSearchResults();
		}
	});
	private JButton newSearch = new JButton(new AbstractAction("New search") {
		@Override
		public void actionPerformed(ActionEvent event) {
			clearFilters();
			clearSearchResults();
		}
	});

	private JPanel panel1 = new JPanel();
	private JPanel filtersPanel = new JPanel();
	private JPanel panel2, panel3;
	
	/**
	 * This constructs an instance of the House Price Trends Session Query with a specified filepath
	 * @param _filepath The filepath of the database to be queried
	 */
	
	public SessionQuery(String _filepath) {
		filepath = _filepath;	
		pageSetup();
	}
	
	/**
	 * This method is called when the search button is pressed. It creates the query and calls the database. It then renders the results.
	 */
	
	public void actionPerformed(ActionEvent event) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
			String sanitisedPostcode = postcodeField.getText().replaceAll("[^a-zA-Z\\d:]", "");
			int parsedLowerPrice = lowerPriceLimit.getText().isEmpty() ? Integer.parseInt("0") : Integer.parseInt(lowerPriceLimit.getText().replaceAll("[^\\d:]", ""));
			int parsedUpperPrice = upperPriceLimit.getText().isEmpty() ? Integer.parseInt("0") : Integer.parseInt(upperPriceLimit.getText().replaceAll("[^\\d:]", ""));
			String query;
			if(propertyTypeCheckbox.isSelected()) {
				if(parsedUpperPrice != 0) {
					query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%' AND prop_type <> 'O' AND price >= " + parsedLowerPrice + " AND price <= " + parsedUpperPrice + ";";
				}
				else {
					query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%' AND prop_type <> 'O' AND price >= " + parsedLowerPrice + ";";
				}
			}
			else {
				if(parsedUpperPrice != 0) {
					query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%' AND price >= " + parsedLowerPrice + " AND price <= " + parsedUpperPrice + ";";
				}
				else {
					query = "SELECT * FROM sales WHERE REPLACE(postcode, ' ', '') LIKE '" + sanitisedPostcode + "%' AND price >= " + parsedLowerPrice + ";";
				}
			}
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			
			if(results.next()) {
				table = createTable(results);
				displayTable(table, sanitisedPostcode);
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
	
	/**
	 * This method sets whether the search button is enabled based on the contents of the postcode field
	 */
	
	public void checkFieldsNotEmpty() {
		if(postcodeField.getText().isEmpty()) {
			search.setEnabled(false);
			return;
		}
		search.setEnabled(true);
	}
	
	/**
	 * This method resets the filter values to their default empty values
	 */
	
	private void clearFilters() {
		postcodeField.setText("");
		lowerPriceLimit.setText("");
		upperPriceLimit.setText("");
		propertyTypeCheckbox.setSelected(false);
	}
	
	/**
	 * This method clears the JFrame and resets the view to the initial search display
	 */
	
	private void clearSearchResults() {
		remove(panel2);
		remove(panel3);
		resultCount = 0;
		panel1.remove(noResults);
		panel1.add(processing);
		add(panel1);
		setSize(1100,250);
		revalidate();
		repaint();
	}
	
	/**
	 * This method takes the result of the query performed in the actionPerformed() method and creates the JTable
	 * @param results The results of the query
	 * @return An ordered table of the results
	 */
	
	private JTable createTable(ResultSet results) {
		table = new JTable(populateModel(results));
		table.moveColumn(3, 0);
		table.moveColumn(4, 1);
		table.moveColumn(5, 2);
		table.moveColumn(6, 3);
		table.moveColumn(7, 4);
		table.moveColumn(8, 5);
		table.moveColumn(8, 6);
		table.moveColumn(8, 7);
		table.setAutoCreateRowSorter(true);
		
		return table;
	}
	
	/**
	 * This method renders the table in a JScrollPane and modifies the existing frame to remove the search and replace with the table
	 * @param table The table of query results
	 */
	
	private void displayTable(JTable table, String postcode) {
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JLabel resultsText = new JLabel("Your search for " + postcode + " produced " + resultCount + " matching results");
		
		panel1.remove(processing);
		remove(panel1);
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		panel2.add(resultsText);
		panel2.add(refineSearch);
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
	
	/**
	 * This method sets up the Frame and the appropriate display conditions and renders the initial search page
	 */
	
	private void pageSetup() {
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1100,250);
		setLocation(350,100);
		
		panel1.setLayout(new GridLayout(0,1,0,5));
		filtersPanel.setLayout(new FlowLayout());
		
		instructions.setHorizontalAlignment(JTextField.CENTER);
		postcodeField.setHorizontalAlignment(JTextField.CENTER);
		
		search.setEnabled(false);
		search.addActionListener(this);
		processing.setForeground(Color.red);
		processing.setHorizontalAlignment(JTextField.CENTER);
		noResults.setForeground(Color.red);
		noResults.setHorizontalAlignment(JTextField.CENTER);
		
		panel1.add(instructions);
		panel1.add(postcodeField);
		panel1.add(propertyTypeCheckbox);
		
		filtersPanel.add(lowerLimitLabel);
		filtersPanel.add(lowerPriceLimit);
		filtersPanel.add(upperLimitLabel);
		filtersPanel.add(upperPriceLimit);
		
		panel1.add(filtersPanel);
		panel1.add(search);
		panel1.add(processing);
		panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(panel1);
		postcodeField.getDocument().addDocumentListener(textListener);
	
		setVisible(true);
	}
	
	/**
	 * This method takes the results and creates the appropriate model for the creation of the table using only the sale date, price, and address
	 * @param results The results of the query
	 * @return The results as a DefaultTableModel containing only the sale date, sale price, and address 
	 */
	
	public DefaultTableModel populateModel(ResultSet results) {	
		Vector<Vector<Object>> updatedData = new Vector<Vector<Object>>();
		try {
			while (results.next()) {
				
				Vector<Object> vector = new Vector<Object>();
				for(int column = 1; column <= 16; column++) {
					// Adds the values for sale price and address fields except SAON & PAON 
					if(column == 2 || column == 4 || (column >= 10 && column <= 14)) {
						vector.add(results.getObject(column));
					}
					// Checks whether SAON exists
					else if(column == 8) {
						// Adds PAON only if SAON is empty
						if(results.getString(9) == "") {
							vector.add(results.getObject(8));
						}
						// Concatenates the SAON & PAON if SAON is present and adds
						else {
							String propIdentifier;
							propIdentifier = results.getString("saon") + " " + results.getString("paon");
							vector.add(propIdentifier);
						}
					}
					// Removes the time from the sale date field and adds
					else if(column == 3) {						
						String shortDate = results.getString(3).substring(0, 10);
						vector.add(shortDate);
					}
				}
			resultCount++;
			updatedData.add(vector);
			}
		}
		catch (SQLException e) {
			
		}
		DefaultTableModel model = new DefaultTableModel(updatedData, columnHeaders) {
			// This class sets the type of data in the column allowing the sort feature to work properly for sorting by price
			@Override
			public Class getColumnClass(int column) {
				if(column == 0) {
					return Integer.class;
				}
				else {
					return String.class;
				}
			}
		};
		return model;
	}
	
	/**
	 * This private class implements a document listener for validating the contents of a field
	 * @author Lydia Gray 17106282
	 *
	 */

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