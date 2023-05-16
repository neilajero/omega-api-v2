package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranch;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class LocalAdBranchHome {

    public static final String JNDI_NAME = "LocalAdBranchHome!com.ejb.ad.LocalAdBranchHome";

    @EJB
    public PersistenceBeanClass em;

    private String branchCode = null;
    private String branchName = null;
    private String branchDesc = null;
    private String branchType = null;
    private byte branchHq = EJBCommon.FALSE;
    private String branchAddress = null;
    private String branchContactPerson = null;
    private String branchContactNumber = null;
    private char branchDownloadStatus = 'N';
    private byte branchApplyShipping = EJBCommon.FALSE;
    private double branchPercentMarkup = 0d;
    private Integer companyCode = null;
    private String branchRegionName = null;
    private byte branchIsBauBranch = EJBCommon.FALSE;
    private String branchBauName = null;

    // FINDER METHODS
    public LocalAdBranch findByPrimaryKey(Integer pk) throws FinderException {
        try {
            LocalAdBranch entity = (LocalAdBranch) em.find(new LocalAdBranch(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        } catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public Collection<LocalAdBranch> findBrAll(Integer paramCompanyCode) throws FinderException {
        Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brAdCompany = ?1");
        query.setParameter(1, paramCompanyCode);
        return query.getResultList();
    }

    public Collection<LocalAdBranch> findBrAll(Integer paramCompanyCode, String paramCompanyShortName) throws FinderException {
        Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br WHERE br.brAdCompany = ?1", paramCompanyShortName);
        query.setParameter(1, paramCompanyCode);
        return query.getResultList();
    }

    public List<String> findBrCodeByBrAdCompany(Integer paramCompanyCode, String paramCompanyShortName) throws FinderException {
        List<String> retVal = new ArrayList<>();
        Query query = em.createQueryPerCompanyIntegerClass("SELECT br.brCode FROM AdBranch br WHERE br.brAdCompany = ?1", paramCompanyShortName);
        query.setParameter(1, paramCompanyCode);
        List<Integer> resultList = query.getResultList();
        for (Integer i : resultList) {
            retVal.add(i.toString());
        }
        return retVal;
    }

    public LocalAdBranch findByBrBranchCode(String paramBranchCode, Integer paramCompanyCode) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brBranchCode = ?1 AND br.brAdCompany = ?2");
            query.setParameter(1, paramBranchCode);
            query.setParameter(2, paramCompanyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdBranch findByBrBranchCode(String paramBranchCode, Integer paramCompanyCode, String paramCompanyShortName) throws FinderException {
        Debug.print("LocalAdBranchHome findByBrBranchCode");
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br WHERE br.brBranchCode = ?1 AND br.brAdCompany = ?2", paramCompanyShortName);
            query.setParameter(1, paramBranchCode);
            query.setParameter(2, paramCompanyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdBranch findByBrName(String paramBranchName, Integer paramCompanyCode) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brName = ?1 AND br.brAdCompany = ?2");
            query.setParameter(1, paramBranchName);
            query.setParameter(2, paramCompanyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdBranch findByBrName(String paramBranchName, Integer paramCompanyCode, String paramCompanyShortName) throws FinderException {
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br "
                    + "WHERE br.brName = ?1 AND br.brAdCompany = ?2", paramCompanyShortName);
            query.setParameter(1, paramBranchName);
            query.setParameter(2, paramCompanyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdBranch findByBrHeadQuarter(Integer paramCompanyCode) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brHeadQuarter = 1 AND br.brAdCompany = ?1");
            query.setParameter(1, paramCompanyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdBranch buildBranch() throws CreateException {
        Debug.print("AdBranchBean buildBranch");
        try {
            LocalAdBranch entity = new LocalAdBranch();
            entity.setBrBranchCode(branchCode);
            entity.setBrName(branchName);
            entity.setBrDescription(branchDesc);
            entity.setBrType(branchType);
            entity.setBrHeadQuarter(branchHq);
            entity.setBrAddress(branchAddress);
            entity.setBrContactPerson(branchContactPerson);
            entity.setBrContactNumber(branchContactNumber);
            entity.setBrDownloadStatus(branchDownloadStatus);
            entity.setBrApplyShipping(branchApplyShipping);
            entity.setBrPercentMarkup(branchPercentMarkup);
            entity.setBrAdCompany(companyCode);
            entity.setBrRegionName(branchRegionName);
            entity.setBrIsBauBranch(branchIsBauBranch);
            entity.setBrBauName(branchBauName);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdBranchHome BrBranchCode(String branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public LocalAdBranchHome BrName(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public LocalAdBranchHome BrDescription(String branchDesc) {
        this.branchDesc = branchDesc;
        return this;
    }

    public LocalAdBranchHome BrType(String branchType) {
        this.branchType = branchType;
        return this;
    }

    public LocalAdBranchHome BrHeadQuarter(byte branchHq) {
        this.branchHq = branchHq;
        return this;
    }

    public LocalAdBranchHome BrAddress(String branchAddress) {
        this.branchAddress = branchAddress;
        return this;
    }

    public LocalAdBranchHome BrContactPerson(String branchContactPerson) {
        this.branchContactPerson = branchContactPerson;
        return this;
    }

    public LocalAdBranchHome BrContactNumber(String branchContactNumber) {
        this.branchContactNumber = branchContactNumber;
        return this;
    }

    public LocalAdBranchHome BrDownloadStatus(char branchDownloadStatus) {
        this.branchDownloadStatus = branchDownloadStatus;
        return this;
    }

    public LocalAdBranchHome BrApplyShipping(byte branchApplyShipping) {
        this.branchApplyShipping = branchApplyShipping;
        return this;
    }

    public LocalAdBranchHome BrPercentMarkup(double branchPercentMarkup) {
        this.branchPercentMarkup = branchPercentMarkup;
        return this;
    }

    public LocalAdBranchHome BrAdCompany(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public LocalAdBranchHome BrRegionName(String branchRegionName) {
        this.branchRegionName = branchRegionName;
        return this;
    }

    public LocalAdBranchHome BrIsBauBranch(byte branchIsBauBranch) {
        this.branchIsBauBranch = branchIsBauBranch;
        return this;
    }

    public LocalAdBranchHome BrBauName(String branchBauName) {
        this.branchBauName = branchBauName;
        return this;
    }

    // CREATE METHODS
    public LocalAdBranch create(Integer BR_CODE, String branchCode, String branchName, String branchDesc,
                                String branchType, byte branchHq, String branchAddress, String branchContactPerson,
                                String branchContactNumber, char branchDownloadStatus, byte branchApplyShipping,
                                double branchPercentMarkup, Integer companyCode)
            throws CreateException {
        Debug.print("AdBranchBean create");
        try {
            LocalAdBranch entity = new LocalAdBranch();
            entity.setBrCode(BR_CODE);
            entity.setBrBranchCode(branchCode);
            entity.setBrName(branchName);
            entity.setBrDescription(branchDesc);
            entity.setBrType(branchType);
            entity.setBrHeadQuarter(branchHq);
            entity.setBrAddress(branchAddress);
            entity.setBrContactPerson(branchContactPerson);
            entity.setBrContactNumber(branchContactNumber);
            entity.setBrDownloadStatus(branchDownloadStatus);
            entity.setBrApplyShipping(branchApplyShipping);
            entity.setBrPercentMarkup(branchPercentMarkup);
            entity.setBrAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdBranch create(String branchCode, String branchName, String branchDesc, String branchType,
                                byte branchHq, String branchAddress, String branchContactPerson, String branchContactNumber,
                                char branchDownloadStatus, byte branchApplyShipping, double branchPercentMarkup,
                                Integer companyCode) throws CreateException {
        Debug.print("AdBranchBean create");
        try {
            LocalAdBranch entity = new LocalAdBranch();
            entity.setBrBranchCode(branchCode);
            entity.setBrName(branchName);
            entity.setBrDescription(branchDesc);
            entity.setBrType(branchType);
            entity.setBrHeadQuarter(branchHq);
            entity.setBrAddress(branchAddress);
            entity.setBrContactPerson(branchContactPerson);
            entity.setBrContactNumber(branchContactNumber);
            entity.setBrDownloadStatus(branchDownloadStatus);
            entity.setBrApplyShipping(branchApplyShipping);
            entity.setBrPercentMarkup(branchPercentMarkup);
            entity.setBrAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}