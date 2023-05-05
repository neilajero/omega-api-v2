package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlAccountRange;
import com.util.Debug;

@Stateless
public class LocalGlAccountRangeHome {

	public static final String JNDI_NAME = "LocalGlAccountRangeHome!com.ejb.gl.LocalGlAccountRangeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlAccountRangeHome() {
	}

	// FINDER METHODS

	public LocalGlAccountRange findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlAccountRange entity = (LocalGlAccountRange) em
					.find(new LocalGlAccountRange(), pk);
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

	public LocalGlAccountRange create(java.lang.Integer AR_CODE, short AR_LN, java.lang.String AR_ACCNT_LW,
                                      java.lang.String AR_ACCNT_HGH, java.lang.Integer AR_AD_CMPNY) throws CreateException {
		try {

			LocalGlAccountRange entity = new LocalGlAccountRange();

			Debug.print("GlAccountRangeBean create");
			entity.setArCode(AR_CODE);
			entity.setArLine(AR_LN);
			entity.setArAccountLow(AR_ACCNT_LW);
			entity.setArAccountHigh(AR_ACCNT_HGH);
			entity.setArAdCompany(AR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlAccountRange create(short AR_LN, java.lang.String AR_ACCNT_LW,
                                      java.lang.String AR_ACCNT_HGH, java.lang.Integer AR_AD_CMPNY) throws CreateException {
		try {

			LocalGlAccountRange entity = new LocalGlAccountRange();

			Debug.print("GlAccountRangeBean create");

			entity.setArLine(AR_LN);
			entity.setArAccountLow(AR_ACCNT_LW);
			entity.setArAccountHigh(AR_ACCNT_HGH);
			entity.setArAdCompany(AR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}