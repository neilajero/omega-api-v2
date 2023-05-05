/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindPurchaseRequisitionControllerBean
 * @created April 20, 2005, 9:30 PM
 * @author Aliza D.J. Anos
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.exception.global.*;
import com.ejb.dao.gen.*;
import com.ejb.entities.gl.*;
import com.ejb.dao.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.dao.inv.*;
import com.util.ap.ApPurchaseOrderDetails;
import com.util.mod.ap.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModTagListDetails;

@Stateless(name = "ApFindPurchaseRequisitionControllerEJB")
public class ApFindPurchaseRequisitionControllerBean extends EJBContextClass implements ApFindPurchaseRequisitionController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdAmountLimitHome adAmountLimitHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdApprovalUserHome adApprovalUserHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApCanvassHome apCanvassHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApPurchaseRequisitionLineHome apPurchaseRequisitionLineHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalInvTransactionalBudgetHome invTransactionalBudgetHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;


    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean getAdUsrAll");

        LocalAdUser adUser = null;

        Collection adUsers = null;

        ArrayList list = new ArrayList();

        try {

            adUsers = adUserHome.findUsrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adUsers.isEmpty()) {

            return null;
        }

        for (Object user : adUsers) {

            adUser = (LocalAdUser) user;

            list.add(adUser.getUsrName());
        }

        return list;
    }

    public ArrayList getAdLvDEPARTMENT(Integer AD_CMPNY) {

        Debug.print("ApRepAnnualProcurementControllerBean getAdLvDEPARTMENT");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AD DEPARTMENT", AD_CMPNY);

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

    public ArrayList getGlFcAll(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean getGlFcAll");

        ArrayList list = new ArrayList();

        try {

            Collection glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAll(AD_CMPNY);

            for (Object functionalCurrency : glFunctionalCurrencies) {

                LocalGlFunctionalCurrency glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

                list.add(glFunctionalCurrency.getFcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApPrByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, boolean omitBranch, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseRequisitionControllerBean getApPrByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(pr) FROM ApPurchaseRequisition pr ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("user")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            if (criteria.containsKey("canvassApprovalStatus")) {

                String canvassApprovalStatus = (String) criteria.get("canvassApprovalStatus");

                if (canvassApprovalStatus.equals("DRAFT") || canvassApprovalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pr.prVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("purchaseRequisitionVoid");
            ctr++;

            if (criteria.containsKey("purchaseRequisitionGenerated")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prGenerated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("purchaseRequisitionGenerated");
                ctr++;
            }

            if (criteria.containsKey("user")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prLastModifiedBy = '").append(criteria.get("user")).append("' ");

            }

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT")) {

                    jbossQl.append("pr.prApprovalStatus IS NULL AND pr.prReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("pr.prReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("pr.prApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("canvassApprovalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String canvassApprovalStatus = (String) criteria.get("canvassApprovalStatus");

                if (canvassApprovalStatus.equals("DRAFT")) {

                    jbossQl.append("pr.prCanvassApprovalStatus IS NULL AND pr.prCanvassReasonForRejection IS NULL ");

                } else if (canvassApprovalStatus.equals("REJECTED")) {

                    jbossQl.append("pr.prCanvassReasonForRejection IS NOT NULL ");
                    omitBranch = true;

                } else {

                    jbossQl.append("pr.prCanvassApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = canvassApprovalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("canvassPosted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prCanvassPosted=?").append(ctr + 1).append(" ");

                String canvassPosted = (String) criteria.get("canvassPosted");

                if (canvassPosted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("pr.prNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("pr.prDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("nextRunDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prNextRunDate>=?").append(ctr + 1).append(" ");
                Debug.print("DATE--->" + criteria.get("nextRunDateFrom"));
                obj[ctr] = criteria.get("nextRunDateFrom");
                ctr++;
            }

            if (criteria.containsKey("nextRunDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prNextRunDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("nextRunDateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            if (!omitBranch) {
                jbossQl.append("pr.prAdBranch=").append(AD_BRNCH).append(" ");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
            }

            jbossQl.append("pr.prAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("DATE")) {

                orderBy = "pr.prDate";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "pr.prNumber";
            }

            jbossQl.append("ORDER BY ").append(orderBy);

            Debug.print("SQL :" + jbossQl);
            Collection apPurchaseRequisitions = apPurchaseRequisitionHome.getPrByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (apPurchaseRequisitions.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object purchaseRequisition : apPurchaseRequisitions) {

                LocalApPurchaseRequisition apPurchaseRequisition = (LocalApPurchaseRequisition) purchaseRequisition;

                ApModPurchaseRequisitionDetails mdetails = new ApModPurchaseRequisitionDetails();
                mdetails.setPrCode(apPurchaseRequisition.getPrCode());
                mdetails.setPrNumber(apPurchaseRequisition.getPrNumber());
                mdetails.setPrDate(apPurchaseRequisition.getPrDate());
                mdetails.setPrReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
                LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(apPurchaseRequisition.getPrAdBranch());
                mdetails.setPrAdBranchCode(adBranchCode.getBrBranchCode());
                mdetails.setPrApprovalStatus(apPurchaseRequisition.getPrApprovalStatus() == null ? "DRAFT" : apPurchaseRequisition.getPrApprovalStatus());
                mdetails.setPrLastModifiedBy(apPurchaseRequisition.getPrLastModifiedBy());
                mdetails.setPrDepartment(apPurchaseRequisition.getPrDepartment());

                String supplierName = "";
                double totalAmount = 0;

                Collection apPurchaseRequisionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                for (Object apPurchaseRequisionLine : apPurchaseRequisionLines) {

                    LocalApPurchaseRequisitionLine prlDetails = (LocalApPurchaseRequisitionLine) apPurchaseRequisionLine;

                    Collection canvasses = prlDetails.getApCanvasses();

                    for (Object o : canvasses) {
                        LocalApCanvass canvass = (LocalApCanvass) o;
                        // -- Get only selected canvass
                        if (canvass.getCnvPo() == 1) {
                            if (canvass.getApSupplier() != null) {
                                supplierName = canvass.getApSupplier().getSplName();
                            }
                        }
                    }
                    totalAmount += prlDetails.getPrlAmount();
                }

                mdetails.setPrSupplierName(supplierName);
                mdetails.setPrAmount(totalAmount);
                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getApPrSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseRequisitionControllerBean getApPrSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pr) FROM ApPurchaseRequisition pr ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            if (criteria.containsKey("canvassApprovalStatus")) {

                String canvassApprovalStatus = (String) criteria.get("canvassApprovalStatus");

                if (canvassApprovalStatus.equals("DRAFT") || canvassApprovalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pr.prVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("purchaseRequisitionVoid");
            ctr++;

            if (criteria.containsKey("purchaseRequisitionGenerated")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prGenerated=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("purchaseRequisitionGenerated");
                ctr++;
            }

            if (criteria.containsKey("user")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prLastModifiedBy LIKE '%").append(criteria.get("user")).append("%' ");
                obj[ctr] = criteria.get("user");
                ctr++;
            }

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("currency");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT")) {

                    jbossQl.append("pr.prApprovalStatus IS NULL AND pr.prReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("pr.prReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("pr.prApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("canvassApprovalStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String canvassApprovalStatus = (String) criteria.get("canvassApprovalStatus");

                if (canvassApprovalStatus.equals("DRAFT")) {

                    jbossQl.append("pr.prCanvassApprovalStatus IS NULL AND pr.prCanvassReasonForRejection IS NULL ");

                } else if (canvassApprovalStatus.equals("REJECTED")) {

                    jbossQl.append("pr.prCanvassReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("pr.prCanvassApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = canvassApprovalStatus;
                    ctr++;
                }
            }

            if (criteria.containsKey("canvassPosted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prCanvassPosted=?").append(ctr + 1).append(" ");

                String canvassPosted = (String) criteria.get("canvassPosted");

                if (canvassPosted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pr.prNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("pr.prNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("pr.prDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("nextRunDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prNextRunDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("nextRunDateFrom");
                ctr++;
            }

            if (criteria.containsKey("nextRunDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pr.prNextRunDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("nextRunDateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pr.prAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pr.prAdCompany=").append(AD_CMPNY).append(" ");
            Collection apPurchaseRequisitions = apPurchaseRequisitionHome.getPrByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apPurchaseRequisitions.size() == 0) throw new GlobalNoRecordFoundException();

            return apPurchaseRequisitions.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList generateApPo(Integer PR_CODE, String CRTD_BY, String BR_BRNCH_CODE, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean generateApPo");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, AD_CMPNY);
            Integer AD_BRNCH = adBranch.getBrCode();

            LocalApPurchaseRequisition apPurchaseRequisition = null;

            try {

                apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            } catch (FinderException ex) {

            }

            ArrayList poList = new ArrayList();

            Date CURR_DT = EJBCommon.getGcCurrentDateWoTime().getTime();

            Collection apGenPoLines = apCanvassHome.findByPrCodeAndCnvPo(PR_CODE, EJBCommon.TRUE, AD_CMPNY);

            Iterator i = apGenPoLines.iterator();

            String SPL_SPPLR_CODE = null;
            short PL_LN = 1;

            while (i.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) i.next();

                LocalApSupplier apSupplier = apCanvass.getApSupplier();

                if (!apSupplier.getSplSupplierCode().equals(SPL_SPPLR_CODE)) {

                    SPL_SPPLR_CODE = apSupplier.getSplSupplierCode();
                    PL_LN = 1;

                    String PO_NMBR = null;

                    // generate document number

                    LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                    LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE ORDER", AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    PO_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    PO_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                    // create new purchase order

                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
                    String PO_DESC = "GENERATED PO " + formatter.format(new java.util.Date()) + " > " + apPurchaseRequisition.getPrDescription();

                    // ADDED DESCRIPTION FROM PR TO PO
                    if (!apPurchaseRequisition.getPrDescription().isEmpty() || apPurchaseRequisition.getPrDescription() != null) {
                        PO_DESC = apPurchaseRequisition.getPrDescription();
                    }

                    apPurchaseRequisition.setPrGenerated(EJBCommon.TRUE);
                    apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.FALSE, null, CURR_DT, apPurchaseRequisition.getPrDeliveryPeriod(), PO_NMBR, Integer.toString(apPurchaseRequisition.getPrCode()), null, PO_DESC, null, null, apPurchaseRequisition.getPrConversionDate(), apPurchaseRequisition.getPrConversionRate(), 0d, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, null, CRTD_BY, CURR_DT, CRTD_BY, CURR_DT, null, null, null, null, EJBCommon.FALSE, 0d, 0d, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);

                    apPurchaseOrder.setPoApprovedRejectedBy(apPurchaseRequisition.getPrCanvassApprovedRejectedBy());
                    apPurchaseOrder.setApSupplier(apSupplier);

                    LocalAdPaymentTerm adPaymentTerm = apSupplier.getAdPaymentTerm();
                    apPurchaseOrder.setAdPaymentTerm(adPaymentTerm);

                    LocalGlFunctionalCurrency glFunctionalCurrency = apPurchaseRequisition.getGlFunctionalCurrency();
                    apPurchaseOrder.setGlFunctionalCurrency(glFunctionalCurrency);

                    LocalApTaxCode apTaxCode = apPurchaseRequisition.getApTaxCode();
                    apPurchaseOrder.setApTaxCode(apTaxCode);

                    poList.add(apPurchaseOrder.getPoCode());
                }

                // add purchase order line

                double PL_AMNT = EJBCommon.roundIt(apCanvass.getCnvQuantity() * apCanvass.getCnvUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY));

                LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(PL_LN, apCanvass.getCnvQuantity(), apCanvass.getCnvUnitCost(), PL_AMNT, null, null, null, 0d, 0d, null, 0, 0, 0, 0, 0, AD_CMPNY);

                // REMARKS ADDED
                if (!apCanvass.getApPurchaseRequisitionLine().getPrlRemarks().isEmpty() || apCanvass.getApPurchaseRequisitionLine().getPrlRemarks() != null) {

                    apPurchaseOrderLine.setPlRemarks(apCanvass.getApPurchaseRequisitionLine().getPrlRemarks());
                }

                apPurchaseOrderLine.setApPurchaseOrder(apPurchaseOrder);

                LocalInvUnitOfMeasure invUnitOfMeasure = apCanvass.getApPurchaseRequisitionLine().getInvUnitOfMeasure();
                apPurchaseOrderLine.setInvUnitOfMeasure(invUnitOfMeasure);

                LocalInvItemLocation invItemLocation = apCanvass.getApPurchaseRequisitionLine().getInvItemLocation();
                apPurchaseOrderLine.setInvItemLocation(invItemLocation);

                try {
                    Iterator t = apCanvass.getApPurchaseRequisitionLine().getInvTags().iterator();

                    while (t.hasNext()) {
                        LocalInvTag invPlTag = (LocalInvTag) t.next();

                        LocalInvTag invTag = invTagHome.create(invPlTag.getTgPropertyCode(), invPlTag.getTgSerialNumber(), null, invPlTag.getTgExpiryDate(), invPlTag.getTgSpecs(), AD_CMPNY, invPlTag.getTgTransactionDate(), invPlTag.getTgType());

                        invTag.setApPurchaseOrderLine(apPurchaseOrderLine);

                        invTag.setAdUser(invPlTag.getAdUser());
                    }

                } catch (Exception ex) {

                }

                PL_LN++;
            }

            return poList;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApPoEntry(ApPurchaseOrderDetails details, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE, ArrayList plList, boolean isDraft, String BR_BRNCH_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException {

        Debug.print("ApFindPurchaseRequisitionControllerBean saveApPoEntry");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, AD_CMPNY);
            Integer AD_BRNCH = adBranch.getBrCode();

            // validate if purchase order is already deleted

            try {

                if (details.getPoCode() != null) {

                    apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(details.getPoCode());
                }

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if purchase order is already posted, void, approved or pending

            if (details.getPoCode() != null && details.getPoVoid() == EJBCommon.FALSE) {

                if (apPurchaseOrder.getPoApprovalStatus() != null) {

                    if (apPurchaseOrder.getPoApprovalStatus().equals("APPROVED") || apPurchaseOrder.getPoApprovalStatus().equals("N/A")) {

                        throw new GlobalTransactionAlreadyApprovedException();

                    } else if (apPurchaseOrder.getPoApprovalStatus().equals("PENDING")) {

                        throw new GlobalTransactionAlreadyPendingException();
                    }
                }

                if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyPostedException();

                } else if (apPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidException();
                }
            }

            // purchase order void

            if (details.getPoCode() != null && details.getPoVoid() == EJBCommon.TRUE) {

                apPurchaseOrder.setPoVoid(EJBCommon.TRUE);
                apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
                apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());

                return apPurchaseOrder.getPoCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence

            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getPoCode() == null) {

                    try {

                        adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE ORDER", AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    try {

                        adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    LocalApPurchaseOrder apExistingPurchaseOrder = null;

                    try {

                        apExistingPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(details.getPoDocumentNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (apExistingPurchaseOrder != null) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getPoDocumentNumber() == null || details.getPoDocumentNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                } catch (FinderException ex) {

                                    details.setPoDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                    adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                    break;
                                }

                            } else {

                                try {

                                    apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                } catch (FinderException ex) {

                                    details.setPoDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                    break;
                                }
                            }
                        }
                    }

                } else {

                    LocalApPurchaseOrder apExistingPurchaseOrder = null;

                    try {

                        apExistingPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(details.getPoDocumentNumber(), AD_BRNCH, AD_CMPNY);

                    } catch (FinderException ex) {
                    }

                    if (apExistingPurchaseOrder != null && !apExistingPurchaseOrder.getPoCode().equals(details.getPoCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apPurchaseOrder.getPoDocumentNumber() != details.getPoDocumentNumber() && (details.getPoDocumentNumber() == null || details.getPoDocumentNumber().trim().length() == 0)) {

                        details.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                    }
                }

            } catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            } catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if conversion date exists

            try {

                if (details.getPoConversionDate() != null) {

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPoConversionDate(), AD_CMPNY);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getPoConversionDate(), AD_CMPNY);
                    }
                }

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule

            if (adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY).getAdPaymentSchedules().isEmpty()) {

                throw new GlobalPaymentTermInvalidException();
            }

            boolean isRecalculate = true;

            // create purchase order

            if (details.getPoCode() == null) {

                apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.FALSE, null, details.getPoDate(), details.getPoDeliveryPeriod(), details.getPoDocumentNumber(), details.getPoReferenceNumber(), null, details.getPoDescription(), details.getPoBillTo(), details.getPoShipTo(), details.getPoConversionDate(), details.getPoConversionRate(), 0d, EJBCommon.FALSE, EJBCommon.FALSE, null, null, EJBCommon.FALSE, null, details.getPoCreatedBy(), details.getPoDateCreated(), details.getPoLastModifiedBy(), details.getPoDateLastModified(), null, null, null, null, EJBCommon.FALSE, 0d, 0d, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);

            } else {

                // check if critical fields are changed

                if (!apPurchaseOrder.getApTaxCode().getTcName().equals(TC_NM) || !apPurchaseOrder.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apPurchaseOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) || plList.size() != apPurchaseOrder.getApPurchaseOrderLines().size()) {

                    isRecalculate = true;

                } else if (plList.size() == apPurchaseOrder.getApPurchaseOrderLines().size()) {

                    Iterator ilIter = apPurchaseOrder.getApPurchaseOrderLines().iterator();
                    Iterator ilListIter = plList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();
                        ApModPurchaseOrderLineDetails mdetails = (ApModPurchaseOrderLineDetails) ilListIter.next();

                        if (!apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPlIiName()) || !apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getPlIiDescription()) || !apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPlLocName()) || !apPurchaseOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPlUomName()) || apPurchaseOrderLine.getPlQuantity() != mdetails.getPlQuantity() || apPurchaseOrderLine.getPlUnitCost() != mdetails.getPlUnitCost() || apPurchaseOrderLine.getPlTotalDiscount() != mdetails.getPlTotalDiscount()) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }

                } else {

                    // isRecalculate = false;

                }

                apPurchaseOrder.setPoDate(details.getPoDate());
                apPurchaseOrder.setPoDeliveryPeriod(details.getPoDeliveryPeriod());
                apPurchaseOrder.setPoDocumentNumber(details.getPoDocumentNumber());
                apPurchaseOrder.setPoReferenceNumber(details.getPoReferenceNumber());
                apPurchaseOrder.setPoDescription(details.getPoDescription());
                apPurchaseOrder.setPoBillTo(details.getPoBillTo());
                apPurchaseOrder.setPoShipTo(details.getPoShipTo());
                apPurchaseOrder.setPoPrinted(details.getPoPrinted());
                apPurchaseOrder.setPoVoid(details.getPoVoid());
                apPurchaseOrder.setPoConversionDate(details.getPoConversionDate());
                apPurchaseOrder.setPoConversionRate(details.getPoConversionRate());
                apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
                apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());
            }

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
            apPurchaseOrder.setApSupplier(apSupplier);

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, AD_CMPNY);
            apPurchaseOrder.setAdPaymentTerm(adPaymentTerm);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);
            apPurchaseOrder.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
            apPurchaseOrder.setGlFunctionalCurrency(glFunctionalCurrency);

            double ABS_TOTAL_AMOUNT = 0d;

            // Map Supplier and Item

            for (Object value : plList) {

                ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) value;
                LocalInvItem invItem = invItemHome.findByIiName(mPlDetails.getPlIiName(), AD_CMPNY);

                if (invItem.getApSupplier() != null && !invItem.getApSupplier().getSplSupplierCode().equals(apPurchaseOrder.getApSupplier().getSplSupplierCode())) {

                    throw new GlobalSupplierItemInvalidException("" + mPlDetails.getPlLine());
                }
            }

            if (isRecalculate) {

                // remove all purchase order line items

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                Iterator i = apPurchaseOrderLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();

                    i.remove();

                    // apPurchaseOrderLine.entityRemove();
                    em.remove(apPurchaseOrderLine);
                }

                // add new purchase order line item

                i = plList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();

                    LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(mPlDetails.getPlLine(), mPlDetails.getPlQuantity(), mPlDetails.getPlUnitCost(), mPlDetails.getPlAmount(), null, null, null, 0d, 0d, null, mPlDetails.getPlDiscount1(), mPlDetails.getPlDiscount2(), mPlDetails.getPlDiscount3(), mPlDetails.getPlDiscount4(), mPlDetails.getPlTotalDiscount(), AD_CMPNY);

                    // apPurchaseOrder.addApPurchaseOrderLine(apPurchaseOrderLine);
                    apPurchaseOrderLine.setApPurchaseOrder(apPurchaseOrder);

                    // REMARKS ADDED
                    if (mPlDetails.getPlRemarks().isEmpty() || mPlDetails.getPlRemarks() != null) {

                        apPurchaseOrderLine.setPlRemarks(mPlDetails.getPlRemarks());
                    }

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPlDetails.getPlLocName(), mPlDetails.getPlIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    ABS_TOTAL_AMOUNT += apPurchaseOrderLine.getPlAmount();

                    // invItemLocation.addApPurchaseOrderLine(apPurchaseOrderLine);
                    apPurchaseOrderLine.setInvItemLocation(invItemLocation);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mPlDetails.getPlUomName(), AD_CMPNY);

                    // invUnitOfMeasure.addApPurchaseOrderLine(apPurchaseOrderLine);
                    apPurchaseOrderLine.setInvUnitOfMeasure(invUnitOfMeasure);

                    // TODO: add new inv Tag
                    try {
                        // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
                        Iterator t = mPlDetails.getPlTagList().iterator();

                        LocalInvTag invTag = null;
                        while (t.hasNext()) {
                            InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                            if (tgLstDetails.getTgCode() == null) {
                                invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                                invTag.setApPurchaseOrderLine(apPurchaseOrderLine);
                                LocalAdUser adUser = null;
                                try {
                                    adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                                } catch (FinderException ex) {

                                }
                                invTag.setAdUser(adUser);
                            }
                        }

                    } catch (Exception ex) {

                    }
                }

            } else {

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                for (Object purchaseOrderLine : apPurchaseOrderLines) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                    ABS_TOTAL_AMOUNT += apPurchaseOrderLine.getPlAmount();
                }
            }

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            // set purchase order approval status

            String PO_APPRVL_STATUS = null;

            if (!isDraft) {

                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(AD_CMPNY);

                // check if ap voucher approval is enabled

                if (adApproval.getAprEnableApPurchaseOrder() == EJBCommon.FALSE) {

                    PO_APPRVL_STATUS = "N/A";

                } else {

                    // check if voucher is self approved

                    LocalAdAmountLimit adAmountLimit = null;

                    try {

                        adAmountLimit = adAmountLimitHome.findByAdcTypeAndAuTypeAndUsrName("AP PURCHASE ORDER", "REQUESTER", details.getPoLastModifiedBy(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalNoApprovalRequesterFoundException();
                    }

                    if (ABS_TOTAL_AMOUNT <= adAmountLimit.getCalAmountLimit()) {

                        PO_APPRVL_STATUS = "N/A";

                    } else {

                        // for approval, create approval queue

                        Collection adAmountLimits = adAmountLimitHome.findByAdcTypeAndGreaterThanCalAmountLimit("AP PURCHASE ORDER", adAmountLimit.getCalAmountLimit(), AD_CMPNY);

                        if (adAmountLimits.isEmpty()) {

                            Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                            if (adApprovalUsers.isEmpty()) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }

                            for (Object approvalUser : adApprovalUsers) {

                                LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, apPurchaseOrder, adAmountLimit, adApprovalUser);
                                adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                            }

                        } else {

                            boolean isApprovalUsersFound = false;

                            Iterator i = adAmountLimits.iterator();

                            while (i.hasNext()) {

                                LocalAdAmountLimit adNextAmountLimit = (LocalAdAmountLimit) i.next();

                                if (ABS_TOTAL_AMOUNT <= adNextAmountLimit.getCalAmountLimit()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;
                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, apPurchaseOrder, adAmountLimit, adApprovalUser);
                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;

                                } else if (!i.hasNext()) {

                                    Collection adApprovalUsers = adApprovalUserHome.findByAuTypeAndCalCode("APPROVER", adNextAmountLimit.getCalCode(), AD_CMPNY);

                                    for (Object approvalUser : adApprovalUsers) {

                                        isApprovalUsersFound = true;

                                        LocalAdApprovalUser adApprovalUser = (LocalAdApprovalUser) approvalUser;

                                        LocalAdApprovalQueue adApprovalQueue = createApprovalQueue(AD_BRNCH, AD_CMPNY, adApprovalQueueHome, apPurchaseOrder, adAmountLimit, adApprovalUser);

                                        adApprovalUser.getAdUser().addAdApprovalQueue(adApprovalQueue);
                                    }

                                    break;
                                }

                                adAmountLimit = adNextAmountLimit;
                            }

                            if (!isApprovalUsersFound) {

                                throw new GlobalNoApprovalApproverFoundException();
                            }
                        }

                        PO_APPRVL_STATUS = "PENDING";
                    }
                }

                apPurchaseOrder.setPoApprovalStatus(PO_APPRVL_STATUS);

                // set post purchase order

                if (PO_APPRVL_STATUS.equals("N/A")) {

                    apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                    apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                    apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                    apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                    LocalApPurchaseRequisition apPurchaseRequisition = null;
                    try {
                        apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(new Integer(apPurchaseOrder.getPoReferenceNumber()));

                        for (Object o : plList) {

                            ApModPurchaseOrderLineDetails mdetails = (ApModPurchaseOrderLineDetails) o;
                            this.setInvTbForItemForCurrentMonth(mdetails.getPlIiName(), apPurchaseRequisition.getPrCreatedBy(), apPurchaseRequisition.getPrDate(), mdetails.getPlQuantity(), AD_CMPNY);
                        }
                    } catch (FinderException ex) {

                    }
                }
            }

            return apPurchaseOrder.getPoCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalSupplierItemInvalidException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
                 GlobalTransactionAlreadyApprovedException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseOrderDetails getApPoByPoCode(Integer PO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseRequisitionControllerBean getApPoByPoCode");

        try {

            LocalApPurchaseOrder apPurchaseOrder = null;

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get purchase order lines if any

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();

                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlLine(apPurchaseOrderLine.getPlLine());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount());

                ArrayList tagList = new ArrayList();
                Collection invTags = apPurchaseOrderLine.getInvTags();
                for (Object tag : invTags) {
                    LocalInvTag invTag = (LocalInvTag) tag;
                    InvModTagListDetails tgLstDetails = new InvModTagListDetails();
                    tgLstDetails.setTgPropertyCode(invTag.getTgPropertyCode());
                    tgLstDetails.setTgSpecs(invTag.getTgSpecs());
                    tgLstDetails.setTgExpiryDate(invTag.getTgExpiryDate());
                    tgLstDetails.setTgSerialNumber(invTag.getTgSerialNumber());
                    try {

                        tgLstDetails.setTgCustodian(invTag.getAdUser().getUsrName());
                    } catch (Exception ex) {
                        tgLstDetails.setTgCustodian("");
                    }

                    tagList.add(tgLstDetails);

                }

                plDetails.setPlTagList(tagList);

                list.add(plDetails);
            }

            ApModPurchaseOrderDetails mPoDetails = new ApModPurchaseOrderDetails();

            mPoDetails.setPoCode(apPurchaseOrder.getPoCode());
            mPoDetails.setPoDate(apPurchaseOrder.getPoDate());
            mPoDetails.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
            mPoDetails.setPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
            mPoDetails.setPoDescription(apPurchaseOrder.getPoDescription());
            mPoDetails.setPoVoid(apPurchaseOrder.getPoVoid());
            mPoDetails.setPoBillTo(apPurchaseOrder.getPoBillTo());
            mPoDetails.setPoShipTo(apPurchaseOrder.getPoShipTo());
            mPoDetails.setPoConversionDate(apPurchaseOrder.getPoConversionDate());
            mPoDetails.setPoConversionRate(apPurchaseOrder.getPoConversionRate());
            mPoDetails.setPoApprovalStatus(apPurchaseOrder.getPoApprovalStatus());
            mPoDetails.setPoPosted(apPurchaseOrder.getPoPosted());
            mPoDetails.setPoCreatedBy(apPurchaseOrder.getPoCreatedBy());
            mPoDetails.setPoDateCreated(apPurchaseOrder.getPoDateCreated());
            mPoDetails.setPoLastModifiedBy(apPurchaseOrder.getPoLastModifiedBy());
            mPoDetails.setPoDateLastModified(apPurchaseOrder.getPoDateLastModified());
            mPoDetails.setPoApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy());
            mPoDetails.setPoDateApprovedRejected(apPurchaseOrder.getPoDateApprovedRejected());
            mPoDetails.setPoPostedBy(apPurchaseOrder.getPoPostedBy());
            mPoDetails.setPoDatePosted(apPurchaseOrder.getPoDatePosted());
            mPoDetails.setPoReasonForRejection(apPurchaseOrder.getPoReasonForRejection());
            mPoDetails.setPoSplSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
            mPoDetails.setPoPytName(apPurchaseOrder.getAdPaymentTerm().getPytName());
            mPoDetails.setPoTcName(apPurchaseOrder.getApTaxCode().getTcName());
            mPoDetails.setPoTcType(apPurchaseOrder.getApTaxCode().getTcType());
            mPoDetails.setPoTcRate(apPurchaseOrder.getApTaxCode().getTcRate());
            mPoDetails.setPoFcName(apPurchaseOrder.getGlFunctionalCurrency().getFcName());
            mPoDetails.setPoPrinted(EJBCommon.FALSE);
            mPoDetails.setPoSplName(apPurchaseOrder.getApSupplier().getSplName());

            mPoDetails.setPoPlList(list);

            return mPoDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseRequisitionDetails getApPrByPrCode(Integer PR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseRequisitionControllerBean getApPrByPrCode");

        try {

            LocalApPurchaseRequisition apPurchaseRequisition = null;

            try {

                apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get purchase order lines if any

            Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

            for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {

                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;

                ApModPurchaseRequisitionLineDetails plDetails = new ApModPurchaseRequisitionLineDetails();

                plDetails.setPrlCode(apPurchaseRequisitionLine.getPrlCode());
                plDetails.setPrlLine(apPurchaseRequisitionLine.getPrlLine());
                plDetails.setPrlIlIiName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPrlIlLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());
                plDetails.setPrlUomName(apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPrlAmount(apPurchaseRequisitionLine.getPrlAmount());

                list.add(plDetails);
            }

            ApModPurchaseRequisitionDetails mPrDetails = new ApModPurchaseRequisitionDetails();

            mPrDetails.setPrPrlList(list);

            return mPrDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer generateApPr(ArrayList prSelectedList, Integer AD_BRNCH, String AD_USR, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean generateApPr");

        try {

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");

            LocalApPurchaseRequisition apPurchaseRequisition = null;
            Iterator i = prSelectedList.iterator();

            Double qty = 0d;
            while (i.hasNext()) {

                // validate if document number is unique document number is automatic then set
                // next sequence

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
                String prNumber = null;

                try {

                    adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE REQUISITION", AD_CMPNY);

                } catch (FinderException ex) {

                }

                try {

                    adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                    while (true) {

                        if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                            } catch (FinderException ex) {

                                prNumber = adDocumentSequenceAssignment.getDsaNextSequence();
                                adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                break;
                            }

                        } else {

                            try {

                                apPurchaseRequisitionHome.findByPrNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                            } catch (FinderException ex) {

                                prNumber = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                                adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                break;
                            }
                        }
                    }
                }

                LocalApPurchaseRequisition apForConPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey((Integer) i.next());

                // create purchase requisition
                apPurchaseRequisition = apPurchaseRequisitionHome.create("GENERATED PR " + formatter.format(new java.util.Date()), prNumber, EJBCommon.getGcCurrentDateWoTime().getTime(), apForConPurchaseRequisition.getPrDeliveryPeriod(), null, apForConPurchaseRequisition.getPrConversionDate(), apForConPurchaseRequisition.getPrConversionRate(), null, EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, AD_USR, EJBCommon.getGcCurrentDateWoTime().getTime(), AD_USR, new java.util.Date(), AD_USR, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, null, null, null, null, "REGULAR", "Inventoriable", null, null, null, null, AD_BRNCH, AD_CMPNY);

                LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

                LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(adPreference.getPrfApDefaultPrTax(), AD_CMPNY);
                apPurchaseRequisition.setApTaxCode(apTaxCode);

                LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adPreference.getPrfApDefaultPrCurrency(), AD_CMPNY);
                apPurchaseRequisition.setGlFunctionalCurrency(glFunctionalCurrency);

                Collection apPurchaseRequisitionLines = apForConPurchaseRequisition.getApPurchaseRequisitionLines();

                Iterator j = apPurchaseRequisitionLines.iterator();

                short ctr = 0;
                while (j.hasNext()) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) j.next();

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLineNew = apPurchaseRequisitionLineHome.create(++ctr, apPurchaseRequisitionLine.getPrlQuantity(), apPurchaseRequisitionLine.getPrlAmount(), apPurchaseRequisitionLine.getPrlRemarks(), AD_CMPNY);
                    apPurchaseRequisition.addApPurchaseRequisitionLine(apPurchaseRequisitionLineNew);
                    LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName(), AD_CMPNY);
                    invItemLocation.addApPurchaseRequisitionLine(apPurchaseRequisitionLineNew);
                    apPurchaseRequisitionLineNew.setPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());
                    apPurchaseRequisitionLineNew.setPrlAmount(invItemLocation.getInvItem().getIiUnitCost());

                    LocalInvUnitOfMeasure invUnitOfMeasure = invItemLocation.getInvItem().getInvUnitOfMeasure();
                    invUnitOfMeasure.addApPurchaseRequisitionLine(apPurchaseRequisitionLineNew);

                    Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();
                    Iterator k = apCanvasses.iterator();
                    double PRL_AMNT = 0d;
                    double PRL_QTTY = 0d;

                    while (k.hasNext()) {
                        LocalApCanvass apCanvass = (LocalApCanvass) k.next();

                        LocalApCanvass apCanvassNew = apCanvassHome.create(apCanvass.getCnvLine(), apCanvass.getCnvRemarks(), apCanvass.getCnvQuantity(), apCanvass.getCnvUnitCost(), EJBCommon.roundIt(apCanvass.getCnvQuantity() * apCanvass.getCnvUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY)), apCanvass.getCnvPo(), AD_CMPNY);

                        apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

                        LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(apCanvass.getApSupplier().getSplSupplierCode(), AD_CMPNY);

                        Debug.print(apSupplier.getSplSupplierCode());
                        apSupplier.addApCanvass(apCanvassNew);
                        apPurchaseRequisitionLineNew.addApCanvass(apCanvassNew);
                        if (apCanvass.getCnvPo() == 1) {
                            PRL_AMNT += apCanvass.getCnvAmount();

                            PRL_QTTY += apCanvass.getCnvQuantity();
                        }
                    }

                }

                // adjust recurring pr date.
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(apForConPurchaseRequisition.getPrNextRunDate());

                switch (apForConPurchaseRequisition.getPrSchedule()) {
                    case "DAILY":

                        gc.add(Calendar.DATE, 1);

                        break;
                    case "WEEKLY":

                        gc.add(Calendar.DATE, 7);

                        break;
                    case "SEMI MONTHLY":

                        gc.add(Calendar.DATE, 15);

                        break;
                    case "MONTHLY":

                        gc.add(Calendar.MONTH, 1);

                        break;
                    case "QUARTERLY":

                        gc.add(Calendar.MONTH, 3);

                        break;
                    case "SEMI ANNUALLY":

                        gc.add(Calendar.MONTH, 6);

                        break;
                    case "ANNUALLY":

                        gc.add(Calendar.YEAR, 1);
                        break;
                }

                apForConPurchaseRequisition.setPrLastRunDate(EJBCommon.getGcCurrentDateWoTime().getTime());
                apForConPurchaseRequisition.setPrNextRunDate(gc.getTime());
            }

            return apPurchaseRequisition.getPrCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer consolidateApPr(ArrayList prSelectedList, Integer AD_BRNCH, String AD_USR, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean consolidateApPr");


        try {

            // validate if document number is unique document number is automatic then set
            // next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            String prNumber = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP PURCHASE REQUISITION", AD_CMPNY);

            } catch (FinderException ex) {

            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            } catch (FinderException ex) {

            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            apPurchaseRequisitionHome.findByPrNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        } catch (FinderException ex) {

                            prNumber = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            apPurchaseRequisitionHome.findByPrNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        } catch (FinderException ex) {

                            prNumber = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSS");
            // create purchase requisition
            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.create("CONSOLIDATED PR " + formatter.format(new java.util.Date()), prNumber, new java.util.Date(), null, null, null, 1.000000, "APPROVED", EJBCommon.TRUE, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, AD_USR, new java.util.Date(), AD_USR, new java.util.Date(), AD_USR, new java.util.Date(), null, null, null, null, null, null, "REGULAR", "Inventoriable", null, null, null, null, AD_BRNCH, AD_CMPNY);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(adPreference.getPrfApDefaultPrTax(), AD_CMPNY);
            apPurchaseRequisition.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adPreference.getPrfApDefaultPrCurrency(), AD_CMPNY);
            apPurchaseRequisition.setGlFunctionalCurrency(glFunctionalCurrency);

            // psuedo code
            HashMap hm = new HashMap();
            Iterator i = prSelectedList.iterator();
            ApModPurchaseRequisitionLineDetails prlDetails = null;

            Double qty = 0d;
            while (i.hasNext()) {

                LocalApPurchaseRequisition apForConPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey((Integer) i.next());

                Collection apPurchaseRequisitionLines = apForConPurchaseRequisition.getApPurchaseRequisitionLines();
                apForConPurchaseRequisition.setPrCanvassPosted(EJBCommon.TRUE);
                apForConPurchaseRequisition.setPrCanvassApprovalStatus("N/A");
                apForConPurchaseRequisition.setPrGenerated(EJBCommon.TRUE);
                apForConPurchaseRequisition.setPrCanvassApprovedRejectedBy(AD_USR);
                apForConPurchaseRequisition.setPrCanvassPostedBy(AD_USR);
                apForConPurchaseRequisition.setPrCanvassDateApprovedRejected(new java.util.Date());
                apForConPurchaseRequisition.setPrCanvassDatePosted(new java.util.Date());

                for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;
                    if (hm.containsKey(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName() + apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName())) {

                        ApModPurchaseRequisitionLineDetails apExistingPrlDetails = (ApModPurchaseRequisitionLineDetails) hm.get(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName() + apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                        apExistingPrlDetails.setPrlQuantity(apExistingPrlDetails.getPrlQuantity() + apPurchaseRequisitionLine.getPrlQuantity());
                        hm.put(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName() + apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName(), apExistingPrlDetails);

                    } else {

                        ApModPurchaseRequisitionLineDetails apNewPrlDetails = new ApModPurchaseRequisitionLineDetails();
                        apNewPrlDetails.setPrlIlIiName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                        apNewPrlDetails.setPrlIlLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                        apNewPrlDetails.setPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());


                        hm.put(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName() + apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName(), apNewPrlDetails);

                    }
                }
            }

            // looping for hm
            // create prLines
            Set set = hm.entrySet();

            Iterator z = set.iterator();

            short ctr = 0;
            while (z.hasNext()) {
                Map.Entry me = (Map.Entry) z.next();
                ApModPurchaseRequisitionLineDetails prlDetailsC = (ApModPurchaseRequisitionLineDetails) me.getValue();

                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.create(++ctr, 0d, 0d, null, AD_CMPNY);
                apPurchaseRequisition.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(prlDetailsC.getPrlIlIiName(), prlDetailsC.getPrlIlLocName(), AD_CMPNY);
                invItemLocation.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);
                apPurchaseRequisitionLine.setPrlQuantity(prlDetailsC.getPrlQuantity());
                apPurchaseRequisitionLine.setPrlAmount(invItemLocation.getInvItem().getIiUnitCost());

                LocalInvUnitOfMeasure invUnitOfMeasure = invItemLocation.getInvItem().getInvUnitOfMeasure();
                invUnitOfMeasure.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);
            }

            return apPurchaseRequisition.getPrCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private boolean hasSupplier(LocalApPurchaseRequisitionLine apPurchaseRequisitionLine, String SPL_SPPLR_CD) {

        Debug.print("ApCanvassControllerBean hasSupplier");

        Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

        for (Object canvass : apCanvasses) {

            LocalApCanvass apCanvass = (LocalApCanvass) canvass;

            if (apCanvass.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CD)) {

                return true;
            }
        }

        return false;
    }

    private double getInvLastPurchasePriceBySplSupplierCode(LocalInvItemLocation invItemLocation, String SPL_SPPLR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean getInvLastPurchasePriceBySplSupplierCode");

        try {

            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSpplrCode(invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), EJBCommon.TRUE, EJBCommon.TRUE, invItemLocation.getInvItem().getApSupplier().getSplSupplierCode(), AD_BRNCH, AD_CMPNY);

            return this.convertCostByUom(invItemLocation.getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);

        } catch (FinderException ex) {

            return 0d;
        }
    }

    private double getInvLastPurchasePrice(LocalInvItemLocation invItemLocation, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean getInvLastPurchasePrice");

        try {

            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPosted(invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

            return this.convertCostByUom(invItemLocation.getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);

        } catch (FinderException ex) {

            return 0d;
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseRequisitionControllerBean convertCostByUom");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            if (isFromDefault) {

                return EJBCommon.roundIt(unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

            } else {

                return EJBCommon.roundIt(unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void setInvTbForItemForCurrentMonth(String itemName, String userName, Date date, double qtyConsumed, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseRequisitionControllerBean getInvTrnsctnlBdgtForCurrentMonth");

        ArrayList list = new ArrayList();


        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM");
            java.text.SimpleDateFormat sdfYear = new java.text.SimpleDateFormat("yyyy");
            String month = sdf.format(date);

            LocalAdUser adUser = adUserHome.findByUsrName(userName, AD_CMPNY);
            LocalAdLookUpValue adLookUpValue = adLookUpValueHome.findByLuNameAndLvName("AD DEPARTMENT", adUser.getUsrDept(), AD_CMPNY);

            LocalInvTransactionalBudget invTransactionalBudget = invTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(itemName, adLookUpValue.getLvCode(), Integer.parseInt(sdfYear.format(date)), AD_CMPNY);

            switch (month) {
                case "01":
                    invTransactionalBudget.setTbQuantityJan(invTransactionalBudget.getTbQuantityJan() - qtyConsumed);
                    break;
                case "02":
                    invTransactionalBudget.setTbQuantityFeb(invTransactionalBudget.getTbQuantityFeb() - qtyConsumed);
                    break;
                case "03":
                    invTransactionalBudget.setTbQuantityMrch(invTransactionalBudget.getTbQuantityMrch() - qtyConsumed);
                    break;
                case "04":
                    invTransactionalBudget.setTbQuantityAprl(invTransactionalBudget.getTbQuantityAprl() - qtyConsumed);
                    break;
                case "05":
                    invTransactionalBudget.setTbQuantityMay(invTransactionalBudget.getTbQuantityMay() - qtyConsumed);
                    break;
                case "06":
                    invTransactionalBudget.setTbQuantityJun(invTransactionalBudget.getTbQuantityJun() - qtyConsumed);
                    break;
                case "07":
                    invTransactionalBudget.setTbQuantityJul(invTransactionalBudget.getTbQuantityJul() - qtyConsumed);
                    break;
                case "08":
                    invTransactionalBudget.setTbQuantityAug(invTransactionalBudget.getTbQuantityAug() - qtyConsumed);
                    break;
                case "09":
                    invTransactionalBudget.setTbQuantitySep(invTransactionalBudget.getTbQuantitySep() - qtyConsumed);
                    break;
                case "10":
                    invTransactionalBudget.setTbQuantityOct(invTransactionalBudget.getTbQuantityOct() - qtyConsumed);
                    break;
                case "11":
                    invTransactionalBudget.setTbQuantityNov(invTransactionalBudget.getTbQuantityNov() - qtyConsumed);
                    break;
                default:
                    invTransactionalBudget.setTbQuantityDec(invTransactionalBudget.getTbQuantityDec() - qtyConsumed);
                    break;
            }

        } catch (FinderException ex) {
            Debug.print("no item found in transactional budget table");
            // throw new GlobalNoRecordFoundException();
        }
    }

    private LocalAdApprovalQueue createApprovalQueue(Integer branchCode, Integer companyCode, LocalAdApprovalQueueHome adApprovalQueueHome, LocalApPurchaseOrder apPurchaseOrder, LocalAdAmountLimit adAmountLimit, LocalAdApprovalUser adApprovalUser) throws CreateException {
        return adApprovalQueueHome.AqForApproval(EJBCommon.TRUE).AqDocument("AP PURCHASE ORDER").AqDocumentCode(apPurchaseOrder.getPoCode()).AqDocumentNumber(apPurchaseOrder.getPoDocumentNumber()).AqDate(apPurchaseOrder.getPoDate()).AqAndOr(adAmountLimit.getCalAndOr()).AqUserOr(adApprovalUser.getAuOr()).AqAdBranch(branchCode).AqAdCompany(companyCode).buildApprovalQueue();
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApFindPurchaseRequisitionControllerBean ejbCreate");
    }
}