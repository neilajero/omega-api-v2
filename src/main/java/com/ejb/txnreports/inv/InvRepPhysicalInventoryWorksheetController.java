package com.ejb.txnreports.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface InvRepPhysicalInventoryWorksheetController {

    java.util.ArrayList getInvCstAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeInvRepPhysicalInventoryWorksheet(java.util.HashMap criteria, java.util.Date PI_DT, boolean INCLD_UNPSTD, boolean INCLD_ENCDD, java.lang.Integer AD_CMPNY, boolean INCLD_INTRNST) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}