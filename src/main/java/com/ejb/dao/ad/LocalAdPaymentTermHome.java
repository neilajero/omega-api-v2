package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdPaymentTermHome {

	public static final String JNDI_NAME = "LocalAdPaymentTermHome!com.ejb.ad.LocalAdPaymentTermHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdPaymentTermHome() {
	}

	// FINDER METHODS

	public LocalAdPaymentTerm findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdPaymentTerm entity = (LocalAdPaymentTerm) em
					.find(new LocalAdPaymentTerm(), pk);
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

	public java.util.Collection findEnabledPytAll(java.lang.Integer PYT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pyt) FROM AdPaymentTerm pyt WHERE pyt.pytEnable = 1 AND pyt.pytAdCompany = ?1");
			query.setParameter(1, PYT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdPaymentTermHome.findEnabledPytAll(java.lang.Integer PYT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPytAll(java.lang.Integer PYT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(pyt) FROM AdPaymentTerm pyt WHERE pyt.pytAdCompany = ?1");
			query.setParameter(1, PYT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdPaymentTermHome.findPytAll(java.lang.Integer PYT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdPaymentTerm findByPytName(java.lang.String PYT_NM, java.lang.Integer PYT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pyt) FROM AdPaymentTerm pyt WHERE pyt.pytName = ?1 AND pyt.pytAdCompany = ?2");
			query.setParameter(1, PYT_NM);
			query.setParameter(2, PYT_AD_CMPNY);
            return (LocalAdPaymentTerm) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdPaymentTermHome.findByPytName(java.lang.String PYT_NM, java.lang.Integer PYT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdPaymentTermHome.findByPytName(java.lang.String PYT_NM, java.lang.Integer PYT_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdPaymentTerm findByPytName(java.lang.String PYT_NM, Integer companyCode, String companyShortName) throws FinderException {
		Debug.print("LocalAdPaymentTermHome findByPytName");
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(pyt) FROM AdPaymentTerm pyt WHERE pyt.pytName = ?1 AND pyt.pytAdCompany = ?2", companyShortName);
			query.setParameter(1, PYT_NM);
			query.setParameter(2, companyCode);
			return (LocalAdPaymentTerm) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalAdPaymentTerm create(Integer PYT_CODE, String PYT_NM, String PYT_DESC, double PYT_BS_AMNT,
                                     double PYT_MNTHLY_INT_RT, byte PYT_ENBL, byte PYT_ENBL_RBT, byte PYT_ENBL_INT, byte PYT_DSCNT_ON_INVC,
                                     String PYT_DSCNT_DESC, String PYT_SCHDL_BSS, Integer PYT_AD_CMPNY) throws CreateException {
		try {

			LocalAdPaymentTerm entity = new LocalAdPaymentTerm();

			Debug.print("AdPaymentTermBean create");
			entity.setPytCode(PYT_CODE);
			entity.setPytName(PYT_NM);
			entity.setPytDescription(PYT_DESC);
			entity.setPytBaseAmount(PYT_BS_AMNT);
			entity.setPytMonthlyInterestRate(PYT_MNTHLY_INT_RT);
			entity.setPytEnable(PYT_ENBL);
			entity.setPytEnableRebate(PYT_ENBL_RBT);
			entity.setPytEnableInterest(PYT_ENBL_INT);
			entity.setPytDiscountOnInvoice(PYT_DSCNT_ON_INVC);
			entity.setPytDiscountDescription(PYT_DSCNT_DESC);
			entity.setPytScheduleBasis(PYT_SCHDL_BSS);
			entity.setPytAdCompany(PYT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdPaymentTerm create(String PYT_NM, String PYT_DESC, double PYT_BS_AMNT,
                                     double PYT_MNTHLY_INT_RT, byte PYT_ENBL, byte PYT_ENBL_RBT, byte PYT_ENBL_INT, byte PYT_DSCNT_ON_INVC,
                                     String PYT_DSCNT_DESC, String PYT_SCHDL_BSS, Integer PYT_AD_CMPNY) throws CreateException {
		try {

			LocalAdPaymentTerm entity = new LocalAdPaymentTerm();

			Debug.print("AdPaymentTermBean create");
			entity.setPytName(PYT_NM);
			entity.setPytDescription(PYT_DESC);
			entity.setPytBaseAmount(PYT_BS_AMNT);
			entity.setPytMonthlyInterestRate(PYT_MNTHLY_INT_RT);
			entity.setPytEnable(PYT_ENBL);
			entity.setPytEnableRebate(PYT_ENBL_RBT);
			entity.setPytEnableInterest(PYT_ENBL_INT);
			entity.setPytDiscountOnInvoice(PYT_DSCNT_ON_INVC);
			entity.setPytDiscountDescription(PYT_DSCNT_DESC);
			entity.setPytScheduleBasis(PYT_SCHDL_BSS);
			entity.setPytAdCompany(PYT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}