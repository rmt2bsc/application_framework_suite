package com.api.messaging.email.smtp;

import com.api.messaging.MessageManager;

/**
 * Interface that provides methods for setting up and sending messages via the
 * SMTP protocol. There are two ways to use this api which centers around how
 * the email content is assembled prior to transmission: <i>simple</i> and
 * <i>template</i>.
 * <p>
 * The sipmle approach basically assembles the <i>From</i>, <i>To</i>,
 * <i>Subject</i>, <i>Body</i>, and <i>Attachments</i> comonents to a
 * MimeMessage and then proceeds to send the email to its intended recipients.
 * Conversly, the template approach requires or uses a tempalting engine to
 * generate dynamic content which closely resembles the concepts of Mail Merge.
 * <p>
 * <b>USAGE</b>
 * 
 * <pre>
 * <b><u>Simple Example:</u></b>
 *    // Setup bean that represents the email message.
 *    EmailMessageBean bean = new EmailMessageBean();
 *    bean.setFromAddress("roy.terrell@aviall.com");
 *    
 *    // You can optionally enter multiple email addresses separated by commas
 *    bean.setToAddress("someother.address@aviall.com");  <br>
 *    bean.setSubject("SMTP Email Test");  
 *    bean.setBody("Test", EmailBean.HTML_CONTENT);
 *    
 *    // Declare and initialize SMTP api and allow the system to discover SMTP host 
 *    SmtpApi api = SmtpFactory.getSmtpIntance();
 *    // Send simple email to its intended destination
 *    try {
 *       api.sendMessage(bean); 
 *       // Close the service.
 *       api.close();
 *    }
 *    catch (Exception e) {
 *       // handle error
 *    }
 *    
 * <b><u>Template Example:</u></b>
 *    // Setup bean that represents the email message.
 *    EmailBean bean = new EmailBean();
 *    bean.setFromAddress("roy.terrell@aviall.com");
 *    
 *    // You can optionally enter multiple email addresses separated by commas
 *    bean.setToAddress("someother.address@aviall.com");  <br>
 *    bean.setSubject("SMTP Email Test");  
 *    
 *    // Declare and initialize SMTP api and allow the system to discover SMTP host 
 *    SmtpApi api = AviallEmailFactory.getSmtpIntance();
 *    
 *    // Create a Hashtable containg the data that will dynamically substitute 
 *    // the place holders in the Velocity document.
 *    // For all intents and purposes, we will be processing a Velocity docuemnt 
 *    // named, <i>contact.vm</i>, which contains the following variable place 
 *    // holders, firstname and lastname.
 *    Map tempData = new Hashtable();
 *    tempData.put("firstname", "Bill");
 *    tempData.put("lastname", "Clinton");
 *    
 *    // Process template email and send the results to its intended recipients
 *    try {
 *       api.sendMessage(bean, tempData, "contact"); 
 *       // Close the service.
 *       api.close();
 *    }
 *    catch (Exception e) {
 *       // handle error
 *    }
 * </pre>
 * 
 * @author RTerrell
 * 
 */
public interface SmtpApi extends MessageManager {

    String REQUIRED_EMAIL_ATTACHMENT = "images/logotip.jpg";

}
