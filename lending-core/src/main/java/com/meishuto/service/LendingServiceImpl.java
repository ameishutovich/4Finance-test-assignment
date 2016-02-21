package com.meishuto.service;

import com.google.common.base.Preconditions;
import com.meishuto.common.persistence.PersistenceFacade;
import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.persistence.domain.LoanStatus;
import com.meishuto.common.persistence.domain.User;
import com.meishuto.error.MicroLendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.meishuto.common.error.ErrorCode.LOAN_NOT_FOUND;
import static com.meishuto.common.error.ErrorCode.MAX_APPLICATIONS_REACHED;
import static com.meishuto.common.error.ErrorCode.NIGHT_MAX_LOAN;
import static com.meishuto.common.persistence.domain.LoanStatus.CLOSED;
import static com.meishuto.common.persistence.domain.LoanStatus.DENIED;
import static com.meishuto.common.persistence.domain.LoanStatus.EXTENDED;
import static com.meishuto.common.persistence.domain.LoanStatus.OPEN;

@Service
public class LendingServiceImpl implements LendingService {
    public static final BigDecimal ZERO = new BigDecimal(0);

    @Autowired
    private PersistenceFacade persistenceFacade;

    @Override
    public Loan applyForLoan(BigDecimal amount, Integer term, String ipAddress) throws MicroLendingException {
        Preconditions.checkArgument(amount != null && amount.compareTo(ZERO) > 0, "Invalid amount of loan!");
        Preconditions.checkArgument(term != null && term > 0, "Invalid term of loan!");
        boolean isSuccessful = false;
        Loan loan;
        try {
            checkRisks(amount, ipAddress);
            isSuccessful = true;
        } finally {
            loan = saveApplicationAnyway(amount, term, ipAddress, isSuccessful);
        }
        return loan;
    }

    private Loan saveApplicationAnyway(BigDecimal amount, Integer term, String ipAddress, boolean isSuccessful) {
        Application application = createApplicationForUser(ipAddress, amount, term, isSuccessful);
        return persistenceFacade.saveOrUpdateApplicationForLoan(application);
    }

    @Override
    public Loan extendLoan(String ipAddress, Long loanId) throws MicroLendingException {
        Loan loan = persistenceFacade.findLoan(loanId);
        verifyLoanCanBeExtended(loan);
        Loan extendedLoan = extendLoanOfUser(loan);
        loan.setStatus(CLOSED);
        persistenceFacade.saveOrUpdateLoan(loan);
        Application application = createApplicationForUser(ipAddress, extendedLoan, true);
        return persistenceFacade.saveOrUpdateApplicationForLoan(application);
    }

    private void verifyLoanCanBeExtended(Loan loan) throws MicroLendingException {
        if (loan == null || loan.getStatus() == DENIED || loan.getStatus() == CLOSED) {
            throw new MicroLendingException(LOAN_NOT_FOUND);
        }
    }

    private Loan extendLoanOfUser(Loan loan) {
        Integer oldTerm = loan.getTerm();
        Float oldInterest = loan.getInterest();
        return new Loan(loan.getAmount(), oldTerm + WEEK, oldInterest * INTEREST_FACTOR, EXTENDED);
    }

    @Override
    public List<Application> getHistoryOfLoans(String ipAddress) {
        return persistenceFacade.getApplicationsOfUser(ipAddress);
    }

    private void checkRisks(BigDecimal amount, String ipAddress) throws MicroLendingException {
        if (!isValidAmount(amount)) {
            throw new MicroLendingException(NIGHT_MAX_LOAN);
        }
        if (!isValidNumberOfApplications(ipAddress)) {
            throw new MicroLendingException(MAX_APPLICATIONS_REACHED);
        }
    }

    private boolean isValidNumberOfApplications(String ipAddress) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date from = calendar.getTime();
        Integer[] statuses = new Integer[]{OPEN.getId(), DENIED.getId(), EXTENDED.getId()};
        Long num = persistenceFacade.getNumberOfApplicationsFromPeriodOfUser(from, calendar.getTime(), ipAddress, statuses);
        return num.longValue() < MAX_NUM_OF_APPLICATIONS_PER_DAY;
    }

    private boolean isValidAmount(BigDecimal amount) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        boolean isNight = hour >= LOW_BOUND && hour <= HIGH_BOUND;
        boolean isMoreMaxAmount = amount.compareTo(MAX_AMOUNT) > 0;
        boolean isMaxAmount = amount.compareTo(MAX_AMOUNT) == 0;
        return !(isMoreMaxAmount || (isNight && isMaxAmount));
    }

    private Application createApplicationForUser(String ipAddress, BigDecimal amount, Integer term, boolean successful) {
        LoanStatus status = successful ? OPEN : DENIED;
        Loan loan = new Loan(amount, term, DEFAULT_INTEREST, status);
        return createApplicationForUser(ipAddress, loan, successful);
    }

    private Application createApplicationForUser(String ipAddress, Loan loan, boolean successful) {
        return new Application(new Date(), successful, new User(ipAddress), loan);
    }
}
