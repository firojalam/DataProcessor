/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.suffolk;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import qa.org.qcri.dataprocessor.processors.TokenizeAndFilter;

/**
 *
 * @author Firoj Alam
 */
public class JSONParser {

    private static Logger LOG = Logger.getLogger(JSONParser.class.getName());

    private void parseAIDRObject(Content content, JsonReader reader) {

        try {

            //reader.beginObject();
            while (reader.hasNext()) {
                String attribute = "";
                try {
                    attribute = reader.nextName();
                    System.out.println(attribute);
                    if (attribute.equals("nominal_labels")) {
                        reader.beginArray();
                        reader.hasNext();
                        reader.beginObject();
                        String label = "";
                        while (reader.hasNext()) {
                            String aidrAttName = reader.nextName();
                            if (aidrAttName.equals("label_name")) {
                                if (reader.peek() != JsonToken.NULL) {
                                    label = reader.nextString();
                                } else {
                                    System.out.println("NULL");
                                    label = null;
                                    reader.skipValue();
                                }
                            } else if (aidrAttName.equals("from_human")) {
                                boolean manualLabel = reader.nextBoolean();
                                if (manualLabel) {
                                    content.setLabelManual(label);
                                } else {
                                    content.setLabelAuto(label);
                                }
                            } else {
                                reader.skipValue(); //avoid some unhandle events
                            }
                        }

                        reader.endObject();
                        reader.endArray();
                    } else {
                        reader.skipValue(); //avoid some unhandle events
                    }
                } catch (Exception ex) {
                    LOG.error("Error in parsing AIDR object." + attribute);
                    ex.printStackTrace();
                }

            }
            //reader.endObject();
        } catch (IOException ex) {
            LOG.error("Error in parsing label information", ex);
        }
    }

    private Tweet getTweet(String line) {
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        Tweet tweet = new Tweet();
        try {

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = (JsonObject) parser.parse(line);

            if (jsonObj.get("id") != null) {
                tweet.setTweetID(jsonObj.get("id").getAsString());
            }

            if (jsonObj.get("text") != null) {
                //String text = tokFilter.tokenizeText(jsonObj.get("text").getAsString());
                String text = jsonObj.get("text").getAsString();
                tweet.setMessage(text);
            }
            if (jsonObj.get("retweeted").getAsBoolean() == true) {
                return null;
            }

            JsonObject aidrObject = null;
            if (!jsonObj.get("aidr").isJsonNull()) {
                aidrObject = jsonObj.get("aidr").getAsJsonObject();
                String crisisName = "None";
                if (!aidrObject.get("crisis_name").isJsonNull()) {
                    crisisName = aidrObject.get("crisis_name").getAsString();
                }
                tweet.setCrisisName(crisisName);
                if (aidrObject.has("nominal_labels") && !aidrObject.get("nominal_labels").isJsonNull()) {
                    JsonArray nominalLabels = aidrObject.get("nominal_labels").getAsJsonArray();
                    for (int i = 0; i < nominalLabels.size(); i++) {
                        JsonObject label = nominalLabels.get(i).getAsJsonObject();
                        String lab = !label.get("label_name").isJsonNull() ? label.get("label_name").getAsString() : "null";
                        boolean humanLabel = !label.get("from_human").isJsonNull() ? label.get("from_human").getAsBoolean() : false;
                        if (lab != null && humanLabel) {
                            tweet.setLabelAuto(lab);
                        } else if (lab == null) {
                            tweet.setLabelAuto("?");
                        } else {
                            tweet.setLabelAuto(lab);
                        }
                    }
                }
            }
            JsonObject jsonUserObj = null;
            if (jsonObj.get("user") != null) {
                jsonUserObj = jsonObj.get("user").getAsJsonObject();
                if (jsonUserObj.get("id") != null) {
                    tweet.setUserID(jsonUserObj.get("id").getAsString());
                }
            }

        } catch (Exception ex) {
            LOG.error("Exception while parsing the json to tweet" + ex);
            return null;
        }
        return tweet;
    }

    public ArrayList<Tweet> readJSONFiles(String fName) {
        ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fName));
            String line;
            while ((line = br.readLine()) != null) {
                Tweet tweet = getTweet(line);
                if (tweet != null) {
                    tweetList.add(tweet);
                }
            }
        } catch (Exception ex) {

        }
        return tweetList;
    }

    public HashMap<String, Tweet> readJSONFilestoDict(String fName) {
        HashMap<String, Tweet> tweetList = new HashMap();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fName));
            String line;
            while ((line = br.readLine()) != null) {
                Tweet tweet = getTweet(line);
                if (tweet != null) {
                    tweetList.put(tweet.getTweetID(), tweet);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return tweetList;
    }

    /**
     *
     * @param fileDict
     * @return
     */
    public ArrayList<Tweet> processJSONs(HashMap<String, String> fileDict) {
        ArrayList<Tweet> allTweetList = new ArrayList<Tweet>();
        System.out.println("Total number of files: " + fileDict.size());
        for (Map.Entry<String, String> entry : fileDict.entrySet()) {
            //String baseName = entry.getKey();
            String fullPath = entry.getValue();
            ArrayList<Tweet> tweetList = readJSONFiles(fullPath);
            allTweetList.addAll(tweetList);
        }///end of hashMap

        return allTweetList;
    }

    /**
     *
     * @param fileDict
     * @return
     */
    public ArrayList<Tweet> processJSONs(ArrayList<String> fileDict) {
        ArrayList<Tweet> allTweetList = new ArrayList<Tweet>();
        System.out.println("Total number of files: " + fileDict.size());
        for (String fullPath : fileDict) {
            ArrayList<Tweet> tweetList = readJSONFiles(fullPath);
            allTweetList.addAll(tweetList);
        }///end of hashMap

        return allTweetList;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

    }

}
