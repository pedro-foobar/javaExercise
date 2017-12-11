package com.cardfraud.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.multi.FileTransactionReaderRunnable;

public class runMultiMode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();
		
		List<Callable<List<CardTransaction>>> callableTasks = new ArrayList<Callable<List<CardTransaction>>>();
		
		List<CardTransaction> cardTransactions = new ArrayList<CardTransaction>();
		
		FraudDetection detector = new FraudDetection();
		
		for (int i = 0; i < args.length; i++) {
			String inputPath = args[i];
			Runnable task = new FileTransactionReaderRunnable(detector, inputPath,cardTransactions);
			executor.submit(task);
		}
		
		try {
			if(executor.awaitTermination(10, TimeUnit.SECONDS)){
				System.out.println("done !");
				System.out.println("number of items inserted in list "+cardTransactions.size());
			}
		} catch (InterruptedException e) {
			System.out.println("error !");
			e.printStackTrace();
		}
		
	}

}
