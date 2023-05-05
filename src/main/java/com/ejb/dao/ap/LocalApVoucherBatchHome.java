package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApVoucherBatchHome {

	public static final String JNDI_NAME = "LocalApVoucherBatchHome!com.ejb.ap.LocalApVoucherBatchHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApVoucherBatchHome() {
	}

	// FINDER METHODS

	public LocalApVoucherBatch findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApVoucherBatch entity = (LocalApVoucherBatch) em
					.find(new LocalApVoucherBatch(), pk);
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

	public LocalApVoucherBatch findByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH,
			java.lang.Integer VB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbName=?1 AND vb.vbAdBranch=?2 AND vb.vbAdCompany=?3");
			query.setParameter(1, VB_NM);
			query.setParameter(2, VB_AD_BRNCH);
			query.setParameter(3, VB_AD_CMPNY);
            return (LocalApVoucherBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherBatchHome.findByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApVoucherBatch findVoucherByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH,
			java.lang.Integer VB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbType='VOUCHER' AND vb.vbName=?1 AND vb.vbAdBranch=?2 AND vb.vbAdCompany=?3");
			query.setParameter(1, VB_NM);
			query.setParameter(2, VB_AD_BRNCH);
			query.setParameter(3, VB_AD_CMPNY);
            return (LocalApVoucherBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherBatchHome.findVoucherByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findVoucherByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApVoucherBatch findDebitMemoByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH,
			java.lang.Integer VB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbType='DEBIT MEMO' AND vb.vbName=?1 AND vb.vbAdBranch=?2 AND vb.vbAdCompany=?3");
			query.setParameter(1, VB_NM);
			query.setParameter(2, VB_AD_BRNCH);
			query.setParameter(3, VB_AD_CMPNY);
            return (LocalApVoucherBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApVoucherBatchHome.findDebitMemoByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findDebitMemoByVbName(java.lang.String VB_NM, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenVbByVbType(java.lang.String VB_TYP, java.lang.Integer VB_AD_BRNCH,
			java.lang.Integer VB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbType=?1 AND vb.vbStatus='OPEN' AND vb.vbAdBranch=?2 AND vb.vbAdCompany=?3");
			query.setParameter(1, VB_TYP);
			query.setParameter(2, VB_AD_BRNCH);
			query.setParameter(3, VB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findOpenVbByVbType(java.lang.String VB_TYP, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenVbByVbTypeDepartment(java.lang.String VB_TYP, java.lang.String VB_DPRTMNT,
			java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbType=?1 AND vb.vbDepartment=?2 AND vb.vbStatus='OPEN' AND vb.vbAdBranch=?3 AND vb.vbAdCompany=?4");
			query.setParameter(1, VB_TYP);
			query.setParameter(2, VB_DPRTMNT);
			query.setParameter(3, VB_AD_BRNCH);
			query.setParameter(4, VB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findOpenVbByVbTypeDepartment(java.lang.String VB_TYP, java.lang.String VB_DPRTMNT, java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenVbAll(java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vb) FROM ApVoucherBatch vb WHERE vb.vbStatus='OPEN' AND vb.vbAdBranch = ?1 AND vb.vbAdCompany=?2");
			query.setParameter(1, VB_AD_BRNCH);
			query.setParameter(2, VB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherBatchHome.findOpenVbAll(java.lang.Integer VB_AD_BRNCH, java.lang.Integer VB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getVbByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalApVoucherBatch create(Integer VB_CODE, String VB_NM, String VB_DESC, String VB_STATUS,
                                      String VB_TYP, Date VB_DT_CRTD, String VB_CRTD_BY, String VB_DPRTMNT, Integer VB_AD_BRNCH,
                                      Integer VB_AD_CMPNY) throws CreateException {
		try {

			LocalApVoucherBatch entity = new LocalApVoucherBatch();

			Debug.print("ApVoucherBatchBean create");
			entity.setVbCode(VB_CODE);
			entity.setVbName(VB_NM);
			entity.setVbDescription(VB_DESC);
			entity.setVbStatus(VB_STATUS);
			entity.setVbType(VB_TYP);
			entity.setVbDateCreated(VB_DT_CRTD);
			entity.setVbCreatedBy(VB_CRTD_BY);
			entity.setVbDepartment(VB_DPRTMNT);
			entity.setVbAdBranch(VB_AD_BRNCH);
			entity.setVbAdCompany(VB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApVoucherBatch create(String VB_NM, String VB_DESC, String VB_STATUS, String VB_TYP,
                                      Date VB_DT_CRTD, String VB_CRTD_BY, String VB_DPRTMNT, Integer VB_AD_BRNCH, Integer VB_AD_CMPNY)
			throws CreateException {
		try {

			LocalApVoucherBatch entity = new LocalApVoucherBatch();

			Debug.print("ApVoucherBatchBean create");
			entity.setVbName(VB_NM);
			entity.setVbDescription(VB_DESC);
			entity.setVbStatus(VB_STATUS);
			entity.setVbType(VB_TYP);
			entity.setVbDateCreated(VB_DT_CRTD);
			entity.setVbCreatedBy(VB_CRTD_BY);
			entity.setVbDepartment(VB_DPRTMNT);
			entity.setVbAdBranch(VB_AD_BRNCH);
			entity.setVbAdCompany(VB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}