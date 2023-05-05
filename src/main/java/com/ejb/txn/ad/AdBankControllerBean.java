package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.dao.ad.LocalAdBankHome;
import com.ejb.entities.ad.LocalAdBank;
import com.ejb.exception.global.*;
import com.util.*;
import com.util.ad.AdBankDetails;

@Stateless(name = "AdBankControllerEJB")
public class AdBankControllerBean extends EJBContextClass implements AdBankController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBankHome adBankHome;

    public ArrayList getAdBnkAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdBankControllerBean getAdBnkAll");

        Collection adBanks = null;
        LocalAdBank adBank;
        ArrayList list = new ArrayList();
        try {

            adBanks = adBankHome.findBnkAll(companyCode);

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adBanks.isEmpty()) {

            throw new GlobalNoRecordFoundException();

        }
        for (Object bank : adBanks) {

            adBank = (LocalAdBank) bank;


            AdBankDetails details = new AdBankDetails();
            details.setBnkCode(adBank.getBnkCode());
            details.setBnkName(adBank.getBnkName());
            details.setBnkBranch(adBank.getBnkBranch());
            details.setBnkNumber(adBank.getBnkNumber());
            details.setBnkInstitution(adBank.getBnkInstitution());
            details.setBnkDescription(adBank.getBnkDescription());
            details.setBnkAddress(adBank.getBnkAddress());
            details.setBnkEnable(adBank.getBnkEnable());

            list.add(details);
        }
        return list;
    }

    public void addAdBnkEntry(AdBankDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdBankControllerBean addAdBnkEntry");

        LocalAdBank adBank;
        try {
            adBank = adBankHome.findByBnkName(details.getBnkName(), companyCode);
            throw new GlobalRecordAlreadyExistException();
        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
        try {
            // create new bank
            adBank = adBankHome.create(details.getBnkName(), details.getBnkBranch(), details.getBnkNumber(), details.getBnkInstitution(), details.getBnkDescription(), details.getBnkAddress(), details.getBnkEnable(), companyCode);
        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public void updateAdBnkEntry(AdBankDetails details, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdBankControllerBean updateAdBnkEntry");

        LocalAdBank adBank;
        try {
            LocalAdBank adExistingBank = adBankHome.findByBnkName(details.getBnkName(), companyCode);
            if (!adExistingBank.getBnkCode().equals(details.getBnkCode())) {

                throw new GlobalRecordAlreadyExistException();

            }
        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }

        try {
            // find and update bank
            adBank = adBankHome.findByPrimaryKey(details.getBnkCode());
            adBank.setBnkName(details.getBnkName());
            adBank.setBnkBranch(details.getBnkBranch());
            adBank.setBnkNumber(details.getBnkNumber());
            adBank.setBnkInstitution(details.getBnkInstitution());
            adBank.setBnkDescription(details.getBnkDescription());
            adBank.setBnkAddress(details.getBnkAddress());
            adBank.setBnkEnable(details.getBnkEnable());
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());

        }
    }

    public void deleteAdBankEntry(Integer bankCode, Integer companyCode) throws GlobalRecordAlreadyAssignedException, GlobalRecordAlreadyDeletedException {

        Debug.print("AdBankControllerBean deleteAdBankEntry");

        Collection adBankAccounts;
        LocalAdBank adBank;
        try {
            adBank = adBankHome.findByPrimaryKey(bankCode);
        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }
        try {
            adBankAccounts = adBank.getAdBankAccounts();
            if (!adBankAccounts.isEmpty()) {
                throw new GlobalRecordAlreadyAssignedException();
            }
            em.remove(adBank);
        } catch (GlobalRecordAlreadyAssignedException ex) {
            throw new GlobalRecordAlreadyAssignedException();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdBankControllerBean ejbCreate");

    }
}