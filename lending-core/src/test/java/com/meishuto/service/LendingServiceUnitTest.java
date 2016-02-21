package com.meishuto.service;

import com.meishuto.common.persistence.PersistenceFacade;
import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.persistence.domain.LoanStatus;
import com.meishuto.error.MicroLendingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LendingServiceUnitTest {

    public static final String IP_ADDRESS = "255.255.255.255";
    public static final String IP_ADDRESS2 = "255.255.0.0";
    public static final String IP_ADDRESS3 = "255.0.0.0";
    public static final String IP_ADDRESS4 = "0.0.0.0";

    @Mock
    private PersistenceFacade persistenceFacade;

    @InjectMocks
    private LendingServiceImpl lendingService;

    @Test(expected = IllegalArgumentException.class)
    public void testAmountTermInvalid() throws MicroLendingException {
        lendingService.applyForLoan(new BigDecimal(-1), -100, IP_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmountInvalid() throws MicroLendingException {
        lendingService.applyForLoan(new BigDecimal(-283.23), 10, IP_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTremInvalid() throws MicroLendingException {
        lendingService.applyForLoan(new BigDecimal(72.23), 0, IP_ADDRESS);
    }

    @Test
    public void testApplyForLoan() throws MicroLendingException {
        when(persistenceFacade.saveOrUpdateApplicationForLoan(any(Application.class))).thenAnswer(getAnswer());

        Loan loan = lendingService.applyForLoan(new BigDecimal(374.213), 14, IP_ADDRESS);
        Assert.assertEquals(LoanStatus.OPEN, loan.getStatus());
    }

    @Test(expected = MicroLendingException.class)
    public void testLoanWithMaxAmount() throws MicroLendingException {
        lendingService.applyForLoan(new BigDecimal(2418728473.283), 14, IP_ADDRESS);
    }

    @Test(expected = MicroLendingException.class)
    public void testLoanWithMaxApplications() throws MicroLendingException {
        when(persistenceFacade.getNumberOfApplicationsFromPeriodOfUser(any(Date.class), any(Date.class), anyString())).thenReturn(Long.valueOf(3));
        lendingService.applyForLoan(new BigDecimal(2418728473.283), 14, IP_ADDRESS);
    }

    @Test
    public void testExtendLoan() throws MicroLendingException {
        when(persistenceFacade.saveOrUpdateApplicationForLoan(any(Application.class))).thenAnswer(getAnswer());
        Long loanId = Long.valueOf(1);
        Loan loanOld = new Loan(new BigDecimal(134.34), 14, 0.01f, LoanStatus.OPEN);
        when(persistenceFacade.findLoan(loanId)).thenReturn(loanOld);

        Loan loan = lendingService.extendLoan(IP_ADDRESS, loanId);
        Assert.assertEquals(LoanStatus.EXTENDED, loan.getStatus());
        Assert.assertEquals(loanOld.getTerm() + 7, loan.getTerm().intValue());
        Assert.assertEquals(loanOld.getAmount(), loan.getAmount());
        Assert.assertEquals(loanOld.getInterest()*1.5, loan.getInterest(), 0.0001f);
    }


    private Answer<Loan> getAnswer() {
        return new Answer<Loan>() {
            @Override
            public Loan answer(InvocationOnMock invocation) throws Throwable {
                Application application = (Application) invocation.getArguments()[0];
                return application.getLoan();
            }
        };
    }
}
