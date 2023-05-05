/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class CmRepReleasedChecksControllerBean
 * @created December 08, 2005, 04:44 PM
 * @author Farrah S. Garing
 */
package com.ejb.txnreports.cm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import com.ejb.entities.ap.LocalApAppliedVoucher;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.dao.ap.LocalApCheckHome;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.cm.CmRepReleasedChecksDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "CmRepReleasedChecksControllerEJB")
public class CmRepReleasedChecksControllerBean extends EJBContextClass implements CmRepReleasedChecksController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalApCheckHome apCheckHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;


    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("CmRepReleasedChecksControllerBean getApScAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);

            for (Object supplierClass : apSupplierClasses) {

                LocalApSupplierClass apSupplierClass = (LocalApSupplierClass) supplierClass;

                list.add(apSupplierClass.getScName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("CmRepReleasedChecksControllerBean getApStAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);

            for (Object supplierType : apSupplierTypes) {

                LocalApSupplierType apSupplierType = (LocalApSupplierType) supplierType;

                list.add(apSupplierType.getStName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBaAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("CmRepReleasedChecksControllerBean getAdBaAll");

        ArrayList list = new ArrayList();

        try {

            Collection adBankAccounts = adBankAccountHome.findEnabledBaAll(AD_BRNCH, AD_CMPNY);

            for (Object bankAccount : adBankAccounts) {

                LocalAdBankAccount adBankAccount = (LocalAdBankAccount) bankAccount;

                list.add(adBankAccount.getBaName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeCmRepReleasedChecks(HashMap criteria, String ORDER_BY, String GROUP_BY, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepReleasedChecksControllerBean executeCmRepReleasedChecks");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(chk) FROM ApCheck chk ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includedUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("status")) {

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

                jbossQl.append("chk.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.apSupplier.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;
            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.apSupplier.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkCheckDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkCheckDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("checkNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("checkNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("checkNumberTo");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("chk.chkDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("((chk.chkPosted = 1 AND chk.chkVoid = 0) OR (chk.chkPosted = 1 AND chk.chkVoid = 1 AND chk.chkVoidPosted = 0)) ");

                } else {

                    jbossQl.append("chk.chkVoid = 0 ");
                }
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

                jbossQl.append("chk.chkAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

                    if (firstLoop == false) {
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

            if (criteria.containsKey("status") && !(criteria.get("status").equals(""))) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String STTS = (String) criteria.get("status");

                if (STTS.equals("RELEASED")) {

                    jbossQl.append("chk.chkReleased = 1 ");

                } else if (STTS.equals("UNRELEASED")) {

                    jbossQl.append("chk.chkReleased = 0 ");
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("chk.chkAdCompany = ").append(AD_CMPNY).append(" ");
            Collection apChecks = apCheckHome.getChkByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apChecks.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object check : apChecks) {

                LocalApCheck apCheck = (LocalApCheck) check;

                CmRepReleasedChecksDetails details = new CmRepReleasedChecksDetails();
                details.setRrcDate(apCheck.getChkDate());
                details.setRrcCheckNumber(apCheck.getChkNumber());
                details.setRrcReferenceNumber(apCheck.getChkReferenceNumber());
                details.setRrcDescription(apCheck.getChkDescription());
                details.setRrcSplSupplierClass(apCheck.getApSupplier().getApSupplierClass().getScName());

                details.setRrcSplSupplierCode(apCheck.getApSupplier().getSplSupplierCode());

                Debug.print("[01] : " + details.getRrcSplSupplierCode());
                Debug.print("[02] : " + apCheck.getApSupplier().getSplSupplierCode() + "-" + apCheck.getChkSupplierName());
                Debug.print("[03] : " + apCheck.getApSupplier().getSplName());

                details.setRrcChkDate(apCheck.getChkCheckDate());
                details.setRrcChkDateReleased(apCheck.getChkDateReleased());

                Collection apAppliedVouchers = apCheck.getApAppliedVouchers();

                for (Object appliedVoucher : apAppliedVouchers) {

                    LocalApAppliedVoucher apAppliedVoucher = (LocalApAppliedVoucher) appliedVoucher;

                    if (!apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApPurchaseOrderLines().isEmpty()) {

                        // find rr from ap_prchs_ordr where po_number = getVouPoNumber
                        Collection apPurchaseOrderLines = apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getApPurchaseOrderLines();
                        ArrayList apPurchaseOrderLineList = new ArrayList(apPurchaseOrderLines);
                        LocalApPurchaseOrderLine apReceivingItem = (LocalApPurchaseOrderLine) apPurchaseOrderLineList.get(0);
                        details.setRrcVouDate(apReceivingItem.getApPurchaseOrder().getPoDate());
                        details.setRrcVouReferenceNumber(apReceivingItem.getApPurchaseOrder().getPoReferenceNumber());

                    } else {
                        details.setRrcVouDate(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouDate());
                        details.setRrcVouReferenceNumber(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getVouReferenceNumber());
                    }
                    details.setRrcVpsDueDate(apAppliedVoucher.getApVoucherPaymentSchedule().getVpsDueDate());

                    // Set Vou Due Date
                    Calendar vouDueDate = new GregorianCalendar();

                    vouDueDate.setTime(details.getRrcVouDate());

                    ArrayList adPaymntSchedules = new ArrayList(apAppliedVoucher.getApVoucherPaymentSchedule().getApVoucher().getAdPaymentTerm().getAdPaymentSchedules());

                    vouDueDate.add(Calendar.DAY_OF_MONTH, ((LocalAdPaymentSchedule) adPaymntSchedules.get(0)).getPsDueDay());
                    details.setVouDueDate(vouDueDate.getTime());
                }
                details.setRrcMemo(apCheck.getChkMemo());

                // type
                if (apCheck.getApSupplier().getApSupplierType() == null) {

                    details.setRrcSplSupplierType("UNDEFINE");

                } else {

                    details.setRrcSplSupplierType(apCheck.getApSupplier().getApSupplierType().getStName());
                }

                details.setRrcAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(apCheck.getGlFunctionalCurrency().getFcCode(), apCheck.getGlFunctionalCurrency().getFcName(), apCheck.getChkConversionDate(), apCheck.getChkConversionRate(), apCheck.getChkAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                details.setRrcBankAccount(apCheck.getAdBankAccount().getBaName());
                details.setOrderBy(ORDER_BY);
                details.setRrcChkReleased(apCheck.getChkReleased() == 1 ? "YES" : "NO");

                list.add(details);
            }

            // sort

            switch (GROUP_BY) {
                case "SUPPLIER CODE":

                    list.sort(CmRepReleasedChecksDetails.SupplierCodeComparator);

                    break;
                case "SUPPLIER TYPE":

                    list.sort(CmRepReleasedChecksDetails.SupplierTypeComparator);

                    break;
                case "SUPPLIER CLASS":

                    list.sort(CmRepReleasedChecksDetails.SupplierClassComparator);

                    break;
                default:

                    list.sort(CmRepReleasedChecksDetails.NoGroupComparator);
                    break;
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("CmRepReleasedChecksControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("CmRepReleasedChecksControllerBean getAdBrResAll");

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

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("CmRepReleasedChecksControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("CmRepReleasedChecksControllerBean ejbCreate");
    }
}