/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepAdjustmentPrintControllerBean
 * @created September 19,2006 2:58 PM
 * @author Rey B. Limosenero
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.inv.LocalInvAdjustmentHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepAdjustmentPrintDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "InvRepAdjustmentPrintControllerEJB")
public class InvRepAdjustmentPrintControllerBean extends EJBContextClass implements InvRepAdjustmentPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;

    public ArrayList executeInvRepAdjustmentPrint(ArrayList adjCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepAdjustmentPrintControllerBean executeInvRepAdjustmentPrint");
        ArrayList list = new ArrayList();
        try {

            for (Object value : adjCodeList) {

                Integer ADJ_CODE = (Integer) value;
                LocalInvAdjustment invAdjustment;
                try {
                    invAdjustment = invAdjustmentHome.findByPrimaryKey(ADJ_CODE);
                } catch (FinderException ex) {
                    continue;
                }

                // get adjustment lines
                Collection invAdjustmentLines = invAdjustment.getInvAdjustmentLines();
                for (Object adjustmentLine : invAdjustmentLines) {
                    double COST = 0d;
                    LocalInvAdjustmentLine invAdjustmentLine = (LocalInvAdjustmentLine) adjustmentLine;

                    boolean isService = invAdjustmentLine.getInvItemLocation().getInvItem().getIiAdLvCategory().startsWith("Service");
                    if (!invAdjustmentLine.getInvTags().isEmpty() && invAdjustment.getAdjType().equals("GENERAL") && invAdjustmentLine.getInvItemLocation().getInvItem().getIiTraceMisc() == 1) {

                        Collection invTags = invAdjustmentLine.getInvTags();
                        for (Object tag : invTags) {

                            InvRepAdjustmentPrintDetails details = new InvRepAdjustmentPrintDetails();
                            details.setApAlIsService(isService);
                            details.setApAlIsInventoriable(invAdjustmentLine.getInvItemLocation().getInvItem().getIiNonInventoriable() != 1);
                            details.setApAdjType(invAdjustment.getAdjType());
                            details.setApAdjDate(invAdjustment.getAdjDate());
                            details.setApAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                            details.setApAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
                            details.setApAdjDescription(invAdjustment.getAdjDescription());
                            details.setApAdjGlCoaAccount(invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
                            details.setApAdjPosted(invAdjustment.getAdjPosted());

                            Collection BrList = genValueSetValueHome.findByVsName("BRANCH", AD_CMPNY);
                            String sample = invAdjustment.getGlChartOfAccount().getCoaAccountDescription();

                            for (Object o : BrList) {
                                LocalGenValueSetValue glvsv = (LocalGenValueSetValue) o;
                                if (sample.contains(glvsv.getVsvDescription() + "-")) {
                                    sample = glvsv.getVsvDescription();
                                    break;
                                }
                            }

                            details.setApAdjGlCoaAccountDesc(sample);
                            try {
                                details.setApAdjSupplierName(invAdjustment.getApSupplier().getSplName());
                                details.setApAdjSupplierAddress(invAdjustment.getApSupplier().getSplAddress());
                            } catch (Exception ex) {
                                details.setApAdjSupplierName("");
                                details.setApAdjSupplierAddress("");
                            }
                            details.setApAlBranchCode(invAdjustment.getAdjAdBranch());
                            details.setApAdjPostedBy(invAdjustment.getAdjPostedBy());
                            // details.setApAlBranchName(invAdjustment.getAdjAdBranch().getBrName());
                            details.setApAdjApprovedBy(invAdjustment.getAdjApprovedRejectedBy());

                            LocalAdUser adUser = adUserHome.findByUsrName(invAdjustment.getAdjCreatedBy(), AD_CMPNY);
                            details.setApAdjCreatedBy(adUser.getUsrDescription());
                            details.setApAdjCreatedByPosition(adUser.getUsrPosition());
                            details.setApAlIiName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                            details.setApAlIiDescription(invAdjustmentLine.getInvItemLocation().getInvItem().getIiDescription());
                            details.setApAlLocName(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                            details.setApAlUomName(invAdjustmentLine.getInvUnitOfMeasure().getUomName());
                            details.setApAlAdjustQuantity(invAdjustmentLine.getAlAdjustQuantity());
                            details.setApAlUnitCost(invAdjustmentLine.getAlUnitCost());
                            details.setApAdjNotedBy(invAdjustment.getAdjNotedBy());

                            LocalInvCosting invLastCosting;
                            try {
                                invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getIlCode(), invAdjustmentLine.getInvAdjustment().getAdjAdBranch(), AD_CMPNY);
                                COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());
                            } catch (Exception e) {
                                COST = 0;
                            }

                            // details.setApAlAveCost(COST);
                            details.setApAlAveCost(invAdjustmentLine.getAlUnitCost());

                            LocalInvTag invTag = (LocalInvTag) tag;
                            try {
                                details.setApTgCustodian(invTag.getAdUser().getUsrDescription());
                                details.setApTgCustodianPosition(invTag.getAdUser().getUsrPosition());
                            } catch (Exception ex) {
                                details.setApTgCustodian("");
                                details.setApTgCustodianPosition("");
                            }

                            details.setApTgDocumentNumber(invTag.getTgDocumentNumber());
                            details.setApTgExpiryDate(invTag.getTgExpiryDate());
                            details.setApTgPropertyCode(invTag.getTgPropertyCode());
                            details.setApTgSerialNumber(invTag.getTgSerialNumber());
                            details.setApTgSpecs(invTag.getTgSpecs());
                            list.add(details);
                        }

                    } else {

                        InvRepAdjustmentPrintDetails details = new InvRepAdjustmentPrintDetails();
                        details.setApAdjType(invAdjustment.getAdjType());
                        details.setApAdjDate(invAdjustment.getAdjDate());
                        details.setApAdjDocumentNumber(invAdjustment.getAdjDocumentNumber());
                        details.setApAdjReferenceNumber(invAdjustment.getAdjReferenceNumber());
                        details.setApAdjDescription(invAdjustment.getAdjDescription());
                        details.setApAdjGlCoaAccount(invAdjustment.getGlChartOfAccount().getCoaAccountNumber());
                        // String x = invAdjustment.getGlChartOfAccount().getCoaAccountDescription();

                        Collection BrList = genValueSetValueHome.findByVsName("BRANCH", AD_CMPNY);

                        String sample = invAdjustment.getGlChartOfAccount().getCoaAccountDescription();

                        for (Object o : BrList) {
                            LocalGenValueSetValue glvsv = (LocalGenValueSetValue) o;
                            if (sample.contains(glvsv.getVsvDescription() + "-")) {
                                Debug.print(glvsv.getVsvDescription());
                                sample = glvsv.getVsvDescription();
                                break;
                            }
                        }

                        details.setApAdjGlCoaAccountDesc(sample);
                        details.setApAlBranchCode(invAdjustment.getAdjAdBranch());
                        try {
                            details.setApAdjSupplierName(invAdjustment.getApSupplier().getSplName());
                            details.setApAdjSupplierAddress(invAdjustment.getApSupplier().getSplAddress());
                        } catch (Exception ex) {
                            details.setApAdjSupplierName("");
                            details.setApAdjSupplierAddress("");
                        }

                        details.setApAdjPostedBy(invAdjustment.getAdjPostedBy());
                        // details.setApAlBranchName(invAdjustment.getAdjAdBranch().getBrName());
                        details.setApAdjApprovedBy(invAdjustment.getAdjApprovedRejectedBy());
                        LocalAdUser adUser = adUserHome.findByUsrName(invAdjustment.getAdjCreatedBy(), AD_CMPNY);
                        details.setApAdjCreatedBy(adUser.getUsrDescription());
                        details.setApAdjCreatedByPosition(adUser.getUsrPosition());
                        details.setApAlIiName(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName());
                        details.setApAlIiDescription(invAdjustmentLine.getInvItemLocation().getInvItem().getIiDescription());
                        details.setApAlLocName(invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName());
                        details.setApAlUomName(invAdjustmentLine.getInvUnitOfMeasure().getUomName());
                        details.setApAlAdjustQuantity(invAdjustmentLine.getAlAdjustQuantity());
                        details.setApAlUnitCost(invAdjustmentLine.getAlUnitCost());
                        details.setApAdjNotedBy(invAdjustment.getAdjNotedBy());

                        LocalInvCosting invLastCosting = null;
                        try {
                            invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(invAdjustment.getAdjDate(), invAdjustmentLine.getInvItemLocation().getIlCode(), invAdjustmentLine.getInvAdjustment().getAdjAdBranch(), AD_CMPNY);
                            COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());
                        } catch (Exception e) {
                            COST = 0;
                        }
                        // details.setApAlAveCost(COST);
                        details.setApAlAveCost(invAdjustmentLine.getAlUnitCost());
                        list.add(details);
                    }
                }
            }
            if (list.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            list.sort(InvRepAdjustmentPrintDetails.ItemComparator);
            return list;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public boolean getInvTraceMisc(String II_NAME, Integer AD_CMPNY) {

        Debug.print("InvAdjustmentEntryControllerBean getInvLocAll");
        boolean isTraceMisc = false;

        try {
            LocalInvItem invItem = invItemHome.findByIiName(II_NAME, AD_CMPNY);
            if (invItem.getIiTraceMisc() == 1) {
                isTraceMisc = true;
            }
            return isTraceMisc;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public String getPrfApDefaultChecker(java.lang.Integer AD_CMPNY) {

        Debug.print("InvAdjustmentEntryControllerBean getPrfApDefaultChecker");
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfApDefaultChecker();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepAdjustmentPrintControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpPhone(adCompany.getCmpPhone());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepAdjustmentPrintControllerBean ejbCreate");
    }

}