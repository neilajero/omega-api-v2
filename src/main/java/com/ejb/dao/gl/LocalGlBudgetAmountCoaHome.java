package com.ejb.dao.gl;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlBudgetAmountCoa;
import com.util.Debug;

@Stateless
public class LocalGlBudgetAmountCoaHome {

	public static final String JNDI_NAME = "LocalGlBudgetAmountCoaHome!com.ejb.gl.LocalGlBudgetAmountCoaHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlBudgetAmountCoaHome() {
	}

	// FINDER METHODS

	public LocalGlBudgetAmountCoa findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlBudgetAmountCoa entity = (LocalGlBudgetAmountCoa) em
					.find(new LocalGlBudgetAmountCoa(), pk);
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

	public LocalGlBudgetAmountCoa findByCoaCodeAndBgtCode(java.lang.Integer COA_CODE, java.lang.Integer BGT_CODE,
			java.lang.Integer BC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(bc) FROM GlBudgetAmountCoa bc WHERE bc.glChartOfAccount.coaCode = ?1 AND bc.glBudgetAmount.glBudget.bgtCode = ?2 AND bc.bcAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, BGT_CODE);
			query.setParameter(3, BC_AD_CMPNY);
            return (LocalGlBudgetAmountCoa) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlBudgetAmountCoaHome.findByCoaCodeAndBgtCode(java.lang.Integer COA_CODE, java.lang.Integer BGT_CODE, java.lang.Integer BC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlBudgetAmountCoaHome.findByCoaCodeAndBgtCode(java.lang.Integer COA_CODE, java.lang.Integer BGT_CODE, java.lang.Integer BC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getBcByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalGlBudgetAmountCoa create(java.lang.Integer BC_CODE, double BC_RL_AMNT, double BC_RL_PRCNTG1,
                                         double BC_RL_PRCNTG2, java.lang.String BC_DESC, java.lang.String BC_TYP, java.lang.Integer BC_AD_CMPNY)
			throws CreateException {
		try {

			LocalGlBudgetAmountCoa entity = new LocalGlBudgetAmountCoa();

			Debug.print("GlBudgetAmountCoa create");

			entity.setBcCode(BC_CODE);
			entity.setBcRuleAmount(BC_RL_AMNT);
			entity.setBcRulePercentage1(BC_RL_PRCNTG1);
			entity.setBcRulePercentage2(BC_RL_PRCNTG2);
			entity.setBcDescription(BC_DESC);
			entity.setBcRuleType(BC_TYP);
			entity.setBcAdCompany(BC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlBudgetAmountCoa create(double BC_RL_AMNT, double BC_RL_PRCNTG1, double BC_RL_PRCNTG2,
                                         java.lang.String BC_DESC, java.lang.String BC_TYP, java.lang.Integer BC_AD_CMPNY) throws CreateException {
		try {

			LocalGlBudgetAmountCoa entity = new LocalGlBudgetAmountCoa();

			Debug.print("GlBudgetAmountCoa create");

			entity.setBcRuleAmount(BC_RL_AMNT);
			entity.setBcRulePercentage1(BC_RL_PRCNTG1);
			entity.setBcRulePercentage2(BC_RL_PRCNTG2);
			entity.setBcDescription(BC_DESC);
			entity.setBcRuleType(BC_TYP);
			entity.setBcAdCompany(BC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}