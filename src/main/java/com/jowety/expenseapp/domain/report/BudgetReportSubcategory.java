package com.jowety.expenseapp.domain.report;

import lombok.Data;

@Data
public class BudgetReportSubcategory {

	String name;
	Float total;
	Float budget;
	
	public BudgetReportSubcategory(String name) {
		super();
		this.name = name;
	}
}
