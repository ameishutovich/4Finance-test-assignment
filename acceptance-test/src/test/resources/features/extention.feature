Feature: Loan extention
  In order to extend a loan
  A user
  Should be able to apply for extention

  Scenario: Apply for successful extention of loan
      Given I have a loan
      When I pass loan id to api
      Then my loan should be extended for a week
      And  the interest should be increased by a factor of '1.5'.
