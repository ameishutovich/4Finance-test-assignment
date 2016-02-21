package com.meishuto.error;

import com.meishuto.common.error.ErrorCode;

public class MicroLendingException extends Exception{

    private ErrorCode errorCode;

    public MicroLendingException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return errorCode.getDescription();
    }
}
