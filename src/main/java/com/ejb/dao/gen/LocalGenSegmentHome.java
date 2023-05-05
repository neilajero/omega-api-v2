package com.ejb.dao.gen;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.gen.LocalGenSegment;
import com.util.Debug;

@Stateless
public class LocalGenSegmentHome {

	public static final String JNDI_NAME = "LocalGenSegmentHome!com.ejb.genfld.LocalGenSegmentHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalGenSegmentHome() {
	}

	// FINDER METHODS

	public LocalGenSegment findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalGenSegment entity = (LocalGenSegment) em
					.find(new LocalGenSegment(), pk);
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

	public LocalGenSegment findByFlCodeAndSegmentType(java.lang.Integer FL_CODE, char SG_SGMNT_TYP,
			java.lang.Integer SG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sg) FROM GenField fl, IN(fl.genSegments) sg WHERE fl.flCode=?1 AND sg.sgSegmentType=?2 AND sg.sgAdCompany = ?3");
			query.setParameter(1, FL_CODE);
			query.setParameter(2, SG_SGMNT_TYP);
			query.setParameter(3, SG_AD_CMPNY);
            return (LocalGenSegment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndSegmentType(java.lang.Integer FL_CODE, char SG_SGMNT_TYP, java.lang.Integer SG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndSegmentType(java.lang.Integer FL_CODE, char SG_SGMNT_TYP, java.lang.Integer SG_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenSegment findByFlCodeAndVsName(java.lang.Integer FL_CODE, java.lang.String VS_NM,
			java.lang.Integer SG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sg) FROM GenSegment sg WHERE sg.genField.flCode=?1 AND sg.genValueSet.vsName=?2 AND sg.sgAdCompany = ?3");
			query.setParameter(1, FL_CODE);
			query.setParameter(2, VS_NM);
			query.setParameter(3, SG_AD_CMPNY);
            return (LocalGenSegment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndVsName(java.lang.Integer FL_CODE, java.lang.String VS_NM, java.lang.Integer SG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndVsName(java.lang.Integer FL_CODE, java.lang.String VS_NM, java.lang.Integer SG_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByFlCode(java.lang.Integer FL_CODE, java.lang.Integer SG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sg) FROM GenField fl, IN(fl.genSegments) sg WHERE fl.flCode=?1 AND sg.sgAdCompany = ?2");
			query.setParameter(1, FL_CODE);
			query.setParameter(2, SG_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenSegmentHome.findByFlCode(java.lang.Integer FL_CODE, java.lang.Integer SG_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenSegment findByVsCode(java.lang.Integer VS_CODE, java.lang.Integer SG_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sg) FROM GenValueSet vs, IN(vs.genSegments) sg WHERE vs.vsCode=?1 AND sg.sgAdCompany = ?2");
			query.setParameter(1, VS_CODE);
			query.setParameter(2, SG_AD_CMPNY);
            return (LocalGenSegment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenSegmentHome.findByVsCode(java.lang.Integer VS_CODE, java.lang.Integer SG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenSegmentHome.findByVsCode(java.lang.Integer VS_CODE, java.lang.Integer SG_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalGenSegment findByFlCodeAndSgSegmentNumber(java.lang.Integer FL_CODE, short SG_SGMNT_NMBR,
			java.lang.Integer SG_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(sg) FROM GenField fl, IN(fl.genSegments) sg WHERE fl.flCode=?1 AND sg.sgSegmentNumber=?2 AND sg.sgAdCompany = ?3");
			query.setParameter(1, FL_CODE);
			query.setParameter(2, SG_SGMNT_NMBR);
			query.setParameter(3, SG_AD_CMPNY);
            return (LocalGenSegment) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndSgSegmentNumber(java.lang.Integer FL_CODE, short SG_SGMNT_NMBR, java.lang.Integer SG_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.genfld.LocalGenSegmentHome.findByFlCodeAndSgSegmentNumber(java.lang.Integer FL_CODE, short SG_SGMNT_NMBR, java.lang.Integer SG_AD_CMPNY)");
			throw ex;
		}
	}

	// OTHER METHODS

	// CREATE METHODS

	public LocalGenSegment create(java.lang.Integer SG_CODE, java.lang.String SG_NM,
                                  java.lang.String SG_DESC, short SG_MAX_SZ, java.lang.String SG_DFLT_VL, char SG_SGMNT_TYP,
                                  short SG_SGMNT_NMBR, Integer SG_AD_CMPNY) throws CreateException {
		try {

			LocalGenSegment entity = new LocalGenSegment();

			Debug.print("GenSegmentBean create");

			entity.setSgCode(SG_CODE);
			entity.setSgName(SG_NM);
			entity.setSgDescription(SG_DESC);
			entity.setSgMaxSize(SG_MAX_SZ);
			entity.setSgDefaultValue(SG_DFLT_VL);
			entity.setSgSegmentType(SG_SGMNT_TYP);
			entity.setSgSegmentNumber(SG_SGMNT_NMBR);
			entity.setSgAdCompany(SG_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalGenSegment create(java.lang.String SG_NM, java.lang.String SG_DESC, short SG_MAX_SZ,
                                  java.lang.String SG_DFLT_VL, char SG_SGMNT_TYP, short SG_SGMNT_NMBR, Integer SG_AD_CMPNY)
			throws CreateException {
		try {

			LocalGenSegment entity = new LocalGenSegment();

			Debug.print("GenSegmentBean create");

			entity.setSgName(SG_NM);
			entity.setSgDescription(SG_DESC);
			entity.setSgMaxSize(SG_MAX_SZ);
			entity.setSgDefaultValue(SG_DFLT_VL);
			entity.setSgSegmentType(SG_SGMNT_TYP);
			entity.setSgSegmentNumber(SG_SGMNT_NMBR);
			entity.setSgAdCompany(SG_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}