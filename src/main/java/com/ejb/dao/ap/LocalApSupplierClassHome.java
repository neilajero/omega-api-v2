package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApSupplierClass;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApSupplierClassHome {

	public static final String JNDI_NAME = "LocalApSupplierClassHome!com.ejb.ap.LocalApSupplierClassHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApSupplierClassHome() {
	}

	// FINDER METHODS

	public LocalApSupplierClass findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApSupplierClass entity = (LocalApSupplierClass) em
					.find(new LocalApSupplierClass(), pk);
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

	public java.util.Collection findEnabledScAll(java.lang.Integer SC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sc) FROM ApSupplierClass sc WHERE sc.scEnable = 1 AND sc.scAdCompany = ?1");
			query.setParameter(1, SC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierClassHome.findEnabledScAll(java.lang.Integer SC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findScAll(java.lang.Integer SC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sc) FROM ApSupplierClass sc WHERE sc.scAdCompany = ?1");
			query.setParameter(1, SC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierClassHome.findScAll(java.lang.Integer SC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByScGlCoaPayableAccount(java.lang.Integer COA_CODE, java.lang.Integer SC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sc) FROM ApSupplierClass sc WHERE sc.scGlCoaPayableAccount=?1 AND sc.scAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, SC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierClassHome.findByScGlCoaPayableAccount(java.lang.Integer COA_CODE, java.lang.Integer SC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByScGlCoaExpenseAccount(java.lang.Integer COA_CODE, java.lang.Integer SC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sc) FROM ApSupplierClass sc WHERE sc.scGlCoaExpenseAccount=?1 AND sc.scAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, SC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierClassHome.findByScGlCoaExpenseAccount(java.lang.Integer COA_CODE, java.lang.Integer SC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApSupplierClass findByScName(java.lang.String SC_NM, java.lang.Integer SC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sc) FROM ApSupplierClass sc WHERE sc.scName = ?1 AND sc.scAdCompany = ?2");
			query.setParameter(1, SC_NM);
			query.setParameter(2, SC_AD_CMPNY);
            return (LocalApSupplierClass) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApSupplierClassHome.findByScName(java.lang.String SC_NM, java.lang.Integer SC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApSupplierClassHome.findByScName(java.lang.String SC_NM, java.lang.Integer SC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getScByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalApSupplierClass create(Integer SC_CODE, String SC_NM, String SC_DESC,
                                       Integer SC_GL_COA_PYBL_ACCNT, Integer SC_GL_COA_EXPNS_ACCNT, double SC_INVSTR_BNS_RT,
                                       double SC_INVSTR_INT_RT, byte SC_ENBL, byte SC_LDGR, byte SC_INVT, byte SC_LN, byte SC_VT_RLF_VCHR_ITM,
                                       String SC_LST_MDFD_BY, Date SC_DT_LST_MDFD, java.lang.Integer SC_AD_CMPNY) throws CreateException {
		try {

			LocalApSupplierClass entity = new LocalApSupplierClass();

			Debug.print("ApSupplierClassBean create");
			entity.setScCode(SC_CODE);
			entity.setScName(SC_NM);
			entity.setScDescription(SC_DESC);
			entity.setScGlCoaPayableAccount(SC_GL_COA_PYBL_ACCNT);
			entity.setScGlCoaExpenseAccount(SC_GL_COA_EXPNS_ACCNT);
			entity.setScInvestorBonusRate(SC_INVSTR_BNS_RT);
			entity.setScInvestorInterestRate(SC_INVSTR_INT_RT);
			entity.setScEnable(SC_ENBL);
			entity.setScLedger(SC_LDGR);
			entity.setScIsInvestment(SC_INVT);
			entity.setScIsLoan(SC_LN);
			entity.setScIsVatReliefVoucherItem(SC_VT_RLF_VCHR_ITM);
			entity.setScLastModifiedBy(SC_LST_MDFD_BY);
			entity.setScDateLastModified(SC_DT_LST_MDFD);
			entity.setScAdCompany(SC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApSupplierClass create(String SC_NM, String SC_DESC, Integer SC_GL_COA_PYBL_ACCNT,
                                       Integer SC_GL_COA_EXPNS_ACCNT, double SC_INVSTR_BNS_RT, double SC_INVSTR_INT_RT, byte SC_ENBL, byte SC_LDGR,
                                       byte SC_INVT, byte SC_LN, byte SC_VT_RLF_VCHR_ITM, String SC_LST_MDFD_BY, Date SC_DT_LST_MDFD,
                                       java.lang.Integer SC_AD_CMPNY) throws CreateException {
		try {

			LocalApSupplierClass entity = new LocalApSupplierClass();

			Debug.print("ApSupplierClassBean create");
			entity.setScName(SC_NM);
			entity.setScDescription(SC_DESC);
			entity.setScGlCoaPayableAccount(SC_GL_COA_PYBL_ACCNT);
			entity.setScGlCoaExpenseAccount(SC_GL_COA_EXPNS_ACCNT);
			entity.setScInvestorBonusRate(SC_INVSTR_BNS_RT);
			entity.setScInvestorInterestRate(SC_INVSTR_INT_RT);
			entity.setScEnable(SC_ENBL);
			entity.setScLedger(SC_LDGR);
			entity.setScIsInvestment(SC_INVT);
			entity.setScIsLoan(SC_LN);
			entity.setScIsVatReliefVoucherItem(SC_VT_RLF_VCHR_ITM);
			entity.setScLastModifiedBy(SC_LST_MDFD_BY);
			entity.setScDateLastModified(SC_DT_LST_MDFD);
			entity.setScAdCompany(SC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}