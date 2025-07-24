package com.jowety.expenseapp.unit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.RecurringExpenseDao;
import com.jowety.expenseapp.dao.RecurringExpenseStatusDao;
import com.jowety.expenseapp.service.RecurringService;
import com.jowety.util.TestUtil;

@SpringBootTest()
@ActiveProfiles("test")
@Rollback(false)
class RecurringTests extends TestUtil{	

	static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);
	
	@Autowired ExpenseDao expenseDao;
	@Autowired RecurringExpenseDao recurringDao;
	@Autowired RecurringExpenseStatusDao recurringStatusDao;
	@Autowired RecurringService recurringService;
	

	@Test
	public void testRunJob() {
		recurringService.runDailyInsertJob();
	}
}
