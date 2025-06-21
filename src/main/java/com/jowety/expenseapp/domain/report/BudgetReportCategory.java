package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BudgetReportCategory {
	
	String name;
	Float total;
	Float budget;
	List<BudgetReportSubcategory> subcategories = new ArrayList<>();
	
	public BudgetReportCategory(String name) {
		super();
		this.name = name;
	}
}
