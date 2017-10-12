/**
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. <br/>
 * http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package qa.org.qcri.suffolk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.AIDRdata;

/**
 * This is used to read the list of files from a file
 *
 * @author Firoj Alam (firojalam@gmail.com)
 * @version $Revision: 0.1 12/28/2013 $
 */
public class ReadFileList {

    private static Logger LOG = Logger.getLogger(ReadFileList.class.getName());
    protected HashMap<String, String> fileDict = new HashMap<String, String>();

    public ReadFileList(String fileName) {
        this.readFileList(fileName);
    }

    public ReadFileList() {
    }

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public HashMap<String, String> readFileList(String fileName) {
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                File theFile = new File(str);
                String baseName = theFile.getName();
                String path = theFile.getAbsolutePath();
                if (!this.fileDict.containsKey(baseName)) {
                    this.fileDict.put(baseName, str);
                } else {
                    System.out.println(str + " - this file is already used.");
                }
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return fileDict;
    }//method read file list

    /**
     *
     * @return the list as a hashmap
     */
    public HashMap<String, String> getFileDict() {
        return this.fileDict;
    }

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public ArrayList<String> readJsonList(String fileName) {
        ArrayList<String> jsonList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                File theFile = new File(str);
                String baseName = theFile.getName();
                String path = theFile.getAbsolutePath();
                jsonList.add(path);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return jsonList;
    }//method read file list

    /**
     * 
     * @param file
     * @return 
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    } 
    /**
     *
     * @param directoryName
     * @param files
     */
    public void listFiles(String directoryName, ArrayList<String> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && getFileExtension(file).equalsIgnoreCase("json") && !file.getName().startsWith("Classified")) {
                files.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }
    }       

    
    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public ArrayList<String> readEventList(String fileName) {
        ArrayList<String> eventList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                eventList.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return eventList;
    }//method read file list

    
    /**
     * 
     * @param eventList
     * @param fileList
     * @return 
     */
    public ArrayList<String> filter(ArrayList<String> eventList, ArrayList<String> fileList) {
        ArrayList<String> filteredList = new ArrayList<String>();
        for (int i = 0; i < fileList.size(); i++) {
            String fname = fileList.get(i);
            for (int j = 0; j < eventList.size(); j++) {
                String event = eventList.get(j);
                if(fname.contains(event)){
                    filteredList.add(fname);
                }
            }
        }
        return filteredList;
    }
    
    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public ArrayList<String> readPrevTweet(String fileName) {
        ArrayList<String> tIDList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            fileRead.readLine();
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                String[] arr = str.split("\\s+");
                tIDList.add(arr[0].trim());
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return tIDList;
    }//method read file list
    
    /**
     * 
     * @param fileList
     * @return 
     */
    public ArrayList<String> filterEvent(ArrayList<String> fileList) {
        ArrayList<String> filteredList = new ArrayList<String>();
        //150517124204_twbdemo/150517124204_twbdemo_20150518_vol-2.json
        int i;
        for (i = 0; i < fileList.size(); i++) {
            String fname = fileList.get(i);
            //if(fname.contains("150517124204_twbdemo/150517124204_twbdemo_20150518_vol-2.json"))
            LOG.info("File Ignored: "+fname);
            if(fname.contains("150517124204_twbdemo/150517124204_twbdemo_20150518_vol-2.json"))
            {
                break;
            }
        }
        for (int j = i; j < fileList.size(); j++) {
            String fname = fileList.get(j);
            filteredList.add(fname);
        }
        return filteredList;
    }    
    
}
