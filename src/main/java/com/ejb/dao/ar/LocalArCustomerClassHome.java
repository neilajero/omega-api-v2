package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArCustomerClass;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArCustomerClassHome {

	public static final String JNDI_NAME = "LocalArCustomerClassHome!com.ejb.ar.LocalArCustomerClassHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArCustomerClassHome() {
	}

	// FINDER METHODS

	public LocalArCustomerClass findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArCustomerClass entity = (LocalArCustomerClass) em
					.find(new LocalArCustomerClass(), pk);
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

	public java.util.Collection findEnabledCcAll(java.lang.Integer CC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccEnable = 1 AND cc.ccAdCompany = ?1");
			query.setParameter(1, CC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findEnabledCcAll(java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomerClass findByCcName(java.lang.String CC_NM, java.lang.Integer CC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccName = ?1 AND cc.ccAdCompany = ?2");
			query.setParameter(1, CC_NM);
			query.setParameter(2, CC_AD_CMPNY);
            return (LocalArCustomerClass) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArCustomerClassHome.findByCcName(java.lang.String CC_NM, java.lang.Integer CC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findByCcName(java.lang.String CC_NM, java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArCustomerClass findByCcName(java.lang.String CC_NM, java.lang.Integer CC_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccName = ?1 AND cc.ccAdCompany = ?2",
					companyShortName);
			query.setParameter(1, CC_NM);
			query.setParameter(2, CC_AD_CMPNY);
			return (LocalArCustomerClass) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findCcAll(java.lang.Integer CC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccAdCompany = ?1");
			query.setParameter(1, CC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findCcAll(java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCcGlCoaChargeAccount(java.lang.Integer COA_CODE, java.lang.Integer CC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccGlCoaChargeAccount=?1 AND cc.ccAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findByCcGlCoaChargeAccount(java.lang.Integer COA_CODE, java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCcGlCoaReceivableAccount(java.lang.Integer COA_CODE,
			java.lang.Integer CC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccGlCoaReceivableAccount=?1 AND cc.ccAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findByCcGlCoaReceivableAccount(java.lang.Integer COA_CODE, java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByCcGlCoaRevenueAccount(java.lang.Integer COA_CODE, java.lang.Integer CC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cc) FROM ArCustomerClass cc WHERE cc.ccGlCoaRevenueAccount=?1 AND cc.ccAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, CC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArCustomerClassHome.findByCcGlCoaRevenueAccount(java.lang.Integer COA_CODE, java.lang.Integer CC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getCcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArCustomerClass create(Integer CC_CODE, String CC_NM, String CC_DESC,
                                       String CC_NXT_CSTMR_CODE, String CC_CSTMR_BTCH, String CC_DL_PRC, double CC_MNTHLY_INT_RT,
                                       double CC_MNMM_FNNC_CHRG, short CC_GRC_PRD_DY, short CC_DYS_IN_PRD, Integer CC_GL_COA_CHRG_ACCNT,
                                       byte CC_CHRG_BY_DUE_DT, Integer CC_GL_COA_RCVBL_ACCNT, Integer CC_GL_COA_RVNUE_ACCNT,
                                       Integer CC_GL_COA_UNERND_INT_ACCNT, Integer CC_GL_COA_ERND_INT_ACCNT, Integer CC_GL_COA_UNERND_PNT_ACCNT,
                                       Integer CC_GL_COA_ERND_PNT_ACCNT, byte CC_ENBL, byte CC_ENBL_RBT, byte CC_AUTO_CMPUTE_INT,
                                       byte CC_AUTO_CMPUTE_PNT, double CC_CRDT_LMT, Integer CC_AD_CMPNY) throws CreateException {
		try {

			LocalArCustomerClass entity = new LocalArCustomerClass();

			Debug.print("ArCustomerClassBean create");
			entity.setCcCode(CC_CODE);
			entity.setCcName(CC_NM);
			entity.setCcDescription(CC_DESC);
			entity.setCcNextCustomerCode(CC_NXT_CSTMR_CODE);
			entity.setCcCustomerBatch(CC_CSTMR_BTCH);
			entity.setCcDealPrice(CC_DL_PRC);
			entity.setCcMonthlyInterestRate(CC_MNTHLY_INT_RT);
			entity.setCcMinimumFinanceCharge(CC_MNMM_FNNC_CHRG);
			entity.setCcGracePeriodDay(CC_GRC_PRD_DY);
			entity.setCcDaysInPeriod(CC_DYS_IN_PRD);
			entity.setCcGlCoaChargeAccount(CC_GL_COA_CHRG_ACCNT);
			entity.setCcChargeByDueDate(CC_CHRG_BY_DUE_DT);
			entity.setCcGlCoaReceivableAccount(CC_GL_COA_RCVBL_ACCNT);
			entity.setCcGlCoaRevenueAccount(CC_GL_COA_RVNUE_ACCNT);
			entity.setCcGlCoaUnEarnedInterestAccount(CC_GL_COA_UNERND_INT_ACCNT);
			entity.setCcGlCoaEarnedInterestAccount(CC_GL_COA_ERND_INT_ACCNT);
			entity.setCcGlCoaUnEarnedPenaltyAccount(CC_GL_COA_UNERND_PNT_ACCNT);
			entity.setCcGlCoaEarnedPenaltyAccount(CC_GL_COA_ERND_PNT_ACCNT);
			entity.setCcEnable(CC_ENBL);
			entity.setCcEnableRebate(CC_ENBL_RBT);
			entity.setCcAutoComputeInterest(CC_AUTO_CMPUTE_INT);
			entity.setCcAutoComputePenalty(CC_AUTO_CMPUTE_PNT);
			entity.setCcCreditLimit(CC_CRDT_LMT);
			entity.setCcAdCompany(CC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArCustomerClass create(String CC_NM, String CC_DESC, String CC_NXT_CSTMR_CODE,
                                       String CC_CSTMR_BTCH, String CC_DL_PRC, double CC_MNTHLY_INT_RT, double CC_MNMM_FNNC_CHRG,
                                       short CC_GRC_PRD_DY, short CC_DYS_IN_PRD, Integer CC_GL_COA_CHRG_ACCNT, byte CC_CHRG_BY_DUE_DT,
                                       Integer CC_GL_COA_RCVBL_ACCNT, Integer CC_GL_COA_RVNUE_ACCNT, Integer CC_GL_COA_UNERND_INT_ACCNT,
                                       Integer CC_GL_COA_ERND_INT_ACCNT, Integer CC_GL_COA_UNERND_PNT_ACCNT, Integer CC_GL_COA_ERND_PNT_ACCNT,
                                       byte CC_ENBL, byte CC_ENBL_RBT, byte CC_AUTO_CMPUTE_INT, byte CC_AUTO_CMPUTE_PNT, double CC_CRDT_LMT,
                                       Integer CC_AD_CMPNY) throws CreateException {
		try {

			LocalArCustomerClass entity = new LocalArCustomerClass();

			Debug.print("ArCustomerClassBean create");
			entity.setCcName(CC_NM);
			entity.setCcDescription(CC_DESC);
			entity.setCcNextCustomerCode(CC_NXT_CSTMR_CODE);
			entity.setCcCustomerBatch(CC_CSTMR_BTCH);
			entity.setCcDealPrice(CC_DL_PRC);
			entity.setCcMonthlyInterestRate(CC_MNTHLY_INT_RT);
			entity.setCcMinimumFinanceCharge(CC_MNMM_FNNC_CHRG);
			entity.setCcGracePeriodDay(CC_GRC_PRD_DY);
			entity.setCcDaysInPeriod(CC_DYS_IN_PRD);
			entity.setCcGlCoaChargeAccount(CC_GL_COA_CHRG_ACCNT);
			entity.setCcChargeByDueDate(CC_CHRG_BY_DUE_DT);
			entity.setCcGlCoaReceivableAccount(CC_GL_COA_RCVBL_ACCNT);
			entity.setCcGlCoaRevenueAccount(CC_GL_COA_RVNUE_ACCNT);
			entity.setCcGlCoaUnEarnedInterestAccount(CC_GL_COA_UNERND_INT_ACCNT);
			entity.setCcGlCoaEarnedInterestAccount(CC_GL_COA_ERND_INT_ACCNT);
			entity.setCcGlCoaUnEarnedPenaltyAccount(CC_GL_COA_UNERND_PNT_ACCNT);
			entity.setCcGlCoaEarnedPenaltyAccount(CC_GL_COA_ERND_PNT_ACCNT);
			entity.setCcEnable(CC_ENBL);
			entity.setCcAutoComputeInterest(CC_AUTO_CMPUTE_INT);
			entity.setCcAutoComputePenalty(CC_AUTO_CMPUTE_PNT);
			entity.setCcCreditLimit(CC_CRDT_LMT);
			entity.setCcAdCompany(CC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}