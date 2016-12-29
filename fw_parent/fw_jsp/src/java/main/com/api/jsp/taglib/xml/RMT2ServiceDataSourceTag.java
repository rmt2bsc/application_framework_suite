package com.api.jsp.taglib.xml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.api.config.old.ProviderConfig;
import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.messaging.webservice.http.client.HttpClientResourceFactory;
import com.api.messaging.webservice.http.client.HttpMessageSender;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.api.web.security.RMT2SessionBean;
import com.constants.GeneralConst;
import com.constants.RMT2ServletConst;

/**
 * Queries an external data source using the services module and exports the
 * resuls to the calling JSP as a XML String. An example of how to use this tag
 * in a JSP goes as follows:
 * 
 * <pre>
 *    &lt;xml:WebServiceQuery id="dsoName" 
 *                            serviceId="getState"
 *                            where=" country_id = 1 and description like \'%A\'"
 *                            order="descrioption asc"/&gt;
 * </pre>
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ServiceDataSourceTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 7925895142246509976L;

    /** The id of the web service to invoke */
    protected String serviceId = null;

    /** The state id parameter */
    protected String stateId = null;

    /** The country id parameter */
    protected String countryId = null;

    /** The state name parameter */
    protected String stateName = null;

    /** The abbreviated state code */
    protected String abbrvState = null;

    /** The column to order result by */
    protected String order = null;

    /**
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @param stateId
     *            the stateId to set
     */
    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    /**
     * @param countryId
     *            the countryId to set
     */
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    /**
     * @param stateName
     *            the stateName to set
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * @param abbrvState
     *            the abbrvState to set
     */
    public void setAbbrvState(String abbrvState) {
        this.abbrvState = abbrvState;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Uses HTTP URL Web Service, {@link com.services.HttpUsStatesSearchService
     * HttpUsStatesSearchService} to fetch a list of U.S. States records using
     * custom selection and ordering criteria. Returns the XML results to the
     * ancestor for exporting to JSP.
     * 
     * @return A String as XML.
     * @throws JspException
     *             General action handler errors.
     */
    protected Object initObject() throws JspException {
        super.initObject();
        HttpSession session = pageContext.getSession();
        RMT2SessionBean sessionBean = (RMT2SessionBean) session
                .getAttribute(RMT2ServletConst.SESSION_BEAN);
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext
                .getResponse();

        request.setAttribute(GeneralConst.REQ_CLIENTACTION, this.serviceId);

        if (sessionBean != null) {
            request.setAttribute(AuthenticationConst.AUTH_PROP_USERID,
                    sessionBean.getLoginId());
        }
        else {
            request.setAttribute(AuthenticationConst.AUTH_PROP_USERID,
                    AuthenticationConst.FAKE_USER);
        }
        request.setAttribute("StateId", this.stateId);
        request.setAttribute("CountryId", this.countryId);
        request.setAttribute("StateName", this.stateName);
        request.setAttribute("AbbrvState", this.abbrvState);
        request.setAttribute("OrderCriteria", this.order);
        request.setAttribute(AuthenticationConst.AUTH_PROP_MAINAPP, "contacts");

        Request genericRequest = HttpVariableScopeFactory
                .createHttpRequest(request);
        Response genericResponse = HttpVariableScopeFactory
                .createHttpResponse(response);

        HttpMessageSender client = HttpClientResourceFactory.getHttpInstance();
        ProviderConfig config;
        try {
            config = HttpClientResourceFactory.getHttpConfigInstance();
            client.connect(config);
            Object result = client.sendMessage(genericRequest);
            return result.toString();
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

}