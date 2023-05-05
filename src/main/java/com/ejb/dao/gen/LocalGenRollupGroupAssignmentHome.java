package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenRollupGroupAssignment;
import com.util.Debug;

@Stateless
public class LocalGenRollupGroupAssignmentHome {

	public static final String JNDI_NAME = "LocalGenRollupGroupAssignmentHome!com.ejb.genfld.LocalGenRollupGroupAssignmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenRollupGroupAssignmentHome() {
	}

	// FINDER METHODS

	public LocalGenRollupGroupAssignment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenRollupGroupAssignment entity = (LocalGenRollupGroupAssignment) em
					.find(new LocalGenRollupGroupAssignment(), pk);
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

	// OTHER METHODS

	// CREATE METHODS

	public LocalGenRollupGroupAssignment create(java.lang.Integer RLGA_CODE, Integer RLGA_AD_CMPNY)
			throws CreateException {
		try {

			LocalGenRollupGroupAssignment entity = new LocalGenRollupGroupAssignment();

			Debug.print("GenRollupGroupAssignment create");
			entity.setRlgaCode(RLGA_CODE);
			entity.setRlgaAdCompany(RLGA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}