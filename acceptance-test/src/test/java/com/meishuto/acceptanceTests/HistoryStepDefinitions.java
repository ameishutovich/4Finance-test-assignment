package com.meishuto.acceptanceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meishuto.common.dto.ResponseMessage;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.dto.LoanCreation;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONCompare;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.meishuto.acceptanceTests.MicroLendingApplicationTests.SERVER_URL;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class HistoryStepDefinitions {

    private Loan loan;
    private Loan extLoan;
    private RestTemplate restTemplate = MicroLendingApplicationTests.restTemplate();
    private List<String> message;

    private Date startTime;

    @Before
    public void init(){
        startTime = new Date();
    }

    @Given("^I have 1 loan and 1 extention$")
    public void i_have_1_loan_and_1_extention() throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoanCreation creation = new LoanCreation(new BigDecimal("343.34357"), 21);
        HttpEntity<LoanCreation> entity = new HttpEntity<LoanCreation>(creation, headers);
        ResponseEntity<ResponseMessage> responseEntity = restTemplate.postForEntity(SERVER_URL + "/loan", entity, ResponseMessage.class);
        ResponseMessage body = responseEntity.getBody();
        loan = body.getLoan();
        Assert.assertNotNull(body.getMessage(), loan);
        ResponseEntity<ResponseMessage> message = restTemplate.postForEntity(SERVER_URL + "/loan/" + loan.getId() + "/extention", null, ResponseMessage.class);
        body = message.getBody();
        Assert.assertEquals("Your loan is successfully extended for a week.", body.getMessage());
        extLoan = body.getLoan();
        long extId = extLoan.getId();
        Assert.assertFalse(extId == -1);
    }

    @When("^I ask for history of loans$")
    public void i_ask_for_history_of_loans() throws Throwable {
        message = restTemplate.getForEntity(SERVER_URL+"/history",List.class).getBody();
    }

    @Then("^a list of my loans and extentions should be printed.$")
    public void a_list_of_my_loans_and_extentions_should_be_printed() throws Throwable {
        String realStr = new ObjectMapper().writeValueAsString(Arrays.asList(loan, extLoan));
        String expectedStr = new ObjectMapper().writeValueAsString(message);
        JSONCompare.compareJSON(realStr, expectedStr, STRICT).passed();
    }

}
