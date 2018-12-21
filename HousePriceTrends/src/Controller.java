import java.sql.*;
import org.sqlite.*;

public class Controller {
	public static void main(String[] args) {
		// Create the object class for each run
		// Set the user input as the url property on button click
		// Create mock search screen
		Interface.welcomeFrame();
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/pinkl/OneDrive/Documents/Uni/pp-monthly-update-new-version");
			System.out.println("It worked");
		}
		
		catch(SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}
}
