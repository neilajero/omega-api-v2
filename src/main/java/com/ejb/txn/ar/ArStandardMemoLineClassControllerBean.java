package com.ejb.txn.ar;

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
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerClass;
import com.ejb.dao.ar.LocalArCustomerClassHome;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.entities.ar.LocalArStandardMemoLineClass;
import com.ejb.dao.ar.LocalArStandardMemoLineClassHome;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.ar.ArCustomerDetails;
import com.util.mod.ar.ArModStandardMemoLineClassDetails;
import com.util.ar.ArStandardMemoLineDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArStandardMemoLineClassControllerEJB")
public class ArStandardMemoLineClassControllerBean extends EJBContextClass implements ArStandardMemoLineClassController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalArCustomerClassHome arCustomerClassHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArStandardMemoLineClassHome arStandardMemoLineClassHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;

    public void saveArSmcEntry(ArrayList list, String USR_NM, String CC_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean saveArSmcEntry");

        LocalAdCompany adCompany = null;

        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            for (Object o : list) {

                ArModStandardMemoLineClassDetails details = (ArModStandardMemoLineClassDetails) o;

                String lineNumber = details.getSmcLine();

                LocalAdBranch adBranch = null;
                LocalArCustomerClass arCustomerClass = null;
                LocalArStandardMemoLine arStandardMemoLine = null;
                LocalArCustomer arCustomer = null;
                LocalArStandardMemoLineClass arStandardMemoLineClass = null;

                try {

                    Debug.print(CC_NM + " customer class name in controller");
                    arCustomerClass = arCustomerClassHome.findByCcName(CC_NM, AD_CMPNY);
                } catch (FinderException ex) {
                    // customer class not exist
                    Debug.print("Customer Class : " + CC_NM + " Not Exist in Line Number " + lineNumber);
                    throw ex;
                }

                try {
                    arStandardMemoLine = arStandardMemoLineHome.findBySmlName(details.getSmcStandardMemoLineName(), AD_CMPNY);
                } catch (FinderException ex) {
                    // standard memo line not exist
                    Debug.print("Standard Memo Line : " + details.getSmcStandardMemoLineName() + " Not Exist in Line Number " + lineNumber);
                    throw ex;
                }

                try {
                    adBranch = adBranchHome.findByBrName(details.getSmcAdBranchName(), AD_CMPNY);
                } catch (FinderException ex) {
                    // branch not exist
                    Debug.print("Branch : " + details.getSmcAdBranchName() + " Not Exist in Line Number " + lineNumber);
                    throw ex;
                }

                if (details.getSmcCode() != null) {

                    // to update
                    try {
                        arStandardMemoLineClass = arStandardMemoLineClassHome.findByPrimaryKey(details.getSmcCode());
                    } catch (FinderException ex) {
                        Debug.print("Standard Memo Line Class : " + details.getSmcAdBranchName() + " Not Exist for updating in Line Number " + lineNumber);
                        throw ex;
                    }

                    arStandardMemoLineClass.setSmcUnitPrice(details.getSmcUnitPrice());
                    arStandardMemoLineClass.setSmcStandardMemoLineDescription(details.getSmcStandardMemoLineDescription());
                    arStandardMemoLineClass.setSmcModifiedBy(USR_NM);
                    arStandardMemoLineClass.setSmcDateModified(new java.util.Date());
                    arStandardMemoLineClass.setSmcAdBranch(adBranch.getBrCode());

                    // ar customer if exist
                    if (details.getSmcCustomerCode() != null && !details.getSmcCustomerCode().equals("")) {
                        try {
                            arCustomer = arCustomerHome.findByCstCustomerCode(details.getSmcCustomerCode(), AD_CMPNY);

                        } catch (FinderException ex) {
                            Debug.print("Customer Code  : " + details.getSmcCustomerCode() + " Not Exist for updating in Line Number " + lineNumber);
                            throw ex;
                        }
                        arStandardMemoLineClass.setArCustomer(arCustomer);

                    } else {
                        arStandardMemoLineClass.setArCustomer(null);
                    }

                } else {
                    // to add

                    arStandardMemoLineClass = arStandardMemoLineClassHome.create(details.getSmcUnitPrice(), details.getSmcStandardMemoLineDescription(), USR_NM, new java.util.Date(), USR_NM, new java.util.Date(), adBranch.getBrCode(), AD_CMPNY);
                    arStandardMemoLineClass.setArCustomerClass(arCustomerClass);
                    arStandardMemoLineClass.setArStandardMemoLine(arStandardMemoLine);

                    details.setSmcCustomerCode(arStandardMemoLineClass.getArCustomer() == null ? "" : arStandardMemoLineClass.getArCustomer().getCstCustomerCode());
                    details.setSmcCustomerName(arStandardMemoLineClass.getArCustomer() == null ? "" : arStandardMemoLineClass.getArCustomer().getCstName());

                    // ar customer if exist
                    if (details.getSmcCustomerCode() != null && !details.getSmcCustomerCode().equals("")) {
                        try {
                            arCustomer = arCustomerHome.findByCstCustomerCode(details.getSmcCustomerCode(), AD_CMPNY);

                        } catch (FinderException ex) {
                            Debug.print("Customer Code  : " + details.getSmcCustomerCode() + " Not Exist for updating in Line Number " + lineNumber);
                            throw ex;
                        }
                        arStandardMemoLineClass.setArCustomer(arCustomer);
                    }
                }
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteArSmcEntry(int SMC_CODE) throws GlobalRecordAlreadyDeletedException {

        Debug.print("ArStandardMemoLineClassControllerBean deleteArSmcEntry");

        try {

            LocalArStandardMemoLineClass arStandardMemoLineClass = null;
            arStandardMemoLineClass = arStandardMemoLineClassHome.findByPrimaryKey(SMC_CODE);

            if (arStandardMemoLineClass == null) {
                throw new FinderException();
            } else {
                //                arStandardMemoLineClass.entityRemove();
                em.remove(arStandardMemoLineClass);
            }

        } catch (FinderException ex) {
            // no standard memo line class exist or already deleted
            throw new GlobalRecordAlreadyDeletedException();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAllSmlNm(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getAllSmlNm");

        ArrayList list = new ArrayList();

        try {

            Collection smlList = arStandardMemoLineHome.findSmlAll(AD_CMPNY);

            for (Object o : smlList) {
                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) o;

                list.add(arStandardMemoLine.getSmlName());
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getAllCcNm(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getAllCcNm");

        ArrayList list = new ArrayList();

        try {

            Collection ccList = arCustomerClassHome.findCcAll(AD_CMPNY);

            for (Object o : ccList) {

                LocalArCustomerClass arCustomerClass = (LocalArCustomerClass) o;
                Debug.print(arCustomerClass.getCcName() + "  from controller");
                list.add(arCustomerClass.getCcName());
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArModStandardMemoLineClassDetails getSmcByCcNmSmlNmCstCstmrCodeBrNm(String CC_NM, String SML_NM, String CST_CSTMR_CODE, String BR_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException, FinderException {

        Debug.print("ArStandardMemoLineClassControllerBean getSmcByCcNmSmlNmCstCstmrCodeBrNm");

        ArrayList list = new ArrayList();

        ArModStandardMemoLineClassDetails details = null;

        try {
            LocalAdBranch adBranch = adBranchHome.findByBrName(BR_NM, AD_CMPNY);

            LocalArStandardMemoLineClass arStandardMemoLineClass = arStandardMemoLineClassHome.findSmcByCcNameSmlNameCstCstmrCodeAdBrnch(CC_NM, SML_NM, CST_CSTMR_CODE, adBranch.getBrCode(), AD_CMPNY);

            details = new ArModStandardMemoLineClassDetails();

            details.setSmcCode(arStandardMemoLineClass.getSmcCode());
            details.setSmcAdBranchName(adBranch.getBrName());
            details.setSmcAdCompany(arStandardMemoLineClass.getSmcAdCompany());
            details.setSmcCustomerClassName(arStandardMemoLineClass.getArCustomerClass().getCcName());
            details.setSmcStandardMemoLineName(arStandardMemoLineClass.getArStandardMemoLine().getSmlName());
            details.setSmcStandardMemoLineDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
            details.setSmcUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());
            details.setSmcCreatedBy(arStandardMemoLineClass.getSmcCreatedBy());
            details.setSmcDateCreated(arStandardMemoLineClass.getSmcDateCreated());
            details.setSmcModifiedBy(arStandardMemoLineClass.getSmcModifiedBy());
            details.setSmcDateModified(arStandardMemoLineClass.getSmcDateModified());

            if (arStandardMemoLineClass.getArCustomer() != null) {
                details.setSmcCustomerCode(arStandardMemoLineClass.getArCustomer().getCstCustomerCode());
                details.setSmcCustomerCode(arStandardMemoLineClass.getArCustomer().getCstName());
            }

        } catch (FinderException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return details;
    }

    public ArrayList getAllCst(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getAllCst");

        ArrayList list = new ArrayList();

        try {

            Collection cstList = arCustomerHome.findEnabledCstAllOrderByCstName(AD_CMPNY);

            for (Object o : cstList) {
                LocalArCustomer arCustomer = (LocalArCustomer) o;

                list.add(arCustomer.getCstCustomerCode());
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList getAllBrNm(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getAllBrNm");

        ArrayList list = new ArrayList();

        try {

            Collection brList = adBranchHome.findBrAll(AD_CMPNY);

            for (Object o : brList) {
                LocalAdBranch adBranch = (LocalAdBranch) o;

                list.add(adBranch.getBrName());
            }

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    public ArrayList getAllSmcByCcNm(String CC_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getAllCcNm");

        ArrayList list = new ArrayList();

        try {

            Collection smcList = arStandardMemoLineClassHome.findSmcByCcName(CC_NM, AD_CMPNY);

            for (Object o : smcList) {

                LocalArStandardMemoLineClass arStandardMemoLineClass = (LocalArStandardMemoLineClass) o;
                Debug.print(arStandardMemoLineClass.getSmcCode() + "  from controller");

                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(arStandardMemoLineClass.getSmcAdBranch());

                ArModStandardMemoLineClassDetails details = new ArModStandardMemoLineClassDetails();

                details.setSmcCode(arStandardMemoLineClass.getSmcCode());
                details.setSmcAdBranchName(adBranch.getBrName());
                details.setSmcAdCompany(arStandardMemoLineClass.getSmcAdCompany());
                details.setSmcCustomerClassName(arStandardMemoLineClass.getArCustomerClass().getCcName());
                details.setSmcStandardMemoLineName(arStandardMemoLineClass.getArStandardMemoLine().getSmlName());
                details.setSmcStandardMemoLineDescription(arStandardMemoLineClass.getSmcStandardMemoLineDescription());
                details.setSmcUnitPrice(arStandardMemoLineClass.getSmcUnitPrice());
                details.setSmcCreatedBy(arStandardMemoLineClass.getSmcCreatedBy());
                details.setSmcDateCreated(arStandardMemoLineClass.getSmcDateCreated());
                details.setSmcModifiedBy(arStandardMemoLineClass.getSmcModifiedBy());
                details.setSmcDateModified(arStandardMemoLineClass.getSmcDateModified());
                details.setSmcCustomerCode(arStandardMemoLineClass.getArCustomer() == null ? null : arStandardMemoLineClass.getArCustomer().getCstCustomerCode());
                details.setSmcCustomerName(arStandardMemoLineClass.getArCustomer() == null ? null : arStandardMemoLineClass.getArCustomer().getCstName());

                list.add(details);
            }

        } catch (FinderException ex) {

            return new ArrayList();

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArStandardMemoLineDetails getArSmlBySmlName(String SML_NM, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getArSmlBySmlName");

        try {

            LocalArStandardMemoLine arStandardMemoLine = null;

            try {

                arStandardMemoLine = arStandardMemoLineHome.findBySmlName(SML_NM, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArStandardMemoLineDetails details = new ArStandardMemoLineDetails();
            details.setSmlCode(arStandardMemoLine.getSmlCode());
            details.setSmlName(arStandardMemoLine.getSmlName());
            details.setSmlDescription(arStandardMemoLine.getSmlDescription());
            details.setSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArCustomerDetails getArCstByCstCstmrCode(String CST_CSTMR_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArStandardMemoLineClassControllerBean getArCstByCstCstmrCode");

        try {

            LocalArCustomer arCustomer = null;

            try {

                arCustomer = arCustomerHome.findByCstCustomerCode(CST_CSTMR_CODE, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            ArCustomerDetails details = new ArCustomerDetails();
            details.setCstCustomerCode(arCustomer.getCstCustomerCode());
            details.setCstName(arCustomer.getCstName());

            return details;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArStandardMemoLineClassControllerBean ejbCreate");
    }
}