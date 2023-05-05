package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.dao.ad.LocalAdApprovalQueueHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvTag;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepPurchaseRequisitionPrintDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepPurchaseRequisitionPrintControllerEJB")
public class ApRepPurchaseRequisitionPrintControllerBean extends EJBContextClass implements ApRepPurchaseRequisitionPrintController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdApprovalQueueHome adApprovalQueueHome;
    @EJB
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;

    public ArrayList executeApRepPurchaseRequisitionPrint(ArrayList prCodeList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepPurchaseRequisitionPrintControllerBean executeApRepPurchaseRequisitionPrint");

        ArrayList list = new ArrayList();

        try {

            for (Object value : prCodeList) {

                Integer PR_CODE = (Integer) value;

                LocalApPurchaseRequisition apPurchaseRequisition = null;

                try {

                    apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(PR_CODE);

                } catch (FinderException ex) {

                    continue;
                }

                // get purchase requisition lines

                Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();

                Iterator prlIter = apPurchaseRequisitionLines.iterator();
                double grandTotal = 0;
                double totalPerLine;
                while (prlIter.hasNext()) {

                    LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) prlIter.next();

                    boolean isService = apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiAdLvCategory().startsWith("Service");

                    ApRepPurchaseRequisitionPrintDetails details = new ApRepPurchaseRequisitionPrintDetails();

                    if (isService) {
                        Debug.print(isService + " <== service ba?");
                        details.setPrpPrIsService(true);
                        Debug.print(details.getPrpPrIsService() + " <== details.getRrpPlIsService");
                    } else {
                        Debug.print(isService + " <== o hindi?");
                        details.setPrpPrIsService(false);
                        Debug.print(details.getPrpPrIsService() + " <== details.getRrpPlIsService");
                    }

                    details.setPrpPrNumber(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrNumber());
                    details.setPrpPrDate(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrDate());
                    details.setPrpPrDeliveryPeriod(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrDeliveryPeriod());
                    details.setPrpPrDescription(apPurchaseRequisitionLine.getApPurchaseRequisition().getPrDescription());
                    details.setPrpFcSymbol(apPurchaseRequisitionLine.getApPurchaseRequisition().getGlFunctionalCurrency().getFcSymbol());
                    details.setPrpPrlIiName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                    details.setPrpPrlIiDescription(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription());
                    details.setPrpPrlIiPartNumber(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiPartNumber());
                    details.setPrpPrlIiBarCode1(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiBarCode1());
                    details.setPrpPrlIiBarCode2(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiBarCode2());
                    details.setPrpPrlIiBarCode3(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiBarCode3());
                    details.setPrpPrlIiBrand(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiBrand());
                    details.setPrpPrlLocName(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                    details.setPrpPrlQuantity(apPurchaseRequisitionLine.getPrlQuantity());
                    details.setPrpPrlUomShortName(apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomShortName());
                    details.setPrpPrlAmount(apPurchaseRequisitionLine.getPrlAmount());
                    details.setPrpPrlRemarks(apPurchaseRequisitionLine.getPrlRemarks());
                    details.setPrpPrlApprovedBy(apPurchaseRequisition.getPrApprovedRejectedBy());
                    details.setPrpPrCreatedBy(apPurchaseRequisition.getPrCreatedBy());
                    details.setPrpPrlUnitCost(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiUnitCost());


                    totalPerLine = apPurchaseRequisitionLine.getPrlQuantity() * apPurchaseRequisitionLine.getPrlAmount();
                    grandTotal += totalPerLine;
                    details.setPrpPrlGrandTotalAmount(grandTotal);
                    details.setPrpPrPosted(apPurchaseRequisition.getPrPosted());
          
                      /*
                       * OLD CODE FOR GETTING DEPARTMENT
                      try {
                        details.setPrpPrDepartment(
                            adUserHome
                                .findByUsrName(apPurchaseRequisition.getPrCreatedBy(), AD_CMPNY)
                                .getUsrDept());
                      } catch (FinderException ex) {
                        throw new EJBException(ex.getMessage());
                      }
                       */

                    //NEW CODE
                    details.setPrpPrDepartment(apPurchaseRequisition.getPrDepartment());

                    // trace misc
                    if (apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiTraceMisc() == EJBCommon.TRUE) {

                        if (apPurchaseRequisitionLine.getInvTags().size() > 0) {
                            Debug.print("new code");
                            StringBuilder strBProperty = new StringBuilder();
                            StringBuilder strBSerial = new StringBuilder();
                            StringBuilder strBSpecs = new StringBuilder();
                            StringBuilder strBCustodian = new StringBuilder();
                            StringBuilder strBExpirationDate = new StringBuilder();

                            for (Object o : apPurchaseRequisitionLine.getInvTags()) {

                                LocalInvTag invTag = (LocalInvTag) o;

                                // property code
                                if (!invTag.getTgPropertyCode().equals("")) {
                                    strBProperty.append(invTag.getTgPropertyCode());
                                    strBProperty.append(System.getProperty("line.separator"));
                                }

                                // serial

                                if (!invTag.getTgSerialNumber().equals("")) {
                                    strBSerial.append(invTag.getTgSerialNumber());
                                    strBSerial.append(System.getProperty("line.separator"));
                                }

                                // spec

                                if (!invTag.getTgSpecs().equals("")) {
                                    strBSpecs.append(invTag.getTgSpecs());
                                    strBSpecs.append(System.getProperty("line.separator"));
                                }

                                // custodian

                                if (invTag.getAdUser() != null) {
                                    strBCustodian.append(invTag.getAdUser().getUsrName());
                                    strBCustodian.append(System.getProperty("line.separator"));
                                }

                                // exp date

                                if (invTag.getTgExpiryDate() != null) {
                                    strBExpirationDate.append(invTag.getTgExpiryDate());
                                    strBExpirationDate.append(System.getProperty("line.separator"));
                                }
                            }
                            // property code
                            details.setPrpPrlPropertyCode(strBProperty.toString());
                            // serial number
                            details.setPrpPrlSerialNumber(strBSerial.toString());
                            // specs
                            details.setPrpPrlSpecs(strBSpecs.toString());
                            // custodian
                            details.setPrpPrlCustodian(strBCustodian.toString());
                            // expiration date
                            details.setPrpPrlExpiryDate(strBExpirationDate.toString());
                        }
                    }

                    //get approver list that approved the document
                    StringBuilder strBldr = new StringBuilder();
                    Collection adApprovalQueues = adApprovalQueueHome.findAllByAqDocumentAndAqDocumentCode("AP PURCHASE REQUISITION", apPurchaseRequisitionLine.getApPurchaseRequisition().getPrCode(), AD_CMPNY);

                    for (Object approvalQueue : adApprovalQueues) {

                        LocalAdApprovalQueue adApprovalQueue = (LocalAdApprovalQueue) approvalQueue;

                        if (adApprovalQueue.getAqApproved() == EJBCommon.FALSE) continue;

                        strBldr.append(adApprovalQueue.getAdUser().getUsrName());
                        strBldr.append(System.getProperty("line.separator"));


                    }

                    details.setPrpPrApprovedBy(strBldr.toString());


                    list.add(details);
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

        Debug.print("ApRepPurchaseRequisitionPrintControllerBean getAdCompany");

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
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepPurchaseRequisitionPrintControllerBean ejbCreate");
    }
}