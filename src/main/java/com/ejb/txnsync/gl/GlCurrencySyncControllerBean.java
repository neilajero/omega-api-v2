package com.ejb.txnsync.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncResponse;
import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncRequest;
import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

@Stateless(name = "GlCurrencySyncControllerBeanEJB")
public class GlCurrencySyncControllerBean extends EJBContextClass implements GlCurrencySyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;

    @Override
    public String[] getGlFcAll(Integer companyCode, String companyShortName) {

        Debug.print("GlCurrencySyncControllerBean getGlFcAll");

        try {
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(new Date(), companyCode);

            String[] results = new String[glFunctionalCurrencies.size()];

            Iterator i = glFunctionalCurrencies.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) i.next();
                results[ctr] = currencyRowEncode(glFunctionalCurrency, adCompany);
                ctr++;
            }
            return removeDuplicate(results);

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public String[] getGlCurrentFcRates(Integer companyCode, String companyShortName) {

        Debug.print("GlCurrencySyncControllerBean getGlCurrentFcRates");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            float fcRate;
            boolean isFcExist = true;
            try {

                // get fc rate to usd
                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome
                        .findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome
                        .findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);
                fcRate = (float)glFunctionalCurrencyRate.getFrXToUsd();
            } catch (FinderException ex) {

                fcRate = 1;
                isFcExist = false;

            }

            Collection glFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findByFrDate(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);
            String[] results;
            if (isFcExist) {
                results = new String[glFunctionalCurrencyRates.size() + 1];
            } else {
                results = new String[glFunctionalCurrencyRates.size()];
            }

            Iterator i = glFunctionalCurrencyRates.iterator();
            int ctr = 0;
            while (i.hasNext()) {

                LocalGlFunctionalCurrencyRate glForeignCurrencyRate = (LocalGlFunctionalCurrencyRate) i
                        .next();
                // get foreign currency rate to usd
                float foreignRate = (float)glForeignCurrencyRate.getFrXToUsd();

                results[ctr] = this.currencyRateRowEncode(String
                        .valueOf(glForeignCurrencyRate
                                .getGlFunctionalCurrency().getFcCode()), String
                        .valueOf(foreignRate / fcRate));
                ctr++;
            }

            if (isFcExist) {
                // for usd
                LocalGlFunctionalCurrency glUsdFunctionalCurrency = glFunctionalCurrencyHome.findByFcName("USD", companyCode);
                results[ctr] = this.currencyRateRowEncode(String
                        .valueOf(glUsdFunctionalCurrency.getFcCode()), String
                        .valueOf(1 / fcRate));
            }

            return removeDuplicate(results);

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    @Override
    public GlCurrencySyncResponse getGlFcAll(GlCurrencySyncRequest request) {

        Debug.print("GlCurrencySyncControllerBean getGlFcAll");

        GlCurrencySyncResponse response = new GlCurrencySyncResponse();

        LocalAdCompany adCompany;
        Integer companyCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            String[] result = this.getGlFcAll(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public GlCurrencySyncResponse getGlCurrentFcRates(GlCurrencySyncRequest request) {

        Debug.print("GlCurrencySyncControllerBean getGlCurrentFcRates");

        GlCurrencySyncResponse response = new GlCurrencySyncResponse();

        LocalAdCompany adCompany;
        Integer companyCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            String[] result = this.getGlCurrentFcRates(companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResult(result);
            response.setStatus("Set all invoice new and void data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private String currencyRowEncode(LocalGlFunctionalCurrency glFunctionalCurrency, LocalAdCompany adCompany) {

        char separator = EJBCommon.SEPARATOR;
        StringBuffer tempResult = new StringBuffer();
        String encodedResult = new String();

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(glFunctionalCurrency.getFcCode());
        tempResult.append(separator);

        // Currency name
        tempResult.append(glFunctionalCurrency.getFcName());
        tempResult.append(separator);

        // Base Currency
        if (glFunctionalCurrency.getFcName().equals(
                adCompany.getGlFunctionalCurrency().getFcName())) {
            tempResult.append("1");
            tempResult.append(separator);
        } else {
            tempResult.append("0");
            tempResult.append(separator);
        }

        // remove unwanted chars from encodedResult;
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");

        return encodedResult;

    }

    private String currencyRateRowEncode(String primaryKey, String foreignRate) {

        char separator = EJBCommon.SEPARATOR;
        StringBuffer tempResult = new StringBuffer();
        String encodedResult = new String();

        // Start separator
        tempResult.append(separator);

        // Primary Key
        tempResult.append(primaryKey);
        tempResult.append(separator);

        // Foreign Rate
        tempResult.append(foreignRate);
        tempResult.append(separator);


        // remove unwanted chars from encodedResult;
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");

        return encodedResult;

    }

    private String[] removeDuplicate(String[] s) {

        HashSet mySet = new HashSet();

        for (int x = 0; x < s.length; x++) {
            mySet.add(s[x]);
            System.out.println("To be added to set = " + s[x]);
        }

        System.out.println("mySet size=" + mySet.size());

        String[] newResults = new String[mySet.size()];

        Iterator iter = mySet.iterator();

        int newCtr = 0;

        String myString;

        while (iter.hasNext()) {
            myString = (String) iter.next();
            newResults[newCtr] = myString;
            System.out.println("Eto yung string = " + myString);
            newCtr++;
        }

        return newResults;
    }
}