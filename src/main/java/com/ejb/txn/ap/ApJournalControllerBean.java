/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApJournalControllerBean
 * @created Mar 01, 2004, 10:23 AM
 * @author Dennis Hilario
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.exception.global.GlobalBranchAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.mod.ap.ApModDistributionRecordDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApJournalControllerEJB")
public class ApJournalControllerBean extends EJBContextClass implements ApJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;

    public ArrayList getApDrByChkCode(Integer CHK_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApJournalControllerBean getApDrByChkCode");

        try {

            LocalApCheck apCheck = null;

            try {

                apCheck = apCheckHome.findByPrimaryKey(CHK_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection apDistributionRecords = apDistributionRecordHome.findByChkCode(apCheck.getChkCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : apDistributionRecords) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                mdetails.setDrCode(apDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(apDistributionRecord.getDrClass());
                mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrByVouCode(Integer VOU_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApJournalControllerBean getApDrByVouCode");

        try {

            LocalApVoucher apVoucher = null;

            try {

                apVoucher = apVoucherHome.findByPrimaryKey(VOU_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection apDistributionRecords = apDistributionRecordHome.findByVouCode(apVoucher.getVouCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : apDistributionRecords) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                mdetails.setDrCode(apDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(apDistributionRecord.getDrClass());
                mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApDrByPoCode(Integer PO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApJournalControllerBean getApDrByPoCode");

        try {

            LocalApPurchaseOrder apPurchaseOrder = null;

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection apDistributionRecords = apDistributionRecordHome.findByPoCode(apPurchaseOrder.getPoCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : apDistributionRecords) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) distributionRecord;

                ApModDistributionRecordDetails mdetails = new ApModDistributionRecordDetails();

                mdetails.setDrCode(apDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(apDistributionRecord.getDrClass());
                mdetails.setDrDebit(apDistributionRecord.getDrDebit());
                mdetails.setDrAmount(apDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(apDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(apDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApJournalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("ApJournalControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveApDrEntry(Integer PRMRY_KEY, ArrayList drList, String transactionType, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApJournalControllerBean saveApDrEntry");

        try {

            LocalApVoucher apVoucher = null;
            LocalApCheck apCheck = null;
            LocalApPurchaseOrder apPurchaseOrder = null;

            Collection apDistributionRecords = null;
            Iterator i;

            switch (transactionType) {
                case "VOUCHER":
                case "DEBIT MEMO":

                    apVoucher = apVoucherHome.findByPrimaryKey(PRMRY_KEY);

                    // remove all distribution records

                    apDistributionRecords = apVoucher.getApDistributionRecords();

                    i = apDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                        i.remove();

                        //    	       	  	    apDistributionRecord.entityRemove();
                        em.remove(apDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();

                    while (i.hasNext()) {

                        ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();

                        this.addApDrEntry(mDrDetails, apVoucher, null, null, null, AD_BRNCH, AD_CMPNY);
                    }

                    break;
                case "PAYMENT":
                case "DIRECT CHECK":

                    apCheck = apCheckHome.findByPrimaryKey(PRMRY_KEY);
                    ArrayList appliedVoucherList = new ArrayList();

                    // remove all distribution records

                    apDistributionRecords = apCheck.getApDistributionRecords();

                    i = apDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();
                        appliedVoucherList.add(apDistributionRecord.getApAppliedVoucher());
                        i.remove();

                        //		       	  	    apDistributionRecord.entityRemove();
                        em.remove(apDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();
                    int ctr = 0;
                    while (i.hasNext()) {

                        ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();
                        try {
                            LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucherList.toArray()[ctr];
                            this.addApDrEntry(mDrDetails, null, apCheck, apAppliedVoucher, null, AD_BRNCH, AD_CMPNY);

                        } catch (Exception e) {
                            this.addApDrEntry(mDrDetails, null, apCheck, null, null, AD_BRNCH, AD_CMPNY);
                        }
                        ctr++;
                    }

                    break;
                case "RECEIVING":

                    apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PRMRY_KEY);

                    // remove all distribution records

                    apDistributionRecords = apPurchaseOrder.getApDistributionRecords();

                    i = apDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                        i.remove();

                        //    	       	  	    apDistributionRecord.entityRemove();
                        em.remove(apDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();

                    while (i.hasNext()) {

                        ApModDistributionRecordDetails mDrDetails = (ApModDistributionRecordDetails) i.next();

                        this.addApDrEntry(mDrDetails, null, null, null, apPurchaseOrder, AD_BRNCH, AD_CMPNY);
                    }
                    break;
            }

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    //  private methods

    private void addApDrEntry(ApModDistributionRecordDetails mdetails, LocalApVoucher apVoucher, LocalApCheck apCheck, LocalApAppliedVoucher apAppliedVoucher, LocalApPurchaseOrder apPurchaseOrder, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApJournalControllerBean addApDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalBranchAccountNumberInvalidException();

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            glChartOfAccount.addApDistributionRecord(apDistributionRecord);

            if (apVoucher != null) {

                apVoucher.addApDistributionRecord(apDistributionRecord);

            } else if (apCheck != null) {

                apCheck.addApDistributionRecord(apDistributionRecord);

            } else if (apAppliedVoucher != null) {

                apAppliedVoucher.addApDistributionRecord(apDistributionRecord);

            } else if (apPurchaseOrder != null) {

                apPurchaseOrder.addApDistributionRecord(apDistributionRecord);
            }

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }
    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApJournalControllerBean ejbCreate");
    }
}