create or replace view expense_db.expense_view as
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
expense.notes as notes
from expense_db.expense expense
join expense_db.account account on expense.account_id = account.id
join expense_db.payee payee on expense.payee_id = payee.id
join expense_db.category category on expense.category_id = category.id
join expense_db.subcategory subcategory on expense.subcategory_id = subcategory.id