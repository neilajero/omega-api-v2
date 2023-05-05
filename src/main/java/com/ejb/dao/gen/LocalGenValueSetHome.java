package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenValueSet;
import com.util.Debug;

@Stateless
public class LocalGenValueSetHome {

	public static final String JNDI_NAME = "LocalGenValueSetHome!com.ejb.genfld.LocalGenValueSetHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenValueSetHome() {
	}

	// FINDER METHODS

	public LocalGenValueSet findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenValueSet entity = (LocalGenValueSet) em
					.find(new LocalGenValueSet(), pk);
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

	public java.util.Collection findVsAll(java.lang.Integer VS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(vs) FROM GenValueSet vs WHERE vs.vsAdCompany = ?1");
			query.setParameter(1, VS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetHome.findVsAll(java.lang.Integer VS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenValueSet findByVsName(java.lang.String VS_NM, java.lang.Integer VS_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(vs) FROM GenValueSet vs WHERE vs.vsName = ?1 AND vs.vsAdCompany = ?2");
			query.setParameter(1, VS_NM);
			query.setParameter(2, VS_AD_CMPNY);
            return (LocalGenValueSet) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetHome.findByVsName(java.lang.String VS_NM, java.lang.Integer VS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetHome.findByVsName(java.lang.String VS_NM, java.lang.Integer VS_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenValueSet findBySegmentType(char SG_SGMNT_TYP, java.lang.Integer VS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vs) FROM GenValueSet vs, IN(vs.genSegments) sg WHERE sg.sgSegmentType = ?1 AND vs.vsAdCompany = ?2");
			query.setParameter(1, SG_SGMNT_TYP);
			query.setParameter(2, VS_AD_CMPNY);
            return (LocalGenValueSet) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetHome.findBySegmentType(char SG_SGMNT_TYP, java.lang.Integer VS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetHome.findBySegmentType(char SG_SGMNT_TYP, java.lang.Integer VS_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGenValueSet create(java.lang.Integer VS_CODE, java.lang.String VS_NM,
                                   java.lang.String VS_DESC, byte VS_ENBL, Integer VS_AD_CMPNY) throws CreateException {
		try {

			LocalGenValueSet entity = new LocalGenValueSet();

			Debug.print("GenValueSetBean create");
			entity.setVsCode(VS_CODE);
			entity.setVsName(VS_NM);
			entity.setVsDescription(VS_DESC);
			entity.setVsEnable(VS_ENBL);
			entity.setVsAdCompany(VS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGenValueSet create(java.lang.String VS_NM, java.lang.String VS_DESC, byte VS_ENBL,
                                   Integer VS_AD_CMPNY) throws CreateException {
		try {

			LocalGenValueSet entity = new LocalGenValueSet();

			Debug.print("GenValueSetBean create");

			entity.setVsName(VS_NM);
			entity.setVsDescription(VS_DESC);
			entity.setVsEnable(VS_ENBL);
			entity.setVsAdCompany(VS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}