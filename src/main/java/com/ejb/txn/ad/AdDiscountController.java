package com.ejb.txn.ad;

import jakarta.ejb.Local;

import java.util.ArrayList;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdDiscountDetails;
import com.util.ad.AdPaymentScheduleDetails;

@Local
public interface AdDiscountController {

    ArrayList getAdDscByPsCode(Integer paymentScheduleCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdPaymentScheduleDetails getAdPsByPsCode(Integer paymentScheduleCode, Integer companyCode);

    void addAdDscEntry(AdDiscountDetails details, Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdDscEntry(AdDiscountDetails details, Integer paymentScheduleCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdDscEntry(Integer discountCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

}