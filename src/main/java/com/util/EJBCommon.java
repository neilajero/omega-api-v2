package com.util;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

public final class EJBCommon implements java.io.Serializable {

    public static final byte TRUE = 1;
    public static final byte FALSE = 0;
    public static final char SEPARATOR = '$';
    public static final char COMMA_DELIMITER = ',';
    public static final String DELIMETER = "$";

    public static final String SUPER = "Super";
    public static final String SYS_ADMIN = "Sys Admin";
    public static final String ACCOUNTANT = "Accountant";
    public static final String GL_CLERK = "Accounting Clerk";
    public static final String AP_CLERK = "AP Clerk";
    public static final String AR_CLERK = "AR Clerk";
    public static final String BOOKKEEPER = "Bookkeeper";
    public static final String FINANCE_OFFICER = "Finance Officer";

    public static final String DEFAULT = "DEFAULT";
    public static final String NO_RECORD_FOUND = "No Record Found";
    public static final String RECORD_EXISTS = "Record Exists";
    public static final String ADDED_NEW_RECORD = "Added New Record";
    public static final String NEW_COLLECTION_APPLIED = "New Collection Applied";
    public static final String UPDATED_EXISTING_RECORD = "Updated Existing Record";
    public static final String DELETED_RECORD = "Deleted Record";
    public static final String REVERSED_RECORD = "Reversed Existing Record.";
    public static final String TRANSACTION_FAILED = "Transaction Failed";

    public static final String API_ITEMS = "ITEMS";
    public static final String API_MEMOLINES = "MEMO LINES";

    public static final String INV_PRICE_LEVEL = "INV PRICE LEVEL";
    public static final String INV_ITEM_CATEGORY = "INV ITEM CATEGORY";

    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String STORE = "STORE";
    public static final String BTS = "BTS";

    public static final String REQUESTER = "REQUESTER";
    public static final String APPROVER = "APPROVER";
    public static final String LEVEL1APPROVER = "LEVEL 1";

    public static final String INVENTORY_UPLOAD = "U";
    public static final String INVENTORY_TRANSFER = "T";

    public static final String REVENUE = "REVENUE";
    public static final String SERVICE_CHARGE = "SC";
    public static final String APPROVED = "APPROVED";
    public static final String NOT_APPLICABLE = "N/A";
    public static final String PENDING = "PENDING";
    public static final String COST_OF_GOODS_SOLD = "COGS";
    public static final String INVENTORY = "INVENTORY";
    public static final String WITHHOLDING_TAX = "W-TAX";
    public static final String DISCOUNT = "DISCOUNT";
    public static final String RECEIVABLE = "RECEIVABLE";
    public static final String OTHER = "OTHER";
    public static final String AR_INVOICE = "AR INVOICE";
    public static final String AR_CREDIT_MEMO = "AR CREDIT MEMO";
    public static final String AR_SALES_ORDER = "AR SALES ORDER";
    public static final String AR_RECEIPT = "AR RECEIPT";
    public static final String INVOICE = "INVOICE";
    public static final String TAX = "TAX";

    public static final String DOCUMENT_POSTED = "DOCUMENT POSTED";
    public static final String DOCUMENT_REJECTED = "DOCUMENT REJECTED";
    public static final String DEFERRED_TAX = "DEFERRED TAX";

    public static final String ASSET = "ASSET";
    public static final String EXPENSE = "EXPENSE";

    public static final String MONTHLY = "MONTHLY";
    public static final String BI_MONTHLY = "BI-MONTHLY";

    public static final String SALES_INVOICES = "SALES INVOICES";
    public static final String JOURNAL_IMPORT = "JOURNAL IMPORT";
    public static final String CLOSED = "CLOSED";

    public static final String NONE = "NONE";
    public static final String EXEMPT = "EXEMPT";

    public static final String UNINTEREST = "UNINTEREST";
    public static final String RECEIVABLE_INTEREST = "RECEIVABLE INTEREST";
    public static final String AR_CUSTOMER = "AR CUSTOMER";
    public static final String AR_STANDARD_MEMO_LINE = "AR STANDARD MEMO LINE";
    public static final String INCLUSIVE = "INCLUSIVE";
    public static final String EXCLUSIVE = "EXCLUSIVE";

    public static final String itemName = "itemName";
    public static final String location = "location";
    public static final String Average = "Average";
    public static final String Standard = "Standard";
    public static final String FIFO = "FIFO";

    public static final String REPORT_VIEW_TYPE_PDF = "PDF";
    public static final String SOA_REPORT_FILENAME = "SOA";

    public static String DATE_FORMAT_INPUT = null;

    public static double roundIt(double amount, short precision) {

        return Math.round(amount * Math.pow(10, precision)) / Math.pow(10, precision);
    }

    public static boolean validateAmount(double amount, double minimumAccountUnit) {

        String strAmount = Double.toString(minimumAccountUnit);
        strAmount = strAmount.substring(strAmount.indexOf('.'));
        double precision = strAmount.length();

        return ((amount * Math.pow(10, precision)) / (minimumAccountUnit * Math.pow(10, precision))) == Math
                .round((amount * Math.pow(10, precision))
                        / (minimumAccountUnit * Math.pow(10, precision)));
    }

    public static GregorianCalendar getGcCurrentDateWTime() {

        GregorianCalendar gcCurrDate = new GregorianCalendar();
        gcCurrDate.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH),
                gcCurrDate.get(Calendar.DAY_OF_MONTH), gcCurrDate.get(Calendar.HOUR_OF_DAY),
                gcCurrDate.get(Calendar.MINUTE), gcCurrDate.get(Calendar.SECOND));
        gcCurrDate.set(Calendar.MILLISECOND, 0);
        return (gcCurrDate);
    }

    public static GregorianCalendar getGcCurrentDateWoTime() {

        GregorianCalendar gcCurrDate = new GregorianCalendar();
        gcCurrDate.set(gcCurrDate.get(Calendar.YEAR), gcCurrDate.get(Calendar.MONTH), gcCurrDate.get(Calendar.DATE), 0,
                0, 0);
        gcCurrDate.set(Calendar.MILLISECOND, 0);
        return (gcCurrDate);
    }

    public static Date getGcCurrentStartOfDay() {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getIntendedDate(int year) {

        GregorianCalendar intendedGc = new GregorianCalendar(year, 0, 1, 0, 0, 0);
        intendedGc.set(Calendar.MILLISECOND, 0);

        return (new Date(intendedGc.getTime().getTime()));
    }

    public static String incrementStringNumber(String stringNumber) {

        String regexPattern1 = Pattern.compile(".*\\D(\\d+)\\D*$").pattern();
        String regexPattern2 = Pattern.compile("\\d+(\\D*)$").pattern();
        String strNumberExtract = stringNumber.replaceFirst(regexPattern1, "$1");

        long num;

        try {
            num = Long.parseLong(strNumberExtract);
        }
        catch (NumberFormatException ex) {
            return strNumberExtract + "1";
        }

        String strIncrementedNumberExtract = Long.toString(++num);
        int diffLen = strNumberExtract.length() - strIncrementedNumberExtract.length();
        StringBuilder zeroPadding = new StringBuilder();
        if (diffLen > 0) {
            for (int i = 0; i < diffLen; i++) {
                zeroPadding.append("0");
            }
        }
        return stringNumber.replaceFirst(regexPattern2, zeroPadding + strIncrementedNumberExtract + "$1");
    }

    public static Date convertStringToSQLDate(String strDateInput) {
        String dateFormatInput = Pattern.compile("dd-MM-yyyy").pattern();
        if (strDateInput != null && strDateInput.length() >= 1) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInput);
            sdf.setLenient(false);
            try {
                return (sdf.parse(strDateInput));
            }
            catch (ParseException e) {
                return (null);
            }
        } else {
            return (null);
        }
    }

    public static Date convertStringToSQLDateV3(String strDateInput) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            java.util.Date utilDate = inputFormat.parse(strDateInput);
            String formattedDate = outputFormat.format(utilDate);
            java.util.Date parsedDate = outputFormat.parse(formattedDate);
            return new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToSQLDateV2(String strDateInput) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy"); // Date format of the string
        java.util.Date utilDate = df.parse(strDateInput); // Parse the string to java.util.Date
        return new java.sql.Date(utilDate.getTime()); // Convert java.util.Date to java.sql.Date
    }

    public static boolean validateApiDate(String strDateInput, String dateFormatInput) {

        if (strDateInput.trim().equals("")) {
            return false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInput);
            sdf.setLenient(false);
            try {
                sdf.parse(strDateInput);
            }
            catch (ParseException e) {
                return true;
            }
            return false;
        }
    }

    public static Date convertStringToSQLDate(String strDateInput, String dateFormat) {

        if (strDateInput != null && strDateInput.length() >= 1) {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);

            try {

                return (sdf.parse(strDateInput));

            }
            catch (ParseException e) {

                return (null);
            }

        } else {

            return (null);
        }
    }

    public static List<String> miscList(String longtext) {

        List<String> miscList = new ArrayList<>();
        if (longtext.length() == 0) {
            return miscList;
        }
        if (longtext.startsWith(DELIMETER)) {
            longtext = longtext.substring(1);
        }
        while (longtext.length() <= 1 || !longtext.equals(DELIMETER)) {
            int index = longtext.indexOf(DELIMETER);
            if (index < 0) {
                break;
            }
            String equipmentIdentity = longtext.substring(0, index);
            miscList.add(equipmentIdentity);
            longtext = longtext.substring(index + 1);
        }
        return miscList;
    }

    public static String convertSQLDateToString(Date dtInput) {

        String dateFormatInput = Pattern.compile("MM/dd/yyyy").pattern();

        if (dtInput != null) {
            SimpleDateFormat sdfDateOutput = new SimpleDateFormat(dateFormatInput);
            return (sdfDateOutput.format(dtInput));
        } else {
            return (null);
        }
    }

    public static String convertDoubleToStringMoney(double number, short precisionUnit) {

        StringBuilder decimalFormat = new StringBuilder("#,###,###,###,###,###,##0.");
        for (int i = 0; i < precisionUnit; i++) {
            decimalFormat.append("0");
        }
        DecimalFormat dcf = new DecimalFormat(decimalFormat.toString());
        return dcf.format(number);
    }

    public static boolean validateRequired(String strRequired) {

        return strRequired == null || strRequired.length() < 1;
    }

    public static byte booleanToByte(boolean value) {

        return (byte) (value ? 1 : 0);
    }

    public static boolean byteToBoolean(byte value) {

        return value != 0;
    }

    public static LocalDate convertLocalDateObject(Date inputDate) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(inputDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDate.of(year, month, day);
    }

    public static String rebuildBranchMapCOA(String chartOfAccount, String branchCoaSegment) {

        String branchCOASegment = null;
        String[] splitCoa = chartOfAccount.split("-");
        StringBuilder modifiedCOA = new StringBuilder();
        int count = 1;

        for (String s : splitCoa) {
            branchCOASegment = s;
            if (count == splitCoa.length) {
                branchCOASegment = branchCoaSegment;
            }
            rebuildCoa(branchCOASegment, modifiedCOA, count);
            count++;
        }
        return modifiedCOA.toString();
    }

    private static void rebuildCoa(String coaSegmentValue, StringBuilder modifiedCOA, int count) {

        if (count > 1) {
            modifiedCOA.append("-");
        }
        modifiedCOA.append(coaSegmentValue);
    }

    public static double calculateNetAmount(double inputAmount, byte isTax, double tcRate, String tcType, short precisionUnit) {

        double amount;
        if (isTax == EJBCommon.FALSE) {
            amount = inputAmount;
            return amount;
        }
        if (tcType.equals("INCLUSIVE")) {
            amount = EJBCommon.roundIt(inputAmount / (1 + (tcRate / 100)), precisionUnit);
        } else {
            // tax exclusive, none, zero rated or exempt
            amount = inputAmount;
        }
        return amount;
    }

    public static double calculateTaxAmount(double inputAmount, byte isTax, double tcRate, String tcType, double amount, short precisionUnit) {

        double taxAmount = 0d;
        if (isTax == EJBCommon.FALSE) {
            return taxAmount;
        }
        if (!tcType.equals("NONE") && !tcType.equals("EXEMPT")) {
            if (tcType.equals("INCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(inputAmount - amount, precisionUnit);
            } else if (tcType.equals("EXCLUSIVE")) {
                taxAmount = EJBCommon.roundIt(inputAmount * tcRate / 100, precisionUnit);
            } else {
                // tax none zero-rated or exempt
            }
        }
        return taxAmount;
    }

    public static boolean validateInputData(String inputData) {
        boolean validValue = true;
        if (inputData == null || inputData.isEmpty()) {
            validValue = false;
        } else if (inputData.isBlank()) {
            validValue = false;
        }
        return validValue;
    }
}