package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="expense_subcat_mon_totals_view")
@Data
public class ExpenseSubcatMonTotalsView  extends ExpenseMonTotalsViewBase{
	
	@Id private String category;
	@Id private String subcategory;
}
