package com.cardfraud.model;

import java.time.LocalDate;

public class Fraud {

	private String hashCardNumber;

	private double totalTransactionAmount;

	private LocalDate fraudDate;

	public Fraud(String hashCardNumber, double totalTransactionAmount, LocalDate fraudDate) {
		this.hashCardNumber = hashCardNumber;
		this.totalTransactionAmount = totalTransactionAmount;
		this.fraudDate = fraudDate;
	}

	public String getHashCardNumber() {
		return hashCardNumber;
	}

	public void setHashCardNumber(String hashCardNumber) {
		this.hashCardNumber = hashCardNumber;
	}

	public double getTotalTransactionAmount() {
		return totalTransactionAmount;
	}

	public void setTotalTransactionAmount(double totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}

	public LocalDate getFraudDate() {
		return fraudDate;
	}

	public void setFraudDate(LocalDate fraudDate) {
		this.fraudDate = fraudDate;
	}

}
