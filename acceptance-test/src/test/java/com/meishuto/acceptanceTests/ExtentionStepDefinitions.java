package com.meishuto.acceptanceTests;

import com.meishuto.common.dto.ResponseMessage;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.dto.LoanCreation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.meishuto.acceptanceTests.MicroLendingApplicationTests.SERVER_URL;

public class ExtentionStepDefinitions {

    private Loan loan;
    private Loan extLoan;
    private RestTemplate restTemplate = MicroLendingApplicationTests.restTemplate();

    @Given("^I have a loan$")
    public void i_have_a_loan() throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoanCreation creation = new LoanCreation(new BigDecimal("3487.273"), 7);
        HttpEntity<LoanCreation> entity = new HttpEntity<LoanCreation>(creation, headers);
        ResponseEntity<ResponseMessage> responseEntity = restTemplate.postForEntity(SERVER_URL + "/loan", entity, ResponseMessage.class);
        ResponseMessage body = responseEntity.getBody();
        loan = body.getLoan();
        Assert.assertNotNull(body.getMessage(), loan);
    }

    @When("^I pass loan id to api$")
    public void i_pass_loan_id_to_api() throws Throwable {
        ResponseEntity<ResponseMessage> message = restTemplate.postForEntity(SERVER_URL + "/loan/" + loan.getId() + "/extention", null, ResponseMessage.class);
        ResponseMessage body = message.getBody();
        Assert.assertEquals("Your loan is successfully extended for a week.", body.getMessage());
        extLoan = body.getLoan();
        long extId = extLoan.getId();
        Assert.assertFalse(extId == -1);
    }

    @Then("^the interest should be increased by a factor of '(\\d+\\.\\d+)'\\.$")
    public void the_interest_should_be_increased_by_a_factor_of(float factor) throws Throwable {
        Assert.assertEquals(loan.getInterest().floatValue()*factor,extLoan.getInterest().floatValue(), 0.0001f);
    }

    @Then("^my loan should be extended for a week$")
    public void my_loan_should_be_extended_for_a_week() throws Throwable {
        Assert.assertEquals((int)(loan.getTerm() + 7),extLoan.getTerm().intValue());
    }
}
