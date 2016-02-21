Feature: Apply for loan
	In order to apply for loan
	A user
	Should be able to pass amount and term to api

    Scenario Outline: Apply for successful loan
          Given I've less than 3 applications during a day
          And   it isn't between midnight and 7 am
          When I pass valid '<amount>' and <term> to api
          Then I should see a "<successful message>".
    Examples:
      | amount | term | successful message |
      | 200.55 | 4 | Your loan is successfully issued. |