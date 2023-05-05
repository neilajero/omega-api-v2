/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvJournalControllerBean
 * @created August 30, 2004, 11:48 AM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferHome;
import com.ejb.dao.inv.LocalInvDistributionRecordHome;
import com.ejb.dao.inv.LocalInvStockTransferHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.entities.inv.LocalInvDistributionRecord;
import com.ejb.entities.inv.LocalInvStockTransfer;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModDistributionRecordDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "InvJournalControllerEJB")
public class InvJournalControllerBean extends EJBContextClass implements InvJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvStockTransferHome invStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;

    public ArrayList getInvDrByAdjCode(Integer ADJ_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvJournalControllerBean getInvDrByAdjCode");
        try {
            LocalInvAdjustment invAdjustment;
            try {
                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);
            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection invDistributionRecords = invDistributionRecordHome.findByAdjCode(invAdjustment.getAdjCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : invDistributionRecords) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) distributionRecord;

                InvModDistributionRecordDetails mdetails = new InvModDistributionRecordDetails();

                mdetails.setDrCode(invDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(invDistributionRecord.getDrClass());
                mdetails.setDrDebit(invDistributionRecord.getDrDebit());
                mdetails.setDrAmount(invDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(invDistributionRecord.getInvChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(invDistributionRecord.getInvChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvDrByStCode(Integer ST_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvJournalControllerBean getInvDrByStCode");
        try {

            LocalInvStockTransfer invStockTransfer = null;

            try {

                invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection invDistributionRecords = invDistributionRecordHome.findByStCode(invStockTransfer.getStCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : invDistributionRecords) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) distributionRecord;

                InvModDistributionRecordDetails mdetails = new InvModDistributionRecordDetails();

                mdetails.setDrCode(invDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(invDistributionRecord.getDrClass());
                mdetails.setDrDebit(invDistributionRecord.getDrDebit());
                mdetails.setDrAmount(invDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(invDistributionRecord.getInvChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(invDistributionRecord.getInvChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvDrByBstCode(Integer BST_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvJournalControllerBean getInvDrByBstCode");
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;

            try {

                invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get distribution records

            Collection invDistributionRecords = invDistributionRecordHome.findByBstCode(invBranchStockTransfer.getBstCode(), AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : invDistributionRecords) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) distributionRecord;

                InvModDistributionRecordDetails mdetails = new InvModDistributionRecordDetails();

                mdetails.setDrCode(invDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(invDistributionRecord.getDrClass());
                mdetails.setDrDebit(invDistributionRecord.getDrDebit());
                mdetails.setDrAmount(invDistributionRecord.getDrAmount());
                mdetails.setDrCoaAccountNumber(invDistributionRecord.getInvChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(invDistributionRecord.getInvChartOfAccount().getCoaAccountDescription());

                list.add(mdetails);

                lineNumber++;
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvJournalControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfInvJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("InvJournalControllerBean getAdPrfInvJournalLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvInventoryLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveInvDrEntry(Integer PRMRY_KEY, ArrayList drList, String transactionType, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalAccountNumberInvalidException {

        Debug.print("InvJournalControllerBean updateInvDrEntry");

        try {

            LocalInvAdjustment invAdjustment;
            LocalInvStockTransfer invStockTransfer;
            LocalInvBranchStockTransfer invBranchStockTransfer;

            Collection invDistributionRecords;
            Iterator i;

            switch (transactionType) {
                case "ADJUSTMENT":

                    invAdjustment = invAdjustmentHome.findByPrimaryKey(PRMRY_KEY);

                    // remove all distribution records

                    invDistributionRecords = invAdjustment.getInvDistributionRecords();

                    i = invDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                        i.remove();

                        // 	       	  	    invDistributionRecord.entityRemove();
                        em.remove(invDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();

                    while (i.hasNext()) {

                        InvModDistributionRecordDetails mDrDetails = (InvModDistributionRecordDetails) i.next();

                        this.addInvDrEntry(mDrDetails, invAdjustment, null, null, AD_BRNCH, AD_CMPNY);
                    }

                    break;

                case "STOCK TRANSFER":

                    invStockTransfer = invStockTransferHome.findByPrimaryKey(PRMRY_KEY);

                    // remove all distribution records

                    invDistributionRecords = invStockTransfer.getInvDistributionRecords();

                    i = invDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                        i.remove();

                        //       	  	    invDistributionRecord.entityRemove();
                        em.remove(invDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();

                    while (i.hasNext()) {

                        InvModDistributionRecordDetails mDrDetails = (InvModDistributionRecordDetails) i.next();

                        this.addInvDrEntry(mDrDetails, null, invStockTransfer, null, AD_BRNCH, AD_CMPNY);
                    }

                    break;
                case "BRANCH STOCK TRANSFER OUT":
                case "BRANCH STOCK TRANSFER IN":

                    invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(PRMRY_KEY);

                    // remove all distribution records

                    invDistributionRecords = invBranchStockTransfer.getInvDistributionRecords();

                    i = invDistributionRecords.iterator();

                    while (i.hasNext()) {

                        LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();

                        i.remove();

                        //       	  	    invDistributionRecord.entityRemove();
                        em.remove(invDistributionRecord);
                    }

                    // add new distribution records

                    i = drList.iterator();

                    while (i.hasNext()) {

                        InvModDistributionRecordDetails mDrDetails = (InvModDistributionRecordDetails) i.next();

                        this.addInvDrEntry(mDrDetails, null, null, invBranchStockTransfer, AD_BRNCH, AD_CMPNY);
                    }
                    break;
            }

        }
        catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(InvModDistributionRecordDetails mdetails, LocalInvAdjustment invAdjustment,
                               LocalInvStockTransfer invStockTransfer,
                               LocalInvBranchStockTransfer invBranchStockTransfer,
                               Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalAccountNumberInvalidException {

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

            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getDrLine()));
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord
                    = invDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(),
                    mdetails.getDrDebit(), EJBCommon.roundIt(mdetails.getDrAmount(),
                            adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

            if (invAdjustment != null) {

                invAdjustment.addInvDistributionRecord(invDistributionRecord);

            } else if (invStockTransfer != null) {

                invStockTransfer.addInvDistributionRecord(invDistributionRecord);

            } else if (invBranchStockTransfer != null) {

                invBranchStockTransfer.addInvDistributionRecord(invDistributionRecord);
            }

        }
        catch (GlobalAccountNumberInvalidException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvJournalControllerBean ejbCreate");
    }

}