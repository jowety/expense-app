package com.jowety.expenseapp.domain.report;

import lombok.Data;

@Data
public class Stats {

	Float min;
	Float max;
	Float average1;//Average across all completed months
	Float average2;//Average across only months with data
	Float total;
}
