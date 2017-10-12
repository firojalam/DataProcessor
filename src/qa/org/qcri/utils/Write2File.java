/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.slf4j.LoggerFactory;

/**
 * Driver class to print text/string to a file.
 * <br/>
 *
 * @author Firoj Alam (firojalam@gmail.com)
 * @version $Revision: 0.1 5/09/2016 $
 */
public class Write2File {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Write2File.class);
    public BufferedWriter fileWrite = null;

    /**
     * Default constructor
     */
    public Write2File() {

    }

    /**
     * This function create a file specified by a file-name.
     *
     * @param fileName - name of the file to create
     */
    public void openFile(String fileName) {
        try {
            fileWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

        } catch (FileNotFoundException ex) {
            LOG.error("Problem in opening input file.", ex);
        } catch (Exception ex) {
            LOG.error("Please check the stack-trace...", ex);
        }
    }

    /**
     * Writes text to the end of the file
     *
     * @param str - string or text to write to the file
     */
    public void write2File(String str) {
        try {

            this.fileWrite.write(str);

        } catch (FileNotFoundException ex) {
            LOG.error("Problem in opening input file.", ex);
        } catch (Exception ex) {
            LOG.error("Please check the stack-trace...", ex);
        }
    }

    /**
     * It closes the file.
     */
    public void closeFile() {
        try {
            this.fileWrite.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Problem in opening input file.", ex);
        } catch (Exception ex) {
            LOG.error("Please check the stack-trace...", ex);
        }
    }

}
