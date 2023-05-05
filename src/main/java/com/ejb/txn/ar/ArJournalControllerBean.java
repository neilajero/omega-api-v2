/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArJournalControllerBean
 * @created March 10, 2004, 11:08 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModDistributionRecordDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArJournalControllerEJB")
public class ArJournalControllerBean extends EJBContextClass implements ArJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;

    public ArrayList getArDrByInvCode(Integer INV_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArJournalControllerBean getArDrByInvCode");
        try {

            LocalArInvoice arInvoice = null;

            try {

                arInvoice = arInvoiceHome.findByPrimaryKey(INV_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection arDistributionRecords = arDistributionRecordHome.findByInvCode(arInvoice.getInvCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : arDistributionRecords) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                ArModDistributionRecordDetails mdetails = new ArModDistributionRecordDetails();

                mdetails.setDrCode(arDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(arDistributionRecord.getDrClass());
                mdetails.setDrDebit(arDistributionRecord.getDrDebit());
                mdetails.setDrAmount(arDistributionRecord.getDrAmount());
                mdetails.setDrReversed(arDistributionRecord.getDrReversed());
                mdetails.setDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

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

    public ArrayList getArDrByRctCode(Integer RCT_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArJournalControllerBean getArDrByRctCode");
        try {

            LocalArReceipt arReceipt = null;

            try {

                arReceipt = arReceiptHome.findByPrimaryKey(RCT_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection arDistributionRecords = arDistributionRecordHome.findByRctCode(arReceipt.getRctCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : arDistributionRecords) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) distributionRecord;

                ArModDistributionRecordDetails mdetails = new ArModDistributionRecordDetails();

                mdetails.setDrCode(arDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(arDistributionRecord.getDrClass());
                mdetails.setDrDebit(arDistributionRecord.getDrDebit());
                mdetails.setDrAmount(arDistributionRecord.getDrAmount());
                mdetails.setDrReversed(arDistributionRecord.getDrReversed());
                mdetails.setDrCoaAccountNumber(arDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(arDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

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
        Debug.print("ArJournalControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer AD_CMPNY) {
        Debug.print("ArJournalControllerBean getAdPrfArInvoiceLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveArDrEntry(Integer PRMRY_KEY, ArrayList drList, String transactionType, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {
        Debug.print("ArJournalControllerBean saveArDrEntry");
        try {

            LocalArInvoice arInvoice = null;
            LocalArReceipt arReceipt = null;

            Collection arDistributionRecords = null;
            Iterator i;

            if (transactionType.equals("INVOICE") || transactionType.equals("CREDIT MEMO")) {

                arInvoice = arInvoiceHome.findByPrimaryKey(PRMRY_KEY);

                // remove all distribution records

                arDistributionRecords = arInvoice.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                Integer drScAccount = null;
                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                    if (arDistributionRecord.getDrClass().equals("SC")) {
                        drScAccount = arDistributionRecord.getDrScAccount();
                    }

                    i.remove();

                    //	    		   arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new distribution records

                i = drList.iterator();

                while (i.hasNext()) {

                    ArModDistributionRecordDetails mDrDetails = (ArModDistributionRecordDetails) i.next();

                    // Set SC Entries
                    if (mDrDetails.getDrClass().equals("SC")) {
                        mDrDetails.setDrScAccount(drScAccount);
                    }

                    this.addArDrEntry(mDrDetails, arInvoice, null, null, AD_BRNCH, AD_CMPNY);
                }

            } else if (transactionType.equals("RECEIPT") || transactionType.equals("MISC RECEIPT")) {

                ArrayList appliedInvoiceList = new ArrayList();

                arReceipt = arReceiptHome.findByPrimaryKey(PRMRY_KEY);

                // remove all distribution records

                arDistributionRecords = arReceipt.getArDistributionRecords();

                i = arDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();
                    appliedInvoiceList.add(arDistributionRecord.getArAppliedInvoice());
                    i.remove();

                    //	    		   arDistributionRecord.entityRemove();
                    em.remove(arDistributionRecord);
                }

                // add new distribution records

                i = drList.iterator();
                int ctr = 0;
                while (i.hasNext()) {
                    ArModDistributionRecordDetails mDrDetails = (ArModDistributionRecordDetails) i.next();
                    try {
                        LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) appliedInvoiceList.toArray()[ctr];
                        this.addArDrEntry(mDrDetails, null, arReceipt, arAppliedInvoice, AD_BRNCH, AD_CMPNY);
                    } catch (Exception e) {
                        this.addArDrEntry(mDrDetails, null, arReceipt, null, AD_BRNCH, AD_CMPNY);
                    }
                    ctr++;
                }
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private void addArDrEntry(ArModDistributionRecordDetails mdetails, LocalArInvoice arInvoice, LocalArReceipt arReceipt, LocalArAppliedInvoice arAppliedInvoice, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {
        Debug.print("ArJournalControllerBean addArDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getDrLine()));
                }

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getDrLine()));
            }

            // create distribution record

            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), mdetails.getDrDebit(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            // glChartOfAccount.addArDistributionRecord(arDistributionRecord);
            arDistributionRecord.setGlChartOfAccount(glChartOfAccount);

            if (arDistributionRecord.getDrClass().equals("SC")) {

                arDistributionRecord.setDrScAccount(mdetails.getDrScAccount());
            }

            if (arReceipt != null) {
                arReceipt.addArDistributionRecord(arDistributionRecord);

            } else if (arInvoice != null) {

                arInvoice.addArDistributionRecord(arDistributionRecord);
            }

            // to be used by gl journal interface for cross currency receipts
            if (arAppliedInvoice != null) {
                arAppliedInvoice.addArDistributionRecord(arDistributionRecord);
            }

        } catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArJournalControllerBean ejbCreate");
    }

}