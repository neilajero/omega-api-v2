package com.ejb.txn.ad;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.exception.ad.AdUSRPasswordInvalidException;
import com.util.*;

@Stateless(name = "AdChangePasswordControllerEJB")
public class AdChangePasswordControllerBean extends EJBContextClass implements AdChangePasswordController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdUserHome adUserHome;

    public void executeChangePassword(String userPassword, String userNewPassword, Integer userCode, Integer companyCode) throws AdUSRPasswordInvalidException {

        Debug.print("AdChangePasswordControllerBean executeChangePassword");

        LocalAdUser adUser;
        try {
            adUser = adUserHome.findByPrimaryKey(userCode);
            if (!adUser.getUsrPassword().equals(this.encryptPassword(userPassword))) {
                throw new AdUSRPasswordInvalidException();
            } else {
                adUser.setUsrPassword(this.encryptPassword(userNewPassword));
            }
        } catch (AdUSRPasswordInvalidException ex) {
            throw ex;
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // private method
    private String encryptPassword(String password) {

        Debug.print("AdChangePasswordControllerBean encryptPassword");

        String encPassword = "";
        try {
            String keyString = "Some things are better left unread.";
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);

            // encode password to utf-8
            byte[] utf8 = password.getBytes(StandardCharsets.UTF_8);
            int length = key.length;
            if (utf8.length < key.length) length = utf8.length;

            // encrypt
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (utf8[i] ^ key[i]);
            }

            encPassword = Base64.getEncoder().encodeToString(data);

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return encPassword;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdUserControllerBean ejbCreate");
    }
}