package com.ejb.dao.ap;

import com.ejb.entities.ap.LocalApSupplier;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalApSupplierHome {

	LocalApSupplier findByPrimaryKey(java.lang.Integer pk) throws FinderException;
	
	LocalApSupplier findById(java.lang.Integer pk) throws FinderException;

	java.util.Collection findEnabledSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;
	
	java.util.Collection findSplAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findEnabledSplTradeAll(java.lang.Integer SPL_AD_BRNCH, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findEnabledSplAllOrderBySplName(java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	java.util.Collection findAllEnabledSplScLedger(java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	java.util.Collection findAllSplByScCode(java.lang.Integer SC_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findAllSplInvestor(java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	LocalApSupplier findBySplSupplierCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	LocalApSupplier findBySplSupplierCode(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH,
                                          java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	LocalApSupplier findBySplNameAndAddress(java.lang.String SPL_NM, java.lang.String SPL_ADDRSS,
                                            java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	java.util.Collection findBySplCoaGlPayableAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findBySplCoaGlExpenseAccount(java.lang.Integer COA_CODE, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	LocalApSupplier findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_BRNCH,
                                  java.lang.Integer SPL_AD_CMPNY) throws FinderException;

	LocalApSupplier findBySplName(java.lang.String SPL_NM, java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findEnabledSplAllOrderBySplSupplierCode(java.lang.Integer SPL_AD_CMPNY)
			throws FinderException;

	java.util.Collection findSplBySplNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
                                                   char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException;

	java.util.Collection getSplByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
                                          java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException;
	
	void updateSupplier(LocalApSupplier apSupplier);
	
	LocalApSupplier buildSupplier() throws CreateException;
	
	ILocalApSupplierHome SplSupplierCode(String SPL_SPPLR_CODE);
	
	ILocalApSupplierHome SplName(String SPL_NM);

	ILocalApSupplierHome SplEnable(byte SPL_ENBL);

	ILocalApSupplierHome SplAdCompany(Integer SPL_AD_CMPNY);

	LocalApSupplier create(Integer SPL_CODE, String SPL_SPPLR_CODE, String SPL_ACCNT_NMBR,
                           String SPL_NM, String SPL_ADDRSS, String SPL_CTY, String SPL_STT_PRVNC, String SPL_PSTL_CD,
                           String SPL_CNTRY, String SPL_CNTCT, String SPL_PHN, String SPL_FX, String SPL_ALTRNT_PHN,
                           String SPL_ALTRNT_CNTCT, String SPL_EML, String SPL_TIN, Integer SPL_COA_GL_PYBL_ACCNT,
                           Integer SPL_COA_GL_EXPNS_ACCNT, byte SPL_ENBL, String SPL_RMRKS, Integer SPL_AD_CMPNY)
			throws CreateException;

	LocalApSupplier create(String SPL_SPPLR_CODE, String SPL_ACCNT_NMBR, String SPL_NM,
                           String SPL_ADDRSS, String SPL_CTY, String SPL_STT_PRVNC, String SPL_PSTL_CD, String SPL_CNTRY,
                           String SPL_CNTCT, String SPL_PHN, String SPL_FX, String SPL_ALTRNT_PHN, String SPL_ALTRNT_CNTCT,
                           String SPL_EML, String SPL_TIN, Integer SPL_COA_GL_PYBL_ACCNT, Integer SPL_COA_GL_EXPNS_ACCNT,
                           byte SPL_ENBL, String SPL_RMRKS, Integer SPL_AD_CMPNY) throws CreateException;

}