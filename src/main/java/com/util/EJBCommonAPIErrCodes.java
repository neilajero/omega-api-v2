package com.util;

import com.ejb.ConfigurationClass;

public final class EJBCommonAPIErrCodes implements java.io.Serializable {

	public static final String OAPI_ERR_000 = "OAPI-ERR-000";
	public static final String OAPI_ERR_000_MSG = "Success";

	public static final String OAPI_ERR_001 = "OAPI-ERR-001";
	public static final String OAPI_ERR_001_MSG = "Company code does not exist.";
	
	public static final String OAPI_ERR_002 = "OAPI-ERR-002";
	public static final String OAPI_ERR_002_MSG = "Username does not exist.";
	
	public static final String OAPI_ERR_003 = "OAPI-ERR-003";
	public static final String OAPI_ERR_003_MSG = "Unit of measure does not exist.";
	
	public static final String OAPI_ERR_004 = "OAPI-ERR-004";
	public static final String OAPI_ERR_004_MSG = "Item category does not exist.";
	
	public static final String OAPI_ERR_005 = "OAPI-ERR-005";
	public static final String OAPI_ERR_005_MSG = "Error creating Item Location to the provided item name. Please contact the administrator.";
	
	public static final String OAPI_ERR_006 = "OAPI-ERR-006";
	public static final String OAPI_ERR_006_MSG = "Item name already assigned.";
	
	public static final String OAPI_ERR_007 = "OAPI-ERR-007";
	public static final String OAPI_ERR_007_MSG = "System/Unknown error. Please contact the administrator.";
	
	public static final String OAPI_ERR_008 = "OAPI-ERR-008";
	public static final String OAPI_ERR_008_MSG = "Branch code does not exist.";
	
	public static final String OAPI_ERR_009 = "OAPI-ERR-009";
	public static final String OAPI_ERR_009_MSG = "Customer code does not exist.";
	
	public static final String OAPI_ERR_010 = "OAPI-ERR-010";
	public static final String OAPI_ERR_010_MSG = "Item %s does not exist.";
	
	public static final String OAPI_ERR_011 = "OAPI-ERR-011";
	public static final String OAPI_ERR_011_MSG = "Item location for %s does not exist.";
	
	public static final String OAPI_ERR_012 = "OAPI-ERR-012";
	public static final String OAPI_ERR_012_MSG = "Payment term invalid.";
	
	public static final String OAPI_ERR_013 = "OAPI-ERR-013";
	public static final String OAPI_ERR_013_MSG = "Amount exceeds credit limit.";
	
	public static final String OAPI_ERR_014 = "OAPI-ERR-014";
	public static final String OAPI_ERR_014_MSG = "Item location not found.";
	
	public static final String OAPI_ERR_015 = "OAPI-ERR-015";
	public static final String OAPI_ERR_015_MSG = "No period exists for effective date.";
	
	public static final String OAPI_ERR_016 = "OAPI-ERR-016";
	public static final String OAPI_ERR_016_MSG = "Period already closed for effective date.";
	
	public static final String OAPI_ERR_017 = "OAPI-ERR-017";
	public static final String OAPI_ERR_017_MSG = "Journal not balance.";
	
	public static final String OAPI_ERR_018 = "OAPI-ERR-018";
	public static final String OAPI_ERR_018_MSG = "Invalid branch chart of account number.";
	
	public static final String OAPI_ERR_019 = "OAPI-ERR-019";
	public static final String OAPI_ERR_019_MSG = "Variance COA account not found.";
	
	public static final String OAPI_ERR_020 = "OAPI-ERR-020";
	public static final String OAPI_ERR_020_MSG = "Record invalid. Insufficient Items.";
	
	public static final String OAPI_ERR_021 = "OAPI-ERR-021";
	public static final String OAPI_ERR_021_MSG = "Invalid transaction date format (e.g., %s)";
	
	public static final String OAPI_ERR_022 = "OAPI-ERR-022";
	public static final String OAPI_ERR_022_MSG = "Customer already exists.";
	
	public static final String OAPI_ERR_023 = "OAPI-ERR-023";
	public static final String OAPI_ERR_023_MSG = "COA don't exist. Please contact the administrator.";

    public static final String OAPI_ERR_024 = "OAPI-ERR-024";
    public static final String OAPI_ERR_024_MSG = "Invoice Number %s doesn't exist.";
    
    public static final String OAPI_ERR_025 = "OAPI-ERR-025";
    public static final String OAPI_ERR_025_MSG = "Invalid Receipt Date format (e.g. "+ ConfigurationClass.DEFAULT_DATE_FORMAT +").";
    
    public static final String OAPI_ERR_026 = "OAPI-ERR-026";
    public static final String OAPI_ERR_026_MSG = "Sales price of item %s is not yet setup. Please contact the administrator.";
    
    public static final String OAPI_ERR_027 = "OAPI-ERR-027";
    public static final String OAPI_ERR_027_MSG = "Invalid adjustment identifier.";
    
    public static final String OAPI_ERR_028 = "OAPI-ERR-028";
    public static final String OAPI_ERR_028_MSG = "Branch account number invalid.";
    
    public static final String OAPI_ERR_029 = "OAPI-ERR-029";
    public static final String OAPI_ERR_029_MSG = "Invalid unit cost for %s.";
    
    public static final String OAPI_ERR_030 = "OAPI-ERR-030";
    public static final String OAPI_ERR_030_MSG = "Invoice %s is already fully paid.";
    
    public static final String OAPI_ERR_031 = "OAPI-ERR-031";
    public static final String OAPI_ERR_031_MSG = "No unit cost setup for item in line %s. Please contact the administrator.";
    
    public static final String OAPI_ERR_032 = "OAPI-ERR-032";
    public static final String OAPI_ERR_032_MSG = "No item costing setup for item %s. Please contact the administrator.";
    
    public static final String OAPI_ERR_033 = "OAPI-ERR-033";
    public static final String OAPI_ERR_033_MSG = "Receipt Number not exist.";
    
    public static final String OAPI_ERR_034 = "OAPI-ERR-034";
    public static final String OAPI_ERR_034_MSG = "Receipt Number already reversed.";
    
    public static final String OAPI_ERR_035 = "OAPI-ERR-035";
    public static final String OAPI_ERR_035_MSG = "Apply amount is more than the amount due.";
    
    public static final String OAPI_ERR_036 = "OAPI-ERR-036";
    public static final String OAPI_ERR_036_MSG = "Apply amount is less than the amount due.";

	public static final String OAPI_ERR_037 = "OAPI-ERR-037";
	public static final String OAPI_ERR_037_MSG = "Invalid invoice type.";

	public static final String OAPI_ERR_038 = "OAPI-ERR-038";
	public static final String OAPI_ERR_038_MSG = "Invalid transaction type.";

	public static final String OAPI_ERR_039 = "OAPI-ERR-039";
	public static final String OAPI_ERR_039_MSG = "Invoice number %s doesn't exists in Omega ERP.";

	public static final String OAPI_ERR_040 = "OAPI-ERR-040";
	public static final String OAPI_ERR_040_MSG = "Invoice number %s already exists in Omega ERP.";

	public static final String OAPI_ERR_041 = "OAPI-ERR-041";
	public static final String OAPI_ERR_041_MSG = "Invalid company code.";

	public static final String OAPI_ERR_042 = "OAPI-ERR-042";
	public static final String OAPI_ERR_042_MSG = "Invalid username.";

	public static final String OAPI_ERR_043 = "OAPI-ERR-043";
	public static final String OAPI_ERR_043_MSG = "Invalid unit of measure.";

	public static final String OAPI_ERR_044 = "OAPI-ERR-044";
	public static final String OAPI_ERR_044_MSG = "Invalid item category.";

	public static final String OAPI_ERR_045 = "OAPI-ERR-045";
	public static final String OAPI_ERR_045_MSG = "Invalid item name.";

	public static final String OAPI_ERR_046 = "OAPI-ERR-046";
	public static final String OAPI_ERR_046_MSG = "Invalid item description.";

	public static final String OAPI_ERR_047 = "OAPI-ERR-047";
	public static final String OAPI_ERR_047_MSG = "Invalid branch code.";

	public static final String OAPI_ERR_048 = "OAPI-ERR-048";
	public static final String OAPI_ERR_048_MSG = "Invalid customer name.";

	public static final String OAPI_ERR_049 = "OAPI-ERR-049";
	public static final String OAPI_ERR_049_MSG = "Invalid apply amount in Invoice %s.";

	public static final String OAPI_ERR_050 = "OAPI-ERR-050";
	public static final String OAPI_ERR_050_MSG = "Invalid item location.";

	public static final String OAPI_ERR_051 = "OAPI-ERR-051";
	public static final String OAPI_ERR_051_MSG = "Item %s already exist.";

	public static final String OAPI_ERR_052 = "OAPI-ERR-052";
	public static final String OAPI_ERR_052_MSG = "Invalid adjustment account.";

	public static final String OAPI_ERR_053 = "OAPI-ERR-053";
	public static final String OAPI_ERR_053_MSG = "Invalid adjustment action.";

	public static final String OAPI_ERR_054 = "OAPI-ERR-054";
	public static final String OAPI_ERR_054_MSG = "No unit cost setup for item %s. Please contact the administrator.";

	public static final String OAPI_ERR_055 = "OAPI-ERR-055";
	public static final String OAPI_ERR_055_MSG = "Insufficient stocks to transfer.";

	public static final String OAPI_ERR_056 = "OAPI-ERR-056";
	public static final String OAPI_ERR_056_MSG = "Invalid item class.";

	public static final String OAPI_ERR_057 = "OAPI-ERR-057";
	public static final String OAPI_ERR_057_MSG = "Invalid cost method.";

	public static final String OAPI_ERR_058 = "OAPI-ERR-058";
	public static final String OAPI_ERR_058_MSG = "Invalid unit cost.";

	public static final String OAPI_ERR_059 = "OAPI-ERR-059";
	public static final String OAPI_ERR_059_MSG = "Invalid adjustment receipt.";

	public static final String OAPI_ERR_060 = "OAPI-ERR-060";
	public static final String OAPI_ERR_060_MSG = "Prior month transactions are not allowed.";

	public static final String OAPI_ERR_061 = "OAPI-ERR-061";
	public static final String OAPI_ERR_061_MSG = "Invalid invoice. Line items is missing.";

	public static final String OAPI_ERR_062 = "OAPI-ERR-062";
	public static final String OAPI_ERR_062_MSG = "%s field with value %s is not yet supported. Please contact system administrator.";

	public static final String OAPI_ERR_063 = "OAPI-ERR-063";
	public static final String OAPI_ERR_063_MSG = "Invalid branch code.";

	public static final String OAPI_ERR_064 = "OAPI-ERR-064";
	public static final String OAPI_ERR_064_MSG = "Location code %s does not exist.";

	public static final String OAPI_ERR_065 = "OAPI-ERR-065";
	public static final String OAPI_ERR_065_MSG = "Branch code %s does not exist.";

	public static final String OAPI_ERR_066 = "OAPI-ERR-066";
	public static final String OAPI_ERR_066_MSG = "Entered branch codes does not exist.";

	public static final String OAPI_ERR_067 = "OAPI-ERR-067";
	public static final String OAPI_ERR_067_MSG = "Branch account for %s branch map is not setup.";

	public static final String OAPI_ERR_068 = "OAPI-ERR-068";
	public static final String OAPI_ERR_068_MSG = "Overapplied for invoice %s is not allowed.";

	public static final String OAPI_ERR_069 = "OAPI-ERR-069";
	public static final String OAPI_ERR_069_MSG = "Duplicate invoice %s is not allowed.";

	public static final String OAPI_ERR_070 = "OAPI-ERR-070";
	public static final String OAPI_ERR_070_MSG = "Item Category - %s COA for this item is not setup. Please contact system administrator.";

	public static final String OAPI_ERR_071 = "OAPI-ERR-071";
	public static final String OAPI_ERR_071_MSG = "Invalid currency.";

	public static final String OAPI_ERR_072 = "OAPI-ERR-072";
	public static final String OAPI_ERR_072_MSG = "Invalid bank account name.";
	public static final String OAPI_ERR_073 = "OAPI-ERR-073";
	public static final String OAPI_ERR_073_MSG = "Invalid branch name.";

	public static final String OAPI_ERR_074 = "OAPI-ERR-074";
	public static final String OAPI_ERR_074_MSG = "Record already exist.";

	public static final String OAPI_ERR_075 = "OAPI-ERR-075";
	public static final String OAPI_ERR_075_MSG = "Approver User requires a level.";
	public static final String OAPI_ERR_076 = "OAPI-ERR-076";
	public static final String OAPI_ERR_076_MSG = "Level is not required in Requester User.";

	public static final String OAPI_ERR_077 = "OAPI-ERR-077";
	public static final String OAPI_ERR_077_MSG = "Approver User Type is required.";

	public static final String OAPI_ERR_078 = "OAPI-ERR-078";
	public static final String OAPI_ERR_078_MSG = "Approver or Requester Type is only allowed.";

	public static final String OAPI_ERR_079 = "OAPI-ERR-079";
	public static final String OAPI_ERR_079_MSG = "Please assign a requester(s).";
}