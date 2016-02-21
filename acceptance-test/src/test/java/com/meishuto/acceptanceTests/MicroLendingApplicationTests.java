package com.meishuto.acceptanceTests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features/applyForLoan.feature", "classpath:features/extention.feature"})
public class MicroLendingApplicationTests {

    public static final String SERVER_PORT = "8080";
    public static String SERVER_URL = "http://localhost:" + SERVER_PORT + "/micro-lending";

    public static RestTemplate restTemplate(){
        List listOfConverters = new ArrayList<HttpMessageConverter>();
        listOfConverters.add(new MappingJackson2HttpMessageConverter());
        return new RestTemplate(listOfConverters);
    }

}