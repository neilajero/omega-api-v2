package com.ejb.txn.inv;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;

import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;
import com.ejb.dao.inv.LocalInvBranchStockTransferHome;
import com.ejb.entities.inv.LocalInvBranchStockTransferLine;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.util.EJBContextClass;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.mod.inv.InvModBranchStockTransferDetails;

@Stateless(name = "InvFindBranchStockTransferControllerEJB")
public class InvFindBranchStockTransferControllerBean extends EJBContextClass implements InvFindBranchStockTransferController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalInvBranchStockTransferHome invBranchStockTransferHome;
    @EJB
    LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    LocalAdBranchHome adBranchHome;

    public ArrayList getInvBstByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, boolean isBstLookup, Integer AD_BRNCH, Integer AD_CMPNY)
            throws GlobalNoRecordFoundException {

        Debug.print("InvFindBranchStockTransferControllerBean getInvBstByCriteria");

        ArrayList list = new ArrayList();
        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(bst) FROM InvBranchStockTransfer bst ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter
            String type = "";
            String statusType = "";

            if (criteria.containsKey("statusType")) {
                statusType = (String) criteria.get("statusType");
            }

            if (criteria.containsKey("type")) {
                type = (String) criteria.get("type");
            }

            if (criteria.containsKey("transferOutNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("transferOrderNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("statusType")) {
                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {
                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {
                    criteriaSize--;
                }
            }

            if (!criteria.containsKey("posted") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                criteriaSize++;
            } else if (!criteria.containsKey("posted") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                criteriaSize++;
            }

            obj = new Object[criteriaSize];
            if (criteria.containsKey("type")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("(bst.bstType=?").append(ctr + 1).append(" ");
                jbossQl.append(" OR bst.bstType='REGULAR' OR bst.bstType='EMERGENCY') ");
                obj[ctr] = type;
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("brVoid")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("brVoid");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");
                if ((type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                    if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED") || approvalStatus.equals("PENDING")) {
                        throw new GlobalNoRecordFoundException();
                    }
                } else if ((type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                    if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED") || approvalStatus.equals("PENDING")) {
                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (approvalStatus.equals("DRAFT")) {
                    jbossQl.append("bst.bstApprovalStatus IS NULL ");
                } else {
                    jbossQl.append("bst.bstApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }

            } else if (!criteria.containsKey("approvalStatus") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("(bst.bstApprovalStatus='N/A' OR bst.bstApprovalStatus='APPROVED') AND bst.bstLock=0 ");
            } else if (!criteria.containsKey("approvalStatus") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("(bst.bstApprovalStatus='N/A' OR bst.bstApprovalStatus='APPROVED') AND bst.bstLock=0 ");
            }

            if (criteria.containsKey("posted")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");
                String posted = (String) criteria.get("posted");
                if (posted.equals("YES")) {
                    obj[ctr] = EJBCommon.TRUE;
                } else {
                    obj[ctr] = EJBCommon.FALSE;
                }
                ctr++;
            } else if (!criteria.containsKey("posted") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
            } else if (!criteria.containsKey("posted") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            if ((type != null) && (type.equals("IN"))) {
                if (criteria.containsKey("transferOutNumber")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bst.bstTransferOutNumber LIKE '%").append(criteria.get("transferOutNumber")).append("%' ");
                }
            }

            if ((type != null) && (type.equals("OUT"))) {
                if (criteria.containsKey("transferOrderNumber")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bst.bstTransferOrderNumber LIKE '%").append(criteria.get("transferOrderNumber")).append("%' ");
                }
            }

            if (type.equals("OUT")) {
                if (statusType.equalsIgnoreCase("INCOMING")) {
                    jbossQl.append("bst.adBranch.brCode=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                } else if (statusType.equalsIgnoreCase("OUTGOING")) {
                    jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                }
            } else if (type.equals("ORDER")) {
                if (statusType.equalsIgnoreCase("INCOMING")) {
                    jbossQl.append("bst.adBranch.brCode=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                } else {
                    jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                }
            } else {
                jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
            }

            String orderBy = null;
            if (ORDER_BY.equals("DOCUMENT NUMBER")) {
                orderBy = "bst.bstNumber";
            }

            if (orderBy != null) {
                jbossQl.append("ORDER BY ").append(orderBy).append(", bst.bstDate");
            } else {
                jbossQl.append("ORDER BY bst.bstDate");
            }

            if (isBstLookup) {
                LIMIT = 0;
                OFFSET = 0;
            }
            Debug.print(jbossQl.toString());
            Collection invBranchStockTransfers = invBranchStockTransferHome.getBstByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (invBranchStockTransfers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object branchStockTransfer : invBranchStockTransfers) {
                LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) branchStockTransfer;
                if (isBstLookup) {
                    boolean isBstBreak = false;
                    Collection invBranchStockTransferLines = invBranchStockTransfer.getInvBranchStockTransferLines();
                    for (Object branchStockTransferLine : invBranchStockTransferLines) {
                        LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;
                        Collection invServedBsts = null;
                        if (invBranchStockTransfer.getBstType().equals("ORDER") || invBranchStockTransfer.getBstType().equals("REGULAR") || invBranchStockTransfer.getBstType().equals("EMERGENCY")) {
                            invServedBsts = invBranchStockTransferLineHome.findByBstTransferOrderNumberAndIiNameAndLocNameAndBrCode(
                                    invBranchStockTransfer.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                    invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransfer.getAdBranch().getBrCode(), AD_CMPNY);
                        } else if (invBranchStockTransfer.getBstType().equals("OUT")) {
                            invServedBsts = invBranchStockTransferLineHome.findByBstTransferOutNumberAndIiNameAndLocNameAndBrCode(
                                    invBranchStockTransfer.getBstNumber(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(),
                                    invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransfer.getAdBranch().getBrCode(), AD_CMPNY);
                        }
                        double totalQuantityReceived = 0;
                        for (Object servedBst : invServedBsts) {
                            LocalInvBranchStockTransferLine invServedBst = (LocalInvBranchStockTransferLine) servedBst;
                            totalQuantityReceived += invServedBst.getBslQuantityReceived();
                        }
                        if (invServedBsts.size() == 0 || totalQuantityReceived != invBranchStockTransferLine.getBslQuantity()) {
                            InvModBranchStockTransferDetails mdetails = new InvModBranchStockTransferDetails();
                            mdetails.setBstCode(invBranchStockTransfer.getBstCode());
                            mdetails.setBstType(invBranchStockTransfer.getBstType());
                            mdetails.setBstDate(invBranchStockTransfer.getBstDate());
                            mdetails.setBstNumber(invBranchStockTransfer.getBstNumber());
                            mdetails.setBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());
                            mdetails.setBstTransferOrderNumber(invBranchStockTransfer.getBstTransferOrderNumber());
                            if (mdetails.getBstType().equals("OUT")) {
                                mdetails.setBstBranchFrom(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                                mdetails.setBstBranchTo(invBranchStockTransfer.getAdBranch().getBrName());
                            } else {
                                mdetails.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrName());
                                mdetails.setBstBranchTo(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                            }
                            if (type.equals("IN") || (type.equals("OUT")
                                    && mdetails.getBstType().equals("OUT") &&
                                    ((!statusType.equalsIgnoreCase("INCOMING") &&
                                            invBranchStockTransfer.getBstAdBranch().equals(AD_BRNCH)) ||
                                            (invBranchStockTransfer.getAdBranch().getBrCode().equals(AD_BRNCH) &&
                                                    invBranchStockTransfer.getBstLock() == EJBCommon.FALSE &&
                                                    invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE)))) {
                                list.add(mdetails);
                            } else if (type.equals("IN") || (type.equals("ORDER")
                                    && (mdetails.getBstType().equals("ORDER") || mdetails.getBstType().equals("REGULAR") || mdetails.getBstType().equals("EMERGENCY")) &&
                                    ((!statusType.equalsIgnoreCase("INCOMING") &&
                                            invBranchStockTransfer.getBstAdBranch().equals(AD_BRNCH)) ||
                                            (invBranchStockTransfer.getAdBranch().getBrCode().equals(AD_BRNCH) &&
                                                    invBranchStockTransfer.getBstLock() == EJBCommon.FALSE &&
                                                    invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE)))) {
                                list.add(mdetails);
                            }
                            isBstBreak = true;
                        }
                        if (isBstBreak) break;
                    }
                    if (isBstBreak) continue;

                } else {
                    InvModBranchStockTransferDetails mdetails = new InvModBranchStockTransferDetails();
                    mdetails.setBstCode(invBranchStockTransfer.getBstCode());
                    mdetails.setBstType(invBranchStockTransfer.getBstType());
                    mdetails.setBstDate(invBranchStockTransfer.getBstDate());
                    mdetails.setBstNumber(invBranchStockTransfer.getBstNumber());
                    mdetails.setBstTransferOutNumber(invBranchStockTransfer.getBstTransferOutNumber());
                    mdetails.setBstTransferOrderNumber(invBranchStockTransfer.getBstTransferOrderNumber());

                    if (mdetails.getBstType().equals("OUT")) {
                        mdetails.setBstBranchFrom(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                        mdetails.setBstBranchTo(invBranchStockTransfer.getAdBranch().getBrName());
                    } else {
                        mdetails.setBstBranchFrom(invBranchStockTransfer.getAdBranch().getBrName());
                        mdetails.setBstBranchTo(this.getAdBrNameByBrCode(invBranchStockTransfer.getBstAdBranch()));
                    }
                    if (type.equals("IN") || (type.equals("OUT") && mdetails.getBstType().equals("OUT") &&
                            ((!statusType.equalsIgnoreCase("INCOMING") && invBranchStockTransfer.getBstAdBranch().equals(AD_BRNCH)) ||
                                    (invBranchStockTransfer.getAdBranch().getBrCode().equals(AD_BRNCH) &&
                                            invBranchStockTransfer.getBstLock() == EJBCommon.FALSE &&
                                            invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE)))) {

                        list.add(mdetails);
                    } else if (type.equals("IN") || (type.equals("ORDER")
                            && (mdetails.getBstType().equals("ORDER") || mdetails.getBstType().equals("REGULAR") || mdetails.getBstType().equals("EMERGENCY")) &&
                            ((!statusType.equalsIgnoreCase("INCOMING") &&
                                    invBranchStockTransfer.getBstAdBranch().equals(AD_BRNCH)) ||
                                    (invBranchStockTransfer.getAdBranch().getBrCode().equals(AD_BRNCH) &&
                                            invBranchStockTransfer.getBstLock() == EJBCommon.FALSE &&
                                            invBranchStockTransfer.getBstPosted() == EJBCommon.TRUE)))) {

                        list.add(mdetails);
                    }
                }
            }
            return list;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getInvBstSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("InvFindBranchStockTransferControllerBean getInvBstSizeByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bst) FROM InvBranchStockTransfer bst ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter
            String type = "";
            String statusType = "";

            if (criteria.containsKey("statusType")) {
                statusType = (String) criteria.get("statusType");
            }

            if (criteria.containsKey("type")) {
                type = (String) criteria.get("type");
            }

            if (criteria.containsKey("transferOutNumber")) {
                criteriaSize--;
            }

            if (criteria.containsKey("statusType")) {
                criteriaSize--;
            }

            if (criteria.containsKey("approvalStatus")) {
                String approvalStatus = (String) criteria.get("approvalStatus");
                if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED")) {
                    criteriaSize--;
                }
            }

            if (!criteria.containsKey("posted") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                criteriaSize++;
            }

            if (!criteria.containsKey("posted") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                criteriaSize++;
            }

            obj = new Object[criteriaSize];
            if (criteria.containsKey("type")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstType=?").append(ctr + 1).append(" ");
                obj[ctr] = type;
                ctr++;
            }

            if (criteria.containsKey("documentNumberFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("documentNumberTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (criteria.containsKey("brVoid")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstVoid=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("brVoid");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                String approvalStatus = (String) criteria.get("approvalStatus");
                if ((type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                    if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED") || approvalStatus.equals("PENDING")) {
                        throw new GlobalNoRecordFoundException();
                    }
                } else if ((type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                    if (approvalStatus.equals("DRAFT") || approvalStatus.equals("REJECTED") || approvalStatus.equals("PENDING")) {
                        throw new GlobalNoRecordFoundException();
                    }
                }

                if (approvalStatus.equals("DRAFT")) {
                    jbossQl.append("bst.bstApprovalStatus IS NULL ");
                } else {
                    jbossQl.append("bst.bstApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = approvalStatus;
                    ctr++;
                }

            } else if (!criteria.containsKey("approvalStatus") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("(bst.bstApprovalStatus='N/A' OR bst.bstApprovalStatus='APPROVED') AND bst.bstLock=0 ");

            } else if (!criteria.containsKey("approvalStatus") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("(bst.bstApprovalStatus='N/A' OR bst.bstApprovalStatus='APPROVED') AND bst.bstLock=0 ");
            }

            if (criteria.containsKey("posted")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");
                if (posted.equals("YES")) {
                    obj[ctr] = EJBCommon.TRUE;
                } else {
                    obj[ctr] = EJBCommon.FALSE;
                }
                ctr++;

            } else if (!criteria.containsKey("posted") && (type != null) && type.equals("OUT") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

            } else if (!criteria.containsKey("posted") && (type != null) && type.equals("ORDER") && statusType.equalsIgnoreCase("INCOMING")) {
                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("bst.bstPosted=?").append(ctr + 1).append(" ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            if ((type != null) && (type.equals("IN"))) {
                if (criteria.containsKey("transferOutNumber")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bst.bstTransferOutNumber LIKE '%").append(criteria.get("transferOutNumber")).append("%' ");
                }
            }

            if ((type != null) && (type.equals("OUT"))) {
                if (criteria.containsKey("transferOrderNumber")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bst.bstTransferOrderNumber LIKE '%").append(criteria.get("transferOrderNumber")).append("%' ");
                }
            }

            if (type.equals("OUT")) {
                if (statusType.equalsIgnoreCase("INCOMING")) {
                    jbossQl.append("bst.adBranch.brCode=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                } else if (statusType.equalsIgnoreCase("OUTGOING")) {
                    jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                }
            } else if (type.equals("ORDER")) {
                if (statusType.equalsIgnoreCase("INCOMING")) {
                    jbossQl.append("bst.adBranch.brCode=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                } else if (statusType.equalsIgnoreCase("OUTGOING")) {
                    jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
                }
            } else {
                jbossQl.append("bst.bstAdBranch=").append(AD_BRNCH).append(" AND bst.bstAdCompany=").append(AD_CMPNY).append(" ");
            }

            Collection invBranchStockTransfers = invBranchStockTransferHome.getBstByCriteria(jbossQl.toString(), obj, 0, 0);

            if (invBranchStockTransfers.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            int SIZE = invBranchStockTransfers.size();
            for (Object branchStockTransfer : invBranchStockTransfers) {
                LocalInvBranchStockTransfer invBranchStockTransfer = (LocalInvBranchStockTransfer) branchStockTransfer;
                if (type.equals("OUT") && ((invBranchStockTransfer.getAdBranch().getBrCode().equals(AD_BRNCH) && invBranchStockTransfer.getBstLock() != EJBCommon.TRUE))) {
                    SIZE = SIZE - 1;
                }
            }
            return SIZE;

        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private String getAdBrNameByBrCode(Integer BR_CODE) {
        Debug.print("InvFindBranchStockTransferControllerBean getAdBrNameByBrCode");
        try {
            return adBranchHome.findByPrimaryKey(BR_CODE).getBrBranchCode();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }


    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("InvFindBranchStockTransferControllerBean ejbCreate");
    }
}