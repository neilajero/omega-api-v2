/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ApFindPurchaseOrderControllerBean
 * @created April 20, 2005, 12:37 PM
 * @author Jolly T. Martin
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.dao.inv.LocalInvTagHome;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApFindPurchaseOrderControllerEJB")
public class ApFindPurchaseOrderControllerBean extends EJBContextClass implements ApFindPurchaseOrderController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;

    @EJB
    private LocalInvTagHome invTagHome;

    public ArrayList getGlFcAll(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseOrderControllerBean getGlFcAll");

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

    public ArrayList getApPoByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, boolean isPoLookup, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseOrderControllerBean getApPoByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(po) FROM ApPurchaseOrder po ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            String serialNumber = "";
            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("serialNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {

                String approvalStatus = (String) criteria.get("approvalStatus");

                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {

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

                jbossQl.append("po.poReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("serialNumber")) {

                serialNumber = (String) criteria.get("serialNumber");
            }

            if (criteria.containsKey("batchName")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.apVoucherBatch.vbName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("batchName");
                ctr++;
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
                ctr++;
            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.poType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("type");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poReceiving=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("receiving");
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("purchaseOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("po.poReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("po.poApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("po.poPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("printed")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.poPrinted=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("printed");
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.poDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("po.poDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("po.poDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("po.poDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("SUPPLIER CODE")) {

                orderBy = "po.apSupplier.splSupplierCode";

            } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                orderBy = "po.poDocumentNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", po.poDate");

            } else {

                jbossQl.append("ORDER BY po.poDate");
            }

            if (isPoLookup) {

                LIMIT = 0;
                OFFSET = 0;
            }

            Collection apPurchaseOrders = apPurchaseOrderHome.getPoByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (apPurchaseOrders.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object purchaseOrder : apPurchaseOrders) {

                LocalApPurchaseOrder apPurchaseOrder = (LocalApPurchaseOrder) purchaseOrder;

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseOrder.getPoAdBranch());

                if (isPoLookup) {
                    boolean isPoBreak = false;
                    Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();
                    for (Object purchaseOrderLine : apPurchaseOrderLines) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;
                        Collection apReceivedPls = apPurchaseOrderLineHome.findByPlPlCode(apPurchaseOrderLine.getPlCode(), AD_CMPNY);

                        double totalReceived = 0;

                        for (Object receivedPl : apReceivedPls) {

                            LocalApPurchaseOrderLine apReceivedPl = (LocalApPurchaseOrderLine) receivedPl;
                            totalReceived += apReceivedPl.getPlQuantity();
                        }

                        if (apReceivedPls.size() == 0 || totalReceived != apPurchaseOrderLine.getPlQuantity()) {
                            ApModPurchaseOrderDetails mdetails = new ApModPurchaseOrderDetails();
                            mdetails.setPoCode(apPurchaseOrder.getPoCode());
                            mdetails.setPoSplSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
                            mdetails.setPoDate(apPurchaseOrder.getPoDate());
                            mdetails.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                            mdetails.setPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
                            mdetails.setPoReceiving(apPurchaseOrder.getPoReceiving());

                            mdetails.setPoTotalAmount(apPurchaseOrder.getPoTotalAmount());
                            mdetails.setPoBranchCode(adBranch.getBrBranchCode());
                            mdetails.setPoDepartment(apPurchaseOrder.getPoDepartment());
                            String status = apPurchaseOrder.getPoApprovalStatus();
                            if (apPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {
                                status = "VOID";
                            } else if (apPurchaseOrder.getPoPosted() == EJBCommon.FALSE) {
                                status = "DRAFT";
                            } else if (status.equals("N/A")) {
                                status = "POSTED";
                            }
                            mdetails.setPoStatus(status);

                            if (!serialNumber.trim().equals("")) {

                                if (this.searchSerialNumber(apPurchaseOrder, serialNumber)) {
                                    list.add(mdetails);
                                }

                            } else {
                                list.add(mdetails);
                            }

                            // list.add(mdetails);
                            isPoBreak = true;
                        }
                        if (isPoBreak) break;
                    }

                    if (isPoBreak) continue;

                } else {

                    ApModPurchaseOrderDetails mdetails = new ApModPurchaseOrderDetails();
                    mdetails.setPoCode(apPurchaseOrder.getPoCode());
                    mdetails.setPoSplSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
                    mdetails.setPoDate(apPurchaseOrder.getPoDate());
                    mdetails.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                    mdetails.setPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
                    mdetails.setPoReceiving(apPurchaseOrder.getPoReceiving());
                    mdetails.setPoTotalAmount(apPurchaseOrder.getPoTotalAmount());
                    mdetails.setPoBranchCode(adBranch.getBrBranchCode());
                    mdetails.setPoDepartment(apPurchaseOrder.getPoDepartment());
                    String status = apPurchaseOrder.getPoApprovalStatus();
                    if (apPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {
                        status = "VOID";
                    } else if (apPurchaseOrder.getPoPosted() == EJBCommon.FALSE) {
                        status = "DRAFT";
                    } else if (status.equals("N/A")) {
                        status = "POSTED";
                    }
                    mdetails.setPoStatus(status);

                    if (!serialNumber.trim().equals("")) {

                        if (this.searchSerialNumber(apPurchaseOrder, serialNumber)) {
                            list.add(mdetails);
                        }

                    } else {
                        list.add(mdetails);
                    }

                    // list.add(mdetails);
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApPOBatch(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseOrderControllerBean getAdPrfEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApPOBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenVbAll(String TYPE, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseOrderControllerBean getApOpenVbAll");


        ArrayList list = new ArrayList();

        try {

            Collection apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType(TYPE, AD_BRNCH, AD_CMPNY);

            for (Object voucherBatch : apVoucherBatches) {

                LocalApVoucherBatch apVoucherBatch = (LocalApVoucherBatch) voucherBatch;

                list.add(apVoucherBatch.getVbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getApPoSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindPurchaseOrderControllerBean getApPoSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(po) FROM ApPurchaseOrder po ");

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

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.poReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierCode");
                ctr++;
            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.poType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("type");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poReceiving=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("receiving");
            ctr++;

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poVoid=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("purchaseOrderVoid");
            ctr++;

            if (criteria.containsKey("currency")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("po.glFunctionalCurrency.fcName=?").append(ctr + 1).append(" ");
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

                    jbossQl.append("po.poApprovalStatus IS NULL AND po.poReasonForRejection IS NULL ");

                } else if (approvalStatus.equals("REJECTED")) {

                    jbossQl.append("po.poReasonForRejection IS NOT NULL ");

                } else {

                    jbossQl.append("po.poApprovalStatus=?").append(ctr + 1).append(" ");
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

                jbossQl.append("po.poPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

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

                jbossQl.append("po.poDocumentNumber>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("po.poDocumentNumber<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("po.poDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("po.poDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poAdBranch=").append(AD_BRNCH).append(" ");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("po.poAdCompany=").append(AD_CMPNY).append(" ");

            Collection apPurchaseOrders = apPurchaseOrderHome.getPoByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apPurchaseOrders.size() == 0) throw new GlobalNoRecordFoundException();

            return apPurchaseOrders.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApFindPurchaseOrderControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private boolean searchSerialNumber(LocalApPurchaseOrder apPurchaseOrder, String serialNumber) throws FinderException {

        Debug.print("ArFindInvoiceControllerBean searchSerialNumber");

        Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

        for (Object purchaseOrderLine : apPurchaseOrderLines) {

            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

            if (apPurchaseOrderLine.getInvTags().size() > 0) {

                Collection invTags = invTagHome.findTgSerialNumberByPlCode(serialNumber, apPurchaseOrderLine.getPlCode());

                if (invTags.size() > 0) {
                    return true;
                }
            }
        }

        return false;
    }
    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApFindPurchaseOrderControllerBean ejbCreate");
    }
}