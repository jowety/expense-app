# expense-app
This is an lightweight expense manager application I wrote for myself for two reasons.
1. For my own use to track my expenses across multiple accounts WITHOUT paying a subscription or giving a company my bank logins. 
2. As a way to learn Angular and get more experience with Spring Boot and Docker and try out some things with my custom JPA data framework.

Platform
- Spring Boot back-end (this project) with a REST API controller
- Docker packaging (optional)
- MySQL database
- Angular 19 UI (expense-app-ng project)

Features
- Predefine your Accounts (e.g. checking, credit cards), Categories & Subcategories, Payees
- Payees can have a default account, category and subcategory that get prefilled when you create an expense
- Categories and Subcategories can have an optional budget amount defined
- Expense List view can be filtered by Month, Account, Payee, Category, and Subcategory with a dollar total calculated on the page. You can also hide or include future expenses
- Reports: 
	- Totals by category/subcategory, payee, or account. 
	- Budget comparison with totals by category/subcategory for a single month 
- Recurring Expenses: Define expenses to be automatically entered on each or every n weeks/months/years. Option to enter at the beginning of the period or on the exact day.

Building
1. Install MySQL 
2. Run the DDL script located under /database
3. Clone and install my dependency projects: jowety-common, client-search, and jowety-data
4. Clone this project and expense-app-ng in sibling folders
5. Update the database schema/username/password in the src/main/resources/application.properties to your own
6. (optional) Update the baseUrl variable in expense-app-ng/src/app/service/expense.service.ts to your local ip address if you want to access the page on a different device on your network. It is currently set to localhost.
7. Do a maven install on this project (mvn clean install -DskipTests). This will also compile the Angular UI app and copy the static files into the Spring Boot build. You'll then have a Spring Boot jar file under the /target directory you can run. Command line example: java -jar target/expense-app-0.0.1-SNAPSHOT.jar
8. Alternatively, if you have Docker installed, run the build.bat script. This will build the app, package it into a Docker container and then deploy/redeploy it to Docker.
9. Open http://localhost:8080 in the browser
