package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlInvestorAccountBalance;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlInvestorAccountBalanceHome {

	LocalGlInvestorAccountBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	LocalGlInvestorAccountBalance findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE,
                                                          java.lang.Integer IRAB_AD_CMPNY) throws FinderException;

	LocalGlInvestorAccountBalance findByAcvCodeAndSplCode(java.lang.Integer ACV_CODE, java.lang.Integer SPL_CODE,
														  java.lang.Integer IRAB_AD_CMPNY, String companyShortName) throws FinderException;

	java.util.Collection findBonusAndInterestByAcCodeAndSplCode(java.lang.Integer GL_AC_CODE,
                                                                java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY) throws FinderException;

	java.util.Collection findByAcvCode(java.lang.Integer ACV_CODE, java.lang.Integer IRAB_AD_CMPNY)
			throws FinderException;

	java.util.Collection findByAcvCodeBonusAndInterest(java.lang.Integer ACV_CODE,
                                                       java.lang.Integer IRAB_AD_CMPNY) throws FinderException;

	LocalGlInvestorAccountBalance findByOtEffectiveDateAndSplCode(java.util.Date OT_EFFCTV_DT,
                                                                  java.lang.Integer SPL_CODE, java.lang.Integer IRAB_AD_CMPNY) throws FinderException;

	LocalGlInvestorAccountBalance findBySplCodeAndAcvCode(java.lang.Integer SPL_CODE, java.lang.Integer ACV_CODE,
                                                          java.lang.Integer IRAB_AD_CMPNY) throws FinderException;

	LocalGlInvestorAccountBalance create(java.lang.Integer IRAB_CODE, double IRAB_TTL_DBT,
                                         double IRAB_TTL_CRDT, byte IRAB_BNS, byte IRAB_INT, double IRAB_TTL_BNS, double IRAB_TTL_INT,
                                         double IRAB_MNTHLY_BNS_RT, double IRAB_MNTHLY_INT_RT, double IRAB_BEG_BLNC, double IRAB_END_BLNC,
                                         Integer IRAB_AD_CMPNY) throws CreateException;

	LocalGlInvestorAccountBalance create(double IRAB_TTL_DBT, double IRAB_TTL_CRDT, byte IRAB_BNS,
                                         byte IRAB_INT, double IRAB_TTL_BNS, double IRAB_TTL_INT, double IRAB_MNTHLY_BNS_RT,
                                         double IRAB_MNTHLY_INT_RT, double IRAB_BEG_BLNC, double IRAB_END_BLNC, Integer IRAB_AD_CMPNY)
			throws CreateException;

}