package com.jowety.expenseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseViewDao;
import com.jowety.expenseapp.dao.PayeeDao;
import com.jowety.expenseapp.dao.SubcategoryDao;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired AccountDao accountDao;
	@Autowired CategoryDao categoryDao;
	@Autowired SubcategoryDao subcategoryDao;
	@Autowired PayeeDao payeeDao;
	@Autowired ExpenseDao expenseDao;
	@Autowired ExpenseViewDao expenseViewDao;
	
	

}
