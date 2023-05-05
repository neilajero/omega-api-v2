package com.ejb.dao.inv;

import jakarta.ejb.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvLocation;
import com.util.Debug;

@SuppressWarnings("ALL")
@Stateless
public class LocalInvLocationHome {

	public static final String JNDI_NAME = "LocalInvLocationHome!com.ejb.inv.LocalInvLocationHome";

	@EJB
	public PersistenceBeanClass em;

	private String LOC_NM = null;
	private final String LOC_DESC = null;
	private String LOC_LV_TYP = null;
	private final String LOC_ADDRSS = null;
	private final String LOC_CNTCT_PRSN = null;
	private final String LOC_CNTCT_NMBR = null;
	private final String LOC_EML_ADDRSS = null;
	private final String LOC_BRNCH = null;
	private final String LOC_DPRTMNT = null;
	private final String LOC_PSTN = null;
	private final String LOC_DT_HRD = null;
	private final String LOC_EMPLYMNT_STTS = null;
	private Integer LOC_AD_CMPNY = null;

	public LocalInvLocationHome() {
	}

	// FINDER METHODS

	public LocalInvLocation findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvLocation entity = (LocalInvLocation) em
					.find(new LocalInvLocation(), pk);
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

	public LocalInvLocation findById(java.lang.Integer pk) throws FinderException {

		try {

			LocalInvLocation entity = (LocalInvLocation) em
					.find(new LocalInvLocation(), pk);
			
			if (entity != null) {
				return entity;
			}

			throw new FinderException();
		} catch (FinderException ex) {
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	public java.util.Collection findLocAll(java.lang.Integer LOC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(loc) FROM InvLocation loc WHERE loc.locAdCompany=?1 ORDER BY loc.locName");
			query.setParameter(1, LOC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findLocAll(java.lang.Integer LOC_AD_CMPNY)");
			throw ex;
		}
	}
	
	//TODO: Location Lookup Value Type here is hard-coded. Review if it will be used by other client.
	public java.util.Collection findLocBranchAll(java.lang.Integer LOC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(loc) FROM InvLocation loc WHERE loc.locLvType in ('HO','BRANCH+REGION') loc.locAdCompany=?1 ORDER BY loc.locName");
			query.setParameter(1, LOC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findLocBranchAll(java.lang.Integer LOC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findAll(java.lang.Integer LOC_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(loc) FROM InvLocationModel loc WHERE loc.locAdCompany=?1 ORDER BY loc.locName");
			query.setParameter(1, LOC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findLocAll(java.lang.Integer LOC_AD_CMPNY)");
			throw ex;
		}
	}

	public LocalInvLocation findByLocName(java.lang.String LOC_NM, java.lang.Integer LOC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(loc) FROM InvLocation loc WHERE loc.locName=?1 AND loc.locAdCompany=?2");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, LOC_AD_CMPNY);
            return (LocalInvLocation) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.inv.LocalInvLocationHome.findByLocName(java.lang.String LOC_NM, java.lang.Integer LOC_AD_CMPNY)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findByLocName(java.lang.String LOC_NM, java.lang.Integer LOC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByLocAdLvType(java.lang.String LOC_LV_TYP, java.lang.Integer LOC_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(loc) FROM InvLocation loc WHERE loc.locLvType=?1 AND loc.locAdCompany=?2");
			query.setParameter(1, LOC_LV_TYP);
			query.setParameter(2, LOC_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findByLocAdLvType(java.lang.String LOC_LV_TYP, java.lang.Integer LOC_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findLocByLocNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY,
			char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT DISTINCT OBJECT(loc) FROM InvLocation loc, IN(loc.invItemLocations) il, IN(il.adBranchItemLocations) bil WHERE (bil.bilLocationDownloadStatus = ?3 OR bil.bilLocationDownloadStatus = ?4 OR bil.bilLocationDownloadStatus = ?5) AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2");
			query.setParameter(1, BR_CODE);
			query.setParameter(2, AD_CMPNY);
			query.setParameter(3, NEW);
			query.setParameter(4, UPDATED);
			query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.inv.LocalInvLocationHome.findLocByLocNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED)");
			throw ex;
		}
	}

	public java.util.Collection getLocByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
	public void updateLocation(LocalInvLocation invLocation) {

		Debug.print("InvLocationBean updateLocation");

		em.merge(invLocation);
	}
	
	public void deleteLocation(LocalInvLocation invLocation) {

		Debug.print("InvLocationBean deleteLocation");

		try {
			
			em.remove(invLocation);
			
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LocalInvLocation buildLocation() throws CreateException {

		try {

			LocalInvLocation entity = new LocalInvLocation();

			Debug.print("InvLocationBean buildLocation");

			entity.setLocName(LOC_NM);
			entity.setLocDescription(LOC_DESC);
			entity.setLocLvType(LOC_LV_TYP);
			entity.setLocAddress(LOC_ADDRSS);
			entity.setLocContactPerson(LOC_CNTCT_PRSN);
			entity.setLocContactNumber(LOC_CNTCT_NMBR);
			entity.setLocEmailAddress(LOC_EML_ADDRSS);
			entity.setLocAdCompany(LOC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLocationHome LocName(String LOC_NM) {
		this.LOC_NM = LOC_NM;
		return this;
	}

	public LocalInvLocationHome LocLvType(String LOC_LV_TYP) {
		this.LOC_LV_TYP = LOC_LV_TYP;
		return this;
	}

	public LocalInvLocationHome LocAdCompany(Integer LOC_AD_CMPNY) {
		this.LOC_AD_CMPNY = LOC_AD_CMPNY;
		return this;
	}

	public LocalInvLocation create(java.lang.Integer LOC_CODE, java.lang.String LOC_NM,
                                   java.lang.String LOC_DESC, java.lang.String LOC_LV_TYP, java.lang.String LOC_ADDRSS,
                                   java.lang.String LOC_CNTCT_PRSN, java.lang.String LOC_CNTCT_NMBR, java.lang.String LOC_EML_ADDRSS,
                                   java.lang.String LOC_BRNCH, java.lang.String LOC_DPRTMNT, java.lang.String LOC_PSTN,
                                   java.lang.String LOC_DT_HRD, java.lang.String LOC_EMPLYMNT_STTS, java.lang.Integer LOC_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvLocation entity = new LocalInvLocation();

			Debug.print("InvLocationBean create");

			entity.setLocCode(LOC_CODE);
			entity.setLocName(LOC_NM);
			entity.setLocDescription(LOC_DESC);
			entity.setLocLvType(LOC_LV_TYP);
			entity.setLocAddress(LOC_ADDRSS);
			entity.setLocContactPerson(LOC_CNTCT_PRSN);
			entity.setLocContactNumber(LOC_CNTCT_NMBR);
			entity.setLocEmailAddress(LOC_EML_ADDRSS);
			entity.setLocBranch(LOC_BRNCH);
			entity.setLocDepartment(LOC_DPRTMNT);
			entity.setLocPosition(LOC_PSTN);
			entity.setLocDateHired(LOC_DT_HRD);
			entity.setLocEmploymentStatus(LOC_EMPLYMNT_STTS);
			entity.setLocAdCompany(LOC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLocation create(java.lang.Integer LOC_CODE, java.lang.String LOC_NM,
                                   java.lang.String LOC_DESC, java.lang.String LOC_LV_TYP, java.lang.String LOC_ADDRSS,
                                   java.lang.String LOC_CNTCT_PRSN, java.lang.String LOC_CNTCT_NMBR, java.lang.String LOC_EML_ADDRSS,
                                   java.lang.Integer LOC_AD_CMPNY) throws CreateException {
		try {

			LocalInvLocation entity = new LocalInvLocation();

			Debug.print("InvLocationBean create");
			entity.setLocCode(LOC_CODE);
			entity.setLocName(LOC_NM);
			entity.setLocDescription(LOC_DESC);
			entity.setLocLvType(LOC_LV_TYP);
			entity.setLocAddress(LOC_ADDRSS);
			entity.setLocContactPerson(LOC_CNTCT_PRSN);
			entity.setLocContactNumber(LOC_CNTCT_NMBR);
			entity.setLocEmailAddress(LOC_EML_ADDRSS);
			entity.setLocAdCompany(LOC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLocation create(java.lang.String LOC_NM, java.lang.String LOC_DESC,
                                   java.lang.String LOC_LV_TYP, java.lang.String LOC_ADDRSS, java.lang.String LOC_CNTCT_PRSN,
                                   java.lang.String LOC_CNTCT_NMBR, java.lang.String LOC_EML_ADDRSS, java.lang.Integer LOC_AD_CMPNY)
			throws CreateException {
		try {

			LocalInvLocation entity = new LocalInvLocation();

			Debug.print("InvLocationBean create");

			entity.setLocName(LOC_NM);
			entity.setLocDescription(LOC_DESC);
			entity.setLocLvType(LOC_LV_TYP);
			entity.setLocAddress(LOC_ADDRSS);
			entity.setLocContactPerson(LOC_CNTCT_PRSN);
			entity.setLocContactNumber(LOC_CNTCT_NMBR);
			entity.setLocEmailAddress(LOC_EML_ADDRSS);
			entity.setLocAdCompany(LOC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalInvLocation create(java.lang.String LOC_NM, java.lang.String LOC_DESC,
                                   java.lang.String LOC_LV_TYP, java.lang.String LOC_ADDRSS, java.lang.String LOC_CNTCT_PRSN,
                                   java.lang.String LOC_CNTCT_NMBR, java.lang.String LOC_EML_ADDRSS, java.lang.String LOC_BRNCH,
                                   java.lang.String LOC_DPRTMNT, java.lang.String LOC_PSTN, java.lang.String LOC_DT_HRD,
                                   java.lang.String LOC_EMPLYMNT_STTS, java.lang.Integer LOC_AD_CMPNY) throws CreateException {

		try {

			LocalInvLocation entity = new LocalInvLocation();

			Debug.print("InvLocationBean create");

			entity.setLocName(LOC_NM);
			entity.setLocDescription(LOC_DESC);
			entity.setLocLvType(LOC_LV_TYP);
			entity.setLocAddress(LOC_ADDRSS);
			entity.setLocContactPerson(LOC_CNTCT_PRSN);
			entity.setLocContactNumber(LOC_CNTCT_NMBR);
			entity.setLocEmailAddress(LOC_EML_ADDRSS);
			entity.setLocBranch(LOC_BRNCH);
			entity.setLocDepartment(LOC_DPRTMNT);
			entity.setLocPosition(LOC_PSTN);
			entity.setLocDateHired(LOC_DT_HRD);
			entity.setLocEmploymentStatus(LOC_EMPLYMNT_STTS);
			entity.setLocAdCompany(LOC_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}