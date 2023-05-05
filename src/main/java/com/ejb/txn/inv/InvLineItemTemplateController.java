package com.ejb.txn.inv;

import com.ejb.exception.global.*;
import com.util.inv.InvLineItemTemplateDetails;

import jakarta.ejb.Local;

@Local
public interface InvLineItemTemplateController {

    void addInvLitEntry(InvLineItemTemplateDetails details, java.util.ArrayList liList, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, GlobalInvItemLocationNotFoundException;

    void updateInvLitEntry(InvLineItemTemplateDetails details, java.util.ArrayList liList, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalInvItemLocationNotFoundException;

    InvLineItemTemplateDetails getInvLitByLitCode(java.lang.Integer LIT_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    void deleteInvLitEntry(java.lang.Integer LIT_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    java.util.ArrayList getInvLitAll(java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvLiAllByLitCode(java.lang.Integer LIT_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    short getAdPrfInvJournalLineNumber(java.lang.Integer AD_CMPNY);

}