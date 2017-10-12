/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.utils;

import java.io.File;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Firoj Alam
 */
public class LibraryPath {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(LibraryPath.class);
    private String libPath = "";

    public LibraryPath() {
        this.setLibPath();
    }

    /**
     * Get the value of libPath
     *
     * @return the value of name of the current working directory (directory of
     * the .jar)
     */
    public String getLibPath() {
        return libPath;
    }

    /**
     * Set the value of libPath
     *
     */
    public void setLibPath() {
        this.libPath = "";
        String decodedPath = "";
        try {
            File jarFile = new File(LibraryPath.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            decodedPath = jarFile.getParentFile().getPath();
            LOG.info("Current system directory: " + decodedPath);
        } catch (Exception ex) {
            LOG.error("Problem in locating the path of the library. " + decodedPath,ex);
        }
        decodedPath = "/Users/firojalam/JavaProjects/DataProcessor";
        this.libPath = decodedPath;
    }

}
