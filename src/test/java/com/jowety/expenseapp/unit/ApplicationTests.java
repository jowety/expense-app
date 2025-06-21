package com.jowety.expenseapp.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.jowety.data.client.search.Report;
import com.jowety.data.query.Filter;
import com.jowety.data.query.Search;
import com.jowety.data.query.Select;
import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseViewDao;
import com.jowety.expenseapp.dao.PayeeDao;
import com.jowety.expenseapp.dao.SubcategoryDao;
import com.jowety.expenseapp.domain.ExpenseView;
import com.jowety.expenseapp.domain.report.BudgetReport;
import com.jowety.expenseapp.service.ReportService;
import com.jowety.util.TestUtil;

@SpringBootTest
@Rollback(false)
class ApplicationTests extends TestUtil{	

	static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);
	
	@Autowired AccountDao accountDao;
	@Autowired CategoryDao categoryDao;
	@Autowired SubcategoryDao subcategoryDao;
	@Autowired PayeeDao payeeDao;
	@Autowired ExpenseDao expenseDao;
	@Autowired ExpenseViewDao expenseViewDao;
	@Autowired ReportService reportService;
	
//	@Test
	public void testGets() {
//		logResults(accountDao.findAll());
		logResults(categoryDao.findAll());
		logResults(subcategoryDao.findAll());
//		logResults(payeeDao.findAll());
//		logResults(expenseDao.findAll());
//		logResults(expenseViewDao.findAll());
	}
//	@Test
	public void testQuery() {

		List<Filter<ExpenseView>> baseFilters = new ArrayList<>();
		baseFilters.add(new Filter<>("year", 2025));
		baseFilters.add(new Filter<>("monthString", "June"));

		Search<ExpenseView> search = new Search<ExpenseView>()
				.select(Select.sum("amount", "Total"));
		search.setFilters(baseFilters);
		List<Float> results = (expenseViewDao.singleColumnSearch(search));
		log(results);
	}
	
//	@Test
	public void testReport1() {
		Search<ExpenseView> s = new Search<ExpenseView>()
			.select("year", "month", "category")
			.select("subcategory")
			.select(Select.sum("amount", "Total"))
			.addGroupByPath("category")
			.addGroupByPath("subcategory")
			.filter("year", 2025).filter("month", 5)
			.orderByAsc("category")
			.orderByAsc("subcategory");
		Report report = expenseViewDao.report(s);
		System.out.println(report.toString());
	}
	
	@Test
	public void testReport() {
		BudgetReport report = reportService.getBudgetReport(2025, "June");
		System.out.println(report.toString());
	}
//	@Test
	public void testMonthList() {
		logResults(expenseViewDao.getMonthList(2025));
	}
}
