package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdFormFunction;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.util.Debug;

@Stateless
public class LocalAdFormFunctionHome {

	public static final String JNDI_NAME = "LocalAdFormFunctionHome!com.ejb.ad.LocalAdFormFunctionHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdFormFunctionHome() {
	}

	// FINDER METHODS

	public LocalAdFormFunction findByPrimaryKey(java.lang.Integer pk) throws FinderException {
		
		Debug.print("Primary Key : " + pk);

		try {

			LocalAdFormFunction entity = (LocalAdFormFunction) em
					.find(new LocalAdFormFunction(), pk);
			
			Debug.print("FF Entity : " + entity);
			
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

	public LocalAdFormFunction create(Integer FF_CODE, String FF_NM) throws CreateException {
		try {

			LocalAdFormFunction entity = new LocalAdFormFunction();

			Debug.print("AdFormFunctionBean create");
			entity.setFfCode(FF_CODE);
			entity.setFfName(FF_NM);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdFormFunction create(String FF_NM) throws CreateException {
		try {

			LocalAdFormFunction entity = new LocalAdFormFunction();

			Debug.print("AdFormFunctionBean create");
			entity.setFfName(FF_NM);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}