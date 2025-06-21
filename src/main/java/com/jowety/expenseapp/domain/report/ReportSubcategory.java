package com.jowety.expenseapp.domain.report;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ReportSubcategory {

	String name;
	Map<String, Float> monthTotals = new HashMap<>();
	
	public ReportSubcategory(String name) {
		super();
		this.name = name;
	}
}
