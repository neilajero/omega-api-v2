package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFrgAccountAssignment;
import com.util.Debug;

@Stateless
public class LocalGlFrgAccountAssignmentHome {

	public static final String JNDI_NAME = "LocalGlFrgAccountAssignmentHome!com.ejb.gl.LocalGlFrgAccountAssignmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlFrgAccountAssignmentHome() {
	}

	// FINDER METHODS

	public LocalGlFrgAccountAssignment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlFrgAccountAssignment entity = (LocalGlFrgAccountAssignment) em
					.find(new LocalGlFrgAccountAssignment(), pk);
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

	public java.util.Collection findByRowCode(java.lang.Integer ROW_CODE, java.lang.Integer AA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(aa) FROM GlFrgRow row, IN(row.glFrgAccountAssignments) aa WHERE row.rowCode = ?1 AND aa.aaAdCompany = ?2");
			query.setParameter(1, ROW_CODE);
			query.setParameter(2, AA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlFrgAccountAssignmentHome.findByRowCode(java.lang.Integer ROW_CODE, java.lang.Integer AA_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlFrgAccountAssignment create(Integer GL_AA_CODE, String AA_ACCNT_LOW, String AA_ACCNT_HGH,
                                              String AA_ACTVTY_TYP, Integer AA_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgAccountAssignment entity = new LocalGlFrgAccountAssignment();

			Debug.print("GlFrgAccountAssignmentBean create");

			entity.setAaCode(GL_AA_CODE);
			entity.setAaAccountLow(AA_ACCNT_LOW);
			entity.setAaAccountHigh(AA_ACCNT_HGH);
			entity.setAaActivityType(AA_ACTVTY_TYP);
			entity.setAaAdCompany(AA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlFrgAccountAssignment create(String AA_ACCNT_LOW, String AA_ACCNT_HGH, String AA_ACTVTY_TYP,
                                              Integer AA_AD_CMPNY) throws CreateException {
		try {

			LocalGlFrgAccountAssignment entity = new LocalGlFrgAccountAssignment();

			Debug.print("GlFrgAccountAssignmentBean create");

			entity.setAaAccountLow(AA_ACCNT_LOW);
			entity.setAaAccountHigh(AA_ACCNT_HGH);
			entity.setAaActivityType(AA_ACTVTY_TYP);
			entity.setAaAdCompany(AA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}