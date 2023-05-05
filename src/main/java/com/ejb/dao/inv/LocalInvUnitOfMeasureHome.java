package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.util.Debug;

@Stateless
public class LocalInvUnitOfMeasureHome {

	public static final String JNDI_NAME = "LocalInvUnitOfMeasureHome!com.ejb.inv.LocalInvUnitOfMeasureHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvUnitOfMeasureHome() {
	}

	// FINDER METHODS

	public LocalInvUnitOfMeasure findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvUnitOfMeasure entity = (LocalInvUnitOfMeasure) em
					.find(new LocalInvUnitOfMeasure(), pk);
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

	public java.util.Collection findUomAll(java.lang.Integer UOM_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomAdCompany = ?1");
			query.setParameter(1, UOM_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findUomAll(java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledUomAll(java.lang.Integer UOM_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomEnable = 1 AND uom.uomAdCompany = ?1");
			query.setParameter(1, UOM_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findEnabledUomAll(java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasure findByUomName(java.lang.String UOM_NM, java.lang.Integer UOM_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomName=?1 AND uom.uomAdCompany = ?2");
			query.setParameter(1, UOM_NM);
			query.setParameter(2, UOM_AD_CMPNY);
            return (LocalInvUnitOfMeasure) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomName(java.lang.String UOM_NM, java.lang.Integer UOM_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomName(java.lang.String UOM_NM, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasure findByUomName(java.lang.String UOM_NM, java.lang.Integer UOM_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomName=?1 AND uom.uomAdCompany = ?2",
					companyShortName);
			query.setParameter(1, UOM_NM);
			query.setParameter(2, UOM_AD_CMPNY);
			return (LocalInvUnitOfMeasure) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalInvUnitOfMeasure findByUomShortName(java.lang.String UOM_SHRT_NM, java.lang.Integer UOM_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomShortName = ?1 AND uom.uomAdCompany = ?2");
			query.setParameter(1, UOM_SHRT_NM);
			query.setParameter(2, UOM_AD_CMPNY);
            return (LocalInvUnitOfMeasure) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomShortName(java.lang.String UOM_SHRT_NM, java.lang.Integer UOM_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomShortName(java.lang.String UOM_SHRT_NM, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasure findByUomAdLvClassAndBaseUnitEqualsTrue(java.lang.String UOM_AD_LV_CLSS,
			java.lang.Integer UOM_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomAdLvClass = ?1 AND uom.uomBaseUnit = 1 AND uom.uomAdCompany = ?2");
			query.setParameter(1, UOM_AD_LV_CLSS);
			query.setParameter(2, UOM_AD_CMPNY);
            return (LocalInvUnitOfMeasure) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomAdLvClassAndBaseUnitEqualsTrue(java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomAdLvClassAndBaseUnitEqualsTrue(java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUomAdLvClass(java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomAdLvClass=?1 AND uom.uomAdCompany = ?2 ORDER BY uom.uomName");
			query.setParameter(1, UOM_AD_LV_CLSS);
			query.setParameter(2, UOM_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomAdLvClass(java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUomCodeAndUomDownloadStatus(java.lang.Integer UOM_CODE, char UOM_DWNLD_STATUS,
			java.lang.Integer UOM_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomCode = ?1 AND uom.uomDownloadStatus = ?2 AND uom.uomAdCompany = ?3");
			query.setParameter(1, UOM_CODE);
			query.setParameter(2, UOM_DWNLD_STATUS);
			query.setParameter(3, UOM_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomCodeAndUomDownloadStatus(java.lang.Integer UOM_CODE, char UOM_DWNLD_STATUS, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledUomByUomNewAndUpdated(java.lang.Integer UOM_AD_CMPNY, char NEW, char UPDATED,
			char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomEnable = 1 AND uom.uomAdCompany = ?1 AND (uom.uomDownloadStatus = ?2 OR uom.uomDownloadStatus = ?3 OR uom.uomDownloadStatus = ?4)");
			query.setParameter(1, UOM_AD_CMPNY);
			query.setParameter(2, NEW);
			query.setParameter(3, UPDATED);
			query.setParameter(4, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findEnabledUomByUomNewAndUpdated(java.lang.Integer UOM_AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public LocalInvUnitOfMeasure findByUomNameAndUomAdLvClass(java.lang.String UOM_NM, java.lang.String UOM_AD_LV_CLSS,
			java.lang.Integer UOM_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(uom) FROM InvUnitOfMeasure uom WHERE uom.uomName = ?1 AND uom.uomAdLvClass = ?2 AND uom.uomAdCompany = ?3");
			query.setParameter(1, UOM_NM);
			query.setParameter(2, UOM_AD_LV_CLSS);
			query.setParameter(3, UOM_AD_CMPNY);
            return (LocalInvUnitOfMeasure) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomNameAndUomAdLvClass(java.lang.String UOM_NM, java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvUnitOfMeasureHome.findByUomNameAndUomAdLvClass(java.lang.String UOM_NM, java.lang.String UOM_AD_LV_CLSS, java.lang.Integer UOM_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalInvUnitOfMeasure create(Integer UOM_CODE, String UOM_NM, String UOM_DESC,
                                        String UOM_SHRT_NM, String UOM_AD_LV_CLSS, double UOM_CNVRSN_FCTR, byte UOM_BS_UNT, byte UOM_ENBL,
                                        char UOM_DWNLD_STATUS, Integer UOM_AD_CMPNY) throws CreateException {
		try {

			LocalInvUnitOfMeasure entity = new LocalInvUnitOfMeasure();

			Debug.print("InvUnitOfMeasureBean create");

			entity.setUomCode(UOM_CODE);
			entity.setUomName(UOM_NM);
			entity.setUomDescription(UOM_DESC);
			entity.setUomShortName(UOM_SHRT_NM);
			entity.setUomAdLvClass(UOM_AD_LV_CLSS);
			entity.setUomConversionFactor(UOM_CNVRSN_FCTR);
			entity.setUomBaseUnit(UOM_BS_UNT);
			entity.setUomEnable(UOM_ENBL);
			entity.setUomDownloadStatus(UOM_DWNLD_STATUS);
			entity.setUomAdCompany(UOM_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvUnitOfMeasure create(String UOM_NM, String UOM_DESC, String UOM_SHRT_NM,
                                        String UOM_AD_LV_CLSS, double UOM_CNVRSN_FCTR, byte UOM_BS_UNT, byte UOM_ENBL, char UOM_DWNLD_STATUS,
                                        Integer UOM_AD_CMPNY) throws CreateException {
		try {

			LocalInvUnitOfMeasure entity = new LocalInvUnitOfMeasure();

			Debug.print("InvUnitOfMeasureBean create");

			entity.setUomName(UOM_NM);
			entity.setUomDescription(UOM_DESC);
			entity.setUomShortName(UOM_SHRT_NM);
			entity.setUomAdLvClass(UOM_AD_LV_CLSS);
			entity.setUomConversionFactor(UOM_CNVRSN_FCTR);
			entity.setUomBaseUnit(UOM_BS_UNT);
			entity.setUomEnable(UOM_ENBL);
			entity.setUomDownloadStatus(UOM_DWNLD_STATUS);
			entity.setUomAdCompany(UOM_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}