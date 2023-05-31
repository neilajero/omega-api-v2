package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.util.ad.AdApprovalQueueDetails;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

import java.util.*;

@Stateless
public class LocalAdApprovalQueueHome {

    public static final String JNDI_NAME = "LocalAdApprovalQueueHome!com.ejb.ad.LocalAdApprovalQueueHome";

    @EJB
    public PersistenceBeanClass em;

    private Integer approvalQueueCode = null;
    private byte isForApproval = EJBCommon.FALSE;
    private String approvalQueueDoc = null;
    private Integer approvalQueueDocCode = null;
    private String approvalQueueDocNumber = null;
    private Date approvalQueueDate = null;
    private String approvalQueueAndOr = null;
    private byte isUserOr = EJBCommon.FALSE;
    private Integer branchCode = null;
    private Integer companyCode = null;

    private String approvalQueueDept = null;
    private String approvalQueueLevel = null;
    private String approvalQueueNextLevel = null;
    private Date approvalQueueApprovalDate = null;
    private String approvalQueueRequesterName = null;

    public LocalAdApprovalQueueHome() {
    }

    // FINDER METHODS
    public LocalAdApprovalQueue findByPrimaryKey(Integer pk)
            throws FinderException {
        try {
            LocalAdApprovalQueue entity = (LocalAdApprovalQueue) em.find(new LocalAdApprovalQueue(), pk);
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

    public LocalAdApprovalQueue findByPrimaryKey(Integer pk, String companyShortName)
            throws FinderException {
        try {
            LocalAdApprovalQueue entity = (LocalAdApprovalQueue)
                    em.findPerCompany(new LocalAdApprovalQueue(), pk, companyShortName);
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

    public Collection findByAqDocumentAndAqDocumentCode(String approvalQueueDoc, Integer approvalQueueDocCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq " +
                "WHERE aq.aqApproved = 0 " +
                "AND aq.aqDocument = ?1 " +
                "AND aq.aqDocumentCode = ?2 " +
                "AND aq.aqAdCompany = ?3";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findAllByAqDocumentAndAqDocumentCode(String approvalQueueDoc, Integer approvalQueueDocCode, Integer companyCode)
            throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDocument = ?1 AND aq.aqDocumentCode = ?2 AND aq.aqAdCompany = ?3");
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAqDocumentAndAqDocumentCodeAll(String approvalQueueDoc, Integer approvalQueueDocCode, Integer companyCode)
            throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDocument = ?1 AND aq.aqDocumentCode = ?2 AND aq.aqAdCompany = ?3 ORDER BY aqLevel ASC");
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdApprovalQueue findByAqDocumentAndAqDocumentCodeAndUsrName(String approvalQueueDoc, Integer approvalQueueDocCode, String username, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDocument = ?1 AND aq.aqDocumentCode = ?2 AND aq.adUser.usrName = ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, username);
            query.setParameter(4, companyCode);
            return (LocalAdApprovalQueue) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findListByAqDeptAndAqLevelAndAqDocumentCode(String approvalQueueDept, String approvalQueueLevel, Integer approvalQueueDocCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDepartment = ?1 AND aq.aqLevel = ?2 AND aq.aqDocumentCode = ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDept);
            query.setParameter(2, approvalQueueLevel);
            query.setParameter(3, approvalQueueDocCode);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdApprovalQueue findByAqDeptAndAqLevelAndAqDocumentCode(String approvalQueueDept, String approvalQueueLevel, Integer approvalQueueDocCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDepartment = ?1 AND aq.aqLevel = ?2 AND aq.aqDocumentCode = ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDept);
            query.setParameter(2, approvalQueueLevel);
            query.setParameter(3, approvalQueueDocCode);
            query.setParameter(4, companyCode);
            return (LocalAdApprovalQueue) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdApprovalQueue findByAqDeptAndAqLevelAndAqDocumentCode(String approvalQueueDept,
                                                                        String approvalQueueLevel, Integer approvalQueueDocCode, Integer companyCode,
                                                                        String companyShortName)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq WHERE aq.aqDepartment = ?1 "
                + "AND aq.aqLevel = ?2 AND aq.aqDocumentCode = ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQueryPerCompany(sqlCommand, companyShortName);
            query.setParameter(1, approvalQueueDept);
            query.setParameter(2, approvalQueueLevel);
            query.setParameter(3, approvalQueueDocCode);
            query.setParameter(4, companyCode);
            return (LocalAdApprovalQueue) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAqDocumentAndUsrCode(String approvalQueueDoc, Integer userCode, Integer branchCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq " +
                "WHERE aq.aqForApproval = 1 AND aq.aqApproved = 0 " +
                "AND aq.aqDocument = ?1 AND aq.adUser.usrCode = ?2 " +
                "AND aq.aqAdBranch = ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, userCode);
            query.setParameter(3, branchCode);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAqDocumentAndAqDocumentCodeGreaterThanAsc(String approvalQueueDoc, Integer approvalQueueDocCode, Integer approvalQueueCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq " +
                "WHERE aq.aqApproved = 0 AND aq.aqDocument = ?1 " +
                "AND aq.aqDocumentCode = ?2 AND aq.aqCode > ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, approvalQueueCode);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAqDocumentAndAqDocumentCodeLessThanDesc(String approvalQueueDoc, Integer approvalQueueDocCode, Integer approvalQueueCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq " +
                "WHERE aq.aqApproved = 0 AND aq.aqDocument = ?1 " +
                "AND aq.aqDocumentCode = ?2 AND aq.aqCode < ?3 AND aq.aqAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, approvalQueueDocCode);
            query.setParameter(3, approvalQueueCode);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection getAqByCriteria(String jbossQl, Object[] args, Integer LIMIT, Integer OFFSET)
            throws FinderException {
        try {
            Query query = em.createQuery(jbossQl);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            if (LIMIT > 0) {
                query.setMaxResults(LIMIT);
            }
            query.setFirstResult(OFFSET);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection getAqByCriteria(String jbossQl, Object[] args, Integer LIMIT, Integer OFFSET, String companyShortName)
            throws FinderException {
        try {
            Query query = em.createQueryPerCompany(jbossQl, companyShortName);
            int cnt = 1;
            for (Object data : args) {
                query.setParameter(cnt, data);
                cnt++;
            }
            if (LIMIT > 0) {
                query.setMaxResults(LIMIT);
            }
            query.setFirstResult(OFFSET);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAqDocumentAndUserName(String approvalQueueDoc, String username, Integer OFFSET, Integer LIMIT, Integer companyCode) {
        String sqlCommand = "SELECT OBJECT(aq) FROM AdApprovalQueue aq " +
                "WHERE aq.aqApproved = 0 AND aq.aqDocument = ?1 " +
                "AND aq.adUser.usrName = ?2 AND aq.aqAdCompany = ?5";
        try {

            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalQueueDoc);
            query.setParameter(2, username);
            query.setParameter(3, OFFSET);
            query.setParameter(4, LIMIT);
            query.setParameter(5, companyCode);
            query.setFirstResult(OFFSET);
            if (LIMIT > 0) {
                query.setMaxResults(LIMIT);
            }
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdApprovalQueue buildApprovalQueue(String companyShortName) throws CreateException {
        try {
            LocalAdApprovalQueue entity = new LocalAdApprovalQueue();
            Debug.print("AdApprovalQueueBean buildApprovalQueue");
            entity.setAqCode(approvalQueueCode);
            entity.setAqForApproval(isForApproval);
            entity.setAqDocument(approvalQueueDoc);
            entity.setAqDocumentCode(approvalQueueDocCode);
            entity.setAqDocumentNumber(approvalQueueDocNumber);
            entity.setAqDate(approvalQueueDate);
            entity.setAqAndOr(approvalQueueAndOr);
            entity.setAqUserOr(isUserOr);
            entity.setAqAdBranch(branchCode);
            entity.setAqAdCompany(companyCode);
            entity.setAqDepartment(approvalQueueDept);
            entity.setAqLevel(approvalQueueLevel);
            entity.setAqNextLevel(approvalQueueNextLevel);
            entity.setAqApprovedDate(approvalQueueApprovalDate);
            entity.setAqRequesterName(approvalQueueRequesterName);
            em.persist(entity, companyShortName);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdApprovalQueue buildApprovalQueue() throws CreateException {
        try {
            LocalAdApprovalQueue entity = new LocalAdApprovalQueue();
            Debug.print("AdApprovalQueueBean buildApprovalQueue");
            entity.setAqCode(approvalQueueCode);
            entity.setAqForApproval(isForApproval);
            entity.setAqDocument(approvalQueueDoc);
            entity.setAqDocumentCode(approvalQueueDocCode);
            entity.setAqDocumentNumber(approvalQueueDocNumber);
            entity.setAqDate(approvalQueueDate);
            entity.setAqAndOr(approvalQueueAndOr);
            entity.setAqUserOr(isUserOr);
            entity.setAqAdBranch(branchCode);
            entity.setAqAdCompany(companyCode);
            entity.setAqDepartment(approvalQueueDept);
            entity.setAqLevel(approvalQueueLevel);
            entity.setAqNextLevel(approvalQueueNextLevel);
            entity.setAqApprovedDate(approvalQueueApprovalDate);
            entity.setAqRequesterName(approvalQueueRequesterName);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdApprovalQueueHome AqCode(Integer approvalQueueCode) {
        this.approvalQueueCode = approvalQueueCode;
        return this;
    }

    public LocalAdApprovalQueueHome AqForApproval(byte isForApproval) {
        this.isForApproval = isForApproval;
        return this;
    }

    public LocalAdApprovalQueueHome AqDocument(String approvalQueueDoc) {
        this.approvalQueueDoc = approvalQueueDoc;
        return this;
    }

    public LocalAdApprovalQueueHome AqDocumentCode(Integer approvalQueueDocCode) {
        this.approvalQueueDocCode = approvalQueueDocCode;
        return this;
    }

    public LocalAdApprovalQueueHome AqDocumentNumber(String approvalQueueDocNumber) {
        this.approvalQueueDocNumber = approvalQueueDocNumber;
        return this;
    }

    public LocalAdApprovalQueueHome AqDate(Date approvalQueueDate) {
        this.approvalQueueDate = approvalQueueDate;
        return this;
    }

    public LocalAdApprovalQueueHome AqAndOr(String approvalQueueAndOr) {
        this.approvalQueueAndOr = approvalQueueAndOr;
        return this;
    }

    public LocalAdApprovalQueueHome AqUserOr(byte isUserOr) {
        this.isUserOr = isUserOr;
        return this;
    }

    public LocalAdApprovalQueueHome AqDepartment(String approvalQueueDept) {
        this.approvalQueueDept = approvalQueueDept;
        return this;
    }

    public LocalAdApprovalQueueHome AqLevel(String approvalQueueLevel) {
        this.approvalQueueLevel = approvalQueueLevel;
        return this;
    }

    public LocalAdApprovalQueueHome AqNextLevel(String approvalQueueNextLevel) {
        this.approvalQueueNextLevel = approvalQueueNextLevel;
        return this;
    }

    public LocalAdApprovalQueueHome AqApprovedDate(Date approvalQueueApprovalDate) {
        this.approvalQueueApprovalDate = approvalQueueApprovalDate;
        return this;
    }

    public LocalAdApprovalQueueHome AqRequesterName(String approvalQueueRequesterName) {
        this.approvalQueueRequesterName = approvalQueueRequesterName;
        return this;
    }

    public LocalAdApprovalQueueHome AqAdBranch(Integer branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public LocalAdApprovalQueueHome AqAdCompany(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public LocalAdApprovalQueue create(Integer AQ_CODE,
                             byte AQ_FOR_APPRVL, String AQ_DCMNT, Integer AQ_DCMNT_CODE, String AQ_DCMNT_NMBR, Date AQ_DT, String AQ_AND_OR,
                             byte AQ_USR_OR, Integer AQ_AD_BRNCH, Integer AQ_AD_CMPNY)
            throws CreateException {

        Debug.print("AdApprovalQueueBean create");

        LocalAdApprovalQueue entity = new LocalAdApprovalQueue();

        entity.setAqCode(AQ_CODE);
        entity.setAqForApproval(AQ_FOR_APPRVL);
        entity.setAqDocument(AQ_DCMNT);
        entity.setAqDocumentCode(AQ_DCMNT_CODE);
        entity.setAqDocumentNumber(AQ_DCMNT_NMBR);
        entity.setAqDate(AQ_DT);
        entity.setAqAndOr(AQ_AND_OR);
        entity.setAqUserOr(AQ_USR_OR);
        entity.setAqAdBranch(AQ_AD_BRNCH);
        entity.setAqAdCompany(AQ_AD_CMPNY);

        em.persist(entity);
        return entity;
    }

    public LocalAdApprovalQueue create(
            byte AQ_FOR_APPRVL, String AQ_DCMNT, Integer AQ_DCMNT_CODE, String AQ_DCMNT_NMBR, Date AQ_DT, String AQ_AND_OR,
            byte AQ_USR_OR, Integer AQ_AD_BRNCH, Integer AQ_AD_CMPNY)
            throws CreateException {

        Debug.print("AdApprovalQueueBean create");

        LocalAdApprovalQueue entity = new LocalAdApprovalQueue();

        entity.setAqForApproval(AQ_FOR_APPRVL);
        entity.setAqDocument(AQ_DCMNT);
        entity.setAqDocumentCode(AQ_DCMNT_CODE);
        entity.setAqDocumentNumber(AQ_DCMNT_NMBR);
        entity.setAqDate(AQ_DT);
        entity.setAqAndOr(AQ_AND_OR);
        entity.setAqUserOr(AQ_USR_OR);
        entity.setAqAdBranch(AQ_AD_BRNCH);
        entity.setAqAdCompany(AQ_AD_CMPNY);

        em.persist(entity);
        return entity;
    }
}