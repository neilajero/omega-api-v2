package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdRole;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.util.Debug;

@Stateless
public class LocalAdRoleHome {

	public static final String JNDI_NAME = "LocalAdRoleHome!com.ejb.ad.LocalAdRoleHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdRoleHome() {
	}

	// FINDER METHODS

	public LocalAdRole findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdRole entity = (LocalAdRole) em.find(new LocalAdRole(), pk);
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

	public LocalAdRole create(Integer RL_CODE, String RL_USR_NM, String RL_RL_NM) throws CreateException {
		try {

			LocalAdRole entity = new LocalAdRole();

			Debug.print("AdRoleBean create");
			entity.setRlCode(RL_CODE);
			entity.setRlUserName(RL_USR_NM);
			entity.setRlRoleName(RL_RL_NM);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdRole create(String RL_USR_NM, String RL_RL_NM) throws CreateException {
		try {

			LocalAdRole entity = new LocalAdRole();

			Debug.print("AdRoleBean create");
			entity.setRlUserName(RL_USR_NM);
			entity.setRlRoleName(RL_RL_NM);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}