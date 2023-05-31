package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArSalesOrderLine;
import com.util.Debug;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class LocalArSalesOrderLineHome {

	public static final String JNDI_NAME = "LocalArSalesOrderLineHome!com.ejb.ar.LocalArSalesOrderLineHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArSalesOrderLineHome() {
	}

	// FINDER METHODS

	public LocalArSalesOrderLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArSalesOrderLine entity = (LocalArSalesOrderLine) em
					.find(new LocalArSalesOrderLine(), pk);
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

	public java.util.Collection findOpenSolAll(java.lang.Integer SOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(sol) FROM ArSalesOrderLine sol WHERE sol.solAdCompany = ?1");
			query.setParameter(1, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderLineHome.findOpenSolAll(java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findBySalesOrderCode(java.lang.Integer SO_CODE, java.lang.Integer SOL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sol) FROM ArSalesOrderLine sol WHERE sol.arSalesOrder.soCode = ?1 AND sol.solAdCompany = ?2");
			query.setParameter(1, SO_CODE);
			query.setParameter(2, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderLineHome.findBySalesOrderCode(java.lang.Integer SO_CODE, java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndSoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date SO_DT_FRM, java.util.Date SO_DT_TO, java.lang.Integer SO_AD_BRNCH,
			java.lang.Integer SOL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sol) FROM ArSalesOrderLine sol WHERE sol.invItemLocation.invItem.iiName = ?1 AND sol.invItemLocation.invLocation.locName = ?2 AND sol.arSalesOrder.soDate >= ?3 AND sol.arSalesOrder.soDate <= ?4 AND sol.arSalesOrder.soAdBranch = ?5 AND sol.solAdCompany = ?6");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, SO_DT_FRM);
			query.setParameter(4, SO_DT_TO);
			query.setParameter(5, SO_AD_BRNCH);
			query.setParameter(6, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderLineHome.findCommittedQtyByIiNameAndLocNameAndSoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date SO_DT_FRM, java.com.util.Date SO_DT_TO, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCommittedQtyByIiNameAndLocNameAndWithoutDateAndSoAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sol) FROM ArSalesOrderLine sol WHERE sol.invItemLocation.invItem.iiName = ?1 AND sol.invItemLocation.invLocation.locName = ?2 AND sol.arSalesOrder.soAdBranch = ?3 AND sol.solAdCompany = ?4");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, SO_AD_BRNCH);
			query.setParameter(4, SOL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArSalesOrderLineHome.findCommittedQtyByIiNameAndLocNameAndWithoutDateAndSoAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.lang.Integer SO_AD_BRNCH, java.lang.Integer SOL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getSalesOrderLineByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArSalesOrderLine create(Integer SOL_CODE, short SOL_LN, String SOL_IDESC, double SOL_QTY,
                                        double SOL_UNT_PRC, double SOL_AMNT, double SOL_GRSS_AMNT, double SOL_TX_AMNT,
										double SOL_DSCNT_1, double SOL_DSCNT_2, double SOL_DSCNT_3,
                                        double SOL_DSCNT_4, double TTL_SOL_DSCNT, double SOL_RQST_QTY, String SOL_MISC, byte SOL_TX,
                                        Integer SOL_AD_CMPNY) throws CreateException {
		try {

			LocalArSalesOrderLine entity = new LocalArSalesOrderLine();

			Debug.print("ArSalesOrderLineBean create");

			entity.setSolCode(SOL_CODE);
			entity.setSolLine(SOL_LN);
			entity.setSolLineIDesc(SOL_IDESC);
			entity.setSolQuantity(SOL_QTY);
			entity.setSolUnitPrice(SOL_UNT_PRC);
			entity.setSolAmount(SOL_AMNT);
			entity.setSolGrossAmount(SOL_GRSS_AMNT);
			entity.setSolTaxAmount(SOL_TX_AMNT);
			entity.setSolDiscount1(SOL_DSCNT_1);
			entity.setSolDiscount2(SOL_DSCNT_2);
			entity.setSolDiscount3(SOL_DSCNT_3);
			entity.setSolDiscount4(SOL_DSCNT_4);
			entity.setSolTotalDiscount(TTL_SOL_DSCNT);
			entity.setSolRequestQuantity(SOL_RQST_QTY);
			entity.setSolMisc(SOL_MISC);
			entity.setSolTax(SOL_TX);
			entity.setSolAdCompany(SOL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArSalesOrderLine create(short SOL_LN, String SOL_IDESC, double SOL_QTY, double SOL_UNT_PRC,
                                        double SOL_AMNT, double SOL_GRSS_AMNT, double SOL_TX_AMNT,
										double SOL_DSCNT_1, double SOL_DSCNT_2, double SOL_DSCNT_3, double SOL_DSCNT_4,
                                        double TTL_SOL_DSCNT, double SOL_RQST_QTY, String SOL_MISC, byte SOL_TX, Integer SOL_AD_CMPNY)
			throws CreateException {
		try {

			LocalArSalesOrderLine entity = new LocalArSalesOrderLine();

			Debug.print("ArSalesOrderLineBean create");

			entity.setSolLine(SOL_LN);
			entity.setSolLineIDesc(SOL_IDESC);
			entity.setSolQuantity(SOL_QTY);
			entity.setSolUnitPrice(SOL_UNT_PRC);
			entity.setSolAmount(SOL_AMNT);
			entity.setSolGrossAmount(SOL_GRSS_AMNT);
			entity.setSolTaxAmount(SOL_TX_AMNT);
			entity.setSolDiscount1(SOL_DSCNT_1);
			entity.setSolDiscount2(SOL_DSCNT_2);
			entity.setSolDiscount3(SOL_DSCNT_3);
			entity.setSolDiscount4(SOL_DSCNT_4);
			entity.setSolTotalDiscount(TTL_SOL_DSCNT);
			entity.setSolRequestQuantity(SOL_RQST_QTY);
			entity.setSolMisc(SOL_MISC);
			entity.setSolTax(SOL_TX);
			entity.setSolAdCompany(SOL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}