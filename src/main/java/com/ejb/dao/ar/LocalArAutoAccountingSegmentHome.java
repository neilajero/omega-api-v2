package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArAutoAccountingSegment;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

import java.util.Collection;

@Stateless
public class LocalArAutoAccountingSegmentHome {

	public static final String JNDI_NAME = "LocalArAutoAccountingSegmentHome!com.ejb.ar.LocalArAutoAccountingSegmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArAutoAccountingSegmentHome() {
	}

	// FINDER METHODS

	public LocalArAutoAccountingSegment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArAutoAccountingSegment entity = (LocalArAutoAccountingSegment) em
					.find(new LocalArAutoAccountingSegment(), pk);
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

	public Collection<LocalArAutoAccountingSegment> findByAaAccountType(java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(aas) FROM ArAutoAccounting aa, IN(aa.arAutoAccountingSegments) aas WHERE aa.aaAccountType = ?1 AND aas.aasAdCompany = ?2");
			query.setParameter(1, AA_ACCNT_TYP);
			query.setParameter(2, AAS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(ex.getMessage());
			throw ex;
		}
	}

	public Collection<LocalArAutoAccountingSegment> findByAaAccountType(java.lang.String AA_ACCNT_TYP,
																		java.lang.Integer AAS_AD_CMPNY,
																		String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany("SELECT OBJECT(aas) FROM ArAutoAccounting aa, IN(aa.arAutoAccountingSegments) aas "
					+ "WHERE aa.aaAccountType = ?1 AND aas.aasAdCompany = ?2",
					companyShortName);
			query.setParameter(1, AA_ACCNT_TYP);
			query.setParameter(2, AAS_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(ex.getMessage());
			throw ex;
		}
	}

	public LocalArAutoAccountingSegment findByAasSegmentNumberAndAaAccountType(short AAS_SGMNT_NMBR,
			java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(aas) FROM ArAutoAccounting aa, IN(aa.arAutoAccountingSegments) aas WHERE aas.aasSegmentNumber=?1 AND aa.aaAccountType=?2 AND aas.aasAdCompany = ?3");
			query.setParameter(1, AAS_SGMNT_NMBR);
			query.setParameter(2, AA_ACCNT_TYP);
			query.setParameter(3, AAS_AD_CMPNY);
            return (LocalArAutoAccountingSegment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArAutoAccountingSegmentHome.findByAasSegmentNumberAndAaAccountType(short AAS_SGMNT_NMBR, java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAutoAccountingSegmentHome.findByAasSegmentNumberAndAaAccountType(short AAS_SGMNT_NMBR, java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAasClassTypeAndAaAccountType(java.lang.String AAS_CLSS_TYP,
			java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(aas) FROM ArAutoAccounting aa, IN(aa.arAutoAccountingSegments) aas WHERE aas.aasClassType=?1 AND aa.aaAccountType = ?2 AND aas.aasAdCompany = ?3");
			query.setParameter(1, AAS_CLSS_TYP);
			query.setParameter(2, AA_ACCNT_TYP);
			query.setParameter(3, AAS_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAutoAccountingSegmentHome.findByAasClassTypeAndAaAccountType(java.lang.String AAS_CLSS_TYP, java.lang.String AA_ACCNT_TYP, java.lang.Integer AAS_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArAutoAccountingSegment create(Integer AAS_CODE, short AAS_SGMNT_NMBR, String AAS_CLSS_TYP,
                                               Integer AAS_AD_CMPNY) throws CreateException {
		try {

			LocalArAutoAccountingSegment entity = new LocalArAutoAccountingSegment();

			Debug.print("ArAutoAccountingSegmentBean create");
			entity.setAasCode(AAS_CODE);
			entity.setAasSegmentNumber(AAS_SGMNT_NMBR);
			entity.setAasClassType(AAS_CLSS_TYP);
			entity.setAasAdCompany(AAS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArAutoAccountingSegment create(short AAS_SGMNT_NMBR, String AAS_CLSS_TYP,
                                               Integer AAS_AD_CMPNY) throws CreateException {
		try {

			LocalArAutoAccountingSegment entity = new LocalArAutoAccountingSegment();

			Debug.print("ArAutoAccountingSegmentBean create");
			entity.setAasSegmentNumber(AAS_SGMNT_NMBR);
			entity.setAasClassType(AAS_CLSS_TYP);
			entity.setAasAdCompany(AAS_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}