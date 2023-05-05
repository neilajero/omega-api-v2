package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ad.AdModCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepIncomeTaxWithheldDetails;

@Stateless(name = "GlRepIncomeTaxWithheldControllerEJB")
public class GlRepIncomeTaxWithheldControllerBean extends EJBContextClass implements GlRepIncomeTaxWithheldController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;

    public ArrayList executeGlRepIncomeTaxWithheld(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepIncomeTaxWithheldControllerBean executeGlRepIncomeTaxWithheld");

        ArrayList list = new ArrayList();

        try {
            // get all available record

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = 0;

            if (!DT_FRM.equals(null)) {
                criteriaSize++;
            }

            if (!DT_TO.equals(null)) {
                criteriaSize++;
            }

            Object[] obj = new Object[criteriaSize];

            if (!DT_FRM.equals(null)) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ti.tiTxnDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = DT_FRM;
                ctr++;
            }

            if (!DT_TO.equals(null)) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ti.tiTxnDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = DT_TO;
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("(ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' OR " + "ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='GL JOURNAL') " + "AND ti.tiIsArDocument=0 AND ti.tiTcCode IS NULL AND ti.tiWtcCode IS NOT NULL AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList apWtcList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                // get available records per wtc code

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (apWtcList.contains(glTaxInterface.getTiWtcCode())) {

                    continue;
                }

                apWtcList.add(glTaxInterface.getTiWtcCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' " + "OR ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='GL JOURNAL') ");

                ctr = 0;
                criteriaSize = 0;
                Integer WTC_CD = glTaxInterface.getTiWtcCode();

                if (DT_FRM != null) {
                    criteriaSize++;
                }

                if (DT_TO != null) {
                    criteriaSize++;
                }

                if (WTC_CD != null) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (WTC_CD != null) {

                    jbossQl.append("AND ti.tiWtcCode =?").append(ctr + 1).append(" ");
                    obj[ctr] = WTC_CD;
                    ctr++;
                }

                if (DT_FRM != null) {

                    jbossQl.append("AND ti.tiTxnDate >=?").append(ctr + 1).append(" ");
                    obj[ctr] = DT_FRM;
                    ctr++;
                }

                if (DT_TO != null) {

                    jbossQl.append("AND ti.tiTxnDate <=?").append(ctr + 1).append(" ");
                    obj[ctr] = DT_TO;
                    ctr++;
                }

                jbossQl.append("AND ti.tiIsArDocument=0 AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                Collection glTaxInterfaceWtcs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                Iterator j = glTaxInterfaceWtcs.iterator();

                double WITHHOLDING_TAX = 0d;
                double NET_AMOUNT = 0d;

                while (j.hasNext()) {

                    LocalGlTaxInterface glTaxInterfaceWtc = (LocalGlTaxInterface) j.next();

                    if (glTaxInterfaceWtc.getTiDocumentType().equals("GL JOURNAL")) {

                        LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                        if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                            WITHHOLDING_TAX -= EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                            NET_AMOUNT -= EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);

                        } else {

                            WITHHOLDING_TAX += EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                            NET_AMOUNT += EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                        }

                    } else {

                        LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                        if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                            WITHHOLDING_TAX -= EJBCommon.roundIt(apDistributionRecord.getDrAmount(), PRECISION_UNIT);
                            NET_AMOUNT -= EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);

                        } else {

                            WITHHOLDING_TAX += EJBCommon.roundIt(apDistributionRecord.getDrAmount(), PRECISION_UNIT);
                            NET_AMOUNT += EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                        }
                    }
                }

                LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());

                GlRepIncomeTaxWithheldDetails details = new GlRepIncomeTaxWithheldDetails();

                details.setNatureOfIncomePayment(apWithholdingTaxCode.getWtcDescription());
                details.setAtcCode(apWithholdingTaxCode.getWtcName());
                details.setTaxBase(NET_AMOUNT);
                details.setTaxRate(apWithholdingTaxCode.getWtcRate());
                details.setTaxRequiredWithheld(WITHHOLDING_TAX);

                list.add(details);
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getAdLvReportTypeAll");


        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL REPORT TYPE - INCOME TAX WITHHELD", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getArCmp(Integer AD_CMPNY) {

        Debug.print("GlRepIncomeTaxWithheldControllerBean getArCmp");

        LocalAdCompany adCompany = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdModCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpZip(adCompany.getCmpZip());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpIndustry(adCompany.getCmpIndustry());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        LocalAdCompany adCompany = null;


        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepIncomeTaxWithheldControllerBean ejbCreate");
    }
}