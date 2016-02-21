package com.meishuto.common.persistence;

import com.meishuto.common.persistence.dao.ApplicationDAO;
import com.meishuto.common.persistence.dao.LoanDAO;
import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PersistenseFacadeImpl implements PersistenceFacade {

    @Autowired
    private LoanDAO loanDAO;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Override
    public List<Application> getApplicationsOfUser(String ipAddress){
        return applicationDAO.getApplicationsOfUser(ipAddress);
    }

    @Override
    public Long getNumberOfApplicationsFromPeriodOfUser(Date from, Date to, String ipAddress, Integer... statuses){
        return applicationDAO.countApplicationsFromPeriod(from, to, ipAddress, statuses);
    }

    @Override
    public Loan saveOrUpdateApplicationForLoan(Application application){
        return applicationDAO.saveApplication(application).getLoan();
    }

    @Override
    public Loan findLoan(Long loanId) {
        return loanDAO.findLoan(loanId);
    }

    @Override
    public Loan saveOrUpdateLoan(Loan loan) {
        return loanDAO.saveLoan(loan);
    }
}
