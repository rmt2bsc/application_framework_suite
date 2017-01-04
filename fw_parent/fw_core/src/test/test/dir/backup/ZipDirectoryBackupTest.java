package test.dir.backup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author royterrell
 * 
 */
public class ZipDirectoryBackupTest {
    private String inPath;

    private String outPath;

    private String zipFileName;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // this.inPath =
        // "/Users/royterrell/Documents/ProjectServer/FileBackup/src/test/data/in";
        // this.outPath =
        // "/Users/royterrell/Documents/ProjectServer/FileBackup/src/test/data/out";
        // this.inPath = "c:/ProjectServer/fw2012/src/test/data/in";
        // this.outPath = "c:/ProjectServer/fw2012/src/test/data/out";
        this.inPath = "//rmtdaldb01/archives";
        this.outPath = "//rmtdalmedia01/data";

        this.zipFileName = "testZipFile";
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        // DirectoryBackup mgr = new DirectoryBackup();
        // try {
        // mgr.process(this.inPath, this.outPath, this.zipFileName, true);
        // } catch (BatchFileException e) {
        // e.printStackTrace();
        // }
    }

}
