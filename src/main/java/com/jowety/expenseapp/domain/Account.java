package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="ACCOUNT", schema="EXPENSE_DB")
@Data
public class Account {
	
	@Id 
	private String id;
	private String name;
	@Enumerated(EnumType.STRING)
	private AccountType type;
}
