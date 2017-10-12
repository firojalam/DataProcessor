/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.processors.DataInfo;

/**
 * This is used to read the list of files from a file
 *
 * @author Firoj Alam (firojalam@gmail.com)
 * @version $Revision: 0.1 05/09/2016 $
 */
public class ReadFileList {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReadFileList.class);

    protected LinkedHashMap<String, DataInfo> fileDict = new LinkedHashMap<String, DataInfo>();

    public ReadFileList() {
    }

    public ReadFileList(String fileName) {
        this.readFileList(fileName);
    }

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return
     */
    public LinkedHashMap<String, DataInfo> readFileListEvent(String fileName) {
        LinkedHashMap<String, DataInfo> fileDict = new LinkedHashMap<String, DataInfo>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";

            while ((str = fileRead.readLine()) != null) {
                String arr[] = str.trim().split("\t");
                File theFile = new File(arr[0]);
                //String baseName = theFile.getName();
                String path = theFile.getAbsolutePath();
                DataInfo dataInfo = new DataInfo();
                dataInfo.setFilePath(path);
                //dataInfo.setCorpus(arr[1]);
                //dataInfo.setDataFormat(arr[2]);
                fileDict.put(path, dataInfo);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            logger.error("File is not available: " + fileName);
        } catch (IOException ex) {
            logger.error("Please check your file and check the stack-trace.", ex);
        }
        return fileDict;
    }//method read file list

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     */
    private void readFileList(String fileName) {
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";

            while ((str = fileRead.readLine()) != null) {
                String arr[] = str.trim().split("\t");
                File theFile = new File(arr[0]);
                String path = theFile.getAbsolutePath();
                DataInfo dataInfo = new DataInfo();
                dataInfo.setFilePath(path);
                dataInfo.setCorpus(arr[1]);
                dataInfo.setDataFormat(arr[2]);
                dataInfo.setFileOutPath(arr[3]);
                this.fileDict.put(path, dataInfo);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            logger.error("File is not available: " + fileName);
        } catch (IOException ex) {
            logger.error("Please check your file and check the stack-trace.", ex);
        }
    }//method read file list

    /**
     *
     * @return the list as a hashmap
     */
    public LinkedHashMap<String, DataInfo> getFileDict() {
        return this.fileDict;
    }

    /**
     *
     * @param fileNames
     * @param dir
     * @param allowedFormats
     * @return
     */
    public ArrayList<String> listFiles(ArrayList<String> fileNames, Path dir, String[] allowedFormats) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    listFiles(fileNames, path, allowedFormats);
                } else {
                    String ext = FilenameUtils.getExtension(path.toFile().toString());
                    for (int i = 0; i < allowedFormats.length; i++) {
                        String allowedFormat = allowedFormats[i];
                        if (allowedFormat.equals(ext)) {
                            fileNames.add(path.toFile().toString());
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Error in listing files in directory.");
        }
        return fileNames;
    }
    
    /**
     *
     * @param dirName
     * @return
     */
    public ArrayList<String> fileList(String dirName) {
        String[] ALLOWED_FORMATS = {"json"};
        File dir = new File(dirName);
        ArrayList<String> fileNames = new ArrayList();
        try {
            if (dir.exists()) {
                listFiles(fileNames, dir.toPath(), ALLOWED_FORMATS);
            }
        } catch (Exception ex) {
            logger.error("Error in creating csv file.");
        }
        return fileNames;
    }
    
}
