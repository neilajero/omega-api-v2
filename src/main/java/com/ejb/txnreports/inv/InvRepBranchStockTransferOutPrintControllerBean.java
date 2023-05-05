/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepBranchStockTransferOutPrintControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.entities.inv.LocalInvBranchStockTransferLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepBranchStockTransferOutPrintDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "InvRepBranchStockTransferOutPrintControllerEJB")
public class InvRepBranchStockTransferOutPrintControllerBean extends EJBContextClass implements InvRepBranchStockTransferOutPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;

    public ArrayList executeInvRepBranchStockTransferOutPrint(ArrayList bstCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepBranchStockTransferPrintOutControllerBean executeInvRepBranchStockTransferOutPrint");
        ArrayList list = new ArrayList();
        try {

            for (Object o : bstCodeList) {

                Integer BST_CODE = (Integer) o;

                LocalInvBranchStockTransfer invBranchStockTransfer = null;
                LocalInvBranchStockTransfer invBranchStockTransferOrder = null;

                try {

                    invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);
                    Debug.print("invBranchStockTransfer.getBstTransferOrderNumber() = " + invBranchStockTransfer.getBstTransferOrderNumber() + " pk = " + BST_CODE);

                } catch (FinderException ex) {
                    Debug.print("catch 1");
                }
                try {

                    invBranchStockTransferOrder = invBranchStockTransferHome.findByBstNumberAndAdCompany(invBranchStockTransfer.getBstTransferOrderNumber(), AD_CMPNY);
                    Debug.print("2");
                } catch (Exception e) {
                    Debug.print("catch 2");
                }

                Debug.print("--------------->");
                // get stock transfer lines
                Collection invBranchStockTransferLines = null;
                Collection invBranchStockTransferOrderLines = null;

                try {
                    invBranchStockTransferLines = invBranchStockTransferLineHome.findByBstCode(invBranchStockTransfer.getBstCode(), AD_CMPNY);
                    Debug.print("invBranchStockTransfer.getBstCode()=" + invBranchStockTransfer.getBstCode());

                } catch (Exception e) {
                    Debug.print("catch 3");
                }
                try {
                    invBranchStockTransferOrderLines = invBranchStockTransferLineHome.findByBstCode(invBranchStockTransferOrder.getBstCode(), AD_CMPNY);

                } catch (Exception e) {
                    Debug.print("catch 4");
                }

                Iterator bslIter = null;
                Iterator bslIter2 = null;

                try {
                    bslIter = invBranchStockTransferLines.iterator();
                } catch (Exception e) {
                    // TODO: handle exception
                }

                try {
                    bslIter2 = invBranchStockTransferOrderLines.iterator();
                } catch (Exception e) {
                    // TODO: handle exception
                }

                Debug.print("3----------invBranchStockTransferLines=" + invBranchStockTransferLines.size());
                while (bslIter.hasNext()) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = null;
                    LocalInvBranchStockTransferLine invBranchStockTransferOrderLine = null;

                    try {
                        invBranchStockTransferLine = (LocalInvBranchStockTransferLine) bslIter.next();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    try {
                        invBranchStockTransferOrderLine = (LocalInvBranchStockTransferLine) bslIter2.next();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    InvRepBranchStockTransferOutPrintDetails details = new InvRepBranchStockTransferOutPrintDetails();

                    details.setBstpBstType(invBranchStockTransfer.getBstType());
                    details.setBstpBstDate(invBranchStockTransfer.getBstDate());
                    details.setBstpBstNumber(invBranchStockTransfer.getBstNumber());
                    details.setBstpBstDescription(invBranchStockTransfer.getBstDescription());
                    details.setBstpBstCreatedBy(invBranchStockTransfer.getBstCreatedBy());
                    details.setBstpBstApprovedRejectedBy(invBranchStockTransfer.getBstApprovedRejectedBy());
                    details.setBstpBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());
                    details.setBstpBstBranch(invBranchStockTransfer.getAdBranch().getBrName());
                    details.setBstpBslQuantity(invBranchStockTransferLine.getBslQuantity());
                    try {
                        details.setBstpBslOrderedQuantity(invBranchStockTransferOrderLine.getBslQuantity());
                    } catch (Exception e) {
                        details.setBstpBslOrderedQuantity(0d);
                    }

                    details.setBstpBslUnitOfMeasure(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName());
                    details.setBstpBslItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    details.setBstpBslItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setBstpBslUnitPrice(invBranchStockTransferLine.getBslUnitCost());
                    details.setBstpBslAmount(invBranchStockTransferLine.getBslAmount());
                    details.setBstpSlsPrc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiSalesPrice());
                    details.setBstpBslMscItm(invBranchStockTransferLine.getBslMisc());

                    Debug.print("4");
                    if (details.getBstpBstType().equalsIgnoreCase("OUT")) {
                        list.add(details);
                    }
                }
            }
            Debug.print("5 -----------LIST" + list.size());
            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepBranchStockTransferPrintControllerBean getAdCompany");
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

        Debug.print("InvRepBranchStockTransferPrintControllerBean ejbCreate");
    }

}