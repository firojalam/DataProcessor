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

import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;


/**
 *
 * @author firojalam
 */
public class Reader {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Reader.class);

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
                    String cls = record.get(record.size() - 1);
                    if (!cls.isEmpty() && cls.length() > 0) {
                        if (cls.startsWith("\"")) {
                            cls = cls.substring(1);
                        }
                        if (cls.endsWith("\"")) {
                            cls = cls.substring(0, cls.length() - 1);
                        }
                    }
                    cls = cls.replaceAll(",", "").trim();
                    cls = cls.replaceAll("\\s+", "_").trim().toLowerCase();
                    if (cls.isEmpty()) {
                        continue;
                    }
                    if (cls.equalsIgnoreCase("true")||cls.equalsIgnoreCase("on-topic")||(cls.equalsIgnoreCase("yes") && event.getName().equalsIgnoreCase("airline"))) {
                        cls = "relevant";
                    } else if (cls.equalsIgnoreCase("false")||cls.equalsIgnoreCase("off-topic") ||(cls.equalsIgnoreCase("no") && event.getName().equalsIgnoreCase("airline"))) {
                        cls = "not_relevant";
                    }
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(fileName + "\t" + str);
                    LOG.error("Please check your file and check the stack-trace.", ex, fileName, str);
                }
            }
            in.close();

            event.setClassDist(classDist);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }

        return event;
    }//method read file list

    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
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
                str = str.trim();
                arr = str.split(",");
                String cls = arr[arr.length - 1];

                if (!cls.isEmpty() && cls.length() > 0) {
                    if (cls.startsWith("\"")) {
                        cls = cls.substring(1);
                    }
                    if (cls.endsWith("\"")) {
                        cls = cls.substring(0, cls.length() - 1);
                    }
                }
                cls = cls.replaceAll("\\s+", "_").trim().toLowerCase();
                if (cls.isEmpty()) {
                    continue;
                }
                if (cls.equalsIgnoreCase("true")) {
                    cls = "relevant";
                } else if (cls.equalsIgnoreCase("true")) {
                    cls = "not_relevant";
                }
                if (classDist.containsKey(cls)) {
                    int cnt = classDist.get(cls);
                    classDist.put(cls, cnt + 1);

                } else {
                    classDist.put(cls, 1);
                }
            }//end read file
            fileRead.close();
            event.setClassDist(classDist);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + fileName);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }

        return event;
    }//method read file list

    /**
     * 
     * @param filePath
     * @return 
     */
    public Event readEventCSVFileData(String filePath) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
            String str = "";
            String basename = new File(filePath).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");
            
            
            event.setYear(arr[0]);
            event.setRegion(arr[1]);
            event.setName(arr[2]);
            event.setLang(arr[arr.length - 1]);

            FileReader in = new FileReader(filePath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = record.get(record.size() - 1);
                    if (!cls.isEmpty() && cls.length() > 0) {
                        if (cls.startsWith("\"")) {
                            cls = cls.substring(1);
                        }
                        if (cls.endsWith("\"")) {
                            cls = cls.substring(0, cls.length() - 1);
                        }
                    }
                    cls = cls.replaceAll(",", "").trim();
                    cls = cls.replaceAll("[()]", "").trim();                     
                    cls = cls.replace("/", "_").trim();
                    cls = cls.replaceAll("\\s+", "_").trim().toLowerCase();
                    cls = cls.replace("_-_", "_").trim();
                    if (cls.isEmpty()) {
                        continue;
                    }
                    if (cls.equalsIgnoreCase("true")||cls.equalsIgnoreCase("on-topic")||(cls.equalsIgnoreCase("yes") && event.getName().equalsIgnoreCase("airline"))) {
                        cls = "relevant";
                    } else if (cls.equalsIgnoreCase("false")||cls.equalsIgnoreCase("off-topic") ||(cls.equalsIgnoreCase("no") && event.getName().equalsIgnoreCase("airline"))) {
                        cls = "not_relevant";
                    }
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }
                    Instance inst = new Instance();
                    String id = record.get(0);
                    String text = record.get(1);
                    inst.setLabel(cls);
                    inst.setId(id);
                    text=tokFilter.tokenizeText(text);
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(filePath + "\t" + str);
                    System.out.println(record.toString());
                    LOG.error("Please check your file and check the stack-trace.", ex, filePath);
                }
            }
            in.close();
            event.setClassDist(classDist);
            event.setInstList(eventInstList);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + filePath);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return event;
    }
    
}
