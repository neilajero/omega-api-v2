package com.ejb.dao.ad;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBankAccount;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBankAccountHome {

	public static final String JNDI_NAME = "LocalAdBankAccountHome!com.ejb.ad.LocalAdBankAccountHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBankAccountHome() {
	}

	// FINDER METHODS

	public LocalAdBankAccount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBankAccount entity = (LocalAdBankAccount) em
					.find(new LocalAdBankAccount(), pk);
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

	public LocalAdBankAccount findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

		try {

			LocalAdBankAccount entity = (LocalAdBankAccount) em
					.findPerCompany(new LocalAdBankAccount(), pk, companyShortName);
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

	public java.util.Collection findEnabledBaAll(java.lang.Integer BA_AD_BRNCH, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba, IN(ba.adBranchBankAccounts)bba WHERE ba.baEnable = 1 AND bba.adBranch.brCode = ?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, BA_AD_BRNCH);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findEnabledBaAll(java.lang.Integer BA_AD_BRNCH, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledBaAll(java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baAdCompany = ?1");
			query.setParameter(1, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findEnabledBaAll(java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBaAll(java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baAdCompany = ?1");
			query.setParameter(1, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findBaAll(java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBankAccount findByBaName(java.lang.String BA_NM, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baName = ?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, BA_NM);
			query.setParameter(2, BA_AD_CMPNY);
            return (LocalAdBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBankAccountHome.findByBaName(java.lang.String BA_NM, java.lang.Integer BA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaName(java.lang.String BA_NM, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBankAccount findByBaName(java.lang.String BA_NM, java.lang.Integer BA_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baName = ?1 AND ba.baAdCompany = ?2", companyShortName);
			query.setParameter(1, BA_NM);
			query.setParameter(2, BA_AD_CMPNY);
			return (LocalAdBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaCashAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlCashAccount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaCashAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaOnAccountReceipt(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlOnAccountReceipt=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaOnAccountReceipt(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaUnappliedReceipt(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlUnappliedReceipt=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaUnappliedReceipt(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaBankChargeAccount(java.lang.Integer COA_CODE,
			java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlBankChargeAccount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaBankChargeAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaClearingAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlClearingAccount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaClearingAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaInterestAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlInterestAccount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaInterestAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaAdjustmentAccount(java.lang.Integer COA_CODE,
			java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlAdjustmentAccount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaAdjustmentAccount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaCashDiscount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlCashDiscount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaCashDiscount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaSalesDiscount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlSalesDiscount=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaSalesDiscount(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBaGlCoaUnappliedCheck(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baCoaGlUnappliedCheck=?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaGlCoaUnappliedCheck(java.lang.Integer COA_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledAndNotCashAccountBaAll(java.lang.Integer BA_AD_BRNCH,
			java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba, IN(ba.adBranchBankAccounts)bba WHERE ba.baEnable = 1 AND ba.baIsCashAccount = 0 AND bba.adBranch.brCode = ?1 AND ba.baAdCompany = ?2");
			query.setParameter(1, BA_AD_BRNCH);
			query.setParameter(2, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findEnabledAndNotCashAccountBaAll(java.lang.Integer BA_AD_BRNCH, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBankAccount findByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE,
			java.lang.Integer BA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba, IN(ba.adBranchBankAccounts)bba WHERE ba.baName = ?1 AND bba.adBranch.brCode = ?2 AND ba.baAdCompany = ?3");
			query.setParameter(1, BA_NM);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, BA_AD_CMPNY);
            return (LocalAdBankAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBankAccountHome.findByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findByBaNameAndBrCode(java.lang.String BA_NM, java.lang.Integer BR_CODE, java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBaByBaNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW,
			char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(ba) FROM AdBankAccount ba, IN(ba.adBranchBankAccounts) bba WHERE (bba.bbaDownloadStatus = ?3 OR bba.bbaDownloadStatus = ?4 OR bba.bbaDownloadStatus = ?5) AND bba.adBranch.brCode = ?1 AND bba.bbaAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findBaByBaNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledAndNotCashAccountBaAll(java.lang.Integer BA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ba) FROM AdBankAccount ba WHERE ba.baEnable = 1 AND ba.baIsCashAccount = 0 AND ba.baAdCompany = ?1");
			query.setParameter(1, BA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankAccountHome.findEnabledAndNotCashAccountBaAll(java.lang.Integer BA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getBaByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalAdBankAccount create(Integer BA_CODE, String BA_NM, String BA_DESC, String BA_ACCNT_TYP,
                                     String BA_ACCNT_NMBR, String BA_ACCNT_USE, Integer BA_COA_GL_CSH_ACCNT, Integer BA_COA_GL_ON_ACCNT_RCPT,
                                     Integer BA_COA_GL_UNPPLD_RCPT, Integer BA_COA_GL_BNK_CHRG_ACCNT, Integer BA_COA_GL_CLRNG_ACCNT,
                                     Integer BA_COA_GL_INTRST_ACCNT, Integer BA_COA_GL_ADJSTMNT_ACCNT, Integer BA_COA_GL_CSH_DSCNT,
                                     Integer BA_COA_GL_SLS_DSCNT, Integer BA_COA_GL_ADVNC_ACCNT, Integer BA_COA_GL_UNPPLD_CHK,
                                     double BA_FLT_BLNC, Date BA_LST_RCNCLD_DT, double BA_LST_RCNCLD_BLNC, String BA_NXT_CHK_NMBR, byte BA_ENBL,
                                     byte BA_ACCNT_NMBR_SHW, int BA_ACCNT_NMBR_TP, int BA_ACCNT_NMBR_LFT, byte BA_ACCNT_NM_SHW,
                                     int BA_ACCNT_NM_TP, int BA_ACCNT_NM_LFT, byte BA_NMBR_SHW, int BA_NMBR_TP, int BA_NMBR_LFT, byte BA_DT_SHW,
                                     int BA_DT_TP, int BA_DT_LFT, byte BA_PY_SHW, int BA_PY_TP, int BA_PY_LFT, byte BA_AMNT_SHW, int BA_AMNT_TP,
                                     int BA_AMNT_LFT, byte BA_WRD_AMNT_SHW, int BA_WRD_AMNT_TP, int BA_WRD_AMNT_LFT, byte BA_CRRNCY_SHW,
                                     int BA_CRRNCY_TP, int BA_CRRNCY_LFT, byte BA_ADDRSS_SHW, int BA_ADDRSS_TP, int BA_ADDRSS_LFT,
                                     byte BA_MM_SHW, int BA_MM_TP, int BA_MM_LFT, byte BA_DC_NMBR_SHW, int BA_DC_NMBR_TP, int BA_DC_NMBR_LFT,
                                     int BA_FNT_SZ, String BA_FNT_STYL, byte BA_IS_CSH_ACCNT, Integer BA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBankAccount entity = new LocalAdBankAccount();

			Debug.print("AdBankAccountBean create");

			entity.setBaCode(BA_CODE);
			entity.setBaName(BA_NM);
			entity.setBaDescription(BA_DESC);
			entity.setBaAccountType(BA_ACCNT_TYP);
			entity.setBaAccountNumber(BA_ACCNT_NMBR);
			entity.setBaAccountUse(BA_ACCNT_USE);
			entity.setBaCoaGlCashAccount(BA_COA_GL_CSH_ACCNT);
			entity.setBaCoaGlOnAccountReceipt(BA_COA_GL_ON_ACCNT_RCPT);
			entity.setBaCoaGlUnappliedReceipt(BA_COA_GL_UNPPLD_RCPT);
			entity.setBaCoaGlBankChargeAccount(BA_COA_GL_BNK_CHRG_ACCNT);
			entity.setBaCoaGlClearingAccount(BA_COA_GL_CLRNG_ACCNT);
			entity.setBaCoaGlInterestAccount(BA_COA_GL_INTRST_ACCNT);
			entity.setBaCoaGlAdjustmentAccount(BA_COA_GL_ADJSTMNT_ACCNT);
			entity.setBaCoaGlCashDiscount(BA_COA_GL_CSH_DSCNT);
			entity.setBaCoaGlSalesDiscount(BA_COA_GL_SLS_DSCNT);
			entity.setBaCoaGlAdvanceAccount(BA_COA_GL_ADVNC_ACCNT);
			entity.setBaCoaGlUnappliedCheck(BA_COA_GL_UNPPLD_CHK);
			entity.setBaFloatBalance(BA_FLT_BLNC);
			entity.setBaLastReconciledDate(BA_LST_RCNCLD_DT);
			entity.setBaLastReconciledBalance(BA_LST_RCNCLD_BLNC);
			entity.setBaNextCheckNumber(BA_NXT_CHK_NMBR);
			entity.setBaEnable(BA_ENBL);
			entity.setBaAccountNumberShow(BA_ACCNT_NMBR_SHW);
			entity.setBaAccountNumberTop(BA_ACCNT_NMBR_TP);
			entity.setBaAccountNumberLeft(BA_ACCNT_NMBR_LFT);
			entity.setBaAccountNameShow(BA_ACCNT_NM_SHW);
			entity.setBaAccountNameTop(BA_ACCNT_NM_TP);
			entity.setBaAccountNameLeft(BA_ACCNT_NM_LFT);
			entity.setBaNumberShow(BA_NMBR_SHW);
			entity.setBaNumberTop(BA_NMBR_TP);
			entity.setBaNumberLeft(BA_NMBR_LFT);
			entity.setBaDateShow(BA_DT_SHW);
			entity.setBaDateTop(BA_DT_TP);
			entity.setBaDateLeft(BA_DT_LFT);
			entity.setBaPayeeShow(BA_PY_SHW);
			entity.setBaPayeeTop(BA_PY_TP);
			entity.setBaPayeeLeft(BA_PY_LFT);
			entity.setBaAmountShow(BA_AMNT_SHW);
			entity.setBaAmountTop(BA_AMNT_TP);
			entity.setBaAmountLeft(BA_AMNT_LFT);
			entity.setBaWordAmountShow(BA_WRD_AMNT_SHW);
			entity.setBaWordAmountTop(BA_WRD_AMNT_TP);
			entity.setBaWordAmountLeft(BA_WRD_AMNT_LFT);
			entity.setBaCurrencyShow(BA_CRRNCY_SHW);
			entity.setBaCurrencyTop(BA_CRRNCY_TP);
			entity.setBaCurrencyLeft(BA_CRRNCY_LFT);
			entity.setBaAddressShow(BA_ADDRSS_SHW);
			entity.setBaAddressTop(BA_ADDRSS_TP);
			entity.setBaAddressLeft(BA_ADDRSS_LFT);
			entity.setBaMemoShow(BA_MM_SHW);
			entity.setBaMemoTop(BA_MM_TP);
			entity.setBaMemoLeft(BA_MM_LFT);
			entity.setBadocNumberShow(BA_DC_NMBR_SHW);
			entity.setBadocNumberTop(BA_DC_NMBR_TP);
			entity.setBadocNumberLeft(BA_DC_NMBR_LFT);
			entity.setBaFontSize(BA_FNT_SZ);
			entity.setBaFontStyle(BA_FNT_STYL);
			entity.setBaIsCashAccount(BA_IS_CSH_ACCNT);
			entity.setBaAdCompany(BA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

	public LocalAdBankAccount create(String BA_NM, String BA_DESC, String BA_ACCNT_TYP, String BA_ACCNT_NMBR,
                                     String BA_ACCNT_USE, Integer BA_COA_GL_CSH_ACCNT, Integer BA_COA_GL_ON_ACCNT_RCPT,
                                     Integer BA_COA_GL_UNPPLD_RCPT, Integer BA_COA_GL_BNK_CHRG_ACCNT, Integer BA_COA_GL_CLRNG_ACCNT,
                                     Integer BA_COA_GL_INTRST_ACCNT, Integer BA_COA_GL_ADJSTMNT_ACCNT, Integer BA_COA_GL_CSH_DSCNT,
                                     Integer BA_COA_GL_SLS_DSCNT, Integer BA_COA_GL_ADVNC_ACCNT, Integer BA_COA_GL_UNPPLD_CHK,
                                     double BA_FLT_BLNC, Date BA_LST_RCNCLD_DT, double BA_LST_RCNCLD_BLNC, String BA_NXT_CHK_NMBR, byte BA_ENBL,
                                     byte BA_ACCNT_NMBR_SHW, int BA_ACCNT_NMBR_TP, int BA_ACCNT_NMBR_LFT, byte BA_ACCNT_NM_SHW,
                                     int BA_ACCNT_NM_TP, int BA_ACCNT_NM_LFT, byte BA_NMBR_SHW, int BA_NMBR_TP, int BA_NMBR_LFT, byte BA_DT_SHW,
                                     int BA_DT_TP, int BA_DT_LFT, byte BA_PY_SHW, int BA_PY_TP, int BA_PY_LFT, byte BA_AMNT_SHW, int BA_AMNT_TP,
                                     int BA_AMNT_LFT, byte BA_WRD_AMNT_SHW, int BA_WRD_AMNT_TP, int BA_WRD_AMNT_LFT, byte BA_CRRNCY_SHW,
                                     int BA_CRRNCY_TP, int BA_CRRNCY_LFT, byte BA_ADDRSS_SHW, int BA_ADDRSS_TP, int BA_ADDRSS_LFT,
                                     byte BA_MM_SHW, int BA_MM_TP, int BA_MM_LFT, byte BA_DC_NMBR_SHW, int BA_DC_NMBR_TP, int BA_DC_NMBR_LFT,
                                     int BA_FNT_SZ, String BA_FNT_STYL, byte BA_IS_CSH_ACCNT, Integer BA_AD_CMPNY) throws CreateException {
		try {

			LocalAdBankAccount entity = new LocalAdBankAccount();

			Debug.print("AdBankAccountBean create");
			entity.setBaName(BA_NM);
			entity.setBaDescription(BA_DESC);
			entity.setBaAccountType(BA_ACCNT_TYP);
			entity.setBaAccountNumber(BA_ACCNT_NMBR);
			entity.setBaAccountUse(BA_ACCNT_USE);
			entity.setBaCoaGlCashAccount(BA_COA_GL_CSH_ACCNT);
			entity.setBaCoaGlOnAccountReceipt(BA_COA_GL_ON_ACCNT_RCPT);
			entity.setBaCoaGlUnappliedReceipt(BA_COA_GL_UNPPLD_RCPT);
			entity.setBaCoaGlBankChargeAccount(BA_COA_GL_BNK_CHRG_ACCNT);
			entity.setBaCoaGlClearingAccount(BA_COA_GL_CLRNG_ACCNT);
			entity.setBaCoaGlInterestAccount(BA_COA_GL_INTRST_ACCNT);
			entity.setBaCoaGlAdjustmentAccount(BA_COA_GL_ADJSTMNT_ACCNT);
			entity.setBaCoaGlCashDiscount(BA_COA_GL_CSH_DSCNT);
			entity.setBaCoaGlSalesDiscount(BA_COA_GL_SLS_DSCNT);
			entity.setBaCoaGlAdvanceAccount(BA_COA_GL_ADVNC_ACCNT);
			entity.setBaCoaGlUnappliedCheck(BA_COA_GL_UNPPLD_CHK);
			entity.setBaFloatBalance(BA_FLT_BLNC);
			entity.setBaLastReconciledDate(BA_LST_RCNCLD_DT);
			entity.setBaLastReconciledBalance(BA_LST_RCNCLD_BLNC);
			entity.setBaNextCheckNumber(BA_NXT_CHK_NMBR);
			entity.setBaEnable(BA_ENBL);
			entity.setBaAccountNumberShow(BA_ACCNT_NMBR_SHW);
			entity.setBaAccountNumberTop(BA_ACCNT_NMBR_TP);
			entity.setBaAccountNumberLeft(BA_ACCNT_NMBR_LFT);
			entity.setBaAccountNameShow(BA_ACCNT_NM_SHW);
			entity.setBaAccountNameTop(BA_ACCNT_NM_TP);
			entity.setBaAccountNameLeft(BA_ACCNT_NM_LFT);
			entity.setBaNumberShow(BA_NMBR_SHW);
			entity.setBaNumberTop(BA_NMBR_TP);
			entity.setBaNumberLeft(BA_NMBR_LFT);
			entity.setBaDateShow(BA_DT_SHW);
			entity.setBaDateTop(BA_DT_TP);
			entity.setBaDateLeft(BA_DT_LFT);
			entity.setBaPayeeShow(BA_PY_SHW);
			entity.setBaPayeeTop(BA_PY_TP);
			entity.setBaPayeeLeft(BA_PY_LFT);
			entity.setBaAmountShow(BA_AMNT_SHW);
			entity.setBaAmountTop(BA_AMNT_TP);
			entity.setBaAmountLeft(BA_AMNT_LFT);
			entity.setBaWordAmountShow(BA_WRD_AMNT_SHW);
			entity.setBaWordAmountTop(BA_WRD_AMNT_TP);
			entity.setBaWordAmountLeft(BA_WRD_AMNT_LFT);
			entity.setBaCurrencyShow(BA_CRRNCY_SHW);
			entity.setBaCurrencyTop(BA_CRRNCY_TP);
			entity.setBaCurrencyLeft(BA_CRRNCY_LFT);
			entity.setBaAddressShow(BA_ADDRSS_SHW);
			entity.setBaAddressTop(BA_ADDRSS_TP);
			entity.setBaAddressLeft(BA_ADDRSS_LFT);
			entity.setBaMemoShow(BA_MM_SHW);
			entity.setBaMemoTop(BA_MM_TP);
			entity.setBaMemoLeft(BA_MM_LFT);
			entity.setBadocNumberShow(BA_DC_NMBR_SHW);
			entity.setBadocNumberTop(BA_DC_NMBR_TP);
			entity.setBadocNumberLeft(BA_DC_NMBR_LFT);
			entity.setBaFontSize(BA_FNT_SZ);
			entity.setBaFontStyle(BA_FNT_STYL);
			entity.setBaIsCashAccount(BA_IS_CSH_ACCNT);
			entity.setBaAdCompany(BA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}

	}

}