package testcases.general;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.util.RMT2HashUtility;
import com.util.RMT2String;

public class StringTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String str1 = new String("gabbar");
        String str2 = new String("gabbar");
        String str3 = "gabbar";

        System.out.println(str1 == str1.intern());
        System.out.println(str3 == str1.intern());
        System.out.println(str3 == str2.intern());
        return;

    }

    @Test
    public void testStringToMD5() {
        String str = "This is a String to MD5 test";
        String results = RMT2HashUtility.md5(str);
        System.out.println("MD5: " + results);
    }

    @Test
    public void testStringToSHA() {
        String str = "This is a String to SHA test";
        String results = RMT2HashUtility.sha(str);
        System.out.println("SHA: " + results);
    }

    @Test
    public void testReplaceAll2() {
        String test = "Smokin' In The Pit";
        String val = RMT2String.replaceAll2(test, "''", "'");
        test = "Smokin' In The' Pit";
        val = RMT2String.replaceAll2(test, "''", "'");
        test = "In The Pit";
        val = RMT2String.replaceAll2(test, "''", "'");
        System.out.println(val);

    }
}
