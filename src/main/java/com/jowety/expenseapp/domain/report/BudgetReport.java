package com.jowety.expenseapp.domain.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BudgetReport {

	Integer year;
	String month;
	Float total;
	List<BudgetReportCategory> categories = new ArrayList<>();
}
