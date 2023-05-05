package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApproval;
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
public class LocalAdApprovalHome {

  public static final String JNDI_NAME = "LocalAdApprovalHome!com.ejb.ad.LocalAdApprovalHome";

  @EJB public PersistenceBeanClass em;

  private final byte APR_ENBL_GL_JRNL = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_VCHR = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_VCHR_DEPT = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_DBT_MMO = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_CHCK = EJBCommon.FALSE;
  private final byte APR_ENBL_AR_INVC = EJBCommon.FALSE;
  private final byte APR_ENBL_AR_CRDT_MMO = EJBCommon.FALSE;
  private final byte APR_ENBL_CHCK_PYMNT_RQST = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT = EJBCommon.FALSE;
  private final byte APR_ENBL_AR_RCPT = EJBCommon.FALSE;
  private final byte APR_ENBL_CM_FND_TRNSFR = EJBCommon.FALSE;
  private final byte APR_ENBL_CM_ADJSTMNT = EJBCommon.FALSE;
  private int APR_APPRVL_QUEUE_EXPRTN = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_ADJSTMNT = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_BLD = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_BLD_ORDR = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_PRCHS_RQSTN = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_CNVSS = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_ADJSTMNT_RQST = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_STCK_TRNSFR = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_PRCHS_ORDR = EJBCommon.FALSE;
  private final byte APR_ENBL_AP_RCVNG_ITM = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_BRNCH_STCK_TRNSFR = EJBCommon.FALSE;
  private final byte APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR = EJBCommon.FALSE;
  private final byte APR_ENBL_AR_SLS_ORDR = EJBCommon.FALSE;
  private final byte APR_ENBL_AR_CSTMR = EJBCommon.FALSE;
  private Integer APR_AD_CMPNY = null;

  public LocalAdApprovalHome() {}

  // FINDER METHODS

  public LocalAdApproval findByPrimaryKey(java.lang.Integer pk) throws FinderException {

    try {

      LocalAdApproval entity =
          (LocalAdApproval) em.find(new LocalAdApproval(), pk);
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

  public LocalAdApproval findByAprAdCompany(java.lang.Integer APR_AD_CMPNY) throws FinderException {

    try {
      Query query =
          em.createQuery("SELECT OBJECT(apr) FROM AdApproval apr WHERE apr.aprAdCompany = ?1");
      query.setParameter(1, APR_AD_CMPNY);
      return (LocalAdApproval) query.getSingleResult();
    } catch (NoResultException ex) {
      Debug.print(
          "EXCEPTION: NoResultException com.ejb.ad.LocalAdApprovalHome.findByAprAdCompany(java.lang.Integer APR_AD_CMPNY)");
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      Debug.print(
          "EXCEPTION: Exception com.ejb.ad.LocalAdApprovalHome.findByAprAdCompany(java.lang.Integer APR_AD_CMPNY)");
      throw ex;
    }
  }

  public LocalAdApproval findByAprAdCompany(java.lang.Integer APR_AD_CMPNY, String companyShortName) throws FinderException {

    try {
      Query query =
              em.createQueryPerCompany("SELECT OBJECT(apr) FROM AdApproval apr WHERE apr.aprAdCompany = ?1",
                      companyShortName);
      query.setParameter(1, APR_AD_CMPNY);
      return (LocalAdApproval) query.getSingleResult();
    } catch (NoResultException ex) {
      throw new FinderException(ex.getMessage());
    } catch (Exception ex) {
      throw ex;
    }
  }

  public LocalAdApproval buildApproval() throws CreateException {

    try {

      LocalAdApproval entity = new LocalAdApproval();

      Debug.print("ApGlobalPreferenceBean buildApproval");

      entity.setAprEnableGlJournal(APR_ENBL_GL_JRNL);
      entity.setAprEnableApVoucher(APR_ENBL_AP_VCHR);
      entity.setAprEnableApVoucherDepartment(APR_ENBL_AP_VCHR_DEPT);
      entity.setAprEnableApDebitMemo(APR_ENBL_AP_DBT_MMO);
      entity.setAprEnableApCheck(APR_ENBL_AP_CHCK);
      entity.setAprEnableArInvoice(APR_ENBL_AR_INVC);
      entity.setAprEnableArCreditMemo(APR_ENBL_AR_CRDT_MMO);
      entity.setAprEnableArReceipt(APR_ENBL_AR_RCPT);
      entity.setAprEnableCmFundTransfer(APR_ENBL_CM_FND_TRNSFR);
      entity.setAprEnableCmAdjustment(APR_ENBL_CM_ADJSTMNT);
      entity.setAprApprovalQueueExpiration(APR_APPRVL_QUEUE_EXPRTN);
      entity.setAprEnableInvAdjustment(APR_ENBL_INV_ADJSTMNT);
      entity.setAprEnableInvBuild(APR_ENBL_INV_BLD);
      entity.setAprEnableInvBuildOrder(APR_ENBL_INV_BLD_ORDR);
      entity.setAprEnableApCheckPaymentRequest(APR_ENBL_CHCK_PYMNT_RQST);
      entity.setAprEnableApCheckPaymentRequestDepartment(APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT);
      entity.setAprEnableApPurReq(APR_ENBL_AP_PRCHS_RQSTN);
      entity.setAprEnableApCanvass(APR_ENBL_AP_CNVSS);
      entity.setAprEnableInvAdjustmentRequest(APR_ENBL_INV_ADJSTMNT_RQST);
      entity.setAprEnableInvStockTransfer(APR_ENBL_INV_STCK_TRNSFR);
      entity.setAprEnableApPurchaseOrder(APR_ENBL_AP_PRCHS_ORDR);
      entity.setAprEnableApReceivingItem(APR_ENBL_AP_RCVNG_ITM);
      entity.setAprEnableInvBranchStockTransfer(APR_ENBL_INV_BRNCH_STCK_TRNSFR);
      entity.setAprEnableInvBranchStockTransferOrder(APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR);
      entity.setAprEnableArSalesOrder(APR_ENBL_AR_SLS_ORDR);
      entity.setAprEnableArCustomer(APR_ENBL_AR_CSTMR);
      entity.setAprAdCompany(APR_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }
  
  public LocalAdApprovalHome ApprovalQueueExpiration(int APR_APPRVL_QUEUE_EXPRTN)
  {
      this.APR_APPRVL_QUEUE_EXPRTN = APR_APPRVL_QUEUE_EXPRTN;
      return this;
  }
  
  public LocalAdApprovalHome CompanyCode(Integer APR_AD_CMPNY)
  {
      this.APR_AD_CMPNY = APR_AD_CMPNY;
      return this;
  }

  public LocalAdApproval create(
      Integer APR_CODE,
      byte APR_ENBL_GL_JRNL,
      byte APR_ENBL_AP_VCHR,
      byte APR_ENBL_AP_VCHR_DEPT,
      byte APR_ENBL_AP_DBT_MMO,
      byte APR_ENBL_AP_CHCK,
      byte APR_ENBL_AR_INVC,
      byte APR_ENBL_AR_CRDT_MMO,
      byte APR_ENBL_CHCK_PYMNT_RQST,
      byte APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT,
      byte APR_ENBL_AR_RCPT,
      byte APR_ENBL_CM_FND_TRNSFR,
      byte APR_ENBL_CM_ADJSTMNT,
      int APR_APPRVL_QUEUE_EXPRTN,
      byte APR_ENBL_INV_ADJSTMNT,
      byte APR_ENBL_INV_BLD,
      byte APR_ENBL_INV_BLD_ORDR,
      byte APR_ENBL_AP_PRCHS_RQSTN,
      byte APR_ENBL_AP_CNVSS,
      byte APR_ENBL_INV_ADJSTMNT_RQST,
      byte APR_ENBL_INV_STCK_TRNSFR,
      byte APR_ENBL_AP_PRCHS_ORDR,
      byte APR_ENBL_AP_RCVNG_ITM,
      byte APR_ENBL_INV_BRNCH_STCK_TRNSFR,
      byte APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR,
      byte APR_ENBL_AR_SLS_ORDR,
      byte APR_ENBL_AR_CSTMR,
      Integer APR_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdApproval entity = new LocalAdApproval();

      Debug.print("ApGlobalPreferenceBean create");

      entity.setAprCode(APR_CODE);
      entity.setAprEnableGlJournal(APR_ENBL_GL_JRNL);
      entity.setAprEnableApVoucher(APR_ENBL_AP_VCHR);
      entity.setAprEnableApVoucherDepartment(APR_ENBL_AP_VCHR_DEPT);
      entity.setAprEnableApDebitMemo(APR_ENBL_AP_DBT_MMO);
      entity.setAprEnableApCheckPaymentRequest(APR_ENBL_CHCK_PYMNT_RQST);
      entity.setAprEnableApCheckPaymentRequestDepartment(APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT);
      entity.setAprEnableApCheck(APR_ENBL_AP_CHCK);
      entity.setAprEnableArInvoice(APR_ENBL_AR_INVC);
      entity.setAprEnableArCreditMemo(APR_ENBL_AR_CRDT_MMO);
      entity.setAprEnableArReceipt(APR_ENBL_AR_RCPT);
      entity.setAprEnableCmFundTransfer(APR_ENBL_CM_FND_TRNSFR);
      entity.setAprEnableCmAdjustment(APR_ENBL_CM_ADJSTMNT);
      entity.setAprApprovalQueueExpiration(APR_APPRVL_QUEUE_EXPRTN);
      entity.setAprEnableInvAdjustment(APR_ENBL_INV_ADJSTMNT);
      entity.setAprEnableInvBuild(APR_ENBL_INV_BLD);
      entity.setAprEnableInvBuildOrder(APR_ENBL_INV_BLD_ORDR);
      entity.setAprEnableApPurReq(APR_ENBL_AP_PRCHS_RQSTN);
      entity.setAprEnableApCanvass(APR_ENBL_AP_CNVSS);
      entity.setAprEnableInvAdjustmentRequest(APR_ENBL_INV_ADJSTMNT_RQST);
      entity.setAprEnableInvStockTransfer(APR_ENBL_INV_STCK_TRNSFR);
      entity.setAprEnableApPurchaseOrder(APR_ENBL_AP_PRCHS_ORDR);
      entity.setAprEnableApReceivingItem(APR_ENBL_AP_RCVNG_ITM);
      entity.setAprEnableInvBranchStockTransfer(APR_ENBL_INV_BRNCH_STCK_TRNSFR);
      entity.setAprEnableInvBranchStockTransferOrder(APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR);
      entity.setAprEnableArSalesOrder(APR_ENBL_AR_SLS_ORDR);
      entity.setAprEnableArCustomer(APR_ENBL_AR_CSTMR);
      entity.setAprAdCompany(APR_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }

  public LocalAdApproval create(
      byte APR_ENBL_GL_JRNL,
      byte APR_ENBL_AP_VCHR,
      byte APR_ENBL_AP_VCHR_DEPT,
      byte APR_ENBL_AP_DBT_MMO,
      byte APR_ENBL_AP_CHCK,
      byte APR_ENBL_AR_INVC,
      byte APR_ENBL_AR_CRDT_MMO,
      byte APR_ENBL_CHCK_PYMNT_RQST,
      byte APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT,
      byte APR_ENBL_AR_RCPT,
      byte APR_ENBL_CM_FND_TRNSFR,
      byte APR_ENBL_CM_ADJSTMNT,
      int APR_APPRVL_QUEUE_EXPRTN,
      byte APR_ENBL_INV_ADJSTMNT,
      byte APR_ENBL_INV_BLD,
      byte APR_ENBL_INV_BLD_ORDR,
      byte APR_ENBL_AP_PRCHS_RQSTN,
      byte APR_ENBL_AP_CNVSS,
      byte APR_ENBL_INV_ADJSTMNT_RQST,
      byte APR_ENBL_INV_STCK_TRNSFR,
      byte APR_ENBL_AP_PRCHS_ORDR,
      byte APR_ENBL_AP_RCVNG_ITM,
      byte APR_ENBL_INV_BRNCH_STCK_TRNSFR,
      byte APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR,
      byte APR_ENBL_AR_SLS_ORDR,
      byte APR_ENBL_AR_CSTMR,
      Integer APR_AD_CMPNY)
      throws CreateException {
    try {

      LocalAdApproval entity = new LocalAdApproval();

      Debug.print("ApGlobalPreferenceBean create");

      entity.setAprEnableGlJournal(APR_ENBL_GL_JRNL);
      entity.setAprEnableApVoucher(APR_ENBL_AP_VCHR);
      entity.setAprEnableApVoucherDepartment(APR_ENBL_AP_VCHR_DEPT);
      entity.setAprEnableApDebitMemo(APR_ENBL_AP_DBT_MMO);
      entity.setAprEnableApCheck(APR_ENBL_AP_CHCK);
      entity.setAprEnableArInvoice(APR_ENBL_AR_INVC);
      entity.setAprEnableArCreditMemo(APR_ENBL_AR_CRDT_MMO);
      entity.setAprEnableArReceipt(APR_ENBL_AR_RCPT);
      entity.setAprEnableCmFundTransfer(APR_ENBL_CM_FND_TRNSFR);
      entity.setAprEnableCmAdjustment(APR_ENBL_CM_ADJSTMNT);
      entity.setAprApprovalQueueExpiration(APR_APPRVL_QUEUE_EXPRTN);
      entity.setAprEnableInvAdjustment(APR_ENBL_INV_ADJSTMNT);
      entity.setAprEnableInvBuild(APR_ENBL_INV_BLD);
      entity.setAprEnableInvBuildOrder(APR_ENBL_INV_BLD_ORDR);
      entity.setAprEnableApCheckPaymentRequest(APR_ENBL_CHCK_PYMNT_RQST);
      entity.setAprEnableApCheckPaymentRequestDepartment(APR_ENBL_AP_CHCK_PYMNT_RQST_DEPT);
      entity.setAprEnableApPurReq(APR_ENBL_AP_PRCHS_RQSTN);
      entity.setAprEnableApCanvass(APR_ENBL_AP_CNVSS);
      entity.setAprEnableInvAdjustmentRequest(APR_ENBL_INV_ADJSTMNT_RQST);
      entity.setAprEnableInvStockTransfer(APR_ENBL_INV_STCK_TRNSFR);
      entity.setAprEnableApPurchaseOrder(APR_ENBL_AP_PRCHS_ORDR);
      entity.setAprEnableApReceivingItem(APR_ENBL_AP_RCVNG_ITM);
      entity.setAprEnableInvBranchStockTransfer(APR_ENBL_INV_BRNCH_STCK_TRNSFR);
      entity.setAprEnableInvBranchStockTransferOrder(APR_ENBL_INV_BRNCH_STCK_TRNSFR_ORDR);
      entity.setAprEnableArSalesOrder(APR_ENBL_AR_SLS_ORDR);
      entity.setAprEnableArCustomer(APR_ENBL_AR_CSTMR);
      entity.setAprAdCompany(APR_AD_CMPNY);

      em.persist(entity);
      return entity;

    } catch (Exception ex) {
      throw new CreateException(ex.getMessage());
    }
  }
}