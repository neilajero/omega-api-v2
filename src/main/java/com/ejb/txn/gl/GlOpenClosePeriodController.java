package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoSetOfBookFoundException;
import com.util.gl.GlAccountingCalendarValueDetails;

import jakarta.ejb.Local;

@Local
public interface GlOpenClosePeriodController {

    java.util.ArrayList getEditableYears(java.lang.Integer AD_CMPNY) throws GlobalNoSetOfBookFoundException;

    java.util.ArrayList getGlAcvByYear(int YR, java.lang.Integer AD_CMPNY);

    void updateGlAcvStatus(GlAccountingCalendarValueDetails details, int YR, java.lang.Integer AD_CMPNY);

    boolean isGlAcvPriorPeriodStatusNeedConfirm(java.lang.Integer ACV_CODE, int YR, java.lang.Integer AD_CMPNY);

}