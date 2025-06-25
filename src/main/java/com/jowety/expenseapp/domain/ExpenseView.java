package com.jowety.expenseapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="EXPENSE_VIEW", schema="EXPENSE_DB")
@Data
public class ExpenseView {
	
	@Id 
	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
	private Integer year;
	private Integer monthNumber;
	private String monthString;
	private String account;
	private String payee;
	private String category;
	private String subcategory;
	private Float amount;
	private String notes;	
}
