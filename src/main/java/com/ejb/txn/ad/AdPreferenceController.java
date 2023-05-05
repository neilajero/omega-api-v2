package com.ejb.txn.ad;

import com.ejb.exception.ad.*;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ad.AdModPreferenceDetails;

import jakarta.ejb.Local;

import java.util.ArrayList;

@Local()
public interface AdPreferenceController {

    byte getAdPrfAdDisableMultipleLogin(String companyShortName);

    short getGlFcPrecisionUnit(Integer companyCode);

    AdModPreferenceDetails getAdPrf(Integer companyCode);

    void saveAdPrfEntry(AdModPreferenceDetails mdetails, String withholdingTaxCode, Integer companyCode) throws GlobalRecordAlreadyExistException, AdPRFCoaGlAccruedVatAccountNotFoundException, AdPRFCoaGlPettyCashAccountNotFoundException, AdPRFCoaGlPOSAdjustmentAccountNotFoundException, AdPRFCoaGlPosDiscountAccountNotFoundException, AdPRFCoaGlPosGiftCertificateAccountNotFoundException, AdPRFCoaGlPosServiceChargeAccountNotFoundException, AdPRFCoaGlPosDineInChargeAccountNotFoundException, AdPRFCoaGlCustomerDepositAccountNotFoundException;

    ArrayList getArWtcAll(Integer companyCode);

    ArrayList getArTcAll(Integer companyCode);

    ArrayList getGlFcAll(Integer companyCode);

    ArrayList getInvLocAll(Integer companyCode);

    ArrayList getAdApplicationAllInstalled(Integer companyCode);

}