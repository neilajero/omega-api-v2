/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApPurchaseOrderEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import java.util.*;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.entities.ad.LocalAdApproval;
import com.ejb.dao.ad.LocalAdApprovalHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.dao.ad.LocalAdApprovalUserHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdDeleteAuditTrailHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.dao.ad.LocalAdPaymentTermHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.dao.ap.LocalApVoucherBatch;
import com.ejb.dao.ap.LocalApVoucherBatchHome;
import com.ejb.exception.global.GlobalConversionDateNotExistException;
import com.ejb.exception.global.GlobalDocumentNumberNotUniqueException;
import com.ejb.exception.global.GlobalInvItemLocationNotFoundException;
import com.ejb.exception.global.GlobalNoApprovalApproverFoundException;
import com.ejb.exception.global.GlobalNoApprovalRequesterFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalPaymentTermInvalidException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.exception.global.GlobalSupplierItemInvalidException;
import com.ejb.exception.global.GlobalTransactionAlreadyApprovedException;
import com.ejb.exception.global.GlobalTransactionAlreadyPendingException;
import com.ejb.exception.global.GlobalTransactionAlreadyPostedException;
import com.ejb.exception.global.GlobalTransactionAlreadyVoidException;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvTag;
import com.ejb.dao.inv.LocalInvTagHome;
import com.ejb.entities.inv.LocalInvTransactionalBudget;
import com.ejb.dao.inv.LocalInvTransactionalBudgetHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.txn.ad.AdApprovalDocumentEmailNotificationController;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModPurchaseOrderLineDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;
import com.util.SendEmailDetails;
import com.util.ap.ApPurchaseOrderDetails;

@Stateless(name = "ApPurchaseOrderEntryControllerEJB")
public class ApPurchaseOrderEntryControllerBean extends EJBContextClass implements ApPurchaseOrderEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ApApprovalController apApprovalController;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private AdApprovalDocumentEmailNotificationController adApprovalDocumentEmailNotification;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
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
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApVoucherBatchHome apVoucherBatchHome;
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
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalInvTransactionalBudgetHome invTransactionalBudgetHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;


    public ArrayList getApSplAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(AD_BRNCH, AD_CMPNY);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdPytAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdPytAll");

        ArrayList list = new ArrayList();

        try {

            Collection adPaymentTerms = adPaymentTermHome.findEnabledPytAll(AD_CMPNY);

            for (Object paymentTerm : adPaymentTerms) {

                LocalAdPaymentTerm adPaymentTerm = (LocalAdPaymentTerm) paymentTerm;

                list.add(adPaymentTerm.getPytName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrAll(Integer AD_CMPNY) {

        Debug.print("ApReceivingItemEntryControllerBean getAdUsrAll");

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

    public ArrayList getAdLvPurchaseRequisitionMisc1(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc1");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC1", AD_CMPNY);

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

    public ArrayList getAdLvPurchaseRequisitionMisc2(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc2");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC2", AD_CMPNY);

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

    public ArrayList getAdLvPurchaseRequisitionMisc3(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc3");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC3", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }
            Debug.print("PR T9=" + list.size());

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc4(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc4");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC4", AD_CMPNY);

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

    public ArrayList getAdLvPurchaseRequisitionMisc5(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc5");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC5", AD_CMPNY);

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

    public ArrayList getAdLvPurchaseRequisitionMisc6(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdLvPurchaseRequisitionMisc6");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC6", AD_CMPNY);

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

    private void setInvTbForItemForCurrentMonth(String itemName, String userName, Date date, double qtyConsumed, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvTrnsctnlBdgtForCurrentMonth");

        ArrayList list = new ArrayList();


        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM");
            java.text.SimpleDateFormat sdfYear = new java.text.SimpleDateFormat("yyyy");
            String month = sdf.format(date);

            LocalAdUser adUser = adUserHome.findByUsrName(userName, AD_CMPNY);
            LocalAdLookUpValue adLookUpValue = adLookUpValueHome.findByLuNameAndLvName("DEPARTMENT", adUser.getUsrDept(), AD_CMPNY);

            LocalInvTransactionalBudget invTransactionalBudget = invTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(itemName, adLookUpValue.getLvCode(), Integer.parseInt(sdfYear.format(date)), AD_CMPNY);
            // LocalInvItem invItem = LocalInvItemHome

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

    public ArrayList getApTcAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getApTcAll");

        ArrayList list = new ArrayList();


        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(AD_CMPNY);

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;

                list.add(apTaxCode.getTcName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getArTcByTcName");

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);

            ApTaxCodeDetails details = new ApTaxCodeDetails();
            details.setTcType(apTaxCode.getTcType());
            details.setTcRate(apTaxCode.getTcRate());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();

        try {

            invLocations = invLocationHome.findLocAll(AD_CMPNY);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpCostPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvGpCostPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvCostPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvTraceMisc(String II_NAME, Integer AD_CMPNY) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");


        Collection invLocations = null;
        ArrayList list = new ArrayList();
        boolean isTraceMisc = false;

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseOrderDetails getApPoByPoCode(Integer PO_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApPurchaseOrderEntryControllerBean getApPoByPoCode");

        try {

            LocalApPurchaseOrder apPurchaseOrder = null;

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            String specs = null;
            String propertyCode = null;
            String expiryDate = null;
            String serialNumber = null;
            ArrayList list = new ArrayList();

            // get purchase order lines if any

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();

                ArrayList tagList = new ArrayList();
                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                if (plDetails.getTraceMisc() == 1) {

                    tagList = this.getInvTagList(apPurchaseOrderLine);

                    plDetails.setPlTagList(tagList);
                }
                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlLine(apPurchaseOrderLine.getPlLine());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlQcNumber(apPurchaseOrderLine.getPlQcNumber());
                plDetails.setPlQcExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate());
                plDetails.setPlRemarks(apPurchaseOrderLine.getPlRemarks());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount());

                list.add(plDetails);
            }

            ApModPurchaseOrderDetails mPoDetails = new ApModPurchaseOrderDetails();

            mPoDetails.setPoCode(apPurchaseOrder.getPoCode());

            if (apPurchaseOrder.getApVoucherBatch() != null) {
                mPoDetails.setPoBatchName(apPurchaseOrder.getApVoucherBatch().getVbName());
            }

            mPoDetails.setPoDate(apPurchaseOrder.getPoDate());
            mPoDetails.setPoDeliveryPeriod(apPurchaseOrder.getPoDeliveryPeriod());
            mPoDetails.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
            mPoDetails.setPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
            mPoDetails.setPoDescription(apPurchaseOrder.getPoDescription());
            mPoDetails.setPoVoid(apPurchaseOrder.getPoVoid());
            mPoDetails.setPoShipmentNumber(apPurchaseOrder.getPoShipmentNumber());
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
            mPoDetails.setPoTotalAmount(apPurchaseOrder.getPoTotalAmount());
            mPoDetails.setPoPlList(list);


            //Check if PO came from PR By PO_RFRNC_NMBR. IF yes then PR_CODE is not null
            Integer pr_code = this.getPrCodeByPrNumberAndBrCode(apPurchaseOrder.getPoReferenceNumber(), apPurchaseOrder.getPoAdBranch(), AD_CMPNY);
            mPoDetails.setPoPrCode(pr_code);

            return mPoDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApPoEntry(ApPurchaseOrderDetails details, String BTCH_NM, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE, ArrayList plList, boolean isDraft, boolean validateShipmentNumber, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalSupplierItemInvalidException, GlobalRecordInvalidException {

        Debug.print("ApPurchaseOrderEntryControllerBean saveApPoEntry");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

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

            try {
                Debug.print("PO SHIPMENT" + details.getPoShipmentNumber() + details.getPoShipmentNumber().length());
            } catch (Exception ex) {

            }

            // validate if shipment number exists
            if (validateShipmentNumber && details.getPoShipmentNumber() != null && details.getPoShipmentNumber().length() > 0) {

                Collection apExistingPurchaseOrderLines = apPurchaseOrderLineHome.findByPoReceivingAndPoShipmentAndBrCode(EJBCommon.FALSE, details.getPoShipmentNumber(), AD_CMPNY);
                if (apExistingPurchaseOrderLines.size() > 0) {

                    for (Object existingPurchaseOrderLine : apExistingPurchaseOrderLines) {
                        LocalApPurchaseOrderLine apExistingPurchaseOrderLine = (LocalApPurchaseOrderLine) existingPurchaseOrderLine;
                        if (!apExistingPurchaseOrderLine.getApPurchaseOrder().getPoCode().equals(details.getPoCode()))
                            throw new GlobalRecordInvalidException();
                    }
                }

            } else if (!validateShipmentNumber && details.getPoShipmentNumber() != null && details.getPoShipmentNumber().length() > 0) {
                Collection apExistingPurchaseOrderLines = apPurchaseOrderLineHome.findByPoReceivingAndPoShipmentAndBrCode(EJBCommon.FALSE, details.getPoShipmentNumber(), AD_CMPNY);
                if (apExistingPurchaseOrderLines.size() == 0) throw new GlobalRecordInvalidException();
            }
            // validate if conversion date exists

            try {

                if (details.getPoConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);
                    LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPoConversionDate(), AD_CMPNY);

                    details.setPoConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
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
                apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.FALSE, null, details.getPoDate(), details.getPoDeliveryPeriod(), details.getPoDocumentNumber(), details.getPoReferenceNumber(), null, details.getPoDescription(), details.getPoBillTo(), details.getPoShipTo(), details.getPoConversionDate(), details.getPoConversionRate(), 0d, EJBCommon.FALSE, EJBCommon.FALSE, details.getPoShipmentNumber(), null, EJBCommon.FALSE, null, details.getPoCreatedBy(), details.getPoDateCreated(), details.getPoLastModifiedBy(), details.getPoDateLastModified(), null, null, null, null, EJBCommon.FALSE, 0d, 0d, 0d, 0d, 0d, AD_BRNCH, AD_CMPNY);

                //ADD DEPARTMENT
                String department = null;
                try {
                    LocalAdUser adUser = adUserHome.findByUsrName(details.getPoCreatedBy(), AD_CMPNY);
                    department = adUser.getUsrDept();
                } catch (FinderException ex) {
                    Debug.print("NO user found, no department assigned");
                }

                apPurchaseOrder.setPoDepartment(department);

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

                        if (!apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPlIiName()) || !apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription().equals(mdetails.getPlIiDescription()) || !apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPlLocName()) || !apPurchaseOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPlUomName()) || apPurchaseOrderLine.getPlQuantity() != mdetails.getPlQuantity() || apPurchaseOrderLine.getPlUnitCost() != mdetails.getPlUnitCost() || apPurchaseOrderLine.getPlQcNumber() != mdetails.getPlQcNumber() || apPurchaseOrderLine.getPlRemarks() != mdetails.getPlRemarks() || apPurchaseOrderLine.getPlQcExpiryDate() != mdetails.getPlQcExpiryDate() || apPurchaseOrderLine.getPlTotalDiscount() != mdetails.getPlTotalDiscount() || apPurchaseOrderLine.getPlRemarks() != mdetails.getPlRemarks()) {

                            isRecalculate = true;
                            break;
                        }
                        isRecalculate = false;
                    }

                } else {

                    isRecalculate = false;
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
                apPurchaseOrder.setPoShipmentNumber(details.getPoShipmentNumber());
                apPurchaseOrder.setPoConversionDate(details.getPoConversionDate());
                apPurchaseOrder.setPoConversionRate(details.getPoConversionRate());
                apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
                apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());
            }

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);
            apPurchaseOrder.setApSupplier(apSupplier);

            try {

                LocalApVoucherBatch apVoucherBatch = apVoucherBatchHome.findByVbName(BTCH_NM, AD_BRNCH, AD_CMPNY);
                apPurchaseOrder.setApVoucherBatch(apVoucherBatch);

            } catch (FinderException ex) {

            }

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

                    // remove all inv tag inside PO line
                    Collection invTags = apPurchaseOrderLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }

                    i.remove();

                    // apPurchaseOrderLine.entityRemove();
                    em.remove(apPurchaseOrderLine);
                }

                // add new purchase order line item
                double totalAmount = 0;
                i = plList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();

                    LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(mPlDetails.getPlLine(), mPlDetails.getPlQuantity(), mPlDetails.getPlUnitCost(), mPlDetails.getPlAmount(), mPlDetails.getPlQcNumber(), mPlDetails.getPlQcExpiryDate(), mPlDetails.getPlRemarks(), mPlDetails.getPlConversionFactor(), 0d, null, mPlDetails.getPlDiscount1(), mPlDetails.getPlDiscount2(), mPlDetails.getPlDiscount3(), mPlDetails.getPlDiscount4(), mPlDetails.getPlTotalDiscount(), AD_CMPNY);

                    apPurchaseOrderLine.setApPurchaseOrder(apPurchaseOrder);

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPlDetails.getPlLocName(), mPlDetails.getPlIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    ABS_TOTAL_AMOUNT += apPurchaseOrderLine.getPlAmount();

                    apPurchaseOrderLine.setInvItemLocation(invItemLocation);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mPlDetails.getPlUomName(), AD_CMPNY);

                    apPurchaseOrderLine.setInvUnitOfMeasure(invUnitOfMeasure);

                    if (apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {
                        // TODO: add new inv Tag

                        this.createInvTagList(apPurchaseOrderLine, mPlDetails.getPlTagList(), AD_CMPNY);
                    }
                }

            } else {

                Iterator y = plList.iterator();

                while (y.hasNext()) {
                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) y.next();

                    LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(mPlDetails.getPlCode());

                    // remove all inv tag inside PO line
                    Collection invTags = apPurchaseOrderLine.getInvTags();

                    Iterator x = invTags.iterator();

                    while (x.hasNext()) {

                        LocalInvTag invTag = (LocalInvTag) x.next();

                        x.remove();

                        // invTag.entityRemove();
                        em.remove(invTag);
                    }
                    if (mPlDetails.getTraceMisc() == 1) {

                        this.createInvTagList(apPurchaseOrderLine, mPlDetails.getPlTagList(), AD_CMPNY);
                    }
                }

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                for (Object purchaseOrderLine : apPurchaseOrderLines) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                    ABS_TOTAL_AMOUNT += apPurchaseOrderLine.getPlAmount();
                }
            }

            apPurchaseOrder.setPoTotalAmount(ABS_TOTAL_AMOUNT);

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

                    LocalAdUser adUser = adUserHome.findByUsrName(details.getPoLastModifiedBy(), AD_CMPNY);

                    PO_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getPoLastModifiedBy(), adUser.getUsrDescription(), "AP PURCHASE ORDER", apPurchaseOrder.getPoCode(), apPurchaseOrder.getPoDocumentNumber(), apPurchaseOrder.getPoDate(), apPurchaseOrder.getPoTotalAmount(), AD_BRNCH, AD_CMPNY);
                }

                apPurchaseOrder.setPoApprovalStatus(PO_APPRVL_STATUS);

                // set post purchase order

                if (PO_APPRVL_STATUS.equals("N/A")) {

                    apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                    apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
                    apPurchaseOrder.setPoPostedBy(apPurchaseOrder.getPoLastModifiedBy());
                    apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

                    if (!isDraft && !apPurchaseOrder.getPoReferenceNumber().equals("")) {

                        LocalApPurchaseRequisition apPurchaseRequisition = null;
                        try {
                            apPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(apPurchaseOrder.getPoReferenceNumber(), AD_BRNCH, AD_CMPNY);

                            for (Object o : plList) {

                                ApModPurchaseOrderLineDetails mdetails = (ApModPurchaseOrderLineDetails) o;

                                this.setInvTbForItemForCurrentMonth(mdetails.getPlIiName(), apPurchaseRequisition.getPrCreatedBy(), apPurchaseRequisition.getPrDate(), mdetails.getPlQuantity(), AD_CMPNY);
                            }
                        } catch (FinderException ex) {

                        }
                    }
                }
            }

            return apPurchaseOrder.getPoCode();

        } catch (GlobalRecordAlreadyDeletedException | GlobalRecordInvalidException |
                 GlobalSupplierItemInvalidException | GlobalNoApprovalApproverFoundException |
                 GlobalNoApprovalRequesterFoundException | GlobalInvItemLocationNotFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalTransactionAlreadyApprovedException |
                 GlobalPaymentTermInvalidException | GlobalConversionDateNotExistException |
                 GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteApPoEntry(Integer PO_CODE, String AD_USR, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApPurchaseOrderEntryControllerBean deleteApPoEntry");

        try {

            LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            if (apPurchaseOrder.getPoApprovalStatus() != null && apPurchaseOrder.getPoApprovalStatus().equals("PENDING")) {

                Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP PURCHASE ORDER", apPurchaseOrder.getPoCode(), AD_CMPNY);

                for (Object approvalQueue : adApprovalQueues) {

                    LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                    em.remove(adApprovalQueue);
                }
            }

            adDeleteAuditTrailHome.create("AP PURCHASE ORDER", apPurchaseOrder.getPoDate(), apPurchaseOrder.getPoDocumentNumber(), apPurchaseOrder.getPoReferenceNumber(), 0d, AD_USR, new Date(), AD_CMPNY);

            em.remove(apPurchaseOrder);

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApPurchaseOrderEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
            mdetails.setSplScWtcName(apSupplier.getApSupplierClass().getApWithholdingTaxCode() != null ? apSupplier.getApSupplierClass().getApWithholdingTaxCode().getWtcName() : null);
            mdetails.setSplName(apSupplier.getSplName());

            if (apSupplier.getApSupplierClass().getApTaxCode() != null) {

                mdetails.setSplScTcName(apSupplier.getApSupplierClass().getApTaxCode().getTcName());
                mdetails.setSplScTcType(apSupplier.getApSupplierClass().getApTaxCode().getTcType());
                mdetails.setSplScTcRate(apSupplier.getApSupplierClass().getApTaxCode().getTcRate());
            }

            return mdetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvUomByIiName");

        ArrayList list = new ArrayList();


        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            // TODO
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), AD_CMPNY)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getInvIiUnitCostByIiNameAndUomName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getInvGpCostPrecisionUnit(AD_CMPNY));

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByPoCode(Integer PO_CODE, SendEmailDetails sendEmailDetails, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getAdApprovalNotifiedUsersByVouCode");

        ArrayList list = new ArrayList();


        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP PURCHASE ORDER", PO_CODE, AD_CMPNY);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                if (adPreference.getPrfAdEnableEmailNotification() == EJBCommon.TRUE) {

                    if (adApprovalQueue.getAqForApproval() == EJBCommon.FALSE) {
                        continue;
                    } else if (adApprovalQueue.getAqApproved() == EJBCommon.TRUE) {
                        continue;
                    }

                    String status = "";

                    status = adApprovalDocumentEmailNotification.sendEmailToApprover(adApprovalQueue, sendEmailDetails);
                    list.add(adApprovalQueue.getAqLevel() + " APPROVER - " + adApprovalQueue.getAdUser().getUsrDescription() + "- " + status);

                } else {

                    list.add(adApprovalQueue.getAqLevel() + " APPROVER - " + adApprovalQueue.getAdUser().getUsrDescription());
                }


            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer AD_CMPNY) throws GlobalConversionDateNotExistException {

        Debug.print("GlJournalEntryControllerBean getFrRateByFrNameAndFrDate");


        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, AD_CMPNY);

            double CONVERSION_RATE = 1;

            // Get functional currency rate


            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                CONVERSION_RATE = CONVERSION_RATE / glCompanyFunctionalCurrencyRate.getFrXToUsd();
            }

            return CONVERSION_RATE;

        } catch (FinderException ex) {

            getSessionContext().setRollbackOnly();
            throw new GlobalConversionDateNotExistException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableApPOBatch(Integer AD_CMPNY) {

        Debug.print("ApDebitMemoEntryControllerBean getAdPrfEnableApPOBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfEnableApPOBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApOpenVbAll(String DPRTMNT, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getApOpenVbAll");

        ArrayList list = new ArrayList();


        try {
            Collection apVoucherBatches = null;

            if (DPRTMNT.equals("") || DPRTMNT.equals("default") || DPRTMNT.equals("NO RECORD FOUND")) {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbType("PURCHASE ORDER", AD_BRNCH, AD_CMPNY);

            } else {
                apVoucherBatches = apVoucherBatchHome.findOpenVbByVbTypeDepartment("PURCHASE ORDER", DPRTMNT, AD_BRNCH, AD_CMPNY);
            }

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


    public Integer getPrCodeByPrNumberAndBrCode(String PR_NMBR, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean getPrCodeByPrNumberAndBrCode");


        try {
            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.findByPrNumberAndBrCode(PR_NMBR, AD_BRNCH, AD_CMPNY);

            return apPurchaseRequisition.getPrCode();
        } catch (FinderException ex) {

            //return null if pr not found using reference numbe from PO.
            return null;


        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

    }


    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean convertForeignToFunctionalCurrency");

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

        return EJBCommon.roundIt(AMOUNT, this.getInvGpCostPrecisionUnit(AD_CMPNY));
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

            String ACCOUNT_TYPE = glChartOfAccount.getCoaAccountType();
            short FC_EXTNDD_PRCSN = adCompany.getGlFunctionalCurrency().getFcExtendedPrecision();

            if (((ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("EXPENSE")) && isDebit == EJBCommon.TRUE) || (!ACCOUNT_TYPE.equals("ASSET") && !ACCOUNT_TYPE.equals("EXPENSE") && isDebit == EJBCommon.FALSE)) {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() + JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() + JL_AMNT, FC_EXTNDD_PRCSN));
                }

            } else {

                glChartOfAccountBalance.setCoabEndingBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabEndingBalance() - JL_AMNT, FC_EXTNDD_PRCSN));

                if (!isCurrentAcv) {

                    glChartOfAccountBalance.setCoabBeginningBalance(EJBCommon.roundIt(glChartOfAccountBalance.getCoabBeginningBalance() - JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

            if (isCurrentAcv) {

                if (isDebit == EJBCommon.TRUE) {

                    glChartOfAccountBalance.setCoabTotalDebit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalDebit() + JL_AMNT, FC_EXTNDD_PRCSN));

                } else {

                    glChartOfAccountBalance.setCoabTotalCredit(EJBCommon.roundIt(glChartOfAccountBalance.getCoabTotalCredit() + JL_AMNT, FC_EXTNDD_PRCSN));
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, Integer AD_CMPNY) {

        Debug.print("ApPurchaseOrderEntryControllerBean convertByUomFromAndUomToAndQuantity");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(QTY_RCVD * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void createInvTagList(LocalApPurchaseOrderLine apPurchaseOrderLine, List<InvModTagListDetails> list, Integer AD_CMPNY) throws Exception {

        Debug.print("ApPurchaseOrderEntryControllerBean createInvTagList");


        try {
            Iterator<InvModTagListDetails> t = list.iterator();
            LocalInvTag invTag;
            while (t.hasNext()) {
                InvModTagListDetails tgLstDetails = t.next();
                if (tgLstDetails.getTgCode() == null) {
                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), AD_CMPNY, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());
                    invTag.setApPurchaseOrderLine(apPurchaseOrderLine);
                    invTag.setInvItemLocation(apPurchaseOrderLine.getInvItemLocation());
                    LocalAdUser adUser = null;
                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), AD_CMPNY);
                    } catch (FinderException ex) {

                    }
                    invTag.setAdUser(adUser);
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    private ArrayList getInvTagList(LocalApPurchaseOrderLine apPurchaseOrderLine) {

        ArrayList list = new ArrayList();

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

            list.add(tgLstDetails);

        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApPurchaseOrderEntryControllerBean ejbCreate");
    }
}