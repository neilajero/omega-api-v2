package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBranchProjectTypeType;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBranchProjectTypeTypeHome {

	public static final String JNDI_NAME = "LocalAdBranchProjectTypeTypeHome!com.ejb.ad.LocalAdBranchProjectTypeTypeHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBranchProjectTypeTypeHome() {
	}

	// FINDER METHODS

	public LocalAdBranchProjectTypeType findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBranchProjectTypeType entity = (LocalAdBranchProjectTypeType) em
					.find(new LocalAdBranchProjectTypeType(), pk);
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

	public java.util.Collection findByPmPttAll(java.lang.Integer ITM_LCTN_CODE, java.lang.Integer BPTT_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bptt) FROM AdBranchProjectTypeType bptt WHERE bptt.pmProjectTypeType.pttCode = ?1 AND bptt.bpttAdCompany = ?2");
			query.setParameter(1, ITM_LCTN_CODE);
			query.setParameter(2, BPTT_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchProjectTypeTypeHome.findByPmPttAll(java.lang.Integer ITM_LCTN_CODE, java.lang.Integer BPTT_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBpttByPttCodeAndRsCode(java.lang.Integer PTT_CODE, java.lang.Integer AD_RS_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bptt) FROM AdBranchProjectTypeType bptt, IN(bptt.adBranch.adBranchResponsibilities)brs WHERE bptt.pmProjectTypeType.pttCode = ?1 AND brs.adResponsibility.rsCode = ?2 AND bptt.bpttAdCompany = ?3");
			query.setParameter(1, PTT_CODE);
			query.setParameter(2, AD_RS_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchProjectTypeTypeHome.findBpttByPttCodeAndRsCode(java.lang.Integer PTT_CODE, java.lang.Integer AD_RS_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBranchProjectTypeType findBpttByPttCodeAndBrCode(java.lang.Integer PTT_CODE,
			java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bptt) FROM AdBranchProjectTypeType bptt WHERE bptt.pmProjectTypeType.pttCode = ?1 AND  bptt.adBranch.brCode = ?2 AND bptt.bpttAdCompany = ?3");
			query.setParameter(1, PTT_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return (LocalAdBranchProjectTypeType) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBranchProjectTypeTypeHome.findBpttByPttCodeAndBrCode(java.lang.Integer PTT_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchProjectTypeTypeHome.findBpttByPttCodeAndBrCode(java.lang.Integer PTT_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBpttByPrjCodeAndBrCode(java.lang.Integer PRJ_CODE, java.lang.Integer BR_CODE,
			java.lang.Integer AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bptt) FROM AdBranchProjectTypeType bptt WHERE bptt.pmProjectTypeType.pmProject.prjCode = ?1 AND bptt.adBranch.brCode = ?2 AND bptt.bpttAdCompany = ?3");
			query.setParameter(1, PRJ_CODE);
			query.setParameter(2, BR_CODE);
			query.setParameter(3, AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBranchProjectTypeTypeHome.findBpttByPrjCodeAndBrCode(java.lang.Integer PRJ_CODE, java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getBpttByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalAdBranchProjectTypeType create(Integer BPTT_CODE, Integer BPTT_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdBranchProjectTypeType entity = new LocalAdBranchProjectTypeType();

			Debug.print("AdBranchProjectTypeTypeBean create");
			entity.setBpttCode(BPTT_CODE);
			entity.setBpttAdCompany(BPTT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBranchProjectTypeType create(Integer BPTT_AD_CMPNY) throws CreateException {
		try {

			LocalAdBranchProjectTypeType entity = new LocalAdBranchProjectTypeType();

			Debug.print("AdBranchProjectTypeTypeBean create");
			entity.setBpttAdCompany(BPTT_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}