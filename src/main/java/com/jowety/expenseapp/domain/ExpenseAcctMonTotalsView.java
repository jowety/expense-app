package com.jowety.expenseapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="expense_acct_mon_totals_view")
@Data
public class ExpenseAcctMonTotalsView extends ExpenseMonTotalsViewBase{
	
	@Id private String account;
}
