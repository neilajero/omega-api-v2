package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApprovalDocument;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdApprovalDocumentHome {

	public static final String JNDI_NAME = "LocalAdApprovalDocumentHome!com.ejb.ad.LocalAdApprovalDocumentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdApprovalDocumentHome() {
	}

	public LocalAdApprovalDocument findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdApprovalDocument entity = (LocalAdApprovalDocument) em
					.find(new LocalAdApprovalDocument(), pk);
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

	public LocalAdApprovalDocument findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

		try {

			LocalAdApprovalDocument entity = (LocalAdApprovalDocument) em
					.findPerCompany(new LocalAdApprovalDocument(), pk, companyShortName);
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

	public LocalAdApprovalDocument findByAdcType(java.lang.String ADC_TYP, java.lang.Integer ADC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(adc) FROM AdApprovalDocument adc WHERE adc.adcType = ?1 AND adc.adcAdCompany = ?2");
			query.setParameter(1, ADC_TYP);
			query.setParameter(2, ADC_AD_CMPNY);
            return (LocalAdApprovalDocument) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalDocumentHome.findByAdcType(java.lang.String ADC_TYP, java.lang.Integer ADC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalDocumentHome.findByAdcType(java.lang.String ADC_TYP, java.lang.Integer ADC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdApprovalDocument findByAdcType(java.lang.String ADC_TYP, java.lang.Integer ADC_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(adc) FROM AdApprovalDocument adc WHERE adc.adcType = ?1 AND adc.adcAdCompany = ?2",
					companyShortName);
			query.setParameter(1, ADC_TYP);
			query.setParameter(2, ADC_AD_CMPNY);
			return (LocalAdApprovalDocument) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findAdcAll(java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(adc) FROM AdApprovalDocument adc WHERE adc.adcAdCompany = ?1");
			query.setParameter(1, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdApprovalDocumentHome.findAdcAll(java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getAdcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalAdApprovalDocument create(Integer ADC_CODE, String ADC_TYP, String ADC_PRNT_OPTN,
                                          byte ADC_ALLW_DPLCT, byte ADC_TRCK_DPLCT, byte ADC_ENBL_CRDT_LMT_CHCKNG, Integer ADC_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdApprovalDocument entity = new LocalAdApprovalDocument();

			Debug.print("AdApprovalDocumentBean create");

			entity.setAdcCode(ADC_CODE);
			entity.setAdcType(ADC_TYP);
			entity.setAdcPrintOption(ADC_PRNT_OPTN);
			entity.setAdcAllowDuplicate(ADC_ALLW_DPLCT);
			entity.setAdcTrackDuplicate(ADC_TRCK_DPLCT);
			entity.setAdcEnableCreditLimitChecking(ADC_ENBL_CRDT_LMT_CHCKNG);
			entity.setAdcAdCompany(ADC_AD_CMPNY);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdApprovalDocument create(String ADC_TYP, String ADC_PRNT_OPTN, byte ADC_ALLW_DPLCT,
                                          byte ADC_TRCK_DPLCT, byte ADC_ENBL_CRDT_LMT_CHCKNG, Integer ADC_AD_CMPNY) throws CreateException {
		try {

			LocalAdApprovalDocument entity = new LocalAdApprovalDocument();

			Debug.print("AdApprovalDocumentBean create");

			entity.setAdcType(ADC_TYP);
			entity.setAdcPrintOption(ADC_PRNT_OPTN);
			entity.setAdcAllowDuplicate(ADC_ALLW_DPLCT);
			entity.setAdcTrackDuplicate(ADC_TRCK_DPLCT);
			entity.setAdcEnableCreditLimitChecking(ADC_ENBL_CRDT_LMT_CHCKNG);
			entity.setAdcAdCompany(ADC_AD_CMPNY);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}