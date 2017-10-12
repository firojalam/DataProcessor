/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.dataprocessor.sentiment;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import java.io.BufferedReader;
import qa.org.qcri.dataprocessor.processors.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author firojalam
 */
public class SentiDataReader {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SentiDataReader.class);

    public SentiDataReader() {
        
        try {
            this.init("./profilelangdetect/profiles/");
        } catch (LangDetectException ex) {
            LOG.error("Error in loading model.");
        }
    }

    
    public void init(String profileDirectory) throws LangDetectException {
        DetectorFactory.loadProfile(profileDirectory);
    }

    public String detect(String text) throws LangDetectException {
        try {
            Detector detector = DetectorFactory.create();
            detector.append(text);
            return detector.detect();
        } catch (LangDetectException ex) {
            ex.printStackTrace();
            System.out.println("");
        }
        return null;
    }

    public ArrayList<Language> detectLangs(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }
    
    /**
     * queensland data
     *
     * @param filePath
     * @param eventName
     * @return
     */
    public Event readEventTSVFileData(String filePath, String eventName) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        //ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
            
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);

            BufferedReader fileRead = new BufferedReader(in);
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                try {                    
                    Instance inst = new Instance();
                    String [] record=str.split("\t");
                    String id = record[0];                    
                    String cls = record[1];
                    String text = record[2];
//                    if (cls.equalsIgnoreCase("neutral")) continue;
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    //if (text.trim().isEmpty()||text.length()<5) {
                    //    continue;
                    //}                     
                    //if(!this.detect(text).equalsIgnoreCase("en")||this.detect(text)==null) continue;
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOG.error("Please check your file and check the stack-trace.", ex, filePath);
                }
            }
            in.close();
            //event.setClassDist(classDist);
            event.setInstList(eventInstList);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + filePath);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return event;
    }    
    
    /**
     * 2013_Alberta_Floods-ontopic_en data
     *
     * @param filePath
     * @param eventName
     * @return
     */
    public Event readEventCSVFileData140(String filePath, String eventName) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
//            String str = "";
            String basename = new File(filePath).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");

            event.setName(eventName);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
//            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//            for (CSVRecord record : records) {
//                try {                    
            BufferedReader fileRead = new BufferedReader(in);
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                try {
                    Instance inst = new Instance();
                    String[] record = str.split(",");
                    //Instance inst = new Instance();
//                    String id = record.get(0);
//                    String text = record.get(record.size() - 1);
//                    String cls = record.get(1);
                    String id = record[0];                    
                    String cls = record[1];
                    String text = record[record.length - 1];
                    
                    if (cls.equalsIgnoreCase("0")) {
                        cls = "negative";
                    } else if (cls.equalsIgnoreCase("2")) {
                        cls = "neutral";
                    } else if (cls.equalsIgnoreCase("4")) {
                        cls = "positive";
                    }                   
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
//                    if (text.trim().isEmpty()||text.length()<5) {
//                        continue;
//                    }                     
//                    String lang = record.get(4);
//                    if(!lang.equalsIgnoreCase("en")) continue;
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOG.error("Please check your file and check the stack-trace.", ex, filePath);
                }
            }
            in.close();
            event.setInstList(eventInstList);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + filePath);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return event;
    }

    /**
     * 2013_Alberta_Floods-ontopic_en data
     *
     * @param filePath
     * @param eventName
     * @return
     */
    public Event readEventCSVFileDataOrginal(String filePath, String eventName) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
            String str = "";
            String basename = new File(filePath).getName();
            if (basename.contains(".")) {
                basename = basename.substring(0, basename.lastIndexOf('.'));
            }
            String[] arr = basename.split("_");

            event.setName(eventName);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    
                    Instance inst = new Instance();
                    String id = record.get(1);
                    id=id.replaceAll("\"", "").trim();
                    String text = record.get(record.size()-1);
                    String cls = record.get(0).replaceAll("\"", "").trim();
                    if(cls.equalsIgnoreCase("0")){
                        cls="negative";
                    }else if(cls.equalsIgnoreCase("2")){
                        cls="neutral";
                    }else if(cls.equalsIgnoreCase("4")){
                        cls="positive";
                    }
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
//                    if (text.trim().isEmpty()||text.length()<5) {
//                        continue;
//                    }                     
//                    String lang = record.get(4);
//                    if(!lang.equalsIgnoreCase("en")) continue;
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOG.error("Please check your file and check the stack-trace.", ex, filePath);
                }
            }
            in.close();
            event.setInstList(eventInstList);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + filePath);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return event;
    }
    
}
