package com.ejb.txn.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArJobOrderAssignment;
import com.ejb.dao.ar.LocalArJobOrderAssignmentHome;
import com.ejb.entities.ar.LocalArJobOrderLine;
import com.ejb.dao.ar.LocalArJobOrderLineHome;
import com.ejb.entities.ar.LocalArPersonel;
import com.ejb.dao.ar.LocalArPersonelHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModJobOrderAssignmentDetails;
import com.util.mod.ar.ArModJobOrderLineDetails;
import com.util.ar.ArPersonelDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArJobOrderAssignmentControllerEJB")
public class ArJobOrderAssignmentControllerBean extends EJBContextClass implements ArJobOrderAssignmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArJobOrderAssignmentHome arJobOrderAssignmentHome;
    @EJB
    private LocalArJobOrderLineHome arJobOrderLineHome;
    @EJB
    private LocalArPersonelHome arPersonelHome;

    public ArrayList getArPeAll(Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ArJobOrderAssignmentControllerBean getApSplAll");

        ArrayList list = new ArrayList();

        try {

            Collection arPersonels = arPersonelHome.findPeAll(AD_CMPNY);

            for (Object personel : arPersonels) {

                LocalArPersonel arPersonel = (LocalArPersonel) personel;

                list.add(arPersonel.getPeIdNumber());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModJobOrderLineDetails getArJolByJolCode(Integer JOL_CODE, Integer AD_CMPNY) {

        Debug.print("ArJobOrderAssignmentControllerBean getArJolByJolCode");

        try {

            LocalArJobOrderLine arJobOrderLine = arJobOrderLineHome.findByPrimaryKey(JOL_CODE);

            ArModJobOrderLineDetails details = new ArModJobOrderLineDetails();

            details.setJolJoDocumentNumber(arJobOrderLine.getArJobOrder().getJoDocumentNumber());
            details.setJolJoDate(arJobOrderLine.getArJobOrder().getJoDate());
            details.setJolIiName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
            details.setJolLocName(arJobOrderLine.getInvItemLocation().getInvLocation().getLocName());
            details.setJolUomName(arJobOrderLine.getInvUnitOfMeasure().getUomName());
            details.setJolQuantity(arJobOrderLine.getJolQuantity());
            details.setJolUnitPrice(arJobOrderLine.getJolUnitPrice());
            details.setJolAmount(arJobOrderLine.getJolAmount());
            details.setJolIiDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
            details.setJoVoid(arJobOrderLine.getArJobOrder().getJoVoid() == EJBCommon.TRUE);
            details.setJoPosted(arJobOrderLine.getArJobOrder().getJoPosted() == EJBCommon.TRUE);

            Collection arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

            // get canvass lines

            for (Object jobOrderAssignment : arJobOrderAssignments) {

                LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) jobOrderAssignment;

                ArModJobOrderAssignmentDetails jaDetails = new ArModJobOrderAssignmentDetails();
                Debug.print("ja code:" + arJobOrderAssignment.getJaCode());
                jaDetails.setJaCode(arJobOrderAssignment.getJaCode());
                jaDetails.setJaLine(arJobOrderAssignment.getJaLine());
                Debug.print("REMARKS=" + arJobOrderAssignment.getJaRemarks());

                jaDetails.setJaRemarks(arJobOrderAssignment.getJaRemarks());
                jaDetails.setJaPeIdNumber(arJobOrderAssignment.getArPersonel().getPeIdNumber());
                jaDetails.setJaQuantity(arJobOrderAssignment.getJaQuantity());

                Debug.print("unit cost:?" + arJobOrderAssignment.getJaUnitCost());
                jaDetails.setJaUnitCost(arJobOrderAssignment.getJaUnitCost());
                jaDetails.setJaAmount(arJobOrderAssignment.getJaAmount());
                jaDetails.setJaSo(arJobOrderAssignment.getJaSo());
                jaDetails.setJaPeName(arJobOrderAssignment.getArPersonel().getPeName());

                details.saveJaList(jaDetails);
            }

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveArJbOrdrAssgnmnt(ArrayList jaList, Integer JOL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArJobOrderAssignmentControllerBean saveArJbOrdrAssgnmnt");

        try {

            LocalArJobOrderLine arJobOrderLine = arJobOrderLineHome.findByPrimaryKey(JOL_CODE);

            Collection arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

            // remove all canvass lines

            Iterator i = arJobOrderAssignments.iterator();

            while (i.hasNext()) {

                LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) i.next();

                i.remove();

                //				arJobOrderAssignment.entityRemove();
                em.remove(arJobOrderAssignment);
            }

            // add new canvass lines to purchase requisition line

            double JOL_AMNT = 0d;
            double JOL_QTTY = 0d;

            i = jaList.iterator();

            while (i.hasNext()) {

                ArModJobOrderAssignmentDetails mdetails = (ArModJobOrderAssignmentDetails) i.next();
                Debug.print("REMARKS=" + mdetails.getJaRemarks());

                LocalArJobOrderAssignment arJobOrderAssignment = arJobOrderAssignmentHome.create(mdetails.getJaLine(), mdetails.getJaRemarks(), mdetails.getJaQuantity(), mdetails.getJaUnitCost(), EJBCommon.roundIt(mdetails.getJaQuantity() * mdetails.getJaUnitCost(), this.getGlFcPrecisionUnit(AD_CMPNY)), mdetails.getJaSo(), AD_CMPNY);

                arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

                LocalArPersonel arPersonel = arPersonelHome.findByPeIdNumber(mdetails.getJaPeIdNumber(), AD_CMPNY);

                if (this.hasSupplier(arJobOrderLine, arPersonel.getPeIdNumber())) {

                    throw new GlobalRecordAlreadyExistException(arPersonel.getPeIdNumber());
                }

                arJobOrderAssignment.setArPersonel(arPersonel);

                arJobOrderAssignment.setArJobOrderLine(arJobOrderLine);

                if (arJobOrderAssignment.getJaSo() == 1) {
                    JOL_AMNT += arJobOrderAssignment.getJaAmount();

                    JOL_QTTY += arJobOrderAssignment.getJaQuantity();
                }
            }

            arJobOrderLine.setJolUnitPrice(JOL_AMNT);
            arJobOrderLine.setJolAmount(JOL_AMNT);
            arJobOrderLine.setJolQuantity(1);
            Debug.print("JOL_QTTY-----" + JOL_QTTY);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArJobOrderAssignmentControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getInvGpQuantityPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArJobOrderAssignmentControllerBean getInvGpQuantityPrecisionUnit");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvQuantityPrecisionUnit();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfArInvoiceLineNumber(Integer AD_CMPNY) {

        Debug.print("ArJobOrderAssignmentControllerBean getAdPrfArInvoiceLineNumber");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfArInvoiceLineNumber();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArPersonelDetails getArPrsnlByPeIdNmbr(String PE_ID_NMBR, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArJobOrderAssignmentControllerBean getArPrsnlByPeIdNmbr");

        try {

            LocalArPersonel arPersonel = arPersonelHome.findByPeIdNumber(PE_ID_NMBR, AD_CMPNY);

            ArPersonelDetails details = new ArPersonelDetails();

            details.setPeCode(arPersonel.getPeCode());
            details.setPeName(arPersonel.getPeName());
            details.setPeIdNumber(arPersonel.getPeIdNumber());
            details.setPeDescription(arPersonel.getPeDescription());
            details.setPeAddress(arPersonel.getPeAddress());
            details.setPeRate(arPersonel.getPeRate());

            return details;
        } catch (FinderException ex) {

            throw new GlobalNoRecordFoundException(ex.getMessage());
        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private boolean hasSupplier(LocalArJobOrderLine arJobOrderLine, String PE_ID_NMBR) {

        Debug.print("ArJobOrderAssignmentControllerBean hasSupplier");

        Collection arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

        for (Object jobOrderAssignment : arJobOrderAssignments) {

            LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) jobOrderAssignment;

            if (arJobOrderAssignment.getArPersonel().getPeIdNumber().equals(PE_ID_NMBR)) {

                return true;
            }
        }

        return false;
    }

    //	 SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArJobOrderAssignmentControllerBean ejbCreate");
    }
}