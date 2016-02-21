package com.meishuto.common.dto;

import com.meishuto.common.error.ErrorCode;
import com.meishuto.common.persistence.domain.Loan;

public class ResponseMessage {

    private int code;

    private Loan loan;

    private String message;

    public ResponseMessage() {
    }

    public ResponseMessage(int code, Loan loan, String message) {
        this.code = code;
        this.loan = loan;
        this.message = message;
    }

    public ResponseMessage(int code, ErrorCode errorCode) {
        this.code = code;
        this.message = errorCode.getDescription();
        this.loan = null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
