package com.ejb.txn.ad;

import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ad.AdModFormFunctionResponsibilityDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdFormFunctionResponsibilityController {

    AdModFormFunctionResponsibilityDetails getAdFrByRsCodeAndFfCode(Integer responsibilityCode, Integer formFunctionCode, Integer companyCode);

    ArrayList getAdFrByRsCode(Integer responsibilityCode, Integer companyCode);

    AdResponsibilityDetails getAdRsByRsCode(Integer responsibilityCode, Integer companyCode);

    void saveAdFrEntry(ArrayList list, Integer responsibilityCode, Integer companyCode);
}