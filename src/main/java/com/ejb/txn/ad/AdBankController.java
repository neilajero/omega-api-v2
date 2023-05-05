package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdBankDetails;

@Local
public interface AdBankController {

    ArrayList getAdBnkAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdBnkEntry(AdBankDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdBnkEntry(AdBankDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdBankEntry(Integer bankCode, Integer companyCode) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException;
}