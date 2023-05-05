package com.ejb.dao.ad;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdDeleteAuditTrail;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdDeleteAuditTrailHome {

	public static final String JNDI_NAME = "LocalAdDeleteAuditTrailHome!com.ejb.ad.LocalAdDeleteAuditTrailHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdDeleteAuditTrailHome() {
	}

	// FINDER METHODS

	public LocalAdDeleteAuditTrail findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdDeleteAuditTrail entity = (LocalAdDeleteAuditTrail) em
					.find(new LocalAdDeleteAuditTrail(), pk);
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

	public java.util.Collection getDatByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalAdDeleteAuditTrail create(Integer DAT_CODE, String DAT_TYP, Date DAT_TXN_DT,
                                          String DAT_DCMNT_NMBR, String DAT_RFRNC_NMBR, double DAT_AMNT, String DAT_USR, Date DAT_DT,
                                          Integer DAT_AD_CMPNY) throws CreateException {
		try {

			LocalAdDeleteAuditTrail entity = new LocalAdDeleteAuditTrail();

			Debug.print("AdUserBean create");
			entity.setDatCode(DAT_CODE);
			entity.setDatType(DAT_TYP);
			entity.setDatTxnDate(DAT_TXN_DT);
			entity.setDatDocumentNumber(DAT_DCMNT_NMBR);
			entity.setDatReferenceNumber(DAT_RFRNC_NMBR);
			entity.setDatAmount(DAT_AMNT);
			entity.setDatUser(DAT_USR);
			entity.setDatDate(DAT_DT);
			entity.setDatAdCompany(DAT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdDeleteAuditTrail create(String DAT_TYP, Date DAT_TXN_DT, String DAT_DCMNT_NMBR,
                                          String DAT_RFRNC_NMBR, double DAT_AMNT, String DAT_USR, Date DAT_DT, Integer DAT_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdDeleteAuditTrail entity = new LocalAdDeleteAuditTrail();

			Debug.print("AdUserBean create");
			entity.setDatType(DAT_TYP);
			entity.setDatTxnDate(DAT_TXN_DT);
			entity.setDatDocumentNumber(DAT_DCMNT_NMBR);
			entity.setDatReferenceNumber(DAT_RFRNC_NMBR);
			entity.setDatAmount(DAT_AMNT);
			entity.setDatUser(DAT_USR);
			entity.setDatDate(DAT_DT);
			entity.setDatAdCompany(DAT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}