/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvLineItemTemplateControllerBean
 * @created October 30, 2005, 10:10 AM
 * @author Jolly Martin
 */
package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.*;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.inv.InvLineItemTemplateDetails;
import com.util.mod.inv.InvModLineItemDetails;
import com.util.mod.inv.InvModUnitOfMeasureDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "InvLineItemTemplateControllerEJB")
public class InvLineItemTemplateControllerBean extends EJBContextClass implements InvLineItemTemplateController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvLineItemTemplateHome invLineItemTemplateHome;
    @EJB
    private LocalInvLineItemHome invLineItemHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalInvItemHome invItemHome;

    public void addInvLitEntry(InvLineItemTemplateDetails details, ArrayList liList, Integer AD_CMPNY)
            throws GlobalRecordAlreadyExistException, GlobalInvItemLocationNotFoundException {

        Debug.print("InvLineItemTemplateControllerBean addInvLitEntry");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            // validate if template already exists

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByLitName(details.getLitName(), AD_CMPNY);

                if (details.getLitCode() == null || details.getLitCode() != null && !invLineItemTemplate.getLitCode().equals(details.getLitCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            }
            catch (GlobalRecordAlreadyExistException ex) {

                throw ex;

            }
            catch (FinderException ex) {

            }

            invLineItemTemplate = invLineItemTemplateHome.create(details.getLitCode(), details.getLitName(), details.getLitDescription(), AD_CMPNY);

            // add line items

            for (Object o : liList) {

                InvModLineItemDetails mdetails = (InvModLineItemDetails) o;

                LocalInvLineItem invLineItem = invLineItemHome.create(mdetails.getLiLine(), AD_CMPNY);

                invLineItemTemplate.addInvLineItem(invLineItem);

                LocalInvItemLocation invItemLocation = null;

                try {

                    invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getLiLocName(), mdetails.getLiIiName(), AD_CMPNY);

                }
                catch (FinderException ex) {

                    throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getLiLine()));
                }

                invItemLocation.addInvLineItem(invLineItem);

                LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getLiUomName(), AD_CMPNY);
                invUnitOfMeasure.addInvLineItem(invLineItem);
            }

        }
        catch (GlobalRecordAlreadyExistException | GlobalInvItemLocationNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateInvLitEntry(InvLineItemTemplateDetails details, ArrayList liList, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalInvItemLocationNotFoundException {

        Debug.print("InvLineItemTemplateControllerBean updateInvLitEntry");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            // validate if template already deleted

            try {

                if (details.getLitCode() != null) {

                    invLineItemTemplate = invLineItemTemplateHome.findByPrimaryKey(details.getLitCode());
                }

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            boolean isRecalculate = true;

            // update line item template

            invLineItemTemplate = invLineItemTemplateHome.findByPrimaryKey(details.getLitCode());

            invLineItemTemplate.setLitName(details.getLitName());
            invLineItemTemplate.setLitDescription(details.getLitDescription());

            // check if critical fields are changed

            if (liList.size() != invLineItemTemplate.getInvLineItems().size()) {

                isRecalculate = true;

            } else if (liList.size() == invLineItemTemplate.getInvLineItems().size()) {

                Iterator liIter = invLineItemTemplate.getInvLineItems().iterator();
                Iterator liListIter = liList.iterator();

                while (liIter.hasNext()) {

                    LocalInvLineItem invLineItem = (LocalInvLineItem) liIter.next();
                    InvModLineItemDetails mdetails = (InvModLineItemDetails) liListIter.next();

                    if (!invLineItem.getInvItemLocation().getInvItem().getIiName().equals(mdetails.getLiIiName()) || !invLineItem.getInvItemLocation().getInvLocation().getLocName().equals(mdetails.getLiLocName()) || !invLineItem.getInvUnitOfMeasure().getUomName().equals(mdetails.getLiUomName())) {

                        isRecalculate = true;
                        break;
                    }

                    isRecalculate = false;
                }
            }

            if (isRecalculate) {

                // remove all line items

                Collection invLineItems = invLineItemTemplate.getInvLineItems();

                Iterator i = invLineItems.iterator();

                while (i.hasNext()) {

                    LocalInvLineItem invLineItem = (LocalInvLineItem) i.next();

                    i.remove();

                    //					invLineItem.entityRemove();
                    em.remove(invLineItem);
                }

                // add line items

                i = liList.iterator();

                while (i.hasNext()) {

                    InvModLineItemDetails mdetails = (InvModLineItemDetails) i.next();

                    LocalInvLineItem invLineItem = invLineItemHome.create(mdetails.getLiLine(), AD_CMPNY);

                    invLineItemTemplate.addInvLineItem(invLineItem);

                    LocalInvItemLocation invItemLocation = null;

                    try {

                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(mdetails.getLiLocName(), mdetails.getLiIiName(), AD_CMPNY);

                    }
                    catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException(String.valueOf(mdetails.getLiLine()));
                    }

                    invItemLocation.addInvLineItem(invLineItem);

                    LocalInvUnitOfMeasure invUnitOfMeasure = invUnitOfMeasureHome.findByUomName(mdetails.getLiUomName(), AD_CMPNY);
                    invUnitOfMeasure.addInvLineItem(invLineItem);
                }
            }

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalInvItemLocationNotFoundException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public InvLineItemTemplateDetails getInvLitByLitCode(Integer LIT_CODE, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvLineItemTemplateControllerBean getInvLitByLitCode");
        LocalInvLineItemTemplate invLineItemTemplate;
        try {

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByPrimaryKey(LIT_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            InvLineItemTemplateDetails litDetails = new InvLineItemTemplateDetails();

            litDetails.setLitName(invLineItemTemplate.getLitName());
            litDetails.setLitDescription(invLineItemTemplate.getLitDescription());

            return litDetails;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteInvLitEntry(Integer LIT_CODE, Integer AD_CMPNY)
            throws GlobalRecordAlreadyDeletedException, GlobalRecordAlreadyAssignedException {

        Debug.print("InvLineItemTemplateControllerBean deleteInvLitEntry");
        try {

            LocalInvLineItemTemplate invLineItemTemplate = null;

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByPrimaryKey(LIT_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (!invLineItemTemplate.getArCustomers().isEmpty() || !invLineItemTemplate.getApSuppliers().isEmpty()) {

                throw new GlobalRecordAlreadyAssignedException();
            }

            Collection invLineItems = invLineItemTemplate.getInvLineItems();

            Iterator i = invLineItems.iterator();

            while (i.hasNext()) {

                LocalInvLineItem invLineItem = (LocalInvLineItem) i.next();

                i.remove();

                //				invLineItem.entityRemove();
                em.remove(invLineItem);
            }

            //			invLineItemTemplate.entityRemove();
            em.remove(invLineItemTemplate);

        }
        catch (GlobalRecordAlreadyDeletedException | GlobalRecordAlreadyAssignedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLitAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvLineItemTemplateControllerBean getInvLitAll");
        ArrayList litList = new ArrayList();
        try {

            Collection invLineItemTemplates = invLineItemTemplateHome.findLitAll(AD_CMPNY);

            if (invLineItemTemplates.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object lineItemTemplate : invLineItemTemplates) {

                LocalInvLineItemTemplate invLineItemTemplate = (LocalInvLineItemTemplate) lineItemTemplate;

                InvLineItemTemplateDetails mdetails = new InvLineItemTemplateDetails();

                mdetails.setLitCode(invLineItemTemplate.getLitCode());
                mdetails.setLitName(invLineItemTemplate.getLitName());
                mdetails.setLitDescription(invLineItemTemplate.getLitDescription());

                litList.add(mdetails);
            }

            return litList;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLiAllByLitCode(Integer LIT_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvLineItemTemplateControllerBean getInvLiAllByLitCode");
        LocalInvLineItemTemplate invLineItemTemplate;
        try {

            try {

                invLineItemTemplate = invLineItemTemplateHome.findByPrimaryKey(LIT_CODE);

            }
            catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArrayList list = new ArrayList();

            // get line items if any

            Collection invLineItems = invLineItemTemplate.getInvLineItems();

            if (!invLineItems.isEmpty()) {

                for (Object lineItem : invLineItems) {

                    LocalInvLineItem invLineItem = (LocalInvLineItem) lineItem;

                    InvModLineItemDetails liDetails = new InvModLineItemDetails();

                    liDetails.setLiCode(invLineItem.getLiCode());
                    liDetails.setLiLine(invLineItem.getLiLine());
                    liDetails.setLiIiName(invLineItem.getInvItemLocation().getInvItem().getIiName());
                    liDetails.setLiLocName(invLineItem.getInvItemLocation().getInvLocation().getLocName());
                    liDetails.setLiUomName(invLineItem.getInvUnitOfMeasure().getUomName());
                    liDetails.setLiIiDescription(invLineItem.getInvItemLocation().getInvItem().getIiDescription());

                    list.add(liDetails);
                }
            }

            return list;

        }
        catch (GlobalNoRecordFoundException ex) {

            throw ex;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvUomByIiName(String II_NM, Integer AD_CMPNY) {

        Debug.print("InvLineItemTemplateControllerBean getInvUomByIiName");
        ArrayList list = new ArrayList();
        try {

            LocalInvItem invItem = null;
            LocalInvUnitOfMeasure invItemUnitOfMeasure = null;

            invItem = invItemHome.findByIiName(II_NM, AD_CMPNY);
            invItemUnitOfMeasure = invItem.getInvUnitOfMeasure();

            Collection invUnitOfMeasures = null;

            for (Object o : invUnitOfMeasureHome.findByUomAdLvClass(invItemUnitOfMeasure.getUomAdLvClass(), AD_CMPNY)) {

                LocalInvUnitOfMeasure invUnitOfMeasure = (LocalInvUnitOfMeasure) o;
                InvModUnitOfMeasureDetails details = new InvModUnitOfMeasureDetails();

                details.setUomName(invUnitOfMeasure.getUomName());

                if (invUnitOfMeasure.getUomName().equals(invItemUnitOfMeasure.getUomName())) {

                    details.setDefault(true);
                }

                list.add(details);
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getAdPrfInvJournalLineNumber(Integer AD_CMPNY) {

        Debug.print("InvLineItemTemplateControllerBean getAdPrfInvJournalLineNumber");
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfInvInventoryLineNumber();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void ejbCreate() throws CreateException {

        Debug.print("InvLineItemTemplateControllerBean ejbCreate");
    }

}