package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.Payee;


@Repository
@Transactional
public class PayeeDaoImpl extends DaoImpl<Payee> implements PayeeDao {
}
