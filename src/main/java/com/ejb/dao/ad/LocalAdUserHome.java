package com.ejb.dao.ad;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdUser;
import jakarta.ejb.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdUserHome {

    public static final String JNDI_NAME = "LocalAdUserHome!com.ejb.ad.LocalAdUserHome";

    @EJB
    public PersistenceBeanClass em;

    public boolean validateUserDetails(String username, String password,
                                       Integer companyCode, String companyShortName)
            throws FinderException {
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(usr) FROM AdUser usr "
                    + "WHERE usr.usrName = ?1 AND usr.usrPassword = ?2 "
                    + "AND usr.usrAdCompany = ?3", companyShortName);
            query.setParameter(1, username);
            query.setParameter(2, this.encryptPassword(password));
            query.setParameter(3, companyCode);
            LocalAdUser entity = (LocalAdUser) query.getSingleResult();
            return entity == null ? false : true;
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String getConsolePass(String username, String password) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrName = ?1 AND usr.usrPassword = ?2");

            query.setParameter(1, username);
            query.setParameter(2, this.encryptPassword(password));
            LocalAdUser entity = (LocalAdUser) query.getSingleResult();

            return this.decryptPassword(entity.getUsrConsolePass());

        } catch (NoResultException ex) {
            Debug.print("EXCEPTION: NoResultException com.ejb.ad.LocalAdUserHome.getConsolePass(String username, String password)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdUserHome.getConsolePass(String username, String password)");
            throw ex;
        }
    }

    public LocalAdUser findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalAdUser entity = (LocalAdUser) em.find(new LocalAdUser(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        } catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findUsrAll(java.lang.Integer USR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrAdCompany = ?1");
            query.setParameter(1, USR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdUserHome.findUsrAll(java.lang.Integer USR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUsrByDepartmentHead(java.lang.String USR_DEPT, byte USR_HEAD, java.lang.Integer USR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrDept = ?1 AND usr.usrHead = ?2 AND usr.usrAdCompany = ?3");
            query.setParameter(1, USR_DEPT);
            query.setParameter(2, USR_HEAD);
            query.setParameter(3, USR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdUserHome.findUsrByDepartmentHead(java.lang.String USR_DEPT, byte USR_HEAD, java.lang.Integer USR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUsrByDepartment(java.lang.String USR_DEPT, java.lang.Integer USR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrDept = ?1 AND usr.usrAdCompany = ?2");
            query.setParameter(1, USR_DEPT);
            query.setParameter(2, USR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdUserHome.findUsrByDepartment(java.lang.String USR_DEPT, java.lang.Integer USR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalAdUser findByUsrName(java.lang.String USR_NM, java.lang.Integer USR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrName = ?1 AND usr.usrAdCompany = ?2");
            query.setParameter(1, USR_NM);
            query.setParameter(2, USR_AD_CMPNY);
            return (LocalAdUser) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print("EXCEPTION: NoResultException com.ejb.ad.LocalAdUserHome.findByUsrName(java.lang.String USR_NM, java.lang.Integer USR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception com.ejb.ad.LocalAdUserHome.findByUsrName(java.lang.String USR_NM, java.lang.Integer USR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalAdUser findByUsrName(java.lang.String USR_NM, Integer companyCode, String companyShortName) throws FinderException {
        Debug.print("LocalAdUserHome findByUsrName");
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(usr) FROM AdUser usr WHERE usr.usrName = ?1 AND usr.usrAdCompany = ?2", companyShortName);
            query.setParameter(1, USR_NM);
            query.setParameter(2, companyCode);
            return (LocalAdUser) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection getUsrByCriteria(java.lang.String jbossQl, java.lang.Object[] args) throws FinderException {

        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdUser create(String USR_NM, String USR_DEPT, String USR_DESC, String USR_PSTN, String USR_EML_ADDRSS,
                              byte USR_HEAD, byte USR_INSPCTR, String USR_PSSWRD, short USR_PSSWRD_EXPRTN_CODE,
                              short USR_PSSWRD_EXPRTN_DYS, short USR_PSSWRD_EXPRTN_ACCSS, short USR_PSSWRD_CURR_ACCSS,
                              Date USR_DT_FRM, Date USR_DT_TO, Integer USR_AD_CMPNY) throws CreateException {
        try {

            LocalAdUser entity = new LocalAdUser();

            Debug.print("AdUserBean create");
            entity.setUsrName(USR_NM);
            entity.setUsrDept(USR_DEPT);
            entity.setUsrDescription(USR_DESC);
            entity.setUsrPosition(USR_PSTN);
            entity.setUsrEmailAddress(USR_EML_ADDRSS);
            entity.setUsrHead(USR_HEAD);
            entity.setUsrInspector(USR_INSPCTR);
            entity.setUsrPassword(USR_PSSWRD);
            entity.setUsrPasswordExpirationCode(USR_PSSWRD_EXPRTN_CODE);
            entity.setUsrPasswordExpirationDays(USR_PSSWRD_EXPRTN_DYS);
            entity.setUsrPasswordExpirationAccess(USR_PSSWRD_EXPRTN_ACCSS);
            entity.setUsrPasswordCurrentAccess(USR_PSSWRD_CURR_ACCSS);
            entity.setUsrDateFrom(USR_DT_FRM);
            entity.setUsrDateTo(USR_DT_TO);
            entity.setUsrAdCompany(USR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    private String encryptPassword(String password) {

        String encPassword = "";

        try {

            String keyString = "Some things are better left unread.";
            StringReader key = new StringReader(keyString);
            key.mark(keyString.length() + 1);

            StringReader reader = new StringReader(password);
            byte[] data = new byte[password.getBytes().length];

            int r, k;
            int ctr = 0;
            while ((r = reader.read()) != -1) {

                if ((k = key.read()) == -1) {

                    key.reset();
                    k = key.read();
                }

                data[ctr] = (byte) (r ^ k);
                ctr++;
            }

            encPassword = Base64.getEncoder().encodeToString(data);

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return encPassword;
    }

    private String decryptPassword(String password) {

        String decPassword = "";

        try {

            String keyString = "Some things are better left unread.";
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
            byte[] base64 = Base64.getDecoder().decode(password);
            int length = key.length;
            if (base64.length < key.length) length = base64.length;

            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (key[i] ^ base64[i]);
            }

            decPassword = new String(data, StandardCharsets.UTF_8);

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return decPassword;
    }
}