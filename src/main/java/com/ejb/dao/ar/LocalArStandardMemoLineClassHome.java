package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArStandardMemoLineClass;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArStandardMemoLineClassHome {

	public static final String JNDI_NAME = "LocalArStandardMemoLineClassHome!com.ejb.ar.LocalArStandardMemoLineClassHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArStandardMemoLineClassHome() {
	}

	// FINDER METHODS

	public LocalArStandardMemoLineClass findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArStandardMemoLineClass entity = (LocalArStandardMemoLineClass) em
					.find(new LocalArStandardMemoLineClass(), pk);
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

	public java.util.Collection findSmcAll(java.lang.Integer SMC_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(smc) FROM ArStandardMemoLineClass smc WHERE smc.smcAdCompany = ?1");
			query.setParameter(1, SMC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcAll(java.lang.Integer SMC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSmcByCcCode(java.lang.Integer CC_CODE, java.lang.Integer SMC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(smc) FROM ArStandardMemoLineClass smc WHERE smc.arCustomerClass.ccCode = ?1 AND smc.smcAdCompany = ?2");
			query.setParameter(1, CC_CODE);
			query.setParameter(2, SMC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcCode(java.lang.Integer CC_CODE, java.lang.Integer SMC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findSmcByCcName(java.lang.String CC_NM, java.lang.Integer SMC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(smc) FROM ArStandardMemoLineClass smc WHERE smc.arCustomerClass.ccName = ?1 AND smc.smcAdCompany = ?2");
			query.setParameter(1, CC_NM);
			query.setParameter(2, SMC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcName(java.lang.String CC_NM, java.lang.Integer SMC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArStandardMemoLineClass findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(java.lang.String CC_NM,
			java.lang.String SML_NM, java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRNCH,
			java.lang.Integer SML_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(smc) FROM ArStandardMemoLineClass smc WHERE smc.arCustomerClass.ccName = ?1 AND smc.arStandardMemoLine.smlName = ?2 AND smc.arCustomer.cstCustomerCode = ?3 AND smc.smcAdBranch = ?4 AND smc.smcAdCompany = ?5");
			query.setParameter(1, CC_NM);
			query.setParameter(2, SML_NM);
			query.setParameter(3, CST_CSTMR_CODE);
			query.setParameter(4, AD_BRNCH);
			query.setParameter(5, SML_AD_CMPNY);
            return (LocalArStandardMemoLineClass) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(java.lang.String CC_NM, java.lang.String SML_NM, java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer SML_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(java.lang.String CC_NM, java.lang.String SML_NM, java.lang.String CST_CSTMR_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArStandardMemoLineClass findSmcByCcNameSmlNameAdBrnch(java.lang.String CC_NM, java.lang.String SML_NM,
			java.lang.Integer AD_BRNCH, java.lang.Integer SML_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(smc) FROM ArStandardMemoLineClass smc WHERE smc.arCustomerClass.ccName = ?1 AND smc.arStandardMemoLine.smlName = ?2 AND smc.arCustomer IS NULL AND smc.smcAdBranch = ?3 AND smc.smcAdCompany = ?4");
			query.setParameter(1, CC_NM);
			query.setParameter(2, SML_NM);
			query.setParameter(3, AD_BRNCH);
			query.setParameter(4, SML_AD_CMPNY);
            return (LocalArStandardMemoLineClass) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcNameSmlNameAdBrnch(java.lang.String CC_NM, java.lang.String SML_NM, java.lang.Integer AD_BRNCH, java.lang.Integer SML_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArStandardMemoLineClassHome.findSmcByCcNameSmlNameAdBrnch(java.lang.String CC_NM, java.lang.String SML_NM, java.lang.Integer AD_BRNCH, java.lang.Integer SML_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSmcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
			throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalArStandardMemoLineClass create(Integer SMC_CODE, double SMC_UNT_PRC, String SMC_SML_DESC,
                                               String SMC_CRTD_BY, Date SMC_CRTD_DT, String SMC_MDFD_BY, Date SMC_DT_MDFD, int SMC_AD_BRNCH,
                                               int SMC_AD_CMPNY) throws CreateException {
		try {

			LocalArStandardMemoLineClass entity = new LocalArStandardMemoLineClass();

			Debug.print("ArStandardMemoLineClassBean create");

			entity.setSmcCode(SMC_CODE);
			entity.setSmcUnitPrice(SMC_UNT_PRC);
			entity.setSmcStandardMemoLineDescription(SMC_SML_DESC);
			entity.setSmcCreatedBy(SMC_CRTD_BY);
			entity.setSmcDateCreated(SMC_CRTD_DT);
			entity.setSmcModifiedBy(SMC_MDFD_BY);
			entity.setSmcDateModified(SMC_DT_MDFD);
			entity.setSmcAdBranch(SMC_AD_BRNCH);
			entity.setSmcAdCompany(SMC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArStandardMemoLineClass create(double SMC_UNT_PRC, String SMC_SML_DESC, String SMC_CRTD_BY,
                                               Date SMC_CRTD_DT, String SMC_MDFD_BY, Date SMC_DT_MDFD, int SMC_AD_BRNCH, int SMC_AD_CMPNY)
			throws CreateException {
		try {

			LocalArStandardMemoLineClass entity = new LocalArStandardMemoLineClass();

			Debug.print("ArStandardMemoLineClassBean create");

			entity.setSmcUnitPrice(SMC_UNT_PRC);
			entity.setSmcStandardMemoLineDescription(SMC_SML_DESC);
			entity.setSmcCreatedBy(SMC_CRTD_BY);
			entity.setSmcDateCreated(SMC_CRTD_DT);
			entity.setSmcModifiedBy(SMC_MDFD_BY);
			entity.setSmcDateModified(SMC_DT_MDFD);
			entity.setSmcAdBranch(SMC_AD_BRNCH);
			entity.setSmcAdCompany(SMC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}