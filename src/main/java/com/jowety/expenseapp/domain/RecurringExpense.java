package com.jowety.expenseapp.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="RECURRING_EXPENSE")
@Data
public class RecurringExpense {
	
	public static enum InsertStrategy{
		MONTH, //1st of the month, MONTHS and YEARS frequency only
		WEEK, //start of week (Monday), only for WEEKS frequency
		DAY; //on the actual day, for all frequencies
	}
	
	public static enum Frequency{
		MONTHS, YEARS, WEEKS;
		//MONTHS and YEARS frequency can use MONTH or DAY insert strategy
		//WEEKS frequency can use WEEK or DAY insert strategy
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Account account;
	@ManyToOne
	private Payee payee;
	@ManyToOne
	private Subcategory subcategory;
	
	private Float amount;
	private boolean amountVaries;
	
	@Enumerated(EnumType.STRING)
	private Frequency frequency;
	private Integer month;//only used for frequency=YEARS
	private Integer day;//day of the month
	private Integer every;//every X frequency
	private LocalDate startDate;//required for every > 1

	@Enumerated(EnumType.STRING)
	private InsertStrategy insertOption;//when to insert the Expense
	private String notes;	
}
