package com.ejb.txn;

import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.ar.ArTaxCodeDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.Date;

@Stateless(name = "OmegaCommonDataControllerEJB")
public class OmegaCommonDataControllerBean extends EJBContextClass implements OmegaCommonDataController {

    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    @Override
    public short getGlFcPrecisionUnit(Integer companyCode) {
        Debug.print("OmegaCommonDataControllerBean getGlFcPrecisionUnit");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            return adCompany.getGlFunctionalCurrency().getFcPrecision();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public double convertForeignToFunctionalCurrency(Integer fcCode, String fcName, Date conversionDate, double conversionRate, double amount, Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (conversionRate != 1 && conversionRate != 0) {

            amount = amount / conversionRate;
        }
        return EJBCommon.roundIt(amount, this.getInvGpCostPrecisionUnit(companyCode));
    }


    public double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean getInvIiUnitCostByIiNameAndUomName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getInvGpCostPrecisionUnit(companyCode));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getInvIiIsTaxByIiName(String II_NM, Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean getInvIiIsTaxByIiName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);

            return invItem.getIiIsTax();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public double convertByUomFromAndUomToAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvUnitOfMeasure invToUnitOfMeasure, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("OmegaCommonDataControllerBean convertByUomFromAndUomToAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invFromUnitOfMeasure.getUomConversionFactor()
                    / invToUnitOfMeasure.getUomConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }


    public double getFrRateByFrNameAndFrDate(String fcName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("OmegaCommonDataControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(fcName, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!fcName.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), conversionDate, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), conversionDate, companyCode);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("OmegaCommonDataControllerBean convertByUomFromAndItemAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                    .findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor()
                    / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);

            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("OmegaCommonDataControllerBean convertByUomAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    @Override
    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("OmegaCommonDataControllerBean getInvGpCostPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvCostPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public ArTaxCodeDetails getArTcByTcName(String taxCode, Integer companyCode) {
        Debug.print("OmegaCommonDataControllerBean getArTcByTcName");
        try {
            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(taxCode, companyCode);
            ArTaxCodeDetails details = new ArTaxCodeDetails();
            details.setTcType(arTaxCode.getTcType());
            details.setTcRate(arTaxCode.getTcRate());
            return details;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {
        Debug.print("OmegaCommonDataControllerBean getInvTraceMisc");
        boolean isTraceMisc = false;
        try {
            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, companyCode);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public AdCompanyDetails getAdCompany(Integer companyCode) {
        Debug.print("OmegaCommonDataControllerBean getAdCompany");
        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
}