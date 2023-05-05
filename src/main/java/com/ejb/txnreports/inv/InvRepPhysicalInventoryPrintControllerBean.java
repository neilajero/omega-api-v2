/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepPhysicalInventoryPrintControllerBean
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
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.inv.LocalInvPhysicalInventory;
import com.ejb.dao.inv.LocalInvPhysicalInventoryHome;
import com.ejb.entities.inv.LocalInvPhysicalInventoryLine;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.inv.InvRepPhysicalInventoryPrintDetails;

@Stateless(name = "InvRepPhysicalInventoryPrintControllerEJB")
public class InvRepPhysicalInventoryPrintControllerBean extends EJBContextClass implements InvRepPhysicalInventoryPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalInvCostingHome invCostingHome;
    @EJB
    private LocalInvPhysicalInventoryHome invPhysicalInventoryHome;


    public ArrayList executeInvRepPhysicalInventoryPrint(ArrayList piCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepPhysicalInventoryPrintControllerBean executeInvRepPhysicalInventoryPrint");

        ArrayList list = new ArrayList();


        try {

            for (Object o : piCodeList) {

                Integer PI_CODE = (Integer) o;

                LocalInvPhysicalInventory invPhysicalInventory = null;

                try {

                    invPhysicalInventory = invPhysicalInventoryHome.findByPrimaryKey(PI_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get adjustment lines

                Collection invPhysicalInventoryLines = invPhysicalInventory.getInvPhysicalInventoryLines();

                for (Object physicalInventoryLine : invPhysicalInventoryLines) {

                    LocalInvPhysicalInventoryLine invPhysicalInventoryLine = (LocalInvPhysicalInventoryLine) physicalInventoryLine;

                    InvRepPhysicalInventoryPrintDetails details = new InvRepPhysicalInventoryPrintDetails();

                    details.setPipPiDate(invPhysicalInventory.getPiDate());
                    details.setPipPiReferenceNumber(invPhysicalInventory.getPiReferenceNumber());
                    details.setPipPiDescription(invPhysicalInventory.getPiDescription());

                    LocalAdUser adUser = adUserHome.findByUsrName(invPhysicalInventory.getPiCreatedBy(), AD_CMPNY);
                    details.setPipPiCreatedBy(adUser.getUsrDescription());

                    details.setPipPlIiName(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiName());
                    details.setPipPlIiDescription(invPhysicalInventoryLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setPipPlLocName(invPhysicalInventoryLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setPipPlUomName(invPhysicalInventoryLine.getInvUnitOfMeasure().getUomName());
                    details.setPipPlEndingInventory(invPhysicalInventoryLine.getPilEndingInventory());
                    details.setPipPlWastage(invPhysicalInventoryLine.getPilWastage());
                    details.setPipPlVariance(invPhysicalInventoryLine.getPilVariance());

                    LocalInvCosting invLastCosting = null;

                    double COST = 0d;

                    try {

                        invLastCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIlCode(invPhysicalInventory.getPiDate(), invPhysicalInventoryLine.getInvItemLocation().getIlCode(), invPhysicalInventory.getPiAdBranch(), AD_CMPNY);
                        COST = Math.abs(invLastCosting.getCstRemainingValue() / invLastCosting.getCstRemainingQuantity());

                    } catch (Exception e) {
                        COST = 0;
                    }

                    details.setPipPlUnitCost(COST);

                    list.add(details);
                }
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            list.sort(InvRepPhysicalInventoryPrintDetails.ItemComparator);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepPhysicalInventoryPrintControllerBean getAdCompany");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("InvRepPhysicalInventoryPrintControllerBean ejbCreate");
    }
}