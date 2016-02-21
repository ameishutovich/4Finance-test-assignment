package com.meishuto.common.dto;

import java.math.BigDecimal;

public class LoanCreation {

    public LoanCreation() {
    }

    public LoanCreation(BigDecimal amount, Integer term) {
        this.amount = amount;
        this.term = term;
    }

    private BigDecimal amount;

    private Integer term;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }
}
