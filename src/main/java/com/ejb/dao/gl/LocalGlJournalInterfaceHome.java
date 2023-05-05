package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlJournalInterface;
import com.util.Debug;

@Stateless
public class LocalGlJournalInterfaceHome {

	public static final String JNDI_NAME = "LocalGlJournalInterfaceHome!com.ejb.gl.LocalGlJournalInterfaceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlJournalInterfaceHome() {
	}

	// FINDER METHODS

	public LocalGlJournalInterface findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlJournalInterface entity = (LocalGlJournalInterface) em
					.find(new LocalGlJournalInterface(), pk);
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

	public java.util.Collection findByJsNameAndDateRange(java.lang.String JS_NM, java.util.Date DT_FRM,
			java.util.Date DT_TO, java.lang.Integer JRI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jri) FROM GlJournalInterface jri WHERE jri.jriJournalSource=?1 AND jri.jriDate >= ?2 AND jri.jriDate <= ?3 AND jri.jriAdCompany = ?4");
			query.setParameter(1, JS_NM);
			query.setParameter(2, DT_FRM);
			query.setParameter(3, DT_TO);
			query.setParameter(4, JRI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalInterfaceHome.findByJsNameAndDateRange(java.lang.String JS_NM, java.com.util.Date DT_FRM, java.com.util.Date DT_TO, java.lang.Integer JRI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlJournalInterface findByJriName(java.lang.String JRI_NM, java.lang.Integer JRI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jri) FROM GlJournalInterface jri WHERE jri.jriName = ?1 AND jri.jriAdCompany = ?2");
			query.setParameter(1, JRI_NM);
			query.setParameter(2, JRI_AD_CMPNY);
            return (LocalGlJournalInterface) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlJournalInterfaceHome.findByJriName(java.lang.String JRI_NM, java.lang.Integer JRI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlJournalInterfaceHome.findByJriName(java.lang.String JRI_NM, java.lang.Integer JRI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getJriByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if(LIMIT>0){query.setMaxResults(LIMIT);}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlJournalInterface create(java.lang.Integer JRI_CODE, java.lang.String JRI_NM,
                                          java.lang.String JRI_DESC, Date JRI_EFFCTV_DT, String JRI_JRNL_CTGRY, String JRI_JRNL_SRC,
                                          String JRI_FNCTNL_CRRNCY, Date JRI_DT_RVRSL, String JRI_DCMNT_NMBR, Date JRI_CNVRSN_DT,
                                          double JRI_CNVRSN_RT, char JRI_FND_STATUS, byte JRI_RVRSD, String JRI_TIN, String JRI_SUB_LDGR,
                                          String JRI_RFRNC_NMBR, Integer JRI_AD_BRNCH, Integer JRI_AD_CMPNY) throws CreateException {
		try {

			LocalGlJournalInterface entity = new LocalGlJournalInterface();

			Debug.print("GlJournalInterfaceBean create");

			entity.setJriCode(JRI_CODE);
			entity.setJriName(JRI_NM);
			entity.setJriDescription(JRI_DESC);
			entity.setJriEffectiveDate(JRI_EFFCTV_DT);
			entity.setJriJournalCategory(JRI_JRNL_CTGRY);
			entity.setJriJournalSource(JRI_JRNL_SRC);
			entity.setJriFunctionalCurrency(JRI_FNCTNL_CRRNCY);
			entity.setJriDateReversal(JRI_DT_RVRSL);
			entity.setJriDocumentNumber(JRI_DCMNT_NMBR);
			entity.setJriConversionDate(JRI_CNVRSN_DT);
			entity.setJriConversionRate(JRI_CNVRSN_RT);
			entity.setJriFundStatus(JRI_FND_STATUS);
			entity.setJriReversed(JRI_RVRSD);
			entity.setJriTin(JRI_TIN);
			entity.setJriSubLedger(JRI_SUB_LDGR);
			entity.setJriReferenceNumber(JRI_RFRNC_NMBR);
			entity.setJriAdBranch(JRI_AD_BRNCH);
			entity.setJriAdCompany(JRI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlJournalInterface create(java.lang.String JRI_NM, java.lang.String JRI_DESC,
                                          Date JRI_EFFCTV_DT, String JRI_JRNL_CTGRY, String JRI_JRNL_SRC, String JRI_FNCTNL_CRRNCY, Date JRI_DT_RVRSL,
                                          String JRI_DCMNT_NMBR, Date JRI_CNVRSN_DT, double JRI_CNVRSN_RT, char JRI_FND_STATUS, byte JRI_RVRSD,
                                          String JRI_TIN, String JRI_SUB_LDGR, String JRI_RFRNC_NMBR, Integer JRI_AD_BRNCH, Integer JRI_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlJournalInterface entity = new LocalGlJournalInterface();

			Debug.print("GlJournalInterfaceBean create");

			entity.setJriName(JRI_NM);
			entity.setJriDescription(JRI_DESC);
			entity.setJriEffectiveDate(JRI_EFFCTV_DT);
			entity.setJriJournalCategory(JRI_JRNL_CTGRY);
			entity.setJriJournalSource(JRI_JRNL_SRC);
			entity.setJriFunctionalCurrency(JRI_FNCTNL_CRRNCY);
			entity.setJriDateReversal(JRI_DT_RVRSL);
			entity.setJriDocumentNumber(JRI_DCMNT_NMBR);
			entity.setJriConversionDate(JRI_CNVRSN_DT);
			entity.setJriConversionRate(JRI_CNVRSN_RT);
			entity.setJriFundStatus(JRI_FND_STATUS);
			entity.setJriReversed(JRI_RVRSD);
			entity.setJriTin(JRI_TIN);
			entity.setJriSubLedger(JRI_SUB_LDGR);
			entity.setJriReferenceNumber(JRI_RFRNC_NMBR);
			entity.setJriAdBranch(JRI_AD_BRNCH);
			entity.setJriAdCompany(JRI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}