package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ar.LocalArPersonelHome;
import com.ejb.dao.ar.LocalArPersonelTypeHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArPersonel;
import com.ejb.entities.ar.LocalArPersonelType;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.*;
import com.util.ar.ArPersonelTypeDetails;
import com.util.mod.ar.ArModPersonelDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArPersonelControllerEJB")
public class ArPersonelControlleBean extends EJBContextClass implements ArPersonelController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArPersonelHome arPersonelHome;
    @EJB
    private LocalArPersonelTypeHome arPersonelTypeHome;

    public ArModPersonelDetails getArPeByPeCode(Integer PE_CODE) throws GlobalNoRecordFoundException {
        Debug.print("ArPersonelControlleBean getArPrlAll");
        LocalArPersonel arPersonel = null;
        try {

            try {
                arPersonel = arPersonelHome.findByPrimaryKey(PE_CODE);
            } catch (FinderException ex) {
                throw new GlobalNoRecordFoundException();
            }

            ArModPersonelDetails details = new ArModPersonelDetails();

            details.setPeCode(arPersonel.getPeCode());
            details.setPeIdNumber(arPersonel.getPeIdNumber());
            details.setPeName(arPersonel.getPeName());
            details.setPeDescription(arPersonel.getPeDescription());
            details.setPeAddress(arPersonel.getPeAddress());
            details.setPePtName(arPersonel.getArPersonelType().getPtName());
            details.setPePtShortName(arPersonel.getArPersonelType().getPtShortName());
            details.setPeRate(arPersonel.getPeRate());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArPeAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArPersonelControlleBean getArPrlAll");
        ArrayList list = new ArrayList();
        try {

            Collection arSalespersons = arPersonelHome.findPeAll(AD_CMPNY);

            if (arSalespersons.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object arSalesperson : arSalespersons) {

                LocalArPersonel arPersonel = (LocalArPersonel) arSalesperson;

                ArModPersonelDetails details = new ArModPersonelDetails();

                details.setPeCode(arPersonel.getPeCode());
                details.setPeIdNumber(arPersonel.getPeIdNumber());

                details.setPeName(arPersonel.getPeName());
                details.setPeDescription(arPersonel.getPeDescription());
                details.setPeAddress(arPersonel.getPeAddress());
                details.setPePtName(arPersonel.getArPersonelType().getPtName());
                details.setPeAdCompany(AD_CMPNY);
                details.setPeRate(arPersonel.getPeRate());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void addArPeEntry(ArModPersonelDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {
        Debug.print("ArPersonelController addArPeEntry");
        try {

            LocalArPersonel arPersonel = null;

            try {

                arPersonel = arPersonelHome.findByPeIdNumber(details.getPeIdNumber(), AD_CMPNY);
                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            try {

                arPersonel = arPersonelHome.findByPeName(details.getPeName(), AD_CMPNY);
                throw new GlobalRecordAlreadyExistException();

            } catch (FinderException ex) {

            }

            arPersonel = arPersonelHome.create(details.getPeIdNumber(), details.getPeName(), details.getPeDescription(), details.getPeAddress(), details.getPeRate(), AD_CMPNY);

            LocalArPersonelType arPersonelType = arPersonelTypeHome.findByPtName(details.getPePtName(), AD_CMPNY);

            arPersonel.setArPersonelType(arPersonelType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateArPeEntry(ArModPersonelDetails details, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException {
        Debug.print("ArPersonelController updateArPeEntry");
        try {
            LocalArPersonel arPersonel = null;

            try {

                arPersonel = arPersonelHome.findByPrimaryKey(details.getPeCode());

            } catch (FinderException ex) {
                throw new GlobalRecordAlreadyExistException();
            }
            Debug.print("pasok 2");

            try {

                LocalArPersonel arExistingPersonel = arPersonelHome.findByPeIdNumber(details.getPeIdNumber(), AD_CMPNY);

                if (!arExistingPersonel.getPeCode().equals(arPersonel.getPeCode())) {

                    throw new GlobalRecordAlreadyExistException(details.getPeName());
                }

                arExistingPersonel = arPersonelHome.findByPeName(details.getPeName(), AD_CMPNY);

                if (!arExistingPersonel.getPeCode().equals(arPersonel.getPeCode())) {

                    throw new GlobalRecordAlreadyExistException(details.getPeName());
                }

            } catch (FinderException ex) {

            }

            Debug.print("pasok 3");

            arPersonel = arPersonelHome.findByPrimaryKey(details.getPeCode());
            arPersonel.setPeIdNumber(details.getPeIdNumber());
            arPersonel.setPeName(details.getPeName());
            arPersonel.setPeDescription(details.getPeDescription());
            arPersonel.setPeAddress(details.getPeAddress());
            arPersonel.setPeRate(details.getPeRate());
            Debug.print("pasok 4");
            LocalArPersonelType arPersonelType = arPersonelTypeHome.findByPtName(details.getPePtName(), AD_CMPNY);

            Debug.print("pasok 1");
            arPersonel.setArPersonelType(arPersonelType);

        } catch (GlobalRecordAlreadyExistException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArPeEntry(Integer PRL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {
        Debug.print("ArPersonelController deleteArPeEntry");
        try {

            LocalArPersonel arPersonel = null;

            try {

                arPersonel = arPersonelHome.findByPrimaryKey(PRL_CODE);

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            // Assigned Exception not in ready

            //    		 arPersonel.entityRemove();
            em.remove(arPersonel);

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

            //   } catch (GlobalRecordAlreadyAssignedException ex) {

            //   	getSessionContext().setRollbackOnly();
            //    	throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArPersonelController getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getPersonelTypeRateByPersonelTypeName(String PT_NM, Integer AD_CMPNY) {
        Debug.print("ArPersonelControlleBean getPersonelTypeRateByPersonelTypeName");
        try {

            LocalArPersonelType arPersonelType = arPersonelTypeHome.findByPtName(PT_NM, AD_CMPNY);

            if (arPersonelType != null) {

                return arPersonelType.getPtRate();

            } else {
                return 0;
            }

        } catch (FinderException ex) {

            return 0;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArPtAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArPersonelControlleBean getArPtAll");
        ArrayList list = new ArrayList();
        try {

            Collection arPersonelTypes = arPersonelTypeHome.findPtAll(AD_CMPNY);

            if (arPersonelTypes.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object personelType : arPersonelTypes) {

                LocalArPersonelType arPersonelType = (LocalArPersonelType) personelType;

                ArPersonelTypeDetails details = new ArPersonelTypeDetails();

                details.setPtCode(arPersonelType.getPtCode());
                details.setPtShortName(arPersonelType.getPtShortName());
                details.setPtName(arPersonelType.getPtName());
                details.setPtDescription(arPersonelType.getPtDescription());
                details.setPtRate(arPersonelType.getPtRate());
                details.setPtAdCompany(AD_CMPNY);

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

}