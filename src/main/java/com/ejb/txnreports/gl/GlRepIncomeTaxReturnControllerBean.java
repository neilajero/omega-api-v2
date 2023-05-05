package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArInvoicePaymentScheduleHome;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlReportValue;
import com.ejb.dao.gl.LocalGlReportValueHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ad.AdModCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModReportValueDetails;
import com.util.mod.gl.GlModTaxInterfaceDetails;
import com.util.reports.gl.GlRepIncomeTaxReturnDetails;

@Stateless(name = "GlRepIncomeTaxReturnControllerEJB")
public class GlRepIncomeTaxReturnControllerBean extends EJBContextClass implements GlRepIncomeTaxReturnController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlReportValueHome glReportValueHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;

    public ArrayList getGlTaxInterfaceList(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean executeGlRepMonthlyVatDeclaration");

        try {
            // get available records with ar documents

            ArrayList list = new ArrayList();

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

            jbossQl.append("(ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            for (Object taxInterface : glTaxInterfaces) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                GlModTaxInterfaceDetails details = new GlModTaxInterfaceDetails();

                details.setTiDocumentType(glTaxInterface.getTiDocumentType());
                details.setTiSource(glTaxInterface.getTiSource());
                details.setTiNetAmount(glTaxInterface.getTiNetAmount());
                details.setTiTaxAmount(glTaxInterface.getTiTaxAmount());
                details.setTiSalesAmount(glTaxInterface.getTiSalesAmount());
                details.setTiServicesAmount(glTaxInterface.getTiServicesAmount());
                details.setTiCapitalGoodsAmount(glTaxInterface.getTiCapitalGoodsAmount());
                details.setTiOtherCapitalGoodsAmount(glTaxInterface.getTiOtherCapitalGoodsAmount());
                details.setTiTxnCode(glTaxInterface.getTiTxnCode());
                details.setTiTxnDate(glTaxInterface.getTiTxnDate());
                details.setTiTxnDocumentNumber(glTaxInterface.getTiTxnDocumentNumber());
                details.setTiTaxExempt(glTaxInterface.getTiTaxExempt());
                details.setTiTaxZeroRated(glTaxInterface.getTiTaxZeroRated());
                details.setTiTxlCode(glTaxInterface.getTiTxlCode());
                details.setTiTxlCoaCode(glTaxInterface.getTiTxlCoaCode());
                details.setTiTcCode(glTaxInterface.getTiTcCode());
                details.setTiWtcCode(glTaxInterface.getTiWtcCode());
                details.setTiSlTin(glTaxInterface.getTiSlTin());
                details.setTiSlSubledgerCode(glTaxInterface.getTiSlSubledgerCode());
                details.setTiSlName(glTaxInterface.getTiSlName());
                details.setTiSlAddress(glTaxInterface.getTiSlAddress());
                details.setTiSlAddress2(glTaxInterface.getTiSlAddress2());
                details.setTiIsArDocument(glTaxInterface.getTiIsArDocument());
                details.setTiAdBranch(glTaxInterface.getTiAdBranch());
                details.setTiAdCompany(glTaxInterface.getTiAdCompany());

                if (glTaxInterface.getTiTxlCoaCode() != null) {

                    LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(glTaxInterface.getTiTxlCoaCode());

                    details.setTiTxlCoaNumber(glChartOfAccount.getCoaAccountNumber());
                }

                if (glTaxInterface.getTiTcCode() != null) {
                    if (glTaxInterface.getTiIsArDocument() == EJBCommon.TRUE) {

                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());
                        details.setTiTcName(arTaxCode.getTcName());
                        details.setTiTcRate(arTaxCode.getTcRate());

                    } else {
                        LocalApTaxCode apTaxCode = apTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());
                        details.setTiTcName(apTaxCode.getTcName());
                        details.setTiTcRate(apTaxCode.getTcRate());
                    }
                }

                if (glTaxInterface.getTiWtcCode() != null) {
                    if (glTaxInterface.getTiIsArDocument() == EJBCommon.TRUE) {

                        LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());
                        details.setTiWtcName(arWithholdingTaxCode.getWtcName());
                        details.setTiWtcRate(arWithholdingTaxCode.getWtcRate());

                    } else {
                        LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());
                        details.setTiWtcName(apWithholdingTaxCode.getWtcName());
                        details.setTiWtcRate(apWithholdingTaxCode.getWtcRate());
                    }
                }

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlRepIncomeTaxReturnDetails executeGlIncomeTaxReturn(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean executeGlRepMonthlyVatDeclaration");

        try {
            // get available records with ar documents

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

            Debug.print("AR SSQL=" + jbossQl);
            Debug.print("AR glTaxInterfaces=" + glTaxInterfaces.size());

            Iterator i = glTaxInterfaces.iterator();

            double TOTAL_NET_AMOUNT = 0d;
            double TOTAL_OUTPUT_TAX = 0d;
            double TOTAL_OUTPUT_TAX_NEW = 0d;
            double TOTAL_SALES = 0d;
            double TOTAL_SALES_EXEMPT = 0d;
            double TOTAL_SALES_ZERO_RATED = 0d;
            double TOTAL_SALES_EXEMPT_ZERO = 0d;

            while (i.hasNext()) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) i.next();

                TOTAL_OUTPUT_TAX += EJBCommon.roundIt(glTaxInterface.getTiTaxAmount(), PRECISION_UNIT);
                TOTAL_NET_AMOUNT += EJBCommon.roundIt(glTaxInterface.getTiNetAmount(), PRECISION_UNIT);
                TOTAL_OUTPUT_TAX_NEW += EJBCommon.roundIt(glTaxInterface.getTiTaxAmount(), PRECISION_UNIT);
                TOTAL_SALES += EJBCommon.roundIt(glTaxInterface.getTiSalesAmount(), PRECISION_UNIT);

                TOTAL_SALES_ZERO_RATED += EJBCommon.roundIt(glTaxInterface.getTiTaxZeroRated(), PRECISION_UNIT);
                TOTAL_SALES_EXEMPT_ZERO += EJBCommon.roundIt(glTaxInterface.getTiTaxExempt(), PRECISION_UNIT);
            }

            TOTAL_SALES += TOTAL_SALES_EXEMPT_ZERO;
            // 	TOTAL_SALES_EXEMPT = TOTAL_SALES - TOTAL_SALES_EXEMPT;
            TOTAL_SALES_EXEMPT_ZERO = TOTAL_SALES_EXEMPT + TOTAL_SALES_ZERO_RATED;

            // get available records with ap documents

            jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti ");

            firstArgument = true;
            ctr = 0;

            obj = new Object[criteriaSize];

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

            jbossQl.append("(ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' OR " + "ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='AP RECEIVING ITEM' OR " + "ti.tiDocumentType='GL JOURNAL') AND ti.tiIsArDocument=0 AND ti.tiTcCode IS NOT NULL AND " + "ti.tiWtcCode IS NULL AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            i = glTaxInterfaces.iterator();
            Debug.print("AP SSQL=" + jbossQl);
            Debug.print("AP glTaxInterfaces=" + glTaxInterfaces.size());
            double TOTAL_INPUT_TAX = 0d;
            double TOTAL_INPUT_TAX_NEW = 0d;
            double TOTAL_REVENUE = 0d;
            double TOTAL_REV_EXEMPT = 0d;
            double TOTAL_REV_ZERO_RATED = 0d;
            double TOTAL_REV_EXEMPT_ZERO = 0d;

            while (i.hasNext()) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) i.next();

                TOTAL_INPUT_TAX -= EJBCommon.roundIt(glTaxInterface.getTiTaxAmount(), PRECISION_UNIT);
                TOTAL_INPUT_TAX_NEW -= EJBCommon.roundIt(glTaxInterface.getTiTaxAmount(), PRECISION_UNIT);
                TOTAL_REVENUE -= EJBCommon.roundIt(glTaxInterface.getTiSalesAmount(), PRECISION_UNIT);

                TOTAL_REV_ZERO_RATED += EJBCommon.roundIt(glTaxInterface.getTiTaxZeroRated(), PRECISION_UNIT);
                TOTAL_REV_EXEMPT_ZERO += EJBCommon.roundIt(glTaxInterface.getTiTaxExempt(), PRECISION_UNIT);
            }

            TOTAL_REVENUE += TOTAL_REV_EXEMPT_ZERO;
            // 	TOTAL_REV_EXEMPT = TOTAL_REVENUE - TOTAL_REV_EXEMPT;
            TOTAL_REV_EXEMPT_ZERO = TOTAL_REV_EXEMPT + TOTAL_REV_ZERO_RATED;

            GlRepIncomeTaxReturnDetails details = new GlRepIncomeTaxReturnDetails();

            details.setGrossSalesReceipt(TOTAL_NET_AMOUNT + TOTAL_OUTPUT_TAX);
            details.setTaxDue(TOTAL_OUTPUT_TAX);
            details.setOnTaxableGoods(TOTAL_INPUT_TAX);

            details.setOutputTax(TOTAL_OUTPUT_TAX_NEW);
            details.setInputTax(TOTAL_INPUT_TAX_NEW);
            details.setSalesAmount(TOTAL_SALES);
            details.setRevenue(TOTAL_REVENUE);
            details.setCostOfSales(TOTAL_REVENUE);

            details.setSalesAmountExempt(TOTAL_SALES_EXEMPT);
            details.setSaleAmountZeroRated(TOTAL_SALES_ZERO_RATED);
            details.setTotalSalesExemptZero(TOTAL_SALES_EXEMPT_ZERO);

            details.setRevenueAmountExempt(TOTAL_REV_EXEMPT);
            details.setRevenueAmountZeroRated(TOTAL_REV_ZERO_RATED);
            details.setTotalRevenueExemptZero(TOTAL_REV_EXEMPT_ZERO);

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
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

    public ArrayList getAdLvProvincesAll(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getAdLvProvincesAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL PROVINCES - MONTHLY VAT RETURN", AD_CMPNY);

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

    public ArrayList getAllReportValues(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getAllReportValues");

        ArrayList list = new ArrayList();

        try {

            Collection glReportValueList = glReportValueHome.findRvByRvType("GL_REP_INCOME_TAX_RETURN", AD_CMPNY);

            for (Object o : glReportValueList) {

                LocalGlReportValue glReportValue = (LocalGlReportValue) o;

                GlModReportValueDetails details = new GlModReportValueDetails();

                details.setRvCode(glReportValue.getRvCode());
                details.setRvParameter(glReportValue.getRvParameter());
                details.setRvDescription(glReportValue.getRvDescription());
                details.setRvDefaultValue(glReportValue.getRvDefaultValue());

                if (glReportValue.getGlChartOfAccount() == null) {
                    details.setRvCoaAccountNumber("");
                    details.setRvCoaAccountDescription("");
                } else {

                    details.setRvCoaAccountNumber(glReportValue.getGlChartOfAccount().getCoaAccountNumber());
                    details.setRvCoaAccountDescription(glReportValue.getGlChartOfAccount().getCoaAccountDescription());
                }

                list.add(details);
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public void saveGlReportValue(ArrayList list, Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean saveGlReportValue");

        try {

            // clear all data

            // save all new data
            for (Object o : list) {

                LocalGlReportValue glReportValue = null;

                GlModReportValueDetails details = (GlModReportValueDetails) o;
                Debug.print("code is: " + details.getRvCode());

                if (details.getRvCode() != null) {

                    glReportValue = glReportValueHome.findByPrimaryKey(details.getRvCode());

                    if (details.getRvParameter().equals("")) {

                        //						  glReportValue.entityRemove();
                        em.remove(glReportValue);

                    } else {

                        glReportValue.setRvParameter(details.getRvParameter());
                        glReportValue.setRvDescription(details.getRvDescription());
                        glReportValue.setRvDefaultValue(details.getRvDefaultValue());
                        // if coa account exist

                        Debug.print("COA IS :" + details.getRvCoaAccountNumber());
                        if (!details.getRvCoaAccountNumber().equals("")) {

                            try {
                                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getRvCoaAccountNumber(), AD_CMPNY);

                                glReportValue.setGlChartOfAccount(glChartOfAccount);
                            } catch (FinderException fx) {
                                glReportValue.setGlChartOfAccount(null);
                            }

                        } else {
                            Debug.print("removed chart of account");
                            glReportValue.setGlChartOfAccount(null);
                        }
                    }

                    continue;
                }

                if (details.getRvParameter().equals("")) {
                    continue;
                }

                glReportValue = glReportValueHome.create("GL_REP_INCOME_TAX_RETURN", details.getRvParameter(), details.getRvDescription(), details.getRvDefaultValue(), AD_CMPNY);

                // if coa account exist
                if (!details.getRvCoaAccountNumber().equals("")) {

                    try {
                        LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getRvCoaAccountNumber(), AD_CMPNY);

                        glReportValue.setGlChartOfAccount(glChartOfAccount);
                    } catch (FinderException fx) {
                        glReportValue.setGlChartOfAccount(null);
                    }
                }
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();

            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpZip(adCompany.getCmpZip());
            details.setCmpIndustry(adCompany.getCmpIndustry());

            details.setCmpName(adCompany.getCmpName());
            details.setCmpTaxPayerName(adCompany.getCmpTaxPayerName());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());

            details.setCmpRevenueOffice(adCompany.getCmpRevenueOffice());

            details.setCmpMailSectionNo(adCompany.getCmpMailSectionNo());
            details.setCmpMailLotNo(adCompany.getCmpMailLotNo());
            details.setCmpMailStreet(adCompany.getCmpMailStreet());
            details.setCmpMailPoBox(adCompany.getCmpMailPoBox());
            details.setCmpMailCountry(adCompany.getCmpMailCountry());
            details.setCmpMailProvince(adCompany.getCmpMailProvince());
            details.setCmpMailPostOffice(adCompany.getCmpMailPostOffice());
            details.setCmpMailCareOff(adCompany.getCmpMailCareOff());
            details.setCmpTaxPeriodFrom(adCompany.getCmpTaxPeriodFrom());
            details.setCmpTaxPeriodTo(adCompany.getCmpTaxPeriodTo());
            details.setCmpPublicOfficeName(adCompany.getCmpPublicOfficeName());
            details.setCmpDateAppointment(adCompany.getCmpDateAppointment());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepIncomeTaxWithheldControllerBean ejbCreate");
    }
}