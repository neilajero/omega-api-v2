package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.*;

import com.ejb.exception.ad.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdDocumentSequenceAssignmentDetails;
import com.util.ad.AdResponsibilityDetails;

@Local
public interface AdDocumentSequenceAssignmentController {

    ArrayList getAdDcAll(Integer companyCode) throws AdDCNoDocumentCategoryFoundException;

    ArrayList getAdDsAll(Integer companyCode) throws AdDSNoDocumentSequenceFoundException;

    ArrayList getAdDsaAll(Integer companyCode) throws AdDSANoDocumentSequenceAssignmentFoundException;

    void updateAdDSASequenceByCode(Integer documentSequenceAssignmentCode, String nextSequenceNumber, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException;

    void addAdDsaEntry(AdDocumentSequenceAssignmentDetails details, String documentName, String documentSequenceName, ArrayList branchList, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException;

    void updateAdDsaEntry(AdDocumentSequenceAssignmentDetails details, String documentName, String documentSequenceName, String responsibilityName, ArrayList branchList, Integer companyCode) throws AdDCNoDocumentCategoryFoundException, AdDSNoDocumentSequenceFoundException, AdDCDocumentCategoryAlreadyExistException, AdDSADocumentSequenceAssignmentAlreadyDeletedException;

    void deleteAdDsaEntry(Integer documentSequenceAssignmentCode, Integer companyCode) throws AdDSADocumentSequenceAssignmentAlreadyAssignedException, AdDSADocumentSequenceAssignmentAlreadyDeletedException;

    ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    ArrayList getAdBrDsaAll(Integer documentSequenceAssignmentCode, Integer companyCode) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(Integer responsibilityCode) throws GlobalNoRecordFoundException;

}