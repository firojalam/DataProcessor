/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */

package qa.org.qcri.utils;

import org.slf4j.LoggerFactory;

/**
 *
 * @author firoj.alam
 */
public class MachineInfo {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(MachineInfo.class);
    
    public MachineInfo() {
    }
    
    
    /**
     * 
     * It detects the OS on the system it is running 
     * @return os information
     */
    public String getOSInfo() {
        String osInfo = "";
        String str = System.getProperty("os.name");
        if (str.startsWith("Windows")) {
            osInfo = "windows";
        } else if (str.startsWith("Mac")) {
            osInfo = "mac";
        } else if (str.startsWith("Linux")) {
            osInfo = "linux";
        }
        return osInfo;
    }        


    /**
     * Reads number of core does the system has 
     * @return number of threads that system will run.
     */
    public int getNumberOfThreads() {
        int selectedThreads = 1;
        try {
            int threads = Runtime.getRuntime().availableProcessors();
            if (threads > 1) {
                selectedThreads = (threads * 50) / 100;
            } else {
                selectedThreads = threads;
            }
            LOG.info( "Number of core "+threads+" and selected to run "+ selectedThreads);
        } catch (Exception ex) {
            LOG.error("Problem in reading number of processors.", ex);
        }
        return selectedThreads;
    }
    
}
