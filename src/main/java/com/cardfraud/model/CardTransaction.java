package com.cardfraud.model;

import java.time.LocalDateTime;

/**
 * representation of a single credit card transaction
 * 
 * @author Pedro
 *
 */
public class CardTransaction {

	private String hashCardNumber;

	private double transactionAmount;

	private LocalDateTime transactionDate;

	public CardTransaction(String hashCardNumber, double transactionAmount, LocalDateTime transactionDate) {
		this.hashCardNumber = hashCardNumber;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
	}

	public String getHashCardNumber() {
		return hashCardNumber;
	}

	public void setHashCardNumber(String hashCardNumber) {
		this.hashCardNumber = hashCardNumber;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

}
