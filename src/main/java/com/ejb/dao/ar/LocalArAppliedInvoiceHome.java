package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArAppliedInvoice;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import com.util.EJBCommon;

@Stateless
public class LocalArAppliedInvoiceHome {

	public static final String JNDI_NAME = "LocalArAppliedInvoiceHome!com.ejb.ar.LocalArAppliedInvoiceHome";

	@EJB
	public PersistenceBeanClass em;
	private double AI_APPLY_AMNT = 0d;
	private double AI_PNTLY_APPLY_AMNT = 0d;
	private double AI_CRDTBL_W_TX = 0d;
	private double AI_DSCNT_AMNT = 0d;
	private double AI_RBT = 0d;
	private double AI_APPLD_DPST = 0d;
	private double AI_ALLCTD_PYMNT_AMNT = 0d;
	private double AI_FRX_GN_LSS = 0d;
	private byte AI_APPLY_RBT = EJBCommon.FALSE;
	private Integer AI_AD_CMPNY = null;

	public LocalArAppliedInvoiceHome() {
	}

	public LocalArAppliedInvoice findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArAppliedInvoice entity = (LocalArAppliedInvoice) em
					.find(new LocalArAppliedInvoice(), pk);
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

	public java.util.Collection findPostedAiByRctTypeAndRctDateRange(java.lang.String RCT_TYP,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.Integer AI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai, IN(ai.arDistributionRecords) dr WHERE ai.arReceipt.rctType = ?1 AND ai.arReceipt.rctDate >= ?2 AND ai.arReceipt.rctDate <= ?3 AND ai.arReceipt.rctPosted = 1 AND ai.arReceipt.rctVoid = 0 AND dr.drDebit = 0 AND dr.drClass = 'TAX' AND ai.aiAdCompany = ?4 ORDER BY ai.arReceipt.rctDate");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findPostedAiByRctTypeAndRctDateRange(java.lang.String RCT_TYP, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAiByIpsCode(java.lang.Integer IPS_CODE, java.lang.Integer AI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arInvoicePaymentSchedule.ipsCode = ?1 AND ai.arReceipt.rctVoid = 0 AND ai.arReceipt.rctPosted = 1 AND ai.aiAdCompany = ?2");
			query.setParameter(1, IPS_CODE);
			query.setParameter(2, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findPostedAiByIpsCode(java.lang.Integer IPS_CODE, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findPostedAiByRctTypeAndRctDateRangeAndCstNameAndTcType(java.lang.String RCT_TYP,
			java.util.Date RCT_DT_FRM, java.util.Date RCT_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP,
			java.lang.Integer AI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai, IN(ai.arDistributionRecords) dr WHERE ai.arReceipt.rctType = ?1 AND ai.arReceipt.rctDate >= ?2 AND ai.arReceipt.rctDate <= ?3 AND  ai.arReceipt.arCustomer.cstName = ?4 AND ai.arReceipt.arTaxCode.tcType = ?5 AND ai.arReceipt.rctPosted = 1 AND ai.arReceipt.rctVoid = 0 AND dr.drDebit = 0 AND dr.drClass = 'TAX' AND ai.aiAdCompany = ?6");
			query.setParameter(1, RCT_TYP);
			query.setParameter(2, RCT_DT_FRM);
			query.setParameter(3, RCT_DT_TO);
			query.setParameter(4, CST_NM);
			query.setParameter(5, TC_TYP);
			query.setParameter(6, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findPostedAiByRctTypeAndRctDateRangeAndCstNameAndTcType(java.lang.String RCT_TYP, java.com.util.Date RCT_DT_FRM, java.com.util.Date RCT_DT_TO, java.lang.String CST_NM, java.lang.String TC_TYP, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByIpsCode(java.lang.Integer IPS_CODE, java.lang.Integer AI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arInvoicePaymentSchedule.ipsCode=?1 AND ai.aiAdCompany=?2");
			query.setParameter(1, IPS_CODE);
			query.setParameter(2, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findByIpsCode(java.lang.Integer IPS_CODE, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalArAppliedInvoice findByPdcCode(java.lang.Integer PDC_CODE, java.lang.Integer AI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arPdc.pdcCode=?1 AND ai.aiAdCompany=?2");
			query.setParameter(1, PDC_CODE);
			query.setParameter(2, AI_AD_CMPNY);
            return (LocalArAppliedInvoice) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArAppliedInvoiceHome.findByPdcCode(java.lang.Integer PDC_CODE, java.lang.Integer AI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findByPdcCode(java.lang.Integer PDC_CODE, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByIpsCodeAndRctCode(java.lang.Integer IPS_CODE, java.lang.Integer RCT_CODE,
  			java.lang.Integer AI_AD_CMPNY) throws FinderException {
  
  		try {
  			Query query = em.createQuery(
  					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arInvoicePaymentSchedule.ipsCode=?1 AND ai.arReceipt.rctCode=?2 AND ai.aiAdCompany=?3");
  			query.setParameter(1, IPS_CODE);
  			query.setParameter(2, RCT_CODE);
  			query.setParameter(3, AI_AD_CMPNY);
            return query.getResultList();
  		} catch (Exception ex) {
  			Debug.print(
  					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findByIpsCodeAndRctCode(java.lang.Integer IPS_CODE, java.lang.Integer RCT_CODE, java.lang.Integer AI_AD_CMPNY)");
  			throw ex;
  		}
  	}

  	public java.util.Collection findByRctCode(java.lang.Integer RCT_CODE,
            java.lang.Integer AI_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctCode=?1 AND ai.aiAdCompany=?2");
            query.setParameter(1, RCT_CODE);
            query.setParameter(2, AI_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            Debug.print(
                    "EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findByRctCode(java.lang.Integer RCT_CODE, java.lang.Integer AI_AD_CMPNY)");
            throw ex;
        }
    }

	public java.util.Collection findByRctCode(java.lang.Integer RCT_CODE,
											  java.lang.Integer AI_AD_CMPNY,
											  String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctCode=?1 AND ai.aiAdCompany=?2",
					companyShortName);
			query.setParameter(1, RCT_CODE);
			query.setParameter(2, AI_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}


	public java.util.Collection findUnpostedAiWithDepositByCstCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer AI_AD_BRNCH, java.lang.Integer AI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctPosted = 0 AND ai.arReceipt.rctVoid = 0 AND ai.aiAppliedDeposit > 0 AND ai.arReceipt.arCustomer.cstCustomerCode = ?1 AND ai.arReceipt.rctAdBranch = ?2 AND ai.aiAdCompany=?3");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, AI_AD_BRNCH);
			query.setParameter(3, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findUnpostedAiWithDepositByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AI_AD_BRNCH, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAiWithCreditBalancePaidByCstCustomerCode(java.lang.String CST_CSTMR_CODE,
			java.lang.Integer AI_AD_BRNCH, java.lang.Integer AI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctVoid = 0 AND ai.aiCreditBalancePaid > 0 AND ai.arReceipt.arCustomer.cstCustomerCode = ?1 AND ai.arReceipt.rctAdBranch = ?2 AND ai.aiAdCompany=?3");
			query.setParameter(1, CST_CSTMR_CODE);
			query.setParameter(2, AI_AD_BRNCH);
			query.setParameter(3, AI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findAiWithCreditBalancePaidByCstCustomerCode(java.lang.String CST_CSTMR_CODE, java.lang.Integer AI_AD_BRNCH, java.lang.Integer AI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAiByInvCode(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arInvoicePaymentSchedule.arInvoice.invCode = ?1 AND ai.aiAdCompany = ?2");
			query.setParameter(1, INV_CODE);
			query.setParameter(2, INV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findAiByInvCode(java.lang.Integer INV_CODE, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAiInvNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctVoid = 0 AND ai.arReceipt.rctPosted = 1 AND ai.arInvoicePaymentSchedule.arInvoice.invNumber = ?1 AND ai.aiAdCompany = ?2");
			query.setParameter(1, INV_NMBR);
			query.setParameter(2, INV_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findAiInvNumber(java.lang.String INV_NMBR, java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAiPmProject(java.lang.Integer INV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ai) FROM ArAppliedInvoice ai WHERE ai.arReceipt.rctVoid = 0 AND ai.arInvoicePaymentSchedule.arInvoice.pmProject IS NOT NULL AND ai.aiAdCompany = ?1");
			query.setParameter(1, INV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArAppliedInvoiceHome.findAiPmProject(java.lang.Integer INV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getAiByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	public LocalArAppliedInvoiceHome AiApplyAmount(double AI_APPLY_AMNT) {
		this.AI_APPLY_AMNT = AI_APPLY_AMNT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiPenaltyApplyAmount(double AI_PNTLY_APPLY_AMNT) {
		this.AI_PNTLY_APPLY_AMNT = AI_PNTLY_APPLY_AMNT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiCreditableWTax(double AI_CRDTBL_W_TX) {
		this.AI_CRDTBL_W_TX = AI_CRDTBL_W_TX;
		return this;
	}

	public LocalArAppliedInvoiceHome AiDiscountAmount(double AI_DSCNT_AMNT) {
		this.AI_DSCNT_AMNT = AI_DSCNT_AMNT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiRebate(double AI_RBT) {
		this.AI_RBT = AI_RBT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiAppliedDeposit(double AI_APPLD_DPST) {
		this.AI_APPLD_DPST = AI_APPLD_DPST;
		return this;
	}

	public LocalArAppliedInvoiceHome AiAllocatedPaymentAmount(double AI_ALLCTD_PYMNT_AMNT) {
		this.AI_ALLCTD_PYMNT_AMNT = AI_ALLCTD_PYMNT_AMNT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiForexGainLoss(double AI_FRX_GN_LSS) {
		this.AI_FRX_GN_LSS = AI_FRX_GN_LSS;
		return this;
	}

	public LocalArAppliedInvoiceHome AiApplyRebate(byte AI_APPLY_RBT) {
		this.AI_APPLY_RBT = AI_APPLY_RBT;
		return this;
	}

	public LocalArAppliedInvoiceHome AiAdCompany(Integer AI_AD_CMPNY) {
		this.AI_AD_CMPNY = AI_AD_CMPNY;
		return this;
	}

	public LocalArAppliedInvoice buildAppliedInvoice(String companyShortName)
			throws CreateException {
		try {

			LocalArAppliedInvoice entity = new LocalArAppliedInvoice();

			Debug.print("ArAppliedInvoiceBean buildAppliedInvoice");
			entity.setAiApplyAmount(AI_APPLY_AMNT);
			entity.setAiPenaltyApplyAmount(AI_PNTLY_APPLY_AMNT);
			entity.setAiCreditableWTax(AI_CRDTBL_W_TX);
			entity.setAiDiscountAmount(AI_DSCNT_AMNT);
			entity.setAiRebate(AI_RBT);
			entity.setAiAppliedDeposit(AI_APPLD_DPST);
			entity.setAiAllocatedPaymentAmount(AI_ALLCTD_PYMNT_AMNT);
			entity.setAiForexGainLoss(AI_FRX_GN_LSS);
			entity.setAiApplyRebate(AI_APPLY_RBT);
			entity.setAiAdCompany(AI_AD_CMPNY);

			em.persist(entity, companyShortName);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArAppliedInvoice create(Integer AI_CODE, double AI_APPLY_AMNT, double AI_PNTLY_APPLY_AMNT,
                                        double AI_CRDTBL_W_TX, double AI_DSCNT_AMNT, double AI_RBT, double AI_APPLD_DPST,
                                        double AI_ALLCTD_PYMNT_AMNT, double AI_FRX_GN_LSS, byte AI_APPLY_RBT, Integer AI_AD_CMPNY)
			throws CreateException {
		try {

			LocalArAppliedInvoice entity = new LocalArAppliedInvoice();

			Debug.print("ArAppliedInvoiceBean create");
			entity.setAiCode(AI_CODE);
			entity.setAiApplyAmount(AI_APPLY_AMNT);
			entity.setAiPenaltyApplyAmount(AI_PNTLY_APPLY_AMNT);
			entity.setAiCreditableWTax(AI_CRDTBL_W_TX);
			entity.setAiDiscountAmount(AI_DSCNT_AMNT);
			entity.setAiRebate(AI_RBT);
			entity.setAiAppliedDeposit(AI_APPLD_DPST);
			entity.setAiAllocatedPaymentAmount(AI_ALLCTD_PYMNT_AMNT);
			entity.setAiForexGainLoss(AI_FRX_GN_LSS);
			entity.setAiApplyRebate(AI_APPLY_RBT);
			entity.setAiAdCompany(AI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArAppliedInvoice create(double AI_APPLY_AMNT, double AI_PNTLY_APPLY_AMNT,
                                        double AI_CRDTBL_W_TX, double AI_DSCNT_AMNT, double AI_RBT, double AI_APPLD_DPST,
                                        double AI_ALLCTD_PYMNT_AMNT, double AI_FRX_GN_LSS, byte AI_APPLY_RBT, Integer AI_AD_CMPNY)
			throws CreateException {
		try {

			LocalArAppliedInvoice entity = new LocalArAppliedInvoice();

			Debug.print("ArAppliedInvoiceBean create");
			entity.setAiApplyAmount(AI_APPLY_AMNT);
			entity.setAiPenaltyApplyAmount(AI_PNTLY_APPLY_AMNT);
			entity.setAiCreditableWTax(AI_CRDTBL_W_TX);
			entity.setAiDiscountAmount(AI_DSCNT_AMNT);
			entity.setAiRebate(AI_RBT);
			entity.setAiAppliedDeposit(AI_APPLD_DPST);
			entity.setAiAllocatedPaymentAmount(AI_ALLCTD_PYMNT_AMNT);
			entity.setAiForexGainLoss(AI_FRX_GN_LSS);
			entity.setAiApplyRebate(AI_APPLY_RBT);
			entity.setAiAdCompany(AI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}