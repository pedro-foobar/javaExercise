package com.cardfraud.processors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.model.Fraud;
import com.cardfraud.utils.DateUtils;

/**
 * Implementation of Fraud detection
 * 
 * @author Pedro
 *
 */
public class FraudDetection {

	private LocalDate dayFilter;

	protected Map<String, Double> cardMap = new HashMap<String, Double>();

	protected List<Fraud> fraudList = new ArrayList<Fraud>();

	protected double amountThreshold;

	/**
	 * launch the fraud detection given a transactions list, threshold and date
	 * 
	 * 
	 * @param transactionList
	 * @param amountThreshold
	 * @param dayFilter
	 * @return
	 */
	public List<Fraud> detectFraud(List<CardTransaction> transactionList, double amountThreshold, LocalDate dayFilter) {
		this.dayFilter = dayFilter;
		this.amountThreshold = amountThreshold;
		for (CardTransaction cardTransaction : transactionList) {

			LocalDateTime transactionDate = cardTransaction.getTransactionDate();

			// here we compare the date given dayFilter to the transaction date
			// because we want to take only the transactions happening at the date given in
			// dayFilter
			if (isTransactionDateWithinDayFilter(transactionDate)) {

				String cardNumber = cardTransaction.getHashCardNumber();
				double transactionAmount = cardTransaction.getTransactionAmount();

				// populating the map of card number given by a transaction
				// and iterating the transaction amount if the card was already used in a
				// transaction
				addCardAndAmount(cardNumber, transactionAmount);
			}
		}
		buildFraudList();
		return fraudList;
	}

	private void addCardAndAmount(String cardNumber, double transactionAmount) {

		if (isCardInMap(cardNumber)) {
			double oldTotalAmount = cardMap.get(cardNumber);
			double newTotalAmount = transactionAmount + oldTotalAmount;
			cardMap.put(cardNumber, newTotalAmount);
		} else {
			cardMap.put(cardNumber, transactionAmount);
		}

	}

	private boolean isCardInMap(String cardNumber) {
		if (cardMap.get(cardNumber) == null)
			return false;
		return true;
	}

	/**
	 * for each card added in the card map, we create a fraud object if the total
	 * amount for a card is supperior or equal to the threshold given
	 */
	private void buildFraudList() {
		cardMap.forEach((cardNumber, amount) -> {
			if (amount >= amountThreshold)
				fraudList.add(new Fraud(cardNumber, amount, dayFilter));
		});
	}

	private boolean isTransactionDateWithinDayFilter(LocalDateTime transactionDate) {
		boolean isSameDay = DateUtils.isOnSameDay(transactionDate, dayFilter);
		return isSameDay;
	}

	/**
	 * method which give a file path , will parse the file and return a java
	 * representation of the transaction list in that file
	 * 
	 * @param inputPath
	 * @return transactions list
	 * @throws IOException
	 * @throws DateTimeParseException
	 * @throws NumberFormatException
	 */
	public List<CardTransaction> readTransactionListFromFile(String inputPath)
			throws IOException, DateTimeParseException, NumberFormatException {
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();

		Stream<String> lines = Files.lines(Paths.get(inputPath));
		lines.forEach(line -> {
			CardTransaction item = parseSingleTransaction(line);
			transactionList.add(item);
		});
		lines.close();
		return transactionList;
	}

	/**
	 * transform a transaction given as a string , in a java CardTransaction object
	 * 
	 * @param line
	 * @return transaction
	 */
	public CardTransaction parseSingleTransaction(String line) {
		String[] elements = line.replaceAll("\\s+", "").split(",");
		String cardNumber = elements[0];
		LocalDateTime transactionDate = DateUtils.parseStringDateTime(elements[1]);
		double amount = Double.parseDouble(elements[2]);

		return new CardTransaction(cardNumber, amount, transactionDate);
	}

}
