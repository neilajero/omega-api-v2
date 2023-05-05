package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvDepreciationLedger;
import com.util.Debug;

@Stateless
public class LocalInvDepreciationLedgerHome {

	public static final String JNDI_NAME = "LocalInvDepreciationLedgerHome!com.ejb.inv.LocalInvDepreciationLedgerHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvDepreciationLedgerHome() {
	}

	// FINDER METHODS

	public LocalInvDepreciationLedger findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvDepreciationLedger entity = (LocalInvDepreciationLedger) em
					.find(new LocalInvDepreciationLedger(), pk);
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

	public LocalInvDepreciationLedger findExistingDLbyDateAndInvTag(java.util.Date DL_DT, java.lang.Integer TG_CD,
			java.lang.Integer TG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dl) FROM InvDepreciationLedger dl WHERE dl.dlDate=?1 AND dl.invTag.tgCode=?2 AND dl.dlAdCompany=?3");
			query.setParameter(1, DL_DT);
			query.setParameter(2, TG_CD);
			query.setParameter(3, TG_AD_CMPNY);
            return (LocalInvDepreciationLedger) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvDepreciationLedgerHome.findExistingDLbyDateAndInvTag(java.com.util.Date DL_DT, java.lang.Integer TG_CD, java.lang.Integer TG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDepreciationLedgerHome.findExistingDLbyDateAndInvTag(java.com.util.Date DL_DT, java.lang.Integer TG_CD, java.lang.Integer TG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getTgByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalInvDepreciationLedger create(Integer DL_CODE, java.util.Date DL_DT, Double DL_ACQSTN_CST,
                                             Double DL_DPRCTN_AMT, Double DL_MNTH_LF_SPN, Double DL_CRRNT_BLNC, Integer DL_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvDepreciationLedger entity = new LocalInvDepreciationLedger();

			Debug.print("InvDepreciationLedgerBean create");

			entity.setDlCode(DL_CODE);
			entity.setDlDate(DL_DT);
			entity.setDlAcquisitionCost(DL_ACQSTN_CST);
			entity.setDlDepreciationAmount(DL_DPRCTN_AMT);
			entity.setDlMonthLifeSpan(DL_MNTH_LF_SPN);
			entity.setDlCurrentBalance(DL_CRRNT_BLNC);
			entity.setDlAdCompany(DL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvDepreciationLedger create(java.util.Date DL_DT, Double DL_ACQSTN_CST,
                                             Double DL_DPRCTN_AMT, Double DL_MNTH_LF_SPN, Double DL_CRRNT_BLNC, Integer DL_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvDepreciationLedger entity = new LocalInvDepreciationLedger();

			Debug.print("InvDepreciationLedgerBean create");

			entity.setDlDate(DL_DT);
			entity.setDlAcquisitionCost(DL_ACQSTN_CST);
			entity.setDlDepreciationAmount(DL_DPRCTN_AMT);
			entity.setDlMonthLifeSpan(DL_MNTH_LF_SPN);
			entity.setDlCurrentBalance(DL_CRRNT_BLNC);
			entity.setDlAdCompany(DL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}