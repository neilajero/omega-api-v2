package com.ejb.txnsync.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvATRAssemblyQtyGreaterThanAvailableQtyException;
import com.ejb.txn.OmegaCommonDataController;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModBranchStockTransferDetails;
import com.util.mod.inv.InvModBranchStockTransferLineDetails;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

@Stateless(name = "InvTransferSyncControllerBeanEJB")
public class InvTransferSyncControllerBean extends EJBContextClass implements InvTransferSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvLocationHome invLocationHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdBranchItemLocationHome adBranchItemLocationHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvDistributionRecordHome invDistributionRecordHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private OmegaCommonDataController commonData;

    @Override
    public int setInvTransferAllNewAndVoid(String[] newTransfers, String[] voidTransfers, String BR_BRNCH_CODE, Integer AD_CMPNY) {

        Debug.print("InvTransferSyncControllerBean setInvTransferAllNewAndVoid");

        LocalAdBranch adBranchFrom = null;

        short quantityPrecisionUnit = 0;

        int row = 0;

        quantityPrecisionUnit = commonData.getGlFcPrecisionUnit(AD_CMPNY);

        // Start of the giant try block
        try {

            LocalInvBranchStockTransfer invBranchStockTransfer = null;
            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_CODE, AD_CMPNY);

            // new transfers
            if (!newTransfers[0].equals(null)) {

                String[][] transfer = null;

                String[][][] transferLines = null;

                StringTokenizer tokenizer = null;


                for (int i = 0; i < newTransfers.length; i++) {

                    tokenizer = new StringTokenizer(newTransfers[i], "~", false);
                    transfer[i] = transferDecode(tokenizer.nextToken(), AD_CMPNY);
                    transferLines[i] = new String[tokenizer.countTokens()][];

                    int count = 0;

                    while (tokenizer.hasMoreTokens()) {
                        transferLines[i][count] = transferLineDecode(tokenizer.nextToken());
                        System.out.println(transferLines[i][count]);
                        count++;
                    }

                }

                do {
                    InvModBranchStockTransferDetails details = new InvModBranchStockTransferDetails();

                    details.setBstNumber(transfer[row][0]);
                    details.setBstTransferOutNumber(transfer[row][1]);
                    details.setBstType(transfer[row][2]);
                    details.setBstDate(EJBCommon.convertStringToSQLDate(transfer[row][3]));
                    details.setBstBranchTo(transfer[row][4]);
                    details.setBstTransitLocation(transfer[row][4]);
                    details.setBstDateLastModified(EJBCommon.convertStringToSQLDate(EJBCommon.convertSQLDateToString(new java.util.Date())));
                    details.setBstDescription("");


                    // line items
                    ArrayList bslList = new ArrayList();

                    for (int lines = 0; lines < transferLines.length; lines++) {
                        InvModBranchStockTransferLineDetails mdetails = new InvModBranchStockTransferLineDetails();

                        mdetails.setBslLineNumber(Short.valueOf(transferLines[row][lines][0]).shortValue());
                        mdetails.setBslQuantity(Double.valueOf(transferLines[row][lines][1]).doubleValue());
                        mdetails.setBslUomName(transferLines[row][lines][2]);
                        mdetails.setBslIiName(transferLines[row][lines][3]);
                        mdetails.setBslLocationName(transferLines[row][lines][4]);

                        Collection invItemLocations = invItemLocationHome.findByIiName(mdetails.getBslIiName(), AD_CMPNY);

                        Iterator i = invItemLocations.iterator();
                        while (i.hasNext()) {
                            LocalInvItemLocation invItemLocation = (LocalInvItemLocation) i.next();
                            mdetails.setBslIiDescription(invItemLocation.getInvItem().getIiDescription());
                        }

                        bslList.add(mdetails);

                    }

                    //LocalInvBranchStockTransfer invBranchStockTransfer = null;

                    // validate if branch stock transfer is already deleted
                    try {

                        if (details.getBstCode() != null) {

                            invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(details.getBstCode());

                        }

                    }
                    catch (FinderException ex) {

                        //throw new GlobalRecordAlreadyDeletedException();

                    }

                    // validate if branch stock transfer is already posted, void, approved or pending
                    if (details.getBstCode() != null) {

                        if (invBranchStockTransfer.getBstApprovalStatus() != null) {

                            if (invBranchStockTransfer.getBstApprovalStatus().equals("APPROVED") ||
                                    invBranchStockTransfer.getBstApprovalStatus().equals("N/A")) {

                                throw new GlobalTransactionAlreadyApprovedException();


                            } else if (invBranchStockTransfer.getBstApprovalStatus().equals("PENDING")) {

                                throw new GlobalTransactionAlreadyPendingException();

                            }

                        }

                        if (invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE) {

                            throw new GlobalTransactionAlreadyPostedException();

                        }

                    }

                    LocalInvBranchStockTransfer invBranchExistingStockTransfer = null;

                    try {

                        invBranchExistingStockTransfer = invBranchStockTransferHome.findByBstNumberAndBrCode(details.getBstNumber(), adBranch.getBrCode(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                    }

                    // validate if document number is unique and if document number is automatic then set next sequence

                    if (details.getBstCode() == null) {

                        LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignment = null;
                        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment = null;

                        if (invBranchExistingStockTransfer != null) {

                            throw new GlobalDocumentNumberNotUniqueException();

                        }

                        try {
                            if (details.getBstType().equalsIgnoreCase("OUT")) {
                                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV BRANCH STOCK TRANSFER-OUT", AD_CMPNY);
                            } else if (details.getBstType().equalsIgnoreCase("IN")) {
                                adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDcName("INV BRANCH STOCK TRANSFER-IN", AD_CMPNY);
                            }

                        }
                        catch (FinderException ex) {

                        }

                        try {

                            adBranchDocumentSequenceAssignment = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignment.getDsaCode(), adBranch.getBrCode(), AD_CMPNY);

                        }
                        catch (FinderException ex) {

                        }

                        if (adDocumentSequenceAssignment.getAdDocumentSequence().getDsNumberingType() == 'A' &&
                                (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                            while (true) {

                                if (adBranchDocumentSequenceAssignment == null || adBranchDocumentSequenceAssignment.getBdsNextSequence() == null) {

                                    try {

                                        invBranchStockTransferHome.findByBstNumberAndBrCode(adDocumentSequenceAssignment.getDsaNextSequence(), adBranch.getBrCode(), AD_CMPNY);
                                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));

                                    }
                                    catch (FinderException ex) {

                                        details.setBstNumber(adDocumentSequenceAssignment.getDsaNextSequence());
                                        adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
                                        break;

                                    }

                                } else {

                                    try {

                                        invBranchStockTransferHome.findByBstNumberAndBrCode(adBranchDocumentSequenceAssignment.getBdsNextSequence(), adBranch.getBrCode(), AD_CMPNY);
                                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));

                                    }
                                    catch (FinderException ex) {

                                        details.setBstNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence());
                                        adBranchDocumentSequenceAssignment.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignment.getBdsNextSequence()));
                                        break;

                                    }

                                }

                            }

                        }

                    } else {

                        if (invBranchExistingStockTransfer != null &&
                                !invBranchExistingStockTransfer.getBstCode().equals(details.getBstCode())) {

                            throw new GlobalDocumentNumberNotUniqueException();

                        }

                        if (invBranchStockTransfer.getBstNumber() != details.getBstNumber() &&
                                (details.getBstNumber() == null || details.getBstNumber().trim().length() == 0)) {

                            details.setBstNumber(invBranchStockTransfer.getBstNumber());

                        }

                    }

                    //Used in checking if branch stock transfer should re-generate distribution records

                    boolean isRecalculate = true;

                    //start ORDER type
                    if (details.getBstType().equalsIgnoreCase("ORDER")) {

                        // create branch stock transfer

                        if (details.getBstCode() == null) {

                            invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(),
                                    "ORDER", details.getBstNumber(), null, null, details.getBstDescription(),
                                    details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(),
                                    details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(),
                                    details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                                    details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                                    EJBCommon.FALSE, EJBCommon.FALSE, adBranch.getBrCode(), AD_CMPNY);

                        } else {

                            if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size() ||
                                    !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                                isRecalculate = true;

                            } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                                Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                                Iterator bslIterList = bslList.iterator();

                                while (bslIter.hasNext()) {

                                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList.next();


                                    if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getBslLocationName()) ||
                                            invBranchStockTransferLine.getBslUnitCost() != mdetails.getBslUnitCost() ||
                                            invBranchStockTransferLine.getBslQuantity() != mdetails.getBslQuantity() ||
                                            invBranchStockTransferLine.getBslAmount() != mdetails.getBslAmount() ||
                                            !invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getBslIiName()) ||
                                            !invBranchStockTransferLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getBslUomName())) {

                                        isRecalculate = true;
                                        break;

                                    }

		                            /* get item cost
		                            double COST = 0d;

		                            try {

		                                LocalInvCosting invCosting  = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndRemainingQuantityNotEqualToZeroAndIiNameAndLocName(
		                                        invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
		    									invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
		    									adBranch.getBrCode(), AD_CMPNY);

		                                COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), commonData.getGlFcPrecisionUnit(AD_CMPNY));

		                            } catch (FinderException ex) {

		                                COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
		                            }

		                            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), AD_CMPNY);
		    	                	LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), AD_CMPNY);

		    	                	COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), commonData.getGlFcPrecisionUnit(AD_CMPNY));

		                           	double AMOUNT = 0d;

		                            AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, commonData.getGlFcPrecisionUnit(AD_CMPNY));

		                            if (invBranchStockTransferLine.getBslUnitCost() != COST) {

		                                mdetails.setBslUnitCost(COST);
		                                mdetails.setBslAmount(AMOUNT);

		                                isRecalculate = true;
		                                break;

		                            }
		                            */
                                    isRecalculate = false;

                                }

                            } else {

                                isRecalculate = true;

                            }

                            invBranchStockTransfer.setBstType("ORDER");
                            invBranchStockTransfer.setBstNumber(details.getBstNumber());
                            invBranchStockTransfer.setBstDescription(details.getBstDescription());
                            invBranchStockTransfer.setBstDate(details.getBstDate());
                            invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                            invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                            invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                            invBranchStockTransfer.setBstReasonForRejection(null);

                        }
                        adBranch = adBranchHome.findByBrName(details.getBstBranchFrom(), AD_CMPNY);
                        adBranch.addInvBranchStockTransfer(invBranchStockTransfer);

                        LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), AD_CMPNY);
                        invLocation.addInvBranchStockTransfer(invBranchStockTransfer);

                    }    // end order type

                    if (details.getBstType().equalsIgnoreCase("OUT")) {    //start out type

                        // create branch stock transfer

                        if (details.getBstCode() == null) {

                            invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(),
                                    "OUT", details.getBstNumber(), null, details.getBstTransferOrderNumber(), details.getBstDescription(),
                                    details.getBstApprovalStatus(), details.getBstPosted(), details.getBstReasonForRejection(),
                                    details.getBstCreatedBy(), details.getBstDateCreated(), details.getBstLastModifiedBy(),
                                    details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                                    details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                                    EJBCommon.FALSE, EJBCommon.FALSE, adBranch.getBrCode(), AD_CMPNY);

                        } else {

                            if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size() ||
                                    !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                                isRecalculate = true;

                            } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                                Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                                Iterator bslIterList = bslList.iterator();

                                while (bslIter.hasNext()) {

                                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList.next();


                                    if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getBslLocationName()) ||
                                            invBranchStockTransferLine.getBslUnitCost() != mdetails.getBslUnitCost() ||
                                            invBranchStockTransferLine.getBslQuantity() != mdetails.getBslQuantity() ||
                                            invBranchStockTransferLine.getBslAmount() != mdetails.getBslAmount() ||
                                            !invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getBslIiName()) ||
                                            !invBranchStockTransferLine.getInvUnitOfMeasure().getUomName().equals(mdetails.getBslUomName())) {

                                        isRecalculate = true;
                                        break;

                                    }

                                    // get item cost
                                    double COST = 0d;

                                    try {

                                        LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                                invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                                invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                                adBranch.getBrCode(), AD_CMPNY);

                                        COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), commonData.getGlFcPrecisionUnit(AD_CMPNY));

                                    }
                                    catch (FinderException ex) {

                                        COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                                    }

                                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                            .findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                                    invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), AD_CMPNY);
                                    LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                                            .findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                                    invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), AD_CMPNY);

                                    COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), commonData.getGlFcPrecisionUnit(AD_CMPNY));

                                    double AMOUNT = 0d;

                                    AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, commonData.getGlFcPrecisionUnit(AD_CMPNY));

                                    if (invBranchStockTransferLine.getBslUnitCost() != COST) {

                                        mdetails.setBslUnitCost(COST);
                                        mdetails.setBslAmount(AMOUNT);

                                        isRecalculate = true;
                                        break;

                                    }

                                    isRecalculate = false;

                                }

                            } else {

                                isRecalculate = true;

                            }

                            invBranchStockTransfer.setBstType("OUT");
                            invBranchStockTransfer.setBstNumber(details.getBstNumber());
                            invBranchStockTransfer.setBstTransferOrderNumber(details.getBstTransferOrderNumber());
                            invBranchStockTransfer.setBstDescription(details.getBstDescription());
                            invBranchStockTransfer.setBstDate(details.getBstDate());
                            invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                            invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                            invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                            invBranchStockTransfer.setBstReasonForRejection(null);

                        }
                        adBranch = adBranchHome.findByBrName(details.getBstBranchTo(), AD_CMPNY);
                        adBranch.addInvBranchStockTransfer(invBranchStockTransfer);

                        LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), AD_CMPNY);
                        invLocation.addInvBranchStockTransfer(invBranchStockTransfer);
                        // lock corresponding transfer order

                        LocalInvBranchStockTransfer invBranchStockTransferOrder =
                                invBranchStockTransferHome.findByBstNumberAndBrCode(invBranchStockTransfer.getBstTransferOrderNumber(),
                                        invBranchStockTransfer.getAdBranch().getBrCode(), AD_CMPNY);

                        invBranchStockTransferOrder.setBstLock(EJBCommon.TRUE);

                        // get transfer out branch

                        try {

                            adBranchFrom = adBranchHome.findByPrimaryKey(invBranchStockTransferOrder.getBstAdBranch());

                        }
                        catch (FinderException ex) {

                        }
                    }    // end out type

                    if (details.getBstType().equalsIgnoreCase("IN")) {    //start in type

                        if (details.getBstCode() == null) {

                            invBranchStockTransfer = invBranchStockTransferHome.create(details.getBstDate(),
                                    "IN", details.getBstNumber(), details.getBstTransferOutNumber(), null,
                                    details.getBstDescription(), details.getBstApprovalStatus(), details.getBstPosted(),
                                    details.getBstReasonForRejection(), details.getBstCreatedBy(), details.getBstDateCreated(),
                                    details.getBstLastModifiedBy(), details.getBstDateLastModified(), details.getBstApprovedRejectedBy(),
                                    details.getBstDateApprovedRejected(), details.getBstPostedBy(), details.getBstDatePosted(),
                                    EJBCommon.FALSE, EJBCommon.FALSE, adBranch.getBrCode(), AD_CMPNY);

                        } else {

                            if (bslList.size() != invBranchStockTransfer.getInvBranchStockTransferLines().size() ||
                                    !(invBranchStockTransfer.getBstDate().equals(details.getBstDate()))) {

                                isRecalculate = true;

                            } else if (bslList.size() == invBranchStockTransfer.getInvBranchStockTransferLines().size()) {

                                Iterator bslIter = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();
                                Iterator bslIterList = bslList.iterator();

                                while (bslIter.hasNext()) {

                                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                                    InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) bslIterList.next();


                                    if (!invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getBslLocationName()) ||
                                            invBranchStockTransferLine.getBslQuantityReceived() != mdetails.getBslQuantityReceived()) {

                                        isRecalculate = true;
                                        break;

                                    }

                                    isRecalculate = false;

                                }

                            } else {

                                isRecalculate = true;

                            }

                            invBranchStockTransfer.setBstType("IN");
                            invBranchStockTransfer.setBstNumber(details.getBstNumber());
                            invBranchStockTransfer.setBstDescription(details.getBstDescription());
                            invBranchStockTransfer.setBstDate(details.getBstDate());
                            invBranchStockTransfer.setBstTransferOutNumber(details.getBstTransferOutNumber());
                            invBranchStockTransfer.setBstApprovalStatus(details.getBstApprovalStatus());
                            invBranchStockTransfer.setBstLastModifiedBy(details.getBstLastModifiedBy());
                            invBranchStockTransfer.setBstDateLastModified(details.getBstDateLastModified());
                            invBranchStockTransfer.setBstReasonForRejection(null);

                        }

                        // mapping to branch from
                        adBranch = adBranchHome.findByBrName(details.getBstBranchFrom(), AD_CMPNY);
                        adBranch.addInvBranchStockTransfer(invBranchStockTransfer);

                        // lock corresponding transfer out

                        LocalInvBranchStockTransfer invBranchStockTransferOut =
                                invBranchStockTransferHome.findByBstNumberAndBrCode(invBranchStockTransfer.getBstTransferOutNumber(),
                                        invBranchStockTransfer.getAdBranch().getBrCode(), AD_CMPNY);

                        invBranchStockTransferOut.setBstLock(EJBCommon.TRUE);

                        // get transfer out branch

                        try {

                            adBranchFrom = adBranchHome.findByPrimaryKey(invBranchStockTransferOut.getBstAdBranch());

                        }
                        catch (FinderException ex) {

                        }


                        LocalInvLocation invLocation = invLocationHome.findByLocName(details.getBstTransitLocation(), AD_CMPNY);
                        invLocation.addInvBranchStockTransfer(invBranchStockTransfer);

                    }    //end in type

                    double ABS_TOTAL_AMOUNT = 0d;

                    if (isRecalculate) {

                        // remove all branch stock transfer lines

                        Iterator i = invBranchStockTransfer.getInvBranchStockTransferLines().iterator();

                        short LINE_NUMBER = 0;

                        while (i.hasNext()) {

                            LINE_NUMBER++;

                            LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                            LocalInvItemLocation invItemLocation = null;

                            try {

                                invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                                        invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                        invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(LINE_NUMBER) + " - " + invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());

                            }
                            double convertedQuantity = 0d;

                            if (details.getBstType().equalsIgnoreCase("OUT")) {

                                convertedQuantity = commonData.convertByUomAndQuantity(
                                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                                        invBranchStockTransferLine.getBslQuantity(), AD_CMPNY);

                            } else if (details.getBstType().equalsIgnoreCase("IN")) {

                                convertedQuantity = commonData.convertByUomAndQuantity(
                                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                                        invBranchStockTransferLine.getBslQuantityReceived(), AD_CMPNY);

                            }

                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() - convertedQuantity);

                            i.remove();

                            em.remove(invBranchStockTransferLine);

                        }

                        //	Remove all distribution records

                        i = invBranchStockTransfer.getInvDistributionRecords().iterator();

                        while (i.hasNext()) {

                            LocalInvDistributionRecord arDistributionRecord = (LocalInvDistributionRecord) i.next();

                            i.remove();

                            em.remove(arDistributionRecord);

                        }

                        //	Add new branch stock transfer entry lines and distribution record

                        byte DEBIT = 0;

                        i = bslList.iterator();

                        while (i.hasNext()) {

                            InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                            LocalInvItemLocation invItemLocation = null;

                            try {

                                invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                                        mdetails.getBslLocationName(),
                                        mdetails.getBslIiName(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName()));

                            }

                            LocalInvItemLocation invItemTransitLocation = null;

                            try {

                                invItemTransitLocation = invItemLocationHome.findByLocNameAndIiName(
                                        details.getBstTransitLocation(),
                                        mdetails.getBslIiName(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                throw new GlobalInvItemLocationNotFoundException("Transit Location " + String.valueOf(details.getBstTransitLocation()));

                            }

                            // start date validation
                            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(),
                                    invItemLocation.getInvLocation().getLocName(), adBranch.getBrCode(), AD_CMPNY);
                            if (!invNegTxnCosting.isEmpty()) {
                                throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                            }


                            LocalInvBranchStockTransferLine invBranchStockTransferLine = this.addInvBslEntry(mdetails, invBranchStockTransfer, AD_CMPNY);

                            //	add physical inventory distribution

                            double AMOUNT = 0d;

                            if (details.getBstType().equalsIgnoreCase("OUT")) {

                                double COST = 0d;

                                try {

                                    LocalInvCosting invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(
                                            invBranchStockTransfer.getBstDate(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                            invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(),
                                            adBranch.getBrCode(), AD_CMPNY);

                                    COST = EJBCommon.roundIt(Math.abs(invCosting.getCstRemainingValue() / invCosting.getCstRemainingQuantity()), commonData.getGlFcPrecisionUnit(AD_CMPNY));

                                }
                                catch (FinderException ex) {

                                    COST = invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiUnitCost();
                                }

                                LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome
                                        .findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                                invBranchStockTransferLine.getInvUnitOfMeasure().getUomName(), AD_CMPNY);
                                LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome
                                        .findUmcByIiNameAndUomName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                                invBranchStockTransferLine.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName(), AD_CMPNY);

                                COST = EJBCommon.roundIt(COST * invDefaultUomConversion.getUmcConversionFactor()
                                        / invUnitOfMeasureConversion.getUmcConversionFactor(), commonData.getGlFcPrecisionUnit(AD_CMPNY));

                                AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantity() * COST, commonData.getGlFcPrecisionUnit(AD_CMPNY));

                            } else if (details.getBstType().equalsIgnoreCase("IN")) {

                                double COST = invBranchStockTransferLine.getBslUnitCost();

                                AMOUNT = EJBCommon.roundIt(invBranchStockTransferLine.getBslQuantityReceived() * COST, commonData.getGlFcPrecisionUnit(AD_CMPNY));

                            }

                            //	check branch mapping

                            LocalAdBranchItemLocation adBranchItemLocation = null;

                            try {

                                adBranchItemLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                        invItemLocation.getIlCode(), adBranch.getBrCode(), AD_CMPNY);


                            }
                            catch (FinderException ex) {

                            }

                            LocalGlChartOfAccount glChartOfAccount = null;

                            if (adBranchItemLocation == null) {

                                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                                        invItemLocation.getIlGlCoaInventoryAccount());

                            } else {

                                glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(
                                        adBranchItemLocation.getBilCoaGlInventoryAccount());

                            }

                            //	add dr for inventory

                            if (details.getBstType().equalsIgnoreCase("OUT")) {
                                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                                        Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, adBranch.getBrCode(), AD_CMPNY);
                            }

                            if (details.getBstType().equalsIgnoreCase("IN")) {
                                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                                        Math.abs(AMOUNT), glChartOfAccount.getCoaCode(), invBranchStockTransfer, adBranch.getBrCode(), AD_CMPNY);
                            }

                            //	check branch mapping for transit location

                            LocalAdBranchItemLocation adBranchItemTransitLocation = null;

                            try {
                                if (details.getBstType().equalsIgnoreCase("OUT")) {
                                    adBranchItemTransitLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                            invItemTransitLocation.getIlCode(), adBranch.getBrCode(), AD_CMPNY);
                                } else if (details.getBstType().equalsIgnoreCase("IN")) {

                                    adBranchItemTransitLocation = adBranchItemLocationHome.findBilByIlCodeAndBrCode(
                                            invItemTransitLocation.getIlCode(), adBranchFrom.getBrCode(), AD_CMPNY);

                                }


                            }
                            catch (FinderException ex) {

                            }

                            LocalGlChartOfAccount glChartOfAccountTransit = null;

                            if (adBranchItemTransitLocation == null) {

                                glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(
                                        invItemTransitLocation.getIlGlCoaInventoryAccount());

                            } else {

                                glChartOfAccountTransit = glChartOfAccountHome.findByPrimaryKey(
                                        adBranchItemTransitLocation.getBilCoaGlInventoryAccount());

                            }

                            double convertedQuantity = 0d;

                            if (details.getBstType().equalsIgnoreCase("OUT")) {

//		                    	 add dr for inventory transit location

                                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.TRUE,
                                        Math.abs(AMOUNT), glChartOfAccountTransit.getCoaCode(), invBranchStockTransfer, adBranch.getBrCode(), AD_CMPNY);


                                ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                                // set ilCommittedQuantity

                                convertedQuantity = commonData.convertByUomAndQuantity(
                                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                                        invBranchStockTransferLine.getBslQuantity(), AD_CMPNY);

                            } else if (details.getBstType().equalsIgnoreCase("IN")) {
                                //	add dr for inventory transit location

                                this.addInvDrEntry(invBranchStockTransfer.getInvDrNextLine(), "INVENTORY", EJBCommon.FALSE,
                                        Math.abs(AMOUNT), glChartOfAccountTransit.getCoaCode(), invBranchStockTransfer, adBranch.getBrCode(), AD_CMPNY);


                                ABS_TOTAL_AMOUNT += Math.abs(AMOUNT);

                                // set ilCommittedQuantity

                                convertedQuantity = commonData.convertByUomAndQuantity(
                                        invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(),
                                        invBranchStockTransferLine.getBslQuantityReceived(), AD_CMPNY);

                            }

                            invItemLocation.setIlCommittedQuantity(invItemLocation.getIlCommittedQuantity() + convertedQuantity);

                        }

                    } else {

                        Iterator i = bslList.iterator();

                        while (i.hasNext()) {

                            InvModBranchStockTransferLineDetails mdetails = (InvModBranchStockTransferLineDetails) i.next();

                            LocalInvItemLocation invItemLocation = null;

                            try {

                                invItemLocation = invItemLocationHome.findByLocNameAndIiName(
                                        mdetails.getBslLocationName(),
                                        mdetails.getBslIiName(), AD_CMPNY);

                            }
                            catch (FinderException ex) {

                                throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(mdetails.getBslLineNumber() + " - " + mdetails.getBslLocationName()));

                            }

                            //	start date validation

                            Collection invNegTxnCosting = invCostingHome.findNegTxnByGreaterThanCstDateAndIiNameAndLocName(
                                    invBranchStockTransfer.getBstDate(), invItemLocation.getInvItem().getIiName(),
                                    invItemLocation.getInvLocation().getLocName(), adBranch.getBrCode(), AD_CMPNY);
                            if (!invNegTxnCosting.isEmpty()) {
                                throw new GlobalInventoryDateException(invItemLocation.getInvItem().getIiName());
                            }


                            i = invBranchStockTransfer.getInvDistributionRecords().iterator();

                            while (i.hasNext()) {

                                LocalInvDistributionRecord distributionRecord = (LocalInvDistributionRecord) i.next();

                                if (distributionRecord.getDrDebit() == 1) {

                                    ABS_TOTAL_AMOUNT += distributionRecord.getDrAmount();

                                }

                            }

                        }

                    }

                    row++;

                } while (row < transfer.length);


                // void transfers

                if (!voidTransfers[0].equals(null)) {

                    //TODO:

                }

            }


            return 0;

            // End of the giant try block

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    private String[] transferDecode(String transfer, Integer AD_CMPNY) {

        Debug.print("InvTransferSyncControllerBean transferDecode");

        String separator = new String(EJBCommon.SEPARATOR + "");
        String decodedResult[] = new String[14];

        StringTokenizer strToken = new StringTokenizer(transfer, separator, true);

        String sep = strToken.nextToken();

        // transfer in number
        decodedResult[0] = strToken.nextToken();
        if (decodedResult[0].equals(separator)) {
            decodedResult[0] = "";
        } else {
            sep = strToken.nextToken();
        }

        // transfer in number
        decodedResult[1] = strToken.nextToken();
        if (decodedResult[1].equals(separator)) {
            decodedResult[1] = "";
        } else {
            sep = strToken.nextToken();
        }

        // transfer in number
        decodedResult[2] = strToken.nextToken();
        if (decodedResult[2].equals(separator)) {
            decodedResult[2] = "";
        } else {
            sep = strToken.nextToken();
        }

        // date transferred
        decodedResult[3] = strToken.nextToken();
        if (decodedResult[3].equals(separator)) {
            decodedResult[3] = "";
        } else {
            sep = strToken.nextToken();
        }

        // branch code
        decodedResult[4] = strToken.nextToken();
        if (decodedResult[4].equals(separator)) {
            decodedResult[4] = "";
        } else {
            sep = strToken.nextToken();
        }

        // transit location
        decodedResult[5] = strToken.nextToken();
        if (decodedResult[5].equals(separator)) {
            decodedResult[5] = "";
        } else {
            sep = strToken.nextToken();
        }

        return decodedResult;
    }

    private String[] transferLineDecode(String transferLines) {

        Debug.print("InvTransferSyncControllerBean transferLineDecode");

        String separator = new String(EJBCommon.SEPARATOR + "");
        String[] decodedResult = new String[4];

        StringTokenizer strToken = new StringTokenizer(transferLines, separator, true);

        String sep = strToken.nextToken();

        // line num
        decodedResult[0] = strToken.nextToken();
        if (decodedResult[0].equals(separator)) {
            decodedResult[0] = "";
        } else {
            sep = strToken.nextToken();
        }

        // quantity
        decodedResult[1] = strToken.nextToken();
        if (decodedResult[1].equals(separator)) {
            decodedResult[1] = "";
        } else {
            sep = strToken.nextToken();
        }

        // UOM
        decodedResult[2] = strToken.nextToken();
        if (decodedResult[2].equals(separator)) {
            decodedResult[2] = "";
        } else {
            sep = strToken.nextToken();
        }

        // item code
        decodedResult[3] = strToken.nextToken();
        if (decodedResult[3].equals(separator)) {
            decodedResult[3] = "";
        } else {
            sep = strToken.nextToken();
        }

        // location name
        decodedResult[4] = strToken.nextToken();
        if (decodedResult[4].equals(separator)) {
            decodedResult[4] = "";
        } else {
            sep = strToken.nextToken();
        }

        return decodedResult;
    }

    private void addInvDrEntry(short DR_LN, String DR_CLSS, byte DR_DBT, double DR_AMNT,
                               Integer COA_CODE, LocalInvBranchStockTransfer invBranchStockTransfer,
                               Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalBranchAccountNumberInvalidException {

        Debug.print("InvBranchStockTransferOutEntryControllerBean addInvDrEntry");

        try {

            // get company

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // validate coa

            LocalGlChartOfAccount glChartOfAccount = null;

            try {

                glChartOfAccount = glChartOfAccountHome.findByCoaCodeAndBranchCode(COA_CODE, AD_BRNCH, AD_CMPNY);

            } catch(FinderException ex) {

                throw new GlobalBranchAccountNumberInvalidException ();

            }

            // create distribution record

            LocalInvDistributionRecord invDistributionRecord = invDistributionRecordHome.create(
                    DR_LN, DR_CLSS, DR_DBT, EJBCommon.roundIt(DR_AMNT, adCompany.getGlFunctionalCurrency().getFcPrecision()),
                    EJBCommon.FALSE, EJBCommon.FALSE, AD_CMPNY);

            invBranchStockTransfer.addInvDistributionRecord(invDistributionRecord);
            glChartOfAccount.addInvDistributionRecord(invDistributionRecord);

        } catch (GlobalBranchAccountNumberInvalidException ex) {

            ctx.setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }


    private LocalInvBranchStockTransferLine addInvBslEntry(InvModBranchStockTransferLineDetails mdetails,
                                                           LocalInvBranchStockTransfer invBranchStockTransfer, Integer AD_CMPNY)
            throws InvATRAssemblyQtyGreaterThanAvailableQtyException {

        Debug.print("InvBranchStockTransferInEntryControllerBean addInvBslEntry");

        try {

            if(mdetails.getBslQuantity() < mdetails.getBslQuantityReceived())
                throw new InvATRAssemblyQtyGreaterThanAvailableQtyException();

            LocalInvBranchStockTransferLine invBranchStockTransferLine =
                    invBranchStockTransferLineHome.create(mdetails.getBslQuantity(),mdetails.getBslQuantityReceived(),
                            mdetails.getBslUnitCost(),mdetails.getBslAmount(), AD_CMPNY);

            invBranchStockTransfer.addInvBranchStockTransferLine(invBranchStockTransferLine);

            LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(
                    mdetails.getBslUomName(), AD_CMPNY);

            invUnitOfMeasure.addInvBranchStockTransferLine(invBranchStockTransferLine);


            LocalInvItemLocation invItemLocation = invItemLocationHome.findByIiNameAndLocName(
                    mdetails.getBslIiName(), mdetails.getBslLocationName(), AD_CMPNY);

            invItemLocation.addInvBranchStockTransferLine(invBranchStockTransferLine);

            return invBranchStockTransferLine;

        } catch (InvATRAssemblyQtyGreaterThanAvailableQtyException ex) {

            ctx.setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            ctx.setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }

    }

}