package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdDocumentCategoryDetails;

@Local
public interface AdDocumentCategoryController {

    ArrayList getAdAppAll(Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdDcAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void addAdDcEntry(AdDocumentCategoryDetails details, String applicationName, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void updateAdDcEntry(AdDocumentCategoryDetails details, String applicationName, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdDcEntry(Integer documentCategoryCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}