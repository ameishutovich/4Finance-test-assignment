package com.meishuto.rest;

import com.google.common.base.Preconditions;
import com.meishuto.common.dto.ResponseMessage;
import com.meishuto.common.persistence.domain.Application;
import com.meishuto.common.persistence.domain.Loan;
import com.meishuto.common.error.ErrorCode;
import com.meishuto.error.MicroLendingException;
import com.meishuto.common.dto.LoanCreation;
import com.meishuto.service.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/micro-lending")
public class RestAppController {

    public static final BigDecimal ZERO = new BigDecimal(0);
    @Autowired
    private LendingService lendingService;

    @ResponseBody
    @RequestMapping(value = "/loan", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseMessage applyForLoan(@RequestBody LoanCreation creation, HttpServletRequest request) {
        Loan loan;
        try {
            BigDecimal amount = creation.getAmount();
            Integer term = creation.getTerm();
            Preconditions.checkArgument(amount != null && amount.compareTo(ZERO) > 0, "Invalid amount of loan!");
            Preconditions.checkArgument(term != null && term > 0, "Invalid term of loan!");
            loan = lendingService.applyForLoan(amount, term, request.getRemoteAddr());
        } catch (IllegalArgumentException e) {
            return new ResponseMessage(400, ErrorCode.INVALID_LOAN_PARAMS);
        } catch (MicroLendingException e) {
            return new ResponseMessage(500, null, e.getDescription());
        }
        return new ResponseMessage(200, loan, "Your loan is successfully issued.");
    }

    @ResponseBody
    @RequestMapping(value = "/loan/{loanId}/extention", method = POST)
    public ResponseMessage extendLoan(@PathVariable Long loanId, HttpServletRequest request) {
        Loan extended;
        try {
            extended = lendingService.extendLoan(request.getRemoteAddr(), loanId);
        } catch (MicroLendingException e) {
            return new ResponseMessage(500, null, e.getDescription());
        }
        return new ResponseMessage(200, extended, "Your loan is successfully extended for a week.");
    }

    @ResponseBody
    @RequestMapping(value = "/history", method = GET)
    public List<Application> getLendingHistory(HttpServletRequest request) {
        return lendingService.getHistoryOfLoans(request.getRemoteAddr());
    }
}
