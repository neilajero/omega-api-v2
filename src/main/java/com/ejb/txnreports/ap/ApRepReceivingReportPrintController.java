package com.ejb.txnreports.ap;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.inv.InvItemDetails;

import jakarta.ejb.Local;

@Local
public interface ApRepReceivingReportPrintController {

    java.util.ArrayList executeApRepReceivingReportPrintSub(java.util.ArrayList poCodeList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList executeApRepReceivingReportPrint(java.util.ArrayList poCodeList, java.lang.String USR_NM, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY, java.lang.String reportType) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    InvItemDetails getInvItemByIiName(java.lang.String II_NM, java.lang.Integer AD_CMPNY);

    java.lang.String getAdUsrDepartment(java.lang.String USR_NM, java.lang.Integer AD_CMPNY);

}