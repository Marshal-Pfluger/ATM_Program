import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//********************************************************************
//
//  Developer:     Marshal Pfluger
//
//  Project #:     Capstone
//
//  File Name:     RunSQLQueries.java
//
//  Course:        COSC 4301 Modern Programming
//
//  Due Date:      05/12/2023
//
//  Instructor:    Prof. Fred Kumi 
//
//  Java Version:  17.0.4.1
//
//  Description:   class runs queries for capstone project 
//
//********************************************************************
public class RunSQLQueries {
	// Declare class variables 
	private Connection connection;
    private ResultSet resultSet = null;
    
    // Constructor for class
	public RunSQLQueries()
	{
		connection = null;
		
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	
	// ***************************************************************
	//
	// Method:      runAccountAuthentication
	//
	// Description: calls query to look up accounts and verify auth
	//
	// Parameters:  None
	//
	// Returns:     boolean validAccount
	//
	// **************************************************************
	public boolean runAccountAuthentication(CustomerAccount accountDetails) {
    	// declare look up variable 
    	boolean validAccount = false;
    	int accountCheck;
    	int pinCheck;
    	int customerId;
    	// declare query variable 
    	String sql = "SELECT * FROM Customer_Accounts WHERE Account_Number = ?";
    	try{
    		// declare prepare statement 
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		// set values in query 
    		stmt.setInt(1, accountDetails.getAccountNumber());
    		// execute query and receive from db
    		ResultSet results = stmt.executeQuery();
    		// if employee exists return type 
    		if (results.next()) {
    			accountCheck = results.getInt("Account_Number");
    			pinCheck = results.getInt("Customer_Pin");
    			customerId = results.getInt("Customer_Id");
    			if(accountCheck == accountDetails.getAccountNumber() && pinCheck == accountDetails.getCustomerPin()) {
    				validAccount = true;
    				accountDetails.setCustomerId(customerId);
    			}
    		}
    	// handle all exceptions	
    	}catch (SQLException exp) {
    		System.out.println("SQL Error Message 1: " + exp.getMessage());
     	} catch(Exception exp) {
            System.out.println("Failed to run query. \n" + exp.getMessage());
         }
    	return validAccount;
	}
	
	// ***************************************************************
	//
	// Method:      hasSavings
	//
	// Description: calls query to look up accounts and check for savings
	//
	// Parameters:  None
	//
	// Returns:     boolean validity 
	//
	// **************************************************************
	public boolean hasSavings(CustomerAccount accountDetails) {
		boolean savingsStatus = true;
		int count = 0;
		String sql = "SELECT * FROM Customer_Accounts WHERE Customer_Id = ? AND Account_Type = 'Savings'";
		try {
			// declare prepared statement 
			PreparedStatement stmt = connection.prepareStatement(sql);
    		// set values in query 
    		stmt.setInt(1, accountDetails.getCustomerId());
			// execute query and receive from db
			count = stmt.executeUpdate();
        // handle all exceptions
		} catch (SQLException exp) {
			System.err.println("SQL Error Message 1: " + exp.getMessage());
		} catch(Exception exp) {
			System.err.println("Failed to run query. \n" + exp.getMessage());
		}
		// if employee does not exist inform user
		if (count == 0) {
			savingsStatus = false;
		}
		return savingsStatus;	
	}
	
	// ***************************************************************
	//
	// Method:      runViewBalance
	//
	// Description: calls query to look up accounts and return balance
	//
	// Parameters:  None
	//
	// Returns:     double accountBalance 
	//
	// **************************************************************
	public double runViewBalance(CustomerAccount accountDetails, String accountType) {
		String sql = "SELECT Balance FROM Customer_Accounts WHERE Account_Type = ? AND Customer_Id = ? AND Customer_Pin = ?";
		double accountBalance = 0;
    	try{
    		// declare prepare statement 
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		// set values in query 
    		stmt.setString(1, accountType);
    		stmt.setInt(2, accountDetails.getCustomerId());
    		stmt.setInt(3, accountDetails.getCustomerPin());
    		// execute query and receive from db
    		ResultSet results = stmt.executeQuery();
    		// if employee exists return type 
    		if (results.next()) {
    			accountBalance = results.getDouble("Balance");
    			}
    	// handle all exceptions	
    	}catch (SQLException exp) {
    		System.out.println("SQL Error Message 1: " + exp.getMessage());
     	} catch(Exception exp) {
            System.out.println("Failed to run query. \n" + exp.getMessage());
         }
    	return accountBalance;
    	
	}
	
	// ***************************************************************
	//
	// Method:      runWithdrawFunds
	//
	// Description: calls query to look up accounts withdraw funds
	//
	// Parameters:  None
	//
	// Returns:     boolean validity 
	//
	// **************************************************************
	public boolean runWithdrawFunds(CustomerAccount accountDetails, double withdrawAmount, int accountChoice) {
		boolean withdrawStatus = true;
		int count = 0;
		String sql = "UPDATE Customer_Accounts SET Balance = Balance - ? WHERE Balance >= ? AND Account_Type = ? AND Customer_Id = ? AND Customer_Pin = ?";
		try {
			// declare prepared statement 
			PreparedStatement stmt = connection.prepareStatement(sql);
    		// set values in query 
    		stmt.setDouble(1, withdrawAmount);
    		stmt.setDouble(2, withdrawAmount);
    		// set account type based on user choice
			if(accountChoice == 1) {
				stmt.setString(3, "Checking");
			}else {
				stmt.setString(3, "Savings");
			}
    		stmt.setInt(4, accountDetails.getCustomerId());
    		stmt.setInt(5, accountDetails.getCustomerPin());
			// execute query and receive from db
			count = stmt.executeUpdate();
        // handle all exceptions
		} catch (SQLException exp) {
			System.err.println("SQL Error Message 1: " + exp.getMessage());
		} catch(Exception exp) {
			System.err.println("Failed to run query. \n" + exp.getMessage());
		}
		// if employee does not exist inform user
		if (count == 0) {
			withdrawStatus = false;
		}
		return withdrawStatus;	
	}
	
	// ***************************************************************
	//
	// Method:      runDepositFunds
	//
	// Description: calls query to look up accounts deposit
	//
	// Parameters:  None
	//
	// Returns:     boolean validity 
	//
	// **************************************************************
	public boolean runDepositFunds(CustomerAccount accountDetails, double depositAmount, int accountChoice) {
		boolean depositStatus = true;
		int count = 0;
		String sql = "UPDATE Customer_Accounts SET Balance = Balance + ? WHERE Account_Type = ? AND Customer_Id = ? AND Customer_Pin = ?";
		PreparedStatement stmt;
		try {
			// declare prepared statement
			stmt = connection.prepareStatement(sql);
			stmt.setDouble(1, depositAmount);
    		if (accountChoice == 1) {
        		// set values in query 
    			stmt.setString(2, "Checking");
    		}else {

    			stmt.setString(2, "Savings");
    		}
			stmt.setInt(3, accountDetails.getCustomerId());
			stmt.setInt(4, accountDetails.getCustomerPin());
    		
			// execute query and receive from db
			count = stmt.executeUpdate();
        // handle all exceptions
		} catch (SQLException exp) {
			System.err.println("SQL Error Message 1: " + exp.getMessage());
		} catch(Exception exp) {
			System.err.println("Failed to run query. \n" + exp.getMessage());
		}
		// if employee does not exist inform user
		if (count == 0) {
			depositStatus = false;
		}
		return depositStatus;	
	}
}
