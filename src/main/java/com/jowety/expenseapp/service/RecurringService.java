package com.jowety.expenseapp.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;

import com.jowety.expenseapp.domain.RecurringExpense;

public interface RecurringService {

	boolean isMatch(RecurringExpense recur, LocalDate now);

	void saveRecurringExpense(RecurringExpense recur, LocalDate date);

	void runDailyInsertJob();

}
