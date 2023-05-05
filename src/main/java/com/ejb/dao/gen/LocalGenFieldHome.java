package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenField;
import com.util.Debug;

@Stateless
public class LocalGenFieldHome {

	public static final String JNDI_NAME = "LocalGenFieldHome!com.ejb.genfld.LocalGenFieldHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenFieldHome() {
	}

	// FINDER METHODS

	public LocalGenField findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenField entity = (LocalGenField) em
					.find(new LocalGenField(), pk);
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

	public java.util.Collection findFlAllEnabled(java.lang.Integer FL_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(fl) FROM GenField fl WHERE fl.flEnable=1 AND fl.flAdCompany = ?1");
			query.setParameter(1, FL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenFieldHome.findFlAllEnabled(java.lang.Integer FL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenField findByFlName(java.lang.String FL_NM, java.lang.Integer FL_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(fl) FROM GenField fl WHERE fl.flName=?1 AND fl.flAdCompany = ?2");
			query.setParameter(1, FL_NM);
			query.setParameter(2, FL_AD_CMPNY);
            return (LocalGenField) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenFieldHome.findByFlName(java.lang.String FL_NM, java.lang.Integer FL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenFieldHome.findByFlName(java.lang.String FL_NM, java.lang.Integer FL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenField findByFlNumberOfSegment(short FL_NMBR_OF_NMBR, java.lang.Integer FL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(fl) FROM GenField fl WHERE fl.flNumberOfSegment=?1 AND fl.flAdCompany = ?2");
			query.setParameter(1, FL_NMBR_OF_NMBR);
			query.setParameter(2, FL_AD_CMPNY);
            return (LocalGenField) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenFieldHome.findByFlNumberOfSegment(short FL_NMBR_OF_NMBR, java.lang.Integer FL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenFieldHome.findByFlNumberOfSegment(short FL_NMBR_OF_NMBR, java.lang.Integer FL_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGenField create(java.lang.Integer FL_CODE, java.lang.String FL_NM,
                                java.lang.String FL_DESC, char FL_SGMNT_SPRTR, short FL_NMBR_OF_SGMNT, byte FL_FRZ_RLLP, byte FL_ENBL,
                                Integer FL_AD_CMPNY) throws CreateException {
		try {

			LocalGenField entity = new LocalGenField();

			Debug.print("GenFieldBean create");
			entity.setFlCode(FL_CODE);
			entity.setFlName(FL_NM);
			entity.setFlDescription(FL_DESC);
			entity.setFlSegmentSeparator(FL_SGMNT_SPRTR);
			entity.setFlNumberOfSegment(FL_NMBR_OF_SGMNT);
			entity.setFlFreezeRollup(FL_FRZ_RLLP);
			entity.setFlEnable(FL_ENBL);
			entity.setFlAdCompany(FL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGenField create(java.lang.String FL_NM, java.lang.String FL_DESC, char FL_SGMNT_SPRTR,
                                short FL_NMBR_OF_SGMNT, byte FL_FRZ_RLLP, byte FL_ENBL, Integer FL_AD_CMPNY) throws CreateException {
		try {

			LocalGenField entity = new LocalGenField();

			Debug.print("GenFieldBean create");

			entity.setFlName(FL_NM);
			entity.setFlDescription(FL_DESC);
			entity.setFlSegmentSeparator(FL_SGMNT_SPRTR);
			entity.setFlNumberOfSegment(FL_NMBR_OF_SGMNT);
			entity.setFlFreezeRollup(FL_FRZ_RLLP);
			entity.setFlEnable(FL_ENBL);
			entity.setFlAdCompany(FL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}