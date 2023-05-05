package com.ejb.txnapi.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import java.util.ArrayList;

public interface ArStandardMemoLineApiController {
    ArrayList getArSmlAll(String companyShortName) throws GlobalNoRecordFoundException;
}