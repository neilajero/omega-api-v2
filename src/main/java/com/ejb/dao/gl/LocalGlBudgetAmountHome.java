package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudgetAmount;
import com.util.Debug;

@Stateless
public class LocalGlBudgetAmountHome {

	public static final String JNDI_NAME = "LocalGlBudgetAmountHome!com.ejb.gl.LocalGlBudgetAmountHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlBudgetAmountHome() {
	}

	// FINDER METHODS

	public LocalGlBudgetAmount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlBudgetAmount entity = (LocalGlBudgetAmount) em
					.find(new LocalGlBudgetAmount(), pk);
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

	public LocalGlBudgetAmount findByBoNameAndBgtName(java.lang.String BO_NM, java.lang.String BGT_NM,
			java.lang.Integer BGA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bga) FROM GlBudgetAmount bga WHERE bga.glBudgetOrganization.boName = ?1 AND bga.glBudget.bgtName = ?2 AND bga.bgaAdCompany = ?3");
			query.setParameter(1, BO_NM);
			query.setParameter(2, BGT_NM);
			query.setParameter(3, BGA_AD_CMPNY);
            return (LocalGlBudgetAmount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetAmountHome.findByBoNameAndBgtName(java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.Integer BGA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetAmountHome.findByBoNameAndBgtName(java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.Integer BGA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlBudgetAmount findByBoNameAndBgtNameAndCoaAccountNumber(java.lang.String BO_NM,
			java.lang.String BGT_NM, java.lang.String COA_ACCNT_NMBR, java.lang.Integer BGA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bga) FROM GlBudgetAmount bga, In(bga.glBudgetAmountCoas) bc WHERE bga.glBudgetOrganization.boName = ?1 AND bga.glBudget.bgtName = ?2 AND bc.glChartOfAccount.coaAccountNumber = ?3 AND bga.bgaAdCompany = ?4");
			query.setParameter(1, BO_NM);
			query.setParameter(2, BGT_NM);
			query.setParameter(3, COA_ACCNT_NMBR);
			query.setParameter(4, BGA_AD_CMPNY);
            return (LocalGlBudgetAmount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetAmountHome.findByBoNameAndBgtNameAndCoaAccountNumber(java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.String COA_ACCNT_NMBR, java.lang.Integer BGA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetAmountHome.findByBoNameAndBgtNameAndCoaAccountNumber(java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.String COA_ACCNT_NMBR, java.lang.Integer BGA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getBgaByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalGlBudgetAmount create(java.lang.Integer BGA_CODE, java.lang.String BGA_CRTD_BY,
                                      java.util.Date BGA_DT_CRTD, java.lang.String BGA_LST_MDFD_BY, java.util.Date BGA_DT_LST_MDFD,
                                      java.lang.Integer BGA_AD_CMPNY) throws CreateException {
		try {

			LocalGlBudgetAmount entity = new LocalGlBudgetAmount();

			Debug.print("GlBudgetAmount create");

			entity.setBgaCode(BGA_CODE);
			entity.setBgaCreatedBy(BGA_CRTD_BY);
			entity.setBgaDateCreated(BGA_DT_CRTD);
			entity.setBgaLastModifiedBy(BGA_LST_MDFD_BY);
			entity.setBgaDateLastModified(BGA_DT_LST_MDFD);
			entity.setBgaAdCompany(BGA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlBudgetAmount create(java.lang.String BGA_CRTD_BY, java.util.Date BGA_DT_CRTD,
                                      java.lang.String BGA_LST_MDFD_BY, java.util.Date BGA_DT_LST_MDFD, java.lang.Integer BGA_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlBudgetAmount entity = new LocalGlBudgetAmount();

			Debug.print("GlBudgetAmount create");

			entity.setBgaCreatedBy(BGA_CRTD_BY);
			entity.setBgaDateCreated(BGA_DT_CRTD);
			entity.setBgaLastModifiedBy(BGA_LST_MDFD_BY);
			entity.setBgaDateLastModified(BGA_DT_LST_MDFD);
			entity.setBgaAdCompany(BGA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}