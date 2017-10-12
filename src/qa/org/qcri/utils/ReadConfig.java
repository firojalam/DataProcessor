/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.slf4j.LoggerFactory;

/**
 * This is used to the configuration file of the system, which contains
 * different parameters value. And stores the parameters and values in a
 * dictionary as a key-value pair.
 *
 * @author Firoj Alam (firojalam@gmail.com)
 * @version $Revision: 0.1 05/09/2016 $
 */
public class ReadConfig {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReadConfig.class);
    protected HashMap<String, String> configDict = new HashMap<String, String>();
    /**
     * Constructor
     * @param fileName name of the file
     */
    public ReadConfig(String fileName) {
        setConfig(fileName);
    }
    /**
     * Reads configuration file
     * @param fileName name of the file
     */
    private void setConfig(String fileName) {
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                if (str.startsWith("#") || str.trim().isEmpty()) {
                    continue;
                } else {
                    String[] strArray = str.split("\\s*=\\s*");
                    String key = strArray[0].trim();
                    String val = strArray[1].trim();
                    if (!this.configDict.containsKey(key)) {
                        this.configDict.put(key, val);
                    }
                }
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your configuration file. Check that whether it is exist or not.");            
        } catch (Exception ex) {
            LOG.error("Something severe!! Pleae check the stack-trace.",ex);
        }

    }
    /**
     * Configuration dictionary
     * @return system configuration dictionary
     */
    public HashMap<String, String> getConfigDict() {
        return this.configDict;
    }

    //main method
    public static void main(String[] args) {
        //for testing
        ReadConfig obj;
        obj = new ReadConfig("config.txt");
    }
}
