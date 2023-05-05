package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;

import java.util.Date;

@Stateless
public class LocalApPurchaseOrderHome {

    public static final String JNDI_NAME = "LocalApPurchaseOrderHome!com.ejb.ap.LocalApPurchaseOrderHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalApPurchaseOrderHome() {

    }

    // FINDER METHODS

    public LocalApPurchaseOrder findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalApPurchaseOrder entity = (LocalApPurchaseOrder) em
                    .find(new LocalApPurchaseOrder(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        }
        catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalApPurchaseOrder findByPoDocumentNumberAndBrCode(java.lang.String PO_DCMNT_NMBR,
                                                                java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poDocumentNumber = ?1 AND po.poAdBranch = ?2 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_DCMNT_NMBR);
            query.setParameter(2, PO_AD_BRNCH);
            query.setParameter(3, PO_AD_CMPNY);
            return (LocalApPurchaseOrder) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderHome.findByPoDocumentNumberAndBrCode(java.lang.String PO_DCMNT_NMBR, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoDocumentNumberAndBrCode(java.lang.String PO_DCMNT_NMBR, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalApPurchaseOrder findByPoReferenceNumber(java.lang.String PO_RRFRNCE_NMBR, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReferenceNumber = ?1 AND po.poAdCompany = ?2");
            query.setParameter(1, PO_RRFRNCE_NMBR);
            query.setParameter(2, PO_AD_CMPNY);
            return (LocalApPurchaseOrder) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderHome.findByPoReferenceNumber(java.lang.String PO_RRFRNCE_NMBR, java.lang.Integer PO_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoReferenceNumber(java.lang.String PO_RRFRNCE_NMBR, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalApPurchaseOrder findByPoDocumentNumberAndPoReceivingAndBrCode(java.lang.String PO_DCMNT_NMBR,
                                                                              byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poDocumentNumber = ?1 AND po.poReceiving = ?2 AND po.poAdBranch = ?3 AND po.poAdCompany = ?4");
            query.setParameter(1, PO_DCMNT_NMBR);
            query.setParameter(2, PO_RCVNG);
            query.setParameter(3, PO_AD_BRNCH);
            query.setParameter(4, PO_AD_CMPNY);
            return (LocalApPurchaseOrder) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(java.lang.String PO_DCMNT_NMBR, byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(java.lang.String PO_DCMNT_NMBR, byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPoReceivingAndSplSupplierCode(byte PO_RCVNG, java.lang.String SPL_SPPLR_CODE,
                                                                    java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = ?1 AND po.apSupplier.splSupplierCode = ?2 AND po.poLock = 0 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_RCVNG);
            query.setParameter(2, SPL_SPPLR_CODE);
            query.setParameter(3, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoReceivingAndSplSupplierCode(byte PO_RCVNG, java.lang.String SPL_SPPLR_CODE, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPoReceiving(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 1 AND po.poAdBranch = ?1 AND po.poAdCompany = ?2");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoReceiving(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalApPurchaseOrder findByPoRcvPoNumberAndPoReceivingAndSplSupplierCodeAndBrCode(
            java.lang.String PO_RCV_PO_NMBR, byte PO_RCVNG, java.lang.String SPL_SPPLR_CODE,
            java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poDocumentNumber = ?1 AND po.poReceiving = ?2 AND po.apSupplier.splSupplierCode = ?3 AND po.poAdBranch = ?4 AND po.poAdCompany = ?5");
            query.setParameter(1, PO_RCV_PO_NMBR);
            query.setParameter(2, PO_RCVNG);
            query.setParameter(3, SPL_SPPLR_CODE);
            query.setParameter(4, PO_AD_BRNCH);
            query.setParameter(5, VOU_AD_CMPNY);
            return (LocalApPurchaseOrder) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndSplSupplierCodeAndBrCode(java.lang.String PO_RCV_PO_NMBR, byte PO_RCVNG, java.lang.String SPL_SPPLR_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndSplSupplierCodeAndBrCode(java.lang.String PO_RCV_PO_NMBR, byte PO_RCVNG, java.lang.String SPL_SPPLR_CODE, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalApPurchaseOrder findByPoRcvPoNumberAndPoReceivingAndBrCode(java.lang.String PO_RCV_PO_NMBR,
                                                                           byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poRcvPoNumber = ?1 AND po.poReceiving = ?2 AND po.poAdBranch = ?3 AND po.poAdCompany = ?4");
            query.setParameter(1, PO_RCV_PO_NMBR);
            query.setParameter(2, PO_RCVNG);
            query.setParameter(3, PO_AD_BRNCH);
            query.setParameter(4, VOU_AD_CMPNY);
            return (LocalApPurchaseOrder) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndBrCode(java.lang.String PO_RCV_PO_NMBR, byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndBrCode(java.lang.String PO_RCV_PO_NMBR, byte PO_RCVNG, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPoRcvPoNumberAndPoReceivingAndPoAdBranchAndPoAdCompany(
            java.lang.String PO_RCV_PO_NMBR, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poRcvPoNumber = ?1 AND po.poReceiving=1 AND po.poAdBranch = ?2 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_RCV_PO_NMBR);
            query.setParameter(2, PO_AD_BRNCH);
            query.setParameter(3, VOU_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndPoAdBranchAndPoAdCompany(java.lang.String PO_RCV_PO_NMBR, java.lang.Integer PO_AD_BRNCH, java.lang.Integer VOU_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftPoAll(java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poApprovalStatus IS NULL AND po.poVoid = 0 AND po.poAdCompany = ?1");
            query.setParameter(1, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findDraftPoAll(java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftPoByBrCode(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving=0 AND po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NULL AND po.poVoid = 0 AND po.poAdBranch = ?1 AND po.poAdCompany = ?2");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findDraftPoByBrCode(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftRiByBrCode(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving=1 AND po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NULL AND po.poVoid = 0 AND po.poAdBranch = ?1 AND po.poAdCompany = ?2");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findDraftRiByBrCode(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findRejectedPoByBrCodeAndPoCreatedBy(java.lang.Integer PO_AD_BRNCH,
                                                                     java.lang.String PO_CRTD_BY, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 0 AND po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NOT NULL AND po.poVoid = 0 AND po.poAdBranch = ?1 AND po.poCreatedBy=?2 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_CRTD_BY);
            query.setParameter(3, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findRejectedPoByBrCodeAndPoCreatedBy(java.lang.Integer PO_AD_BRNCH, java.lang.String PO_CRTD_BY, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findRejectedRiByBrCodeAndPoCreatedBy(java.lang.Integer PO_AD_BRNCH,
                                                                     java.lang.String PO_CRTD_BY, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 1 AND po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NOT NULL AND po.poVoid = 0 AND po.poAdBranch = ?1 AND po.poCreatedBy=?2 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_CRTD_BY);
            query.setParameter(3, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findRejectedRiByBrCodeAndPoCreatedBy(java.lang.Integer PO_AD_BRNCH, java.lang.String PO_CRTD_BY, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedPoReceivingByPoReceivingDateRange(java.util.Date PO_DT_FRM,
                                                                              java.util.Date PO_DT_TO, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 1 AND po.poPosted = 0 AND po.poVoid = 0 AND po.poDate >= ?1 AND po.poDate <= ?2 AND po.poAdCompany = ?3");
            query.setParameter(1, PO_DT_FRM);
            query.setParameter(2, PO_DT_TO);
            query.setParameter(3, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findUnpostedPoReceivingByPoReceivingDateRange(java.com.util.Date PO_DT_FRM, java.com.util.Date PO_DT_TO, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedPoAll(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 0 AND po.poPosted = 1 AND po.poVoid = 0 AND po.poAdBranch = ?1 AND po.poAdCompany = ?2");
            query.setParameter(1, PO_AD_BRNCH);
            query.setParameter(2, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findPostedPoAll(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPOforPrint(java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_FindPOForPrinting");
            query.registerStoredProcedureParameter("branchCode", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("companyCode", Integer.class, ParameterMode.IN);
            query.setParameter("branchCode", PO_AD_BRNCH);
            query.setParameter("companyCode", PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findPostedTaxPoByVouDateRange(java.util.Date VOU_DT_FRM, java.util.Date PO_DT_TO,
                                                              java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(po) FROM ApPurchaseOrder po WHERE po.poReceiving = 1 AND po.poPosted = 1 AND po.apTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND po.poDate >= ?1 AND po.poDate <= ?2 AND po.poAdBranch = ?3 AND po.poAdCompany = ?4 ORDER BY po.poDate, po.poDocumentNumber");
            query.setParameter(1, VOU_DT_FRM);
            query.setParameter(2, PO_DT_TO);
            query.setParameter(3, PO_AD_BRNCH);
            query.setParameter(4, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderHome.findPostedTaxPoByVouDateRange(java.com.util.Date VOU_DT_FRM, java.com.util.Date PO_DT_TO, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getPoByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    // OTHER METHODS

    // CREATE METHODS

    public LocalApPurchaseOrder create(Integer PO_CODE, byte PO_RCVNG, String PO_TYP, Date PO_DT,
                                       Date PO_DLVRY_PRD, String PO_DCMNT_NMBR, String PO_RFRNC_NMBR, String PO_RCV_PO_NMBR, String PO_DESC,
                                       String PO_BLL_TO, String PO_SHP_TO, Date PO_CNVRSN_DT, double PO_CNVRSN_RT, double PO_TTL_AMNT,
                                       byte PO_PRNTD, byte PO_VD, String PO_SHPMNT_NMBR, String PO_APPRVL_STATUS, byte PO_PSTD,
                                       String PO_RSN_FR_RJCTN, String PO_CRTD_BY, Date PO_DT_CRTD, String PO_LST_MDFD_BY, Date PO_DT_LST_MDFD,
                                       String PO_APPRVD_RJCTD_BY, Date PO_DT_APPRVD_RJCTD, String PO_PSTD_BY, Date PO_DT_PSTD, byte PO_LCK,
                                       double PO_FRGHT, double PO_DTS, double PO_ENT_FEE, double PO_STRG, double PO_WHFG_HNDLNG,
                                       Integer PO_AD_BRNCH, Integer PO_AD_CMPNY) throws CreateException {

        try {

            LocalApPurchaseOrder entity = new LocalApPurchaseOrder();

            Debug.print("ApPurchaseOrderBean create1");
            entity.setPoCode(PO_CODE);
            entity.setPoReceiving(PO_RCVNG);
            entity.setPoType(PO_TYP);
            entity.setPoDate(PO_DT);
            entity.setPoDeliveryPeriod(PO_DLVRY_PRD);
            entity.setPoDocumentNumber(PO_DCMNT_NMBR);
            entity.setPoReferenceNumber(PO_RFRNC_NMBR);
            entity.setPoRcvPoNumber(PO_RCV_PO_NMBR);
            entity.setPoDescription(PO_DESC);
            entity.setPoBillTo(PO_BLL_TO);
            entity.setPoShipTo(PO_SHP_TO);
            entity.setPoConversionDate(PO_CNVRSN_DT);
            entity.setPoConversionRate(PO_CNVRSN_RT);
            entity.setPoTotalAmount(PO_TTL_AMNT);
            entity.setPoPrinted(PO_PRNTD);
            entity.setPoVoid(PO_VD);
            entity.setPoShipmentNumber(PO_SHPMNT_NMBR);
            entity.setPoApprovalStatus(PO_APPRVL_STATUS);
            entity.setPoPosted(PO_PSTD);
            entity.setPoReasonForRejection(PO_RSN_FR_RJCTN);
            entity.setPoCreatedBy(PO_CRTD_BY);
            entity.setPoDateCreated(PO_DT_CRTD);
            entity.setPoLastModifiedBy(PO_LST_MDFD_BY);
            entity.setPoDateLastModified(PO_DT_LST_MDFD);
            entity.setPoApprovedRejectedBy(PO_APPRVD_RJCTD_BY);
            entity.setPoDateApprovedRejected(PO_DT_APPRVD_RJCTD);
            entity.setPoPostedBy(PO_PSTD_BY);
            entity.setPoDatePosted(PO_DT_PSTD);
            entity.setPoLock(PO_LCK);
            entity.setPoFreight(PO_FRGHT);
            entity.setPoDuties(PO_DTS);
            entity.setPoEntryFee(PO_ENT_FEE);
            entity.setPoStorage(PO_STRG);
            entity.setPoWharfageHandling(PO_WHFG_HNDLNG);
            entity.setPoAdBranch(PO_AD_BRNCH);
            entity.setPoAdCompany(PO_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalApPurchaseOrder create(byte PO_RCVNG, String PO_TYP, Date PO_DT, Date PO_DLVRY_PRD,
                                       String PO_DCMNT_NMBR, String PO_RFRNC_NMBR, String PO_RCV_PO_NMBR, String PO_DESC, String PO_BLL_TO,
                                       String PO_SHP_TO, Date PO_CNVRSN_DT, double PO_CNVRSN_RT, double PO_TTL_AMNT, byte PO_PRNTD, byte PO_VD,
                                       String PO_SHPMNT_NMBR, String PO_APPRVL_STATUS, byte PO_PSTD, String PO_RSN_FR_RJCTN, String PO_CRTD_BY,
                                       Date PO_DT_CRTD, String PO_LST_MDFD_BY, Date PO_DT_LST_MDFD, String PO_APPRVD_RJCTD_BY,
                                       Date PO_DT_APPRVD_RJCTD, String PO_PSTD_BY, Date PO_DT_PSTD, byte PO_LCK, double PO_FRGHT, double PO_DTS,
                                       double PO_ENT_FEE, double PO_STRG, double PO_WHFG_HNDLNG, Integer PO_AD_BRNCH, Integer PO_AD_CMPNY)
            throws CreateException {

        try {

            LocalApPurchaseOrder entity = new LocalApPurchaseOrder();

            Debug.print("ApPurchaseOrderBean create2");
            entity.setPoDocumentNumber(PO_DCMNT_NMBR);
            entity.setPoReceiving(PO_RCVNG);
            entity.setPoType(PO_TYP);
            entity.setPoDate(PO_DT);
            entity.setPoDeliveryPeriod(PO_DLVRY_PRD);
            entity.setPoDocumentNumber(PO_DCMNT_NMBR);
            entity.setPoReferenceNumber(PO_RFRNC_NMBR);
            entity.setPoRcvPoNumber(PO_RCV_PO_NMBR);
            entity.setPoDescription(PO_DESC);
            entity.setPoBillTo(PO_BLL_TO);
            entity.setPoShipTo(PO_SHP_TO);
            entity.setPoConversionDate(PO_CNVRSN_DT);
            entity.setPoConversionRate(PO_CNVRSN_RT);
            entity.setPoTotalAmount(PO_TTL_AMNT);
            entity.setPoPrinted(PO_PRNTD);
            entity.setPoVoid(PO_VD);
            entity.setPoShipmentNumber(PO_SHPMNT_NMBR);
            entity.setPoApprovalStatus(PO_APPRVL_STATUS);
            entity.setPoPosted(PO_PSTD);
            entity.setPoReasonForRejection(PO_RSN_FR_RJCTN);
            entity.setPoCreatedBy(PO_CRTD_BY);
            entity.setPoDateCreated(PO_DT_CRTD);
            entity.setPoLastModifiedBy(PO_LST_MDFD_BY);
            entity.setPoDateLastModified(PO_DT_LST_MDFD);
            entity.setPoApprovedRejectedBy(PO_APPRVD_RJCTD_BY);
            entity.setPoDateApprovedRejected(PO_DT_APPRVD_RJCTD);
            entity.setPoPostedBy(PO_PSTD_BY);
            entity.setPoDatePosted(PO_DT_PSTD);
            entity.setPoLock(PO_LCK);
            entity.setPoFreight(PO_FRGHT);
            entity.setPoDuties(PO_DTS);
            entity.setPoEntryFee(PO_ENT_FEE);
            entity.setPoStorage(PO_STRG);
            entity.setPoWharfageHandling(PO_WHFG_HNDLNG);
            entity.setPoAdBranch(PO_AD_BRNCH);
            entity.setPoAdCompany(PO_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}