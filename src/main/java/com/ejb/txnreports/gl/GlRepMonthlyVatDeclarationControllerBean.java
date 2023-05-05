package com.ejb.txnreports.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.dao.ap.LocalApVoucherPaymentScheduleHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.dao.ar.LocalArInvoicePaymentScheduleHome;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepMonthlyVatDeclarationDetails;
import com.util.gl.GlTaxInterfaceDetails;

@Stateless(name = "GlRepMonthlyVatDeclarationControllerEJB")
public class GlRepMonthlyVatDeclarationControllerBean extends EJBContextClass implements GlRepMonthlyVatDeclarationController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalApVoucherPaymentScheduleHome apVoucherPaymentScheduleHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalArInvoicePaymentScheduleHome arInvoicePaymentScheduleHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;


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

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getAdLvReportTypeAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL REPORT TYPE - MONTHLY VAT RETURN", AD_CMPNY);

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

    public GlRepMonthlyVatDeclarationDetails executeGlRepMonthlyVatDeclaration(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

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

            GlRepMonthlyVatDeclarationDetails details = new GlRepMonthlyVatDeclarationDetails();

            details.setGrossSalesReceipt(TOTAL_NET_AMOUNT + TOTAL_OUTPUT_TAX);
            details.setTaxDue(TOTAL_OUTPUT_TAX);
            details.setOnTaxableGoods(TOTAL_INPUT_TAX);

            details.setOutputTax(TOTAL_OUTPUT_TAX_NEW);
            details.setInputTax(TOTAL_INPUT_TAX_NEW);
            details.setSalesAmount(TOTAL_SALES);
            details.setRevenue(TOTAL_REVENUE);

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

    public StringBuilder executeGlRepReliefPurchases(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean executeGlRepReliefPurchases");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            char separator = EJBCommon.COMMA_DELIMITER;
            StringBuilder encodedResult = new StringBuilder();

            // Start separator

            Collection glTaxInterfacesAP = glTaxInterfaceHome.findByTiSourceAndTax("AP", DT_FRM, DT_TO, AD_CMPNY);

            Iterator i = glTaxInterfacesAP.iterator();

            HashMap hm = new HashMap();

            while (i.hasNext()) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) i.next();

                if (hm.containsKey(glTaxInterface.getTiSlTin())) {

                    GlTaxInterfaceDetails glTaxInterfaceExistingDetails = (GlTaxInterfaceDetails) hm.get(glTaxInterface.getTiSlTin());

                    glTaxInterfaceExistingDetails.setTiSalesAmount(glTaxInterfaceExistingDetails.getTiSalesAmount() + glTaxInterface.getTiSalesAmount());
                    glTaxInterfaceExistingDetails.setTiNetAmount(glTaxInterfaceExistingDetails.getTiNetAmount() + glTaxInterface.getTiNetAmount());
                    glTaxInterfaceExistingDetails.setTiTaxAmount(glTaxInterfaceExistingDetails.getTiTaxAmount() + glTaxInterface.getTiTaxAmount());

                    glTaxInterfaceExistingDetails.setTiServicesAmount(glTaxInterfaceExistingDetails.getTiServicesAmount() + glTaxInterface.getTiServicesAmount());
                    glTaxInterfaceExistingDetails.setTiCapitalGoodsAmount(glTaxInterfaceExistingDetails.getTiCapitalGoodsAmount() + glTaxInterface.getTiCapitalGoodsAmount());
                    glTaxInterfaceExistingDetails.setTiOtherCapitalGoodsAmount(glTaxInterfaceExistingDetails.getTiOtherCapitalGoodsAmount() + glTaxInterface.getTiOtherCapitalGoodsAmount());

                    glTaxInterfaceExistingDetails.setTiTaxExempt(glTaxInterfaceExistingDetails.getTiTaxExempt() + glTaxInterface.getTiTaxExempt());
                    glTaxInterfaceExistingDetails.setTiTaxZeroRated(glTaxInterfaceExistingDetails.getTiTaxZeroRated() + glTaxInterface.getTiTaxZeroRated());

                    hm.put(glTaxInterface.getTiSlTin(), glTaxInterfaceExistingDetails);

                } else {

                    GlTaxInterfaceDetails glTaxInterfaceNewDetails = new GlTaxInterfaceDetails();

                    glTaxInterfaceNewDetails.setTiSlName(glTaxInterface.getTiSlName());
                    glTaxInterfaceNewDetails.setTiSlTin(glTaxInterface.getTiSlTin());
                    glTaxInterfaceNewDetails.setTiSlAddress(glTaxInterface.getTiSlAddress());
                    glTaxInterfaceNewDetails.setTiSlAddress2(glTaxInterface.getTiSlAddress2());
                    glTaxInterfaceNewDetails.setTiSalesAmount(glTaxInterface.getTiSalesAmount());
                    glTaxInterfaceNewDetails.setTiNetAmount(glTaxInterface.getTiNetAmount());
                    glTaxInterfaceNewDetails.setTiTaxAmount(glTaxInterface.getTiTaxAmount());

                    glTaxInterfaceNewDetails.setTiServicesAmount(glTaxInterface.getTiServicesAmount());
                    glTaxInterfaceNewDetails.setTiCapitalGoodsAmount(glTaxInterface.getTiCapitalGoodsAmount());
                    glTaxInterfaceNewDetails.setTiOtherCapitalGoodsAmount(glTaxInterface.getTiOtherCapitalGoodsAmount());

                    glTaxInterfaceNewDetails.setTiTaxExempt(glTaxInterface.getTiTaxExempt());
                    glTaxInterfaceNewDetails.setTiTaxZeroRated(glTaxInterface.getTiTaxZeroRated());

                    hm.put(glTaxInterface.getTiSlTin(), glTaxInterfaceNewDetails);
                }
            }

            double SERVICES = 0d;
            double CAPITAL_GOODS = 0d;
            double OTHER_CAPITAL_GOODS = 0d;

            double EXEMPT = 0d;
            double ZERO_RATED = 0d;

            double TAX = 0d;

            Set set = hm.entrySet();

            for (Object o : set) {
                Map.Entry me = (Map.Entry) o;

                GlTaxInterfaceDetails glTaxInterfaceDetails = (GlTaxInterfaceDetails) me.getValue();

                SERVICES += glTaxInterfaceDetails.getTiServicesAmount();
                CAPITAL_GOODS += glTaxInterfaceDetails.getTiCapitalGoodsAmount();
                OTHER_CAPITAL_GOODS += glTaxInterfaceDetails.getTiOtherCapitalGoodsAmount();

                EXEMPT += glTaxInterfaceDetails.getTiTaxExempt();
                ZERO_RATED += glTaxInterfaceDetails.getTiTaxZeroRated();

                TAX += glTaxInterfaceDetails.getTiTaxAmount();

                // LINES
                encodedResult.append("D"); // 1. RECORD TYPE
                encodedResult.append(separator);
                encodedResult.append("P"); // 2. TRANSACTION TYPE
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlTin()); // 3. *VENDOR TIN
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlName()); // 4. *VENDOR NAME
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlAddress()); // 8. ADDRESS 1 ex. House/Bldg#, Street, Barangay
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlAddress2()); // 9. *ADDRESS 2 ex. District, Municipality, City
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiTaxExempt()); // 10. EXEMPT PURCHASES
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiTaxZeroRated()); // 11. ZERO-RATED PURCHASES
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiServicesAmount()); // 12. SERVICES
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiCapitalGoodsAmount()); // 13. CAPITAL GOODS
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiOtherCapitalGoodsAmount()); // 14. GOODS OTHER THAN CAPITAL GOODS
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiTaxAmount()); // 15. INPUT TAX
                encodedResult.append(separator);
                encodedResult.append(adCompany.getCmpTin()); // 16. *OWNERS TIN
                encodedResult.append(separator);
                encodedResult.append(EJBCommon.convertSQLDateToString(DT_TO)); // 17. TAXABLE MONTH ex. 1/1/2018
                encodedResult.append(System.getProperty("line.separator"));
            }

            // HEADER

            StringBuilder headerResult = new StringBuilder();

            headerResult.append("H"); // 1. RECORD TYPE
            headerResult.append(separator);
            headerResult.append("P"); // 2. TRANSACTION TYPE
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpTin()); // 3. *OWNERS TIN
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpName().replace(",", "")); // 4. *REGISTERED NAME
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpName().replace(",", "")); // 8. TRADE NAME
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpAddress().replace(",", "")); // 9. ADDRESS 1 ex. House/Bldg#, Street, Barangay
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpCity().replace(",", "")); // 10. ADDRESS 2 ex. District, Municipality, City
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(EXEMPT, (short) 2)); // 11. EXEMPT PURCHASES
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(ZERO_RATED, (short) 2)); // 12. ZERO-RATED PURCHASES
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(SERVICES, (short) 2)); // 13. SERVICES
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(CAPITAL_GOODS, (short) 2)); // 14. CAPITAL GOODS
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(OTHER_CAPITAL_GOODS, (short) 2)); // 15. GOODS OTHER THAN CAPITAL GOODS
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(TAX, (short) 2)); // 16. INPUT TAX
            headerResult.append(separator);
            headerResult.append(0d); // 17. CREDITABLE
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(TAX, (short) 2)); // 18. NON-CREDITABLE
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpRevenueOffice()); // 19. RDO CODE ex. 050 - South Makati
            headerResult.append(separator);
            headerResult.append(EJBCommon.convertSQLDateToString((DT_TO))); // 20. TAXABLE MONTH ex. 1/1/2018
            headerResult.append(separator);
            headerResult.append(12); // 21. FISCAL YEAR ENDING
            headerResult.append(System.getProperty("line.separator"));

            encodedResult.insert(0, headerResult);

            String sr = "";

            sr = encodedResult.toString();

            sr = sr.replace("\"", " ");
            sr = sr.replace("'", " ");
            sr = sr.replace(";", " ");
            sr = sr.replace("\\", " ");
            sr = sr.replace("|", " ");

            encodedResult = new StringBuilder(sr);

            return encodedResult;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public StringBuilder executeGlRepReliefSales(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean executeGlRepReliefSales");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            char separator = EJBCommon.COMMA_DELIMITER;
            StringBuilder encodedResult = new StringBuilder();

            // Start separator

            Collection glTaxInterfacesAR = glTaxInterfaceHome.findByTiSourceAndTax("AR", DT_FRM, DT_TO, AD_CMPNY);

            Iterator i = glTaxInterfacesAR.iterator();

            HashMap hm = new HashMap();

            while (i.hasNext()) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) i.next();

                if (hm.containsKey(glTaxInterface.getTiSlName() + glTaxInterface.getTiSlTin())) {

                    GlTaxInterfaceDetails glTaxInterfaceExistingDetails = (GlTaxInterfaceDetails) hm.get(glTaxInterface.getTiSlName() + glTaxInterface.getTiSlTin());

                    glTaxInterfaceExistingDetails.setTiSalesAmount(glTaxInterfaceExistingDetails.getTiSalesAmount() + glTaxInterface.getTiSalesAmount());
                    glTaxInterfaceExistingDetails.setTiNetAmount(glTaxInterfaceExistingDetails.getTiNetAmount() + glTaxInterface.getTiNetAmount());
                    glTaxInterfaceExistingDetails.setTiTaxAmount(glTaxInterfaceExistingDetails.getTiTaxAmount() + glTaxInterface.getTiTaxAmount());

                    hm.put(glTaxInterface.getTiSlName() + glTaxInterface.getTiSlTin(), glTaxInterfaceExistingDetails);

                } else {

                    GlTaxInterfaceDetails glTaxInterfaceNewDetails = new GlTaxInterfaceDetails();

                    glTaxInterfaceNewDetails.setTiSlName(glTaxInterface.getTiSlName());
                    glTaxInterfaceNewDetails.setTiSlTin(glTaxInterface.getTiSlTin());
                    glTaxInterfaceNewDetails.setTiSlAddress(glTaxInterface.getTiSlAddress());
                    glTaxInterfaceNewDetails.setTiSlAddress2(glTaxInterface.getTiSlAddress2());
                    glTaxInterfaceNewDetails.setTiSalesAmount(glTaxInterface.getTiSalesAmount());
                    glTaxInterfaceNewDetails.setTiNetAmount(glTaxInterface.getTiNetAmount());
                    glTaxInterfaceNewDetails.setTiTaxAmount(glTaxInterface.getTiTaxAmount());

                    hm.put(glTaxInterface.getTiSlName() + glTaxInterface.getTiSlTin(), glTaxInterfaceNewDetails);
                }
            }

            double NET_AMOUNT = 0d;
            double TAX = 0d;

            Set set = hm.entrySet();

            for (Object o : set) {
                Map.Entry me = (Map.Entry) o;

                GlTaxInterfaceDetails glTaxInterfaceDetails = (GlTaxInterfaceDetails) me.getValue();

                NET_AMOUNT += glTaxInterfaceDetails.getTiNetAmount();
                TAX += glTaxInterfaceDetails.getTiTaxAmount();

                // LINES
                encodedResult.append("D"); // 1. RECORD TYPE
                encodedResult.append(separator);
                encodedResult.append("S"); // 2. TRANSACTION TYPE
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlTin()); // 3. *CUSTOMER TIN
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlName()); // 4. *CUSTOMER NAME
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlAddress().replace(",", "")); // 8. ADDRESS 1 ex. House/Bldg#, Street, Barangay
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiSlAddress2().replace(",", "")); // 9. *ADDRESS 2 ex. District, Municipality, City
                encodedResult.append(separator);
                encodedResult.append(0d); // 10. EXEMPT PURCHASES
                encodedResult.append(separator);
                encodedResult.append(0d); // 11. ZERO-RATED PURCHASES
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiNetAmount()); // 14. TAXABLE SALES (Net of VAT)
                encodedResult.append(separator);
                encodedResult.append(glTaxInterfaceDetails.getTiTaxAmount()); // 15. OUTPUT TAX
                encodedResult.append(separator);
                encodedResult.append(adCompany.getCmpTin()); // 16. *OWNERS TIN
                encodedResult.append(separator);
                encodedResult.append(EJBCommon.convertSQLDateToString(DT_TO)); // 17. TAXABLE MONTH ex. 1/1/2018
                encodedResult.append(System.getProperty("line.separator"));
            }

            // HEADER

            StringBuilder headerResult = new StringBuilder();

            headerResult.append("H"); // 1. RECORD TYPE
            headerResult.append(separator);
            headerResult.append("S"); // 2. TRANSACTION TYPE
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpTin()); // 3. *OWNERS TIN
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpName().replace(",", "")); // 4. *REGISTERED NAME
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpName().replace(",", "")); // 8. TRADE NAME
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpAddress().replace(",", "")); // 9. ADDRESS 1 ex. House/Bldg#, Street, Barangay
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpCity().replace(",", "")); // 10. ADDRESS 2 ex. District, Municipality, City
            headerResult.append(separator);
            headerResult.append(0d); // 11. EXEMPT PURCHASES
            headerResult.append(separator);
            headerResult.append(0d); // 12. ZERO-RATED PURCHASES
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(NET_AMOUNT, (short) 2)); // 13. TAXABLE SALES (Net of VAT)
            headerResult.append(separator);
            headerResult.append(EJBCommon.roundIt(TAX, (short) 2)); // 14. OUTPUT TAX
            headerResult.append(separator);
            headerResult.append(adCompany.getCmpRevenueOffice()); // 15. RDO CODE ex. 050 - South Makati
            headerResult.append(separator);
            headerResult.append(EJBCommon.convertSQLDateToString((DT_TO))); // 16. TAXABLE MONTH ex. 1/1/2018
            headerResult.append(separator);
            headerResult.append(12); // 17. FISCAL YEAR ENDING
            headerResult.append(System.getProperty("line.separator"));

            encodedResult.insert(0, headerResult);

            String sr = "";

            sr = encodedResult.toString();

            sr = sr.replace("\"", " ");
            sr = sr.replace("'", " ");
            sr = sr.replace(";", " ");
            sr = sr.replace("\\", " ");
            sr = sr.replace("|", " ");

            encodedResult = new StringBuilder(sr);

            return encodedResult;

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

    // private

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepMonthlyVatDeclarationControllerBean ejbCreate");
    }
}