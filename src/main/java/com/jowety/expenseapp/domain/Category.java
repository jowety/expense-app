package com.jowety.expenseapp.domain;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="CATEGORY", schema="EXPENSE_DB")
@Data
public class Category {
	
	@Id 
	private String id;
	private String name;
	private Float budget;

	@Formula("(select count(*) from subcategory sc where sc.category_id = id) > 0")
	private boolean inUse;
}
