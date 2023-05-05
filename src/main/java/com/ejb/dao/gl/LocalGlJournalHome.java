package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournal;
import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalGlJournalHome {

    public static final String JNDI_NAME = "LocalGlJournalHome!com.ejb.gl.LocalGlJournalHome";

    @EJB
    public PersistenceBeanClass em;
    private String JR_NM = null;
    private String JR_DESC = null;
    private Date JR_EFFCTV_DT = null;
    private double JR_CNTRL_TTL = 0.00d;
    private Date JR_DT_RVRSL = null;
    private String JR_DCMNT_NMBR = null;
    private Date JR_CNVRSN_DT = null;
    private double JR_CNVRSN_RT = 0d;
    private String JR_APPRVL_STATUS = "N/A";
    private String JR_RSN_FR_RJCTN = null;
    private char JR_FND_STATUS = 'N';
    private byte JR_PSTD = EJBCommon.FALSE;
    private byte JR_RVRSD = EJBCommon.FALSE;
    private String JR_CRTD_BY = null;
    private Date JR_DT_CRTD = new Date();
    private String JR_LST_MDFD_BY = null;
    private Date JR_DT_LST_MDFD = new Date();
    private String JR_APPRVD_RJCTD_BY = null;
    private Date JR_DT_APPRVD_RJCTD = null;
    private String JR_PSTD_BY = null;
    private Date JR_DT_PSTD = null;
    private String JR_TIN = null;
    private String JR_SUB_LDGR = null;
    private byte JR_PRNTD = EJBCommon.FALSE;
    private String JR_RFRNC_NMBR = null;
    private Integer JR_AD_BRNCH = null;
    private Integer JR_AD_CMPNY = null;

    public LocalGlJournalHome() {
    }

    // FINDER METHODS

    public LocalGlJournal findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalGlJournal entity = (LocalGlJournal) em.find(new LocalGlJournal(), pk);
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

    public LocalGlJournal findByJrName(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrName=?1 AND jr.jrAdCompany = ?2");
            query.setParameter(1, JR_NM);
            query.setParameter(2, JR_AD_CMPNY);
            return (LocalGlJournal) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalHome.findByJrName(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrName(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalGlJournal findByJrNameAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH,
                                                java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrName=?1 AND jr.jrAdBranch = ?2 AND jr.jrAdCompany = ?3");
            query.setParameter(1, JR_NM);
            query.setParameter(2, JR_AD_BRNCH);
            query.setParameter(3, JR_AD_CMPNY);
            return (LocalGlJournal) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalHome.findByJrNameAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrNameAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalGlJournal findByJrReferenceNumber(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrReferenceNumber=?1 AND jr.jrAdCompany = ?2");
            query.setParameter(1, JR_NM);
            query.setParameter(2, JR_AD_CMPNY);
            return (LocalGlJournal) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalHome.findByJrReferenceNumber(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrReferenceNumber(java.lang.String JR_NM, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalGlJournal findByJrReferenceNumberAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH,
                                                           java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrReferenceNumber=?1 AND jr.jrAdBranch = ?2 AND jr.jrAdCompany = ?3");
            query.setParameter(1, JR_NM);
            query.setParameter(2, JR_AD_BRNCH);
            query.setParameter(3, JR_AD_CMPNY);
            return (LocalGlJournal) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalHome.findByJrReferenceNumberAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrReferenceNumberAndBrCode(java.lang.String JR_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public LocalGlJournal findByJrDocumentNumberAndJsNameAndBrCode(java.lang.String JR_DCMNT_NMBR,
                                                                   java.lang.String JS_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrDocumentNumber=?1 AND jr.glJournalSource.jsName=?2 AND jr.jrAdBranch = ?3 AND jr.jrAdCompany = ?4");
            query.setParameter(1, JR_DCMNT_NMBR);
            query.setParameter(2, JS_NM);
            query.setParameter(3, JR_AD_BRNCH);
            query.setParameter(4, JR_AD_CMPNY);
            return (LocalGlJournal) query.getSingleResult();
        } catch (NoResultException ex) {
            Debug.print(
                    "EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(java.lang.String JR_DCMNT_NMBR, java.lang.String JS_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrDocumentNumberAndJsNameAndBrCode(java.lang.String JR_DCMNT_NMBR, java.lang.String JS_NM, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findReversibleJrByJrReversalDateAndBrCode(java.util.Date CRRNT_DT,
                                                                          java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrReversed=0 AND jr.jrPosted=1 AND jr.jrDateReversal=?1 AND jr.jrAdBranch = ?2 AND jr.jrAdCompany = ?3");
            query.setParameter(1, CRRNT_DT);
            query.setParameter(2, JR_AD_BRNCH);
            query.setParameter(3, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findReversibleJrByJrReversalDateAndBrCode(java.com.util.Date CRRNT_DT, java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByJrPostedAndJbName(byte JR_PSTD, java.lang.String JB_NM,
                                                        java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrPosted=?1 AND jr.glJournalBatch.jbName = ?2 AND jr.jrAdCompany = ?3");
            query.setParameter(1, JR_PSTD);
            query.setParameter(2, JB_NM);
            query.setParameter(3, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJrPostedAndJbName(byte JR_PSTD, java.lang.String JB_NM, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findByJbName(java.lang.String JB_NM, java.lang.Integer JR_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.glJournalBatch.jbName=?1 AND jr.jrAdCompany = ?2");
            query.setParameter(1, JB_NM);
            query.setParameter(2, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findByJbName(java.lang.String JB_NM, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftJrAll(java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrApprovalStatus IS NULL AND jr.jrAdCompany = ?1");
            query.setParameter(1, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findDraftJrAll(java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findDraftJrByBrCode(java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr WHERE jr.jrApprovalStatus IS NULL AND jr.jrAdBranch = ?1 AND jr.jrAdCompany = ?2");
            query.setParameter(1, JR_AD_BRNCH);
            query.setParameter(2, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findDraftJrByBrCode(java.lang.Integer JR_AD_BRNCH, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection findUnpostedJrByJrDateRange(java.util.Date JR_EFFCTV_DT_FRM,
                                                            java.util.Date JR_EFFCTV_DT_TO, java.lang.Integer JR_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(jr) FROM GlJournal jr  WHERE jr.jrPosted = 0 AND jr.jrEffectiveDate >= ?1 AND jr.jrEffectiveDate <= ?2 AND jr.jrAdCompany = ?3");
            query.setParameter(1, JR_EFFCTV_DT_FRM);
            query.setParameter(2, JR_EFFCTV_DT_TO);
            query.setParameter(3, JR_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.gl.LocalGlJournalHome.findUnpostedJrByJrDateRange(java.com.util.Date JR_EFFCTV_DT_FRM, java.com.util.Date JR_EFFCTV_DT_TO, java.lang.Integer JR_AD_CMPNY)");
            throw ex;
        }
    }

    public java.util.Collection getJrByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
                                                java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

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

    public LocalGlJournal buildJournal(String companyShortName) throws CreateException {
        try {

            LocalGlJournal entity = new LocalGlJournal();

            Debug.print("GlJournalBean buildJournal");

            entity.setJrName(JR_NM);
            entity.setJrDescription(JR_DESC);
            entity.setJrEffectiveDate(JR_EFFCTV_DT);
            entity.setJrControlTotal(JR_CNTRL_TTL);
            entity.setJrDateReversal(JR_DT_RVRSL);
            entity.setJrDocumentNumber(JR_DCMNT_NMBR);
            entity.setJrConversionDate(JR_CNVRSN_DT);
            entity.setJrConversionRate(JR_CNVRSN_RT);
            entity.setJrApprovalStatus(JR_APPRVL_STATUS);
            entity.setJrReasonForRejection(JR_RSN_FR_RJCTN);
            entity.setJrFundStatus(JR_FND_STATUS);
            entity.setJrPosted(JR_PSTD);
            entity.setJrReversed(JR_RVRSD);
            entity.setJrCreatedBy(JR_CRTD_BY);
            entity.setJrDateCreated(JR_DT_CRTD);
            entity.setJrLastModifiedBy(JR_LST_MDFD_BY);
            entity.setJrDateLastModified(JR_DT_LST_MDFD);
            entity.setJrApprovedRejectedBy(JR_APPRVD_RJCTD_BY);
            entity.setJrDateApprovedRejected(JR_DT_APPRVD_RJCTD);
            entity.setJrPostedBy(JR_PSTD_BY);
            entity.setJrDatePosted(JR_DT_PSTD);
            entity.setJrTin(JR_TIN);
            entity.setJrSubLedger(JR_SUB_LDGR);
            entity.setJrPrinted(JR_PRNTD);
            entity.setJrReferenceNumber(JR_RFRNC_NMBR);
            entity.setJrAdBranch(JR_AD_BRNCH);
            entity.setJrAdCompany(JR_AD_CMPNY);

            em.persist(entity, companyShortName);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalGlJournal buildJournal() throws CreateException {
        try {

            LocalGlJournal entity = new LocalGlJournal();

            Debug.print("GlJournalBean buildJournal");

            entity.setJrName(JR_NM);
            entity.setJrDescription(JR_DESC);
            entity.setJrEffectiveDate(JR_EFFCTV_DT);
            entity.setJrControlTotal(JR_CNTRL_TTL);
            entity.setJrDateReversal(JR_DT_RVRSL);
            entity.setJrDocumentNumber(JR_DCMNT_NMBR);
            entity.setJrConversionDate(JR_CNVRSN_DT);
            entity.setJrConversionRate(JR_CNVRSN_RT);
            entity.setJrApprovalStatus(JR_APPRVL_STATUS);
            entity.setJrReasonForRejection(JR_RSN_FR_RJCTN);
            entity.setJrFundStatus(JR_FND_STATUS);
            entity.setJrPosted(JR_PSTD);
            entity.setJrReversed(JR_RVRSD);
            entity.setJrCreatedBy(JR_CRTD_BY);
            entity.setJrDateCreated(JR_DT_CRTD);
            entity.setJrLastModifiedBy(JR_LST_MDFD_BY);
            entity.setJrDateLastModified(JR_DT_LST_MDFD);
            entity.setJrApprovedRejectedBy(JR_APPRVD_RJCTD_BY);
            entity.setJrDateApprovedRejected(JR_DT_APPRVD_RJCTD);
            entity.setJrPostedBy(JR_PSTD_BY);
            entity.setJrDatePosted(JR_DT_PSTD);
            entity.setJrTin(JR_TIN);
            entity.setJrSubLedger(JR_SUB_LDGR);
            entity.setJrPrinted(JR_PRNTD);
            entity.setJrReferenceNumber(JR_RFRNC_NMBR);
            entity.setJrAdBranch(JR_AD_BRNCH);
            entity.setJrAdCompany(JR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalGlJournalHome JrName(String JR_NM) {
        this.JR_NM = JR_NM;
        return this;
    }

    public LocalGlJournalHome JrDescription(String JR_DESC) {
        this.JR_DESC = JR_DESC;
        return this;
    }

    public LocalGlJournalHome JrEffectiveDate(Date JR_EFFCTV_DT) {
        this.JR_EFFCTV_DT = JR_EFFCTV_DT;
        return this;
    }

    public LocalGlJournalHome JrControlTotal(double JR_CNTRL_TTL) {
        this.JR_CNTRL_TTL = JR_CNTRL_TTL;
        return this;
    }

    public LocalGlJournalHome JrDateReversal(Date JR_DT_RVRSL) {
        this.JR_DT_RVRSL = JR_DT_RVRSL;
        return this;
    }

    public LocalGlJournalHome JrDocumentNumber(String JR_DCMNT_NMBR) {
        this.JR_DCMNT_NMBR = JR_DCMNT_NMBR;
        return this;
    }

    public LocalGlJournalHome JrConversionDate(Date JR_CNVRSN_DT) {
        this.JR_CNVRSN_DT = JR_CNVRSN_DT;
        return this;
    }

    public LocalGlJournalHome JrConversionRate(double JR_CNVRSN_RT) {
        this.JR_CNVRSN_RT = JR_CNVRSN_RT;
        return this;
    }

    public LocalGlJournalHome JrApprovalStatus(String JR_APPRVL_STATUS) {
        this.JR_APPRVL_STATUS = JR_APPRVL_STATUS;
        return this;
    }

    public LocalGlJournalHome JrReasonForRejection(String JR_RSN_FR_RJCTN) {
        this.JR_RSN_FR_RJCTN = JR_RSN_FR_RJCTN;
        return this;
    }

    public LocalGlJournalHome JrFundStatus(char JR_FND_STATUS) {
        this.JR_FND_STATUS = JR_FND_STATUS;
        return this;
    }

    public LocalGlJournalHome JrPosted(byte JR_PSTD) {
        this.JR_PSTD = JR_PSTD;
        return this;
    }

    public LocalGlJournalHome JrReversed(byte JR_RVRSD) {
        this.JR_RVRSD = JR_RVRSD;
        return this;
    }

    public LocalGlJournalHome JrCreatedBy(String JR_CRTD_BY) {
        this.JR_CRTD_BY = JR_CRTD_BY;
        return this;
    }

    public LocalGlJournalHome JrDateCreated(Date JR_DT_CRTD) {
        this.JR_DT_CRTD = JR_DT_CRTD;
        return this;
    }

    public LocalGlJournalHome JrLastModifiedBy(String JR_LST_MDFD_BY) {
        this.JR_LST_MDFD_BY = JR_LST_MDFD_BY;
        return this;
    }

    public LocalGlJournalHome JrDateLastModified(Date JR_DT_LST_MDFD) {
        this.JR_DT_LST_MDFD = JR_DT_LST_MDFD;
        return this;
    }

    public LocalGlJournalHome JrApprovedRejectedBy(String JR_APPRVD_RJCTD_BY) {
        this.JR_APPRVD_RJCTD_BY = JR_APPRVD_RJCTD_BY;
        return this;
    }

    public LocalGlJournalHome JrDateApprovedRejected(Date JR_DT_APPRVD_RJCTD) {
        this.JR_DT_APPRVD_RJCTD = JR_DT_APPRVD_RJCTD;
        return this;
    }

    public LocalGlJournalHome JrPostedBy(String JR_PSTD_BY) {
        this.JR_PSTD_BY = JR_PSTD_BY;
        return this;
    }

    public LocalGlJournalHome JrDatePosted(Date JR_DT_PSTD) {
        this.JR_DT_PSTD = JR_DT_PSTD;
        return this;
    }

    public LocalGlJournalHome JrTin(String JR_TIN) {
        this.JR_TIN = JR_TIN;
        return this;
    }

    public LocalGlJournalHome JrSubLedger(String JR_SUB_LDGR) {
        this.JR_SUB_LDGR = JR_SUB_LDGR;
        return this;
    }

    public LocalGlJournalHome JrPrinted(byte JR_PRNTD) {
        this.JR_PRNTD = JR_PRNTD;
        return this;
    }

    public LocalGlJournalHome JrReferenceNumber(String JR_RFRNC_NMBR) {
        this.JR_RFRNC_NMBR = JR_RFRNC_NMBR;
        return this;
    }

    public LocalGlJournalHome JrAdBranch(Integer JR_AD_BRNCH) {
        this.JR_AD_BRNCH = JR_AD_BRNCH;
        return this;
    }

    public LocalGlJournalHome JrAdCompany(Integer JR_AD_CMPNY) {
        this.JR_AD_CMPNY = JR_AD_CMPNY;
        return this;
    }

    public LocalGlJournal create(java.lang.Integer JR_CODE, java.lang.String JR_NM, java.lang.String JR_DESC,
                                 Date JR_EFFCTV_DT, double JR_CNTRL_TTL, Date JR_DT_RVRSL, String JR_DCMNT_NMBR, Date JR_CNVRSN_DT,
                                 double JR_CNVRSN_RT, String JR_APPRVL_STATUS, String JR_RSN_FR_RJCTN, char JR_FND_STATUS, byte JR_PSTD,
                                 byte JR_RVRSD, String JR_CRTD_BY, Date JR_DT_CRTD, String JR_LST_MDFD_BY, Date JR_DT_LST_MDFD,
                                 String JR_APPRVD_RJCTD_BY, Date JR_DT_APPRVD_RJCTD, String JR_PSTD_BY, Date JR_DT_PSTD, String JR_TIN,
                                 String JR_SUB_LDGR, byte JR_PRNTD, String JR_RFRNC_NMBR, Integer JR_AD_BRNCH, Integer JR_AD_CMPNY)
            throws CreateException {
        try {

            LocalGlJournal entity = new LocalGlJournal();

            Debug.print("GlJournalBean create");

            entity.setJrCode(JR_CODE);
            entity.setJrName(JR_NM);
            entity.setJrDescription(JR_DESC);
            entity.setJrEffectiveDate(JR_EFFCTV_DT);
            entity.setJrControlTotal(JR_CNTRL_TTL);
            entity.setJrDateReversal(JR_DT_RVRSL);
            entity.setJrDocumentNumber(JR_DCMNT_NMBR);
            entity.setJrConversionDate(JR_CNVRSN_DT);
            entity.setJrConversionRate(JR_CNVRSN_RT);
            entity.setJrApprovalStatus(JR_APPRVL_STATUS);
            entity.setJrReasonForRejection(JR_RSN_FR_RJCTN);
            entity.setJrFundStatus(JR_FND_STATUS);
            entity.setJrPosted(JR_PSTD);
            entity.setJrReversed(JR_RVRSD);
            entity.setJrCreatedBy(JR_CRTD_BY);
            entity.setJrDateCreated(JR_DT_CRTD);
            entity.setJrLastModifiedBy(JR_LST_MDFD_BY);
            entity.setJrDateLastModified(JR_DT_LST_MDFD);
            entity.setJrApprovedRejectedBy(JR_APPRVD_RJCTD_BY);
            entity.setJrDateApprovedRejected(JR_DT_APPRVD_RJCTD);
            entity.setJrPostedBy(JR_PSTD_BY);
            entity.setJrDatePosted(JR_DT_PSTD);
            entity.setJrTin(JR_TIN);
            entity.setJrSubLedger(JR_SUB_LDGR);
            entity.setJrPrinted(JR_PRNTD);
            entity.setJrReferenceNumber(JR_RFRNC_NMBR);
            entity.setJrAdBranch(JR_AD_BRNCH);
            entity.setJrAdCompany(JR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalGlJournal create(java.lang.String JR_NM, java.lang.String JR_DESC, Date JR_EFFCTV_DT,
                                 double JR_CNTRL_TTL, Date JR_DT_RVRSL, String JR_DCMNT_NMBR, Date JR_CNVRSN_DT, double JR_CNVRSN_RT,
                                 String JR_APPRVL_STATUS, String JR_RSN_FR_RJCTN, char JR_FND_STATUS, byte JR_PSTD, byte JR_RVRSD,
                                 String JR_CRTD_BY, Date JR_DT_CRTD, String JR_LST_MDFD_BY, Date JR_DT_LST_MDFD, String JR_APPRVD_RJCTD_BY,
                                 Date JR_DT_APPRVD_RJCTD, String JR_PSTD_BY, Date JR_DT_PSTD, String JR_TIN, String JR_SUB_LDGR,
                                 byte JR_PRNTD, String JR_RFRNC_NMBR, Integer JR_AD_BRNCH, Integer JR_AD_CMPNY) throws CreateException {
        try {

            LocalGlJournal entity = new LocalGlJournal();

            Debug.print("GlJournalBean create");

            entity.setJrName(JR_NM);
            entity.setJrDescription(JR_DESC);
            entity.setJrEffectiveDate(JR_EFFCTV_DT);
            entity.setJrControlTotal(JR_CNTRL_TTL);
            entity.setJrDateReversal(JR_DT_RVRSL);
            entity.setJrDocumentNumber(JR_DCMNT_NMBR);
            entity.setJrConversionDate(JR_CNVRSN_DT);
            entity.setJrConversionRate(JR_CNVRSN_RT);
            entity.setJrApprovalStatus(JR_APPRVL_STATUS);
            entity.setJrReasonForRejection(JR_RSN_FR_RJCTN);
            entity.setJrFundStatus(JR_FND_STATUS);
            entity.setJrPosted(JR_PSTD);
            entity.setJrReversed(JR_RVRSD);
            entity.setJrCreatedBy(JR_CRTD_BY);
            entity.setJrDateCreated(JR_DT_CRTD);
            entity.setJrLastModifiedBy(JR_LST_MDFD_BY);
            entity.setJrDateLastModified(JR_DT_LST_MDFD);
            entity.setJrApprovedRejectedBy(JR_APPRVD_RJCTD_BY);
            entity.setJrDateApprovedRejected(JR_DT_APPRVD_RJCTD);
            entity.setJrPostedBy(JR_PSTD_BY);
            entity.setJrDatePosted(JR_DT_PSTD);
            entity.setJrTin(JR_TIN);
            entity.setJrSubLedger(JR_SUB_LDGR);
            entity.setJrPrinted(JR_PRNTD);
            entity.setJrReferenceNumber(JR_RFRNC_NMBR);
            entity.setJrAdBranch(JR_AD_BRNCH);
            entity.setJrAdCompany(JR_AD_CMPNY);

            em.persist(entity);
            return entity;

        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}