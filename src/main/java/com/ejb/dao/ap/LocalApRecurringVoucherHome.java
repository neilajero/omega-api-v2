package com.ejb.dao.ap;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApRecurringVoucher;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApRecurringVoucherHome {

	public static final String JNDI_NAME = "LocalApRecurringVoucherHome!com.ejb.ap.LocalApRecurringVoucherHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApRecurringVoucherHome() {
	}

	// FINDER METHODS

	public LocalApRecurringVoucher findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApRecurringVoucher entity = (LocalApRecurringVoucher) em
					.find(new LocalApRecurringVoucher(), pk);
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

	public LocalApRecurringVoucher findByRvName(java.lang.String RV_NM, java.lang.Integer RV_AD_BRNCH,
			java.lang.Integer RV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvName = ?1 AND rv.rvAdBranch = ?2 AND rv.rvAdCompany = ?3");
			query.setParameter(1, RV_NM);
			query.setParameter(2, RV_AD_BRNCH);
			query.setParameter(3, RV_AD_CMPNY);
            return (LocalApRecurringVoucher) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApRecurringVoucherHome.findByRvName(java.lang.String RV_NM, java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByRvName(java.lang.String RV_NM, java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findRvToGenerateByAdUsrCodeAndDate(java.lang.Integer USR_CODE, java.util.Date DT,
			java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE (rv.rvAdUserName1=?1 OR rv.rvAdUserName2=?1 OR rv.rvAdUserName3=?1 OR rv.rvAdUserName4=?1 OR rv.rvAdUserName5=?1) AND rv.rvNextRunDate <= ?2 AND rv.rvAdBranch = ?3 AND rv.rvAdCompany = ?4");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, DT);
			query.setParameter(3, RV_AD_BRNCH);
			query.setParameter(4, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findRvToGenerateByAdUsrCodeAndDate(java.lang.Integer USR_CODE, java.com.util.Date DT, java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode1(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvAdUserName1 = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByUserCode1(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode2(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvAdUserName2 = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByUserCode2(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode3(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvAdUserName3 = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByUserCode3(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode4(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvAdUserName4 = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByUserCode4(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByUserCode5(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvAdUserName5 = ?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, USR_CODE);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByUserCode5(java.lang.Integer USR_CODE, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVbName(java.lang.String VB_NM, java.lang.Integer RV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv  WHERE rv.apVoucherBatch.vbName=?1 AND rv.rvAdCompany = ?2");
			query.setParameter(1, VB_NM);
			query.setParameter(2, RV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByVbName(java.lang.String VB_NM, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalApRecurringVoucher findByRvNameAndSupplierCode(java.lang.String RV_NM, java.lang.String SPL_SPPLR_CODE,
			java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(rv) FROM ApRecurringVoucher rv WHERE rv.rvName = ?1 AND rv.apSupplier.splSupplierCode = ?2 AND rv.rvAdBranch = ?3 AND rv.rvAdCompany = ?4");
			query.setParameter(1, RV_NM);
			query.setParameter(2, SPL_SPPLR_CODE);
			query.setParameter(3, RV_AD_BRNCH);
			query.setParameter(4, RV_AD_CMPNY);
            return (LocalApRecurringVoucher) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ap.LocalApRecurringVoucherHome.findByRvNameAndSupplierCode(java.lang.String RV_NM, java.lang.String SPL_SPPLR_CODE, java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApRecurringVoucherHome.findByRvNameAndSupplierCode(java.lang.String RV_NM, java.lang.String SPL_SPPLR_CODE, java.lang.Integer RV_AD_BRNCH, java.lang.Integer RV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getRvByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalApRecurringVoucher create(Integer AP_RV_CODE, String RV_NM, String RV_DESC, Date RV_CNVRSN_DT,
                                          double RV_CNVRSN_RT, double RV_AMNT, double RV_AMNT_DUE, java.lang.Integer RV_AD_USR_NM1,
                                          java.lang.Integer RV_AD_USR_NM2, java.lang.Integer RV_AD_USR_NM3, java.lang.Integer RV_AD_USR_NM4,
                                          java.lang.Integer RV_AD_USR_NM5, String RV_SCHDL, Date RV_NXT_RN_DT, Date RV_LST_RN_DT, Integer RV_AD_BRNCH,
                                          Integer RV_AD_CMPNY) throws CreateException {
		try {

			LocalApRecurringVoucher entity = new LocalApRecurringVoucher();

			Debug.print("ApRecurringVoucherBean create");
			entity.setRvCode(AP_RV_CODE);
			entity.setRvName(RV_NM);
			entity.setRvDescription(RV_DESC);
			entity.setRvConversionDate(RV_CNVRSN_DT);
			entity.setRvConversionRate(RV_CNVRSN_RT);
			entity.setRvAmount(RV_AMNT);
			entity.setRvAmountDue(RV_AMNT_DUE);
			entity.setRvAdUserName1(RV_AD_USR_NM1);
			entity.setRvAdUserName2(RV_AD_USR_NM2);
			entity.setRvAdUserName3(RV_AD_USR_NM3);
			entity.setRvAdUserName4(RV_AD_USR_NM4);
			entity.setRvAdUserName5(RV_AD_USR_NM5);
			entity.setRvSchedule(RV_SCHDL);
			entity.setRvNextRunDate(RV_NXT_RN_DT);
			entity.setRvLastRunDate(RV_LST_RN_DT);
			entity.setRvAdBranch(RV_AD_BRNCH);
			entity.setRvAdCompany(RV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApRecurringVoucher create(String RV_NM, String RV_DESC, Date RV_CNVRSN_DT,
                                          double RV_CNVRSN_RT, double RV_AMNT, double RV_AMNT_DUE, java.lang.Integer RV_AD_USR_NM1,
                                          java.lang.Integer RV_AD_USR_NM2, java.lang.Integer RV_AD_USR_NM3, java.lang.Integer RV_AD_USR_NM4,
                                          java.lang.Integer RV_AD_USR_NM5, String RV_SCHDL, Date RV_NXT_RN_DT, Date RV_LST_RN_DT, Integer RV_AD_BRNCH,
                                          Integer RV_AD_CMPNY) throws CreateException {
		try {

			LocalApRecurringVoucher entity = new LocalApRecurringVoucher();

			Debug.print("ApRecurringVoucherBean create");
			entity.setRvName(RV_NM);
			entity.setRvDescription(RV_DESC);
			entity.setRvConversionDate(RV_CNVRSN_DT);
			entity.setRvConversionRate(RV_CNVRSN_RT);
			entity.setRvAmount(RV_AMNT);
			entity.setRvAmountDue(RV_AMNT_DUE);
			entity.setRvAdUserName1(RV_AD_USR_NM1);
			entity.setRvAdUserName2(RV_AD_USR_NM2);
			entity.setRvAdUserName3(RV_AD_USR_NM3);
			entity.setRvAdUserName4(RV_AD_USR_NM4);
			entity.setRvAdUserName5(RV_AD_USR_NM5);
			entity.setRvSchedule(RV_SCHDL);
			entity.setRvNextRunDate(RV_NXT_RN_DT);
			entity.setRvLastRunDate(RV_LST_RN_DT);
			entity.setRvAdBranch(RV_AD_BRNCH);
			entity.setRvAdCompany(RV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}