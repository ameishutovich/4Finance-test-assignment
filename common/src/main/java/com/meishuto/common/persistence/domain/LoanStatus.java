package com.meishuto.common.persistence.domain;

public enum LoanStatus {

    OPEN(1),
    CLOSED(2),
    DENIED(3),
    EXTENDED(4);

    private Integer id;

    private LoanStatus(Integer id) {
        this.id = id;
    }

    public static LoanStatus getStatus(Integer id) {
        if(id == null){
            return null;
        }

        for (LoanStatus loanStatus : LoanStatus.values()) {
            if (id.equals(loanStatus.getId())) {
                return loanStatus;
            }
        }
        throw new IllegalArgumentException("No matching type for id " + id);
    }

    public Integer getId() {
        return id;
    }

}
