package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApCheckBatch;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApCheckBatchHome {

	public static final String JNDI_NAME = "LocalApCheckBatchHome!com.ejb.ap.LocalApCheckBatchHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApCheckBatchHome() {
	}

	// FINDER METHODS

	public LocalApCheckBatch findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApCheckBatch entity = (LocalApCheckBatch) em
					.find(new LocalApCheckBatch(), pk);
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

	public LocalApCheckBatch findByCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH,
			java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbName=?1 AND cb.cbAdBranch=?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, CB_NM);
			query.setParameter(2, CB_AD_BRNCH);
			query.setParameter(3, CB_AD_CMPNY);
            return (LocalApCheckBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApCheckBatchHome.findByCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findByCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApCheckBatch findByPaymentCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH,
			java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbType='PAYMENT' AND cb.cbName=?1 AND cb.cbAdBranch=?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, CB_NM);
			query.setParameter(2, CB_AD_BRNCH);
			query.setParameter(3, CB_AD_CMPNY);
            return (LocalApCheckBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApCheckBatchHome.findByPaymentCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findByPaymentCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApCheckBatch findByDirectCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH,
			java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbType='DIRECT' AND cb.cbName=?1 AND cb.cbAdBranch=?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, CB_NM);
			query.setParameter(2, CB_AD_BRNCH);
			query.setParameter(3, CB_AD_CMPNY);
            return (LocalApCheckBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApCheckBatchHome.findByDirectCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findByDirectCbName(java.lang.String CB_NM, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenCbAll(java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbStatus='OPEN' AND cb.cbAdBranch = ?1 AND cb.cbAdCompany = ?2");
			query.setParameter(1, CB_AD_BRNCH);
			query.setParameter(2, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findOpenCbAll(java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenCbByCbType(java.lang.String CB_TYP, java.lang.Integer CB_AD_BRNCH,
			java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbType=?1 AND cb.cbStatus='OPEN' AND cb.cbAdBranch = ?2 AND cb.cbAdCompany = ?3");
			query.setParameter(1, CB_TYP);
			query.setParameter(2, CB_AD_BRNCH);
			query.setParameter(3, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findOpenCbByCbType(java.lang.String CB_TYP, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenCbByCbTypeDepartment(java.lang.String CB_TYP, java.lang.String VB_DPRTMNT,
			java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(cb) FROM ApCheckBatch cb WHERE cb.cbType=?1 AND cb.cbDepartment=?2 AND cb.cbStatus='OPEN' AND cb.cbAdBranch = ?3 AND cb.cbAdCompany = ?4");
			query.setParameter(1, CB_TYP);
			query.setParameter(2, VB_DPRTMNT);
			query.setParameter(3, CB_AD_BRNCH);
			query.setParameter(4, CB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApCheckBatchHome.findOpenCbByCbTypeDepartment(java.lang.String CB_TYP, java.lang.String VB_DPRTMNT, java.lang.Integer CB_AD_BRNCH, java.lang.Integer CB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getCbByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalApCheckBatch create(Integer AP_CB_CODE, String CB_NM, String CB_DESC, String CB_STATUS,
                                    String CB_TYP, Date CB_DT_CRTD, String CB_CRTD_BY, String CB_DPRTMNT, Integer CB_AD_BRNCH,
                                    Integer CB_AD_CMPNY) throws CreateException {
		try {

			LocalApCheckBatch entity = new LocalApCheckBatch();

			Debug.print("ApCheckBatchBean create");
			entity.setCbCode(AP_CB_CODE);
			entity.setCbName(CB_NM);
			entity.setCbDescription(CB_DESC);
			entity.setCbStatus(CB_STATUS);
			entity.setCbType(CB_TYP);
			entity.setCbDateCreated(CB_DT_CRTD);
			entity.setCbCreatedBy(CB_CRTD_BY);
			entity.setCbDepartment(CB_DPRTMNT);
			entity.setCbAdBranch(CB_AD_BRNCH);
			entity.setCbAdCompany(CB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApCheckBatch create(String CB_NM, String CB_DESC, String CB_STATUS, String CB_TYP,
                                    Date CB_DT_CRTD, String CB_CRTD_BY, String CB_DPRTMNT, Integer CB_AD_BRNCH, Integer CB_AD_CMPNY)
			throws CreateException {
		try {

			LocalApCheckBatch entity = new LocalApCheckBatch();

			Debug.print("ApCheckBatchBean create");
			entity.setCbName(CB_NM);
			entity.setCbDescription(CB_DESC);
			entity.setCbStatus(CB_STATUS);
			entity.setCbType(CB_TYP);
			entity.setCbDateCreated(CB_DT_CRTD);
			entity.setCbCreatedBy(CB_CRTD_BY);
			entity.setCbDepartment(CB_DPRTMNT);
			entity.setCbAdBranch(CB_AD_BRNCH);
			entity.setCbAdCompany(CB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}