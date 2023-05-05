package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArAppliedCredit;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArAppliedCreditHome {

	public static final String JNDI_NAME = "LocalArAppliedCreditHome!com.ejb.ar.LocalArAppliedCreditHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArAppliedCreditHome() {
	}

	// FINDER METHODS

	public LocalArAppliedCredit findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArAppliedCredit entity = (LocalArAppliedCredit) em
					.find(new LocalArAppliedCredit(), pk);
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

	public java.util.Collection getAcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArAppliedCredit create(Integer AR_AC_CODE, double AC_APPLY_CRDT, Integer AC_AD_CMPNY)
			throws CreateException {
		try {

			LocalArAppliedCredit entity = new LocalArAppliedCredit();

			Debug.print("ArAppliedCreditBean create");
			entity.setAcCode(AR_AC_CODE);
			entity.setAcApplyCredit(AC_APPLY_CRDT);
			entity.setAcAdCompany(AC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArAppliedCredit create(double AC_APPLY_CRDT, Integer AC_AD_CMPNY) throws CreateException {
		try {

			LocalArAppliedCredit entity = new LocalArAppliedCredit();

			Debug.print("ArAppliedCreditBean create");
			entity.setAcApplyCredit(AC_APPLY_CRDT);
			entity.setAcAdCompany(AC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}