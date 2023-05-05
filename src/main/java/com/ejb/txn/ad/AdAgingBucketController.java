package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.lang.*;
import java.util.*;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdAgingBucketDetails;

@Local
public interface AdAgingBucketController {

    ArrayList getAdAbAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdAbEntry(AdAgingBucketDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdAbEntry(AdAgingBucketDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdAbEntry(Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}