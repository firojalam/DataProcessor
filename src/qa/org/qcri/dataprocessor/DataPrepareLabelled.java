package qa.org.qcri.dataprocessor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import qa.org.qcri.dataprocessor.processors.DataInfo;
import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.Output;
import qa.org.qcri.dataprocessor.processors.Reader;
import qa.org.qcri.dataprocessor.processors.ReaderAIDR;
import qa.org.qcri.dataprocessor.processors.ReaderCF;
import qa.org.qcri.dataprocessor.processors.ReaderCrisisLex26;
import qa.org.qcri.dataprocessor.processors.ReaderIScram;
import qa.org.qcri.dataprocessor.processors.ReaderOChile;
import qa.org.qcri.utils.ReadFileList;


/**
 *
 * @author Firoj Alam
 */
public class DataPrepareLabelled {
    private static Logger LOG = Logger.getLogger(DataPrepareLabelled.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String fileName = "/Users/firojalam/QCRI/crisis_domain_adaptation/aidr_filelist.txt";

        ReadFileList fileList = new ReadFileList(fileName);
        LinkedHashMap<String, DataInfo> fileDict = fileList.getFileDict();
        Output output = new Output();

        //other cat
        Reader reader = new Reader();
        ReaderCF readerCF = new ReaderCF();
        ReaderIScram readerIS = new ReaderIScram();
        ReaderCrisisLex26 readerCLex26 = new ReaderCrisisLex26();
        ReaderAIDR readerAIDR = new ReaderAIDR();
        ReaderOChile readerOChile = new ReaderOChile();

        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
            DataInfo dataInfo = entry.getValue();
            File f = new File(dataInfo.getFilePath());
            String fname = f.getName();
            if(fname.contains(".")) 
                fname = fname.substring(0, fname.lastIndexOf('.'));
            
            String fileout = dataInfo.getFileOutPath()+"/"+fname+".csv";
            
            if (dataInfo.getDataFormat().equalsIgnoreCase("other")) {
                Event event = reader.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("cf")) {
                Event event = readerCF.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("iscram")) {
                Event event = readerIS.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("CrisisLexT26")) {
                Event event = readerCLex26.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                }
            }else if (dataInfo.getDataFormat().equalsIgnoreCase("aidr")) {
                Event event = readerAIDR.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    output.writeEvent(event, fileout);
                }
            }
            else if (dataInfo.getDataFormat().equalsIgnoreCase("other-chile")) {
                Event event = readerOChile.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                }
            }
        }
      
    }

}
