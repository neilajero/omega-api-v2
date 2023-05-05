package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdBank;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalAdBankHome {

	public static final String JNDI_NAME = "LocalAdBankHome!com.ejb.ad.LocalAdBankHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdBankHome() {
	}

	// FINDER METHODS

	public LocalAdBank findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalAdBank entity = (LocalAdBank) em.find(new LocalAdBank(), pk);
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

	public java.util.Collection findBnkAll(java.lang.Integer BNK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(bnk) FROM AdBank bnk WHERE bnk.bnkAdCompany = ?1");
			query.setParameter(1, BNK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankHome.findBnkAll(java.lang.Integer BNK_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalAdBank findByBnkName(java.lang.String BNK_NM, java.lang.Integer BNK_AD_CMPNY) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(bnk) FROM AdBank bnk WHERE bnk.bnkName = ?1 AND bnk.bnkAdCompany = ?2");
			query.setParameter(1, BNK_NM);
			query.setParameter(2, BNK_AD_CMPNY);
            return (LocalAdBank) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ad.LocalAdBankHome.findByBnkName(java.lang.String BNK_NM, java.lang.Integer BNK_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankHome.findByBnkName(java.lang.String BNK_NM, java.lang.Integer BNK_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findEnabledBnkAll(java.lang.Integer BNK_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bnk) FROM AdBank bnk WHERE bnk.bnkEnable = 1 AND bnk.bnkAdCompany = ?1");
			query.setParameter(1, BNK_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ad.LocalAdBankHome.findEnabledBnkAll(java.lang.Integer BNK_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalAdBank create(Integer BNK_CODE, String BNK_NM, String BNK_BRNCH, long BNK_NMBR,
                              String BNK_INSTTTN, String BNK_DESC, String BNK_ADDRSS, byte BNK_ENBL, Integer BNK_AD_CMPNY)
			throws CreateException {
		try {

			LocalAdBank entity = new LocalAdBank();

			Debug.print("AdBankBean create");

			entity.setBnkCode(BNK_CODE);
			entity.setBnkName(BNK_NM);
			entity.setBnkBranch(BNK_BRNCH);
			entity.setBnkNumber(BNK_NMBR);
			entity.setBnkInstitution(BNK_INSTTTN);
			entity.setBnkDescription(BNK_DESC);
			entity.setBnkAddress(BNK_ADDRSS);
			entity.setBnkEnable(BNK_ENBL);
			entity.setBnkAdCompany(BNK_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdBank create(String BNK_NM, String BNK_BRNCH, long BNK_NMBR, String BNK_INSTTTN,
                              String BNK_DESC, String BNK_ADDRSS, byte BNK_ENBL, Integer BNK_AD_CMPNY) throws CreateException {
		try {

			LocalAdBank entity = new LocalAdBank();

			Debug.print("AdBankBean create");

			entity.setBnkName(BNK_NM);
			entity.setBnkBranch(BNK_BRNCH);
			entity.setBnkNumber(BNK_NMBR);
			entity.setBnkInstitution(BNK_INSTTTN);
			entity.setBnkDescription(BNK_DESC);
			entity.setBnkAddress(BNK_ADDRSS);
			entity.setBnkEnable(BNK_ENBL);
			entity.setBnkAdCompany(BNK_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}