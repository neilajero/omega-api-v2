package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvCosting;
import com.util.Debug;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Stateless
public class LocalInvCostingHome {

    public static final String JNDI_NAME = "LocalInvCostingHome!com.ejb.inv.LocalInvCostingHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalInvCostingHome() {

    }

    // FINDER METHODS

    public LocalInvCosting findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvCosting entity = (LocalInvCosting) em
                    .find(new LocalInvCosting(), pk);
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

    public LocalInvCosting findByCstDateAndIlCode(java.util.Date CST_DT, java.lang.Integer INV_IL_CODE,
                                                  java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate=?1 AND cst.invItemLocation.ilCode=?2 AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4");
            query.setParameter(1, CST_DT);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, CST_AD_BRNCH);
            query.setParameter(4, CST_AD_CMPNY);
            return (LocalInvCosting) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvCostingHome.findByCstDateAndIlCode(java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIlCode(java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByIiNameAndLocNameAndAdjLineCode(java.util.Date CST_DT, java.lang.String II_NM,
                                                                     java.lang.String LOC_NM, java.lang.Integer AL_CODE, java.lang.Integer CST_AD_BRNCH,
                                                                     java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate<?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.invAdjustmentLine.alCode=?4 AND cst.cstAdBranch=?5 AND cst.cstAdCompany=?6");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, AL_CODE);
            query.setParameter(5, CST_AD_BRNCH);
            query.setParameter(6, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByIiNameAndLocNameAndAdjLineCode(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer AL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvCosting findByCstLineNumberAndCstDateAndIlCode(int CST_LN_NMBR, java.util.Date CST_DT,
                                                                  java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstLineNumber=?1 AND cst.cstDate=?2 AND cst.invItemLocation.ilCode=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_LN_NMBR);
            query.setParameter(2, CST_DT);
            query.setParameter(3, INV_IL_CODE);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return (LocalInvCosting) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvCostingHome.findByCstLineNumberAndCstDateAndIlCode(int CST_LN_NMBR, java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstLineNumberAndCstDateAndIlCode(int CST_LN_NMBR, java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByGreaterThanCstDateAndIiNameAndLocName(java.util.Date CST_DT,
                                                                            java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
                                                                            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate > ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate, cst.cstLineNumber");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstDateLessThanAndIiNameAndLocName(java.util.Date CST_DT, java.lang.String II_NM,
                                                                         java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }


    public java.util.Collection findByCstDateAndIiNameAndLocName(java.util.Date CST_DT, java.lang.String II_NM,
                                                                 java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate = ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstDateAndIiNameAndLocName2(java.util.Date CST_DT, java.lang.String II_NM,
                                                                  java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 AND cst.cstAdjustQuantity > 0");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocName2(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvCosting findLastReceiveDateByCstDateAndIiNameAndLocName(java.util.Date CST_DT,
                                                                           java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
                                                                           java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 AND (cst.cstAdjustQuantity > 0 OR cst.cstQuantityReceived > 0)");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            query.setFirstResult(0);
            query.setMaxResults(1);

            return (LocalInvCosting) query.getSingleResult();


        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvCostingHome.findLastReceiveDateByCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findLastReceiveDateByCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstDateGeneralOnlyByMonth(java.util.Date CST_DT_FRM, java.util.Date CST_DT_TO,
                                                                java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate >= ?1 AND cst.cstDate <= ?2 AND cst.cstAdCompany = ?3 AND cst.cstAdjustQuantity > 0 ORDER BY cst.invAdjustmentLine.invAdjustment.adjCode,cst.invAdjustmentLine.invItemLocation.invItem.iiName");
            query.setParameter(1, CST_DT_FRM);
            query.setParameter(2, CST_DT_TO);
            query.setParameter(3, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateGeneralOnlyByMonth(java.com.util.Date CST_DT_FRM,java.com.util.Date CST_DT_TO,java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstDateAndIiNameAndLocName3(java.util.Date CST_DT, java.lang.String II_NM,
                                                                  java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 AND cst.cstAdjustQuantity < 0");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocName3(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findNegTxnByGreaterThanCstDateAndIiNameAndLocName(java.util.Date CST_DT,
                                                                                  java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
                                                                                  java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE (cst.cstAssemblyQuantity < 0 OR cst.cstAdjustQuantity < 0 OR cst.cstQuantitySold > 0) AND cst.cstDate > ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstDateFromAndCstDateToAndIlCode(java.util.Date CST_DT_FRM,
                                                                       java.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
                                                                       java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate >= ?1 AND cst.cstDate <= ?2 AND cst.invItemLocation.ilCode=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT_FRM);
            query.setParameter(2, CST_DT_TO);
            query.setParameter(3, INV_IL_CODE);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateFromAndCstDateToAndIlCode(java.com.util.Date CST_DT_FRM, java.com.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findQtySoldByCstDateFromAndCstDateToAndIlCode(java.util.Date CST_DT_FRM,
                                                                              java.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
                                                                              java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE (cst.arInvoiceLineItem.arReceipt.rctVoid = 0 OR cst.arInvoiceLineItem.arInvoice.invVoid=0 OR cst.arSalesOrderInvoiceLine.arInvoice.invVoid=0 OR cst.arJobOrderInvoiceLine.arInvoice.invVoid=0) AND cst.cstQuantitySold > 0 AND cst.cstDate >= ?1 AND cst.cstDate <= ?2 AND cst.invItemLocation.ilCode=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT_FRM);
            query.setParameter(2, CST_DT_TO);
            query.setParameter(3, INV_IL_CODE);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findQtySoldByCstDateFromAndCstDateToAndIlCode(java.com.util.Date CST_DT_FRM, java.com.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvCosting findNegativeRemainingQuantityByIlCodeAndBrCode(java.lang.Integer INV_IL_CODE,
                                                                          java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstRemainingQuantity < 0 AND cst.invItemLocation.ilCode=?1 AND cst.cstAdBranch = ?2 AND cst.cstAdCompany = ?3");
            query.setParameter(1, INV_IL_CODE);
            query.setParameter(2, CST_AD_BRNCH);
            query.setParameter(3, CST_AD_CMPNY);
            return (LocalInvCosting) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvCostingHome.findNegativeRemainingQuantityByIlCodeAndBrCode(java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findNegativeRemainingQuantityByIlCodeAndBrCode(java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalInvCosting findByCstDateAndIiNameAndLocNameLimit1(java.util.Date CST_DT, java.lang.String II_NM,
                                                                  java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate = ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5");
            query.setParameter(1, CST_DT);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, CST_AD_BRNCH);
            query.setParameter(5, CST_AD_CMPNY);
            return (LocalInvCosting) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocNameLimit1(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByCstDateAndIiNameAndLocNameLimit1(java.com.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(
            java.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstRemainingLifoQuantity > 0 AND cst.cstDate <= ?1 AND cst.invItemLocation.ilCode=?2 AND cst.cstAdBranch=?3 AND cst.cstAdCompany=?4 ORDER BY cst.cstDate");
            query.setParameter(1, CST_DT);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, CST_AD_BRNCH);
            query.setParameter(4, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode2(
            java.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstRemainingLifoQuantity > 0 AND cst.cstDate <= ?1 AND cst.invItemLocation.ilCode=?2 AND cst.cstAdBranch=?3 AND cst.cstAdCompany=?4 ORDER BY cst.cstDate");
            query.setParameter(1, CST_DT);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, CST_AD_BRNCH);
            query.setParameter(4, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode2(java.com.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPriorEqualCstDateToAndIlCode(java.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE,
                                                                   java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.ilCode=?2 AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4");
            query.setParameter(1, CST_DT_TO);
            query.setParameter(2, INV_IL_CODE);
            query.setParameter(3, CST_AD_BRNCH);
            query.setParameter(4, CST_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.inv.LocalInvCostingHome.findByPriorEqualCstDateToAndIlCode(java.com.util.Date CST_DT_TO, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH, java.lang.Integer CST_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getCstByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

    public java.util.Collection getCstByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
                                                 java.lang.Integer LIMIT, java.lang.Integer OFFSET, String companyShortName) throws FinderException {

        try {
            Query query = em.createQueryPerCompany(jbossQl, companyShortName);
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

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(
            java.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND  cst.cstRemainingQuantity <> 0 AND cst.invItemLocation.ilCode=?2  AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, INV_IL_CODE);
            q.setParameter(3, CST_AD_BRNCH);
            q.setParameter(4, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();

            Debug.print(entity.getClass()
                    + " - getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode");

            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(
            java.util.Date CST_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.ilCode=?2 AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, INV_IL_CODE);
            q.setParameter(3, CST_AD_BRNCH);
            q.setParameter(4, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(
                    entity.getClass() + " - getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode");

            return entity;
        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(long CST_DT_TO_LNG,
                                                                                    java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
                                                                                    java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDateToLong=?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC, cst.cstDateToLong DESC");
            q.setParameter(1, CST_DT_TO_LNG);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(entity.getClass() + " - getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName");

            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(long CST_DT_TO_LNG,
                                                                                    java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
                                                                                    java.lang.Integer CST_AD_CMPNY, String companyShortName) throws FinderException {

        try {

            Query q = em.createQueryPerCompany(
                    "SELECT OBJECT(cst) FROM InvCosting cst "
                            + "WHERE cst.cstDateToLong=?1 "
                            + "AND cst.invItemLocation.invItem.iiName=?2 "
                            + "AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 "
                            + "AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC, cst.cstDateToLong DESC",
                    companyShortName);
            q.setParameter(1, CST_DT_TO_LNG);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(entity.getClass() + " - getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName");

            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanCstDateAndIiNameAndLocName(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate < ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(entity.getClass()
                    + " - getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanCstDateAndIiNameAndLocName");

            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocNameAndFifoRemainingQuantity(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstRemainingLifoQuantity > 0 AND cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, CST_AD_BRNCH);
            q.setParameter(4, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(entity.getClass()
                    + " - getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocNameAndFifoRemainingQuantity");

            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public double getRemainingQtyToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT SUM(cst.cstQuantity) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstDateToLong DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            double qty = (double) q.getSingleResult();
            Debug.print("getRemainingQtyToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName");

            return qty;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstDateToLong DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);

            return (LocalInvCosting) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getItemAverageCost(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst "
                            + "WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 "
                            + "AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 "
                            + "AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstDateToLong DESC, cst.cstLineNumber DESC");
            q.setParameter(1, CST_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);

            return (LocalInvCosting) q.getSingleResult();

        }
        catch (NoResultException e) {
            return null;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public LocalInvCosting getItemAverageCost(
            java.util.Date CST_DT, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY, String companyShortName) {

        try {

            Query q = em.createQueryPerCompany(
                    "SELECT OBJECT(cst) FROM InvCosting cst "
                            + "WHERE cst.cstDate <= ?1 AND cst.invItemLocation.invItem.iiName=?2 "
                            + "AND cst.invItemLocation.invLocation.locName=?3 AND cst.cstAdBranch = ?4 "
                            + "AND cst.cstAdCompany = ?5 ORDER BY cst.cstDate DESC, cst.cstDateToLong DESC, cst.cstLineNumber DESC",
                    companyShortName);
            q.setParameter(1, CST_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, CST_AD_BRNCH);
            q.setParameter(5, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);

            return (LocalInvCosting) q.getSingleResult();

        }
        catch (NoResultException e) {
            return null;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndRemainingQuantityNotEqualToZeroAndIiNameAndLocName(
            java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.invItemLocation.invItem.iiName=?1 AND cst.invItemLocation.invLocation.locName=?2 AND cst.cstRemainingQuantity <> 0 AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4 ORDER BY cst.cstDate DESC, cst.cstLineNumber DESC");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, CST_AD_BRNCH);
            q.setParameter(4, CST_AD_CMPNY);
            q.setFirstResult(0);
            q.setMaxResults(1);
            LocalInvCosting entity = (LocalInvCosting) q.getSingleResult();
            Debug.print(entity.getClass()
                    + " - getByMaxCstDateToLongAndMaxCstLineNumberAndRemainingQuantityNotEqualToZeroAndIiNameAndLocName");
            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalInvCosting getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(
            java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer CST_AD_BRNCH,
            java.lang.Integer CST_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(cst) FROM InvCosting cst WHERE cst.invItemLocation.invItem.iiName=?1 AND cst.invItemLocation.invLocation.locName=?2 AND cst.cstAdBranch = ?3 AND cst.cstAdCompany = ?4 ORDER BY cst.cstDate DESC, cst.cstDateToLong DESC, cst.cstLineNumber DESC");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, CST_AD_BRNCH);
            q.setParameter(4, CST_AD_CMPNY);
            q.setMaxResults(1);

            return (LocalInvCosting) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    // CREATE METHODS

    public LocalInvCosting create(java.util.Date CST_DT, long CST_DT_TO_LNG,
                                  int CST_LN_NMBR, double CST_QTY_RCVD, double CST_ITM_CST, double CST_ASSMBLY_QTY,
                                  double CST_ASSMBLY_CST, double CST_ADJST_QTY, double CST_ADJST_CST,
                                  double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_QTY,
                                  double CST_VRNC_VL, double CST_RMNNG_LIFO_QTY, Integer CST_AD_BRNCH, Integer CST_AD_CMPNY)
            throws CreateException {

        try {

            LocalInvCosting entity = new LocalInvCosting();

            Debug.print("invCostingBean ejbCreate");
            entity.setCstDate(CST_DT);
            entity.setCstDateToLong(CST_DT_TO_LNG);
            entity.setCstLineNumber(CST_LN_NMBR);
            entity.setCstQuantityReceived(CST_QTY_RCVD);
            entity.setCstItemCost(CST_ITM_CST);
            entity.setCstAssemblyQuantity(CST_ASSMBLY_QTY);
            entity.setCstAssemblyCost(CST_ASSMBLY_CST);
            entity.setCstAdjustQuantity(CST_ADJST_QTY);
            entity.setCstAdjustCost(CST_ADJST_CST);
            entity.setCstQuantitySold(CST_QTY_SLD);
            entity.setCstCostOfSales(CST_CST_OF_SLS);
            entity.setCstRemainingQuantity(CST_RMNNG_QTY);
            entity.setCstRemainingValue(CST_RMNNG_VL);
            entity.setCstVarianceQuantity(CST_VRNC_QTY);
            entity.setCstVarianceValue(CST_VRNC_VL);
            entity.setCstRemainingLifoQuantity(CST_RMNNG_LIFO_QTY);
            entity.setCstAdBranch(CST_AD_BRNCH);
            entity.setCstAdCompany(CST_AD_CMPNY);
            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvCosting create(java.lang.Integer INV_CST_CODE, java.util.Date CST_DT, long CST_DT_TO_LNG,
                                  int CST_LN_NMBR, double CST_QTY_RCVD, double CST_ITM_CST,
                                  double CST_ADJST_QTY, double CST_ADJST_CST, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY,
                                  double CST_RMNNG_VL, double CST_VRNC_QTY, double CST_VRNC_VL, double CST_RMNNG_LIFO_QTY,
                                  Integer CST_AD_BRNCH, Integer CST_AD_CMPNY) throws CreateException {

        try {

            LocalInvCosting entity = new LocalInvCosting();

            Debug.print("invCostingBean create");
            entity.setCstCode(INV_CST_CODE);
            entity.setCstDate(CST_DT);
            entity.setCstDateToLong(CST_DT_TO_LNG);
            entity.setCstLineNumber(CST_LN_NMBR);
            entity.setCstQuantityReceived(CST_QTY_RCVD);
            entity.setCstItemCost(CST_ITM_CST);
            entity.setCstAdjustQuantity(CST_ADJST_QTY);
            entity.setCstAdjustCost(CST_ADJST_CST);
            entity.setCstQuantitySold(CST_QTY_SLD);
            entity.setCstCostOfSales(CST_CST_OF_SLS);
            entity.setCstRemainingQuantity(CST_RMNNG_QTY);
            entity.setCstRemainingValue(CST_RMNNG_VL);
            entity.setCstVarianceQuantity(CST_VRNC_QTY);
            entity.setCstVarianceValue(CST_VRNC_VL);
            entity.setCstRemainingLifoQuantity(CST_RMNNG_LIFO_QTY);
            entity.setCstAdBranch(CST_AD_BRNCH);
            entity.setCstAdCompany(CST_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvCosting create(java.util.Date CST_DT, long CST_DT_TO_LNG, int CST_LN_NMBR,
                                  double CST_QTY_RCVD, double CST_ITM_CST,
                                  double CST_ADJST_QTY, double CST_ADJST_CST, double CST_QTY_SLD, double CST_CST_OF_SLS, double CST_RMNNG_QTY,
                                  double CST_RMNNG_VL, double CST_VRNC_QTY, double CST_VRNC_VL, double CST_RMNNG_LIFO_QTY,
                                  Integer CST_AD_BRNCH, Integer CST_AD_CMPNY) throws CreateException {

        try {

            LocalInvCosting entity = new LocalInvCosting();

            Debug.print("invCostingBean create");
            entity.setCstDate(CST_DT);
            entity.setCstDateToLong(CST_DT_TO_LNG);
            entity.setCstLineNumber(CST_LN_NMBR);
            entity.setCstQuantityReceived(CST_QTY_RCVD);
            entity.setCstItemCost(CST_ITM_CST);
            entity.setCstAdjustQuantity(CST_ADJST_QTY);
            entity.setCstAdjustCost(CST_ADJST_CST);
            entity.setCstQuantitySold(CST_QTY_SLD);
            entity.setCstCostOfSales(CST_CST_OF_SLS);
            entity.setCstRemainingQuantity(CST_RMNNG_QTY);
            entity.setCstRemainingValue(CST_RMNNG_VL);
            entity.setCstVarianceQuantity(CST_VRNC_QTY);
            entity.setCstVarianceValue(CST_VRNC_VL);
            entity.setCstRemainingLifoQuantity(CST_RMNNG_LIFO_QTY);
            entity.setCstAdBranch(CST_AD_BRNCH);
            entity.setCstAdCompany(CST_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}