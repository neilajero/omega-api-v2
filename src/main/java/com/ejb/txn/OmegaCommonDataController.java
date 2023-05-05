package com.ejb.txn;

import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.util.ad.AdCompanyDetails;
import com.util.ar.ArTaxCodeDetails;

import jakarta.ejb.Local;
import java.util.Date;

@Local
public interface OmegaCommonDataController {

    byte getAdPrfApUseSupplierPulldown(Integer companyCode);

    short getGlFcPrecisionUnit(Integer companyCode);

    double convertForeignToFunctionalCurrency(Integer fcCode, String fcName, Date conversionDate, double conversionRate, double amount, Integer companyCode);

    short getInvGpCostPrecisionUnit(Integer companyCode);

    short getInvGpQuantityPrecisionUnit(Integer companyCode);

    ArTaxCodeDetails getArTcByTcName(String taxCode, Integer companyCode);

    boolean getInvTraceMisc(String itemName, Integer companyCode);
    byte getInvIiIsTaxByIiName(String II_NM, Integer companyCode);

    double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode);
    double getFrRateByFrNameAndFrDate(String fcName, Date conversionDate, Integer companyCode) throws GlobalConversionDateNotExistException;

    AdCompanyDetails getAdCompany(Integer companyCode);
}