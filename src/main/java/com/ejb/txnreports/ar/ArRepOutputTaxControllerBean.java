/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepOutputTaxControllerBean
 * @created March 25, 2004, 8:56 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepOutputTaxDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepOutputTaxControllerEJB")
public class ArRepOutputTaxControllerBean extends EJBContextClass implements ArRepOutputTaxController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;

    public ArrayList executeArRepOutputTax(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArRepOutputTaxControllerBean executeArRepOutputTax");
        ArrayList list = new ArrayList();

        try {

            // get all available records

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Date dateFrom = null;
            Date dateTo = null;

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE (");

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

            // Allocate the size of the object parameter

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

            jbossQl.append("(ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' OR " + "ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL') " + "AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL " + "AND ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiSlSubledgerCode");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);

            Collection glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

            ArrayList arCustomerList = new ArrayList();

            for (Object taxInterface : glTaxInterfaces) {

                // get available records per customer

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                if (arCustomerList.contains(glTaxInterface.getTiSlSubledgerCode())) {

                    continue;
                }

                arCustomerList.add(glTaxInterface.getTiSlSubledgerCode());

                jbossQl = new StringBuilder();

                jbossQl.append("SELECT DISTINCT OBJECT(ti) FROM GlTaxInterface ti " + "WHERE (ti.tiDocumentType='AR INVOICE' OR ti.tiDocumentType='AR CREDIT MEMO' " + "OR ti.tiDocumentType='AR RECEIPT' OR ti.tiDocumentType='GL JOURNAL') ");

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

                jbossQl.append("AND ti.tiIsArDocument=1 AND ti.tiTcCode IS NOT NULL AND ti.tiWtcCode IS NULL AND " + "ti.tiAdCompany=").append(AD_CMPNY).append(" ORDER BY ti.tiTxnDate");

                double OUTPUT_TAX = 0d;
                double NET_AMOUNT = 0d;
                StringBuilder DOC_NUM = null;

                Collection glTaxInterfaceTcs = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, 0, 0);

                for (Object taxInterfaceTc : glTaxInterfaceTcs) {

                    LocalGlTaxInterface glTaxInterfaceTc = (LocalGlTaxInterface) taxInterfaceTc;
                    Debug.print("intr code: " + glTaxInterfaceTc.getTiCode());
                    if (glTaxInterfaceTc.getTiDocumentType().equals("GL JOURNAL")) {

                        LocalGlJournalLine glJournal = glJournalHome.findByPrimaryKey(glTaxInterfaceTc.getTiTxlCode());

                        if (glJournal.getJlDebit() == EJBCommon.TRUE) {

                            OUTPUT_TAX -= EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                            NET_AMOUNT -= EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);

                        } else {

                            OUTPUT_TAX += EJBCommon.roundIt(glJournal.getJlAmount(), PRECISION_UNIT);
                            NET_AMOUNT += EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);
                        }

                        DOC_NUM = new StringBuilder(glJournal.getGlJournal().getJrDocumentNumber());

                    } else {
                        Debug.print("enter loop 1: " + glTaxInterfaceTc.getTiTxnCode());

                        //	 LocalArDistributionRecord arDistributionRecord = null;

                        if (glTaxInterfaceTc.getTiDocumentType().equals("AR INVOICE")) {

                            Debug.print("its invoice" + glTaxInterfaceTc.getTiTxnCode());
                            try {
                                String invNumber = "";
                                Collection drlist = arDistributionRecordHome.findDrsByDrClassAndInvCode("RECEIVABLE", glTaxInterfaceTc.getTiTxnCode(), AD_CMPNY);

                                for (Object o : drlist) {

                                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) o;
                                    Debug.print("enter loop 1.1");

                                    invNumber = arDistributionRecord.getArInvoice().getInvNumber();
                                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                        Debug.print("enter loop 1.2");

                                        OUTPUT_TAX -= EJBCommon.roundIt(glTaxInterfaceTc.getTiTaxAmount(), PRECISION_UNIT);
                                        NET_AMOUNT -= EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);

                                    } else {
                                        Debug.print("enter loop 1.3");
                                        OUTPUT_TAX += EJBCommon.roundIt(arDistributionRecord.getDrAmount(), PRECISION_UNIT);
                                        NET_AMOUNT += EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);
                                    }
                                }

                                if (DOC_NUM == null) {

                                    DOC_NUM = new StringBuilder(invNumber);
                                } else {

                                    DOC_NUM.append("; ").append(invNumber);
                                }

                            } catch (FinderException ex) {

                            }
                        }

                        if (glTaxInterfaceTc.getTiDocumentType().equals("AR RECEIPT")) {

                            Debug.print("its receiptL " + glTaxInterfaceTc.getTiTxnCode());
                            try {
                                String rctNumber = "";

                                Collection drlist = arDistributionRecordHome.findDrsByDrClassAndRctCode("CASH", glTaxInterfaceTc.getTiTxnCode(), AD_CMPNY);

                                for (Object o : drlist) {
                                    LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) o;

                                    rctNumber = arDistributionRecord.getArReceipt().getRctNumber();
                                    Debug.print("enter loop 1.1");
                                    if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                                        Debug.print("enter loop 1.2");
                                        OUTPUT_TAX -= EJBCommon.roundIt(glTaxInterfaceTc.getTiTaxAmount(), PRECISION_UNIT);
                                        NET_AMOUNT -= EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);

                                    } else {
                                        Debug.print("enter loop 1.3");
                                        OUTPUT_TAX += EJBCommon.roundIt(glTaxInterfaceTc.getTiTaxAmount(), PRECISION_UNIT);
                                        NET_AMOUNT += EJBCommon.roundIt(glTaxInterfaceTc.getTiNetAmount(), PRECISION_UNIT);
                                    }
                                }

                                Debug.print("enter loop 1.4");
                                if (DOC_NUM == null) {

                                    DOC_NUM = new StringBuilder(rctNumber);
                                } else {

                                    DOC_NUM.append("; ").append(rctNumber);
                                }

                            } catch (FinderException ex) {
                                Debug.print("cash not found");
                            }
                        }

                        //	LocalArDistributionRecord arDistributionRecord =
                        // arDistributionRecordHome.findByPrimaryKey(
                        //	glTaxInterfaceTc.getTiTxlCode());
                        //				glTaxInterfaceTc.getTiTxnCode());

                        Debug.print("enter loop 1 end");
                    }
                }

                ArRepOutputTaxDetails details = new ArRepOutputTaxDetails();
                details.setOtTinOfCustomer(glTaxInterface.getTiSlTin());
                details.setOtRegisteredName(glTaxInterface.getTiSlName());
                details.setOtLastName(null);
                details.setOtFirstName(null);
                details.setOtMiddleName(null);
                details.setOtAddress1(glTaxInterface.getTiSlAddress());
                details.setOtAddress2(null);
                details.setOtNetAmount(NET_AMOUNT);
                details.setOtOutputTax(OUTPUT_TAX);
                details.setOtTransaction(DOC_NUM.toString());

                list.add(details);
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
        Debug.print("ArRepOutputTaxControllerBean getAdCompany");
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

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArRepOutputTaxControllerBean getGlFcPrecisionUnit");
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

        Debug.print("ArRepOutputTaxControllerBean ejbCreate");
    }

}