package com.ejb;

import com.util.EJBCommon;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import jakarta.ejb.CreateException;
import jakarta.ejb.Stateless;
import jakarta.persistence.StoredProcedureQuery;

import java.util.Arrays;

@Stateless
public class PersistenceBeanClass {

    private static final String TwentyFourHDS = "24hDS";
    private static final String TaguigLGUDS = "TaguigLGUDS";
    private static final String CourtsDS = "CourtsDS";

    public EntityManager emgr = null;

    @PersistenceContext(unitName = TwentyFourHDS)
    private EntityManager em24H;

    @PersistenceContext(unitName = TaguigLGUDS)
    private EntityManager emTaguigLGU;

    @PersistenceContext(unitName = CourtsDS)
    private EntityManager emCourts;

    public Object find(Object entityClass, int code) {
        try {
            return emgr.find(entityClass.getClass(), code);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public Object findPerCompany(Object entityClass, int code, String companyShortName) {
        try {
            EntityManager em = this.getEntityManager(companyShortName);
            return em.find(entityClass.getClass(), code);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void persist(Object obj) throws CreateException {
        try {
            emgr.persist(obj);
        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public void persist(Object obj, String companyShortName) throws CreateException {
        try {
            EntityManager em = this.getEntityManager(companyShortName);
            em.persist(obj);
        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public Query createQuery(String sql) {

        try {
            Query q = emgr.createQuery(sql);
            q.setHint("org.hibernate.cacheable", true);
            return q;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public Query createQueryPerCompany(String sql, String companyShortName) {
        try {
            EntityManager em = this.getEntityManager(companyShortName);
            Query q = em.createQuery(sql);
            q.setHint("org.hibernate.cacheable", true);
            return q;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public Query createQueryPerCompanyIntegerClass(String sql, String companyShortName) {
        try {
            EntityManager em = this.getEntityManager(companyShortName);
            Query q = em.createQuery(sql, Integer.class);
            q.setHint("org.hibernate.cacheable", true);
            return q;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public Query createNativeQuery(String sql) {
        try {
            Query q = emgr.createNativeQuery(sql);
            q.setHint("org.hibernate.cacheable", true);
            return q;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public StoredProcedureQuery createStoredProcedureQuery(String spName) {
        try {
            return emgr.createStoredProcedureQuery(spName);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public StoredProcedureQuery createStoredProcedureQuery(String spName, String companyShortName) {
        try {
            EntityManager em = this.getEntityManager(companyShortName);
            return em.createStoredProcedureQuery(spName);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void remove(Object obj) throws RemoveException {
        try {
            emgr.remove(emgr.contains(obj) ? obj : emgr.merge(obj));
        }
        catch (Exception ex) {
            throw new RemoveException(ex.getMessage());
        }
    }

    public void merge(Object obj) {
        emgr.merge(obj);
    }

    private EntityManager getEntityManager(String companyShortName) {
        EntityManager entityManager = null;
        String[] twentyFourHCompanies = {"gnj","mrc","fvj","afm","ogj","mjg","hoa"}; // This is for 24H only.
        if (EJBCommon.validateInputData(companyShortName)) {
            if (companyShortName.equalsIgnoreCase("courts")) {
                entityManager = emCourts;
            } else if (companyShortName.equalsIgnoreCase("tglgu")) {
                // Filter for Taguig LGU company
                entityManager = emTaguigLGU;
            } else {
                int index = Arrays.asList(twentyFourHCompanies).indexOf(companyShortName.toLowerCase());
                if (index >= 0) {
                    // Filter 24H company code
                    entityManager = em24H;
                }
            }
        }
        return entityManager;
    }
}