import javax.swing.*;
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

		instructions.setEditable(false);
		filepathField.requestFocusInWindow();
		
		add(instructions);
		add(filepathField);
		add(load);
		
		load.addActionListener(this);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		dispose();
		new SessionQuery(filepath);
	}
}
