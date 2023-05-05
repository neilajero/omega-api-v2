package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenChildRange;
import com.util.Debug;

@Stateless
public class LocalGenChildRangeHome {

	public static final String JNDI_NAME = "LocalGenChildRangeHome!com.ejb.genfld.LocalGenChildRangeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenChildRangeHome() {
	}

	// FINDER METHODS

	public LocalGenChildRange findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenChildRange entity = (LocalGenChildRange) em
					.find(new LocalGenChildRange(), pk);
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

	public LocalGenChildRange create(java.lang.Integer CR_CODE, int CR_LW, int CR_HGH, char CR_RNG_TYP,
                                     Integer CR_AD_CMPNY) throws CreateException {
		try {

			LocalGenChildRange entity = new LocalGenChildRange();

			Debug.print("GenChildRangeBean create");
			entity.setCrCode(CR_CODE);
			entity.setCrLow(CR_LW);
			entity.setCrHigh(CR_HGH);
			entity.setCrRangeType(CR_RNG_TYP);
			entity.setCrAdCompany(CR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}