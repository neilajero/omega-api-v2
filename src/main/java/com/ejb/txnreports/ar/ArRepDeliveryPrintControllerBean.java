package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArDeliveryHome;
import com.ejb.dao.ar.LocalArSalesOrderHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArDelivery;
import com.ejb.entities.ar.LocalArSalesOrder;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.mod.ar.ArModSalesOrderDetails;
import com.util.reports.ar.ArRepDeliveryPrintDetails;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArRepDeliveryPrintControllerEJB")
public class ArRepDeliveryPrintControllerBean extends EJBContextClass implements ArRepDeliveryPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArDeliveryHome arDeliveryHome;
    @EJB
    private LocalArSalesOrderHome arSalesOrderHome;


    public ArrayList executeArRepDeliveryPrintAll(Integer SO_CODE, Integer AD_CMPNY) {

        Debug.print("ArDeliveryPrintControllerBean executeArDeliveryPrint");

        ArrayList list = new ArrayList();

        try {

            Collection arDeliveries = arDeliveryHome.findBySoCode(SO_CODE);

            for (Object delivery : arDeliveries) {

                LocalArDelivery arDelivery = (LocalArDelivery) delivery;

                ArRepDeliveryPrintDetails details = new ArRepDeliveryPrintDetails();

                details.setDpDvCode(arDelivery.getDvCode().toString());
                details.setDpDvContainer(arDelivery.getDvContainer());
                details.setDpDvDeliveryNumber(arDelivery.getDvDeliveryNumber());
                details.setDpDvBookingTime(arDelivery.getDvBookingTime());
                details.setDpDvTabsFeePullOut(arDelivery.getDvTabsFeePullOut());
                details.setDpDvReleasedDate(arDelivery.getDvReleasedDate());
                details.setDpDvDeliveredDate(arDelivery.getDvDeliveredDate());
                details.setDpDvDeliveredTo(arDelivery.getDvDeliveredTo());
                details.setDpDvPlateNo(arDelivery.getDvPlateNo());
                details.setDpDvDriverName(arDelivery.getDvDriverName());
                details.setDpDvEmptyReturnDate(arDelivery.getDvEmptyReturnDate());
                details.setDpDvEmptyReturnTo(arDelivery.getDvEmptyReturnTo());
                details.setDpDvTabsFeeReturn(arDelivery.getDvTabsFeeReturn());
                details.setDpDvStatus(arDelivery.getDvStatus());
                details.setDpDvRemarks(arDelivery.getDvRemarks());

                details.setDpCstCustomerCode(arDelivery.getArSalesOrder().getArCustomer().getCstCustomerCode());
                details.setDpSoDocumentNumber(arDelivery.getArSalesOrder().getSoDocumentNumber());
                details.setDpSoReferenceNumber(arDelivery.getArSalesOrder().getSoReferenceNumber());

                list.add(details);
            }

            return list;
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeArRepDeliveryByDvCode(Integer DV_CODE, Integer AD_CMPNY) {

        Debug.print("ArDeliveryPrintControllerBean executeArDeliveryPrint");

        ArrayList list = new ArrayList();

        try {

            LocalArDelivery arDelivery = arDeliveryHome.findByPrimaryKey(DV_CODE);

            ArRepDeliveryPrintDetails details = new ArRepDeliveryPrintDetails();

            details.setDpDvCode(arDelivery.getDvCode().toString());
            details.setDpDvContainer(arDelivery.getDvContainer());
            details.setDpDvDeliveryNumber(arDelivery.getDvDeliveryNumber());
            details.setDpDvBookingTime(arDelivery.getDvBookingTime());
            details.setDpDvTabsFeePullOut(arDelivery.getDvTabsFeePullOut());
            details.setDpDvReleasedDate(arDelivery.getDvReleasedDate());
            details.setDpDvDeliveredDate(arDelivery.getDvDeliveredDate());
            details.setDpDvDeliveredTo(arDelivery.getDvDeliveredTo());
            details.setDpDvPlateNo(arDelivery.getDvPlateNo());
            details.setDpDvDriverName(arDelivery.getDvDriverName());
            details.setDpDvEmptyReturnDate(arDelivery.getDvEmptyReturnDate());
            details.setDpDvEmptyReturnTo(arDelivery.getDvEmptyReturnTo());
            details.setDpDvTabsFeeReturn(arDelivery.getDvTabsFeeReturn());
            details.setDpDvStatus(arDelivery.getDvStatus());
            details.setDpDvRemarks(arDelivery.getDvRemarks());

            details.setDpCstCustomerCode(arDelivery.getArSalesOrder().getArCustomer().getCstCustomerCode());
            details.setDpSoDocumentNumber(arDelivery.getArSalesOrder().getSoDocumentNumber());
            details.setDpSoReferenceNumber(arDelivery.getArSalesOrder().getSoReferenceNumber());

            list.add(details);
            return list;
        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArModSalesOrderDetails getArSalesOrderBySoCode(Integer SO_CODE) {

        Debug.print("ArDeliveryPrintControllerBean getArSalesOrderBySoCode");

        try {

            LocalArSalesOrder arSalesOrder = arSalesOrderHome.findByPrimaryKey(SO_CODE);

            ArModSalesOrderDetails details = new ArModSalesOrderDetails();
            Debug.print("doc num:" + arSalesOrder.getSoDocumentNumber());
            details.setSoCode(arSalesOrder.getSoCode());
            details.setSoDocumentNumber(arSalesOrder.getSoDocumentNumber());
            details.setSoReferenceNumber(arSalesOrder.getSoReferenceNumber());
            details.setSoDate(arSalesOrder.getSoDate());
            details.setSoTransactionStatus(arSalesOrder.getSoTransactionStatus());
            details.setSoCstCustomerCode(arSalesOrder.getArCustomer().getCstCustomerCode());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArDeliveryPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpPhone(adCompany.getCmpPhone());
            details.setCmpFax(adCompany.getCmpFax());

            return details;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }
    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArDeliveryPrintControllerBean ejbCreate");
    }

}