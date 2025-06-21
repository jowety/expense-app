package com.jowety.expenseapp.service;

import com.jowety.expenseapp.domain.report.BudgetReport;
import com.jowety.expenseapp.domain.report.ExpenseReport;

public interface ReportService {


	BudgetReport getBudgetReport(Integer year, String month);

	ExpenseReport getExpenseReport(Integer year);

}
