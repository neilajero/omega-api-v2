package com.ejb.dao.gl;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.Date;

@Stateless
public class LocalGlFunctionalCurrencyHome {

    public static final String JNDI_NAME = "LocalGlFunctionalCurrencyHome!com.ejb.gl.LocalGlFunctionalCurrencyHome";

    @EJB
    public PersistenceBeanClass em;

	private String FC_NM = null;
	private String FC_DESC = null;
	private String FC_CNTRY = null;
	private char FC_SYMBL = 'D';
	private short FC_PRCSN = 0;
	private short FC_EXTNDD_PRCSN = 0;
	private double FC_MIN_ACCNT_UNT = 0.00;
	private Date FC_DT_FRM = null;
	private Date FC_DT_TO = null;
	private byte FC_ENBL = 0; // Default Disabled
	private Integer FC_AD_CMPNY = null;

    public LocalGlFunctionalCurrencyHome() {
    }

    public LocalGlFunctionalCurrency findByPrimaryKey(Integer pk) throws FinderException {
        try {
            LocalGlFunctionalCurrency entity = (LocalGlFunctionalCurrency) em.find(new LocalGlFunctionalCurrency(), pk);
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

    public java.util.Collection findFcAllEnabled(java.util.Date CURR_DATE, Integer FC_AD_CMPNY) throws FinderException {
        try {
            Query query = em.createQuery("SELECT OBJECT(fc) FROM GlFunctionalCurrency fc "
					+ "WHERE fc.fcEnable=1 "
					+ "AND ((fc.fcDateFrom <= ?1 AND fc.fcDateTo >= ?1) OR (fc.fcDateFrom <= ?1 AND fc.fcDateTo IS NULL)) "
					+ "AND fc.fcAdCompany = ?2");
            query.setParameter(1, CURR_DATE);
            query.setParameter(2, FC_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalGlFunctionalCurrency findByFcName(String FC_NM, Integer FC_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(fc) FROM GlFunctionalCurrency fc WHERE fc.fcName=?1 AND fc.fcAdCompany = ?2");
            query.setParameter(1, FC_NM);
            query.setParameter(2, FC_AD_CMPNY);
            return (LocalGlFunctionalCurrency) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalGlFunctionalCurrency findByFcName(String FC_NM, Integer companyCode, String companyShortName) throws FinderException {
        Debug.print("LocalGlFunctionalCurrencyHome findByFcName");
        try {
            Query query = em.createQueryPerCompany(
                    "SELECT OBJECT(fc) FROM GlFunctionalCurrency fc WHERE fc.fcName=?1 AND fc.fcAdCompany = ?2", companyShortName);
            query.setParameter(1, FC_NM);
            query.setParameter(2, companyCode);
            return (LocalGlFunctionalCurrency) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findFcAll(Integer FC_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(fc) FROM GlFunctionalCurrency fc WHERE fc.fcAdCompany = ?1");
            query.setParameter(1, FC_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalGlFunctionalCurrencyHome FcName(String FC_NM) {
        this.FC_NM = FC_NM;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcDescription(String FC_DESC) {
        this.FC_DESC = FC_DESC;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcCountry(String FC_CNTRY) {
        this.FC_CNTRY = FC_CNTRY;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcSymbol(char FC_SYMBL) {
        this.FC_SYMBL = FC_SYMBL;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcPrecision(short FC_PRCSN) {
        this.FC_PRCSN = FC_PRCSN;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcExtendedPrecision(short FC_EXTNDD_PRCSN) {
        this.FC_EXTNDD_PRCSN = FC_EXTNDD_PRCSN;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcMinimumAccountUnit(double FC_MIN_ACCNT_UNT) {
        this.FC_MIN_ACCNT_UNT = FC_MIN_ACCNT_UNT;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcDateFrom(Date FC_DT_FRM) {
        this.FC_DT_FRM = FC_DT_FRM;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcDateTo(Date FC_DT_TO) {
        this.FC_DT_TO = FC_DT_TO;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcEnable(byte FC_ENBL) {
        this.FC_ENBL = FC_ENBL;
        return this;
    }

    public LocalGlFunctionalCurrencyHome FcAdCompany(Integer FC_AD_CMPNY) {
        this.FC_AD_CMPNY = FC_AD_CMPNY;
        return this;
    }

    public LocalGlFunctionalCurrency buildFunctionalCurrency() throws CreateException {
        try {
            LocalGlFunctionalCurrency entity = new LocalGlFunctionalCurrency();
            Debug.print("GlFunctionalCurrencyBean buildFunctionalCurrency");
            entity.setFcName(FC_NM);
            entity.setFcDescription(FC_DESC);
            entity.setFcCountry(FC_CNTRY);
            entity.setFcSymbol(FC_SYMBL);
            entity.setFcPrecision(FC_PRCSN);
            entity.setFcExtendedPrecision(FC_EXTNDD_PRCSN);
            entity.setFcMinimumAccountUnit(FC_MIN_ACCNT_UNT);
            entity.setFcDateFrom(FC_DT_FRM);
            entity.setFcDateTo(FC_DT_TO);
            entity.setFcEnable(FC_ENBL);
            entity.setFcAdCompany(FC_AD_CMPNY);
            em.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

}