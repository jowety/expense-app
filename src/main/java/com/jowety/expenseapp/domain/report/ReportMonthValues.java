package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReportMonthValues {

	String name;//field value (example: category name)
	Map<String, Float> monthTotals = new HashMap<>();//month name to total map
	List<ReportMonthValues> subs = new ArrayList<>();//nested values like subcategories
	
	public ReportMonthValues(String name) {
		super();
		this.name = name;
	}
}
