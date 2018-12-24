import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

import org.sqlite.*;

@SuppressWarnings("serial")
public class SessionQuery extends JFrame{
	private Connection connection = null;
	
	private JTextArea instructions = new JTextArea("Dummy text");
	
	public SessionQuery(String filepath) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
			System.out.println("It worked");
		}
		catch(SQLException sqlex) {
			sqlex.printStackTrace();
		}
		
		setTitle("House prices");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(700,350);
		setLocation(300,300);
		
		add(instructions);
		
		setVisible(true);
	}

}
