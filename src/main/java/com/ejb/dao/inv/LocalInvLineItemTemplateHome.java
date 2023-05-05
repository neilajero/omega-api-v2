package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvLineItemTemplate;
import com.util.Debug;

@Stateless
public class LocalInvLineItemTemplateHome {

	public static final String JNDI_NAME = "LocalInvLineItemTemplateHome!com.ejb.inv.LocalInvLineItemTemplateHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvLineItemTemplateHome() {
	}

	// FINDER METHODS

	public LocalInvLineItemTemplate findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvLineItemTemplate entity = (LocalInvLineItemTemplate) em
					.find(new LocalInvLineItemTemplate(), pk);
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

	public LocalInvLineItemTemplate findByLitName(java.lang.String LIT_NM, java.lang.Integer LIT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(lit) FROM InvLineItemTemplate lit WHERE lit.litName = ?1 AND lit.litAdCompany = ?2");
			query.setParameter(1, LIT_NM);
			query.setParameter(2, LIT_AD_CMPNY);
            return (LocalInvLineItemTemplate) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvLineItemTemplateHome.findByLitName(java.lang.String LIT_NM, java.lang.Integer LIT_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLineItemTemplateHome.findByLitName(java.lang.String LIT_NM, java.lang.Integer LIT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findLitAll(java.lang.Integer LIT_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(lit) FROM InvLineItemTemplate lit WHERE lit.litAdCompany = ?1");
			query.setParameter(1, LIT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLineItemTemplateHome.findLitAll(java.lang.Integer LIT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getLitByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalInvLineItemTemplate create(Integer LIT_CODE, String LIT_NM, String LIT_DESC,
                                           Integer LIT_AD_CMPNY) throws CreateException {
		try {

			LocalInvLineItemTemplate entity = new LocalInvLineItemTemplate();

			Debug.print("InvLineItemTemplateBean create");

			entity.setLitCode(LIT_CODE);
			entity.setLitName(LIT_NM);
			entity.setLitDescription(LIT_DESC);
			entity.setLitAdCompany(LIT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLineItemTemplate create(String LIT_NM, String LIT_DESC, Integer LIT_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvLineItemTemplate entity = new LocalInvLineItemTemplate();

			Debug.print("InvLineItemTemplateBean create");

			entity.setLitName(LIT_NM);
			entity.setLitDescription(LIT_DESC);
			entity.setLitAdCompany(LIT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}