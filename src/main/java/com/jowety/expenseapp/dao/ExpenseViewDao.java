package com.jowety.expenseapp.dao;

import java.util.List;

import com.jowety.data.client.search.SimpleSearch;
import com.jowety.data.dao.DaoIF;
import com.jowety.expenseapp.domain.ExpenseView;

public interface ExpenseViewDao extends DaoIF<ExpenseView> {

	List<String> getMonthList(Integer year);

	Float getTotalAmount(SimpleSearch simpleSearch);

}
