package com.api.messaging.email.smtp;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import javax.mail.Address;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.NoSuchProviderException;
import javax.mail.AuthenticationFailedException;
import javax.mail.IllegalWriteException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;
import com.api.messaging.email.AbstractMailImpl;
import com.api.messaging.email.EmailException;
import com.api.messaging.email.EmailMessageBean;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Date;

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

    private boolean useTemplate;

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
     * Set up properties that is required to establish a connection to the SMTP
     * server
     * 
     * (non-Javadoc)
     * 
     * @see com.api.messaging.AbstractMessagingImpl#getConnection()
     */
    @Override
    public Object connect(ProviderConfig config)
            throws ProviderConnectionException {
        props.put("mail.transport.protocol", "smtp");
        this.props.put("mail.smtp.host", this.config.getHost());

        // Determine if authentication is required
        if (this.config.isAuthenticate()) {
            // Enable SMTP authentication
            props.put("mail.smtp.auth", "true");
        }
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
     * @return int Always returns null.
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
            this.connect(this.config);
            this.setupMessage();
            this.transportMessage();
            return null;
        } catch (EmailException e) {
            throw new MessageException(e);
        } catch (ProviderConnectionException e) {
            e.printStackTrace();
            throw new MessageException(e);
        }
    }

    /**
     * Creates and sends an email message using the concepts of "Mail Merge"
     * using the template engine, Velocity, to build dynamic content.
     * <i>tempRootName</i> is the targeted template document containing the
     * place holder variables that will be substituted by embedded dynamic
     * content.
     * 
     * @param emailData
     *            An instance of
     *            {@link com.aviall.apps.dotcom.util.email.EmailMessageBean
     *            EmailMessageBean} containing data representing the components
     *            that comprises an email structure: 'From', 'To', 'Subject',
     *            and any attachments. Content for the body component is not
     *            reuqired since this method will be dynamically managing it.
     * @param tempData
     *            A Map containing the data that will replace the template's
     *            place holder variables.
     * @param tempRootName
     *            The root filename of the template that is to be processed. Do
     *            not include the file extension since this process requires the
     *            template extension to exist as ".vm".
     * @return int Always return 1.
     * @throws EmailException
     *             When the email template resource cannot be found, tempalte
     *             parsing failed due to a syntax error, the invocation of the
     *             template failed, or a general SMTP error(s).
     */
    public int sendMessage(EmailMessageBean emailData,
            Map<Object, Object> tempData, String tempRootName)
            throws EmailException {

        this.useTemplate = true;
        this.emailBean = emailData;
        this.validate();
        this.setupMessage();

        // Manage the velocity template
        VelocityContext context = null;
        Template template = null;
        String tempName = null;
        try {
            // Create an instance of the Velocity engine
            VelocityEngine engine = this.createTemplateEngine();

            // Identify the template we want to work with
            tempName = tempRootName + ".vm";
            template = engine.getTemplate(tempName);

            // Apply dynamic data values to email template.
            context = this.createTemplateContext(tempData, tempRootName);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            writer.close();

            // Assign results of template merge to the body of the actual mime
            // message
            this.emailBean.setBody(writer.getBuffer().toString(), null);
            this.email.setContent(this.emailBean.assembleBody());
        } catch (ResourceNotFoundException e) {
            throw new EmailException("Email template, " + tempName
                    + ", could not be found");
        } catch (ParseErrorException e) {
            throw new EmailException(
                    "Syntax error occurred in email template, " + template
                            + ".  Problem parsing the template");
        } catch (MethodInvocationException e) {
            throw new EmailException("The invocation of email template, "
                    + template + ", threw an exception");
        } catch (Exception e) {
            throw new EmailException(
                    "A general SMTP error occurred for email template, "
                            + template + ".  Additional message: "
                            + e.getMessage());
        }

        // Send email
        this.transportMessage();
        return 1;
    }

    /**
     * Creates an instance of the Velocity runtime engine which employs the
     * 'Classpath' resource loader to manage templates.
     * 
     * @return VelocityEngine
     * @throws EmailException
     *             engine failed to be initialized.
     */
    private VelocityEngine createTemplateEngine() throws EmailException {
        // Identify and configure resource loader properties
        String resourceType = System.getProperty("mail.resourcetype");
        Properties props = new Properties();
        String resourceLoader = "resource.loader";
        String loaderName = null;
        String implClass = null;
        String path = ".resource.loader.path";

        if (resourceType.equalsIgnoreCase("file")) {
            // Setup file resource loader
            loaderName = "filesystem";
            implClass = loaderName + ".resource.loader.class";
            String filePath = System.getProperty("mail.templatepath");
            path = loaderName + path;
            props.setProperty(resourceLoader, loaderName);
            props.setProperty(implClass, FileResourceLoader.class.getName());
            props.setProperty(path, filePath);

        }
        else if (resourceType.equalsIgnoreCase("class")) {
            // Setup class resource loader
            loaderName = "classpath";
            implClass = loaderName + ".resource.loader.class";
            props.setProperty(resourceLoader, loaderName);
            props.setProperty(implClass,
                    ClasspathResourceLoader.class.getName());
        }

        // Initialize velocity engine.
        VelocityEngine engine = new VelocityEngine();
        try {
            engine.init(props);
            return engine;
        } catch (Exception e) {
            throw new EmailException(e);
        }
    }

    /**
     * Creates the Velocity context instance using a Map of template data values
     * and the root name of the template file that will be processed.
     * 
     * @param data
     *            The data that is to be applied to the template.
     * @param tempName
     *            The name of the template file without the file extension.
     * @return {@link VelocityContext}
     */
    private VelocityContext createTemplateContext(Map<Object, Object> data,
            String tempName) {
        VelocityContext context = new VelocityContext();
        context.put(tempName, data);
        return context;
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
            throw new EmailException(
                    "Email bean object is not properly intialized");
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
    private void setupMessage() throws EmailException {
        InternetAddress addr[];
        String component = null;

        // Create a MIME style email message
        if (this.email == null) {
            this.msg = "Seupt of Email MIME message operation failed.  Mime Message has not been initialized";
            logger.log(Level.ERROR, this.msg);
            throw new EmailException(this.msg);
        }

        // Begin initializing E-Mail Components
        try {
            component = this.emailBean.getFromAddress().toString();
            this.email.setFrom(this.emailBean.getFromAddress());

            // Add To addresses
            component = InternetAddress.toString(this.emailBean.getToAddress());
            component = "To Recipients";
            this.email.setRecipients(MimeMessage.RecipientType.TO,
                    this.emailBean.getToAddress());

            // Check if we need to add CC Recipients
            component = "CC Recipients";
            addr = this.emailBean.getCCAddress();
            if (addr != null && addr.length > 0) {
                this.email.setRecipients(MimeMessage.RecipientType.CC, addr);
            }

            // Check if we need to add BCC Recipients
            component = "BCC Recipients";
            addr = this.emailBean.getBCCAddress();
            if (addr != null && addr.length > 0) {
                this.email.setRecipients(MimeMessage.RecipientType.BCC, addr);
            }

            component = "Subject Line";
            this.email.setSubject(this.emailBean.getSubject());
            component = "Sent Date";
            this.email.setSentDate(new Date());
            component = "Header Line";
            this.email.setHeader("X-Mailer", "MailFormJava");

            // Do not manipulate body content if we are processing the body via
            // Velocity
            if (!this.useTemplate) {
                component = "Body Content";
                this.email.setContent(this.emailBean.assembleBody());
            }
        } catch (IllegalWriteException e) {
            throw new EmailException(
                    "The following email bean componenet can not be modified: "
                            + component);
        } catch (IllegalStateException e) {
            throw new EmailException(
                    "The following email bean componenet exist in a read-only folder: "
                            + component);
        } catch (MessagingException e) {
            throw new EmailException(
                    "A generic messaging error occurred for email bean component: "
                            + component);
        }
    }

    /**
     * Performs the actual routing of message. If authentication is required,
     * then a transport object is instantiated in order to email the message.
     * Otherwise, the message is sent using a static call to "send" of the
     * Transport object. Various Exceptions are caught based on a given issue.
     * 
     * @throws EmailException
     */
    private void transportMessage() throws EmailException {
        String msg = null;
        Transport tp;

        try {
            if (this.config.isAuthenticate()) {
                if (this.getEmailSession() == null) {
                    this.msg = "Send message operation failed.  Connection to SMTP server has not be established";
                    logger.log(Level.ERROR, this.msg);
                    throw new EmailException(this.msg);
                }
                // Address addr[] = this.emailBean.getToAddress();
                tp = this.getEmailSession().getTransport();
                try {
                    tp.connect(this.config.getHost(), this.config.getUserId(),
                            this.config.getPassword());
                } catch (Throwable t) {
                    throw new EmailException(t.getMessage());
                }
                if (tp.isConnected()) {
                    // this.email.saveChanges();
                    // Must use the Transport's sendMessage method to transport
                    // email since authentication is required.
                    tp.sendMessage(this.email, this.email.getAllRecipients());
                    tp.close();
                    tp = null;
                }
            }
            else {
                // Send message the simple way
                Transport.send(this.email);
            }
            System.out.println("Email was sent successfully to "
                    + this.emailBean.getRecipients());
        } catch (NoSuchProviderException e) {
            throw new EmailException("No Such SMTP Provider Exist", e);
        } catch (AuthenticationFailedException e) {
            throw new EmailException("Authentication Failed", e);
        } catch (IllegalStateException e) {
            msg = "Illegal State error occurred for recipient(s): "
                    + this.emailBean.getRecipients()
                    + ".  Additional details: " + e.getMessage();
            throw new EmailException(msg, e);
        } catch (SendFailedException e) {
            msg = "Email send failed error occurred for recipient(s): "
                    + this.emailBean.getRecipients()
                    + ".  Additional details: " + e.getMessage();
            throw new EmailException(msg, e);
        } catch (MessagingException e) {
            msg = "Messaging error occurred for recipient(s): "
                    + this.emailBean.getRecipients()
                    + ".  Additional details: " + e.getMessage();
            e.printStackTrace();
            throw new EmailException(msg, e);
        }

    }

}
