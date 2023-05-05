package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvLineItem;
import com.util.Debug;

@Stateless
public class LocalInvLineItemHome {

	public static final String JNDI_NAME = "LocalInvLineItemHome!com.ejb.inv.LocalInvLineItemHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvLineItemHome() {
	}

	// FINDER METHODS

	public LocalInvLineItem findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvLineItem entity = (LocalInvLineItem) em
					.find(new LocalInvLineItem(), pk);
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

	public java.util.Collection getLiByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalInvLineItem create(java.lang.Integer LI_CODE, short LI_LN, Integer LI_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvLineItem entity = new LocalInvLineItem();

			Debug.print("InvLineItemBean create");
			entity.setLiCode(LI_CODE);
			entity.setLiLine(LI_LN);
			entity.setLiAdCompany(LI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLineItem create(short LI_LN, Integer LI_AD_CMPNY) throws CreateException {
		try {

			LocalInvLineItem entity = new LocalInvLineItem();

			Debug.print("InvLineItemBean create");
			entity.setLiLine(LI_LN);
			entity.setLiAdCompany(LI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}