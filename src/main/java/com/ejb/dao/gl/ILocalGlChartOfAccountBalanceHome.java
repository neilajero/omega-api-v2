package com.ejb.dao.gl;

import jakarta.ejb.*;

import com.ejb.entities.gl.LocalGlChartOfAccountBalance;

import java.util.*;
import java.lang.*;

public interface ILocalGlChartOfAccountBalanceHome {

	LocalGlChartOfAccountBalance findByPrimaryKey(Integer pk) throws FinderException;

	LocalGlChartOfAccountBalance findByAcvCodeAndCoaCode(Integer ACV_CODE, Integer COA_CODE,
                                                         Integer COAB_AD_CMPNY) throws FinderException;

	LocalGlChartOfAccountBalance findByAcvCodeAndCoaCode(Integer ACV_CODE, Integer COA_CODE,
														 Integer COAB_AD_CMPNY, String companyShortName) throws FinderException;

	Collection findByAcvCodeAndCoaSegment1(Integer ACV_CODE, String COA_SEGMENT1,
                                                     Integer COAB_AD_CMPNY) throws FinderException;

	LocalGlChartOfAccountBalance findByOtEffectiveDateAndCoaCode(java.util.Date OT_EFFCTV_DT,
                                                                 Integer COA_CODE, Integer COAB_AD_CMPNY) throws FinderException;

	LocalGlChartOfAccountBalance findByCoaCodeAndAcvCode(Integer COA_CODE, Integer ACV_CODE,
                                                         Integer COAB_AD_CMPNY) throws FinderException;

	LocalGlChartOfAccountBalance create(Integer COAB_CODE, double COAB_TTL_DBT,
                                                   double COAB_TTL_CRDT, double COAB_BEG_BLNC, double COAB_END_BLNC, Integer COAB_AD_CMPNY)
			throws CreateException;

	LocalGlChartOfAccountBalance create(double COAB_TTL_DBT, double COAB_TTL_CRDT,
                                                   double COAB_BEG_BLNC, double COAB_END_BLNC, Integer COAB_AD_CMPNY) throws CreateException;
}