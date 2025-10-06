package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.ExpenseSubcatMonTotalsView;

@Repository
@Transactional
public class ExpenseSubcatMonTotalsViewDaoImp extends DaoImpl<ExpenseSubcatMonTotalsView>  implements ExpenseSubcatMonTotalsViewDao {

	
}
