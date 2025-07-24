package com.jowety.expenseapp.unit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jowety.expenseapp.domain.RecurringExpense;
import com.jowety.expenseapp.domain.RecurringExpense.Frequency;
import com.jowety.expenseapp.domain.RecurringExpense.InsertStrategy;
import com.jowety.expenseapp.service.RecurringServiceImpl;

public class RecurringMatchUnitTests {
	
	RecurringServiceImpl serv = new RecurringServiceImpl();

	
	@ParameterizedTest
	@CsvSource({
		"1,'MONTHS', , 15, 'MONTH',, '2025-07-01', true",
		"1,'MONTHS', , 15, 'MONTH',, '2025-07-15', false",
		"1,'MONTHS', , 15, 'DAY',, '2025-07-01', false",
		"1,'MONTHS', , 15, 'DAY',, '2025-07-15', true",
		"2,'MONTHS', , 15, 'MONTH', '2025-01-15', '2025-06-01', false",
		"2,'MONTHS', , 15, 'DAY', '2025-01-15', '2025-06-15', false",
		"2,'MONTHS', , 15, 'MONTH', '2025-01-15', '2025-07-01', true",
		"2,'MONTHS', , 15, 'MONTH', '2025-01-15', '2025-07-15', false",
		"2,'MONTHS', , 15, 'DAY', '2025-01-15', '2025-07-01', false",
		"2,'MONTHS', , 15, 'DAY', '2025-01-15', '2025-07-15', true",

		"1,'YEARS', 7, 15, 'MONTH', , '2025-07-01', true",
		"1,'YEARS', 7, 15, 'DAY', , '2025-07-15', true",
		"1,'YEARS', 7, 15, 'MONTH', , '2025-07-15', FALSE",
		"1,'YEARS', 7, 15, 'DAY', , '2025-07-01', FALSE",
		"2,'YEARS', 7, 15, 'MONTH', '2023-07-15', '2025-07-01', true",
		"2,'YEARS', 7, 15, 'DAY', '2023-07-15', '2025-07-15', true",
		"2,'YEARS', 7, 15, 'MONTH', '2023-07-15', '2025-07-15', FALSE",
		"2,'YEARS', 7, 15, 'DAY', '2023-07-15', '2025-07-01', FALSE",
		"2,'YEARS', 7, 15, 'MONTH', '2024-07-15', '2025-07-01', FALSE",
		"2,'YEARS', 7, 15, 'DAY', '2024-07-15', '2025-07-15', FALSE",

		"1,'WEEKS', , 4, 'DAY', , '2025-07-03', true",
		"1,'WEEKS', , 4, 'WEEK', , '2025-06-30', true",
		"1,'WEEKS', , 4, 'DAY', , '2025-07-01', FALSE",
		"1,'WEEKS', , 4, 'WEEK', , '2025-07-01', FALSE",
		"2,'WEEKS', , 4, 'DAY', '2025-06-05', '2025-07-03', true",
		"2,'WEEKS', , 4, 'WEEK', '2025-06-05', '2025-06-30', true",
		"2,'WEEKS', , 4, 'DAY', '2025-06-05', '2025-06-26', false",
		"2,'WEEKS', , 4, 'WEEK', '2025-06-05', '2025-06-23', false",
		"2,'WEEKS', , 4, 'DAY', '2025-06-05', '2025-07-01', false",
		"2,'WEEKS', , 4, 'WEEK', '2025-06-05', '2025-07-01', false",
		
	})
	public void testMatrix(Integer every, Frequency freq, Integer month, Integer day, InsertStrategy insert, 
			LocalDate startDate, LocalDate today, boolean expected) {
		RecurringExpense recur = new RecurringExpense();
		recur.setEvery(every);
		recur.setFrequency(freq);
		recur.setMonth(month);
		recur.setDay(day);
		recur.setInsertOption(insert);
		recur.setStartDate(startDate);
		boolean result = serv.isMatch(recur, today);
		assertEquals(expected, result);
	}
}
