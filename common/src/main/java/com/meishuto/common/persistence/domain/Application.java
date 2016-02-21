package com.meishuto.common.persistence.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APPLICATION")
public class Application {

    public Application() {
    }

    public Application(Date time, boolean isSuccessful, User user, Loan loan) {
        this.time = time;
        this.isSuccessful = isSuccessful;
        this.user = user;
        this.loan = loan;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APPLICATION_ID", unique = true, nullable = false)
    private Long id;

    @JsonFormat(pattern = "HH:mm:ss, dd/MM/yyyy")
    @Column(name = "TIME", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date time;

    @Column(name = "SUCCESSFUL", nullable = false)
    @Type(type = "true_false")
    private boolean isSuccessful;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    private Loan loan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final Application other = (Application) obj;
        return  Objects.equal(this.id, other.id)
                && Objects.equal(this.time, other.time)
                && Objects.equal(this.isSuccessful, other.isSuccessful)
                && Objects.equal(this.loan, other.loan)
                && Objects.equal(this.user, other.user);
    }
}
