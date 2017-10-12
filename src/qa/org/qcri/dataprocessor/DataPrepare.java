package qa.org.qcri.dataprocessor;

import qa.org.qcri.dataprocessor.processors.ClassGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import org.apache.log4j.Logger;

import qa.org.qcri.dataprocessor.processors.DataInfo;
import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.EventProcess;
import qa.org.qcri.dataprocessor.processors.Output;
import qa.org.qcri.dataprocessor.processors.Reader;
import qa.org.qcri.dataprocessor.processors.ReaderAIDR;
import qa.org.qcri.dataprocessor.processors.ReaderCF;
import qa.org.qcri.dataprocessor.processors.ReaderCrisisLex26;
import qa.org.qcri.dataprocessor.processors.ReaderIScram;
import qa.org.qcri.dataprocessor.processors.ReaderOChile;
import qa.org.qcri.utils.ReadClassListToFilter;
import qa.org.qcri.utils.ReadFileList;


/**
 *
 * @author Firoj Alam
 */
public class DataPrepare {
    private static Logger LOG = Logger.getLogger(DataPrepare.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String fileName = "/Users/firojalam/QCRI/Domain_Adaptation/file_list/all_events.txt";
        String fileout = "/Users/firojalam/QCRI/Domain_Adaptation/data_analysis/all_events_stat.txt";
        String fileoutpath = "/Users/firojalam/QCRI/Domain_Adaptation/data/preprocessed_filtered";
        //String fileoutCluster = "/Users/firojalam/QCRI/Domain_Adaptation/data/all_events_cluster_filtered.csv";
        String fileoutData = "/Users/firojalam/QCRI/Domain_Adaptation/data/all_events_data_filtered.csv";

        ReadFileList fileList = new ReadFileList(fileName);
        LinkedHashMap<String, DataInfo> fileDict = fileList.getFileDict();

        Output output = new Output();
        ArrayList<Event> eventList = new ArrayList();
        //other cat
        Reader reader = new Reader();
        ReaderCF readerCF = new ReaderCF();
        ReaderIScram readerIS = new ReaderIScram();
        ReaderCrisisLex26 readerCLex26 = new ReaderCrisisLex26();
        ReaderAIDR readerAIDR = new ReaderAIDR();
        ReaderOChile readerOChile = new ReaderOChile();

        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
            //String fName = entry.getKey();
            DataInfo dataInfo = entry.getValue();
            if (dataInfo.getDataFormat().equalsIgnoreCase("other")) {
                Event event = reader.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("cf")) {
                Event event = readerCF.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("iscram")) {
                Event event = readerIS.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            } else if (dataInfo.getDataFormat().equalsIgnoreCase("CrisisLexT26")) {
                Event event = readerCLex26.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            }else if (dataInfo.getDataFormat().equalsIgnoreCase("aidr")) {
                Event event = readerAIDR.readEventCSVFileData(dataInfo.getFilePath());
                if (event != null) {
                    event.setEventSource(dataInfo.getCorpus());
                    eventList.add(event);
                }
            }
            else if (dataInfo.getDataFormat().equalsIgnoreCase("other-chile")) {
                Event event = readerOChile.readEventCSVFileData(dataInfo.getFilePath());
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
        //eventProcess.designClassWiseSum(eventTable,classSet);
        //output.writeEventTable(eventTable, classSet, fileout);
        //output.writeDataClassWise(eventTable, classSet, fileoutpath);
        //output.writeDataCluster(eventTable, classSet, fileoutCluster);
        
        
        //Filtering the class - not related and lower frequent
        HashSet<String> clessesToFilter = ReadClassListToFilter.readFileList("/Users/firojalam/QCRI/Domain_Adaptation/data_analysis/class_list_to_remove_for_group.txt");
        HashMap<String, ClassGroup> classGroup = ReadClassListToFilter.readFileListClsGroup("/Users/firojalam/QCRI/Domain_Adaptation/aidr_class_group.txt");
        String outfile="/Users/firojalam/QCRI/Domain_Adaptation/aidr_class_group_desc.txt";
        output.groupDescription(classGroup,outfile,clessesToFilter);
        //uniques.removeAll(dups);
        System.out.println("Size before filtering: "+classSet.size());
        classSet.removeAll(clessesToFilter);
        System.out.println("Size after filtering: "+classSet.size());
        //output.writeDataClassWise(eventTable, classSet, fileoutpath);
        //output.writeDataCluster(eventTable, classSet, fileoutCluster);
        output.writeAllData(eventTable, classSet, fileoutData,classGroup);        
    }

}
