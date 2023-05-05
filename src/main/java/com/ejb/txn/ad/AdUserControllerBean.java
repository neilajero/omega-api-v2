package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.LocalApRecurringVoucherHome;
import com.ejb.dao.gl.LocalGlRecurringJournalHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdUserDetails;
import com.util.mod.ad.AdModUserDetails;

import jakarta.ejb.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

@Stateless(name = "AdUserControllerEJB")
public class AdUserControllerBean extends EJBContextClass implements AdUserController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringVoucherHome;
    @EJB
    private LocalGlRecurringJournalHome glRecurringJournalHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    public ArrayList getAdUsrAll(Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdUserControllerBean getAdUsrAll");

        Collection adUsers = null;
        LocalAdUser adUser;
        ArrayList list = new ArrayList();
        try {

            adUsers = adUserHome.findUsrAll(companyCode);

        }
        catch (FinderException ex) {
        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adUsers.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }
        for (Object user : adUsers) {

            adUser = (LocalAdUser) user;

            AdModUserDetails details = new AdModUserDetails();

            details.setUsrCode(adUser.getUsrCode());
            details.setUsrName(adUser.getUsrName());
            details.setUsrDept(adUser.getUsrDept());
            details.setUsrDescription(adUser.getUsrDescription());
            details.setUsrPosition(adUser.getUsrPosition());
            details.setUsrEmailAddress(adUser.getUsrEmailAddress());
            Debug.print("adUser.getUsrHead()=" + adUser.getUsrHead());
            Debug.print("adUser.getUsrInspector()=" + adUser.getUsrInspector());
            details.setUsrHead(adUser.getUsrHead());
            details.setUsrInspector(adUser.getUsrInspector());
            details.setUsrPassword(this.decryptPassword(adUser.getUsrPassword()));
            details.setUsrPasswordExpirationCode(adUser.getUsrPasswordExpirationCode());
            details.setUsrPasswordExpirationDays(adUser.getUsrPasswordExpirationDays());
            details.setUsrPasswordExpirationAccess(adUser.getUsrPasswordExpirationAccess());
            details.setUsrDateFrom(adUser.getUsrDateFrom());
            details.setUsrDateTo(adUser.getUsrDateTo());

            list.sort(AdModUserDetails.DepartmentComparator);

            list.add(details);
        }
        return list;
    }

    public AdUserDetails getAdUserByUsernamePassword(String username, String password, String companyShortName) throws GlobalNoRecordFoundException {

        Debug.print("AdUserControllerBean getAdUserByUsernamePassword");

        LocalAdUser adUser;
        LocalAdCompany adCompany;
        try {

            AdUserDetails details = new AdUserDetails();

            int companyCode = 0;
            adCompany = adCompanyHome.findByCmpShortName(companyShortName);

            if (adCompany != null) {
                companyCode = adCompany.getCmpCode();
            }

            adUser = adUserHome.findByUsrName(username, companyCode);

            if (password.equals(this.encryptPassword(adUser.getUsrPassword()))) {

                throw new FinderException();
            }

            details.setUsrCode(adUser.getUsrCode());
            details.setUsrName(adUser.getUsrName());
            details.setUsrDept(adUser.getUsrDept());
            details.setUsrDescription(adUser.getUsrDescription());
            details.setUsrPosition(adUser.getUsrPosition());
            details.setUsrEmailAddress(adUser.getUsrEmailAddress());
            details.setUsrHead(adUser.getUsrHead());
            details.setUsrInspector(adUser.getUsrInspector());
            details.setUsrPassword(adUser.getUsrPassword());
            details.setUsrPasswordExpirationCode(adUser.getUsrPasswordExpirationCode());
            details.setUsrPasswordExpirationDays(adUser.getUsrPasswordExpirationDays());
            details.setUsrPasswordExpirationAccess(adUser.getUsrPasswordExpirationAccess());
            details.setUsrDateFrom(adUser.getUsrDateFrom());
            details.setUsrDateTo(adUser.getUsrDateTo());
            details.setDecriptedPassword(this.decryptPassword(adUser.getUsrPassword()));
            details.setUsrAdCompany(companyCode);
            details.setCmpName(adCompany.getCmpName());
            details.setCmpShortName(adCompany.getCmpShortName());
            details.setCmpWelcomeNote(adCompany.getCmpWelcomeNote());

            return details;
        }
        catch (FinderException ex) {

            throw new GlobalNoRecordFoundException();
        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer addAdUsrEntry(AdUserDetails details, String employeeNumber, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdUserControllerBean addAdUsrEntry");

        LocalAdUser adUser;
        try {

            adUser = adUserHome.findByUsrName(details.getUsrName(), companyCode);

            throw new GlobalRecordAlreadyExistException();

        }
        catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        }
        catch (FinderException ex) {
        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // create new user

            adUser = adUserHome.create(details.getUsrName(), details.getUsrDept(), details.getUsrDescription(), details.getUsrPosition(), details.getUsrEmailAddress(), details.getUsrHead(), details.getUsrInspector(), this.encryptPassword(details.getUsrPassword()), details.getUsrPasswordExpirationCode(), details.getUsrPasswordExpirationDays(), details.getUsrPasswordExpirationAccess(), (short) 0, details.getUsrDateFrom(), details.getUsrDateTo(), companyCode);

            return adUser.getUsrCode();
        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer updateAdUsrEntry(AdUserDetails details, String employeeNumber, Integer companyCode) throws GlobalRecordAlreadyExistException {

        Debug.print("AdUserControllerBean updateAdUsrEntry");

        LocalAdUser adUser;
        try {

            LocalAdUser adExistingUser = adUserHome.findByUsrName(details.getUsrName(), companyCode);

            if (!adExistingUser.getUsrCode().equals(details.getUsrCode())) {

                throw new GlobalRecordAlreadyExistException();
            }

        }
        catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        }
        catch (FinderException ex) {
        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // find and update user

            adUser = adUserHome.findByPrimaryKey(details.getUsrCode());

            adUser.setUsrName(details.getUsrName());
            adUser.setUsrDept(details.getUsrDept());
            adUser.setUsrDescription(details.getUsrDescription());
            adUser.setUsrHead(details.getUsrHead());
            adUser.setUsrInspector(details.getUsrInspector());
            adUser.setUsrPosition(details.getUsrPosition());
            adUser.setUsrEmailAddress(details.getUsrEmailAddress());
            adUser.setUsrPassword(this.encryptPassword(details.getUsrPassword()));
            adUser.setUsrPasswordExpirationCode(details.getUsrPasswordExpirationCode());
            adUser.setUsrPasswordExpirationDays(details.getUsrPasswordExpirationDays());
            adUser.setUsrPasswordExpirationAccess(details.getUsrPasswordExpirationAccess());
            adUser.setUsrDateFrom(details.getUsrDateFrom());
            adUser.setUsrDateTo(details.getUsrDateTo());

            return adUser.getUsrCode();
        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteAdUsrEntry(Integer userCode, Integer companyCode) throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("AdUserControllerBean deleteAdUsrEntry");

        LocalAdUser adUser;
        try {

            adUser = adUserHome.findByPrimaryKey(userCode);

        }
        catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        try {

            // recurring voucher

            Collection recurringVouchers1 = apRecurringVoucherHome.findByUserCode1(adUser.getUsrCode(), companyCode);
            Collection recurringVouchers2 = apRecurringVoucherHome.findByUserCode2(adUser.getUsrCode(), companyCode);
            Collection recurringVouchers3 = apRecurringVoucherHome.findByUserCode3(adUser.getUsrCode(), companyCode);
            Collection recurringVouchers4 = apRecurringVoucherHome.findByUserCode4(adUser.getUsrCode(), companyCode);
            Collection recurringVouchers5 = apRecurringVoucherHome.findByUserCode5(adUser.getUsrCode(), companyCode);

            // recurring journal

            Collection recurringJournals1 = glRecurringJournalHome.findByUserName1(adUser.getUsrName(), companyCode);
            Collection recurringJournals2 = glRecurringJournalHome.findByUserName2(adUser.getUsrName(), companyCode);
            Collection recurringJournals3 = glRecurringJournalHome.findByUserName3(adUser.getUsrName(), companyCode);
            Collection recurringJournals4 = glRecurringJournalHome.findByUserName4(adUser.getUsrName(), companyCode);
            Collection recurringJournals5 = glRecurringJournalHome.findByUserName5(adUser.getUsrName(), companyCode);

            if (!adUser.getAdApprovalUsers().isEmpty() || !adUser.getAdApprovalQueues().isEmpty() || !recurringVouchers1.isEmpty() || !recurringVouchers2.isEmpty() || !recurringVouchers3.isEmpty() || !recurringVouchers4.isEmpty() || !recurringVouchers5.isEmpty() || !recurringJournals1.isEmpty() || !recurringJournals2.isEmpty() || !recurringJournals3.isEmpty() || !recurringJournals4.isEmpty() || !recurringJournals5.isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

        }
        catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        // remove user
        try {

            //			adUser.entityRemove();
            em.remove(adUser);

        }
        catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    // private method
    private String encryptPassword(String password) {

        Debug.print("AdUserControllerBean encryptPassword");

        String encPassword;
        try {

            String keyString = "Some things are better left unread.";
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);

            // encode string to utf-8
            byte[] utf8 = password.getBytes(StandardCharsets.UTF_8);

            int length = key.length;
            if (utf8.length < key.length) {
                length = utf8.length;
            }

            // encrypt
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (utf8[i] ^ key[i]);
            }

            encPassword = Base64.getEncoder().encodeToString(data);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return encPassword;
    }

    private String decryptPassword(String password) {

        Debug.print("AdUserControllerBean decryptPassword");

        String decPassword;
        try {

            String keyString = "Some things are better left unread.";
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);

            byte[] base64 = Base64.getDecoder().decode(password);
            int length = key.length;
            if (base64.length < key.length) {
                length = base64.length;
            }

            // decrypt
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (key[i] ^ base64[i]);
            }

            // decode byte to string
            decPassword = new String(data, StandardCharsets.UTF_8);

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return decPassword;
    }

    public void ejbCreate() throws CreateException {

        Debug.print("AdUserControllerBean ejbCreate");
    }

}