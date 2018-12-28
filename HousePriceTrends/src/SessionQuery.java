import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.sqlite.*;

@SuppressWarnings("serial")
public class SessionQuery extends JFrame implements ActionListener{
	private Connection connection = null;
	
	private Vector<String> columnHeaders = new Vector<>();
	
	private String filepath;
	private JTextArea instructions = new JTextArea("Please input the postcode you would like to search for. You must enter a minimum of 4 characters eg. SY16 or SY16 4BN", 2, 1);
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
	private JTextField postcodeField = new JTextField(8);
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	
	public SessionQuery(String _filepath) {
		filepath = _filepath;
		
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
		setLayout(new BorderLayout());
		setSize(700,150);
		setLocation(300,200);
		
		panel1.setLayout(new GridLayout(0, 1));
		panel2.setLayout(new BorderLayout());
		
		panel1.add(instructions);
		panel1.add(postcodeField);
		panel1.add(search);
		add(panel1, BorderLayout.NORTH);
		postcodeField.requestFocusInWindow();
		
		search.addActionListener(this);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			// connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
			connection = DriverManager.getConnection("jdbc:sqlite:C:/code/HousePriceTrendsV1/month-house-prices.db");
			System.out.println("test");
			String postcode = postcodeField.getText();
			Statement statement = connection.createStatement();
			String query = "SELECT * FROM sales WHERE postcode LIKE '" + postcode + "%';";
			ResultSet results = statement.executeQuery(query);
			
			JTable table = new JTable(populateModel(results));
			JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			panel2.add(scrollPane, BorderLayout.CENTER);
			panel2.add(newSearch, BorderLayout.NORTH);
			remove(panel1);
			add(panel2, BorderLayout.NORTH);


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
//		data = updatedData;
		return new DefaultTableModel(updatedData, columnHeaders);
	}
}
