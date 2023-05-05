package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApPurchaseRequisitionLineHome {

	public static final String JNDI_NAME = "LocalApPurchaseRequisitionLineHome!com.ejb.ap.LocalApPurchaseRequisitionLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApPurchaseRequisitionLineHome() {
	}

	// FINDER METHODS

	public LocalApPurchaseRequisitionLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApPurchaseRequisitionLine entity = (LocalApPurchaseRequisitionLine) em
					.find(new LocalApPurchaseRequisitionLine(), pk);
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

	public LocalApPurchaseRequisitionLine findByPrCodeAndPrlLineAndBrCode(java.lang.Integer PR_CODE, short PRL_LN,
			java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(prl) FROM ApPurchaseRequisitionLine prl  WHERE prl.apPurchaseRequisition.prCode=?1 AND prl.prlLine=?2 AND prl.apPurchaseRequisition.prAdBranch = ?3 AND prl.apPurchaseRequisition.prAdCompany = ?4");
			query.setParameter(1, PR_CODE);
			query.setParameter(2, PRL_LN);
			query.setParameter(3, PR_AD_BRNCH);
			query.setParameter(4, PRL_AD_CMPNY);
            return (LocalApPurchaseRequisitionLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndPrlLineAndBrCode(java.lang.Integer PR_CODE, short PRL_LN, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndPrlLineAndBrCode(java.lang.Integer PR_CODE, short PRL_LN, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApPurchaseRequisitionLine findByPrCodeAndBrCode(java.lang.Integer PR_CODE,
			java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(prl) FROM ApPurchaseRequisitionLine prl  WHERE prl.apPurchaseRequisition.prCode=?1 AND prl.apPurchaseRequisition.prAdBranch = ?2 AND prl.apPurchaseRequisition.prAdCompany = ?3");
			query.setParameter(1, PR_CODE);
			query.setParameter(2, PR_AD_BRNCH);
			query.setParameter(3, PRL_AD_CMPNY);
            return (LocalApPurchaseRequisitionLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndBrCode(java.lang.Integer PR_CODE, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndBrCode(java.lang.Integer PR_CODE, java.lang.Integer PR_AD_BRNCH, java.lang.Integer PRL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApPurchaseRequisitionLine findByPrCodeAndPrlIiNameAndPrlIiLoc(java.lang.Integer PR_CODE,
			java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PRL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(prl) FROM ApPurchaseRequisitionLine prl  WHERE prl.apPurchaseRequisition.prCode=?1 AND prl.invItemLocation.invItem.iiName=?2 AND prl.invItemLocation.invLocation.locName = ?3 AND prl.apPurchaseRequisition.prAdCompany = ?4");
			query.setParameter(1, PR_CODE);
			query.setParameter(2, II_NM);
			query.setParameter(3, LOC_NM);
			query.setParameter(4, PRL_AD_CMPNY);
            return (LocalApPurchaseRequisitionLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndPrlIiNameAndPrlIiLoc(java.lang.Integer PR_CODE, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PRL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApPurchaseRequisitionLineHome.findByPrCodeAndPrlIiNameAndPrlIiLoc(java.lang.Integer PR_CODE, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PRL_AD_CMPNY)");
			throw ex;
		}
	}


	public java.util.Collection getPrlByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalApPurchaseRequisitionLine create(Integer PRL_CODE, short PRL_LN, double PRL_QTY,
                                                 double PRL_AMNT, Integer PRL_AD_CMPNY) throws CreateException {
		try {

			LocalApPurchaseRequisitionLine entity = new LocalApPurchaseRequisitionLine();

			Debug.print("ApPurchaseRequisitionLineBean create");
			entity.setPrlCode(PRL_CODE);
			entity.setPrlLine(PRL_LN);
			entity.setPrlQuantity(PRL_QTY);
			entity.setPrlAmount(PRL_AMNT);
			entity.setPrlAdCompany(PRL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApPurchaseRequisitionLine create(short PRL_LN, double PRL_QTY, double PRL_AMNT, String PRL_RMRKS,
                                                 Integer PRL_AD_CMPNY) throws CreateException {
		try {

			LocalApPurchaseRequisitionLine entity = new LocalApPurchaseRequisitionLine();

			Debug.print("ApPurchaseRequisitionLineBean create");
			entity.setPrlLine(PRL_LN);
			entity.setPrlQuantity(PRL_QTY);
			entity.setPrlAmount(PRL_AMNT);
			entity.setPrlRemarks(PRL_RMRKS);
			entity.setPrlAdCompany(PRL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}