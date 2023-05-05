package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdStoredProcedure;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdStoredProcedureHome {

	public static final String JNDI_NAME = "LocalAdStoredProcedureHome!com.ejb.ad.LocalAdStoredProcedureHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdStoredProcedureHome() {
	}

	// FINDER METHODS

	public LocalAdStoredProcedure findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdStoredProcedure entity = (LocalAdStoredProcedure) em
					.find(new LocalAdStoredProcedure(), pk);
			if (entity == null) {
				throw new FinderException();
			}
			return entity;
		} catch (FinderException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdStoredProcedure findBySpAdCompany(java.lang.Integer SP_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sp) FROM AdStoredProcedure sp WHERE sp.spAdCompany = ?1");
			query.setParameter(1, SP_AD_CMPNY);
            return (LocalAdStoredProcedure) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdStoredProcedureHome.findBySpAdCompany(java.lang.Integer SP_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdStoredProcedureHome.findBySpAdCompany(java.lang.Integer SP_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdStoredProcedure create(Integer SP_CODE, byte SP_ENBL_GL_RPT, String SP_NM_GL_RPT,
                                         byte SP_ENBL_TB_RPT, String SP_NM_TB_RPT, byte SP_ENBL_IS_RPT, String SP_NM_IS_RPT, byte SP_ENBL_BS_RPT,
                                         String SP_NM_BS_RPT, byte SP_ENBL_SOA_RPT, String SP_NM_SOA_RPT,

                                         Integer SP_AD_CMPNY) throws CreateException {
		try {

			LocalAdStoredProcedure entity = new LocalAdStoredProcedure();

			Debug.print("AdStoredProcedure create");
			entity.setSpCode(SP_CODE);
			entity.setSpEnableGlGeneralLedgerReport(SP_ENBL_GL_RPT);
			entity.setSpNameGlGeneralLedgerReport(SP_NM_GL_RPT);
			entity.setSpEnableGlTrialBalanceReport(SP_ENBL_TB_RPT);
			entity.setSpNameGlTrialBalanceReport(SP_NM_TB_RPT);
			entity.setSpEnableGlIncomeStatementReport(SP_ENBL_IS_RPT);
			entity.setSpNameGlIncomeStatementReport(SP_NM_IS_RPT);
			entity.setSpEnableGlBalanceSheetReport(SP_ENBL_BS_RPT);
			entity.setSpNameGlBalanceSheetReport(SP_NM_BS_RPT);
			entity.setSpEnableArStatementOfAccountReport(SP_ENBL_SOA_RPT);
			entity.setSpNameArStatementOfAccountReport(SP_NM_SOA_RPT);
			entity.setSpAdCompany(SP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdStoredProcedure create(byte SP_ENBL_GL_RPT, String SP_NM_GL_RPT, byte SP_ENBL_TB_RPT,
                                         String SP_NM_TB_RPT, byte SP_ENBL_IS_RPT, String SP_NM_IS_RPT, byte SP_ENBL_BS_RPT, String SP_NM_BS_RPT,
                                         byte SP_ENBL_SOA_RPT, String SP_NM_SOA_RPT, Integer SP_AD_CMPNY) throws CreateException {
		try {

			LocalAdStoredProcedure entity = new LocalAdStoredProcedure();

			Debug.print("AdStoredProcedure create");
			entity.setSpEnableGlGeneralLedgerReport(SP_ENBL_GL_RPT);
			entity.setSpNameGlGeneralLedgerReport(SP_NM_GL_RPT);
			entity.setSpEnableGlTrialBalanceReport(SP_ENBL_TB_RPT);
			entity.setSpNameGlTrialBalanceReport(SP_NM_TB_RPT);
			entity.setSpEnableGlIncomeStatementReport(SP_ENBL_IS_RPT);
			entity.setSpNameGlIncomeStatementReport(SP_NM_IS_RPT);
			entity.setSpEnableGlBalanceSheetReport(SP_ENBL_BS_RPT);
			entity.setSpNameGlBalanceSheetReport(SP_NM_BS_RPT);
			entity.setSpEnableArStatementOfAccountReport(SP_ENBL_SOA_RPT);
			entity.setSpNameArStatementOfAccountReport(SP_NM_SOA_RPT);
			entity.setSpAdCompany(SP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}