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
		for(String fieldValue: fieldValues) {
			ReportMonthValues monthValues = new ReportMonthValues(fieldValue);
			report.getFields().add(monthValues);
			List<Filter<ExpenseView>> fieldFilters = new ArrayList<>(baseFilters);
			fieldFilters.add(new Filter<ExpenseView>(field, fieldValue));
			search.setFilters(fieldFilters);
			List<Tuple> fieldLevel = expenseViewDao.selectedSearch(search);
			for(Tuple t: fieldLevel) {
				monthValues.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
			}
			if(field.equals("category")) {
				List<String> subcategories = expenseViewDao.search().select("subcategory")
						.filter("year", year).filter("category", fieldValue)
						.orderByAsc("subcategory").distinct().singleColumnSearch();
				for(String subcat: subcategories) {
					ReportMonthValues rsubcat = new ReportMonthValues(subcat);
					monthValues.getSubs().add(rsubcat);
					List<Filter<ExpenseView>> subcatFilters = new ArrayList<>(fieldFilters);
					subcatFilters.add(new Filter<ExpenseView>("subcategory", subcat));
					search.setFilters(subcatFilters);
					List<Tuple> scatLevel = expenseViewDao.selectedSearch(search);
					for(Tuple t: scatLevel) {
						rsubcat.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
					}				
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

}
