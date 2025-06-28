package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReportMonthValues {

	String name;
	Map<String, Float> monthTotals = new HashMap<>();
	List<ReportMonthValues> subs = new ArrayList<>();
	
	public ReportMonthValues(String name) {
		super();
		this.name = name;
	}
}
