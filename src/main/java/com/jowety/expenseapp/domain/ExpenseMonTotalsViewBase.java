package com.jowety.expenseapp.domain;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class ExpenseMonTotalsViewBase {
	
	@Id private Integer year;
	@Id private Integer monthNumber;
	private String monthString;
	private Float total;
}
