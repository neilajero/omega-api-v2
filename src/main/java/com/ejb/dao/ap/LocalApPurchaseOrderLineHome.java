package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;;
import java.util.Date;

@Stateless
public class LocalApPurchaseOrderLineHome {

    public static final String JNDI_NAME = "LocalApPurchaseOrderLineHome!com.ejb.ap.LocalApPurchaseOrderLineHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalApPurchaseOrderLineHome() {

    }

    // FINDER METHODS

    public LocalApPurchaseOrderLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalApPurchaseOrderLine entity = (LocalApPurchaseOrderLine) em
                    .find(new LocalApPurchaseOrderLine(), pk);
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

    public java.util.Collection findByPlPlCode(java.lang.Integer P_PL_CODE, java.lang.Integer CHK_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.plPlCode = ?1 AND pl.plAdCompany = ?2");
            query.setParameter(1, P_PL_CODE);
            query.setParameter(2, CHK_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findByPlPlCode(java.lang.Integer P_PL_CODE, java.lang.Integer CHK_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findRrForProcessing(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
            throws FinderException {

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_FindVoucherForProcessing");
            query.registerStoredProcedureParameter("branchCode", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("companyCode", Integer.class, ParameterMode.IN);
            query.setParameter("branchCode", AD_BRNCH);
            query.setParameter("companyCode", AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(java.lang.Integer INV_IL_CODE,
                                                                                 byte PO_RCVNG, byte PO_PSTD, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.invItemLocation.ilCode=?1 AND pl.apPurchaseOrder.poReceiving=?2 AND pl.apPurchaseOrder.poPosted=?3 AND pl.apPurchaseOrder.poAdBranch = ?4 AND pl.plAdCompany = ?5");
            query.setParameter(1, INV_IL_CODE);
            query.setParameter(2, PO_RCVNG);
            query.setParameter(3, PO_PSTD);
            query.setParameter(4, PO_AD_BRNCH);
            query.setParameter(5, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(java.lang.Integer INV_IL_CODE, byte PO_RCVNG, byte PO_PSTD, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenPlBySplSupplierCodeAndBrCode(java.lang.String SPL_SPPLR_CODE,
                                                                     java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 1 AND pl.apPurchaseOrder.poReceiving = 1 AND pl.apVoucher IS NULL AND pl.apPurchaseOrder.apSupplier.splSupplierCode = ?1 AND pl.apPurchaseOrder.poAdBranch = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, SPL_SPPLR_CODE);
            query.setParameter(2, PL_AD_BRNCH);
            query.setParameter(3, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findOpenPlBySplSupplierCodeAndBrCode(java.lang.String SPL_SPPLR_CODE, java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenPlByPoDcmntNmbrAndBrCode(java.lang.String PO_DCMNT_NMBR,
                                                                 java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 1 AND pl.apPurchaseOrder.poReceiving = 1 AND pl.apVoucher IS NULL AND pl.apPurchaseOrder.poDocumentNumber = ?1 AND pl.apPurchaseOrder.poAdBranch = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, PO_DCMNT_NMBR);
            query.setParameter(2, PL_AD_BRNCH);
            query.setParameter(3, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findOpenPlByPoDcmntNmbrAndBrCode(java.lang.String PO_DCMNT_NMBR, java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenPlByBrCode(java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 1 AND pl.apPurchaseOrder.poReceiving = 1 AND pl.apVoucher IS NULL AND pl.apPurchaseOrder.poAdBranch = ?1 AND pl.plAdCompany = ?2");
            query.setParameter(1, PL_AD_BRNCH);
            query.setParameter(2, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findOpenPlByBrCode(java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenPlBySplSupplierCodeAndBrCode1(java.lang.String SPL_SPPLR_CODE,
                                                                      java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 1 AND pl.apVoucher IS NULL AND pl.apPurchaseOrder.apSupplier.splSupplierCode = ?1 AND pl.apPurchaseOrder.poAdBranch = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, SPL_SPPLR_CODE);
            query.setParameter(2, PL_AD_BRNCH);
            query.setParameter(3, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findOpenPlBySplSupplierCodeAndBrCode1(java.lang.String SPL_SPPLR_CODE, java.lang.Integer PL_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPlIlCodeAndPoReceivingAndBrCode(java.lang.Integer INV_IL_CODE, byte PO_RCVNG,
                                                                      java.lang.Integer PL_AD_BRNCH, java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.invItemLocation.ilCode=?1 AND pl.apPurchaseOrder.poReceiving=?2 AND pl.apPurchaseOrder.poAdBranch = ?3 AND pl.plAdCompany = ?4");
            query.setParameter(1, INV_IL_CODE);
            query.setParameter(2, PO_RCVNG);
            query.setParameter(3, PL_AD_BRNCH);
            query.setParameter(4, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndBrCode(java.lang.Integer INV_IL_CODE, byte PO_RCVNG, java.lang.Integer PL_AD_BRNCH, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(java.lang.String II_NM,
                                                                                              java.lang.String LOC_NM, java.util.Date PO_DT, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 0 AND pl.apPurchaseOrder.poVoid = 0 AND pl.apPurchaseOrder.poReceiving = 1 AND pl.invItemLocation.invItem.iiName = ?1 AND pl.invItemLocation.invLocation.locName = ?2 AND pl.apPurchaseOrder.poDate <= ?3 AND pl.apPurchaseOrder.poAdBranch=?4 AND pl.plAdCompany = ?5");
            query.setParameter(1, II_NM);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, PO_DT);
            query.setParameter(4, PO_AD_BRNCH);
            query.setParameter(5, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findUnpostedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date PO_DT, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnservedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(java.lang.String II_NM,
                                                                                              java.lang.String LOC_NM, java.util.Date PO_DT, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poReceiving=0 AND pl.apPurchaseOrder.poVoid=0 AND pl.invItemLocation.invItem.iiName = ?1 AND pl.invItemLocation.invLocation.locName = ?2 AND pl.apPurchaseOrder.poDate <= ?3 AND pl.apPurchaseOrder.poAdBranch=?4 AND pl.plAdCompany = ?5");
            query.setParameter(1, II_NM);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, PO_DT);
            query.setParameter(4, PO_AD_BRNCH);
            query.setParameter(5, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findUnservedPoByIiNameAndLocNameAndLessEqualDateAndPoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date PO_DT, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedPoByLocNameAndAdBranch(java.lang.String LOC_NM,
                                                                   java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted = 0 AND pl.apPurchaseOrder.poVoid = 0 AND pl.invItemLocation.invLocation.locName = ?1 AND pl.apPurchaseOrder.poAdBranch = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, LOC_NM);
            query.setParameter(2, PO_AD_BRNCH);
            query.setParameter(3, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findUnpostedPoByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPoByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM,
                                                                    java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poVoid = 0 AND pl.invItemLocation.invItem.iiCode=?1 AND pl.invItemLocation.invLocation.locName = ?2 AND pl.apPurchaseOrder.poAdBranch = ?3 AND pl.plAdCompany = ?4");
            query.setParameter(1, II_CODE);
            query.setParameter(2, LOC_NM);
            query.setParameter(3, PO_AD_BRNCH);
            query.setParameter(4, PL_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findPoByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalApPurchaseOrderLine findPlByPoNumberAndIiNameAndLocName(java.lang.String PO_DCMNT_NMBR,
                                                                        java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PO_AD_BRNCH,
                                                                        java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poVoid = 0 AND pl.apPurchaseOrder.poDocumentNumber = ?1 AND pl.invItemLocation.invItem.iiName = ?2 AND pl.invItemLocation.invLocation.locName = ?3 AND pl.apPurchaseOrder.poAdBranch = ?4 AND pl.plAdCompany = ?5");
            query.setParameter(1, PO_DCMNT_NMBR);
            query.setParameter(2, II_NM);
            query.setParameter(3, LOC_NM);
            query.setParameter(4, PO_AD_BRNCH);
            query.setParameter(5, PL_AD_CMPNY);
            return (LocalApPurchaseOrderLine) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.ap.LocalApPurchaseOrderLineHome.findPlByPoNumberAndIiNameAndLocName(java.lang.String PO_DCMNT_NMBR, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findPlByPoNumberAndIiNameAndLocName(java.lang.String PO_DCMNT_NMBR, java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByPoReceivingAndPoShipmentAndBrCode(byte PO_RCVNG, java.lang.String PO_SHPMNT_NMBR,
                                                                        java.lang.Integer PO_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poPosted=1 AND pl.apPurchaseOrder.poReceiving=?1 AND pl.apPurchaseOrder.poShipmentNumber = ?2 AND pl.plAdCompany = ?3");
            query.setParameter(1, PO_RCVNG);
            query.setParameter(2, PO_SHPMNT_NMBR);
            query.setParameter(3, PO_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApPurchaseOrderLineHome.findByPoReceivingAndPoShipmentAndBrCode(byte PO_RCVNG, java.lang.String PO_SHPMNT_NMBR, java.lang.Integer PO_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getPolByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

    // OTHER METHODS

    public java.util.Date ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSupplierCode(
            java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.lang.String SPL_SPPLR_CD,
            java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT MAX(pl.apPurchaseOrder.poDate) FROM ApPurchaseOrderLine pl WHERE pl.invItemLocation.invItem.iiName=?1 AND pl.invItemLocation.invLocation.locName=?2 AND pl.apPurchaseOrder.poReceiving=?3 AND pl.apPurchaseOrder.poPosted=?4 AND pl.apPurchaseOrder.apSupplier.splSupplierCode=?5 AND pl.apPurchaseOrder.poAdBranch = ?6 AND pl.plAdCompany = ?7");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, PO_RCVNG);
            q.setParameter(4, PO_PSTD);
            q.setParameter(5, SPL_SPPLR_CD);
            q.setParameter(6, PO_AD_BRNCH);
            q.setParameter(7, PL_AD_CMPNY);

            return (Date) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public Integer ejbSelectMaxPlCodeByPoDateAndIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSupplierCode(
            java.util.Date PO_DT, java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD,
            java.lang.String SPL_SPPLR_CD, java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT MAX(pl.plCode) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poDate=?1 AND pl.invItemLocation.invItem.iiName=?2 AND pl.invItemLocation.invLocation.locName=?3 AND pl.apPurchaseOrder.poReceiving=?4 AND pl.apPurchaseOrder.poPosted=?5 AND pl.apPurchaseOrder.apSupplier.splSupplierCode=?6 AND pl.apPurchaseOrder.poAdBranch = ?7 AND pl.plAdCompany = ?8");
            q.setParameter(1, PO_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, PO_RCVNG);
            q.setParameter(5, PO_PSTD);
            q.setParameter(6, SPL_SPPLR_CD);
            q.setParameter(7, PO_AD_BRNCH);
            q.setParameter(8, PL_AD_CMPNY);

            return (Integer) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public java.util.Date ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPosted(java.lang.String II_NM,
                                                                                        java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.lang.Integer PO_AD_BRNCH,
                                                                                        java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT MAX(pl.apPurchaseOrder.poDate) FROM ApPurchaseOrderLine pl WHERE pl.invItemLocation.invItem.iiName=?1 AND pl.invItemLocation.invLocation.locName=?2 AND pl.apPurchaseOrder.poReceiving=?3 AND pl.apPurchaseOrder.poPosted=?4 AND pl.apPurchaseOrder.poAdBranch = ?5 AND pl.plAdCompany = ?6");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, PO_RCVNG);
            q.setParameter(4, PO_PSTD);
            q.setParameter(5, PO_AD_BRNCH);
            q.setParameter(6, PL_AD_CMPNY);

            return (Date) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public Integer ejbSelectMaxPlCodeByPoDateAndIiNameAndLocNameAndPoReceivingAndPoPosted(java.util.Date PO_DT,
                                                                                          java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.lang.Integer PO_AD_BRNCH,
                                                                                          java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT MAX(pl.plCode) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poDate=?1 AND pl.invItemLocation.invItem.iiName=?2 AND pl.invItemLocation.invLocation.locName=?3 AND pl.apPurchaseOrder.poReceiving=?4 AND pl.apPurchaseOrder.poPosted=?5 AND pl.apPurchaseOrder.poAdBranch = ?6 AND pl.plAdCompany = ?7");
            q.setParameter(1, PO_DT);
            q.setParameter(2, II_NM);
            q.setParameter(3, LOC_NM);
            q.setParameter(4, PO_RCVNG);
            q.setParameter(5, PO_PSTD);
            q.setParameter(6, PO_AD_BRNCH);
            q.setParameter(7, PL_AD_CMPNY);

            return (Integer) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public java.util.Date ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(
            java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.util.Date PO_DT,
            java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT MAX(pl.apPurchaseOrder.poDate) FROM ApPurchaseOrderLine pl WHERE pl.invItemLocation.invItem.iiName=?1 AND pl.invItemLocation.invLocation.locName=?2 AND pl.apPurchaseOrder.poReceiving=?3 AND pl.apPurchaseOrder.poPosted=?4 AND pl.apPurchaseOrder.poDate <= ?5 AND pl.apPurchaseOrder.poAdBranch = ?6 AND pl.plAdCompany = ?7");
            q.setParameter(1, II_NM);
            q.setParameter(2, LOC_NM);
            q.setParameter(3, PO_RCVNG);
            q.setParameter(4, PO_PSTD);
            q.setParameter(5, PO_DT);
            q.setParameter(6, PO_AD_BRNCH);
            q.setParameter(7, PL_AD_CMPNY);

            return (Date) q.getSingleResult();

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalApPurchaseOrderLine ejbSelectByPlCode(java.lang.Integer PL_CD, java.lang.Integer PL_AD_CMPNY)
            throws FinderException {

        try {

            Query q = em.createQuery(
                    "SELECT OBJECT(pl) FROM ApPurchaseOrderLine pl WHERE pl.plCode=?1 AND pl.plAdCompany = ?2");
            q.setParameter(1, PL_CD);
            q.setParameter(2, PL_AD_CMPNY);

            LocalApPurchaseOrderLine entity = (LocalApPurchaseOrderLine) q.getSingleResult();
            Debug.print(entity.getClass() + " - ejbSelectByPlCode");
            return entity;

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception e) {

            throw e;

        }

    }

    public LocalApPurchaseOrderLine getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSpplrCode(
            java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.lang.String SPL_SPPLR_CD,
            java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Date poDate = ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSupplierCode(II_NM, LOC_NM,
                    PO_RCVNG, PO_PSTD, SPL_SPPLR_CD, PO_AD_BRNCH, PL_AD_CMPNY);
            Integer cstLineNumber = ejbSelectMaxPlCodeByPoDateAndIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSupplierCode(
                    poDate, II_NM, LOC_NM, PO_RCVNG, PO_PSTD, SPL_SPPLR_CD, PO_AD_BRNCH, PL_AD_CMPNY);
            return ejbSelectByPlCode(cstLineNumber, PL_AD_CMPNY);

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception ex) {

            throw ex;

        }

    }

    public LocalApPurchaseOrderLine getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPosted(
            java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.lang.Integer PO_AD_BRNCH,
            java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Date poDate = ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPosted(II_NM, LOC_NM, PO_RCVNG,
                    PO_PSTD, PO_AD_BRNCH, PL_AD_CMPNY);
            Integer cstLineNumber = ejbSelectMaxPlCodeByPoDateAndIiNameAndLocNameAndPoReceivingAndPoPosted(poDate,
                    II_NM, LOC_NM, PO_RCVNG, PO_PSTD, PO_AD_BRNCH, PL_AD_CMPNY);
            return ejbSelectByPlCode(cstLineNumber, PL_AD_CMPNY);

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception ex) {

            throw ex;

        }

    }

    public LocalApPurchaseOrderLine getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(
            java.lang.String II_NM, java.lang.String LOC_NM, byte PO_RCVNG, byte PO_PSTD, java.util.Date PO_DT,
            java.lang.Integer PO_AD_BRNCH, java.lang.Integer PL_AD_CMPNY) throws FinderException {

        try {

            Date poDate = ejbSelectMaxPoDateByIiNameAndLocNameAndPoReceivingAndPoPostedAndLessThanEqualPoDate(II_NM,
                    LOC_NM, PO_RCVNG, PO_PSTD, PO_DT, PO_AD_BRNCH, PL_AD_CMPNY);
            Integer cstLineNumber = ejbSelectMaxPlCodeByPoDateAndIiNameAndLocNameAndPoReceivingAndPoPosted(poDate,
                    II_NM, LOC_NM, PO_RCVNG, PO_PSTD, PO_AD_BRNCH, PL_AD_CMPNY);
            return ejbSelectByPlCode(cstLineNumber, PL_AD_CMPNY);

        }
        catch (NoResultException e) {

            throw new FinderException(e.getMessage());

        }
        catch (Exception ex) {

            throw ex;

        }

    }

    // CREATE METHODS

    public LocalApPurchaseOrderLine create(java.lang.Integer AP_PL_CODE, short PL_LN, double PL_QTY,
                                           double PL_UNT_CST, double PL_AMNT, String PL_QC_NUM, Date PL_QC_EXPRY_DT, String PL_RMRKS, double PL_CNVRSN_FCTR,
                                           double PL_TX_AMNT, Integer PL_PL_CODE, double PL_DSCNT_1, double PL_DSCNT_2, double PL_DSCNT_3,
                                           double PL_DSCNT_4, double TTL_PL_DSCNT, Integer PL_AD_CMPNY) throws CreateException {

        try {

            LocalApPurchaseOrderLine entity = new LocalApPurchaseOrderLine();

            Debug.print("ApPurchaseOrderLineBean create");
            entity.setPlCode(AP_PL_CODE);
            entity.setPlLine(PL_LN);
            entity.setPlQuantity(PL_QTY);
            entity.setPlUnitCost(PL_UNT_CST);
            entity.setPlAmount(PL_AMNT);
            entity.setPlQcNumber(PL_QC_NUM);
            entity.setPlQcExpiryDate(PL_QC_EXPRY_DT);
            entity.setPlRemarks(PL_RMRKS);
            entity.setPlConversionFactor(PL_CNVRSN_FCTR);
            entity.setPlTaxAmount(PL_TX_AMNT);
            entity.setPlPlCode(PL_PL_CODE);
            entity.setPlDiscount1(PL_DSCNT_1);
            entity.setPlDiscount2(PL_DSCNT_2);
            entity.setPlDiscount3(PL_DSCNT_3);
            entity.setPlDiscount4(PL_DSCNT_4);
            entity.setPlTotalDiscount(TTL_PL_DSCNT);
            entity.setPlAdCompany(PL_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalApPurchaseOrderLine create(short PL_LN, double PL_QTY, double PL_UNT_CST, double PL_AMNT,
                                           String PL_QC_NUM, Date PL_QC_EXPRY_DT, String PL_RMRKS, double PL_CNVRSN_FCTR, double PL_TX_AMNT, Integer PL_PL_CODE,
                                           double PL_DSCNT_1, double PL_DSCNT_2, double PL_DSCNT_3, double PL_DSCNT_4, double TTL_PL_DSCNT,
                                           Integer PL_AD_CMPNY) throws CreateException {

        try {

            LocalApPurchaseOrderLine entity = new LocalApPurchaseOrderLine();

            Debug.print("ApPurchaseOrderLineBean create");
            entity.setPlLine(PL_LN);
            entity.setPlQuantity(PL_QTY);
            entity.setPlUnitCost(PL_UNT_CST);
            entity.setPlAmount(PL_AMNT);
            entity.setPlQcNumber(PL_QC_NUM);
            entity.setPlQcExpiryDate(PL_QC_EXPRY_DT);
            entity.setPlRemarks(PL_RMRKS);
            entity.setPlConversionFactor(PL_CNVRSN_FCTR);
            entity.setPlTaxAmount(PL_TX_AMNT);
            entity.setPlPlCode(PL_PL_CODE);
            entity.setPlDiscount1(PL_DSCNT_1);
            entity.setPlDiscount2(PL_DSCNT_2);
            entity.setPlDiscount3(PL_DSCNT_3);
            entity.setPlDiscount4(PL_DSCNT_4);
            entity.setPlTotalDiscount(TTL_PL_DSCNT);
            entity.setPlAdCompany(PL_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}