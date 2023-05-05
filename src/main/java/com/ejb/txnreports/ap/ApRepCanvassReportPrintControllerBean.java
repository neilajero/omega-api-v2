package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepCanvassReportPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepCanvassReportPrintControllerEJB")
public class ApRepCanvassReportPrintControllerBean extends EJBContextClass implements ApRepCanvassReportPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome = null;

    public void selectApRepCanvassPerTotal(ArrayList prCodeList, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("ApRepCanvassReportPrintControllerBean selectApRepCanvassPerTotal");

        ArrayList list = new ArrayList();

        try {

            for (Object item : prCodeList) {

                Integer PR_CODE = (Integer) item;

                LocalApPurchaseRequisition apPurchaseRequisition = null;

                try {

                    apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                java.util.HashMap hm = new java.util.HashMap();

                // get purchase requisition lines

                Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;
                    Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();
                    for (Object canvass : apCanvasses) {

                        LocalApCanvass apCanvass = (LocalApCanvass) canvass;
                        Double cnvAmount = (Double) hm.get(apCanvass.getApSupplier().getSplCode());
                        double dAmount = 0;
                        if (cnvAmount != null) dAmount = cnvAmount;
                        hm.put(apCanvass.getApSupplier().getSplCode(), dAmount + apCanvass.getCnvAmount());
                    }
                }

                Integer currentSupplier = null;
                java.util.Set hmList = hm.keySet();
                for (Object o : hmList) {
                    Integer supplierCode = (Integer) o;
                    if (currentSupplier == null) currentSupplier = supplierCode;
                    else {
                        Double currentValue = (Double) hm.get(currentSupplier);
                        Double value = (Double) hm.get(supplierCode);
                        if (value < currentValue) currentSupplier = supplierCode;
                    }
                }

                if (currentSupplier != null) {

                    for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {

                        LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;
                        Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();
                        for (Object canvass : apCanvasses) {

                            LocalApCanvass apCanvass = (LocalApCanvass) canvass;
                            if (apCanvass.getApSupplier().getSplCode().equals(currentSupplier)) {
                                apCanvass.setCnvPo(EJBCommon.TRUE);
                            } else {
                                apCanvass.setCnvPo(EJBCommon.FALSE);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeApRepCanvassReportPrint(ArrayList prCodeList, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepCanvassReportPrintControllerBean executeApRepCanvassReportPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object o : prCodeList) {

                Integer PR_CODE = (Integer) o;

                LocalApPurchaseRequisition apPurchaseRequisition = null;

                try {

                    apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get purchase requisition lines

                Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;

                    // get last delivery date of item

                    boolean first = true;
                    boolean asteriskSet = false;

                    Date LST_DLVRY_DT = null;

                    Collection apPurchaseOrderLines = null;

                    try {

                        apPurchaseOrderLines = apPurchaseOrderLineHome.findByPlIlCodeAndPoReceivingAndPoPostedAndBrCode(apPurchaseRequisitionLine.getInvItemLocation().getIlCode(), EJBCommon.TRUE, EJBCommon.TRUE, AD_BRNCH, AD_CMPNY);

                        if (!apPurchaseOrderLines.isEmpty() && apPurchaseOrderLines.size() > 0) {

                            LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) apPurchaseOrderLines.iterator().next();

                            LST_DLVRY_DT = apPurchaseOrderLine.getApPurchaseOrder().getPoDate();
                        }

                    } catch (FinderException ex) {

                    }

                    Collection apCanvasses = apPurchaseRequisitionLine.getApCanvasses();

                    for (Object canvass : apCanvasses) {

                        LocalApCanvass apCanvass = (LocalApCanvass) canvass;

                        ApRepCanvassReportPrintDetails details = new ApRepCanvassReportPrintDetails();

                        // pr details
                        details.setCrpPrNumber(apCanvass.getApPurchaseRequisitionLine().getApPurchaseRequisition().getPrNumber());
                        details.setCrpPrDate(apCanvass.getApPurchaseRequisitionLine().getApPurchaseRequisition().getPrDate());
                        details.setCrpPrDescription(apCanvass.getApPurchaseRequisitionLine().getApPurchaseRequisition().getPrDescription());
                        details.setCrpPrFcSymbol(apCanvass.getApPurchaseRequisitionLine().getApPurchaseRequisition().getGlFunctionalCurrency().getFcSymbol());

                        // pr line details
                        details.setCrpPrlIiName(apCanvass.getApPurchaseRequisitionLine().getInvItemLocation().getInvItem().getIiName());
                        details.setCrpPrlIiDescription(apCanvass.getApPurchaseRequisitionLine().getInvItemLocation().getInvItem().getIiDescription());
                        details.setCrpPrlLocName(apCanvass.getApPurchaseRequisitionLine().getInvItemLocation().getInvLocation().getLocName());
                        details.setCrpPrlUomName(apCanvass.getApPurchaseRequisitionLine().getInvUnitOfMeasure().getUomName());

                        if (first) {

                            if (LST_DLVRY_DT != null) {

                                details.setCrpPrlLastDeliveryDate(EJBCommon.convertSQLDateToString(LST_DLVRY_DT));

                            } else {

                                details.setCrpPrlLastDeliveryDate("** ** ****");
                                asteriskSet = true;
                            }

                            first = false;

                        } else {

                            if (!asteriskSet) {

                                details.setCrpPrlLastDeliveryDate("** ** ****");
                                asteriskSet = true;
                            }
                        }

                        // canvass details
                        details.setCrpPrlCnvRemarks(apCanvass.getCnvRemarks());
                        details.setCrpPrlCnvSplSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());
                        details.setCrpPrlCnvSplName(apCanvass.getApSupplier().getSplName());
                        details.setCrpPrlCnvQuantity(apCanvass.getCnvQuantity());
                        details.setCrpPrlCnvUnitPrice(apCanvass.getCnvUnitCost());
                        details.setCrpPrlCnvAmount(apCanvass.getCnvAmount());
                        details.setCrpPrlPO(apCanvass.getCnvPo());

                        list.add(details);
                    }
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

        Debug.print("ApRepCanvassReportPrintControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepCanvassReportPrintControllerBean ejbCreate");
    }
}