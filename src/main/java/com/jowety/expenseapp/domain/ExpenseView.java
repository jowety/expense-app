package com.jowety.expenseapp.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="EXPENSE_VIEW")
@Data
public class ExpenseView {
	
	@Id 
	private Long id;
	private LocalDate date;
	private Integer year;
	private Integer monthNumber;
	private String monthString;
	private String account;
	private String payee;
	private String category;
	private String subcategory;
	private Float amount;
	private String notes;	
	private boolean autoInsert;//inserted by recurring framework
	private boolean estimate;//flag for variable recurring expenses
}
