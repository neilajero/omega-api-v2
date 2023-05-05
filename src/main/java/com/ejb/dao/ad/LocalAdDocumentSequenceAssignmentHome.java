package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Stateless
public class LocalAdDocumentSequenceAssignmentHome {

    public static final String JNDI_NAME = "LocalAdDocumentSequenceAssignmentHome!com.ejb.ad.LocalAdDocumentSequenceAssignmentHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalAdDocumentSequenceAssignmentHome() {

    }

    public LocalAdDocumentSequenceAssignment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalAdDocumentSequenceAssignment entity = (LocalAdDocumentSequenceAssignment) em
                    .find(new LocalAdDocumentSequenceAssignment(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        }
        catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findDsaAll(java.lang.Integer DSA_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(dsa) FROM AdDocumentSequenceAssignment dsa WHERE dsa.dsaAdCompany = ?1");
            query.setParameter(1, DSA_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalAdDocumentSequenceAssignmentHome.findDsaAll(java.lang.Integer DSA_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment findDsaByCode(java.lang.Integer DSA_CODE, java.lang.Integer DSA_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(dsa) FROM AdDocumentSequenceAssignment dsa WHERE dsa.dsaCode = ?1 AND dsa.dsaAdCompany = ?2");
            query.setParameter(1, DSA_CODE);
            query.setParameter(2, DSA_AD_CMPNY);
            return (LocalAdDocumentSequenceAssignment) query.getSingleResult();
        }
        catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalAdDocumentSequenceAssignmentHome.findDsaByCode(java.lang.Integer DSA_CODE, java.lang.Integer DSA_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalAdDocumentSequenceAssignmentHome.findDsaByCode(java.lang.Integer DSA_CODE, java.lang.Integer DSA_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment findByDcName(java.lang.String DC_NM,  java.lang.Integer DSA_AD_CMPNY, String companyShortName)
            throws FinderException {
        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(dsa) FROM AdDocumentCategory dc, IN(dc.adDocumentSequenceAssignments) dsa WHERE dc.dcName=?1 AND dsa.dsaAdCompany = ?2",
                    companyShortName);
            query.setParameter(1, DC_NM);
            query.setParameter(2, DSA_AD_CMPNY);
            return (LocalAdDocumentSequenceAssignment) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment findByDcName(java.lang.String DC_NM, java.lang.Integer DSA_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(dsa) FROM AdDocumentCategory dc, IN(dc.adDocumentSequenceAssignments) dsa WHERE dc.dcName=?1 AND dsa.dsaAdCompany = ?2");
            query.setParameter(1, DC_NM);
            query.setParameter(2, DSA_AD_CMPNY);
            return (LocalAdDocumentSequenceAssignment) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment findByDsName(java.lang.String DS_NM, java.lang.Integer DSA_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(dsa) FROM AdDocumentSequence ds, IN(ds.adDocumentSequenceAssignments) dsa WHERE ds.dsSequenceName=?1 AND dsa.dsaAdCompany = ?2");
            query.setParameter(1, DS_NM);
            query.setParameter(2, DSA_AD_CMPNY);
            return (LocalAdDocumentSequenceAssignment) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment findByDsName(java.lang.String DS_NM, java.lang.Integer DSA_AD_CMPNY, String companyShortName)
            throws FinderException {

        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(dsa) FROM AdDocumentSequence ds, IN(ds.adDocumentSequenceAssignments) dsa "
                            + "WHERE ds.dsSequenceName=?1 AND dsa.dsaAdCompany = ?2",
                    companyShortName);
            query.setParameter(1, DS_NM);
            query.setParameter(2, DSA_AD_CMPNY);
            return (LocalAdDocumentSequenceAssignment) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdDocumentSequenceAssignment create(java.lang.Integer DSA_CODE,
                                                    java.lang.Integer DSA_SOB_CODE, String DSA_NXT_SQNC, Integer DSA_AD_CMPNY) throws CreateException {

        try {

            LocalAdDocumentSequenceAssignment entity = new LocalAdDocumentSequenceAssignment();

            Debug.print("AdDocumentSequenceAssignment create");
            entity.setDsaCode(DSA_CODE);
            entity.setDsaSobCode(DSA_SOB_CODE);
            entity.setDsaNextSequence(DSA_NXT_SQNC);
            entity.setDsaAdCompany(DSA_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdDocumentSequenceAssignment create(java.lang.Integer DSA_SOB_CODE, String DSA_NXT_SQNC,
                                                    Integer DSA_AD_CMPNY) throws CreateException {

        try {

            LocalAdDocumentSequenceAssignment entity = new LocalAdDocumentSequenceAssignment();

            Debug.print("AdDocumentSequenceAssignment create");
            entity.setDsaSobCode(DSA_SOB_CODE);
            entity.setDsaNextSequence(DSA_NXT_SQNC);
            entity.setDsaAdCompany(DSA_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}