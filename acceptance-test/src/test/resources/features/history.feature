Feature: Applications' history
  In order to see the whole history of loans and loan extensions
  A user
  Should be able to get the history

  Scenario: Get history of loans and extentions
    Given I have 1 loan and 1 extention
    When I ask for history of loans
    Then a list of my loans and extentions should be printed.