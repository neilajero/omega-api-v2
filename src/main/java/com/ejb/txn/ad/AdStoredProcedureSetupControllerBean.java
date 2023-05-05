package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;

import com.ejb.entities.ad.LocalAdStoredProcedure;
import com.ejb.dao.ad.LocalAdStoredProcedureHome;
import com.util.EJBContextClass;
import com.util.ad.AdStoredProcedureDetails;
import com.util.Debug;

@Stateless(name = "AdStoredProcedureSetupControllerEJB")
public class AdStoredProcedureSetupControllerBean extends EJBContextClass implements AdStoredProcedureSetupController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdStoredProcedureHome adStoredProcedureHome;

    public AdStoredProcedureDetails getAdSp(Integer companyCode) {

        Debug.print("AdStoredProcedureSetupControllerBean getAdSp");

        LocalAdStoredProcedure adStoredProcedure;
        try {
            adStoredProcedure = adStoredProcedureHome.findBySpAdCompany(companyCode);
            AdStoredProcedureDetails details = new AdStoredProcedureDetails();
            details.setSpEnableGlGeneralLedgerReport(adStoredProcedure.getSpEnableGlGeneralLedgerReport());
            details.setSpNameGlGeneralLedgerReport(adStoredProcedure.getSpNameGlGeneralLedgerReport());
            details.setSpEnableGlTrialBalanceReport(adStoredProcedure.getSpEnableGlTrialBalanceReport());
            details.setSpNameGlTrialBalanceReport(adStoredProcedure.getSpNameGlTrialBalanceReport());
            details.setSpEnableGlIncomeStatementReport(adStoredProcedure.getSpEnableGlIncomeStatementReport());
            details.setSpNameGlIncomeStatementReport(adStoredProcedure.getSpNameGlIncomeStatementReport());
            details.setSpEnableGlBalanceSheetReport(adStoredProcedure.getSpEnableGlBalanceSheetReport());
            details.setSpNameGlBalanceSheetReport(adStoredProcedure.getSpNameGlBalanceSheetReport());
            details.setSpEnableArStatementOfAccountReport(adStoredProcedure.getSpEnableArStatementOfAccountReport());
            details.setSpNameArStatementOfAccountReport(adStoredProcedure.getSpNameArStatementOfAccountReport());
            return details;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveAdSpEntry(AdStoredProcedureDetails details, Integer companyCode) {

        Debug.print("AdStoredProcedureSetupControllerBean saveAdSpEntry");

        try {
            LocalAdStoredProcedure adStoredProcedure;
            // update approval setup
            adStoredProcedure = adStoredProcedureHome.findBySpAdCompany(companyCode);
            adStoredProcedure.setSpEnableGlGeneralLedgerReport(details.getSpEnableGlGeneralLedgerReport());
            adStoredProcedure.setSpNameGlGeneralLedgerReport(details.getSpNameGlGeneralLedgerReport());
            adStoredProcedure.setSpEnableGlTrialBalanceReport(details.getSpEnableGlTrialBalanceReport());
            adStoredProcedure.setSpNameGlTrialBalanceReport(details.getSpNameGlTrialBalanceReport());
            adStoredProcedure.setSpEnableGlIncomeStatementReport(details.getSpEnableGlIncomeStatementReport());
            adStoredProcedure.setSpNameGlIncomeStatementReport(details.getSpNameGlIncomeStatementReport());
            adStoredProcedure.setSpEnableGlBalanceSheetReport(details.getSpEnableGlBalanceSheetReport());
            adStoredProcedure.setSpNameGlBalanceSheetReport(details.getSpNameGlBalanceSheetReport());
            adStoredProcedure.setSpEnableArStatementOfAccountReport(details.getSpEnableArStatementOfAccountReport());
            adStoredProcedure.setSpNameArStatementOfAccountReport(details.getSpNameArStatementOfAccountReport());
            return adStoredProcedure.getSpCode();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdStoredProcedureSetupControllerBean ejbCreate");

    }

}