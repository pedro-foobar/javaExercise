package com.cardfraud.processors;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.model.Fraud;
import com.cardfraud.utils.DateUtils;

public class DetectionRunner {

	/**
	 * entry point of the program
	 * must be run with 3 args 
	 * -  file path being tested eg. /path/file
	 * -  amount threshold in format XX.XX
	 * -  date in format YYYY-mm-DD
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Number of arguments is incorrect. "
					+ "Please provide the path for the input file, the input threshold and the input date");
			System.exit(1);
		}
		new DetectionRunner().LaunchFraudDetection(args[0], args[1], args[2]);
	}

	public void LaunchFraudDetection(String inputPath, String inputThreshold, String inputDate) {
		//validate inputs
		double amountThreshold;
		try {
			amountThreshold = Double.parseDouble(inputThreshold);
		} catch (NumberFormatException e) {

			System.err.println("bad amount argument : expected XX.XX");
			return;
		}

		LocalDate dayFilter;
		try {
			dayFilter = DateUtils.parseStringDateNoTime(inputDate);
		} catch (DateTimeParseException e) {
			System.err.println("bad date argument : expected YYYY-mm-DD");
			return;
		}

		//reading file given in input
		FraudDetection fraudDetection = new FraudDetection();
		List<CardTransaction> transactionList = null;
		try {
			transactionList = fraudDetection.readTransactionListFromFile(inputPath);
		} catch (DateTimeParseException e) {
			System.err.println("Date malformated in the file");
			System.exit(1);
		} catch (NumberFormatException e) {
			System.err.println("Amount malformated in the file");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("File not found, provide a valid path");
			System.exit(1);
		}catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("One line is malformated");
			System.exit(1);
		}

		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, amountThreshold, dayFilter);

		logFraudList(fraudList);
	}

	private void logFraudList(List<Fraud> fraudList) {
		if(fraudList.isEmpty()) {
			System.out.println("No fraud detected this day");
		}
		for(Fraud fraud : fraudList) {
			System.out.println("Fraud detected on "+fraud.getFraudDate().toString()+" | hashed card number "+fraud.getHashCardNumber()+
					" | total transaction amount this day "+fraud.getTotalTransactionAmount());
		}

	}
}
