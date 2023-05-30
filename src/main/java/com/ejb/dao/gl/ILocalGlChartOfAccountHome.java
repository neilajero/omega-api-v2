/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ILocalGlChartOfAccountHome.java
 * @created Sep 08 2020 3:58:48 pm
 * @author neilajero
 */
package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlChartOfAccount;

import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlChartOfAccountHome {

    // FINDER METHODS
    LocalGlChartOfAccount findByPrimaryKey(java.lang.Integer pk) throws FinderException;

    LocalGlChartOfAccount findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException;
    LocalGlChartOfAccount findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY) throws FinderException;

    LocalGlChartOfAccount findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException;
    LocalGlChartOfAccount findByCoaAccountNumberAndBranchCode(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY) throws FinderException;
    Collection findCoaAllEnabled(java.util.Date CURR_DATE, java.lang.Integer COA_AD_CMPNY) throws FinderException;
    Collection findCoaAll(java.lang.Integer COA_AD_CMPNY) throws FinderException;
    Collection findHoCoaAllByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY) throws FinderException;

    Collection findHoCoaAllByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException;
    LocalGlChartOfAccount findHoCoaByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY) throws FinderException;
    LocalGlChartOfAccount findHoCoaByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException;
    LocalGlChartOfAccount findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY) throws FinderException;
    LocalGlChartOfAccount findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException;
    LocalGlChartOfAccount findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY) throws FinderException;
    LocalGlChartOfAccount findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException;
    Collection getCoaByCriteria(java.lang.String jbossQl, java.lang.Object[] args, java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException;

    // CREATE METHODS
    LocalGlChartOfAccount buildCoa() throws CreateException;
    ILocalGlChartOfAccountHome coaAccountNumber(String COA_ACCNT_NMBR);
    ILocalGlChartOfAccountHome coaAccountDesc(String COA_ACCNT_DESC);
    ILocalGlChartOfAccountHome coaAccountType(String COA_ACCNT_TYP);
    ILocalGlChartOfAccountHome coaAdCompany(Integer COA_AD_CMPNY);
    ILocalGlChartOfAccountHome coaDateFrom(Date COA_DT_FRM);
    ILocalGlChartOfAccountHome coaDateTo(Date COA_DT_TO);
    ILocalGlChartOfAccountHome coaSegment1(String coaSegment1);
    ILocalGlChartOfAccountHome coaSegment2(String coaSegment2);
    ILocalGlChartOfAccountHome coaSegment3(String coaSegment3);
    LocalGlChartOfAccount create(
            java.lang.Integer COA_CODE,
            java.lang.String COA_ACCNT_NMBR,
            java.lang.String COA_ACCNT_DESC,
            java.lang.String COA_ACCNT_TYP,
            java.lang.String COA_TX_TYP,
            String COA_CIT_CTGRY,
            String COA_SAW_CTGRY,
            String COA_IIT_CTGRY,
            Date COA_DT_FRM,
            Date COA_DT_TO,
            String COA_SGMNT1,
            String COA_SGMNT2,
            String COA_SGMNT3,
            String COA_SGMNT4,
            String COA_SGMNT5,
            String COA_SGMNT6,
            String COA_SGMNT7,
            String COA_SGMNT8,
            String COA_SGMNT9,
            String COA_SGMNT10,
            byte COA_ENBL,
            byte COA_FR_RVLTN,
            Integer COA_AD_CMPNY)
            throws CreateException;

    LocalGlChartOfAccount create(
            java.lang.String COA_ACCNT_NMBR,
            java.lang.String COA_ACCNT_DESC,
            java.lang.String COA_ACCNT_TYP,
            java.lang.String COA_TX_TYP,
            String COA_CIT_CTGRY,
            String COA_SAW_CTGRY,
            String COA_IIT_CTGRY,
            Date COA_DT_FRM,
            Date COA_DT_TO,
            String COA_SGMNT1,
            String COA_SGMNT2,
            String COA_SGMNT3,
            String COA_SGMNT4,
            String COA_SGMNT5,
            String COA_SGMNT6,
            String COA_SGMNT7,
            String COA_SGMNT8,
            String COA_SGMNT9,
            String COA_SGMNT10,
            byte COA_ENBL,
            byte COA_FR_RVLTN,
            Integer COA_AD_CMPNY)
            throws CreateException;


}