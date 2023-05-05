package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArInvoicePaymentSchedule;
import com.util.Debug;
import com.util.EJBCommon;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class LocalArInvoicePaymentScheduleHome {

    public static final String JNDI_NAME = "LocalArInvoicePaymentScheduleHome!com.ejb.ar.LocalArInvoicePaymentScheduleHome";

    @EJB
    public PersistenceBeanClass em;

    private Date IPS_DUE_DT = null;
    private short IPS_NMBR = 0;
    private double IPS_AMNT_DUE = 0d;
    private double IPS_AMNT_PD = 0d;
    private byte IPS_LCK = EJBCommon.FALSE;
    private short IPS_PNT_CTR = 0;
    private Date IPS_PNT_DUE_DT = null;
    private double IPS_PNT_DUE = 0d;
    private double IPS_PNT_PD = 0d;
    private Integer IPS_AD_CMPNY = null;

    public LocalArInvoicePaymentScheduleHome() {

    }

    // FINDER METHODS

    public LocalArInvoicePaymentSchedule findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalArInvoicePaymentSchedule entity = (LocalArInvoicePaymentSchedule) em
                    .find(new LocalArInvoicePaymentSchedule(), pk);
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

    public LocalArInvoicePaymentSchedule findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {

        try {

            LocalArInvoicePaymentSchedule entity = (LocalArInvoicePaymentSchedule) em
                    .findPerCompany(new LocalArInvoicePaymentSchedule(), pk, companyShortName);
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

    public java.util.Collection findByIpsLockAndInvCode(byte IPS_LCK, java.lang.Integer INV_CODE,
                                                        java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.ipsLock = ?1 AND ips.arInvoice.invCode = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, IPS_LCK);
            query.setParameter(2, INV_CODE);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findByIpsLockAndInvCode(byte IPS_LCK, java.lang.Integer INV_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(byte IPS_LCK,
                                                                                java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.arCustomer.cstCustomerCode=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?3 AND ips.ipsAdCompany = ?4");
            query.setParameter(1, IPS_LCK);
            query.setParameter(2, CST_CSTMR_CODE);
            query.setParameter(3, IPS_AD_BRNCH);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCodeAndBrCode(byte IPS_LCK, java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndCstCustomerCode(byte IPS_LCK, java.lang.String CST_CSTMR_CODE,
                                                                       java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.arCustomer.cstCustomerCode=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?3");
            query.setParameter(1, IPS_LCK);
            query.setParameter(2, CST_CSTMR_CODE);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCode(byte IPS_LCK, java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndLoanReferenceAndCstCustomerCode(byte IPS_LCK, java.lang.String CST_CSTMR_CODE, java.lang.String LOAN_RFRNC,
                                                                                       java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.arCustomer.cstCustomerCode=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?3 AND ips.arInvoice.invReferenceNumber LIKE ?4");
            query.setParameter(1, IPS_LCK);
            query.setParameter(2, CST_CSTMR_CODE);
            query.setParameter(3, IPS_AD_CMPNY);
            query.setParameter(4, "%" + LOAN_RFRNC + "%");

            java.util.Collection lists = query.getResultList();
            return lists;
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndCstCustomerCode(byte IPS_LCK, java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndInvNumber(byte IPS_LOCK, java.lang.String INV_NMBR,
                                                                 java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.invNumber=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?3");
            query.setParameter(1, IPS_LOCK);
            query.setParameter(2, INV_NMBR);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndInvNumber(byte IPS_LOCK, java.lang.String INV_NMBR, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndInvNumberAdBranchCompny(byte IPS_LOCK, java.lang.String INV_NMBR,
                                                                               java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.invNumber=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?3 AND ips.ipsAdCompany = ?4");
            query.setParameter(1, IPS_LOCK);
            query.setParameter(2, INV_NMBR);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndInvNumberAdBranchCompny(byte IPS_LOCK, java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findIpsByCstCstmrCodeAndInvReferenceNumberAdBranchCompny(java.lang.String CST_CSTMR_CODE, java.lang.String INV_RFRNC_NMBR,
                                                                                         java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.arCustomer.cstCustomerCode=?1 AND ips.arInvoice.invReferenceNumber=?2 AND ips.arInvoice.invAdBranch = ?3 AND ips.ipsAdCompany = ?4");

            query.setParameter(1, CST_CSTMR_CODE);
            query.setParameter(2, INV_RFRNC_NMBR);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findIpsByCstCstmrCodeAndInvReferenceNumberAdBranchCompny(byte IPS_LOCK, java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findIpsByCstCstmrCodeAndInvNumberAdBranchCompny(
            java.lang.String CST_CSTMR_CODE, java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY,
            String companyShortName) throws FinderException {

        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips "
                            + "WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.arCustomer.cstCustomerCode=?1 "
                            + "AND ips.arInvoice.invNumber=?2 AND ips.arInvoice.invAdBranch = ?3 AND ips.ipsAdCompany = ?4",
                    companyShortName);

            query.setParameter(1, CST_CSTMR_CODE);
            query.setParameter(2, INV_NMBR);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }


    public java.util.Collection findOpenIpsByIpsLockAndInvReferenceNumberAdBranchCompny(byte IPS_LOCK, java.lang.String INV_RFRNC_NMBR,
                                                                                        java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.arInvoice.invReferenceNumber=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?3 AND ips.ipsAdCompany = ?4");
            query.setParameter(1, IPS_LOCK);
            query.setParameter(2, INV_RFRNC_NMBR);
            query.setParameter(3, INV_AD_BRNCH);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndInvReferenceNumberAdBranchCompny(byte IPS_LOCK, java.lang.String INV_RFRNC_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByInvNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH,
                                                       java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.invNumber = ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_NMBR);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByInvNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.arCustomer.cstCustomerCode = ?1 AND ips.ipsAdCompany = ?2");
            query.setParameter(1, CST_CSTMR_CODE);
            query.setParameter(2, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByAdCompnyAll(java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.ipsAdCompany = ?1");
            query.setParameter(1, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findByAdCompnyAll(java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCodeAndAdCompny(java.lang.Integer INV_CODE, java.lang.Integer IPS_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invCode = ?1 AND ips.ipsAdCompany = ?2");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findByInvCodeAndAdCompny(java.lang.Integer INV_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByInvCodeAndAdBranchCompny(java.lang.Integer INV_CODE,
                                                               java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invCode = ?1 AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_CODE);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findByInvCodeAndAdBranchCompny(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_BRNCH , java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOverdueIpsByInvDateAndCstCustomerCode(java.util.Date INV_DT,
                                                                          java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.ipsDueDate < ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.arCustomer.cstCustomerCode = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_DT);
            query.setParameter(2, CST_CSTMR_CODE);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOverdueIpsByInvDateAndCstCustomerCode(java.com.util.Date INV_DT, java.lang.String CST_CSTMR_CODE, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOverdueIps(java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
                                               java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.invInterest = 0 AND ips.ipsDueDate < ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_DT);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOverdueIps(java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOverdueIpsByNextRunDate2(java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
                                                             java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = 0 AND ips.arInvoice.invInterest = 0 AND ips.arInvoice.invInterestNextRunDate <= ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_DT);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOverdueIpsByNextRunDate2(java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOverdueIpsByNextRunDate(java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
                                                            java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.arCustomer.cstAutoComputeInterest = 1 AND ips.ipsLock = 0 AND ips.arInvoice.invPosted = 1 AND ips.arInvoice.invInterest = 0 AND ips.arInvoice.invDisableInterest = 0 AND ips.arInvoice.invInterestNextRunDate IS NOT NULL AND ips.arInvoice.invInterestNextRunDate <= ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_DT);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOverdueIpsByNextRunDate(java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOverdueIpsByNextRunDateLimit(java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH,
                                                                 java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.arInvoice.arCustomer.cstAutoComputeInterest = 1 AND ips.ipsLock = 0 AND ips.arInvoice.invPosted = 1 AND ips.arInvoice.invInterest = 0 AND ips.arInvoice.invDisableInterest = 0 AND ips.arInvoice.invInterestNextRunDate IS NOT NULL AND ips.arInvoice.invInterestNextRunDate <= ?1 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.arInvoice.invAdBranch = ?2 AND ips.ipsAdCompany = ?3");
            query.setParameter(1, INV_DT);
            query.setParameter(2, INV_AD_BRNCH);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOverdueIpsByNextRunDateLimit(java.com.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findPastdueIpsByPenaltyDueDate(
            java.util.Date INV_DT, java.lang.Integer INV_AD_BRNCH, java.lang.Integer IPS_AD_CMPNY) {

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_FindPastDueIpsByPenaltyDueDate");
            query.registerStoredProcedureParameter("invoice_date", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("branch_code", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("company_code", Integer.class, ParameterMode.IN);

            query.setParameter("invoice_date", INV_DT);
            query.setParameter("branch_code", INV_AD_BRNCH);
            query.setParameter("company_code", IPS_AD_CMPNY);

            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByInvHrDbReferenceIDAndIbName(
            java.lang.Integer DB_REF_ID, java.lang.String IB_NM, java.util.Date INV_DT, java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.ipsLock = 0 AND ips.arInvoice.invPosted = 1 AND ips.arInvoice.hrDeployedBranch.dbReferenceID IN (?1) AND ips.arInvoice.arInvoiceBatch.ibName = ?2 AND ips.ipsDueDate <= ?3 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?4");
            query.setParameter(1, DB_REF_ID);
            query.setParameter(2, IB_NM);
            query.setParameter(3, INV_DT);
            query.setParameter(4, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByInvHrDbReferenceIDAndIbName(java.lang.Integer DB_REF_ID, java.lang.String IB_NM, java.com.util.Date INV_DT, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByCstIbName(java.lang.String IB_NM, java.util.Date INV_DT,
                                                       java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.ipsLock = 0 AND ips.arInvoice.invPosted = 1 AND ips.arInvoice.hrDeployedBranch IS NOT NULL AND ips.arInvoice.arInvoiceBatch.ibName IN (?1) AND ips.ipsDueDate <= ?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?3");
            query.setParameter(1, IB_NM);
            query.setParameter(2, INV_DT);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByCstIbName(java.lang.String IB_NM, java.com.util.Date INV_DT, java.lang.Integer IPS_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findOpenIpsByIpsLockAndIpsCode(byte IPS_LCK, java.lang.Integer IPS_CODE,
                                                               java.lang.Integer IPS_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ips) FROM ArInvoicePaymentSchedule ips WHERE ips.arInvoice.invPosted = 1 AND ips.ipsLock = ?1 AND ips.ipsCode=?2 AND ips.ipsAmountDue > ips.ipsAmountPaid AND ips.ipsAdCompany = ?3");
            query.setParameter(1, IPS_LCK);
            query.setParameter(2, IPS_CODE);
            query.setParameter(3, IPS_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArInvoicePaymentScheduleHome.findOpenIpsByIpsLockAndIpsCode(byte IPS_LCK, java.lang.Integer IPS_CODE, java.lang.Integer IPS_AD_CMPNY)");
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

    public java.util.Collection getIpsByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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
    public List<Object[]> getAgingSummary(String spName) {

        try {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2020);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            Date dateCutOff = cal.getTime();

            StoredProcedureQuery sp = em.createStoredProcedureQuery(spName);
            sp.registerStoredProcedureParameter(1, java.sql.Date.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
            sp.setParameter(1, new java.sql.Date(dateCutOff.getTime()));
            sp.setParameter(2, 0);
            sp.execute();

            return (List<Object[]>) sp.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }

    }

    public LocalArInvoicePaymentScheduleHome IpsDueDate(Date IPS_DUE_DT) {
        this.IPS_DUE_DT = IPS_DUE_DT;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsNumber(short IPS_NMBR) {
        this.IPS_NMBR = IPS_NMBR;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsAmountDue(double IPS_AMNT_DUE) {
        this.IPS_AMNT_DUE = IPS_AMNT_DUE;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsAmountPaid(double IPS_AMNT_PD) {
        this.IPS_AMNT_PD = IPS_AMNT_PD;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsLock(byte IPS_LCK) {
        this.IPS_LCK = IPS_LCK;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsPenaltyCounter(short IPS_PNT_CTR) {
        this.IPS_PNT_CTR = IPS_PNT_CTR;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsPenaltyDueDate(Date IPS_PNT_DUE_DT) {
        this.IPS_PNT_DUE_DT = IPS_PNT_DUE_DT;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsPenaltyDue(double IPS_PNT_DUE) {
        this.IPS_PNT_DUE = IPS_PNT_DUE;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsPenaltyPaid(double IPS_PNT_PD) {
        this.IPS_PNT_PD = IPS_PNT_PD;
        return this;
    }

    public LocalArInvoicePaymentScheduleHome IpsAdCompany(Integer IPS_AD_CMPNY) {
        this.IPS_AD_CMPNY = IPS_AD_CMPNY;
        return this;
    }

    public LocalArInvoicePaymentSchedule buildInvoicePaymentSchedule(String companyShortName) throws CreateException {

        try {

            LocalArInvoicePaymentSchedule entity = new LocalArInvoicePaymentSchedule();

            Debug.print("ArInvoicePaymentScheduleBean buildInvoicePaymentSchedule");

            entity.setIpsDueDate(IPS_DUE_DT);
            entity.setIpsNumber(IPS_NMBR);
            entity.setIpsAmountDue(IPS_AMNT_DUE);
            entity.setIpsAmountPaid(IPS_AMNT_PD);
            entity.setIpsLock(IPS_LCK);
            entity.setIpsPenaltyCounter(IPS_PNT_CTR);
            entity.setIpsPenaltyDueDate(IPS_PNT_DUE_DT);
            entity.setIpsPenaltyDue(IPS_PNT_DUE);
            entity.setIpsPenaltyPaid(IPS_PNT_PD);
            entity.setIpsAdCompany(IPS_AD_CMPNY);

            em.persist(entity, companyShortName);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoicePaymentSchedule create(Integer IPS_CODE, Date IPS_DUE_DT, short IPS_NMBR,
                                                double IPS_AMNT_DUE, double IPS_AMNT_PD, byte IPS_LCK, short IPS_PNT_CTR, Date IPS_PNT_DUE_DT,
                                                double IPS_PNT_DUE, double IPS_PNT_PD, Integer IPS_AD_CMPNY) throws CreateException {

        try {

            LocalArInvoicePaymentSchedule entity = new LocalArInvoicePaymentSchedule();

            Debug.print("ArInvoicePaymentScheduleBean create");

            entity.setIpsCode(IPS_CODE);
            entity.setIpsDueDate(IPS_DUE_DT);
            entity.setIpsNumber(IPS_NMBR);
            entity.setIpsAmountDue(IPS_AMNT_DUE);
            entity.setIpsAmountPaid(IPS_AMNT_PD);
            entity.setIpsLock(IPS_LCK);
            entity.setIpsPenaltyCounter(IPS_PNT_CTR);
            entity.setIpsPenaltyDueDate(IPS_PNT_DUE_DT);
            entity.setIpsPenaltyDue(IPS_PNT_DUE);
            entity.setIpsPenaltyPaid(IPS_PNT_PD);
            entity.setIpsAdCompany(IPS_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalArInvoicePaymentSchedule create(Date IPS_DUE_DT, short IPS_NMBR, double IPS_AMNT_DUE,
                                                double IPS_AMNT_PD, byte IPS_LCK, short IPS_PNT_CTR, Date IPS_PNT_DUE_DT, double IPS_PNT_DUE,
                                                double IPS_PNT_PD, Integer IPS_AD_CMPNY) throws CreateException {

        try {

            LocalArInvoicePaymentSchedule entity = new LocalArInvoicePaymentSchedule();

            Debug.print("ArInvoicePaymentScheduleBean create");

            entity.setIpsDueDate(IPS_DUE_DT);
            entity.setIpsNumber(IPS_NMBR);
            entity.setIpsAmountDue(IPS_AMNT_DUE);
            entity.setIpsAmountPaid(IPS_AMNT_PD);
            entity.setIpsLock(IPS_LCK);
            entity.setIpsPenaltyCounter(IPS_PNT_CTR);
            entity.setIpsPenaltyDueDate(IPS_PNT_DUE_DT);
            entity.setIpsPenaltyDue(IPS_PNT_DUE);
            entity.setIpsPenaltyPaid(IPS_PNT_PD);
            entity.setIpsAdCompany(IPS_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}