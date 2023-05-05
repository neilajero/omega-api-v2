/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepCreditableWithholdingTaxControllerBean
 * @created March 26, 2004, 6:51 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerTypeHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepCreditableWithholdingTaxDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepCreditableWithholdingTaxControllerEJB")
public class ArRepCreditableWithholdingTaxControllerBean extends EJBContextClass implements ArRepCreditableWithholdingTaxController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;

    public ArrayList executeArRepCreditableWithholdingTax(HashMap criteria, ArrayList branchList, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArRepCreditableWithholdingTaxControllerBean executeArRepCreditableWithholdingTax");

        ArrayList list = new ArrayList();

        try {
            // get all available records

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Date dateFrom = null;
            Date dateTo = null;
            int SEQUENCE_NUMBER = 0;

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE(");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails brDetails = (AdBranchDetails) brIter.next();
            jbossQl.append("ti.tiAdBranch=").append(brDetails.getBrCode());

            while (brIter.hasNext()) {

                brDetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR ti.tiAdBranch=").append(brDetails.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            //  Allocate the size of the object parameter

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiSlSubledgerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
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

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("(ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' OR " + "ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL') " + "AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NULL " + "AND ti.tiWtcCode IS NOT NULL AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList arCustomerList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                //	get available record per customer

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (arCustomerList.contains(glTaxInterface.getTiSlSubledgerCode())) {

                    continue;
                }

                arCustomerList.add(glTaxInterface.getTiSlSubledgerCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' " + "OR ti.tiDocumentType='AR RECEIPT' OR " + "ti.tiDocumentType='GL JOURNAL') ");

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

                jbossQl.append("AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NULL AND ti.tiWtcCode IS NOT NULL AND " + "ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                Collection glTaxInterfaceDocs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                Iterator j = glTaxInterfaceDocs.iterator();

                ArrayList apWtcList = new ArrayList();

                while (j.hasNext()) {

                    //	get available record per wtc code

                    LocalGlTaxInterface glTaxInterfaceDoc = (LocalGlTaxInterface) j.next();

                    if (apWtcList.contains(glTaxInterfaceDoc.getTiWtcCode())) {

                        continue;
                    }

                    apWtcList.add(glTaxInterfaceDoc.getTiWtcCode());

                    jbossQl = new StringBuilder();

                    jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' " + "OR ti.tiDocumentType='AR RECEIPT' OR " + "ti.tiDocumentType='GL JOURNAL') ");

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

                    jbossQl.append("AND ti.tiIsArDocument=1 AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                    Collection glTaxInterfaceWtcs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                    Iterator k = glTaxInterfaceWtcs.iterator();

                    double WITHHOLDING_TAX = 0d;
                    double TAX_BASE = 0d;

                    while (k.hasNext()) {

                        LocalGlTaxInterface glTaxInterfaceWtc = (LocalGlTaxInterface) k.next();

                        if (glTaxInterfaceWtc.getTiDocumentType().equals("GL JOURNAL")) {

                            LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                            if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                                WITHHOLDING_TAX += EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                                TAX_BASE += EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);

                            } else {

                                WITHHOLDING_TAX -= EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                                TAX_BASE -= EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                            }

                        } else {

                            LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterfaceWtc.getTiTxlCode());

                            if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                                WITHHOLDING_TAX += EJBCommon.roundIt(arDistributionRecord.getDrAmount(), PRECISION_UNIT);
                                TAX_BASE += EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);

                            } else {

                                WITHHOLDING_TAX -= EJBCommon.roundIt(arDistributionRecord.getDrAmount(), PRECISION_UNIT);
                                TAX_BASE -= EJBCommon.roundIt(glTaxInterfaceWtc.getTiNetAmount(), PRECISION_UNIT);
                            }
                        }
                    }

                    LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterfaceDoc.getTiWtcCode());

                    ArRepCreditableWithholdingTaxDetails details = new ArRepCreditableWithholdingTaxDetails();
                    details.setCwtSequenceNumber(++SEQUENCE_NUMBER);
                    details.setCwtCstTinNumber(glTaxInterfaceDoc.getTiSlTin());
                    details.setCwtCstCustomerName(glTaxInterfaceDoc.getTiSlName());
                    details.setCwtNatureOfIncomePayment(arWithholdingTaxCode.getWtcDescription());
                    details.setCwtAtcCode(arWithholdingTaxCode.getWtcName());
                    details.setCwtTaxBase(TAX_BASE);
                    details.setCwtRate(arWithholdingTaxCode.getWtcRate());
                    details.setCwtTaxWithheld(WITHHOLDING_TAX);

                    list.add(details);
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
        Debug.print("ArRepCreditableWithholdingTaxControllerBean getAdCompany");
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

    // private
    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArInvoiceEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean method
    public void ejbCreate() throws CreateException {

        Debug.print("ArRepCreditableWithholdingTaxControllerBean ejbCreate");
    }

}