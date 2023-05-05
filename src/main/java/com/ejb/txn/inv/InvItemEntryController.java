package com.ejb.txn.inv;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvAccumulatedDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvDepreciationAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetAccountNotFoundException;
import com.ejb.exception.inv.InvFixedAssetInformationNotCompleteException;
import com.util.inv.InvItemDetails;
import com.util.mod.inv.InvModItemDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface InvItemEntryController {

    java.lang.Integer saveInvIiEntry(InvItemDetails itemDetails, String unitOfMeasureName, String supplierCode,
                                     String customerCode, String retailUnitOfMeasureName, String defaultLocation,
                                     String fixedAssetAccount, String depreciationAccount,
                                     String accumulatedDepreciation, boolean trackMisc, Integer companyCode)
            throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException,
            InvFixedAssetAccountNotFoundException, InvDepreciationAccountNotFoundException,
            InvFixedAssetInformationNotCompleteException, InvAccumulatedDepreciationAccountNotFoundException;

    void deleteInvIiEntry(Integer itemCode, Integer companyCode)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

    InvModItemDetails getInvIiByIiCode(Integer itemCode, Integer branchCode, Integer companyCode)
            throws GlobalNoRecordFoundException;

    ArrayList getInvUomAllByUnitMeasureClass(String unitOfMeasureName, Integer companyCode);

    ArrayList getInvIiAll(Integer companyCode);

    double getIiUnitCostByIiName(String itemName, Integer companyCode);

    short getGlFcPrecisionUnit(Integer companyCode);

}