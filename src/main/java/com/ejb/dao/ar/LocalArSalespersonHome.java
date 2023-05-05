package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArSalesperson;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArSalespersonHome {

	public static final String JNDI_NAME = "LocalArSalespersonHome!com.ejb.ar.LocalArSalespersonHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArSalespersonHome() {
	}

	// FINDER METHODS

	public LocalArSalesperson findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArSalesperson entity = (LocalArSalesperson) em
					.find(new LocalArSalesperson(), pk);
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

	public java.util.Collection findSlpAll(java.lang.Integer SLP_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(slp) FROM ArSalesperson slp WHERE slp.slpAdCompany = ?1");
			query.setParameter(1, SLP_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalespersonHome.findSlpAll(java.lang.Integer SLP_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArSalesperson findBySlpSalespersonCode(java.lang.String SLP_SLSPRSN_CODE,
			java.lang.Integer SLP_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(slp) FROM ArSalesperson slp WHERE slp.slpSalespersonCode = ?1 AND slp.slpAdCompany = ?2");
			query.setParameter(1, SLP_SLSPRSN_CODE);
			query.setParameter(2, SLP_AD_CMPNY);
            return (LocalArSalesperson) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArSalespersonHome.findBySlpSalespersonCode(java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer SLP_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalespersonHome.findBySlpSalespersonCode(java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer SLP_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSlpByBrCode(java.lang.Integer SLP_AD_BRNCH, java.lang.Integer SLP_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(slp) FROM ArSalesperson slp, IN(slp.adBranchSalespersons) bslp WHERE bslp.adBranch.brCode = ?1 AND slp.slpAdCompany = ?2");
			query.setParameter(1, SLP_AD_BRNCH);
			query.setParameter(2, SLP_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalespersonHome.findSlpByBrCode(java.lang.Integer SLP_AD_BRNCH, java.lang.Integer SLP_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArSalesperson create(Integer SLP_CODE, String SLP_SLSPRSN_CODE, String SLP_NM,
                                     Date SLP_ENTRY_DT, String SLP_ADDRSS, String SLP_PHN, String SLP_MBL_PHN, String SLP_EML,
                                     Integer SLP_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesperson entity = new LocalArSalesperson();

			Debug.print("ArSalespersonBean create");

			entity.setSlpCode(SLP_CODE);
			entity.setSlpSalespersonCode(SLP_SLSPRSN_CODE);
			entity.setSlpName(SLP_NM);
			entity.setSlpEntryDate(SLP_ENTRY_DT);
			entity.setSlpAddress(SLP_ADDRSS);
			entity.setSlpPhone(SLP_PHN);
			entity.setSlpMobilePhone(SLP_MBL_PHN);
			entity.setSlpEmail(SLP_EML);
			entity.setSlpAdCompany(SLP_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArSalesperson create(String SLP_SLSPRSN_CODE, String SLP_NM, Date SLP_ENTRY_DT,
                                     String SLP_ADDRSS, String SLP_PHN, String SLP_MBL_PHN, String SLP_EML, Integer SLP_AD_CMPNY)
			throws CreateException {
		try {

			LocalArSalesperson entity = new LocalArSalesperson();

			Debug.print("ArSalespersonBean create");

			entity.setSlpSalespersonCode(SLP_SLSPRSN_CODE);
			entity.setSlpName(SLP_NM);
			entity.setSlpEntryDate(SLP_ENTRY_DT);
			entity.setSlpAddress(SLP_ADDRSS);
			entity.setSlpPhone(SLP_PHN);
			entity.setSlpMobilePhone(SLP_MBL_PHN);
			entity.setSlpEmail(SLP_EML);
			entity.setSlpAdCompany(SLP_AD_CMPNY);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}