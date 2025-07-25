package com.jowety.expenseapp;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jowety.data.client.search.SearchResult;
import com.jowety.data.client.search.SimpleSearch;
import com.jowety.data.query.Search;
import com.jowety.expenseapp.dao.AccountDao;
import com.jowety.expenseapp.dao.CategoryDao;
import com.jowety.expenseapp.dao.ExpenseDao;
import com.jowety.expenseapp.dao.ExpenseViewDao;
import com.jowety.expenseapp.dao.PayeeDao;
import com.jowety.expenseapp.dao.RecurringExpenseDao;
import com.jowety.expenseapp.dao.SubcategoryDao;
import com.jowety.expenseapp.domain.Account;
import com.jowety.expenseapp.domain.Category;
import com.jowety.expenseapp.domain.Expense;
import com.jowety.expenseapp.domain.ExpenseView;
import com.jowety.expenseapp.domain.Payee;
import com.jowety.expenseapp.domain.RecurringExpense;
import com.jowety.expenseapp.domain.Subcategory;
import com.jowety.expenseapp.domain.report.BudgetReport;
import com.jowety.expenseapp.domain.report.FieldReport;
import com.jowety.expenseapp.service.ReportService;

@RestController
@RequestMapping("/api")
public class ExpenseAppController {
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired AccountDao accountDao;
	@Autowired CategoryDao categoryDao;
	@Autowired SubcategoryDao subcategoryDao;
	@Autowired PayeeDao payeeDao;
	@Autowired ExpenseDao expenseDao;
	@Autowired ExpenseViewDao expenseViewDao;
	@Autowired RecurringExpenseDao recurringDao;
	@Autowired ReportService reportService;
	
	//ACCOUNTS
	@GetMapping("/account")
	public List<Account> getAccounts(){
		return accountDao
				.search()
				.orderBy("name", true)
				.results();
	}
	@GetMapping("/account/{id}")
	@Transactional
	public Account getAccount(@PathVariable String id){
		return accountDao.findById(id);
	}	
	@PostMapping("/account")
	@Transactional
	public Account saveAccount(@RequestBody Account account) {
		if(account.getId() == null) return accountDao.save(account);
		else return accountDao.update(account);
	}
	@PutMapping("/account")
	@Transactional
	public Account updateAccount(@RequestBody Account account) {
		return accountDao.update(account);
	}
	@DeleteMapping("/account/{id}")
	@Transactional
	public void deleteAccount(@PathVariable(required = true) String id) {
		accountDao.delete(id);
	}

	//PAYEES
	@GetMapping("/payee")
	public List<Payee> getPayees(){
		return payeeDao
				.search()
				.orderBy("name", true)
				.results();
	}
	@GetMapping("/payee/{id}")
	@Transactional
	public Payee getPayee(@PathVariable long id){
		return payeeDao.findById(id);
	}		
	@PostMapping("/payee")
	@Transactional
	public Payee savePayee(@RequestBody Payee payee) {
		if(payee.getId() == null) return payeeDao.save(payee);
		else return payeeDao.update(payee);
	}
	@DeleteMapping("/payee/{id}")
	@Transactional
	public void deletePayee(@PathVariable(required = true) long id) {
		payeeDao.delete(id);
	}
	
	//CATEGORIES
	@GetMapping("/category")
	public List<Category> getCategories(){
		return categoryDao
				.search()
				.orderBy("name", true)
				.results();
	}
	@GetMapping("/category/{id}")
	@Transactional
	public Category getCategory(@PathVariable String id){
		return categoryDao.findById(id);
	}
	@PostMapping("/category")
	@Transactional
	public Category saveCategory(@RequestBody Category category) {
		return categoryDao.save(category);
	}
	@PutMapping("/category")
	@Transactional
	public Category udpateCategory(@RequestBody Category category) {
		return categoryDao.update(category);
	}
	@DeleteMapping("/category/{id}")
	@Transactional
	public void deleteCategory(@PathVariable(required = true) String id) {
		categoryDao.delete(id);
	}
	
	//SUBCATEGORIES
	@GetMapping("/subcategory")
	public List<Subcategory> getSubcategories(@RequestParam(required = false) String categoryId){
		Search<Subcategory> search = subcategoryDao.search().orderBy("name", true);
		if(categoryId != null) search.filter("category.id", categoryId);
		return search.results();
	}
	@GetMapping("/subcategory/{id}")
	@Transactional
	public Subcategory getSubcategory(@PathVariable String id){
		return subcategoryDao.findById(id);
	}
	@PostMapping("/subcategory")
	@Transactional
	public Subcategory saveSubcategory(@RequestBody Subcategory category) {
		return subcategoryDao.save(category);
	}
	@PutMapping("/subcategory")
	@Transactional
	public Subcategory udpateSubcategory(@RequestBody Subcategory category) {
		return subcategoryDao.update(category);
	}
	@DeleteMapping("/subcategory/{id}")
	@Transactional
	public void deleteSubcategory(@PathVariable(required = true) String id) {
		subcategoryDao.delete(id);
	}
	
	//EXPENSE VIEWS
	@PostMapping("/expenseview/search")
	public SearchResult<ExpenseView> searchExpenseViews(@RequestBody(required = false) SimpleSearch search){
		if(search == null) {
			search = new SimpleSearch();
			search.getOrders().add("date desc");
		}
		SearchResult<ExpenseView> out = expenseViewDao.simpleSearchWithResultWrapper(search);
		return out;
	}
	@PostMapping("/expenseview/search/total")
	public Float getTotal(@RequestBody(required = true) SimpleSearch search){
		Float total = expenseViewDao.getTotalAmount(search);
		return total;
	}
	@GetMapping("/expenseview/years")
	public List<Integer> getAvailableYears() {
		return expenseViewDao.search().select("year").orderByDesc("year").distinct().singleColumnSearch();
	}
	@GetMapping("/expenseview/months")
	public List<String> getAvailableMonths(@RequestParam Integer year) {
		List<Integer> months = expenseViewDao.search().select("monthNumber")
				.filter("year", year).orderByDesc("monthNumber")
				.distinct().singleColumnSearch();
		List<String> out  = months.stream().map(i -> Month.of(i).getDisplayName(TextStyle.FULL, Locale.US)).toList();
		return out;
	}
	
	//EXPENSES
	@GetMapping("/expense/{id}")
	@Transactional
	public Expense getExpense(@PathVariable long id) {
		Expense ex = expenseDao.findById(id);
		return ex;
	}	
	@PostMapping("/expense")
	@Transactional
	public Expense saveExpense(@RequestBody Expense expense) {
		if(expense.getId() == null) return expenseDao.save(expense);
		else return expenseDao.update(expense);
	}
	@DeleteMapping("/expense/{id}")
	@Transactional
	public void deleteExpense(@PathVariable(required = true) long id) {
		expenseDao.delete(id);
	}

	//RECURRING EXPENSES
	@GetMapping("recurring")
	public List<RecurringExpense> getRecurring(){
		List<RecurringExpense> results = recurringDao.search().orderBy("payee.name", true).results();
		return results;
	}
	@GetMapping("/recurring/{id}")
	@Transactional
	public RecurringExpense getRecurring(@PathVariable long id) {
		RecurringExpense ex = recurringDao.findById(id);
		return ex;
	}	
	@PostMapping("/recurring")
	@Transactional
	public RecurringExpense saveRecurring(@RequestBody RecurringExpense expense) {
		if(expense.getId() == null) return recurringDao.save(expense);
		else return recurringDao.update(expense);
	}
	/*
	 * @DeleteMapping("/recurring/{id}")
	 * 
	 * @Transactional public void deleteRecurring(@PathVariable(required = true)
	 * long id) { recurringDao.delete(id); }
	 */
	
	//REPORTS
	@GetMapping("/reports/budget")
	public BudgetReport getBudgetReport(@RequestParam Integer year, @RequestParam String month) {
		return reportService.getBudgetReport(year, month);
	}
	@GetMapping("/reports/field")
	public FieldReport getFieldReport(@RequestParam Integer year, @RequestParam String field) {
		return reportService.getFieldReport(year, field);
	}
	
	
	//ERROR TESTS
	@GetMapping("test400")
	public ResponseEntity<?> test400() {
		// Returns HTTP 400 Bad Request with a custom error object
        return new ResponseEntity<>("Custom 400 Error Message from ExpenseApp server",HttpStatus.BAD_REQUEST);// Returns HTTP 400 Bad Request
	}
	@GetMapping("test500")
	public ResponseEntity<?> test500() {
		// Returns HTTP 400 Bad Request with a custom error object
        return new ResponseEntity<>("Custom 500 Error Message from ExpenseApp server",HttpStatus.INTERNAL_SERVER_ERROR);// Returns HTTP 500
	}
	
}
