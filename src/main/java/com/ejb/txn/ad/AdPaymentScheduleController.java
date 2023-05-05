package com.ejb.txn.ad;

import com.ejb.exception.ad.AdPSRelativeAmountLessPytBaseAmountException;
import com.ejb.exception.ad.AdPSRelativeAmountOverPytBaseAmountException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.ad.AdPaymentTermDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdPaymentScheduleController {

    ArrayList getAdPsByPytCode(Integer paymentTermCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdPaymentTermDetails getAdPytByPytCode(Integer paymentTermCode, Integer companyCode);

    void addAdPsEntry(ArrayList psList, Integer paymentTermCode, Integer companyCode) throws AdPSRelativeAmountOverPytBaseAmountException, AdPSRelativeAmountLessPytBaseAmountException;

    void deleteAdPsEntry(Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

}