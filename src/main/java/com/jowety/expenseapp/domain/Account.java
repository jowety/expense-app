package com.jowety.expenseapp.domain;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="ACCOUNT")
@Data
public class Account {
	
	@Id 
	private String id;
	private String name;
	@Enumerated(EnumType.STRING)
	private AccountType type;

	@Formula("(select count(*) from expense ex where ex.account_id = id) > 0 "
	+ "OR (select count(*) from payee p where p.account_default = id) > 0")
	private boolean inUse;
}
