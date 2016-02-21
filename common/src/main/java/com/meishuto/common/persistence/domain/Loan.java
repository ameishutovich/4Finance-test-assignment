package com.meishuto.common.persistence.domain;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "LOAN")
public class Loan {

    public Loan() {
    }

    public Loan(BigDecimal amount, Integer term, Float interest, LoanStatus status) {
        this.amount = amount;
        this.term = term;
        this.interest = interest;
        setStatus(status);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOAN_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TERM", nullable = false)
    private Integer term;

    @Column(name = "INTEREST", nullable = false)
    private Float interest;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Float getInterest() {
        return interest;
    }

    public void setInterest(Float interest) {
        this.interest = interest;
    }

    public LoanStatus getStatus() {
        return LoanStatus.getStatus(status);
    }

    public void setStatus(LoanStatus status) {
        if (status != null) {
            this.status = status.getId();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Loan other = (Loan) obj;
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.amount, other.amount)
                && Objects.equal(this.interest, other.interest)
                && Objects.equal(this.term, other.term);
    }
}
