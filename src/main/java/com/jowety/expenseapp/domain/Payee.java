package com.jowety.expenseapp.domain;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="PAYEE")
@Data
public class Payee {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToOne
	@JoinColumn(name = "account_default")
	private Account accountDefault;
	@ManyToOne
	@JoinColumn(name = "category_default")
	private Category categoryDefault;
	@ManyToOne
	@JoinColumn(name = "subcategory_default")
	private Subcategory subcategoryDefault;
	private String description;
	private boolean excluded;
	
	@Formula("(select count(*) from expense ex where ex.payee_id = id) > 0")
	private boolean inUse;
	
}
