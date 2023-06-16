//********************************************************************
//
//  Developer:     Marshal Pfluger
//
//  Project #:     Capstone
//
//  File Name:     CustomerAccount.java
//
//  Course:        COSC 4301 Modern Programming
//
//  Due Date:      05/12/2023
//
//  Instructor:    Prof. Fred Kumi 
//
//  Java Version:  17.0.4.1
//
//  Description:   object to hold customer account details  
//
//********************************************************************
public class CustomerAccount {
	private int accountNumber;
	private int customerPin;
	private int customerId;

	public CustomerAccount(int accountNumber, int pinNumber) {
		setAccountNumber(accountNumber);
		setCustomerPin(pinNumber);
	}
    // Setter for customerId
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    // Setter for customerPin
    public void setCustomerPin(int customerPin) {
        this.customerPin = customerPin;
    }
    // Setter for accountNumber
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    // Getter for customerPin
    public int getCustomerPin() {
        return customerPin;
    }
    // Getter for accountNumber
    public int getAccountNumber() {
        return accountNumber;
    }
    // Getter for customerId
    public int getCustomerId() {
        return customerId;
    }
}
