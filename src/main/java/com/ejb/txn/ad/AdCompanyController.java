package com.ejb.txn.ad;

import com.util.ad.AdCompanyDetails;
import com.util.mod.ad.AdModCompanyDetails;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdCompanyController {

    ArrayList getGlFcAll(Integer companyCode);

    ArrayList getGenFlAll(Integer companyCode);

    AdModCompanyDetails getArCmp(Integer companyCode);

    AdModCompanyDetails getArCmpByCmpShrtNm(String companyShortName) throws FinderException;

    void saveArCmpEntry(AdCompanyDetails details, String currencyName, String genFieldName, Integer companyCode);
}