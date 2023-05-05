package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.inv.InvPhysicalInventoryDetails;
import com.util.mod.inv.InvModPhysicalInventoryDetails;
import com.util.mod.inv.InvModPhysicalInventoryLineDetails;

import jakarta.ejb.Local;

@Local
public interface InvPhysicalInventoryEntryController {

    java.util.ArrayList getInvUomByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    double getQuantityByIiNameAndUomName(java.lang.String II_NM, java.lang.String LOC_NM,
                                         java.lang.String UOM_NM, java.util.Date PI_DT,
                                         java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    InvModPhysicalInventoryDetails getInvPiByPiCode(java.util.Date PI_DT, java.lang.Integer PI_CODE,
                                                    java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvUnsavedIlByLocNameAndIiAdLvCategoryAndPiCode(
            java.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String II_AD_LV_CTGRY, java.lang.Integer PI_CODE,
            java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvIlByPiDateInvLocNameAndInvIiAdLvCategory(
            java.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String II_AD_LV_CTGRY,
            java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    InvModPhysicalInventoryLineDetails getInvIlByPiDateInvIiName(
            java.util.Date PI_DT, java.lang.String II_NM, java.lang.String LOC_NM,
            java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws GlobalNoRecordFoundException;

    java.lang.Integer saveInvPiEntry(InvPhysicalInventoryDetails details, java.lang.String LOC_NM,
                                     java.util.ArrayList pilList, java.lang.Integer AD_BRNCH,
                                     java.lang.Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException;

    java.lang.Integer executeInvWastageGeneration(java.lang.Integer PI_CODE, java.lang.String WSTG_ACCNT,
                                                  java.lang.String USR_NM, java.lang.Integer AD_BRNCH,
                                                  java.lang.Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException, GlobalAccountNumberInvalidException;

    java.lang.Integer executeInvVarianceGeneration(java.lang.Integer PI_CODE, java.lang.String VRNC_ACCNT,
                                                   java.lang.String USR_NM, java.lang.Integer AD_BRNCH,
                                                   java.lang.Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException;

    void deleteInvPiEntry(java.lang.Integer PI_CODE, java.lang.String AD_USR);

    InvModPhysicalInventoryDetails getInvPrfDefaultVarianceAccount(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    InvModPhysicalInventoryDetails getInvPrfDefaultWastageAccount(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    short getInvGpQuantityPrecisionUnit(java.lang.Integer AD_CMPNY);

}