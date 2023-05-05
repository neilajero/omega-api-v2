package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArPersonel;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArPersonelHome {

	public static final String JNDI_NAME = "LocalArPersonelHome!com.ejb.ar.LocalArPersonelHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArPersonelHome() {
	}

	// FINDER METHODS

	public LocalArPersonel findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArPersonel entity = (LocalArPersonel) em.find(new LocalArPersonel(),
					pk);
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

	public java.util.Collection findPeAll(java.lang.Integer PE_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(pe) FROM ArPersonel pe WHERE pe.peAdCompany = ?1");
			query.setParameter(1, PE_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelHome.findPeAll(java.lang.Integer PE_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArPersonel findByPeIdNumber(java.lang.String PE_ID_NMBR, java.lang.Integer PE_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pe) FROM ArPersonel pe WHERE pe.peIdNumber = ?1 AND pe.peAdCompany = ?2");
			query.setParameter(1, PE_ID_NMBR);
			query.setParameter(2, PE_AD_CMPNY);
            return (LocalArPersonel) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArPersonelHome.findByPeIdNumber(java.lang.String PE_ID_NMBR, java.lang.Integer PE_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelHome.findByPeIdNumber(java.lang.String PE_ID_NMBR, java.lang.Integer PE_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArPersonel findByPeName(java.lang.String PE_NM, java.lang.Integer PE_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(pe) FROM ArPersonel pe WHERE pe.peName = ?1 AND pe.peAdCompany = ?2");
			query.setParameter(1, PE_NM);
			query.setParameter(2, PE_AD_CMPNY);
            return (LocalArPersonel) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArPersonelHome.findByPeName(java.lang.String PE_NM, java.lang.Integer PE_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPersonelHome.findByPeName(java.lang.String PE_NM, java.lang.Integer PE_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getPeByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArPersonel create(Integer PE_CODE, String PE_ID_NMBR, String PE_NM, String PE_DESC,
                                  String PE_ADDRSS, double PE_RT, Integer PE_AD_CMPNY) throws CreateException {
		try {

			LocalArPersonel entity = new LocalArPersonel();

			Debug.print("ArPersonel create");

			entity.setPeCode(PE_CODE);
			entity.setPeIdNumber(PE_ID_NMBR);
			entity.setPeName(PE_NM);
			entity.setPeDescription(PE_DESC);
			entity.setPeAddress(PE_ADDRSS);
			entity.setPeRate(PE_RT);
			entity.setPeAdCompany(PE_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArPersonel create(String PE_ID_NMBR, String PE_NM, String PE_DESC, String PE_ADDRSS,
                                  double PE_RT, Integer PE_AD_CMPNY) throws CreateException {
		try {

			LocalArPersonel entity = new LocalArPersonel();

			Debug.print("ArPersonel create");

			entity.setPeIdNumber(PE_ID_NMBR);
			entity.setPeName(PE_NM);
			entity.setPeDescription(PE_DESC);
			entity.setPeAddress(PE_ADDRSS);
			entity.setPeRate(PE_RT);
			entity.setPeAdCompany(PE_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}