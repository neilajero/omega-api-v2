package com.ejb.dao.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ad.LocalAdPaymentSchedule;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;
import java.lang.*;
import java.util.*;

@Stateless
public class LocalAdPaymentScheduleHome {

	public static final String JNDI_NAME = "LocalAdPaymentScheduleHome!com.ejb.ad.LocalAdPaymentScheduleHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalAdPaymentScheduleHome() { }

	// FINDER METHODS
	public LocalAdPaymentSchedule findByPrimaryKey(Integer pk) throws FinderException {
		try {
			LocalAdPaymentSchedule entity = (LocalAdPaymentSchedule) em.find(new LocalAdPaymentSchedule(), pk);
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

	public Collection findByPytCode(Integer paymentTermCode, Integer companyCode) throws FinderException {
		try {
			Query query = em.createQuery("SELECT OBJECT(ps) FROM AdPaymentTerm pyt, IN(pyt.adPaymentSchedules) ps WHERE pyt.pytCode = ?1 AND ps.psAdCompany = ?2");
			query.setParameter(1, paymentTermCode);
			query.setParameter(2, companyCode);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print("EXCEPTION: Exception LocalAdPaymentScheduleHome.findByPytCode(Integer paymentTermCode, Integer companyCode)");
			throw ex;
		}
	}

	// CREATE METHODS
	public LocalAdPaymentSchedule create(Integer paymentScheduleCode, short lineNumber, double relativeAmount,
			short paymentDueDay, Integer companyCode) throws CreateException {
		try {
			LocalAdPaymentSchedule entity = new LocalAdPaymentSchedule();
			Debug.print("AdPaymentScheduleBean create");
			entity.setPsCode(paymentScheduleCode);
			entity.setPsLineNumber(lineNumber);
			entity.setPsRelativeAmount(relativeAmount);
			entity.setPsDueDay(paymentDueDay);
			entity.setPsAdCompany(companyCode);
			em.persist(entity);
			return entity;
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalAdPaymentSchedule create(short lineNumber, double relativeAmount, short paymentDueDay, Integer companyCode) throws CreateException {
		try {
			LocalAdPaymentSchedule entity = new LocalAdPaymentSchedule();
			Debug.print("AdPaymentScheduleBean create");
			entity.setPsLineNumber(lineNumber);
			entity.setPsRelativeAmount(relativeAmount);
			entity.setPsDueDay(paymentDueDay);
			entity.setPsAdCompany(companyCode);
			em.persist(entity);
			return entity;
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
}