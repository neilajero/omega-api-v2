package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.dao.ad.LocalAdLogHome;
import com.ejb.entities.ad.LocalAdLog;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ad.AdLogDetails;

@Stateless(name = "AdLogControllerEJB")
public class AdLogControllerBean extends EJBContextClass implements AdLogController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdLogHome adLogHome;

    public ArrayList getAdLogAllByLogModuleAndLogModuleKey(String logModule, Integer logModuleKey, Integer companyCode) {

        Debug.print("AdLogModuleControllerBean getAdLogAllByLogModuleAndLogModuleKey");

        ArrayList list = new ArrayList();
        try {
            Collection adLogs = adLogHome.findByLogModuleAndLogModuleKey(logModule, logModuleKey);
            if (adLogs.isEmpty()) {
                return new ArrayList();
            }
            for (Object log : adLogs) {
                LocalAdLog adLog = (LocalAdLog) log;
                AdLogDetails details = new AdLogDetails();
                details.setLogCode(adLog.getLogCode());
                details.setLogDate(adLog.getLogDate());
                details.setLogUsername(adLog.getLogUsername());
                details.setLogAction(adLog.getLogAction());
                details.setLogModule(adLog.getLogModule());
                details.setLogDescription(adLog.getLogDescription());
                list.add(details);
            }
            return list;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdLogEntry(AdLogDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdLogControllerBean addAdLogEntry");

        try {
            LocalAdLog adLog = adLogHome.create(details.getLogDate(), details.getLogUsername(), details.getLogAction(), details.getLogDescription(), details.getLogModule(), details.getLogModuleKey());
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addAdLogEntry(Integer logModuleKey, String logModule, Date logDate, String logUsername, String logAction, String logDesc, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdLogControllerBean addAdLogEntry");

        try {
            LocalAdLog adLog = adLogHome.create(logDate, logUsername, logAction, logDesc, logModule, logModuleKey);
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {
        Debug.print("AdLogControllerBean ejbCreate");
    }
}