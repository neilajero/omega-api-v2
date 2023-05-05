package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApVoucherPaymentSchedule;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;;
import java.util.Date;

@Stateless
public class LocalApVoucherPaymentScheduleHome {

    public static final String JNDI_NAME = "LocalApVoucherPaymentScheduleHome!com.ejb.ap.LocalApVoucherPaymentScheduleHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalApVoucherPaymentScheduleHome() {

    }

    // FINDER METHODS

    public LocalApVoucherPaymentSchedule findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalApVoucherPaymentSchedule entity = (LocalApVoucherPaymentSchedule) em
                    .find(new LocalApVoucherPaymentSchedule(), pk);
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

    public java.util.Collection findOpenVpsByVpsLock(
            byte VPS_LCK, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY) {

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_FindOpenVpsByVpsLock");
            query.registerStoredProcedureParameter("vpsLock", Byte.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("branchCode", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("companyCode", Integer.class, ParameterMode.IN);
            query.setParameter("vpsLock", VPS_LCK);
            query.setParameter("branchCode", VOU_AD_BRNCH);
            query.setParameter("companyCode", VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findOpenVpsByVpsLockAndSplSupplierCode(byte VPS_LCK, java.lang.String SPL_SPPLR_CODE,
                                                                       java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE (vps.apVoucher.vouType NOT LIKE 'REQUEST' OR vps.apVoucher.vouType IS NULL ) "
                            + "AND vps.apVoucher.vouPosted = 1 AND vps.vpsLock = ?1 AND vps.apVoucher.apSupplier.splSupplierCode=?2 "
                            + "AND vps.vpsAmountDue > vps.vpsAmountPaid AND vps.apVoucher.vouAdBranch=?3 AND vps.vpsAdCompany=?4 "
                            + "ORDER BY vps.vpsDueDate");
            query.setParameter(1, VPS_LCK);
            query.setParameter(2, SPL_SPPLR_CODE);
            query.setParameter(3, VOU_AD_BRNCH);
            query.setParameter(4, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findOpenVpsByVpsLockAndSplSupplierCode(byte VPS_LCK, java.lang.String SPL_SPPLR_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByVpsLockAndVouCode(byte VPS_LCK, java.lang.Integer VOU_CODE,
                                                        java.lang.Integer VPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE vps.apVoucher.vouType <> 'REQUEST' AND vps.vpsLock = ?1 AND vps.apVoucher.vouCode = ?2 AND vps.vpsAdCompany=?3");
            query.setParameter(1, VPS_LCK);
            query.setParameter(2, VOU_CODE);
            query.setParameter(3, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findByVpsLockAndVouCode(byte VPS_LCK, java.lang.Integer VOU_CODE, java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenVpsByVouDocumentNumber(java.lang.String VOU_DCMNT_NMBR,
                                                               java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE vps.apVoucher.vouType <> 'REQUEST' AND vps.apVoucher.vouPosted = 1 AND vps.apVoucher.vouDocumentNumber=?1 AND vps.vpsAmountDue > vps.vpsAmountPaid AND vps.apVoucher.vouAdBranch=?2 AND vps.vpsAdCompany=?3");
            query.setParameter(1, VOU_DCMNT_NMBR);
            query.setParameter(2, VOU_AD_BRNCH);
            query.setParameter(3, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findOpenVpsByVouDocumentNumber(java.lang.String VOU_DCMNT_NMBR, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenVpsByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer VPS_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE vps.apVoucher.vouType <> 'REQUEST' AND vps.apVoucher.vouPosted = 1 AND vps.apVoucher.vouCode=?1 AND vps.vpsAdCompany=?2");
            query.setParameter(1, VOU_CODE);
            query.setParameter(2, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findOpenVpsByVouCode(java.lang.Integer VOU_CODE, java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByVouCodeAndVouBranchAndAdCmpny(java.lang.Integer VOU_CODE,
                                                                    java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE vps.apVoucher.vouType <> 'REQUEST' AND vps.apVoucher.vouCode = ?1 AND vps.apVoucher.vouAdBranch=?2 AND vps.vpsAdCompany=?3");
            query.setParameter(1, VOU_CODE);
            query.setParameter(2, VOU_AD_BRNCH);
            query.setParameter(3, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findByVouCodeAndVouBranchAndAdCmpny(java.lang.Integer VOU_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByAdCmpnyAll(java.lang.Integer VPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(vps) FROM ApVoucherPaymentSchedule vps WHERE vps.apVoucher.vouType <> 'REQUEST' AND vps.vpsAdCompany=?1");
            query.setParameter(1, VPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ap.LocalApVoucherPaymentScheduleHome.findByAdCmpnyAll(java.lang.Integer VPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getVpsByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

    // CREATE METHODS

    public LocalApVoucherPaymentSchedule create(Integer VPS_CODE, Date VPS_DUE_DT, short VPS_NMBR,
                                                double VPS_AMNT_DUE, double VPS_AMNT_PD, byte VPS_LCK, Integer VPS_AD_CMPNY) throws CreateException {

        try {

            LocalApVoucherPaymentSchedule entity = new LocalApVoucherPaymentSchedule();

            Debug.print("ApVoucherPaymentScheduleBean create");
            entity.setVpsCode(VPS_CODE);
            entity.setVpsDueDate(VPS_DUE_DT);
            entity.setVpsNumber(VPS_NMBR);
            entity.setVpsAmountDue(VPS_AMNT_DUE);
            entity.setVpsAmountPaid(VPS_AMNT_PD);
            entity.setVpsLock(VPS_LCK);
            entity.setVpsAdCompany(VPS_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalApVoucherPaymentSchedule create(Date VPS_DUE_DT, short VPS_NMBR, double VPS_AMNT_DUE,
                                                double VPS_AMNT_PD, byte VPS_LCK, Integer VPS_AD_CMPNY) throws CreateException {

        try {

            LocalApVoucherPaymentSchedule entity = new LocalApVoucherPaymentSchedule();

            Debug.print("ApVoucherPaymentScheduleBean create");
            entity.setVpsDueDate(VPS_DUE_DT);
            entity.setVpsNumber(VPS_NMBR);
            entity.setVpsAmountDue(VPS_AMNT_DUE);
            entity.setVpsAmountPaid(VPS_AMNT_PD);
            entity.setVpsLock(VPS_LCK);
            entity.setVpsAdCompany(VPS_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}