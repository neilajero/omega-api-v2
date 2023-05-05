package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudgetAccountAssignment;
import com.util.Debug;

@Stateless
public class LocalGlBudgetAccountAssignmentHome {

	public static final String JNDI_NAME = "LocalGlBudgetAccountAssignmentHome!com.ejb.gl.LocalGlBudgetAccountAssignmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlBudgetAccountAssignmentHome() {
	}

	// FINDER METHODS

	public LocalGlBudgetAccountAssignment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlBudgetAccountAssignment entity = (LocalGlBudgetAccountAssignment) em
					.find(new LocalGlBudgetAccountAssignment(), pk);
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

	public java.util.Collection findByBoCode(java.lang.Integer BO_CODE, java.lang.Integer BAA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(baa) FROM GlBudgetOrganization bo, IN(bo.glBudgetAccountAssignments) baa WHERE bo.boCode = ?1 AND baa.baaAdCompany = ?2");
			query.setParameter(1, BO_CODE);
			query.setParameter(2, BAA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetAccountAssignmentHome.findByBoCode(java.lang.Integer BO_CODE, java.lang.Integer BAA_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlBudgetAccountAssignment create(java.lang.Integer BAA_CODE, java.lang.String BAA_ACCNT_FRM,
                                                 java.lang.String BAA_ACCNT_TO, java.lang.String BAA_TYP, java.lang.Integer BAA_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlBudgetAccountAssignment entity = new LocalGlBudgetAccountAssignment();

			Debug.print("GlBudgetAccountAssignment create");
			entity.setBaaCode(BAA_CODE);
			entity.setBaaAccountFrom(BAA_ACCNT_FRM);
			entity.setBaaAccountTo(BAA_ACCNT_TO);
			entity.setBaaType(BAA_TYP);
			entity.setBaaAdCompany(BAA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlBudgetAccountAssignment create(java.lang.String BAA_ACCNT_FRM,
                                                 java.lang.String BAA_ACCNT_TO, java.lang.String BAA_TYP, java.lang.Integer BAA_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlBudgetAccountAssignment entity = new LocalGlBudgetAccountAssignment();

			Debug.print("GlBudgetAccountAssignment create");
			entity.setBaaAccountFrom(BAA_ACCNT_FRM);
			entity.setBaaAccountTo(BAA_ACCNT_TO);
			entity.setBaaType(BAA_TYP);
			entity.setBaaAdCompany(BAA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}