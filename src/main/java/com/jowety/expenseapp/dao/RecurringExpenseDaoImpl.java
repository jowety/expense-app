package com.jowety.expenseapp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.data.dao.DaoImpl;
import com.jowety.expenseapp.domain.RecurringExpense;
import com.jowety.expenseapp.domain.RecurringExpense.Frequency;

import jakarta.persistence.Tuple;


@Repository
@Transactional
public class RecurringExpenseDaoImpl extends DaoImpl<RecurringExpense> implements RecurringExpenseDao {
	
	@Override
	public Float getMonthTotal() {
		List<Tuple> all = search().select("amount", "frequency", "every").selectedResults();
		float yrTotal = 0;
		for(Tuple t: all) {
			Float amount = (Float) t.get(0);
			Frequency frequency = (Frequency) t.get(1);
			Integer every = (Integer) t.get(2);
			
			int multiplier = 12;
			if(frequency == Frequency.MONTHS) multiplier = 12/every;
			else if(frequency == Frequency.WEEKS) multiplier = 52/every;
			else if(frequency == Frequency.YEARS) multiplier = 1;

			yrTotal += amount * multiplier;
		}
		return yrTotal / 12;
	}
}
