/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.annotationtask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.processors.DataInfo;
import org.apache.commons.io.FilenameUtils;

/**
 * This is used to read the list of files from a file
 *
 * @author Firoj Alam (firojalam@gmail.com)
 * @version $Revision: 0.1 05/09/2016 $
 */
public class DataReader {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DataReader.class);

    protected LinkedHashMap<String, DataInfo> fileDict = new LinkedHashMap<String, DataInfo>();

    public DataReader() {
    }

    public DataReader(String fileName) {

    }

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     */
    public ArrayList<String> readCategories(String fileName) {
        ArrayList<String> catList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";

            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                if (str.isEmpty()) {
                    continue;
                }
                catList.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (IOException ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return catList;
    }//method read file list

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public ArrayList<String> readImages(String fileName) {
        ArrayList<String> catList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            ArrayList<String> imageList = new ArrayList<String>();
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                if (str.isEmpty()) {
                    continue;
                }
                File f = new File(str);
                String name = f.getName();
                String ext1 = FilenameUtils.getExtension(name);
//                if (name.contains(".")) {
//                    name = name.substring(0, name.lastIndexOf('.'));
//                }
                imageList.add(name.trim());
                catList.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (IOException ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return catList;
    }//method read file list    

    public ArrayList<String> getImageListfromPath(String path) {
        ArrayList<String> imageList = new ArrayList();
        Queue<File> dirs = new LinkedList<File>();
        dirs.add(new File(path));
        Matcher matcher;
        //String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
//        Pattern pattern = Pattern.compile(IMAGE_PATTERN);

        while (!dirs.isEmpty()) {
            for (File f : dirs.poll().listFiles()) {

                if (f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile()) {
                    String name = f.getName();
                    String ext1 = FilenameUtils.getExtension(name);
                    if (ext1.equalsIgnoreCase("jpg") | ext1.equalsIgnoreCase("png") | ext1.equalsIgnoreCase("gif") | ext1.equalsIgnoreCase("bmp")) {
//                    if (name.contains(".")) {
//                        name = name.substring(0, name.lastIndexOf('.'));
//                    }
//                    matcher = pattern.matcher(name);
//                    if(matcher.matches())
                        imageList.add(name.trim());
                    }
                }
            }
        }
        return imageList;
    }

    public HashMap<String, String> getImageList(String path) {
        HashMap<String, String> imageList = new HashMap();
        Queue<File> dirs = new LinkedList<File>();
        dirs.add(new File(path));
        while (!dirs.isEmpty()) {
            for (File f : dirs.poll().listFiles()) {

                if (f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile()) {
                    String name = f.getName();
                    String ext1 = FilenameUtils.getExtension(name);
                    //if (ext1.equalsIgnoreCase("jpg")) {
//                    if (name.contains(".")) {
//                        name = name.substring(0, name.lastIndexOf('.'));
//                    }
                    if (ext1.equalsIgnoreCase("jpg") | ext1.equalsIgnoreCase("png") | ext1.equalsIgnoreCase("gif") | ext1.equalsIgnoreCase("bmp")) {
                        imageList.put(FilenameUtils.getBaseName(name), name);
                    }
                }
            }
        }
        return imageList;
    }
}
