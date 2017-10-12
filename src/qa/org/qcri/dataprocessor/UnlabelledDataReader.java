/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.dataprocessor;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import java.io.BufferedReader;
import qa.org.qcri.dataprocessor.processors.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author firojalam
 */
public class UnlabelledDataReader {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(UnlabelledDataReader.class);

    public UnlabelledDataReader() {
        
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
     * 2013_Alberta_Floods-ontopic_en data
     *
     * @param filePath
     * @param eventName
     * @return
     */
    public Event readEventCSVFileDataIn(String filePath, String eventName) {
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

//            event.setYear(arr[0]);
//            event.setRegion(arr[1]);
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = "?";
                    Instance inst = new Instance();
                    String id = record.get(0);
                    String text = record.get(1);
                   
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()||text.length()<5) {
                        continue;
                    }                     
                    if(!this.detect(text).equalsIgnoreCase("en")||this.detect(text)==null) continue;
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
    public Event readEventCSVFileData(String filePath, String eventName) {
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

//            event.setYear(arr[0]);
//            event.setRegion(arr[1]);
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = "?";
                    Instance inst = new Instance();
                    String id = record.get(1);
                    String text = record.get(0);
                   
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()||text.length()<5) {
                        continue;
                    }                     
                    if(!this.detect(text).equalsIgnoreCase("en")||this.detect(text)==null) continue;
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
    public Event readEventCSVFileDataCh(String filePath, String eventName) {
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

//            event.setYear(arr[0]);
//            event.setRegion(arr[1]);
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                try {
                    String cls = "?";
                    Instance inst = new Instance();
                    String id = record.get(0);
                    String text = record.get(record.size()-1);
                   
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()||text.length()<5) {
                        continue;
                    }                     
                    String lang = record.get(4);
                    if(!lang.equalsIgnoreCase("en")) continue;
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
     * 
     * @param msgJson
     * @return 
     */
    private String checkMedia(JSONObject msgJson) {
        //String url = null;
        String imageUrl = null;
//        String expandedImageUrl = "";
        try {
            JSONObject entities = msgJson.getJSONObject("entities");
            if (entities != null && entities.has("media") && entities.getJSONArray("media") != null
                    && entities.getJSONArray("media").length() > 0
                    && entities.getJSONArray("media").getJSONObject(0).getString("type") != null
                    && entities.getJSONArray("media").getJSONObject(0).getString("type").equals("photo")) {
                imageUrl = entities.getJSONArray("media").getJSONObject(0).getString("media_url");
                //expandedImageUrl = entities.getJSONArray("media").getJSONObject(0).getString("expanded_url");
                if (imageUrl == null) {
                    return null;
                }
            }
        } catch (JSONException ex) {
            LOG.info("Error in parsing json file.");
        }
        return imageUrl;
    }

    /**
     * 2013_Alberta_Floods-ontopic_en data
     *
     * @param filePath
     * @param eventName
     * @return
     */
    public Event readEventJSONFileData(String filePath, String eventName) {
        
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        //ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        int geoCount =0;
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
//            event.setYear(arr[0]);
//            event.setRegion(arr[1]);
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);

            BufferedReader fileRead = new BufferedReader(in);
            String str = "";
            while ((str = fileRead.readLine()) != null) {
                try {
                    
                    JSONObject msgJson = new JSONObject(str);
                    Instance inst = new Instance();                   
                    //JSONObject aidrJson = msgJson.getJSONObject("aidr");
                    //String docType = aidrJson.getString("doctype");
                    String id = msgJson.get("id").toString();
                    String text = msgJson.get("text").toString();
                    String lang = msgJson.get("lang").toString(); 
                    String createdAt = msgJson.get("created_at").toString(); 
                    inst.setDateOfCreation(createdAt);
                    if (!msgJson.isNull("geo")) {
                        JSONObject obj = (JSONObject) msgJson.get("geo");
                        if (!obj.isNull("coordinates")) {
                            JSONArray arr = (JSONArray) obj.get("coordinates");                            
                            Double latitude = Double.parseDouble(arr.get(0).toString());
                            Double longitude = Double.parseDouble(arr.get(1).toString());
                            inst.setLongitude(longitude);
                            inst.setLatitude(latitude);
                            geoCount++;
                        }
                    }                
                    String imageUrl = checkMedia(msgJson);
                    inst.setImageURL(imageUrl);
                    String cls = "?";                    
                    inst.setLabel(cls);
                    inst.setId(id);
                    String txtNew=text.replaceAll("[\\t\\n\\r]+"," ");
                    inst.setTextOriginal(txtNew.trim());
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()) {
                        //LOG.info("Tweet removed: "+txtNew);
                        continue;
                    }
                    //String lang = record.get(4);
                    if(!lang.equalsIgnoreCase("en")) continue;
                    inst.setText(text);
                    eventInstList.add(inst);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOG.error("Please check your file and check the stack-trace.", ex, filePath);
                }
            }
            in.close();
            //event.setClassDist(classDist);
            LOG.info("Number of tweets: "+eventInstList.size());
            event.setInstList(eventInstList);
            LOG.info("Number of tweets with geolocation: "+geoCount);
        } catch (FileNotFoundException ex) {
            LOG.error("File is not available: " + filePath);
        } catch (Exception ex) {
            LOG.error("Please check your file and check the stack-trace.", ex);
        }
        return event;
    }
    
    /**
     * queensland data
     *
     * @param filePath
     * @return
     */
    public Event readEventTSVFileData(String filePath, String eventName) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
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
                    String cls = "?";
                    Instance inst = new Instance();
                    String [] record=str.split("\t");
                    String id = record[0];
                    String text = record[1];
                   
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()||text.length()<5) {
                        continue;
                    }                     
                    if(!this.detect(text).equalsIgnoreCase("en")||this.detect(text)==null) continue;
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
     * queensland data
     *
     * @param filePath
     * @return
     */
    public Event readEventTSVFileMulti2Binary(String filePath, String eventName) {
        ArrayList<Instance> eventInstList = new ArrayList<Instance>();
        ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
        Event event = new Event();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
            
            event.setName(eventName);
            //event.setLang(arr[arr.length - 1]);
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "utf-8");
            //FileReader in = new FileReader(filePath);

            BufferedReader fileRead = new BufferedReader(in);
            String str = "";
            fileRead.readLine();
            while ((str = fileRead.readLine()) != null) {
                try {

                    Instance inst = new Instance();
                    String [] record=str.split("\t");
                    String id = record[0];
                    String text = record[1];                    
                    String cls = record[2].trim();
                    if(cls.equalsIgnoreCase("not_related_or_irrelevant")||cls.equalsIgnoreCase("not_relevant")){
                        cls="No";
                    } else{
                        cls="Yes";
                    }
                    inst.setLabel(cls);
                    inst.setId(id);
                    text = tokFilter.tokenizeText(text);
                    if (text.trim().isEmpty()||text.length()<5) {
                        continue;
                    }                     
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
    
    
    public static void main(String args[]){
        String baseImageURL="http://scdev5.qcri.org/crisis-nlp/harvey_2017/";
        try {
            String json ="{\"filter_level\":\"low\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":564412199637372928,\"in_reply_to_user_id_str\":null,\"timestamp_ms\":\"1423401329322\",\"in_reply_to_status_id\":null,\"created_at\":\"Sun Feb 08 13:15:29 +0000 2015\",\"favorite_count\":0,\"place\":{\"id\":\"6d73a696edc5306f\",\"bounding_box\":{\"coordinates\":[[[46.4925433,40.9363327],[46.4925433,55.4425719],[87.3152134,55.4425719],[87.3152134,40.9363327]]],\"type\":\"Polygon\"},\"place_type\":\"country\",\"name\":\"Kazakhstan\",\"attributes\":{},\"country_code\":\"KZ\",\"url\":\"https://api.twitter.com/1.1/geo/id/6d73a696edc5306f.json\",\"country\":\"Kazakhstan\",\"full_name\":\"Kazakhstan\"},\"coordinates\":{\"coordinates\":[65.517096,44.855795],\"type\":\"Point\"},\"contributors\":null,\"text\":\"150208 #WuYiFan and #Luhan @ CCTV Spring Festival Gala Rehearsal || #Krishanday @ Play the Video \\u1F3A5 http://t.co/UbWn4cbv23\",\"geo\":{\"coordinates\":[44.855795,65.517096],\"type\":\"Point\"},\"entities\":{\"trends\":[],\"hashtags\":[{\"text\":\"WuYiFan\",\"indices\":[7,15]},{\"text\":\"Luhan\",\"indices\":[20,26]},{\"text\":\"Krishanday\",\"indices\":[68,79]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://instagram.com/p/y120TxyYq5/\",\"indices\":[99,121],\"display_url\":\"instagram.com/p/y120TxyYq5/\",\"url\":\"http://t.co/UbWn4cbv23\"}],\"user_mentions\":[]},\"aidr\":{\"features\":[{\"words\":[\"rehearsal\",\"cctv_spring\",\"cctv\",\"#krishanday\",\"150208_#wuyifan\",\"spring\",\"150208\",\"the_video\",\"#wuyifan_and\",\"gala\",\"festival_gala\",\"festival\",\"play_the\",\"video\",\"#wuyifan\",\"#krishanday_play\",\"play\",\"rehearsal_#krishanday\",\"spring_festival\",\"the\",\"and\",\"#luhan\",\"\\u1F3A5\",\"and_#luhan\",\"gala_rehearsal\",\"video_\\u1F3A5\",\"#luhan_cctv\"],\"type\":\"wordvector\"}],\"crisis_code\":\"128390298CEA-0000001\",\"nominal_labels\":[{\"label_name\":\"Not physical landslide\",\"source_id\":527,\"from_human\":false,\"attribute_description\":\"Indicate if the item is related to a physical landslide\",\"label_code\":\"03_not_landslide\",\"confidence\":1,\"label_description\":\"The item does not refer to a physical landslide\",\"attribute_code\":\"landslide\",\"attribute_name\":\"Landslide\"}],\"doctype\":\"twitter\",\"crisis_name\":\"EMSC Earthquake-Triggered Geo\"},\"source\":\"<a href=\\\"http://instagram.com\\\" rel=\\\"nofollow\\\">Instagram<\\/a>\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"564412199637372928\",\"user\":{\"location\":\"Asia. Kazakhstan\",\"default_profile\":true,\"profile_background_tile\":false,\"statuses_count\":1214,\"lang\":\"ru\",\"profile_link_color\":\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/857830153/1411194795\",\"id\":857830153,\"following\":null,\"protected\":false,\"favourites_count\":422,\"profile_text_color\":\"333333\",\"contributors_enabled\":false,\"description\":\"SNSD & EXO || my instagram: snsd.exo.official \\u2764\\uFE0F\",\"verified\":false,\"name\":\"EXOSHIDAE\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Tue Oct 02 10:24:40 +0000 2012\",\"default_profile_image\":false,\"followers_count\":179,\"geo_enabled\":true,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/552360218114269185/7ej7NfZ1_normal.jpeg\",\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":\"https://instagram.com/snsd.exo.official\",\"utc_offset\":21600,\"time_zone\":\"Almaty\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":36,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"snsdexoofficial\",\"id_str\":\"857830153\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/552360218114269185/7ej7NfZ1_normal.jpeg\",\"listed_count\":0,\"is_translator\":false}}";
            JSONObject msgJson = new JSONObject(json);
            
                    String id = msgJson.get("id").toString();
                    String text = msgJson.get("text").toString();
                    String lang = msgJson.get("lang").toString();
                    JSONObject obj =  (JSONObject) msgJson.get("geo");
                    JSONArray arr = (JSONArray) obj.get("coordinates");
                   System.out.println(arr.get(0));
                   System.out.println("");
        } catch (JSONException ex) {
            Logger.getLogger(UnlabelledDataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
