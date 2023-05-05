package com.ejb.dao.inv;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvPhysicalInventory;
import com.util.Debug;

@Stateless
public class LocalInvPhysicalInventoryHome {

	public static final String JNDI_NAME = "LocalInvPhysicalInventoryHome!com.ejb.inv.LocalInvPhysicalInventoryHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvPhysicalInventoryHome() {
	}

	// FINDER METHODS

	public LocalInvPhysicalInventory findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvPhysicalInventory entity = (LocalInvPhysicalInventory) em
					.find(new LocalInvPhysicalInventory(), pk);
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

	public LocalInvPhysicalInventory findByPiDate(java.util.Date PI_DT, java.lang.Integer PI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pi) FROM InvPhysicalInventory pi WHERE pi.piDate=?1 AND pi.piAdCompany=?2");
			query.setParameter(1, PI_DT);
			query.setParameter(2, PI_AD_CMPNY);
            return (LocalInvPhysicalInventory) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDate(java.com.util.Date PI_DT, java.lang.Integer PI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDate(java.com.util.Date PI_DT, java.lang.Integer PI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvPhysicalInventory findByPiDateAndLocNameAndBrCode(java.util.Date PI_DT, java.lang.String LOC_NM,
			java.lang.Integer PI_AD_BRNCH, java.lang.Integer PI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pi) FROM InvPhysicalInventory pi WHERE pi.piDate=?1 AND pi.invLocation.locName=?2 AND pi.piAdBranch = ?3 AND pi.piAdCompany=?4");
			query.setParameter(1, PI_DT);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, PI_AD_BRNCH);
			query.setParameter(4, PI_AD_CMPNY);
            return (LocalInvPhysicalInventory) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.Integer PI_AD_BRNCH, java.lang.Integer PI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndBrCode(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.Integer PI_AD_BRNCH, java.lang.Integer PI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvPhysicalInventory findByPiDateAndLocNameAndCategoryName(java.util.Date PI_DT,
			java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pi) FROM InvPhysicalInventory pi WHERE pi.piDate=?1 AND pi.invLocation.locName=?2 AND pi.piAdLvCategory=?3 AND pi.piAdCompany = ?4");
			query.setParameter(1, PI_DT);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, CTGRY_NM);
			query.setParameter(4, PI_AD_CMPNY);
            return (LocalInvPhysicalInventory) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryName(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryName(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvPhysicalInventory findByPiDateAndLocNameAndCategoryNameAndBrCode(java.util.Date PI_DT,
			java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_BRNCH,
			java.lang.Integer PI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pi) FROM InvPhysicalInventory pi WHERE pi.piDate=?1 AND pi.invLocation.locName=?2 AND pi.piAdLvCategory=?3 AND pi.piAdBranch = ?4 AND pi.piAdCompany = ?5");
			query.setParameter(1, PI_DT);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, CTGRY_NM);
			query.setParameter(4, PI_AD_BRNCH);
			query.setParameter(5, PI_AD_CMPNY);
            return (LocalInvPhysicalInventory) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryNameAndBrCode(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_BRNCH, java.lang.Integer PI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryNameAndBrCode(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.String CTGRY_NM, java.lang.Integer PI_AD_BRNCH, java.lang.Integer PI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPiAdLvCategory(java.lang.String PI_AD_LV_CTGRY, java.lang.Integer PI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pi) FROM InvPhysicalInventory pi WHERE pi.piAdLvCategory=?1 AND pi.piAdCompany=?2");
			query.setParameter(1, PI_AD_LV_CTGRY);
			query.setParameter(2, PI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryHome.findByPiAdLvCategory(java.lang.String PI_AD_LV_CTGRY, java.lang.Integer PI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getPiByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalInvPhysicalInventory create(Integer PI_CODE, Date PI_DT, String PI_RFRNC_NMBR,
                                            String PI_DESC, String PI_CRTD_BY, Date PI_DT_CRTD, String PI_LST_MDFD_BY, Date PI_DT_LST_MDFD,
                                            String PI_AD_LV_CTGRY, byte PI_VRNC_ADJSTD, byte PI_WSTG_ADJSTD, Integer PI_AD_BRNCH, Integer PI_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvPhysicalInventory entity = new LocalInvPhysicalInventory();

			Debug.print("InvPhysicalInventoryBean create");

			entity.setPiCode(PI_CODE);
			entity.setPiDate(PI_DT);
			entity.setPiReferenceNumber(PI_RFRNC_NMBR);
			entity.setPiDescription(PI_DESC);
			entity.setPiCreatedBy(PI_CRTD_BY);
			entity.setPiDateCreated(PI_DT_CRTD);
			entity.setPiLastModifiedBy(PI_LST_MDFD_BY);
			entity.setPiDateLastModified(PI_DT_LST_MDFD);
			entity.setPiAdLvCategory(PI_AD_LV_CTGRY);
			entity.setPiVarianceAdjusted(PI_VRNC_ADJSTD);
			entity.setPiWastageAdjusted(PI_WSTG_ADJSTD);
			entity.setPiAdBranch(PI_AD_BRNCH);
			entity.setPiAdCompany(PI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvPhysicalInventory create(Date PI_DT, String PI_RFRNC_NMBR, String PI_DESC,
                                            String PI_CRTD_BY, Date PI_DT_CRTD, String PI_LST_MDFD_BY, Date PI_DT_LST_MDFD, String PI_AD_LV_CTGRY,
                                            byte PI_VRNC_ADJSTD, byte PI_WSTG_ADJSTD, Integer PI_AD_BRNCH, Integer PI_AD_CMPNY) throws CreateException {
		try {

			LocalInvPhysicalInventory entity = new LocalInvPhysicalInventory();

			Debug.print("InvPhysicalInventoryBean create");

			entity.setPiDate(PI_DT);
			entity.setPiReferenceNumber(PI_RFRNC_NMBR);
			entity.setPiDescription(PI_DESC);
			entity.setPiCreatedBy(PI_CRTD_BY);
			entity.setPiDateCreated(PI_DT_CRTD);
			entity.setPiLastModifiedBy(PI_LST_MDFD_BY);
			entity.setPiDateLastModified(PI_DT_LST_MDFD);
			entity.setPiAdLvCategory(PI_AD_LV_CTGRY);
			entity.setPiVarianceAdjusted(PI_VRNC_ADJSTD);
			entity.setPiWastageAdjusted(PI_WSTG_ADJSTD);
			entity.setPiAdBranch(PI_AD_BRNCH);
			entity.setPiAdCompany(PI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}