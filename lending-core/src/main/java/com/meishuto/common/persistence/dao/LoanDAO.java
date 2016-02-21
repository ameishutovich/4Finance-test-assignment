package com.meishuto.common.persistence.dao;

import com.meishuto.common.persistence.domain.Loan;

public interface LoanDAO {
    Loan saveLoan(Loan loan);

    Loan findLoan(Long id);
}
