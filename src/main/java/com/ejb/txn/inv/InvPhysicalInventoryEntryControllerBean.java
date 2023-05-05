/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvPhysicalInventoryEntryControllerBean
 * @created
 * @author
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.inv.InvPhysicalInventoryDetails;
import com.util.mod.inv.InvModPhysicalInventoryDetails;
import com.util.mod.inv.InvModPhysicalInventoryLineDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Stateless(name = "InvPhysicalInventoryEntryControllerEJB")
public class InvPhysicalInventoryEntryControllerBean extends EJBContextClass
        implements InvPhysicalInventoryEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;
    @EJB
    private LocalInvPhysicalInventoryLineHome invPhysicalInventoryLineHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvAdjustmentHome invAdjustmentHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDeleteAuditTrailHome adDeleteAuditTrailHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalInvAdjustmentLineHome invAdjustmentLineHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), AD_CMPNY);

            for (Object unitOfMeasure : invUnitOfMeasures) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) unitOfMeasure;

                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();

                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getQuantityByIiNameAndUomName(
            String II_NM, String LOC_NM, String UOM_NM, Date PI_DT, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean getQuantityByIiNameAndUomName");
        try {

            double actualQuantity = 0d;

            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(II_NM, LOC_NM, AD_CMPNY);

            try {

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(PI_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                actualQuantity = invCosting.getCstRemainingQuantity();

            }
            catch (FinderException ex) {

            }

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(actualQuantity * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor(), this.getInvGpQuantityPrecisionUnit(AD_CMPNY));

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public InvModPhysicalInventoryDetails getInvPiByPiCode(Date PI_DT, Integer PI_CODE,
                                                           Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvPiByPiCode");
        try {

            LocalInvPhysicalInventory invPhysicalInventory = null;

            try {

                invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList piPilList = new ArrayList();

            Collection invPhysicalInventoryLines = invPhysicalInventoryLineHome.findByPiCode(PI_CODE, AD_CMPNY);

            for (Object physicalInventoryLine : invPhysicalInventoryLines) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) physicalInventoryLine;

                InvModPhysicalInventoryLineDetails mdetails = new InvModPhysicalInventoryLineDetails();
                mdetails.setPilCode(invPhysicalInventoryLine.getPilCode());
                mdetails.setPilEndingInventory(invPhysicalInventoryLine.getPilEndingInventory());
                mdetails.setPilWastage(invPhysicalInventoryLine.getPilWastage());
                mdetails.setPilVariance(invPhysicalInventoryLine.getPilVariance());
                mdetails.setPilMisc(invPhysicalInventoryLine.getPilMisc());

                try {

                    mdetails.setPilRemainingQuantity(this.getQuantityByIiNameAndUomName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName(), invPhysicalInventoryLine.getInvItemLocation().getInvLocation().getLocName(), invPhysicalInventoryLine.getInvUnitOfMeasure().getUomName(), invPhysicalInventory.getPiDate(), AD_BRNCH, AD_CMPNY));

                }
                catch (Exception ex) {

                    mdetails.setPilRemainingQuantity(0d);
                }
                mdetails.setPilIlLocName(invPhysicalInventoryLine.getInvItemLocation().getInvLocation().getLocName());
                mdetails.setPilIlIiName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName());
                mdetails.setPilIlIiDescription(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiDescription());
                mdetails.setPilIlIiUnit(invPhysicalInventoryLine.getInvUnitOfMeasure().getUomName());

                piPilList.add(mdetails);
            }

            InvModPhysicalInventoryDetails details = new InvModPhysicalInventoryDetails();

            details.setPiCode(invPhysicalInventory.getPiCode());
            details.setPiDate(invPhysicalInventory.getPiDate());
            details.setPiReferenceNumber(invPhysicalInventory.getPiReferenceNumber());
            details.setPiDescription(invPhysicalInventory.getPiDescription());
            details.setPiCreatedBy(invPhysicalInventory.getPiCreatedBy());
            details.setPiDateCreated(invPhysicalInventory.getPiDateCreated());
            details.setPiLastModifiedBy(invPhysicalInventory.getPiLastModifiedBy());
            details.setPiDateLastModified(invPhysicalInventory.getPiDateLastModified());
            try {
                details.setPiLocName(invPhysicalInventory.getInvLocation().getLocName());
            }
            catch (Exception e) {
                details.setPiLocName("");
            }

            details.setPiAdLvCategory(invPhysicalInventory.getPiAdLvCategory());
            details.setPiVarianceAdjusted(invPhysicalInventory.getPiVarianceAdjusted());
            details.setPiWastageAdjusted(invPhysicalInventory.getPiWastageAdjusted());
            details.setPiPilList(piPilList);
            details.setPiWastageAccount(getInvPrfDefaultWastageAccount(AD_BRNCH, AD_CMPNY).getPiWastageAccount());
            details.setPiWastageAccountDescription(getInvPrfDefaultWastageAccount(AD_BRNCH, AD_CMPNY).getPiWastageAccountDescription());

            return details;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUnsavedIlByLocNameAndIiAdLvCategoryAndPiCode(
            Date PI_DT, String LOC_NM, String II_AD_LV_CTGRY, Integer PI_CODE,
            Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvUnsavedIlByLocNameAndIiAdLvCategoryAndPiCode");
        ArrayList list = new ArrayList();
        try {

            Collection invItemLocations = invItemLocationHome.findItemByLocNameAndIiAdLvCategory(LOC_NM, II_AD_LV_CTGRY, AD_CMPNY);

            LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

            if (invItemLocations.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object itemLocation : invItemLocations) {

                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) itemLocation;

                try {

                    LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.findByPiCodeAndIlCode(PI_CODE, invItemLocation.getIlCode(), AD_CMPNY);
                    continue;

                }
                catch (FinderException ex) {

                }

                InvModPhysicalInventoryLineDetails mPilDetails = new InvModPhysicalInventoryLineDetails();
                mPilDetails.setPilEndingInventory(0d);
                mPilDetails.setPilWastage(0d);
                mPilDetails.setPilVariance(0d);

                try {

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invPhysicalInventory.getPiDate(), invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    mPilDetails.setPilRemainingQuantity(invCosting.getCstRemainingQuantity());

                }
                catch (FinderException ex) {

                    mPilDetails.setPilRemainingQuantity(0d);
                }

                mPilDetails.setPilIlLocName(invItemLocation.getInvLocation().getLocName());
                mPilDetails.setPilIlIiName(invItemLocation.getInvItem().getIiName());
                mPilDetails.setPilIlIiDescription(invItemLocation.getInvItem().getIiDescription());
                mPilDetails.setPilIlIiUnit(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());

                list.add(mPilDetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvIlByPiDateInvLocNameAndInvIiAdLvCategory(
            Date PI_DT, String LOC_NM, String II_AD_LV_CTGRY,
            Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvIlByPiDateInvLocNameAndInvIiAdLvCategory");
        ArrayList list = new ArrayList();
        try {

            Collection invItemLocations = invItemLocationHome.findItemByLocNameAndIiAdLvCategoryAndAdBranch(LOC_NM, II_AD_LV_CTGRY, AD_BRNCH, AD_CMPNY);

            if (invItemLocations.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object itemLocation : invItemLocations) {

                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) itemLocation;

                InvModPhysicalInventoryLineDetails mPilDetails = new InvModPhysicalInventoryLineDetails();
                mPilDetails.setPilEndingInventory(0d);
                mPilDetails.setPilWastage(0d);
                mPilDetails.setPilVariance(0d);

                try {

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(PI_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    mPilDetails.setPilRemainingQuantity(invCosting.getCstRemainingQuantity());

                }
                catch (FinderException ex) {

                    mPilDetails.setPilRemainingQuantity(0d);
                }

                mPilDetails.setPilIlIiName(invItemLocation.getInvItem().getIiName());
                mPilDetails.setPilIlLocName(invItemLocation.getInvLocation().getLocName());
                mPilDetails.setPilIlIiDescription(invItemLocation.getInvItem().getIiDescription());
                mPilDetails.setPilIlIiUnit(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());

                list.add(mPilDetails);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public InvModPhysicalInventoryLineDetails getInvIlByPiDateInvIiName(
            Date PI_DT, String II_NM, String LOC_NM, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvIlByPiDateInvIiName");
        try {

            LocalInvItemLocation invItemLocation = null;

            try {

                invItemLocation = invItemLocationHome.findByIiNameAndLocName(II_NM, LOC_NM, AD_CMPNY);

            }
            catch (Exception e) {

                throw new GlobalNoRecordFoundException();
            }

            InvModPhysicalInventoryLineDetails mPilDetails = new InvModPhysicalInventoryLineDetails();
            mPilDetails.setPilEndingInventory(0d);
            mPilDetails.setPilWastage(0d);
            mPilDetails.setPilVariance(0d);

            try {

                LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(PI_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                mPilDetails.setPilRemainingQuantity(invCosting.getCstRemainingQuantity());

            }
            catch (FinderException ex) {

                mPilDetails.setPilRemainingQuantity(0d);
            }

            mPilDetails.setPilIlIiName(invItemLocation.getInvItem().getIiName());
            mPilDetails.setPilIlLocName(invItemLocation.getInvLocation().getLocName());
            mPilDetails.setPilIlIiDescription(invItemLocation.getInvItem().getIiDescription());
            mPilDetails.setPilIlIiUnit(invItemLocation.getInvItem().getInvUnitOfMeasure().getUomName());

            return mPilDetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer saveInvPiEntry(InvPhysicalInventoryDetails details,
                                  String LOC_NM, ArrayList pilList, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException {

        Debug.print("InvPhysicalInventoryEntryControllerBean saveInvPiEntry");
        try {

            LocalInvPhysicalInventory invPhysicalInventory = null;

            try {

                LocalInvPhysicalInventory invExistingPhysicalInventory = invPhysicalInventoryHome.findByPiDateAndLocNameAndCategoryNameAndBrCode(details.getPiDate(), LOC_NM, details.getPiAdLvCategory(), AD_BRNCH, AD_CMPNY);

                if (details.getPiCode() == null || details.getPiCode() != null && !invExistingPhysicalInventory.getPiCode().equals(details.getPiCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            }
            catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            }
            catch (FinderException ex) {

            }

            // create physical inventory

            if (details.getPiCode() == null) {

                invPhysicalInventory = invPhysicalInventoryHome.create(details.getPiDate(), details.getPiReferenceNumber(), details.getPiDescription(), details.getPiCreatedBy(), details.getPiDateCreated(), details.getPiLastModifiedBy(), details.getPiDateLastModified(), details.getPiAdLvCategory(), EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            } else {

                invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(details.getPiCode());

                invPhysicalInventory.setPiReferenceNumber(details.getPiReferenceNumber());
                invPhysicalInventory.setPiDate(details.getPiDate());
                invPhysicalInventory.setPiDescription(details.getPiDescription());
                invPhysicalInventory.setPiLastModifiedBy(details.getPiLastModifiedBy());
                invPhysicalInventory.setPiDateLastModified(details.getPiDateLastModified());
                invPhysicalInventory.setPiAdLvCategory(details.getPiAdLvCategory());
            }

            try {

                LocalInvLocation invLocation = invLocationHome.findByLocName(LOC_NM, AD_CMPNY);
                invPhysicalInventory.setInvLocation(invLocation);

            }
            catch (FinderException ex) {

            }

            // invLocation.addInvPhysicalInventory(invPhysicalInventory);

            // remove all physical inventory lines

            Collection invPhysicalInventoryLines = invPhysicalInventoryLineHome.findByPiCode(invPhysicalInventory.getPiCode(), AD_CMPNY);

            Iterator i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                i.remove();

                //                invPhysicalInventoryLine.entityRemove();
                em.remove(invPhysicalInventoryLine);
            }

            i = pilList.iterator();
            LocalInvItemLocation invItemLocation = null;

            while (i.hasNext()) {

                InvModPhysicalInventoryLineDetails mdetails = (InvModPhysicalInventoryLineDetails) i.next();
                try {
                    Debug.print("mdetails.getPilIlLocName()=" + mdetails.getPilIlLocName());
                    Debug.print("mdetails.getPilIlIiName()=" + mdetails.getPilIlIiName());

                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getPilIlLocName(), mdetails.getPilIlIiName(), AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getPilIlIiName()));
                }

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = this.addInvPilEntry(mdetails, invPhysicalInventory, invItemLocation, AD_CMPNY);
            }

            return invPhysicalInventory.getPiCode();

        }
        catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer executeInvWastageGeneration(Integer PI_CODE, String WSTG_ACCNT, String USR_NM,
                                               Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException, GlobalAccountNumberInvalidException {

        Debug.print("InvPhysicalInventoryEntryControllerBean executeInvWastageGeneration");
        LocalInvPhysicalInventory invPhysicalInventory = null;
        try {

            // 	auto save physical inventory

            try {

                invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

            }
            catch (FinderException ex) {

            }

            // check if physical inventory has wastage

            Collection invPhysicalInventoryLines = invPhysicalInventory.getInvPhysicalInventoryLines();

            double TOTAL_WASTAGE = 0d;

            Iterator i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                TOTAL_WASTAGE += invPhysicalInventoryLine.getPilWastage();
            }

            if (TOTAL_WASTAGE == 0d) {

                // set wastage adjusted to true

                invPhysicalInventory.setPiWastageAdjusted(EJBCommon.TRUE);

                return null;
            }

            String ADJ_NMBR = null;

            // generate document number

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", AD_CMPNY);

            }
            catch (FinderException ex) {
            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // generate adjustment as WASTAGE

            LocalInvAdjustment invAdjustment = invAdjustmentHome.create(ADJ_NMBR, invPhysicalInventory.getPiReferenceNumber(), invPhysicalInventory.getPiDescription(), invPhysicalInventory.getPiDate(), "WASTAGE", null, EJBCommon.FALSE, USR_NM, invPhysicalInventory.getPiDate(), USR_NM, invPhysicalInventory.getPiDate(), null, null, null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            try {

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(WSTG_ACCNT, AD_BRNCH, AD_CMPNY);
                // glChartOfAccount.addInvAdjustment(invAdjustment);
                invAdjustment.setGlChartOfAccount(glChartOfAccount);
            }
            catch (FinderException ex) {

                throw new GlobalAccountNumberInvalidException();
            }

            double TOTAL_ADJUSTMENT_AMOUNT = 0d;
            byte DEBIT = 0;

            i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                if (invPhysicalInventoryLine.getPilWastage() == 0d) {
                    continue;
                }

                // generate inventory adjustment line

                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(invPhysicalInventoryLine, invAdjustment, invPhysicalInventory.getPiWastageAdjusted(), AD_CMPNY);

                // generate inventory adjustment distribution record

                double TOTAL_AMOUNT = 0d;
                double AMOUNT = 0d;
                double COST = 0d;

                try {

                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invPhysicalInventory.getPiDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                }
                catch (FinderException ex) {

                    COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                }

                COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvUnitOfMeasure().getUomName(), COST, true, AD_CMPNY);

                AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * COST, this.getGlFcPrecisionUnit(AD_CMPNY));

                DEBIT = AMOUNT > 0 ? EJBCommon.TRUE : EJBCommon.FALSE;

                // check for branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glInventoryChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT), glInventoryChartOfAccount.getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

                TOTAL_AMOUNT += AMOUNT;

                // add adjust quantity to item location committed quantity

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(), invAdjustmentLine.getInvItemLocation().getInvItem(), Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);

                    LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                }

                TOTAL_ADJUSTMENT_AMOUNT += TOTAL_AMOUNT;
            }

            // add adjustment distribution

            DEBIT = TOTAL_ADJUSTMENT_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;

            this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_ADJUSTMENT_AMOUNT), invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

            // set wastage adjusted to true

            invPhysicalInventory.setPiWastageAdjusted(EJBCommon.TRUE);

            return invAdjustment.getAdjCode();

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (GlobalAccountNumberInvalidException ex) {

            throw new GlobalAccountNumberInvalidException();

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer executeInvVarianceGeneration(Integer PI_CODE, String VRNC_ACCNT, String USR_NM,
                                                Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvPhysicalInventoryEntryControllerBean executeInvVarianceGeneration");
        LocalInvPhysicalInventory invPhysicalInventory = null;
        LocalInvCosting invCosting;
        try {

            // 	auto save physical inventory

            try {

                invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

            }
            catch (FinderException ex) {

            }

            // check if physical inventory has variance

            Collection invPhysicalInventoryLines = invPhysicalInventory.getInvPhysicalInventoryLines();

            boolean isVarianceExisting = false;

            Iterator i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                if (invPhysicalInventoryLine.getPilVariance() != 0) {
                    isVarianceExisting = true;
                }
            }

            if (!isVarianceExisting) {

                // set variance adjusted to true

                invPhysicalInventory.setPiVarianceAdjusted(EJBCommon.TRUE);

                return null;
            }

            String ADJ_NMBR = null;

            // generate document number

            LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

            try {

                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV ADJUSTMENT", AD_CMPNY);

            }
            catch (FinderException ex) {
            }

            try {

                adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {
            }

            if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A') {

                while (true) {

                    if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), AD_BRNCH, AD_CMPNY);
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adDocumentSequenceAssignment.getDsaNextSequence();
                            adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                            break;
                        }

                    } else {

                        try {

                            invAdjustmentHome.findByAdjDocumentNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                        }
                        catch (FinderException ex) {

                            ADJ_NMBR = adBranchDocumentSequenceAssignment.getBdsNextSequence();
                            adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                            break;
                        }
                    }
                }
            }

            // generate adjustment as VARIANCE

            LocalInvAdjustment invAdjustment = invAdjustmentHome.create(ADJ_NMBR, invPhysicalInventory.getPiReferenceNumber(), invPhysicalInventory.getPiDescription(), invPhysicalInventory.getPiDate(), "VARIANCE", null, EJBCommon.FALSE, USR_NM, invPhysicalInventory.getPiDate(), USR_NM, invPhysicalInventory.getPiDate(), null, null, null, null, null, null, EJBCommon.FALSE, EJBCommon.FALSE, AD_BRNCH, AD_CMPNY);

            try {

                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByCoaAccountNumberAndBranchCode(VRNC_ACCNT, AD_BRNCH, AD_CMPNY);
                // glChartOfAccount.addInvAdjustment(invAdjustment);
                invAdjustment.setGlChartOfAccount(glChartOfAccount);
            }
            catch (FinderException ex) {

            }

            double TOTAL_ADJUSTMENT_AMOUNT = 0d;
            byte DEBIT = 0;

            i = invPhysicalInventoryLines.iterator();

            while (i.hasNext()) {

                LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) i.next();

                if (invPhysicalInventoryLine.getPilVariance() == 0d) {
                    continue;
                }

                // generate inventory adjustment line

                LocalInvAdjustmentLine invAdjustmentLine = this.addInvAlEntry(invPhysicalInventoryLine, invAdjustment, invPhysicalInventory.getPiWastageAdjusted(), AD_CMPNY);

                // generate inventory adjustment distribution record

                double TOTAL_AMOUNT = 0d;
                double AMOUNT = 0d;
                double COST = 0d;

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(invPhysicalInventory.getPiDate(), invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvItemLocation().getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                    COST = Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity());

                }
                catch (FinderException ex) {

                    COST = invAdjustmentLine.getInvItemLocation().getInvItem().getIiUnitCost();
                }

                COST = this.convertCostByUom(invAdjustmentLine.getInvItemLocation().getInvItem().getIiName(), invAdjustmentLine.getInvUnitOfMeasure().getUomName(), COST, true, AD_CMPNY);

                AMOUNT = EJBCommon.roundIt(invAdjustmentLine.getAlAdjustQuantity() * COST, this.getGlFcPrecisionUnit(AD_CMPNY));

                DEBIT = AMOUNT > 0 ? EJBCommon.TRUE : EJBCommon.FALSE;

                // check for branch mapping

                LocalAdBranchItemLocation adBranchItemLocation = null;

                try {

                    adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(invAdjustmentLine.getInvItemLocation().getIlCode(), AD_BRNCH, AD_CMPNY);

                }
                catch (FinderException ex) {

                }

                LocalGlChartOfAccount glInventoryChartOfAccount = null;

                if (adBranchItemLocation == null) {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(invAdjustmentLine.getInvItemLocation().getIlGlCoaInventoryAccount());

                } else {

                    glInventoryChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adBranchItemLocation.getBilCoaGlInventoryAccount());
                }

                this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "INVENTORY", DEBIT, Math.abs(AMOUNT), glInventoryChartOfAccount.getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

                TOTAL_AMOUNT += AMOUNT;

                // add adjust quantity to item location committed quantity

                if (invAdjustmentLine.getAlAdjustQuantity() < 0) {

                    double convertedQuantity = this.convertByUomFromAndItemAndQuantity(invAdjustmentLine.getInvUnitOfMeasure(), invAdjustmentLine.getInvItemLocation().getInvItem(), Math.abs(invAdjustmentLine.getAlAdjustQuantity()), AD_CMPNY);

                    LocalInvItemLocation invItemLocation = invAdjustmentLine.getInvItemLocation();

                    invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);
                }

                TOTAL_ADJUSTMENT_AMOUNT += TOTAL_AMOUNT;
            }

            // add adjustment distribution

            DEBIT = TOTAL_ADJUSTMENT_AMOUNT > 0 ? EJBCommon.FALSE : EJBCommon.TRUE;

            this.addInvDrEntry(invAdjustment.getInvDrNextLine(), "ADJUSTMENT", DEBIT, Math.abs(TOTAL_ADJUSTMENT_AMOUNT), invAdjustment.getGlChartOfAccount().getCoaCode(), invAdjustment, AD_BRNCH, AD_CMPNY);

            // set variance adjusted to true

            invPhysicalInventory.setPiVarianceAdjusted(EJBCommon.TRUE);

            return invAdjustment.getAdjCode();

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvPiEntry(Integer PI_CODE, String AD_USR) {

        Debug.print("InvPhysicalInventoryEntryControllerBean deleteInvPiEntry");
        try {

            LocalInvPhysicalInventory invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);
            adDeleteAuditTrailHome.create("INV PHYSICAL INVENTORY", invPhysicalInventory.getPiDate(), invPhysicalInventory.getPiReferenceNumber(), invPhysicalInventory.getPiReferenceNumber(), 0d, AD_USR, new Date(), invPhysicalInventory.getPiAdCompany());
            //           invPhysicalInventory.entityRemove();
            em.remove(invPhysicalInventory);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public InvModPhysicalInventoryDetails getInvPrfDefaultVarianceAccount(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryControllerBean getInvPrfDefaultVarianceAccount");
        InvModPhysicalInventoryDetails details = new InvModPhysicalInventoryDetails();
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvGlCoaVarianceAccount());
            String[] accnt = glChartOfAccount.getCoaAccountNumber().split("-");
            //	String vrncAccntNmbr = accnt[0]+"-"+adBranchHome.findByPrimaryKey(AD_BRNCH).getBrCode();
            String vrncAccntNmbr = glChartOfAccount.getCoaAccountNumber();
            LocalGlChartOfAccount glVarianceAccount = null;
            try {

                Debug.print("acc number: " + vrncAccntNmbr);
                glVarianceAccount = glChartOfAccountHome.findByCoaAccountNumber(vrncAccntNmbr, AD_CMPNY);
                details.setPiVarianceAccount(glVarianceAccount.getCoaAccountNumber());
                details.setPiVarianceAccountDescription(glVarianceAccount.getCoaAccountDescription());
                return details;
            }
            catch (FinderException ex) {
                Debug.printStackTrace(ex);
                details.setPiVarianceAccount("");
                details.setPiVarianceAccountDescription("");
                return details;
            }
        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);
        }
        return details;
    }

    public InvModPhysicalInventoryDetails getInvPrfDefaultWastageAccount(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryControllerBean getInvPrfDefaultWastageAccount");
        InvModPhysicalInventoryDetails details = new InvModPhysicalInventoryDetails();
        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(adPreference.getPrfInvWastageAdjustmentAccount());
            String[] accnt = glChartOfAccount.getCoaAccountNumber().split("-");
            String wstgAccntNmbr = accnt[0] + "-" + adBranchHome.findByPrimaryKey(AD_BRNCH).getBrBranchCode();
            LocalGlChartOfAccount glWastageAccount = null;
            try {
                glWastageAccount = glChartOfAccountHome.findByCoaAccountNumber(wstgAccntNmbr, AD_CMPNY);
                details.setPiWastageAccount(glWastageAccount.getCoaAccountNumber());
                details.setPiWastageAccountDescription(glWastageAccount.getCoaAccountDescription());
                return details;
            }
            catch (FinderException ex) {
                Debug.printStackTrace(ex);
                details.setPiWastageAccount("");
                details.setPiWastageAccountDescription("");
                return details;
            }
        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);
        }
        return details;
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean getInvGpQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private LocalInvPhysicalInventoryLine addInvPilEntry(InvModPhysicalInventoryLineDetails mdetails,
                                                         LocalInvPhysicalInventory invPhysicalInventory,
                                                         LocalInvItemLocation invItemLocation, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean addInvPilEntry");
        try {

            LocalInvPhysicalInventoryLine invPhysicalInventoryLine = invPhysicalInventoryLineHome.create(mdetails.getPilEndingInventory(), mdetails.getPilWastage(), mdetails.getPilVariance(), mdetails.getPilMisc(), AD_CMPNY);

            // invPhysicalInventory.addInvPhysicalInventoryLine(invPhysicalInventoryLine);

            invPhysicalInventoryLine.setInvPhysicalInventory(invPhysicalInventory);

            invPhysicalInventoryLine.setInvItemLocation(invItemLocation);
            // invItemLocation.addInvPhysicalInventoryLine(invPhysicalInventoryLine);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getPilUomName(), AD_CMPNY);
            invPhysicalInventoryLine.setInvUnitOfMeasure(invUnitOfMeasure);
            // invUnitOfMeasure.addInvPhysicalInventoryLine(invPhysicalInventoryLine);

            return invPhysicalInventoryLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private LocalInvAdjustmentLine addInvAlEntry(LocalInvPhysicalInventoryLine invPhysicalInventoryLine,
                                                 LocalInvAdjustment invAdjustment, byte PI_WSTG_VRNC_ADJSTD, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean addInvAlEntry");
        try {

            LocalInvAdjustmentLine invAdjustmentLine = null;
            Debug.print("ITM_NAME=" + invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName());
            LocalInvItem invItem = invItemHome.findByIiName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName(), AD_CMPNY);

            double COST = 0d;

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invPhysicalInventoryLine.getInvUnitOfMeasure().getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            COST = EJBCommon.roundIt(invItem.getIiUnitCost() * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), this.getGlFcPrecisionUnit(AD_CMPNY));

            if (PI_WSTG_VRNC_ADJSTD != EJBCommon.TRUE) {

                invAdjustmentLine = invAdjustmentLineHome.create(COST, null, null, invPhysicalInventoryLine.getPilWastage() * 1, 0, EJBCommon.FALSE, AD_CMPNY);
                // invAdjustment.addInvAdjustmentLine(invAdjustmentLine);
                invAdjustmentLine.setInvAdjustment(invAdjustment);
            } else {

                invAdjustmentLine = invAdjustmentLineHome.create(COST, null, null, invPhysicalInventoryLine.getPilVariance() * -1, 0, EJBCommon.FALSE, AD_CMPNY);
                invAdjustmentLine.setInvAdjustment(invAdjustment);
            }

            LocalInvUnitOfMeasure invUnitOfMeasure = invPhysicalInventoryLine.getInvUnitOfMeasure();
            // invUnitOfMeasure.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvUnitOfMeasure(invUnitOfMeasure);

            LocalInvItemLocation invItemLocation = invPhysicalInventoryLine.getInvItemLocation();
            // invItemLocation.addInvAdjustmentLine(invAdjustmentLine);
            invAdjustmentLine.setInvItemLocation(invItemLocation);

            return invAdjustmentLine;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT, Integer COA_CODE,
                               LocalInvAdjustment invAdjustment, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvPhysicalInventoryEntryControllerBean addInvDrEntry");
        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            }
            catch (FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException();
            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()), EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            // invAdjustment.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvAdjustment(invAdjustment);
            // glChartOfAccount.addInvDistributionRecord(invDistributionRecord);
            invDistributionRecord.setInvChartOfAccount(glChartOfAccount);

        }
        catch (GlobalBranchAccountNumberInvalidException ex) {

            throw new GlobalBranchAccountNumberInvalidException();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvPhysiscalInventoryEntryControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertByUomFromAndItemAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure,
                                                      LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean convertByUomFromAndItemAndQuantity");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertCostByUom(String II_NM, String UOM_NM, double unitCost, boolean isFromDefault, Integer AD_CMPNY) {

        Debug.print("InvPhysicalInventoryEntryControllerBean convertCostByUom");
        try {

            LocalInvItem invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, UOM_NM, AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(II_NM, invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);

            if (isFromDefault) {

                return unitCost * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor();

            } else {

                return unitCost * invUnitOfMeasureConversion.getUmcConversionFactor() / invDefaultUomConversion.getUmcConversionFactor();
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvPhysicalInventoryEntryControllerBean ejbCreate");
    }

}