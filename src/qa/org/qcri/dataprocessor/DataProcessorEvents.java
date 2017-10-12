package qa.org.qcri.dataprocessor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import qa.org.qcri.dataprocessor.processors.DataInfo;
import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.Output;
import qa.org.qcri.dataprocessor.processors.Reader;
import qa.org.qcri.dataprocessor.processors.ReaderCF;
import qa.org.qcri.utils.ReadFileList;

/**
 *
 * @author Firoj Alam
 */
public class DataProcessorEvents {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String fileName = "/Users/firojalam/QCRI/crisis_domain_adaptation/file_list/events_binary_class.txt";
//        String fileout = "/Users/firojalam/QCRI/crisis_domain_adaptation/data";
        String fileName = "/Users/firojalam/QCRI/crisis_domain_adaptation/file_list/events_multi_class.txt";
        String fileout = "/Users/firojalam/QCRI/crisis_domain_adaptation/data/multi";

        String type = "other";
        type = "cf";
        ReadFileList fileList = new ReadFileList();
        LinkedHashMap<String, DataInfo> fileDict = fileList.readFileListEvent(fileName);

        Output output = new Output();
        //other cat
        Reader reader = new Reader();
        ReaderCF readerCF = new ReaderCF();
        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
            String file = entry.getKey();  
            File f = new File(file);
            String fname = f.getName();
            String out = fileout+"/"+fname;
            DataInfo dataInfo = entry.getValue();
            Event event = null;
            if(type.equalsIgnoreCase("other")){
                event = reader.readEventCSVFileData(dataInfo.getFilePath());
            }else{
                event = readerCF.readEventCSVFileData(dataInfo.getFilePath());
            }
            output.writeEvent(event, out);
        }     
        
        
    }

}
