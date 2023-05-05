package com.ejb.dao.cm;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.cm.LocalCmFundTransferReceipt;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalCmFundTransferReceiptHome {

	public static final String JNDI_NAME = "LocalCmFundTransferReceiptHome!com.ejb.cm.LocalCmFundTransferReceiptHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalCmFundTransferReceiptHome() {
	}

	// FINDER METHODS

	public LocalCmFundTransferReceipt findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalCmFundTransferReceipt entity = (LocalCmFundTransferReceipt) em
					.find(new LocalCmFundTransferReceipt(), pk);
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

	public java.util.Collection getFtrByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalCmFundTransferReceipt create(Integer FTR_CODE, double FTR_AMNT_DPSTD, Integer FTR_AD_CMPNY)
			throws CreateException {
		try {

			LocalCmFundTransferReceipt entity = new LocalCmFundTransferReceipt();

			Debug.print("CmFundTransferReceiptBean create");

			entity.setFtrCode(FTR_CODE);
			entity.setFtrAmountDeposited(FTR_AMNT_DPSTD);
			entity.setFtrAdCompany(FTR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalCmFundTransferReceipt create(double FTR_AMNT_DPSTD, Integer FTR_AD_CMPNY)
			throws CreateException {
		try {

			LocalCmFundTransferReceipt entity = new LocalCmFundTransferReceipt();

			Debug.print("CmFundTransferReceiptBean create");

			entity.setFtrAmountDeposited(FTR_AMNT_DPSTD);
			entity.setFtrAdCompany(FTR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}