package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgRowSet;
import com.util.Debug;

@Stateless
public class LocalGlFrgRowSetHome {

	public static final String JNDI_NAME = "LocalGlFrgRowSetHome!com.ejb.gl.LocalGlFrgRowSetHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgRowSetHome() {
	}

	// FINDER METHODS

	public LocalGlFrgRowSet findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgRowSet entity = (LocalGlFrgRowSet) em
					.find(new LocalGlFrgRowSet(), pk);
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
			Query query = em.createQuery("SELECT OBJECT(rs) FROM GlFrgRowSet rs WHERE rs.rsAdCompany = ?1");
			query.setParameter(1, RS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowSetHome.findRsAll(java.lang.Integer RS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlFrgRowSet findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(rs) FROM GlFrgRowSet rs WHERE rs.rsName = ?1 AND rs.rsAdCompany = ?2");
			query.setParameter(1, RS_NM);
			query.setParameter(2, RS_AD_CMPNY);
            return (LocalGlFrgRowSet) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlFrgRowSetHome.findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgRowSetHome.findByRsName(java.lang.String RS_NM, java.lang.Integer RS_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgRowSet create(Integer GL_RS_CODE, String RS_NM, String RS_DESC, Integer RS_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgRowSet entity = new LocalGlFrgRowSet();

			Debug.print("GlFrgRowSetBean create");

			entity.setRsCode(GL_RS_CODE);
			entity.setRsName(RS_NM);
			entity.setRsDescription(RS_DESC);
			entity.setRsAdCompany(RS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgRowSet create(String RS_NM, String RS_DESC, Integer RS_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlFrgRowSet entity = new LocalGlFrgRowSet();

			Debug.print("GlFrgRowSetBean create");

			entity.setRsName(RS_NM);
			entity.setRsDescription(RS_DESC);
			entity.setRsAdCompany(RS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}