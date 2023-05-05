package com.ejb.dao.ar;

import java.util.Date;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ar.LocalArInvoiceBatch;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import com.util.Debug;
import java.util.*;
import java.lang.*;

@Stateless
public class LocalArInvoiceBatchHome {

	public static final String JNDI_NAME = "LocalArInvoiceBatchHome!com.ejb.ar.LocalArInvoiceBatchHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalArInvoiceBatchHome() { }

	// FINDER METHODS
	public LocalArInvoiceBatch findByPrimaryKey(Integer pk) throws FinderException {
		Debug.print("LocalArInvoiceBatchHome findByPrimaryKey");
		try {
			LocalArInvoiceBatch entity = (LocalArInvoiceBatch) em.find(new LocalArInvoiceBatch(), pk);
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

	public LocalArInvoiceBatch findByIbName(String invoiceBatchName, Integer branchCode, Integer companyCode) throws FinderException {
		Debug.print("LocalArInvoiceBatchHome findByIbName");
		try {
			Query query = em.createQuery("SELECT OBJECT(ib) FROM ArInvoiceBatch ib WHERE ib.ibName=?1 AND ib.ibAdBranch = ?2 AND ib.ibAdCompany=?3");
			query.setParameter(1, invoiceBatchName);
			query.setParameter(2, branchCode);
			query.setParameter(3, companyCode);
            return (LocalArInvoiceBatch) query.getSingleResult();
		} catch (NoResultException ex) {
			Debug.print("EXCEPTION: NoResultException LocalArInvoiceBatchHome.findByIbName(String invoiceBatchName, Integer branchCode, Integer companyCode)");
			throw new FinderException(ex.getMessage());
		} catch (Exception ex) {
			Debug.print("EXCEPTION: Exception LocalArInvoiceBatchHome.findByIbName(String invoiceBatchName, Integer branchCode, Integer companyCode)");
			throw ex;
		}
	}

	public java.util.Collection findOpenIbByIbType(String invoiceBatchType, Integer branchCode, Integer companyCode) throws FinderException {
		Debug.print("LocalArInvoiceBatchHome findOpenIbByIbType");
		try {
			Query query = em.createQuery("SELECT OBJECT(ib) FROM ArInvoiceBatch ib WHERE ib.ibType=?1 AND ib.ibStatus='OPEN' AND ib.ibAdBranch=?2 AND ib.ibAdCompany=?3");
			query.setParameter(1, invoiceBatchType);
			query.setParameter(2, branchCode);
			query.setParameter(3, companyCode);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print("EXCEPTION: Exception LocalArInvoiceBatchHome.findOpenIbByIbType(String invoiceBatchType, Integer branchCode, Integer companyCode)");
			throw ex;
		}
	}

	public java.util.Collection findOpenIbAll(Integer branchCode, Integer companyCode) throws FinderException {
		Debug.print("LocalArInvoiceBatchHome findOpenIbAll");
		try {
			Query query = em.createQuery("SELECT OBJECT(ib) FROM ArInvoiceBatch ib WHERE ib.ibStatus='OPEN' AND ib.ibAdBranch=?1 AND ib.ibAdCompany=?2");
			query.setParameter(1, branchCode);
			query.setParameter(2, companyCode);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print("EXCEPTION: Exception LocalArInvoiceBatchHome.findOpenIbAll(Integer branchCode, Integer companyCode)");
			throw ex;
		}
	}

	public Collection getIbByCriteria(String jbossQl, Object[] args, Integer LIMIT, Integer OFFSET) throws FinderException {
		Debug.print("LocalArInvoiceBatchHome getIbByCriteria");
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

	// CREATE METHODS
	public LocalArInvoiceBatch create(Integer invoiceBatchCode, String invoiceBatchName, String invoiceBatchDesc, String invoiceBatchStatus,
			String invoiceBatchType, Date dateCreated, String createdBy, Integer branchCode, Integer companyCode) throws CreateException {
		Debug.print("LocalArInvoiceBatchHome create - new");
		try {
			LocalArInvoiceBatch entity = new LocalArInvoiceBatch();
			entity.setIbCode(invoiceBatchCode);
			entity.setIbName(invoiceBatchName);
			entity.setIbDescription(invoiceBatchDesc);
			entity.setIbStatus(invoiceBatchStatus);
			entity.setIbType(invoiceBatchType);
			entity.setIbDateCreated(dateCreated);
			entity.setIbCreatedBy(createdBy);
			entity.setIbAdBranch(branchCode);
			entity.setIbAdCompany(companyCode);
			em.persist(entity);
			return entity;
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalArInvoiceBatch create(String invoiceBatchName, String invoiceBatchDesc, String invoiceBatchStatus, String invoiceBatchType,
			Date dateCreated, String createdBy, Integer branchCode, Integer companyCode) throws CreateException {
		Debug.print("LocalArInvoiceBatchHome create - update");
		try {
			LocalArInvoiceBatch entity = new LocalArInvoiceBatch();
			entity.setIbName(invoiceBatchName);
			entity.setIbDescription(invoiceBatchDesc);
			entity.setIbStatus(invoiceBatchStatus);
			entity.setIbType(invoiceBatchType);
			entity.setIbDateCreated(dateCreated);
			entity.setIbCreatedBy(createdBy);
			entity.setIbAdBranch(branchCode);
			entity.setIbAdCompany(companyCode);
			em.persist(entity);
			return entity;
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
}