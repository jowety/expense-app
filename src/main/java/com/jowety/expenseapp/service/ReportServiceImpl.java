package com.jowety.expenseapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jowety.data.query.Filter;
import com.jowety.data.query.Search;
import com.jowety.data.query.Select;
import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseViewDao;
import com.jowety.expenseapp.dao.PayeeDao;
import com.jowety.expenseapp.dao.SubcategoryDao;
import com.jowety.expenseapp.domain.Category;
import com.jowety.expenseapp.domain.ExpenseView;
import com.jowety.expenseapp.domain.Subcategory;
import com.jowety.expenseapp.domain.report.BudgetReport;
import com.jowety.expenseapp.domain.report.BudgetReportCategory;
import com.jowety.expenseapp.domain.report.BudgetReportSubcategory;
import com.jowety.expenseapp.domain.report.CategoryReport;
import com.jowety.expenseapp.domain.report.FieldReport;
import com.jowety.expenseapp.domain.report.ReportMonthValues;

import jakarta.persistence.Tuple;

@Service
public class ReportServiceImpl implements ReportService {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired AccountDao accountDao;
	@Autowired CategoryDao categoryDao;
	@Autowired SubcategoryDao subcategoryDao;
	@Autowired PayeeDao payeeDao;
	@Autowired ExpenseDao expenseDao;
	@Autowired ExpenseViewDao expenseViewDao;
	
	@Override
	public FieldReport getFieldReport(Integer year, String field) {
		FieldReport report = new FieldReport(field);
		report.setYear(year);
		List<String> months = expenseViewDao.getMonthList(year);
		report.setMonths(months);
		List<Filter<ExpenseView>> baseFilters = new ArrayList<>();
		baseFilters.add(new Filter<>("year", year));

		Search<ExpenseView> search = new Search<ExpenseView>()
				.select("monthString")
				.select(Select.sum("amount", "Total"))
				.addGroupByPath("monthString");
		search.setFilters(baseFilters);
		List<Tuple> topLevel = expenseViewDao.selectedSearch(search);
		for(Tuple t: topLevel) {
			report.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
		}

		List<String> fieldValues = expenseViewDao.search().select(field)
				.filter("year", year).orderByAsc(field).distinct().singleColumnSearch();
		for(String f: fieldValues) {
			ReportMonthValues rf = new ReportMonthValues(f);
			report.getFields().add(rf);
			List<Filter<ExpenseView>> fieldFilters = new ArrayList<>(baseFilters);
			fieldFilters.add(new Filter<ExpenseView>(field, f));
			search.setFilters(fieldFilters);
			List<Tuple> fieldLevel = expenseViewDao.selectedSearch(search);
			for(Tuple t: fieldLevel) {
				rf.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
			}
		}
		
		return report;
	}
	
	@Override
	public CategoryReport getCategoryReport(Integer year) {
		CategoryReport report = new CategoryReport();
		report.setYear(year);
		List<String> months = expenseViewDao.getMonthList(year);
		report.setMonths(months);
		List<Filter<ExpenseView>> baseFilters = new ArrayList<>();
		baseFilters.add(new Filter<>("year", year));

		Search<ExpenseView> search = new Search<ExpenseView>()
				.select("monthString")
				.select(Select.sum("amount", "Total"))
				.addGroupByPath("monthString");
		search.setFilters(baseFilters);
		List<Tuple> topLevel = expenseViewDao.selectedSearch(search);
		for(Tuple t: topLevel) {
			report.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
		}
		
		List<String> categories = expenseViewDao.search().select("category")
				.filter("year", year).orderByAsc("category").distinct().singleColumnSearch();
		for(String cat: categories) {
			ReportMonthValues rcat = new ReportMonthValues(cat);
			report.getCategories().add(rcat);
			List<Filter<ExpenseView>> catFilters = new ArrayList<>(baseFilters);
			catFilters.add(new Filter<ExpenseView>("category", cat));
			search.setFilters(catFilters);
			List<Tuple> catLevel = expenseViewDao.selectedSearch(search);
			for(Tuple t: catLevel) {
				rcat.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
			}
			List<String> subcategories = expenseViewDao.search().select("subcategory")
					.filter("year", year).filter("category", cat)
					.orderByAsc("subcategory").distinct().singleColumnSearch();
			for(String subcat: subcategories) {
				ReportMonthValues rsubcat = new ReportMonthValues(subcat);
				rcat.getSubs().add(rsubcat);
				List<Filter<ExpenseView>> subcatFilters = new ArrayList<>(catFilters);
				subcatFilters.add(new Filter<ExpenseView>("subcategory", subcat));
				search.setFilters(subcatFilters);
				List<Tuple> scatLevel = expenseViewDao.selectedSearch(search);
				for(Tuple t: scatLevel) {
					rsubcat.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
				}				
			}
		}

		return report;
	}
	
	@Override
	public BudgetReport getBudgetReport(Integer year, String month) {
		BudgetReport report = new BudgetReport();
		report.setYear(year);
		report.setMonth(month);
		List<Filter<ExpenseView>> baseFilters = new ArrayList<>();
		baseFilters.add(new Filter<>("year", year));
		baseFilters.add(new Filter<>("monthString", month));

		Search<ExpenseView> search = new Search<ExpenseView>()
				.select(Select.sum("amount", "Total"));
		search.setFilters(baseFilters);
		List<Float> results = (expenseViewDao.singleColumnSearch(search));
		report.setTotal(results.get(0));
		
		List<Category> categories = categoryDao.search().orderByAsc("name").results();
		for(Category cat: categories) {
			BudgetReportCategory rcat = new BudgetReportCategory(cat.getName());
			report.getCategories().add(rcat);
			rcat.setBudget(cat.getBudget());
			List<Filter<ExpenseView>> catFilters = new ArrayList<>(baseFilters);
			catFilters.add(new Filter<ExpenseView>("category", cat.getName()));
			search.setFilters(catFilters);
			List<Float> catTotal = (expenseViewDao.singleColumnSearch(search));
			rcat.setTotal(catTotal.get(0));

			List<Subcategory> subcategories = subcategoryDao.search().filter("category", cat).orderByAsc("name").results();
			for(Subcategory subcat: subcategories) {
				BudgetReportSubcategory rsubcat = new BudgetReportSubcategory(subcat.getName());
				rsubcat.setBudget(subcat.getBudget());
				rcat.getSubcategories().add(rsubcat);
				List<Filter<ExpenseView>> subcatFilters = new ArrayList<>(catFilters);
				subcatFilters.add(new Filter<ExpenseView>("subcategory", subcat.getName()));
				search.setFilters(subcatFilters);
				List<Float> scatTotal = (expenseViewDao.singleColumnSearch(search));
				rsubcat.setTotal(scatTotal.get(0));
			}
		}

		return report;
	}
	

//	@Override
//	public Report getExpenseReport(Integer year, String month) {
//		Report report = new Report(String.format("Expense Report for %s %s", month, year));
//		Search<ExpenseView> s = new Search<ExpenseView>()
//			.select(Select.sum("amount", "Total"))
//			.filter("year", year).filter("month", month);
//
//		List<Tuple> results = selectedSearch(s);	
//		this.addResultsToRowHolder(report, results, s);
//		s.select("category").addGroupByPath("category").orderByAsc("category");
//		results = selectedSearch(s);
//		ReportRow root = report.getRows().get(0);
//		this.addResultsToRowHolder(root, results, s);
//		s.select("subcategory").addGroupByPath("subcategory").orderByAsc("subcategory");
//		Collection<Filter<ExpenseView>> baseFilters = s.getFilters();
//		for(ReportRow rr: root.getRows()) {
//			String category = (String) rr.getCellByName("category").getValue();
//			List<Filter<ExpenseView>> filters = new ArrayList<>(baseFilters);
//			filters.add(new Filter<>("category", category));
//			s.setFilters(filters);
//			results = selectedSearch(s);
//			this.addResultsToRowHolder(rr, results, s);
//		}
//			
//		return report;
//	}
}
