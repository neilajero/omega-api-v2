package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.*;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdAgingBucketDetails;
import com.util.ad.AdAgingBucketValueDetails;

import java.lang.*;

@Local
public interface AdAgingBucketValueController {

    ArrayList getAdAvByAbCode(Integer agingBucketCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdAgingBucketDetails getAdAbByAbCode(Integer agingBucketCode, Integer companyCode);

    void addAdAvEntry(AdAgingBucketValueDetails details, Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdAvEntry(AdAgingBucketValueDetails details, Integer agingBucketCode, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdAvEntry(Integer agingBucketValueCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException;

}