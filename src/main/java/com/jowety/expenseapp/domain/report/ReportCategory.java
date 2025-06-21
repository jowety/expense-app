package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReportCategory {

	String name;
	Map<String, Float> monthTotals = new HashMap<>();
	List<ReportSubcategory> subcategories = new ArrayList<>();
	
	public ReportCategory(String name) {
		super();
		this.name = name;
	}
}
