package com.jowety.expenseapp.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.RecurringExpenseDao;
import com.jowety.expenseapp.dao.RecurringExpenseStatusDao;
import com.jowety.expenseapp.domain.Expense;
import com.jowety.expenseapp.domain.RecurringExpense;
import com.jowety.expenseapp.domain.RecurringExpense.Frequency;
import com.jowety.expenseapp.domain.RecurringExpense.InsertStrategy;
import com.jowety.expenseapp.domain.RecurringExpenseStatus;

@Service
public class RecurringServiceImpl implements RecurringService {

	@Autowired ExpenseDao expenseDao;
	@Autowired RecurringExpenseDao recurringDao;
	@Autowired RecurringExpenseStatusDao recurringStatusDao;

	@Override
	@Scheduled(cron = "0 0 1 * * *")
	@Transactional
	public void runDailyInsertJob() {
		LocalDate tday = LocalDate.now();
		List<RecurringExpense> recurrings = recurringDao.findAll();
		for(RecurringExpense recur: recurrings) {
			if(isMatch(recur, tday)) {
				saveRecurringExpense(recur, tday);
			}
		}
	}
	
	@Override
	public void saveRecurringExpense(RecurringExpense recur, LocalDate tday) {
		//create Expense and RecurringExpenseStatus
		Expense ex = new Expense();
		ex.setAccount(recur.getAccount());
		ex.setPayee(recur.getPayee());
		ex.setSubcategory(recur.getSubcategory());
		ex.setAmount(recur.getAmount());
		ex.setAutoInsert(true);
		if(recur.isAmountVaries()) ex.setEstimate(true);
		LocalDate exDate = LocalDate.of(tday.getYear(), tday.getMonth(), recur.getDay());
		ex.setDate(exDate);
		ex = expenseDao.save(ex);
		
		RecurringExpenseStatus status = new RecurringExpenseStatus();
		status.setExpense(ex);
		status.setRecurring(recur);
		status.setInsertDate(new Date());
		recurringStatusDao.save(status);
	}

	@Override
	public boolean isMatch(RecurringExpense recur, LocalDate now) {
		if(recur.getEvery() == 1) {
			if(recur.getFrequency() == Frequency.MONTHS) {
				return monthMatchLogic(recur, now);
			}
			else if(recur.getFrequency() == Frequency.YEARS) {
				return yearMatchLogic(recur, now);
			}
			else if(recur.getFrequency() == Frequency.WEEKS) {
				return weekMatchLogic(recur, now);
			}
		}
		else if(recur.getEvery() > 1 && recur.getStartDate() != null) {
			if(recur.getFrequency() == Frequency.MONTHS) {
				LocalDate startRounded = LocalDate.of(recur.getStartDate().getYear(), recur.getStartDate().getMonth(), 1);
				LocalDate nowRounded = LocalDate.of(now.getYear(), now.getMonth(), 1);
				long between = ChronoUnit.MONTHS.between(startRounded, nowRounded);
				long remain = between % recur.getEvery();
				if(remain == 0) {
					return monthMatchLogic(recur, now);
				}
			}
			else if(recur.getFrequency() == Frequency.YEARS) {
				long between = now.getYear() - recur.getStartDate().getYear();
				long remain = between % recur.getEvery();
				if(remain == 0) {
					return yearMatchLogic(recur, now);			
				}
			}
			else if(recur.getFrequency() == Frequency.WEEKS) {
				LocalDate startCopy = LocalDate.of(recur.getStartDate().getYear(), recur.getStartDate().getMonth(), recur.getStartDate().getDayOfMonth());
				int startDayOfWeek = recur.getStartDate().getDayOfWeek().getValue();
				if(startDayOfWeek > 1) {
					startCopy = startCopy.minusDays(startDayOfWeek - 1);
				}
				LocalDate nowCopy = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
				int nowDayOfWeek = now.getDayOfWeek().getValue();
				if(nowDayOfWeek > 1) {
					nowCopy = nowCopy.minusDays(nowDayOfWeek - 1);
				}
				long between = ChronoUnit.WEEKS.between(startCopy, nowCopy);
				long remain = between % recur.getEvery();
				if(remain == 0) {
					return weekMatchLogic(recur, now);
				}
			}
		}
		return false;
	}
	
	public boolean monthMatchLogic(RecurringExpense recur, LocalDate now) {
		if(now.getDayOfMonth() == 1 && recur.getInsertOption() == InsertStrategy.MONTH) {
			return true;
		}
		else if(recur.getInsertOption() == InsertStrategy.DAY 
				&& recur.getDay() == now.getDayOfMonth()) {
			return true;
		}
		return false;
	}
	public boolean yearMatchLogic(RecurringExpense recur, LocalDate now) {
		if(now.getDayOfMonth() == 1 && recur.getInsertOption() == InsertStrategy.MONTH 
				&& recur.getMonth() == now.getMonthValue()) {
			return true;
		}
		else if(recur.getInsertOption() == InsertStrategy.DAY 
				&& recur.getMonth() == now.getMonthValue() && recur.getDay() == now.getDayOfMonth()) {
			return true;
		}
		return false;
	}
	public boolean weekMatchLogic(RecurringExpense recur, LocalDate now) {
		if(now.getDayOfWeek().getValue() == 1 && recur.getInsertOption() == InsertStrategy.WEEK) {
			return true;
		}
		else if(recur.getInsertOption() == InsertStrategy.DAY
				&& recur.getDay() == now.getDayOfWeek().getValue()) {
			return true;
		}
		return false;
	}

}
