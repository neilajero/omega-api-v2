package com.ejb.dao.gl;

import com.ejb.entities.gl.LocalGlAccountingCalendarValue;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;

public interface ILocalGlAccountingCalendarValueHome {

	LocalGlAccountingCalendarValue findByPrimaryKey(java.lang.Integer pk) throws FinderException;

	LocalGlAccountingCalendarValue findByAcCodeAndAcvPeriodPrefix(java.lang.Integer GL_AC_CODE,
                                                                  java.lang.String ACV_PRD_PRFX, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	LocalGlAccountingCalendarValue findByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE,
                                                                  short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findByAcCodeAndPtPeriodPerYear(java.lang.Integer GL_AC_CODE, short PT_PRD_PER_YR,
                                                        java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS,
                                                  java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	LocalGlAccountingCalendarValue findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.util.Date DT,
                                                       java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	LocalGlAccountingCalendarValue findByAcCodeAndDate(java.lang.Integer GL_AC_CODE, java.util.Date DT,
													   java.lang.Integer ACV_AD_CMPNY, String companyShortName) throws FinderException;

	java.util.Collection findAcvByAcCodeAndAcvPeriodNumberAndAcvStatus(java.lang.Integer GL_AC_CODE,
                                                                       short ACV_PRD_NMBR, char ACV_STATUS, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findOpenAndFutureEntryAcvByAcCode(java.lang.Integer GL_AC_CODE, char ACV_STATUS1,
                                                           char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findCurrentAndFutureAcvByAcCodeAndCurrentDate(java.lang.Integer GL_AC_CODE,
                                                                       java.util.Date ACV_DT, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findByAcCodeAndAcvQuarterNumber(java.lang.Integer GL_AC_CODE, short ACV_QRTR_NMBR,
                                                         java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findClosedAndPermanentlyClosedAcvByAcCode(java.lang.Integer GL_AC_CODE,
                                                                   char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findClosedAndPermanentlyClosedAcvByAcCodeAndBackwardAcvDate(
            java.lang.Integer GL_AC_CODE, java.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2,
            java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findClosedAndPermanentlyClosedAcvByAcCodeAndForwardAcvDate(java.lang.Integer GL_AC_CODE,
                                                                                    java.util.Date ACV_DT, char ACV_STATUS1, char ACV_STATUS2, java.lang.Integer ACV_AD_CMPNY)
			throws FinderException;

	java.util.Collection findSubsequentAcvByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE,
                                                                     short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findSubsequentAcvByAcCodeAndAcvPeriodNumber(java.lang.Integer GL_AC_CODE,
																	 short ACV_PRD_NMBR, java.lang.Integer ACV_AD_CMPNY,
																	 String companyShortName) throws FinderException;

	java.util.Collection findReportableAcvByAcCodeAndAcvStatus(java.lang.Integer GL_AC_CODE, char ACV_STATUS1,
                                                               char ACV_STATUS2, char ACV_STATUS3, java.lang.Integer ACV_AD_CMPNY) throws FinderException;

	java.util.Collection findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer ACV_AD_CMPNY)
			throws FinderException;

	java.util.Collection findByAcCode(java.lang.Integer GL_AC_CODE, java.lang.Integer ACV_AD_CMPNY, String companyShortName)
			throws FinderException;

	LocalGlAccountingCalendarValue create(java.lang.Integer ACV_CODE, java.lang.String ACV_PRD_PRFX,
                                          short ACV_QRTR, short ACV_PRD_NMBR, Date ACV_DT_FRM, Date ACV_DT_TO, char ACV_STATUS, Date ACV_DT_OPND,
                                          Date ACV_DT_CLSD, Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD, Integer ACV_AD_CMPNY)
			throws CreateException;

	LocalGlAccountingCalendarValue create(java.lang.String ACV_PRD_PRFX, short ACV_QRTR,
                                          short ACV_PRD_NMBR, Date ACV_DT_FRM, Date ACV_DT_TO, char ACV_STATUS, Date ACV_DT_OPND, Date ACV_DT_CLSD,
                                          Date ACV_DT_PRMNNTLY_CLSD, Date ACV_DT_FTR_ENTRD, Integer ACV_AD_CMPNY) throws CreateException;
}