/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlCurrencyMaintenanceControllerBean
 * @created
 * @author
 **/
package com.ejb.txn.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyAssignedException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyDeletedException;
import com.ejb.exception.gl.GlFCFunctionalCurrencyAlreadyExistException;
import com.ejb.exception.gl.GlFCNoFunctionalCurrencyFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.gl.GlFunctionalCurrencyDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

@Stateless(name = "GlCurrencyMaintenanceControllerEJB")
public class GlCurrencyMaintenanceControllerBean extends EJBContextClass implements GlCurrencyMaintenanceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;

    public ArrayList getGlFcAll(Integer AD_CMPNY) throws GlFCNoFunctionalCurrencyFoundException {

        Debug.print("GlCurrencyMaintenanceControllerBean getGlFcAll");


        ArrayList fcAllList = new ArrayList();
        Collection glFunctionalCurrencies = null;

        try {
            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(AD_CMPNY);
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.size() == 0) {
            throw new GlFCNoFunctionalCurrencyFoundException();
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {
            LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;
            GlFunctionalCurrencyDetails details = new GlFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glFunctionalCurrency.getFcDescription(), glFunctionalCurrency.getFcCountry(), glFunctionalCurrency.getFcSymbol(), glFunctionalCurrency.getFcPrecision(), glFunctionalCurrency.getFcExtendedPrecision(), glFunctionalCurrency.getFcMinimumAccountUnit(), glFunctionalCurrency.getFcDateFrom(), glFunctionalCurrency.getFcDateTo(), glFunctionalCurrency.getFcEnable());
            fcAllList.add(details);
        }

        return fcAllList;
    }


    public void addGlFcEntry(GlFunctionalCurrencyDetails details, Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyExistException {

        Debug.print("GlFunctionalCurrencyControllerBean addGlFcEntry");

        try {

            glFunctionalCurrencyHome.findByFcName(details.getFcName(), AD_CMPNY);

            getSessionContext().setRollbackOnly();
            throw new GlFCFunctionalCurrencyAlreadyExistException();

        }
        catch (FinderException ex) {

        }

        try {
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                    .FcName(details.getFcName())
                    .FcDescription(details.getFcDescription())
                    .FcCountry(details.getFcCountry())
                    .FcSymbol(details.getFcSymbol())
                    .FcPrecision(details.getFcPrecision())
                    .FcExtendedPrecision( details.getFcExtendedPrecision())
                    .FcMinimumAccountUnit(details.getFcMinimumAccountUnit())
                    .FcDateFrom(details.getFcDateFrom())
                    .FcDateTo(details.getFcDateTo())
                    .FcEnable(details.getFcEnable())
                    .FcAdCompany(AD_CMPNY)
                    .buildFunctionalCurrency();
        }
        catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }


    public void updateGlFcEntry(GlFunctionalCurrencyDetails details, Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyExistException, GlFCFunctionalCurrencyAlreadyAssignedException, GlFCFunctionalCurrencyAlreadyDeletedException {

        Debug.print("GlFunctionalCurrencyControllerBean updateGlFcEntry");

        LocalGlFunctionalCurrency glFunctionalCurrency = null;

        try {
            glFunctionalCurrency = glFunctionalCurrencyHome.findByPrimaryKey(details.getFcCode());
        }
        catch (FinderException ex) {
            throw new GlFCFunctionalCurrencyAlreadyDeletedException();
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glFunctionalCurrency, AD_CMPNY)) {
            updateGlFcEntryWithRelation(glFunctionalCurrency, details, AD_CMPNY);
        } else {

            LocalGlFunctionalCurrency glFunctionalCurrency2 = null;

            try {
                glFunctionalCurrency2 = glFunctionalCurrencyHome.findByFcName(details.getFcName(), AD_CMPNY);
            }
            catch (FinderException ex) {
                glFunctionalCurrency.setFcName(details.getFcName());
                glFunctionalCurrency.setFcDescription(details.getFcDescription());
                glFunctionalCurrency.setFcCountry(details.getFcCountry());
                glFunctionalCurrency.setFcSymbol(details.getFcSymbol());
                glFunctionalCurrency.setFcPrecision(details.getFcPrecision());
                glFunctionalCurrency.setFcExtendedPrecision(details.getFcExtendedPrecision());
                glFunctionalCurrency.setFcMinimumAccountUnit(details.getFcMinimumAccountUnit());
                glFunctionalCurrency.setFcDateFrom(details.getFcDateFrom());
                glFunctionalCurrency.setFcDateTo(details.getFcDateTo());
                glFunctionalCurrency.setFcEnable(details.getFcEnable());
            }
            catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            if (glFunctionalCurrency2 != null && !glFunctionalCurrency.getFcCode().equals(glFunctionalCurrency2.getFcCode())) {
                getSessionContext().setRollbackOnly();
                throw new GlFCFunctionalCurrencyAlreadyExistException();
            } else if (glFunctionalCurrency2 != null && glFunctionalCurrency.getFcCode().equals(glFunctionalCurrency2.getFcCode())) {
                glFunctionalCurrency.setFcDescription(details.getFcDescription());
                glFunctionalCurrency.setFcCountry(details.getFcCountry());
                glFunctionalCurrency.setFcSymbol(details.getFcSymbol());
                glFunctionalCurrency.setFcPrecision(details.getFcPrecision());
                glFunctionalCurrency.setFcExtendedPrecision(details.getFcExtendedPrecision());
                glFunctionalCurrency.setFcMinimumAccountUnit(details.getFcMinimumAccountUnit());
                glFunctionalCurrency.setFcDateFrom(details.getFcDateFrom());
                glFunctionalCurrency.setFcDateTo(details.getFcDateTo());
                glFunctionalCurrency.setFcEnable(details.getFcEnable());
            }
        }
    }


    public void deleteGlFcEntry(Integer FC_CODE, Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyDeletedException, GlFCFunctionalCurrencyAlreadyAssignedException {

        Debug.print("GlFunctionalCurrencyControllerBean deleteGlFcEntry");

        LocalGlFunctionalCurrency glFunctionalCurrency = null;

        try {
            glFunctionalCurrency = glFunctionalCurrencyHome.findByPrimaryKey(FC_CODE);
        }
        catch (FinderException ex) {
            throw new GlFCFunctionalCurrencyAlreadyDeletedException();
        }
        catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }

        if (hasRelation(glFunctionalCurrency, AD_CMPNY)) {
            throw new GlFCFunctionalCurrencyAlreadyAssignedException();
        } else {
            try {
//	    glFunctionalCurrency.entityRemove();
                em.remove(glFunctionalCurrency);
            }
            catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }
        }
    }

    // SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("GlFunctionalCurrencyControllerBean ejbCreate");

    }

    // private methods

    private void updateGlFcEntryWithRelation(LocalGlFunctionalCurrency glFunctionalCurrency, GlFunctionalCurrencyDetails details, Integer AD_CMPNY) throws GlFCFunctionalCurrencyAlreadyAssignedException {

        if (!glFunctionalCurrency.getFcName().equals(details.getFcName()) || !glFunctionalCurrency.getFcCountry().equals(details.getFcCountry()) || glFunctionalCurrency.getFcSymbol() != details.getFcSymbol() || glFunctionalCurrency.getFcPrecision() != details.getFcPrecision() || glFunctionalCurrency.getFcExtendedPrecision() != details.getFcExtendedPrecision() || glFunctionalCurrency.getFcMinimumAccountUnit() != details.getFcMinimumAccountUnit() || glFunctionalCurrency.getFcDateFrom().getTime() != details.getFcDateFrom().getTime()) {

            throw new GlFCFunctionalCurrencyAlreadyAssignedException();
        } else {

            try {
                glFunctionalCurrency.setFcDescription(details.getFcDescription());
                glFunctionalCurrency.setFcDateTo(details.getFcDateTo());
                glFunctionalCurrency.setFcEnable(details.getFcEnable());
            }
            catch (Exception ex) {
                getSessionContext().setRollbackOnly();
                throw new EJBException();
            }
        }
    }

    private boolean hasRelation(LocalGlFunctionalCurrency glFunctionalCurrency, Integer AD_CMPNY) {

        Debug.print("GlFunctionalCurrencyControllerBean hasRelation");

        return !glFunctionalCurrency.getGlJournals().isEmpty() || !glFunctionalCurrency.getGlFunctionalCurrencyRates().isEmpty() || !glFunctionalCurrency.getApRecurringVouchers().isEmpty() || !glFunctionalCurrency.getApVouchers().isEmpty() || !glFunctionalCurrency.getApChecks().isEmpty() || !glFunctionalCurrency.getArInvoices().isEmpty() || !glFunctionalCurrency.getArReceipts().isEmpty() || !glFunctionalCurrency.getAdBankAccounts().isEmpty() || !glFunctionalCurrency.getAdCompanies().isEmpty() || !glFunctionalCurrency.getApPurchaseOrders().isEmpty() || !glFunctionalCurrency.getApPurchaseRequisitions().isEmpty() || !glFunctionalCurrency.getArPdcs().isEmpty() || !glFunctionalCurrency.getArReceipts().isEmpty() || !glFunctionalCurrency.getArSalesOrders().isEmpty() || !glFunctionalCurrency.getGlChartOfAccounts().isEmpty();
    }

}