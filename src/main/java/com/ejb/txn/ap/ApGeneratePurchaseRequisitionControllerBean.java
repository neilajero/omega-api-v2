/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApGeneratePurchaseRequisitionControllerBean
 * @created April 12, 2006 1:00 PM
 * @author Aliza D.J. Anos
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
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.dao.ap.LocalApCanvassHome;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.dao.ap.LocalApPurchaseRequisitionLineHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.util.mod.ad.AdModBranchItemLocationDetails;
import com.util.mod.ap.ApModPurchaseRequisitionDetails;
import com.util.mod.ap.ApModPurchaseRequisitionLineDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApGeneratePurchaseRequisitionControllerEJB")
public class ApGeneratePurchaseRequisitionControllerBean extends EJBContextClass implements ApGeneratePurchaseRequisitionController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
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
    private LocalApCanvassHome apCanvassHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApPurchaseRequisitionLineHome apPurchaseRequisitionLineHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalInvCostingHome invReorderItemCostHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;


    public ArrayList getAdBrAll(Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getAdBrAll");

        Collection adBranches = null;
        ArrayList list = new ArrayList();


        try {

            adBranches = adBranchHome.findBrAll(AD_CMPNY);

            if (adBranches.isEmpty()) {

                return null;
            }

            for (Object adBranch : adBranches) {

                LocalAdBranch AdBranch = (LocalAdBranch) adBranch;
                String details = AdBranch.getBrName();

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApGenPRLinesByCriteria(HashMap criteria, String ORDER_BY, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getApGenPRLinesByCriteria");

        ArrayList list = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bil) FROM AdBranchItemLocation bil ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(" WHERE bil.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    LocalAdBranch adBranch = adBranchHome.findByBrName((String) o, AD_CMPNY);
                    jbossQl.append(adBranch.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("itemCategory")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bil.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemCategory");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bil.bilReorderPoint > 0 AND bil.bilAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "ITEM NAME":

                    orderBy = "bil.invItemLocation.invItem.iiName";

                    break;
                case "ITEM DESCRIPTION":

                    orderBy = "bil.invItemLocation.invItem.iiDescription";

                    break;
                case "ITEM CATEGORY":

                    orderBy = "bil.invItemLocation.invItem.iiAdLvCategory";

                    break;
                case "ITEM CLASS":

                    orderBy = "bil.invItemLocation.invItem.iiClass";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection adBranchItemLocations = null;

            try {

                adBranchItemLocations = adBranchItemLocationHome.getBilByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object branchItemLocation : adBranchItemLocations) {

                LocalAdBranchItemLocation adBranchItemLocation = (LocalAdBranchItemLocation) branchItemLocation;

                AdModBranchItemLocationDetails details = new AdModBranchItemLocationDetails();

                details.setBilIlIiName(adBranchItemLocation.getInvItemLocation().getInvItem().getIiName());
                details.setBilIlIiDescription(adBranchItemLocation.getInvItemLocation().getInvItem().getIiDescription());
                details.setBilIlIiClass(adBranchItemLocation.getInvItemLocation().getInvItem().getIiClass());
                details.setBilIlLocName(adBranchItemLocation.getInvItemLocation().getInvLocation().getLocName());
                details.setBilReorderPoint(adBranchItemLocation.getBilReorderPoint());
                details.setBilReorderQuantity(adBranchItemLocation.getBilReorderQuantity());
                details.setBilBrName(adBranchItemLocation.getAdBranch().getBrBranchCode());
                details.setBilBrCode(adBranchItemLocation.getAdBranch().getBrCode());

                double quantity = 0d;

                try {

                    LocalInvCosting invReorderItemCost = invReorderItemCostHome.getByMaxCstDateToLongAndMaxCstLineNumberAndIiNameAndLocName(details.getBilIlIiName(), details.getBilIlLocName(), adBranchItemLocation.getAdBranch().getBrCode(), AD_CMPNY);

                    quantity = invReorderItemCost.getCstRemainingQuantity();

                } catch (FinderException ex) {

                    quantity = 0;
                }
                details.setBilQuantityOnHand(quantity);
                details.setBilQuantity(adBranchItemLocation.getBilReorderQuantity() - quantity);
                if (quantity <= details.getBilReorderPoint()) {

                    list.add(details);
                }
            }

            if (list.isEmpty() || list.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

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

    public Integer generateApPurchaseRequisition(ApModPurchaseRequisitionDetails mdetails, ArrayList prlList, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean generateApPurchaseRequisition");

        try {

            // validate if document number is unique document number is automatic then set next sequence

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

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

                            mdetails.setPrNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            apPurchaseRequisitionHome.findByPrNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        } catch (FinderException ex) {

                            mdetails.setPrNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // create purchase requisition
            LocalApPurchaseRequisition apPurchaseRequisition = apPurchaseRequisitionHome.create(mdetails.getPrDescription(), mdetails.getPrNumber(), mdetails.getPrDate(), mdetails.getPrDeliveryPeriod(), mdetails.getPrReferenceNumber(), mdetails.getPrConversionDate(), mdetails.getPrConversionRate(), null, EJBCommon.FALSE, EJBCommon.FALSE, null, EJBCommon.FALSE, EJBCommon.FALSE, null, null, mdetails.getPrCreatedBy(), mdetails.getPrDateCreated(), mdetails.getPrCreatedBy(), mdetails.getPrDateCreated(), null, null, null, null, null, null, null, null, mdetails.getPrTagStatus(), mdetails.getPrType(), null, null, null, null, AD_BRNCH, AD_CMPNY);

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(adPreference.getPrfApDefaultPrTax(), AD_CMPNY);
            apPurchaseRequisition.setApTaxCode(apTaxCode);

            LocalGlFunctionalCurrency glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(adPreference.getPrfApDefaultPrCurrency(), AD_CMPNY);
            apPurchaseRequisition.setGlFunctionalCurrency(glFunctionalCurrency);

            // add purchase requisition lines

            for (Object o : prlList) {

                ApModPurchaseRequisitionLineDetails prlDetails = (ApModPurchaseRequisitionLineDetails) o;

                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.create(prlDetails.getPrlLine(), 0d, 0d, null, AD_CMPNY);
                apPurchaseRequisition.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(prlDetails.getPrlIlIiName(), prlDetails.getPrlIlLocName(), AD_CMPNY);
                invItemLocation.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                apPurchaseRequisitionLine.setPrlQuantity(prlDetails.getPrlQuantity());

                LocalInvUnitOfMeasure invUnitOfMeasure = invItemLocation.getInvItem().getInvUnitOfMeasure();
                invUnitOfMeasure.addApPurchaseRequisitionLine(apPurchaseRequisitionLine);

                // add canvass lines
                double REORDR_QTY = apPurchaseRequisitionLine.getPrlQuantity();

                LocalInvItem invItem = invItemLocation.getInvItem();

                double II_UNT_CST = 0d;

                if (invItem.getApSupplier() != null) {

                    // if item has supplier retrieve last purchase price

                    II_UNT_CST = this.getInvLastPurchasePriceBySplSupplierCode(invItemLocation, invItem.getApSupplier().getSplSupplierCode(), AD_BRNCH, AD_CMPNY);

                } else {

                    // if item has no supplier retrieve last purchase price

                    II_UNT_CST = this.getInvLastPurchasePrice(invItemLocation, AD_BRNCH, AD_CMPNY);
                }

                if (II_UNT_CST == 0) {

                    II_UNT_CST = invItemLocation.getInvItem().getIiUnitCost();
                }

                apPurchaseRequisitionLine.setPrlAmount(REORDR_QTY * II_UNT_CST);

                // create canvass line for item's default supplier

                short CNV_LN = 0;

                if (invItemLocation.getInvItem().getApSupplier() != null) {

                    CNV_LN++;

                    // create canvass line
                    LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, REORDR_QTY, II_UNT_CST, EJBCommon.roundIt(REORDR_QTY * II_UNT_CST, this.getGlFcPrecisionUnit(AD_CMPNY)), EJBCommon.TRUE, AD_CMPNY);

                    LocalApSupplier apSupplier = invItemLocation.getInvItem().getApSupplier();
                    apSupplier.addApCanvass(apCanvass);
                    apPurchaseRequisitionLine.addApCanvass(apCanvass);
                }

                CNV_LN++;

                // create canvass lines for other suppliers of item
                Collection apPurchaseOrderLines = null;

                try {

                    apPurchaseOrderLines = apPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(apPurchaseRequisitionLine.getInvItemLocation().getIlCode(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                if (!apPurchaseOrderLines.isEmpty() && apPurchaseOrderLines.size() > 0) {

                    for (Object purchaseOrderLine : apPurchaseOrderLines) {

                        LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                        // continue if next purchase order line has existing supplier in canvass

                        Collection apCanvasses = apCanvassHome.findByPrlCodeAndSplSupplierCode(apPurchaseRequisitionLine.getPrlCode(), apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplSupplierCode(), AD_CMPNY);

                        if (!apCanvasses.isEmpty()) continue;

                        // convert purchase order line unit cost
                        double POL_UNT_CST = this.convertCostByUom(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);

                        // create canvass line
                        LocalApCanvass apCanvass = apCanvassHome.create(CNV_LN, null, 0d, POL_UNT_CST, 0d, EJBCommon.FALSE, AD_CMPNY);

                        if (CNV_LN == 1) {

                            apCanvass.setCnvQuantity(REORDR_QTY);
                            apCanvass.setCnvAmount(EJBCommon.roundIt(REORDR_QTY * II_UNT_CST, this.getGlFcPrecisionUnit(AD_CMPNY)));
                            apCanvass.setCnvPo(EJBCommon.TRUE);
                        }

                        LocalApSupplier apSupplier = apPurchaseOrderLine.getApPurchaseOrder().getApSupplier();
                        apSupplier.addApCanvass(apCanvass);

                        apPurchaseRequisitionLine.addApCanvass(apCanvass);

                        CNV_LN++;
                    }
                }
            }

            return apPurchaseRequisition.getPrCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double getInvLastPurchasePriceBySplSupplierCode(LocalInvItemLocation invItemLocation, String SPL_SPPLR_CODE, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getInvLastPurchasePriceBySplSupplierCode");

        try {

            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPostedAndSplSpplrCode(invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), EJBCommon.TRUE, EJBCommon.TRUE, invItemLocation.getInvItem().getApSupplier().getSplSupplierCode(), AD_BRNCH, AD_CMPNY);

            return this.convertCostByUom(invItemLocation.getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);

        } catch (FinderException ex) {

            return 0d;
        }
    }

    private double getInvLastPurchasePrice(LocalInvItemLocation invItemLocation, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean getInvLastPurchasePrice");

        try {

            LocalApPurchaseOrderLine apPurchaseOrderLine = apPurchaseOrderLineHome.getByMaxPoDateAndMaxPlCodeAndIiNameAndLocNameAndPoReceivingAndPoPosted(invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

            return this.convertCostByUom(invItemLocation.getInvItem().getIiName(), apPurchaseOrderLine.getInvUnitOfMeasure().getUomName(), apPurchaseOrderLine.getPlUnitCost(), false, AD_CMPNY);

        } catch (FinderException ex) {

            return 0d;
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean convertCostByUom");

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

    // SessionBean method

    public void ejbCreate() throws CreateException {

        Debug.print("ApGeneratePurchaseRequisitionControllerBean ejbCreate");
    }
}