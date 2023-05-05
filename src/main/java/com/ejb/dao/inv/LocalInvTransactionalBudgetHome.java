package com.ejb.dao.inv;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvTransactionalBudget;
import com.util.Debug;

@Stateless
public class LocalInvTransactionalBudgetHome {

	public static final String JNDI_NAME = "LocalInvTransactionalBudgetHome!com.ejb.inv.LocalInvTransactionalBudgetHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalInvTransactionalBudgetHome() {
	}

	// FINDER METHODS

	public LocalInvTransactionalBudget findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvTransactionalBudget entity = (LocalInvTransactionalBudget) em
					.find(new LocalInvTransactionalBudget(), pk);
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

	public java.util.Collection findByTbCode(java.lang.Integer TB_CODE, java.lang.Integer TB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbCode=?1 AND tb.tbAdCompany=?2");
			query.setParameter(1, TB_CODE);
			query.setParameter(2, TB_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbCode(java.lang.Integer TB_CODE, java.lang.Integer TB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTbItemNameAll(java.lang.String TB_NAME) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbName=?1");
			query.setParameter(1, TB_NAME);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemNameAll(java.lang.String TB_NAME)");
			throw ex;
		}
	}

	public java.util.Collection findByTbTotalAmountAndYear(java.lang.Double TB_TTL_AMNT, java.lang.Integer TB_YR)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbTotalAmount=?1 AND tb.tbYear=?2");
			query.setParameter(1, TB_TTL_AMNT);
			query.setParameter(2, TB_YR);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbTotalAmountAndYear(java.lang.Double TB_TTL_AMNT, java.lang.Integer TB_YR)");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbInvItem(java.lang.Integer INV_ITEM) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.invItem.iiCode=?1");
			query.setParameter(1, INV_ITEM);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbInvItem(java.lang.Integer INV_ITEM)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbInvItem(java.lang.Integer INV_ITEM)");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbItemName(java.lang.String TB_NAME) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbName=?1");
			query.setParameter(1, TB_NAME);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemName(java.lang.String TB_NAME)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemName(java.lang.String TB_NAME)");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbItemNameAndDepartment(java.lang.String TB_NAME,
			java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbName=?1 AND tb.tbAdLookupValue=?2 AND tb.tbAdCompany=?3");
			query.setParameter(1, TB_NAME);
			query.setParameter(2, AD_LOOK_UP_VALUE);
			query.setParameter(3, TB_AD_CMPNY);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemNameAndDepartment(java.lang.String TB_NAME, java.lang.Integer AD_LOOK_UP_VALUE,java.lang.Integer TB_AD_CMPNY )");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemNameAndDepartment(java.lang.String TB_NAME, java.lang.Integer AD_LOOK_UP_VALUE,java.lang.Integer TB_AD_CMPNY )");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbDepartmentCode(java.lang.Integer AD_LOOK_UP_VALUE,
			java.lang.Integer TB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbAdLookupValue=?1 AND tb.tbAdCompany=?2");
			query.setParameter(1, AD_LOOK_UP_VALUE);
			query.setParameter(2, TB_AD_CMPNY);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbDepartmentCode(java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbDepartmentCode(java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbCodeAndDepartmentCode(java.lang.Integer TB_CODE,
			java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbCode=?1 AND tb.tbAdLookupValue=?2 AND tb.tbAdCompany=?3");
			query.setParameter(1, TB_CODE);
			query.setParameter(2, AD_LOOK_UP_VALUE);
			query.setParameter(3, TB_AD_CMPNY);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbCodeAndDepartmentCode(java.lang.Integer TB_CODE, java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbCodeAndDepartmentCode(java.lang.Integer TB_CODE, java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbItemNameAndTbDeptAndTbYear(java.lang.String TB_NAME,
			java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbName=?1 AND tb.tbAdLookupValue=?2 AND tb.tbYear=?3 AND tb.tbAdCompany=?4");
			query.setParameter(1, TB_NAME);
			query.setParameter(2, AD_LOOK_UP_VALUE);
			query.setParameter(3, TB_YR);
			query.setParameter(4, TB_AD_CMPNY);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(java.lang.String TB_NAME, java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY )");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbItemNameAndTbDeptAndTbYear(java.lang.String TB_NAME, java.lang.Integer AD_LOOK_UP_VALUE, java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY )");
			throw ex;
		}
	}

	public LocalInvTransactionalBudget findByTbYear(java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(tb) FROM InvTransactionalBudget tb WHERE tb.tbYear=?1 AND tb.tbAdCompany=?2");
			query.setParameter(1, TB_YR);
			query.setParameter(2, TB_AD_CMPNY);
            return (LocalInvTransactionalBudget) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbYear(java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvTransactionalBudgetHome.findByTbYear(java.lang.Integer TB_YR, java.lang.Integer TB_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getTbByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalInvTransactionalBudget create(Integer TB_CODE, String TB_NAME, String TB_DESC,
                                              double TB_QNTTY_JAN, double TB_QNTTY_FEB, double TB_QNTTY_MRCH, double TB_QNTTY_APRL, double TB_QNTTY_MAY,
                                              double TB_QNTTY_JUN, double TB_QNTTY_JUL, double TB_QNTTY_AUG, double TB_QNTTY_SEP, double TB_QNTTY_OCT,
                                              double TB_QNTTY_NOV, double TB_QNTTY_DEC, double TB_AMNT, double TB_TTL_AMNT, Integer TB_YR,
                                              Integer TB_AD_CMPNY) throws CreateException {
		try {

			LocalInvTransactionalBudget entity = new LocalInvTransactionalBudget();

			Debug.print("InvTransactionalBudget create");

			entity.setTbCode(TB_CODE);
			entity.setTbName(TB_NAME);
			entity.setTbDesc(TB_DESC);
			entity.setTbQuantityJan(TB_QNTTY_JAN);
			entity.setTbQuantityFeb(TB_QNTTY_FEB);
			entity.setTbQuantityMrch(TB_QNTTY_MRCH);
			entity.setTbQuantityAprl(TB_QNTTY_APRL);
			entity.setTbQuantityMay(TB_QNTTY_MAY);
			entity.setTbQuantityJun(TB_QNTTY_JUN);
			entity.setTbQuantityJul(TB_QNTTY_JUL);
			entity.setTbQuantityAug(TB_QNTTY_AUG);
			entity.setTbQuantitySep(TB_QNTTY_SEP);
			entity.setTbQuantityOct(TB_QNTTY_OCT);
			entity.setTbQuantityNov(TB_QNTTY_NOV);
			entity.setTbQuantityDec(TB_QNTTY_DEC);
			entity.setTbAmount(TB_AMNT);
			entity.setTbTotalAmount(TB_TTL_AMNT);
			entity.setTbYear(TB_YR);
			entity.setTbAdCompany(TB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvTransactionalBudget create(String TB_NAME, String TB_DESC, double TB_QNTTY_JAN,
                                              double TB_QNTTY_FEB, double TB_QNTTY_MRCH, double TB_QNTTY_APRL, double TB_QNTTY_MAY, double TB_QNTTY_JUN,
                                              double TB_QNTTY_JUL, double TB_QNTTY_AUG, double TB_QNTTY_SEP, double TB_QNTTY_OCT, double TB_QNTTY_NOV,
                                              double TB_QNTTY_DEC, double TB_AMNT, double TB_TTL_AMNT, Integer TB_YR, Integer TB_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvTransactionalBudget entity = new LocalInvTransactionalBudget();

			Debug.print("InvTransactionalBudget create");

			entity.setTbName(TB_NAME);
			entity.setTbDesc(TB_DESC);
			entity.setTbQuantityJan(TB_QNTTY_JAN);
			entity.setTbQuantityFeb(TB_QNTTY_FEB);
			entity.setTbQuantityMrch(TB_QNTTY_MRCH);
			entity.setTbQuantityAprl(TB_QNTTY_APRL);
			entity.setTbQuantityMay(TB_QNTTY_MAY);
			entity.setTbQuantityJun(TB_QNTTY_JUN);
			entity.setTbQuantityJul(TB_QNTTY_JUL);
			entity.setTbQuantityAug(TB_QNTTY_AUG);
			entity.setTbQuantitySep(TB_QNTTY_SEP);
			entity.setTbQuantityOct(TB_QNTTY_OCT);
			entity.setTbQuantityNov(TB_QNTTY_NOV);
			entity.setTbQuantityDec(TB_QNTTY_DEC);
			entity.setTbAmount(TB_AMNT);
			entity.setTbTotalAmount(TB_TTL_AMNT);
			entity.setTbYear(TB_YR);
			entity.setTbAdCompany(TB_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}