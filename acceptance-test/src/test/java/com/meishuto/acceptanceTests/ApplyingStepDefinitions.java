package com.meishuto.acceptanceTests;

import com.meishuto.common.dto.ResponseMessage;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.meishuto.acceptanceTests.MicroLendingApplicationTests.SERVER_URL;


public class ApplyingStepDefinitions {
    private ResponseEntity<ResponseMessage> message;

    private RestTemplate restTemplate = MicroLendingApplicationTests.restTemplate();

    @Given("^I've less than 3 applications during a day$")
    public void i_ve_less_than_3_applications_during_a_day() throws Throwable {
        int size = restTemplate.getForEntity(SERVER_URL + "/history", List.class).getBody().size();
        Assert.assertTrue(size < 3);
    }

    @Given("^it isn't between midnight and 7 am$")
    public void it_isn_t_between_midnight_and_am() throws Throwable {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Assert.assertTrue(!(hour >= 0 && hour <= 7));
    }

    @When("^I pass valid '(\\d+\\.?\\d+)' and (\\d+) to api$")
    public void i_pass_valid_and_to_api(BigDecimal amount, int term) throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoanCreation creation = new LoanCreation(amount, term);
        HttpEntity<LoanCreation> entity = new HttpEntity<LoanCreation>(creation,headers);
        message = MicroLendingApplicationTests.restTemplate().postForEntity(SERVER_URL + "/loan", entity, ResponseMessage.class);
    }

    @Then("^I should see a \"(.*?)\"\\.$")
    public void i_should_see_a_successful_message(String message) throws Throwable {
        String messageResp = this.message.getBody().getMessage();
        Assert.assertEquals(message, messageResp);
    }
}
