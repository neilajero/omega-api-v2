package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvPhysicalInventoryLine;
import com.util.Debug;

@Stateless
public class LocalInvPhysicalInventoryLineHome {

	public static final String JNDI_NAME = "LocalInvPhysicalInventoryLineHome!com.ejb.inv.LocalInvPhysicalInventoryLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvPhysicalInventoryLineHome() {
	}

	// FINDER METHODS

	public LocalInvPhysicalInventoryLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvPhysicalInventoryLine entity = (LocalInvPhysicalInventoryLine) em
					.find(new LocalInvPhysicalInventoryLine(), pk);
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

	public java.util.Collection findByPiDateAndLocName(java.util.Date PI_DT, java.lang.String LOC_NM,
			java.lang.Integer PIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pil) FROM InvPhysicalInventoryLine pil WHERE pil.invPhysicalInventory.piDate=?1 AND pil.invItemLocation.invLocation.locName=?2 and pil.pilAdCompany=?3");
			query.setParameter(1, PI_DT);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, PIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryLineHome.findByPiDateAndLocName(java.com.util.Date PI_DT, java.lang.String LOC_NM, java.lang.Integer PIL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvPhysicalInventoryLine findByPiCodeAndIlCode(java.lang.Integer PI_CODE, java.lang.Integer LOC_CODE,
			java.lang.Integer PIL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pil) FROM InvPhysicalInventoryLine pil WHERE pil.invPhysicalInventory.piCode=?1 AND pil.invItemLocation.ilCode=?2 and pil.pilAdCompany=?3");
			query.setParameter(1, PI_CODE);
			query.setParameter(2, LOC_CODE);
			query.setParameter(3, PIL_AD_CMPNY);
            return (LocalInvPhysicalInventoryLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvPhysicalInventoryLineHome.findByPiCodeAndIlCode(java.lang.Integer PI_CODE, java.lang.Integer LOC_CODE, java.lang.Integer PIL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryLineHome.findByPiCodeAndIlCode(java.lang.Integer PI_CODE, java.lang.Integer LOC_CODE, java.lang.Integer PIL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByPiCode(java.lang.Integer PI_CODE, java.lang.Integer PIL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pil) FROM InvPhysicalInventoryLine pil WHERE pil.invPhysicalInventory.piCode=?1 AND pil.pilAdCompany = ?2 ORDER BY pil.invItemLocation.invItem.iiName");
			query.setParameter(1, PI_CODE);
			query.setParameter(2, PIL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvPhysicalInventoryLineHome.findByPiCode(java.lang.Integer PI_CODE, java.lang.Integer PIL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalInvPhysicalInventoryLine create(Integer PIL_CODE, double PIL_ENDNG_INVNTRY, double PIL_WSTG,
                                                double PIL_VRNC, String PIL_EXPRY_DT, Integer PIL_AD_CMPNY) throws CreateException {
		try {

			LocalInvPhysicalInventoryLine entity = new LocalInvPhysicalInventoryLine();

			Debug.print("InvPhysicalInventoryLineBean create");

			entity.setPilCode(PIL_CODE);
			entity.setPilEndingInventory(PIL_ENDNG_INVNTRY);
			entity.setPilWastage(PIL_WSTG);
			entity.setPilVariance(PIL_VRNC);
			entity.setPilMisc(PIL_EXPRY_DT);
			entity.setPilAdCompany(PIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvPhysicalInventoryLine create(double PIL_ENDNG_INVNTRY, double PIL_WSTG, double PIL_VRNC,
                                                String PIL_EXPRY_DT, Integer PIL_AD_CMPNY) throws CreateException {
		try {

			LocalInvPhysicalInventoryLine entity = new LocalInvPhysicalInventoryLine();

			Debug.print("InvPhysicalInventoryLineBean create");

			entity.setPilEndingInventory(PIL_ENDNG_INVNTRY);
			entity.setPilWastage(PIL_WSTG);
			entity.setPilVariance(PIL_VRNC);
			entity.setPilMisc(PIL_EXPRY_DT);
			entity.setPilAdCompany(PIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}