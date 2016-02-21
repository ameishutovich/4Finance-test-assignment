package com.meishuto.common.error;

public enum ErrorCode {
    NIGHT_MAX_LOAN("Night loan with maximal amount is deprecated","Sorry, we can't issue this loan now. Please, try later with lower amount","Application time and loan amount"),
    INVALID_LOAN_PARAMS("Invalid loan parameters","Please, check your input parameters and try later","Invalid input parameters"),
    LOAN_NOT_FOUND("Loan doesn't exist","Please, check your input parameter id","Loan id"),
    MAX_APPLICATIONS_REACHED("Daily maximum of applications has been reached","Sorry, we can't issue this loan now. You reached maximum of applications today","Applications per day");

    private String error;

    private String description;

    private String resource;

    private ErrorCode(String error, String description, String resource) {
        this.error = error;
        this.description = description;
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }
}
