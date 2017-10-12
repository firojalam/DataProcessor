/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */


package qa.org.qcri.suffolk;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.slf4j.LoggerFactory;

 


/**
 *
 * @author Firoj Alam
 */
public class GzReader {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(GzReader.class);
    
    /**
     *
     * @param dirName
     * @return
     */
    public ArrayList<String> readDir(String dirName) {
        ArrayList<String> xmlFileList = new ArrayList<String>();
        try {
            String dirPath = new File(dirName).getAbsolutePath();            
            File folder = new File(dirName);
            //File[] listOfFiles = folder.listFiles();
            File[] listOfFiles = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".xml.gz");
                }
            });            
            
            String fileName = null;
            for (int i = 0; i < listOfFiles.length; i++) {
                try {
                    if (listOfFiles[i].isFile()) {
                        fileName = listOfFiles[i].getName();
                        String filePath = listOfFiles[i].getAbsolutePath();
                        //String fileId = fileName.substring(0, fileName.indexOf("."));
                        xmlFileList.add(filePath);
                    }
                } catch (Exception ex) {
                    logger.trace("Some unwanted file, not in proper format.", ex);
                }
            }
        } catch (Exception ex) {
            logger.trace( "Something is wrong. Please follow the stack-trace", ex);
        }
        return xmlFileList;
    }
    
    /**
     * 
     * @param fileName
     * @return 
     */
    public ArrayList<ByteArrayInputStream> readGz(String fileName){
        ArrayList<ByteArrayInputStream> xmlFileList = new ArrayList<ByteArrayInputStream>();
        
        try {
            //String decompressedFile=fileName.substring(0,fileName.lastIndexOf("."))+".xml";
            
            GZIPInputStream gzIn = new GZIPInputStream(new BufferedInputStream(new FileInputStream(fileName)));            
            byte[] buffer = new byte[1024];
            //FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();

            int bytes_read;
            while ((bytes_read = gzIn.read(buffer)) > 0) {
                //fileOutputStream.write(buffer, 0, bytes_read);
                bo.write(buffer, 0, bytes_read);
            }
            gzIn.close();
            //fileOutputStream.close(); 
            ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
            xmlFileList.add(bin);
        } catch (FileNotFoundException ex) {
            logger.trace("File not found...", ex);
        } catch (IOException ex) {
            logger.trace("IO problem...", ex);
            ex.printStackTrace();
        }
        return xmlFileList;
    }
 
    /**
     * 
     * @param fileName
     * @return 
     */
    public ArrayList<ByteArrayInputStream> readTar(String fileName){
        ArrayList<ByteArrayInputStream> xmlFileList = new ArrayList<ByteArrayInputStream>();
        
        try {
            
            //byte[] content = new byte[(int) entry.getSize()];
            //ByteArrayInputStream in = new ByteArrayInputStream(content);
            //ZipInputStream myTarFile=new ZipInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            
            //ZipEntry entry = null;
            //while ((entry = myTarFile.getNextEntry()) != null) {
                /* Get the name of the file */
//                String fileN = entry.getName();
//                if (fileN.endsWith(".xml")) {
//                    //ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//                    //byte[] content = new byte[(int) entry.getSize()];
//                    int offset = 0;
//                    byte[] content = new byte[(int) entry.getSize()];
//                    myTarFile.read(content, offset, content.length - offset);
//                    ByteArrayInputStream in = new ByteArrayInputStream(content);
//                    xmlFileList.add(in);
//                }
           // }
//        } catch (FileNotFoundException ex) {
//            logger.trace("File not found...", ex);
        } 
        catch (Exception ex) {
            logger.trace("IO problem...", ex);
            ex.printStackTrace();
        }
        return xmlFileList;
    }
        
}
