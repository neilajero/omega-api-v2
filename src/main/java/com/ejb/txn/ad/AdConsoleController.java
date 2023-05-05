package com.ejb.txn.ad;

import jakarta.ejb.Local;
import java.util.ArrayList;

import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalRecordInvalidException;

@Local
public interface AdConsoleController {

    void executeAdConsole(String companyName, String companyShortName, String companyWelcomeNote, short numberOfSegment, String segmentSeparator, String companyRetainedEarningsAccount, ArrayList genSegmentList, ArrayList genValueSetValueList, ArrayList glCoaList) throws GlobalRecordAlreadyExistException, GlobalRecordInvalidException;

}