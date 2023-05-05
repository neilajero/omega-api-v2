package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArReceiptBatch;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArReceiptBatchHome {

	public static final String JNDI_NAME = "LocalArReceiptBatchHome!com.ejb.ar.LocalArReceiptBatchHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArReceiptBatchHome() {
	}

	// FINDER METHODS

	public LocalArReceiptBatch findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArReceiptBatch entity = (LocalArReceiptBatch) em
					.find(new LocalArReceiptBatch(), pk);
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

	public LocalArReceiptBatch findByRbName(java.lang.String RB_NM, java.lang.Integer IB_AD_BRNCH,
			java.lang.Integer RB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rb) FROM ArReceiptBatch rb WHERE rb.rbName=?1 AND rb.rbAdBranch=?2 AND rb.rbAdCompany=?3");
			query.setParameter(1, RB_NM);
			query.setParameter(2, IB_AD_BRNCH);
			query.setParameter(3, RB_AD_CMPNY);
            return (LocalArReceiptBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArReceiptBatchHome.findByRbName(java.lang.String RB_NM, java.lang.Integer IB_AD_BRNCH, java.lang.Integer RB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptBatchHome.findByRbName(java.lang.String RB_NM, java.lang.Integer IB_AD_BRNCH, java.lang.Integer RB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArReceiptBatch findByRbName(java.lang.String RB_NM, java.lang.Integer IB_AD_BRNCH,
											java.lang.Integer RB_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(rb) FROM ArReceiptBatch rb WHERE rb.rbName=?1 AND rb.rbAdBranch=?2 AND rb.rbAdCompany=?3",
					companyShortName);
			query.setParameter(1, RB_NM);
			query.setParameter(2, IB_AD_BRNCH);
			query.setParameter(3, RB_AD_CMPNY);
			return (LocalArReceiptBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findOpenRbByRbType(java.lang.String RB_TYP, java.lang.Integer AD_BRNCH,
			java.lang.Integer RB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rb) FROM ArReceiptBatch rb WHERE rb.rbType=?1 AND rb.rbStatus='OPEN' AND rb.rbAdBranch = ?2 AND rb.rbAdCompany=?3");
			query.setParameter(1, RB_TYP);
			query.setParameter(2, AD_BRNCH);
			query.setParameter(3, RB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptBatchHome.findOpenRbByRbType(java.lang.String RB_TYP, java.lang.Integer AD_BRNCH, java.lang.Integer RB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer RB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rb) FROM ArReceiptBatch rb WHERE rb.rbStatus='OPEN' AND rb.rbAdBranch = ?1 AND rb.rbAdCompany=?2");
			query.setParameter(1, AD_BRNCH);
			query.setParameter(2, RB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArReceiptBatchHome.findOpenRbAll(java.lang.Integer AD_BRNCH, java.lang.Integer RB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRbByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArReceiptBatch create(Integer RB_CODE, String RB_NM, String RB_DESC, String RB_STATUS,
                                      String RB_TYP, Date RB_DT_CRTD, String RB_CRTD_BY, Integer RB_AD_BRNCH, Integer RB_AD_CMPNY)
			throws CreateException {
		try {

			LocalArReceiptBatch entity = new LocalArReceiptBatch();

			Debug.print("ArReceiptBatchBean create");

			entity.setRbCode(RB_CODE);
			entity.setRbName(RB_NM);
			entity.setRbDescription(RB_DESC);
			entity.setRbStatus(RB_STATUS);
			entity.setRbType(RB_TYP);
			entity.setRbDateCreated(RB_DT_CRTD);
			entity.setRbCreatedBy(RB_CRTD_BY);
			entity.setRbAdBranch(RB_AD_BRNCH);
			entity.setRbAdCompany(RB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceiptBatch create(String RB_NM, String RB_DESC, String RB_STATUS, String RB_TYP,
                                      Date RB_DT_CRTD, String RB_CRTD_BY, Integer RB_AD_BRNCH, Integer RB_AD_CMPNY) throws CreateException {
		try {

			LocalArReceiptBatch entity = new LocalArReceiptBatch();

			Debug.print("ArReceiptBatchBean create");

			entity.setRbName(RB_NM);
			entity.setRbDescription(RB_DESC);
			entity.setRbStatus(RB_STATUS);
			entity.setRbType(RB_TYP);
			entity.setRbDateCreated(RB_DT_CRTD);
			entity.setRbCreatedBy(RB_CRTD_BY);
			entity.setRbAdBranch(RB_AD_BRNCH);
			entity.setRbAdCompany(RB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}