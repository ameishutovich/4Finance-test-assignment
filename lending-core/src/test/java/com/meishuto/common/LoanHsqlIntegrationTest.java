package com.meishuto.common;

import com.meishuto.common.config.ApplicationConfig;
import com.meishuto.common.persistence.dao.LoanDAO;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.persistence.domain.LoanStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@ContextConfiguration(classes = {ApplicationConfig.class, TestConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class LoanHsqlIntegrationTest {

    @Autowired
    private LoanDAO loanDAO;

    @Test
    public void testMarkerMethod() {
        Loan loan = new Loan();
        loan.setAmount(new BigDecimal(10000));
        loan.setTerm(5);
        loan.setStatus(LoanStatus.OPEN);
        loan.setInterest(0.015f);

        loanDAO.saveLoan(loan);
        Assert.assertNotNull("Loan MUST exist", loan);
        Assert.assertNotNull("Loan MUST have PK", loan.getId());

        Loan loan1 = loanDAO.findLoan(loan.getId());
        Assert.assertEquals("Loan Must be Found by ID", loan1.getId(),loan.getId());
    }
}
