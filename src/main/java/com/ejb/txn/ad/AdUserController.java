package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdUserDetails;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface AdUserController {

    ArrayList getAdUsrAll(Integer companyCode) throws GlobalNoRecordFoundException;

    AdUserDetails getAdUserByUsernamePassword(String username, String password, String companyShortName) throws GlobalNoRecordFoundException;

    Integer addAdUsrEntry(AdUserDetails details, String employeeNumber, Integer companyCode) throws GlobalRecordAlreadyExistException;

    Integer updateAdUsrEntry(AdUserDetails details, String employeeNumber, Integer companyCode) throws GlobalRecordAlreadyExistException;

    void deleteAdUsrEntry(Integer userCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException;

}