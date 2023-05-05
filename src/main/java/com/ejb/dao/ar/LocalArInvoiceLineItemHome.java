package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.util.Debug;
import com.util.EJBCommon;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@SuppressWarnings("ALL")
@Stateless
public class LocalArInvoiceLineItemHome {

    public static final String JNDI_NAME = "LocalArInvoiceLineItemHome!com.ejb.ar.LocalArInvoiceLineItemHome";

    @EJB
    public PersistenceBeanClass em;

    private short ILI_LN = (short) 0;
    private double ILI_QTY = 0d;
    private double ILI_UNT_PRC = 0d;
    private double ILI_AMNT = 0d;
    private double ILI_TX_AMNT = 0d;
    private double ILI_DSCNT_1 = 0d;
    private double ILI_DSCNT_2 = 0d;
    private double ILI_DSCNT_3 = 0d;
    private double ILI_DSCNT_4 = 0d;
    private double TTL_ILI_DSCNT = 0d;
    private byte ILI_TX = EJBCommon.FALSE;
    private Integer ILI_AD_CMPNY = null;

    public LocalArInvoiceLineItemHome() {

    }

    // FINDER METHODS

    public LocalArInvoiceLineItem findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalArInvoiceLineItem entity = (LocalArInvoiceLineItem) em
                    .find(new LocalArInvoiceLineItem(), pk);
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

    public java.util.Collection findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCode=?1 AND ili.iliAdCompany=?2");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCode(java.lang.Integer INV_CODE, java.lang.Integer ILI_AD_CMPNY, String companyShortName)
            throws FinderException {

        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCode=?1 AND ili.iliAdCompany=?2",
                    companyShortName);
            query.setParameter(1, INV_CODE);
            query.setParameter(2, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
           throw ex;
        }
    }

    public java.util.Collection findiliByRctCodeAndAdCompany(java.lang.Integer RCT_CODE, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arReceipt.rctCode = ?1 AND ili.iliAdCompany = ?2");
            query.setParameter(1, RCT_CODE);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findiliByRctCodeAndAdCompany(java.lang.Integer RCT_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCreditMemoAndInvPostedAndIlCodeAndBrCode(byte INV_CRDT_MM, byte INV_PSTD,
                                                                                  java.lang.Integer INV_IL_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCreditMemo=?1 AND ili.arInvoice.invPosted=?2 AND ili.invItemLocation.ilCode=?3 AND ili.arInvoice.invAdBranch=?4 AND ili.iliAdCompany=?5");
            query.setParameter(1, INV_CRDT_MM);
            query.setParameter(2, INV_PSTD);
            query.setParameter(3, INV_IL_CODE);
            query.setParameter(4, INV_AD_BRNCH);
            query.setParameter(5, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByInvCreditMemoAndInvPostedAndIlCodeAndBrCode(byte INV_CRDT_MM, byte INV_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByRctPostedAndIlCodeAndBrCode(byte RCT_PSTD, java.lang.Integer INV_IL_CODE,
                                                                  java.lang.Integer RCT_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arReceipt.rctPosted=?1 AND ili.invItemLocation.ilCode=?2 AND ili.arReceipt.rctAdBranch=?3 AND ili.iliAdCompany=?4");
            query.setParameter(1, RCT_PSTD);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, RCT_AD_BRNCH);
            query.setParameter(4, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByRctPostedAndIlCodeAndBrCode(byte RCT_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findiliByInvCodeAndAdCompany(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCode = ?1 AND ili.iliAdCompany = ?2");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findiliByInvCodeAndAdCompany(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvNumberAndInvCreditMemoAndInvPostedAndIlCodeAndBrCode(
            java.lang.Integer INV_CODE, byte INV_CRDT_MM, byte INV_PSTD, java.lang.Integer INV_IL_CODE,
            java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCode = ?1 AND ili.arInvoice.invCreditMemo=?2 AND ili.arInvoice.invPosted=?3 AND ili.invItemLocation.ilCode=?4 AND ili.arInvoice.invAdBranch=?5 AND ili.iliAdCompany=?6");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, INV_CRDT_MM);
            query.setParameter(3, INV_PSTD);
            query.setParameter(4, INV_IL_CODE);
            query.setParameter(5, INV_AD_BRNCH);
            query.setParameter(6, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByInvNumberAndInvCreditMemoAndInvPostedAndIlCodeAndBrCode(java.lang.Integer INV_CODE, byte INV_CRDT_MM, byte INV_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM,
                                                                                                 java.lang.String LOC_NM, java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
                                                                                                 java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invPosted = 0 AND ili.arInvoice.invVoid = 0 AND ili.invItemLocation.invItem.iiName = ?1 AND ili.invItemLocation.invLocation.locName = ?2 AND ili.arInvoice.invDate <= ?3 AND ili.arInvoice.invAdBranch=?4 AND ili.iliAdCompany = ?5");
            query.setParameter(1, II_NM);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, INV_DT);
            query.setParameter(4, INV_AD_BRNCH);
            query.setParameter(5, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findUnpostedInvcByIiNameAndLocNameAndLessEqualDateAndInvAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedRctByIiNameAndLocNameAndLessEqualDateAndRctAdBranch(java.lang.String II_NM,
                                                                                                java.lang.String LOC_NM, java.util.Date RCT_DT, java.lang.Integer RCT_AD_BRNCH,
                                                                                                java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arReceipt.rctPosted = 0 AND ili.arReceipt.rctVoid = 0 AND ili.invItemLocation.invItem.iiName = ?1 AND ili.invItemLocation.invLocation.locName = ?2 AND ili.arReceipt.rctDate <= ?3 AND ili.arReceipt.rctAdBranch=?4 AND ili.iliAdCompany = ?5");
            query.setParameter(1, II_NM);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, RCT_DT);
            query.setParameter(4, RCT_AD_BRNCH);
            query.setParameter(5, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findUnpostedRctByIiNameAndLocNameAndLessEqualDateAndRctAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date RCT_DT, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedInvcByLocNameAndAdBranch(java.lang.String LOC_NM,
                                                                     java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invPosted = 0 AND ili.arInvoice.invVoid = 0 AND ili.invItemLocation.invLocation.locName = ?1 AND ili.arInvoice.invAdBranch = ?2 AND ili.iliAdCompany = ?3");
            query.setParameter(1, LOC_NM);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findUnpostedInvcByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCodeAndIlIiLvCtgry(java.lang.Integer INV_CODE, java.lang.String IL_II_LV_CTGRY,
                                                            java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invCode=?1 AND ili.invItemLocation.invItem.iiAdLvCategory=?2 AND ili.iliAdCompany=?3");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, IL_II_LV_CTGRY);
            query.setParameter(3, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByInvCodeAndIlIiLvCtgry(java.lang.Integer INV_CODE, java.lang.String IL_II_LV_CTGRY, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findInvcByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE,
                                                                      java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arInvoice.invVoid = 0 AND ili.invItemLocation.invItem.iiCode=?1 AND ili.invItemLocation.invLocation.locName = ?2 AND ili.arInvoice.invAdBranch = ?3 AND ili.iliAdCompany = ?4");
            query.setParameter(1, II_CODE);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findInvcByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer INV_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalArInvoiceLineItem findByRctCodeAndIlCodeAndUomName(java.lang.Integer RCT_CODE,
                                                                   java.lang.Integer INV_IL_CODE, java.lang.String UOM_NM, java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arReceipt.rctCode=?1 AND ili.invItemLocation.ilCode=?2 AND ili.invUnitOfMeasure.uomName=?3 AND ili.iliAdCompany=?4");
            query.setParameter(1, RCT_CODE);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, UOM_NM);
            query.setParameter(4, ILI_AD_CMPNY);
            return (LocalArInvoiceLineItem) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ar.LocalArInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(java.lang.Integer RCT_CODE, java.lang.Integer INV_IL_CODE, java.lang.String UOM_NM, java.lang.Integer ILI_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findByRctCodeAndIlCodeAndUomName(java.lang.Integer RCT_CODE, java.lang.Integer INV_IL_CODE, java.lang.String UOM_NM, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedRctByLocNameAndAdBranch(java.lang.String LOC_NM,
                                                                    java.lang.Integer RCT_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ili) FROM ArInvoiceLineItem ili WHERE ili.arReceipt.rctPosted = 0 AND ili.arReceipt.rctVoid = 0 AND ili.invItemLocation.invLocation.locName = ?1 AND ili.arReceipt.rctAdBranch = ?2 AND ili.iliAdCompany = ?3");
            query.setParameter(1, LOC_NM);
            query.setParameter(2, RCT_AD_BRNCH);
            query.setParameter(3, ILI_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoiceLineItemHome.findUnpostedRctByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer RCT_AD_BRNCH, java.lang.Integer ILI_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getIliByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
            throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalArInvoiceLineItem buildInvoiceLineItem(String companyShortName) throws CreateException {

        try {

            LocalArInvoiceLineItem entity = new LocalArInvoiceLineItem();

            Debug.print("ArInvoiceLineItemBean buildInvoiceLineItem");

            entity.setIliLine(ILI_LN);
            entity.setIliQuantity(ILI_QTY);
            entity.setIliUnitPrice(ILI_UNT_PRC);
            entity.setIliAmount(ILI_AMNT);
            entity.setIliTaxAmount(ILI_TX_AMNT);
            entity.setIliDiscount1(ILI_DSCNT_1);
            entity.setIliDiscount2(ILI_DSCNT_2);
            entity.setIliDiscount3(ILI_DSCNT_3);
            entity.setIliDiscount4(ILI_DSCNT_4);
            entity.setIliTotalDiscount(TTL_ILI_DSCNT);
            entity.setIliTax(ILI_TX);
            entity.setIliAdCompany(ILI_AD_CMPNY);

            em.persist(entity, companyShortName);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }


    public LocalArInvoiceLineItem buildInvoiceLineItem() throws CreateException {

        try {

            LocalArInvoiceLineItem entity = new LocalArInvoiceLineItem();

            Debug.print("ArInvoiceLineItemBean buildInvoiceLineItem");

            entity.setIliLine(ILI_LN);
            entity.setIliQuantity(ILI_QTY);
            entity.setIliUnitPrice(ILI_UNT_PRC);
            entity.setIliAmount(ILI_AMNT);
            entity.setIliTaxAmount(ILI_TX_AMNT);
            entity.setIliDiscount1(ILI_DSCNT_1);
            entity.setIliDiscount2(ILI_DSCNT_2);
            entity.setIliDiscount3(ILI_DSCNT_3);
            entity.setIliDiscount4(ILI_DSCNT_4);
            entity.setIliTotalDiscount(TTL_ILI_DSCNT);
            entity.setIliTax(ILI_TX);
            entity.setIliAdCompany(ILI_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoiceLineItemHome IliLine(short ILI_LN) {

        this.ILI_LN = ILI_LN;
        return this;
    }

    public LocalArInvoiceLineItemHome IliQuantity(double ILI_QTY) {

        this.ILI_QTY = ILI_QTY;
        return this;
    }

    public LocalArInvoiceLineItemHome IliUnitPrice(double ILI_UNT_PRC) {

        this.ILI_UNT_PRC = ILI_UNT_PRC;
        return this;
    }

    public LocalArInvoiceLineItemHome IliAmount(double ILI_AMNT) {

        this.ILI_AMNT = ILI_AMNT;
        return this;
    }

    public LocalArInvoiceLineItemHome IliTaxAmount(double ILI_TX_AMNT) {

        this.ILI_TX_AMNT = ILI_TX_AMNT;
        return this;
    }

    public LocalArInvoiceLineItemHome IliDiscount1(double ILI_DSCNT_1) {

        this.ILI_DSCNT_1 = ILI_DSCNT_1;
        return this;
    }

    public LocalArInvoiceLineItemHome IliDiscount2(double ILI_DSCNT_2) {

        this.ILI_DSCNT_2 = ILI_DSCNT_2;
        return this;
    }

    public LocalArInvoiceLineItemHome IliDiscount3(double ILI_DSCNT_3) {

        this.ILI_DSCNT_3 = ILI_DSCNT_3;
        return this;
    }

    public LocalArInvoiceLineItemHome IliDiscount4(double ILI_DSCNT_4) {

        this.ILI_DSCNT_4 = ILI_DSCNT_4;
        return this;
    }

    public LocalArInvoiceLineItemHome IliTotalDiscount(double TTL_ILI_DSCNT) {

        this.TTL_ILI_DSCNT = TTL_ILI_DSCNT;
        return this;
    }

    public LocalArInvoiceLineItemHome IliTax(byte ILI_TX) {

        this.ILI_TX = ILI_TX;
        return this;
    }

    public LocalArInvoiceLineItemHome IliAdCompany(Integer ILI_AD_CMPNY) {

        this.ILI_AD_CMPNY = ILI_AD_CMPNY;
        return this;
    }

    public LocalArInvoiceLineItem create(java.lang.Integer ILI_CODE, short ILI_LN, double ILI_QTY,
                                         double ILI_UNT_PRC, double ILI_AMNT, double ILI_TX_AMNT, byte ILI_ENBL_AT_BLD, double ILI_DSCNT_1,
                                         double ILI_DSCNT_2, double ILI_DSCNT_3, double ILI_DSCNT_4, double TTL_ILI_DSCNT, byte ILI_TX,
                                         Integer ILI_AD_CMPNY) throws CreateException {

        try {

            LocalArInvoiceLineItem entity = new LocalArInvoiceLineItem();

            Debug.print("ArInvoiceLineItemBean create");
            entity.setIliCode(ILI_CODE);
            entity.setIliLine(ILI_LN);
            entity.setIliQuantity(ILI_QTY);
            entity.setIliUnitPrice(ILI_UNT_PRC);
            entity.setIliAmount(ILI_AMNT);
            entity.setIliTaxAmount(ILI_TX_AMNT);
            entity.setIliDiscount1(ILI_DSCNT_1);
            entity.setIliDiscount2(ILI_DSCNT_2);
            entity.setIliDiscount3(ILI_DSCNT_3);
            entity.setIliDiscount4(ILI_DSCNT_4);
            entity.setIliTotalDiscount(TTL_ILI_DSCNT);
            entity.setIliTax(ILI_TX);
            entity.setIliAdCompany(ILI_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoiceLineItem create(short ILI_LN, double ILI_QTY, double ILI_UNT_PRC, double ILI_AMNT,
                                         double ILI_TX_AMNT, byte ILI_ENBL_AT_BLD, double ILI_DSCNT_1, double ILI_DSCNT_2, double ILI_DSCNT_3,
                                         double ILI_DSCNT_4, double TTL_ILI_DSCNT, byte ILI_TX, Integer ILI_AD_CMPNY) throws CreateException {

        try {

            LocalArInvoiceLineItem entity = new LocalArInvoiceLineItem();

            Debug.print("ArInvoiceLineItemBean create");
            entity.setIliLine(ILI_LN);
            entity.setIliQuantity(ILI_QTY);
            entity.setIliUnitPrice(ILI_UNT_PRC);
            entity.setIliAmount(ILI_AMNT);
            entity.setIliTaxAmount(ILI_TX_AMNT);
            entity.setIliDiscount1(ILI_DSCNT_1);
            entity.setIliDiscount2(ILI_DSCNT_2);
            entity.setIliDiscount3(ILI_DSCNT_3);
            entity.setIliDiscount4(ILI_DSCNT_4);
            entity.setIliTotalDiscount(TTL_ILI_DSCNT);
            entity.setIliTax(ILI_TX);
            entity.setIliAdCompany(ILI_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}