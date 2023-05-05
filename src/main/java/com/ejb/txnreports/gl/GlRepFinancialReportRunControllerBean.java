package com.ejb.txnreports.gl;

import java.util.*;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.dao.ad.LocalAdBankAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.entities.ap.LocalApVoucher;
import com.ejb.dao.ap.LocalApVoucherHome;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.dao.ar.LocalArDistributionRecordHome;
import com.ejb.entities.ar.LocalArInvoice;
import com.ejb.dao.ar.LocalArInvoiceHome;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.dao.cm.LocalCmDistributionRecordHome;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenQualifier;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlAccountingCalendarValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountBalanceHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.dao.gl.ILocalGlSetOfBookHome;
import com.ejb.entities.gl.LocalGlAccountingCalendar;
import com.ejb.entities.gl.LocalGlAccountingCalendarValue;
import com.ejb.entities.gl.LocalGlBudget;
import com.ejb.entities.gl.LocalGlBudgetAmountCoa;
import com.ejb.dao.gl.LocalGlBudgetAmountCoaHome;
import com.ejb.entities.gl.LocalGlBudgetAmountPeriod;
import com.ejb.dao.gl.LocalGlBudgetAmountPeriodHome;
import com.ejb.dao.gl.LocalGlBudgetHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlChartOfAccountBalance;
import com.ejb.entities.gl.LocalGlFrgAccountAssignment;
import com.ejb.entities.gl.LocalGlFrgCalculation;
import com.ejb.dao.gl.LocalGlFrgCalculationHome;
import com.ejb.entities.gl.LocalGlFrgColumn;
import com.ejb.dao.gl.LocalGlFrgColumnHome;
import com.ejb.entities.gl.LocalGlFrgFinancialReport;
import com.ejb.dao.gl.LocalGlFrgFinancialReportHome;
import com.ejb.entities.gl.LocalGlFrgRow;
import com.ejb.dao.gl.LocalGlFrgRowHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyHome;
import com.ejb.entities.gl.LocalGlFunctionalCurrencyRate;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.gl.LocalGlJournalLine;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.entities.gl.LocalGlSetOfBook;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModAccountingCalendarValueDetails;
import com.util.reports.gl.GlRepFinancialReportDetails;

@Stateless(name = "GlRepFinancialReportRunControllerEJB")
public class GlRepFinancialReportRunControllerBean extends EJBContextClass implements GlRepFinancialReportRunController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private ILocalGlSetOfBookHome glSetOfBookHome;
    @EJB
    private ILocalGlAccountingCalendarValueHome glAccountingCalendarValueHome;
    @EJB
    private ILocalGlChartOfAccountBalanceHome glChartOfAccountBalanceHome;
    @EJB
    private LocalAdBankAccountHome adBankAccountHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalApVoucherHome apVoucherHome;
    @EJB
    private LocalArDistributionRecordHome arDistributionRecordHome;
    @EJB
    private LocalArInvoiceHome arInvoiceHome;
    @EJB
    private LocalCmDistributionRecordHome cmDistributionRecordHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlBudgetAmountCoaHome glBudgetAmountCoaHome;
    @EJB
    private LocalGlBudgetAmountPeriodHome glBudgetAmountPeriodHome;
    @EJB
    private LocalGlBudgetHome glBudgetHome;
    @EJB
    private LocalGlFrgCalculationHome glFrgCalculationHome;
    @EJB
    private LocalGlFrgColumnHome glFrgColumnHome;
    @EJB
    private LocalGlFrgFinancialReportHome glFrgFinancialReportHome;
    @EJB
    private LocalGlFrgRowHome glFrgRowHome;
    @EJB
    private LocalGlFunctionalCurrencyHome glFunctionalCurrencyHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalLineHome;


    public ArrayList getGlFrgFrAll(Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlFrgFrAll");

        ArrayList list = new ArrayList();

        try {

            Collection glFrgFinancialReports = glFrgFinancialReportHome.findFrAll(AD_CMPNY);

            for (Object frgFinancialReport : glFrgFinancialReports) {

                LocalGlFrgFinancialReport glFrgFinancialReport = (LocalGlFrgFinancialReport) frgFinancialReport;

                list.add(glFrgFinancialReport.getFrName());
            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlReportableAcvAll(Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlReportableAcvAll");

        ArrayList list = new ArrayList();

        try {

            Collection glSetOfBooks = glSetOfBookHome.findSobAll(AD_CMPNY);

            for (Object setOfBook : glSetOfBooks) {

                LocalGlSetOfBook glSetOfBook = (LocalGlSetOfBook) setOfBook;

                Collection glAccountingCalendarValues = glAccountingCalendarValueHome.findReportableAcvByAcCodeAndAcvStatus(glSetOfBook.getGlAccountingCalendar().getAcCode(), 'O', 'C', 'P', AD_CMPNY);

                for (Object accountingCalendarValue : glAccountingCalendarValues) {

                    LocalGlAccountingCalendarValue glAccountingCalendarValue = (LocalGlAccountingCalendarValue) accountingCalendarValue;

                    GlModAccountingCalendarValueDetails mdetails = new GlModAccountingCalendarValueDetails();

                    mdetails.setAcvPeriodPrefix(glAccountingCalendarValue.getAcvPeriodPrefix());

                    // get year

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(glAccountingCalendarValue.getAcvDateTo());

                    mdetails.setAcvYear(gc.get(Calendar.YEAR));

                    // is current

                    gc = EJBCommon.getGcCurrentDateWoTime();

                    if ((glAccountingCalendarValue.getAcvDateFrom().before(gc.getTime()) || glAccountingCalendarValue.getAcvDateFrom().equals(gc.getTime())) && (glAccountingCalendarValue.getAcvDateTo().after(gc.getTime()) || glAccountingCalendarValue.getAcvDateTo().equals(gc.getTime()))) {

                        mdetails.setAcvCurrent(true);
                    }

                    list.add(mdetails);
                }
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public GlRepFinancialReportDetails getGlRepFrParameters(String FR_NM, String FR_PRD, int FR_YR, Date FR_DT, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlRepFrParameters");

        try {

            // get functional currency name

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder FR_FC_NM = new StringBuilder(adCompany.getGlFunctionalCurrency().getFcName());

            // get report width

            LocalGlFrgFinancialReport glFrgFinancialReport = glFrgFinancialReportHome.findByFrName(FR_NM, AD_CMPNY);

            Collection glFrgColumns = glFrgFinancialReport.getGlFrgColumnSet().getGlFrgColumns();

            for (Object frgColumn : glFrgColumns) {

                LocalGlFrgColumn glFrgColumn = (LocalGlFrgColumn) frgColumn;

                if (glFrgColumn.getGlFunctionalCurrency() != null && !glFrgColumn.getGlFunctionalCurrency().getFcName().equals(adCompany.getGlFunctionalCurrency().getFcName())) {

                    FR_FC_NM.append(", ").append(glFrgColumn.getGlFunctionalCurrency().getFcName());
                }
            }

            int FR_WIDTH = glFrgColumns.size() * (75 + glFrgFinancialReport.getFrFontSize()) + 400;

            return new GlRepFinancialReportDetails(glFrgFinancialReport.getFrTitle(), FR_DT, FR_PRD, FR_YR, FR_FC_NM.toString(), FR_WIDTH, glFrgFinancialReport.getFrFontSize(), glFrgFinancialReport.getFrFontStyle(), glFrgFinancialReport.getFrHorizontalAlign());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlRepFrColumnHeadings(String FR_NM, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlRepFrColumnHeadings");

        ArrayList list = new ArrayList();

        try {

            // get column headings

            Collection glFrgColumns = glFrgColumnHome.findByFrName(FR_NM, AD_CMPNY);

            for (Object frgColumn : glFrgColumns) {

                LocalGlFrgColumn glFrgColumn = (LocalGlFrgColumn) frgColumn;

                GlRepFinancialReportDetails details = new GlRepFinancialReportDetails(glFrgColumn.getColName(), glFrgColumn.getColPosition());

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlRepFrDetails(String FR_NM, String FR_PRD, int FR_YR, Date FR_DT, Date CONVERSION_DATE, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlRepFrDetails");

        LocalGlSetOfBook glSetOfBook = null;

        ArrayList list = new ArrayList();

        try {

            // get columns
            Collection glFrgColumns = glFrgColumnHome.findByFrName(FR_NM, AD_CMPNY);

            // initialize unrealized forex gain/loss array
            ArrayList forexColumns = new ArrayList(glFrgColumns.size() + 1);
            for (int ctr = 0; ctr < glFrgColumns.size() + 1; ctr++) {
                forexColumns.add((double) 0);
            }

            // initialize hashmap for calculations

            HashMap map = new HashMap();

            // get set of book

            Collection glSetOfBooks = glSetOfBookHome.findByAcvPeriodPrefixAndDate(FR_PRD, EJBCommon.getIntendedDate(FR_YR), AD_CMPNY);
            ArrayList glSetOfBookList = new ArrayList(glSetOfBooks);
            glSetOfBook = (LocalGlSetOfBook) glSetOfBookList.get(0);

            // get row data

            Collection glFrgRows = glFrgRowHome.findByFrName(FR_NM, AD_CMPNY);

            for (Object frgRow : glFrgRows) {

                LocalGlFrgRow glFrgRow = (LocalGlFrgRow) frgRow;

                // get column data
                // glFrgColumns = glFrgColumnHome.findByFrName(FR_NM, AD_CMPNY);

                Iterator j = glFrgColumns.iterator();

                ArrayList columnList = new ArrayList();

                int firstColumnPosition = 0;
                int lastColumnPosition = 350;
                int index = 0;

                while (j.hasNext()) {

                    LocalGlFrgColumn glFrgColumn = (LocalGlFrgColumn) j.next();

                    // get last column position

                    if (firstColumnPosition == 0) firstColumnPosition = glFrgColumn.getColPosition();

                    lastColumnPosition = glFrgColumn.getColPosition();

                    // get column amount

                    if (glFrgRow.getGlFrgAccountAssignments().size() > 0 && glFrgColumn.getGlFrgCalculations().size() == 0) {

                        // get acv period

                        LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodPrefix(glSetOfBook.getGlAccountingCalendar().getAcCode(), FR_PRD, AD_CMPNY);

                        // get column period

                        int YEARS_DIFF = (int) Math.ceil(((double) (glAccountingCalendarValue.getAcvPeriodNumber() + glFrgColumn.getColOffset())) / glAccountingCalendarValue.getGlAccountingCalendar().getGlPeriodType().getPtPeriodPerYear()) - 1;

                        short COL_PERIOD_NUMBER = (short) Math.abs(Math.abs(glAccountingCalendarValue.getAcvPeriodNumber() + glFrgColumn.getColOffset()) - (glAccountingCalendarValue.getGlAccountingCalendar().getGlPeriodType().getPtPeriodPerYear() * Math.abs(YEARS_DIFF)));

                        try {

                            LocalGlSetOfBook glColumnSetOfBook = glSetOfBookHome.findByAcYear(glSetOfBook.getGlAccountingCalendar().getAcYear() + YEARS_DIFF, AD_CMPNY);

                            forexColumns = this.getFrgColumnAmount(glFrgColumn, glFrgRow, glColumnSetOfBook, COL_PERIOD_NUMBER, CONVERSION_DATE, forexColumns, ++index, AD_CMPNY);

                            Double COLUMN_AMOUNT = (Double) forexColumns.get(0);
                            Debug.print("z" + COLUMN_AMOUNT);
                            // double COLUMN_AMOUNT = this.getFrgColumnAmount(glFrgColumn, glFrgRow,
                            // glColumnSetOfBook, COL_PERIOD_NUMBER, CONVERSION_DATE, AD_CMPNY);

                            // add to column list

                            GlRepFinancialReportDetails colDetails = new GlRepFinancialReportDetails(glFrgColumn.getColPosition(), glFrgColumn.getColFormatMask(), glFrgColumn.getColFactor(), COLUMN_AMOUNT);
                            if (COLUMN_AMOUNT > 0) columnList.add(colDetails);

                            // put to hashmap to be used by calculations

                            map.put("KEY" + glFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber(), COLUMN_AMOUNT);

                        } catch (FinderException ex) {

                            GlRepFinancialReportDetails colDetails = new GlRepFinancialReportDetails(glFrgColumn.getColPosition(), glFrgColumn.getColFormatMask(), glFrgColumn.getColFactor(), 0.0d);

                            columnList.add(colDetails);

                            // put to hashmap to be used by calculations

                            map.put("KEY" + glFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber(), 0d);
                        }

                    } else if (glFrgRow.getGlFrgAccountAssignments().size() == 0 || (glFrgColumn.getGlFrgCalculations().size() > 0 && glFrgRow.getGlFrgAccountAssignments().size() > 0)) {

                        // get column amount using calculations
                        Double COLUMN_AMOUNT = 0d;

                        Collection glFrgRowCalculations = glFrgCalculationHome.findByRowCodeAndCalType(glFrgRow.getRowCode(), "C1", AD_CMPNY);
                        Collection glFrgColumnCalculations = glFrgCalculationHome.findByColCode(glFrgColumn.getColCode(), AD_CMPNY);

                        if (glFrgRowCalculations.size() > 0 && glFrgColumnCalculations.size() == 0) {

                            COLUMN_AMOUNT = this.executeFrgCalculations(glFrgColumn, glFrgRow, glFrgRowCalculations, map, true, AD_CMPNY);

                        } else if (glFrgRowCalculations.size() == 0 && glFrgColumnCalculations.size() > 0) {

                            COLUMN_AMOUNT = this.executeFrgCalculations(glFrgColumn, glFrgRow, glFrgColumnCalculations, map, false, AD_CMPNY);

                        } else if (glFrgRowCalculations.size() > 0 && glFrgColumnCalculations.size() > 0) {

                            if (glFrgRow.getRowOverrideColumnCalculation() == EJBCommon.TRUE && glFrgColumn.getColOverrideRowCalculation() == EJBCommon.FALSE) {

                                COLUMN_AMOUNT = this.executeFrgCalculations(glFrgColumn, glFrgRow, glFrgRowCalculations, map, true, AD_CMPNY);

                            } else {

                                COLUMN_AMOUNT = this.executeFrgCalculations(glFrgColumn, glFrgRow, glFrgColumnCalculations, map, false, AD_CMPNY);
                            }
                        }

                        // add to column list
                        if (glFrgRowCalculations.size() != 0 || glFrgColumnCalculations.size() != 0) {

                            GlRepFinancialReportDetails colDetails = new GlRepFinancialReportDetails(glFrgColumn.getColPosition(), glFrgColumn.getColFormatMask(), glFrgColumn.getColFactor(), COLUMN_AMOUNT);

                            columnList.add(colDetails);

                            // put to hashmap to be used by other calculations
                            map.put("KEY" + glFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber(), COLUMN_AMOUNT);
                        }

                    } else {

                        // put to hashmap to be used by other calculations

                        map.put("KEY" + glFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber(), 0d);
                    }

                    // For C2 Calculation

                    Double COLUMN_AMOUNT = 0d;

                    // if row has C2
                    Collection glFrgRowCalculations = glFrgCalculationHome.findByRowCodeAndCalType(glFrgRow.getRowCode(), "C2", AD_CMPNY);

                    if (glFrgRowCalculations.size() > 0) {
                        COLUMN_AMOUNT = this.executeFrgCalculations(glFrgColumn, glFrgRow, glFrgRowCalculations, map, true, AD_CMPNY);

                        // add to column list
                        GlRepFinancialReportDetails colDetails = new GlRepFinancialReportDetails(glFrgColumn.getColPosition() + 60, glFrgColumn.getColFormatMask(), glFrgColumn.getColFactor(), COLUMN_AMOUNT);

                        columnList.add(colDetails);
                    }
                }

                GlRepFinancialReportDetails details = new GlRepFinancialReportDetails(glFrgRow.getRowName(), glFrgRow.getRowIndent(), glFrgRow.getRowLineToSkipBefore(), glFrgRow.getRowLineToSkipAfter(), glFrgRow.getRowUnderlineCharacterBefore(), glFrgRow.getRowUnderlineCharacterAfter(), glFrgRow.getRowPageBreakBefore(), glFrgRow.getRowPageBreakAfter(), glFrgRow.getRowFontStyle(), glFrgRow.getRowHorizontalAlign(), glFrgRow.getRowHideRow(), firstColumnPosition > 0 ? firstColumnPosition : 350, lastColumnPosition, columnList);

                list.add(details);
            }

            // add forex row
            boolean showUnrealized = false;
            ArrayList columnList = new ArrayList();

            Iterator iter = glFrgColumns.iterator();

            int firstColumnPosition = 0;
            int lastColumnPosition = 350;
            int index = 0;

            while (iter.hasNext()) {

                LocalGlFrgColumn glFrgColumn = (LocalGlFrgColumn) iter.next();

                if (firstColumnPosition == 0) firstColumnPosition = glFrgColumn.getColPosition();

                lastColumnPosition = glFrgColumn.getColPosition();

                Double FOREX_AMOUNT = (Double) forexColumns.get(++index);

                GlRepFinancialReportDetails colDetails = new GlRepFinancialReportDetails(glFrgColumn.getColPosition(), glFrgColumn.getColFormatMask(), glFrgColumn.getColFactor(), FOREX_AMOUNT);

                columnList.add(colDetails);

                if (FOREX_AMOUNT != 0) showUnrealized = true;
            }

            if (showUnrealized) {

                GlRepFinancialReportDetails details = new GlRepFinancialReportDetails("Unrealized ForEx Gain/Loss", 0, 1, 0, 0, 0, 0, 0, "BOLD", "Left", (byte) 1, firstColumnPosition > 0 ? firstColumnPosition : 350, lastColumnPosition, columnList);

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods

    private ArrayList getFrgColumnAmount(LocalGlFrgColumn glFrgColumn, LocalGlFrgRow glFrgRow, LocalGlSetOfBook glSetOfBook, short PERIOD_NUMBER, Date CONVERSION_DATE, ArrayList forexColumns, int index, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getFrgColumnAmount");

        String strSeparator = null;
        short genNumberOfSegment = 0;
        double COLUMN_AMOUNT = 0d;
        double REVAL_AMOUNT = 0d;


        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            char chrSeparator = genField.getFlSegmentSeparator();
            strSeparator = String.valueOf(chrSeparator);
            genNumberOfSegment = genField.getFlNumberOfSegment();

            // get accounting calendar value

            LocalGlAccountingCalendar glAccountingCalendar = glSetOfBook.getGlAccountingCalendar();
            LocalGlAccountingCalendarValue glAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), PERIOD_NUMBER, AD_CMPNY);

            // get amount type beginning period number

            short periodNumber = 0;
            String AMNT_TYP = glFrgColumn.getColAmountType().substring(0, glFrgColumn.getColAmountType().indexOf('-'));
            String BLNC_TYP = glFrgColumn.getColAmountType().substring(glFrgColumn.getColAmountType().indexOf('-') + 1);

            switch (AMNT_TYP) {
                case "YTD":

                    periodNumber = 1;

                    break;
                case "QTD":

                    Collection glQuarterAccountingCalendarValues = glAccountingCalendarValueHome.findByAcCodeAndAcvQuarterNumber(glAccountingCalendar.getAcCode(), glAccountingCalendarValue.getAcvQuarter(), AD_CMPNY);

                    ArrayList glQuarterAccountingCalendarValueList = new ArrayList(glQuarterAccountingCalendarValues);

                    LocalGlAccountingCalendarValue glQuarterAccountingCalendarValue = (LocalGlAccountingCalendarValue) glQuarterAccountingCalendarValueList.get(0);

                    periodNumber = glQuarterAccountingCalendarValue.getAcvPeriodNumber();

                    break;
                case "PTD":

                    periodNumber = glAccountingCalendarValue.getAcvPeriodNumber();
                    break;
            }

            double CONVERSION_RATE = 1;
            double TEMP_AMOUNT = 0d;

            Collection glFrgAccountAssignments = glFrgRow.getGlFrgAccountAssignments();

            for (Object frgAccountAssignment : glFrgAccountAssignments) {

                LocalGlFrgAccountAssignment glFrgAccountAssignment = (LocalGlFrgAccountAssignment) frgAccountAssignment;

                // get coa selected

                StringBuilder jbossQl = new StringBuilder();
                jbossQl.append("SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE ");

                StringTokenizer stAccountFrom = new StringTokenizer(glFrgAccountAssignment.getAaAccountLow(), strSeparator);
                StringTokenizer stAccountTo = new StringTokenizer(glFrgAccountAssignment.getAaAccountHigh(), strSeparator);

                StringBuilder var = new StringBuilder("1");

                if (genNumberOfSegment > 1) {

                    for (int i = 0; i < genNumberOfSegment; i++) {

                        if (i == 0 && i < genNumberOfSegment - 1) {

                            // for first segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, 1)+1 ");

                        } else if (i != 0 && i < genNumberOfSegment - 1) {

                            // for middle segments

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, ").append(var).append(")) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' AND ");

                            var = new StringBuilder("LOCATE('" + strSeparator + "', coa.coaAccountNumber, " + var + ")+1 ");

                        } else if (i != 0 && i == genNumberOfSegment - 1) {

                            // for last segment

                            jbossQl.append("SUBSTRING(coa.coaAccountNumber, ").append(var).append(", (LENGTH(coa.coaAccountNumber)+1) - (").append(var).append(")) BETWEEN '").append(stAccountFrom.nextToken()).append("' AND '").append(stAccountTo.nextToken()).append("' ");
                        }
                    }

                } else if (genNumberOfSegment == 1) {

                    String accountFrom = stAccountFrom.nextToken();
                    String accountTo = stAccountTo.nextToken();

                    jbossQl.append("SUBSTRING(coa.coaAccountNumber, 1, LOCATE('").append(strSeparator).append("', coa.coaAccountNumber, 1)-1) BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' OR ").append("coa.coaAccountNumber BETWEEN '").append(accountFrom).append("' AND '").append(accountTo).append("' ");
                }

                jbossQl.append("AND coa.coaAdCompany=").append(AD_CMPNY).append(" ");

                Object[] obj = new Object[0];

                // Debug.print(jbossQl.toString());

                Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria(jbossQl.toString(), obj, 0, 0);

                for (Object chartOfAccount : glChartOfAccounts) {

                    LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) chartOfAccount;

                    String ACCOUNT_TYPE = null;

                    // get coa account type

                    Collection genSegments = genSegmentHome.findByFlCode(genField.getFlCode(), AD_CMPNY);

                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), strSeparator);

                    Iterator l = genSegments.iterator();

                    while (st.hasMoreTokens()) {

                        LocalGenSegment genSegment = (LocalGenSegment) l.next();

                        LocalGenValueSet genValueSet = genSegment.getGenValueSet();

                        LocalGenValueSetValue genValueSetValue = genValueSetValueHome.findByVsCodeAndVsvValue(genValueSet.getVsCode(), st.nextToken(), AD_CMPNY);

                        if (genSegment.getSgSegmentType() == 'N') {

                            LocalGenQualifier genQualifier = genValueSetValue.getGenQualifier();
                            ACCOUNT_TYPE = genQualifier.getQlAccountType();

                            break;
                        }
                    }

                    // get coa balance

                    double BEGINNING_BALANCE = 0d;
                    double ACTIVITY = 0d;
                    int runningPeriod = periodNumber;

                    if (BLNC_TYP.equals("Actual")) {

                        while (runningPeriod <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glAccountingCalendar.getAcCode(), (short) runningPeriod, AD_CMPNY);

                            LocalGlChartOfAccountBalance glChartOfAccountBalance = glChartOfAccountBalanceHome.findByAcvCodeAndCoaCode(glRunningAccountingCalendarValue.getAcvCode(), glChartOfAccount.getCoaCode(), AD_CMPNY);

                            if (runningPeriod == periodNumber)
                                BEGINNING_BALANCE = glChartOfAccountBalance.getCoabBeginningBalance();

                            switch (glFrgAccountAssignment.getAaActivityType()) {
                                case "DR":

                                    COLUMN_AMOUNT += glChartOfAccountBalance.getCoabTotalDebit();

                                    break;
                                case "CR":

                                    COLUMN_AMOUNT += glChartOfAccountBalance.getCoabTotalCredit();

                                    break;
                                case "NET":
                                case "END":

                                    if ("ASSET".equals(ACCOUNT_TYPE) || "EXPENSE".equals(ACCOUNT_TYPE)) {

                                        ACTIVITY += (glChartOfAccountBalance.getCoabTotalDebit() - glChartOfAccountBalance.getCoabTotalCredit());

                                    } else {

                                        ACTIVITY += (glChartOfAccountBalance.getCoabTotalCredit() - glChartOfAccountBalance.getCoabTotalDebit());
                                    }
                                    break;
                            }

                            runningPeriod++;
                        }

                        double COA_AMOUNT = 0d;

                        switch (glFrgAccountAssignment.getAaActivityType()) {
                            case "BEG":

                                COLUMN_AMOUNT += BEGINNING_BALANCE;
                                COA_AMOUNT = BEGINNING_BALANCE;

                                break;
                            case "NET":

                                COLUMN_AMOUNT += ACTIVITY;
                                COA_AMOUNT = ACTIVITY;

                                break;
                            case "END":

                                COLUMN_AMOUNT += BEGINNING_BALANCE + ACTIVITY;
                                COA_AMOUNT = BEGINNING_BALANCE + ACTIVITY;
                                break;
                        }

                        // revaluation

                        LocalGlFunctionalCurrency glCoaCurrency = adCompany.getGlFunctionalCurrency();
                        if (glChartOfAccount.getGlFunctionalCurrency() != null)
                            glCoaCurrency = glChartOfAccount.getGlFunctionalCurrency();

                        if (Objects.equals(glFrgColumn.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) {
                            if ("ASSET".equals(ACCOUNT_TYPE) || "LIABILITY".equals(ACCOUNT_TYPE) || "OWNERS EQUITY".equals(ACCOUNT_TYPE)) {
                                if (!Objects.equals(glCoaCurrency.getFcCode(), glFrgColumn.getGlFunctionalCurrency().getFcCode())) {

                                    if (glChartOfAccount.getCoaForRevaluation() == EJBCommon.TRUE) {

                                        REVAL_AMOUNT += this.getRevaluatedColumnAmountInFunctionalCurrency(glChartOfAccount, glAccountingCalendarValue, glCoaCurrency, COA_AMOUNT, CONVERSION_DATE, ACCOUNT_TYPE, AD_CMPNY);
                                    } else {

                                        // accounts that must not be revalued
                                        CONVERSION_RATE = this.getHistoricalRate(glChartOfAccount, glAccountingCalendarValue, glCoaCurrency, COA_AMOUNT, CONVERSION_DATE, ACCOUNT_TYPE, AD_CMPNY);

                                        REVAL_AMOUNT += this.getCoaRevaluationAmount(glChartOfAccount, glAccountingCalendarValue, glFrgColumn.getGlFunctionalCurrency(), AD_CMPNY);
                                    }
                                }
                            }

                        } else {

                            // if column is set as USD, automatic revaluation regardless of account type
                            if (glChartOfAccount.getCoaAccountType().equals("REVENUE") || glChartOfAccount.getCoaAccountType().equals("EXPENSE")) {

                                Debug.print("****************************");
                                Debug.print("Account Number : " + glChartOfAccount.getCoaAccountNumber() + "\t" + COA_AMOUNT);

                                if (COA_AMOUNT != 0) {

                                    CONVERSION_RATE = this.getHistoricalRate(glChartOfAccount, glAccountingCalendarValue, glCoaCurrency, COLUMN_AMOUNT, CONVERSION_DATE, ACCOUNT_TYPE, AD_CMPNY);
                                    TEMP_AMOUNT += (COA_AMOUNT / CONVERSION_RATE);

                                    Debug.print("CONVERSION RATE : " + "\t" + CONVERSION_RATE);
                                    System.out.println("COA_AMOUNT /CONVESION_RATE : " + (COA_AMOUNT / CONVERSION_RATE));
                                    Debug.print("------------------------");
                                }

                            } else {
                                REVAL_AMOUNT += this.getRevaluatedColumnAmountInForeignCurrency(glChartOfAccount, glAccountingCalendarValue, glCoaCurrency, COA_AMOUNT, CONVERSION_DATE, ACCOUNT_TYPE, AD_CMPNY);
                            }
                        }

                    } else { // Budget Balance Type

                        LocalGlBudget glCurrentBudget = null;
                        LocalGlBudgetAmountCoa glBudgetAmountCoa = null;

                        try {

                            glCurrentBudget = glBudgetHome.findByBgtStatus("CURRENT", AD_CMPNY);
                            glBudgetAmountCoa = glBudgetAmountCoaHome.findByCoaCodeAndBgtCode(glChartOfAccount.getCoaCode(), glCurrentBudget.getBgtCode(), AD_CMPNY);

                        } catch (FinderException ex) {

                            continue;
                        }

                        while (runningPeriod <= glAccountingCalendarValue.getAcvPeriodNumber()) {

                            try {

                                LocalGlAccountingCalendarValue glRunningAccountingCalendarValue = glAccountingCalendarValueHome.findByAcCodeAndAcvPeriodNumber(glSetOfBook.getGlAccountingCalendar().getAcCode(), (short) runningPeriod, AD_CMPNY);

                                LocalGlBudgetAmountPeriod glBudgetAmountPeriod = glBudgetAmountPeriodHome.findByBcCodeAndAcvCode(glBudgetAmountCoa.getBcCode(), glRunningAccountingCalendarValue.getAcvCode(), AD_CMPNY);

                                COLUMN_AMOUNT += glBudgetAmountPeriod.getBapAmount();

                                runningPeriod++;

                            } catch (FinderException ex) {
                            }
                        }
                    }
                }
            }

            if (!Objects.equals(glFrgColumn.getGlFunctionalCurrency().getFcCode(), adCompany.getGlFunctionalCurrency().getFcCode())) {

                Debug.print("TEMP_AMOUNT : " + TEMP_AMOUNT);
                Debug.print("COLUMN/REVAL AMOUNT : " + COLUMN_AMOUNT + "\t" + REVAL_AMOUNT);

                if (TEMP_AMOUNT != 0d)
                    // COLUMN_AMOUNT = TEMP_AMOUNT;
                    forexColumns.set(0, TEMP_AMOUNT);
                else
                    forexColumns.set(0, this.convertFunctionalToForeignCurrency(glFrgColumn.getGlFunctionalCurrency().getFcCode(), glFrgColumn.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, CONVERSION_RATE, COLUMN_AMOUNT + REVAL_AMOUNT, AD_CMPNY));

                forexColumns.set(index, ((Double) forexColumns.get(index) + this.convertFunctionalToForeignCurrency(glFrgColumn.getGlFunctionalCurrency().getFcCode(), glFrgColumn.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, CONVERSION_RATE, REVAL_AMOUNT, AD_CMPNY)));

            } else {

                // get REVAL of coa where forRevaluation=true
                // if coa where forRevaluation=true, COA_AMOUNT + REVAL, else COA_AMOUNT only

                forexColumns.set(0, COLUMN_AMOUNT + REVAL_AMOUNT);
                forexColumns.set(index, ((Double) forexColumns.get(index) + REVAL_AMOUNT));
            }

            // return COLUMN_AMOUNT;
            return forexColumns;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private Double executeFrgCalculations(LocalGlFrgColumn glFrgColumn, LocalGlFrgRow glFrgRow, Collection glFrgCalculations, HashMap map, boolean IS_ROW_CALCULATION, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean executeFrgCalculations");

        double COLUMN_AMOUNT = 0d;


        for (Object frgCalculation : glFrgCalculations) {

            LocalGlFrgCalculation glFrgCalculation = (LocalGlFrgCalculation) frgCalculation;

            Debug.print("z " + glFrgCalculation.getCalRowColName());

            try {

                switch (glFrgCalculation.getCalOperator()) {
                    case "ENTER":
                    case "AVERAGE":

                        COLUMN_AMOUNT = this.getFrgCalculationValue(glFrgCalculation, glFrgColumn, glFrgRow, map, IS_ROW_CALCULATION, AD_CMPNY);

                        break;
                    case "+":

                        COLUMN_AMOUNT += this.getFrgCalculationValue(glFrgCalculation, glFrgColumn, glFrgRow, map, IS_ROW_CALCULATION, AD_CMPNY);

                        break;
                    case "-":

                        COLUMN_AMOUNT -= this.getFrgCalculationValue(glFrgCalculation, glFrgColumn, glFrgRow, map, IS_ROW_CALCULATION, AD_CMPNY);

                        break;
                    case "*":

                        COLUMN_AMOUNT *= this.getFrgCalculationValue(glFrgCalculation, glFrgColumn, glFrgRow, map, IS_ROW_CALCULATION, AD_CMPNY);

                        break;
                    case "/":

                        double VAL = this.getFrgCalculationValue(glFrgCalculation, glFrgColumn, glFrgRow, map, IS_ROW_CALCULATION, AD_CMPNY);

                        if (VAL == 0) {

                            COLUMN_AMOUNT = 0d;

                        } else {

                            COLUMN_AMOUNT /= VAL;
                        }

                        break;
                    case "%":

                        COLUMN_AMOUNT = COLUMN_AMOUNT * 100;

                        break;
                    case "ABSTVAL":

                        COLUMN_AMOUNT = Math.abs(COLUMN_AMOUNT);
                        break;
                }
            } catch (NullPointerException Ex) {
                return null;
            }
        }

        return COLUMN_AMOUNT;
    }

    private Double getFrgCalculationValue(LocalGlFrgCalculation glFrgCalculation, LocalGlFrgColumn glFrgColumn, LocalGlFrgRow glFrgRow, HashMap map, boolean IS_ROW_CALCULATION, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getFrgCalculationValue");

        double CAL_VALUE = 0d;

        try {

            if (glFrgCalculation.getCalSequenceLow() > 0 && glFrgCalculation.getCalSequenceHigh() > 0) {

                if (IS_ROW_CALCULATION) {

                    Collection glCalculationFrgRows = glFrgRowHome.findByRsName(glFrgCalculation.getGlFrgRow().getGlFrgRowSet().getRsName(), AD_CMPNY);

                    double SEQUENCE_RANGE_SUM = 0d;
                    int NUMBER_OF_LINES = 0;

                    for (Object calculationFrgRow : glCalculationFrgRows) {

                        LocalGlFrgRow glCalculationFrgRow = (LocalGlFrgRow) calculationFrgRow;

                        if (glCalculationFrgRow.getRowLineNumber() >= glFrgCalculation.getCalSequenceLow() && glCalculationFrgRow.getRowLineNumber() <= glFrgCalculation.getCalSequenceHigh()) {

                            SEQUENCE_RANGE_SUM += (Double) (map.get("KEY" + glCalculationFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber()));

                            NUMBER_OF_LINES++;
                        }
                    }

                    if (glFrgCalculation.getCalOperator().equals("AVERAGE")) {

                        CAL_VALUE = SEQUENCE_RANGE_SUM / NUMBER_OF_LINES;

                    } else {

                        CAL_VALUE = SEQUENCE_RANGE_SUM;
                    }

                } else {

                    Collection glCalculationFrgColumns = glFrgColumnHome.findByCsName(glFrgCalculation.getGlFrgColumn().getGlFrgColumnSet().getCsName(), AD_CMPNY);

                    double SEQUENCE_RANGE_SUM = 0d;
                    int NUMBER_OF_LINES = 0;

                    for (Object calculationFrgColumn : glCalculationFrgColumns) {

                        LocalGlFrgColumn glCalculationFrgColumn = (LocalGlFrgColumn) calculationFrgColumn;

                        if (glCalculationFrgColumn.getColSequenceNumber() >= glFrgCalculation.getCalSequenceLow() && glCalculationFrgColumn.getColSequenceNumber() <= glFrgCalculation.getCalSequenceHigh()) {

                            SEQUENCE_RANGE_SUM += (Double) (map.get("KEY" + glFrgRow.getRowLineNumber() + glCalculationFrgColumn.getColSequenceNumber()));

                            NUMBER_OF_LINES++;
                        }
                    }

                    if (glFrgCalculation.getCalOperator().equals("AVERAGE")) {

                        CAL_VALUE = SEQUENCE_RANGE_SUM / NUMBER_OF_LINES;

                    } else {

                        CAL_VALUE = SEQUENCE_RANGE_SUM;
                    }
                }

            } else if (glFrgCalculation.getCalRowColName() != null) {

                if (IS_ROW_CALCULATION) {

                    LocalGlFrgRow glCalculationFrgRow = glFrgRowHome.findByRowNameAndRsName(glFrgCalculation.getCalRowColName(), glFrgCalculation.getGlFrgRow().getGlFrgRowSet().getRsName(), AD_CMPNY);
                    Debug.print("glCalculationFrgRow.getRowLineNumber(): " + glCalculationFrgRow.getRowLineNumber());
                    Debug.print("glFrgColumn.getColSequenceNumber(): " + glFrgColumn.getColSequenceNumber());

                    CAL_VALUE = (Double) (map.get("KEY" + glCalculationFrgRow.getRowLineNumber() + glFrgColumn.getColSequenceNumber()));

                } else {

                    LocalGlFrgColumn glCalculationFrgColumn = glFrgColumnHome.findByColNameAndCsName(glFrgCalculation.getCalRowColName(), glFrgCalculation.getGlFrgColumn().getGlFrgColumnSet().getCsName(), AD_CMPNY);

                    // KKK
                    try {

                        CAL_VALUE = (Double) (map.get("KEY" + glFrgRow.getRowLineNumber() + glCalculationFrgColumn.getColSequenceNumber()));

                    } catch (Exception ex) {

                        return null;
                    }
                }

            } else {

                CAL_VALUE = glFrgCalculation.getCalConstant();
            }

            return CAL_VALUE;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private double getRevaluatedColumnAmountInFunctionalCurrency(LocalGlChartOfAccount glChartOfAccount, LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlFunctionalCurrency glFunctionalCurrency, double COLUMN_AMOUNT, Date CONVERSION_DATE, String ACCOUNT_TYPE, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getRevaluatedColumnAmountToFunctionalCurrency");

        double REVAL_AMOUNT = COLUMN_AMOUNT;
        double HIST_CNVRSN = 0d;
        double HIST_TOTAL_DEBIT = 0d;
        double HIST_TOTAL_CREDIT = 0d;
        double CURR_CNVRSN = 0d;
        double CURR_TOTAL_DEBIT = 0d;
        double CURR_TOTAL_CREDIT = 0d;


        try {

            // find beg bal manual journals
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Collection glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "MANUAL", AD_CMPNY);

            Iterator i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && !glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {

                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // find beg bal journal reversals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "JOURNAL REVERSAL", AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && !glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {
                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get gl journals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && !glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {
                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap vouchers
            Collection apDrVouchers = apDistributionRecordHome.findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrVouchers.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap debit memoes

            Collection apDrDebitMemoes = apDistributionRecordHome.findVouDebitMemoByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDebitMemoes.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, apVoucher.getVouAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap checks
            Collection apDrChecks = apDistributionRecordHome.findChkByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (apDistributionRecord.getApAppliedVoucher() != null && apDistributionRecord.getDrClass().equals("PAYABLE")) {
                    LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();
                    glDrFunctionalCurrency = apVoucher.getGlFunctionalCurrency();
                    conversionDate = apVoucher.getVouConversionDate();
                    conversionRate = apVoucher.getVouConversionRate();
                } else {
                    LocalApCheck apCheck = apDistributionRecord.getApCheck();
                    glDrFunctionalCurrency = apCheck.getGlFunctionalCurrency();
                    conversionDate = apCheck.getChkConversionDate();
                    conversionRate = apCheck.getChkConversionRate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap direct checks
            Collection apDrDirectChecks = apDistributionRecordHome.findChkDirectByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDirectChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApCheck().getChkConversionDate(), apDistributionRecord.getApCheck().getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap receiving items
            Collection apDrReceivingItems = apDistributionRecordHome.findPoByDateAndCoaAccountNumberAndCurrencyAndPoPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrReceivingItems.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApPurchaseOrder().getPoConversionDate(), apDistributionRecord.getApPurchaseOrder().getPoConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar invoices
            Collection arDrInvoices = arDistributionRecordHome.findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrInvoices.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar credit memoes
            Collection arDrCreditMemoes = arDistributionRecordHome.findInvCreditMemoByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrCreditMemoes.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                LocalArInvoice arCreditedVoucher = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), arCreditedVoucher.getInvConversionDate(), arCreditedVoucher.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar receipts
            Collection arDrReceipts = arDistributionRecordHome.findRctByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (arDistributionRecord.getArAppliedInvoice() != null && arDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                    LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();
                    glDrFunctionalCurrency = arInvoice.getGlFunctionalCurrency();
                    conversionDate = arInvoice.getInvConversionDate();
                    conversionRate = arInvoice.getInvConversionRate();
                } else {
                    LocalArReceipt arReceipt = arDistributionRecord.getArReceipt();
                    glDrFunctionalCurrency = arReceipt.getGlFunctionalCurrency();
                    conversionDate = arReceipt.getRctConversionDate();
                    conversionRate = arReceipt.getRctConversionRate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            Collection arDrMiscReceipts = arDistributionRecordHome.findRctMiscByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrMiscReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArReceipt().getRctConversionDate(), arDistributionRecord.getArReceipt().getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get cm adjustment
            Collection cmDrAdjustments = cmDistributionRecordHome.findAdjByDateAndCoaAccountNumberAndCurrencyAndAdjPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmAdjustment().getAdjConversionDate(), cmDistributionRecord.getCmAdjustment().getAdjConversionRate(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, cmDistributionRecord.getDrAmount(), AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get cm fund transfers
            Collection cmDrFundTransfers = cmDistributionRecordHome.findFtByDateAndCoaAccountNumberAndCurrencyAndFtPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmDistributionRecord.getCmFundTransfer().getFtAdBaAccountFrom());

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmFundTransfer().getFtConversionDate(), cmDistributionRecord.getCmFundTransfer().getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, cmDistributionRecord.getDrAmount(), AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            if ("ASSET".equals(ACCOUNT_TYPE) || "EXPENSE".equals(ACCOUNT_TYPE)) {
                REVAL_AMOUNT = (CURR_TOTAL_DEBIT - CURR_TOTAL_CREDIT) - (HIST_TOTAL_DEBIT - HIST_TOTAL_CREDIT);
            } else {
                REVAL_AMOUNT = (CURR_TOTAL_CREDIT - CURR_TOTAL_DEBIT) - (HIST_TOTAL_CREDIT - HIST_TOTAL_DEBIT);
            }

            return REVAL_AMOUNT;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getRevaluatedColumnAmountInForeignCurrency(LocalGlChartOfAccount glChartOfAccount, LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlFunctionalCurrency glFunctionalCurrency, double COLUMN_AMOUNT, Date CONVERSION_DATE, String ACCOUNT_TYPE, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getRevaluatedColumnAmountInForeignCurrency");

        double REVAL_AMOUNT = COLUMN_AMOUNT;
        double HIST_CNVRSN = 0d;
        double HIST_TOTAL_DEBIT = 0d;
        double HIST_TOTAL_CREDIT = 0d;
        double CURR_CNVRSN = 0d;
        double CURR_TOTAL_DEBIT = 0d;
        double CURR_TOTAL_CREDIT = 0d;
        double FC_AMOUNT = 0d;

        try {

            // find beg bal manual journals
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Collection glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "MANUAL", AD_CMPNY);

            Iterator i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {
                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // find beg bal journal reversals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "JOURNAL REVERSAL", AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {
                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get gl journals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(adCompany.getGlFunctionalCurrency().getFcCode())) {
                    continue;
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION"))
                    HIST_CNVRSN = 0;

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap vouchers
            Collection apDrVouchers = apDistributionRecordHome.findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrVouchers.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap debit memoes
            Collection apDrDebitMemoes = apDistributionRecordHome.findVouDebitMemoByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDebitMemoes.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, apVoucher.getVouAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap checks
            Collection apDrChecks = apDistributionRecordHome.findChkByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (apDistributionRecord.getApAppliedVoucher() != null && apDistributionRecord.getDrClass().equals("PAYABLE")) {
                    LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();
                    glDrFunctionalCurrency = apVoucher.getGlFunctionalCurrency();
                    conversionDate = apVoucher.getVouConversionDate();
                    conversionRate = apVoucher.getVouConversionRate();
                } else {
                    LocalApCheck apCheck = apDistributionRecord.getApCheck();
                    glDrFunctionalCurrency = apCheck.getGlFunctionalCurrency();
                    conversionDate = apCheck.getChkConversionDate();
                    conversionRate = apCheck.getChkConversionRate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap direct checks
            Collection apDrDirectChecks = apDistributionRecordHome.findChkDirectByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDirectChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApCheck().getChkConversionDate(), apDistributionRecord.getApCheck().getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApCheck().getChkConversionDate(), apDistributionRecord.getApCheck().getChkConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ap receiving items
            Collection apDrReceivingItems = apDistributionRecordHome.findPoByDateAndCoaAccountNumberAndCurrencyAndPoPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrReceivingItems.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApPurchaseOrder().getPoConversionDate(), apDistributionRecord.getApPurchaseOrder().getPoConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApPurchaseOrder().getPoConversionDate(), apDistributionRecord.getApPurchaseOrder().getPoConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar invoices
            Collection arDrInvoices = arDistributionRecordHome.findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrInvoices.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar credit memoes
            Collection arDrCreditMemoes = arDistributionRecordHome.findInvCreditMemoByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrCreditMemoes.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                LocalArInvoice arCreditedVoucher = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), arCreditedVoucher.getInvConversionDate(), arCreditedVoucher.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), arCreditedVoucher.getInvConversionDate(), arCreditedVoucher.getInvConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar receipts
            Collection arDrReceipts = arDistributionRecordHome.findRctByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (arDistributionRecord.getArAppliedInvoice() != null && arDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                    LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();
                    glDrFunctionalCurrency = arInvoice.getGlFunctionalCurrency();
                    conversionDate = arInvoice.getInvConversionDate();
                    conversionRate = arInvoice.getInvConversionRate();
                } else {
                    LocalArReceipt arReceipt = arDistributionRecord.getArReceipt();
                    glDrFunctionalCurrency = arReceipt.getGlFunctionalCurrency();
                    conversionDate = arReceipt.getRctConversionDate();
                    conversionRate = arReceipt.getRctConversionRate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get ar misc receipts
            Collection arDrMiscReceipts = arDistributionRecordHome.findRctMiscByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrMiscReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArReceipt().getRctConversionDate(), arDistributionRecord.getArReceipt().getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArReceipt().getRctConversionDate(), arDistributionRecord.getArReceipt().getRctConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get cm adjustment
            Collection cmDrAdjustments = cmDistributionRecordHome.findAdjByDateAndCoaAccountNumberAndCurrencyAndAdjPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmAdjustment().getAdjConversionDate(), cmDistributionRecord.getCmAdjustment().getAdjConversionRate(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmAdjustment().getAdjConversionDate(), cmDistributionRecord.getCmAdjustment().getAdjConversionRate(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            // get cm fund transfers
            Collection cmDrFundTransfers = cmDistributionRecordHome.findFtByDateAndCoaAccountNumberAndCurrencyAndFtPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmDistributionRecord.getCmFundTransfer().getFtAdBaAccountFrom());

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmFundTransfer().getFtConversionDate(), cmDistributionRecord.getCmFundTransfer().getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                FC_AMOUNT = this.convertFunctionalToForeignCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmFundTransfer().getFtConversionDate(), cmDistributionRecord.getCmFundTransfer().getFtConversionRateFrom(), HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    CURR_TOTAL_DEBIT += CURR_CNVRSN;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    CURR_TOTAL_CREDIT += CURR_CNVRSN;
                }
            }

            if ("ASSET".equals(ACCOUNT_TYPE) || "EXPENSE".equals(ACCOUNT_TYPE)) {
                REVAL_AMOUNT = (CURR_TOTAL_DEBIT - CURR_TOTAL_CREDIT) - (HIST_TOTAL_DEBIT - HIST_TOTAL_CREDIT);
            } else {
                REVAL_AMOUNT = (CURR_TOTAL_CREDIT - CURR_TOTAL_DEBIT) - (HIST_TOTAL_CREDIT - HIST_TOTAL_DEBIT);
            }

            return REVAL_AMOUNT;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getHistoricalRate(LocalGlChartOfAccount glChartOfAccount, LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlFunctionalCurrency glFunctionalCurrency, double COLUMN_AMOUNT, Date CONVERSION_DATE, String ACCOUNT_TYPE, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getHistoricalRate");

        double HIST_CNVRSN = 0d;
        double HIST_TOTAL_DEBIT = 0d;
        double HIST_TOTAL_CREDIT = 0d;
        double FC_TOTAL_DEBIT = 0d;
        double FC_TOTAL_CREDIT = 0d;
        double CURR_CNVRSN = 0d;

        try {

            // find beg bal manual journals
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            Collection glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "MANUAL", AD_CMPNY);

            Iterator i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }

                Debug.print("GL Manual Journal");
                Debug.print("HIST_CNVRSN : " + HIST_CNVRSN);
                Debug.print("FC_AMOUNT : " + FC_AMOUNT);
            }

            // find beg bal journal reversals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPostedAndJsName(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, "JOURNAL REVERSAL", AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), glJournalLine.getGlJournal().getJrEffectiveDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, FC_AMOUNT, AD_CMPNY);

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }

                Debug.print("GL Journal Reversal");
                Debug.print("HIST_CNVRSN : " + HIST_CNVRSN);
                Debug.print("FC_AMOUNT : " + FC_AMOUNT);
            }

            // get gl journals
            glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = glJournalLines.iterator();

            while (i.hasNext()) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), glJournalLine.getJlAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), glJournalLine.getGlJournal().getJrConversionDate(), glJournalLine.getGlJournal().getJrConversionRate(), HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode(), glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, glJournalLine.getJlAmount(), AD_CMPNY);

                if (glJournalLine.getJlDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }

                Debug.print("GL Journal");
                Debug.print("HIST_CNVRSN : " + HIST_CNVRSN);
                Debug.print("FC_AMOUNT : " + FC_AMOUNT);
            }

            // get ap vouchers

            Collection apDrVouchers = apDistributionRecordHome.findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            if (apDrVouchers.isEmpty())
                apDrVouchers = apDistributionRecordHome.findVouByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrVouchers.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                Date conversionDate = null;
                double conversionRate = 0d;

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), apVoucher.getVouConversionDate(), apVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apVoucher.getVouConversionDate() != null) {

                    conversionDate = apVoucher.getVouConversionDate();
                    conversionRate = apVoucher.getVouConversionRate();

                } else {

                    conversionDate = apVoucher.getVouDate();
                    conversionRate = 1d;
                }

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(
                        glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), conversionDate, conversionRate, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apVoucher.getGlFunctionalCurrency().getFcCode(), apVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
                Debug.print("AP Voucher");
                Debug.print("HIST_CNVRSN : " + HIST_CNVRSN);
                Debug.print("FC_AMOUNT : " + FC_AMOUNT);
            }

            // get ap debit memoes

            Collection apDrDebitMemoes = apDistributionRecordHome.findVouDebitMemoByDateAndCoaAccountNumberAndCurrencyAndVouPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDebitMemoes.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalApVoucher apVoucher = apDistributionRecord.getApVoucher();

                LocalApVoucher apDebitedVoucher = apVoucherHome.findByVouDocumentNumberAndVouDebitMemoAndBrCode(apVoucher.getVouDmVoucherNumber(), EJBCommon.FALSE, apVoucher.getVouAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), apDebitedVoucher.getVouConversionDate(), apDebitedVoucher.getVouConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), apDebitedVoucher.getVouDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDebitedVoucher.getGlFunctionalCurrency().getFcCode(), apDebitedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get ap checks
            Collection apDrChecks = apDistributionRecordHome.findChkByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date apDate = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (apDistributionRecord.getApAppliedVoucher() != null && apDistributionRecord.getDrClass().equals("PAYABLE")) {
                    LocalApVoucher apVoucher = apDistributionRecord.getApAppliedVoucher().getApVoucherPaymentSchedule().getApVoucher();
                    glDrFunctionalCurrency = apVoucher.getGlFunctionalCurrency();
                    conversionDate = apVoucher.getVouConversionDate();
                    conversionRate = apVoucher.getVouConversionRate();
                    apDate = apVoucher.getVouDate();
                } else {
                    LocalApCheck apCheck = apDistributionRecord.getApCheck();
                    glDrFunctionalCurrency = apCheck.getGlFunctionalCurrency();
                    conversionDate = apCheck.getChkConversionDate();
                    conversionRate = apCheck.getChkConversionRate();
                    apDate = apCheck.getChkDate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), apDate, 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get ap direct checks
            Collection apDrDirectChecks = apDistributionRecordHome.findChkDirectByDateAndCoaAccountNumberAndCurrencyAndChkPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrDirectChecks.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApCheck().getChkConversionDate(), apDistributionRecord.getApCheck().getChkConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), apDistributionRecord.getApCheck().getChkDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApCheck().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get ap receiving items
            Collection apDrReceivingItems = apDistributionRecordHome.findPoByDateAndCoaAccountNumberAndCurrencyAndPoPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = apDrReceivingItems.iterator();

            while (i.hasNext()) {

                LocalApDistributionRecord apDistributionRecord = (LocalApDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), apDistributionRecord.getApPurchaseOrder().getPoConversionDate(), apDistributionRecord.getApPurchaseOrder().getPoConversionRate(), apDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), apDistributionRecord.getApPurchaseOrder().getPoDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcCode(), apDistributionRecord.getApPurchaseOrder().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, apDistributionRecord.getDrAmount(), AD_CMPNY);

                if (apDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get ar invoices
            Collection arDrInvoices = arDistributionRecordHome.findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            if (arDrInvoices.isEmpty())
                arDrInvoices = arDistributionRecordHome.findInvByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrInvoices.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                Date conversionDate = null;
                double conversionRate = 0d;

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), arInvoice.getInvConversionDate(), arInvoice.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arInvoice.getInvConversionDate() != null) {

                    conversionDate = arInvoice.getInvConversionDate();
                    conversionRate = arInvoice.getInvConversionRate();

                } else {

                    conversionDate = arInvoice.getInvDate();
                    conversionRate = 1d;
                }

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), conversionDate, conversionRate, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arInvoice.getGlFunctionalCurrency().getFcCode(), arInvoice.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }

                Debug.print("AR Invoice");
                Debug.print("HIST_CNVRSN : " + HIST_CNVRSN);
                Debug.print("FC_AMOUNT : " + FC_AMOUNT);
            }

            // get ar credit memoes
            Collection arDrCreditMemoes = arDistributionRecordHome.findInvCreditMemoByDateAndCoaAccountNumberAndCurrencyAndInvPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrCreditMemoes.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalArInvoice arInvoice = arDistributionRecord.getArInvoice();

                LocalArInvoice arCreditedVoucher = arInvoiceHome.findByInvNumberAndInvCreditMemoAndBrCode(arInvoice.getInvCmInvoiceNumber(), EJBCommon.FALSE, arInvoice.getInvAdBranch(), AD_CMPNY);

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), arCreditedVoucher.getInvConversionDate(), arCreditedVoucher.getInvConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), arCreditedVoucher.getInvDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arCreditedVoucher.getGlFunctionalCurrency().getFcCode(), arCreditedVoucher.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get ar receipts
            Collection arDrReceipts = arDistributionRecordHome.findRctByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                LocalGlFunctionalCurrency glDrFunctionalCurrency = null;
                Date arDate = null;
                Date conversionDate = null;
                double conversionRate = 0d;

                if (arDistributionRecord.getArAppliedInvoice() != null && arDistributionRecord.getDrClass().equals("RECEIVABLE")) {
                    LocalArInvoice arInvoice = arDistributionRecord.getArAppliedInvoice().getArInvoicePaymentSchedule().getArInvoice();
                    glDrFunctionalCurrency = arInvoice.getGlFunctionalCurrency();
                    conversionDate = arInvoice.getInvConversionDate();
                    conversionRate = arInvoice.getInvConversionRate();
                    arDate = arInvoice.getInvDate();
                } else {
                    LocalArReceipt arReceipt = arDistributionRecord.getArReceipt();
                    glDrFunctionalCurrency = arReceipt.getGlFunctionalCurrency();
                    conversionDate = arReceipt.getRctConversionDate();
                    conversionRate = arReceipt.getRctConversionRate();
                    arDate = arReceipt.getRctDate();
                }

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), conversionDate, conversionRate, arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), arDate, 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(glDrFunctionalCurrency.getFcCode(), glDrFunctionalCurrency.getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            Collection arDrMiscReceipts = arDistributionRecordHome.findRctMiscByDateAndCoaAccountNumberAndCurrencyAndRctPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = arDrMiscReceipts.iterator();

            while (i.hasNext()) {

                LocalArDistributionRecord arDistributionRecord = (LocalArDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), arDistributionRecord.getArReceipt().getRctConversionDate(), arDistributionRecord.getArReceipt().getRctConversionRate(), arDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), arDistributionRecord.getArReceipt().getRctDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcCode(), arDistributionRecord.getArReceipt().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, arDistributionRecord.getDrAmount(), AD_CMPNY);

                if (arDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get cm adjustment
            Collection cmDrAdjustments = cmDistributionRecordHome.findAdjByDateAndCoaAccountNumberAndCurrencyAndAdjPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrAdjustments.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmAdjustment().getAdjConversionDate(), cmDistributionRecord.getCmAdjustment().getAdjConversionRate(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), cmDistributionRecord.getCmAdjustment().getAdjDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcCode(), cmDistributionRecord.getCmAdjustment().getAdBankAccount().getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, cmDistributionRecord.getDrAmount(), AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            // get cm fund transfers
            Collection cmDrFundTransfers = cmDistributionRecordHome.findFtByDateAndCoaAccountNumberAndCurrencyAndFtPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), adCompany.getGlFunctionalCurrency().getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            i = cmDrFundTransfers.iterator();

            while (i.hasNext()) {

                LocalCmDistributionRecord cmDistributionRecord = (LocalCmDistributionRecord) i.next();

                LocalAdBankAccount adBankAccount = adBankAccountHome.findByPrimaryKey(cmDistributionRecord.getCmFundTransfer().getFtAdBaAccountFrom());

                // get historical conversion
                HIST_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), cmDistributionRecord.getCmFundTransfer().getFtConversionDate(), cmDistributionRecord.getCmFundTransfer().getFtConversionRateFrom(), cmDistributionRecord.getDrAmount(), AD_CMPNY);

                // get current conversion
                double FC_AMOUNT = this.convertFunctionalToForeignCurrency(glFunctionalCurrency.getFcCode(), glFunctionalCurrency.getFcName(), cmDistributionRecord.getCmAdjustment().getAdjDate(), 1d, HIST_CNVRSN, AD_CMPNY);

                // get current conversion
                CURR_CNVRSN = this.convertForeignToFunctionalCurrency(adBankAccount.getGlFunctionalCurrency().getFcCode(), adBankAccount.getGlFunctionalCurrency().getFcName(), CONVERSION_DATE, 1d, cmDistributionRecord.getDrAmount(), AD_CMPNY);

                if (cmDistributionRecord.getDrDebit() == EJBCommon.TRUE) {
                    HIST_TOTAL_DEBIT += HIST_CNVRSN;
                    FC_TOTAL_DEBIT += FC_AMOUNT;
                } else {
                    HIST_TOTAL_CREDIT += HIST_CNVRSN;
                    FC_TOTAL_CREDIT += FC_AMOUNT;
                }
            }

            Debug.print("HIST_TOTAL_DEBIT : " + HIST_TOTAL_DEBIT);
            Debug.print("HIST_TOTAL_CREDIT : " + HIST_TOTAL_CREDIT);
            Debug.print("FC_TOTAL_DEBIT : " + FC_TOTAL_DEBIT);
            Debug.print("FC_TOTAL_CREDIT : " + FC_TOTAL_CREDIT);

            if ("ASSET".equals(ACCOUNT_TYPE) || "EXPENSE".equals(ACCOUNT_TYPE)) {
                return ((HIST_TOTAL_DEBIT - HIST_TOTAL_CREDIT) / (FC_TOTAL_DEBIT - FC_TOTAL_CREDIT));
            } else {
                return ((HIST_TOTAL_CREDIT - HIST_TOTAL_DEBIT) / (FC_TOTAL_CREDIT - FC_TOTAL_DEBIT));
            }

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private double getCoaRevaluationAmount(LocalGlChartOfAccount glChartOfAccount, LocalGlAccountingCalendarValue glAccountingCalendarValue, LocalGlFunctionalCurrency glFunctionalCurrency, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean getCoaRevaluationAmount");

        double REVAL_AMOUNT = 0d;

        try {

            // find beg bal manual journals
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            // get gl journals
            Collection glJournalLines = glJournalLineHome.findJrByDateAndCoaAccountNumberAndCurrencyAndJrPosted(glAccountingCalendarValue.getAcvDateTo(), glChartOfAccount.getCoaCode(), glFunctionalCurrency.getFcCode(), EJBCommon.TRUE, AD_CMPNY);

            for (Object journalLine : glJournalLines) {

                LocalGlJournalLine glJournalLine = (LocalGlJournalLine) journalLine;

                if (glJournalLine.getGlJournal().getGlJournalCategory().getJcName().equals("REVALUATION") && glJournalLine.getGlJournal().getGlFunctionalCurrency().getFcCode().equals(glFunctionalCurrency.getFcCode())) {

                    REVAL_AMOUNT += glJournalLine.getJlAmount();
                }
            }

            return REVAL_AMOUNT;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean convertForeignToFunctionalCurrency");

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

                if (!FC_NM.equals("USD")) {

                    LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT * glReceiptFunctionalCurrencyRate.getFrXToUsd();
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    AMOUNT = AMOUNT / glCompanyFunctionalCurrencyRate.getFrXToUsd();
                }

            } catch (Exception ex) {

                Debug.printStackTrace(ex);
                throw new EJBException(ex.getMessage());
            }
        }
        return AMOUNT;

    }

    private double convertFunctionalToForeignCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("GlRepFinancialReportRunControllerBean convertFunctionalToForeignCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT / CONVERSION_RATE;

        } else if (CONVERSION_DATE != null) {

            try {

                double FC_CONVERSION_RATE = 1f;
                double BK_CONVERSION_RATE = 1f;

                // Get functional currency rate

                if (!FC_NM.equals("USD")) {

                    LocalGlFunctionalCurrencyRate glReceiptFunctionalCurrencyRate = null;

                    glReceiptFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                    if (glReceiptFunctionalCurrencyRate != null) {

                        FC_CONVERSION_RATE = glReceiptFunctionalCurrencyRate.getFrXToUsd();

                    } else {

                        // get latest daily rate prior to conversion date

                        Collection glReceiptFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findPriorByFcCodeAndDate(FC_CODE, CONVERSION_DATE, AD_CMPNY);

                        Iterator i = glReceiptFunctionalCurrencyRates.iterator();

                        if (i.hasNext()) {

                            glReceiptFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) i.next();
                            FC_CONVERSION_RATE = glReceiptFunctionalCurrencyRate.getFrXToUsd();
                        }
                    }

                    AMOUNT = AMOUNT / FC_CONVERSION_RATE;
                }

                // Get set of book functional currency rate if necessary

                if (!adCompany.getGlFunctionalCurrency().getFcName().equals("USD")) {

                    LocalGlFunctionalCurrencyRate glCompanyFunctionalCurrencyRate = null;

                    glCompanyFunctionalCurrencyRate = glFunctionalCurrencyRateHome.findByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                    if (glCompanyFunctionalCurrencyRate != null) {

                        BK_CONVERSION_RATE = glCompanyFunctionalCurrencyRate.getFrXToUsd();

                    } else {

                        // get latest daily rate prior to conversion date

                        Collection glCompanyFunctionalCurrencyRates = glFunctionalCurrencyRateHome.findPriorByFcCodeAndDate(adCompany.getGlFunctionalCurrency().getFcCode(), CONVERSION_DATE, AD_CMPNY);

                        Iterator i = glCompanyFunctionalCurrencyRates.iterator();

                        if (i.hasNext()) {

                            glCompanyFunctionalCurrencyRate = (LocalGlFunctionalCurrencyRate) i.next();
                            BK_CONVERSION_RATE = glCompanyFunctionalCurrencyRate.getFrXToUsd();
                        }
                    }

                    // AMOUNT = AMOUNT * (1 / EJBCommon.roundIt(1 / BK_CONVERSION_RATE, (short)3));
                    AMOUNT = AMOUNT * BK_CONVERSION_RATE;
                }

            } catch (Exception ex) {

            }
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GlRepFinancialReportRunControllerBean ejbCreate");
    }
}