package com.ejb.txn.ad;

import jakarta.ejb.Local;

import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.util.SendEmailDetails;

@Local
public interface AdApprovalDocumentEmailNotificationController {
    void sendPurchaseRequisitionEmail(LocalAdApprovalQueue adApprovalQueue, Integer companyCode);

    void setTestEmail(String username, String password, String emailTo);

    String sendEmailToApprover(LocalAdApprovalQueue adApprovalQueue, SendEmailDetails sendEmailDetails);
}