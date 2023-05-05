package com.ejb.dao.inv;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvAdjustmentLine;
import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvAdjustmentLineHome {

	public static final String JNDI_NAME = "LocalInvAdjustmentLineHome!com.ejb.inv.LocalInvAdjustmentLineHome";

	@EJB
	public PersistenceBeanClass em;
	
	private double AL_UNT_CST = 0d;
	private String AL_QC_NUM = null;
	private Date AL_QC_EXPRY_DT = null;
	private double AL_ADJST_QTY = 0d;
	private double AL_SRVD = 0d;
	private byte AL_VD = EJBCommon.FALSE;
	private Integer AL_AD_CMPNY = null;

	public LocalInvAdjustmentLineHome() {
	}

	// FINDER METHODS

	public LocalInvAdjustmentLine findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvAdjustmentLine entity = (LocalInvAdjustmentLine) em
					.find(new LocalInvAdjustmentLine(), pk);
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

	public LocalInvAdjustmentLine findByAdjCodeAndIlCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer INV_IL_CODE,
			java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjCode=?1 AND al.invItemLocation.ilCode=?2 AND al.alAdCompany=?3");
			query.setParameter(1, INV_ADJ_CODE);
			query.setParameter(2, INV_IL_CODE);
			query.setParameter(3, AL_AD_CMPNY);
            return (LocalInvAdjustmentLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvAdjustmentLineHome.findByAdjCodeAndIlCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer INV_IL_CODE, java.lang.Integer AL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findByAdjCodeAndIlCode(java.lang.Integer INV_ADJ_CODE, java.lang.Integer INV_IL_CODE, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvAdjustmentLine findByAlCode(java.lang.Integer AL_CODE, java.lang.Integer AL_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.alCode=?1 AND al.alAdCompany=?2");
			query.setParameter(1, AL_CODE);
			query.setParameter(2, AL_AD_CMPNY);
            return (LocalInvAdjustmentLine) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvAdjustmentLineHome.findByAlCode(java.lang.Integer AL_CODE, java.lang.Integer AL_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findByAlCode(java.lang.Integer AL_CODE, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAdjByLessOrEqualDateAndAdjAdBranch(java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjPosted = 1 AND al.invAdjustment.adjVoid = 0  AND al.invAdjustment.adjAdBranch = ?1 AND al.alAdCompany = ?2");
			query.setParameter(1, ADJ_AD_BRNCH);
			query.setParameter(2, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findPostedAdjByLessOrEqualDateAndAdjAdBranch(java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(java.util.Date ADJ_DT,
			java.lang.Integer INV_IL_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al  WHERE al.invAdjustment.adjVoid = 0 AND al.invAdjustment.adjIsCostVariance = 1 AND al.invAdjustment.adjDate > ?1 AND al.invItemLocation.ilCode = ?2 AND al.invAdjustment.adjAdBranch = ?3 AND al.alAdCompany = ?4");
			query.setParameter(1, ADJ_DT);
			query.setParameter(2, INV_IL_CODE);
			query.setParameter(3, ADJ_AD_BRNCH);
			query.setParameter(4, ADJ_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(java.com.util.Date ADJ_DT, java.lang.Integer INV_IL_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnvoidAndIsCostVarianceGreaterThanAdjDateAndIlCodeAndBrCode(
			java.util.Date ADJ_DT,java.lang.Integer INV_IL_CODE,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer ADJ_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al "
							+ "WHERE al.invAdjustment.adjVoid = 0 AND al.invAdjustment.adjIsCostVariance = 1 "
							+ "AND al.invAdjustment.adjDate > ?1 AND al.invItemLocation.ilCode = ?2 "
							+ "AND al.invAdjustment.adjAdBranch = ?3 AND al.alAdCompany = ?4", companyShortName);
			query.setParameter(1, ADJ_DT);
			query.setParameter(2, INV_IL_CODE);
			query.setParameter(3, ADJ_AD_BRNCH);
			query.setParameter(4, ADJ_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByIiNameAndLocNameAndLessEqualDateAndAdjAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date ADJ_DT, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjPosted = 0 AND al.invAdjustment.adjVoid = 0 AND al.invItemLocation.invItem.iiName = ?1 AND al.invItemLocation.invLocation.locName = ?2 AND al.invAdjustment.adjDate <= ?3 AND al.invAdjustment.adjAdBranch = ?4 AND al.alAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, ADJ_DT);
			query.setParameter(4, ADJ_AD_BRNCH);
			query.setParameter(5, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnpostedAdjByIiNameAndLocNameAndLessEqualDateAndAdjAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date ADJ_DT, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findNegativeAlByAdjPostedAndIlCodeAndBrCode(byte ADJ_PSTD, java.lang.Integer INV_IL_CODE,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.alAdjustQuantity < 0 AND al.invAdjustment.adjPosted=?1 AND al.invItemLocation.ilCode=?2 AND al.invAdjustment.adjAdBranch=?3 AND al.alAdCompany=?4");
			query.setParameter(1, INV_IL_CODE);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findNegativeAlByAdjPostedAndIlCodeAndBrCode(byte ADJ_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedAdjByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjPosted = 0 AND al.invAdjustment.adjVoid = 0 AND al.invItemLocation.invLocation.locName = ?1 AND al.invAdjustment.adjAdBranch = ?2 AND al.alAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnpostedAdjByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedReference(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjPosted = 0 AND  al.invAdjustment.adjReferenceNumber = ?1 AND al.invAdjustment.adjAdBranch = ?2 AND al.alAdCompany = ?3");
			query.setParameter(1, ADJ_RFRNC_NMBR);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnpostedReference(java.lang.String ADJ_RFRNC_NMBR, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByAlVoidAndAdjCode(byte ADJ_VD, java.lang.Integer INV_ADJ_CODE,
			java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustment adj, IN(adj.invAdjustmentLines) al WHERE al.alVoid = ?1 AND adj.adjCode = ?2 AND al.alAdCompany = ?3");
			query.setParameter(1, ADJ_VD);
			query.setParameter(2, INV_ADJ_CODE);
			query.setParameter(3, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findByAlVoidAndAdjCode(byte ADJ_VD, java.lang.Integer INV_ADJ_CODE, java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findStockOnHand(java.lang.Integer DR_AD_CMPNY, java.util.Date ADJ_DT)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustment adj, IN(adj.invAdjustmentLines) al WHERE al.alVoid = 0 AND al.alAdCompany = ?1 AND al.invAdjustment.adjDate < ?2");
			query.setParameter(1, DR_AD_CMPNY);
			query.setParameter(2, ADJ_DT);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findStockOnHand(java.lang.Integer DR_AD_CMPNY, java.com.util.Date ADJ_DT)");
			throw ex;
		}
	}

	public java.util.Collection findGeneral(java.lang.Integer DR_AD_CMPNY, java.util.Date ADJ_DT)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustment adj, IN(adj.invAdjustmentLines) al WHERE al.alVoid = 0 AND al.alAdCompany = ?1 AND al.invAdjustment.adjType = 'GENERAL' AND al.invAdjustment.adjPosted = 1 AND al.invAdjustment.adjDate = ?2");
			query.setParameter(1, DR_AD_CMPNY);
			query.setParameter(2, ADJ_DT);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findGeneral(java.lang.Integer DR_AD_CMPNY, java.com.util.Date ADJ_DT)");
			throw ex;
		}
	}

	public java.util.Collection findPostedIssuance(java.lang.Integer DR_AD_CMPNY, java.util.Date ADJ_DT)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustment adj, IN(adj.invAdjustmentLines) al WHERE al.alVoid = 0 AND al.alAdCompany = ?1 AND al.invAdjustment.adjType = 'ISSUANCE' AND al.invAdjustment.adjPosted = 1 AND al.invAdjustment.adjDate = ?2");
			query.setParameter(1, DR_AD_CMPNY);
			query.setParameter(2, ADJ_DT);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findPostedIssuance(java.lang.Integer DR_AD_CMPNY, java.com.util.Date ADJ_DT)");
			throw ex;
		}
	}

	public java.util.Collection findUnPostedIssuance(java.lang.Integer DR_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustment adj, IN(adj.invAdjustmentLines) al WHERE al.alVoid = 0 AND al.alAdCompany = ?1 AND al.invAdjustment.adjType = 'ISSUANCE' AND al.invAdjustment.adjPosted = 0");
			query.setParameter(1, DR_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnPostedIssuance(java.lang.Integer DR_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedByItem(java.lang.Integer INV_ITEM_LOCATION, java.lang.Integer ADJ_AD_BRNCH,
			java.lang.Integer AL_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(al) FROM InvAdjustmentLine al WHERE al.invAdjustment.adjPosted = 0 AND al.invAdjustment.adjVoid = 0 AND al.invAdjustment.adjType = 'ISSUANCE' AND al.invItemLocation.invItem.iiCode = ?1 AND al.invAdjustment.adjAdBranch = ?2 AND al.alAdCompany = ?3");
			query.setParameter(1, INV_ITEM_LOCATION);
			query.setParameter(2, ADJ_AD_BRNCH);
			query.setParameter(3, AL_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvAdjustmentLineHome.findUnpostedByItem(java.lang.Integer INV_ITEM_LOCATION, java.lang.Integer ADJ_AD_BRNCH, java.lang.Integer AL_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getInvByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
	public LocalInvAdjustmentLine buildAdjustmentLine() throws CreateException {
		try {

			LocalInvAdjustmentLine entity = new LocalInvAdjustmentLine();

			Debug.print("invAdjustmentLineBean buildAdjustmentLine");

			entity.setAlUnitCost(AL_UNT_CST);
			entity.setAlQcNumber(AL_QC_NUM);
			entity.setAlQcExpiryDate(AL_QC_EXPRY_DT);
			entity.setAlAdjustQuantity(AL_ADJST_QTY);
			entity.setAlServed(AL_SRVD);
			entity.setAlVoid(AL_VD);
			entity.setAlAdCompany(AL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	
	public LocalInvAdjustmentLineHome AlUnitCost(double AL_UNT_CST) {
		this.AL_UNT_CST = AL_UNT_CST;
		return this;
	}
	
	public LocalInvAdjustmentLineHome AlQcNumber(String AL_QC_NUM) {
		this.AL_QC_NUM = AL_QC_NUM;
		return this;
	}
	
	public LocalInvAdjustmentLineHome setAlQcExpiryDate(Date AL_QC_EXPRY_DT) {
		this.AL_QC_EXPRY_DT = AL_QC_EXPRY_DT;
		return this;
	}
	
	public LocalInvAdjustmentLineHome AlAdjustQuantity(double AL_ADJST_QTY) {
		this.AL_ADJST_QTY = AL_ADJST_QTY;
		return this;
	}
	
	public LocalInvAdjustmentLineHome AlServed(double AL_SRVD) {
		this.AL_SRVD = AL_SRVD;
		return this;
	}
	
	public LocalInvAdjustmentLineHome AlVoid(byte AL_VD) {
		this.AL_VD = AL_VD;
		return this;
	}
	
	public LocalInvAdjustmentLineHome AlAdCompany(Integer AL_AD_CMPNY) {
		this.AL_AD_CMPNY = AL_AD_CMPNY;
		return this;
	}

	public LocalInvAdjustmentLine create(java.lang.Integer AL_CODE, double AL_UNT_CST, String AL_QC_NUM,
                                         Date AL_QC_EXPRY_DT, double AL_ADJST_QTY, double AL_SRVD, byte AL_VD, Integer AL_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvAdjustmentLine entity = new LocalInvAdjustmentLine();

			Debug.print("invAdjustmentLineBean create");

			entity.setAlCode(AL_CODE);
			entity.setAlUnitCost(AL_UNT_CST);
			entity.setAlQcNumber(AL_QC_NUM);
			entity.setAlQcExpiryDate(AL_QC_EXPRY_DT);
			entity.setAlAdjustQuantity(AL_ADJST_QTY);
			entity.setAlServed(AL_SRVD);
			entity.setAlVoid(AL_VD);
			entity.setAlAdCompany(AL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvAdjustmentLine create(double AL_UNT_CST, String AL_QC_NUM, Date AL_QC_EXPRY_DT,
                                         double AL_ADJST_QTY, double AL_SRVD, byte AL_VD, Integer AL_AD_CMPNY) throws CreateException {
		try {

			LocalInvAdjustmentLine entity = new LocalInvAdjustmentLine();

			Debug.print("invAdjustmentLineBean create");

			entity.setAlUnitCost(AL_UNT_CST);
			entity.setAlQcNumber(AL_QC_NUM);
			entity.setAlQcExpiryDate(AL_QC_EXPRY_DT);
			entity.setAlAdjustQuantity(AL_ADJST_QTY);
			entity.setAlServed(AL_SRVD);
			entity.setAlVoid(AL_VD);
			entity.setAlAdCompany(AL_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}