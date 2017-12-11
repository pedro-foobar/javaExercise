package com.cardfraud.multi;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.Callable;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.processors.FraudDetection;

public class FileTransactionReaderRunnable implements Runnable {

	private FraudDetection detector;
	private String inputPath; 
	private List<CardTransaction> cardTransactions;
	
	
	public FileTransactionReaderRunnable(FraudDetection detector, String inputPath, List<CardTransaction> cardTransactions) {
		this.detector = detector;
		this.inputPath = inputPath;
		this.cardTransactions = cardTransactions;
	}



	@Override
	public void run() {
		try {
			cardTransactions.addAll(detector.readTransactionListFromFile(inputPath));
		} catch (DateTimeParseException | NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
