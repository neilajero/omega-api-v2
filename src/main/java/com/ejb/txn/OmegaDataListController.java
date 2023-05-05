package com.ejb.txn;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ar.ArSalespersonDetails;
import com.util.gen.GenModSegmentDetails;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;
import java.util.List;

@Local
public interface OmegaDataListController {

    List<GlModFunctionalCurrencyDetails> getGlFcAllWithDefault(Integer companyCode);

    List<String> getGlFcAll(Integer companyCode);

    List<String> getAdPytAll(Integer companyCode);

    List<String> getArTcAll(Integer companyCode);

    List<String> getApTcAll(Integer companyCode);

    List<String> getArWtcAll(Integer companyCode);

    List<String> getApWtcAll(Integer companyCode);

    List<String> getAdUsrAll(Integer companyCode);

    List<ArSalespersonDetails> getArSlpAllByBrCode(Integer branchCode, Integer companyCode);

    List<String> getLookupValuesByType(Integer companyCode, String lookupType);

    List<String> getArSmlAll(Integer branchCode, Integer companyCode);

    List<String> getArOpenIbAll(Integer branchCode, Integer companyCode);

    List<String> getInvLocAll(Integer companyCode);

    List<String> getAdBrNameAll(Integer companyCode);

    List<String> getAdBrNameAll(Integer companyCode, String companyShortName);

    List<GlModAccountingCalendarValueDetails> getGlReportableAcvAll(Integer companyCode);

    List<GenModSegmentDetails> getGenSgAll(Integer companyCode);

    List<AdBranchDetails> getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;

    List<String> getApSplAll(Integer branchCode, Integer companyCode);

    List<String> getGenVsAll(Integer companyCode);

    List<String> getGenQlfrAll(Integer companyCode) throws GlobalNoRecordFoundException;

    List<String> getGlJsAll(Integer companyCode);

    List<String> getApOpenVbByType(int typeIndex, String departmentName, Integer branchCode, Integer companyCode, boolean isDepartment);

    List<String> getApOpenVbAll(Integer branchCode, Integer companyCode);

    List<String> getArCstAll(Integer branchCode, Integer companyCode);

    List<String> getAdBaAll(Integer branchCode, Integer companyCode);

    List<String> getArOpenRbAll(Integer branchCode, Integer companyCode);

    List<String> getArCtAll(Integer companyCode);

    List<String> getArSlpAll(Integer companyCode);

    List<String> getArCcAll(Integer companyCode);

    List<ArSalespersonDetails> getArSlpDetailsAll(Integer companyCode) throws GlobalNoRecordFoundException;

    List<String> getInvUomAll(Integer companyCode);

    List<String> getApSplAllOrderBySupplierCode(Integer companyCode);

    List<String> getArCstAllOrderByCstName(Integer companyCode);

    ArrayList getInvUomByIiName(String itemName, Integer companyCode);

}