package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.util.Debug;

@Stateless
public class LocalInvUnitOfMeasureConversionHome {

	public static final String JNDI_NAME = "LocalInvUnitOfMeasureConversionHome!com.ejb.inv.LocalInvUnitOfMeasureConversionHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvUnitOfMeasureConversionHome() {
	}

	// FINDER METHODS

	public LocalInvUnitOfMeasureConversion findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvUnitOfMeasureConversion entity = (LocalInvUnitOfMeasureConversion) em
					.find(new LocalInvUnitOfMeasureConversion(), pk);
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

	public java.util.Collection findUmcAll(java.lang.Integer UMC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc WHERE umc.umcAdCompany = ?1");
			query.setParameter(1, UMC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcAll(java.lang.Integer UMC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasureConversion findUmcByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM,
			java.lang.Integer UMC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc WHERE umc.invItem.iiName = ?1 AND umc.invUnitOfMeasure.uomName = ?2 AND umc.umcAdCompany = ?3");
			query.setParameter(1, II_NM);
			query.setParameter(2, UOM_NM);
			query.setParameter(3, UMC_AD_CMPNY);
            return (LocalInvUnitOfMeasureConversion) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasureConversion findUmcByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM,
																	 java.lang.Integer UMC_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc "
							+ "WHERE umc.invItem.iiName = ?1 AND umc.invUnitOfMeasure.uomName = ?2 AND umc.umcAdCompany = ?3",
					companyShortName);
			query.setParameter(1, II_NM);
			query.setParameter(2, UOM_NM);
			query.setParameter(3, UMC_AD_CMPNY);
			return (LocalInvUnitOfMeasureConversion) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalInvUnitOfMeasureConversion findUmcByUmcBaseUnitAndIiName(java.lang.String II_NM,
			java.lang.Integer UMC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc WHERE umc.umcBaseUnit=1 AND umc.invItem.iiName = ?1 AND umc.umcAdCompany = ?2");
			query.setParameter(1, II_NM);
			query.setParameter(2, UMC_AD_CMPNY);
            return (LocalInvUnitOfMeasureConversion) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByUmcBaseUnitAndIiName(java.lang.String II_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByUmcBaseUnitAndIiName(java.lang.String II_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUmcByUmcNewAndUpdated(java.lang.Integer UOM_AD_CMPNY, char NEW, char UPDATED,
			char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc WHERE umc.umcAdCompany = ?1 AND (umc.umcDownloadStatus = ?2 OR umc.umcDownloadStatus = ?3 OR umc.umcDownloadStatus = ?4)");
			query.setParameter(1, UOM_AD_CMPNY);
			query.setParameter(2, NEW);
			query.setParameter(3, UPDATED);
			query.setParameter(4, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByUmcNewAndUpdated(java.lang.Integer UOM_AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasureConversion findUmcByUomEnableAndByIiNameAndUomName(java.lang.String II_NM,
			java.lang.String UOM_NM, java.lang.Integer UMC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(umc) FROM InvUnitOfMeasureConversion umc WHERE umc.invUnitOfMeasure.uomEnable=1 AND umc.invItem.iiName = ?1 AND umc.invUnitOfMeasure.uomName = ?2 AND umc.umcAdCompany = ?3");
			query.setParameter(1, II_NM);
			query.setParameter(2, UOM_NM);
			query.setParameter(3, UMC_AD_CMPNY);
            return (LocalInvUnitOfMeasureConversion) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByUomEnableAndByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureConversionHome.findUmcByUomEnableAndByIiNameAndUomName(java.lang.String II_NM, java.lang.String UOM_NM, java.lang.Integer UMC_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalInvUnitOfMeasureConversion create(Integer UMC_CODE, double UMC_CNVRSN_FCTR, byte UMC_BS_UNT,
                                                  Integer UMC_AD_CMPNY) throws CreateException {
		try {

			LocalInvUnitOfMeasureConversion entity = new LocalInvUnitOfMeasureConversion();

			Debug.print("InvUnitOfMeasureConversionBean create");

			entity.setUmcCode(UMC_CODE);
			entity.setUmcConversionFactor(UMC_CNVRSN_FCTR);
			entity.setUmcBaseUnit(UMC_BS_UNT);
			entity.setUmcAdCompany(UMC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvUnitOfMeasureConversion create(double UMC_CNVRSN_FCTR, byte UMC_BS_UNT,
                                                  Integer UMC_AD_CMPNY) throws CreateException {
		try {

			LocalInvUnitOfMeasureConversion entity = new LocalInvUnitOfMeasureConversion();

			Debug.print("InvUnitOfMeasureConversionBean create");

			entity.setUmcConversionFactor(UMC_CNVRSN_FCTR);
			entity.setUmcBaseUnit(UMC_BS_UNT);
			entity.setUmcAdCompany(UMC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}