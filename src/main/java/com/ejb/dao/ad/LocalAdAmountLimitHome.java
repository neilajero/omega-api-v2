package com.ejb.dao.ad;

import java.util.Collection;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdAmountLimit;
import jakarta.ejb.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdAmountLimitHome {

    public static final String JNDI_NAME = "LocalAdAmountLimitHome!com.ejb.ad.LocalAdAmountLimitHome";

    @EJB
    public PersistenceBeanClass em;

    private String department = null;
    private double amountLimit = 0d;
    private String andOr = null;
    private Integer companyCode = null;

    public LocalAdAmountLimitHome() { }

    public LocalAdAmountLimit findByPrimaryKey(Integer amountLimitCode)
            throws FinderException {
        try {
            LocalAdAmountLimit entity = (LocalAdAmountLimit) em.find(new LocalAdAmountLimit(), amountLimitCode);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        } catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdAmountLimit findByPrimaryKey(Integer amountLimitCode, String companyShortName)
            throws FinderException {
        try {
            LocalAdAmountLimit entity = (LocalAdAmountLimit) em
                    .findPerCompany(new LocalAdAmountLimit(), amountLimitCode, companyShortName);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        } catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdAmountLimit findByAdcTypeAndAuTypeAndUsrName(String approvalDocumentType, String approvalUserType, String username, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.adApprovalDocument.adcType = ?1 " +
                "AND au.auType = ?2 " +
                "AND au.adUser.usrName = ?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalDocumentType);
            query.setParameter(2, approvalUserType);
            query.setParameter(3, username);
            query.setParameter(4, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
    }

    public LocalAdAmountLimit findByCoaCodeAndAuTypeAndUsrName(Integer chartOfAccountCode, String approvalUserType, String username, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.adApprovalCoaLine.glChartOfAccount.coaCode = ?1 " +
                "AND au.auType = ?2 " +
                "AND au.adUser.usrName = ?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, chartOfAccountCode);
            query.setParameter(2, approvalUserType);
            query.setParameter(3, username);
            query.setParameter(4, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAdcTypeAndGreaterThanCalAmountLimit(String approvalDocumentType, double amountLimit, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal " +
                "WHERE cal.adApprovalDocument.adcType = ?1 " +
                "AND cal.calAmountLimit > ?2 " +
                "AND cal.calAdCompany = ?3";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalDocumentType);
            query.setParameter(2, amountLimit);
            query.setParameter(3, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByCoaCodeAndGreaterThanCalAmountLimit(Integer chartOfAccountCode, double amountLimit, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal " +
                "WHERE cal.adApprovalCoaLine.glChartOfAccount.coaCode = ?1 " +
                "AND cal.calAmountLimit > ?2 " +
                "AND cal.calAdCompany = ?3";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, chartOfAccountCode);
            query.setParameter(2, amountLimit);
            query.setParameter(3, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findByAdcCode(Integer approvalDocumentCode, Integer companyCode) {
        Query query = em.createQuery("SELECT OBJECT(cal) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal WHERE adc.adcCode = ?1 AND cal.calAdCompany = ?2");
        query.setParameter(1, approvalDocumentCode);
        query.setParameter(2, companyCode);
        return query.getResultList();
    }

    public Collection findByAclCode(Integer approvalCoaLineCode, Integer companyCode)
            throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(cal) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal WHERE acl.aclCode = ?1 AND cal.calAdCompany = ?2");
            query.setParameter(1, approvalCoaLineCode);
            query.setParameter(2, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findByCalDeptAndCalAmountLimitAndAdcCode(String department, double amountLimit, Integer approvalDocumentCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal " +
                "WHERE cal.calDept=?1 " +
                "AND cal.calAmountLimit=?2 " +
                "AND adc.adcCode=?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, department);
            query.setParameter(2, amountLimit);
            query.setParameter(3, approvalDocumentCode);
            query.setParameter(4, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findByCalDeptAndCalAmountLimitAndAdcCode(String department, double amountLimit,
                                                                       Integer approvalDocumentCode, Integer companyCode,
                                                                       String companyShortName)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdApprovalDocument adc, IN(adc.adAmountLimits) cal " +
                "WHERE cal.calDept=?1 " +
                "AND cal.calAmountLimit=?2 " +
                "AND adc.adcCode=?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQueryPerCompany(sqlCommand, companyShortName);
            query.setParameter(1, department);
            query.setParameter(2, amountLimit);
            query.setParameter(3, approvalDocumentCode);
            query.setParameter(4, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findByCalAmountLimitAndAclCode(double amountLimit, Integer approvalCoaLineCode, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal " +
                "WHERE cal.calAmountLimit=?1 " +
                "AND acl.aclCode=?2 " +
                "AND cal.calAdCompany = ?3";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, amountLimit);
            query.setParameter(2, approvalCoaLineCode);
            query.setParameter(3, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findByCalAmountLimitAndAclCode(double amountLimit, Integer approvalCoaLineCode,
                                                             Integer companyCode, String companyShortName)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdApprovalCoaLine acl, IN(acl.adAmountLimits) cal " +
                "WHERE cal.calAmountLimit=?1 " +
                "AND acl.aclCode=?2 " +
                "AND cal.calAdCompany = ?3";
        try {
            Query query = em.createQueryPerCompany(sqlCommand, companyShortName);
            query.setParameter(1, amountLimit);
            query.setParameter(2, approvalCoaLineCode);
            query.setParameter(3, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findAmountLimitPerApprovalUser(String department, String approvalDocumentType,
                                                             String approvalUserType, String username, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.calDept=?1 " +
                "AND cal.adApprovalDocument.adcType = ?2 " +
                "AND au.auType = ?3 " +
                "AND au.adUser.usrName = ?4 " +
                "AND cal.calAdCompany = ?5";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, department);
            query.setParameter(2, approvalDocumentType);
            query.setParameter(3, approvalUserType);
            query.setParameter(4, username);
            query.setParameter(5, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findAmountLimitPerApprovalUser(
            String department, String approvalDocumentType,String approvalUserType,
            String username, Integer companyCode, String companyShortName)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.calDept=?1 " +
                "AND cal.adApprovalDocument.adcType = ?2 " +
                "AND au.auType = ?3 " +
                "AND au.adUser.usrName = ?4 " +
                "AND cal.calAdCompany = ?5";
        try {
            Query query = em.createQueryPerCompany(sqlCommand, companyShortName);
            query.setParameter(1, department);
            query.setParameter(2, approvalDocumentType);
            query.setParameter(3, approvalUserType);
            query.setParameter(4, username);
            query.setParameter(5, companyCode);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimit findByAmountCalDeptAdcTypeAndAuTypeAndUsrName(String department, String approvalDocumentType,
                                                                            String approvalUserType, String username, double amountLimit, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.calDept=?1 " +
                "AND cal.adApprovalDocument.adcType = ?2 " +
                "AND au.auType = ?3 " +
                "AND au.adUser.usrName = ?4 " +
                "AND cal.calAmountLimit >= ?5 " +
                "AND cal.calAdCompany = ?6 " +
                "ORDER BY cal.calAmountLimit ASC";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, department);
            query.setParameter(2, approvalDocumentType);
            query.setParameter(3, approvalUserType);
            query.setParameter(4, username);
            query.setParameter(5, amountLimit);
            query.setParameter(6, companyCode);
            query.setMaxResults(1);
            return (LocalAdAmountLimit) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findAllByAmountCalDeptAdcTypeAndAuTypeAndUsrName(String department, String approvalDocumentType, String approvalUserType, String username, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal, IN(cal.adApprovalUsers) au " +
                "WHERE cal.calDept=?1 " +
                "AND cal.adApprovalDocument.adcType = ?2 " +
                "AND au.auType = ?3 " +
                "AND au.adUser.usrName =?4 " +
                "AND cal.calAdCompany =?5 " +
                "ORDER BY cal.calAmountLimit ASC";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, department);
            query.setParameter(2, approvalDocumentType);
            query.setParameter(3, approvalUserType);
            query.setParameter(4, username);
            query.setParameter(5, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findAmountLimitsPerDepartment(String approvalDocumentType, String department, double amountLimit, Integer companyCode)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal " +
                "WHERE cal.adApprovalDocument.adcType = ?1 " +
                "AND cal.calDept=?2 " +
                "AND cal.calAmountLimit > ?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQuery(sqlCommand);
            query.setParameter(1, approvalDocumentType);
            query.setParameter(2, department);
            query.setParameter(3, amountLimit);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection findAmountLimitsPerDepartment(String approvalDocumentType, String department, double amountLimit, Integer companyCode, String companyShortName)
            throws FinderException {
        String sqlCommand = "SELECT OBJECT(cal) FROM AdAmountLimit cal " +
                "WHERE cal.adApprovalDocument.adcType = ?1 " +
                "AND cal.calDept=?2 " +
                "AND cal.calAmountLimit > ?3 " +
                "AND cal.calAdCompany = ?4";
        try {
            Query query = em.createQueryPerCompany(sqlCommand, companyShortName);
            query.setParameter(1, approvalDocumentType);
            query.setParameter(2, department);
            query.setParameter(3, amountLimit);
            query.setParameter(4, companyCode);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdAmountLimitHome CalDept(String department) {
        this.department = department;
        return this;
    }

    public LocalAdAmountLimitHome CalAmountLimit(double amountLimit) {
        this.amountLimit = amountLimit;
        return this;
    }

    public LocalAdAmountLimitHome CalAndOr(String andOr) {
        this.andOr = andOr;
        return this;
    }

    public LocalAdAmountLimitHome CalAdCompany(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }


    public LocalAdAmountLimit buildAmountLimit(String companyShortName)
            throws CreateException {
        try {
            LocalAdAmountLimit entity = new LocalAdAmountLimit();
            Debug.print("AdAmountLimitBean buildAmountLimit");
            entity.setCalDept(department);
            entity.setCalAmountLimit(amountLimit);
            entity.setCalAndOr(andOr);
            entity.setCalAdCompany(companyCode);
            em.persist(entity, companyShortName);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    // CREATE METHODS
    public LocalAdAmountLimit create(Integer amountLimitCode, String department, double amountLimit, String andOr, Integer companyCode)
            throws CreateException {
        try {
            LocalAdAmountLimit entity = new LocalAdAmountLimit();
            Debug.print("AdAmountLimitBean create");
            entity.setCalCode(amountLimitCode);
            entity.setCalDept(department);
            entity.setCalAmountLimit(amountLimit);
            entity.setCalAndOr(andOr);
            entity.setCalAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdAmountLimit create(String department, double amountLimit, String andOr, Integer companyCode)
            throws CreateException {
        try {
            LocalAdAmountLimit entity = new LocalAdAmountLimit();
            Debug.print("AdAmountLimitBean create");
            entity.setCalDept(department);
            entity.setCalAmountLimit(amountLimit);
            entity.setCalAndOr(andOr);
            entity.setCalAdCompany(companyCode);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}