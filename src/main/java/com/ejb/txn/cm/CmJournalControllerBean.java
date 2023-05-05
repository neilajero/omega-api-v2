/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class CmJournalControllerBean
 * @created December 24, 2003, 10:31 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.cm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.dao.cm.LocalCmAdjustmentHome;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.entities.cm.LocalCmFundTransfer;
import com.ejb.dao.cm.LocalCmFundTransferHome;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.mod.cm.CmModDistributionRecordDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmJournalControllerEJB")
public class CmJournalControllerBean extends EJBContextClass implements CmJournalController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalCmAdjustmentHome cmAdjustmentHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalCmFundTransferHome cmFundTransferHome;


    public ArrayList getCmDrByFtCode(Integer FT_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmJournalControllerBean getCmDrByFtCode");

        ArrayList list = new ArrayList();

        try {

            Collection cmDistributionRecords = cmDistributionRecordHome.findByFtCode(FT_CODE, AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : cmDistributionRecords) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) distributionRecord;

                CmModDistributionRecordDetails mdetails = new CmModDistributionRecordDetails();

                mdetails.setDrCode(cmDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(cmDistributionRecord.getDrClass());
                mdetails.setDrAmount(cmDistributionRecord.getDrAmount());
                mdetails.setDrDebit(cmDistributionRecord.getDrDebit());
                mdetails.setDrReversal(cmDistributionRecord.getDrReversal());
                mdetails.setDrCoaAccountNumber(cmDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(cmDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

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

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getCmDrByAdjCode(Integer ADJ_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmJournalControllerBean getCmDrByAdjCode");

        ArrayList list = new ArrayList();

        try {

            Collection cmDistributionRecords = cmDistributionRecordHome.findByAdjCode(ADJ_CODE, AD_CMPNY);

            short lineNumber = 1;

            for (Object distributionRecord : cmDistributionRecords) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) distributionRecord;

                CmModDistributionRecordDetails mdetails = new CmModDistributionRecordDetails();

                mdetails.setDrCode(cmDistributionRecord.getDrCode());
                mdetails.setDrLine(lineNumber);
                mdetails.setDrClass(cmDistributionRecord.getDrClass());
                mdetails.setDrDebit(cmDistributionRecord.getDrDebit());
                mdetails.setDrAmount(cmDistributionRecord.getDrAmount());
                mdetails.setDrReversal(cmDistributionRecord.getDrReversal());
                mdetails.setDrCoaAccountNumber(cmDistributionRecord.getGlChartOfAccount().getCoaAccountNumber());
                mdetails.setDrCoaAccountDescription(cmDistributionRecord.getGlChartOfAccount().getCoaAccountDescription());

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

            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("CmJournalControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveCmDrEntry(Integer PRMRY_KEY, ArrayList drList, String transactionType, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {

        Debug.print("CmJournalControllerBean saveCmDrEntry");

        try {

            LocalCmAdjustment cmAdjustment = null;
            LocalCmFundTransfer cmFundTransfer = null;

            Collection cmDistributionRecords = null;
            Iterator i;

            if (transactionType.equals("BANK ADJUSTMENTS")) {

                cmAdjustment = cmAdjustmentHome.findByPrimaryKey(PRMRY_KEY);

                // remove all distribution records

                cmDistributionRecords = cmAdjustment.getCmDistributionRecords();

                i = cmDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                    i.remove();

                    //	       	  	    cmDistributionRecord.entityRemove();
                    em.remove(cmDistributionRecord);
                }

                // add new distribution records

                i = drList.iterator();

                while (i.hasNext()) {

                    CmModDistributionRecordDetails mDrDetails = (CmModDistributionRecordDetails) i.next();

                    this.addCmDrEntry(mDrDetails, cmAdjustment, null, AD_BRNCH, AD_CMPNY);
                }

            } else if (transactionType.equals("FUND TRANSFER")) {

                cmFundTransfer = cmFundTransferHome.findByPrimaryKey(PRMRY_KEY);

                // remove all distribution records

                cmDistributionRecords = cmFundTransfer.getCmDistributionRecords();

                i = cmDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                    i.remove();

                    //	       	  	    cmDistributionRecord.entityRemove();
                    em.remove(cmDistributionRecord);
                }

                // add new distribution records

                i = drList.iterator();

                while (i.hasNext()) {

                    CmModDistributionRecordDetails mDrDetails = (CmModDistributionRecordDetails) i.next();

                    this.addCmDrEntry(mDrDetails, null, cmFundTransfer, AD_BRNCH, AD_CMPNY);
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

    public short getAdPrfGlJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("CmJournalControllerBean getAdPrfGlJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfGlJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private void addCmDrEntry(CmModDistributionRecordDetails mdetails, LocalCmAdjustment cmAdjustment, LocalCmFundTransfer cmFundTransfer, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalAccountNumberInvalidException {

        Debug.print("CmJournalControllerBean addCmDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate if coa exists

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(mdetails.getDrCoaAccountNumber(), AD_BRNCH, AD_CMPNY);

                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE)
                    throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getDrLine()));

            } catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException(String.valueOf(mdetails.getDrLine()));
            }

            // create distribution record

            LocalCmDistributionRecord cmDistributionRecord = cmDistributionRecordHome.create(mdetails.getDrLine(), mdetails.getDrClass(), EJBCommon.roundIt(mdetails.getDrAmount(), adCompany.getGlFunctionalCurrency().getFcPrecision()), mdetails.getDrDebit(), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            glChartOfAccount.addCmDistributionRecord(cmDistributionRecord);

            if (cmAdjustment != null) {

                cmAdjustment.addCmDistributionRecord(cmDistributionRecord);

            } else if (cmFundTransfer != null) {

                cmFundTransfer.addCmDistributionRecord(cmDistributionRecord);
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

        Debug.print("CmJournalControllerBean ejbCreate");
    }
}