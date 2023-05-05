/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApReceivingItemEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ap.*;
import com.ejb.dao.gl.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.ap.*;
import com.ejb.entities.gl.*;
import com.ejb.entities.inv.*;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.ap.ApRINoPurchaseOrderLinesFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvTagSerialNumberAlreadyExistException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ap.ApPurchaseOrderDetails;
import com.util.ap.ApTaxCodeDetails;
import com.util.mod.ad.AdModApprovalQueueDetails;
import com.util.mod.ap.ApModPurchaseOrderDetails;
import com.util.mod.ap.ApModPurchaseOrderLineDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;
import com.util.mod.inv.InvModTagListDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import javax.naming.NamingException;
import java.util.*;

@Stateless(name = "ApReceivingItemEntryControllerEJB")
public class ApReceivingItemEntryControllerBean extends EJBContextClass implements ApReceivingItemEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    ApApprovalController apApprovalController;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdPaymentTermHome adPaymentTermHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalAdApprovalHome adApprovalHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalInvTagHome invTagHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchApTaxCodeHome adBranchApTaxCodeHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalGlForexLedgerHome glForexLedgerHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private LocalGlJournalHome glJournalHome;
    @EJB
    private LocalGlJournalBatchHome glJournalBatchHome;
    @EJB
    private LocalGlSuspenseAccountHome glSuspenseAccountHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlJournalSourceHome glJournalSourceHome;
    @EJB
    private LocalGlJournalCategoryHome glJournalCategoryHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ArrayList getGlFcAllWithDefault(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), companyCode);

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

    public ArrayList getAdPytAll(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdPytAll");

        ArrayList list = new ArrayList();

        try {

            Collection adPaymentTerms = adPaymentTermHome.findEnabledPytAll(companyCode);

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

    public ArrayList getApTcAll(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getApTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(companyCode);

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

    public ApTaxCodeDetails getApTcByTcName(String TC_NM, Integer companyCode) {

        Debug.print("ApPurchaseOrderEntryControllerBean getArTcByTcName");

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);

            ApTaxCodeDetails details = new ApTaxCodeDetails();
            details.setTcType(apTaxCode.getTcType());
            details.setTcRate(apTaxCode.getTcRate());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApRcvOpenPo(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getApRcvOpenPo");

        ArrayList list = new ArrayList();

        try {

            Collection poNumbers = apPurchaseOrderHome.findPostedPoAll(AD_BRNCH, companyCode);

            for (Object poNumber : poNumbers) {

                LocalApPurchaseOrder apPurchaseOrder = (LocalApPurchaseOrder) poNumber;

                Collection apPurchaseOrderLineColl = apPurchaseOrder.getApPurchaseOrderLines();

                Iterator j = apPurchaseOrderLineColl.iterator();
                double totalPoUnServed = 0;
                while (j.hasNext()) {
                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) j.next();
                    totalPoUnServed += apPurchaseOrderLine.getPlQuantity();
                }

                Collection apPurchaseOrderUnServedColl = apPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndPoAdBranchAndPoAdCompany(apPurchaseOrder.getPoDocumentNumber(), AD_BRNCH, companyCode);

                Iterator k = apPurchaseOrderUnServedColl.iterator();
                double totalPoServed = 0;
                while (k.hasNext()) {
                    LocalApPurchaseOrder apPurchaseOrderUnServed = (LocalApPurchaseOrder) k.next();
                    Collection apPurchaseOrderLineUnserved = apPurchaseOrderUnServed.getApPurchaseOrderLines();
                    for (Object o : apPurchaseOrderLineUnserved) {
                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) o;
                        totalPoServed += apPurchaseOrderLine.getPlQuantity();
                    }
                }

                if (totalPoServed < totalPoUnServed) {
                    list.add(apPurchaseOrder.getPoDocumentNumber());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLocAll(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();

        try {

            invLocations = invLocationHome.findLocAll(companyCode);

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

    public ArrayList getInvUomByIiName(String II_NM, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvUomByIiName");

        ArrayList list = new ArrayList();

        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, companyCode);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;
            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), companyCode)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();
                details.setUomName(invUnitOfMeasure.getUomName());
                details.setUomConversionFactor(invUnitOfMeasure.getUomConversionFactor());
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

    public boolean getInvTraceMisc(String II_NAME, Integer companyCode) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();
        boolean isTraceMisc = false;

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, companyCode);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseOrderDetails getApPoByPoRcvPoNumberAndSplSupplierCodeAndAdBranch(String PO_RCV_PO_NMBR, String SPL_SPPLR_CODE, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, ApRINoPurchaseOrderLinesFoundException {

        Debug.print("ApReceivingItemEntryControllerBean getApPoByPoRcvPoNumberAndSplSupplierCodeAndAdBranch");

        ArrayList list = new ArrayList();

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            apPurchaseOrder = apPurchaseOrderHome.findByPoRcvPoNumberAndPoReceivingAndSplSupplierCodeAndBrCode(PO_RCV_PO_NMBR, EJBCommon.FALSE, SPL_SPPLR_CODE, AD_BRNCH, companyCode);

            if (apPurchaseOrder.getPoPosted() != EJBCommon.TRUE) {

                throw new GlobalNoRecordFoundException();
            }

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();
            String serialNumber = null;
            for (Object purchaseOrderLine : apPurchaseOrderLines) {
                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;
                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();
                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());
                if (plDetails.getTraceMisc() == 1) {
                    ArrayList tagList = new ArrayList();
                    tagList = this.getInvTagList(apPurchaseOrderLine);
                    plDetails.setPlTagList(tagList);
                    plDetails.setTraceMisc(plDetails.getTraceMisc());
                }
                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlLine(apPurchaseOrderLine.getPlLine());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlQcNumber(apPurchaseOrderLine.getPlQcNumber());
                plDetails.setPlQcExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());
                plDetails.setPlConversionFactor(this.getConversionFactorByUomFromAndItem(apPurchaseOrderLine.getInvUnitOfMeasure(), apPurchaseOrderLine.getInvItemLocation().getInvItem(), companyCode));

                double RCVD_QTY = 0d;
                double RCVD_DSCNT = 0d;
                double RCVD_AMNT = 0d;

                Collection apReceivingItemLines = apPurchaseOrderLineHome.findByPlPlCode(apPurchaseOrderLine.getPlCode(), companyCode);

                for (Object receivingItemLine : apReceivingItemLines) {

                    LocalApPurchaseOrderLine apReceivingItemLine = (LocalApPurchaseOrderLine) receivingItemLine;

                    if (apReceivingItemLine.getApPurchaseOrder().getPoPosted() == EJBCommon.TRUE) {

                        RCVD_QTY += apReceivingItemLine.getPlQuantity();
                        RCVD_DSCNT += apReceivingItemLine.getPlTotalDiscount();
                        RCVD_AMNT += apReceivingItemLine.getPlAmount() + apReceivingItemLine.getPlTaxAmount();
                    }
                }

                double REMAINING_QTY = apPurchaseOrderLine.getPlQuantity() - RCVD_QTY;
                double REMAINING_DSCNT = apPurchaseOrderLine.getPlTotalDiscount() - RCVD_DSCNT;
                double REMAINING_AMNT = apPurchaseOrderLine.getPlAmount() - RCVD_AMNT;

                if (REMAINING_QTY <= 0) {
                    continue;
                }

                plDetails.setPlRemaining(REMAINING_QTY < 0 ? 0 : REMAINING_QTY);
                plDetails.setPlQuantity(REMAINING_QTY < 0 ? 0 : REMAINING_QTY);
                plDetails.setPlTotalDiscount(REMAINING_DSCNT);
                plDetails.setPlAmount(REMAINING_AMNT);

                list.add(plDetails);
            }

            if (list == null || list.size() == 0) {

                throw new ApRINoPurchaseOrderLinesFoundException();
            }

            ApModPurchaseOrderDetails mdetails = new ApModPurchaseOrderDetails();

            mdetails.setPoCode(apPurchaseOrder.getPoCode());
            mdetails.setPoTcName(apPurchaseOrder.getApTaxCode().getTcName());
            mdetails.setPoTcType(apPurchaseOrder.getApTaxCode().getTcType());
            mdetails.setPoTcRate(apPurchaseOrder.getApTaxCode().getTcRate());
            mdetails.setPoFcName(apPurchaseOrder.getGlFunctionalCurrency().getFcName());
            mdetails.setPoPytName(apPurchaseOrder.getAdPaymentTerm().getPytName());
            mdetails.setPoSplName(apPurchaseOrder.getApSupplier().getSplName());
            mdetails.setPoConversionDate(apPurchaseOrder.getPoConversionDate());
            mdetails.setPoConversionRate(apPurchaseOrder.getPoConversionRate());
            mdetails.setPoDescription(apPurchaseOrder.getPoDescription());
            mdetails.setPoPlList(list);

            return mdetails;

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();

        } catch (GlobalNoRecordFoundException | ApRINoPurchaseOrderLinesFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseOrderDetails getApByPoNumberAndAdBranch(String PO_NMBR, Integer AD_BRNCH, Integer companyCode) throws GlobalNoRecordFoundException, ApRINoPurchaseOrderLinesFoundException {

        Debug.print("ApReceivingItemEntryControllerBean getApByPoNumberAndAdBranch");

        ArrayList list = new ArrayList();

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            apPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(PO_NMBR, EJBCommon.FALSE, AD_BRNCH, companyCode);

            if (apPurchaseOrder.getPoPosted() != EJBCommon.TRUE) {

                throw new GlobalNoRecordFoundException();

            }

            // TODO: get po item line

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            String serialNumber = null;

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();
                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                if (plDetails.getTraceMisc() == 1) {

                    ArrayList tagList = new ArrayList();

                    tagList = this.getInvTagList(apPurchaseOrderLine);
                    plDetails.setPlTagList(tagList);
                    plDetails.setTraceMisc(plDetails.getTraceMisc());
                }

                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlLine(apPurchaseOrderLine.getPlLine());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlQcNumber(apPurchaseOrderLine.getPlQcNumber());
                plDetails.setPlQcExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());
                plDetails.setPlConversionFactor(this.getConversionFactorByUomFromAndItem(apPurchaseOrderLine.getInvUnitOfMeasure(), apPurchaseOrderLine.getInvItemLocation().getInvItem(), companyCode));

                double RCVD_QTY = 0d;
                double RCVD_DSCNT = 0d;
                double RCVD_AMNT = 0d;

                Collection apReceivingItemLines = apPurchaseOrderLineHome.findByPlPlCode(apPurchaseOrderLine.getPlCode(), companyCode);

                for (Object receivingItemLine : apReceivingItemLines) {

                    LocalApPurchaseOrderLine apReceivingItemLine = (LocalApPurchaseOrderLine) receivingItemLine;

                    if (apReceivingItemLine.getApPurchaseOrder().getPoPosted() == EJBCommon.TRUE) {

                        RCVD_QTY += apReceivingItemLine.getPlQuantity();
                        RCVD_DSCNT += apReceivingItemLine.getPlTotalDiscount();
                        RCVD_AMNT += apReceivingItemLine.getPlAmount() + apReceivingItemLine.getPlTaxAmount();
                    }
                }

                double REMAINING_QTY = apPurchaseOrderLine.getPlQuantity() - RCVD_QTY;
                double REMAINING_DSCNT = apPurchaseOrderLine.getPlTotalDiscount() - RCVD_DSCNT;
                double REMAINING_AMNT = apPurchaseOrderLine.getPlAmount() - RCVD_AMNT;

                if (REMAINING_QTY <= 0) {
                    continue;
                }

                plDetails.setPlRemaining(REMAINING_QTY < 0 ? 0 : REMAINING_QTY);
                plDetails.setPlQuantity(REMAINING_QTY < 0 ? 0 : REMAINING_QTY);
                plDetails.setPlTotalDiscount(REMAINING_DSCNT);
                plDetails.setPlAmount(REMAINING_AMNT);

                list.add(plDetails);
            }

            if (list == null || list.size() == 0) {

                throw new ApRINoPurchaseOrderLinesFoundException();
            }

            ApModPurchaseOrderDetails mdetails = new ApModPurchaseOrderDetails();

            mdetails.setPoCode(apPurchaseOrder.getPoCode());
            mdetails.setPoTcName(apPurchaseOrder.getApTaxCode().getTcName());
            mdetails.setPoTcType(apPurchaseOrder.getApTaxCode().getTcType());
            mdetails.setPoTcRate(apPurchaseOrder.getApTaxCode().getTcRate());
            mdetails.setPoFcName(apPurchaseOrder.getGlFunctionalCurrency().getFcName());
            mdetails.setPoPytName(apPurchaseOrder.getAdPaymentTerm().getPytName());
            mdetails.setPoSplName(apPurchaseOrder.getApSupplier().getSplName());
            mdetails.setPoConversionDate(apPurchaseOrder.getPoConversionDate());
            mdetails.setPoConversionRate(apPurchaseOrder.getPoConversionRate());
            mdetails.setPoDescription(apPurchaseOrder.getPoDescription());
            mdetails.setPoPlList(list);

            return mdetails;

        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();

        } catch (GlobalNoRecordFoundException | ApRINoPurchaseOrderLinesFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvIiUnitCostByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvIiUnitCostByIiNameAndUomName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvCostPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getInvUmcByIiNameAndUomName(String II_NM, String UOM_NM, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvUmcByIiNameAndUomName");

        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, companyCode);
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), companyCode);

            return EJBCommon.roundIt(invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvCostPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdUsrAll(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdUsrAll");

        LocalAdUser adUser = null;

        Collection adUsers = null;

        ArrayList list = new ArrayList();

        try {

            adUsers = adUserHome.findUsrAll(companyCode);

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

    public ApModPurchaseOrderDetails getApPoByPoCode(Integer PO_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApReceivingItemEntryControllerBean getApPoByPoCode");

        // TODO: getApPoByPoCOde
        try {

            LocalApPurchaseOrder apPurchaseOrder = null;

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get receiving item line

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            Iterator i = apPurchaseOrderLines.iterator();
            while (i.hasNext()) {
                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();
                ApModPurchaseOrderLineDetails plDetails = new ApModPurchaseOrderLineDetails();
                List<InvModTagListDetails> tagList;
                plDetails.setPlCode(apPurchaseOrderLine.getPlCode());
                plDetails.setPlLine(apPurchaseOrderLine.getPlLine());
                if (apPurchaseOrderLine.getPlPlCode() != null) {
                    double PL_RCVD = 0d;
                    double PL_RMNNG = 0d;
                    Collection apReceivingItemLines = apPurchaseOrderLineHome.findByPlPlCode(apPurchaseOrderLine.getPlPlCode(), companyCode);
                    for (Object receivingItemLine : apReceivingItemLines) {
                        LocalApPurchaseOrderLine apReceivingItemLine = (LocalApPurchaseOrderLine) receivingItemLine;
                        if (apReceivingItemLine.getApPurchaseOrder().getPoPosted() == EJBCommon.TRUE) {
                            PL_RCVD += apReceivingItemLine.getPlQuantity();
                        }
                    }

                    try {
                        LocalApPurchaseOrderLine apPlLine = apPurchaseOrderLineHome.findByPrimaryKey(apPurchaseOrderLine.getPlPlCode());
                        PL_RMNNG = apPlLine.getPlQuantity() - PL_RCVD;
                        plDetails.setPlRemaining(PL_RMNNG < 0 ? 0 : PL_RMNNG);
                    } catch (FinderException ex) {
                        Debug.printStackTrace(ex);
                        throw new EJBException(ex.getMessage());
                    }
                }

                plDetails.setTraceMisc(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiTraceMisc());

                if (plDetails.getTraceMisc() == 1) {

                    tagList = this.getInvTagList(apPurchaseOrderLine);
                    plDetails.setPlTagList(tagList);
                }

                plDetails.setPlPlCode(apPurchaseOrderLine.getPlPlCode());
                plDetails.setPlQuantity(apPurchaseOrderLine.getPlQuantity());
                plDetails.setPlUnitCost(apPurchaseOrderLine.getPlUnitCost());
                plDetails.setPlQcNumber(apPurchaseOrderLine.getPlQcNumber());
                plDetails.setPlQcExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate());
                plDetails.setPlIiName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                plDetails.setPlLocName(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                plDetails.setPlUomName(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                plDetails.setPlIiDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                plDetails.setPlDiscount1(apPurchaseOrderLine.getPlDiscount1());
                plDetails.setPlDiscount2(apPurchaseOrderLine.getPlDiscount2());
                plDetails.setPlDiscount3(apPurchaseOrderLine.getPlDiscount3());
                plDetails.setPlDiscount4(apPurchaseOrderLine.getPlDiscount4());
                plDetails.setPlTotalDiscount(apPurchaseOrderLine.getPlTotalDiscount());
                plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());
                plDetails.setPlConversionFactor(apPurchaseOrderLine.getPlConversionFactor());
                plDetails.setPlAddonCost(apPurchaseOrderLine.getPlAddonCost());

                if (apPurchaseOrder.getApTaxCode().getTcType().equals("INCLUSIVE")) {
                    plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount() + apPurchaseOrderLine.getPlTaxAmount());
                } else {
                    plDetails.setPlAmount(apPurchaseOrderLine.getPlAmount());
                }
                // plDetails.setPlMisc(apPurchaseOrderLine.getPlMisc());
                list.add(plDetails);
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCodeAll("AP RECEIVING ITEM", apPurchaseOrder.getPoCode(), companyCode);

            ArrayList approverlist = new ArrayList();

            i = adApprovalQueues.iterator();

            while (i.hasNext()) {
                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) i.next();

                AdModApprovalQueueDetails adModApprovalQueueDetails = new AdModApprovalQueueDetails();
                adModApprovalQueueDetails.setAqApprovedDate(adApprovalQueue.getAqApprovedDate());
                adModApprovalQueueDetails.setAqApproverName(adApprovalQueue.getAdUser().getUsrDescription());
                adModApprovalQueueDetails.setAqStatus(adApprovalQueue.getAqApproved() == EJBCommon.TRUE ? adApprovalQueue.getAqLevel() + " APPROVED" : adApprovalQueue.getAqLevel() + " PENDING");

                approverlist.add(adModApprovalQueueDetails);
            }

            ApModPurchaseOrderDetails mPoDetails = new ApModPurchaseOrderDetails();

            mPoDetails.setPoCode(apPurchaseOrder.getPoCode());
            mPoDetails.setPoRcvPoNumber(apPurchaseOrder.getPoRcvPoNumber());
            mPoDetails.setPoType(apPurchaseOrder.getPoType());
            mPoDetails.setPoDescription(apPurchaseOrder.getPoDescription());
            mPoDetails.setPoDate(apPurchaseOrder.getPoDate());
            mPoDetails.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
            mPoDetails.setPoReferenceNumber(apPurchaseOrder.getPoReferenceNumber());
            mPoDetails.setPoConversionDate(apPurchaseOrder.getPoConversionDate());
            mPoDetails.setPoConversionRate(apPurchaseOrder.getPoConversionRate());
            mPoDetails.setPoTotalAmount(apPurchaseOrder.getPoTotalAmount());
            mPoDetails.setPoApprovalStatus(apPurchaseOrder.getPoApprovalStatus());
            mPoDetails.setPoReasonForRejection(apPurchaseOrder.getPoReasonForRejection());
            mPoDetails.setPoPosted(apPurchaseOrder.getPoPosted());
            mPoDetails.setPoVoid(apPurchaseOrder.getPoVoid());
            mPoDetails.setPoShipmentNumber(apPurchaseOrder.getPoShipmentNumber());
            mPoDetails.setPoCreatedBy(apPurchaseOrder.getPoCreatedBy());
            mPoDetails.setPoDateCreated(apPurchaseOrder.getPoDateCreated());
            mPoDetails.setPoLastModifiedBy(apPurchaseOrder.getPoLastModifiedBy());
            mPoDetails.setPoDateLastModified(apPurchaseOrder.getPoDateLastModified());
            mPoDetails.setPoApprovedRejectedBy(apPurchaseOrder.getPoApprovedRejectedBy());
            mPoDetails.setPoDateApprovedRejected(apPurchaseOrder.getPoDateApprovedRejected());
            mPoDetails.setPoPostedBy(apPurchaseOrder.getPoPostedBy());
            mPoDetails.setPoDatePosted(apPurchaseOrder.getPoDatePosted());
            mPoDetails.setPoFcName(apPurchaseOrder.getGlFunctionalCurrency().getFcName());
            mPoDetails.setPoTcName(apPurchaseOrder.getApTaxCode().getTcName());
            mPoDetails.setPoTcType(apPurchaseOrder.getApTaxCode().getTcType());
            mPoDetails.setPoTcRate(apPurchaseOrder.getApTaxCode().getTcRate());
            mPoDetails.setPoSplSupplierCode(apPurchaseOrder.getApSupplier().getSplSupplierCode());
            mPoDetails.setPoPytName(apPurchaseOrder.getAdPaymentTerm().getPytName());
            mPoDetails.setPoLock(apPurchaseOrder.getPoLock());
            mPoDetails.setPoSplName(apPurchaseOrder.getApSupplier().getSplName());
            mPoDetails.setPoFreight(apPurchaseOrder.getPoFreight());
            mPoDetails.setPoDuties(apPurchaseOrder.getPoDuties());
            mPoDetails.setPoEntryFee(apPurchaseOrder.getPoEntryFee());
            mPoDetails.setPoStorage(apPurchaseOrder.getPoStorage());
            mPoDetails.setPoWharfageHandling(apPurchaseOrder.getPoWharfageHandling());

            mPoDetails.setPoPlList(list);

            mPoDetails.setPoAPRList(approverlist);

            return mPoDetails;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModSupplierDetails getApSplBySplSupplierCode(String SPL_SPPLR_CODE, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("ApReceivingItemEntryControllerBean getApSplBySplSupplierCode");

        try {

            LocalApSupplier apSupplier = null;

            try {

                apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ApModSupplierDetails mdetails = new ApModSupplierDetails();

            mdetails.setSplPytName(apSupplier.getAdPaymentTerm() != null ? apSupplier.getAdPaymentTerm().getPytName() : null);
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

    public Integer saveApPoEntryMobile(ApPurchaseOrderDetails details, String PYT_NM, String FC_NM, ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException, InvTagSerialNumberAlreadyExistException {

        Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile");

        LocalApPurchaseOrder apPurchaseOrder = null;

        Date txnStartDate = new Date();

        try {

            apPurchaseOrder = deletedRecord(details, apPurchaseOrder);

            // validate if receiving item is already posted, void, approved or pending
            validateReceivingItemStatus(details, apPurchaseOrder);

            // set receiving item as void
            if (voidRecevingItem(details, apPurchaseOrder)) {
                return apPurchaseOrder.getPoCode();
            }

            // validate if document number is unique document number is automatic then set
            // next sequence
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile A");
            try {

                LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                if (details.getPoCode() == null) {

                    generateDocumentNumber(details, AD_BRNCH, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);

                    Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile A1");

                } else {

                    LocalApPurchaseOrder apExistingReceivingItem = null;

                    try {

                        apExistingReceivingItem = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(details.getPoDocumentNumber(), EJBCommon.TRUE, AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    if (apExistingReceivingItem != null && !apExistingReceivingItem.getPoCode().equals(details.getPoCode())) {

                        throw new GlobalDocumentNumberNotUniqueException();
                    }

                    if (apPurchaseOrder.getPoDocumentNumber() != details.getPoDocumentNumber() && (details.getPoDocumentNumber() == null || details.getPoDocumentNumber().trim().length() == 0)) {

                        details.setPoDocumentNumber(apPurchaseOrder.getPoDocumentNumber());
                    }

                    Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile A2");
                }

                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile A Done");

            } catch (GlobalDocumentNumberNotUniqueException ex) {

                getSessionContext().setRollbackOnly();
                throw ex;

            } catch (Exception ex) {

                Debug.printStackTrace(ex);
                getSessionContext().setRollbackOnly();
                throw new EJBException(ex.getMessage());
            }

            // validate if conversion date exists
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile B");
            try {

                if (details.getPoConversionDate() != null) {

                    LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

                    if (!glValidateFunctionalCurrency.getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPoConversionDate(), companyCode);

                    } else if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                        LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), details.getPoConversionDate(), companyCode);
                    }
                }
                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile B Done");

            } catch (FinderException ex) {

                throw new GlobalConversionDateNotExistException();
            }

            // validate if payment term has at least one payment schedule
            validatePaymentTerm("IMMEDIATE", companyCode);

            // lock purchase order
            LocalApPurchaseOrder apExistingPurchaseOrder = null;
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile C");
            if (details.getPoType().equals("PO MATCHED")) {

                apExistingPurchaseOrder = validatedPoMatched(details, AD_BRNCH, companyCode, apPurchaseOrder, apExistingPurchaseOrder);

            } else {

                // replace the empty string with null
                details.setPoRcvPoNumber(null);
            }
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile C Done");
            // used in checking if receiving item should re-generate distribution records
            // and re-calculate taxes
            boolean isRecalculate = true;

            // create receiving item

            boolean newPurchaseOrder = false;
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile D");

            if (details.getPoCode() == null) {

                apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.TRUE, details.getPoType(), details.getPoDate(), details.getPoDeliveryPeriod(), details.getPoDocumentNumber(), details.getPoReferenceNumber(), details.getPoRcvPoNumber(), details.getPoDescription(), null, null, details.getPoConversionDate(), details.getPoConversionRate(), details.getPoTotalAmount(), EJBCommon.FALSE, EJBCommon.FALSE, details.getPoShipmentNumber(), null, EJBCommon.FALSE, null, details.getPoCreatedBy(), details.getPoDateCreated(), details.getPoLastModifiedBy(), details.getPoDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getPoFreight(), details.getPoDuties(), details.getPoEntryFee(), details.getPoStorage(), details.getPoWharfageHandling(), AD_BRNCH, companyCode);

                newPurchaseOrder = true;
                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile D1");

            } else {

                // check if critical fields are changed

                if (!apPurchaseOrder.getApTaxCode().getTcName().equals("") || !apPurchaseOrder.getApSupplier().getSplSupplierCode().equals("") || !apPurchaseOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) || plList.size() != apPurchaseOrder.getApPurchaseOrderLines().size() || apPurchaseOrder.getPoFreight() != details.getPoFreight() || apPurchaseOrder.getPoDuties() != details.getPoDuties() || apPurchaseOrder.getPoEntryFee() != details.getPoEntryFee() || apPurchaseOrder.getPoStorage() != details.getPoStorage() || apPurchaseOrder.getPoWharfageHandling() != details.getPoWharfageHandling() || !(apPurchaseOrder.getPoDate().equals(details.getPoDate()))) {

                    isRecalculate = true;

                } else if (plList.size() == apPurchaseOrder.getApPurchaseOrderLines().size()) {

                    Iterator ilIter = apPurchaseOrder.getApPurchaseOrderLines().iterator();
                    Iterator ilListIter = plList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();
                        ApModPurchaseOrderLineDetails mdetails = (ApModPurchaseOrderLineDetails) ilListIter.next();

                        if (!apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPlIiName()) || !apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPlLocName()) || !apPurchaseOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPlUomName()) || apPurchaseOrderLine.getPlQuantity() != mdetails.getPlQuantity() || apPurchaseOrderLine.getPlUnitCost() != mdetails.getPlUnitCost() || apPurchaseOrderLine.getPlTotalDiscount() != mdetails.getPlTotalDiscount()) {

                            isRecalculate = true;
                            break;
                        }

                        // isRecalculate = false;

                    }
                    Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile D3");

                } else {

                    // isRecalculate = false;

                }

                apPurchaseOrder.setPoReceiving(EJBCommon.TRUE);
                apPurchaseOrder.setPoDescription(details.getPoDescription());
                apPurchaseOrder.setPoRcvPoNumber(details.getPoRcvPoNumber());
                apPurchaseOrder.setPoType(details.getPoType());
                apPurchaseOrder.setPoDate(details.getPoDate());
                apPurchaseOrder.setPoDeliveryPeriod(details.getPoDeliveryPeriod());
                apPurchaseOrder.setPoDocumentNumber(details.getPoDocumentNumber());
                apPurchaseOrder.setPoReferenceNumber(details.getPoReferenceNumber());
                apPurchaseOrder.setPoShipmentNumber(details.getPoShipmentNumber());
                apPurchaseOrder.setPoFreight(details.getPoFreight());
                apPurchaseOrder.setPoDuties(details.getPoDuties());
                apPurchaseOrder.setPoEntryFee(details.getPoEntryFee());
                apPurchaseOrder.setPoStorage(details.getPoStorage());
                apPurchaseOrder.setPoWharfageHandling(details.getPoWharfageHandling());
                apPurchaseOrder.setPoConversionDate(details.getPoConversionDate());
                apPurchaseOrder.setPoConversionRate(details.getPoConversionRate());
                apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
                apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());
                apPurchaseOrder.setPoReasonForRejection(null);
            }
            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile D Done");

            // get original PO
            LocalApPurchaseOrder apOrigPurchaseOrder = null;
            apOrigPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndBrCode(details.getPoRcvPoNumber(), AD_BRNCH, companyCode);
            LocalApTaxCode apTaxCode = apOrigPurchaseOrder.getApTaxCode();
            apPurchaseOrder.setPoReferenceNumber(apOrigPurchaseOrder.getPoReferenceNumber());
            apPurchaseOrder.setAdPaymentTerm(apOrigPurchaseOrder.getAdPaymentTerm());
            apPurchaseOrder.setApTaxCode(apOrigPurchaseOrder.getApTaxCode());
            apPurchaseOrder.setGlFunctionalCurrency(apOrigPurchaseOrder.getGlFunctionalCurrency());
            apPurchaseOrder.setApSupplier(apOrigPurchaseOrder.getApSupplier());

            double TOTAL_AMOUNT = 0;

            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile E");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            if (isRecalculate) {

                // remove all receiving item line

                Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

                Iterator i = apPurchaseOrderLines.iterator();

                while (i.hasNext()) {

                    LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) i.next();

                    i.remove();

                    // apPurchaseOrderLine.entityRemove();
                    em.remove(apPurchaseOrderLine);
                }

                // remove all distribution records

                Collection apDistributionRecords = apPurchaseOrder.getApDistributionRecords();

                i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    i.remove();

                    // apDistributionRecord.entityRemove();
                    em.remove(apDistributionRecord);
                }

                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile E01");

                // add new receiving item line and distribution record

                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                LocalInvItemHome invItemHome = null;
                LocalApPurchaseOrderLineHome apPurchaseOrderLineHome = null;

                try {

                    invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
                    apPurchaseOrderLineHome = (LocalApPurchaseOrderLineHome) EJBHomeFactory.lookUpLocalHome(LocalApPurchaseOrderLineHome.JNDI_NAME, LocalApPurchaseOrderLineHome.class);

                } catch (NamingException ex) {

                    throw new EJBException(ex.getMessage());
                }

                // get all inv tags that was already received
                Collection invTagColl = invTagHome.findAllByPlCodeNotNull(companyCode);

                i = plList.iterator();
                LocalInvItemLocation invItemLocation = null;

                String invItemCategory = null;

                while (i.hasNext()) {

                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();

                    try {

                        LocalInvItem invItem = invItemHome.findByPartNumber(mPlDetails.getPlPartNumber(), companyCode);
                        LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invLocation.getLocName(), invItem.getIiName(), companyCode);

                        // -- Get item category
                        invItemCategory = invItemLocation.getInvItem().getIiAdLvCategory();

                        LocalApPurchaseOrderLine apOrigPurchaseOrderLine = apPurchaseOrderLineHome.findPlByPoNumberAndIiNameAndLocName(apOrigPurchaseOrder.getPoDocumentNumber(), invItem.getIiName(), invLocation.getLocName(), apOrigPurchaseOrder.getPoAdBranch(), companyCode);

                        mPlDetails.setPlCode(apOrigPurchaseOrderLine.getPlCode());
                        mPlDetails.setPlLine(apOrigPurchaseOrderLine.getPlLine());
                        mPlDetails.setPlIiName(invItem.getIiName());
                        mPlDetails.setPlLocName(invLocation.getLocName());
                        mPlDetails.setPlUomName(apOrigPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                        mPlDetails.setPlUnitCost(apOrigPurchaseOrderLine.getPlUnitCost());
                        mPlDetails.setPlAmount(mPlDetails.getPlUnitCost() * mPlDetails.getPlQuantity());
                        mPlDetails.setPlConversionFactor(apOrigPurchaseOrderLine.getPlConversionFactor());
                        mPlDetails.setPlPlCode(apOrigPurchaseOrderLine.getPlCode());
                        mPlDetails.setPlDiscount1(apOrigPurchaseOrderLine.getPlDiscount1());
                        mPlDetails.setPlDiscount2(apOrigPurchaseOrderLine.getPlDiscount2());
                        mPlDetails.setPlDiscount3(apOrigPurchaseOrderLine.getPlDiscount3());
                        mPlDetails.setPlDiscount4(apOrigPurchaseOrderLine.getPlDiscount4());
                        mPlDetails.setPlTotalDiscount(apOrigPurchaseOrderLine.getPlTotalDiscount());
                        mPlDetails.setPlConversionFactor(this.getConversionFactorByUomFromAndItem(apOrigPurchaseOrderLine.getInvUnitOfMeasure(), apOrigPurchaseOrderLine.getInvItemLocation().getInvItem(), companyCode));
                        // mPlDetails

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    // start date validation

                    validatePriorDateTransaction(AD_BRNCH, companyCode, apPurchaseOrder, adPreference, invItemLocation, mPlDetails);

                    LocalApPurchaseOrderLine apPurchaseOrderLine = this.addApPlEntry(mPlDetails, apPurchaseOrder, invItemLocation, newPurchaseOrder, companyCode);

                    if (mPlDetails.getPlQuantity() == 0) {
                        continue;
                    }
                    // add inventory distributions

                    LocalAdBranchItemLocation adBranchItemLocation = null;

                    try {
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(apPurchaseOrderLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);

                    } catch (FinderException ex) {

                    }

                    String DR_CLASS = (invItemCategory == "DEFAULT" ? "EXPENSE" : "INVENTORY");

                    if (adBranchItemLocation != null) {

                        Integer debitCoa = (DR_CLASS == "INVENTORY" ? adBranchItemLocation.getBilCoaGlInventoryAccount() : adBranchItemLocation.getBilCoaGlExpenseAccount());

                        // Use AdBranchItemLocation Coa Account
                        this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), DR_CLASS, EJBCommon.TRUE, apPurchaseOrderLine.getPlAmount(), debitCoa, apPurchaseOrder, AD_BRNCH, companyCode);

                    } else {

                        Integer debitCoa = (DR_CLASS == "INVENTORY" ? apPurchaseOrderLine.getInvItemLocation().getIlGlCoaInventoryAccount() : apPurchaseOrderLine.getInvItemLocation().getIlGlCoaExpenseAccount());

                        // Use default account
                        this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "INVENTORY", EJBCommon.TRUE, apPurchaseOrderLine.getPlAmount(), debitCoa, apPurchaseOrder, AD_BRNCH, companyCode);
                    }

                    TOTAL_LINE += apPurchaseOrderLine.getPlAmount();
                    TOTAL_TAX += apPurchaseOrderLine.getPlTaxAmount();
                    Iterator t = null;

                    LocalInvItem invItem = invItemHome.findByIiName(mPlDetails.getPlIiName(), companyCode);
                    if (invItem.getIiTraceMisc() == 1) { // change this
                        t = mPlDetails.getPlTagList().iterator();
                    }
                    LocalInvTag invTag = null;

                    if (invItem.getIiPartNumber().charAt(2) == '1') {
                        if (invItem.getIiTraceMisc() == 1) {
                            while (t.hasNext()) {
                                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                                // check if the serial number was already received else throw an exception
                                for (Object o : invTagColl) {
                                    LocalInvTag invExistingTag = (LocalInvTag) o;
                                    if (tgLstDetails.getTgSerialNumber().equals(invExistingTag.getTgSerialNumber())) {

                                        throw new InvTagSerialNumberAlreadyExistException(tgLstDetails.getTgSerialNumber());
                                    }
                                }

                                if (tgLstDetails.getTgCode() == null) {
                                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                                    invTag.setApPurchaseOrderLine(apPurchaseOrderLine);
                                    LocalAdUser adUser = null;
                                    try {
                                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                                    } catch (FinderException ex) {

                                    }
                                    if (invItem.getIiNonInventoriable() == 1) {
                                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber2());
                                        adPreference.setPrfInvNextCustodianNumber2(EJBCommon.incrementStringNumber(adPreference.getPrfInvNextCustodianNumber2()));
                                    } else {
                                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber1());
                                        adPreference.setPrfInvNextCustodianNumber1(EJBCommon.incrementStringNumber(adPreference.getPrfInvNextCustodianNumber1()));
                                    }
                                    invTag.setAdUser(adUser);
                                }
                            }
                        }
                    }
                }
                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile E02");
                // add tax distribution if necessary

                if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                    if (adPreference.getPrfApUseAccruedVat() == EJBCommon.FALSE) {

                        LocalAdBranchApTaxCode adBranchTaxCode = null;
                        Debug.print("ApReceivingItemEntryControllerBean saveArInvIliEntry J");

                        try {
                            adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apPurchaseOrder.getApTaxCode().getTcCode(), AD_BRNCH, companyCode);

                        } catch (FinderException ex) {

                        }

                        if (adBranchTaxCode != null) {

                            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, adBranchTaxCode.getBtcGlCoaTaxCode(), apPurchaseOrder, AD_BRNCH, companyCode);

                        } else {

                            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, apTaxCode.getGlChartOfAccount().getCoaCode(), apPurchaseOrder, AD_BRNCH, companyCode);
                        }

                    } else {

                        this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "ACC TAX", EJBCommon.TRUE, TOTAL_TAX, adPreference.getPrfApGlCoaAccruedVatAccount(), apPurchaseOrder, AD_BRNCH, companyCode);
                    }
                }

                TOTAL_AMOUNT = TOTAL_LINE + TOTAL_TAX;

            } else {

                LocalInvItemHome invItemHome = null;
                LocalApPurchaseOrderLineHome apPurchaseOrderLineHome = null;

                try {

                    invItemHome = (LocalInvItemHome) EJBHomeFactory.lookUpLocalHome(LocalInvItemHome.JNDI_NAME, LocalInvItemHome.class);
                    apPurchaseOrderLineHome = (LocalApPurchaseOrderLineHome) EJBHomeFactory.lookUpLocalHome(LocalApPurchaseOrderLineHome.JNDI_NAME, LocalApPurchaseOrderLineHome.class);

                } catch (NamingException ex) {

                    throw new EJBException(ex.getMessage());
                }

                Iterator i = plList.iterator();

                LocalInvItemLocation invItemLocation = null;

                while (i.hasNext()) {

                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();

                    try {

                        LocalInvItem invItem = invItemHome.findByPartNumber(mPlDetails.getPlPartNumber(), companyCode);
                        LocalInvLocation invLocation = invLocationHome.findByPrimaryKey(invItem.getIiDefaultLocation());
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invLocation.getLocName(), invItem.getIiName(), companyCode);

                        LocalApPurchaseOrderLine apOrigPurchaseOrderLine = apPurchaseOrderLineHome.findPlByPoNumberAndIiNameAndLocName(apOrigPurchaseOrder.getPoDocumentNumber(), invItem.getIiName(), invLocation.getLocName(), apOrigPurchaseOrder.getPoAdBranch(), companyCode);

                        mPlDetails.setPlCode(apOrigPurchaseOrderLine.getPlCode());
                        mPlDetails.setPlLine(apOrigPurchaseOrderLine.getPlLine());
                        mPlDetails.setPlIiName(invItem.getIiName());
                        mPlDetails.setPlLocName(invLocation.getLocName());
                        mPlDetails.setPlUomName(apOrigPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                        mPlDetails.setPlUnitCost(apOrigPurchaseOrderLine.getPlUnitCost());
                        mPlDetails.setPlAmount(mPlDetails.getPlUnitCost() * mPlDetails.getPlQuantity());
                        mPlDetails.setPlConversionFactor(apOrigPurchaseOrderLine.getPlConversionFactor());
                        mPlDetails.setPlPlCode(apOrigPurchaseOrderLine.getPlCode());
                        mPlDetails.setPlDiscount1(apOrigPurchaseOrderLine.getPlDiscount1());
                        mPlDetails.setPlDiscount2(apOrigPurchaseOrderLine.getPlDiscount2());
                        mPlDetails.setPlDiscount3(apOrigPurchaseOrderLine.getPlDiscount3());
                        mPlDetails.setPlDiscount4(apOrigPurchaseOrderLine.getPlDiscount4());
                        mPlDetails.setPlTotalDiscount(apOrigPurchaseOrderLine.getPlTotalDiscount());
                        mPlDetails.setPlConversionFactor(this.getConversionFactorByUomFromAndItem(apOrigPurchaseOrderLine.getInvUnitOfMeasure(), apOrigPurchaseOrderLine.getInvItemLocation().getInvItem(), companyCode));

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }
                }

                Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile E03");

                Collection apDistributionRecords = apPurchaseOrder.getApDistributionRecords();

                i = apDistributionRecords.iterator();

                while (i.hasNext()) {

                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                    if (apDistributionRecord.getDrDebit() == 0) {

                        TOTAL_AMOUNT += apDistributionRecord.getDrAmount();
                    }
                }
            }

            Debug.print("ApReceivingItemEntryControllerBean saveApPoEntryMobile E Done");
            // generate approval status

            String PO_APPRVL_STATUS = null;

            if (!isDraft) {

                PO_APPRVL_STATUS = "N/A";

                // release purchase order lock

                if (details.getPoType().equals("PO MATCHED")) {

                    apExistingPurchaseOrder.setPoLock(EJBCommon.FALSE);
                }
            }

            if (PO_APPRVL_STATUS != null && PO_APPRVL_STATUS.equals("N/A")) {

                this.executeApReceivingPost(apPurchaseOrder.getPoCode(), apPurchaseOrder.getPoLastModifiedBy(), AD_BRNCH, companyCode);
            }

            // set receiving item approval status

            apPurchaseOrder.setPoApprovalStatus(PO_APPRVL_STATUS);
            return apPurchaseOrder.getPoCode();

        } catch (GlobalRecordAlreadyDeletedException | InvTagSerialNumberAlreadyExistException |
                 GlobalMiscInfoIsRequiredException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalBranchAccountNumberInvalidException | GlobalTransactionAlreadyVoidPostedException |
                 GlobalInventoryDateException | GlobalTransactionAlreadyLockedException |
                 GlobalJournalNotBalanceException | GlJREffectiveDatePeriodClosedException |
                 GlJREffectiveDateNoPeriodExistException | GlobalInvItemLocationNotFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalTransactionAlreadyPendingException | GlobalPaymentTermInvalidException |
                 GlobalConversionDateNotExistException | GlobalDocumentNumberNotUniqueException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveApPoEntry(ApPurchaseOrderDetails details, boolean recalculateJournal, String PYT_NM, String TC_NM, String FC_NM, String SPL_SPPLR_CODE, ArrayList plList, boolean isDraft, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalDocumentNumberNotUniqueException, GlobalConversionDateNotExistException, GlobalPaymentTermInvalidException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlobalInvItemLocationNotFoundException, GlobalNoApprovalRequesterFoundException, GlobalNoApprovalApproverFoundException, GlobalInvTagMissingException, GlobalInvTagExistingException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalTransactionAlreadyLockedException, GlobalInventoryDateException, GlobalTransactionAlreadyVoidPostedException, GlobalBranchAccountNumberInvalidException, AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException, GlobalRecordInvalidException, GlobalReferenceNumberNotUniqueException, InvTagSerialNumberAlreadyExistException {

        Debug.print("ApReceivingItemEntryControllerBean saveApPoEntry");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            // validate if receiving item is already deleted
            apPurchaseOrder = deletedRecord(details, apPurchaseOrder);

            validateReceivingItemStatus(details, apPurchaseOrder);

            // set receiving item as void
            if (voidRecevingItem(details, apPurchaseOrder)) {
                return apPurchaseOrder.getPoCode();
            }

            validateDocumentSequence(details, AD_BRNCH, companyCode, apPurchaseOrder);
            validatePaymentTerm(PYT_NM, companyCode);
            validateShipmentNumber(details, companyCode);
            validatedConversionDate(details, FC_NM, companyCode);

            // lock purchase order
            LocalApPurchaseOrder apExistingPurchaseOrder = null;
            if (details.getPoType().equals("PO MATCHED")) {
                apExistingPurchaseOrder = validatedPoMatched(details, AD_BRNCH, companyCode, apPurchaseOrder, apExistingPurchaseOrder);
            }

            // used in checking if receiving item should re-generate distribution records and re-calculate taxes
            boolean isRecalculate = true;

            // create receiving item
            boolean newPurchaseOrder = false;
            if (details.getPoCode() == null) {

                apPurchaseOrder = apPurchaseOrderHome.create(EJBCommon.TRUE, details.getPoType(), details.getPoDate(), details.getPoDeliveryPeriod(), details.getPoDocumentNumber(), details.getPoReferenceNumber(), details.getPoRcvPoNumber(), details.getPoDescription(), null, null, details.getPoConversionDate(), details.getPoConversionRate(), details.getPoTotalAmount(), EJBCommon.FALSE, EJBCommon.FALSE, details.getPoShipmentNumber(), null, EJBCommon.FALSE, null, details.getPoCreatedBy(), details.getPoDateCreated(), details.getPoLastModifiedBy(), details.getPoDateLastModified(), null, null, null, null, EJBCommon.FALSE, details.getPoFreight(), details.getPoDuties(), details.getPoEntryFee(), details.getPoStorage(), details.getPoWharfageHandling(), AD_BRNCH, companyCode);
                recalculateJournal = true;
                newPurchaseOrder = true;

            } else {

                // check if critical fields are changed
                if (!apPurchaseOrder.getApTaxCode().getTcName().equals(TC_NM) || !apPurchaseOrder.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CODE) || !apPurchaseOrder.getAdPaymentTerm().getPytName().equals(PYT_NM) || plList.size() != apPurchaseOrder.getApPurchaseOrderLines().size() || apPurchaseOrder.getPoFreight() != details.getPoFreight() || apPurchaseOrder.getPoDuties() != details.getPoDuties() || apPurchaseOrder.getPoEntryFee() != details.getPoEntryFee() || apPurchaseOrder.getPoStorage() != details.getPoStorage() || apPurchaseOrder.getPoWharfageHandling() != details.getPoWharfageHandling() || !(apPurchaseOrder.getPoDate().equals(details.getPoDate()))) {

                    isRecalculate = true;

                } else if (plList.size() == apPurchaseOrder.getApPurchaseOrderLines().size()) {

                    Iterator ilIter = apPurchaseOrder.getApPurchaseOrderLines().iterator();
                    Iterator ilListIter = plList.iterator();

                    while (ilIter.hasNext()) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) ilIter.next();
                        ApModPurchaseOrderLineDetails mdetails = (ApModPurchaseOrderLineDetails) ilListIter.next();

                        if (!apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getPlIiName()) || !apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getPlLocName()) || !apPurchaseOrderLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getPlUomName()) || apPurchaseOrderLine.getPlQuantity() != mdetails.getPlQuantity() || apPurchaseOrderLine.getPlUnitCost() != mdetails.getPlUnitCost() || apPurchaseOrderLine.getPlAmount() != mdetails.getPlAmount()) {
                            isRecalculate = true;
                            break;
                        }
                    }
                }

                apPurchaseOrder.setPoReceiving(EJBCommon.TRUE);
                apPurchaseOrder.setPoDescription(details.getPoDescription());
                apPurchaseOrder.setPoRcvPoNumber(details.getPoRcvPoNumber());
                apPurchaseOrder.setPoType(details.getPoType());
                apPurchaseOrder.setPoDate(details.getPoDate());
                apPurchaseOrder.setPoDeliveryPeriod(details.getPoDeliveryPeriod());
                apPurchaseOrder.setPoDocumentNumber(details.getPoDocumentNumber());
                apPurchaseOrder.setPoReferenceNumber(details.getPoReferenceNumber());
                apPurchaseOrder.setPoShipmentNumber(details.getPoShipmentNumber());
                apPurchaseOrder.setPoFreight(details.getPoFreight());
                apPurchaseOrder.setPoDuties(details.getPoDuties());
                apPurchaseOrder.setPoEntryFee(details.getPoEntryFee());
                apPurchaseOrder.setPoStorage(details.getPoStorage());
                apPurchaseOrder.setPoWharfageHandling(details.getPoWharfageHandling());
                apPurchaseOrder.setPoConversionDate(details.getPoConversionDate());
                apPurchaseOrder.setPoConversionRate(details.getPoConversionRate());
                apPurchaseOrder.setPoTotalAmount(details.getPoTotalAmount());
                apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
                apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());
                apPurchaseOrder.setPoReasonForRejection(null);
            }

            LocalAdPaymentTerm adPaymentTerm = adPaymentTermHome.findByPytName(PYT_NM, companyCode);
            apPurchaseOrder.setAdPaymentTerm(adPaymentTerm);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, companyCode);
            apPurchaseOrder.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
            apPurchaseOrder.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SPL_SPPLR_CODE, companyCode);
            apPurchaseOrder.setApSupplier(apSupplier);

            double TOTAL_AMOUNT = 0;

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdUser adUser = adUserHome.findByUsrName(apPurchaseOrder.getPoCreatedBy(), companyCode);

            if (isRecalculate) {

                // remove all receiving item line
                removeReceivingItemLines(apPurchaseOrder);
                Iterator i;

                // remove all distribution records
                removeDistributionRecords(recalculateJournal, apPurchaseOrder);

                double PO_TTL_AMNT = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrder.getPoTotalAmount(), companyCode);

                double TTL_ADDON_COST = apPurchaseOrder.getPoDuties() + apPurchaseOrder.getPoFreight() + apPurchaseOrder.getPoEntryFee() + apPurchaseOrder.getPoStorage() + apPurchaseOrder.getPoWharfageHandling();

                // add new receiving item line and distribution record
                double TOTAL_TAX = 0d;
                double TOTAL_LINE = 0d;

                i = plList.iterator();

                LocalInvItemLocation invItemLocation = null;
                String invItemCategory = null;

                while (i.hasNext()) {

                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();

                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPlDetails.getPlLocName(), mPlDetails.getPlIiName(), companyCode);
                        invItemCategory = invItemLocation.getInvItem().getIiAdLvCategory();
                    } catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    // start date validation
                    validatePriorDateTransaction(AD_BRNCH, companyCode, apPurchaseOrder, adPreference, invItemLocation, mPlDetails);

                    LocalApPurchaseOrderLine apPurchaseOrderLine = this.addApPlEntry(mPlDetails, apPurchaseOrder, invItemLocation, newPurchaseOrder, companyCode);

                    if (mPlDetails.getPlQuantity() == 0) {
                        continue;
                    }

                    // add inventory distributions
                    LocalAdBranchItemLocation adBranchItemLocation = null;
                    try {
                        adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(apPurchaseOrderLine.getInvItemLocation().getIlCode(), AD_BRNCH, companyCode);
                    } catch (FinderException ex) {

                    }

                    double PL_AMNT = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrderLine.getPlAmount(), companyCode);

                    double PL_TX_AMNT = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrderLine.getPlTaxAmount(), companyCode);

                    if (recalculateJournal) {
                        double ADDON_COST = addOnCostJournals(AD_BRNCH, companyCode, apPurchaseOrder, PO_TTL_AMNT, TTL_ADDON_COST, adBranchItemLocation, PL_AMNT);

                        // -- Setup DR Class
                        String DR_CLASS = (invItemCategory.equals("DEFAULT")) ? "EXPENSE" : "INVENTORY";
                        inventoryJournals(AD_BRNCH, companyCode, apPurchaseOrder, apPurchaseOrderLine, adBranchItemLocation, PL_AMNT, PL_TX_AMNT, ADDON_COST, DR_CLASS);
                    }

                    TOTAL_LINE += PL_AMNT;
                    TOTAL_TAX += PL_TX_AMNT;

                    // TODO: add new inv Tag
                    // validate inventoriable and non-inventoriable items
                    boolean invTagExisting = false;
                    String serialNumberExisting = null;
                    if (mPlDetails.getTraceMisc() == 1) {
                        adUser = setupInventoryTag(companyCode, adPreference, adUser, mPlDetails, apPurchaseOrderLine, invTagExisting, serialNumberExisting);
                    }
                }

                if (recalculateJournal) {
                    // add tax distribution if necessary
                    taxJournals(AD_BRNCH, companyCode, apPurchaseOrder, apTaxCode, adPreference, TOTAL_TAX);
                    TOTAL_AMOUNT = TOTAL_LINE + TOTAL_TAX;
                }

            } else {

                Iterator i = plList.iterator();
                LocalInvItemLocation invItemLocation = null;
                while (i.hasNext()) {
                    ApModPurchaseOrderLineDetails mPlDetails = (ApModPurchaseOrderLineDetails) i.next();
                    try {
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mPlDetails.getPlLocName(), mPlDetails.getPlIiName(), companyCode);
                    } catch (FinderException ex) {
                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mPlDetails.getPlLine()));
                    }

                    // start date validation
                    if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                        Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                        if (!invNegTxnCosting.isEmpty()) {
                            throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                        }
                    }

                    LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.findByPrimaryKey(mPlDetails.getPlCode());

                    // remove all inv tag inside PO line
                    Collection invTags = apPurchaseOrderLine.getInvTags();
                    Iterator x = invTags.iterator();
                    while (x.hasNext()) {
                        LocalInvTag invTag = (LocalInvTag) x.next();
                        x.remove();
                        em.remove(invTag);
                    }

                    // validate existing inventoriable and non-inventoriable items
                    LocalInvTag invTag = null;

                    if (mPlDetails.getTraceMisc() == 1) {

                        // try {
                        for (Object o : mPlDetails.getPlTagList()) {
                            InvModTagListDetails tgLstDetails = (InvModTagListDetails) o;

                            invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                            invTag.setApPurchaseOrderLine(apPurchaseOrderLine);

                            try {
                                adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                            } catch (FinderException ex) {

                            }
                            invTag.setAdUser(adUser);
                        }
                    }
                }

                Collection apDistributionRecords = apPurchaseOrder.getApDistributionRecords();
                i = apDistributionRecords.iterator();
                while (i.hasNext()) {
                    LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();
                    if (apDistributionRecord.getDrDebit() == 0) {
                        TOTAL_AMOUNT += apDistributionRecord.getDrAmount();
                    }
                }
            }

            // set receiving item approval status
            String PO_APPRVL_STATUS = null;
            if (!isDraft) {
                if (details.getPoType().equals("PO MATCHED")) {
                    apExistingPurchaseOrder.setPoLock(EJBCommon.FALSE);
                }
                LocalAdApproval adApproval = adApprovalHome.findByAprAdCompany(companyCode);

                // check if receiving item approval is enabled
                if (adApproval.getAprEnableApReceivingItem() == EJBCommon.FALSE) {
                    PO_APPRVL_STATUS = "N/A";

                } else {
                    // check if voucher is self approved
                    LocalAdUser adUser2 = adUserHome.findByUsrName(details.getPoLastModifiedBy(), companyCode);
                    PO_APPRVL_STATUS = apApprovalController.getApprovalStatus(adUser.getUsrDept(), details.getPoLastModifiedBy(), adUser2.getUsrDescription(), "AP RECEIVING ITEM", apPurchaseOrder.getPoCode(), apPurchaseOrder.getPoDocumentNumber(), apPurchaseOrder.getPoDate(), TOTAL_AMOUNT, AD_BRNCH, companyCode);

                }
                apPurchaseOrder.setPoApprovalStatus(PO_APPRVL_STATUS);

                if (PO_APPRVL_STATUS != null && PO_APPRVL_STATUS.equals("N/A")) {
                    this.executeApReceivingPost(apPurchaseOrder.getPoCode(), apPurchaseOrder.getPoLastModifiedBy(), AD_BRNCH, companyCode);
                }
            }

            return apPurchaseOrder.getPoCode();

        } catch (GlobalRecordAlreadyDeletedException | InvTagSerialNumberAlreadyExistException |
                 GlobalReferenceNumberNotUniqueException | GlobalMiscInfoIsRequiredException |
                 AdPRFCoaGlVarianceAccountNotFoundException | GlobalBranchAccountNumberInvalidException |
                 GlobalTransactionAlreadyVoidPostedException | GlobalInventoryDateException |
                 GlobalTransactionAlreadyLockedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException | GlJREffectiveDateNoPeriodExistException |
                 GlobalNoApprovalApproverFoundException | GlobalNoApprovalRequesterFoundException |
                 GlobalInvItemLocationNotFoundException | GlobalTransactionAlreadyVoidException |
                 GlobalTransactionAlreadyPostedException | GlobalTransactionAlreadyPendingException |
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

    private void inventoryJournals(Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder, LocalApPurchaseOrderLine apPurchaseOrderLine, LocalAdBranchItemLocation adBranchItemLocation, double PL_AMNT, double PL_TX_AMNT, double ADDON_COST, String DR_CLASS) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean inventoryJournals");

        if (adBranchItemLocation != null) {

            // -- Assign the right debit COA
            Integer debitCoa = (DR_CLASS == "INVENTORY") ? adBranchItemLocation.getBilCoaGlInventoryAccount() : adBranchItemLocation.getBilCoaGlExpenseAccount();

            // Use AdBranchItemLocation Coa Account
            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), DR_CLASS, EJBCommon.TRUE, PL_AMNT + ADDON_COST, debitCoa, apPurchaseOrder, AD_BRNCH, companyCode);

            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "ACC INV", EJBCommon.FALSE, PL_AMNT + PL_TX_AMNT, adBranchItemLocation.getBilCoaGlAccruedInventoryAccount(), apPurchaseOrder, AD_BRNCH, companyCode);

        } else {

            Integer debitCoa = (DR_CLASS == "INVENTORY") ? apPurchaseOrderLine.getInvItemLocation().getIlGlCoaInventoryAccount() : apPurchaseOrderLine.getInvItemLocation().getIlGlCoaExpenseAccount();

            // Use default account
            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), DR_CLASS, EJBCommon.TRUE, PL_AMNT + ADDON_COST, debitCoa, apPurchaseOrder, AD_BRNCH, companyCode);

            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "ACC INV", EJBCommon.FALSE, PL_AMNT + PL_TX_AMNT, apPurchaseOrderLine.getInvItemLocation().getIlGlCoaAccruedInventoryAccount(), apPurchaseOrder, AD_BRNCH, companyCode);
        }
    }

    private double addOnCostJournals(Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder, double PO_TTL_AMNT, double TTL_ADDON_COST, LocalAdBranchItemLocation adBranchItemLocation, double PL_AMNT) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean addOnCostJournals");

        double ADDON_COST = EJBCommon.roundIt((PL_AMNT / PO_TTL_AMNT) * TTL_ADDON_COST, this.getInvGpCostPrecisionUnit(companyCode));

        if (ADDON_COST > 0) {
            this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "ADDON COST", EJBCommon.FALSE, ADDON_COST, adBranchItemLocation.getBilCoaGlAccruedInventoryAccount(), apPurchaseOrder, AD_BRNCH, companyCode);
        }
        return ADDON_COST;
    }

    private void taxJournals(Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder, LocalApTaxCode apTaxCode, LocalAdPreference adPreference, double TOTAL_TAX) throws GlobalBranchAccountNumberInvalidException, FinderException {

        Debug.print("ApReceivingItemEntryControllerBean taxJournals");

        if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {
            if (adPreference.getPrfApUseAccruedVat() == EJBCommon.FALSE) {
                // add branch tax code
                LocalAdBranchApTaxCode adBranchTaxCode = null;
                try {
                    adBranchTaxCode = adBranchApTaxCodeHome.findBtcByTcCodeAndBrCode(apPurchaseOrder.getApTaxCode().getTcCode(), AD_BRNCH, companyCode);
                } catch (FinderException ex) {
                }

                String centralWarehouse = !adPreference.getPrfInvCentralWarehouse().equals("") && adPreference.getPrfInvCentralWarehouse() != null ? adPreference.getPrfInvCentralWarehouse() : "POM WAREHOUSE LOCATION";
                LocalAdBranch centralWarehouseBranchCode = adBranchHome.findByBrName(centralWarehouse, companyCode);

                this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "TAX", EJBCommon.TRUE, TOTAL_TAX, (adBranchTaxCode != null) ? adBranchTaxCode.getBtcGlCoaTaxCode() : apTaxCode.getGlChartOfAccount().getCoaCode(), apPurchaseOrder, (adBranchTaxCode != null) ? AD_BRNCH : centralWarehouseBranchCode.getBrCode(), companyCode);
            } else {

                this.addApDrPlEntry(apPurchaseOrder.getApDrNextLine(), "ACC TAX", EJBCommon.TRUE, TOTAL_TAX, adPreference.getPrfApGlCoaAccruedVatAccount(), apPurchaseOrder, AD_BRNCH, companyCode);
            }
        }
    }

    private LocalAdUser setupInventoryTag(Integer companyCode, LocalAdPreference adPreference, LocalAdUser adUser, ApModPurchaseOrderLineDetails mPlDetails, LocalApPurchaseOrderLine apPurchaseOrderLine, boolean invTagExisting, String serialNumberExisting) throws FinderException, CreateException, InvTagSerialNumberAlreadyExistException {

        Debug.print("ApReceivingItemEntryControllerBean setupInventoryTag");

        try {

            // Iterator t = apPurchaseOrderLine.getInvTag().iterator();
            Iterator t = mPlDetails.getPlTagList().iterator();
            LocalInvItem invItem = invItemHome.findByIiName(mPlDetails.getPlIiName(), companyCode);
            LocalInvTag invTag = null;

            while (t.hasNext()) {

                InvModTagListDetails tgLstDetails = (InvModTagListDetails) t.next();

                // get all inv tags that was already received
                Collection invTagColl = invTagHome.findAllPlReceiving(companyCode);

                // check if the serial number was already received else throw an exception

                // Serial Validation
                for (Object o : invTagColl) {
                    LocalInvTag invExistingTag = (LocalInvTag) o;
                    if (invExistingTag.getApPurchaseOrderLine().getApPurchaseOrder().getPoReceiving() == 1) {

                        if (tgLstDetails.getTgSerialNumber().equals(invExistingTag.getTgSerialNumber())) {

                            invTagExisting = true;
                            serialNumberExisting = tgLstDetails.getTgSerialNumber();
                            throw new InvTagSerialNumberAlreadyExistException(tgLstDetails.getTgSerialNumber());
                        }
                    }
                }

                //

                if (tgLstDetails.getTgCode() == null) {

                    invTag = invTagHome.create(tgLstDetails.getTgPropertyCode(), tgLstDetails.getTgSerialNumber(), null, tgLstDetails.getTgExpiryDate(), tgLstDetails.getTgSpecs(), companyCode, tgLstDetails.getTgTransactionDate(), tgLstDetails.getTgType());

                    invTag.setApPurchaseOrderLine(apPurchaseOrderLine);

                    try {
                        adUser = adUserHome.findByUsrName(tgLstDetails.getTgCustodian(), companyCode);
                    } catch (FinderException ex) {

                    }
                    if (invItem.getIiNonInventoriable() == 1) {
                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber2());
                        // adPreference.setPrfInvNextCustodianNumber2(EJBCommon.incrementStringNumber(adPreference.getPrfInvNextCustodianNumber2()));
                    } else {
                        invTag.setTgDocumentNumber(adPreference.getPrfInvNextCustodianNumber1());
                        // adPreference.setPrfInvNextCustodianNumber1(EJBCommon.incrementStringNumber(adPreference.getPrfInvNextCustodianNumber1()));

                    }

                    invTag.setAdUser(adUser);
                }
            }

        } catch (InvTagSerialNumberAlreadyExistException ex) {

            if (invTagExisting) {
                throw new InvTagSerialNumberAlreadyExistException(serialNumberExisting);
            }
        }
        return adUser;
    }

    private void validatePriorDateTransaction(Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder, LocalAdPreference adPreference, LocalInvItemLocation invItemLocation, ApModPurchaseOrderLineDetails mPlDetails) throws FinderException, GlobalInventoryDateException {

        Debug.print("ApReceivingItemEntryControllerBean validatePriorDateTransaction");

        if (mPlDetails.getPlQuantity() > 0 && adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {

            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            if (!invNegTxnCosting.isEmpty()) {
                throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
            }
        }
    }

    private void removeDistributionRecords(boolean recalculateJournal, LocalApPurchaseOrder apPurchaseOrder) throws RemoveException {

        Debug.print("ApReceivingItemEntryControllerBean removeDistributionRecords");

        Iterator i;
        Collection apDistributionRecords = apPurchaseOrder.getApDistributionRecords();
        i = apDistributionRecords.iterator();
        while (i.hasNext()) {
            LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();
            if (recalculateJournal) {
                i.remove();
                em.remove(apDistributionRecord);
            }
        }
    }

    private void removeReceivingItemLines(LocalApPurchaseOrder apPurchaseOrder) throws RemoveException {

        Debug.print("ApReceivingItemEntryControllerBean removeReceivingItemLines");

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
                em.remove(invTag);
            }
            i.remove();
            em.remove(apPurchaseOrderLine);
        }
    }

    private LocalApPurchaseOrder validatedPoMatched(ApPurchaseOrderDetails details, Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder, LocalApPurchaseOrder apExistingPurchaseOrder) throws GlobalTransactionAlreadyLockedException, GlobalTransactionAlreadyVoidPostedException {

        Debug.print("ApReceivingItemEntryControllerBean validatedPoMatched");

        try {

            apExistingPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(details.getPoRcvPoNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

            if (apPurchaseOrder == null || (apPurchaseOrder != null && !apPurchaseOrder.getPoRcvPoNumber().equals(details.getPoRcvPoNumber()))) {

                if (apExistingPurchaseOrder.getPoLock() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyLockedException();
                }

                apExistingPurchaseOrder.setPoLock(EJBCommon.TRUE);

                if (apPurchaseOrder != null && apPurchaseOrder.getPoRcvPoNumber() != null) {

                    LocalApPurchaseOrder apPreviousPO = null;

                    apPreviousPO = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(apPurchaseOrder.getPoRcvPoNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);

                    apPreviousPO.setPoLock(EJBCommon.FALSE);
                }

                if (apExistingPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {

                    throw new GlobalTransactionAlreadyVoidPostedException();
                }
            }

        } catch (FinderException ex) {

        }
        return apExistingPurchaseOrder;
    }

    private LocalApPurchaseOrder deletedRecord(ApPurchaseOrderDetails details, LocalApPurchaseOrder apPurchaseOrder) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApReceivingItemEntryControllerBean deletedRecord");

        try {
            if (details.getPoCode() != null) {
                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(details.getPoCode());
            }
        } catch (FinderException ex) {
            throw new GlobalRecordAlreadyDeletedException();
        }
        return apPurchaseOrder;
    }

    private void validateDocumentSequence(ApPurchaseOrderDetails details, Integer AD_BRNCH, Integer companyCode, LocalApPurchaseOrder apPurchaseOrder) throws GlobalDocumentNumberNotUniqueException {

        Debug.print("ApReceivingItemEntryControllerBean validateDocumentSequence");
        try {

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;
            if (details.getPoCode() == null) {
                generateDocumentNumber(details, AD_BRNCH, companyCode, adBranchDocumentSequenceAssignment, adDocumentSequenceAssignment);
            } else {
                LocalApPurchaseOrder apExistingReceivingItem = null;
                try {
                    apExistingReceivingItem = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(details.getPoDocumentNumber(), EJBCommon.TRUE, AD_BRNCH, companyCode);
                } catch (FinderException ex) {

                }
                if (apExistingReceivingItem != null && !apExistingReceivingItem.getPoCode().equals(details.getPoCode())) {
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
    }

    private boolean voidRecevingItem(ApPurchaseOrderDetails details, LocalApPurchaseOrder apPurchaseOrder) {

        Debug.print("ApReceivingItemEntryControllerBean voidRecevingItem");

        if (details.getPoCode() != null && details.getPoVoid() == EJBCommon.TRUE && apPurchaseOrder.getPoPosted() == EJBCommon.FALSE) {
            apPurchaseOrder.setPoVoid(EJBCommon.TRUE);
            apPurchaseOrder.setPoLastModifiedBy(details.getPoLastModifiedBy());
            apPurchaseOrder.setPoDateLastModified(details.getPoDateLastModified());
            return true;
        }
        return false;
    }

    private void validateReceivingItemStatus(ApPurchaseOrderDetails details, LocalApPurchaseOrder apPurchaseOrder) throws GlobalTransactionAlreadyApprovedException, GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException {

        Debug.print("ApReceivingItemEntryControllerBean validateReceivingItemStatus");

        if (details.getPoCode() != null) {
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
    }

    private void validatePaymentTerm(String PYT_NM, Integer companyCode) throws FinderException, GlobalPaymentTermInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean validatePaymentTerm");

        if (adPaymentTermHome.findByPytName(PYT_NM, companyCode).getAdPaymentSchedules().isEmpty()) {
            throw new GlobalPaymentTermInvalidException();
        }
    }

    private void validateShipmentNumber(ApPurchaseOrderDetails details, Integer companyCode) throws FinderException, GlobalReferenceNumberNotUniqueException {

        Debug.print("ApReceivingItemEntryControllerBean validateShipmentNumber");

        if (details.getPoShipmentNumber() != null && details.getPoShipmentNumber().length() > 0) {
            Collection apExistingApPurchaseOrderLine = apPurchaseOrderLineHome.findByPoReceivingAndPoShipmentAndBrCode(EJBCommon.FALSE, details.getPoShipmentNumber(), companyCode);
            if (apExistingApPurchaseOrderLine.size() == 0) {
                throw new GlobalReferenceNumberNotUniqueException();
            }
        }
    }

    private void validatedConversionDate(ApPurchaseOrderDetails details, String FC_NM, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ApReceivingItemEntryControllerBean validatedConversionDate");

        try {

            if (details.getPoConversionDate() != null) {
                LocalGlFunctionalCurrency glValidateFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);
                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glValidateFunctionalCurrency.getFcCode(), details.getPoConversionDate(), companyCode);

                details.setPoConversionRate(glFunctionalCurrencyRate.getFrXToUsd());
            }

        } catch (FinderException ex) {
            throw new GlobalConversionDateNotExistException();
        }
    }

    private void generateDocumentNumber(ApPurchaseOrderDetails details, Integer AD_BRNCH, Integer companyCode, LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment, LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment) throws GlobalDocumentNumberNotUniqueException {

        Debug.print("ApReceivingItemEntryControllerBean generateDocumentNumber");

        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("AP RECEIVING ITEM", companyCode);
        } catch (FinderException ex) {
        }
        try {
            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, companyCode);

        } catch (FinderException ex) {
        }

        LocalApPurchaseOrder apExistingReceivingItem = null;
        try {
            apExistingReceivingItem = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(details.getPoDocumentNumber(), EJBCommon.TRUE, AD_BRNCH, companyCode);
        } catch (FinderException ex) {
        }

        if (apExistingReceivingItem != null) {
            throw new GlobalDocumentNumberNotUniqueException();
        }

        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getPoDocumentNumber() == null || details.getPoDocumentNumber().trim().length() == 0)) {
            while (true) {
                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {
                    try {
                        apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), EJBCommon.TRUE, AD_BRNCH, companyCode);
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                    } catch (FinderException ex) {
                        details.setPoDocumentNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                        break;
                    }
                } else {
                    try {
                        apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), EJBCommon.TRUE, AD_BRNCH, companyCode);
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                    } catch (FinderException ex) {
                        details.setPoDocumentNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                        break;
                    }
                }
            }
        }
    }

    public void deleteApPoEntry(Integer PO_CODE, String AD_USR, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ApReceivingItemEntryControllerBean deleteApPoEntry");

        try {

            LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            LocalApPurchaseOrder apExistingPurchaseOrder = null;

            if (apPurchaseOrder.getPoRcvPoNumber() == null) {

                apExistingPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(apPurchaseOrder.getPoDocumentNumber(), EJBCommon.TRUE, AD_BRNCH, companyCode);

            } else {

                apExistingPurchaseOrder = apPurchaseOrderHome.findByPoDocumentNumberAndPoReceivingAndBrCode(apPurchaseOrder.getPoRcvPoNumber(), EJBCommon.FALSE, AD_BRNCH, companyCode);
            }

            apExistingPurchaseOrder.setPoLock(EJBCommon.FALSE);
            apExistingPurchaseOrder.setPoApprovalStatus(null);
            apExistingPurchaseOrder.setPoPosted(EJBCommon.FALSE);
            apExistingPurchaseOrder.setPoPostedBy(null);
            apExistingPurchaseOrder.setPoDatePosted(null);

            adDeleteAuditTrailHome.create("AP RECEIVING ITEM", apPurchaseOrder.getPoDate(), apPurchaseOrder.getPoDocumentNumber(), apPurchaseOrder.getPoReferenceNumber(), 0d, AD_USR, new Date(), companyCode);

            // apPurchaseOrder.entityRemove();
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

    public short getGlFcPrecisionUnit(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdApprovalNotifiedUsersByPoCode(Integer PO_CODE, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdApprovalNotifiedUsersByPoCode");

        ArrayList list = new ArrayList();

        try {

            LocalApPurchaseOrder apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                list.add("DOCUMENT POSTED");
                return list;
            }

            Collection adApprovalQueues = adApprovalQueueHome.findByAqDocumentAndAqDocumentCode("AP RECEIVING ITEM", PO_CODE, companyCode);

            for (Object approvalQueue : adApprovalQueues) {

                LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                list.add(adApprovalQueue.getAdUser().getUsrDescription());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpCostPrecisionUnit(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getInvGpCostPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfInvCostPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfEnableGlJournalBatch(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdPrfApEnableApVoucherBatch");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);

            return adPreference.getPrfEnableGlJournalBatch();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlOpenJbAll(Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getGlOpenJbAll");

        ArrayList list = new ArrayList();

        try {

            Collection glJournalBatches = glJournalBatchHome.findOpenJbAll(AD_BRNCH, companyCode);

            for (Object journalBatch : glJournalBatches) {

                LocalGlJournalBatch glJournalBatch = (LocalGlJournalBatch) journalBatch;

                list.add(glJournalBatch.getJbName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getFrRateByFrNameAndFrDate(String FC_NM, Date CONVERSION_DATE, Integer companyCode) throws GlobalConversionDateNotExistException {

        Debug.print("ApReceivingItemEntryControllerBean getFrRateByFrNameAndFrDate");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(FC_NM, companyCode);

            double CONVERSION_RATE = 1;

            // Get functional currency rate

            if (!FC_NM.equals("USD")) {

                LocalGlFunctionalCurrencyRate glFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(glFunctionalCurrency.getFcCode(), CONVERSION_DATE, companyCode);

                CONVERSION_RATE = glFunctionalCurrencyRate.getFrXToUsd();
            }

            // Get set of book functional currency rate if necessary

            if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, companyCode);

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

    // private methods

    private double getInvFifoCost(Date CST_DT, Integer IL_CODE, double CST_QTY, double CST_COST, boolean isAdjustFifo, Integer AD_BRNCH, Integer companyCode) {

        try {

            Collection invFifoCostings = invCostingHome.findFifoRemainingQuantityByLessThanOrEqualCstDateAndIlCodeAndBrCode(CST_DT, IL_CODE, AD_BRNCH, companyCode);

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

                        // if needed qty is not yet satisfied but no more quantities to fetch, get the
                        // default cost
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
                        return EJBCommon.roundIt(invFifoCosting.getCstItemCost() / invFifoCosting.getCstQuantityReceived(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else if (invFifoCosting.getArInvoiceLineItem() != null) {
                        return EJBCommon.roundIt(invFifoCosting.getCstCostOfSales() / invFifoCosting.getCstQuantitySold(), this.getInvGpCostPrecisionUnit(companyCode));
                    } else {
                        return EJBCommon.roundIt(invFifoCosting.getCstAdjustCost() / invFifoCosting.getCstAdjustQuantity(), this.getInvGpCostPrecisionUnit(companyCode));
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

    private void addApDrPlEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE, LocalApPurchaseOrder apPurchaseOrder, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean addApDrPlEntry");

        try {

            // get company
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalGlChartOfAccount glChartOfAccount = null;
            try {
                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);
                if (glChartOfAccount.getCoaEnable() == EJBCommon.FALSE) {
                    throw new GlobalBranchAccountNumberInvalidException();
                }
            } catch (FinderException ex) {
                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record
            LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.create(DR_LN, DR_CLSS, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_DBT, EJBCommon.FALSE, EJBCommon.FALSE, companyCode);

            apDistributionRecord.setApPurchaseOrder(apPurchaseOrder);
            apDistributionRecord.setGlChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeApReceivingPost(Integer PO_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlobalTransactionAlreadyVoidException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApReceivingItemEntryControllerBean executeApReceivingPost");

        LocalApPurchaseOrder apPurchaseOrder = null;

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            // validate if receiving item/debit memo is already deleted

            try {

                apPurchaseOrder = apPurchaseOrderHome.findByPrimaryKey(PO_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if receiving item is already posted or void

            if (apPurchaseOrder.getPoPosted() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyPostedException();

            } else if (apPurchaseOrder.getPoVoid() == EJBCommon.TRUE) {

                throw new GlobalTransactionAlreadyVoidException();
            }

            // post receiving item

            Collection apPurchaseOrderLines = apPurchaseOrder.getApPurchaseOrderLines();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;
                LocalInvItemLocation invItemLocation = apPurchaseOrderLine.getInvItemLocation();

                if (apPurchaseOrderLine.getPlQuantity() == 0) {
                    continue;
                }

                // start date validation
                if (adPreference.getPrfArAllowPriorDate() == EJBCommon.FALSE) {
                    Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                    if (!invNegTxnCosting.isEmpty()) {
                        throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                    }
                }

                String II_NM = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();
                String LOC_NM = apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName();

                double QTY_RCVD = this.convertByUomFromAndItemAndQuantity(apPurchaseOrderLine.getInvUnitOfMeasure(), apPurchaseOrderLine.getInvItemLocation().getInvItem(), apPurchaseOrderLine.getPlQuantity(), apPurchaseOrderLine.getPlConversionFactor(), companyCode);

                LocalInvCosting invCosting = null;

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(apPurchaseOrder.getPoDate(), II_NM, LOC_NM, AD_BRNCH, companyCode);

                } catch (FinderException ex) {

                }

                double ADDON_COST = apPurchaseOrderLine.getPlAddonCost();
                double COST = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrderLine.getPlUnitCost(), companyCode);

                double AMOUNT = (COST * QTY_RCVD) + ADDON_COST;

                if (invCosting == null) {

                    this.postToInv(apPurchaseOrderLine, apPurchaseOrder.getPoDate(),
                            // QTY_RCVD, AMOUNT,
                            QTY_RCVD, AMOUNT, QTY_RCVD, AMOUNT, 0d, null, AD_BRNCH, companyCode);

                } else {

                    this.postToInv(apPurchaseOrderLine, apPurchaseOrder.getPoDate(), QTY_RCVD, COST * QTY_RCVD, invCosting.getCstRemainingQuantity() + QTY_RCVD, invCosting.getCstRemainingValue() + AMOUNT, 0d, null, AD_BRNCH, companyCode);
                }
            }

            // set receiving item post status

            apPurchaseOrder.setPoPosted(EJBCommon.TRUE);
            apPurchaseOrder.setPoPostedBy(USR_NM);
            apPurchaseOrder.setPoDatePosted(EJBCommon.getGcCurrentDateWoTime().getTime());

            // post to gl if necessary

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(apPurchaseOrder.getPoDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), apPurchaseOrder.getPoDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if receiving item is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection apDistributionRecords = apDistributionRecordHome.findImportableDrByPoCode(apPurchaseOrder.getPoCode(), companyCode);

            Iterator j = apDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                double DR_AMNT = apDistributionRecord.getDrAmount();

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += DR_AMNT;

                } else {

                    TOTAL_CREDIT += DR_AMNT;
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, adCompany.getGlFunctionalCurrency().getFcPrecision());
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, adCompany.getGlFunctionalCurrency().getFcPrecision());

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("ACCOUNTS PAYABLES", "RECEIVING ITEM", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (apDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                // glChartOfAccount.addGlJournalLine(glOffsetJournalLine);
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(apPurchaseOrder.getPoReferenceNumber(), apPurchaseOrder.getPoDescription(), apPurchaseOrder.getPoDate(), 0.0d, null, apPurchaseOrder.getPoDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), apPurchaseOrder.getApSupplier().getSplTin(), apPurchaseOrder.getApSupplier().getSplName(), EJBCommon.FALSE, null, AD_BRNCH, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("ACCOUNTS PAYABLES", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("RECEIVING ITEMS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            // create journal lines

            j = apDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = apDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(apDistributionRecord.getDrLine(), apDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);

                glJournalLine.setGlChartOfAccount(apDistributionRecord.getGlChartOfAccount());
                glJournalLine.setGlJournal(glJournal);
                apDistributionRecord.setDrImported(EJBCommon.TRUE);

                // for FOREX revaluation

                if ((!Objects.equals(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) && glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency() != null && (glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcCode().equals(apPurchaseOrder.getGlFunctionalCurrency().getFcCode()))) {

                    double CONVERSION_RATE = 1;

                    if (apPurchaseOrder.getPoConversionRate() != 0 && apPurchaseOrder.getPoConversionRate() != 1) {

                        CONVERSION_RATE = apPurchaseOrder.getPoConversionRate();

                    } else if (apPurchaseOrder.getPoConversionDate() != null) {

                        CONVERSION_RATE = this.getFrRateByFrNameAndFrDate(glJournalLine.getGlChartOfAccount().getGlFunctionalCurrency().getFcName(), glJournal.getJrConversionDate(), companyCode);
                    }

                    Collection glForexLedgers = null;

                    try {

                        glForexLedgers = glForexLedgerHome.findLatestGlFrlByFrlDateAndByCoaCode(apPurchaseOrder.getPoDate(), glJournalLine.getGlChartOfAccount().getCoaCode(), companyCode);

                    } catch (FinderException ex) {

                    }

                    LocalGlForexLedger glForexLedger = (glForexLedgers.isEmpty() || glForexLedgers == null) ? null : (LocalGlForexLedger) glForexLedgers.iterator().next();

                    int FRL_LN = (glForexLedger != null && glForexLedger.getFrlDate().compareTo(apPurchaseOrder.getPoDate()) == 0) ? glForexLedger.getFrlLine() + 1 : 1;

                    // compute balance
                    double COA_FRX_BLNC = glForexLedger == null ? 0 : glForexLedger.getFrlBalance();
                    double FRL_AMNT = apDistributionRecord.getDrAmount();

                    if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                        FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                    } else {
                        FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                    }

                    COA_FRX_BLNC = COA_FRX_BLNC + FRL_AMNT;

                    glForexLedger = glForexLedgerHome.create(apPurchaseOrder.getPoDate(), FRL_LN, "OTH", FRL_AMNT, CONVERSION_RATE, COA_FRX_BLNC, 0d, companyCode);

                    // glJournalLine.getGlChartOfAccount().addGlForexLedger(glForexLedger);
                    glForexLedger.setGlChartOfAccount(glJournalLine.getGlChartOfAccount());

                    // propagate balances
                    try {

                        glForexLedgers = glForexLedgerHome.findByGreaterThanFrlDateAndCoaCode(glForexLedger.getFrlDate(), glForexLedger.getGlChartOfAccount().getCoaCode(), glForexLedger.getFrlAdCompany());

                    } catch (FinderException ex) {

                    }

                    for (Object forexLedger : glForexLedgers) {

                        glForexLedger = (LocalGlForexLedger) forexLedger;
                        FRL_AMNT = apDistributionRecord.getDrAmount();

                        if (glJournalLine.getGlChartOfAccount().getCoaAccountType().equalsIgnoreCase("ASSET")) {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? FRL_AMNT : (-1 * FRL_AMNT));
                        } else {
                            FRL_AMNT = (glJournalLine.getJlDebit() == EJBCommon.TRUE ? (-1 * FRL_AMNT) : FRL_AMNT);
                        }

                        glForexLedger.setFrlBalance(glForexLedger.getFrlBalance() + FRL_AMNT);
                    }
                }
            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumber(adCompany.getCmpRetainedEarnings(), companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }

        } catch (GlJREffectiveDateNoPeriodExistException | AdPRFCoaGlVarianceAccountNotFoundException |
                 GlobalTransactionAlreadyVoidException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(companyCode);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, this.getInvGpCostPrecisionUnit(companyCode));
    }

    private LocalApPurchaseOrderLine addApPlEntry(ApModPurchaseOrderLineDetails mdetails, LocalApPurchaseOrder apPurchaseOrder, LocalInvItemLocation invItemLocation, boolean newPurchaseOrder, Integer companyCode) throws GlobalMiscInfoIsRequiredException {

        Debug.print("ApReceivingItemEntryControllerBean addApPlEntry");

        try {

            short precisionUnit = this.getGlFcPrecisionUnit(companyCode);

            double VLI_AMNT = 0d;
            double VLI_TAX_AMNT = 0d;

            // calculate net amount

            LocalApTaxCode apTaxCode = apPurchaseOrder.getApTaxCode();
            if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                VLI_AMNT = EJBCommon.roundIt(mdetails.getPlAmount() / (1 + (apTaxCode.getTcRate() / 100)), precisionUnit);

            } else {

                // tax exclusive, none, zero rated or exempt

                VLI_AMNT = mdetails.getPlAmount();
            }

            // calculate tax

            if (!apTaxCode.getTcType().equals("NONE") && !apTaxCode.getTcType().equals("EXEMPT")) {

                if (apTaxCode.getTcType().equals("INCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getPlAmount() - VLI_AMNT, precisionUnit);

                } else if (apTaxCode.getTcType().equals("EXCLUSIVE")) {

                    VLI_TAX_AMNT = EJBCommon.roundIt(mdetails.getPlAmount() * apTaxCode.getTcRate() / 100, precisionUnit);

                } else {

                    // tax none zero-rated or exempt

                }
            }

            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.create(mdetails.getPlLine(), mdetails.getPlQuantity(), mdetails.getPlUnitCost(), VLI_AMNT, mdetails.getPlQcNumber(), mdetails.getPlQcExpiryDate(), mdetails.getPlRemarks(), mdetails.getPlConversionFactor(), VLI_TAX_AMNT, apPurchaseOrder.getPoType().equals("PO MATCHED") ? mdetails.getPlPlCode() : null, mdetails.getPlDiscount1(), mdetails.getPlDiscount2(), mdetails.getPlDiscount3(), mdetails.getPlDiscount4(), mdetails.getPlTotalDiscount(), companyCode);

            double PO_TTL_AMNT = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), apPurchaseOrder.getPoTotalAmount(), companyCode);

            double AMOUNT_LOCAL = this.convertForeignToFunctionalCurrency(apPurchaseOrder.getGlFunctionalCurrency().getFcCode(), apPurchaseOrder.getGlFunctionalCurrency().getFcName(), apPurchaseOrder.getPoConversionDate(), apPurchaseOrder.getPoConversionRate(), VLI_AMNT, companyCode);

            double TTL_ADDON_COST = apPurchaseOrder.getPoDuties() + apPurchaseOrder.getPoFreight() + apPurchaseOrder.getPoEntryFee() + apPurchaseOrder.getPoStorage() + apPurchaseOrder.getPoWharfageHandling();
            double UNIT_COST_LOCAL = EJBCommon.roundIt(mdetails.getPlUnitCost() * apPurchaseOrder.getPoConversionRate(), precisionUnit);
            double ADDON_COST = EJBCommon.roundIt((AMOUNT_LOCAL / PO_TTL_AMNT) * TTL_ADDON_COST, precisionUnit);

            apPurchaseOrderLine.setPlUnitCostLocal(UNIT_COST_LOCAL);
            apPurchaseOrderLine.setPlAmountLocal(AMOUNT_LOCAL);
            apPurchaseOrderLine.setPlAddonCost(ADDON_COST);

            apPurchaseOrderLine.setApPurchaseOrder(apPurchaseOrder);
            apPurchaseOrderLine.setInvItemLocation(invItemLocation);
            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getPlUomName(), companyCode);
            apPurchaseOrderLine.setInvUnitOfMeasure(invUnitOfMeasure);

            // validate misc

            // validate misc

            return apPurchaseOrderLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String checkExpiryDates(String misc, double qty, String reverse) throws Exception {
        // ActionErrors errors = new ActionErrors();
        Debug.print("ApReceivingItemControllerBean getExpiryDates");

        String separator = "";
        if (reverse == "False") {
            separator = "$";
        } else {
            separator = " ";
        }

        // Remove first $ character
        misc = misc.substring(1);

        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();
        String miscList2 = "";

        for (int x = 0; x < qty; x++) {

            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            String g = misc.substring(start, start + length);

            if (g.length() != 0) {
                if (g != null || g != "" || g != "null") {
                    if (g.contains("null")) {
                        miscList2 = "Error";
                    } else {
                        miscList.append("$").append(g);
                    }
                } else {
                    miscList2 = "Error";
                }

            } else {
                miscList2 = "Error";
            }
        }

        if (miscList2 == "") {
            miscList.append("$");
        } else {
            miscList = new StringBuilder(miscList2);
        }

        return (miscList.toString());
    }

    private void postToGl(LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlChartOfAccount glChartOfAccount, boolean isCurrentAcv, byte isDebit, double JL_AMNT, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean postToGl");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), companyCode);

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

    private void postToInv(LocalApPurchaseOrderLine apPurchaseOrderLine, Date CST_DT, double CST_QTY_RCVD, double CST_ITM_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, double CST_VRNC_VL, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

        Debug.print("ApReceivingItemEntryControllerBean postToInv");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);
            LocalInvItemLocation invItemLocation = apPurchaseOrderLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_QTY_RCVD = EJBCommon.roundIt(CST_QTY_RCVD, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ITM_CST = EJBCommon.roundIt(CST_ITM_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            try {

                // generate line number
                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            // void subsequent cost variance adjustments
            Collection invAdjustmentLines = invAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);
            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.voidInvAdjustment(invAdjustmentLine.getInvAdjustment(), AD_BRNCH, companyCode);
            }

            String prevExpiryDates = "";
            String prevQcNumbers = "";

            String miscListPrpgt = "";
            double qtyPrpgt = 0;
            try {
                LocalInvCosting prevCst = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIlCode(CST_DT, invItemLocation.getIlCode(), AD_BRNCH, companyCode);

                prevExpiryDates = prevCst.getCstExpiryDate();
                prevQcNumbers = prevCst.getCstQCNumber();

                qtyPrpgt = prevCst.getCstRemainingQuantity();
                if (prevExpiryDates == null) {
                    prevExpiryDates = "";
                }

                if (prevQcNumbers == null) {
                    prevQcNumbers = "";
                }

            } catch (Exception ex) {
                prevQcNumbers = "";
                prevExpiryDates = "";
            }
            // create costing
            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, CST_QTY_RCVD, CST_ITM_CST, 0d, 0d, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, 0d, AD_BRNCH, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setApPurchaseOrderLine(apPurchaseOrderLine);
            invCosting.setCstQCNumber(apPurchaseOrderLine.getPlQcNumber());
            invCosting.setCstQCExpiryDate(apPurchaseOrderLine.getPlQcExpiryDate());

            invCosting.setCstQuantity(CST_QTY_RCVD);
            invCosting.setCstCost(CST_ITM_CST);

            // Get Latest Expiry Dates

            if (prevExpiryDates != null && prevExpiryDates != "" && prevExpiryDates.length() != 0) {

                if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {

                    int qty2Prpgt = Integer.parseInt(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                    String miscList2Prpgt = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), qty2Prpgt);
                    prevExpiryDates = prevExpiryDates.substring(1);
                    String propagateMiscPrpgt = miscList2Prpgt + prevExpiryDates;

                    invCosting.setCstExpiryDate(propagateMiscPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            } else {
                if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {
                    int initialQty = Integer.parseInt(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                    String initialPrpgt = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), initialQty);

                    invCosting.setCstExpiryDate(initialPrpgt);
                } else {
                    invCosting.setCstExpiryDate(prevExpiryDates);
                }
            }

            // if cost variance is not 0, generate cost variance for the transaction
            if (CST_VRNC_VL != 0) {

                this.generateCostVariance(invCosting.getInvItemLocation(), CST_VRNC_VL, "APRI" + apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber(), apPurchaseOrderLine.getApPurchaseOrder().getPoDescription(), apPurchaseOrderLine.getApPurchaseOrder().getPoDate(), USR_NM, AD_BRNCH, companyCode);
            }

            // propagate balance if necessary

            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            String miscList = "";
            i = invCostings.iterator();
            if (apPurchaseOrderLine.getPlMisc() != null && apPurchaseOrderLine.getPlMisc() != "" && apPurchaseOrderLine.getPlMisc().length() != 0) {
                double qty = Double.parseDouble(this.getQuantityExpiryDates(apPurchaseOrderLine.getPlMisc()));
                miscList = this.propagateExpiryDates(apPurchaseOrderLine.getPlMisc(), qty);
            }
            // miscList = miscList.substring(1);
            while (i.hasNext()) {

                LocalInvCosting invPropagatedCosting = (LocalInvCosting) i.next();

                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_QTY_RCVD);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ITM_CST);
                // String miscList2 =
                // propagateMisc;//this.propagateExpiryDates(invCosting.getCstExpiryDate(),
                // qty);

            }

            // regenerate cost variance
            this.regenerateCostVariance(invCostings, invCosting, AD_BRNCH, companyCode);

        } catch (AdPRFCoaGlVarianceAccountNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double QTY_RCVD, double conversionFactor, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean convertByUomFromAndUomToAndQuantity");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            return EJBCommon.roundIt(QTY_RCVD * conversionFactor, adPreference.getPrfInvQuantityPrecisionUnit());
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getConversionFactorByUomFromAndItem(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getConversionFactorByUomFromAndItem");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), companyCode);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), companyCode);
            return EJBCommon.roundIt(invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getQuantityExpiryDates(String qntty) {

        String separator = "$";
        // Remove first $ character
        qntty = qntty.substring(1);
        // Counter
        int start = 0;
        int nextIndex = qntty.indexOf(separator, start);
        int length = nextIndex - start;
        String y;
        y = (qntty.substring(start, start + length));
        return y;
    }

    public String propagateExpiryDates(String misc, double qty) throws Exception {

        Debug.print("ApReceivingItemControllerBean getExpiryDates");
        String separator = "$";
        // Remove first $ character
        misc = misc.substring(1);
        // Counter
        int start = 0;
        int nextIndex = misc.indexOf(separator, start);
        int length = nextIndex - start;

        StringBuilder miscList = new StringBuilder();
        for (int x = 0; x < qty; x++) {
            // Date
            start = nextIndex + 1;
            nextIndex = misc.indexOf(separator, start);
            length = nextIndex - start;
            try {
                miscList.append("$").append(misc, start, start + length);
            } catch (Exception ex) {
                throw ex;
            }
        }
        miscList.append("$");
        return (miscList.toString());
    }

    private void voidInvAdjustment(LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean voidInvAdjustment");
        try {
            Collection invDistributionRecords = invAdjustment.getInvDistributionRecords();
            ArrayList list = new ArrayList();
            Iterator i = invDistributionRecords.iterator();
            while (i.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();
                list.add(invDistributionRecord);
            }
            i = list.iterator();
            while (i.hasNext()) {
                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) i.next();
                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), invDistributionRecord.getDrClass(), invDistributionRecord.getDrDebit() == EJBCommon.TRUE ? EJBCommon.FALSE : EJBCommon.TRUE, invDistributionRecord.getDrAmount(), EJBCommon.TRUE, invDistributionRecord.getInvChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, companyCode);
            }
            Collection invAjustmentLines = invAdjustment.getInvAdjustmentLines();
            i = invAjustmentLines.iterator();
            list.clear();
            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                list.add(invAdjustmentLine);
            }
            i = list.iterator();
            while (i.hasNext()) {
                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();
                this.addInvAlEntry(invAdjustmentLine.getInvItemLocation(), invAdjustment, (invAdjustmentLine.getAlUnitCost()) * -1, EJBCommon.TRUE, companyCode);
            }
            invAdjustment.setAdjVoid(EJBCommon.TRUE);
            this.executeInvAdjPost(invAdjustment.getAdjCode(), invAdjustment.getAdjLastModifiedBy(), AD_BRNCH, companyCode);
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void generateCostVariance(LocalInvItemLocation invItemLocation, double CST_VRNC_VL, String ADJ_RFRNC_NMBR, String ADJ_DSCRPTN, Date ADJ_DT, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void regenerateCostVariance(Collection invCostings, LocalInvCosting invCosting, Integer AD_BRNCH, Integer companyCode) throws AdPRFCoaGlVarianceAccountNotFoundException {

    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, byte DR_RVRSL, Integer COA_CODE, LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer companyCode) throws GlobalBranchAccountNumberInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, companyCode);

            } catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), DR_RVRSL, EJBCommon.FALSE, companyCode);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void executeInvAdjPost(Integer ADJ_CODE, String USR_NM, Integer AD_BRNCH, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyPostedException, GlJREffectiveDateNoPeriodExistException, GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException, GlobalBranchAccountNumberInvalidException {

        Debug.print("ApReceivingItemEntryControllerBean executeInvAdjPost");

        try {

            // validate if adjustment is already deleted

            LocalInvAdjustment invAdjustment = null;

            try {

                invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // validate if adjustment is already posted or void

            if (invAdjustment.getAdjPosted() == EJBCommon.TRUE) {

                if (invAdjustment.getAdjVoid() != EJBCommon.TRUE) {
                    throw new GlobalTransactionAlreadyPostedException();
                }
            }

            Collection invAdjustmentLines = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);
            } else {
                invAdjustmentLines = invAdjustmentLineHome.findByAlVoidAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator i = invAdjustmentLines.iterator();

            while (i.hasNext()) {

                LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) i.next();

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, companyCode);

                this.postInvAdjustmentToInventory(invAdjustmentLine, invAdjustment.getAdjDate(), 0, invAdjustmentLine.getAlUnitCost(), invCosting.getCstRemainingQuantity(), invCosting.getCstRemainingValue() + invAdjustmentLine.getAlUnitCost(), AD_BRNCH, companyCode);
            }

            // post to gl if necessary

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            // validate if date has no period and period is closed

            LocalGlSetOfBook glJournalSetOfBook = null;

            try {

                glJournalSetOfBook = glSetOfBookHome.findByDate(invAdjustment.getAdjDate(), companyCode);

            } catch (FinderException ex) {

                throw new GlJREffectiveDateNoPeriodExistException();
            }

            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndDate(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), invAdjustment.getAdjDate(), companyCode);

            if (glAccountingCalendarValue.getAcvStatus() == 'N' || glAccountingCalendarValue.getAcvStatus() == 'C' || glAccountingCalendarValue.getAcvStatus() == 'P') {

                throw new GlJREffectiveDatePeriodClosedException();
            }

            // check if invoice is balance if not check suspense posting

            LocalGlJournalLine glOffsetJournalLine = null;

            Collection invDistributionRecords = null;

            if (invAdjustment.getAdjVoid() == EJBCommon.FALSE) {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.FALSE, invAdjustment.getAdjCode(), companyCode);

            } else {

                invDistributionRecords = invDistributionRecordHome.findImportableDrByDrReversedAndAdjCode(EJBCommon.TRUE, invAdjustment.getAdjCode(), companyCode);
            }

            Iterator j = invDistributionRecords.iterator();

            double TOTAL_DEBIT = 0d;
            double TOTAL_CREDIT = 0d;

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                if (invDistributionRecord.getDrDebit() == EJBCommon.TRUE) {

                    TOTAL_DEBIT += DR_AMNT;

                } else {

                    TOTAL_CREDIT += DR_AMNT;
                }
            }

            TOTAL_DEBIT = EJBCommon.roundIt(TOTAL_DEBIT, this.getGlFcPrecisionUnit(companyCode));
            TOTAL_CREDIT = EJBCommon.roundIt(TOTAL_CREDIT, this.getGlFcPrecisionUnit(companyCode));

            if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.TRUE && TOTAL_DEBIT != TOTAL_CREDIT) {

                LocalGlSuspenseAccount glSuspenseAccount = null;

                try {

                    glSuspenseAccount = glSuspenseAccountHome.findByJsNameAndJcName("INVENTORY", "INVENTORY ADJUSTMENTS", companyCode);

                } catch (FinderException ex) {

                    throw new GlobalJournalNotBalanceException();
                }

                if (TOTAL_DEBIT - TOTAL_CREDIT < 0) {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.TRUE, TOTAL_CREDIT - TOTAL_DEBIT, "", companyCode);

                } else {

                    glOffsetJournalLine = glJournalLineHome.create((short) (invDistributionRecords.size() + 1), EJBCommon.FALSE, TOTAL_DEBIT - TOTAL_CREDIT, "", companyCode);
                }

                LocalGlChartOfAccount glChartOfAccount = glSuspenseAccount.getGlChartOfAccount();
                glOffsetJournalLine.setGlChartOfAccount(glChartOfAccount);

            } else if (adPreference.getPrfAllowSuspensePosting() == EJBCommon.FALSE && TOTAL_DEBIT != TOTAL_CREDIT) {

                throw new GlobalJournalNotBalanceException();
            }

            // create journal batch if necessary

            LocalGlJournalBatch glJournalBatch = null;
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");

            try {

                glJournalBatch = glJournalBatchHome.findByJbName("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", AD_BRNCH, companyCode);

            } catch (FinderException ex) {

            }

            if (glJournalBatch == null) {

                glJournalBatch = glJournalBatchHome.create("JOURNAL IMPORT " + formatter.format(new Date()) + " INVENTORY ADJUSTMENTS", "JOURNAL IMPORT", "CLOSED", EJBCommon.getGcCurrentDateWoTime().getTime(), USR_NM, AD_BRNCH, companyCode);
            }

            // create journal entry

            LocalGlJournal glJournal = glJournalHome.create(invAdjustment.getAdjReferenceNumber(), invAdjustment.getAdjDescription(), invAdjustment.getAdjDate(), 0.0d, null, invAdjustment.getAdjDocumentNumber(), null, 1d, "N/A", null, 'N', EJBCommon.TRUE, EJBCommon.FALSE, USR_NM, new Date(), USR_NM, new Date(), null, null, USR_NM, EJBCommon.getGcCurrentDateWoTime().getTime(), null, null, EJBCommon.FALSE, null, AD_BRNCH, companyCode);

            LocalGlJournalSource glJournalSource = glJournalSourceHome.findByJsName("INVENTORY", companyCode);
            glJournal.setGlJournalSource(glJournalSource);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adCompany.getGlFunctionalCurrency().getFcName(), companyCode);
            glJournal.setGlFunctionalCurrency(glFunctionalCurrency);

            LocalGlJournalCategory glJournalCategory = glJournalCategoryHome.findByJcName("INVENTORY ADJUSTMENTS", companyCode);
            glJournal.setGlJournalCategory(glJournalCategory);

            if (glJournalBatch != null) {

                glJournal.setGlJournalBatch(glJournalBatch);
            }

            // create journal lines

            j = invDistributionRecords.iterator();

            while (j.hasNext()) {

                LocalInvDistributionRecord invDistributionRecord = (LocalInvDistributionRecord) j.next();

                double DR_AMNT = 0d;

                DR_AMNT = invDistributionRecord.getDrAmount();

                LocalGlJournalLine glJournalLine = glJournalLineHome.create(invDistributionRecord.getDrLine(), invDistributionRecord.getDrDebit(), DR_AMNT, "", companyCode);
                glJournalLine.setGlChartOfAccount(invDistributionRecord.getInvChartOfAccount());
                glJournalLine.setGlJournal(glJournal);

                invDistributionRecord.setDrImported(EJBCommon.TRUE);
            }

            if (glOffsetJournalLine != null) {

                glOffsetJournalLine.setGlJournal(glJournal);
            }

            // post journal to gl

            Collection glJournalLines = glJournalLineHome.findByJrCode(glJournal.getJrCode(), companyCode);
            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // post current to current acv

                this.postToGl(glAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), true, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                // post to subsequent acvs (propagate)

                Collection glSubsequentAccountingCalendarValues = glAccountingCalendarValueHome.findSubsequentAcvByAcCodeAndAcvPeriodNumber(glJournalSetOfBook.getGlAccountingCalendar().getAcCode(), glAccountingCalendarValue.getAcvPeriodNumber(), companyCode);

                for (Object subsequentAccountingCalendarValue : glSubsequentAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) subsequentAccountingCalendarValue;

                    this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                }

                // post to subsequent years if necessary

                Collection glSubsequentSetOfBooks = glSetOfBookHome.findSubsequentSobByAcYear(glJournalSetOfBook.getGlAccountingCalendar().getAcYear(), companyCode);

                if (!glSubsequentSetOfBooks.isEmpty() && glJournalSetOfBook.getSobYearEndClosed() == 1) {

                    adCompany = adCompanyHome.findByPrimaryKey(companyCode);
                    LocalGlChartOfAccount glRetainedEarningsAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(adCompany.getCmpRetainedEarnings(), AD_BRNCH, companyCode);

                    for (Object subsequentSetOfBook : glSubsequentSetOfBooks) {

                        LocalGlSetOfBook glSubsequentSetOfBook = (LocalGlSetOfBook) subsequentSetOfBook;

                        String ACCOUNT_TYPE = glJournalLine.getGlChartOfAccount().getCoaAccountType();

                        // post to subsequent acvs of subsequent set of book(propagate)

                        Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCode(glSubsequentSetOfBook.getGlAccountingCalendar().getAcCode(), companyCode);

                        for (Object accountingCalendarValue : glAccountingCalendarValues) {

                            LocalGlAccountingCalendarValue glSubsequentAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                            if (ACCOUNT_TYPE.equals("ASSET") || ACCOUNT_TYPE.equals("LIABILITY") || ACCOUNT_TYPE.equals("OWNERS EQUITY")) {

                                this.postToGl(glSubsequentAccountingCalendarValue, glJournalLine.getGlChartOfAccount(), false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);

                            } else { // revenue & expense

                                this.postToGl(glSubsequentAccountingCalendarValue, glRetainedEarningsAccount, false, glJournalLine.getJlDebit(), glJournalLine.getJlAmount(), companyCode);
                            }
                        }

                        if (glSubsequentSetOfBook.getSobYearEndClosed() == 0) {
                            break;
                        }
                    }
                }
            }

            invAdjustment.setAdjPosted(EJBCommon.TRUE);

        } catch (GlJREffectiveDateNoPeriodExistException | GlobalTransactionAlreadyPostedException |
                 GlobalRecordAlreadyDeletedException | GlobalJournalNotBalanceException |
                 GlJREffectiveDatePeriodClosedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvItemLocation invItemLocation, LocalInvAdjustment invAdjustment, double CST_VRNC_VL, byte AL_VD, Integer companyCode) {

        Debug.print("ArMiscReceiptEntryControllerBean addInvAlEntry");

        try {

            // create dr entry
            LocalInvAdjustmentLine invAdjustmentLine = null;
            invAdjustmentLine = invAdjustmentLineHome.create(CST_VRNC_VL, null, null, 0, 0, AL_VD, companyCode);

            // map adjustment, unit of measure, item location
            invAdjustmentLine.setInvAdjustment(invAdjustment);
            invAdjustmentLine.setInvUnitOfMeasure(invItemLocation.getInvItem().getInvUnitOfMeasure());
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void postInvAdjustmentToInventory(LocalInvAdjustmentLine invAdjustmentLine, Date CST_DT, double CST_ADJST_QTY, double CST_ADJST_CST, double CST_RMNNG_QTY, double CST_RMNNG_VL, Integer AD_BRNCH, Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean postInvAdjustmentToInventory");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(companyCode);
            LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();
            int CST_LN_NMBR = 0;

            CST_ADJST_QTY = EJBCommon.roundIt(CST_ADJST_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_ADJST_CST = EJBCommon.roundIt(CST_ADJST_CST, adPreference.getPrfInvCostPrecisionUnit());
            CST_RMNNG_QTY = EJBCommon.roundIt(CST_RMNNG_QTY, adPreference.getPrfInvQuantityPrecisionUnit());
            CST_RMNNG_VL = EJBCommon.roundIt(CST_RMNNG_VL, adPreference.getPrfInvCostPrecisionUnit());

            if (CST_ADJST_QTY < 0) {

                invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - Math.abs(CST_ADJST_QTY));
            }

            // create costing

            try {

                // generate line number

                LocalInvCosting invCurrentCosting = invCostingHome.getByMaxCstLineNumberAndCstDateToLongAndIiNameAndLocName(CST_DT.getTime(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
                CST_LN_NMBR = invCurrentCosting.getCstLineNumber() + 1;

            } catch (FinderException ex) {

                CST_LN_NMBR = 1;
            }

            LocalInvCosting invCosting = invCostingHome.create(CST_DT, CST_DT.getTime(), CST_LN_NMBR, 0d, 0d, CST_ADJST_QTY, CST_ADJST_CST, 0d, 0d, CST_RMNNG_QTY, CST_RMNNG_VL, 0d, 0d, CST_ADJST_QTY > 0 ? CST_ADJST_QTY : 0, AD_BRNCH, companyCode);
            invCosting.setInvItemLocation(invItemLocation);
            invCosting.setInvAdjustmentLine(invAdjustmentLine);
            invCosting.setCstQuantity(CST_ADJST_QTY);
            invCosting.setCstCost(CST_ADJST_CST);

            // propagate balance if necessary
            Collection invCostings = invCostingHome.findByGreaterThanCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, companyCode);
            for (Object costing : invCostings) {
                LocalInvCosting invPropagatedCosting = (LocalInvCosting) costing;
                invPropagatedCosting.setCstRemainingQuantity(invPropagatedCosting.getCstRemainingQuantity() + CST_ADJST_QTY);
                invPropagatedCosting.setCstRemainingValue(invPropagatedCosting.getCstRemainingValue() + CST_ADJST_CST);
            }
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvPurchaseRequisitionMisc1(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc1");
        ArrayList list = new ArrayList();
        try {
            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC1", companyCode);
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

    public ArrayList getAdLvPurchaseRequisitionMisc2(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc2");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC2", companyCode);

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

    public ArrayList getAdLvPurchaseRequisitionMisc3(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc3");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC3", companyCode);

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

    public ArrayList getAdLvPurchaseRequisitionMisc4(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc4");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC4", companyCode);

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

    public ArrayList getAdLvPurchaseRequisitionMisc5(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc5");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC5", companyCode);

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

    public ArrayList getAdLvPurchaseRequisitionMisc6(Integer companyCode) {

        Debug.print("ApReceivingItemEntryControllerBean getAdLvPurchaseRequisitionMisc6");
        ArrayList list = new ArrayList();
        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("AP PURCHASE REQUISITION MISC6", companyCode);
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

    private ArrayList getInvTagList(LocalApPurchaseOrderLine apPurchaseOrderLine) {

        Debug.print("ApReceivingItemEntryControllerBean getInvTagList");
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

        Debug.print("ApReceivingItemEntryControllerBean ejbCreate");
    }

}