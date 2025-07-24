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
expense.estimate as estimate
from expense expense
join account account on expense.account_id = account.id
join payee payee on expense.payee_id = payee.id
join subcategory subcategory on expense.subcategory_id = subcategory.id
join category category on subcategory.category_id = category.id