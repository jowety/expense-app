package com.jowety.expenseapp.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.client.search.Report;
import com.jowety.data.client.search.SimpleSearch;
import com.jowety.data.dao.DaoImpl;
import com.jowety.data.query.Search;
import com.jowety.data.query.Select;
import com.jowety.data.query.Select.Aggregate;
import com.jowety.data.query.SimpleSearchConverter;
import com.jowety.expenseapp.domain.ExpenseView;

import jakarta.persistence.Tuple;


@Repository
@Transactional
public class ExpenseViewDaoImpl extends DaoImpl<ExpenseView> implements ExpenseViewDao {
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Float getTotalAmount(SimpleSearch simpleSearch) {
		Search<ExpenseView> search = SimpleSearchConverter.convertIn(simpleSearch, ExpenseView.class);
		return (Float) getAggregateValue(Aggregate.SUM, "amount", search.getFilters());
	}
	
	
	@Override
	public List<String> getMonthList(Integer year){
		List<Tuple> ints = search()
				.select("monthString","monthNumber")
				.filter("year", year)
				.orderByAsc("monthNumber")
				.distinct()
				.selectedResults();
		List<String> strings = ints.stream().map(i-> i.get(0, String.class)).collect(Collectors.toList());
		return strings;
	}
	
	@Override
	public Report getTotalsByField(String field, Integer year, String month) {
		Search<ExpenseView> s = new Search<ExpenseView>()
			.select(field)	
			.select("monthString")
			.select(Select.sum("amount", "Total"))
			.addGroupByPath("payee")
			.addGroupByPath("monthString")
			.filter("year", year)
//			.filter("monthString", month)
			.orderByAsc(field);
		Report report = report(s);
		return report;
	}
	
}
