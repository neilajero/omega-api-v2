/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApCanvassControllerBean
 * @created April 20, 2006 02:00 PM
 * @author Aliza D.J. Anos
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.dao.ap.LocalApCanvassHome;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.dao.ap.LocalApPurchaseRequisitionLineHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ap.ApModCanvassDetails;
import com.util.mod.ap.ApModPurchaseRequisitionLineDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApCanvassControllerEJB")
public class ApCanvassControllerBean extends EJBContextClass implements ApCanvassController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisionHome;
    @EJB
    private LocalApPurchaseRequisitionLineHome apPurchaseRequisitionLineHome;
    @EJB
    private LocalApCanvassHome apCanvassHome;

    public ArrayList getApSplAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(AD_BRNCH, AD_CMPNY);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ApModPurchaseRequisitionLineDetails getApPrlByPrlCode(Integer PRL_CODE, Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getApPrlByPrlCode");

        try {

            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrimaryKey(PRL_CODE);

            ApModPurchaseRequisitionLineDetails details = new ApModPurchaseRequisitionLineDetails();

            details.setPrlPrNumber(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrNumber());
            details.setPrlPrDate(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrDate());
            details.setPrlIlIiName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
            details.setPrlIlLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
            details.setPrlUomName(apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName());
            details.setPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());
            details.setPrlAmount(apPurchaseRequisitionLine.getPrlAmount());
            details.setPrlIlIiDescription(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription());
            details.setPrApprovalStatus(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrApprovalStatus());
            Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

            // get canvass lines

            for (Object canvass : apCanvasses) {

                LocalApCanvass apCanvass = (LocalApCanvass) canvass;

                ApModCanvassDetails cnvDetails = new ApModCanvassDetails();

                cnvDetails.setCnvLine(apCanvass.getCnvLine());
                Debug.print("REMARKS=" + apCanvass.getCnvRemarks());
                cnvDetails.setCnvRemarks(apCanvass.getCnvRemarks());
                cnvDetails.setCnvSplSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());
                cnvDetails.setCnvQuantity(apCanvass.getCnvQuantity());
                cnvDetails.setCnvUnitCost(apCanvass.getCnvUnitCost());
                cnvDetails.setCnvAmount(apCanvass.getCnvAmount());
                cnvDetails.setCnvPo(apCanvass.getCnvPo());
                cnvDetails.setCnvSplSupplierName(apCanvass.getApSupplier().getSplName());

                details.saveCnvList(cnvDetails);
            }

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveApCnvss(ArrayList cnvList, Integer PRL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ApCanvassControllerBean saveApCnvss");

        try {

            LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = apPurchaseRequisitionLine = apPurchaseRequisitionLineHome.findByPrimaryKey(PRL_CODE);


            Integer PR_CODE = apPurchaseRequisitionLine.getApPurchaseRequisition().getPrCode();

            Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

            // remove all canvass lines

            Iterator i = apCanvasses.iterator();

            while (i.hasNext()) {

                LocalApCanvass apCanvass = (LocalApCanvass) i.next();

                i.remove();

                em.remove(apCanvass);
            }

            // add new canvass lines to purchase requisition line

            double PRL_AMNT = 0d;
            double PRL_QTTY = 0d;
            String SPL_SPPLR_CODE = null;

            i = cnvList.iterator();

            while (i.hasNext()) {

                ApModCanvassDetails mdetails = (ApModCanvassDetails) i.next();
                Debug.print("REMARKS=" + mdetails.getCnvRemarks());
                LocalApCanvass apCanvass = apCanvassHome.create(mdetails.getCnvLine(), mdetails.getCnvRemarks(), mdetails.getCnvQuantity(), mdetails.getCnvUnitCost(), EJBCommon.roundIt(mdetails.getCnvQuantity() * mdetails.getCnvUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY)), mdetails.getCnvPo(), AD_CMPNY);

                apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

                LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(mdetails.getCnvSplSupplierCode(), AD_CMPNY);
                SPL_SPPLR_CODE = apSupplier.getSplSupplierCode();
                if (this.hasSupplier(apPurchaseRequisitionLine, apSupplier.getSplSupplierCode())) {

                    throw new GlobalRecordAlreadyExistException(apSupplier.getSplSupplierCode());
                }

                apSupplier.addApCanvass(apCanvass);
                apPurchaseRequisitionLine.addApCanvass(apCanvass);
                if (apCanvass.getCnvPo() == 1) {
                    PRL_AMNT += apCanvass.getCnvAmount();

                    PRL_QTTY += apCanvass.getCnvQuantity();
                }
            }


            //Update all supplier canvasses
            this.updateSupplierCanvasses(SPL_SPPLR_CODE, PR_CODE, AD_CMPNY);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfApJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getAdPrfApJournalLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApJournalLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApCanvassControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void updateSupplierCanvasses(String SUPPLIER_CODE, Integer PR_CODE, Integer AD_CMPNY) {


        Debug.print("ApCanvassControllerBean updateSupplierCanvasses");
        try {

            LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(SUPPLIER_CODE, AD_CMPNY);

            Collection list = apCanvassHome.findByPrCodeAndCnvPo(PR_CODE, EJBCommon.TRUE, AD_CMPNY);

            for (Object o : list) {
                LocalApCanvass apCanvass = (LocalApCanvass) o;
                apCanvass.setApSupplier(apSupplier);

            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }


    }


    // private methods

    private boolean hasSupplier(LocalApPurchaseRequisitionLine apPurchaseRequisitionLine, String SPL_SPPLR_CD) {

        Debug.print("ApCanvassControllerBean hasSupplier");

        Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

        for (Object canvass : apCanvasses) {

            LocalApCanvass apCanvass = (LocalApCanvass) canvass;

            if (apCanvass.getApSupplier().getSplSupplierCode().equals(SPL_SPPLR_CD)) {

                return true;
            }
        }

        return false;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApCanvassControllerBean ejbCreate");
    }
}