package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.RecurringExpense;


@Repository
@Transactional
public class RecurringExpenseDaoImpl extends DaoImpl<RecurringExpense> implements RecurringExpenseDao {
}
