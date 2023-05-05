package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArReceiptImportPreferenceLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArReceiptImportPreferenceLineHome {

	public static final String JNDI_NAME = "LocalArReceiptImportPreferenceLineHome!com.ejb.ar.LocalArReceiptImportPreferenceLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArReceiptImportPreferenceLineHome() {
	}

	// FINDER METHODS

	public LocalArReceiptImportPreferenceLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArReceiptImportPreferenceLine entity = (LocalArReceiptImportPreferenceLine) em
					.find(new LocalArReceiptImportPreferenceLine(), pk);
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

	public java.util.Collection getRilByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArReceiptImportPreferenceLine create(Integer RIL_CODE, String RIL_TYP, String RIL_NM,
                                                     String RIL_GL_ACCNT_NUM, String RIL_BNK_ACCNT_NM, short RIL_CLMN, String RIL_FILE, short RIL_AMNT_CLMN,
                                                     Integer RIL_AD_CMPNY) throws CreateException {
		try {

			LocalArReceiptImportPreferenceLine entity = new LocalArReceiptImportPreferenceLine();

			Debug.print("ArReceiptImportPreferenceLineBean create");

			entity.setRilCode(RIL_CODE);
			entity.setRilType(RIL_TYP);
			entity.setRilName(RIL_NM);
			entity.setRilGlAccountNumber(RIL_GL_ACCNT_NUM);
			entity.setRilBankAccountName(RIL_BNK_ACCNT_NM);
			entity.setRilColumn(RIL_CLMN);
			entity.setRilFile(RIL_FILE);
			entity.setRilAmountColumn(RIL_AMNT_CLMN);
			entity.setRilAdCompany(RIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArReceiptImportPreferenceLine create(String RIL_TYP, String RIL_NM, String RIL_GL_ACCNT_NUM,
                                                     String RIL_BNK_ACCNT_NM, short RIL_CLMN, String RIL_FILE, short RIL_AMNT_CLMN, Integer RIL_AD_CMPNY)
			throws CreateException {
		try {

			LocalArReceiptImportPreferenceLine entity = new LocalArReceiptImportPreferenceLine();

			Debug.print("ArReceiptImportPreferenceLineBean create");

			entity.setRilType(RIL_TYP);
			entity.setRilName(RIL_NM);
			entity.setRilGlAccountNumber(RIL_GL_ACCNT_NUM);
			entity.setRilBankAccountName(RIL_BNK_ACCNT_NM);
			entity.setRilColumn(RIL_CLMN);
			entity.setRilFile(RIL_FILE);
			entity.setRilAmountColumn(RIL_AMNT_CLMN);
			entity.setRilAdCompany(RIL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}