package com.ejb.txn.inv;

import jakarta.ejb.*;
import java.util.*;
import java.lang.*;

import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.exception.ad.AdPRFCoaGlVarianceAccountNotFoundException;
import com.ejb.exception.gl.GlJREffectiveDateNoPeriodExistException;
import com.ejb.exception.gl.GlJREffectiveDatePeriodClosedException;
import com.ejb.exception.global.*;
import com.ejb.exception.inv.InvATRAssemblyQtyGreaterThanAvailableQtyException;
import com.ejb.exception.inv.InvTagSerialNumberNotFoundException;
import com.util.mod.inv.InvModBranchStockTransferDetails;

@Local
public interface InvBranchStockTransferInEntryController
{
   InvModBranchStockTransferDetails getInvBstByBstCode(Date BSTI_DT, Integer BST_CODE, Integer AD_BRNCH, Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   InvModBranchStockTransferDetails getInvBstOutByBstCode(Integer BST_OUT_CODE, Integer AD_BRNCH, Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   ArrayList getInvBranchUomByIiName(String II_NM, Integer AD_CMPNY);

   Integer saveInvBstInEntry(InvModBranchStockTransferDetails details, ArrayList bslList, String type, Integer AD_BRNCH, Integer AD_CMPNY)
           throws GlobalRecordAlreadyDeletedException,
           GlobalTransactionAlreadyApprovedException,
           GlobalTransactionAlreadyPendingException,
           GlobalTransactionAlreadyPostedException,
           GlobalInvItemLocationNotFoundException,
           GlJREffectiveDateNoPeriodExistException,
           GlJREffectiveDatePeriodClosedException,
           GlobalJournalNotBalanceException,
           GlobalDocumentNumberNotUniqueException,
           GlobalInventoryDateException,
           GlobalBranchAccountNumberInvalidException,
           InvATRAssemblyQtyGreaterThanAvailableQtyException,
           GlobalRecordAlreadyAssignedException,
           AdPRFCoaGlVarianceAccountNotFoundException;

   Integer saveInvBstEntry(InvModBranchStockTransferDetails details, ArrayList bslList,
                           boolean isDraft, String type, Integer AD_BRNCH, Integer AD_CMPNY)
      throws GlobalRecordAlreadyDeletedException, GlobalTransactionAlreadyApprovedException,
           GlobalTransactionAlreadyPendingException, GlobalTransactionAlreadyPostedException,
           GlobalInvItemLocationNotFoundException, GlobalInvTagMissingException,
           GlobalInvTagExistingException, GlJREffectiveDateNoPeriodExistException,
           GlJREffectiveDatePeriodClosedException, GlobalJournalNotBalanceException,
           GlobalDocumentNumberNotUniqueException, GlobalInventoryDateException,
           GlobalBranchAccountNumberInvalidException, InvATRAssemblyQtyGreaterThanAvailableQtyException,
           AdPRFCoaGlVarianceAccountNotFoundException, GlobalMiscInfoIsRequiredException,
           GlobalExpiryDateNotFoundException, InvTagSerialNumberNotFoundException;

   void deleteInvBstEntry(Integer BST_CODE, String type, String AD_USR, Integer AD_CMPNY)
      throws GlobalRecordAlreadyDeletedException;

   short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY);

   short getInvGpInventoryLineNumber(Integer AD_CMPNY);

   ArrayList getAdApprovalNotifiedUsersByBstCode(Integer BST_CODE, Integer AD_CMPNY);

   double getInvIiUnitCostByIiNameAndLocFromAndUomNameAndDate(String II_NM, String LOC_FRM, String UOM_NM, Date ST_DT, String BR_NM, Integer AD_CMPNY);

   LocalInvBranchStockTransfer getInvBstOutByBstNumberAndBstBranch(String BST_NMBR, String BR_NM, Integer AD_BRNCH, Integer AD_CMPNY)
      throws GlobalNoRecordFoundException;

   double getInvIiShippingCostByIiUnitCostAndAdBranch(double II_UNT_CST, Integer AD_BRNCH, Integer AD_CMPNY);
}