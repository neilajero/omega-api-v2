package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.util.Debug;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Stateless
public class LocalInvBranchStockTransferHome {

    public static final String JNDI_NAME = "LocalInvBranchStockTransferHome!com.ejb.inv.LocalInvBranchStockTransferHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalInvBranchStockTransferHome() {
    }

    // FINDER METHODS

    public LocalInvBranchStockTransfer findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvBranchStockTransfer entity = (LocalInvBranchStockTransfer) em
                    .find(new LocalInvBranchStockTransfer(), pk);
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

    public LocalInvBranchStockTransfer findByBstNumberAndBrCode(java.lang.String BST_NMBR,
                                                                java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstNumber = ?1 AND bst.bstAdBranch = ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_NMBR);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return (LocalInvBranchStockTransfer) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvBranchStockTransferHome.findByBstNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findByBstNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvBranchStockTransfer findByBstNumberAndAdCompany(java.lang.String BST_NMBR,
                                                                   java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstNumber = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_NMBR);
            query.setParameter(2, BST_AD_CMPNY);
            return (LocalInvBranchStockTransfer) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvBranchStockTransferHome.findByBstNumberAndAdCompany(java.lang.String BST_NMBR, java.lang.Integer BST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findByBstNumberAndAdCompany(java.lang.String BST_NMBR, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvBranchStockTransfer findAllByBstNumberAndBrCode(java.lang.String BST_NMBR,
                                                                   java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstNumber = ?1 AND bst.bstAdBranch = ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_NMBR);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return (LocalInvBranchStockTransfer) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvBranchStockTransferHome.findAllByBstNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findAllByBstNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findBstByBstTransferOutNumberAndBstAdBranchAndBstAdCompany(
            java.lang.String BST_OUT_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstVoid = 0 AND bst.bstPosted = 1 AND bst.bstType = 'IN' AND bst.bstTransferOutNumber = ?1 AND bst.bstAdBranch = ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_OUT_NMBR);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findBstByBstTransferOutNumberAndBstAdBranchAndBstAdCompany(java.lang.String BST_OUT_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvBranchStockTransfer findInByBstOutNumberAndBrCode(java.lang.String BST_NMBR,
                                                                     java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstType = 'IN' AND bst.bstTransferOutNumber = ?1 AND bst.bstAdBranch = ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_NMBR);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return (LocalInvBranchStockTransfer) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvBranchStockTransferHome.findInByBstOutNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findInByBstOutNumberAndBrCode(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedBstByBstDateRange(java.util.Date BST_DT_FRM, java.util.Date BST_DT_TO,
                                                              java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstPosted = 0 AND bst.bstDate >= ?1 AND bst.bstDate <= ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_DT_FRM);
            query.setParameter(2, BST_DT_TO);
            query.setParameter(3, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findUnpostedBstByBstDateRange(java.com.util.Date BST_DT_FRM, java.com.util.Date BST_DT_TO, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findBstByAdBranchAndAdCompany(java.lang.Integer BST_AD_BRNCH,
                                                              java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstAdBranch = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findBstByAdBranchAndAdCompany(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvBranchStockTransfer findBstByBstNumberAndAdBranchAndAdCompany(java.lang.String BST_NMBR,
                                                                                 java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst  WHERE bst.bstVoid = 0 AND bst.bstNumber = ?1 AND bst.adBranch.brCode = ?2 AND bst.bstAdCompany = ?3");
            query.setParameter(1, BST_NMBR);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return (LocalInvBranchStockTransfer) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvBranchStockTransferHome.findBstByBstNumberAndAdBranchAndAdCompany(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findBstByBstNumberAndAdBranchAndAdCompany(java.lang.String BST_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedIncomingBstByAdBranchAndBstAdCompany(java.lang.Integer AD_BRNCH,
                                                                               java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstVoid = 0 AND bst.bstPosted = 1 AND bst.bstType = 'OUT' AND bst.adBranch.brCode= ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findPostedIncomingBstByAdBranchAndBstAdCompany(java.lang.Integer AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findBstByBstTypeAndAdBranchAndAdCompany(java.lang.String BST_TYP,
                                                                        java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstType=?1 AND bst.bstVoid = 0 AND (bst.bstApprovalStatus='N/A' OR bst.bstApprovalStatus='APPROVED') AND bst.bstLock=0 AND bst.bstPosted=1 AND bst.adBranch.brCode=?2 AND bst.bstAdCompany=?3");
            query.setParameter(1, BST_TYP);
            query.setParameter(2, BST_AD_BRNCH);
            query.setParameter(3, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findBstByBstTypeAndAdBranchAndAdCompany(java.lang.String BST_TYP, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findBstByBstTypeAndTransferOrderNumberAndAdBranchAndAdCompany(java.lang.String BST_TYP,
                                                                                              java.lang.String BST_TRNSFR_ORDR_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstType=?1 AND bst.bstTransferOrderNumber=?2 AND bst.bstVoid = 0 AND bst.bstLock=0 AND bst.adBranch.brCode=?3 AND bst.bstAdCompany=?4");
            query.setParameter(1, BST_TYP);
            query.setParameter(2, BST_TRNSFR_ORDR_NMBR);
            query.setParameter(3, BST_AD_BRNCH);
            query.setParameter(4, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findBstByBstTypeAndTransferOrderNumberAndAdBranchAndAdCompany(java.lang.String BST_TYP, java.lang.String BST_TRNSFR_ORDR_NMBR, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftBstOrderByBrCode(java.lang.Integer BST_AD_BRNCH,
                                                          java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE (bst.bstType = 'ORDER' OR bst.bstType = 'REGULAR' OR bst.bstType = 'EMERGENCY') AND bst.bstApprovalStatus IS NULL AND bst.bstAdBranch = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findDraftBstOrderByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftBstOutByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstType = 'OUT' AND bst.bstApprovalStatus IS NULL AND bst.bstAdBranch = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findDraftBstOutByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftBstInByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstType = 'IN' AND bst.bstApprovalStatus IS NULL AND bst.bstAdBranch = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findDraftBstInByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedBstOrderByBrCode(java.lang.Integer BST_AD_BRNCH,
                                                           java.lang.Integer BST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(bst) FROM InvBranchStockTransfer bst WHERE bst.bstType = 'ORDER' AND bst.bstApprovalStatus = 'N/A' AND bst.bstPosted = 1 AND bst.bstAdBranch = ?1 AND bst.bstAdCompany = ?2");
            query.setParameter(1, BST_AD_BRNCH);
            query.setParameter(2, BST_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvBranchStockTransferHome.findPostedBstOrderByBrCode(java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getBstByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
                                                 java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            if (LIMIT > 0) {
                query.setMaxResults(LIMIT);
            }
            query.setFirstResult(OFFSET);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    // OTHER METHODS

    // CREATE METHODS

    public LocalInvBranchStockTransfer create(java.lang.Integer BST_CODE, java.util.Date BST_DT,
                                              java.lang.String BST_TYP, java.lang.String BST_NMBR, java.lang.String BST_TRNSFR_OUT_NMBR,
                                              java.lang.String BST_TRNSFR_ORDER_NMBR, java.lang.String BST_DSCRPTN, java.lang.String BST_APPRVL_STATUS,
                                              byte BST_PSTD, java.lang.String BST_RSN_FR_RJCTN, java.lang.String BST_CRTD_BY, java.util.Date BST_DT_CRTD,
                                              java.lang.String BST_LST_MDFD_BY, java.util.Date BST_DT_LST_MDFD, java.lang.String BST_APPRVD_RJCTD_BY,
                                              java.util.Date BST_DT_APPRVD_RJCTD, java.lang.String BST_PSTD_BY, java.util.Date BST_DT_PSTD, byte BST_LCK,
                                              byte BST_VOID, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws CreateException {
        try {

            LocalInvBranchStockTransfer entity = new LocalInvBranchStockTransfer();

            Debug.print("InvBranchStockTransferBean create");

            entity.setBstCode(BST_CODE);
            entity.setBstDate(BST_DT);
            entity.setBstType(BST_TYP);
            entity.setBstNumber(BST_NMBR);
            entity.setBstTransferOutNumber(BST_TRNSFR_OUT_NMBR);
            entity.setBstTransferOrderNumber(BST_TRNSFR_ORDER_NMBR);
            entity.setBstDescription(BST_DSCRPTN);
            entity.setBstApprovalStatus(BST_APPRVL_STATUS);
            entity.setBstPosted(BST_PSTD);
            entity.setBstReasonForRejection(BST_RSN_FR_RJCTN);
            entity.setBstCreatedBy(BST_CRTD_BY);
            entity.setBstDateCreated(BST_DT_CRTD);
            entity.setBstLastModifiedBy(BST_LST_MDFD_BY);
            entity.setBstDateLastModified(BST_DT_LST_MDFD);
            entity.setBstApprovedRejectedBy(BST_APPRVD_RJCTD_BY);
            entity.setBstDateApprovedRejected(BST_DT_APPRVD_RJCTD);
            entity.setBstPostedBy(BST_PSTD_BY);
            entity.setBstDatePosted(BST_DT_PSTD);
            entity.setBstLock(BST_LCK);
            entity.setBstVoid(BST_VOID);
            entity.setBstAdBranch(BST_AD_BRNCH);
            entity.setBstAdCompany(BST_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvBranchStockTransfer create(java.util.Date BST_DT, java.lang.String BST_TYP,
                                              java.lang.String BST_NMBR, java.lang.String BST_TRNSFR_OUT_NMBR, java.lang.String BST_TRNSFR_ORDER_NMBR,
                                              java.lang.String BST_DSCRPTN, java.lang.String BST_APPRVL_STATUS, byte BST_PSTD,
                                              java.lang.String BST_RSN_FR_RJCTN, java.lang.String BST_CRTD_BY, java.util.Date BST_DT_CRTD,
                                              java.lang.String BST_LST_MDFD_BY, java.util.Date BST_DT_LST_MDFD, java.lang.String BST_APPRVD_RJCTD_BY,
                                              java.util.Date BST_DT_APPRVD_RJCTD, java.lang.String BST_PSTD_BY, java.util.Date BST_DT_PSTD, byte BST_LCK,
                                              byte BST_VOID, java.lang.Integer BST_AD_BRNCH, java.lang.Integer BST_AD_CMPNY) throws CreateException {
        try {

            LocalInvBranchStockTransfer entity = new LocalInvBranchStockTransfer();

            Debug.print("InvBranchStockTransferBean create");

            entity.setBstDate(BST_DT);
            entity.setBstType(BST_TYP);
            entity.setBstNumber(BST_NMBR);
            entity.setBstTransferOutNumber(BST_TRNSFR_OUT_NMBR);
            entity.setBstTransferOrderNumber(BST_TRNSFR_ORDER_NMBR);
            entity.setBstDescription(BST_DSCRPTN);
            entity.setBstApprovalStatus(BST_APPRVL_STATUS);
            entity.setBstPosted(BST_PSTD);
            entity.setBstReasonForRejection(BST_RSN_FR_RJCTN);
            entity.setBstCreatedBy(BST_CRTD_BY);
            entity.setBstDateCreated(BST_DT_CRTD);
            entity.setBstLastModifiedBy(BST_LST_MDFD_BY);
            entity.setBstDateLastModified(BST_DT_LST_MDFD);
            entity.setBstApprovedRejectedBy(BST_APPRVD_RJCTD_BY);
            entity.setBstDateApprovedRejected(BST_DT_APPRVD_RJCTD);
            entity.setBstPostedBy(BST_PSTD_BY);
            entity.setBstDatePosted(BST_DT_PSTD);
            entity.setBstLock(BST_LCK);
            entity.setBstVoid(BST_VOID);
            entity.setBstAdBranch(BST_AD_BRNCH);
            entity.setBstAdCompany(BST_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}