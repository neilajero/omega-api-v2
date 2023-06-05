package com.ejb.dao.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvBillOfMaterial;
import com.util.Debug;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class LocalInvBillOfMaterialHome {

    public static final String JNDI_NAME = "LocalInvBuildOfMaterialHome!com.ejb.inv.LocalInvBuildOfMaterialHome";

    @EJB
    public PersistenceBeanClass em;

    public LocalInvBillOfMaterial findByPrimaryKey(java.lang.Integer pk) throws FinderException {

        try {

            LocalInvBillOfMaterial entity = (LocalInvBillOfMaterial) em
                    .find(new LocalInvBillOfMaterial(), pk);
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

    public java.util.Collection findByBomIiName(java.lang.String BOM_II_NM, java.lang.Integer BOM_AD_CMPNY) {

        try {
            Query query = em.createQuery("SELECT OBJECT(bom) FROM InvBillOfMaterial bom WHERE bom.bomIiName=?1 AND bom.bomAdCompany=?2");
            query.setParameter(1, BOM_II_NM);
            query.setParameter(2, BOM_AD_CMPNY);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public LocalInvBillOfMaterial create(
            Integer BOM_CODE, String BOM_II_NM, String BOM_LOC_NM, double BOM_QNTTY_NDD,
            char BOM_DWNLD_STATUS, Integer BOM_AD_CMPNY)
            throws CreateException {

        Debug.print("InvBillOfMaterialBean ejbCreate");

        LocalInvBillOfMaterial entity = new LocalInvBillOfMaterial();

        entity.setBomCode(BOM_CODE);
        entity.setBomIiName(BOM_II_NM);
        entity.setBomLocName(BOM_LOC_NM);
        entity.setBomQuantityNeeded(BOM_QNTTY_NDD);
        entity.setBomDownloadStatus(BOM_DWNLD_STATUS);
        entity.setBomAdCompany(BOM_AD_CMPNY);

        em.persist(entity);
        return entity;

    }

    public LocalInvBillOfMaterial create(
            String BOM_II_NM, String BOM_LOC_NM, double BOM_QNTTY_NDD,
            char BOM_DWNLD_STATUS, Integer BOM_AD_CMPNY)
            throws CreateException {

        Debug.print("InvBillOfMaterialBean create");

        LocalInvBillOfMaterial entity = new LocalInvBillOfMaterial();

        entity.setBomIiName(BOM_II_NM);
        entity.setBomLocName(BOM_LOC_NM);
        entity.setBomQuantityNeeded(BOM_QNTTY_NDD);
        entity.setBomDownloadStatus(BOM_DWNLD_STATUS);
        entity.setBomAdCompany(BOM_AD_CMPNY);

        em.persist(entity);
        return entity;

    }
}