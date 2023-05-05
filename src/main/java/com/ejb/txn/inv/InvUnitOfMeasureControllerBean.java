/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvUnitOfMeasureControllerBean
 * @created
 * @author
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.inv.InvUOMOneBaseUnitIsAllowedException;
import com.ejb.exception.inv.InvUOMShortNameAlreadyExistException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.inv.InvUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "InvUnitOfMeasureControllerEJB")
public class InvUnitOfMeasureControllerBean extends EJBContextClass implements InvUnitOfMeasureController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;

    public ArrayList getInvUomAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvUnitOfMeasureControllerBean getInvUomAll");
        ArrayList list = new ArrayList();
        try {

            Collection invUnitOfMeasures = invUnitOfMeasureHome.findUomAll(AD_CMPNY);

            if (invUnitOfMeasures.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object unitOfMeasure : invUnitOfMeasures) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) unitOfMeasure;

                InvUnitOfMeasureDetails details = new InvUnitOfMeasureDetails();
                details.setUomCode(invUnitOfMeasure.getUomCode());
                details.setUomName(invUnitOfMeasure.getUomName());
                details.setUomDescription(invUnitOfMeasure.getUomDescription());
                details.setUomShortName(invUnitOfMeasure.getUomShortName());
                details.setUomAdLvClass(invUnitOfMeasure.getUomAdLvClass());
                details.setUomConversionFactor(invUnitOfMeasure.getUomConversionFactor());
                details.setUomBaseUnit(invUnitOfMeasure.getUomBaseUnit());
                details.setUomEnable(invUnitOfMeasure.getUomEnable());

                list.add(details);
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addInvUomEntry(InvUnitOfMeasureDetails details, Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, InvUOMShortNameAlreadyExistException, InvUOMOneBaseUnitIsAllowedException {

        Debug.print("InvUnitOfMeasureControllerBean addInvUomEntry");
        try {

            LocalInvUnitOfMeasure invUnitOfMeasure = null;

            try {

                invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(details.getUomName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            }
            catch (FinderException ex) {

            }

            try {

                invUnitOfMeasure = invUnitOfMeasureHome.findByUomShortName(details.getUomShortName(), AD_CMPNY);

                throw new InvUOMShortNameAlreadyExistException();

            }
            catch (FinderException ex) {

            }

            if (details.getUomBaseUnit() == EJBCommon.TRUE) {

                try {

                    invUnitOfMeasure = invUnitOfMeasureHome.findByUomAdLvClassAndBaseUnitEqualsTrue(details.getUomAdLvClass(), AD_CMPNY);

                    throw new InvUOMOneBaseUnitIsAllowedException();

                }
                catch (FinderException ex) {

                }
            }

            // create new unit of measure

            invUnitOfMeasure = invUnitOfMeasureHome.create(details.getUomName(), details.getUomDescription(), details.getUomShortName(), details.getUomAdLvClass(), details.getUomConversionFactor(), details.getUomBaseUnit(), details.getUomEnable(), 'N', AD_CMPNY);

            // create new unit of measure conversion

            this.updateIiUomConversion(invUnitOfMeasure, AD_CMPNY);

        }
        catch (GlobalRecordAlreadyExistException | InvUOMOneBaseUnitIsAllowedException |
               InvUOMShortNameAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateInvUomEntry(InvUnitOfMeasureDetails details, Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, InvUOMShortNameAlreadyExistException, InvUOMOneBaseUnitIsAllowedException {

        Debug.print("InvUnitOfMeasureControllerBean updateInvUomEntry");
        LocalInvUnitOfMeasure invUnitOfMeasure;
        try {

            LocalInvUnitOfMeasure invExistingUnitOfMeasure = null;

            try {

                invExistingUnitOfMeasure = invUnitOfMeasureHome.findByUomName(details.getUomName(), AD_CMPNY);

                if (!invExistingUnitOfMeasure.getUomCode().equals(details.getUomCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            }
            catch (FinderException ex) {

            }

            try {

                invExistingUnitOfMeasure = invUnitOfMeasureHome.findByUomShortName(details.getUomShortName(), AD_CMPNY);

                if (!invExistingUnitOfMeasure.getUomCode().equals(details.getUomCode())) {

                    throw new InvUOMShortNameAlreadyExistException();
                }

            }
            catch (FinderException ex) {

            }

            if (details.getUomBaseUnit() == EJBCommon.TRUE) {

                try {

                    invExistingUnitOfMeasure = invUnitOfMeasureHome.findByUomAdLvClassAndBaseUnitEqualsTrue(details.getUomAdLvClass(), AD_CMPNY);

                    if (!invExistingUnitOfMeasure.getUomCode().equals(details.getUomCode())) {

                        throw new InvUOMOneBaseUnitIsAllowedException();
                    }

                }
                catch (FinderException ex) {

                }
            }

            // find and update unit of measure

            invUnitOfMeasure = invUnitOfMeasureHome.findByPrimaryKey(details.getUomCode());

            invUnitOfMeasure.setUomName(details.getUomName());
            invUnitOfMeasure.setUomDescription(details.getUomDescription());
            invUnitOfMeasure.setUomShortName(details.getUomShortName());
            invUnitOfMeasure.setUomAdLvClass(details.getUomAdLvClass());
            invUnitOfMeasure.setUomConversionFactor(details.getUomConversionFactor());
            invUnitOfMeasure.setUomBaseUnit(details.getUomBaseUnit());
            invUnitOfMeasure.setUomEnable(details.getUomEnable());

            if (invUnitOfMeasure.getUomDownloadStatus() == 'N') {

                invUnitOfMeasure.setUomDownloadStatus('U');

            } else if (invUnitOfMeasure.getUomDownloadStatus() == 'D') {

                invUnitOfMeasure.setUomDownloadStatus('X');
            }

        }
        catch (GlobalRecordAlreadyExistException | InvUOMOneBaseUnitIsAllowedException |
               InvUOMShortNameAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvUomEntry(Integer UOM_CODE, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("InvUnitOfMeasureControllerBean deleteInvUomEntry");
        try {

            LocalInvUnitOfMeasure invUnitOfMeasure = null;

            try {

                invUnitOfMeasure = invUnitOfMeasureHome.findByPrimaryKey(UOM_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!invUnitOfMeasure.getInvItems().isEmpty()
                    || !invUnitOfMeasure.getInvAdjustmentLines().isEmpty() || !invUnitOfMeasure.getApPurchaseOrderLines().isEmpty()
                    || !invUnitOfMeasure.getApPurchaseRequisitionLines().isEmpty() || !invUnitOfMeasure.getApVoucherLineItems().isEmpty()
                    || !invUnitOfMeasure.getArInvoiceLineItems().isEmpty() || !invUnitOfMeasure.getArSalesOrderLines().isEmpty()
                    || !invUnitOfMeasure.getInvBranchStockTransferLines().isEmpty() || !invUnitOfMeasure.getInvStockTransferLines().isEmpty()
                    || !invUnitOfMeasure.getInvPhysicalInventoryLines().isEmpty()
                    || !invUnitOfMeasure.getInvLineItems().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            // if already downloaded to pos
            if (invUnitOfMeasure.getUomDownloadStatus() == 'D' || invUnitOfMeasure.getUomDownloadStatus() == 'X') {

                throw new GlobalRecordAlreadyAssignedException();
            }

            //		    invUnitOfMeasure.entityRemove();
            em.remove(invUnitOfMeasure);

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfInvQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvUnitOfMeasureControllerBean getAdPrfInvQuantityPrecisionUnit");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateIiUomConversion(LocalInvUnitOfMeasure invUnitOfMeasure, Integer AD_CMPNY) {

        Debug.print("InvUnitOfMeasureControllerBean updateIiUomConversion");
        try {

            Collection invItems = invItemHome.findIiByUmcAdLvClass(invUnitOfMeasure.getUomAdLvClass(), AD_CMPNY);

            if (!invItems.isEmpty()) {

                for (Object item : invItems) {

                    LocalInvItem invItem = (LocalInvItem) item;

                    // create umc
                    LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.create(invUnitOfMeasure.getUomConversionFactor(), EJBCommon.FALSE, AD_CMPNY);

                    // map uom
                    invUnitOfMeasureConversion.setInvUnitOfMeasure(invUnitOfMeasure);

                    // map item
                    invUnitOfMeasureConversion.setInvItem(invItem);
                }
            }

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvUnitOfMeasureControllerBean ejbCreate");
    }

}