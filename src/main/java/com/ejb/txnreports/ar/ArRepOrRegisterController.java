package com.ejb.txnreports.ar;

import jakarta.ejb.Local;
import java.lang.*;
import java.sql.ResultSet;
import java.util.*;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import javax.sql.DataSource;

@Local
public interface ArRepOrRegisterController {

    void executeSpArRepOrRegister(String STORED_PROCEDURE, String SUPPLIER_CODE, java.util.Date DT_FRM, java.util.Date DT_TO, boolean INCLUDE_UNPOSTED, boolean DIRECT_CHECK_ONLY, String BRANCH_CODES, Integer AD_COMPANY) throws GlobalNoRecordFoundException;

    ArrayList getAdLvCustomerBatchAll(Integer AD_CMPNY);

    ArrayList getArCcAll(Integer AD_CMPNY);

    ArrayList getArCtAll(Integer AD_CMPNY);

    ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY);

    ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    ArrayList executeArRepOrRegisterSP(ResultSet rs, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean detailedReport, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_PR, Integer AD_CMPNY, DataSource dataSource);

    ArrayList executeArRepOrRegister(HashMap criteria, ArrayList branchList, String ORDER_BY, String GROUP_BY, boolean detailedReport, boolean SHOW_ENTRIES, boolean SUMMARIZE, boolean INCLUDE_PR, Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(Integer AD_CMPNY);

    ArrayList getArRbAll(Integer AD_BRNCH, Integer AD_CMPNY);

}