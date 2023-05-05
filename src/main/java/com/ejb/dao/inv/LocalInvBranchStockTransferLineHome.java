package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvBranchStockTransferLine;
import com.util.Debug;

@Stateless
public class LocalInvBranchStockTransferLineHome {

	public static final String JNDI_NAME = "LocalInvBranchStockTransferLineHome!com.ejb.inv.LocalInvBranchStockTransferLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvBranchStockTransferLineHome() {
	}

	// FINDER METHODS

	public LocalInvBranchStockTransferLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvBranchStockTransferLine entity = (LocalInvBranchStockTransferLine) em
					.find(new LocalInvBranchStockTransferLine(), pk);
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

	public java.util.Collection findPostedIncomingBslByBstTransferOutNumberAndBstAdBranchAndBstAdCompany(
			java.lang.String BST_OUT_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstVoid = 0 AND bsl.invBranchStockTransfer.bstPosted = 1 AND bsl.invBranchStockTransfer.bstType = 'OUT' AND bsl.invBranchStockTransfer.bstTransferOutNumber = ?1 AND bsl.invBranchStockTransfer.bstAdBranch = ?2 AND bsl.invBranchStockTransfer.bstAdCompany = ?3");
			query.setParameter(1, BST_OUT_NMBR);
			query.setParameter(2, BST_AD_BRNCH);
			query.setParameter(3, BST_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findPostedIncomingBslByBstTransferOutNumberAndBstAdBranchAndBstAdCompany(java.lang.String BST_OUT_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBstTransferOrderNumberAndIiNameAndLocNameAndBrCode(
			java.lang.String BST_TRNSFR_ORDR_NMBR, java.lang.String INV_ITEM, java.lang.String INV_LOCATION,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstTransferOrderNumber=?1 AND bsl.invItemLocation.invItem.iiName=?2 AND bsl.invItemLocation.invLocation.locName=?3 AND bsl.invBranchStockTransfer.bstAdBranch=?4 AND bsl.bslAdCompany=?5");
			query.setParameter(1, BST_TRNSFR_ORDR_NMBR);
			query.setParameter(2, INV_ITEM);
			query.setParameter(3, INV_LOCATION);
			query.setParameter(4, BST_AD_BRNCH);
			query.setParameter(5, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findByBstTransferOrderNumberAndIiNameAndLocNameAndBrCode(java.lang.String BST_TRNSFR_ORDR_NMBR, java.lang.String INV_ITEM, java.lang.String INV_LOCATION, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOutBslByBstPostedAndIlCodeAndBrCode(byte BST_PSTD, java.lang.Integer INV_IL_CODE,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstType = 'OUT' AND bsl.invBranchStockTransfer.bstPosted=?1 AND bsl.invItemLocation.ilCode=?2 AND bsl.invBranchStockTransfer.bstAdBranch=?3 AND bsl.bslAdCompany=?4");
			query.setParameter(1, BST_PSTD);
			query.setParameter(2, INV_IL_CODE);
			query.setParameter(3, BST_AD_BRNCH);
			query.setParameter(4, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome. findOutBslByBstPostedAndIlCodeAndBrCode(byte BST_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInBslByBstPostedAndLocCodeAndIiCodeAndAdBrCode(byte BST_PSTD,
			java.lang.Integer LOC_CODE, java.lang.Integer II_CODE, java.lang.Integer BST_AD_BRNCH,
			java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstType = 'IN' AND bsl.invBranchStockTransfer.bstPosted=?1 AND bsl.invBranchStockTransfer.invLocation.locCode=?2 AND bsl.invItemLocation.invItem.iiCode=?3 AND bsl.invBranchStockTransfer.adBranch.brCode=?4 AND bsl.bslAdCompany=?5");
			query.setParameter(1, BST_PSTD);
			query.setParameter(2, LOC_CODE);
			query.setParameter(3, II_CODE);
			query.setParameter(4, BST_AD_BRNCH);
			query.setParameter(5, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findInBslByBstPostedAndLocCodeAndIiCodeAndAdBrCode(byte BST_PSTD, java.lang.Integer LOC_CODE, java.lang.Integer II_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBstByIiNameAndLocNameAndLessEqualDateAndBstAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date BST_DT, java.lang.Integer BST_AD_BRNCH,
			java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstPosted = 0 AND bsl.invBranchStockTransfer.bstVoid = 0 AND bsl.invItemLocation.invItem.iiName = ?1 AND bsl.invItemLocation.invLocation.locName = ?2 AND bsl.invBranchStockTransfer.bstDate <= ?3 AND bsl.invBranchStockTransfer.bstAdBranch = ?4 AND bsl.bslAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, BST_DT);
			query.setParameter(4, BST_AD_BRNCH);
			query.setParameter(5, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findUnpostedBstByIiNameAndLocNameAndLessEqualDateAndBstAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date BST_DT, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBstTransferOutNumberAndIiNameAndLocNameAndBrCode(
			java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.String INV_ITEM, java.lang.String INV_LOCATION,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstTransferOutNumber=?1 AND bsl.invItemLocation.invItem.iiName=?2 AND bsl.invItemLocation.invLocation.locName=?3 AND bsl.invBranchStockTransfer.bstAdBranch=?4 AND bsl.bslAdCompany=?5");
			query.setParameter(1, BST_TRNSFR_OUT_NMBR);
			query.setParameter(2, INV_ITEM);
			query.setParameter(3, INV_LOCATION);
			query.setParameter(4, BST_AD_BRNCH);
			query.setParameter(5, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findByBstTransferOutNumberAndIiNameAndLocNameAndBrCode(java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.String INV_ITEM, java.lang.String INV_LOCATION, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findQtyTransferedByBstTranferOutNumberAndIiCode(java.lang.String BST_TRNSFR_OUT_NMBR,
			java.lang.String II_NAME, java.lang.String LOC_NAME, java.lang.Integer BSL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstType = 'IN' AND bsl.invBranchStockTransfer.bstTransferOutNumber = ?1 AND bsl.invItemLocation.invItem.iiName = ?2 AND bsl.invItemLocation.invLocation.locName = ?3 AND bsl.bslAdCompany = ?4");
			query.setParameter(1, BST_TRNSFR_OUT_NMBR);
			query.setParameter(2, II_NAME);
			query.setParameter(3, LOC_NAME);
			query.setParameter(4, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findQtyTransferedByBstTranferOutNumberAndIiCode(java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.String II_NAME, java.lang.String LOC_NAME, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedBstByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstPosted = 0 AND bsl.invBranchStockTransfer.bstVoid = 0 AND bsl.invItemLocation.invLocation.locName = ?1 AND bsl.invBranchStockTransfer.bstAdBranch = ?2 AND bsl.bslAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, BST_AD_BRNCH);
			query.setParameter(3, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findUnpostedBstByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByBstCode(java.lang.Integer BST_CODE, java.lang.Integer BSL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstCode=?1 AND bsl.bslAdCompany=?2");
			query.setParameter(1, BST_CODE);
			query.setParameter(2, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findByBstCode(java.lang.Integer BST_CODE, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findInBslByBstTransferOutNumberAndItemLocAndAdBranchAndAdCompany(
			java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.Integer INV_IL_CODE, java.lang.Integer BST_AD_BRNCH,
			java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstVoid = 0 AND bsl.invBranchStockTransfer.bstPosted = 1 AND bsl.invBranchStockTransfer.bstType = 'IN' AND bsl.invBranchStockTransfer.bstTransferOutNumber=?1 AND bsl.invItemLocation.ilCode=?2 AND bsl.invBranchStockTransfer.bstAdBranch=?3 AND bsl.bslAdCompany=?4");
			query.setParameter(1, BST_TRNSFR_OUT_NMBR);
			query.setParameter(2, INV_IL_CODE);
			query.setParameter(3, BST_AD_BRNCH);
			query.setParameter(4, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findInBslByBstTransferOutNumberAndItemLocAndAdBranchAndAdCompany(java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.Integer INV_IL_CODE, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM,
			java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bsl) FROM InvBranchStockTransferLine bsl WHERE bsl.invBranchStockTransfer.bstVoid = 0 AND bsl.invItemLocation.invItem.iiCode=?1 AND bsl.invItemLocation.invLocation.locName = ?2 AND bsl.invBranchStockTransfer.bstAdBranch = ?3 AND bsl.bslAdCompany = ?4");
			query.setParameter(1, II_CODE);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, BST_AD_BRNCH);
			query.setParameter(4, BSL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferLineHome.findByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BSL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getBstlByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalInvBranchStockTransferLine create(java.lang.Integer BSL_CODE, double BSL_QTY,
                                                  double BSL_QTY_RCVD, double BSL_UNT_CST, double BSL_AMNT, java.lang.Integer BSL_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvBranchStockTransferLine entity = new LocalInvBranchStockTransferLine();

			Debug.print("InvBranchStockTransferLineBean create");

			entity.setBslCode(BSL_CODE);
			entity.setBslQuantity(BSL_QTY);
			entity.setBslQuantityReceived(BSL_QTY_RCVD);
			entity.setBslUnitCost(BSL_UNT_CST);
			entity.setBslAmount(BSL_AMNT);
			entity.setBslAdCompany(BSL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvBranchStockTransferLine create(double BSL_QTY, double BSL_QTY_RCVD, double BSL_UNT_CST,
                                                  double BSL_AMNT, java.lang.Integer BSL_AD_CMPNY) throws CreateException {
		try {

			LocalInvBranchStockTransferLine entity = new LocalInvBranchStockTransferLine();

			Debug.print("InvBranchStockTransferLineBean create");

			entity.setBslQuantity(BSL_QTY);
			entity.setBslQuantityReceived(BSL_QTY_RCVD);
			entity.setBslUnitCost(BSL_UNT_CST);
			entity.setBslAmount(BSL_AMNT);
			entity.setBslAdCompany(BSL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}