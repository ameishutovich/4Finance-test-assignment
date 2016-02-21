# 4Finance-test-assignment
My implementation of a test assignment for 4Finance.

## 4finance Homework - Micro-Lending app

### Goal
Create a simple micro-lending app api similar to one of our existing products.

### Business requirements

* User can apply for loan by passing amount and term to api.
* Loan application risk analysis is performed. Risk is considered too high if:
    * the attempt to take loan is made after 00:00 with max possible amount.
    * reached max applications (e.g. 3) per day from a single IP.
* Loan is issued if there are no risks associated with the application. If so, client gets response status ok. However, if risk is surrounding the application, client error with message.
* Client should be able to extend a loan. Loan term gets extended for one week, interest gets increased by a factor of 1.5.
* The whole history of loans is visible for clients, including loan extensions.

### Technical requirements

* Backend in Java 6+, XML-less Spring, Hibernate.
* Code is production-ready, covered with unit tests.
* Acceptance tests for the happy path scenario included.
* Ability to run application from the command line.

### What gets evaluated

* Requirements
* Code quality (both production and test)
* How simple it is to run the application (embedded DB/embedded container)

###How to work with project

To start application from command line you need:
  1. run ` clean install ` from root
  2. go to ` lending-rest ` module, target directory
  3. from cmd run ` java -jar lending-rest.jar `

To run acceptance tests you need:
  1. run server (see above)
  2. go to ` acceptance-test ` module
  3. run ` MicroLendingApplicationTests.java ` and ` HistoryTestRunner.java ` classes
