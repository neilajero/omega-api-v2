package com.ejb.dao.ad;

import com.ejb.entities.ad.LocalAdCompany;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalAdCompanyHome {

    // FINDER METHODS
    LocalAdCompany findByPrimaryKey(Integer pk) throws FinderException;

    LocalAdCompany findByPrimaryKey(Integer pk, String companyShortName) throws FinderException;

    LocalAdCompany findByCmpName(String CMP_NM) throws FinderException;

    LocalAdCompany findByCmpShortName(String CMP_SHRT_NM) throws FinderException;

    // CREATE METHODS
    LocalAdCompany buildCompany() throws CreateException;

    ILocalAdCompanyHome CompanyName(String CMP_NM);

    ILocalAdCompanyHome CompanyShortName(String CMP_SHRT_NM);

    ILocalAdCompanyHome WelcomeNote(String CMP_WLCM_NT);

    ILocalAdCompanyHome RetainedEarnings(String CMP_RTND_EARNNGS);

    LocalAdCompany create(Integer CMP_CODE, String CMP_NM, String CMP_SHRT_NM, String CMP_TX_PYR_NM,
                          String CMP_CNTCT, String CMP_WLCM_NT, String CMP_DESC, String CMP_ADDRSS,
                          String CMP_CTY, String CMP_ZP, String CMP_CNTRY, String CMP_PHN, String CMP_FX,
                          String CMP_EML, String CMP_TIN, String CMP_ML_SCTN_NO, String CMP_ML_LT_NO,
                          String CMP_ML_STRT, String CMP_ML_PO_BX, String CMP_ML_CNTRY, String CMP_ML_PRVNC,
                          String CMP_ML_PST_OFFC, String CMP_ML_CR_OFF, String CMP_TX_PRD_FRM, String CMP_TX_PRD_TO,
                          String CMP_PBLC_OFFC_NM, String CMP_DT_APPNTMNT, String CMP_RVN_OFFC,
                          String CMP_FSCL_YR_ENDNG, String CMP_INDSTRY, String CMP_RTND_EARNNGS) throws CreateException;

    LocalAdCompany create(String CMP_NM, String CMP_SHRT_NM, String CMP_TX_PYR_NM, String CMP_CNTCT,
                          String CMP_WLCM_NT, String CMP_DESC, String CMP_ADDRSS, String CMP_CTY, String CMP_ZP,
                          String CMP_CNTRY, String CMP_PHN, String CMP_FX, String CMP_EML, String CMP_TIN,
                          String CMP_ML_SCTN_NO, String CMP_ML_LT_NO, String CMP_ML_STRT, String CMP_ML_PO_BX,
                          String CMP_ML_CNTRY, String CMP_ML_PRVNC, String CMP_ML_PST_OFFC, String CMP_ML_CR_OFF,
                          String CMP_TX_PRD_FRM, String CMP_TX_PRD_TO, String CMP_PBLC_OFFC_NM, String CMP_DT_APPNTMNT,
                          String CMP_RVN_OFFC, String CMP_FSCL_YR_ENDNG, String CMP_INDSTRY, String CMP_RTND_EARNNGS) throws CreateException;

    LocalAdCompany create(String CMP_NM, String CMP_SHRT_NM, String CMP_WLCM_NT, String CMP_RTND_EARNNGS) throws CreateException;
}