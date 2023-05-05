package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdDiscount;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdDiscountHome {

	public static final String JNDI_NAME = "LocalAdDiscountHome!com.ejb.ad.LocalAdDiscountHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdDiscountHome() {
	}

	// FINDER METHODS

	public LocalAdDiscount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdDiscount entity = (LocalAdDiscount) em.find(new LocalAdDiscount(),
					pk);
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

	public java.util.Collection findByPsLineNumberAndPytName(short PS_LN_NMBR, java.lang.String PYT_NM,
			java.lang.Integer DSC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dsc) FROM AdPaymentSchedule ps, IN(ps.adDiscounts) dsc WHERE ps.psLineNumber = ?1 AND ps.adPaymentTerm.pytName = ?2 AND dsc.dscAdCompany = ?3");
			query.setParameter(1, PS_LN_NMBR);
			query.setParameter(2, PYT_NM);
			query.setParameter(3, DSC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdDiscountHome.findByPsLineNumberAndPytName(short PS_LN_NMBR, java.lang.String PYT_NM, java.lang.Integer DSC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPsLineNumberAndPytName(short PS_LN_NMBR, java.lang.String PYT_NM,
															 java.lang.Integer DSC_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(dsc) FROM AdPaymentSchedule ps, IN(ps.adDiscounts) dsc "
							+ "WHERE ps.psLineNumber = ?1 AND ps.adPaymentTerm.pytName = ?2 AND dsc.dscAdCompany = ?3",
					companyShortName);
			query.setParameter(1, PS_LN_NMBR);
			query.setParameter(2, PYT_NM);
			query.setParameter(3, DSC_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByPsCode(java.lang.Integer PS_CODE, java.lang.Integer DSC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dsc) FROM AdPaymentSchedule ps, IN(ps.adDiscounts) dsc WHERE ps.psCode = ?1 AND dsc.dscAdCompany = ?2");
			query.setParameter(1, PS_CODE);
			query.setParameter(2, DSC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdDiscountHome.findByPsCode(java.lang.Integer PS_CODE, java.lang.Integer DSC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdDiscount findByPsCodeAndDscPaidWithinDate(java.lang.Integer PS_CODE, short DSC_PD_WTHN_DY,
			java.lang.Integer DSC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dsc) FROM AdPaymentSchedule ps, IN(ps.adDiscounts) dsc WHERE ps.psCode = ?1 AND dsc.dscPaidWithinDay = ?2 AND dsc.dscAdCompany = ?3");
			query.setParameter(1, PS_CODE);
			query.setParameter(2, DSC_PD_WTHN_DY);
			query.setParameter(3, DSC_AD_CMPNY);
            return (LocalAdDiscount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdDiscountHome.findByPsCodeAndDscPaidWithinDate(java.lang.Integer PS_CODE, short DSC_PD_WTHN_DY, java.lang.Integer DSC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdDiscountHome.findByPsCodeAndDscPaidWithinDate(java.lang.Integer PS_CODE, short DSC_PD_WTHN_DY, java.lang.Integer DSC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdDiscount findByDscPaidWithinDate(short DSC_PD_WTHN_DY, java.lang.Integer DSC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dsc) FROM AdDiscount dsc WHERE dsc.dscPaidWithinDay = ?1 AND dsc.dscAdCompany = ?2");
			query.setParameter(1, DSC_PD_WTHN_DY);
			query.setParameter(2, DSC_AD_CMPNY);
            return (LocalAdDiscount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdDiscountHome.findByDscPaidWithinDate(short DSC_PD_WTHN_DY, java.lang.Integer DSC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdDiscountHome.findByDscPaidWithinDate(short DSC_PD_WTHN_DY, java.lang.Integer DSC_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdDiscount create(Integer DSC_CODE, double DSC_DSCNT_PRCNT, short DSC_PD_WTHN_DY,
                                  Integer DSC_AD_CMPNY) throws CreateException {
		try {

			LocalAdDiscount entity = new LocalAdDiscount();

			Debug.print("AdDiscountBean create");
			entity.setDscCode(DSC_CODE);
			entity.setDscDiscountPercent(DSC_DSCNT_PRCNT);
			entity.setDscPaidWithinDay(DSC_PD_WTHN_DY);
			entity.setDscAdCompany(DSC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdDiscount create(double DSC_DSCNT_PRCNT, short DSC_PD_WTHN_DY, Integer DSC_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdDiscount entity = new LocalAdDiscount();

			Debug.print("AdDiscountBean create");
			entity.setDscDiscountPercent(DSC_DSCNT_PRCNT);
			entity.setDscPaidWithinDay(DSC_PD_WTHN_DY);
			entity.setDscAdCompany(DSC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}