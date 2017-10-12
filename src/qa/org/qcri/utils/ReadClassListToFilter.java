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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.processors.ClassGroup;

/**
 *
 * @author firojalam
 */
public class ReadClassListToFilter {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReadClassListToFilter.class);

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return 
     */
    public static HashSet<String> readFileList(String fileName) {
        HashSet<String> classSet = new HashSet<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                classSet.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (IOException ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return classSet;
    }//method read file list
    
    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return 
     */
    public static HashMap<String, ClassGroup> readFileListClsGroup(String fileName) {
        HashMap<String, ClassGroup> classGroup = new HashMap<String, ClassGroup>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            fileRead.readLine();
            ClassGroup clsGroup = null;
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                //System.out.println(str);
                String arr[]=str.split("\t");
                String grpName = arr[1];
//                if(grpName.equalsIgnoreCase("informative")){
//                    System.out.println("Wait");
//                }
                String className = arr[2];
                String source = arr[3];
                String annotationDesc = arr[4];
                grpName = grpName.replaceAll("\\s+", "_").toLowerCase().trim();
                className = className.replaceAll("\\s+", "_").toLowerCase().trim();
                clsGroup = new ClassGroup();
                clsGroup.setGrpName(grpName);
                clsGroup.setClassName(className);
                clsGroup.setSource(source);
                clsGroup.setAnnotationDesc(annotationDesc);
                classGroup.put(className, clsGroup);
                //classSet.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (IOException ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return classGroup;
    }//method read file list    
}
