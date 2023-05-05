package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "GlRepCsvQuarterlyVatReturnControllerEJB")
public class GlRepCsvQuarterlyVatReturnControllerBean extends EJBContextClass implements GlRepCsvQuarterlyVatReturnController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;


    public ArrayList getCstSalesByDateRange(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepCsvQuarterlyVatReturnControllerBean getCstSalesByDateRange");

        ArrayList list = new ArrayList();

        try {

            double TOTAL_ZERO_RATED_SALES = 0d;
            double TOTAL_TAXABLE_SALES = 0d;
            double TOTAL_OUTPUT_TAX = 0d;

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

            jbossQl.append("(ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' OR " + "ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL') " + "AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList arCustomerList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                // get available records per customer

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (arCustomerList.contains(glTaxInterface.getTiSlSubledgerCode())) {

                    continue;
                }

                if (glTaxInterface.getTiSlCode() == null) {

                    continue;
                }

                arCustomerList.add(glTaxInterface.getTiSlSubledgerCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' " + "OR ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL') ");

                ctr = 0;
                criteriaSize = 0;
                String sbldgrCode = glTaxInterface.getTiSlSubledgerCode();

                if (DT_FRM != null) {
                    criteriaSize++;
                }

                if (DT_TO != null) {
                    criteriaSize++;
                }

                if (sbldgrCode != null) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (sbldgrCode != null) {

                    jbossQl.append("AND ti.tiSlSubledgerCode = ?").append(ctr + 1).append(" ");
                    obj[ctr] = sbldgrCode;
                    ctr++;

                } else {

                    jbossQl.append("AND ti.tiSlSubledgerCode IS NULL ");
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

                jbossQl.append("AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL AND " + "ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                double CST_ZERO_RATED_SALES = 0d;
                double CST_TAXABLE_SALES = 0d;
                double CST_OUTPUT_TAX = 0d;

                Collection glTaxInterfaceDocs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                for (Object taxInterfaceDoc : glTaxInterfaceDocs) {

                    LocalGlTaxInterface glTaxInterfaceDoc = (LocalGlTaxInterface) taxInterfaceDoc;

                    LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName("ZERO-RATED", AD_CMPNY);

                    // zero-rated
                    if (glTaxInterfaceDoc.getTiTcCode().equals(arTaxCode.getTcCode())) {

                        if (glTaxInterfaceDoc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                CST_ZERO_RATED_SALES -= glTaxInterfaceDoc.getTiNetAmount();

                            } else {

                                CST_ZERO_RATED_SALES += glTaxInterfaceDoc.getTiNetAmount();
                            }

                        } else {

                            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                            if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                CST_ZERO_RATED_SALES -= glTaxInterfaceDoc.getTiNetAmount();

                            } else {

                                CST_ZERO_RATED_SALES += glTaxInterfaceDoc.getTiNetAmount();
                            }
                        }

                    } else { // exclusive,inclusive

                        if (glTaxInterfaceDoc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                CST_TAXABLE_SALES -= glTaxInterfaceDoc.getTiNetAmount();
                                CST_OUTPUT_TAX -= glJournal.getJlAmount();

                            } else {

                                CST_TAXABLE_SALES += glTaxInterfaceDoc.getTiNetAmount();
                                CST_OUTPUT_TAX += glJournal.getJlAmount();
                            }
                        } else {

                            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                CST_TAXABLE_SALES -= glTaxInterfaceDoc.getTiNetAmount();
                                CST_OUTPUT_TAX -= arDistributionRecord.getDrAmount();

                            } else {

                                CST_TAXABLE_SALES += glTaxInterfaceDoc.getTiNetAmount();
                                CST_OUTPUT_TAX += arDistributionRecord.getDrAmount();
                            }
                        }
                    }
                }

                TOTAL_ZERO_RATED_SALES += CST_ZERO_RATED_SALES;
                TOTAL_TAXABLE_SALES += CST_TAXABLE_SALES;
                TOTAL_OUTPUT_TAX += CST_OUTPUT_TAX;

                ArModCustomerDetails details = new ArModCustomerDetails();

                LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(glTaxInterface.getTiSlCode());

                details.setCstTin(arCustomer.getCstTin());
                details.setCstName(arCustomer.getCstName());
                details.setCstAddress(arCustomer.getCstAddress());
                details.setCstCity(arCustomer.getCstCity());
                details.setCstStateProvince(arCustomer.getCstStateProvince());
                details.setCstCountry(arCustomer.getCstCountry());

                details.setCstZeroRatedSales(CST_ZERO_RATED_SALES);
                details.setCstTotalZeroRatedSales(TOTAL_ZERO_RATED_SALES);
                details.setCstTaxableSales(CST_TAXABLE_SALES);
                details.setCstTotalTaxableSales(TOTAL_TAXABLE_SALES);
                details.setCstOutputTax(CST_OUTPUT_TAX);
                details.setCstTotalOutputTax(TOTAL_OUTPUT_TAX);

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getSplPurchasesByDateRange(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepCsvQuarterlyVatReturnControllerBean getSplPurchasesByDateRange");

        ArrayList list = new ArrayList();

        try {
            double TOTAL_SPL_ZR_RTD_PRCHS = 0d;
            double TOTAL_SPL_INPT_TX = 0d;

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

            jbossQl.append("(ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' OR " + "ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='AP RECEIVING ITEM' OR " + "ti.tiDocumentType='GL JOURNAL') AND ti.tiIsArDocument=0 " + "AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL" + " AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList apSupplierList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                // get available record per supplier

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (apSupplierList.contains(glTaxInterface.getTiSlSubledgerCode())) {

                    continue;
                }

                if (glTaxInterface.getTiSlCode() == null) {

                    continue;
                }

                apSupplierList.add(glTaxInterface.getTiSlSubledgerCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' " + "OR ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='AP RECEIVING ITEM' OR " + "ti.tiDocumentType='GL JOURNAL') ");

                ctr = 0;
                criteriaSize = 0;
                String sbldgrCode = glTaxInterface.getTiSlSubledgerCode();

                if (DT_FRM != null) {
                    criteriaSize++;
                }

                if (DT_TO != null) {
                    criteriaSize++;
                }

                if (sbldgrCode != null) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (sbldgrCode != null) {

                    jbossQl.append("AND ti.tiSlSubledgerCode = ?").append(ctr + 1).append(" ");
                    obj[ctr] = sbldgrCode;
                    ctr++;

                } else {

                    jbossQl.append("AND ti.tiSlSubledgerCode IS NULL ");
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

                jbossQl.append("AND ti.tiIsArDocument=0 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL AND " + "ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                double SPL_ZR_RTD_PRCHS = 0d;
                double SPL_INPT_TX = 0d;

                Collection glTaxInterfaceDocs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                for (Object taxInterfaceDoc : glTaxInterfaceDocs) {

                    LocalGlTaxInterface glTaxInterfaceDoc = (LocalGlTaxInterface) taxInterfaceDoc;

                    LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName("ZERO-RATED", AD_CMPNY);

                    // zero-rated
                    if (glTaxInterfaceDoc.getTiTcCode().equals(apTaxCode.getTcCode())) {

                        if (glTaxInterfaceDoc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                SPL_ZR_RTD_PRCHS += glTaxInterfaceDoc.getTiNetAmount();

                            } else {

                                SPL_ZR_RTD_PRCHS -= glTaxInterfaceDoc.getTiNetAmount();
                            }

                        } else {

                            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                SPL_ZR_RTD_PRCHS += glTaxInterfaceDoc.getTiNetAmount();

                            } else {

                                SPL_ZR_RTD_PRCHS -= glTaxInterfaceDoc.getTiNetAmount();
                            }
                        }

                    } else { // inclusive/exclusive

                        if (glTaxInterfaceDoc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                SPL_INPT_TX += glJournal.getJlAmount();

                            } else {

                                SPL_INPT_TX -= glJournal.getJlAmount();
                            }

                        } else {

                            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterfaceDoc.getTiTxlCode());

                            if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                SPL_INPT_TX += apDistributionRecord.getDrAmount();

                            } else {

                                SPL_INPT_TX -= apDistributionRecord.getDrAmount();
                            }
                        }
                    }
                }

                TOTAL_SPL_ZR_RTD_PRCHS += SPL_ZR_RTD_PRCHS;
                TOTAL_SPL_INPT_TX += SPL_INPT_TX;

                ApModSupplierDetails details = new ApModSupplierDetails();

                LocalApSupplier apSupplier = apSupplierHome.findByPrimaryKey(glTaxInterface.getTiSlCode());

                details.setSplTin(apSupplier.getSplTin());
                details.setSplName(apSupplier.getSplName());
                details.setSplAddress(apSupplier.getSplAddress());
                details.setSplCity(apSupplier.getSplCity());
                details.setSplStateProvince(apSupplier.getSplStateProvince());
                details.setSplCountry(apSupplier.getSplCountry());

                details.setSplZeroRatedPurchase(SPL_ZR_RTD_PRCHS);
                details.setSplInputTax(SPL_INPT_TX);
                details.setSplTotalZeroRatedPurchase(TOTAL_SPL_ZR_RTD_PRCHS);
                details.setSplTotalInputTax(TOTAL_SPL_INPT_TX);

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("GlRepCsvQuarterlyVatReturnControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepCsvQuarterlyVatReturnControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpName(adCompany.getCmpName());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpZip(adCompany.getCmpZip());
            details.setCmpIndustry(adCompany.getCmpIndustry());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private

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

        Debug.print("GlRepQuarterlyVatReturnControllerBean ejbCreate");
    }
}