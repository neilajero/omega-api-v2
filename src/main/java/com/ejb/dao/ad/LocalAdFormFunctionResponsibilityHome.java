package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdFormFunctionResponsibility;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdFormFunctionResponsibilityHome {

	public static final String JNDI_NAME = "LocalAdFormFunctionResponsibilityHome!com.ejb.ad.LocalAdFormFunctionResponsibilityHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdFormFunctionResponsibilityHome() {
	}

	// FINDER METHODS

	public LocalAdFormFunctionResponsibility findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdFormFunctionResponsibility entity = (LocalAdFormFunctionResponsibility) em
					.find(new LocalAdFormFunctionResponsibility(), pk);
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

	public java.util.Collection findByRsCode(java.lang.Integer AD_RS_CODE, java.lang.Integer FR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM AdResponsibility rs, IN(rs.adFormFunctionResponsibilities) fr WHERE rs.rsCode = ?1 AND fr.frAdCompany = ?2");
			query.setParameter(1, AD_RS_CODE);
			query.setParameter(2, FR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdFormFunctionResponsibilityHome.findByRsCode(java.lang.Integer AD_RS_CODE, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdFormFunctionResponsibility findByRsCodeAndFfName(java.lang.Integer AD_RS_CODE, java.lang.String FF_NM,
			java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM AdFormFunctionResponsibility fr WHERE fr.adResponsibility.rsCode=?1 AND fr.adFormFunction.ffName=?2 AND fr.frAdCompany = ?3");
			query.setParameter(1, AD_RS_CODE);
			query.setParameter(2, FF_NM);
			query.setParameter(3, FR_AD_CMPNY);
            return (LocalAdFormFunctionResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdFormFunctionResponsibilityHome.findByRsCodeAndFfName(java.lang.Integer AD_RS_CODE, java.lang.String FF_NM, java.lang.Integer FR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdFormFunctionResponsibilityHome.findByRsCodeAndFfName(java.lang.Integer AD_RS_CODE, java.lang.String FF_NM, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdFormFunctionResponsibility findByRsCodeAndFfCode(java.lang.Integer AD_RS_CODE, java.lang.Integer FF_CODE,
			java.lang.Integer FR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fr) FROM AdFormFunctionResponsibility fr WHERE fr.adResponsibility.rsCode=?1 AND fr.adFormFunction.ffCode=?2 AND fr.frAdCompany = ?3");
			query.setParameter(1, AD_RS_CODE);
			query.setParameter(2, FF_CODE);
			query.setParameter(3, FR_AD_CMPNY);
            return (LocalAdFormFunctionResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdFormFunctionResponsibilityHome.findByRsCodeAndFfCode(java.lang.Integer AD_RS_CODE, java.lang.Integer FF_CODE, java.lang.Integer FR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdFormFunctionResponsibilityHome.findByRsCodeAndFfCode(java.lang.Integer AD_RS_CODE, java.lang.Integer FF_CODE, java.lang.Integer FR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdFormFunctionResponsibility create(Integer FR_CODE, String FR_PARAM, Integer FR_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdFormFunctionResponsibility entity = new LocalAdFormFunctionResponsibility();

			Debug.print("AdFormFunctionResponsibilityBean create");
			entity.setFrCode(FR_CODE);
			entity.setFrParameter(FR_PARAM);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdFormFunctionResponsibility create(String FR_PARAM, Integer FR_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdFormFunctionResponsibility entity = new LocalAdFormFunctionResponsibility();

			Debug.print("AdFormFunctionResponsibilityBean create");
			entity.setFrParameter(FR_PARAM);
			entity.setFrAdCompany(FR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}