package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArReceiptCreditBalance;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.util.Debug;

@Stateless
public class LocalArReceiptCreditBalanceHome {

	public static final String JNDI_NAME = "LocalArReceiptCreditBalanceHome!com.ejb.ar.LocalArReceiptCreditBalanceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArReceiptCreditBalanceHome() {
	}

	// FINDER METHODS

	public LocalArReceiptCreditBalance findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArReceiptCreditBalance entity = (LocalArReceiptCreditBalance) em
					.find(new LocalArReceiptCreditBalance(), pk);
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

	// OTHER METHODS

	// CREATE METHODS

	public LocalArReceiptCreditBalance create(Integer RCB_CODE, Integer RCB_ADJ_CODE, Integer RCB_RCT_CODE)
			throws CreateException {
		try {

			LocalArReceiptCreditBalance entity = new LocalArReceiptCreditBalance();

			Debug.print("ArReceiptCreditBalance create");

			entity.setRcbCode(RCB_CODE);
			entity.setRcbAdjCode(RCB_ADJ_CODE);
			entity.setRcbRctCode(RCB_RCT_CODE);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceiptCreditBalance create(Integer RCB_ADJ_CODE, Integer RCB_RCT_CODE)
			throws CreateException {
		try {

			LocalArReceiptCreditBalance entity = new LocalArReceiptCreditBalance();

			Debug.print("ArReceiptCreditBalance create");

			entity.setRcbAdjCode(RCB_ADJ_CODE);
			entity.setRcbRctCode(RCB_RCT_CODE);
			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}