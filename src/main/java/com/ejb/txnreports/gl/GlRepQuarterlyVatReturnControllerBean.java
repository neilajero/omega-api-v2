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
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.gl.GlRepQuarterlyVatReturnDetails;

@Stateless(name = "GlRepQuarterlyVatReturnControllerEJB")
public class GlRepQuarterlyVatReturnControllerBean extends EJBContextClass implements GlRepQuarterlyVatReturnController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;


    public GlRepQuarterlyVatReturnDetails executeGlRepQuarterlyVatReturn(Date DT_FRM, Date DT_TO, Integer AD_CMPNY) {

        Debug.print("GlRepQuarterlyVatReturnControllerBean executeGlRepQuarterlyVatReturn");

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

            jbossQl.append("(ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' OR " + "ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL')" + " AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL" + " AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);
            Debug.print("AR jbossQl.toString()=" + jbossQl);
            Debug.print("SIZE=" + glTaxInterfaces.size());

            Iterator i = glTaxInterfaces.iterator();

            double TOTAL_NET_SALES = 0d;
            double TOTAL_OUTPUT_TAX = 0d;

            while (i.hasNext()) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) i.next();

                if (glTaxInterface.getTiDocumentType().equals("GL JOURNAL")) {

                    LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                    if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                        TOTAL_OUTPUT_TAX -= EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                        TOTAL_NET_SALES -= EJBCommon.roundIt(glTaxInterface.getTiNetAmount(), PRECISION_UNIT);

                    } else {

                        TOTAL_OUTPUT_TAX += EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                        TOTAL_NET_SALES += EJBCommon.roundIt(glTaxInterface.getTiNetAmount(), PRECISION_UNIT);
                    }

                } else {

                    LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                        TOTAL_OUTPUT_TAX -= EJBCommon.roundIt(arDistributionRecord.getDrAmount(), PRECISION_UNIT);
                        TOTAL_NET_SALES -= EJBCommon.roundIt(glTaxInterface.getTiNetAmount(), PRECISION_UNIT);

                    } else {

                        TOTAL_OUTPUT_TAX += EJBCommon.roundIt(arDistributionRecord.getDrAmount(), PRECISION_UNIT);
                        TOTAL_NET_SALES += EJBCommon.roundIt(glTaxInterface.getTiNetAmount(), PRECISION_UNIT);
                    }
                }
            }

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
            Debug.print("AP jbossQl.toString()=" + jbossQl);
            Debug.print("SIZE=" + glTaxInterfaces.size());
            i = glTaxInterfaces.iterator();

            double TOTAL_NET_PURCHASE = 0d;
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
            TOTAL_REV_EXEMPT_ZERO = TOTAL_REV_EXEMPT + TOTAL_REV_ZERO_RATED;

            GlRepQuarterlyVatReturnDetails details = new GlRepQuarterlyVatReturnDetails();
            details.setSalesReceiptForQuarter(TOTAL_NET_SALES);
            details.setTaxOutputQuarter(TOTAL_OUTPUT_TAX);
            details.setNetPurchasesQuarter(TOTAL_NET_PURCHASE);
            details.setInputTaxQuarter(TOTAL_INPUT_TAX);

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvReportTypeAll(Integer AD_CMPNY) {

        Debug.print("GlRepQuarterlyVatReturnControllerBean getAdLvReportTypeAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("GL REPORT TYPE - QUARTERLY VAT RETURN", AD_CMPNY);

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

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GlRepQuarterlyVatReturnControllerBean getAdCompany");

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

        Debug.print("GlRepQuarterlyVatReturnControllerBean getGlFcPrecisionUnit");

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

        Debug.print("GlRepQuarterlyVatReturnControllerBean ejbCreate");
    }
}