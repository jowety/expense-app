package com.jowety.expenseapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.client.search.SearchResult;
import com.jowety.data.client.search.SimpleSearch;
import com.jowety.data.query.Search;
import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseViewDao;
import com.jowety.expenseapp.dao.PayeeDao;
import com.jowety.expenseapp.dao.SubcategoryDao;
import com.jowety.expenseapp.domain.Account;
import com.jowety.expenseapp.domain.Category;
import com.jowety.expenseapp.domain.Expense;
import com.jowety.expenseapp.domain.ExpenseView;
import com.jowety.expenseapp.domain.Payee;
import com.jowety.expenseapp.domain.Subcategory;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired AccountDao accountDao;
	@Autowired CategoryDao categoryDao;
	@Autowired SubcategoryDao subcategoryDao;
	@Autowired PayeeDao payeeDao;
	@Autowired ExpenseDao expenseDao;
	@Autowired ExpenseViewDao expenseViewDao;
	
	@Override
	public List<Account> getAccounts(){
		return accountDao
				.search()
				.orderBy("name", true)
				.results();
	}
	
	@Override
	public List<Payee> getPayees(){
		return payeeDao
				.search()
				.orderBy("name", true)
				.results();
	}

	@Override
	public List<Category> getCategories(){
		return categoryDao
				.search()
				.orderBy("name", true)
				.results();
	}
	
	@Override
	public List<Subcategory> getSubcategories(String categoryId){
		Search<Subcategory> search = subcategoryDao.search().orderBy("name", true);
		if(categoryId != null) search.filter("category.id", categoryId);
		return search.results();
	}
	
	@Override
	public SearchResult<ExpenseView> getExpenseViews(SimpleSearch search){
		return expenseViewDao.simpleSearchWithResultWrapper(search);
	}
	
	public Expense getExpenseById(Long id) {
		return expenseDao.findById(id);
	}
	
	@Override
	@Transactional
	public Expense saveExpense(Expense expense) {
		if(expense.getId() == null) return expenseDao.save(expense);
		else return expenseDao.update(expense);
	}

}
