package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jakarta.ejb.EJBException;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdApprovalQueue;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.dao.ap.LocalApCanvassHome;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.util.EJBContextClass;
import com.util.mod.ad.AdModApprovalQueueDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.SendEmailDetails;

@Stateless(name = "AdApprovalDocumentEmailNotificationControllerEJB")
public class AdApprovalDocumentEmailNotificationControllerBean extends EJBContextClass implements AdApprovalDocumentEmailNotificationController {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final boolean STARTTLS_ENABLE = true;
    private static final boolean SMTP_AUTH = true;
    private static final String SMTP_USER = "info@omegabci.com";
    private static final String SMTP_PASS = "website.omega001!";
    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;
    @EJB
    private LocalApCanvassHome apCanvassHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;

    public void sendPurchaseRequisitionEmail(LocalAdApprovalQueue adApprovalQueue, Integer companyCode) {

        Debug.print("AdApprovalDocumentEmailNotificationControllerBean sendPurchaseRequisitionEmail");

        StringBuilder composedEmail = new StringBuilder();
        LocalApPurchaseRequisition apPurchaseRequisition;
        try {

            apPurchaseRequisition = apPurchaseRequisitionHome.findByPrimaryKey(adApprovalQueue.getAqDocumentCode());
            HashMap hm = new HashMap();

            for (Object value : apPurchaseRequisition.getApPurchaseRequisitionLines()) {

                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) value;
                Collection apCanvasses = apCanvassHome.findByPrlCodeAndCnvPo(apPurchaseRequisitionLine.getPrlCode(), EJBCommon.TRUE, companyCode);

                for (Object canvass : apCanvasses) {
                    LocalApCanvass apCanvass = (LocalApCanvass) canvass;
                    if (hm.containsKey(apCanvass.getApSupplier().getSplSupplierCode())) {
                        AdModApprovalQueueDetails adModApprovalQueueExistingDetails = (AdModApprovalQueueDetails) hm.get(apCanvass.getApSupplier().getSplSupplierCode());
                        adModApprovalQueueExistingDetails.setAqSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());
                        adModApprovalQueueExistingDetails.setAqSupplierName(apCanvass.getApSupplier().getSplName());
                        adModApprovalQueueExistingDetails.setAqAmount(adModApprovalQueueExistingDetails.getAqAmount() + apCanvass.getCnvAmount());
                    } else {
                        AdModApprovalQueueDetails adModApprovalQueueNewDetails = new AdModApprovalQueueDetails();
                        adModApprovalQueueNewDetails.setAqSupplierCode(apCanvass.getApSupplier().getSplSupplierCode());
                        adModApprovalQueueNewDetails.setAqSupplierName(apCanvass.getApSupplier().getSplName());
                        adModApprovalQueueNewDetails.setAqAmount(apCanvass.getCnvAmount());
                        hm.put(apCanvass.getApSupplier().getSplSupplierCode(), adModApprovalQueueNewDetails);
                    }
                }
            }

            Set set = hm.entrySet();
            composedEmail.append("<table border='1' style='width:100%'>");
            composedEmail.append("<tr>");
            composedEmail.append("<th> VENDOR </th>");
            composedEmail.append("<th> AMOUNT </th>");
            composedEmail.append("</tr>");

            for (Object o : set) {
                Map.Entry me = (Map.Entry) o;
                AdModApprovalQueueDetails adModApprovalQueueDetails = (AdModApprovalQueueDetails) me.getValue();
                composedEmail.append("<tr>");
                composedEmail.append("<td>");
                composedEmail.append(adModApprovalQueueDetails.getAqSupplierName());
                composedEmail.append("</td>");
                composedEmail.append("<td>");
                composedEmail.append(adModApprovalQueueDetails.getAqAmount());
                composedEmail.append("</td>");
                composedEmail.append("</tr>");
            }
            composedEmail.append("</table>");

        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
        String emailTo = adApprovalQueue.getAdUser().getUsrEmailAddress();
        Properties props = new Properties();

        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        // props.put("mail.smtp.auth", SOCKET_FACTORY_PORT );
        props.put("mail.smtp.port", SMTP_AUTH);

        Session session = Session.getDefaultInstance(props, null);
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ofs-notifcation@daltron.net.pg"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));


            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("cromero@wrcpng.com"));

            message.setSubject("DALTRON - OFS - PURCHASE REQUISTION APPROVAL PR #:" + apPurchaseRequisition.getPrNumber());

            System.out.println("adApprovalQueue.getAqRequesterName()=" + adApprovalQueue.getAqRequesterName());
            Debug.print("adApprovalQueue.getAqDocumentNumber()=" + adApprovalQueue.getAqDocumentNumber());
            Debug.print("adApprovalQueue.getAdUser().getUsrDescription()=" + adApprovalQueue.getAdUser().getUsrDescription());

            message.setContent("Dear Mr/Mrs," + adApprovalQueue.getAdUser().getUsrDescription() + "<br><br>" + "A purchase request was raised by " + adApprovalQueue.getAqRequesterName() + " for your approval.<br>" + "PR Number: " + adApprovalQueue.getAqDocumentNumber() + ".<br>" + "Delivery Period: " + EJBCommon.convertSQLDateToString(apPurchaseRequisition.getPrDeliveryPeriod()) + ".<br>" + "Description: " + apPurchaseRequisition.getPrDescription() + ".<br>" + composedEmail +

                    "Please click the link <a href=\"http://180.150.253.99:8080/daltron\">http://180.150.253.99:8080/daltron</a>.<br><br><br>" +

                    "This is an automated message and was sent from a notification-only email address.<br><br>" + "Please do not reply to this message.<br><br>", "text/html");


            Transport.send(message);
            adApprovalQueue.setAqEmailSent(EJBCommon.TRUE);

            Debug.print("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public String sendEmailToApprover(LocalAdApprovalQueue adApprovalQueue, SendEmailDetails sendEmailDetails) {

        Debug.print("sendEmailToApprover AdApprovalDocumentEmailNotificationController");

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        LocalAdPreference adPreference;
        try {

            adCompany = adCompanyHome.findByPrimaryKey(adApprovalQueue.getAqAdCompany());
            adBranch = adBranchHome.findByPrimaryKey(adApprovalQueue.getAqAdBranch());
            adPreference = adPreferenceHome.findByPrfAdCompany(adCompany.getCmpCode());
        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        if (adApprovalQueue.getAdUser() == null) {
            // no user
            return "No approver user exist";
        }
        String approversEmail = adApprovalQueue.getAdUser().getUsrEmailAddress();
        if (approversEmail == null || approversEmail.isEmpty()) {
            // no email user
            return "No approver email exist";
        }
        String approverUsername = adApprovalQueue.getAdUser().getUsrName();
        String approverName = adApprovalQueue.getAdUser().getUsrDescription();
        String documentType = adApprovalQueue.getAqDocument();
        String documentNumber = adApprovalQueue.getAqDocumentNumber();
        String requester = adApprovalQueue.getAqRequesterName();

        String APP_CODE = this.getAppCodeByDocumentType(documentType);
        String BR_CODE = adApprovalQueue.getAqAdBranch().toString();
        String LOCATION = this.getLocationParameter(APP_CODE, adApprovalQueue.getAqDocument(), adApprovalQueue.getAqDocumentNumber());
        String URL = "http://" + SendEmailDetails.HOST + "/" + adCompany.getCmpShortName() + "/adLogon.do?isLink=1&brCode=" + BR_CODE + "&appCode=" + APP_CODE + LOCATION;

        String subject = adCompany.getCmpShortName() + "-" + adBranch.getBrDescription() + "-" + documentType;
        String messageString = "A request " + documentType + " was raised by " + requester + " for your approval.<br> " + " Document Number: <a href=\"" + URL + "\" >" + documentNumber + "</a> " + "<br><br>This is an automated message and was sent from a notification-only email address.<br><br>" + "Please do not reply to this message.<br><br>";

        String smtpuser = adPreference.getPrfMailFrom();
        String smtPpass = adPreference.getPrfMailPassword();

        Properties props = new Properties();

        Debug.print("host: " + adPreference.getPrfMailHost());
        Debug.print("port: " + adPreference.getPrfMailPort());
        Debug.print("auth: " + EJBCommon.byteToBoolean(adPreference.getPrfMailAuthenticator()));
        Debug.print("user: " + smtpuser);
        Debug.print("pass: " + smtPpass);

        props.put("mail.smtp.host", adPreference.getPrfMailHost());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", adPreference.getPrfMailPort());
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpuser, smtPpass);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpuser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(approversEmail));

            if (!adPreference.getPrfMailCc().isEmpty() && adPreference.getPrfMailCc() != null) {
                message.addRecipient(RecipientType.BCC, new InternetAddress(adPreference.getPrfMailCc()));
            }

            if (!adPreference.getPrfMailBcc().isEmpty() && adPreference.getPrfMailBcc() != null) {
                message.addRecipient(RecipientType.BCC, new InternetAddress(adPreference.getPrfMailBcc()));
            }


            message.setSubject(subject);

            message.setContent(messageString, "text/html");

            Transport.send(message);

        } catch (MessagingException e) {
            Debug.print(e.getMessage());
            return "Send email failed";


        }
        return "Email sent";

    }

    public void setTestEmail(String username, String password, String emailToVal) {

        Debug.print("setTestEmail sendPurchaseRequisitionEmail");

        StringBuilder composedEmail = new StringBuilder();
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.omegabci.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("support@omegabci.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailToVal));


            message.setSubject("subject sample");

            String test = "11 line" + System.lineSeparator() +
                    "22 line" + System.lineSeparator();
            message.setContent(test, "text/html");


            Transport.send(message);


            Debug.print("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAppCodeByDocumentType(String DOCUMENT_TYPE) {

        String[] splitString = DOCUMENT_TYPE.split(" ");
        Debug.print("doc type is " + DOCUMENT_TYPE);
        Debug.print("splitString length " + splitString.length);
        if (splitString.length > 2) {
            String shortAppName = splitString[0];

            Debug.print("shortAppName" + splitString[0]);
            if (shortAppName.equalsIgnoreCase("GL")) {
                return "1";

            } else if (shortAppName.equalsIgnoreCase("AR")) {
                return "2";

            } else if (shortAppName.equalsIgnoreCase("AP")) {
                return "3";

            } else if (shortAppName.equalsIgnoreCase("CM")) {
                return "5";

            } else if (shortAppName.equalsIgnoreCase("INV")) {
                return "6";

            } else {
                return "0";
            }


        } else {
            return "0";
        }
    }

    private String getLocationParameter(String APP_CODE, String DOCUMENT_TYPE, String DOCUMENT_NUMBER) {

        String url = "&location=";
        switch (APP_CODE) {
            case "1":
                url += "/glApproval.do?document=" + DOCUMENT_TYPE + "%26documentNumberFrom=" + DOCUMENT_NUMBER + "%26documentNumberTo=" + DOCUMENT_NUMBER + "%26maxRows=20%26goButton=1";

                break;
            case "2":
                url += "/arApproval.do?document=" + DOCUMENT_TYPE + "%26documentNumberFrom=" + DOCUMENT_NUMBER + "%26documentNumberTo=" + DOCUMENT_NUMBER + "%26maxRows=20%26goButton=1";

                break;
            case "3":
                url += "/apApproval.do?document=" + DOCUMENT_TYPE + "%26documentNumberFrom=" + DOCUMENT_NUMBER + "%26documentNumberTo=" + DOCUMENT_NUMBER + "%26maxRows=20%26goButton=1";

                break;
            case "5":
                url += "/cmApproval.do?document=" + DOCUMENT_TYPE + "%26documentNumberFrom=" + DOCUMENT_NUMBER + "%26documentNumberTo=" + DOCUMENT_NUMBER + "%26maxRows=20%26goButton=1";

                break;
            case "6":
                url += "/invApproval.do?document=" + DOCUMENT_TYPE + "%26documentNumberFrom=" + DOCUMENT_NUMBER + "%26documentNumberTo=" + DOCUMENT_NUMBER + "%26maxRows=20%26goButton=1";

                break;
            default:
                return "";
        }
        return url;
    }
}