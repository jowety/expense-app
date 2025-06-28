package com.jowety.expenseapp.service;

import com.jowety.expenseapp.domain.report.BudgetReport;
import com.jowety.expenseapp.domain.report.CategoryReport;
import com.jowety.expenseapp.domain.report.FieldReport;

public interface ReportService {


	BudgetReport getBudgetReport(Integer year, String month);

	CategoryReport getCategoryReport(Integer year);

	FieldReport getFieldReport(Integer year, String field);

}
