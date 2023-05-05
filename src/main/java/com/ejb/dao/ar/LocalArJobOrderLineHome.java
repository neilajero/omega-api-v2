package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArJobOrderLine;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArJobOrderLineHome {

	public static final String JNDI_NAME = "LocalArJobOrderLineHome!com.ejb.ar.LocalArJobOrderLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArJobOrderLineHome() {
	}

	// FINDER METHODS

	public LocalArJobOrderLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArJobOrderLine entity = (LocalArJobOrderLine) em
					.find(new LocalArJobOrderLine(), pk);
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

	public java.util.Collection findOpenJolAll(java.lang.Integer JOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(jol) FROM ArJobOrderLine jol WHERE jol.jolAdCompany = ?1");
			query.setParameter(1, JOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderLineHome.findOpenJolAll(java.lang.Integer JOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySalesOrderCode(java.lang.Integer JO_CODE, java.lang.Integer JOL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jol) FROM ArJobOrderLine jol WHERE jol.arJobOrder.joCode = ?1 AND jol.jolAdCompany = ?2");
			query.setParameter(1, JO_CODE);
			query.setParameter(2, JOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderLineHome.findBySalesOrderCode(java.lang.Integer JO_CODE, java.lang.Integer JOL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArJobOrderLine findByJoCodeAndJolLineAndBrCode(java.lang.Integer JO_CODE, short JOL_LN,
			java.lang.Integer JO_AD_BRNCH, java.lang.Integer JOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jol) FROM ArJobOrderLine jol  WHERE jol.arJobOrder.joCode=?1 AND jol.jolLine=?2 AND jol.arJobOrder.joAdBranch = ?3 AND jol.arJobOrder.joAdCompany = ?4");
			query.setParameter(1, JO_CODE);
			query.setParameter(2, JOL_LN);
			query.setParameter(3, JO_AD_BRNCH);
			query.setParameter(4, JOL_AD_CMPNY);
            return (LocalArJobOrderLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArJobOrderLineHome.findByJoCodeAndJolLineAndBrCode(java.lang.Integer JO_CODE, short JOL_LN, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JOL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderLineHome.findByJoCodeAndJolLineAndBrCode(java.lang.Integer JO_CODE, short JOL_LN, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndJoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date JO_DT_FRM, java.util.Date JO_DT_TO, java.lang.Integer JO_AD_BRNCH,
			java.lang.Integer JOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jol) FROM ArJobOrderLine jol WHERE jol.invItemLocation.invItem.iiName = ?1 AND jol.invItemLocation.invLocation.locName = ?2 AND jol.arJobOrder.joDate >= ?3 AND jol.arJobOrder.joDate <= ?4 AND jol.arJobOrder.joAdBranch = ?5 AND jol.jolAdCompany = ?6");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, JO_DT_FRM);
			query.setParameter(4, JO_DT_TO);
			query.setParameter(5, JO_AD_BRNCH);
			query.setParameter(6, JOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderLineHome.findCommittedQtyByIiNameAndLocNameAndJoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date JO_DT_FRM, java.com.util.Date JO_DT_TO, java.lang.Integer JO_AD_BRNCH, java.lang.Integer JOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndWithoutDateAndJoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(jol) FROM ArJobOrderLine jol WHERE jol.invItemLocation.invItem.iiName = ?1 AND jol.invItemLocation.invLocation.locName = ?2 AND jol.arJobOrder.joAdBranch = ?3 AND jol.jolAdCompany = ?4");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, SO_AD_BRNCH);
			query.setParameter(4, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArJobOrderLineHome.findCommittedQtyByIiNameAndLocNameAndWithoutDateAndJoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSalesOrderLineByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArJobOrderLine create(Integer JOL_CODE, short JOL_LN, String JOL_IDESC, double JOL_QTY,
                                      double JOL_UNT_PRC, double JOL_AMNT, double JOL_DSCNT_1, double JOL_DSCNT_2, double JOL_DSCNT_3,
                                      double JOL_DSCNT_4, double TTL_JOL_DSCNT, double JOL_RQST_QTY, String JOL_MISC, byte JOL_TX,
                                      Integer JOL_AD_CMPNY) throws CreateException {
		try {

			LocalArJobOrderLine entity = new LocalArJobOrderLine();

			Debug.print("ArJobOrderLineBean create");

			entity.setJolCode(JOL_CODE);
			entity.setJolLine(JOL_LN);
			entity.setJolLineIDesc(JOL_IDESC);
			entity.setJolQuantity(JOL_QTY);
			entity.setJolUnitPrice(JOL_UNT_PRC);
			entity.setJolAmount(JOL_AMNT);
			entity.setJolDiscount1(JOL_DSCNT_1);
			entity.setJolDiscount2(JOL_DSCNT_2);
			entity.setJolDiscount3(JOL_DSCNT_3);
			entity.setJolDiscount4(JOL_DSCNT_4);
			entity.setJolTotalDiscount(TTL_JOL_DSCNT);
			entity.setJolRequestQuantity(JOL_RQST_QTY);
			entity.setJolMisc(JOL_MISC);
			entity.setJolTax(JOL_TX);
			entity.setJolAdCompany(JOL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArJobOrderLine create(short JOL_LN, String JOL_IDESC, double JOL_QTY, double JOL_UNT_PRC,
                                      double JOL_AMNT, double JOL_DSCNT_1, double JOL_DSCNT_2, double JOL_DSCNT_3, double JOL_DSCNT_4,
                                      double TTL_JOL_DSCNT, double JOL_RQST_QTY, String JOL_MISC, byte JOL_TX, Integer JOL_AD_CMPNY)
			throws CreateException {
		try {

			LocalArJobOrderLine entity = new LocalArJobOrderLine();

			Debug.print("ArJobOrderLineBean create");

			entity.setJolLine(JOL_LN);
			entity.setJolLineIDesc(JOL_IDESC);
			entity.setJolQuantity(JOL_QTY);
			entity.setJolUnitPrice(JOL_UNT_PRC);
			entity.setJolAmount(JOL_AMNT);
			entity.setJolDiscount1(JOL_DSCNT_1);
			entity.setJolDiscount2(JOL_DSCNT_2);
			entity.setJolDiscount3(JOL_DSCNT_3);
			entity.setJolDiscount4(JOL_DSCNT_4);
			entity.setJolTotalDiscount(TTL_JOL_DSCNT);
			entity.setJolRequestQuantity(JOL_RQST_QTY);
			entity.setJolMisc(JOL_MISC);
			entity.setJolTax(JOL_TX);
			entity.setJolAdCompany(JOL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}