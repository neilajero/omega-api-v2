package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlResponsibility;
import com.util.Debug;

@Stateless
public class LocalGlResponsibilityHome {

	public static final String JNDI_NAME = "LocalGlResponsibilityHome!com.ejb.gl.LocalGlResponsibilityHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlResponsibilityHome() {
	}

	// FINDER METHODS

	public LocalGlResponsibility findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlResponsibility entity = (LocalGlResponsibility) em
					.find(new LocalGlResponsibility(), pk);
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

	public LocalGlResponsibility create(java.lang.Integer RES_CODE, byte RES_ENBL, Integer RES_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlResponsibility entity = new LocalGlResponsibility();

			Debug.print("GlResponsibilityBean create");

			entity.setResCode(RES_CODE);
			entity.setResEnable(RES_ENBL);
			entity.setResAdCompany(RES_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}