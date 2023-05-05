package com.ejb.dao.ad;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdUserResponsibility;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdUserResponsibilityHome {

	public static final String JNDI_NAME = "LocalAdUserResponsibilityHome!com.ejb.ad.LocalAdUserResponsibilityHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdUserResponsibilityHome() {
	}

	// FINDER METHODS

	public LocalAdUserResponsibility findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdUserResponsibility entity = (LocalAdUserResponsibility) em
					.find(new LocalAdUserResponsibility(), pk);
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

	public java.util.Collection findByUsrCode(java.lang.Integer USR_CODE, java.lang.Integer UR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ur) FROM AdUser usr, IN(usr.adUserResponsibilities) ur WHERE usr.usrCode = ?1 AND ur.urAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, UR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdUserResponsibilityHome.findByUsrCode(java.lang.Integer USR_CODE, java.lang.Integer UR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdUserResponsibility findByRsNameAndUsrCode(java.lang.String RS_NM, java.lang.Integer USR_CODE,
			java.lang.Integer UR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ur) FROM AdUserResponsibility ur WHERE ur.adResponsibility.rsName=?1 AND ur.adUser.usrCode=?2 AND ur.urAdCompany = ?3");
			query.setParameter(1, RS_NM);
			query.setParameter(2, USR_CODE);
			query.setParameter(3, UR_AD_CMPNY);
            return (LocalAdUserResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdUserResponsibilityHome.findByRsNameAndUsrCode(java.lang.String RS_NM, java.lang.Integer USR_CODE, java.lang.Integer UR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdUserResponsibilityHome.findByRsNameAndUsrCode(java.lang.String RS_NM, java.lang.Integer USR_CODE, java.lang.Integer UR_AD_CMPNY)");
			throw ex;
		}
	}
	
	public LocalAdUserResponsibility findRsCodeByUsrName(java.lang.Integer USR_CODE, java.lang.Integer UR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ur) FROM AdUserResponsibility ur WHERE ur.adUser.usrCode=?1 AND ur.urAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, UR_AD_CMPNY);
            return (LocalAdUserResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdUserResponsibilityHome.findRsCodeByUsrName(String USR_NM, java.lang.Integer UR_AD_CMPNY) ");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdUserResponsibilityHome.findRsCodeByUsrName(String USR_NM, java.lang.Integer UR_AD_CMPNY) ");
			throw ex;
		}
	}

	public LocalAdUserResponsibility findByUrPmUsrReferenceID(java.lang.Integer UR_PM_USR_ID,
			java.lang.Integer UR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ur) FROM AdUserResponsibility ur WHERE ur.adUser.pmUser.usrReferenceID = ?1 AND ur.urAdCompany = ?2");
			query.setParameter(1, UR_PM_USR_ID);
			query.setParameter(2, UR_AD_CMPNY);
            return (LocalAdUserResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdUserResponsibilityHome.findByUrPmUsrReferenceID(java.lang.Integer UR_PM_USR_ID, java.lang.Integer UR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdUserResponsibilityHome.findByUrPmUsrReferenceID(java.lang.Integer UR_PM_USR_ID, java.lang.Integer UR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdUserResponsibility create(Integer UR_CODE, Date UR_DT_FRM, Date UR_DT_TO,
                                            Integer UR_AD_CMPNY) throws CreateException {
		try {

			LocalAdUserResponsibility entity = new LocalAdUserResponsibility();

			Debug.print("AdUserResponsibilityBean create");
			entity.setUrCode(UR_CODE);
			entity.setUrDateFrom(UR_DT_FRM);
			entity.setUrDateTo(UR_DT_TO);
			entity.setUrAdCompany(UR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdUserResponsibility create(Date UR_DT_FRM, Date UR_DT_TO, Integer UR_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdUserResponsibility entity = new LocalAdUserResponsibility();

			Debug.print("AdUserResponsibilityBean create");
			entity.setUrDateFrom(UR_DT_FRM);
			entity.setUrDateTo(UR_DT_TO);
			entity.setUrAdCompany(UR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}