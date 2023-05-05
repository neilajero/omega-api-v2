package com.ejb.txn.ad;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.gen.LocalGenFieldHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.util.*;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ad.AdModCompanyDetails;

@Stateless(name = "AdCompanyControllerEJB")
public class AdCompanyControllerBean extends EJBContextClass implements AdCompanyController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGenFieldHome genFieldHome;

    public ArrayList getGlFcAll(Integer companyCode) {

        Debug.print("ArCustomerEntryControllerBean getGlFcAll");

        ArrayList list = new ArrayList();
        try {
            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(new java.util.Date(), companyCode);
            for (Object functionalCurrency : glFunctionalCurrencies) {
                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;
                list.add(glFunctionalCurrency.getFcName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenFlAll(Integer companyCode) {

        Debug.print("ArCustomerEntryControllerBean getGenFlAll");

        ArrayList list = new ArrayList();
        try {
            Collection genFields = genFieldHome.findFlAllEnabled(companyCode);
            for (Object field : genFields) {
                LocalGenField genField = (LocalGenField) field;
                list.add(genField.getFlName());
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdModCompanyDetails getArCmp(Integer companyCode) {

        Debug.print("AdCompanyControllerBean getArCmp");

        LocalAdCompany adCompany;
        try {
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            AdModCompanyDetails mdetails = new AdModCompanyDetails();
            mdetails.setCmpName(adCompany.getCmpName());
            mdetails.setCmpTaxPayerName(adCompany.getCmpTaxPayerName());
            mdetails.setCmpContact(adCompany.getCmpContact());
            mdetails.setCmpDescription(adCompany.getCmpDescription());
            mdetails.setCmpAddress(adCompany.getCmpAddress());
            mdetails.setCmpCity(adCompany.getCmpCity());
            mdetails.setCmpZip(adCompany.getCmpZip());
            mdetails.setCmpCountry(adCompany.getCmpCountry());
            mdetails.setCmpPhone(adCompany.getCmpPhone());
            mdetails.setCmpFax(adCompany.getCmpFax());
            mdetails.setCmpEmail(adCompany.getCmpEmail());
            mdetails.setCmpTin(adCompany.getCmpTin());
            mdetails.setCmpMailSectionNo(adCompany.getCmpMailSectionNo());
            mdetails.setCmpMailLotNo(adCompany.getCmpMailLotNo());
            mdetails.setCmpMailStreet(adCompany.getCmpMailStreet());
            mdetails.setCmpMailPoBox(adCompany.getCmpMailPoBox());
            mdetails.setCmpMailCountry(adCompany.getCmpMailCountry());
            mdetails.setCmpMailProvince(adCompany.getCmpMailProvince());
            mdetails.setCmpMailPostOffice(adCompany.getCmpMailPostOffice());
            mdetails.setCmpMailCareOff(adCompany.getCmpMailCareOff());
            mdetails.setCmpTaxPeriodFrom(adCompany.getCmpTaxPeriodFrom());
            mdetails.setCmpTaxPeriodTo(adCompany.getCmpTaxPeriodTo());
            mdetails.setCmpPublicOfficeName(adCompany.getCmpPublicOfficeName());
            mdetails.setCmpDateAppointment(adCompany.getCmpDateAppointment());
            mdetails.setCmpRevenueOffice(adCompany.getCmpRevenueOffice());
            mdetails.setCmpFiscalYearEnding(adCompany.getCmpFiscalYearEnding());
            mdetails.setCmpIndustry(adCompany.getCmpIndustry());
            mdetails.setCmpRetainedEarnings(adCompany.getCmpRetainedEarnings());
            mdetails.setCmpGlFcName(adCompany.getGlFunctionalCurrency().getFcName());
            mdetails.setCmpGenFlName(adCompany.getGenField().getFlName());
            return mdetails;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdModCompanyDetails getArCmpByCmpShrtNm(String companyShortName) throws FinderException {

        Debug.print("AdCompanyControllerBean getArCmpByCmpShrtNm");

        LocalAdCompany adCompany;
        try {
            adCompany = adCompanyHome.findByCmpShortName(companyShortName);
            AdModCompanyDetails mdetails = new AdModCompanyDetails();
            mdetails.setCmpName(adCompany.getCmpName());
            mdetails.setCmpTaxPayerName(adCompany.getCmpTaxPayerName());
            mdetails.setCmpContact(adCompany.getCmpContact());
            mdetails.setCmpDescription(adCompany.getCmpDescription());
            mdetails.setCmpAddress(adCompany.getCmpAddress());
            mdetails.setCmpCity(adCompany.getCmpCity());
            mdetails.setCmpZip(adCompany.getCmpZip());
            mdetails.setCmpCountry(adCompany.getCmpCountry());
            mdetails.setCmpPhone(adCompany.getCmpPhone());
            mdetails.setCmpFax(adCompany.getCmpFax());
            mdetails.setCmpEmail(adCompany.getCmpEmail());
            mdetails.setCmpTin(adCompany.getCmpTin());
            mdetails.setCmpMailSectionNo(adCompany.getCmpMailSectionNo());
            mdetails.setCmpMailLotNo(adCompany.getCmpMailLotNo());
            mdetails.setCmpMailStreet(adCompany.getCmpMailStreet());
            mdetails.setCmpMailPoBox(adCompany.getCmpMailPoBox());
            mdetails.setCmpMailCountry(adCompany.getCmpMailCountry());
            mdetails.setCmpMailProvince(adCompany.getCmpMailProvince());
            mdetails.setCmpMailPostOffice(adCompany.getCmpMailPostOffice());
            mdetails.setCmpMailCareOff(adCompany.getCmpMailCareOff());
            mdetails.setCmpTaxPeriodFrom(adCompany.getCmpTaxPeriodFrom());
            mdetails.setCmpTaxPeriodTo(adCompany.getCmpTaxPeriodTo());
            mdetails.setCmpPublicOfficeName(adCompany.getCmpPublicOfficeName());
            mdetails.setCmpDateAppointment(adCompany.getCmpDateAppointment());
            mdetails.setCmpRevenueOffice(adCompany.getCmpRevenueOffice());
            mdetails.setCmpFiscalYearEnding(adCompany.getCmpFiscalYearEnding());
            mdetails.setCmpIndustry(adCompany.getCmpIndustry());
            mdetails.setCmpRetainedEarnings(adCompany.getCmpRetainedEarnings());
            mdetails.setCmpGlFcName(adCompany.getGlFunctionalCurrency().getFcName());
            mdetails.setCmpGenFlName(adCompany.getGenField().getFlName());

            return mdetails;
        } catch (FinderException ex) {
            Debug.printStackTrace(ex);
            throw new FinderException();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveArCmpEntry(AdCompanyDetails details, String currencyName, String genFieldName, Integer companyCode) {

        Debug.print("AdCompanyControllerBean saveArCmpEntry");

        try {
            LocalAdCompany adCompany;
            // update company
            adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            adCompany.setCmpName(details.getCmpName());
            adCompany.setCmpTaxPayerName(details.getCmpTaxPayerName());
            adCompany.setCmpContact(details.getCmpContact());
            adCompany.setCmpDescription(details.getCmpDescription());
            adCompany.setCmpAddress(details.getCmpAddress());
            adCompany.setCmpCity(details.getCmpCity());
            adCompany.setCmpZip(details.getCmpZip());
            adCompany.setCmpCountry(details.getCmpCountry());
            adCompany.setCmpPhone(details.getCmpPhone());
            adCompany.setCmpFax(details.getCmpFax());
            adCompany.setCmpEmail(details.getCmpEmail());
            adCompany.setCmpTin(details.getCmpTin());
            adCompany.setCmpMailSectionNo(details.getCmpMailSectionNo());
            adCompany.setCmpMailLotNo(details.getCmpMailLotNo());
            adCompany.setCmpMailStreet(details.getCmpMailStreet());
            adCompany.setCmpMailPoBox(details.getCmpMailPoBox());
            adCompany.setCmpMailCountry(details.getCmpMailCountry());
            adCompany.setCmpMailProvince(details.getCmpMailProvince());
            adCompany.setCmpMailPostOffice(details.getCmpMailPostOffice());
            adCompany.setCmpMailCareOff(details.getCmpMailCareOff());
            adCompany.setCmpTaxPeriodFrom(details.getCmpTaxPeriodFrom());
            adCompany.setCmpTaxPeriodTo(details.getCmpTaxPeriodTo());
            adCompany.setCmpPublicOfficeName(details.getCmpPublicOfficeName());
            adCompany.setCmpDateAppointment(details.getCmpDateAppointment());
            adCompany.setCmpRevenueOffice(details.getCmpRevenueOffice());
            adCompany.setCmpFiscalYearEnding(details.getCmpFiscalYearEnding());
            adCompany.setCmpIndustry(details.getCmpIndustry());
            adCompany.setCmpRetainedEarnings(details.getCmpRetainedEarnings());

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(currencyName, companyCode);
            glFunctionalCurrency.addAdCompany(adCompany);

            LocalGenField genField = genFieldHome.findByFlName(genFieldName, companyCode);
            genField.addAdCompany(adCompany);
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("AdCompanyControllerBean ejbCreate");
    }
}