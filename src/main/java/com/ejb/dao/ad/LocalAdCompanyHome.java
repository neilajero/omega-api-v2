package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdCompany;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdCompanyHome implements ILocalAdCompanyHome {

    public static String JNDI_NAME = "LocalAdCompanyHome!com.ejb.ad.LocalAdCompanyHome";

    @EJB
    public PersistenceBeanClass em;

    private String CMP_NM = null;
    private String CMP_SHRT_NM = null;
    private String CMP_TX_PYR_NM = null;
    private String CMP_CNTCT = null;
    private String CMP_WLCM_NT = null;
    private String CMP_DESC = null;
    private String CMP_ADDRSS = null;
    private String CMP_CTY = null;
    private String CMP_ZP = null;
    private String CMP_CNTRY = null;
    private String CMP_PHN = null;
    private String CMP_FX = null;
    private String CMP_EML = null;
    private String CMP_TIN = null;
    private String CMP_ML_SCTN_NO = null;
    private String CMP_ML_LT_NO = null;
    private String CMP_ML_STRT = null;
    private String CMP_ML_PO_BX = null;
    private String CMP_ML_CNTRY = null;
    private String CMP_ML_PRVNC = null;
    private String CMP_ML_PST_OFFC = null;
    private String CMP_ML_CR_OFF = null;
    private String CMP_TX_PRD_FRM = null;
    private String CMP_TX_PRD_TO = null;
    private String CMP_PBLC_OFFC_NM = null;
    private String CMP_DT_APPNTMNT = null;
    private String CMP_RVN_OFFC = null;
    private String CMP_FSCL_YR_ENDNG = null;
    private String CMP_INDSTRY = null;
    private String CMP_RTND_EARNNGS = null;

    public LocalAdCompanyHome() {
    }

    // FINDER METHODS
    public LocalAdCompany findByPrimaryKey(Integer pk) throws FinderException {
        try {
            LocalAdCompany entity = (LocalAdCompany) em.find(new LocalAdCompany(), pk);
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

    public LocalAdCompany findByPrimaryKey(Integer pk, String companyShortName) throws FinderException {
        try {
            LocalAdCompany entity = (LocalAdCompany) em.findPerCompany(new LocalAdCompany(), pk, companyShortName);
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

    public LocalAdCompany findByCmpName(String CMP_NM) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(cmp) FROM AdCompany cmp WHERE cmp.cmpName = ?1");
            query.setParameter(1, CMP_NM);
            return (LocalAdCompany) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalAdCompany findByCmpShortName(String CMP_SHRT_NM) throws FinderException {
        Debug.print("LocalAdCompanyHome findByCmpShortName");
        try {
            Query query = em.createQueryPerCompany("SELECT OBJECT(cmp) FROM AdCompany cmp WHERE cmp.cmpShortName = ?1", CMP_SHRT_NM.toLowerCase());
            query.setParameter(1, CMP_SHRT_NM.toLowerCase());
            return (LocalAdCompany) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }
    }

    // CREATE METHODS
    public LocalAdCompany buildCompany() throws CreateException {
        try {
            LocalAdCompany entity = new LocalAdCompany();
            Debug.print("AdCompanyBean buildCompany");
            entity.setCmpName(CMP_NM);
            entity.setCmpShortName(CMP_SHRT_NM);
            entity.setCmpTaxPayerName(CMP_TX_PYR_NM);
            entity.setCmpContact(CMP_CNTCT);
            entity.setCmpWelcomeNote(CMP_WLCM_NT);
            entity.setCmpDescription(CMP_DESC);
            entity.setCmpAddress(CMP_ADDRSS);
            entity.setCmpCity(CMP_CTY);
            entity.setCmpZip(CMP_ZP);
            entity.setCmpCountry(CMP_CNTRY);
            entity.setCmpPhone(CMP_PHN);
            entity.setCmpFax(CMP_FX);
            entity.setCmpEmail(CMP_EML);
            entity.setCmpTin(CMP_TIN);
            entity.setCmpMailSectionNo(CMP_ML_SCTN_NO);
            entity.setCmpMailLotNo(CMP_ML_LT_NO);
            entity.setCmpMailStreet(CMP_ML_STRT);
            entity.setCmpMailPoBox(CMP_ML_PO_BX);
            entity.setCmpMailCountry(CMP_ML_CNTRY);
            entity.setCmpMailProvince(CMP_ML_PRVNC);
            entity.setCmpMailPostOffice(CMP_ML_PST_OFFC);
            entity.setCmpMailCareOff(CMP_ML_CR_OFF);
            entity.setCmpTaxPeriodFrom(CMP_TX_PRD_FRM);
            entity.setCmpTaxPeriodTo(CMP_TX_PRD_TO);
            entity.setCmpPublicOfficeName(CMP_PBLC_OFFC_NM);
            entity.setCmpDateAppointment(CMP_DT_APPNTMNT);
            entity.setCmpRevenueOffice(CMP_RVN_OFFC);
            entity.setCmpFiscalYearEnding(CMP_FSCL_YR_ENDNG);
            entity.setCmpIndustry(CMP_INDSTRY);
            entity.setCmpRetainedEarnings(CMP_RTND_EARNNGS);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdCompanyHome CompanyName(String CMP_NM) {
        this.CMP_NM = CMP_NM;
        return this;
    }

    public ILocalAdCompanyHome CompanyShortName(String CMP_SHRT_NM) {
        this.CMP_SHRT_NM = CMP_SHRT_NM;
        return this;
    }

    public ILocalAdCompanyHome WelcomeNote(String CMP_WLCM_NT) {
        this.CMP_WLCM_NT = CMP_WLCM_NT;
        return this;
    }

    public ILocalAdCompanyHome RetainedEarnings(String CMP_RTND_EARNNGS) {
        this.CMP_RTND_EARNNGS = CMP_RTND_EARNNGS;
        return this;
    }

    public LocalAdCompany create(
            Integer CMP_CODE,
            String CMP_NM,
            String CMP_SHRT_NM,
            String CMP_TX_PYR_NM,
            String CMP_CNTCT,
            String CMP_WLCM_NT,
            String CMP_DESC,
            String CMP_ADDRSS,
            String CMP_CTY,
            String CMP_ZP,
            String CMP_CNTRY,
            String CMP_PHN,
            String CMP_FX,
            String CMP_EML,
            String CMP_TIN,
            String CMP_ML_SCTN_NO,
            String CMP_ML_LT_NO,
            String CMP_ML_STRT,
            String CMP_ML_PO_BX,
            String CMP_ML_CNTRY,
            String CMP_ML_PRVNC,
            String CMP_ML_PST_OFFC,
            String CMP_ML_CR_OFF,
            String CMP_TX_PRD_FRM,
            String CMP_TX_PRD_TO,
            String CMP_PBLC_OFFC_NM,
            String CMP_DT_APPNTMNT,
            String CMP_RVN_OFFC,
            String CMP_FSCL_YR_ENDNG,
            String CMP_INDSTRY,
            String CMP_RTND_EARNNGS)
            throws CreateException {
        try {

            LocalAdCompany entity = new LocalAdCompany();
            Debug.print("AdCompanyBean create");
            entity.setCmpCode(CMP_CODE);
            entity.setCmpName(CMP_NM);
            entity.setCmpShortName(CMP_SHRT_NM);
            entity.setCmpTaxPayerName(CMP_TX_PYR_NM);
            entity.setCmpContact(CMP_CNTCT);
            entity.setCmpWelcomeNote(CMP_WLCM_NT);
            entity.setCmpDescription(CMP_DESC);
            entity.setCmpAddress(CMP_ADDRSS);
            entity.setCmpCity(CMP_CTY);
            entity.setCmpZip(CMP_ZP);
            entity.setCmpCountry(CMP_CNTRY);
            entity.setCmpPhone(CMP_PHN);
            entity.setCmpFax(CMP_FX);
            entity.setCmpEmail(CMP_EML);
            entity.setCmpTin(CMP_TIN);
            entity.setCmpMailSectionNo(CMP_ML_SCTN_NO);
            entity.setCmpMailLotNo(CMP_ML_LT_NO);
            entity.setCmpMailStreet(CMP_ML_STRT);
            entity.setCmpMailPoBox(CMP_ML_PO_BX);
            entity.setCmpMailCountry(CMP_ML_CNTRY);
            entity.setCmpMailProvince(CMP_ML_PRVNC);
            entity.setCmpMailPostOffice(CMP_ML_PST_OFFC);
            entity.setCmpMailCareOff(CMP_ML_CR_OFF);
            entity.setCmpTaxPeriodFrom(CMP_TX_PRD_FRM);
            entity.setCmpTaxPeriodTo(CMP_TX_PRD_TO);
            entity.setCmpPublicOfficeName(CMP_PBLC_OFFC_NM);
            entity.setCmpDateAppointment(CMP_DT_APPNTMNT);
            entity.setCmpRevenueOffice(CMP_RVN_OFFC);
            entity.setCmpFiscalYearEnding(CMP_FSCL_YR_ENDNG);
            entity.setCmpIndustry(CMP_INDSTRY);
            entity.setCmpRetainedEarnings(CMP_RTND_EARNNGS);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdCompany create(
            String CMP_NM,
            String CMP_SHRT_NM,
            String CMP_TX_PYR_NM,
            String CMP_CNTCT,
            String CMP_WLCM_NT,
            String CMP_DESC,
            String CMP_ADDRSS,
            String CMP_CTY,
            String CMP_ZP,
            String CMP_CNTRY,
            String CMP_PHN,
            String CMP_FX,
            String CMP_EML,
            String CMP_TIN,
            String CMP_ML_SCTN_NO,
            String CMP_ML_LT_NO,
            String CMP_ML_STRT,
            String CMP_ML_PO_BX,
            String CMP_ML_CNTRY,
            String CMP_ML_PRVNC,
            String CMP_ML_PST_OFFC,
            String CMP_ML_CR_OFF,
            String CMP_TX_PRD_FRM,
            String CMP_TX_PRD_TO,
            String CMP_PBLC_OFFC_NM,
            String CMP_DT_APPNTMNT,
            String CMP_RVN_OFFC,
            String CMP_FSCL_YR_ENDNG,
            String CMP_INDSTRY,
            String CMP_RTND_EARNNGS)
            throws CreateException {
        try {
            LocalAdCompany entity = new LocalAdCompany();
            Debug.print("AdCompanyBean create");
            entity.setCmpName(CMP_NM);
            entity.setCmpShortName(CMP_SHRT_NM);
            entity.setCmpTaxPayerName(CMP_TX_PYR_NM);
            entity.setCmpContact(CMP_CNTCT);
            entity.setCmpWelcomeNote(CMP_WLCM_NT);
            entity.setCmpDescription(CMP_DESC);
            entity.setCmpAddress(CMP_ADDRSS);
            entity.setCmpCity(CMP_CTY);
            entity.setCmpZip(CMP_ZP);
            entity.setCmpCountry(CMP_CNTRY);
            entity.setCmpPhone(CMP_PHN);
            entity.setCmpFax(CMP_FX);
            entity.setCmpEmail(CMP_EML);
            entity.setCmpTin(CMP_TIN);
            entity.setCmpMailSectionNo(CMP_ML_SCTN_NO);
            entity.setCmpMailLotNo(CMP_ML_LT_NO);
            entity.setCmpMailStreet(CMP_ML_STRT);
            entity.setCmpMailPoBox(CMP_ML_PO_BX);
            entity.setCmpMailCountry(CMP_ML_CNTRY);
            entity.setCmpMailProvince(CMP_ML_PRVNC);
            entity.setCmpMailPostOffice(CMP_ML_PST_OFFC);
            entity.setCmpMailCareOff(CMP_ML_CR_OFF);
            entity.setCmpTaxPeriodFrom(CMP_TX_PRD_FRM);
            entity.setCmpTaxPeriodTo(CMP_TX_PRD_TO);
            entity.setCmpPublicOfficeName(CMP_PBLC_OFFC_NM);
            entity.setCmpDateAppointment(CMP_DT_APPNTMNT);
            entity.setCmpRevenueOffice(CMP_RVN_OFFC);
            entity.setCmpFiscalYearEnding(CMP_FSCL_YR_ENDNG);
            entity.setCmpIndustry(CMP_INDSTRY);
            entity.setCmpRetainedEarnings(CMP_RTND_EARNNGS);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalAdCompany create(String CMP_NM, String CMP_SHRT_NM, String CMP_WLCM_NT, String CMP_RTND_EARNNGS) throws CreateException {
        try {
            LocalAdCompany entity = new LocalAdCompany();
            Debug.print("AdCompanyBean create");
            entity.setCmpName(CMP_NM);
            entity.setCmpShortName(CMP_SHRT_NM);
            entity.setCmpWelcomeNote(CMP_WLCM_NT);
            entity.setCmpRetainedEarnings(CMP_RTND_EARNNGS);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
}