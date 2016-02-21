package com.meishuto.common.persistence.dao.impl;

import com.meishuto.common.persistence.dao.LoanDAO;
import com.meishuto.common.persistence.domain.Loan;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class LoanDAOImpl implements LoanDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Loan saveLoan(Loan loan) {
        if (loan.getId() == null) {
            em.persist(loan);
            return loan;
        } else {
            return em.merge(loan);
        }
    }

    @Override
    public Loan findLoan(Long id) {
        return em.find(Loan.class, id);
    }
}
