package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalBatch;
import com.util.Debug;

@Stateless
public class LocalGlJournalBatchHome {

	public static final String JNDI_NAME = "LocalGlJournalBatchHome!com.ejb.gl.LocalGlJournalBatchHome";

	@EJB
	public PersistenceBeanClass em;

	private String JB_NM = null;
	private String JB_DESC = null;
	private String JB_STATUS = null;
	private Date JB_DT_CRTD = null;
	private String JB_CRTD_BY = null;
	private Integer JB_AD_BRNCH = null;
	private Integer JB_AD_CMPNY = null;

	public LocalGlJournalBatchHome() {
	}

	// FINDER METHODS

	public LocalGlJournalBatch findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalBatch entity = (LocalGlJournalBatch) em
					.find(new LocalGlJournalBatch(), pk);
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

	public LocalGlJournalBatch findByJbName(java.lang.String JB_NM, java.lang.Integer JB_AD_BRNCH,
			java.lang.Integer JB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jb) FROM GlJournalBatch jb WHERE jb.jbName=?1 AND jb.jbAdBranch=?2 AND jb.jbAdCompany = ?3");
			query.setParameter(1, JB_NM);
			query.setParameter(2, JB_AD_BRNCH);
			query.setParameter(3, JB_AD_CMPNY);
            return (LocalGlJournalBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalBatchHome.findByJbName(java.lang.String JB_NM, java.lang.Integer JB_AD_BRNCH, java.lang.Integer JB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalBatchHome.findByJbName(java.lang.String JB_NM, java.lang.Integer JB_AD_BRNCH, java.lang.Integer JB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalBatch findByJbName(java.lang.String JB_NM, java.lang.Integer JB_AD_BRNCH,
											java.lang.Integer JB_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(jb) FROM GlJournalBatch jb WHERE jb.jbName=?1 AND jb.jbAdBranch=?2 AND jb.jbAdCompany = ?3",
					companyShortName);
			query.setParameter(1, JB_NM);
			query.setParameter(2, JB_AD_BRNCH);
			query.setParameter(3, JB_AD_CMPNY);
			return (LocalGlJournalBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findOpenJbAll(java.lang.Integer JB_AD_BRNCH, java.lang.Integer JB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jb) FROM GlJournalBatch jb WHERE jb.jbStatus='OPEN' AND jb.jbAdBranch = ?1 AND jb.jbAdCompany = ?2");
			query.setParameter(1, JB_AD_BRNCH);
			query.setParameter(2, JB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalBatchHome.findOpenJbAll(java.lang.Integer JB_AD_BRNCH, java.lang.Integer JB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJbByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalGlJournalBatchHome JbName(String JB_NM) {
		this.JB_NM = JB_NM;
		return this;
	}

	public LocalGlJournalBatchHome JbDescription(String JB_DESC) {
		this.JB_DESC = JB_DESC;
		return this;
	}

	public LocalGlJournalBatchHome JbStatus(String JB_STATUS) {
		this.JB_STATUS = JB_STATUS;
		return this;
	}

	public LocalGlJournalBatchHome JbDateCreated(Date JB_DT_CRTD) {
		this.JB_DT_CRTD = JB_DT_CRTD;
		return this;
	}

	public LocalGlJournalBatchHome JbCreatedBy(String JB_CRTD_BY) {
		this.JB_CRTD_BY = JB_CRTD_BY;
		return this;
	}

	public LocalGlJournalBatchHome JbAdBranch(Integer JB_AD_BRNCH) {
		this.JB_AD_BRNCH = JB_AD_BRNCH;
		return this;
	}

	public LocalGlJournalBatchHome JbAdCompany(Integer JB_AD_CMPNY) {
		this.JB_AD_CMPNY = JB_AD_CMPNY;
		return this;
	}

	public LocalGlJournalBatch buildJournalBatch(String companyShortName) throws CreateException {
		try {

			LocalGlJournalBatch entity = new LocalGlJournalBatch();

			Debug.print("GlJournalBatchBean buildJournalBatch");

			entity.setJbName(JB_NM);
			entity.setJbDescription(JB_DESC);
			entity.setJbStatus(JB_STATUS);
			entity.setJbDateCreated(JB_DT_CRTD);
			entity.setJbCreatedBy(JB_CRTD_BY);
			entity.setJbAdBranch(JB_AD_BRNCH);
			entity.setJbAdCompany(JB_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalBatch create(Integer JB_CODE, String JB_NM, String JB_DESC, String JB_STATUS,
                                      Date JB_DT_CRTD, String JB_CRTD_BY, Integer JB_AD_BRNCH, Integer JB_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalBatch entity = new LocalGlJournalBatch();

			Debug.print("GlJournalBatchBean create");

			entity.setJbCode(JB_CODE);
			entity.setJbName(JB_NM);
			entity.setJbDescription(JB_DESC);
			entity.setJbStatus(JB_STATUS);
			entity.setJbDateCreated(JB_DT_CRTD);
			entity.setJbCreatedBy(JB_CRTD_BY);
			entity.setJbAdBranch(JB_AD_BRNCH);
			entity.setJbAdCompany(JB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalBatch create(String JB_NM, String JB_DESC, String JB_STATUS, Date JB_DT_CRTD,
                                      String JB_CRTD_BY, Integer JB_AD_BRNCH, Integer JB_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalBatch entity = new LocalGlJournalBatch();

			Debug.print("GlJournalBatchBean create");

			entity.setJbName(JB_NM);
			entity.setJbDescription(JB_DESC);
			entity.setJbStatus(JB_STATUS);
			entity.setJbDateCreated(JB_DT_CRTD);
			entity.setJbCreatedBy(JB_CRTD_BY);
			entity.setJbAdBranch(JB_AD_BRNCH);
			entity.setJbAdCompany(JB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}