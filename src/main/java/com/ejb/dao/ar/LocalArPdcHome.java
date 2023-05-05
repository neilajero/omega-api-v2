package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArPdc;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArPdcHome {

	public static final String JNDI_NAME = "LocalArPdcHome!com.ejb.ar.LocalArPdcHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArPdcHome() {
	}

	// FINDER METHODS

	public LocalArPdc findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArPdc entity = (LocalArPdc) em.find(new LocalArPdc(), pk);
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

	public java.util.Collection findPdcToGenerate(java.lang.Integer PDC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pdc) FROM ArPdc pdc WHERE pdc.pdcStatus='MATURED' AND pdc.pdcPosted=1 AND pdc.pdcAdCompany = ?1");
			query.setParameter(1, PDC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPdcHome.findPdcToGenerate(java.lang.Integer PDC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPdcToGenerateByBrCode(java.lang.Integer PDC_AD_BRNCH,
			java.lang.Integer PDC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pdc) FROM ArPdc pdc WHERE pdc.pdcStatus='MATURED' AND pdc.pdcPosted=1 AND pdc.pdcAdBranch = ?1 AND pdc.pdcAdCompany = ?2");
			query.setParameter(1, PDC_AD_BRNCH);
			query.setParameter(2, PDC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPdcHome.findPdcToGenerateByBrCode(java.lang.Integer PDC_AD_BRNCH, java.lang.Integer PDC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findOpenPdcAll(java.lang.Integer PDC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pdc) FROM ArPdc pdc WHERE pdc.pdcStatus='OPEN' AND pdc.pdcAdCompany = ?1");
			query.setParameter(1, PDC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPdcHome.findOpenPdcAll(java.lang.Integer PDC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArPdc findPdcByReferenceNumber(java.lang.String PDC_RFRNC_NMBR, java.lang.Integer PDC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pdc) FROM ArPdc pdc WHERE pdc.pdcReferenceNumber = ?1 AND pdc.pdcAdCompany = ?2");
			query.setParameter(1, PDC_RFRNC_NMBR);
			query.setParameter(2, PDC_AD_CMPNY);
            return (LocalArPdc) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArPdcHome.findPdcByReferenceNumber(java.lang.String PDC_RFRNC_NMBR, java.lang.Integer PDC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPdcHome.findPdcByReferenceNumber(java.lang.String PDC_RFRNC_NMBR, java.lang.Integer PDC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPdcByPdcType(java.lang.Integer PDC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(pdc) FROM ArPdc pdc WHERE pdc.adBankAccount IS NOT NULL AND pdc.pdcAdCompany = ?1");
			query.setParameter(1, PDC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArPdcHome.findPdcByPdcType(java.lang.Integer PDC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getPdcByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArPdc create(Integer PDC_CODE, String PDC_STATUS, String PDC_LV_SHFT, String PDC_CHCK_NMBR,
                             String PDC_RFRNC_NMBR, Date PDC_DT_RCVD, Date PDC_MTRTY_DT, String PDC_DESC, byte PDC_CNCLLD,
                             double PDC_AMNT, double PDC_CNVRSN_RT, Date PDC_CNVRSN_DT, String PDC_LV_FRGHT, String PDC_APPRVL_STATUS,
                             byte PDC_PSTD, String PDC_CRTD_BY, Date PDC_DT_CRTD, String PDC_LST_MDFD_BY, Date PDC_DT_LST_MDFD,
                             String PDC_APPRVD_RJCTD_BY, Date PDC_DT_APPRVD_RJCTD, String PDC_PSTD_BY, Date PDC_DT_PSTD, byte PDC_PRNTD,
                             Date PDC_EFFCTVTY_DT, Integer PDC_AD_BRNCH, Integer PDC_AD_CMPNY) throws CreateException {
		try {

			LocalArPdc entity = new LocalArPdc();

			Debug.print("ArPdcBean create");

			entity.setPdcCode(PDC_CODE);
			entity.setPdcStatus(PDC_STATUS);
			entity.setPdcLvShift(PDC_LV_SHFT);
			entity.setPdcCheckNumber(PDC_CHCK_NMBR);
			entity.setPdcReferenceNumber(PDC_RFRNC_NMBR);
			entity.setPdcDateReceived(PDC_DT_RCVD);
			entity.setPdcMaturityDate(PDC_MTRTY_DT);
			entity.setPdcDescription(PDC_DESC);
			entity.setPdcCancelled(PDC_CNCLLD);
			entity.setPdcAmount(PDC_AMNT);
			entity.setPdcConversionRate(PDC_CNVRSN_RT);
			entity.setPdcConversionDate(PDC_CNVRSN_DT);
			entity.setPdcLvFreight(PDC_LV_FRGHT);
			entity.setPdcApprovalStatus(PDC_APPRVL_STATUS);
			entity.setPdcPosted(PDC_PSTD);
			entity.setPdcCreatedBy(PDC_CRTD_BY);
			entity.setPdcDateCreated(PDC_DT_CRTD);
			entity.setPdcLastModifiedBy(PDC_LST_MDFD_BY);
			entity.setPdcDateLastModified(PDC_DT_LST_MDFD);
			entity.setPdcApprovedRejectedBy(PDC_APPRVD_RJCTD_BY);
			entity.setPdcDateApprovedRejected(PDC_DT_APPRVD_RJCTD);
			entity.setPdcPostedBy(PDC_PSTD_BY);
			entity.setPdcDatePosted(PDC_DT_PSTD);
			entity.setPdcPrinted(PDC_PRNTD);
			entity.setPdcEffectivityDate(PDC_EFFCTVTY_DT);
			entity.setPdcAdBranch(PDC_AD_BRNCH);
			entity.setPdcAdCompany(PDC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArPdc create(String PDC_STATUS, String PDC_LV_SHFT, String PDC_CHCK_NMBR,
                             String PDC_RFRNC_NMBR, Date PDC_DT_RCVD, Date PDC_MTRTY_DT, String PDC_DESC, byte PDC_CNCLLD,
                             double PDC_AMNT, double PDC_CNVRSN_RT, Date PDC_CNVRSN_DT, String PDC_LV_FRGHT, String PDC_APPRVL_STATUS,
                             byte PDC_PSTD, String PDC_CRTD_BY, Date PDC_DT_CRTD, String PDC_LST_MDFD_BY, Date PDC_DT_LST_MDFD,
                             String PDC_APPRVD_RJCTD_BY, Date PDC_DT_APPRVD_RJCTD, String PDC_PSTD_BY, Date PDC_DT_PSTD, byte PDC_PRNTD,
                             Date PDC_EFFCTVTY_DT, Integer PDC_AD_BRNCH, Integer PDC_AD_CMPNY) throws CreateException {
		try {

			LocalArPdc entity = new LocalArPdc();

			Debug.print("ArPdcBean create");

			entity.setPdcStatus(PDC_STATUS);
			entity.setPdcLvShift(PDC_LV_SHFT);
			entity.setPdcCheckNumber(PDC_CHCK_NMBR);
			entity.setPdcReferenceNumber(PDC_RFRNC_NMBR);
			entity.setPdcDateReceived(PDC_DT_RCVD);
			entity.setPdcMaturityDate(PDC_MTRTY_DT);
			entity.setPdcDescription(PDC_DESC);
			entity.setPdcCancelled(PDC_CNCLLD);
			entity.setPdcAmount(PDC_AMNT);
			entity.setPdcConversionRate(PDC_CNVRSN_RT);
			entity.setPdcConversionDate(PDC_CNVRSN_DT);
			entity.setPdcLvFreight(PDC_LV_FRGHT);
			entity.setPdcApprovalStatus(PDC_APPRVL_STATUS);
			entity.setPdcPosted(PDC_PSTD);
			entity.setPdcCreatedBy(PDC_CRTD_BY);
			entity.setPdcDateCreated(PDC_DT_CRTD);
			entity.setPdcLastModifiedBy(PDC_LST_MDFD_BY);
			entity.setPdcDateLastModified(PDC_DT_LST_MDFD);
			entity.setPdcApprovedRejectedBy(PDC_APPRVD_RJCTD_BY);
			entity.setPdcDateApprovedRejected(PDC_DT_APPRVD_RJCTD);
			entity.setPdcPostedBy(PDC_PSTD_BY);
			entity.setPdcDatePosted(PDC_DT_PSTD);
			entity.setPdcPrinted(PDC_PRNTD);
			entity.setPdcEffectivityDate(PDC_EFFCTVTY_DT);
			entity.setPdcAdBranch(PDC_AD_BRNCH);
			entity.setPdcAdCompany(PDC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}