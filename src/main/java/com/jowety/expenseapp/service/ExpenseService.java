package com.jowety.expenseapp.service;

import java.util.List;

import com.jowety.data.client.search.SearchResult;
import com.jowety.data.client.search.SimpleSearch;
import com.jowety.expenseapp.domain.Account;
import com.jowety.expenseapp.domain.Category;
import com.jowety.expenseapp.domain.Expense;
import com.jowety.expenseapp.domain.ExpenseView;
import com.jowety.expenseapp.domain.Payee;
import com.jowety.expenseapp.domain.Subcategory;

public interface ExpenseService {

	List<Account> getAccounts();

	List<Payee> getPayees();

	List<Category> getCategories();

	List<Subcategory> getSubcategories(String categoryId);

	SearchResult<ExpenseView> getExpenseViews(SimpleSearch search);

	Expense saveExpense(Expense expense);

}
