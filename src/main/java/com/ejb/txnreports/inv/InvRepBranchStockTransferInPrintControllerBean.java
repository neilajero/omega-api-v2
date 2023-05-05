/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepBranchStockTransferInPrintControllerBean
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
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.dao.inv.LocalInvBranchStockTransferHome;
import com.ejb.entities.inv.LocalInvBranchStockTransferLine;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.reports.inv.InvRepBranchStockTransferInPrintDetails;

@Stateless(name = "InvRepBranchStockTransferInPrintControllerEJB")
public class InvRepBranchStockTransferInPrintControllerBean extends EJBContextClass implements InvRepBranchStockTransferInPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;

    public ArrayList executeInvRepBranchStockTransferInPrint(ArrayList bstCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepBranchStockTransferPrintInControllerBean executeInvRepBranchStockTransferInPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : bstCodeList) {

                Integer BST_CODE = (Integer) o;

                LocalInvBranchStockTransfer invBranchStockTransfer = null;

                try {

                    invBranchStockTransfer = invBranchStockTransferHome.findByPrimaryKey(BST_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get stock transfer lines

                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.findByBstCode(invBranchStockTransfer.getBstCode(), AD_CMPNY);

                for (Object branchStockTransferLine : invBranchStockTransferLines) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;

                    InvRepBranchStockTransferInPrintDetails details = new InvRepBranchStockTransferInPrintDetails();

                    details.setBstpBstType(invBranchStockTransfer.getBstType());
                    details.setBstpBstDate(invBranchStockTransfer.getBstDate());
                    details.setBstpBstNumber(invBranchStockTransfer.getBstNumber());
                    details.setBstpBstDescription(invBranchStockTransfer.getBstDescription());
                    details.setBstpBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());
                    details.setBstpBstTransitLocation(invBranchStockTransfer.getInvLocation().getLocName());
                    details.setBstpBstCreatedBy(invBranchStockTransfer.getBstCreatedBy());
                    details.setBstpBstApprovedRejectedBy(invBranchStockTransfer.getBstApprovedRejectedBy());
                    details.setBstpBstBranch(invBranchStockTransfer.getAdBranch().getBrName());
                    details.setBstpBslQuantity(invBranchStockTransferLine.getBslQuantity());
                    details.setBstpBslQuantityReceived(invBranchStockTransferLine.getBslQuantityReceived());
                    details.setBstpBslUnitOfMeasure(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName());
                    details.setBstpBslItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    details.setBstpBslItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setBstpBslItemLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setBstpBslUnitPrice(invBranchStockTransferLine.getBslUnitCost());
                    details.setBstpBslAmount(invBranchStockTransferLine.getBslAmount());
                    details.setBstpSlsPrc(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiSalesPrice());
                    details.setBstpBslMscItm(invBranchStockTransferLine.getBslMisc());

                    if (details.getBstpBstType().equalsIgnoreCase("IN")) list.add(details);
                }
            }

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

        Debug.print("InvRepBranchStockTransferInPrintControllerBean getAdCompany");

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