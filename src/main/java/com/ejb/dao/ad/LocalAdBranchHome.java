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

@Stateless
public class LocalAdBranchHome {

    public static final String JNDI_NAME = "LocalAdBranchHome!com.ejb.ad.LocalAdBranchHome";

    @EJB
    public PersistenceBeanClass em;

    private String BR_BRNCH_CODE = null;
    private String BR_NM = null;
    private String BR_DESC = null;
    private String BR_TYP = null;
    private byte BR_HD_QTR = EJBCommon.FALSE;
    private String BR_ADDRSS = null;
    private String BR_CNTCT_PRSN = null;
    private String BR_CNTCT_NMBR = null;
    private char BR_DWNLD_STATUS = 'N';
    private byte BR_APPLY_SHPPNG = EJBCommon.FALSE;
    private double BR_PRCNT_MRKP = 0d;
    private Integer BR_AD_CMPNY = null;

    //Additional fields
    private String BR_REGION_NM = null;
    private byte BR_IS_BAU_BR = EJBCommon.FALSE;
    private String BR_BAU_NM = null;

    // FINDER METHODS
    public LocalAdBranch findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalAdBranch entity = (LocalAdBranch) em.find(new LocalAdBranch(), pk);
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

    public java.util.Collection findBrAll(java.lang.Integer AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brAdCompany = ?1");
            query.setParameter(1, AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findBrAll(java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br WHERE br.brAdCompany = ?1", companyShortName);
            query.setParameter(1, AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findAll(java.lang.Integer AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranchModel br WHERE br.brAdCompany = ?1");
            query.setParameter(1, AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findAll(java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranchModel br WHERE br.brAdCompany = ?1",
                    companyShortName);
            query.setParameter(1, AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch findByBrBranchCode(java.lang.String BR_BRNCH_CODE, java.lang.Integer AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brBranchCode = ?1 AND br.brAdCompany = ?2");
            query.setParameter(1, BR_BRNCH_CODE);
            query.setParameter(2, AD_CMPNY);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch findByBrBranchCode(String branchCode, Integer companyCode, String companyShortName) throws FinderException {
        Debug.print("LocalAdBranchHome findByBrBranchCode");
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br WHERE br.brBranchCode = ?1 AND br.brAdCompany = ?2", companyShortName);
            query.setParameter(1, branchCode);
            query.setParameter(2, companyCode);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch findByBrName(java.lang.String BR_BRNCH_NM, java.lang.Integer AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brName = ?1 AND br.brAdCompany = ?2");
            query.setParameter(1, BR_BRNCH_NM);
            query.setParameter(2, AD_CMPNY);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch findByBrName(java.lang.String BR_BRNCH_NM,
                                      java.lang.Integer AD_CMPNY, String companyShortName) throws FinderException {
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(br) FROM AdBranch br "
                    + "WHERE br.brName = ?1 AND br.brAdCompany = ?2", companyShortName);
            query.setParameter(1, BR_BRNCH_NM);
            query.setParameter(2, AD_CMPNY);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch findByBrHeadQuarter(java.lang.Integer AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brHeadQuarter = 1 AND br.brAdCompany = ?1");
            query.setParameter(1, AD_CMPNY);
            return (LocalAdBranch) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findByBrCodeAndBrDownloadStatus(java.lang.Integer BR_CODE, char BR_DWNLD_STATUS, java.lang.Integer BR_AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brCode = ?1 AND br.brDownloadStatus = ?2 AND br.brAdCompany = ?3");
            query.setParameter(1, BR_CODE);
            query.setParameter(2, BR_DWNLD_STATUS);
            query.setParameter(3, BR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findByBrNewAndUpdated(java.lang.Integer BR_AD_CMPNY, char NEW, char UPDATED,
                                                      char DOWNLOADED_UPDATED) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(br) FROM AdBranch br WHERE br.brAdCompany = ?1 AND (br.brDownloadStatus = ?2 OR br.brDownloadStatus = ?3 OR br.brDownloadStatus = ?4)");
            query.setParameter(1, BR_AD_CMPNY);
            query.setParameter(2, NEW);
            query.setParameter(3, UPDATED);
            query.setParameter(4, DOWNLOADED_UPDATED);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdBranch buildBranch() throws CreateException {
        try {

            LocalAdBranch entity = new LocalAdBranch();

            Debug.print("AdBranchBean buildBranch");

            entity.setBrBranchCode(BR_BRNCH_CODE);
            entity.setBrName(BR_NM);
            entity.setBrDescription(BR_DESC);
            entity.setBrType(BR_TYP);
            entity.setBrHeadQuarter(BR_HD_QTR);
            entity.setBrAddress(BR_ADDRSS);
            entity.setBrContactPerson(BR_CNTCT_PRSN);
            entity.setBrContactNumber(BR_CNTCT_NMBR);
            entity.setBrDownloadStatus(BR_DWNLD_STATUS);
            entity.setBrApplyShipping(BR_APPLY_SHPPNG);
            entity.setBrPercentMarkup(BR_PRCNT_MRKP);
            entity.setBrAdCompany(BR_AD_CMPNY);
            entity.setBrRegionName(BR_REGION_NM);
            entity.setBrIsBauBranch(BR_IS_BAU_BR);
            entity.setBrBauName(BR_BAU_NM);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdBranchHome BrBranchCode(String BR_BRNCH_CODE) {
        this.BR_BRNCH_CODE = BR_BRNCH_CODE;
        return this;
    }

    public LocalAdBranchHome BrName(String BR_NM) {
        this.BR_NM = BR_NM;
        return this;
    }

    public LocalAdBranchHome BrDescription(String BR_DESC) {
        this.BR_DESC = BR_DESC;
        return this;
    }

    public LocalAdBranchHome BrType(String BR_TYP) {
        this.BR_TYP = BR_TYP;
        return this;
    }

    public LocalAdBranchHome BrHeadQuarter(byte BR_HD_QTR) {
        this.BR_HD_QTR = BR_HD_QTR;
        return this;
    }

    public LocalAdBranchHome BrAddress(String BR_ADDRSS) {
        this.BR_ADDRSS = BR_ADDRSS;
        return this;
    }

    public LocalAdBranchHome BrContactPerson(String BR_CNTCT_PRSN) {
        this.BR_CNTCT_PRSN = BR_CNTCT_PRSN;
        return this;
    }

    public LocalAdBranchHome BrContactNumber(String BR_CNTCT_NMBR) {
        this.BR_CNTCT_NMBR = BR_CNTCT_NMBR;
        return this;
    }

    public LocalAdBranchHome BrDownloadStatus(char BR_DWNLD_STATUS) {
        this.BR_DWNLD_STATUS = BR_DWNLD_STATUS;
        return this;
    }

    public LocalAdBranchHome BrApplyShipping(byte BR_APPLY_SHPPNG) {
        this.BR_APPLY_SHPPNG = BR_APPLY_SHPPNG;
        return this;
    }

    public LocalAdBranchHome BrPercentMarkup(double BR_PRCNT_MRKP) {
        this.BR_PRCNT_MRKP = BR_PRCNT_MRKP;
        return this;
    }

    public LocalAdBranchHome BrAdCompany(Integer BR_AD_CMPNY) {
        this.BR_AD_CMPNY = BR_AD_CMPNY;
        return this;
    }

    public LocalAdBranchHome BrRegionName(String BR_REGION_NM) {
        this.BR_REGION_NM = BR_REGION_NM;
        return this;
    }

    public LocalAdBranchHome BrIsBauBranch(byte BR_IS_BAU_BR) {
        this.BR_IS_BAU_BR = BR_IS_BAU_BR;
        return this;
    }

    public LocalAdBranchHome BrBauName(String BR_BAU_NM) {
        this.BR_BAU_NM = BR_BAU_NM;
        return this;
    }

    // CREATE METHODS
    public LocalAdBranch create(Integer BR_CODE, String BR_BRNCH_CODE, String BR_NM, String BR_DESC,
                                String BR_TYP, byte BR_HD_QTR, String BR_ADDRSS, String BR_CNTCT_PRSN, String BR_CNTCT_NMBR,
                                char BR_DWNLD_STATUS, byte BR_APPLY_SHPPNG, double BR_PRCNT_MRKP, Integer BR_AD_CMPNY)
            throws CreateException {
        try {

            LocalAdBranch entity = new LocalAdBranch();

            Debug.print("AdBranchBean create");

            entity.setBrCode(BR_CODE);
            entity.setBrBranchCode(BR_BRNCH_CODE);
            entity.setBrName(BR_NM);
            entity.setBrDescription(BR_DESC);
            entity.setBrType(BR_TYP);
            entity.setBrHeadQuarter(BR_HD_QTR);
            entity.setBrAddress(BR_ADDRSS);
            entity.setBrContactPerson(BR_CNTCT_PRSN);
            entity.setBrContactNumber(BR_CNTCT_NMBR);
            entity.setBrDownloadStatus(BR_DWNLD_STATUS);
            entity.setBrApplyShipping(BR_APPLY_SHPPNG);
            entity.setBrPercentMarkup(BR_PRCNT_MRKP);
            entity.setBrAdCompany(BR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdBranch create(String BR_BRNCH_CODE, String BR_NM, String BR_DESC, String BR_TYP,
                                byte BR_HD_QTR, String BR_ADDRSS, String BR_CNTCT_PRSN, String BR_CNTCT_NMBR, char BR_DWNLD_STATUS,
                                byte BR_APPLY_SHPPNG, double BR_PRCNT_MRKP, Integer BR_AD_CMPNY) throws CreateException {
        try {

            LocalAdBranch entity = new LocalAdBranch();

            Debug.print("AdBranchBean create");

            entity.setBrBranchCode(BR_BRNCH_CODE);
            entity.setBrName(BR_NM);
            entity.setBrDescription(BR_DESC);
            entity.setBrType(BR_TYP);
            entity.setBrHeadQuarter(BR_HD_QTR);
            entity.setBrAddress(BR_ADDRSS);
            entity.setBrContactPerson(BR_CNTCT_PRSN);
            entity.setBrContactNumber(BR_CNTCT_NMBR);
            entity.setBrDownloadStatus(BR_DWNLD_STATUS);
            entity.setBrApplyShipping(BR_APPLY_SHPPNG);
            entity.setBrPercentMarkup(BR_PRCNT_MRKP);
            entity.setBrAdCompany(BR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}