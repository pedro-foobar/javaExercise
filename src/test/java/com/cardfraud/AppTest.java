package com.cardfraud;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.cardfraud.model.CardTransaction;
import com.cardfraud.model.Fraud;
import com.cardfraud.processors.FraudDetection;
import com.cardfraud.utils.DateUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
		System.out.println("Test case :" + testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * test date format with time
	 */
	public void testDateTimeFormat() {
		String dateString = "2001-12-03T12:34:56";
		LocalDateTime date = DateUtils.parseStringDateTime(dateString);
		assertEquals("dates should be equal ", dateString, date.toString());
	}

	/**
	 * test date format without time
	 */
	public void testDateFormat() {
		String dateString = "2001-12-03";
		LocalDate date = DateUtils.parseStringDateNoTime(dateString);
		assertEquals("dates should be equal ", "2001-12-03", date.toString());
	}

	/**
	 * test the comparison based on date without time
	 */
	public void testCompareOnDay() {
		String dateTimeString = "2001-12-03T12:34:56";
		LocalDateTime dateTime = DateUtils.parseStringDateTime(dateTimeString);
		String dateString = "2001-12-03";
		LocalDate date = DateUtils.parseStringDateNoTime(dateString);

		boolean compareDay = DateUtils.isOnSameDay(dateTime, date);
		assertEquals("should return true ", true, compareDay);
	}

	/********************************
	 * Test FraudDetection behaviors * TDD approach *
	 ********************************/

	/**
	 * test for a single transaction with fraud
	 */
	public void testAddSimpleTransactionWithFraud() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-10");

		CardTransaction cardTransaction = new CardTransaction("card", 100,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction);

		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);

		assertEquals("Fraud list should contains 1 element", 1, fraudList.size());
	}

	/**
	 * test with a single transaction where date is not on given day
	 */
	public void testAddSimpleTransactionWithoutFraudOnGivenDay() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-09");

		CardTransaction cardTransaction = new CardTransaction("card", 100,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction);

		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);

		assertEquals("Fraud list should contains 0 element", 0, fraudList.size());
	}

	/**
	 * test with a single transaction not getting over the threshold amount
	 */
	public void testAddSimpleTransactionWithoutFraudOnThatDay() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-10");

		CardTransaction cardTransaction = new CardTransaction("card", 10,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction);

		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);

		assertEquals("Fraud list should contains 0 element", 0, fraudList.size());
	}

	/**
	 * test with two transaction on same card, resulting in amount >= fraud, on the given day
	 */
	public void testAddTwoTransactionFromSameCardWithFraud() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-10");

		CardTransaction cardTransaction1 = new CardTransaction("card", 10,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		CardTransaction cardTransaction2 = new CardTransaction("card", 90,
				DateUtils.parseStringDateTime("2000-10-10T12:00:01"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction1);
		transactionList.add(cardTransaction2);
		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);

		assertEquals("Fraud list should contains 1 element", 1, fraudList.size());
	}

	/**
	 * test with 2 transaction from 2 card, being both in fraud
	 */
	public void testAddTwoTransactionWithFraud() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-10");

		CardTransaction cardTransaction1 = new CardTransaction("card1", 100,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		CardTransaction cardTransaction2 = new CardTransaction("card2", 101,
				DateUtils.parseStringDateTime("2000-10-10T12:00:01"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction1);
		transactionList.add(cardTransaction2);
		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);

		assertEquals("Fraud list should contains 2 element", 2, fraudList.size());
	}

	/**
	 * test the card hashed number is being retrieved 
	 */
	public void testAddTwoTransactionWithFraudDisplayName() {

		double Threshold = 100;
		LocalDate date = DateUtils.parseStringDateNoTime("2000-10-10");

		CardTransaction cardTransaction1 = new CardTransaction("card1", 100,
				DateUtils.parseStringDateTime("2000-10-10T12:00:00"));
		CardTransaction cardTransaction2 = new CardTransaction("card2", 101,
				DateUtils.parseStringDateTime("2000-10-10T12:00:01"));
		List<CardTransaction> transactionList = new ArrayList<CardTransaction>();
		transactionList.add(cardTransaction1);
		transactionList.add(cardTransaction2);
		FraudDetection fraudDetection = new FraudDetection();
		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, Threshold, date);
		String cardNumbers = "";
		cardNumbers = fraudList.get(0).getHashCardNumber() + "," + fraudList.get(1).getHashCardNumber();

		assertEquals("card numbers should be ", "card1,card2", cardNumbers);

	}

	/**
	 * test single line parsing
	 * @throws Exception
	 */
	public void testParseSingleLine() throws Exception {
		String line = "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00";

		FraudDetection fraudDetection = new FraudDetection();
		CardTransaction cardTransaction = fraudDetection.parseSingleTransaction(line);
		assertEquals("card number should be", "10d7ce2f43e35fa57d1bbf8b1e2", cardTransaction.getHashCardNumber());
		assertEquals("transaction date should be", "2014-04-29T13:15:54",
				cardTransaction.getTransactionDate().toString());
		assertEquals("transaction amount should be", 10.00, cardTransaction.getTransactionAmount());
	}

	/**
	 * test parsing a file with transactions
	 * @throws IOException
	 */
	public void testParseFileWithTwoTransaction() throws IOException {
		FraudDetection fraudDetection = new FraudDetection();
		int transactionNumber = fraudDetection.readTransactionListFromFile("src/test/resources/file_two_transaction")
				.size();
		assertEquals(2, transactionNumber);
	}

	
	public void testParseFileWithDateMalformatted() throws IOException {
		FraudDetection fraudDetection = new FraudDetection();
		boolean rightException = false;
		try {
			fraudDetection.readTransactionListFromFile("src/test/resources/file_date_malformated");
		} catch (DateTimeParseException e) {
			rightException = true;
		}
		assertTrue(rightException);
	}

	public void testParseFileWithWrongFilePath() {
		FraudDetection fraudDetection = new FraudDetection();
		boolean rightException = false;
		try {
			fraudDetection.readTransactionListFromFile("non_existing_file");
		} catch (DateTimeParseException e) {
		} catch (IOException e) {
			rightException = true;
		}
		assertTrue(rightException);
	}

	public void testParseFileWithMalformattedAmount() throws IOException {
		FraudDetection fraudDetection = new FraudDetection();
		boolean rightException = false;
		try {
			fraudDetection.readTransactionListFromFile("src/test/resources/file_amount_malformated");
		} catch (DateTimeParseException e) {

		} catch (NumberFormatException e) {
			rightException = true;
		}
		assertTrue(rightException);
	}

	/**
	 * test the whole implementation
	 * @throws IOException
	 */
	public void testGlobalDetection() throws DateTimeParseException, NumberFormatException, IOException {
		FraudDetection fraudDetection = new FraudDetection();

		List<CardTransaction> transactionList = fraudDetection
				.readTransactionListFromFile("src/test/resources/transaction_test_file");

		List<Fraud> fraudList = fraudDetection.detectFraud(transactionList, 10, LocalDate.parse("2014-04-29"));
		assertEquals("Fraud list should contains 3 element", 3, fraudList.size());
	}
}
