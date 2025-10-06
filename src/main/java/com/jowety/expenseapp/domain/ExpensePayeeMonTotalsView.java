package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="expense_payee_mon_totals_view")
@Data
public class ExpensePayeeMonTotalsView  extends ExpenseMonTotalsViewBase{
	
	@Id private String payee;
}
