package com.ejb.txn.ad;

import jakarta.ejb.Local;

import com.ejb.exception.global.*;
import com.util.ad.AdPaymentTermDetails;

import java.util.ArrayList;

@Local
public interface AdPaymentTermController {

    ArrayList getAdPytAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdPytEntry(AdPaymentTermDetails details, String coaAccountNumber, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void updateAdPytEntry(AdPaymentTermDetails details, String coaAccountNumber, Integer companyCode) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException;

    void deleteAdPytEntry(Integer paymentTermCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}