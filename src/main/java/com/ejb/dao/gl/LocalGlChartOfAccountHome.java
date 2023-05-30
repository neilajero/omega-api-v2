package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.util.Debug;
import com.util.EJBCommon;

@SuppressWarnings("ALL")
@Stateless
public class LocalGlChartOfAccountHome implements ILocalGlChartOfAccountHome {

	public static final String JNDI_NAME = "LocalGlChartOfAccountHome!com.ejb.gl.LocalGlChartOfAccountHome";

	@EJB
	public PersistenceBeanClass em;

	private String COA_ACCNT_NMBR = null;
	private String COA_ACCNT_DESC = null;
	private String COA_ACCNT_TYP = null;
	private final String COA_TX_TYP = null;
	private final String COA_CIT_CTGRY = null;
	private final String COA_SAW_CTGRY = null;
	private final String COA_IIT_CTGRY = null;
	private Date COA_DT_FRM = null;
	private Date COA_DT_TO = null;
	private String COA_SGMNT1 = null;
	private String COA_SGMNT2 = null;
	private String COA_SGMNT3 = null;
	private final String COA_SGMNT4 = null;
	private final String COA_SGMNT5 = null;
	private final String COA_SGMNT6 = null;
	private final String COA_SGMNT7 = null;
	private final String COA_SGMNT8 = null;
	private final String COA_SGMNT9 = null;
	private final String COA_SGMNT10 = null;
	private final byte COA_ENBL = EJBCommon.TRUE;
	private final byte COA_FR_RVLTN = EJBCommon.TRUE;
	private Integer COA_AD_CMPNY = null;

	public LocalGlChartOfAccountHome() {
	}

	// FINDER METHODS

	public LocalGlChartOfAccount findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlChartOfAccount entity = (LocalGlChartOfAccount) em
					.find(new LocalGlChartOfAccount(), pk);
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

	public LocalGlChartOfAccount findByPrimaryKey(java.lang.Integer pk, String companyShortName) throws FinderException {
		try {
			LocalGlChartOfAccount entity = (LocalGlChartOfAccount) em
					.findPerCompany(new LocalGlChartOfAccount(), pk, companyShortName);
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

	public LocalGlChartOfAccount findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaAccountNumber=?1 AND coa.coaAdCompany = ?2");
			query.setParameter(1, COA_ACCNT_NMBR);
			query.setParameter(2, COA_AD_CMPNY);
            return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountHome.findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaAccountNumber(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaAccountNumber=?1 AND coa.coaAdCompany = ?2",
					companyShortName);
			query.setParameter(1, COA_ACCNT_NMBR);
			query.setParameter(2, COA_AD_CMPNY);
			return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaAccountNumberAndBranchCode(java.lang.String COA_ACCNT_NMBR,
			java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa, In(coa.adBranchCoas) bcoa WHERE coa.coaAccountNumber=?1 AND bcoa.adBranch.brCode=?2 AND coa.coaAdCompany = ?3");
			query.setParameter(1, COA_ACCNT_NMBR);
			query.setParameter(2, COA_AD_BRNCH);
			query.setParameter(3, COA_AD_CMPNY);
            return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountHome.findByCoaAccountNumberAndBranchCode(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findByCoaAccountNumberAndBranchCode(java.lang.String COA_ACCNT_NMBR, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCoaAllEnabled(java.util.Date CURR_DATE, java.lang.Integer COA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaEnable=1 AND ((coa.coaDateFrom <= ?1 AND coa.coaDateTo >= ?1) OR (coa.coaDateFrom <= ?1 AND coa.coaDateTo IS NULL)) AND coa.coaAdCompany = ?2");
			query.setParameter(1, CURR_DATE);
			query.setParameter(2, COA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findCoaAllEnabled(java.com.util.Date CURR_DATE, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findCoaAll(java.lang.Integer COA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaAdCompany = ?1 ORDER BY coa.coaAccountNumber");
			query.setParameter(1, COA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findCoaAll(java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findHoCoaAllByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY) throws FinderException {
		try {
			
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaAccountDescription LIKE '%"+ COA_CTRGY +"%' and coa.coaAccountDescription like '%"+ COA_DEFAULT_BRANCH +"%' and coa.coaAdCompany =:companyCode ORDER BY coa.coaAccountNumber");
			query.setParameter("companyCode", COA_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findHoCoaAllByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findHoCoaAllByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException {
		try {

			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa "
							+ "WHERE coa.coaAccountDescription LIKE '%"+ COA_CTRGY +"%' "
							+ "and coa.coaAccountDescription like '%"+ COA_DEFAULT_BRANCH +"%' "
							+ "and coa.coaAdCompany =:companyCode ORDER BY coa.coaAccountNumber",
					companyShortName);
			query.setParameter("companyCode", COA_AD_CMPNY);
			return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public LocalGlChartOfAccount findHoCoaByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY) throws FinderException {
		try {
			Query query = em.createQuery(
				"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaAccountDescription LIKE '%"+ COA_CTRGY +"%' and coa.coaAccountDescription like '%"+ COA_DEFAULT_BRANCH +"%' and coa.coaAdCompany =:companyCode ORDER BY coa.coaAccountNumber");
			query.setParameter("companyCode", COA_AD_CMPNY);

            return (LocalGlChartOfAccount) query.getSingleResult();
			
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountHome.findHoCoaByCoaCategory(String COA_CTRGY, java.lang.Integer COA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findHoCoaByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccount findHoCoaByCoaCategory(String COA_CTRGY, String COA_DEFAULT_BRANCH, java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException {
		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa "
							+ "WHERE coa.coaAccountDescription LIKE '%"+ COA_CTRGY +"%' "
							+ "and coa.coaAccountDescription like '%"+ COA_DEFAULT_BRANCH +"%' "
							+ "and coa.coaAdCompany =:companyCode ORDER BY coa.coaAccountNumber",
					companyShortName);
			query.setParameter("companyCode", COA_AD_CMPNY);

			return (LocalGlChartOfAccount) query.getSingleResult();

		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH,
			java.lang.Integer COA_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa, In(coa.adBranchCoas) bcoa WHERE coa.coaCode=?1 AND bcoa.adBranch.brCode=?2 AND coa.coaAdCompany = ?3");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, COA_AD_BRNCH);
			query.setParameter(3, COA_AD_CMPNY);
            return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountHome.findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaCodeAndBranchCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_BRNCH,
															java.lang.Integer COA_AD_CMPNY, String companyShortName) throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa, In(coa.adBranchCoas) bcoa "
							+ "WHERE coa.coaCode=?1 AND bcoa.adBranch.brCode=?2 AND coa.coaAdCompany = ?3",
					companyShortName);
			query.setParameter(1, COA_CODE);
			query.setParameter(2, COA_AD_BRNCH);
			query.setParameter(3, COA_AD_CMPNY);
			return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaCode=?1 AND coa.coaAdCompany = ?2");
			query.setParameter(1, COA_CODE);
			query.setParameter(2, COA_AD_CMPNY);
            return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlChartOfAccountHome.findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlChartOfAccountHome.findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlChartOfAccount findByCoaCode(java.lang.Integer COA_CODE, java.lang.Integer COA_AD_CMPNY, String companyShortName)
			throws FinderException {

		try {
			Query query = em.createQueryPerCompany(
					"SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaCode=?1 AND coa.coaAdCompany = ?2", companyShortName.toLowerCase());
			query.setParameter(1, COA_CODE);
			query.setParameter(2, COA_AD_CMPNY);
			return (LocalGlChartOfAccount) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection getCoaByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
			java.lang.Integer LIMIT, java.lang.Integer OFFSET) throws FinderException {

		try {
			Query query = em.createQuery(jbossQl);
			int cnt = 1;
			for (Object data : args) {
				query.setParameter(cnt, data);
				cnt++;
			}
			if (LIMIT > 0) {
				query.setMaxResults(LIMIT);
			}
            query.setFirstResult(OFFSET);
            return query.getResultList();
		} catch (Exception ex) {
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGlChartOfAccount buildCoa() throws CreateException {

		try {

			LocalGlChartOfAccount entity = new LocalGlChartOfAccount();

			Debug.print("GlChartOfAccountBean create");

			entity.setCoaAccountNumber(COA_ACCNT_NMBR);
			entity.setCoaAccountDescription(COA_ACCNT_DESC);
			entity.setCoaAccountType(COA_ACCNT_TYP);
			entity.setCoaTaxType(COA_TX_TYP);
			entity.setCoaCitCategory(COA_CIT_CTGRY);
			entity.setCoaSawCategory(COA_SAW_CTGRY);
			entity.setCoaIitCategory(COA_IIT_CTGRY);
			entity.setCoaDateFrom(COA_DT_FRM);
			entity.setCoaDateTo(COA_DT_TO);
			entity.setCoaSegment1(COA_SGMNT1);
			entity.setCoaSegment2(COA_SGMNT2);
			entity.setCoaSegment3(COA_SGMNT3);
			entity.setCoaSegment4(COA_SGMNT4);
			entity.setCoaSegment5(COA_SGMNT5);
			entity.setCoaSegment6(COA_SGMNT6);
			entity.setCoaSegment7(COA_SGMNT7);
			entity.setCoaSegment8(COA_SGMNT8);
			entity.setCoaSegment9(COA_SGMNT9);
			entity.setCoaSegment10(COA_SGMNT10);
			entity.setCoaEnable(COA_ENBL);
			entity.setCoaForRevaluation(COA_FR_RVLTN);
			entity.setCoaAdCompany(COA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public ILocalGlChartOfAccountHome coaAccountNumber(String COA_ACCNT_NMBR) {
		this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
		return this;
	}

	public ILocalGlChartOfAccountHome coaAccountDesc(String COA_ACCNT_DESC) {
		this.COA_ACCNT_DESC = COA_ACCNT_DESC;
		return this;
	}

	public LocalGlChartOfAccountHome coaAccountType(String COA_ACCNT_TYP) {
		this.COA_ACCNT_TYP = COA_ACCNT_TYP;
		return this;
	}

	public ILocalGlChartOfAccountHome coaAdCompany(Integer COA_AD_CMPNY) {
		this.COA_AD_CMPNY = COA_AD_CMPNY;
		return this;
	}

	public ILocalGlChartOfAccountHome coaDateFrom(Date COA_DT_FRM) {
		this.COA_DT_FRM = COA_DT_FRM;
		return this;
	}

	public ILocalGlChartOfAccountHome coaDateTo(Date COA_DT_TO) {
		this.COA_DT_TO = COA_DT_TO;
		return this;
	}

	public LocalGlChartOfAccountHome coaSegment1(String COA_SGMNT1) {
		this.COA_SGMNT1 = COA_SGMNT1;
		return this;
	}

	public LocalGlChartOfAccountHome coaSegment2(String COA_SGMNT2) {
		this.COA_SGMNT2 = COA_SGMNT2;
		return this;
	}

	public LocalGlChartOfAccountHome coaSegment3(String COA_SGMNT3) {
		this.COA_SGMNT3 = COA_SGMNT3;
		return this;
	}

	public LocalGlChartOfAccount create(java.lang.Integer COA_CODE, java.lang.String COA_ACCNT_NMBR,
                                        java.lang.String COA_ACCNT_DESC, java.lang.String COA_ACCNT_TYP, java.lang.String COA_TX_TYP,
                                        String COA_CIT_CTGRY, String COA_SAW_CTGRY, String COA_IIT_CTGRY, Date COA_DT_FRM, Date COA_DT_TO,
                                        String COA_SGMNT1, String COA_SGMNT2, String COA_SGMNT3, String COA_SGMNT4, String COA_SGMNT5,
                                        String COA_SGMNT6, String COA_SGMNT7, String COA_SGMNT8, String COA_SGMNT9, String COA_SGMNT10,
                                        byte COA_ENBL, byte COA_FR_RVLTN, Integer COA_AD_CMPNY) throws CreateException {
		try {

			LocalGlChartOfAccount entity = new LocalGlChartOfAccount();

			Debug.print("GlChartOfAccountBean create");
			entity.setCoaCode(COA_CODE);
			entity.setCoaAccountNumber(COA_ACCNT_NMBR);
			entity.setCoaAccountDescription(COA_ACCNT_DESC);
			entity.setCoaAccountType(COA_ACCNT_TYP);
			entity.setCoaTaxType(COA_TX_TYP);
			entity.setCoaCitCategory(COA_CIT_CTGRY);
			entity.setCoaSawCategory(COA_SAW_CTGRY);
			entity.setCoaIitCategory(COA_IIT_CTGRY);
			entity.setCoaDateFrom(COA_DT_FRM);
			entity.setCoaDateTo(COA_DT_TO);
			entity.setCoaSegment1(COA_SGMNT1);
			entity.setCoaSegment2(COA_SGMNT2);
			entity.setCoaSegment3(COA_SGMNT3);
			entity.setCoaSegment4(COA_SGMNT4);
			entity.setCoaSegment5(COA_SGMNT5);
			entity.setCoaSegment6(COA_SGMNT6);
			entity.setCoaSegment7(COA_SGMNT7);
			entity.setCoaSegment8(COA_SGMNT8);
			entity.setCoaSegment9(COA_SGMNT9);
			entity.setCoaSegment10(COA_SGMNT10);
			entity.setCoaEnable(COA_ENBL);
			entity.setCoaForRevaluation(COA_FR_RVLTN);
			entity.setCoaAdCompany(COA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlChartOfAccount create(java.lang.String COA_ACCNT_NMBR, java.lang.String COA_ACCNT_DESC,
                                        java.lang.String COA_ACCNT_TYP, java.lang.String COA_TX_TYP, String COA_CIT_CTGRY, String COA_SAW_CTGRY,
                                        String COA_IIT_CTGRY, Date COA_DT_FRM, Date COA_DT_TO, String COA_SGMNT1, String COA_SGMNT2,
                                        String COA_SGMNT3, String COA_SGMNT4, String COA_SGMNT5, String COA_SGMNT6, String COA_SGMNT7,
                                        String COA_SGMNT8, String COA_SGMNT9, String COA_SGMNT10, byte COA_ENBL, byte COA_FR_RVLTN,
                                        Integer COA_AD_CMPNY) throws CreateException {
		try {

			LocalGlChartOfAccount entity = new LocalGlChartOfAccount();

			Debug.print("GlChartOfAccountBean create");

			entity.setCoaAccountNumber(COA_ACCNT_NMBR);
			entity.setCoaAccountDescription(COA_ACCNT_DESC);
			entity.setCoaAccountType(COA_ACCNT_TYP);
			entity.setCoaTaxType(COA_TX_TYP);
			entity.setCoaCitCategory(COA_CIT_CTGRY);
			entity.setCoaSawCategory(COA_SAW_CTGRY);
			entity.setCoaIitCategory(COA_IIT_CTGRY);
			entity.setCoaDateFrom(COA_DT_FRM);
			entity.setCoaDateTo(COA_DT_TO);
			entity.setCoaSegment1(COA_SGMNT1);
			entity.setCoaSegment2(COA_SGMNT2);
			entity.setCoaSegment3(COA_SGMNT3);
			entity.setCoaSegment4(COA_SGMNT4);
			entity.setCoaSegment5(COA_SGMNT5);
			entity.setCoaSegment6(COA_SGMNT6);
			entity.setCoaSegment7(COA_SGMNT7);
			entity.setCoaSegment8(COA_SGMNT8);
			entity.setCoaSegment9(COA_SGMNT9);
			entity.setCoaSegment10(COA_SGMNT10);
			entity.setCoaEnable(COA_ENBL);
			entity.setCoaForRevaluation(COA_FR_RVLTN);
			entity.setCoaAdCompany(COA_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
}