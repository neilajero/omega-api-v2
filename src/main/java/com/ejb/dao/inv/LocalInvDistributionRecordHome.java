package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvDistributionRecord;
import com.util.Debug;

@Stateless
public class LocalInvDistributionRecordHome {

	public static final String JNDI_NAME = "LocalInvDistributionRecordHome!com.ejb.inv.LocalInvDistributionRecordHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvDistributionRecordHome() {
	}

	// FINDER METHODS

	public LocalInvDistributionRecord findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvDistributionRecord entity = (LocalInvDistributionRecord) em
					.find(new LocalInvDistributionRecord(), pk);
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

	public LocalInvDistributionRecord findByDrClassAndAdjCode(java.lang.String DR_CLSS, java.lang.Integer INV_ADJ_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvDistributionRecord dr WHERE dr.drClass=?1 AND dr.invAdjustment.adjCode=?2 AND dr.drAdCompany=?3");
			query.setParameter(1, DR_CLSS);
			query.setParameter(2, INV_ADJ_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return (LocalInvDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvDistributionRecordHome.findByDrClassAndAdjCode(java.lang.String DR_CLSS, java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByDrClassAndAdjCode(java.lang.String DR_CLSS, java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvDistributionRecord findByAdjCodeCredit(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvDistributionRecord dr WHERE dr.invAdjustment.adjCode=?1 AND dr.drAdCompany=?2 and dr.drDebit=0");
			query.setParameter(1, INV_ADJ_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return (LocalInvDistributionRecord) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvDistributionRecordHome.findByAdjCodeCredit(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByAdjCodeCredit(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByAdjCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE dr.drImported = 0 AND adj.adjCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, INV_ADJ_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByAdjCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByAtrCode(java.lang.Integer ATR_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAssemblyTransfer atr, IN(atr.invDistributionRecords) dr WHERE dr.drImported = 0 AND atr.atrCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, ATR_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByAtrCode(java.lang.Integer ATR_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByBuaCode(java.lang.Integer BUA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBuildUnbuildAssembly bua, IN(bua.invDistributionRecords) dr WHERE dr.drImported = 0 AND bua.buaCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, BUA_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByBuaCode(java.lang.Integer BUA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByOhCode(java.lang.Integer OH_CODE, java.lang.Integer OH_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvOverhead oh, IN(oh.invDistributionRecords) dr WHERE dr.drImported = 0 AND oh.ohCode = ?1 AND oh.ohAdCompany = ?2");
			query.setParameter(1, OH_CODE);
			query.setParameter(2, OH_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByOhCode(java.lang.Integer OH_CODE, java.lang.Integer OH_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrBySiCode(java.lang.Integer SI_CODE, java.lang.Integer SI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockIssuance si, IN(si.invDistributionRecords) dr WHERE dr.drImported = 0 AND si.siCode = ?1 AND si.siAdCompany = ?2");
			query.setParameter(1, SI_CODE);
			query.setParameter(2, SI_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrBySiCode(java.lang.Integer SI_CODE, java.lang.Integer SI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockTransfer st, IN(st.invDistributionRecords) dr WHERE dr.drImported = 0 AND st.stCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, INV_ST_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByBstCode(java.lang.Integer BST_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvDistributionRecord dr WHERE dr.drImported = 0 AND dr.invBranchStockTransfer.bstCode = ?1 AND dr.drAdCompany=?2");
			query.setParameter(1, BST_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByBstCode(java.lang.Integer BST_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAdjCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE adj.adjCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, INV_ADJ_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByAdjCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBuaCode(java.lang.Integer BUA_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBuildUnbuildAssembly bua, IN(bua.invDistributionRecords) dr WHERE bua.buaCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, BUA_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByBuaCode(java.lang.Integer BUA_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByOhCode(java.lang.Integer OH_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvOverhead oh, IN(oh.invDistributionRecords) dr WHERE oh.ohCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, OH_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByOhCode(java.lang.Integer OH_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySiCode(java.lang.Integer SI_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockIssuance si, IN(si.invDistributionRecords) dr WHERE si.siCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, SI_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findBySiCode(java.lang.Integer SI_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAtrCode(java.lang.Integer ATR_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAssemblyTransfer atr, IN(atr.invDistributionRecords) dr WHERE atr.atrCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, ATR_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByAtrCode(java.lang.Integer ATR_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockTransfer st, IN(st.invDistributionRecords) dr WHERE st.stCode =?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, INV_ST_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByStCode(java.lang.Integer INV_ST_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDateRangeAndCoaAccountNumber(java.util.Date ADJ_DT_FRM,
			java.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE adj.adjVoid=0 AND adj.adjPosted=0 AND adj.adjDate>=?1 AND adj.adjDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND adj.adjAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, ADJ_DT_FRM);
			query.setParameter(2, ADJ_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, ADJ_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAdjByDateRangeAndCoaAccountNumber(java.com.util.Date ADJ_DT_FRM, java.com.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBuaByDateRangeAndCoaAccountNumber(java.util.Date BUA_DT_FRM,
			java.util.Date BUA_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BUA_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBuildUnbuildAssembly bua, IN(bua.invDistributionRecords) dr WHERE bua.buaVoid=0 AND bua.buaPosted=0 AND bua.buaDate>=?1 AND bua.buaDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND bua.buaAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, BUA_DT_FRM);
			query.setParameter(2, BUA_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, BUA_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBuaByDateRangeAndCoaAccountNumber(java.com.util.Date BUA_DT_FRM, java.com.util.Date BUA_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BUA_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedOhByDateRangeAndCoaAccountNumber(java.util.Date OH_DT_FRM,
			java.util.Date OH_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer OH_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvOverhead oh, IN(oh.invDistributionRecords) dr WHERE oh.ohPosted=0 AND oh.ohDate>=?1 AND oh.ohDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND oh.ohAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, OH_DT_FRM);
			query.setParameter(2, OH_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, OH_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedOhByDateRangeAndCoaAccountNumber(java.com.util.Date OH_DT_FRM, java.com.util.Date OH_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer OH_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedSiByDateRangeAndCoaAccountNumber(java.util.Date SI_DT_FRM,
			java.util.Date SI_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer SI_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockIssuance si, IN(si.invDistributionRecords) dr WHERE si.siVoid=0 AND si.siPosted=0 AND si.siDate>=?1 AND si.siDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND si.siAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, SI_DT_FRM);
			query.setParameter(2, SI_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, SI_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedSiByDateRangeAndCoaAccountNumber(java.com.util.Date SI_DT_FRM, java.com.util.Date SI_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer SI_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAtrByDateRangeAndCoaAccountNumber(java.util.Date ATR_DT_FRM,
			java.util.Date ATR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ATR_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAssemblyTransfer atr, IN(atr.invDistributionRecords) dr WHERE atr.atrVoid=0 AND atr.atrPosted=0 AND atr.atrDate>=?1 AND atr.atrDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND atr.atrAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, ATR_DT_FRM);
			query.setParameter(2, ATR_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, ATR_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAtrByDateRangeAndCoaAccountNumber(java.com.util.Date ATR_DT_FRM, java.com.util.Date ATR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ATR_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByDateRangeAndCoaAccountNumber(java.util.Date ST_DT_FRM,
			java.util.Date ST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ST_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockTransfer st, IN(st.invDistributionRecords) dr WHERE st.stPosted=0 AND st.stDate>=?1 AND st.stDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND st.stAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, ST_DT_FRM);
			query.setParameter(2, ST_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, ST_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedStByDateRangeAndCoaAccountNumber(java.com.util.Date ST_DT_FRM, java.com.util.Date ST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBstCode(java.lang.Integer BST_CODE, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBranchStockTransfer bst, IN(bst.invDistributionRecords) dr WHERE bst.bstCode = ?1 AND dr.drAdCompany=?2 ORDER BY dr.drDebit DESC");
			query.setParameter(1, BST_CODE);
			query.setParameter(2, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findByBstCode(java.lang.Integer BST_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE adj.adjPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND adj.adjAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAdjByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBuaByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer BUA_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBuildUnbuildAssembly bua, IN(bua.invDistributionRecords) dr WHERE bua.buaPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND bua.buaAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BUA_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBuaByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer BUA_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedSiByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer SI_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockIssuance si, IN(si.invDistributionRecords) dr WHERE si.siPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND si.siAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, SI_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedSiByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer SI_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAtrByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer ATR_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAssemblyTransfer atr, IN(atr.invDistributionRecords) dr WHERE atr.atrPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND atr.atrAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ATR_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAtrByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer ATR_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer ST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockTransfer st, IN(st.invDistributionRecords) dr WHERE st.stPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND st.stAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, ST_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedStByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBstByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBranchStockTransfer bst, IN(bst.invDistributionRecords) dr WHERE bst.bstPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND bst.bstAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BST_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBstByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedOhByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE,
			java.lang.Integer OH_AD_BRNCH, java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvOverhead oh, IN(oh.invDistributionRecords) dr WHERE oh.ohPosted=0 AND dr.invChartOfAccount.coaCode=?1 AND oh.ohAdBranch = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, OH_AD_BRNCH);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedOhByDrCoaAccountNumberAndBrCode(java.lang.Integer COA_CODE, java.lang.Integer OH_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findImportableDrByDrReversedAndAdjCode(byte DR_RVRSD, java.lang.Integer INV_ADJ_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE dr.drImported = 0 AND dr.drReversal = ?1 AND adj.adjCode = ?2 AND dr.drAdCompany = ?3");
			query.setParameter(1, DR_RVRSD);
			query.setParameter(2, INV_ADJ_CODE);
			query.setParameter(3, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(byte DR_RVRSD, java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBstByDateRangeAndCoaAccountNumber(java.util.Date BST_DT_FRM,
			java.util.Date BST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BST_AD_BRNCH,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBranchStockTransfer bst, IN(bst.invDistributionRecords) dr WHERE bst.bstVoid=0 AND bst.bstPosted=0 AND bst.bstDate>=?1 AND bst.bstDate<=?2 AND dr.invChartOfAccount.coaCode=?3 AND bst.bstAdBranch=?4 AND dr.drAdCompany=?5");
			query.setParameter(1, BST_DT_FRM);
			query.setParameter(2, BST_DT_TO);
			query.setParameter(3, COA_CODE);
			query.setParameter(4, BST_AD_BRNCH);
			query.setParameter(5, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBstByDateRangeAndCoaAccountNumber(java.com.util.Date BST_DT_FRM, java.com.util.Date BST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByDateAndCoaAccountNumber(java.util.Date ADJ_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAdjustment adj, IN(adj.invDistributionRecords) dr WHERE adj.adjVoid=0 AND adj.adjPosted=0 AND adj.adjDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND adj.adjAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, ADJ_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, ADJ_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAdjByDateAndCoaAccountNumber(java.com.util.Date ADJ_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBuaByDateAndCoaAccountNumber(java.util.Date BUA_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer BUA_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBuildUnbuildAssembly bua, IN(bua.invDistributionRecords) dr WHERE bua.buaVoid=0 AND bua.buaPosted=0 AND bua.buaDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND bua.buaAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, BUA_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, BUA_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBuaByDateAndCoaAccountNumber(java.com.util.Date BUA_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BUA_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedOhByDateAndCoaAccountNumber(java.util.Date OH_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer OH_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvOverhead oh, IN(oh.invDistributionRecords) dr WHERE oh.ohPosted=0 AND oh.ohDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND oh.ohAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, OH_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, OH_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedOhByDateAndCoaAccountNumber(java.com.util.Date OH_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer OH_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedSiByDateAndCoaAccountNumber(java.util.Date SI_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer SI_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockIssuance si, IN(si.invDistributionRecords) dr WHERE si.siVoid=0 AND si.siPosted=0 AND si.siDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND si.siAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, SI_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, SI_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedSiByDateAndCoaAccountNumber(java.com.util.Date SI_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer SI_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAtrByDateAndCoaAccountNumber(java.util.Date ATR_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer ATR_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvAssemblyTransfer atr, IN(atr.invDistributionRecords) dr WHERE atr.atrVoid=0 AND atr.atrPosted=0 AND atr.atrDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND atr.atrAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, ATR_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, ATR_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedAtrByDateAndCoaAccountNumber(java.com.util.Date ATR_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ATR_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedStByDateAndCoaAccountNumber(java.util.Date ST_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvStockTransfer st, IN(st.invDistributionRecords) dr WHERE st.stPosted=0 AND st.stDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND st.stAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, ST_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, ST_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedStByDateAndCoaAccountNumber(java.com.util.Date ST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer ST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBstByDateAndCoaAccountNumber(java.util.Date BST_DT_TO,
			java.lang.Integer COA_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dr) FROM InvBranchStockTransfer bst, IN(bst.invDistributionRecords) dr WHERE bst.bstVoid=0 AND bst.bstPosted=0 AND bst.bstDate<=?1 AND dr.invChartOfAccount.coaCode=?2 AND bst.bstAdBranch=?3 AND dr.drAdCompany=?4");
			query.setParameter(1, BST_DT_TO);
			query.setParameter(2, COA_CODE);
			query.setParameter(3, BST_AD_BRNCH);
			query.setParameter(4, DR_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvDistributionRecordHome.findUnpostedBstByDateAndCoaAccountNumber(java.com.util.Date BST_DT_TO, java.lang.Integer COA_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalInvDistributionRecord create(java.lang.Integer INV_DR_CODE, short DR_LN,
                                             java.lang.String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, byte DR_IMPRTD, Integer DR_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvDistributionRecord entity = new LocalInvDistributionRecord();

			Debug.print("InvDistributionRecordBean create");
			entity.setDrCode(INV_DR_CODE);
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrDebit(DR_DBT);
			entity.setDrAmount(DR_AMNT);
			entity.setDrReversal(DR_RVRSL);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvDistributionRecord create(short DR_LN, java.lang.String DR_CLSS, byte DR_DBT,
                                             double DR_AMNT, byte DR_RVRSL, byte DR_IMPRTD, Integer DR_AD_CMPNY) throws CreateException {
		try {

			LocalInvDistributionRecord entity = new LocalInvDistributionRecord();

			Debug.print("InvDistributionRecordBean create");
			entity.setDrLine(DR_LN);
			entity.setDrClass(DR_CLSS);
			entity.setDrDebit(DR_DBT);
			entity.setDrAmount(DR_AMNT);
			entity.setDrReversal(DR_RVRSL);
			entity.setDrImported(DR_IMPRTD);
			entity.setDrAdCompany(DR_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}