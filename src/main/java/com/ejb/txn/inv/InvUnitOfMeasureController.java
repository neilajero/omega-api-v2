package com.ejb.txn.inv;

import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvUOMOneBaseUnitIsAllowedException;
import com.ejb.exception.inv.InvUOMShortNameAlreadyExistException;
import com.util.inv.InvUnitOfMeasureDetails;

import jakarta.ejb.Local;

@Local
public interface InvUnitOfMeasureController {

    java.util.ArrayList getInvUomAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    void addInvUomEntry(InvUnitOfMeasureDetails details, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, InvUOMShortNameAlreadyExistException, InvUOMOneBaseUnitIsAllowedException;

    void updateInvUomEntry(InvUnitOfMeasureDetails details, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, InvUOMShortNameAlreadyExistException, InvUOMOneBaseUnitIsAllowedException;

    void deleteInvUomEntry(java.lang.Integer UOM_CODE, java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    short getAdPrfInvQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

    void updateIiUomConversion(LocalInvUnitOfMeasure invUnitOfMeasure, java.lang.Integer AD_CMPNY);

}