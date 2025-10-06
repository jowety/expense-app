create or replace view expense_view as
select
expense.id as id,
expense.date as date,
year(expense.date) as year,
month(expense.date) as month_number,
monthname(expense.date) as month_string,
account.name as account,
payee.name as payee,
category.name as category,
subcategory.name as subcategory,
expense.amount as amount,
expense.notes as notes,
expense.auto_insert as auto_insert,
expense.estimate as estimate,
account.excluded as account_excluded,
payee.excluded as payee_excluded,
category.excluded as category_excluded,
subcategory.excluded as subcategory_excluded
from expense expense
join account account on expense.account_id = account.id
join payee payee on expense.payee_id = payee.id
join subcategory subcategory on expense.subcategory_id = subcategory.id
join category category on subcategory.category_id = category.id;

create or replace view expense_mon_totals_view as
select
year,
month_string,
month_number,
sum(amount) as total
from expense_view ev
where ev.account_excluded = 0 and ev.payee_excluded = 0 and ev.category_excluded = 0 and ev.subcategory_excluded = 0
group by year, month_string, month_number;

create or replace view expense_cat_mon_totals_view as
select
year,
month_string,
month_number,
category,
sum(amount) as total
from expense_view ev
where ev.account_excluded = 0 and ev.payee_excluded = 0 and ev.category_excluded = 0 and ev.subcategory_excluded = 0
group by year, month_string, month_number, category;

create or replace view expense_subcat_mon_totals_view as
select
year,
month_string,
month_number,
category,
subcategory,
sum(amount) as total
from expense_view ev
where ev.account_excluded = 0 and ev.payee_excluded = 0 and ev.category_excluded = 0 and ev.subcategory_excluded = 0
group by year, month_string, month_number, category, subcategory;

create or replace view expense_acct_mon_totals_view as
select
year,
month_string,
month_number,
account,
sum(amount) as total
from expense_view ev
where ev.account_excluded = 0 and ev.payee_excluded = 0 and ev.category_excluded = 0 and ev.subcategory_excluded = 0
group by year, month_string, month_number, account;

create or replace view expense_payee_mon_totals_view as
select
year,
month_string,
month_number,
payee,
sum(amount) as total
from expense_view ev
where ev.account_excluded = 0 and ev.payee_excluded = 0 and ev.category_excluded = 0 and ev.subcategory_excluded = 0
group by year, month_string, month_number, payee;

