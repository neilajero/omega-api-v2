package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.util.Debug;

@Stateless
public class LocalGenValueSetValueHome {

	public static final String JNDI_NAME = "LocalGenValueSetValueHome!com.ejb.genfld.LocalGenValueSetValueHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenValueSetValueHome() {
	}

	// FINDER METHODS

	public LocalGenValueSetValue findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenValueSetValue entity = (LocalGenValueSetValue) em
					.find(new LocalGenValueSetValue(), pk);
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

	public LocalGenValueSetValue findByVsvValue(java.lang.String VSV_VL, java.lang.Integer VSV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(vsv) FROM GenValueSetValue vsv WHERE vsv.vsvValue=?1 AND vsv.vsvAdCompany = ?2");
			query.setParameter(1, VSV_VL);
			query.setParameter(2, VSV_AD_CMPNY);
			return (LocalGenValueSetValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print("EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetValueHome.findByVsvValue(java.lang.String VSV_VL, java.lang.Integer VSV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print("EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsvValue(java.lang.String VSV_VL, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenValueSetValue findByVsCodeAndVsvValue(java.lang.Integer VS_CODE, java.lang.String VSV_VL,
			java.lang.Integer VSV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vs.vsCode=?1 AND vsv.vsvValue=?2 AND vsv.vsvAdCompany = ?3");
			query.setParameter(1, VS_CODE);
			query.setParameter(2, VSV_VL);
			query.setParameter(3, VSV_AD_CMPNY);
            return (LocalGenValueSetValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetValueHome.findByVsCodeAndVsvValue(java.lang.Integer VS_CODE, java.lang.String VSV_VL, java.lang.Integer VSV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsCodeAndVsvValue(java.lang.Integer VS_CODE, java.lang.String VSV_VL, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenValueSetValue findByVsvValueAndVsName(java.lang.String VSV_VL, java.lang.String VS_NM,
			java.lang.Integer VSV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vsv.vsvValue=?1 AND vs.vsName=?2 AND vsv.vsvAdCompany = ?3");
			query.setParameter(1, VSV_VL);
			query.setParameter(2, VS_NM);
			query.setParameter(3, VSV_AD_CMPNY);
            return (LocalGenValueSetValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetValueHome.findByVsvValueAndVsName(java.lang.String VSV_VL, java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsvValueAndVsName(java.lang.String VSV_VL, java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVsName(java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vs.vsName=?1 AND vsv.vsvAdCompany = ?2 ORDER BY vsv.vsvValue ASC");
			query.setParameter(1, VS_NM);
			query.setParameter(2, VSV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsName(java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVsvDescriptionAndVsName(java.lang.String VSV_DESC, java.lang.String VS_NM,
			java.lang.Integer VSV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vsv.vsvDescription LIKE CONCAT('%',CONCAT(?1,'%')) AND vs.vsName=?2 AND vsv.vsvParent=0 AND vsv.vsvAdCompany = ?3 ORDER BY vsv.vsvValue ASC");
			query.setParameter(1, VSV_DESC);
			query.setParameter(2, VS_NM);
			query.setParameter(3, VSV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsvDescriptionAndVsName(java.lang.String VSV_DESC, java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVsvValueRangeAndVsName(java.lang.String VSV_VL_FRM, java.lang.String VSV_VL_TO,
			java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vsv.vsvValue BETWEEN ?1 AND ?2 AND vs.vsName=?3 AND vsv.vsvAdCompany = ?4");
			query.setParameter(1, VSV_VL_FRM);
			query.setParameter(2, VSV_VL_TO);
			query.setParameter(3, VS_NM);
			query.setParameter(4, VSV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsvValueRangeAndVsName(java.lang.String VSV_VL_FRM, java.lang.String VSV_VL_TO, java.lang.String VS_NM, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenValueSetValue findByVsCodeAndVsvDescription(java.lang.Integer VS_CODE, java.lang.String VSV_DESC,
			java.lang.Integer VSV_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenValueSet vs, IN(vs.genValueSetValues) vsv WHERE vs.vsCode=?1 AND vsv.vsvDescription LIKE ?2 AND vsv.vsvAdCompany = ?3");
			query.setParameter(1, VS_CODE);
			query.setParameter(2, VSV_DESC);
			query.setParameter(3, VSV_AD_CMPNY);
            return (LocalGenValueSetValue) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenValueSetValueHome.findByVsCodeAndVsvDescription(java.lang.Integer VS_CODE, java.lang.String VSV_DESC, java.lang.Integer VSV_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByVsCodeAndVsvDescription(java.lang.Integer VS_CODE, java.lang.String VSV_DESC, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByQlAccountType(java.lang.String QL_ACCNT_TYP, java.lang.Integer VSV_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vsv) FROM GenQualifier ql, IN(ql.genValueSetValues) vsv WHERE ql.qlAccountType=?1 AND vsv.vsvParent=0 AND vsv.vsvAdCompany = ?2 ");
			query.setParameter(1, QL_ACCNT_TYP);
			query.setParameter(2, VSV_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenValueSetValueHome.findByQlAccountType(java.lang.String QL_ACCNT_TYP, java.lang.Integer VSV_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getVsvByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	// CREATE METHOD
	public LocalGenValueSetValue create(java.lang.String VSV_VL, java.lang.String VSV_DESC,
                                        byte VSV_PRNT, short VSV_LVL, byte VSV_ENBL, Integer VSV_AD_CMPNY) throws CreateException {
		try {

			LocalGenValueSetValue entity = new LocalGenValueSetValue();

			Debug.print("GenValueSetValueBean create");
			
			entity.setVsvValue(VSV_VL);
			entity.setVsvDescription(VSV_DESC);
			entity.setVsvParent(VSV_PRNT);
			entity.setVsvLevel(VSV_LVL);
			entity.setVsvEnable(VSV_ENBL);
			entity.setVsvAdCompany(VSV_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}