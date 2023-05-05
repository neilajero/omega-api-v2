package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepWithholdingTaxExpandedDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepWithholdingTaxExpandedControllerEJB")
public class ApRepWithholdingTaxExpandedControllerBean extends EJBContextClass implements ApRepWithholdingTaxExpandedController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
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


    public ArrayList executeApRepWithholdingTaxExpanded(HashMap criteria, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepWithholdingTaxExpandedControllerBean executeApRepWithholdingTaxExpanded");

        ArrayList list = new ArrayList();

        try {
            // get all available records

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Date dateFrom = null;
            Date dateTo = null;
            int SQNC_NMBR = 0;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiSlSubledgerCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ti.tiTxnDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                dateFrom = (Date) criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("ti.tiTxnDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                dateTo = (Date) criteria.get("dateTo");
                ctr++;
            }

            if (adBrnchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("(ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' OR " + "ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='GL JOURNAL') " + "AND ti.tiIsArDocument=0 AND ti.tiTcCode IS NULL AND ti.tiWtcCode IS NOT NULL " + "AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList apSupplierList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                // get available record per supplier

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (apSupplierList.contains(glTaxInterface.getTiSlSubledgerCode())) {

                    continue;
                }

                apSupplierList.add(glTaxInterface.getTiSlSubledgerCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' " + "OR ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='GL JOURNAL') ");

                ctr = 0;
                criteriaSize = 0;
                String sbldgrCode = glTaxInterface.getTiSlSubledgerCode();

                if (dateFrom != null) {
                    criteriaSize++;
                }

                if (dateTo != null) {
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

                if (dateFrom != null) {

                    jbossQl.append("AND ti.tiTxnDate >=?").append(ctr + 1).append(" ");
                    obj[ctr] = dateFrom;
                    ctr++;
                }

                if (dateTo != null) {

                    jbossQl.append("AND ti.tiTxnDate <=?").append(ctr + 1).append(" ");
                    obj[ctr] = dateTo;
                    ctr++;
                }

                if (!adBrnchList.isEmpty()) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("ti.tiAdBranch in (");

                    boolean firstLoop = true;

                    for (Object o : adBrnchList) {

                        if (!firstLoop) {
                            jbossQl.append(", ");
                        } else {
                            firstLoop = false;
                        }

                        AdBranchDetails mdetails = (AdBranchDetails) o;

                        jbossQl.append(mdetails.getBrCode());
                    }

                    jbossQl.append(") ");

                    firstArgument = false;
                }

                jbossQl.append("AND ti.tiIsArDocument=0 AND ti.tiTcCode IS NULL AND ti.tiWtcCode IS NOT NULL AND " + "ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                Collection glTaxInterfaceDocs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                Iterator j = glTaxInterfaceDocs.iterator();

                ArrayList apWtcList = new ArrayList();

                while (j.hasNext()) {

                    // get available record per wtc code

                    LocalGlTaxInterface glTaxInterfaceDoc = (LocalGlTaxInterface) j.next();

                    if (apWtcList.contains(glTaxInterfaceDoc.getTiWtcCode())) {

                        continue;
                    }

                    apWtcList.add(glTaxInterfaceDoc.getTiWtcCode());

                    jbossQl = new StringBuilder();

                    jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AP VOUCHER' OR ti.tiDocumentType='AP DEBIT MEMO' " + "OR ti.tiDocumentType='AP CHECK' OR ti.tiDocumentType='GL JOURNAL') ");

                    ctr = 0;
                    criteriaSize = 0;
                    Integer wtcCode = glTaxInterfaceDoc.getTiWtcCode();

                    if (dateFrom != null) {
                        criteriaSize++;
                    }

                    if (dateTo != null) {
                        criteriaSize++;
                    }

                    if (wtcCode != null) {
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

                    if (dateFrom != null) {

                        jbossQl.append("AND ti.tiTxnDate >=?").append(ctr + 1).append(" ");
                        obj[ctr] = dateFrom;
                        ctr++;
                    }

                    if (dateTo != null) {

                        jbossQl.append("AND ti.tiTxnDate <=?").append(ctr + 1).append(" ");
                        obj[ctr] = dateTo;
                        ctr++;
                    }

                    if (wtcCode != null) {

                        jbossQl.append("AND ti.tiWtcCode =?").append(ctr + 1).append(" ");
                        obj[ctr] = wtcCode;
                        ctr++;
                    }

                    if (!adBrnchList.isEmpty()) {

                        if (!firstArgument) {

                            jbossQl.append("AND ");

                        } else {

                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }

                        jbossQl.append("ti.tiAdBranch in (");

                        boolean firstLoop = true;

                        for (Object o : adBrnchList) {

                            if (!firstLoop) {
                                jbossQl.append(", ");
                            } else {
                                firstLoop = false;
                            }

                            AdBranchDetails mdetails = (AdBranchDetails) o;

                            jbossQl.append(mdetails.getBrCode());
                        }

                        jbossQl.append(") ");

                        firstArgument = false;
                    }

                    jbossQl.append("AND ti.tiIsArDocument=0 AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                    Collection glTaxInterfaceWtcs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                    Iterator k = glTaxInterfaceWtcs.iterator();

                    double WITHHOLDING_TAX = 0d;
                    double NET_AMOUNT = 0d;

                    while (k.hasNext()) {

                        LocalGlTaxInterface glTaxInterfaceWtc = (LocalGlTaxInterface) k.next();

                        if (glTaxInterfaceWtc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                WITHHOLDING_TAX = EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT) * -1;
                                NET_AMOUNT = EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT) * -1;

                            } else {

                                WITHHOLDING_TAX = EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                                NET_AMOUNT = EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                            }

                        } else {

                            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                            if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                WITHHOLDING_TAX = EJBCommon.roundIt(apDistributionRecord.getDrAmount(), PRECISION_UNIT) * -1;
                                NET_AMOUNT = EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT) * -1;

                            } else {

                                WITHHOLDING_TAX = EJBCommon.roundIt(apDistributionRecord.getDrAmount(), PRECISION_UNIT);
                                NET_AMOUNT = EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                            }
                        }
                        LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterfaceDoc.getTiWtcCode());

                        ApRepWithholdingTaxExpandedDetails details = new ApRepWithholdingTaxExpandedDetails();
                        details.setWteSequenceNumber(++SQNC_NMBR);
                        details.setWteSplTinNumber(glTaxInterfaceWtc.getTiSlTin());
                        details.setWteSplSupplierName(glTaxInterfaceWtc.getTiSlName());
                        details.setWteNatureOfIncomePayment(apWithholdingTaxCode.getWtcDescription());
                        details.setWteAtcCode(apWithholdingTaxCode.getWtcName());
                        details.setWteTaxBase(NET_AMOUNT);
                        details.setWteRate(apWithholdingTaxCode.getWtcRate());
                        details.setWteTaxWithheld(WITHHOLDING_TAX);
                        details.setWteTxnDate(glTaxInterfaceWtc.getTiTxnDate());

                        list.add(details);
                    }
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepWithholdingTaxExpandedControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

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

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApVoucherEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepInputTaxControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();


        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    // private

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        // get company and extended precision

        LocalAdCompany adCompany = null;

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepWithholdingTaxExpandedControllerBean ejbCreate");
    }
}