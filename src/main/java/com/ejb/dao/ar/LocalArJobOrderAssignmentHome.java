package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArJobOrderAssignment;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArJobOrderAssignmentHome {

	public static final String JNDI_NAME = "LocalArJobOrderAssignmentHome!com.ejb.ar.LocalArJobOrderAssignmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArJobOrderAssignmentHome() {
	}

	// FINDER METHODS

	public LocalArJobOrderAssignment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArJobOrderAssignment entity = (LocalArJobOrderAssignment) em
					.find(new LocalArJobOrderAssignment(), pk);
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

	public java.util.Collection findByJoCodeAndJaSo(java.lang.Integer JO_CODE, byte JA_SO,
			java.lang.Integer JA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ja) FROM ArJobOrderAssignment ja WHERE ja.arJobOrderLine.arJobOrder.joCode=?1 AND ja.jaSo=?2 AND ja.jaAdCompany = ?3");
			query.setParameter(1, JO_CODE);
			query.setParameter(2, JA_SO);
			query.setParameter(3, JA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderAssignmentHome.findByJoCodeAndJaSo(java.lang.Integer JO_CODE, byte JA_SO, java.lang.Integer JA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJolCodeAndPeIdNumber(java.lang.Integer JOL_CODE, java.lang.String PE_ID_NUMBER,
			java.lang.Integer JA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ja) FROM ArJobOrderAssignment ja WHERE ja.arJobOrderLine.jolCode=?1 AND ja.arPersonel.peIdNumber = ?2 AND ja.jaAdCompany = ?3");
			query.setParameter(1, JOL_CODE);
			query.setParameter(2, PE_ID_NUMBER);
			query.setParameter(3, JA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderAssignmentHome.findByJolCodeAndPeIdNumber(java.lang.Integer JOL_CODE, java.lang.String PE_ID_NUMBER, java.lang.Integer JA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByJolCodeAndJaSo(java.lang.Integer JOL_CODE, byte JA_SO,
			java.lang.Integer JA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ja) FROM ArJobOrderAssignment ja WHERE ja.arJobOrderLine.jolCode=?1 AND ja.jaSo = ?2 AND ja.jaAdCompany = ?3");
			query.setParameter(1, JOL_CODE);
			query.setParameter(2, JA_SO);
			query.setParameter(3, JA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderAssignmentHome.findByJolCodeAndJaSo(java.lang.Integer JOL_CODE, byte JA_SO, java.lang.Integer JA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJaByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArJobOrderAssignment create(Integer JA_CODE, short JA_LN, String JA_RMKS, double JA_QTY,
                                            double JA_UNT_CST, double JA_AMNT, byte JA_SO, Integer JA_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderAssignment entity = new LocalArJobOrderAssignment();

			Debug.print("ArJobOrderAssignment create");

			entity.setJaCode(JA_CODE);
			entity.setJaLine(JA_LN);
			entity.setJaRemarks(JA_RMKS);
			entity.setJaQuantity(JA_QTY);
			entity.setJaUnitCost(JA_UNT_CST);
			entity.setJaAmount(JA_AMNT);
			entity.setJaSo(JA_SO);
			entity.setJaAdCompany(JA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArJobOrderAssignment create(short JA_LN, String JA_RMKS, double JA_QTY, double JA_UNT_CST,
                                            double JA_AMNT, byte JA_SO, Integer JA_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderAssignment entity = new LocalArJobOrderAssignment();

			Debug.print("ArJobOrderAssignment create");

			entity.setJaLine(JA_LN);
			entity.setJaRemarks(JA_RMKS);
			entity.setJaQuantity(JA_QTY);
			entity.setJaUnitCost(JA_UNT_CST);
			entity.setJaAmount(JA_AMNT);
			entity.setJaSo(JA_SO);
			entity.setJaAdCompany(JA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}