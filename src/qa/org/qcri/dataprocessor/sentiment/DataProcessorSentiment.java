package qa.org.qcri.dataprocessor.sentiment;

import qa.org.qcri.dataprocessor.*;
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
public class DataProcessorSentiment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileName = "/Users/firojalam/QCRI/Sentiment_data/semeval16_subtask_A/test.tsv";
        String fileout = "/Users/firojalam/QCRI/Sentiment_data/semeval16_subtask_A/test_feat.csv";
//        String fileName = "/Users/firojalam/QCRI/Sentiment_data/semeval16_subtask_A/training.1600000.processed.noemoticon.csv";
//        String fileout = "/Users/firojalam/QCRI/Sentiment_data/semeval16_subtask_A/training.1600000.processed.noemoticon_feat.csv";

//        String fileName = args[0].trim();
//        String fileout = args[1].trim();
//        String type = args[2].trim();

        String type = "semeval";
        type = "senti140";
        type = "other";
//        type = "senti_original";
        ReadFileList fileList = new ReadFileList();
        LinkedHashMap<String, DataInfo> fileDict = fileList.readFileListEvent(fileName);

        Output output = new Output();
        //other cat
//        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
        String file = fileName;
        File f = new File(file);
        String fname = f.getName();
        String out = fileout; //+"/"+fname;
//            DataInfo dataInfo = entry.getValue();
        Event event = null;
        SentiDataReader reader = new SentiDataReader();
        if (type.equalsIgnoreCase("other")) {
            event = reader.readEventTSVFileData(fileName, type);
        } else if(type.equalsIgnoreCase("senti140")) {
            event = reader.readEventCSVFileData140(fileName, type);
        }else if(type.equalsIgnoreCase("senti_original")) {
            event = reader.readEventCSVFileDataOrginal(fileName, type);
        }
        output.writeEvent(event, out);
//        }     

    }

}
