package com.ejb.txnreports.gl;

import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;

@Local
public interface GlRepCsvQuarterlyVatReturnController {

    java.util.ArrayList getCstSalesByDateRange(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getSplPurchasesByDateRange(java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_CMPNY);

    short getAdPrfApJournalLineNumber(java.lang.Integer AD_CMPNY);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}