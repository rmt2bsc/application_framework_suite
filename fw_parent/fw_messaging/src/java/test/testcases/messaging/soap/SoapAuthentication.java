/**
 * 
 */
package testcases.messaging.soap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.messaging.MessagingResourceFactory;
import com.api.web.security.RMT2SecurityToken;
import com.api.web.security.UserAuthenticationFactory;
import com.api.web.security.UserAuthenticator;

/**
 * @author rterrell
 * 
 */
public class SoapAuthentication extends CommonSoapTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doSuccessfulAuthentication() {
        try {
            UserAuthenticator a = UserAuthenticationFactory
                    .getAuthenticator(
                            "com.api.security.authentication.web.BasicSoapUserAuthenticationImpl",
                            this.getConfig());
            RMT2SecurityToken results = (RMT2SecurityToken) a.authenticate(
                    "admin", "drum7777", "accounting",
                    "KFDKS99393KDKDKDSKS39393KDKD9d");

            String xml = (String) results.getToken();

            // This line of code will not work in this application since the
            // framework does no anything about the compiled JAXB objects.
            // However, this line of code is applicable when the framework is a
            // part of an application context that is aware of JAXB objects.
            Object obj = MessagingResourceFactory.getJaxbMessageBinder()
                    .unMarshalMessage(xml);
            a.logout("admin", "KFDKS99393KDKDKDSKS39393KDKD9d");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Test
    // public void doInvalidUserAuthentication() {
    // try {
    // UserAuthenticator a =
    // UserAuthenticationFactory.getAuthenticator("com.api.security.authentication.web.BasicSoapUserAuthenticationImpl",
    // this.getConfig());
    // a.authenticate("xxxxxxx", "dummy", "accounting",
    // "KFDKS99393KDKDKDSKS39393KDKD9d");
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void doInvalidPasswordAuthentication() {
    // try {
    // UserAuthenticator a =
    // UserAuthenticationFactory.getAuthenticator("com.api.security.authentication.web.BasicSoapUserAuthenticationImpl",
    // this.getConfig());
    // a.authenticate("admin", "dummy", "accounting",
    // "KFDKS99393KDKDKDSKS39393KDKD9d");
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //

    // @Test
    // public void doLogout() {
    // try {
    // UserAuthenticator a =
    // UserAuthenticationFactory.getAuthenticator("com.api.security.authentication.web.BasicSoapUserAuthenticationImpl",
    // this.getConfig());
    // a.logout("admin", "KFDKS99393KDKDKDSKS39393KDKD9d");
    // }
    // catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

}
