/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.dataprocessor.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Firoj Alam
 */
public class ReaderAIDR {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReaderAIDR.class);

    public ArrayList<Event> readEventFiles(HashMap<String, DataInfo> fileDict) {
        ArrayList<Event> eventList = new ArrayList<Event>();
        for (Map.Entry<String, DataInfo> entry : fileDict.entrySet()) {
            String fileName = entry.getKey();
            DataInfo dataInfo = entry.getValue();
            Event event = this.readEventFile(dataInfo.getFilePath());
            event.setEventSource(dataInfo.getCorpus());
            eventList.add(event);
        }
        return eventList;
    }

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     */
    public Event readEventCSVFile(String fileName) {
        Event event = new Event();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        try {
            
            String str = "";
            String basename = new File(fileName).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");
            event.setYear(arr[0]);
            event.setRegion(arr[1]);
            event.setName(arr[2]);
            event.setLang(arr[arr.length - 1]);

            FileReader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = record.get(5);                    
                    if (!cls.isEmpty() && cls.length() > 0) {
                        if (cls.startsWith("\"")) {
                            cls = cls.substring(1);
                        }
                        if (cls.endsWith("\"")) {
                            cls = cls.substring(0, cls.length() - 1);
                        }
                    }
                    cls = cls.replaceAll(",", "").trim();
                    cls=cls.replaceAll("\\s+", "_").trim().toLowerCase();
                    if(cls.isEmpty()) continue;
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(fileName+"\t"+str);
                    LOG.error("Please check your file and check the stack-trace.", ex, fileName, str);
                }
            }//end read file
            in.close();
            event.setClassDist(classDist);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Please check your file and check the stack-trace.", ex);
        }

        return event;
    }//method read file list
    
    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     * @return 
     */
    public Event readEventFile(String fileName) {
        Event event = new Event();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        try {

            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            String basename = new File(fileName).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");
            event.setYear(arr[0]);
            event.setRegion(arr[1]);
            event.setName(arr[2]);
            event.setLang(arr[arr.length - 1]);

            fileRead.readLine(); // considering the first character is header 
            while ((str = fileRead.readLine()) != null) {
                try {
                    str = str.trim();
                    arr = str.split(",");
                    String cls = arr[5];
                    
                    if (!cls.isEmpty() && cls.length() > 0) {
                        if (cls.startsWith("\"")) {
                            cls = cls.substring(1);
                        }
                        if (cls.endsWith("\"")) {
                            cls = cls.substring(0, cls.length() - 1);
                        }
                    }
                    cls=cls.replaceAll("\\s+", "_").trim().toLowerCase();
                    if(cls.isEmpty()) continue;
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(fileName+"\t"+str);
                    LOG.error("Please check your file and check the stack-trace.", ex, fileName, str);
                }
            }//end read file
            fileRead.close();
            event.setClassDist(classDist);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Please check your file and check the stack-trace.", ex);
        }

        return event;
    }//method read file list

    public Event readEventCSVFileData(String fileName) {
        Event event = new Event();
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {

            String str = "";
            String basename = new File(fileName).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");
            event.setYear(arr[0]);
            event.setRegion(arr[1]);
            event.setName(arr[2]);
            event.setLang(arr[arr.length - 1]);

            FileReader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = record.get(record.size()-1);
                    if (!cls.isEmpty() && cls.length() > 0) {
                        if (cls.startsWith("\"")) {
                            cls = cls.substring(1);
                        }
                        if (cls.endsWith("\"")) {
                            cls = cls.substring(0, cls.length() - 1);
                        }
                    }
                    cls = cls.replaceAll("[()]", "").trim();                    
                    cls = cls.replaceAll(",", "").trim();                    
                    cls = cls.replace("/", "_").trim();
                    cls = cls.replaceAll("\\s+", "_").trim().toLowerCase();
                    cls = cls.replace("_-_", "_").trim();
                    if (cls.isEmpty()) {
                        continue;
                    }
                    if (cls.equalsIgnoreCase("true")||cls.equalsIgnoreCase("on-topic")) {
                        cls = "relevant";
                    } else if (cls.equalsIgnoreCase("false")||cls.equalsIgnoreCase("off-topic")) {
                        cls = "not_relevant";
                    }
                    if (cls.equalsIgnoreCase("yes") && event.getName().equalsIgnoreCase("airline")) {
                        cls = "terrorism_related";
                    } else if (cls.equalsIgnoreCase("no") && event.getName().equalsIgnoreCase("airline")) {
                        cls = "terrorism_not_related";
                    }
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }
//                    if(cls.equalsIgnoreCase("donations_of_supplies_and/or_volunteer_work")){
//                        System.out.println("here");
//                    }
                    Instance inst = new Instance();
                    String id = record.get("tweet_id");
                    String text = record.get(record.size()-3);
                    inst.setLabel(cls);
                    inst.setId(id);
                    text=tokFilter.tokenizeText(text);
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(fileName + "\t" + str);
                    LOG.error("Please check your file and check the stack-trace.", ex, fileName, str);
                }
            }//end read file
            in.close();
            event.setClassDist(classDist);
            event.setInstList(eventInstList);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Please check your file and check the stack-trace.", ex);
        }

        return event;
    }
}
