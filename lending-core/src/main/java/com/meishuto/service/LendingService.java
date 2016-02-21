package com.meishuto.service;

import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.error.MicroLendingException;

import java.math.BigDecimal;
import java.util.List;

public interface LendingService {
    int HIGH_BOUND = 7;
    int LOW_BOUND = 0;
    BigDecimal MAX_AMOUNT = new BigDecimal(5000);
    long MAX_NUM_OF_APPLICATIONS_PER_DAY = 3;
    int WEEK = 7;
    float INTEREST_FACTOR = 1.5f;
    float DEFAULT_INTEREST = 0.01f;

    Loan applyForLoan(BigDecimal amount, Integer term, String ipAddress) throws MicroLendingException;

    Loan extendLoan(String ipAddress, Long loanId) throws MicroLendingException;

    List<Application> getHistoryOfLoans(String ipAddress);
}
