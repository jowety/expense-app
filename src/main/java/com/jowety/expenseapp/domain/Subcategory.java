package com.jowety.expenseapp.domain;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="SUBCATEGORY")
@Data
public class Subcategory {

	@Id 
	private String id;
	private String name;
	private String description;
	@ManyToOne
	private Category category;
	
	private Float budget;

	@Formula("(select count(*) from expense ex where ex.subcategory_id = id) > 0")
	private boolean inUse;
}
