package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CategoryReport {

	Integer year;
	List<String> months;
	Map<String, Float> monthTotals = new HashMap<>();
	List<ReportMonthValues> categories = new ArrayList<>();
}
