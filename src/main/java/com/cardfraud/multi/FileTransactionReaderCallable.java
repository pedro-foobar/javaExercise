package com.cardfraud.multi;

import java.util.List;
import java.util.concurrent.Callable;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.processors.FraudDetection;

public class FileTransactionReaderCallable implements Callable<List<CardTransaction>> {

	private FraudDetection detector;
	private String inputPath; 
	private List<CardTransaction> cardTransactions;
	
	
	public FileTransactionReaderCallable(FraudDetection detector, String inputPath, List<CardTransaction> cardTransactions) {
		this.detector = detector;
		this.inputPath = inputPath;
		this.cardTransactions = cardTransactions;
	}



	@Override
	public List<CardTransaction> call() throws Exception {
		return detector.readTransactionListFromFile(inputPath);
	}

}
