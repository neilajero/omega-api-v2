/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepStockTransferPrintControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.entities.inv.LocalInvStockTransfer;
import com.ejb.dao.inv.LocalInvStockTransferHome;
import com.ejb.entities.inv.LocalInvStockTransferLine;
import com.ejb.dao.inv.LocalInvStockTransferLineHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.inv.InvRepStockTransferPrintDetails;

import javax.naming.NamingException;

@Stateless(name = "InvRepStockTransferPrintControllerEJB")
public class InvRepStockTransferPrintControllerBean extends EJBContextClass implements InvRepStockTransferPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    LocalInvStockTransferHome invStockTransferHome;
    @EJB
    LocalInvStockTransferLineHome invStockTransferLineHome;

    public ArrayList executeInvRepStockTransferPrint(ArrayList stCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepStockTransferPrintControllerBean executeInvRepStockTransferPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : stCodeList) {

                Integer ST_CODE = (Integer) o;

                LocalInvStockTransfer invStockTransfer = null;

                try {

                    invStockTransfer = invStockTransferHome.findByPrimaryKey(ST_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get stock transfer lines

                Collection invStockTransferLines = invStockTransferLineHome.findByStCode(invStockTransfer.getStCode(), AD_CMPNY);

                for (Object stockTransferLine : invStockTransferLines) {

                    LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) stockTransferLine;

                    InvRepStockTransferPrintDetails details = new InvRepStockTransferPrintDetails();

                    details.setStpStDate(invStockTransfer.getStDate());
                    details.setStpStDocumentNumber(invStockTransfer.getStDocumentNumber());
                    details.setStpStReferenceNumber(invStockTransfer.getStReferenceNumber());
                    details.setStpStCreatedBy(invStockTransfer.getStCreatedBy());
                    details.setStpStApprovedRejectedBy(invStockTransfer.getStApprovedRejectedBy());
                    details.setStpStDescription(invStockTransfer.getStDescription());
                    details.setStpStlLocationTo(this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationTo()));
                    details.setStpStlQuantityDelivered(invStockTransferLine.getStlQuantityDelivered());
                    details.setStpStlUnit(invStockTransferLine.getInvUnitOfMeasure().getUomName());
                    details.setStpStlItemName(invStockTransferLine.getInvItem().getIiName());
                    details.setStpStlItemDescription(invStockTransferLine.getInvItem().getIiDescription());
                    details.setStpStlLocationFrom(this.getInvLocNameByLocCode(invStockTransferLine.getStlLocationFrom()));
                    details.setStpStlUnitPrice(invStockTransferLine.getStlUnitCost());
                    details.setStpStlAmount(invStockTransferLine.getStlAmount());
                    details.setStpStlItemCategory(invStockTransferLine.getInvItem().getIiAdLvCategory());

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            // sort list by locationTo

            list.sort(InvRepStockTransferPrintDetails.LocationToComparator);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepStockTransferPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private String getInvLocNameByLocCode(Integer LOC_CODE) {

        Debug.print("InvRepStockTransferPrintControllerBean getInvLocNameByLocCode");

        LocalInvLocationHome invLocationHome = null;

        try {

            invLocationHome = (LocalInvLocationHome) EJBHomeFactory.lookUpLocalHome(LocalInvLocationHome.JNDI_NAME, LocalInvLocationHome.class);

        } catch (NamingException ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            return invLocationHome.findByPrimaryKey(LOC_CODE).getLocName();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("InvRepStockTransferPrintControllerBean ejbCreate");
    }
}