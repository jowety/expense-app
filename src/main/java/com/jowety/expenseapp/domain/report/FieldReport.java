package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class FieldReport {

	Integer year;
	List<String> months;
	String fieldName;
	Map<String, Float> monthTotals = new HashMap<>();
	List<ReportMonthValues> fields = new ArrayList<>();

	public FieldReport(String fieldName) {
		super();
		this.fieldName = fieldName;
	}
}
