package com.ejb.dao.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.ap.LocalApVoucherLineItem;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

import com.util.Debug;

@Stateless
public class LocalApVoucherLineItemHome {

	public static final String JNDI_NAME = "LocalApVoucherLineItemHome!com.ejb.ap.LocalApVoucherLineItemHome";

	@EJB
	public PersistenceBeanClass em;

	public LocalApVoucherLineItemHome() {
	}

	// FINDER METHODS

	public LocalApVoucherLineItem findByPrimaryKey(java.lang.Integer pk) throws FinderException {

		try {

			LocalApVoucherLineItem entity = (LocalApVoucherLineItem) em
					.find(new LocalApVoucherLineItem(), pk);
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

	public java.util.Collection findUnpostedVouByIiNameAndLocNameAndLessEqualDateAndVouAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date VOU_DT, java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouPosted = 0 AND vli.apVoucher.vouVoid = 0 AND vli.invItemLocation.invItem.iiName = ?1 AND vli.invItemLocation.invLocation.locName = ?2 AND vli.apVoucher.vouDate <= ?3 AND vli.apVoucher.vouAdBranch=?4 AND vli.vliAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, VOU_DT);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findUnpostedVouByIiNameAndLocNameAndLessEqualDateAndVouAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date VOU_DT, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByIiNameAndLocNameAndLessEqualDateAndChkAdBranch(java.lang.String II_NM,
			java.lang.String LOC_NM, java.util.Date CHK_DT, java.lang.Integer CHK_AD_BRNCH,
			java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apCheck.chkPosted = 0 AND vli.apCheck.chkVoid = 0 AND vli.invItemLocation.invItem.iiName = ?1 AND vli.invItemLocation.invLocation.locName = ?2 AND vli.apCheck.chkDate <= ?3 AND vli.apCheck.chkAdBranch = ?4 AND vli.vliAdCompany = ?5");
			query.setParameter(1, II_NM);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, CHK_DT);
			query.setParameter(4, CHK_AD_BRNCH);
			query.setParameter(5, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findUnpostedChkByIiNameAndLocNameAndLessEqualDateAndChkAdBranch(java.lang.String II_NM, java.lang.String LOC_NM, java.com.util.Date CHK_DT, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVliPmProject(java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouPosted=0 AND vli.apVoucher.vouDebitMemo=0 AND vli.pmProject IS NOT NULL AND vli.vliAdCompany=?1");
			query.setParameter(1, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findUnpostedVliPmProject(java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedVouByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouPosted = 0 AND vli.apVoucher.vouVoid = 0 AND vli.invItemLocation.invLocation.locName = ?1 AND vli.apVoucher.vouAdBranch = ?2 AND vli.vliAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, VOU_AD_BRNCH);
			query.setParameter(3, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findUnpostedVouByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDebitMemoAndVouPostedAndIlCodeAndBrCode(byte VOU_DBT_MMO, byte VOU_PSTD,
			java.lang.Integer INV_IL_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)
			throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouDebitMemo=?1 AND vli.apVoucher.vouPosted=?2 AND vli.invItemLocation.ilCode=?3 AND vli.apVoucher.vouAdBranch=?4 AND vli.vliAdCompany=?5");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_PSTD);
			query.setParameter(3, INV_IL_CODE);
			query.setParameter(4, VOU_AD_BRNCH);
			query.setParameter(5, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findByVouDebitMemoAndVouPostedAndIlCodeAndBrCode(byte VOU_DBT_MMO, byte VOU_PSTD, java.lang.Integer INV_IL_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findVouByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM,
			java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouVoid = 0 AND vli.invItemLocation.invItem.iiCode=?1 AND vli.invItemLocation.invLocation.locName = ?2 AND vli.apVoucher.vouAdBranch = ?3 AND vli.vliAdCompany = ?4");
			query.setParameter(1, II_CODE);
			query.setParameter(2, LOC_NM);
			query.setParameter(3, VOU_AD_BRNCH);
			query.setParameter(4, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findVouByIiCodeAndLocNameAndAdBranch(java.lang.Integer II_CODE, java.lang.String LOC_NM, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findUnpostedChkByLocNameAndAdBranch(java.lang.String LOC_NM,
			java.lang.Integer CHK_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apCheck.chkPosted = 0 AND vli.apCheck.chkVoid = 0 AND vli.invItemLocation.invLocation.locName = ?1 AND vli.apCheck.chkAdBranch = ?2 AND vli.vliAdCompany = ?3");
			query.setParameter(1, LOC_NM);
			query.setParameter(2, CHK_AD_BRNCH);
			query.setParameter(3, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findUnpostedChkByLocNameAndAdBranch(java.lang.String LOC_NM, java.lang.Integer CHK_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection findByVouDebitMemoAndVouPostedVouDmVoucherNumberAndIlCodeAndBrCode(byte VOU_DBT_MMO,
			byte VOU_PSTD, java.lang.String VOU_DCMNT_NMBR, java.lang.Integer INV_IL_CODE, java.lang.Integer VOU_AD_BRNCH,
			java.lang.Integer VLI_AD_CMPNY) throws FinderException {

		try {
			Query query = em.createQuery(
					"SELECT OBJECT(vli) FROM ApVoucherLineItem vli WHERE vli.apVoucher.vouDebitMemo=?1 AND vli.apVoucher.vouPosted=?2 AND vli.apVoucher.vouDmVoucherNumber=?3 AND vli.invItemLocation.ilCode=?4 AND vli.apVoucher.vouAdBranch=?5 AND vli.vliAdCompany=?6");
			query.setParameter(1, VOU_DBT_MMO);
			query.setParameter(2, VOU_PSTD);
			query.setParameter(3, VOU_DCMNT_NMBR);
			query.setParameter(4, INV_IL_CODE);
			query.setParameter(5, VOU_AD_BRNCH);
			query.setParameter(6, VLI_AD_CMPNY);
            return query.getResultList();
		} catch (Exception ex) {
			Debug.print(
					"EXCEPTION: Exception com.ejb.ap.LocalApVoucherLineItemHome.findByVouDebitMemoAndVouPostedVouDmVoucherNumberAndIlCodeAndBrCode(byte VOU_DBT_MMO, byte VOU_PSTD, java.lang.String VOU_DCMNT_NMBR, java.lang.Integer INV_IL_CODE, java.lang.Integer VOU_AD_BRNCH, java.lang.Integer VLI_AD_CMPNY)");
			throw ex;
		}
	}

	public java.util.Collection getVliByCriteria(java.lang.String jbossQl, java.lang.Object[] args)
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

	// OTHER METHODS

	// CREATE METHODS

	public LocalApVoucherLineItem create(java.lang.Integer VLI_CODE, short VLI_LN, double VLI_QTY,
                                         double VLI_UNT_CST, double VLI_AMNT, double VLI_TX_AMNT, double VLI_DSCNT_1, double VLI_DSCNT_2,
                                         double VLI_DSCNT_3, double VLI_DSCNT_4, double TTL_VLI_DSCNT, String VLI_SPL_NM, String VLI_SPL_TIN,
                                         String VLI_SPL_ADDRSS, byte VLI_TX, Integer VLI_AD_CMPNY) throws CreateException {
		try {

			LocalApVoucherLineItem entity = new LocalApVoucherLineItem();

			Debug.print("ApVoucherLineItemBean create");
			entity.setVliCode(VLI_CODE);
			entity.setVliLine(VLI_LN);
			entity.setVliQuantity(VLI_QTY);
			entity.setVliUnitCost(VLI_UNT_CST);
			entity.setVliAmount(VLI_AMNT);
			entity.setVliTaxAmount(VLI_TX_AMNT);
			entity.setVliDiscount1(VLI_DSCNT_1);
			entity.setVliDiscount2(VLI_DSCNT_2);
			entity.setVliDiscount3(VLI_DSCNT_3);
			entity.setVliDiscount4(VLI_DSCNT_4);
			entity.setVliTotalDiscount(TTL_VLI_DSCNT);
			entity.setVliSplName(VLI_SPL_NM);
			entity.setVliSplTin(VLI_SPL_TIN);
			entity.setVliSplAddress(VLI_SPL_ADDRSS);
			entity.setVliTax(VLI_TX);
			entity.setVliAdCompany(VLI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public LocalApVoucherLineItem create(short VLI_LN, double VLI_QTY, double VLI_UNT_CST, double VLI_AMNT,
                                         double VLI_TX_AMNT, double VLI_DSCNT_1, double VLI_DSCNT_2, double VLI_DSCNT_3, double VLI_DSCNT_4,
                                         double TTL_VLI_DSCNT, String VLI_SPL_NM, String VLI_SPL_TIN, String VLI_SPL_ADDRSS, byte VLI_TX,
                                         Integer VLI_AD_CMPNY) throws CreateException {
		try {

			LocalApVoucherLineItem entity = new LocalApVoucherLineItem();

			Debug.print("ApVoucherLineItemBean create");
			entity.setVliLine(VLI_LN);
			entity.setVliQuantity(VLI_QTY);
			entity.setVliUnitCost(VLI_UNT_CST);
			entity.setVliAmount(VLI_AMNT);
			entity.setVliTaxAmount(VLI_TX_AMNT);
			entity.setVliDiscount1(VLI_DSCNT_1);
			entity.setVliDiscount2(VLI_DSCNT_2);
			entity.setVliDiscount3(VLI_DSCNT_3);
			entity.setVliDiscount4(VLI_DSCNT_4);
			entity.setVliTotalDiscount(TTL_VLI_DSCNT);
			entity.setVliSplName(VLI_SPL_NM);
			entity.setVliSplTin(VLI_SPL_TIN);
			entity.setVliSplAddress(VLI_SPL_ADDRSS);
			entity.setVliTax(VLI_TX);
			entity.setVliAdCompany(VLI_AD_CMPNY);

			em.persist(entity);
			return entity;

		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

}