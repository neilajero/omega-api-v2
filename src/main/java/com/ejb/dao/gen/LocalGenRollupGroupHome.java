package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenRollupGroup;
import com.util.Debug;

@Stateless
public class LocalGenRollupGroupHome {

	public static final String JNDI_NAME = "LocalGenRollupGroupHome!com.ejb.genfld.LocalGenRollupGroupHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenRollupGroupHome() {
	}

	// FINDER METHODS

	public LocalGenRollupGroup findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenRollupGroup entity = (LocalGenRollupGroup) em
					.find(new LocalGenRollupGroup(), pk);
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

	public LocalGenRollupGroup create(java.lang.Integer RLG_CODE, java.lang.String RLG_NM,
                                      java.lang.String RLG_DESC, Integer RLG_AD_CMPNY) throws CreateException {
		try {

			LocalGenRollupGroup entity = new LocalGenRollupGroup();

			Debug.print("GenRollupGroupBean create");
			entity.setRlgCode(RLG_CODE);
			entity.setRlgName(RLG_NM);
			entity.setRlgDescription(RLG_DESC);
			entity.setRlgAdCompany(RLG_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}