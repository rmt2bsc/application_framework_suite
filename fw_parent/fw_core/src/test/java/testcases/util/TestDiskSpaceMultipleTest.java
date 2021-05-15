package testcases.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.api.util.HealthResults;
import com.api.util.TestDiskSpaceMultiple;

/**
 * The class <code>TestDiskSpaceMultipleTest</code> contains tests for the class <code>{@link TestDiskSpaceMultiple}</code>.
 */
public class TestDiskSpaceMultipleTest {
    /**
     * Run the TestDiskSpaceMultiple() constructor test.
     *
     */
    @Test
    public void testTestDiskSpaceMultiple_1() throws Exception {
        TestDiskSpaceMultiple result = new TestDiskSpaceMultiple();
        assertNotNull(result);

    }

    /**
     * Run the HealthResults execute(HealthResults) method test.
     *
     * @throws Exception
     *
     */
    @Test
    public void testExecute_1() throws Exception {
        TestDiskSpaceMultiple fixture = new TestDiskSpaceMultiple();
        fixture.execute(new HealthResults());
        HealthResults results = new HealthResults();

        HealthResults result = fixture.execute(results);

        assertNotNull(result);
        assertEquals(0, result.getNumLogins());
//        assertEquals(true, result.isDiskSpaceOK());

    }

    /**
     * Run the void main(String[]) method test.
     *
     * @throws Exception
     *
     */
    @Test
    public void testMain_1() throws Exception {
        String[] args = new String[] {};

        TestDiskSpaceMultiple.main(args);

    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     *
     */
    @Before
    public void setUp() throws Exception {
        // add additional set up code here
    }

    /**
     * Perform post-test clean-up.
     *
     * @throws Exception
     *             if the clean-up fails for some reason
     *
     */
    @After
    public void tearDown() throws Exception {
        // Add additional tear down code here
    }

    /**
     * Launch the test.
     *
     * @param args
     *            the command line arguments
     *
     */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(TestDiskSpaceMultipleTest.class);
    }
}