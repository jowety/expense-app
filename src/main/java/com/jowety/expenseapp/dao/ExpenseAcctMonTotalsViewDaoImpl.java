package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.ExpenseAcctMonTotalsView;

@Repository
@Transactional
public class ExpenseAcctMonTotalsViewDaoImpl extends DaoImpl<ExpenseAcctMonTotalsView>  implements ExpenseAcctMonTotalsViewDao {

	
}
