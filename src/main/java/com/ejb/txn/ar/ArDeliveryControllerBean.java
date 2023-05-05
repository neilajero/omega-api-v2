package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdBranchDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ar.LocalArDeliveryHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ad.LocalAdBranchDocumentSequenceAssignment;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ar.LocalArDelivery;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModDeliveryDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import jakarta.ejb.*;

import java.util.ArrayList;
import java.util.Collection;


@Stateless(name = "ArDeliveryControllerEJB")
public class ArDeliveryControllerBean extends EJBContextClass implements ArDeliveryController {


    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalAdBranchDocumentSequenceAssignmentHome adBranchDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalArDeliveryHome arDeliveryHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;

    public void saveArDelivery(java.util.ArrayList list, java.lang.String SO_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) {

        Debug.print("ArDeliveryControllerBean saveArDelivery");

        try {

            for (Object o : list) {

                ArModDeliveryDetails details = (ArModDeliveryDetails) o;

                if (details.getDvCode() == null) {


                    LocalAdBranchDocumentSequenceAssignment adBranchDocumentSequenceAssignmentDrNum = null;
                    LocalAdDocumentSequenceAssignment adDocumentSequenceAssignmentDrNum = null;

                    //DR NUMBER GENERATOR


                    try {
                        adDocumentSequenceAssignmentDrNum = adDocumentSequenceAssignmentHome.findByDcName("AR DELIVERY", AD_CMPNY);
                    }
                    catch (FinderException ex) {

                    }


                    try {

                        adBranchDocumentSequenceAssignmentDrNum = adBranchDocumentSequenceAssignmentHome.findBdsByDsaCodeAndBrCode(adDocumentSequenceAssignmentDrNum.getDsaCode(), AD_BRNCH, AD_CMPNY);

                    }
                    catch (FinderException ex) {

                    }


                    if (adDocumentSequenceAssignmentDrNum.getAdDocumentSequence().getDsNumberingType() == 'A' && (details.getDvDeliveryNumber() == null || details.getDvDeliveryNumber().trim().length() == 0)) {

                        while (true) {

                            if (adBranchDocumentSequenceAssignmentDrNum == null || adBranchDocumentSequenceAssignmentDrNum.getBdsNextSequence() == null) {

                                try {

                                    arDeliveryHome.findByDvDeliveryNumber(adDocumentSequenceAssignmentDrNum.getDsaNextSequence());
                                    adDocumentSequenceAssignmentDrNum.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignmentDrNum.getDsaNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setDvDeliveryNumber(adDocumentSequenceAssignmentDrNum.getDsaNextSequence());
                                    adDocumentSequenceAssignmentDrNum.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignmentDrNum.getDsaNextSequence()));
                                    break;

                                }

                            } else {

                                try {

                                    arSalesOrderHome.findBySoDocumentNumberAndBrCode(adBranchDocumentSequenceAssignmentDrNum.getBdsNextSequence(), AD_BRNCH, AD_CMPNY);
                                    adBranchDocumentSequenceAssignmentDrNum.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignmentDrNum.getBdsNextSequence()));

                                }
                                catch (FinderException ex) {

                                    details.setDvDeliveryNumber(adBranchDocumentSequenceAssignmentDrNum.getBdsNextSequence());
                                    adBranchDocumentSequenceAssignmentDrNum.setBdsNextSequence(EJBCommon.incrementStringNumber(adBranchDocumentSequenceAssignmentDrNum.getBdsNextSequence()));
                                    break;

                                }

                            }

                        }

                    }


                    LocalArDelivery arDelivery = arDeliveryHome.create(details.getDvContainer(), details.getDvDeliveryNumber(), details.getDvBookingTime(), details.getDvTabsFeePullOut(), details.getDvReleasedDate(), details.getDvDeliveredDate(), details.getDvDeliveredTo(), details.getDvPlateNo(), details.getDvDriverName(), details.getDvEmptyReturnDate(), details.getDvEmptyReturnTo(), details.getDvTabsFeeReturn(), details.getDvStatus(), details.getDvRemarks());


                    LocalArSalesOrder arSalesOrder = arSalesOrderHome.findBySoDocumentNumberAndBrCode(SO_DCMNT_NMBR, AD_BRNCH, AD_CMPNY);


                    arDelivery.setArSalesOrder(arSalesOrder);

                    arSalesOrder.setSoTransactionStatus(details.getSoTransactionStatus());


                } else {

                    Debug.print("not null detected in :" + details.getDvCode());
                    try {

                        Integer dvCode = details.getDvCode();


                        LocalArDelivery arDelivery = arDeliveryHome.findByPrimaryKey(dvCode);


                        arDelivery.setDvContainer(details.getDvContainer());
                        arDelivery.setDvDeliveryNumber(details.getDvDeliveryNumber());
                        arDelivery.setDvBookingTime(details.getDvBookingTime());
                        arDelivery.setDvTabsFeePullOut(details.getDvTabsFeePullOut());
                        arDelivery.setDvReleasedDate(details.getDvReleasedDate());
                        arDelivery.setDvDeliveredDate(details.getDvDeliveredDate());
                        arDelivery.setDvDeliveredTo(details.getDvDeliveredTo());
                        arDelivery.setDvPlateNo(details.getDvPlateNo());
                        arDelivery.setDvDriverName(details.getDvDriverName());
                        arDelivery.setDvEmptyReturnDate(details.getDvEmptyReturnDate());
                        arDelivery.setDvEmptyReturnTo(details.getDvEmptyReturnTo());
                        arDelivery.setDvTabsFeeReturn(details.getDvTabsFeeReturn());
                        arDelivery.setDvStatus(details.getDvStatus());
                        arDelivery.setDvRemarks(details.getDvRemarks());


                        LocalArSalesOrder arSalesOrder = arDelivery.getArSalesOrder();


                        arSalesOrder.setSoTransactionStatus(details.getSoTransactionStatus());
                    }
                    catch (FinderException ex) {

                    }


                }


            }

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }


    }


    public void deleteDeliveryByDvCode(java.lang.Integer DV_CODE) throws FinderException {

        Debug.print("ArDeliveryControllerBean deleteDeliveryByDvCode");


        try {

            LocalArDelivery arDelivery = arDeliveryHome.findByPrimaryKey(DV_CODE);

//			arDelivery.entityRemove();
            em.remove(arDelivery);
        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);

            throw new FinderException();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }

    public ArModSalesOrderDetails getArSalesOrderByCode(java.lang.Integer SO_CODE) throws FinderException {

        Debug.print("ArDeliveryControllerBean deleteDeliveryByDvCode");

        try {


            LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);

            ArModSalesOrderDetails details = new ArModSalesOrderDetails();


            details.setSoCode(arSalesOrder.getSoCode());
            details.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
            details.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
            details.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());
            details.setSoTransactionStatus(arSalesOrder.getSoTransactionStatus());


            return details;

        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);

            throw new FinderException();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public java.util.ArrayList getDeliveryBySoCode(java.lang.Integer SO_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException {

        Debug.print("ArDeliveryControllerBean getDeliveryBySoCode");

        ArrayList list = new ArrayList();

        try {


            Collection dbList = arDeliveryHome.findBySoCode(SO_CODE);


            for (Object o : dbList) {

                LocalArDelivery arDelivery = (LocalArDelivery) o;


                ArModDeliveryDetails details = new ArModDeliveryDetails();


                details.setDvCode(arDelivery.getDvCode());
                details.setDvContainer(arDelivery.getDvContainer());
                details.setDvDeliveryNumber(arDelivery.getDvDeliveryNumber());
                details.setDvBookingTime(arDelivery.getDvBookingTime());
                details.setDvTabsFeePullOut(arDelivery.getDvTabsFeePullOut());
                details.setDvReleasedDate(arDelivery.getDvReleasedDate());
                details.setDvDeliveredDate(arDelivery.getDvDeliveredDate());
                details.setDvDeliveredTo(arDelivery.getDvDeliveredTo());
                details.setDvPlateNo(arDelivery.getDvPlateNo());
                details.setDvDriverName(arDelivery.getDvDriverName());
                details.setDvEmptyReturnDate(arDelivery.getDvEmptyReturnDate());
                details.setDvEmptyReturnTo(arDelivery.getDvEmptyReturnTo());
                details.setDvTabsFeeReturn(arDelivery.getDvTabsFeeReturn());
                details.setDvStatus(arDelivery.getDvStatus());
                details.setDvRemarks(arDelivery.getDvRemarks());

                details.setCstName(arDelivery.getArSalesOrder().getArCustomer().getCstCustomerCode());
                details.setSoDocumentNumber(arDelivery.getArSalesOrder().getSoDocumentNumber());
                details.setSoReferenceNumber(arDelivery.getArSalesOrder().getSoReferenceNumber());
                details.setSoTransactionStatus(arDelivery.getArSalesOrder().getSoTransactionStatus());

                list.add(details);
            }


            return list;

        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);

            throw new FinderException();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public java.util.ArrayList getDeliveryBySoDocumentNumber(java.lang.String SO_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException {

        Debug.print("ArDeliveryControllerBean getDeliveryBySoDocumentNumber");

        ArrayList list = new ArrayList();

        try {


            Collection dbList = arDeliveryHome.findBySoDocumentNumber(SO_DCMNT_NMBR);


            for (Object o : dbList) {

                LocalArDelivery arDelivery = (LocalArDelivery) o;


                ArModDeliveryDetails details = new ArModDeliveryDetails();


                details.setDvCode(arDelivery.getDvCode());
                details.setDvContainer(arDelivery.getDvContainer());
                details.setDvDeliveryNumber(arDelivery.getDvDeliveryNumber());
                details.setDvBookingTime(arDelivery.getDvBookingTime());
                details.setDvTabsFeePullOut(arDelivery.getDvTabsFeePullOut());
                details.setDvReleasedDate(arDelivery.getDvReleasedDate());
                details.setDvDeliveredDate(arDelivery.getDvDeliveredDate());
                details.setDvDeliveredTo(arDelivery.getDvDeliveredTo());
                details.setDvPlateNo(arDelivery.getDvPlateNo());
                details.setDvDriverName(arDelivery.getDvDriverName());
                details.setDvEmptyReturnDate(arDelivery.getDvEmptyReturnDate());
                details.setDvEmptyReturnTo(arDelivery.getDvEmptyReturnTo());
                details.setDvTabsFeeReturn(arDelivery.getDvTabsFeeReturn());
                details.setDvStatus(arDelivery.getDvStatus());
                details.setDvRemarks(arDelivery.getDvRemarks());

                details.setCstName(arDelivery.getArSalesOrder().getArCustomer().getCstCustomerCode());
                details.setSoDocumentNumber(arDelivery.getArSalesOrder().getSoDocumentNumber());
                details.setSoReferenceNumber(arDelivery.getArSalesOrder().getSoReferenceNumber());
                details.setSoTransactionStatus(arDelivery.getArSalesOrder().getSoTransactionStatus());

                list.add(details);
            }


            return list;

        }
        catch (FinderException ex) {
            Debug.printStackTrace(ex);

            throw new FinderException();

        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }

    }


    public ArrayList getAdLvTransactionStatusAll(Integer AD_CMPNY) {

        Debug.print("ArDeliveryControllerBean getAdLvTransactionStatusAll");

        ArrayList list = new ArrayList();

        try {

            String lookUpName = "AR SALES ORDER TRANSACTION STATUS";


            Collection adLookUpValues = adLookUpValueHome.findByLuName(lookUpName, AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());

            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }
    //	 SessionBean methods


    public void ejbCreate() throws CreateException {

        Debug.print("ArDeliveryControllerBean ejbCreate");

    }

}