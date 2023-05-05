package com.ejb.dao.gl;

import java.util.Date;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.util.Debug;

@Stateless
public class LocalGlTaxInterfaceHome {

	public static final String JNDI_NAME = "LocalGlTaxInterfaceHome!com.ejb.gl.LocalGlTaxInterfaceHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGlTaxInterfaceHome() {
	}

	// FINDER METHODS

	public LocalGlTaxInterface findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGlTaxInterface entity = (LocalGlTaxInterface) em
					.find(new LocalGlTaxInterface(), pk);
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

	public LocalGlTaxInterface findByTiDocumentTypeAndTxlCode(java.lang.String TI_DCMNT_TYP,
			java.lang.Integer TI_TXL_CODE, java.lang.Integer TI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE ti.tiDocumentType=?1 AND ti.tiTxlCode=?2 AND ti.tiAdCompany = ?3");
			query.setParameter(1, TI_DCMNT_TYP);
			query.setParameter(2, TI_TXL_CODE);
			query.setParameter(3, TI_AD_CMPNY);
            return (LocalGlTaxInterface) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlTaxInterfaceHome.findByTiDocumentTypeAndTxlCode(java.lang.String TI_DCMNT_TYP, java.lang.Integer TI_TXL_CODE, java.lang.Integer TI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTaxInterfaceHome.findByTiDocumentTypeAndTxlCode(java.lang.String TI_DCMNT_TYP, java.lang.Integer TI_TXL_CODE, java.lang.Integer TI_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGlTaxInterface findByTiDocumentTypeAndTxnCode(java.lang.String TI_DCMNT_TYP,
			java.lang.Integer TI_TXN_CODE, java.lang.Integer TI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE ti.tiDocumentType=?1 AND ti.tiTxnCode=?2 AND ti.tiAdCompany = ?3");
			query.setParameter(1, TI_DCMNT_TYP);
			query.setParameter(2, TI_TXN_CODE);
			query.setParameter(3, TI_AD_CMPNY);
            return (LocalGlTaxInterface) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.gl.LocalGlTaxInterfaceHome.findByTiDocumentTypeAndTxnCode(java.lang.String TI_DCMNT_TYP, java.lang.Integer TI_TXN_CODE, java.lang.Integer TI_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTaxInterfaceHome.findByTiDocumentTypeAndTxnCode(java.lang.String TI_DCMNT_TYP, java.lang.Integer TI_TXN_CODE, java.lang.Integer TI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAllByTiDocumentTypeAndTxnCode(java.lang.String TI_DCMNT_TYP,
			java.lang.Integer TI_TXN_CODE, java.lang.Integer TI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE ti.tiDocumentType=?1 AND ti.tiTxnCode=?2 AND ti.tiAdCompany = ?3");
			query.setParameter(1, TI_DCMNT_TYP);
			query.setParameter(2, TI_TXN_CODE);
			query.setParameter(3, TI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTaxInterfaceHome.findAllByTiDocumentTypeAndTxnCode(java.lang.String TI_DCMNT_TYP, java.lang.Integer TI_TXN_CODE, java.lang.Integer TI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByTiSourceAndTax(java.lang.String TI_SRC, java.util.Date TI_TXN_DT_FRM,
			java.util.Date TI_TXN_DT_TO, java.lang.Integer TI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(ti) FROM GlTaxInterface ti WHERE ti.tiTcCode IS NOT NULL AND ti.tiSource=?1 AND ti.tiTxnDate >= ?2 AND ti.tiTxnDate <= ?3 AND ti.tiAdCompany = ?4");
			query.setParameter(1, TI_SRC);
			query.setParameter(2, TI_TXN_DT_FRM);
			query.setParameter(3, TI_TXN_DT_TO);
			query.setParameter(4, TI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.gl.LocalGlTaxInterfaceHome.findByTiSourceAndTax(java.lang.String TI_SRC, java.com.util.Date TI_TXN_DT_FRM, java.com.util.Date TI_TXN_DT_TO, java.lang.Integer TI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getTiByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalGlTaxInterface create(Integer TI_CODE, String TI_DCMNT_TYP, String TI_SRC,
                                      double TI_NET_AMNT, double TI_TX_AMNT, double TI_SLS_AMNT, double TI_SRVCS_AMNT, double TI_CPTL_GDS_AMNT,
                                      double TI_OTHR_CPTL_GDS_AMNT, Integer TI_TXN_CODE, Date TI_TXN_DT, String TI_TXN_DCMNT_NMBR,
                                      String TI_TXN_RFRNC_NMBR, double TI_TX_EXMPT, double TI_TX_ZR_RTD, Integer TI_TXL_CODE,
                                      Integer TI_TXL_COA_CODE, Integer TI_TC_CODE, Integer TI_WTC_CODE, Integer TI_SL_CODE, String TI_SL_TIN,
                                      String TI_SL_SBLDGR_CODE, String TI_SL_NM, String TI_SL_ADDRSS, String TI_SL_ADDRSS2, byte TI_IS_AR_DCMNT,
                                      Integer TI_AD_BRNCH, Integer TI_AD_CMPNY) throws CreateException {
		try {

			LocalGlTaxInterface entity = new LocalGlTaxInterface();

			Debug.print("GlTaxInterfaceBean create");

			entity.setTiCode(TI_CODE);
			entity.setTiDocumentType(TI_DCMNT_TYP);
			entity.setTiSource(TI_SRC);
			entity.setTiNetAmount(TI_NET_AMNT);
			entity.setTiTaxAmount(TI_TX_AMNT);
			entity.setTiSalesAmount(TI_SLS_AMNT);
			entity.setTiServicesAmount(TI_SRVCS_AMNT);
			entity.setTiCapitalGoodsAmount(TI_CPTL_GDS_AMNT);
			entity.setTiOtherCapitalGoodsAmount(TI_OTHR_CPTL_GDS_AMNT);
			entity.setTiTxnCode(TI_TXN_CODE);
			entity.setTiTxnDate(TI_TXN_DT);
			entity.setTiTxnDocumentNumber(TI_TXN_DCMNT_NMBR);
			entity.setTiTxnReferenceNumber(TI_TXN_RFRNC_NMBR);
			entity.setTiTaxExempt(TI_TX_EXMPT);
			entity.setTiTaxZeroRated(TI_TX_ZR_RTD);
			entity.setTiTxlCode(TI_TXL_CODE);
			entity.setTiTxlCoaCode(TI_TXL_COA_CODE);
			entity.setTiTcCode(TI_TC_CODE);
			entity.setTiWtcCode(TI_WTC_CODE);
			entity.setTiSlCode(TI_SL_CODE);
			entity.setTiSlTin(TI_SL_TIN);
			entity.setTiSlSubledgerCode(TI_SL_SBLDGR_CODE);
			entity.setTiSlName(TI_SL_NM);
			entity.setTiSlAddress(TI_SL_ADDRSS);
			entity.setTiSlAddress2(TI_SL_ADDRSS2);
			entity.setTiIsArDocument(TI_IS_AR_DCMNT);
			entity.setTiAdBranch(TI_AD_BRNCH);
			entity.setTiAdCompany(TI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGlTaxInterface create(String TI_DCMNT_TYP, String TI_SRC, double TI_NET_AMNT,
                                      double TI_TX_AMNT, double TI_SLS_AMNT, double TI_SRVCS_AMNT, double TI_CPTL_GDS_AMNT,
                                      double TI_OTHR_CPTL_GDS_AMNT, Integer TI_TXN_CODE, Date TI_TXN_DT, String TI_TXN_DCMNT_NMBR,
                                      String TI_TXN_RFRNC_NMBR, double TI_TX_EXMPT, double TI_TX_ZR_RTD, Integer TI_TXL_CODE,
                                      Integer TI_TXL_COA_CODE, Integer TI_TC_CODE, Integer TI_WTC_CODE, Integer TI_SL_CODE, String TI_SL_TIN,
                                      String TI_SL_SBLDGR_CODE, String TI_SL_NM, String TI_SL_ADDRSS, String TI_SL_ADDRSS2, byte TI_IS_AR_DCMNT,
                                      Integer TI_AD_BRNCH, Integer TI_AD_CMPNY) throws CreateException {
		try {

			LocalGlTaxInterface entity = new LocalGlTaxInterface();

			Debug.print("GlTaxInterfaceBean create");

			entity.setTiDocumentType(TI_DCMNT_TYP);
			entity.setTiSource(TI_SRC);
			entity.setTiNetAmount(TI_NET_AMNT);
			entity.setTiTaxAmount(TI_TX_AMNT);
			entity.setTiSalesAmount(TI_SLS_AMNT);
			entity.setTiServicesAmount(TI_SRVCS_AMNT);
			entity.setTiCapitalGoodsAmount(TI_CPTL_GDS_AMNT);
			entity.setTiOtherCapitalGoodsAmount(TI_OTHR_CPTL_GDS_AMNT);
			entity.setTiTxnCode(TI_TXN_CODE);
			entity.setTiTxnDate(TI_TXN_DT);
			entity.setTiTxnDocumentNumber(TI_TXN_DCMNT_NMBR);
			entity.setTiTxnReferenceNumber(TI_TXN_RFRNC_NMBR);
			entity.setTiTaxExempt(TI_TX_EXMPT);
			entity.setTiTaxZeroRated(TI_TX_ZR_RTD);
			entity.setTiTxlCode(TI_TXL_CODE);
			entity.setTiTxlCoaCode(TI_TXL_COA_CODE);
			entity.setTiTcCode(TI_TC_CODE);
			entity.setTiWtcCode(TI_WTC_CODE);
			entity.setTiSlCode(TI_SL_CODE);
			entity.setTiSlTin(TI_SL_TIN);
			entity.setTiSlSubledgerCode(TI_SL_SBLDGR_CODE);
			entity.setTiSlName(TI_SL_NM);
			entity.setTiSlAddress(TI_SL_ADDRSS);
			entity.setTiSlAddress2(TI_SL_ADDRSS2);
			entity.setTiIsArDocument(TI_IS_AR_DCMNT);
			entity.setTiAdBranch(TI_AD_BRNCH);
			entity.setTiAdCompany(TI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}