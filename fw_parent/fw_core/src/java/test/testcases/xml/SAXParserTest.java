package testcases.xml;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import testcases.TestCaseConstants;

import com.api.config.AppPropertyPool;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.xml.parsers.datasource.RMT2OrmDatasourceParser;
import com.api.xml.parsers.datasource.RMT2OrmDatasourceParserFactory;

/**
 * Test parsing datasurce type XML documents via RMT2OrmDatasourceParserFactory
 * class
 * 
 * @author Roy Terrell
 * 
 */
public class SAXParserTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        AppPropertyPool pool = AppPropertyPool.getInstance();
        System.setProperty("SAXDriver", "org.apache.xerces.parsers.SAXParser");
        // System.setProperty("webapps_drive", "c:");
        // System.setProperty("webapps_dir",
        // "\\projects\\framework\\Data\\test");
        // System.setProperty("app_dir", "\\framework");
        // System.setProperty("datasource_dir", "\\data\\");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSax2Implementation() {
        String xmlDoc = TestCaseConstants.DATA_DIR + "CreditorView.xml";
        RMT2OrmDatasourceParserFactory f = RMT2OrmDatasourceParserFactory
                .getNewInstance();
        Assert.assertNotNull(f);
        RMT2OrmDatasourceParser parser = f.getSax2OrmDatasourceParser(xmlDoc);
        Assert.assertNotNull(parser);
        ObjectMapperAttrib r = parser.parseDocument();
        Assert.assertNotNull(r);
        Assert.assertEquals(13, r.getColumnCount());
        DataSourceColumn dsc = r.getDsoColumn("CreditorId");
        String dbName = dsc.getDbName();
        Assert.assertEquals("creditor_id", dbName);
        return;

    }
}
