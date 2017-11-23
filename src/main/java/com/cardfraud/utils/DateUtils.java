package com.cardfraud.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to be used for date processing across all the application for
 * re-usability
 * 
 * @author Pedro
 *
 */
public class DateUtils {

	/**
	 * Date format given by the card transactions
	 */
	public final static DateTimeFormatter CARD_TRANSACTION_DATE_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public final static DateTimeFormatter CARD_TRANSACTION_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	public static LocalDate parseStringDateNoTime(String dateString) {

		final LocalDate date = LocalDate.parse(dateString, CARD_TRANSACTION_DATE_FORMAT);
		return date;
	}

	public static LocalDateTime parseStringDateTime(String dateString) {

		final LocalDateTime date = LocalDateTime.parse(dateString, CARD_TRANSACTION_DATE_TIME_FORMAT);
		return date;
	}

	public static boolean isOnSameDay(LocalDateTime transactionDate, LocalDate dayFilter) {
		int compare = transactionDate.toLocalDate().compareTo(dayFilter);
		if (compare == 0) {
			return true;
		} else {
			return false;
		}
	}

}
