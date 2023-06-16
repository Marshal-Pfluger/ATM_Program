//********************************************************************
//
//  Developer:     Marshal Pfluger + Professor Kumi
//
//  Project #:     capstone
//
//  File Name:     ConnectToOracleDB.java
//
//  Course:        COSC 4301 Modern Programming
//
//  Due Date:      05/12/2023
//
//  Instructor:    Prof. Fred Kumi 
//
//  Java Version:  17.0.4.1
//
//  Description:   db connection class
//
//********************************************************************
import java.sql.*;
import java.util.Scanner;
public class ConnectToOracleDB {
	// The Constant dbURL. 


		Scanner input;

		// Declare a connection from the Interface Connection
		private Connection connection;

		public ConnectToOracleDB()
		{
			connection = null;
		}
		
		//***************************************************************
		//
		// Method:      loadDrivers
		//
		// Description: Loads the Oracle Drivers
		//
		// Parameters:  None
		//
		// Returns:     N/A
		//
		//**************************************************************
		public void loadDrivers()
		{
			try {
				// Declare a connection from the Interface Connection
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (ClassNotFoundException exp) {
				System.err.println("The SQL Driver class was not found." + exp);
				System.exit(1);
			} catch (Exception exp) {
				System.err.println("Failed to load SQL Driver." + exp);
				System.exit(1);
			}
		}
 
		//***************************************************************
		//
		// Method:      connectDriver
		//
		// Description: The main line routine of the program
		//              @throws Exception the exception
		//
		// Parameters:  None
		//
		// Returns:     N/A
		//
		//**************************************************************
		public Connection connectDriver() throws Exception
		{		
			input = new Scanner(System.in);
			connection = dbLocationHome();
	        return connection;
		}

		
		// I LEFT THIS IN CASE YOU NEED IT PROFESSOR KUMI
		//***************************************************************
		//
		// Method:      dbLocationACC
		//
		// Description: A method to connect to the ACC database
		//
		// Parameters:  None
		//
		// Returns:     N/A
		//
		//**************************************************************
		//private Connection dbLocationACC() throws SQLException
		//{
			//String dbURL = db_ACC_URL;
			
			//System.out.print("\nEnter your ACC Oracle user name: ");
			//String userName = input.next();
			

			//System.out.print("Enter your ACC Oracle password: ");
			//String userPassword = input.next();
			

			//System.out.println("\nConnecting to the ACC CIT Oracle database...");
			//connection = connectToDb(dbURL, userName, userPassword);
			
	        //return connection;
	    //}
		
		//***************************************************************
		//
		// Method:      dbLocationHome
		//
		// Description: A method to connect to your home database
		//
		// Parameters:  None
		//
		// Returns:     N/A
		//
		//**************************************************************
		private Connection dbLocationHome() throws SQLException
		{
			String dbURL = db_Home_URL;
		    // user and password information removed for security. 
			connection = connectToDb(dbURL, userName, userPassword);
			
	        return connection;
	    }
 
		//***************************************************************
		//
		// Method:      connectToDb
		//
		// Description: Connect to the Database.
		//
		// Parameters:  DB URL, User ID, and DB Password
		//
		// Returns:     true, if successful
		//
		//**************************************************************
		private Connection connectToDb(String dbURL, String dbUser, String dbPasswd) throws SQLException
		{
			DatabaseMetaData databaseMetaData = null;
			
			connection = DriverManager.getConnection(dbURL, dbUser, dbPasswd);

			if (connection != null) {
				//System.out.println("The database connection was successful\n");
			    databaseMetaData = connection.getMetaData();
			    //System.out.println("Product Name    : " +  databaseMetaData.getDatabaseProductName());
			    //System.out.println("Product Version : " +  databaseMetaData.getDatabaseProductVersion());
			   // System.out.println("Driver Version  : " +  databaseMetaData.getDriverVersion());
			}
			else {
				//System.err.println("\nThe database connection was not successful - Bye\n");
				//System.exit(1);
			}
			
			return connection;
		}

		//***************************************************************
		//
		// Method:      closeDBConnection
		//
		// Description: Close the Database connection
		//
	    //              @throws Exception the exception
	    //              @throws SQLException the sQL exception
		//
		// Parameters:  None
		//
		// Returns:     N/A
		//
		//**************************************************************
		public void closeDBConnection() {
			System.out.println("\nClosing the Database Connection...");

			if (connection != null) {
				try {
					// Close the database connection
					connection.close(); 
					} catch (SQLException exp) {
						System.out.println("SQL Error Message: " + exp.getMessage());
					} catch(Exception exp) {
						System.out.println("Failed to run query. \n" + exp.getMessage());
						}
			}
		}
}
