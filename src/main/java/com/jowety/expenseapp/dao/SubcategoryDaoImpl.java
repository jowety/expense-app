package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.Subcategory;


@Repository
@Transactional
public class SubcategoryDaoImpl extends DaoImpl<Subcategory> implements SubcategoryDao {
}
