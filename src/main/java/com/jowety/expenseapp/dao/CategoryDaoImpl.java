package com.jowety.expenseapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.Category;


@Repository
@Transactional
public class CategoryDaoImpl extends DaoImpl<Category> implements CategoryDao {
}
