package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="expense_cat_mon_totals_view")
@Data
public class ExpenseCatMonTotalsView  extends ExpenseMonTotalsViewBase{
	
	@Id private String category;
}
