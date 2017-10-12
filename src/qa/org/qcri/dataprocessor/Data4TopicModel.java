/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.dataprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.Instance;
import qa.org.qcri.utils.ReadFileList;
import qa.org.qcri.utils.Write2File;

/**
 *
 * @author Firoj Alam
 */
public class Data4TopicModel {

    protected static final Logger log = LoggerFactory.getLogger(Data4TopicModel.class);

    public Data4TopicModel() {
    }

    public static void main(String[] args) {
        String dirName = "/Users/firojalam/QCRI/crisis_2017_data_analysis/170920155836_hurricane_maria_2017/";
        String outFileName = "/Users/firojalam/QCRI/crisis_2017_data_analysis/out/hurricane_maria_2017.csv";

//        String dirName = "/Users/firojalam/QCRI/irma_data_analysis/170826214349_hurricane_harvey_2_2017/";
//        String outFileName = "/Users/firojalam/QCRI/irma_data_analysis/out/irma_data_2017.csv";
        ReadFileList reader = new ReadFileList();
        ArrayList<String> fileList = reader.fileList(dirName);
        log.info("Number of json file: "+fileList.size());
        UnlabelledDataReader uReader = new UnlabelledDataReader();
        Write2File wrt = new Write2File();
        wrt.openFile(outFileName);
        String baseImageURL="http://scdev5.qcri.org/crisis-nlp/mexico_earthquake_2017/";
        for (Iterator<String> iterator = fileList.iterator(); iterator.hasNext();) {

            String fileName = iterator.next();
            log.info("Name of the file: " + fileName);
            Event event = uReader.readEventJSONFileData(fileName, "hurricane");            
            log.info("Name of the file: " + fileName+ "\t" + event.getInstList().size());

            //String basename = FilenameUtils.getBaseName(fileName);
            //String outFile = outFileName + "/" + basename + "_out.csv";
            for (int i = 0; i < event.getInstList().size(); i++) {
                try {
                    Instance inst = event.getInstList().get(i);
                    String txt = inst.getText().replaceAll("[\\t\\n\\r]+", " ");
                    String originalURL = inst.getImageURL();
                    String newURL = "None";
                    if(originalURL!=null ||(originalURL!=null && !originalURL.isEmpty())){
                        newURL=baseImageURL+inst.getId()+"."+FilenameUtils.getExtension(inst.getImageURL());
                    }else{
                        originalURL = "None";
                    }
                    wrt.write2File(inst.getId() + "\t" + inst.getDateOfCreation() + "\t" + inst.getTextOriginal() + "\t" + txt + "\t" + inst.getLatitude()+"\t" + inst.getLongitude() + "\t" + originalURL +"\t"+newURL+ "\n");
                } catch (Exception ex) {
                    log.error("Error in writing to the output file.");
                    ex.printStackTrace();
                }
            }
        }
        wrt.closeFile();
    }

}
