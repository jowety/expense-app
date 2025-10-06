package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.ExpenseCatMonTotalsView;

@Repository
@Transactional
public class ExpenseCatMonTotalsViewDaoImpl extends DaoImpl<ExpenseCatMonTotalsView>  implements ExpenseCatMonTotalsViewDao {

	
}
