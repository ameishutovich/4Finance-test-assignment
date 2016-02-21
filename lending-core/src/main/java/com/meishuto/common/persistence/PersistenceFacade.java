package com.meishuto.common.persistence;

import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;

import java.util.Date;
import java.util.List;

public interface PersistenceFacade {
    List<Application> getApplicationsOfUser(String ipAddress);

    Long getNumberOfApplicationsFromPeriodOfUser(Date from, Date to, String ipAddress, Integer... statuses);

    Loan saveOrUpdateApplicationForLoan(Application application);

    Loan saveOrUpdateLoan(Loan loan);

    Loan findLoan(Long loanId);
}
