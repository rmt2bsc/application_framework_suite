package com.api.messaging.email.smtp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.email.AbstractMailImpl;
import com.api.messaging.email.EmailException;
import com.api.messaging.email.EmailMessageBean;
import com.sun.mail.smtp.SMTPTransport;

/**
 * This class implements SmtpApi interface which is used for sending emails
 * using the SMTP Protocol. Other implementations could follow as this concept
 * matures sucha as a POP3 api.
 * 
 * @author RTerrell
 * 
 */
class SmtpImpl extends AbstractMailImpl implements SmtpApi {

    private Logger logger = Logger.getLogger(AbstractMailImpl.class);

    /**
     * Creates an CommonMailImpl object which the identification of the host
     * server's name is supplied by the user. Obtains other service information
     * that is needed to establish a valid connection such authentication
     * requirements, and user id/password.
     * 
     * @throws EmailException
     *             General intialization errors.
     */
    protected SmtpImpl(ProviderConfig config) throws EmailException {
        super();
        this.config = config;
        this.initApi();
        return;
    }

    /**
     * Performs the bulk of instance initialization. To date, the SMTP name is
     * obtained from the System properties collection and any authentication
     * information that may be needed to establish a valid service connection.
     * respnsible for creating the email session object.
     * 
     * @throws EmailException
     *             Problem obtaining the SMTP host server name from System
     *             properties.
     */
    protected void initApi() throws EmailException {
        this.setEmailSession(null);
        this.email = null;
    }

    /**
     * Set Session with properties that is required to establish a connection to
     * the SMTP server
     * 
     * (non-Javadoc)
     * 
     * @see com.api.messaging.AbstractMessagingImpl#getConnection()
     */
    @Override
    public Object connect(ProviderConfig config) throws ProviderConnectionException {
        props.put("mail.transport.protocol", "smtp");
        this.props.put("mail.smtp.host", this.config.getHost());

        // Determine if authentication is required
        if (this.config.isAuthenticate()) {
            // Enable SMTP authentication
            props.put("mail.smtp.auth", "true");
        }

        // TODO: This is gmail specific. Add to AppServer configuration file
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        return super.connect(config);
    }

    /**
     * Closes the this service and terminates its connection. Also, member
     * variales are reinitialized to null.
     * 
     * @throws SystemException
     *             for errors occurring during the closing process.
     */
    public void close() throws SystemException {
        super.close();
        this.email = null;
    }

    /**
     * Creates and sends an email message using the data contained in
     * <i>emailData</i>.
     * 
     * @param emailData
     *            Required to be n instance of
     *            {@link com.api.messaging.email.EmailMessageBean
     *            EmailMessageBean} containing data representing the components
     *            that comprises an email structure: 'From', 'To', 'Subject',
     *            'Body', and any attachments.
     * @return int The SMTP return code.
     * @throws MessageException
     *             SMTP server is invalid or not named, validation errors,
     *             invalid assignment of data values to the email message, email
     *             transmission errors, or <i>emailData</i> is of the incorrect
     *             type.
     */
    public Object sendMessage(Serializable emailData) throws MessageException {
        if (emailData instanceof EmailMessageBean) {
            this.emailBean = (EmailMessageBean) emailData;
        }
        else {
            this.msg = "Problem interpreting email message.  Email package must be of type, EmailMessageBean";
            logger.log(Level.ERROR, this.msg);
            throw new MessageException(this.msg);
        }
        try {
            this.validate();
            Message msg = this.setupEmailComponents();
            return this.transportMessageSmtp(msg);
        } catch (EmailException e) {
            throw new MessageException(e);
        } catch (ProviderConnectionException e) {
            e.printStackTrace();
            throw new MessageException(e);
        }
    }

    /**
     * Validates the EMailBean Object by ensuring that the object is
     * instantiated, the To Address has at least on email address, and the From
     * Address is populated with an email address.
     * 
     * @throws EMailException
     *             if the email bean is null, the From-Address and/or the
     *             To-Address is null.
     */
    private void validate() throws EmailException {
        if (this.emailBean == null) {
            throw new EmailException("Email bean object is not properly intialized");
        }
        if (this.emailBean.getFromAddress() == null) {
            throw new EmailException("Email From-Address is required");
        }
        if (this.emailBean.getToAddress() == null) {
            throw new EmailException("Email To-Address is required");
        }
    }

    /**
     * Assigns EmailMessageBean values to the MimeMessage component. Various
     * Exceptions are caught based on a given issue.
     * 
     * @throws EMailException
     *             When modifications are applied to an email bean component
     *             that is flagged as not modifyable or the occurrence of a
     *             general messaging error.
     */
    private Message setupEmailComponents() throws EmailException {
        InternetAddress addr[];
        Message email = (Message) this.connect(this.config);
        String component = null;

        // Create a MIME style email message
        if (email == null) {
            this.msg = "Seupt of Email MIME message operation failed.  Mime Message has not been initialized";
            logger.log(Level.ERROR, this.msg);
            throw new EmailException(this.msg);
        }

        // Begin initializing E-Mail Components
        try {
            component = this.emailBean.getFromAddress().toString();
            email.setFrom(this.emailBean.getFromAddress());

            // Add To addresses
            component = InternetAddress.toString(this.emailBean.getToAddress());
            component = "To Recipients";
            email.setRecipients(MimeMessage.RecipientType.TO, this.emailBean.getToAddress());

            // Check if we need to add CC Recipients
            component = "CC Recipients";
            addr = this.emailBean.getCCAddress();
            if (addr != null && addr.length > 0) {
                email.setRecipients(MimeMessage.RecipientType.CC, addr);
            }

            // Check if we need to add BCC Recipients
            component = "BCC Recipients";
            addr = this.emailBean.getBCCAddress();
            if (addr != null && addr.length > 0) {
                email.setRecipients(MimeMessage.RecipientType.BCC, addr);
            }

            component = "Subject Line";
            email.setSubject(this.emailBean.getSubject());
            component = "Sent Date";
            email.setSentDate(new Date());
            component = "Header Line";
            email.setHeader("X-Mailer", "MailFormJava");

            Multipart mp = new MimeMultipart();

            // HTML email body
            email.setDataHandler(new DataHandler(new HTMLDataSource(this.emailBean.getBody())));

            // MimeBodyPart mbp = new MimeBodyPart();
            // mbp.setContent(this.emailBean.getBody(),
            // EmailMessageBean.HTML_CONTENT);
            // mbp.setDataHandler(new DataHandler(new
            // HTMLDataSource(this.emailBean.getBody())));
            // mp.addBodyPart(mbp);

            // Add required attachment
            // this.emailBean.addAttachment(SmtpApi.REQUIRED_EMAIL_ATTACHMENT);

            // Add ramining attachments from client, if available
            // List<MimeBodyPart> clientAttachments =
            // this.emailBean.getAttachments();
            // for (MimeBodyPart attachment : clientAttachments) {
            // mp.addBodyPart(attachment);
            // }
            // email.setContent(mp);

            return email;
        } catch (IllegalWriteException e) {
            throw new EmailException("The following email bean componenet can not be modified: " + component);
        } catch (IllegalStateException e) {
            throw new EmailException("The following email bean componenet exist in a read-only folder: " + component);
        } catch (MessagingException e) {
            throw new EmailException("A generic messaging error occurred for email bean component: " + component);
        }
    }

    private int transportMessageSmtp(Message msg) throws EmailException {
        try {
            // Setup SMTP transport
            SMTPTransport t = (SMTPTransport) this.emailSession.getTransport("smtp");

            // connect to server
            t.connect(this.config.getHost(), this.config.getUserId(), this.config.getPassword());

            // send message
            t.sendMessage(msg, msg.getAllRecipients());

            // Display server response
            logger.info("Response: " + t.getLastServerResponse());

            // Close SMTP transport
            t.close();
            return t.getLastReturnCode();
        } catch (Exception e) {
            this.msg = "A problem occured attempting to setup SMTP transport, connect to SMTP server, sending the email message, or closing the SMTP transport";
            logger.error(this.msg, e);
            throw new EmailException(this.msg, e);
        }
    }
    

    /**
     * 
     * @author appdev
     *
     */
    static class HTMLDataSource implements DataSource {

        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) {
                throw new IOException("html message is null!");
            }
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public String getName() {
            return "HTMLDataSource";
        }
    }
}
