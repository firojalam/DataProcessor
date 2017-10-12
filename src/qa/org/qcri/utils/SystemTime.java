/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.utils;

import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author firojalam
 */
public class SystemTime {
    protected static final Logger logger = Logger.getLogger(SystemTime.class);
  public static void duration(Date d1, Date d2) {

        String timeTaken = "";
        try {
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            timeTaken = diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds.";
        } catch (Exception ex) {
            logger.error("Time cal problem.", ex);
        }
        logger.info("Time taken: " + timeTaken);

    }        
}
