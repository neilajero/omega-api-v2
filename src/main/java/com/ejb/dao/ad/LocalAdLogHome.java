package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdLog;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

import java.util.Date;
import java.util.Collection;

@Stateless
public class LocalAdLogHome {

    public static final String JNDI_NAME = "LocalAdLogHome!com.ejb.ad.LocalAdLogHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalAdLogHome() { }

    // FINDER METHODS
    public LocalAdLog findByPrimaryKey(Integer pk) throws FinderException {
        try {
            LocalAdLog entity = (LocalAdLog) em.find(new LocalAdLog(), pk);
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

    public Collection findByLogModuleAndLogModuleKey(String logModule, Integer logModuleKey) {

        try {
            Query query = em.createQuery("SELECT OBJECT(log) FROM AdLog log WHERE log.logModule = ?1 AND log.logModuleKey = ?2");
            query.setParameter(1, logModule);
            query.setParameter(2, logModuleKey);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception LocalAdLogHome.findByLogModuleAndLogModuleKey(String logModule, Integer logModuleKey)");
            throw ex;
        }
    }

    public Collection findByLogUsername(String logUsername) {

        try {
            Query query = em.createQuery("SELECT OBJECT(log) FROM AdLog log WHERE log.logUsername = ?1");
            query.setParameter(1, logUsername);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print("EXCEPTION: Exception LocalAdLogHome.findByLogUsername(String logUsername)");
            throw ex;
        }
    }

    // CREATE METHODS
    public LocalAdLog create(Integer logCode, Date logDate, String logUsername, String logAction,
                             String logDesc, String logModule, Integer logModuleKey) throws CreateException {
        try {
            LocalAdLog entity = new LocalAdLog();
            Debug.print("AdLogBean create");
            entity.setLogCode(logCode);
            entity.setLogDate(logDate);
            entity.setLogUsername(logUsername);
            entity.setLogAction(logAction);
            entity.setLogDescription(logDesc);
            entity.setLogModule(logModule);
            entity.setLogModuleKey(logModuleKey);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdLog create(Date logDate, String logUsername, String logAction, String logDesc, String logModule, Integer logModuleKey) throws CreateException {
        try {
            LocalAdLog entity = new LocalAdLog();
            Debug.print("AdLogBean create");
            entity.setLogDate(logDate);
            entity.setLogUsername(logUsername);
            entity.setLogAction(logAction);
            entity.setLogDescription(logDesc);
            entity.setLogModule(logModule);
            entity.setLogModuleKey(logModuleKey);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}