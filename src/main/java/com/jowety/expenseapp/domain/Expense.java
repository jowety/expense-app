package com.jowety.expenseapp.domain;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="EXPENSE")
@Data
public class Expense {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	@ManyToOne
	private Account account;
	@ManyToOne
	private Payee payee;
	private Float amount;
	@ManyToOne
	private Subcategory subcategory;
	private String notes;	
	@Column(name = "import_desc")
	private String importedDescription;
}
