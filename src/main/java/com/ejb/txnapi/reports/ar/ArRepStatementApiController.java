package com.ejb.txnapi.reports.ar;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.restfulapi.reports.ar.models.StatementDetails;
import com.ejb.restfulapi.reports.ar.models.StatementRequest;
import com.ejb.restfulapi.reports.ar.models.StatementResponse;

public interface ArRepStatementApiController {
    void executeSpArRepStatementOfAccount(StatementDetails details)
            throws GlobalNoRecordFoundException;

    StatementResponse generateSoa(StatementRequest request);

}