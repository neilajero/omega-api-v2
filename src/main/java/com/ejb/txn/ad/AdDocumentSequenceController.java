package com.ejb.txn.ad;

import com.ejb.exception.ad.*;
import com.util.ad.AdDocumentSequenceDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdDocumentSequenceController {

    ArrayList getAdAppAll(Integer companyCode) throws AdAPPNoApplicationFoundException;

    ArrayList getAdDsAll(Integer companyCode) throws AdDSNoDocumentSequenceFoundException;

    void addAdDsEntry(AdDocumentSequenceDetails details, String applicationName, Integer companyCode) throws AdAPPNoApplicationFoundException, AdDSDocumentSequenceAlreadyExistException;

    void updateAdDsEntry(AdDocumentSequenceDetails details, String applicationName, Integer companyCode) throws AdAPPNoApplicationFoundException, AdDSDocumentSequenceAlreadyExistException, AdDSDocumentSequenceAlreadyAssignedException, AdDSDocumentSequenceAlreadyDeletedException;

    void deleteAdDsEntry(Integer documentSequenceCode, Integer companyCode) throws AdDSDocumentSequenceAlreadyAssignedException, AdDSDocumentSequenceAlreadyDeletedException;

}