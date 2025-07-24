package com.jowety.expenseapp.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="RECURRING_EXPENSE_STATUS")
@Data
public class RecurringExpenseStatus {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Expense expense;
	@ManyToOne
	private RecurringExpense recurring;
	private Date insertDate;
}
