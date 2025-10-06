package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="expense_mon_totals_view")
@Data
public class ExpenseMonTotalsView  extends ExpenseMonTotalsViewBase{
	
}
