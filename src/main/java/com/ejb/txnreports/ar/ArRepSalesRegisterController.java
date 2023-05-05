package com.ejb.txnreports.ar;

import jakarta.ejb.Local;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

@Local
public interface ArRepSalesRegisterController {

    void executeSpArRepSalesRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, java.util.Date DT_FRM, java.util.Date DT_TO, boolean INCLUDE_UNPOSTED, boolean INCLUDE_INVOICE, boolean INCLUDE_COLLECTION, boolean DIRECT_CHECK_ONLY, String BRANCH_CODES, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    ArrayList getAdLvCustomerBatchAll(java.lang.Integer AD_CMPNY);

    ArrayList getAdLvReportTypeAll(java.lang.Integer AD_CMPNY);

    ArrayList getArCcAll(java.lang.Integer AD_CMPNY);

    ArrayList getArCtAll(java.lang.Integer AD_CMPNY);

    ArrayList getAdBrResAll(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArrayList executeArRepSalesRegisterMemoLine(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean summary, boolean detailedSales, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArrayList executeArRepSalesRegister(java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean detailedSales, boolean summary, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArrayList executeArRepSalesRegisterV2(ResultSet rsInv, ResultSet rsDr, ResultSet rsIli, java.util.HashMap criteria, java.util.ArrayList branchList, java.lang.String ORDER_BY, java.lang.String GROUP_BY, boolean PRINT_SALES_RELATED_ONLY, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean detailedSales, boolean summary, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

    ArrayList getAdLvArRegionAll(java.lang.Integer AD_CMPNY);

    ArrayList getArSlpAll(java.lang.Integer AD_CMPNY);

    ArrayList getArIbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    ArrayList getArRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}