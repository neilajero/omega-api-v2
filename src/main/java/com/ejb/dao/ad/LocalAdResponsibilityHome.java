package com.ejb.dao.ad;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdResponsibility;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdResponsibilityHome {

	public static final String JNDI_NAME = "LocalAdResponsibilityHome!com.ejb.ad.LocalAdResponsibilityHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdResponsibilityHome() {
	}

	// FINDER METHODS

	public LocalAdResponsibility findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdResponsibility entity = (LocalAdResponsibility) em
					.find(new LocalAdResponsibility(), pk);
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

	public java.util.Collection findRsAll(java.lang.Integer RS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(rs) FROM AdResponsibility rs WHERE rs.rsAdCompany = ?1");
			query.setParameter(1, RS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdResponsibilityHome.findRsAll(java.lang.Integer RS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdResponsibility findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rs) FROM AdResponsibility rs WHERE rs.rsName = ?1 AND rs.rsAdCompany = ?2");
			query.setParameter(1, RS_NM);
			query.setParameter(2, RS_AD_CMPNY);
            return (LocalAdResponsibility) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdResponsibilityHome.findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdResponsibilityHome.findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRsByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalAdResponsibility create(Integer AD_RS_CODE, String RS_NM, String RS_DESC, Date RS_DT_FRM,
                                        Date RS_DT_TO, Integer RS_AD_CMPNY) throws CreateException {
		try {

			LocalAdResponsibility entity = new LocalAdResponsibility();

			Debug.print("AdResponsibilityBean create");
			entity.setRsCode(AD_RS_CODE);
			entity.setRsName(RS_NM);
			entity.setRsDescription(RS_DESC);
			entity.setRsDateFrom(RS_DT_FRM);
			entity.setRsDateTo(RS_DT_TO);
			entity.setRsAdCompany(RS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdResponsibility create(String RS_NM, String RS_DESC, Date RS_DT_FRM, Date RS_DT_TO,
                                        Integer RS_AD_CMPNY) throws CreateException {
		try {

			LocalAdResponsibility entity = new LocalAdResponsibility();

			Debug.print("AdResponsibilityBean create");
			entity.setRsName(RS_NM);
			entity.setRsDescription(RS_DESC);
			entity.setRsDateFrom(RS_DT_FRM);
			entity.setRsDateTo(RS_DT_TO);
			entity.setRsAdCompany(RS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}