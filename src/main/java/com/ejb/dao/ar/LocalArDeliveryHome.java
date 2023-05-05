package com.ejb.dao.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArDelivery;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalArDeliveryHome {

	public static final String JNDI_NAME = "LocalArDeliveryHome!com.ejb.ar.LocalArDeliveryHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArDeliveryHome() {
	}

	// FINDER METHODS

	public LocalArDelivery findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalArDelivery entity = (LocalArDelivery) em.find(new LocalArDelivery(),
					pk);
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

	public LocalArDelivery findByDvContainerAndSoDocumentNumber(java.lang.String DV_CNTNR,
			java.lang.String SO_DCMNT_NMBR) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(dv) FROM ArDelivery dv  WHERE dv.dvContainer = ?1 AND dv.arSalesOrder.soDocumentNumber = ?2");
			query.setParameter(1, DV_CNTNR);
			query.setParameter(2, SO_DCMNT_NMBR);
            return (LocalArDelivery) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArDeliveryHome.findByDvContainerAndSoDocumentNumber(java.lang.String DV_CNTNR, java.lang.String SO_DCMNT_NMBR)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDeliveryHome.findByDvContainerAndSoDocumentNumber(java.lang.String DV_CNTNR, java.lang.String SO_DCMNT_NMBR)");
			throw ex;
		}
	}

	public LocalArDelivery findByDvDeliveryNumber(java.lang.String DV_DLVRY_NMBR) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(dv) FROM ArDelivery dv  WHERE dv.dvDeliveryNumber = ?1");
			query.setParameter(1, DV_DLVRY_NMBR);
            return (LocalArDelivery) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print(
					"EXCEPTION: NoResultException com.ejb.ar.LocalArDeliveryHome.findByDvDeliveryNumber(java.lang.String DV_DLVRY_NMBR)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDeliveryHome.findByDvDeliveryNumber(java.lang.String DV_DLVRY_NMBR)");
			throw ex;
		}
	}

	public java.util.Collection findBySoCode(java.lang.Integer SO_CODE) throws FinderException {

		try {
			Query query = em.createQuery("SELECT OBJECT(dv) FROM ArDelivery dv  WHERE dv.arSalesOrder.soCode = ?1");
			query.setParameter(1, SO_CODE);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDeliveryHome.findBySoCode(java.lang.Integer SO_CODE)");
			throw ex;
		}
	}

	public java.util.Collection findBySoReferenceNumber(java.lang.String SO_RFRNC_NMBR) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(dv) FROM ArDelivery dv  WHERE dv.arSalesOrder.soReferenceNumber = ?1");
			query.setParameter(1, SO_RFRNC_NMBR);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDeliveryHome.findBySoReferenceNumber(java.lang.String SO_RFRNC_NMBR)");
			throw ex;
		}
	}

	public java.util.Collection findBySoDocumentNumber(java.lang.String SO_DCMNT_NMBR) throws FinderException {

		try {
			Query query = em
					.createQuery("SELECT OBJECT(dv) FROM ArDelivery dv  WHERE dv.arSalesOrder.soDocumentNumber = ?1");
			query.setParameter(1, SO_DCMNT_NMBR);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ar.LocalArDeliveryHome.findBySoDocumentNumber(java.lang.String SO_DCMNT_NMBR)");
			throw ex;
		}
	}

	public java.util.Collection getDvByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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

	public LocalArDelivery create(Integer DV_CODE, String DV_DLVRY_NMBR, String DV_CNTNR, String DV_BKNG_TM,
                                  String DV_TBS_FEE_PLL_OUT, String DV_RLSD_DT, String DV_DLVRD_DT, String DV_DLVRD_TO, String DV_PLT_NO,
                                  String DV_DRVR_NM, String DV_EMPTY_RTRN_DT, String DV_EMPTY_RTRN_TO, String DV_TBS_FEE_RTRN, String DV_STTS,
                                  String DV_RMRKS) throws CreateException {
		try {

			LocalArDelivery entity = new LocalArDelivery();

			Debug.print("ArDeliveryBean create");
			entity.setDvCode(DV_CODE);
			entity.setDvDeliveryNumber(DV_DLVRY_NMBR);
			entity.setDvContainer(DV_CNTNR);
			entity.setDvBookingTime(DV_BKNG_TM);
			entity.setDvTabsFeePullOut(DV_TBS_FEE_PLL_OUT);
			entity.setDvReleasedDate(DV_RLSD_DT);
			entity.setDvDeliveredDate(DV_DLVRD_DT);
			entity.setDvDeliveredTo(DV_DLVRD_TO);
			entity.setDvPlateNo(DV_PLT_NO);
			entity.setDvDriverName(DV_DRVR_NM);
			entity.setDvEmptyReturnDate(DV_EMPTY_RTRN_DT);
			entity.setDvEmptyReturnTo(DV_EMPTY_RTRN_TO);
			entity.setDvTabsFeeReturn(DV_TBS_FEE_RTRN);
			entity.setDvStatus(DV_STTS);
			entity.setDvRemarks(DV_RMRKS);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArDelivery create(String DV_CNTNR, String DV_DLVRY_NMBR, String DV_BKNG_TM,
                                  String DV_TBS_FEE_PLL_OUT, String DV_RLSD_DT, String DV_DLVRD_DT, String DV_DLVRD_TO, String DV_PLT_NO,
                                  String DV_DRVR_NM, String DV_EMPTY_RTRN_DT, String DV_EMPTY_RTRN_TO, String DV_TBS_FEE_RTRN, String DV_STTS,
                                  String DV_RMRKS) throws CreateException {
		try {

			LocalArDelivery entity = new LocalArDelivery();

			Debug.print("ArDeliveryBean create");
			entity.setDvContainer(DV_CNTNR);
			entity.setDvDeliveryNumber(DV_DLVRY_NMBR);
			entity.setDvBookingTime(DV_BKNG_TM);
			entity.setDvTabsFeePullOut(DV_TBS_FEE_PLL_OUT);
			entity.setDvReleasedDate(DV_RLSD_DT);
			entity.setDvDeliveredDate(DV_DLVRD_DT);
			entity.setDvDeliveredTo(DV_DLVRD_TO);
			entity.setDvPlateNo(DV_PLT_NO);
			entity.setDvDriverName(DV_DRVR_NM);
			entity.setDvEmptyReturnDate(DV_EMPTY_RTRN_DT);
			entity.setDvEmptyReturnTo(DV_EMPTY_RTRN_TO);
			entity.setDvTabsFeeReturn(DV_TBS_FEE_RTRN);
			entity.setDvStatus(DV_STTS);
			entity.setDvRemarks(DV_RMRKS);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}