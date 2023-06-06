package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArInvoice;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalArInvoiceHome {

    public static final String JNDI_NAME = "LocalArInvoiceHome!com.ejb.ar.LocalArInvoiceHome";

    @EJB
    public PersistenceBeanClass em;

    private String INV_TYP = null;
    private byte INV_CRDT_MMO = EJBCommon.FALSE;
    private String INV_DESC = null;
    private Date INV_DT = null;
    private String INV_NMBR = null;
    private String INV_RFRNC_NMBR = null;
    private String INV_UPLD_NMBR = null;
    private String INV_CM_INVC_NMBR = null;
    private String INV_CM_RFRNC_NMBR = null;
    private double INV_AMNT_DUE = 0d;
    private double INV_DWN_PYMNT = 0d;
    private double INV_AMNT_PD = 0d;
    private double INV_PNT_DUE = 0d;
    private double INV_PNT_PD = 0d;
    private double INV_AMNT_UNEARND_INT = 0d;
    private Date INV_CNVRSN_DT = null;
    private double INV_CNVRSN_RT = 0d;
    private String INV_MMO = null;
    private double INV_PRV_RDNG = 0d;
    private double INV_PRSNT_RDNG = 0d;
    private String INV_BLL_TO_ADDRSS = null;
    private String INV_BLL_TO_CNTCT = null;
    private String INV_BLL_TO_ALT_CNTCT = null;
    private String INV_BLL_TO_PHN = null;
    private String INV_BLLNG_HDR = null;
    private String INV_BLLNG_FTR = null;
    private String INV_BLLNG_HDR2 = null;
    private String INV_BLLNG_FTR2 = null;
    private String INV_BLLNG_HDR3 = null;
    private String INV_BLLNG_FTR3 = null;
    private String INV_BLLNG_SGNTRY = null;
    private String INV_SGNTRY_TTL = null;
    private String INV_SHP_TO_ADDRSS = null;
    private String INV_SHP_TO_CNTCT = null;
    private String INV_SHP_TO_ALT_CNTCT = null;
    private String INV_SHP_TO_PHN = null;
    private Date INV_SHP_DT = null;
    private String INV_LV_FRGHT = null;
    private String INV_APPRVL_STATUS = null;
    private String INV_RSN_FR_RJCTN = null;
    private byte INV_PSTD = EJBCommon.FALSE;
    private String INV_VD_APPRVL_STATUS = null;
    private byte INV_VD_PSTD = EJBCommon.FALSE;
    private byte INV_VD = EJBCommon.FALSE;
    private byte INV_CONT = EJBCommon.FALSE;
    private byte INV_DSBL_INTRST = EJBCommon.FALSE;
    private byte INV_INTRST = EJBCommon.FALSE;
    private String INV_INTRST_RFRNC_NMBR = null;
    private double INV_INTRST_AMNT = 0d;
    private String INV_INTRST_CRTD_BY = null;
    private Date INV_INTRST_DT_CRTD = null;
    private Date INV_INTRST_NXT_RN_DT = null;
    private Date INV_INTRST_LST_RN_DT = null;
    private String INV_CRTD_BY = null;
    private Date INV_DT_CRTD = null;
    private String INV_LST_MDFD_BY = null;
    private Date INV_DT_LST_MDFD = null;
    private String INV_APPRVD_RJCTD_BY = null;
    private Date INV_DT_APPRVD_RJCTD = null;
    private String INV_PSTD_BY = null;
    private Date INV_DT_PSTD = null;
    private byte INV_PRNTD = EJBCommon.FALSE;
    private String INV_LV_SHFT = null;
    private String INV_SO_NMBR = null;
    private String INV_JO_NMBR = null;
    private byte INV_DBT_MEMO = EJBCommon.FALSE;
    private byte INV_SBJCT_TO_CMMSSN = EJBCommon.FALSE;
    private String INV_CLNT_PO = null;
    private Date INV_EFFCTVTY_DT = null;
    private Integer INV_AD_BRNCH = null;
    private Integer INV_AD_CMPNY = null;

    // FINDER METHODS

    public LocalArInvoice findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalArInvoice entity = (LocalArInvoice) em.find(new LocalArInvoice(), pk);
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

    public LocalArInvoice findByPrimaryKey(java.lang.Integer pk, String companyCode) throws FinderException {
        try {
            LocalArInvoice entity = (LocalArInvoice) em.findPerCompany(new LocalArInvoice(), pk, companyCode);
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

    public java.util.Collection findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCode(byte INV_CRDT_MMO,
                                                                                         java.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.invDate <= ?2 AND inv.arCustomer.cstCustomerCode = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_TO);
            query.setParameter(3, CST_CSTMR_CODE);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCode(byte INV_CRDT_MMO, java.com.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvPmProject(byte INV_CRDT_MMO, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.pmProject IS NOT NULL AND inv.pmProjectTypeType IS NOT NULL AND inv.pmContractTerm IS NOT NULL AND inv.invAdCompany = ?2");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvPmProject(byte INV_CRDT_MMO, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeOrderBySlp(byte INV_CRDT_MMO,
                                                                                                   java.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.invDate <= ?2 AND inv.arCustomer.cstCustomerCode = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_TO);
            query.setParameter(3, CST_CSTMR_CODE);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeOrderBySlp(byte INV_CRDT_MMO, java.com.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeSlpCodeOrderBySlp(
            byte INV_CRDT_MMO, java.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE,
            java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.invDate <= ?2 AND inv.arCustomer.cstCustomerCode = ?3 AND inv.arSalesperson.slpSalespersonCode = ?4 AND inv.invAdCompany = ?5");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_TO);
            query.setParameter(3, CST_CSTMR_CODE);
            query.setParameter(4, SLP_SLSPRSN_CODE);
            query.setParameter(5, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndInvDateAndCustomerCodeSlpCodeOrderBySlp(byte INV_CRDT_MMO, java.com.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.String SLP_SLSPRSN_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByTTInvNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invReferenceNumber = ?1 AND inv.invCreditMemo = 0 AND inv.invAdBranch = ?2 AND inv.invAdCompany = ?3");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByTTInvNumber(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByTTInvNumber(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO,
                                                                   java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invNumber = ?1 AND inv.invCreditMemo = ?2 AND inv.invAdBranch = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_CRDT_MMO);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO,
                                                                   java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY,
                                                                   String companyShortName) throws FinderException {

        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invNumber = ?1 AND inv.invCreditMemo = ?2 AND inv.invAdBranch = ?3 AND inv.invAdCompany = ?4",
                    companyShortName);
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_CRDT_MMO);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalArInvoice findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO ) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invNumber = ?1 AND inv.invCreditMemo = ?2 AND inv.invAdBranch = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_CRDT_MMO);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR,
                                                              java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 0 AND inv.invVoid = 0 AND inv.invReferenceNumber = ?1 AND inv.invCreditMemo = 0 AND inv.invAdCompany = ?2");
            query.setParameter(1, INV_RFRNC_NMBR);
            query.setParameter(2, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByInvoiceReferenceNumber(java.lang.String INV_RFRNC_NMBR) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted = 1 AND inv.invVoid = 0 AND inv.invReferenceNumber = ?1 AND inv.invCreditMemo = 0");
            query.setParameter(1, INV_RFRNC_NMBR);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByInvoiceReferenceNumber(java.lang.String INV_RFRNC_NMBR)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvoiceReferenceNumber(java.lang.String INV_RFRNC_NMBR)");
            throw ex;
        }
    }

    public java.util.Collection findAllByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR,
                                                                       java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE  inv.invVoid = 0 AND inv.invReferenceNumber = ?1 AND inv.invCreditMemo = 0 AND inv.invAdCompany = ?2");
            query.setParameter(1, INV_RFRNC_NMBR);
            query.setParameter(2, INV_AD_CMPNY);

            java.util.Collection lists = query.getResultList();

            return lists;

        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByReferenceNumberAndCompanyCode(java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByUploadNumberAndCompanyCode(java.lang.String INV_UPLD_NMBR,
                                                           java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invVoid = 0 AND inv.invUploadNumber = ?1 AND inv.invCreditMemo = 0 AND inv.invAdCompany = ?2");
            query.setParameter(1, INV_UPLD_NMBR);
            query.setParameter(2, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByUploadNumberAndCompanyCode(java.lang.String INV_UPLD_NMBR, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByUploadNumberAndCompanyCode(java.lang.String INV_UPLD_NMBR, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCmInvoiceNumberCreditMemoAndCompanyCode(java.lang.String INV_INVC_NMBR,
                                                                              java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = 1 AND inv.invPosted = 1  AND inv.invVoid = 0 AND inv.invVoidPosted = 0 AND inv.invCmInvoiceNumber = ?1 AND inv.invAdBranch = ?2 AND inv.invAdCompany = ?3");
            query.setParameter(1, INV_INVC_NMBR);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByCmInvoiceNumberCreditMemoAndCompanyCode(java.lang.String INV_INVC_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice findByInvNumberAndInvCreditMemoAndCstCustomerCode(java.lang.String INV_NMBR,
                                                                            byte INV_CRDT_MMO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invNumber = ?1 AND inv.invCreditMemo = ?2 AND inv.arCustomer.cstCustomerCode = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_CRDT_MMO);
            query.setParameter(3, CST_CSTMR_CODE);
            query.setParameter(4, INV_AD_CMPNY);
            return (LocalArInvoice) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNumberAndInvCreditMemoAndCstCustomerCode(java.lang.String INV_NMBR, byte INV_CRDT_MMO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedInvByInvCreditMemoAndInvDateRange(byte VOU_CRDT_MMO,
                                                                            java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 1 AND inv.invCreditMemo = ?1 AND inv.invDate >= ?2 AND inv.invDate <= ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, VOU_CRDT_MMO);
            query.setParameter(2, INV_DT_FRM);
            query.setParameter(3, INV_DT_TO);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedInvByInvCreditMemoAndInvDateRange(byte VOU_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoid(byte INV_CRDT_MMO,
                                                                                   java.lang.String INV_CM_INVC_NMBR, byte INV_VD, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.invCmInvoiceNumber = ?2 AND inv.invVoid = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_CM_INVC_NMBR);
            query.setParameter(3, INV_VD);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoid(byte INV_CRDT_MMO, java.lang.String INV_CM_INVC_NMBR, byte INV_VD, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted(byte INV_CRDT_MMO,
                                                                                               java.lang.String INV_CM_INVC_NMBR, byte INV_VD, byte INV_PSTD, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = ?1 AND inv.invCmInvoiceNumber = ?2 AND inv.invVoid = ?3 AND inv.invPosted = ?4 AND inv.invAdCompany = ?5");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_CM_INVC_NMBR);
            query.setParameter(3, INV_VD);
            query.setParameter(4, INV_PSTD);
            query.setParameter(5, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvCreditMemoAndInvCmInvoiceNumberAndInvVoidAndInvPosted(byte INV_CRDT_MMO, java.lang.String INV_CM_INVC_NMBR, byte INV_VD, byte INV_PSTD, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvPostedAndInvVoidAndIbName(byte INV_PSTD, byte INV_VD, java.lang.String IB_NM,
                                                                   java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted=?1 AND inv.invVoid = ?2 AND inv.arInvoiceBatch.ibName = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_PSTD);
            query.setParameter(2, INV_VD);
            query.setParameter(3, IB_NM);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvPostedAndInvVoidAndIbName(byte INV_PSTD, byte INV_VD, java.lang.String IB_NM, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByIbName(java.lang.String IB_NM, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.arInvoiceBatch.ibName=?1 AND inv.invAdCompany = ?2");
            query.setParameter(1, IB_NM);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByIbName(java.lang.String IB_NM, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftInvByInvCreditMemoAndBrCode(java.lang.String INV_TYP, byte INV_CRDT_MMO,
                                                                     java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted = 0 AND inv.invApprovalStatus IS NULL AND inv.invVoid = 0 AND inv.invType = ?1 AND inv.invCreditMemo = ?2 AND inv.invAdBranch = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_TYP);
            query.setParameter(2, INV_CRDT_MMO);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findDraftInvByInvCreditMemoAndBrCode(java.lang.String INV_TYP, byte INV_CRDT_MMO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedByInvCreditMemoAndInvDateRangeAndCstNameAndTcType(byte INV_CRDT_MMO,
                                                                                            java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP,
                                                                                            java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 1 AND inv.invCreditMemo = ?1 AND inv.invDate >= ?2 AND inv.invDate <= ?3 AND inv.arCustomer.cstName = ?4 AND inv.arTaxCode.tcType = ?5 AND inv.invAdCompany = ?6");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_FRM);
            query.setParameter(3, INV_DT_TO);
            query.setParameter(4, CST_NM);
            query.setParameter(5, TC_TYP);
            query.setParameter(6, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedByInvCreditMemoAndInvDateRangeAndCstNameAndTcType(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedByInvCreditMemoAndInvDateRangeExemptZero(byte INV_CRDT_MMO,
                                                                                   java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 1 AND inv.invCreditMemo = ?1 AND inv.invDate >= ?2 AND inv.invDate <= ?3 AND inv.arTaxCode.tcType IN ('EXEMPT','ZERO-RATED') AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_FRM);
            query.setParameter(3, INV_DT_TO);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedByInvCreditMemoAndInvDateRangeExemptZero(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedByInvCreditMemoAndInvDateToAndCstCustomerCode(java.util.Date INV_DT_TO,
                                                                                        java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 1 AND inv.invDate <= ?1 AND inv.arCustomer.cstCustomerCode = ?2 AND inv.invAdCompany = ?3");
            query.setParameter(1, INV_DT_TO);
            query.setParameter(2, CST_CSTMR_CODE);
            query.setParameter(3, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedByInvCreditMemoAndInvDateToAndCstCustomerCode(java.com.util.Date INV_DT_TO, java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvByInvDateRange(java.util.Date INV_DT_FRM, java.util.Date INV_DT_TO,
                                                              java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 0 AND inv.invVoid = 0 AND inv.invDate >= ?1 AND inv.invDate <= ?2 AND inv.invAdCompany = ?3");
            query.setParameter(1, INV_DT_FRM);
            query.setParameter(2, INV_DT_TO);
            query.setParameter(3, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findUnpostedInvByInvDateRange(java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvByInvDateRangeByBranch(java.util.Date INV_DT_FRM,
                                                                      java.util.Date INV_DT_TO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invPosted = 0 AND inv.invVoid = 0 AND inv.invDate >= ?1 AND inv.invDate <= ?2 AND inv.invAdBranch = ?3 AND inv.invAdCompany = ?4");
            query.setParameter(1, INV_DT_FRM);
            query.setParameter(2, INV_DT_TO);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findUnpostedInvByInvDateRangeByBranch(java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvByCstCustomerCode(
            java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv "
                            + "WHERE inv.arCustomer.cstCustomerCode = ?1 "
                            + "AND inv.invPosted = 0 AND inv.invAdCompany = ?2");
            query.setParameter(1, CST_CSTMR_CODE);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvByCstCustomerCode(
            java.lang.String CST_CSTMR_CODE, java.lang.Integer INV_AD_CMPNY, String companyShortName) throws FinderException {

        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(inv) FROM ArInvoice inv "
                            + "WHERE inv.arCustomer.cstCustomerCode = ?1 "
                            + "AND inv.invPosted = 0 AND inv.invAdCompany = ?2", companyShortName.toLowerCase());
            query.setParameter(1, CST_CSTMR_CODE);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findPostedSoMatchedInvByCstCustomerCode(java.lang.String CST_CSTMR_CD,
                                                                        java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted = 1 AND inv.invSoNumber IS NOT NULL AND inv.arCustomer.cstCustomerCode = ?1 AND inv.invAdCompany = ?2");
            query.setParameter(1, CST_CSTMR_CD);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedSoMatchedInvByCstCustomerCode(java.lang.String CST_CSTMR_CD, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedJoMatchedInvByCstCustomerCode(java.lang.String CST_CSTMR_CD,
                                                                        java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted = 1 AND inv.invJoNumber IS NOT NULL AND inv.arCustomer.cstCustomerCode = ?1 AND inv.invAdCompany = ?2");
            query.setParameter(1, CST_CSTMR_CD);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedJoMatchedInvByCstCustomerCode(java.lang.String CST_CSTMR_CD, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findInvForPostingByBranch(java.lang.Integer INV_AD_BRNCH,
                                                          java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invCreditMemo=0 AND (inv.invApprovalStatus='APPROVED' OR inv.invApprovalStatus='N/A') AND inv.invPosted = 0 AND inv.invVoid = 0 AND inv.invAdBranch=?1 AND inv.invAdCompany=?2");
            query.setParameter(1, INV_AD_BRNCH);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findInvForPostingByBranch(java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findInvCreditMemoForPostingByBranch(java.lang.Integer INV_AD_BRNCH,
                                                                    java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invCreditMemo=1 AND (inv.invApprovalStatus='APPROVED' OR inv.invApprovalStatus='N/A') AND inv.invPosted = 0 AND inv.invVoid = 0 AND inv.invAdBranch=?1 AND inv.invAdCompany=?2");
            query.setParameter(1, INV_AD_BRNCH);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findInvCreditMemoForPostingByBranch(java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvLvFreight(java.lang.String INV_LV_FRGHT, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invLvFreight=?1 AND inv.invAdCompany=?2");
            query.setParameter(1, INV_LV_FRGHT);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvLvFreight(java.lang.String INV_LV_FRGHT, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvLvShift(java.lang.String INV_LV_SHFT, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invLvShift=?1 AND inv.invAdCompany=?2");
            query.setParameter(1, INV_LV_SHFT);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvLvShift(java.lang.String INV_LV_SHFT, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByAdCmpnyAll(byte INV_CRDT_MMO, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invCreditMemo = ?1 AND inv.invAdCompany=?2");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByAdCmpnyAll(byte INV_CRDT_MMO, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCreditMemoAndIbName(byte INV_CRDT_MMO, java.lang.String IB_NM,
                                                             java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invCreditMemo = ?1 AND inv.arInvoiceBatch.ibName=?2 AND inv.invAdCompany=?3");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, IB_NM);
            query.setParameter(3, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvCreditMemoAndIbName(byte INV_CRDT_MMO, java.lang.String IB_NM, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvNmbrAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR,
                                                                       java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv  WHERE inv.invCreditMemo = 1 AND inv.invNumber = ?1 AND inv.invAdBranch = ?2 AND inv.invAdCompany = ?3");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findByInvNmbrAndInvCreditMemoAndBrCode(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPostedTaxInvByInvDateRange(byte INV_CRDT_MMO, java.util.Date INV_DT_FRM,
                                                               java.util.Date INV_DT_TO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoice inv WHERE inv.invPosted = 1 AND inv.arTaxCode.tcType IN ('EXEMPT','ZERO-RATED','INCLUSIVE','EXCLUSIVE') AND inv.invCreditMemo = ?1 AND inv.invDate >= ?2 AND inv.invDate <= ?3 AND inv.invAdBranch = ?4 AND inv.invAdCompany = ?5 ORDER BY inv.invDate, inv.invNumber");
            query.setParameter(1, INV_CRDT_MMO);
            query.setParameter(2, INV_DT_FRM);
            query.setParameter(3, INV_DT_TO);
            query.setParameter(4, INV_AD_BRNCH);
            query.setParameter(5, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findPostedTaxInvByInvDateRange(byte INV_CRDT_MMO, java.com.util.Date INV_DT_FRM, java.com.util.Date INV_DT_TO, java.lang.Integer INV_AD_BRNCH, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getInvByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
                                                 java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            if (LIMIT != null) {
                if (LIMIT > 0) {
                    query.setMaxResults(LIMIT);
                }
            }

            query.setFirstResult(OFFSET);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection getInvByCriteria(java.lang.String jbossQl, java.lang.Object[] args) throws FinderException {

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

    public java.util.Collection findAll(java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(inv) FROM ArInvoiceModel inv WHERE inv.invAdCompany=?1");
            query.setParameter(1, INV_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceHome.findAll(java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoice buildInvoice(String companyCode) throws CreateException {

        try {

            LocalArInvoice entity = new LocalArInvoice();

            Debug.print("ArInvoiceBean buildInvoice");

            entity.setInvType(INV_TYP);
            entity.setInvCreditMemo(INV_CRDT_MMO);
            entity.setInvDescription(INV_DESC);
            entity.setInvDate(INV_DT);
            entity.setInvNumber(INV_NMBR);
            entity.setInvReferenceNumber(INV_RFRNC_NMBR);
            entity.setInvUploadNumber(INV_UPLD_NMBR);
            entity.setInvCmInvoiceNumber(INV_CM_INVC_NMBR);
            entity.setInvCmReferenceNumber(INV_CM_RFRNC_NMBR);
            entity.setInvAmountDue(INV_AMNT_DUE);
            entity.setInvDownPayment(INV_DWN_PYMNT);
            entity.setInvAmountPaid(INV_AMNT_PD);
            entity.setInvPenaltyDue(INV_PNT_DUE);
            entity.setInvPenaltyPaid(INV_PNT_PD);
            entity.setInvAmountUnearnedInterest(INV_AMNT_UNEARND_INT);
            entity.setInvConversionDate(INV_CNVRSN_DT);
            entity.setInvConversionRate(INV_CNVRSN_RT);
            entity.setInvMemo(INV_MMO);
            entity.setInvPreviousReading(INV_PRV_RDNG);
            entity.setInvPresentReading(INV_PRSNT_RDNG);
            entity.setInvBillToAddress(INV_BLL_TO_ADDRSS);
            entity.setInvBillToContact(INV_BLL_TO_CNTCT);
            entity.setInvBillToAltContact(INV_BLL_TO_ALT_CNTCT);
            entity.setInvBillToPhone(INV_BLL_TO_PHN);
            entity.setInvBillingHeader(INV_BLLNG_HDR);
            entity.setInvBillingFooter(INV_BLLNG_FTR);
            entity.setInvBillingHeader2(INV_BLLNG_HDR2);
            entity.setInvBillingFooter2(INV_BLLNG_FTR2);
            entity.setInvBillingHeader3(INV_BLLNG_HDR3);
            entity.setInvBillingFooter3(INV_BLLNG_FTR3);
            entity.setInvBillingSignatory(INV_BLLNG_SGNTRY);
            entity.setInvSignatoryTitle(INV_SGNTRY_TTL);
            entity.setInvShipToAddress(INV_SHP_TO_ADDRSS);
            entity.setInvShipToContact(INV_SHP_TO_CNTCT);
            entity.setInvShipToAltContact(INV_SHP_TO_ALT_CNTCT);
            entity.setInvShipToPhone(INV_SHP_TO_PHN);
            entity.setInvShipDate(INV_SHP_DT);
            entity.setInvLvFreight(INV_LV_FRGHT);
            entity.setInvApprovalStatus(INV_APPRVL_STATUS);
            entity.setInvReasonForRejection(INV_RSN_FR_RJCTN);
            entity.setInvPosted(INV_PSTD);
            entity.setInvVoidApprovalStatus(INV_VD_APPRVL_STATUS);
            entity.setInvVoidPosted(INV_VD_PSTD);
            entity.setInvVoid(INV_VD);
            entity.setInvContention(INV_CONT);
            entity.setInvDisableInterest(INV_DSBL_INTRST);
            entity.setInvInterest(INV_INTRST);
            entity.setInvInterestReferenceNumber(INV_INTRST_RFRNC_NMBR);
            entity.setInvInterestAmount(INV_INTRST_AMNT);
            entity.setInvInterestCreatedBy(INV_INTRST_CRTD_BY);
            entity.setInvInterestDateCreated(INV_INTRST_DT_CRTD);
            entity.setInvInterestNextRunDate(INV_INTRST_NXT_RN_DT);
            entity.setInvInterestLastRunDate(INV_INTRST_LST_RN_DT);
            entity.setInvCreatedBy(INV_CRTD_BY);
            entity.setInvDateCreated(INV_DT_CRTD);
            entity.setInvLastModifiedBy(INV_LST_MDFD_BY);
            entity.setInvDateLastModified(INV_DT_LST_MDFD);
            entity.setInvApprovedRejectedBy(INV_APPRVD_RJCTD_BY);
            entity.setInvDateApprovedRejected(INV_DT_APPRVD_RJCTD);
            entity.setInvPostedBy(INV_PSTD_BY);
            entity.setInvDatePosted(INV_DT_PSTD);
            entity.setInvPrinted(INV_PRNTD);
            entity.setInvLvShift(INV_LV_SHFT);
            entity.setInvSoNumber(INV_SO_NMBR);
            entity.setInvJoNumber(INV_JO_NMBR);
            entity.setInvDebitMemo(INV_DBT_MEMO);
            entity.setInvSubjectToCommission(INV_SBJCT_TO_CMMSSN);
            entity.setInvClientPO(INV_CLNT_PO);
            entity.setInvEffectivityDate(INV_EFFCTVTY_DT);
            entity.setInvAdBranch(INV_AD_BRNCH);
            entity.setInvAdCompany(INV_AD_CMPNY);

            em.persist(entity, companyCode);
            return entity;

        } catch (Exception ex) {
            Debug.print("buildInvoice Exception : " + ex.getMessage());
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoiceHome InvType(String INV_TYP) {
        this.INV_TYP = INV_TYP;
        return this;
    }

    public LocalArInvoiceHome InvCreditMemo(byte INV_CRDT_MMO) {
        this.INV_CRDT_MMO = INV_CRDT_MMO;
        return this;
    }

    public LocalArInvoiceHome InvDescription(String INV_DESC) {
        this.INV_DESC = INV_DESC;
        return this;
    }

    public LocalArInvoiceHome InvDate(Date INV_DT) {
        this.INV_DT = INV_DT;
        return this;
    }

    public LocalArInvoiceHome InvNumber(String INV_NMBR) {
        this.INV_NMBR = INV_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvCmInvoiceNumber(String INV_CM_INVC_NMBR) {
        this.INV_CM_INVC_NMBR = INV_CM_INVC_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvReferenceNumber(String INV_RFRNC_NMBR) {
        this.INV_RFRNC_NMBR = INV_RFRNC_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvUploadNumber(String INV_UPLD_NMBR) {
        this.INV_UPLD_NMBR = INV_UPLD_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvCmReferenceNumber(String INV_CM_RFRNC_NMBR) {
        this.INV_CM_RFRNC_NMBR = INV_CM_RFRNC_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvAmountDue(double INV_AMNT_DUE) {
        this.INV_AMNT_DUE = INV_AMNT_DUE;
        return this;
    }

    public LocalArInvoiceHome InvConversionDate(Date INV_CNVRSN_DT) {
        this.INV_CNVRSN_DT = INV_CNVRSN_DT;
        return this;
    }

    public LocalArInvoiceHome InvConversionRate(double INV_CNVRSN_RT) {
        this.INV_CNVRSN_RT = INV_CNVRSN_RT;
        return this;
    }

    public LocalArInvoiceHome InvMemo(String INV_MMO) {
        this.INV_MMO = INV_MMO;
        return this;
    }

    public LocalArInvoiceHome InvPreviousReading(double INV_PRV_RDNG) {
        this.INV_PRV_RDNG = INV_PRV_RDNG;
        return this;
    }

    public LocalArInvoiceHome InvPresentReading(double INV_PRSNT_RDNG) {
        this.INV_PRSNT_RDNG = INV_PRSNT_RDNG;
        return this;
    }

    public LocalArInvoiceHome InvBillToAddress(String INV_BLL_TO_ADDRSS) {
        this.INV_BLL_TO_ADDRSS = INV_BLL_TO_ADDRSS;
        return this;
    }

    public LocalArInvoiceHome InvBillToContact(String INV_BLL_TO_CNTCT) {
        this.INV_BLL_TO_CNTCT = INV_BLL_TO_CNTCT;
        return this;
    }

    public LocalArInvoiceHome InvBillToAltContact(String INV_BLL_TO_ALT_CNTCT) {
        this.INV_BLL_TO_ALT_CNTCT = INV_BLL_TO_ALT_CNTCT;
        return this;
    }

    public LocalArInvoiceHome InvBillToPhone(String INV_BLL_TO_PHN) {
        this.INV_BLL_TO_PHN = INV_BLL_TO_PHN;
        return this;
    }

    public LocalArInvoiceHome InvBillingHeader(String INV_BLLNG_HDR) {
        this.INV_BLLNG_HDR = INV_BLLNG_HDR;
        return this;
    }

    public LocalArInvoiceHome InvBillingFooter(String INV_BLLNG_FTR) {
        this.INV_BLLNG_FTR = INV_BLLNG_FTR;
        return this;
    }

    public LocalArInvoiceHome InvBillingHeader2(String INV_BLLNG_HDR2) {
        this.INV_BLLNG_HDR2 = INV_BLLNG_HDR2;
        return this;
    }

    public LocalArInvoiceHome InvBillingFooter2(String INV_BLLNG_FTR2) {
        this.INV_BLLNG_FTR2 = INV_BLLNG_FTR2;
        return this;
    }

    public LocalArInvoiceHome InvBillingHeader3(String INV_BLLNG_HDR3) {
        this.INV_BLLNG_HDR3 = INV_BLLNG_HDR3;
        return this;
    }

    public LocalArInvoiceHome InvBillingFooter3(String INV_BLLNG_FTR3) {
        this.INV_BLLNG_FTR3 = INV_BLLNG_FTR3;
        return this;
    }

    public LocalArInvoiceHome InvBillingSignatory(String INV_BLLNG_SGNTRY) {
        this.INV_BLLNG_SGNTRY = INV_BLLNG_SGNTRY;
        return this;
    }

    public LocalArInvoiceHome InvSignatoryTitle(String INV_SGNTRY_TTL) {
        this.INV_SGNTRY_TTL = INV_SGNTRY_TTL;
        return this;
    }

    public LocalArInvoiceHome InvShipToAddress(String INV_SHP_TO_ADDRSS) {
        this.INV_SHP_TO_ADDRSS = INV_SHP_TO_ADDRSS;
        return this;
    }

    public LocalArInvoiceHome InvShipToContact(String INV_SHP_TO_CNTCT) {
        this.INV_SHP_TO_CNTCT = INV_SHP_TO_CNTCT;
        return this;
    }

    public LocalArInvoiceHome InvShipToAltContact(String INV_SHP_TO_ALT_CNTCT) {
        this.INV_SHP_TO_ALT_CNTCT = INV_SHP_TO_ALT_CNTCT;
        return this;
    }

    public LocalArInvoiceHome InvShipToPhone(String INV_SHP_TO_PHN) {
        this.INV_SHP_TO_PHN = INV_SHP_TO_PHN;
        return this;
    }

    public LocalArInvoiceHome InvShipDate(Date INV_SHP_DT) {
        this.INV_SHP_DT = INV_SHP_DT;
        return this;
    }

    public LocalArInvoiceHome InvLvFreight(String INV_LV_FRGHT) {
        this.INV_LV_FRGHT = INV_LV_FRGHT;
        return this;
    }

    public LocalArInvoiceHome InvApprovalStatus(String INV_APPRVL_STATUS) {
        this.INV_APPRVL_STATUS = INV_APPRVL_STATUS;
        return this;
    }

    public LocalArInvoiceHome InvReasonForRejection(String INV_RSN_FR_RJCTN) {
        this.INV_RSN_FR_RJCTN = INV_RSN_FR_RJCTN;
        return this;
    }

    public LocalArInvoiceHome InvPosted(byte INV_PSTD) {
        this.INV_PSTD = INV_PSTD;
        return this;
    }

    public LocalArInvoiceHome InvVoidApprovalStatus(String INV_VD_APPRVL_STATUS) {
        this.INV_VD_APPRVL_STATUS = INV_VD_APPRVL_STATUS;
        return this;
    }

    public LocalArInvoiceHome InvVoidPosted(byte INV_VD_PSTD) {
        this.INV_VD_PSTD = INV_VD_PSTD;
        return this;
    }

    public LocalArInvoiceHome InvVoid(byte INV_VD) {
        this.INV_VD = INV_VD;
        return this;
    }

    public LocalArInvoiceHome InvContention(byte INV_CONT) {
        this.INV_CONT = INV_CONT;
        return this;
    }

    public LocalArInvoiceHome InvDisableInterest(byte INV_DSBL_INTRST) {
        this.INV_DSBL_INTRST = INV_DSBL_INTRST;
        return this;
    }

    public LocalArInvoiceHome InvInterest(byte INV_INTRST) {
        this.INV_INTRST = INV_INTRST;
        return this;
    }

    public LocalArInvoiceHome InvInterestReferenceNumber(String INV_INTRST_RFRNC_NMBR) {
        this.INV_INTRST_RFRNC_NMBR = INV_INTRST_RFRNC_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvInterestAmount(double INV_INTRST_AMNT) {
        this.INV_INTRST_AMNT = INV_INTRST_AMNT;
        return this;
    }

    public LocalArInvoiceHome InvInterestCreatedBy(String INV_INTRST_CRTD_BY) {
        this.INV_BLL_TO_ADDRSS = INV_BLL_TO_ADDRSS;
        return this;
    }

    public LocalArInvoiceHome InvInterestDateCreated(Date INV_INTRST_DT_CRTD) {
        this.INV_INTRST_DT_CRTD = INV_INTRST_DT_CRTD;
        return this;
    }

    public LocalArInvoiceHome InvInterestNextRunDate(Date INV_INTRST_NXT_RN_DT) {
        this.INV_INTRST_NXT_RN_DT = INV_INTRST_NXT_RN_DT;
        return this;
    }

    public LocalArInvoiceHome InvInterestLastRunDate(Date INV_INTRST_LST_RN_DT) {
        this.INV_INTRST_LST_RN_DT = INV_INTRST_LST_RN_DT;
        return this;
    }

    public LocalArInvoiceHome InvCreatedBy(String INV_CRTD_BY) {
        this.INV_CRTD_BY = INV_CRTD_BY;
        return this;
    }

    public LocalArInvoiceHome InvDateCreated(Date INV_DT_CRTD) {
        this.INV_DT_CRTD = INV_DT_CRTD;
        return this;
    }

    public LocalArInvoiceHome InvLastModifiedBy(String INV_LST_MDFD_BY) {
        this.INV_LST_MDFD_BY = INV_LST_MDFD_BY;
        return this;
    }

    public LocalArInvoiceHome InvDateLastModified(Date INV_DT_LST_MDFD) {
        this.INV_DT_LST_MDFD = INV_DT_LST_MDFD;
        return this;
    }

    public LocalArInvoiceHome InvApprovedRejectedBy(String INV_APPRVD_RJCTD_BY) {
        this.INV_APPRVD_RJCTD_BY = INV_APPRVD_RJCTD_BY;
        return this;
    }

    public LocalArInvoiceHome InvDateApprovedRejected(Date INV_DT_APPRVD_RJCTD) {
        this.INV_DT_APPRVD_RJCTD = INV_DT_APPRVD_RJCTD;
        return this;
    }

    public LocalArInvoiceHome InvPostedBy(String INV_PSTD_BY) {
        this.INV_PSTD_BY = INV_PSTD_BY;
        return this;
    }

    public LocalArInvoiceHome InvDatePosted(Date INV_DT_PSTD) {
        this.INV_DT_PSTD = INV_DT_PSTD;
        return this;
    }

    public LocalArInvoiceHome InvPrinted(byte INV_PRNTD) {
        this.INV_PRNTD = INV_PRNTD;
        return this;
    }

    public LocalArInvoiceHome InvLvShift(String INV_LV_SHFT) {
        this.INV_LV_SHFT = INV_LV_SHFT;
        return this;
    }

    public LocalArInvoiceHome InvSoNumber(String INV_SO_NMBR) {
        this.INV_SO_NMBR = INV_SO_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvJoNumber(String INV_JO_NMBR) {
        this.INV_JO_NMBR = INV_JO_NMBR;
        return this;
    }

    public LocalArInvoiceHome InvDebitMemo(byte INV_DBT_MEMO) {
        this.INV_DBT_MEMO = INV_DBT_MEMO;
        return this;
    }

    public LocalArInvoiceHome InvSubjectToCommission(byte INV_SBJCT_TO_CMMSSN) {
        this.INV_SBJCT_TO_CMMSSN = INV_SBJCT_TO_CMMSSN;
        return this;
    }

    public LocalArInvoiceHome InvClientPO(String INV_CLNT_PO) {
        this.INV_CLNT_PO = INV_CLNT_PO;
        return this;
    }

    public LocalArInvoiceHome InvEffectivityDate(Date INV_EFFCTVTY_DT) {
        this.INV_EFFCTVTY_DT = INV_EFFCTVTY_DT;
        return this;
    }

    public LocalArInvoiceHome InvAdBranch(Integer INV_AD_BRNCH) {
        this.INV_AD_BRNCH = INV_AD_BRNCH;
        return this;
    }

    public LocalArInvoiceHome InvAdCompany(Integer INV_AD_CMPNY) {
        this.INV_AD_CMPNY = INV_AD_CMPNY;
        return this;
    }

    public LocalArInvoice create(Integer INV_CODE, String INV_TYP, byte INV_CRDT_MMO, String INV_DESC,
                                 Date INV_DT, String INV_NMBR, String INV_RFRNC_NMBR, String INV_UPLD_NMBR, String INV_CM_INVC_NMBR,
                                 String INV_CM_RFRNC_NMBR, double INV_AMNT_DUE, double INV_DWN_PYMNT, double INV_AMNT_PD, double INV_PNT_DUE,
                                 double INV_PNT_PD, double INV_AMNT_UNEARND_INT, Date INV_CNVRSN_DT, double INV_CNVRSN_RT, String INV_MMO,
                                 double INV_PRV_RDNG, double INV_PRSNT_RDNG, String INV_BLL_TO_ADDRSS, String INV_BLL_TO_CNTCT,
                                 String INV_BLL_TO_ALT_CNTCT, String INV_BLL_TO_PHN, String INV_BLLNG_HDR, String INV_BLLNG_FTR,
                                 String INV_BLLNG_HDR2, String INV_BLLNG_FTR2, String INV_BLLNG_HDR3, String INV_BLLNG_FTR3,
                                 String INV_BLLNG_SGNTRY, String INV_SGNTRY_TTL, String INV_SHP_TO_ADDRSS, String INV_SHP_TO_CNTCT,
                                 String INV_SHP_TO_ALT_CNTCT, String INV_SHP_TO_PHN, Date INV_SHP_DT, String INV_LV_FRGHT,
                                 String INV_APPRVL_STATUS, String INV_RSN_FR_RJCTN, byte INV_PSTD, String INV_VD_APPRVL_STATUS,
                                 byte INV_VD_PSTD, byte INV_VD, byte INV_CONT, byte INV_DSBL_INTRST, byte INV_INTRST,
                                 String INV_INTRST_RFRNC_NMBR, double INV_INTRST_AMNT, String INV_INTRST_CRTD_BY, Date INV_INTRST_DT_CRTD,
                                 Date INV_INTRST_NXT_RN_DT, Date INV_INTRST_LST_RN_DT, String INV_CRTD_BY, Date INV_DT_CRTD,
                                 String INV_LST_MDFD_BY, Date INV_DT_LST_MDFD, String INV_APPRVD_RJCTD_BY, Date INV_DT_APPRVD_RJCTD,
                                 String INV_PSTD_BY, Date INV_DT_PSTD, byte INV_PRNTD, String INV_LV_SHFT, String INV_SO_NMBR,
                                 String INV_JO_NMBR, byte INV_DBT_MEMO, byte INV_SBJCT_TO_CMMSSN, String INV_CLNT_PO, Date INV_EFFCTVTY_DT,
                                 Integer INV_AD_BRNCH, Integer INV_AD_CMPNY) throws CreateException {
        try {

            LocalArInvoice entity = new LocalArInvoice();

            Debug.print("ArInvoiceBean create");
            Debug.print("INV_AD BRNCH" + INV_AD_BRNCH);

            entity.setInvCode(INV_CODE);
            entity.setInvType(INV_TYP);
            entity.setInvCreditMemo(INV_CRDT_MMO);
            entity.setInvDescription(INV_DESC);
            entity.setInvDate(INV_DT);
            entity.setInvNumber(INV_NMBR);
            entity.setInvReferenceNumber(INV_RFRNC_NMBR);
            entity.setInvUploadNumber(INV_UPLD_NMBR);
            entity.setInvCmInvoiceNumber(INV_CM_INVC_NMBR);
            entity.setInvCmReferenceNumber(INV_CM_RFRNC_NMBR);
            entity.setInvAmountDue(INV_AMNT_DUE);
            entity.setInvDownPayment(INV_DWN_PYMNT);
            entity.setInvAmountPaid(INV_AMNT_PD);
            entity.setInvPenaltyDue(INV_PNT_DUE);
            entity.setInvPenaltyPaid(INV_PNT_PD);
            entity.setInvPenaltyDue(INV_PNT_DUE);
            entity.setInvAmountUnearnedInterest(INV_AMNT_UNEARND_INT);
            entity.setInvConversionDate(INV_CNVRSN_DT);
            entity.setInvConversionRate(INV_CNVRSN_RT);
            entity.setInvMemo(INV_MMO);
            entity.setInvPreviousReading(INV_PRV_RDNG);
            entity.setInvPresentReading(INV_PRSNT_RDNG);
            entity.setInvBillToAddress(INV_BLL_TO_ADDRSS);
            entity.setInvBillToContact(INV_BLL_TO_CNTCT);
            entity.setInvBillToAltContact(INV_BLL_TO_ALT_CNTCT);
            entity.setInvBillToPhone(INV_BLL_TO_PHN);
            entity.setInvBillingHeader(INV_BLLNG_HDR);
            entity.setInvBillingFooter(INV_BLLNG_FTR);
            entity.setInvBillingHeader2(INV_BLLNG_HDR2);
            entity.setInvBillingFooter2(INV_BLLNG_FTR2);
            entity.setInvBillingHeader3(INV_BLLNG_HDR3);
            entity.setInvBillingFooter3(INV_BLLNG_FTR3);
            entity.setInvBillingSignatory(INV_BLLNG_SGNTRY);
            entity.setInvSignatoryTitle(INV_SGNTRY_TTL);
            entity.setInvShipToAddress(INV_SHP_TO_ADDRSS);
            entity.setInvShipToContact(INV_SHP_TO_CNTCT);
            entity.setInvShipToAltContact(INV_SHP_TO_ALT_CNTCT);
            entity.setInvShipToPhone(INV_SHP_TO_PHN);
            entity.setInvShipDate(INV_SHP_DT);
            entity.setInvLvFreight(INV_LV_FRGHT);
            entity.setInvApprovalStatus(INV_APPRVL_STATUS);
            entity.setInvReasonForRejection(INV_RSN_FR_RJCTN);
            entity.setInvPosted(INV_PSTD);
            entity.setInvVoidApprovalStatus(INV_VD_APPRVL_STATUS);
            entity.setInvVoidPosted(INV_VD_PSTD);
            entity.setInvVoid(INV_VD);
            entity.setInvContention(INV_CONT);
            entity.setInvDisableInterest(INV_DSBL_INTRST);
            entity.setInvInterest(INV_INTRST);
            entity.setInvInterestReferenceNumber(INV_INTRST_RFRNC_NMBR);
            entity.setInvInterestAmount(INV_INTRST_AMNT);
            entity.setInvInterestCreatedBy(INV_INTRST_CRTD_BY);
            entity.setInvInterestDateCreated(INV_INTRST_DT_CRTD);
            entity.setInvInterestNextRunDate(INV_INTRST_NXT_RN_DT);
            entity.setInvInterestLastRunDate(INV_INTRST_LST_RN_DT);
            entity.setInvCreatedBy(INV_CRTD_BY);
            entity.setInvDateCreated(INV_DT_CRTD);
            entity.setInvLastModifiedBy(INV_LST_MDFD_BY);
            entity.setInvDateLastModified(INV_DT_LST_MDFD);
            entity.setInvApprovedRejectedBy(INV_APPRVD_RJCTD_BY);
            entity.setInvDateApprovedRejected(INV_DT_APPRVD_RJCTD);
            entity.setInvPostedBy(INV_PSTD_BY);
            entity.setInvDatePosted(INV_DT_PSTD);
            entity.setInvPrinted(INV_PRNTD);
            entity.setInvLvShift(INV_LV_SHFT);
            entity.setInvJoNumber(INV_JO_NMBR);
            entity.setInvSoNumber(INV_SO_NMBR);
            entity.setInvDebitMemo(INV_DBT_MEMO);
            entity.setInvSubjectToCommission(INV_SBJCT_TO_CMMSSN);
            entity.setInvClientPO(INV_CLNT_PO);
            entity.setInvEffectivityDate(INV_EFFCTVTY_DT);
            entity.setInvAdBranch(INV_AD_BRNCH);
            entity.setInvAdCompany(INV_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoice create(String INV_TYP, byte INV_CRDT_MMO, String INV_DESC, Date INV_DT,
                                 String INV_NMBR, String INV_RFRNC_NMBR, String INV_UPLD_NMBR, String INV_CM_INVC_NMBR,
                                 String INV_CM_RFRNC_NMBR, double INV_AMNT_DUE, double INV_DWN_PYMNT, double INV_AMNT_PD, double INV_PNT_DUE,
                                 double INV_PNT_PD, double INV_AMNT_UNEARND_INT, Date INV_CNVRSN_DT, double INV_CNVRSN_RT, String INV_MMO,
                                 double INV_PRV_RDNG, double INV_PRSNT_RDNG, String INV_BLL_TO_ADDRSS, String INV_BLL_TO_CNTCT,
                                 String INV_BLL_TO_ALT_CNTCT, String INV_BLL_TO_PHN, String INV_BLLNG_HDR, String INV_BLLNG_FTR,
                                 String INV_BLLNG_HDR2, String INV_BLLNG_FTR2, String INV_BLLNG_HDR3, String INV_BLLNG_FTR3,
                                 String INV_BLLNG_SGNTRY, String INV_SGNTRY_TTL, String INV_SHP_TO_ADDRSS, String INV_SHP_TO_CNTCT,
                                 String INV_SHP_TO_ALT_CNTCT, String INV_SHP_TO_PHN, Date INV_SHP_DT, String INV_LV_FRGHT,
                                 String INV_APPRVL_STATUS, String INV_RSN_FR_RJCTN, byte INV_PSTD, String INV_VD_APPRVL_STATUS,
                                 byte INV_VD_PSTD, byte INV_VD, byte INV_CONT, byte INV_DSBL_INTRST, byte INV_INTRST,
                                 String INV_INTRST_RFRNC_NMBR, double INV_INTRST_AMNT, String INV_INTRST_CRTD_BY, Date INV_INTRST_DT_CRTD,
                                 Date INV_INTRST_NXT_RN_DT, Date INV_INTRST_LST_RN_DT, String INV_CRTD_BY, Date INV_DT_CRTD,
                                 String INV_LST_MDFD_BY, Date INV_DT_LST_MDFD, String INV_APPRVD_RJCTD_BY, Date INV_DT_APPRVD_RJCTD,
                                 String INV_PSTD_BY, Date INV_DT_PSTD, byte INV_PRNTD, String INV_LV_SHFT, String INV_SO_NMBR,
                                 String INV_JO_NMBR, byte INV_DBT_MEMO, byte INV_SBJCT_TO_CMMSSN, String INV_CLNT_PO, Date INV_EFFCTVTY_DT,
                                 Integer INV_AD_BRNCH, Integer INV_AD_CMPNY) throws CreateException {
        try {

            LocalArInvoice entity = new LocalArInvoice();

            Debug.print("ArInvoiceBean create");
            Debug.print("INV_AD BRNCH =" + INV_AD_BRNCH);

            entity.setInvType(INV_TYP);
            entity.setInvCreditMemo(INV_CRDT_MMO);
            entity.setInvDescription(INV_DESC);
            entity.setInvDate(INV_DT);
            entity.setInvNumber(INV_NMBR);
            entity.setInvReferenceNumber(INV_RFRNC_NMBR);
            entity.setInvUploadNumber(INV_UPLD_NMBR);
            entity.setInvCmInvoiceNumber(INV_CM_INVC_NMBR);
            entity.setInvCmReferenceNumber(INV_CM_RFRNC_NMBR);
            entity.setInvAmountDue(INV_AMNT_DUE);
            entity.setInvDownPayment(INV_DWN_PYMNT);
            entity.setInvAmountPaid(INV_AMNT_PD);
            entity.setInvPenaltyDue(INV_PNT_DUE);
            entity.setInvPenaltyPaid(INV_PNT_PD);
            entity.setInvAmountUnearnedInterest(INV_AMNT_UNEARND_INT);
            entity.setInvConversionDate(INV_CNVRSN_DT);
            entity.setInvConversionRate(INV_CNVRSN_RT);
            entity.setInvMemo(INV_MMO);
            entity.setInvPreviousReading(INV_PRV_RDNG);
            entity.setInvPresentReading(INV_PRSNT_RDNG);
            entity.setInvBillToAddress(INV_BLL_TO_ADDRSS);
            entity.setInvBillToContact(INV_BLL_TO_CNTCT);
            entity.setInvBillToAltContact(INV_BLL_TO_ALT_CNTCT);
            entity.setInvBillToPhone(INV_BLL_TO_PHN);
            entity.setInvBillingHeader(INV_BLLNG_HDR);
            entity.setInvBillingFooter(INV_BLLNG_FTR);
            entity.setInvBillingHeader2(INV_BLLNG_HDR2);
            entity.setInvBillingFooter2(INV_BLLNG_FTR2);
            entity.setInvBillingHeader3(INV_BLLNG_HDR3);
            entity.setInvBillingFooter3(INV_BLLNG_FTR3);
            entity.setInvBillingSignatory(INV_BLLNG_SGNTRY);
            entity.setInvSignatoryTitle(INV_SGNTRY_TTL);
            entity.setInvShipToAddress(INV_SHP_TO_ADDRSS);
            entity.setInvShipToContact(INV_SHP_TO_CNTCT);
            entity.setInvShipToAltContact(INV_SHP_TO_ALT_CNTCT);
            entity.setInvShipToPhone(INV_SHP_TO_PHN);
            entity.setInvShipDate(INV_SHP_DT);
            entity.setInvLvFreight(INV_LV_FRGHT);
            entity.setInvApprovalStatus(INV_APPRVL_STATUS);
            entity.setInvReasonForRejection(INV_RSN_FR_RJCTN);
            entity.setInvPosted(INV_PSTD);
            entity.setInvVoidApprovalStatus(INV_VD_APPRVL_STATUS);
            entity.setInvVoidPosted(INV_VD_PSTD);
            entity.setInvVoid(INV_VD);
            entity.setInvContention(INV_CONT);
            entity.setInvDisableInterest(INV_DSBL_INTRST);
            entity.setInvInterest(INV_INTRST);
            entity.setInvInterestReferenceNumber(INV_INTRST_RFRNC_NMBR);
            entity.setInvInterestAmount(INV_INTRST_AMNT);
            entity.setInvInterestCreatedBy(INV_INTRST_CRTD_BY);
            entity.setInvInterestDateCreated(INV_INTRST_DT_CRTD);
            entity.setInvInterestNextRunDate(INV_INTRST_NXT_RN_DT);
            entity.setInvInterestLastRunDate(INV_INTRST_LST_RN_DT);
            entity.setInvCreatedBy(INV_CRTD_BY);
            entity.setInvDateCreated(INV_DT_CRTD);
            entity.setInvLastModifiedBy(INV_LST_MDFD_BY);
            entity.setInvDateLastModified(INV_DT_LST_MDFD);
            entity.setInvApprovedRejectedBy(INV_APPRVD_RJCTD_BY);
            entity.setInvDateApprovedRejected(INV_DT_APPRVD_RJCTD);
            entity.setInvPostedBy(INV_PSTD_BY);
            entity.setInvDatePosted(INV_DT_PSTD);
            entity.setInvPrinted(INV_PRNTD);
            entity.setInvLvShift(INV_LV_SHFT);
            entity.setInvSoNumber(INV_SO_NMBR);
            entity.setInvJoNumber(INV_JO_NMBR);
            entity.setInvDebitMemo(INV_DBT_MEMO);
            entity.setInvSubjectToCommission(INV_SBJCT_TO_CMMSSN);
            entity.setInvClientPO(INV_CLNT_PO);
            entity.setInvEffectivityDate(INV_EFFCTVTY_DT);
            entity.setInvAdBranch(INV_AD_BRNCH);
            entity.setInvAdCompany(INV_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}