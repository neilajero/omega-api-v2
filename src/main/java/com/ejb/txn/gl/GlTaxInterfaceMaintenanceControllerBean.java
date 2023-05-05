package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.dao.ap.LocalApTaxCodeHome;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.dao.ap.LocalApWithholdingTaxCodeHome;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.dao.ar.LocalArCustomerHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.dao.ar.LocalArTaxCodeHome;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.dao.ar.LocalArWithholdingTaxCodeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlTaxInterface;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModTaxInterfaceDetails;

@Stateless(name = "GlTaxInterfaceMaintenanceControllerEJB")
public class GlTaxInterfaceMaintenanceControllerBean extends EJBContextClass implements GlTaxInterfaceMaintenanceController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApTaxCodeHome apTaxCodeHome;
    @EJB
    private LocalApWithholdingTaxCodeHome apWithholdingTaxCodeHome;
    @EJB
    private LocalArCustomerHome arCustomerHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArTaxCodeHome arTaxCodeHome;
    @EJB
    private LocalArWithholdingTaxCodeHome arWithholdingTaxCodeHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;


    public ArrayList getArCstmrAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getArCstmrAll");

        ArrayList list = new ArrayList();

        try {

            Collection arCustomers = arCustomerHome.findEnabledCstAll(1, AD_CMPNY);

            for (Object customer : arCustomers) {

                LocalArCustomer arCustomer = (LocalArCustomer) customer;

                list.add(arCustomer.getCstCustomerCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArTcAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getArTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection arTaxCodes = arTaxCodeHome.findEnabledTcAll(AD_CMPNY);

            for (Object taxCode : arTaxCodes) {

                LocalArTaxCode arTaxCode = (LocalArTaxCode) taxCode;
                LocalGlChartOfAccount glChartOfAccount = arTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    list.add(arTaxCode.getTcName());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getArTcRate(String TC_NM, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getArTcRate");

        ArrayList list = new ArrayList();

        try {

            LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);

            return arTaxCode.getTcRate();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArWtcAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getArWtcAll");

        ArrayList list = new ArrayList();

        try {

            Collection arWithholdingTaxCodes = arWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

            for (Object withholdingTaxCode : arWithholdingTaxCodes) {

                LocalArWithholdingTaxCode arWithholdingTaxCode = (LocalArWithholdingTaxCode) withholdingTaxCode;
                LocalGlChartOfAccount glChartOfAccount = arWithholdingTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    list.add(arWithholdingTaxCode.getWtcName());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getArWtcRate(String WTC_NM, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getArWtcRate");

        ArrayList list = new ArrayList();

        try {

            LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);

            return arWithholdingTaxCode.getWtcRate();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApSpplrAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getApSpplrAll");

        ArrayList list = new ArrayList();

        try {

            Collection apSuppliers = apSupplierHome.findEnabledSplAll(1, AD_CMPNY);

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                list.add(apSupplier.getSplSupplierCode());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApTcAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getApTcAll");

        ArrayList list = new ArrayList();

        try {

            Collection apTaxCodes = apTaxCodeHome.findEnabledTcAll(AD_CMPNY);

            for (Object taxCode : apTaxCodes) {

                LocalApTaxCode apTaxCode = (LocalApTaxCode) taxCode;
                LocalGlChartOfAccount glChartOfAccount = apTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    list.add(apTaxCode.getTcName());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getApTcRate(String TC_NM, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getApTcRate");

        ArrayList list = new ArrayList();

        try {

            LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(TC_NM, AD_CMPNY);

            return apTaxCode.getTcRate();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getApWtcAll(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getApWtcAll");

        ArrayList list = new ArrayList();

        try {

            Collection arWithholdingTaxCodes = apWithholdingTaxCodeHome.findEnabledWtcAll(AD_CMPNY);

            for (Object arWithholdingTaxCode : arWithholdingTaxCodes) {

                LocalApWithholdingTaxCode apWithholdingTaxCode = (LocalApWithholdingTaxCode) arWithholdingTaxCode;
                LocalGlChartOfAccount glChartOfAccount = apWithholdingTaxCode.getGlChartOfAccount();

                if (glChartOfAccount != null) {

                    list.add(apWithholdingTaxCode.getWtcName());
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public double getApWtcRate(String WTC_NM, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getApWtcRate");

        ArrayList list = new ArrayList();

        try {

            LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(WTC_NM, AD_CMPNY);

            return apWithholdingTaxCode.getWtcRate();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlTiByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getGlTiByCriteria");

        try {

            ArrayList tiList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(ti) FROM GlTaxInterface ti ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            Object[] obj = new Object[criteriaSize];

            if (criteria.containsKey("documentType")) {

                firstArgument = false;

                jbossQl.append("WHERE ti.tiDocumentType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentType");
                ctr++;
            }

            if (criteria.containsKey("docNumFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiTxnDocumentNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("docNumFrom");
                ctr++;
            }

            if (criteria.containsKey("docNumTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiTxnDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("docNumTo");
                ctr++;
            }

            if (criteria.containsKey("refNumFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiTxnReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("refNumFrom");
                ctr++;
            }

            if (criteria.containsKey("refNumTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiTxnReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("refNumTo");
                ctr++;
            }

            if (criteria.containsKey("subledgerFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiSlSubledegrCode>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("subledgerFrom");
                ctr++;
            }

            if (criteria.containsKey("subledgerTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("ti.tiSlSubledegrCode<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("subledgerTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("ti.tiAdBranch=").append(AD_BRNCH).append(" AND ti.tiAdCompany=").append(AD_CMPNY).append(" ");

            Collection glTaxInterfaces = null;

            try {

                glTaxInterfaces = glTaxInterfaceHome.getTiByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (glTaxInterfaces.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object taxInterface : glTaxInterfaces) {

                LocalGlTaxInterface glTaxInterface = (LocalGlTaxInterface) taxInterface;

                String docType = glTaxInterface.getTiDocumentType();

                GlModTaxInterfaceDetails mdetails = new GlModTaxInterfaceDetails();

                mdetails.setTiCode(glTaxInterface.getTiCode());
                mdetails.setTiDocumentType(docType);
                mdetails.setTiTxnCode(glTaxInterface.getTiTxnCode());
                mdetails.setTiTxnDate(glTaxInterface.getTiTxnDate());
                mdetails.setTiTxnDocumentNumber(glTaxInterface.getTiTxnDocumentNumber());
                mdetails.setTiTxnReferenceNumber(glTaxInterface.getTiTxnReferenceNumber());
                mdetails.setTiTxlCode(glTaxInterface.getTiTxlCode());

                // coa
                LocalGlChartOfAccount glChartOfAccount = glChartOfAccountHome.findByPrimaryKey(glTaxInterface.getTiTxlCoaCode());
                mdetails.setTiTxlCoaNumber(glChartOfAccount.getCoaAccountNumber());
                mdetails.setTiTxlCoaCode(glTaxInterface.getTiTxlCoaCode());

                double TC_AMNT = 0d;
                double WTC_AMNT = 0d;
                double NET_AMNT = glTaxInterface.getTiNetAmount();

                // tax & withholding tax
                // ar records
                if (docType.equals("AR INVOICE") || docType.equals("AR CREDIT MEMO") || docType.equals("AR RECEIPT")) {

                    mdetails.setTiIsArDocument(EJBCommon.TRUE);
                    mdetails.setTiEditGlDocument(EJBCommon.FALSE);

                    if (glTaxInterface.getTiTcCode() != null) {

                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());
                        LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                        TC_AMNT = arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? arDistributionRecord.getDrAmount() * -1 : arDistributionRecord.getDrAmount();
                        NET_AMNT = arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? NET_AMNT * -1 : NET_AMNT;

                        mdetails.setTiNetAmount(NET_AMNT);
                        mdetails.setTiTaxAmount(TC_AMNT);
                        mdetails.setTiTcName(arTaxCode.getTcName());
                        mdetails.setTiWtcName(null);

                    } else {

                        LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());
                        LocalArDistributionRecord arDistributionRecord = arDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                        WTC_AMNT = arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? arDistributionRecord.getDrAmount() : arDistributionRecord.getDrAmount() * -1;
                        NET_AMNT = arDistributionRecord.getDrDebit() == EJBCommon.TRUE ? NET_AMNT : NET_AMNT * -1;

                        mdetails.setTiNetAmount(NET_AMNT);
                        mdetails.setTiTaxAmount(WTC_AMNT);
                        mdetails.setTiTcName(null);
                        mdetails.setTiWtcName(arWithholdingTaxCode.getWtcName());
                    }

                    LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(glTaxInterface.getTiSlCode());
                    mdetails.setTiSlCode(arCustomer.getCstCode());
                    mdetails.setTiSlSubledgerCode(arCustomer.getCstCustomerCode());

                    // ap records
                } else if (docType.equals("AP VOUCHER") || docType.equals("AP DEBIT MEMO") || docType.equals("AP CHECK") || docType.equals("AP RECEIVING ITEM")) {

                    mdetails.setTiIsArDocument(EJBCommon.FALSE);
                    mdetails.setTiEditGlDocument(EJBCommon.FALSE);

                    if (glTaxInterface.getTiTcCode() != null) {

                        LocalApTaxCode apTaxCode = apTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());
                        LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                        TC_AMNT = apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? apDistributionRecord.getDrAmount() : apDistributionRecord.getDrAmount() * -1;
                        NET_AMNT = apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? NET_AMNT : NET_AMNT * -1;

                        mdetails.setTiNetAmount(NET_AMNT);
                        mdetails.setTiTaxAmount(TC_AMNT);
                        mdetails.setTiTcName(apTaxCode.getTcName());
                        mdetails.setTiWtcName(null);

                    } else {

                        LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());
                        LocalApDistributionRecord apDistributionRecord = apDistributionRecordHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                        WTC_AMNT = apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? apDistributionRecord.getDrAmount() * -1 : apDistributionRecord.getDrAmount();
                        NET_AMNT = apDistributionRecord.getDrDebit() == EJBCommon.TRUE ? NET_AMNT * -1 : NET_AMNT;

                        mdetails.setTiNetAmount(NET_AMNT);
                        mdetails.setTiTaxAmount(WTC_AMNT);
                        mdetails.setTiTcName(null);
                        mdetails.setTiWtcName(apWithholdingTaxCode.getWtcName());
                    }

                    LocalApSupplier apSupplier = apSupplierHome.findByPrimaryKey(glTaxInterface.getTiSlCode());
                    mdetails.setTiSlCode(apSupplier.getSplCode());
                    mdetails.setTiSlSubledgerCode(apSupplier.getSplSupplierCode());

                    // gl records
                } else {

                    LocalGlJournalLine glJournalLine = glJournalLineHome.findByPrimaryKey(glTaxInterface.getTiTxlCode());

                    TC_AMNT = glJournalLine.getJlDebit() == EJBCommon.TRUE ? glJournalLine.getJlAmount() * -1 : glJournalLine.getJlAmount();
                    NET_AMNT = glJournalLine.getJlDebit() == EJBCommon.TRUE ? NET_AMNT * -1 : NET_AMNT;

                    mdetails.setTiTaxAmount(TC_AMNT);
                    mdetails.setTiNetAmount(NET_AMNT);

                    // edited gl record
                    if (glTaxInterface.getTiTcCode() != null || glTaxInterface.getTiWtcCode() != null) {

                        if (glTaxInterface.getTiIsArDocument() == EJBCommon.TRUE) {

                            if (glTaxInterface.getTiTcCode() != null) {

                                LocalArTaxCode arTaxCode = arTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());

                                mdetails.setTiTcName(arTaxCode.getTcName());
                                mdetails.setTiWtcName(null);

                            } else {

                                LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());

                                mdetails.setTiTcName(null);
                                mdetails.setTiWtcName(arWithholdingTaxCode.getWtcName());
                            }

                            mdetails.setTiEditGlDocument(EJBCommon.FALSE);
                            mdetails.setTiIsArDocument(EJBCommon.TRUE);

                            LocalArCustomer arCustomer = arCustomerHome.findByPrimaryKey(glTaxInterface.getTiSlCode());
                            mdetails.setTiSlCode(arCustomer.getCstCode());
                            mdetails.setTiSlSubledgerCode(arCustomer.getCstCustomerCode());

                        } else {

                            if (glTaxInterface.getTiTcCode() != null) {

                                LocalApTaxCode apTaxCode = apTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiTcCode());

                                mdetails.setTiTcName(apTaxCode.getTcName());
                                mdetails.setTiWtcName(null);

                            } else {

                                LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByPrimaryKey(glTaxInterface.getTiWtcCode());

                                mdetails.setTiTcName(null);
                                mdetails.setTiWtcName(apWithholdingTaxCode.getWtcName());
                            }

                            mdetails.setTiEditGlDocument(EJBCommon.FALSE);
                            mdetails.setTiIsArDocument(EJBCommon.FALSE);

                            LocalApSupplier apSupplier = apSupplierHome.findByPrimaryKey(glTaxInterface.getTiSlCode());
                            mdetails.setTiSlCode(apSupplier.getSplCode());
                            mdetails.setTiSlSubledgerCode(apSupplier.getSplSupplierCode());
                        }

                    } else {

                        mdetails.setTiEditGlDocument(EJBCommon.TRUE);
                        mdetails.setTiIsArDocument(EJBCommon.FALSE);

                        mdetails.setTiTcName(null);
                        mdetails.setTiWtcName(null);
                        mdetails.setTiSlCode(null);
                        mdetails.setTiSlSubledgerCode(null);
                    }
                }

                tiList.add(mdetails);
            }

            return tiList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveGlTiMaintenance(GlModTaxInterfaceDetails mdetails, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean saveGlTiMaintenance");

        try {

            LocalGlTaxInterface glTaxInterface = null;

            // validate if tax interface already exist

            try {

                glTaxInterface = glTaxInterfaceHome.findByPrimaryKey(mdetails.getTiCode());

            } catch (FinderException ex) {

                throw new GlobalRecordAlreadyDeletedException();
            }

            if (mdetails.getTiDocumentType().equals("GL JOURNAL")) {

                LocalGlJournalLine glJournalLine = null;

                try {

                    glJournalLine = glJournalLineHome.findByPrimaryKey(mdetails.getTiTxlCode());

                } catch (FinderException ex) {

                    throw new GlobalRecordAlreadyDeletedException();
                }

                double DR_AMNT = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                if (mdetails.getTiIsArDocument() == EJBCommon.TRUE) {

                    glTaxInterface.setTiIsArDocument(EJBCommon.TRUE);
                    glTaxInterface.setTiTxnReferenceNumber(mdetails.getTiTxnReferenceNumber());

                    if (mdetails.getTiTcName() != null) {

                        LocalArTaxCode arTaxCode = arTaxCodeHome.findByTcName(mdetails.getTiTcName(), AD_CMPNY);

                        double NET_AMNT = DR_AMNT / (arTaxCode.getTcRate() / 100);

                        glTaxInterface.setTiNetAmount(NET_AMNT);
                        glTaxInterface.setTiTcCode(arTaxCode.getTcCode());

                    } else {

                        LocalArWithholdingTaxCode arWithholdingTaxCode = arWithholdingTaxCodeHome.findByWtcName(mdetails.getTiWtcName(), AD_CMPNY);

                        double NET_AMNT = DR_AMNT / (arWithholdingTaxCode.getWtcRate() / 100);

                        glTaxInterface.setTiNetAmount(NET_AMNT);
                        glTaxInterface.setTiWtcCode(arWithholdingTaxCode.getWtcCode());
                    }

                    LocalArCustomer arCustomer = arCustomerHome.findByCstCustomerCode(mdetails.getTiSlSubledgerCode(), AD_CMPNY);
                    glTaxInterface.setTiSlCode(arCustomer.getCstCode());
                    glTaxInterface.setTiSlSubledgerCode(arCustomer.getCstCustomerCode());
                    glTaxInterface.setTiSlName(arCustomer.getCstName());
                    glTaxInterface.setTiSlTin(arCustomer.getCstTin());
                    glTaxInterface.setTiSlAddress(arCustomer.getCstAddress());

                } else {

                    glTaxInterface.setTiIsArDocument(EJBCommon.FALSE);
                    glTaxInterface.setTiTxnReferenceNumber(mdetails.getTiTxnReferenceNumber());

                    if (mdetails.getTiTcName() != null) {

                        LocalApTaxCode apTaxCode = apTaxCodeHome.findByTcName(mdetails.getTiTcName(), AD_CMPNY);

                        double NET_AMNT = DR_AMNT / (apTaxCode.getTcRate() / 100);

                        glTaxInterface.setTiNetAmount(NET_AMNT);
                        glTaxInterface.setTiTcCode(apTaxCode.getTcCode());

                    } else {

                        LocalApWithholdingTaxCode apWithholdingTaxCode = apWithholdingTaxCodeHome.findByWtcName(mdetails.getTiWtcName(), AD_CMPNY);

                        double NET_AMNT = DR_AMNT / (apWithholdingTaxCode.getWtcRate() / 100);

                        glTaxInterface.setTiNetAmount(NET_AMNT);
                        glTaxInterface.setTiWtcCode(apWithholdingTaxCode.getWtcCode());
                    }

                    LocalApSupplier apSupplier = apSupplierHome.findBySplSupplierCode(mdetails.getTiSlSubledgerCode(), AD_CMPNY);
                    glTaxInterface.setTiSlCode(apSupplier.getSplCode());
                    glTaxInterface.setTiSlSubledgerCode(apSupplier.getSplSupplierCode());
                    glTaxInterface.setTiSlName(apSupplier.getSplName());
                    glTaxInterface.setTiSlTin(apSupplier.getSplTin());
                    glTaxInterface.setTiSlAddress(apSupplier.getSplAddress());
                    glTaxInterface.setTiTxnReferenceNumber(mdetails.getTiTxnReferenceNumber());
                }
            }

        } catch (GlobalRecordAlreadyDeletedException ex) {

            getSessionContext().setRollbackOnly();
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                // Get functional currency rate

                LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                if (!FC_NM.equals("USD")) {

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlTaxInterfaceMaintenanceControllerBean ejbCreate");
    }
}