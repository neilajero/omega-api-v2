/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ArFindReceiptControllerBean
 * @created March 5, 2004, 9:43 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ar.LocalArReceiptBatchHome;
import com.ejb.dao.ar.LocalArReceiptHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import com.ejb.entities.ar.LocalArInvoiceLineItem;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.ar.LocalArReceiptBatch;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModReceiptDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "ArFindReceiptControllerEJB")
public class ArFindReceiptControllerBean extends EJBContextClass implements ArFindReceiptController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArReceiptBatchHome arReceiptBatchHome;
    @EJB
    private LocalArReceiptHome arReceiptHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList getArOpenRbAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getArOpenRbAll");
        ArrayList list = new ArrayList();
        try {

            Collection arReceiptBatches = arReceiptBatchHome.findOpenRbAll(AD_BRNCH, AD_CMPNY);

            for (Object receiptBatch : arReceiptBatches) {

                LocalArReceiptBatch arReceiptBatch = (LocalArReceiptBatch) receiptBatch;

                list.add(arReceiptBatch.getRbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableArReceiptBatch(Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getAdPrfEnableArReceiptBatch");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableArReceiptBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getArPrfDefaultReceiptType(Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getArPrfDefaultReceiptType");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArFindReceiptDefaultType();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableInvShift(Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getAdPrfEnableInvShift");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvEnableShift();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArRctByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindReceiptControllerBean getArRctByCriteria");
        try {

            ArrayList rctList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj;

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            if (criteria.containsKey("computeRatio")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("batchName")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.arReceiptBatch.rbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("shift")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.rctLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");
                ctr++;
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptType");
                ctr++;
            }

            if (criteria.containsKey("receiptVoid")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptVoid");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
                ctr++;
            }

            if (criteria.containsKey("receiptReferenceNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptReferenceNumberFrom");
                Debug.print("receiptReferenceNumberFrom=" + criteria.get("receiptReferenceNumberFrom"));
                ctr++;
            }

            if (criteria.containsKey("receiptReferenceNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptReferenceNumberTo");
                Debug.print("receiptReferenceNumberTo=" + criteria.get("receiptReferenceNumberTo"));
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

                    jbossQl.append("rct.rctApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("rct.rctReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("rct.rctApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctAdBranch=").append(AD_BRNCH).append(" AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "BANK ACCOUNT":

                    orderBy = "rct.adBankAccount.baName";

                    break;
                case "CUSTOMER CODE":

                    orderBy = "rct.arCustomer.cstCustomerCode";

                    break;
                case "RECEIPT NUMBER":

                    orderBy = "rct.rctNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", rct.rctDate");

            } else {

                jbossQl.append("ORDER BY rct.rctDate");
            }

            Collection arReceipts = null;
            String computeRatio = "";

            if (criteria.containsKey("computeRatio")) {

                computeRatio = (String) criteria.get("computeRatio");
            }

            try {
                Debug.print("getArRctByCriteria jbossQl.toString()=" + jbossQl);
                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (arReceipts.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object receipt : arReceipts) {

                LocalArReceipt arReceipt = (LocalArReceipt) receipt;

                ArModReceiptDetails mdetails = new ArModReceiptDetails();
                Collection arAppliedInvoices = arReceipt.getArAppliedInvoices();
                Iterator x = arAppliedInvoices.iterator();
                double totalAmount = 0d;
                double creditedBalance = 0d;
                double totalMiscReceipt = arReceipt.getRctAmountCash() + arReceipt.getRctAmountCard1() + arReceipt.getRctAmountCard2() + arReceipt.getRctAmountCard3();

                while (x.hasNext()) {
                    LocalArAppliedInvoice arAppliedInvoice = (LocalArAppliedInvoice) x.next();
                    totalAmount += (arAppliedInvoice.getAiApplyAmount() + arAppliedInvoice.getAiPenaltyApplyAmount() + arAppliedInvoice.getAiDiscountAmount() + arAppliedInvoice.getAiCreditBalancePaid() + arAppliedInvoice.getAiRebate());
                    creditedBalance += arAppliedInvoice.getAiCreditBalancePaid();
                }

                mdetails.setRctCode(arReceipt.getRctCode());
                mdetails.setRctType(arReceipt.getRctType());
                mdetails.setRctDate(arReceipt.getRctDate());
                mdetails.setRctNumber(arReceipt.getRctNumber());
                mdetails.setRctReferenceNumber(arReceipt.getRctReferenceNumber());
                mdetails.setRctAmount(arReceipt.getRctAmount());
                mdetails.setRctCreditedBalance(creditedBalance);
                mdetails.setRctCstName(arReceipt.getRctCustomerName() == null || arReceipt.getRctCustomerName().equals("") ? arReceipt.getArCustomer().getCstName() : arReceipt.getRctCustomerName());
                mdetails.setRctBaName(arReceipt.getAdBankAccount().getBaName());

                // call private method for computing ratio
                mdetails.setRctRatio(computeRatio.equals("YES") ? this.getCogsRatio(arReceipt, AD_BRNCH, AD_CMPNY) : 0);

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arReceipt.getRctAdBranch());
                mdetails.setRctBranchCode(adBranch.getBrBranchCode());

                String status = arReceipt.getRctApprovalStatus();
                if (arReceipt.getRctVoid() == EJBCommon.TRUE) {
                    status = "VOID";
                } else if (arReceipt.getRctPosted() == EJBCommon.FALSE) {
                    status = "DRAFT";
                } else if (status.equals("N/A")) {
                    status = "POSTED";
                }
                mdetails.setRctStatus(status);


                rctList.add(mdetails);
            }

            return rctList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getArRctSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindReceiptControllerBean getArRctSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(rct) FROM ArReceipt rct ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj;

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

                    criteriaSize--;
                }
            }

            if (criteria.containsKey("computeRatio")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("batchName")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.arReceiptBatch.rbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");

                ctr++;
            }

            if (criteria.containsKey("shift")) {

                firstArgument = false;

                jbossQl.append("WHERE rct.rctLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");

                ctr++;
            }

            if (criteria.containsKey("bankAccount")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.adBankAccount.baName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("bankAccount");
                ctr++;
            }

            if (criteria.containsKey("receiptType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctType=?").append(ctr + 1).append(" ");

                obj[ctr] = criteria.get("receiptType");

                ctr++;
            }

            if (criteria.containsKey("receiptVoid")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctVoid=?").append(ctr + 1).append(" ");

                obj[ctr] = criteria.get("receiptVoid");

                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");

                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptNumberTo");
                ctr++;
            }

            if (criteria.containsKey("receiptReferenceNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptReferenceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("receiptReferenceNumberTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rct.rctReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("receiptReferenceNumberTo");
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

                    jbossQl.append("rct.rctApprovalStatus IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("rct.rctReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("rct.rctApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("rct.rctPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rct.rctAdBranch=").append(AD_BRNCH).append(" AND rct.rctAdCompany=").append(AD_CMPNY).append(" ");

            Collection arReceipts = null;

            try {

                Debug.print("getArRctSizeByCriteria jbossQl.toString()=" + jbossQl);

                arReceipts = arReceiptHome.getRctByCriteria(jbossQl.toString(), obj, 0, 0);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (arReceipts.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            return arReceipts.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // Private Methods
    private double getCogsRatio(LocalArReceipt arReceipt, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArFindReceiptControllerBean getCogsRatio");
        try {

            double TOTAL_COGS = 0d;

            if (arReceipt.getRctType().equals("MISC") && arReceipt.getArInvoiceLineItems().size() > 0) {

                for (Object o : arReceipt.getArInvoiceLineItems()) {

                    LocalArInvoiceLineItem arInvoiceLineItem = (LocalArInvoiceLineItem) o;
                    LocalInvItemLocation invItemLocation = arInvoiceLineItem.getInvItemLocation();
                    // add cost of sales distribution and inventory

                    double COST = 0d;

                    try {

                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(arReceipt.getRctDate(), invItemLocation.getIlCode(), AD_BRNCH, AD_CMPNY);

                        switch (invCosting.getInvItemLocation().getInvItem().getIiCostMethod()) {
                            case "Average":
                                COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());
                                break;
                            case "FIFO":
                                COST = Math.abs(this.getInvFifoCost(invCosting.getCstDate(), invCosting.getInvItemLocation().getIlCode(), arInvoiceLineItem.getIliQuantity(), arInvoiceLineItem.getIliUnitPrice(), false, AD_BRNCH, AD_CMPNY));
                                break;
                            case "Standard":
                                COST = invCosting.getInvItemLocation().getInvItem().getIiUnitCost();
                                break;
                        }

                    } catch (FinderException ex) {

                        COST = arInvoiceLineItem.getInvItemLocation().getInvItem().getIiUnitCost();
                    }

                    double QTY_SLD = this.convertByUomFromAndItemAndQuantity(arInvoiceLineItem.getInvUnitOfMeasure(), arInvoiceLineItem.getInvItemLocation().getInvItem(), arInvoiceLineItem.getIliQuantity(), AD_CMPNY);

                    TOTAL_COGS += COST * QTY_SLD;
                }

                return EJBCommon.roundIt((arReceipt.getRctAmount() / (TOTAL_COGS + arReceipt.getRctAmount())) * 100, (short) 2);

            } else {
                return 0d;
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer AD_BRNCH, Integer AD_CMPNY) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, AD_CMPNY);

            if (invFifoCostings.size() > 0) {

                Iterator x = invFifoCostings.iterator();

                if (isAdjustFifo) {

                    // executed during POST transaction

                    double totalCost = 0d;
                    double cost;

                    if (CST_QTY < 0) {

                        // for negative quantities
                        double neededQty = -(CST_QTY);

                        while (x.hasNext() && neededQty != 0) {

                            LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                            if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                                cost = invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived();
                            } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                                cost = invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold();
                            } else {
                                cost = invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity();
                            }

                            if (neededQty <= invFifoCosting.getCstRemainingLifoQuantity()) {

                                invFifoCosting.setCstRemainingLifoQuantity(invFifoCosting.getCstRemainingLifoQuantity() - neededQty);
                                totalCost += (neededQty * cost);
                                neededQty = 0d;
                            } else {

                                neededQty -= invFifoCosting.getCstRemainingLifoQuantity();
                                totalCost += (invFifoCosting.getCstRemainingLifoQuantity() * cost);
                                invFifoCosting.setCstRemainingLifoQuantity(0);
                            }
                        }

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the default
                        // cost
                        if (neededQty != 0) {

                            LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                            totalCost += (neededQty * invItemLocation.getInvItem().getIiUnitCost());
                        }

                        cost = totalCost / -CST_QTY;
                    } else {

                        // for positive quantities
                        cost = CST_COST;
                    }
                    return cost;
                } else {

                    // executed during ENTRY transaction

                    LocalInvCosting invFifoCosting = (LocalInvCosting) x.next();

                    if (invFifoCosting.getApPurchaseOrderLine() != null || invFifoCosting.getApVoucherLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getGlFcPrecisionUnit(AD_CMPNY));
                    }
                }
            } else {

                // most applicable in 1st entries of data
                LocalInvItemLocation invItemLocation = invItemLocationHome.findByPrimaryKey(IL_CODE);
                return invItemLocation.getInvItem().getIiUnitCost();
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("ArMiscReceiptEntryControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArFindReceiptControllerBean ejbCreate");
    }

}