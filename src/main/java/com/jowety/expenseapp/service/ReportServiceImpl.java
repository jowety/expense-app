package com.jowety.expenseapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jowety.data.dao.DaoIF;
import com.jowety.data.query.Filter;
import com.jowety.data.query.Filter.MatchMode;
import com.jowety.data.query.Search;
import com.jowety.data.query.Select;
import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseAcctMonTotalsViewDao;
import com.jowety.expenseapp.dao.ExpenseCatMonTotalsViewDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseMonTotalsViewDao;
import com.jowety.expenseapp.dao.ExpensePayeeMonTotalsViewDao;
import com.jowety.expenseapp.dao.ExpenseSubcatMonTotalsViewDao;
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
	@Autowired ExpenseMonTotalsViewDao expenseMonTotalsViewDao;
	@Autowired ExpenseCatMonTotalsViewDao expenseCatMonTotalsViewDao;
	@Autowired ExpenseSubcatMonTotalsViewDao expenseSubcatMonTotalsViewDao;
	@Autowired ExpenseAcctMonTotalsViewDao expenseAcctMonTotalsViewDao;
	@Autowired ExpensePayeeMonTotalsViewDao expensePayeeMonTotalsViewDao;
	
	@Override
	public FieldReport getFieldReport(Integer year, String field) {
		DaoIF fieldTotalDao = null;
		switch (field) {
			case "category": fieldTotalDao = expenseCatMonTotalsViewDao; break;
			case "account": fieldTotalDao = expenseAcctMonTotalsViewDao; break;
			case "payee": fieldTotalDao = expensePayeeMonTotalsViewDao; break;
			default:
				throw new RuntimeException("Invalid field for field report: " + field);
		}
		
		FieldReport report = new FieldReport(field);
		report.setYear(year);
		List<String> months = expenseViewDao.getMonthList(year);//get list of months with data in the year
		int monthCountExcluding = expenseViewDao.getMonthCountExcludingCurrent(year).intValue();
		report.setMonths(months);
		List<Filter> baseFilters = new ArrayList<>();
		baseFilters.add(new Filter<>("year", year));//base filters have the year set

		//Month total expenses
		Search search = new Search()	//Our search object will be reused
				.select("monthString", "total");	//month and total are selected
		search.setFilters(baseFilters);
		List<Tuple> topLevel = expenseMonTotalsViewDao.selectedSearch(search);
		for(Tuple t: topLevel) {
			report.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
		}
		
		//Create 2 stat searches: filter out the current month for min/max/avg but include it for total
		Search statSearchFiltered = new Search()
				.select(Select.min("total"))
				.select(Select.max("total"))
				.select(Select.avg("total"))
				.select(Select.sum("total"))
				.select(Select.count("total"));
		List<Filter> stat1Filters = new ArrayList<>(baseFilters);//copy base filters
		statSearchFiltered.setFilters(stat1Filters);
		//filter out the current month
		statSearchFiltered.filter("monthNumber", LocalDate.now().getMonthValue(), MatchMode.NOT_EQUAL);
		
		Search statSearchUnfiltered = new Search()
				.select(Select.sum("total"));
		List<Filter> stat2Filters = new ArrayList<>(baseFilters);//copy base filters
		statSearchUnfiltered.setFilters(stat2Filters);
		

		//Month total stats
		List<Tuple> topStats1 = expenseMonTotalsViewDao.selectedSearch(statSearchFiltered);
		for(Tuple t: topStats1) {
			report.getStats().setMin(getFloat(t.get(0)));
			report.getStats().setMax(getFloat(t.get(1)));
			report.getStats().setAverage1(getFloat(t.get(2)));
			report.getStats().setAverage2(getFloat(t.get(2)));
		}
		List<Tuple> topStats2 = expenseMonTotalsViewDao.selectedSearch(statSearchUnfiltered);
		for(Tuple t: topStats2) {
			report.getStats().setTotal(getFloat(t.get(0)));
		}

		//Find all the field values (categories/accounts/payees) available
		List<String> fieldValues = fieldTotalDao.search().select(field)
				.filter("year", year).orderByAsc(field).distinct().singleColumnSearch();
		
		//Iterate through each field value to get the month totals
		for(String fieldValue: fieldValues) {
			ReportMonthValues monthValues = new ReportMonthValues(fieldValue);
			report.getFields().add(monthValues);
			List<Filter> fieldFilters = new ArrayList<>(baseFilters);//copy base filters
			fieldFilters.add(new Filter(field, fieldValue));//filter by the field
			search.setFilters(fieldFilters);//overwrite the filters
			List<Tuple> fieldLevel = fieldTotalDao.selectedSearch(search);//month and total already selected
			for(Tuple t: fieldLevel) {//set the Month and Total values for this category/account/payee
				monthValues.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
			}
			
			//stats for field totals
			List<Filter> stat1FieldFilters = new ArrayList<>(stat1Filters);//copy stat1 filters
			stat1FieldFilters.add(new Filter(field, fieldValue));//filter by the field
			statSearchFiltered.setFilters(stat1FieldFilters);//overwrite the filters
			List<Tuple> fieldStats1 = fieldTotalDao.selectedSearch(statSearchFiltered);
			for(Tuple t: fieldStats1) {
				Float minWData = getFloat(t.get(0));
				Float sum = getFloat(t.get(3));
				Long count = (Long) t.get(4);
				
				Float min = count < monthCountExcluding? 0F: minWData;
				Float avg = sum / monthCountExcluding;
				
				monthValues.getStats().setMin(min);
				monthValues.getStats().setMax(getFloat(t.get(1)));
				monthValues.getStats().setAverage1(avg);
				monthValues.getStats().setAverage2(getFloat(t.get(2)));
			}
			List<Filter> stat2FieldFilters = new ArrayList<>(stat2Filters);//copy stat2 filters
			stat2FieldFilters.add(new Filter(field, fieldValue));//add the field
			statSearchUnfiltered.setFilters(stat2FieldFilters);//overwrite the filters
			List<Tuple> fieldStats2 = fieldTotalDao.selectedSearch(statSearchUnfiltered);
			for(Tuple t: fieldStats2) {
				monthValues.getStats().setTotal(getFloat(t.get(0)));
			}

			
			//For categories, also query for the subcategories under each
			if(field.equals("category")) {
				//get the list of distinct subcategories in the current category
				List<String> subcategories = expenseSubcatMonTotalsViewDao.search()
						.select("subcategory")
						.filter("year", year)
						.filter("category", fieldValue)
						.orderByAsc("subcategory").distinct().singleColumnSearch();
				
				//get totals for each subcategory
				for(String subcat: subcategories) {
					ReportMonthValues rsubcat = new ReportMonthValues(subcat);
					monthValues.getSubs().add(rsubcat);
					//copy fieldFilters (already has year and category) and add the subcategory
					List<Filter<ExpenseView>> subcatFilters = new ArrayList<>(fieldFilters);
					subcatFilters.add(new Filter("subcategory", subcat));
					//overwrite the filters in our search, already has month & total selected
					search.setFilters(subcatFilters);
					List<Tuple> scatLevel = expenseSubcatMonTotalsViewDao.selectedSearch(search);
					for(Tuple t: scatLevel) {
						rsubcat.getMonthTotals().put((String)t.get(0), (Float)t.get(1));
					}			

					//Subcategory Stats
					List<Filter> stat1SubcatFilters = new ArrayList<>(stat1FieldFilters);//copy stat1 field filters
					stat1SubcatFilters.add(new Filter("subcategory", subcat));//add the subcategory
					statSearchFiltered.setFilters(stat1SubcatFilters);//overwrite the filters
					List<Tuple> subcatStats1 = expenseSubcatMonTotalsViewDao.selectedSearch(statSearchFiltered);
					for(Tuple t: subcatStats1) {
						Float minWData = getFloat(t.get(0));
						Float sum = getFloat(t.get(3));
						Long count = (Long) t.get(4);
						
						Float min = count < monthCountExcluding? 0F: minWData;
						Float avg = sum / monthCountExcluding;
						
						rsubcat.getStats().setMin(min);
						rsubcat.getStats().setMax(getFloat(t.get(1)));
						rsubcat.getStats().setAverage1(avg);
						rsubcat.getStats().setAverage2(getFloat(t.get(2)));
					}
					List<Filter> stat2SubcatFilters = new ArrayList<>(stat2FieldFilters);//copy stat2 field filters
					stat2SubcatFilters.add(new Filter("subcategory", subcat));//add the subcategory
					statSearchUnfiltered.setFilters(stat2SubcatFilters);//overwrite the filters
					List<Tuple> subcatStats2 = expenseSubcatMonTotalsViewDao.selectedSearch(statSearchUnfiltered);
					for(Tuple t: subcatStats2) {
						rsubcat.getStats().setTotal(getFloat(t.get(0)));
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

	private Float getFloat(Object val) {
		if(val == null) return 0F;
		if(val instanceof Float) return (Float)val;
		if(val instanceof Double) {
			Double d = (Double)val;
			return d.floatValue();
		}
		else throw new RuntimeException("Unexpected data type returned");
	}
}
