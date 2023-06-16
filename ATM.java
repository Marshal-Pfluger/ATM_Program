//********************************************************************
//
//  Developer:     Marshal Pfluger
//
//  Project #:     Capstone
//
//  File Name:     ATM.java
//
//  Course:        COSC 4301 Modern Programming
//
//  Due Date:      05/12/2023
//
//  Instructor:    Prof. Fred Kumi 
//
//  Java Version:  17.0.4.1
//
//  Description:   Running class for Capstone project
//
//********************************************************************
import java.sql.*;
import java.util.Scanner;

public class ATM {
	// declare class variable
    private ConnectToOracleDB dbConnect;
    private Connection connection = null;
    private RunSQLQueries queryObj; 
    private CustomerAccount authenticatedAccount;
    
    
	public static void main(String[] args) {
		ATM obj = new ATM();
		obj.runProgram();
	}
	
	//***************************************************************
	//
	//Method:       runProgram
	//
	//Description:  loops through the program for each user. 
	//
	//Parameters:   none
	//
	//Returns:      N/A
	//
	//**************************************************************
public void runProgram() {
	// Display developer info
	developerInfo();
	do {
		// welcome user
		printOutput("Welcome to bank of Marshal");
		// Prompt them to enter the program
		printOutput("Please press enter to continue: ");
		userChoiceString(4);
		// create connection to database
		createConnection();
		// if the user is authenticated let them into program
		if(userAuthentication()) {
			runMainMenu();
		}
	// Infinite loop 
	}while(true);

}

//***************************************************************
//
//Method:       userAuthentication
//
//Description:  authenticates the user for the session. 
//
//Parameters:   none
//
//Returns:      N/A
//
//**************************************************************
public boolean userAuthentication() {
	// declare status variable
	boolean validAccount = false;
	// declare customerAccount variable 
	CustomerAccount customerCheck;
	do {
		// prompt user for account number 
		printOutput("Please enter account number: ");
		int accountNumber = Integer.parseInt(userChoiceString(3));
		// prompt user for pin number 
		printOutput("Please enter your PIN");
		int pinNumber = Integer.parseInt(userChoiceString(3));
		// create object and call authenticator method 
		customerCheck = new CustomerAccount(accountNumber, pinNumber);
		validAccount = queryObj.runAccountAuthentication(customerCheck);
		// if user is not authorized do not let them proceed. 
		if (!validAccount) {
			// Inform the user they are not authorized
			printOutput("Access Denied: Incorrect account or pin");
			printOutput("Please press 1 try again or press 0 to exit: ");
			if (userChoiceString(3).equals("0")) {
				break;
			}
		}else {
			// set authorized user to auth account
			authenticatedAccount = customerCheck;

		}
	// loop while user is not authorized or decides to exit
	}while(!validAccount);
	return validAccount;
}

// ***************************************************************
//
// Method:      createConnection
//
// Description: Establishes connections to database for program
//
// Parameters:  None
//
// Returns:     N/A
//
// **************************************************************
public void createConnection() 
{
	try {
		dbConnect = new ConnectToOracleDB();
		dbConnect.loadDrivers();
		connection = dbConnect.connectDriver();

	    if (connection != null)
	    {
		   //System.out.print("\nReceived success connection handle ");
		  // System.out.println("in main TestApp");
	    }
	} catch(Exception exp) {
        System.out.println("Something terrible went wrong "  + exp);
		System.exit(1);
    }
	queryObj = new RunSQLQueries();
	queryObj.setConnection(connection);
	
}
//***************************************************************
//
//Method:      displayMainMenu
//
//Description: calls printOutput method with main menu string 
//
//Parameters:  None
//
//Returns:     N/A
//
//**************************************************************
public void displayMainMenu() {
	printOutput("********Main Menu********\n"+
            "1. View my balance\n" +
            "2. Withdraw money\n" +
		    "3. Deposit money\n" +
            "4. Transfer money\n" +
		    "5. Exit\n\n" +
		    "Enter your choice: ");
}
//***************************************************************
//
//Method:      displayWithdrawMenu
//
//Description: calls printOutput method with withdraw menu string 
//
//Parameters:  None
//
//Returns:     N/A
//
//**************************************************************
public void displayWithdrawMenu() {
	printOutput("********Withdraw Menu********\n"+
            "1-$20        5-$100\n" +
            "2-$40        6-$120\n" +
		    "3-$60        7-$160\n" +
            "4-$80        8-$200\n" +
		    "9. Cancel tansaction\n");
}

//***************************************************************
//
//Method:      runWithdrawMenu
//
//Description: Contains switch statement to execute withdraw menu
//
//Parameters:  None
//
//Returns:     N/A
//
//**************************************************************
public void runWithdrawMenu() {
	boolean withdrawStatus = false;
	String userChoice = "";
	printOutput("Which account would you like to withdraw from? \n 1. Checking  2. Savings");
	int accountChoice = Integer.parseInt(userChoiceString(5));
	double withdrawAmount = 0;
	do {
		displayWithdrawMenu();
		userChoice = userChoiceString(2);
	 switch(userChoice) {
	 case "1":
		 withdrawAmount = 20;
	 	break;
	 case "2":
	 
		 withdrawAmount = 40;
	 	break;
	 case "3":
		 withdrawAmount = 60;
	 
	 	break;
	 case "4":
		 withdrawAmount = 80;
	 
	 	break;
	 case "5":
		 withdrawAmount = 100;
	 
	 	break;
	 case "6":
		 withdrawAmount = 120;
	 
	 	break;
	 case "7":
		 withdrawAmount = 160;
	 
	 	break;
	 case "8":
		 withdrawAmount = 200;
	 
	 	break;
	 case "9":
		 withdrawStatus = true;
		 withdrawAmount = 0;
	 
	 	break;

	 	}
	 if (withdrawAmount != 0) {
		 // if the amount is not zero call withdraw method
		 withdrawStatus = withdraw(authenticatedAccount, withdrawAmount, accountChoice);
		// if withdraw is successful tell them to take their money
		 if (withdrawStatus) {
			 printOutput("Please press enter to take your money");
			 userChoiceString(4);
		 }
		 }
	}while(!withdrawStatus);

 }

//***************************************************************
//
//Method:      withdraw
//
//Description: calls query method to withdraw funds from db
//
//Parameters:  CustomerAccount authenticatedAccount, double withdrawAmount, int accountChoice
//
//Returns:     boolean Status
//
//**************************************************************
public boolean withdraw(CustomerAccount authenticatedAccount, double withdrawAmount, int accountChoice) {
	// call run query for withdraw
	boolean status = queryObj.runWithdrawFunds(authenticatedAccount, withdrawAmount, accountChoice);
	 if (!status) {
		 // Inform user their balance is too low and display balance
		 printOutput("Insuficient funds please try a smaller amount, your available account balance is: ");
		 viewBalance();
	 }
	return status;
}

// ***************************************************************
//
// Method:      runMainMenu
//
// Description: Contains switch statement to execute main menu
//
// Parameters:  None
//
// Returns:     N/A
//
// **************************************************************
public void runMainMenu() {
	String userChoice;
	do {
		displayMainMenu();
		userChoice = userChoiceString(1);
	    switch(userChoice) {
	    case "1":
	    	viewBalance();
	    	break;
	    case "2":
	    	runWithdrawMenu();
	    	break;
	    case "3":
	    	depositFunds();
	    	break;
	    case "4":
	    	transferFunds();
	    	break;
	    case "5":
	    	printOutput("Thank you! have a nice day");
	    	}
	}while(!userChoice.equals("5"));
}

//***************************************************************
//
//Method:       viewBalance
//
//Description:  Displays the users balance(s)
//
//Parameters:   none
//
//Returns:      N/A
//
//**************************************************************
public void viewBalance() {
	// if they have a savings account display their balance in both accounts 
	if (queryObj.hasSavings(authenticatedAccount)) {
		double checkingBalance = queryObj.runViewBalance(authenticatedAccount, "Checking");
		double savingsBalance = queryObj.runViewBalance(authenticatedAccount, "Savings");
		printOutput("Checking: "+ checkingBalance + "\n" + "Savings: " + savingsBalance);
    // if they only have a checking display balance of it. 
	}else {
		double balance = queryObj.runViewBalance(authenticatedAccount, "Checking");
		printOutput("Checking: "+ balance);
	}	
}

//***************************************************************
//
//Method:       depositFunds
//
//Description:  deposits funds into checking
//
//Parameters:   none
//
//Returns:      N/A
//
//**************************************************************
public void depositFunds() {
	// declare status variable
	boolean depositStatus = false;
	// get account type from user
	printOutput("Which account would you like to deposit to? \n 1. Checking  2. Savings");
	int accountChoice = Integer.parseInt(userChoiceString(5));
	// Get deposit amount from user
	printOutput("Please enter a deposit amount or press 0 to cancel: ");
	double depositAmount = Double.parseDouble(userChoiceString(3));
	// if amount is greater than zero call deposit method
	if(depositAmount != 0) {
		depositStatus = queryObj.runDepositFunds(authenticatedAccount, depositAmount, accountChoice);
	}
	// Indicate successful deposit
	if (depositStatus) {
		printOutput("Deposit successful");
	}
}

//***************************************************************
//
//Method:       transferFunds
//
//Description:  transfers funds from checking to saving 
//
//Parameters:   none
//
//Returns:      N/A
//
//**************************************************************
public void transferFunds() {
	// declare status variable
	boolean transferStatus = false;
	// declare amount variable
	double transferAmount;
	// get account type from user
	printOutput("How would you like to transfer? \n 1. Checking->Savings  2. Savings->Checking");
	int accountChoice = Integer.parseInt(userChoiceString(5));
	do {
		// get the amount the user wants to transfer to savings 
		printOutput("Please enter an amount to transfer to savings or press 0 to cancel: ");
		transferAmount = Double.parseDouble(userChoiceString(3));
		// if they want to transfer, make sure they have enough in the account. 
		if(transferAmount != 0) {
			if (accountChoice == 1) {
				transferStatus = withdraw(authenticatedAccount, transferAmount, 1);
			}else {
				transferStatus = withdraw(authenticatedAccount, transferAmount, 2);
			}
			
			// only deposit in savings if withdraw is successful
			if (transferStatus) {
				if (accountChoice == 1) {
					transferStatus = queryObj.runDepositFunds(authenticatedAccount, transferAmount, 2);
				}else {
					transferStatus = queryObj.runDepositFunds(authenticatedAccount, transferAmount, 1);
				}
				
			}
	    // if they choose to exit change status variable 
		}else {
			transferStatus = true;
		}
	}while(!transferStatus);

}

//***************************************************************
//
//  Method:       userChoiceString
// 
//  Description:  takes an string from user. 
//
//  Parameters:   boolean questionType
//
//  Returns:      String userChoiceString
//
//**************************************************************
public String userChoiceString(int questionType) {
	boolean validInput = false; 
	String userChoiceString = "";
	// Use Scanner to receive user input
	Scanner userInput = new Scanner(System.in);
	do {
		//Get user input 
    	userChoiceString = userInput.nextLine();
        //Send user input to input validation
        validInput = inputValidation(userChoiceString, questionType);
        }while(!validInput);
	// Close scanner when program exits. 
	if (userChoiceString == "7") {
		userInput.close();
		}
	return userChoiceString;
	}// End userChoice


//***************************************************************
//
//  Method:       inputValidation
// 
//  Description:  validates the input of the program depending on what is being asked.  
//
//  Parameters:   String userChoiceString, boolean questionType
//
//  Returns:      boolean validity
//
//**************************************************************
public boolean inputValidation(String userChoiceString, int questionType) {
	// declare variable to hold bool validity
	boolean validInvalid = false;
	int validation = 0;
    // Main menu check
    if(questionType == 1) {
    	// If input is non numeric this will handle exception and indicate invalid input. 
    	try {
    		validation = Integer.parseInt(userChoiceString);
    	}catch (NumberFormatException e) {
    		validInvalid = false;
    	}
    	// If input is in the correct range will return valid
    	if(validation > 0 && validation <= 5) {
    		validInvalid = true;
    	}
    // Withdraw menu check. 
    }else if(questionType == 2) {
    	// If input is non numeric this will handle exception and indicate invalid input. 
    	try {
    		validation = Integer.parseInt(userChoiceString);
    	}catch (NumberFormatException e) {
    		validInvalid = false;
    	}
    	// If input is in the correct range will return valid
    	if(validation > 0 && validation <= 9) {
    		validInvalid = true;
    	}
    // numeric only check
    }else if (questionType == 3) {
    	validInvalid = true;
    	// If input is non numeric this will handle exception and indicate invalid input. 
    	try {
    		Double.parseDouble(userChoiceString);
    	}catch (NumberFormatException e) {
    		validInvalid = false;
    	}
    // checks if the user hits enter
    }else if (questionType == 4) {
    	validInvalid = true;
    	// If input does not equal "" not valid
    	if(!userChoiceString.equals("")) {
    		validInvalid = false;
    	}
    }else if(questionType == 5) {
    	// If input is non numeric this will handle exception and indicate invalid input. 
    	try {
    		validation = Integer.parseInt(userChoiceString);
    	}catch (NumberFormatException e) {
    		validInvalid = false;
    	}
    	// If input is in the correct range will return valid
    	if(validation == 1 || validation == 2) {
    		validInvalid = true;
    	}
    }
	// If the input is invalid, inform user. 
	if(!validInvalid)
		printOutput("Input Error, please try again.");
	return validInvalid;
}
//***************************************************************
//
//  Method:       printOutput
// 
//  Description:  Prints the output from the user selected operation 
//
//  Parameters:   N/A
//
//  Returns:      N/A
//
//**************************************************************
public void printOutput(String outputString) {
	// print nice looking output for user
	System.out.println("\n*************************\n");
	System.out.println(outputString);
	}// End printOutput method


//***************************************************************
//
//  Method:       developerInfo (Non Static)
// 
//  Description:  The developer information method of the program
//
//  Parameters:   None
//
//  Returns:      N/A 
//
//**************************************************************
    public void developerInfo()
    {
    	System.out.println("Name:    Marshal Pfluger");
	    System.out.println("Course:  COSC 4301 Modern Programming");
	    System.out.println("Project: Capstone\n\n");
	    } // End of the developerInfo method
}
