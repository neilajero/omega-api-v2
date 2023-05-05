package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArJobOrderTypeHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArJobOrderType;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.ar.ArModJobOrderTypeDetails;
import com.util.Debug;
import com.util.EJBContextClass;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArJobOrderTypeControllerEJB")
public class ArJobOrderTypeControllerBean extends EJBContextClass implements ArJobOrderTypeController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalArJobOrderTypeHome arJobOrderTypeHome;

    public ArrayList getArJotAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArJobOrderTypeControllerBean getArJotAll");
        ArrayList list = new ArrayList();
        try {

            Collection arJobOrderTypes = arJobOrderTypeHome.findJotAll(AD_CMPNY);

            if (arJobOrderTypes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object jobOrderType : arJobOrderTypes) {

                LocalArJobOrderType arJobOrderType = (LocalArJobOrderType) jobOrderType;
                LocalGlChartOfAccount glJobOrderTypeAccount = null;

                ArModJobOrderTypeDetails mdetails = new ArModJobOrderTypeDetails();
                try {

                    glJobOrderTypeAccount = glChartOfAccountHome.findByPrimaryKey(arJobOrderType.getJotGlCoaJobOrderAccount() == null ? 0 : arJobOrderType.getJotGlCoaJobOrderAccount());

                } catch (FinderException ex) {

                }

                if (arJobOrderType.getJotGlCoaJobOrderAccount() != null) {

                    glJobOrderTypeAccount = glChartOfAccountHome.findByPrimaryKey(arJobOrderType.getJotGlCoaJobOrderAccount());
                    mdetails.setJotGlCoaJobOrderAccountNumber(glJobOrderTypeAccount.getCoaAccountNumber());

                    mdetails.setJotGlCoaJobOrderAccountDescription(glJobOrderTypeAccount.getCoaAccountDescription());
                }

                mdetails.setJotCode(arJobOrderType.getJotCode());
                mdetails.setJotName(arJobOrderType.getJotName());
                mdetails.setJotDocumentType(arJobOrderType.getJotDocumentType());
                mdetails.setJotReportType(arJobOrderType.getJotReportType());
                mdetails.setJotDescription(arJobOrderType.getJotDescription());

                mdetails.setJotEnable(arJobOrderType.getJotEnable());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addArJotEntry(ArModJobOrderTypeDetails mdetails, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArJobOrderTypeControllerBean addArJotEntry");
        try {

            LocalArJobOrderType arJobOrderType = null;

            LocalGlChartOfAccount glJobOrderTypeAccount = null;

            try {

                arJobOrderType = arJobOrderTypeHome.findByJotName(mdetails.getJotName(), AD_CMPNY);

                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            // get receivable and revenue account to validate accounts

            try {

                glJobOrderTypeAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getJotGlCoaJobOrderAccountNumber(), AD_CMPNY);

            } catch (FinderException ex) {

                //  throw new ArJOTCoaGlJobOrderAccountNotFoundException();

            }

            // create new customer class

            arJobOrderType = arJobOrderTypeHome.create(mdetails.getJotName(), mdetails.getJotDescription(), mdetails.getJotGlCoaJobOrderAccount(), mdetails.getJotEnable(), AD_CMPNY);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

            //    } catch (ArJOTCoaGlJobOrderAccountNotFoundException ex) {

            //  	getSessionContext().setRollbackOnly();
            //  	throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArJotEntry(ArModJobOrderTypeDetails mdetails, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {

        Debug.print("ArJobOrderTypeControllerBean updateArJoEntry");
        try {

            LocalArJobOrderType arJobOrderType = null;

            LocalGlChartOfAccount glJobOrderAccount = null;

            try {

                LocalArJobOrderType arExistingArJobOrderType = arJobOrderTypeHome.findByJotName(mdetails.getJotName(), AD_CMPNY);

                if (!arExistingArJobOrderType.getJotCode().equals(mdetails.getJotCode())) {

                    throw new GlobalRecordAlreadyExistException();
                }

            } catch (FinderException ex) {

            }

            // get receivable and revenue account to validate accounts

            try {

                glJobOrderAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getJotGlCoaJobOrderAccountNumber(), AD_CMPNY);

                arJobOrderType.setJotGlCoaJobOrderAccount(glJobOrderAccount.getCoaCode());

            } catch (FinderException ex) {

                //     throw new ArJOTCoaGlJobOrderAccountNotFoundException();

            }

            // find and update customer class
            Debug.print("--------------------------->");
            arJobOrderType = arJobOrderTypeHome.findByPrimaryKey(mdetails.getJotCode());

            arJobOrderType.setJotName(mdetails.getJotName());
            arJobOrderType.setJotDocumentType(mdetails.getJotDocumentType());
            arJobOrderType.setJotDescription(mdetails.getJotDescription());

            arJobOrderType.setJotEnable(mdetails.getJotEnable());
            arJobOrderType.setJotDocumentType(mdetails.getJotDocumentType());
            arJobOrderType.setJotReportType(mdetails.getJotReportType());

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

            //      } catch (ArJOTCoaGlJobOrderAccountNotFoundException ex) {

            //      	getSessionContext().setRollbackOnly();
            //      	throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArJotEntry(Integer JOT_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArJobOrderTypeControllerBean deleteArJotEntry");
        try {

            LocalArJobOrderType arJobOrderType = null;

            try {

                arJobOrderType = arJobOrderTypeHome.findByPrimaryKey(JOT_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            //	        arJobOrderType.entityRemove();
            em.remove(arJobOrderType);

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ArJobOrderTypeControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArJobOrderTypeControllerBean ejbCreate");
    }

}