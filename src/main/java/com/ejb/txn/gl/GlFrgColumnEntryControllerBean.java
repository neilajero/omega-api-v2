package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gl.GlFrgCOLColumnNameAlreadyExistException;
import com.ejb.exception.gl.GlFrgCOLSequenceNumberAlreadyExistException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.entities.gl.LocalGlFrgColumn;
import com.ejb.dao.gl.LocalGlFrgColumnHome;
import com.ejb.entities.gl.LocalGlFrgColumnSet;
import com.ejb.dao.gl.LocalGlFrgColumnSetHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gl.GlFrgColumnSetDetails;
import com.util.mod.gl.GlModFrgColumnDetails;
import com.util.mod.gl.GlModFunctionalCurrencyDetails;

@Stateless(name = "GlFrgColumnEntryControllerEJB")
public class GlFrgColumnEntryControllerBean extends EJBContextClass implements GlFrgColumnEntryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlFrgColumnHome glFrgColumnHome;
    @EJB
    private LocalGlFrgColumnSetHome glFrgColumnSetHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;


  public ArrayList getGlFrgColByCsCode(Integer CS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFrgColumnEntryControllerBean getGlFrgColByCsCode");

        Collection glFrgColumns = null;

        LocalGlFrgColumn glFrgColumn = null;

        ArrayList list = new ArrayList();

        try {

            glFrgColumns = glFrgColumnHome.findByCsCode(CS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFrgColumns.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object frgColumn : glFrgColumns) {

            glFrgColumn = (LocalGlFrgColumn) frgColumn;

            GlModFrgColumnDetails mdetails = new GlModFrgColumnDetails();
            mdetails.setColCode(glFrgColumn.getColCode());
            mdetails.setColName(glFrgColumn.getColName());
            mdetails.setColPosition(glFrgColumn.getColPosition());
            mdetails.setColSequenceNumber(glFrgColumn.getColSequenceNumber());
            mdetails.setColFormatMask(glFrgColumn.getColFormatMask());
            mdetails.setColFactor(glFrgColumn.getColFactor());
            mdetails.setColAmountType(glFrgColumn.getColAmountType());
            mdetails.setColOffset(glFrgColumn.getColOffset());
            mdetails.setColOverrideRowCalculation(glFrgColumn.getColOverrideRowCalculation());
            mdetails.setColFcName(glFrgColumn.getGlFunctionalCurrency().getFcName());

            list.add(mdetails);
        }

        return list;
    }

    public GlFrgColumnSetDetails getGlFrgCsByCsCode(Integer CS_CODE, Integer AD_CMPNY) {

        Debug.print("GlFrgColumnEntryControllerBean getGlFrgCsByCsCode");

        try {

            LocalGlFrgColumnSet glFrgColumnSet = glFrgColumnSetHome.findByPrimaryKey(CS_CODE);

            GlFrgColumnSetDetails details = new GlFrgColumnSetDetails();
            details.setCsCode(glFrgColumnSet.getCsCode());
            details.setCsName(glFrgColumnSet.getCsName());
            details.setCsDescription(glFrgColumnSet.getCsDescription());

            return details;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void addGlFrgColEntry(GlModFrgColumnDetails mdetails, Integer CS_CODE, Integer AD_CMPNY) throws GlFrgCOLColumnNameAlreadyExistException, GlFrgCOLSequenceNumberAlreadyExistException {

        Debug.print("GlFrgColumnEntryControllerBean addGlFrgColumnEntry");

        LocalGlFrgColumn glFrgColumn = null;
        LocalGlFrgColumnSet glFrgColumnSet = null;
        LocalGlFunctionalCurrency glFunctionalCurrency = null;

        try {

            glFrgColumn = glFrgColumnHome.findByColumnNameAndCsCode(mdetails.getColName(), CS_CODE, AD_CMPNY);

            throw new GlFrgCOLColumnNameAlreadyExistException();

        } catch (GlFrgCOLColumnNameAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            glFrgColumn = glFrgColumnHome.findBySequenceNumberAndCsCode(mdetails.getColSequenceNumber(), CS_CODE, AD_CMPNY);

            throw new GlFrgCOLSequenceNumberAlreadyExistException();

        } catch (GlFrgCOLSequenceNumberAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // create new columns

            glFrgColumn = glFrgColumnHome.create(mdetails.getColName(), mdetails.getColPosition(), mdetails.getColSequenceNumber(), mdetails.getColFormatMask(), mdetails.getColFactor(), mdetails.getColAmountType(), mdetails.getColOffset(), mdetails.getColOverrideRowCalculation(), AD_CMPNY);

            glFrgColumnSet = glFrgColumnSetHome.findByPrimaryKey(CS_CODE);
            glFrgColumnSet.addGlFrgColumn(glFrgColumn);

            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(mdetails.getColFcName(), AD_CMPNY);
            glFunctionalCurrency.addGlFrgColumn(glFrgColumn);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGlFrgColEntry(GlModFrgColumnDetails mdetails, Integer CS_CODE, Integer AD_CMPNY) throws GlFrgCOLColumnNameAlreadyExistException, GlFrgCOLSequenceNumberAlreadyExistException {

        Debug.print("GlFrgColumnEntryControllerBean updateGlFrgColEntry");

        LocalGlFrgColumn glFrgColumn = null;
        LocalGlFrgColumnSet glFrgColumnSet = null;
        LocalGlFunctionalCurrency glFunctionalCurrency = null;

        try {

            glFrgColumn = glFrgColumnHome.findByColumnNameAndCsCode(mdetails.getColName(), CS_CODE, AD_CMPNY);

            if (glFrgColumn != null && !glFrgColumn.getColCode().equals(mdetails.getColCode())) {

                throw new GlFrgCOLColumnNameAlreadyExistException();
            }

        } catch (GlFrgCOLColumnNameAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            glFrgColumn = glFrgColumnHome.findBySequenceNumberAndCsCode(mdetails.getColSequenceNumber(), CS_CODE, AD_CMPNY);

            if (glFrgColumn != null && !glFrgColumn.getColCode().equals(mdetails.getColCode())) {

                throw new GlFrgCOLSequenceNumberAlreadyExistException();
            }

        } catch (GlFrgCOLSequenceNumberAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Find and Update Columns

        try {

            glFrgColumn = glFrgColumnHome.findByPrimaryKey(mdetails.getColCode());

            glFrgColumn.setColName(mdetails.getColName());
            glFrgColumn.setColPosition(mdetails.getColPosition());
            glFrgColumn.setColSequenceNumber(mdetails.getColSequenceNumber());
            glFrgColumn.setColFormatMask(mdetails.getColFormatMask());
            glFrgColumn.setColFactor(mdetails.getColFactor());
            glFrgColumn.setColAmountType(mdetails.getColAmountType());
            glFrgColumn.setColOffset(mdetails.getColOffset());
            glFrgColumn.setColOverrideRowCalculation(mdetails.getColOverrideRowCalculation());

            glFrgColumnSet = glFrgColumnSetHome.findByPrimaryKey(CS_CODE);
            glFrgColumnSet.addGlFrgColumn(glFrgColumn);

            glFunctionalCurrency = glFunctionalCurrencyHome.findByFcName(mdetails.getColFcName(), AD_CMPNY);
            glFunctionalCurrency.addGlFrgColumn(glFrgColumn);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGlFrgColEntry(Integer COL_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlFrgColumnEntryControllerBean deleteGlFrgColEntry");

        LocalGlFrgColumn glFrgColumn = null;

        try {

            glFrgColumn = glFrgColumnHome.findByPrimaryKey(COL_CODE);

        } catch (FinderException ex) {

            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            //    		glFrgColumn.entityRemove();
            em.remove(glFrgColumn);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());

        }
    }

    public ArrayList getGlFcAllWithDefault(Integer AD_CMPNY) {

        Debug.print("GlFrgColumnEntryControllerBean getGlFcAllWithDefault");

        Collection glFunctionalCurrencies = null;

        LocalGlFunctionalCurrency glFunctionalCurrency = null;
        LocalAdCompany adCompany = null;

        ArrayList list = new ArrayList();

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            glFunctionalCurrencies = glFunctionalCurrencyHome.findFcAllEnabled(EJBCommon.getGcCurrentDateWoTime().getTime(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (glFunctionalCurrencies.isEmpty()) {

            return null;
        }

        for (Object functionalCurrency : glFunctionalCurrencies) {

            glFunctionalCurrency = (LocalGlFunctionalCurrency) functionalCurrency;

            GlModFunctionalCurrencyDetails mdetails = new GlModFunctionalCurrencyDetails(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), adCompany.getGlFunctionalCurrency().getFcName().equals(glFunctionalCurrency.getFcName()) ? EJBCommon.TRUE : EJBCommon.FALSE);

            list.add(mdetails);
        }

        return list;
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlFrgColumnEntryControllerBean ejbCreate");
    }
}