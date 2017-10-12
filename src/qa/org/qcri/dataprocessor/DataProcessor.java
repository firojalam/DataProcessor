package qa.org.qcri.dataprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import qa.org.qcri.dataprocessor.processors.DataInfo;
import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.EventProcess;
import qa.org.qcri.dataprocessor.processors.Output;
import qa.org.qcri.dataprocessor.processors.Reader;
import qa.org.qcri.dataprocessor.processors.ReaderCF;
import qa.org.qcri.dataprocessor.processors.ReaderCrisisLex26;
import qa.org.qcri.dataprocessor.processors.ReaderIScram;
import qa.org.qcri.utils.ReadFileList;

/**
 *
 * @author Firoj Alam
 */
public class DataProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String fileName = "/Users/firojalam/AIDR/file_list/all_events.txt";
        String fileout = "/Users/firojalam/AIDR/data_analysis/all_events_stat.txt";

        ReadFileList fileList = new ReadFileList(fileName);
        LinkedHashMap<String, DataInfo> fileDict = fileList.getFileDict();

        Output output = new Output();
        ArrayList<Event> eventList = new ArrayList();
        //other cat
        Reader reader = new Reader();
        ReaderCF readerCF = new ReaderCF();
        ReaderIScram readerIS = new ReaderIScram();
        ReaderCrisisLex26 readerCLex26 = new ReaderCrisisLex26();

        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
            //String fName = entry.getKey();
            DataInfo dataInfo = entry.getValue();
            if (dataInfo.getDataFormat().equalsIgnoreCase("other")) {
                Event event = reader.readEventCSVFile(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("cf")) {
                Event event = readerCF.readEventCSVFile(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("iscram")) {
                Event event = readerIS.readEventCSVFile(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("CrisisLexT26")) {
                Event event = readerCLex26.readEventCSVFile(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            }
        }
        EventProcess eventProcess = new EventProcess();
        eventList = eventProcess.removeUnwanted(eventList); // removes the instances with frequency 1.
        TreeSet<String> classSet = eventProcess.getClassList(eventList);
        HashMap<Event,ArrayList<Integer>> eventTable =  eventProcess.designEventClassDistTable(eventList,classSet);
        //eventProcess.designClassWiseEventDist(eventTable,classSet);
        eventProcess.designClassWiseSum(eventTable,classSet);
        
        //output.writeEventTable(eventTable, classSet, fileout);
        
        
        
        //output.write(eventList, fileout);

        //eventList=reader.readEventFiles(fileDict);
        //output.write(eventList, fileout);
        //other CF
//        ReaderCF readerCF = new ReaderCF();
//        eventList=readerCF.readEventFiles(fileDict);
//        output.write(eventList, fileout);
//        
//        //other ISCRAM
//        ReaderIScram readerIS = new ReaderIScram();
//        eventList=readerIS.readEventFiles(fileDict);
//        output.write(eventList, fileout);
    }

}
