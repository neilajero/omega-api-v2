package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvItem;
import com.util.Debug;
import com.util.EJBCommon;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.Date;

@Stateless
public class LocalInvItemHome {

    public static final String JNDI_NAME = "LocalInvItemHome!com.ejb.inv.LocalInvItemHome";

    @EJB
    public PersistenceBeanClass em;

    private Integer II_CODE = null;
    private String II_NM = null;
    private String II_DESC = null;
    private String II_PRT_NMBR = null;
    private String II_SHRT_NM = null;
    private String II_BR_CD_1 = null;
    private String II_BR_CD_2 = null;
    private String II_BR_CD_3 = null;
    private String II_BRND = null;
    private String II_CLSS = null;
    private String II_AD_LV_CTGRY = null;
    private String II_CST_MTHD = null;
    private double II_UNT_CST = 0d;
    private double II_SLS_PRC = 0d;
    private byte II_ENBL = EJBCommon.FALSE;
    private byte II_VS_ITM = EJBCommon.FALSE;
    private byte II_ENBLE_AT_BLD = EJBCommon.FALSE;
    private String II_DNNSS = null;
    private String II_SDNGS = null;
    private String II_RMRKS = null;
    private byte II_SRVC_CHRG = EJBCommon.FALSE;
    private byte II_DN_IN_CHRG = EJBCommon.FALSE;
    private byte II_SRVCS = EJBCommon.FALSE;
    private byte II_JB_SRVCS = EJBCommon.FALSE;
    private byte II_IS_VAT_RLF = EJBCommon.FALSE;
    private byte II_IS_TX = EJBCommon.FALSE;
    private byte II_IS_PRJCT = EJBCommon.FALSE;
    private double II_PRCNT_MRKP = 0d;
    private double II_SHPPNG_CST = 0d;
    private double II_SPCFC_GRVTY = 0d;
    private double II_STNDRD_FLL_SZ = 0d;
    private double II_YLD = 0d;
    private double II_LBR_CST = 0d;
    private double II_PWR_CST = 0d;
    private double II_OHD_CST = 0d;
    private double II_FRGHT_CST = 0d;
    private double II_FXD_CST = 0d;
    private String II_GL_LBR_CST_ACCNT = null;
    private String II_GL_PWR_CST_ACCNT = null;
    private String II_GL_OHD_CST_ACCNT = null;
    private String II_GL_FRGHT_CST_ACCNT = null;
    private String II_GL_FXD_CST_ACCNT = null;
    private double II_STD_LST_PRCNTG = 0d;
    private double II_MRKP_VAL = 0d;
    private String II_MRKT = null;
    private byte II_ENBL_PO = EJBCommon.FALSE;
    private short II_PO_CYCL = 0;
    private String II_UMC_PCKGNG = null;
    private Integer II_RETAIL_UOM = null;
    private byte II_OPN_PRDCT = EJBCommon.FALSE;
    private byte II_FXD_ASST = EJBCommon.FALSE;
    private Date II_DT_ACQRD = null;
    private Integer II_DFLT_LCTN = null;
    private byte II_TRC_MSC = EJBCommon.FALSE;
    private byte II_SC_SNDY = EJBCommon.FALSE;
    private byte II_SC_MNDY = EJBCommon.FALSE;
    private byte II_SC_TSDY = EJBCommon.FALSE;
    private byte II_SC_WDNSDY = EJBCommon.FALSE;
    private byte II_SC_THRSDY = EJBCommon.FALSE;
    private byte II_SC_FRDY = EJBCommon.FALSE;
    private byte II_SC_STRDY = EJBCommon.FALSE;
    private double II_ACQSTN_CST = 0d;
    private double II_LF_SPN = 0d;
    private double II_RSDL_VL = 0d;
    private String II_GL_COA_FXD_ASST_ACCNT = null;
    private String II_GL_COA_DPRCTN_ACCNT = null;
    private String II_GL_ACCMLTD_DPRCTN_ACCNT = null;
    private String II_CNDTN = null;
    private String II_TX_CD = null;
    private Integer II_AD_CMPNY = null;
    private String II_CRTD_BY = null;
    private Date II_DT_CRTD = null;
    private String II_LST_MDFD_BY = null;
    private Date II_DT_LST_MDFD = null;
    private byte II_INTRST_EXCPTN = EJBCommon.FALSE;
    private byte II_PYMNT_TRM_EXCPTN = EJBCommon.FALSE;

    // FINDER METHODS
    public LocalInvItem findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvItem entity = (LocalInvItem) em.find(new LocalInvItem(), pk);
            if (entity == null) {
                throw new FinderException();
            }
            return entity;
        }
        catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvItem findById(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvItem entity = (LocalInvItem) em.find(new LocalInvItem(), pk);

            if (entity != null && entity.getIiEnable() == EJBCommon.TRUE) {
                return entity;
            }

            throw new FinderException();

        }
        catch (FinderException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvItem findByIiName(java.lang.String II_NM, java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiName=?1 AND ii.iiAdCompany=?2");
            query.setParameter(1, II_NM);
            query.setParameter(2, II_AD_CMPNY);
            return (LocalInvItem) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvItem findByIiCode(java.lang.Integer II_CD, java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiCode=?1 AND ii.iiAdCompany=?2");
            query.setParameter(1, II_CD);
            query.setParameter(2, II_AD_CMPNY);
            return (LocalInvItem) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvItem findByPartNumber(java.lang.String II_PRT_NMBR, java.lang.Integer II_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiPartNumber=?1 AND ii.iiAdCompany=?2");
            query.setParameter(1, II_PRT_NMBR);
            query.setParameter(2, II_AD_CMPNY);
            return (LocalInvItem) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvItem findByIiNameAndIiAdLvCategory(java.lang.String II_NM, java.lang.String II_AD_LV_CTGRY,
                                                      java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiName=?1 AND ii.iiAdLvCategory=?2 AND ii.iiAdCompany=?3");
            query.setParameter(1, II_NM);
            query.setParameter(2, II_AD_LV_CTGRY);
            query.setParameter(3, II_AD_CMPNY);
            return (LocalInvItem) query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FinderException(ex.getMessage());
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findAssemblyIiAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiClass = 'Assembly' AND ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findStockIiAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiClass = 'Stock' AND ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findFixedAssetIiAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiFixedAsset = 1 AND ii.iiEnable = 1 AND ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findFixedAssetIiByDateAcquired(java.util.Date II_DT_ACQRD_TO,
                                                               java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiFixedAsset = 1 AND ii.iiEnable = 1 AND ii.iiDateAcquired <= ?1 AND ii.iiAdCompany=?2");
            query.setParameter(1, II_DT_ACQRD_TO);
            query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findEnabledIiAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiEnable = 1 AND ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ii) FROM InvItemModel ii WHERE ii.iiEnable = 1 AND ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findEnabledIiByLocName(java.lang.String LOC_NM, java.lang.Integer II_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii, IN(ii.invItemLocations) il WHERE il.invLocation.locName = ?1 AND ii.iiEnable = 1 AND ii.iiAdCompany=?2 ORDER BY ii.iiName");
            query.setParameter(1, LOC_NM);
            query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findEnabledIiByIiAdLvCategory(java.lang.String II_AD_LV_CTGRY,
                                                              java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiAdLvCategory = ?1 AND ii.iiEnable = 1 AND ii.iiAdCompany = ?2");
            query.setParameter(1, II_AD_LV_CTGRY);
            query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findEnabledIiByIiAdLvCategoryAndIiClass(java.lang.String II_AD_LV_CTGRY,
                                                                        java.lang.String II_CLSS, java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiAdLvCategory = ?1 AND ii.iiEnable = 1 AND ii.iiClass=?2 AND ii.iiAdCompany = ?3 ORDER BY ii.iiName");
            query.setParameter(1, II_AD_LV_CTGRY);
            query.setParameter(2, II_CLSS);
            query.setParameter(3, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findIiAll(java.lang.Integer II_AD_CMPNY) throws FinderException {

        try {
            Query query = em.createQuery("SELECT OBJECT(ii) FROM InvItem ii WHERE ii.iiAdCompany=?1");
            query.setParameter(1, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findIiByUmcAdLvClass(java.lang.String UOM_AD_LV_CLSS, java.lang.Integer II_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT DISTINCT OBJECT(ii) FROM InvItem ii, IN(ii.invUnitOfMeasureConversions) umc WHERE umc.invUnitOfMeasure.uomAdLvClass = ?1 AND ii.iiAdCompany = ?2");
            query.setParameter(1, UOM_AD_LV_CLSS);
            query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findByIiDoneness(java.lang.String II_DNNSS, java.lang.Integer II_AD_CMPNY)
            throws FinderException {

        try {
            Query query = em
                    .createQuery("SELECT OBJECT(ii) FROM InvItem ii  WHERE ii.iiDoneness = ?1 AND ii.iiAdCompany = ?2");
            query.setParameter(1, II_DNNSS);
            query.setParameter(2, II_AD_CMPNY);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection findIiByIiNewAndUpdated(java.lang.Integer BR_CODE, java.lang.String II_IL_LOCATION,
                                                        java.lang.Integer AD_CMPNY, char NEW, char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT DISTINCT OBJECT(ii) FROM InvItem ii, IN(ii.invItemLocations) il, IN(il.adBranchItemLocations) bil WHERE (bil.bilItemDownloadStatus = ?4 OR bil.bilItemDownloadStatus = ?5 OR bil.bilItemDownloadStatus = ?6) AND ii.iiRetailUom IS NOT NULL AND bil.adBranch.brCode = ?1 AND bil.invItemLocation.invLocation.locLvType = ?2 AND bil.bilAdCompany = ?3");
            query.setParameter(1, BR_CODE);
            query.setParameter(2, II_IL_LOCATION);
            query.setParameter(3, AD_CMPNY);
            query.setParameter(4, NEW);
            query.setParameter(5, UPDATED);
            query.setParameter(6, DOWNLOADED_UPDATED);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    // OTHER METHODS

    public java.util.Collection findIiByIiNewAndUpdated(java.lang.Integer BR_CODE, java.lang.Integer AD_CMPNY, char NEW,
                                                        char UPDATED, char DOWNLOADED_UPDATED) throws FinderException {

        try {
            Query query = em.createQuery(
                    "SELECT DISTINCT OBJECT(ii) FROM InvItem ii, IN(ii.invItemLocations) il, IN(il.adBranchItemLocations) bil WHERE (bil.bilItemDownloadStatus = ?3 OR bil.bilItemDownloadStatus = ?4 OR bil.bilItemDownloadStatus = ?5) AND ii.iiRetailUom IS NOT NULL AND bil.adBranch.brCode = ?1 AND bil.bilAdCompany = ?2");
            query.setParameter(1, BR_CODE);
            query.setParameter(2, AD_CMPNY);
            query.setParameter(3, NEW);
            query.setParameter(4, UPDATED);
            query.setParameter(5, DOWNLOADED_UPDATED);
            return query.getResultList();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public java.util.Collection getIiByCriteria(java.lang.String jbossQl, java.lang.Object[] args,
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
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    // CREATE/UPDATE METHODS
    public void updateItem(LocalInvItem invItem) {

        Debug.print("InvItemBean updateItem");

        em.merge(invItem);
    }

    public LocalInvItem buildItem() throws CreateException {

        try {

            LocalInvItem entity = new LocalInvItem();

            Debug.print("InvItemBean buildItem");

            //entity.setIiCode(II_CODE);
            entity.setIiName(II_NM);
            entity.setIiDescription(II_DESC);
            entity.setIiPartNumber(II_PRT_NMBR);
            entity.setIiShortName(II_SHRT_NM);
            entity.setIiBarCode1(II_BR_CD_1);
            entity.setIiBarCode2(II_BR_CD_2);
            entity.setIiBarCode3(II_BR_CD_3);
            entity.setIiBrand(II_BRND);
            entity.setIiClass(II_CLSS);
            entity.setIiAdLvCategory(II_AD_LV_CTGRY);
            entity.setIiCostMethod(II_CST_MTHD);
            entity.setIiUnitCost(II_UNT_CST);
            entity.setIiSalesPrice(II_SLS_PRC);
            entity.setIiEnable(II_ENBL);
            entity.setIiVirtualStore(II_VS_ITM);
            entity.setIiEnableAutoBuild(II_ENBLE_AT_BLD);
            entity.setIiDoneness(II_DNNSS);
            entity.setIiSidings(II_SDNGS);
            entity.setIiRemarks(II_RMRKS);
            entity.setIiServiceCharge(II_SRVC_CHRG);
            entity.setIiNonInventoriable(II_DN_IN_CHRG);
            entity.setIiServices(II_SRVCS);
            entity.setIiJobServices(II_JB_SRVCS);
            entity.setIiIsVatRelief(II_IS_VAT_RLF);
            entity.setIiIsTax(II_IS_TX);
            entity.setIiIsProject(II_IS_PRJCT);
            entity.setIiPercentMarkup(II_PRCNT_MRKP);
            entity.setIiShippingCost(II_SHPPNG_CST);
            entity.setIiStandardFillSize(II_STNDRD_FLL_SZ);
            entity.setIiSpecificGravity(II_SPCFC_GRVTY);
            entity.setIiYield(II_YLD);
            entity.setIiLaborCost(II_LBR_CST);
            entity.setIiPowerCost(II_PWR_CST);
            entity.setIiOverHeadCost(II_OHD_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFixedCost(II_FXD_CST);
            entity.setGlCoaLaborCostAccount(II_GL_LBR_CST_ACCNT);
            entity.setGlCoaPowerCostAccount(II_GL_PWR_CST_ACCNT);
            entity.setGlCoaOverHeadCostAccount(II_GL_OHD_CST_ACCNT);
            entity.setGlCoaFreightCostAccount(II_GL_FRGHT_CST_ACCNT);
            entity.setGlCoaFixedCostAccount(II_GL_FXD_CST_ACCNT);
            entity.setIiLossPercentage(II_STD_LST_PRCNTG);
            entity.setIiMarkupValue(II_MRKP_VAL);
            entity.setIiMarket(II_MRKT);
            entity.setIiEnablePo(II_ENBL_PO);
            entity.setIiPoCycle(II_PO_CYCL);
            entity.setIiUmcPackaging(II_UMC_PCKGNG);
            entity.setIiRetailUom(II_RETAIL_UOM);
            entity.setIiOpenProduct(II_OPN_PRDCT);
            entity.setIiFixedAsset(II_FXD_ASST);
            entity.setIiDateAcquired(II_DT_ACQRD);
            entity.setIiDefaultLocation(II_DFLT_LCTN);

            entity.setIiTraceMisc(II_TRC_MSC);
            entity.setIiScSunday(II_SC_SNDY);
            entity.setIiScMonday(II_SC_MNDY);
            entity.setIiScTuesday(II_SC_TSDY);
            entity.setIiScWednesday(II_SC_WDNSDY);
            entity.setIiScThursday(II_SC_THRSDY);
            entity.setIiScFriday(II_SC_FRDY);
            entity.setIiScSaturday(II_SC_STRDY);

            entity.setIiAcquisitionCost(II_ACQSTN_CST);
            entity.setIiLifeSpan(II_LF_SPN);
            entity.setIiResidualValue(II_RSDL_VL);

            entity.setGlCoaFixedAssetAccount(II_GL_COA_FXD_ASST_ACCNT);
            entity.setGlCoaDepreciationAccount(II_GL_COA_DPRCTN_ACCNT);
            entity.setGlCoaAccumulatedDepreciationAccount(II_GL_ACCMLTD_DPRCTN_ACCNT);

            entity.setIiCondition(II_CNDTN);
            entity.setIiTaxCode(II_TX_CD);
            entity.setIiAdCompany(II_AD_CMPNY);

            //-- Audit Logs
            entity.setIiCreatedBy(II_CRTD_BY);
            entity.setIiDateCreated(II_DT_CRTD);
            entity.setIiLastModifiedBy(II_LST_MDFD_BY);
            entity.setIiDateLastModified(II_DT_LST_MDFD);

            entity.setIiInterestException(II_INTRST_EXCPTN);
            entity.setIiPaymentTermException(II_PYMNT_TRM_EXCPTN);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvItemHome IiName(String II_NM) {

        this.II_NM = II_NM;
        return this;
    }

    public LocalInvItemHome IiDescription(String II_DESC) {

        this.II_DESC = II_DESC;
        return this;
    }

    public LocalInvItemHome IiClass(String II_CLSS) {

        this.II_CLSS = II_CLSS;
        return this;
    }

    public LocalInvItemHome IiPartNumber(String II_PRT_NMBR) {

        this.II_PRT_NMBR = II_PRT_NMBR;
        return this;
    }

    public LocalInvItemHome IiShortName(String II_SHRT_NM) {

        this.II_SHRT_NM = II_SHRT_NM;
        return this;
    }

    public LocalInvItemHome IiBarCode1(String II_BR_CD_1) {

        this.II_BR_CD_1 = II_BR_CD_1;
        return this;
    }

    public LocalInvItemHome IiBarCode2(String II_BR_CD_2) {

        this.II_BR_CD_2 = II_BR_CD_2;
        return this;
    }

    public LocalInvItemHome IiBarCode3(String II_BR_CD_3) {

        this.II_BR_CD_3 = II_BR_CD_3;
        return this;
    }

    public LocalInvItemHome IiBrand(String II_BRND) {

        this.II_BRND = II_BRND;
        return this;
    }

    public LocalInvItemHome IiAdLvCategory(String II_AD_LV_CTGRY) {

        this.II_AD_LV_CTGRY = II_AD_LV_CTGRY;
        return this;
    }

    public LocalInvItemHome IiCostMethod(String II_CST_MTHD) {

        this.II_CST_MTHD = II_CST_MTHD;
        return this;
    }

    public LocalInvItemHome IiUnitCost(double II_UNT_CST) {

        this.II_UNT_CST = II_UNT_CST;
        return this;
    }

    public LocalInvItemHome IiSalesPrice(double II_SLS_PRC) {

        this.II_SLS_PRC = II_SLS_PRC;
        return this;
    }

    public LocalInvItemHome IiEnable(byte II_ENBL) {

        this.II_ENBL = II_ENBL;
        return this;
    }

    public LocalInvItemHome IiVirtualStore(byte II_VS_ITM) {

        this.II_VS_ITM = II_VS_ITM;
        return this;
    }

    public LocalInvItemHome IiEnableAutoBuild(byte II_ENBLE_AT_BLD) {

        this.II_ENBLE_AT_BLD = II_ENBLE_AT_BLD;
        return this;
    }

    public LocalInvItemHome IiDoneness(String II_DNNSS) {

        this.II_DNNSS = II_DNNSS;
        return this;
    }

    public LocalInvItemHome IiSidings(String II_SDNGS) {

        this.II_SDNGS = II_SDNGS;
        return this;
    }

    public LocalInvItemHome IiRemarks(String II_RMRKS) {

        this.II_RMRKS = II_RMRKS;
        return this;
    }

    public LocalInvItemHome IiServiceCharge(byte II_SRVC_CHRG) {

        this.II_SRVC_CHRG = II_SRVC_CHRG;
        return this;
    }

    public LocalInvItemHome IiNonInventoriable(byte II_DN_IN_CHRG) {

        this.II_DN_IN_CHRG = II_DN_IN_CHRG;
        return this;
    }

    public LocalInvItemHome IiServices(byte II_SRVCS) {

        this.II_SRVCS = II_SRVCS;
        return this;
    }

    public LocalInvItemHome IiJobServices(byte II_JB_SRVCS) {

        this.II_JB_SRVCS = II_JB_SRVCS;
        return this;
    }

    public LocalInvItemHome IiIsVatRelief(byte II_IS_VAT_RLF) {

        this.II_IS_VAT_RLF = II_IS_VAT_RLF;
        return this;
    }

    public LocalInvItemHome IiIsTax(byte II_IS_TX) {

        this.II_IS_TX = II_IS_TX;
        return this;
    }

    public LocalInvItemHome IiIsProject(byte II_IS_PRJCT) {

        this.II_IS_PRJCT = II_IS_PRJCT;
        return this;
    }

    public LocalInvItemHome IiPercentMarkup(double II_PRCNT_MRKP) {

        this.II_PRCNT_MRKP = II_PRCNT_MRKP;
        return this;
    }

    public LocalInvItemHome IiShippingCost(double II_SHPPNG_CST) {

        this.II_SHPPNG_CST = II_SHPPNG_CST;
        return this;
    }

    public LocalInvItemHome IiStandardFillSize(double II_STNDRD_FLL_SZ) {

        this.II_STNDRD_FLL_SZ = II_STNDRD_FLL_SZ;
        return this;
    }

    public LocalInvItemHome IiSpecificGravity(double II_SPCFC_GRVTY) {

        this.II_SPCFC_GRVTY = II_SPCFC_GRVTY;
        return this;
    }

    public LocalInvItemHome IiYeild(double II_YLD) {

        this.II_YLD = II_YLD;
        return this;
    }

    public LocalInvItemHome IiLaborCost(double II_LBR_CST) {

        this.II_YLD = II_YLD;
        return this;
    }

    public LocalInvItemHome IiPowerCost(double II_PWR_CST) {

        this.II_PWR_CST = II_PWR_CST;
        return this;
    }

    public LocalInvItemHome IiOverHeadCost(double II_OHD_CST) {

        this.II_OHD_CST = II_OHD_CST;
        return this;
    }

    public LocalInvItemHome IiFreightCost(double II_FRGHT_CST) {

        this.II_FRGHT_CST = II_FRGHT_CST;
        return this;
    }

    public LocalInvItemHome IiFixedCost(double II_FXD_CST) {

        this.II_FXD_CST = II_FXD_CST;
        return this;
    }

    public LocalInvItemHome GlCoaLaborCostAccount(String II_GL_LBR_CST_ACCNT) {

        this.II_GL_LBR_CST_ACCNT = II_GL_LBR_CST_ACCNT;
        return this;
    }

    public LocalInvItemHome GlCoaPowerCostAccount(String II_GL_PWR_CST_ACCNT) {

        this.II_GL_PWR_CST_ACCNT = II_GL_PWR_CST_ACCNT;
        return this;
    }

    public LocalInvItemHome GlCoaOverHeadCostAccount(String II_GL_OHD_CST_ACCNT) {

        this.II_GL_OHD_CST_ACCNT = II_GL_OHD_CST_ACCNT;
        return this;
    }

    public LocalInvItemHome GlCoaFreightCostAccount(String II_GL_FRGHT_CST_ACCNT) {

        this.II_GL_FRGHT_CST_ACCNT = II_GL_FRGHT_CST_ACCNT;
        return this;
    }

    public LocalInvItemHome GlCoaFixedCostAccount(String II_GL_FXD_CST_ACCNT) {

        this.II_GL_FXD_CST_ACCNT = II_GL_FXD_CST_ACCNT;
        return this;
    }

    public LocalInvItemHome IiLossPercentage(double II_STD_LST_PRCNTG) {

        this.II_STD_LST_PRCNTG = II_STD_LST_PRCNTG;
        return this;
    }

    public LocalInvItemHome IiMarkupValue(double II_MRKP_VAL) {

        this.II_MRKP_VAL = II_MRKP_VAL;
        return this;
    }

    public LocalInvItemHome IiMarket(String II_MRKT) {

        this.II_MRKT = II_MRKT;
        return this;
    }

    public LocalInvItemHome IiEnablePo(byte II_ENBL_PO) {

        this.II_ENBL_PO = II_ENBL_PO;
        return this;
    }

    public LocalInvItemHome IiPoCycle(short II_PO_CYCL) {

        this.II_PO_CYCL = II_PO_CYCL;
        return this;
    }

    public LocalInvItemHome IiUmcPackaging(String II_UMC_PCKGNG) {

        this.II_UMC_PCKGNG = II_UMC_PCKGNG;
        return this;
    }

    public LocalInvItemHome IiRetailUom(Integer II_RETAIL_UOM) {

        this.II_RETAIL_UOM = II_RETAIL_UOM;
        return this;
    }

    public LocalInvItemHome IiOpenProduct(byte II_OPN_PRDCT) {

        this.II_OPN_PRDCT = II_OPN_PRDCT;
        return this;
    }

    public LocalInvItemHome IiFixedAsset(byte II_FXD_ASST) {

        this.II_FXD_ASST = II_FXD_ASST;
        return this;
    }

    public LocalInvItemHome IiDateAcquired(Date II_DT_ACQRD) {

        this.II_DT_ACQRD = II_DT_ACQRD;
        return this;
    }

    public LocalInvItemHome IiDefaultLocation(Integer II_DFLT_LCTN) {

        this.II_DFLT_LCTN = II_DFLT_LCTN;
        return this;
    }

    public LocalInvItemHome IiTraceMisc(byte II_TRC_MSC) {

        this.II_TRC_MSC = II_TRC_MSC;
        return this;
    }

    public LocalInvItemHome IiScSunday(byte II_SC_SNDY) {

        this.II_SC_SNDY = II_SC_SNDY;
        return this;
    }

    public LocalInvItemHome IiScMonday(byte II_SC_MNDY) {

        this.II_SC_MNDY = II_SC_MNDY;
        return this;
    }

    public LocalInvItemHome IiScTuesday(byte II_SC_TSDY) {

        this.II_SC_TSDY = II_SC_TSDY;
        return this;
    }

    public LocalInvItemHome IiScWednesday(byte II_SC_WDNSDY) {

        this.II_SC_WDNSDY = II_SC_WDNSDY;
        return this;
    }

    public LocalInvItemHome IiScThursday(byte II_SC_THRSDY) {

        this.II_SC_THRSDY = II_SC_THRSDY;
        return this;
    }

    public LocalInvItemHome IiScFriday(byte II_SC_FRDY) {

        this.II_SC_FRDY = II_SC_FRDY;
        return this;
    }

    public LocalInvItemHome IiScSaturday(byte II_SC_STRDY) {

        this.II_SC_STRDY = II_SC_STRDY;
        return this;
    }

    public LocalInvItemHome IiAcquisitionCost(double II_ACQSTN_CST) {

        this.II_ACQSTN_CST = II_ACQSTN_CST;
        return this;
    }

    public LocalInvItemHome IiLifeSpan(double II_LF_SPN) {

        this.II_LF_SPN = II_LF_SPN;
        return this;
    }

    public LocalInvItemHome IiResidualValue(double II_RSDL_VL) {

        this.II_RSDL_VL = II_RSDL_VL;
        return this;
    }

    public LocalInvItemHome IiCoaFixedAccount(String II_GL_COA_FXD_ASST_ACCNT) {

        this.II_GL_COA_FXD_ASST_ACCNT = II_GL_COA_FXD_ASST_ACCNT;
        return this;
    }

    public LocalInvItemHome IiCoaDepreciationAccount(String II_GL_COA_DPRCTN_ACCNT) {

        this.II_GL_COA_DPRCTN_ACCNT = II_GL_COA_DPRCTN_ACCNT;
        return this;
    }

    public LocalInvItemHome IiCoaAccumDepreciationAccount(String II_GL_ACCMLTD_DPRCTN_ACCNT) {

        this.II_GL_ACCMLTD_DPRCTN_ACCNT = II_GL_ACCMLTD_DPRCTN_ACCNT;
        return this;
    }

    public LocalInvItemHome IiCondition(String II_CNDTN) {

        this.II_CNDTN = II_CNDTN;
        return this;
    }

    public LocalInvItemHome IiTaxCode(String II_TX_CD) {

        this.II_TX_CD = II_TX_CD;
        return this;
    }

    public LocalInvItemHome IiCreatedBy(String II_CRTD_BY) {

        this.II_CRTD_BY = II_CRTD_BY;
        return this;
    }

    public LocalInvItemHome IiDateCreated(Date II_DT_CRTD) {

        this.II_DT_CRTD = II_DT_CRTD;
        return this;
    }

    public LocalInvItemHome IiLastModifiedBy(String II_LST_MDFD_BY) {

        this.II_LST_MDFD_BY = II_LST_MDFD_BY;
        return this;
    }

    public LocalInvItemHome IiDateLastModified(Date II_DT_LST_MDFD) {

        this.II_DT_LST_MDFD = II_DT_LST_MDFD;
        return this;
    }

    public LocalInvItemHome IiInterestException(byte II_INTRST_EXCPTN) {

        this.II_INTRST_EXCPTN = II_INTRST_EXCPTN;
        return this;
    }

    public LocalInvItemHome IiPaymentTermException(byte II_PYMNT_TRM_EXCPTN) {

        this.II_PYMNT_TRM_EXCPTN = II_PYMNT_TRM_EXCPTN;
        return this;
    }

    public LocalInvItemHome IiAdCompany(Integer II_AD_CMPNY) {

        this.II_AD_CMPNY = II_AD_CMPNY;
        return this;
    }

    public LocalInvItem create(Integer II_CODE, String II_NM, String II_DESC, String II_PRT_NMBR,
                               String II_SHRT_NM, String II_BR_CD_1, String II_BR_CD_2, String II_BR_CD_3, String II_BRND, String II_CLSS,
                               String II_AD_LV_CTGRY, String II_CST_MTHD, double II_UNT_CST, double II_SLS_PRC, byte II_ENBL,
                               byte II_VS_ITM, String II_DNNSS, String II_SDNGS, String II_RMRKS, byte II_SRVC_CHRG,
                               byte II_DN_IN_CHRG, byte II_SRVCS, byte II_JB_SRVCS, byte II_IS_VAT_RLF, byte II_IS_TX, byte II_IS_PRJCT,
                               double II_PRCNT_MRKP, double II_SHPPNG_CST, double II_SPCFC_GRVTY, double II_STNDRD_FLL_SZ, double II_YLD,
                               double II_LBR_CST, double II_PWR_CST, double II_OHD_CST, double II_FRGHT_CST, double II_FXD_CST,
                               String II_GL_LBR_CST_ACCNT, String II_GL_PWR_CST_ACCNT, String II_GL_OHD_CST_ACCNT,
                               String II_GL_FRGHT_CST_ACCNT, String II_GL_FXD_CST_ACCNT, double II_STD_LST_PRCNTG, double II_MRKP_VAL,
                               String II_MRKT, byte II_ENBL_PO, short II_PO_CYCL, String II_UMC_PCKGNG, Integer II_RETAIL_UOM,
                               byte II_OPN_PRDCT, byte II_FXD_ASST, java.util.Date II_DT_ACQRD, Integer II_DFLT_LCTN, Integer II_AD_CMPNY)
            throws CreateException {

        try {

            LocalInvItem entity = new LocalInvItem();

            Debug.print("InvItemBean create");

            entity.setIiCode(II_CODE);
            entity.setIiName(II_NM);
            entity.setIiDescription(II_DESC);
            entity.setIiPartNumber(II_PRT_NMBR);
            entity.setIiShortName(II_SHRT_NM);
            entity.setIiBarCode1(II_BR_CD_1);
            entity.setIiBarCode2(II_BR_CD_2);
            entity.setIiBarCode3(II_BR_CD_3);
            entity.setIiBrand(II_BRND);
            entity.setIiClass(II_CLSS);
            entity.setIiAdLvCategory(II_AD_LV_CTGRY);
            entity.setIiCostMethod(II_CST_MTHD);
            entity.setIiUnitCost(II_UNT_CST);
            entity.setIiSalesPrice(II_SLS_PRC);
            entity.setIiEnable(II_ENBL);
            entity.setIiVirtualStore(II_VS_ITM);
            entity.setIiDoneness(II_DNNSS);
            entity.setIiSidings(II_SDNGS);
            entity.setIiRemarks(II_RMRKS);
            entity.setIiServiceCharge(II_SRVC_CHRG);
            entity.setIiNonInventoriable(II_DN_IN_CHRG);
            entity.setIiServices(II_SRVCS);
            entity.setIiJobServices(II_JB_SRVCS);
            entity.setIiIsVatRelief(II_IS_VAT_RLF);
            entity.setIiIsTax(II_IS_TX);
            entity.setIiIsProject(II_IS_PRJCT);
            entity.setIiPercentMarkup(II_PRCNT_MRKP);
            entity.setIiShippingCost(II_SHPPNG_CST);
            entity.setIiSpecificGravity(II_SPCFC_GRVTY);
            entity.setIiStandardFillSize(II_STNDRD_FLL_SZ);
            entity.setIiYield(II_YLD);
            entity.setIiLaborCost(II_LBR_CST);
            entity.setIiPowerCost(II_PWR_CST);
            entity.setIiOverHeadCost(II_OHD_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFixedCost(II_FXD_CST);
            entity.setGlCoaLaborCostAccount(II_GL_LBR_CST_ACCNT);
            entity.setGlCoaPowerCostAccount(II_GL_PWR_CST_ACCNT);
            entity.setGlCoaOverHeadCostAccount(II_GL_OHD_CST_ACCNT);
            entity.setGlCoaFreightCostAccount(II_GL_FRGHT_CST_ACCNT);
            entity.setGlCoaFixedCostAccount(II_GL_FXD_CST_ACCNT);
            entity.setIiLossPercentage(II_STD_LST_PRCNTG);
            entity.setIiMarkupValue(II_MRKP_VAL);
            entity.setIiMarket(II_MRKT);
            entity.setIiEnablePo(II_ENBL_PO);
            entity.setIiPoCycle(II_PO_CYCL);
            entity.setIiUmcPackaging(II_UMC_PCKGNG);
            entity.setIiRetailUom(II_RETAIL_UOM);
            entity.setIiOpenProduct(II_OPN_PRDCT);
            entity.setIiFixedAsset(II_FXD_ASST);
            entity.setIiDateAcquired(II_DT_ACQRD);
            entity.setIiDefaultLocation(II_DFLT_LCTN);
            entity.setIiAdCompany(II_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvItem create(String II_NM, String II_DESC, String II_PRT_NMBR, String II_SHRT_NM,
                               String II_BR_CD_1, String II_BR_CD_2, String II_BR_CD_3, String II_BRND, String II_CLSS,
                               String II_AD_LV_CTGRY, String II_CST_MTHD, double II_UNT_CST, double II_SLS_PRC, byte II_ENBL,
                               byte II_VS_ITM, String II_DNNSS, String II_SDNGS, String II_RMRKS, byte II_SRVC_CHRG,
                               byte II_DN_IN_CHRG, byte II_SRVCS, byte II_JB_SRVCS, byte II_IS_VAT_RLF, byte II_IS_TX, byte II_IS_PRJCT,
                               double II_PRCNT_MRKP, double II_SHPPNG_CST, double II_SPCFC_GRVTY, double II_STNDRD_FLL_SZ, double II_YLD,
                               double II_LBR_CST, double II_PWR_CST, double II_OHD_CST, double II_FRGHT_CST, double II_FXD_CST,
                               String II_GL_LBR_CST_ACCNT, String II_GL_PWR_CST_ACCNT, String II_GL_OHD_CST_ACCNT,
                               String II_GL_FRGHT_CST_ACCNT, String II_GL_FXD_CST_ACCNT, double II_STD_LST_PRCNTG, double II_MRKP_VAL,
                               String II_MRKT, byte II_ENBL_PO, short II_PO_CYCL, String II_UMC_PCKGNG, Integer II_RETAIL_UOM,
                               byte II_OPN_PRDCT, byte II_FXD_ASST, java.util.Date II_DT_ACQRD, Integer II_DFLT_LCTN, String II_TX_CD,
                               byte II_SC_SNDY, byte II_SC_MNDY, byte II_SC_TSDY, byte II_SC_WDNSDY, byte II_SC_THRSDY, byte II_SC_FRDY,
                               byte II_SC_STRDY, Integer II_AD_CMPNY) throws CreateException {

        try {

            LocalInvItem entity = new LocalInvItem();

            Debug.print("InvItemBean create");

            entity.setIiName(II_NM);
            entity.setIiDescription(II_DESC);
            entity.setIiPartNumber(II_PRT_NMBR);
            entity.setIiShortName(II_SHRT_NM);
            entity.setIiBarCode1(II_BR_CD_1);
            entity.setIiBarCode2(II_BR_CD_2);
            entity.setIiBarCode3(II_BR_CD_3);
            entity.setIiBrand(II_BRND);
            entity.setIiClass(II_CLSS);
            entity.setIiAdLvCategory(II_AD_LV_CTGRY);
            entity.setIiCostMethod(II_CST_MTHD);
            entity.setIiUnitCost(II_UNT_CST);
            entity.setIiSalesPrice(II_SLS_PRC);
            entity.setIiEnable(II_ENBL);
            entity.setIiVirtualStore(II_VS_ITM);
            entity.setIiDoneness(II_DNNSS);
            entity.setIiSidings(II_SDNGS);
            entity.setIiRemarks(II_RMRKS);
            entity.setIiServiceCharge(II_SRVC_CHRG);
            entity.setIiNonInventoriable(II_DN_IN_CHRG);
            entity.setIiServices(II_SRVCS);
            entity.setIiJobServices(II_JB_SRVCS);
            entity.setIiIsVatRelief(II_IS_VAT_RLF);
            entity.setIiIsTax(II_IS_TX);
            entity.setIiIsProject(II_IS_PRJCT);
            entity.setIiPercentMarkup(II_PRCNT_MRKP);
            entity.setIiShippingCost(II_SHPPNG_CST);
            entity.setIiSpecificGravity(II_SPCFC_GRVTY);
            entity.setIiStandardFillSize(II_STNDRD_FLL_SZ);
            entity.setIiYield(II_YLD);
            entity.setIiLaborCost(II_LBR_CST);
            entity.setIiPowerCost(II_PWR_CST);
            entity.setIiOverHeadCost(II_OHD_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFixedCost(II_FXD_CST);
            entity.setGlCoaLaborCostAccount(II_GL_LBR_CST_ACCNT);
            entity.setGlCoaPowerCostAccount(II_GL_PWR_CST_ACCNT);
            entity.setGlCoaOverHeadCostAccount(II_GL_OHD_CST_ACCNT);
            entity.setGlCoaFreightCostAccount(II_GL_FRGHT_CST_ACCNT);
            entity.setGlCoaFixedCostAccount(II_GL_FXD_CST_ACCNT);
            entity.setIiLossPercentage(II_STD_LST_PRCNTG);
            entity.setIiMarkupValue(II_MRKP_VAL);
            entity.setIiMarket(II_MRKT);
            entity.setIiEnablePo(II_ENBL_PO);
            entity.setIiPoCycle(II_PO_CYCL);
            entity.setIiUmcPackaging(II_UMC_PCKGNG);
            entity.setIiRetailUom(II_RETAIL_UOM);
            entity.setIiOpenProduct(II_OPN_PRDCT);
            entity.setIiFixedAsset(II_FXD_ASST);
            entity.setIiDateAcquired(II_DT_ACQRD);
            entity.setIiDefaultLocation(II_DFLT_LCTN);
            entity.setIiScSunday(II_SC_SNDY);
            entity.setIiScMonday(II_SC_MNDY);
            entity.setIiScTuesday(II_SC_TSDY);
            entity.setIiScWednesday(II_SC_WDNSDY);
            entity.setIiScThursday(II_SC_THRSDY);
            entity.setIiScFriday(II_SC_FRDY);
            entity.setIiScSaturday(II_SC_STRDY);
            entity.setIiAdCompany(II_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    public LocalInvItem create(
            String II_NM, String II_DESC, String II_PRT_NMBR, String II_SHRT_NM, String II_BR_CD_1, String II_BR_CD_2,
            String II_BR_CD_3, String II_BRND, String II_CLSS, String II_AD_LV_CTGRY, String II_CST_MTHD, double II_UNT_CST,
            double II_SLS_PRC, byte II_ENBL, byte II_VS_ITM, byte II_ENBL_AT_BLD, String II_DNNSS, String II_SDNGS,
            String II_RMRKS, byte II_SRVC_CHRG, byte II_DN_IN_CHRG, byte II_SRVCS, byte II_JB_SRVCS, byte II_IS_VAT_RLF,
            byte II_IS_TX, byte II_IS_PRJCT, double II_PRCNT_MRKP, double II_SHPPNG_CST, double II_SPCFC_GRVTY,
            double II_STNDRD_FLL_SZ, double II_YLD, double II_LBR_CST, double II_PWR_CST, double II_OHD_CST,
            double II_FRGHT_CST, double II_FXD_CST, String II_GL_LBR_CST_ACCNT, String II_GL_PWR_CST_ACCNT,
            String II_GL_OHD_CST_ACCNT, String II_GL_FRGHT_CST_ACCNT, String II_GL_FXD_CST_ACCNT, double II_STD_LST_PRCNTG,
            double II_MRKP_VAL, String II_MRKT, byte II_ENBL_PO, short II_PO_CYCL, String II_UMC_PCKGNG,
            Integer II_RETAIL_UOM, byte II_OPN_PRDCT, byte II_FXD_ASST, java.util.Date II_DT_ACQRD,
            Integer II_DFLT_LCTN, String II_TX_CD, byte II_SC_SNDY, byte II_SC_MNDY, byte II_SC_TSDY,
            byte II_SC_WDNSDY, byte II_SC_THRSDY, byte II_SC_FRDY, byte II_SC_STRDY, Integer II_AD_CMPNY) throws CreateException {

        try {

            LocalInvItem entity = new LocalInvItem();

            Debug.print("InvItemBean create");

            entity.setIiName(II_NM);
            entity.setIiDescription(II_DESC);
            entity.setIiPartNumber(II_PRT_NMBR);
            entity.setIiShortName(II_SHRT_NM);
            entity.setIiBarCode1(II_BR_CD_1);
            entity.setIiBarCode2(II_BR_CD_2);
            entity.setIiBarCode3(II_BR_CD_3);
            entity.setIiBrand(II_BRND);
            entity.setIiClass(II_CLSS);
            entity.setIiAdLvCategory(II_AD_LV_CTGRY);
            entity.setIiCostMethod(II_CST_MTHD);
            entity.setIiUnitCost(II_UNT_CST);
            entity.setIiSalesPrice(II_SLS_PRC);
            entity.setIiEnable(II_ENBL);
            entity.setIiVirtualStore(II_VS_ITM);
            entity.setIiDoneness(II_DNNSS);
            entity.setIiSidings(II_SDNGS);
            entity.setIiRemarks(II_RMRKS);
            entity.setIiServiceCharge(II_SRVC_CHRG);
            entity.setIiNonInventoriable(II_DN_IN_CHRG);
            entity.setIiServices(II_SRVCS);
            entity.setIiJobServices(II_JB_SRVCS);
            entity.setIiIsVatRelief(II_IS_VAT_RLF);
            entity.setIiIsTax(II_IS_TX);
            entity.setIiIsProject(II_IS_PRJCT);
            entity.setIiPercentMarkup(II_PRCNT_MRKP);
            entity.setIiShippingCost(II_SHPPNG_CST);
            entity.setIiSpecificGravity(II_SPCFC_GRVTY);
            entity.setIiStandardFillSize(II_STNDRD_FLL_SZ);
            entity.setIiYield(II_YLD);
            entity.setIiLaborCost(II_LBR_CST);
            entity.setIiPowerCost(II_PWR_CST);
            entity.setIiOverHeadCost(II_OHD_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFreightCost(II_FRGHT_CST);
            entity.setIiFixedCost(II_FXD_CST);
            entity.setGlCoaLaborCostAccount(II_GL_LBR_CST_ACCNT);
            entity.setGlCoaPowerCostAccount(II_GL_PWR_CST_ACCNT);
            entity.setGlCoaOverHeadCostAccount(II_GL_OHD_CST_ACCNT);
            entity.setGlCoaFreightCostAccount(II_GL_FRGHT_CST_ACCNT);
            entity.setGlCoaFixedCostAccount(II_GL_FXD_CST_ACCNT);
            entity.setIiAcquisitionCost(II_ACQSTN_CST);
            entity.setIiLifeSpan(II_LF_SPN);
            entity.setIiResidualValue(II_RSDL_VL);
            entity.setGlCoaFixedAssetAccount(II_GL_COA_FXD_ASST_ACCNT);
            entity.setGlCoaDepreciationAccount(II_GL_COA_DPRCTN_ACCNT);
            entity.setGlCoaAccumulatedDepreciationAccount(II_GL_ACCMLTD_DPRCTN_ACCNT);
            entity.setIiLossPercentage(II_STD_LST_PRCNTG);
            entity.setIiMarkupValue(II_MRKP_VAL);
            entity.setIiMarket(II_MRKT);
            entity.setIiEnablePo(II_ENBL_PO);
            entity.setIiPoCycle(II_PO_CYCL);
            entity.setIiUmcPackaging(II_UMC_PCKGNG);
            entity.setIiRetailUom(II_RETAIL_UOM);
            entity.setIiOpenProduct(II_OPN_PRDCT);
            entity.setIiFixedAsset(II_FXD_ASST);
            entity.setIiDateAcquired(II_DT_ACQRD);
            entity.setIiDefaultLocation(II_DFLT_LCTN);
            entity.setIiTraceMisc(II_TRC_MSC);
            entity.setIiScSunday(II_SC_SNDY);
            entity.setIiScMonday(II_SC_MNDY);
            entity.setIiScTuesday(II_SC_TSDY);
            entity.setIiScWednesday(II_SC_WDNSDY);
            entity.setIiScThursday(II_SC_THRSDY);
            entity.setIiScFriday(II_SC_FRDY);
            entity.setIiScSaturday(II_SC_STRDY);
            entity.setIiCondition(II_CNDTN);
            entity.setIiTaxCode(II_TX_CD);
            entity.setIiAdCompany(II_AD_CMPNY);

            em.persist(entity);
            return entity;

        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

//    public LocalInvItem create(
//            String II_NM, String II_DESC, String II_PRT_NMBR, String II_SHRT_NM, String II_BR_CD_1, String II_BR_CD_2,
//            String II_BR_CD_3, String II_BRND, String II_CLSS, String II_AD_LV_CTGRY, String II_CST_MTHD, double II_UNT_CST,
//            double II_SLS_PRC, byte II_ENBL, byte II_VS_ITM, byte II_ENBL_AT_BLD, String II_DNNSS, String II_SDNGS,
//            String II_RMRKS, byte II_SRVC_CHRG, byte II_DN_IN_CHRG, byte II_SRVCS, byte II_JB_SRVCS, byte II_IS_VAT_RLF,
//            byte II_IS_TX, byte II_IS_PRJCT, double II_PRCNT_MRKP, double II_SHPPNG_CST, double II_SPCFC_GRVTY,
//            double II_STNDRD_FLL_SZ, double II_YLD, double II_LBR_CST, double II_PWR_CST, double II_OHD_CST,
//            double II_FRGHT_CST, double II_FXD_CST, String II_GL_LBR_CST_ACCNT, String II_GL_PWR_CST_ACCNT,
//            String II_GL_OHD_CST_ACCNT, String II_GL_FRGHT_CST_ACCNT, String II_GL_FXD_CST_ACCNT, double II_STD_LST_PRCNTG,
//            double II_MRKP_VAL, String II_MRKT, byte II_ENBL_PO, short II_PO_CYCL, String II_UMC_PCKGNG,
//            Integer II_RETAIL_UOM, byte II_OPN_PRDCT, byte II_FXD_ASST, java.util.Date II_DT_ACQRD,
//            Integer II_DFLT_LCTN, String II_TX_CD, byte II_SC_SNDY, byte II_SC_MNDY, byte II_SC_TSDY,
//            byte II_SC_WDNSDY, byte II_SC_THRSDY, byte II_SC_FRDY, byte II_SC_STRDY, Integer II_AD_CMPNY)
//            throws CreateException {
//
//        Debug.print("InvItemBean create");
//
//        try {
//
//            LocalInvItem entity = new LocalInvItem();
//
//            entity.setIiName(II_NM);
//            entity.setIiDescription(II_DESC);
//            entity.setIiPartNumber(II_PRT_NMBR);
//            entity.setIiShortName(II_SHRT_NM);
//            entity.setIiBarCode1(II_BR_CD_1);
//            entity.setIiBarCode2(II_BR_CD_2);
//            entity.setIiBarCode3(II_BR_CD_3);
//            entity.setIiBrand(II_BRND);
//            entity.setIiClass(II_CLSS);
//            entity.setIiAdLvCategory(II_AD_LV_CTGRY);
//            entity.setIiCostMethod(II_CST_MTHD);
//            entity.setIiUnitCost(II_UNT_CST);
//            entity.setIiSalesPrice(II_SLS_PRC);
//            entity.setIiEnable(II_ENBL);
//            entity.setIiVirtualStore(II_VS_ITM);
//            entity.setIiEnableAutoBuild(II_ENBL_AT_BLD);
//            entity.setIiDoneness(II_DNNSS);
//            entity.setIiSidings(II_SDNGS);
//            entity.setIiRemarks(II_RMRKS);
//            entity.setIiServiceCharge(II_SRVC_CHRG);
//            entity.setIiNonInventoriable(II_DN_IN_CHRG);
//            entity.setIiServices(II_SRVCS);
//            entity.setIiJobServices(II_JB_SRVCS);
//            entity.setIiIsVatRelief(II_IS_VAT_RLF);
//            entity.setIiIsTax(II_IS_TX);
//            entity.setIiIsProject(II_IS_PRJCT);
//
//            entity.setIiPercentMarkup(II_PRCNT_MRKP);
//            entity.setIiShippingCost(II_SHPPNG_CST);
//            entity.setIiSpecificGravity(II_SPCFC_GRVTY);
//            entity.setIiStandardFillSize(II_STNDRD_FLL_SZ);
//            entity.setIiYield(II_YLD);
//
//            entity.setIiLaborCost(II_LBR_CST);
//            entity.setIiPowerCost(II_PWR_CST);
//            entity.setIiOverHeadCost(II_OHD_CST);
//            entity.setIiFreightCost(II_FRGHT_CST);
//            entity.setIiFreightCost(II_FRGHT_CST);
//            entity.setIiFixedCost(II_FXD_CST);
//
//            entity.setGlCoaLaborCostAccount(II_GL_LBR_CST_ACCNT);
//            entity.setGlCoaPowerCostAccount(II_GL_PWR_CST_ACCNT);
//            entity.setGlCoaOverHeadCostAccount(II_GL_OHD_CST_ACCNT);
//            entity.setGlCoaFreightCostAccount(II_GL_FRGHT_CST_ACCNT);
//            entity.setGlCoaFixedCostAccount(II_GL_FXD_CST_ACCNT);
//
//            entity.setIiLossPercentage(II_STD_LST_PRCNTG);
//            entity.setIiMarkupValue(II_MRKP_VAL);
//            entity.setIiMarket(II_MRKT);
//            entity.setIiEnablePo(II_ENBL_PO);
//            entity.setIiPoCycle(II_PO_CYCL);
//            entity.setIiUmcPackaging(II_UMC_PCKGNG);
//            entity.setIiRetailUom(II_RETAIL_UOM);
//            entity.setIiOpenProduct(II_OPN_PRDCT);
//            entity.setIiFixedAsset(II_FXD_ASST);
//            entity.setIiDateAcquired(II_DT_ACQRD);
//            entity.setIiDefaultLocation(II_DFLT_LCTN);
//
//            entity.setIiScSunday(II_SC_SNDY);
//            entity.setIiScMonday(II_SC_MNDY);
//            entity.setIiScTuesday(II_SC_TSDY);
//            entity.setIiScWednesday(II_SC_WDNSDY);
//            entity.setIiScThursday(II_SC_THRSDY);
//            entity.setIiScFriday(II_SC_FRDY);
//            entity.setIiScSaturday(II_SC_STRDY);
//
//            entity.setIiAdCompany(II_AD_CMPNY);
//
//            em.persist(entity);
//            return entity;
//
//        }
//        catch (Exception ex) {
//            throw new CreateException(ex.getMessage());
//        }

//    }

}