package com.ejb.txn.ap;

import com.ejb.exception.global.GlobalRecordInvalidException;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface ApAnnualProcurementUploadController {

    void uploadAnnualProcurement(ArrayList list, String userDepartment, Integer branchCode, Integer companyCode) throws GlobalRecordInvalidException;

    String getAdUsrDepartment(String username, Integer companyCode);

}