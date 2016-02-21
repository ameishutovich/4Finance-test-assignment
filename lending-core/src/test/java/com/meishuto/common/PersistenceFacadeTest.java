package com.meishuto.common;

import com.google.common.collect.Lists;
import com.meishuto.common.config.ApplicationConfig;
import com.meishuto.common.persistence.PersistenceFacade;
import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.persistence.domain.LoanStatus;
import com.meishuto.common.persistence.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.meishuto.common.persistence.domain.LoanStatus.DENIED;
import static com.meishuto.common.persistence.domain.LoanStatus.OPEN;

//import com.meishuto.common.persistence.domain.ApplicationType;

@Transactional
@ContextConfiguration(classes = {ApplicationConfig.class, TestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PersistenceFacadeTest {

    public static final String IP_ADDRESS = "255.255.255.255";
    public static final String IP_ADDRESS2 = "255.255.0.0";
    public static final String IP_ADDRESS3 = "255.0.0.0";
    public static final String IP_ADDRESS4 = "0.0.0.0";
    @Autowired
    private PersistenceFacade persistenceFacade;

    @Test
    public void testGettingApplicationsOfUser(){
        List<Application> applications = getRandomListApplicationsForUser(5, IP_ADDRESS);
        for(Application application: applications){
            persistenceFacade.saveOrUpdateApplicationForLoan(application);
        }

        List<Application> applicationsOfUser = persistenceFacade.getApplicationsOfUser(IP_ADDRESS);
        Assert.assertArrayEquals(applications.toArray(), applicationsOfUser.toArray());
    }

    @Test
    public void testGettingApplicationsOfUserEmptyList(){
        List<Application> applicationsOfUser = persistenceFacade.getApplicationsOfUser(IP_ADDRESS2);
        Assert.assertArrayEquals(new ArrayList[]{}, applicationsOfUser.toArray());
    }

    @Test
    public void testGettingNumberOfApplicationsFromPeriod(){
        persistenceFacade.saveOrUpdateApplicationForLoan(getRandomApplicationForUser(IP_ADDRESS3, new Date(2014, 10, 20)));
        persistenceFacade.saveOrUpdateApplicationForLoan(getRandomApplicationForUser(IP_ADDRESS3, new Date(2014, 10, 3)));
        persistenceFacade.saveOrUpdateApplicationForLoan(getRandomApplicationForUser(IP_ADDRESS3, new Date(2014, 11, 1)));
        persistenceFacade.saveOrUpdateApplicationForLoan(getRandomApplicationForUser(IP_ADDRESS3, new Date(2013, 7, 1)));
        persistenceFacade.saveOrUpdateApplicationForLoan(getRandomApplicationForUser(IP_ADDRESS3, new Date(2011, 12, 21)));
        Long number = persistenceFacade.getNumberOfApplicationsFromPeriodOfUser(new Date(2014, 10, 2), new Date(2014, 11, 1), IP_ADDRESS3);

        Assert.assertEquals(Long.valueOf(3), number);
    }

    @Test
    public void testSavingOrUpdateApplication(){
        Application application = getRandomApplicationForUser(IP_ADDRESS4);
        persistenceFacade.saveOrUpdateApplicationForLoan(application);

        List<Application> applicationsOfUser = persistenceFacade.getApplicationsOfUser(IP_ADDRESS4);
        Assert.assertEquals(1, applicationsOfUser.size());
        Assert.assertEquals(application, applicationsOfUser.get(0));
    }

    private List<Application> getRandomListApplicationsForUser(int size, String ipAddress){
        ArrayList<Application> list = Lists.newArrayList();
        while(list.size() != size){
            list.add(getRandomApplicationForUser(ipAddress));
        }
        return list;
    }

    private Application getRandomApplicationForUser(String ipAddress, Date date){
        Application applicationForUser = getRandomApplicationForUser(ipAddress);
        applicationForUser.setTime(date);
        return applicationForUser;
    }

    private Application getRandomApplicationForUser(String ipAddress){
        Random random = new Random();
        Application application = new Application();
        boolean successful = random.nextBoolean();
        application.setSuccessful(successful);
        application.setTime(new Date());
        User user = new User(ipAddress);
        application.setUser(user);
        LoanStatus status = successful ? OPEN : DENIED;
        Loan loan = new Loan(new BigDecimal(random.nextInt()), random.nextInt(), random.nextFloat(), status);
        application.setLoan(loan);
        return application;
    }
}
