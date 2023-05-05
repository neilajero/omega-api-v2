package com.ejb.txn.ad;

import com.ejb.exception.ad.AdUSRPasswordInvalidException;

import jakarta.ejb.Local;

@Local
public interface AdChangePasswordController {
    void executeChangePassword(String userPassword, String userNewPassword, Integer userCode, Integer companyCode) throws AdUSRPasswordInvalidException;
}