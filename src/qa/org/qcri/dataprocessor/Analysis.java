package qa.org.qcri.dataprocessor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.org.qcri.utils.Write2File;
import info.debatty.java.stringsimilarity.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import qa.org.qcri.dataprocessor.processors.TokenizeAndFilter;

/**
 *
 * @author firojalam
 */
public class Analysis {

    protected static final Logger logger = LoggerFactory.getLogger(Analysis.class);
    private static HashSet<String> notNeededClass = new HashSet();

    public Analysis() {
        notNeededClass.add("not_related_or_irrelevant");
        notNeededClass.add("sympathy_and_support");
        notNeededClass.add("relevant_information");
        notNeededClass.add("personal");
    }

    
    
private static boolean duplicates2(HashMap<String, ArrayList<String>> data, String tweetText) {
        boolean bool = false;
        HashMap<String, Tweet> dataNew = new HashMap();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        try {
            tweetText = tokFilter.tokenizeText(tweetText);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (Map.Entry<String, ArrayList<String>> entry : data.entrySet()) {
            try{
            String text = entry.getKey();
            //Tweet tweet = entry.getValue();
            //String text = tweet.getMessage();
            text = tokFilter.tokenizeText(text);
            
            //https://github.com/tdebatty/java-string-similarity#shingle-n-gram-based-algorithms
             Cosine cosine = new Cosine(2);
            Map<String, Integer> profile1 = cosine.getProfile(text);
            Map<String, Integer> profile2 = cosine.getProfile(tweetText);

            // Prints 0.516185
            double sim = cosine.similarity(profile1, profile2);
            if(sim>=0.6) return true;

            //boolean bool = checkDuplicates(data,tweetText);
             if(bool) continue;
            //System.out.println();            
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return bool;
    }          
    
private static boolean duplicates(HashMap<String, Tweet> data, String tweetText) {
        boolean bool = false;
        HashMap<String, Tweet> dataNew = new HashMap();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        for (Map.Entry<String, Tweet> entry : data.entrySet()) {
            try{
            String id = entry.getKey();
            Tweet tweet = entry.getValue();
            String text = tweet.getMessage();
            text = tokFilter.tokenizeText(text);
            tweetText = tokFilter.tokenizeText(tweetText);
            //https://github.com/tdebatty/java-string-similarity#shingle-n-gram-based-algorithms
             Cosine cosine = new Cosine(2);
            Map<String, Integer> profile1 = cosine.getProfile(text);
            Map<String, Integer> profile2 = cosine.getProfile(tweetText);

            // Prints 0.516185
            double sim = cosine.similarity(profile1, profile2);
            if(sim>=0.6) return true;

            //boolean bool = checkDuplicates(data,tweetText);
             if(bool) continue;
            //System.out.println();            
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return bool;
    }        
    
    private static HashMap<String, Tweet> checkDuplicates(HashMap<String, Tweet> data) {
        boolean bool = false;
        HashMap<String, Tweet> dataNew = new HashMap();
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        for (Map.Entry<String, Tweet> entry : data.entrySet()) {
            try{
            String id = entry.getKey();
            Tweet tweet = entry.getValue();
            String text = tweet.getMessage();
            bool = duplicates(data,text);
             if(bool) continue;
             dataNew.put(id, tweet);
            //System.out.println();            
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return dataNew;
    }    
    /**
     *
     * @param inFileName
     */
    private static HashMap<String, Tweet> readData(String inFileName, double conf) {
        int cnt=0;
        HashMap<String, Tweet> data = new HashMap();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(inFileName)));
            String str = "";
            String id = "", damageClass = "";
            Tweet tweet = null;
            fileRead.readLine();
            while ((str = fileRead.readLine()) != null) {
                //if(cnt%1000==0) System.out.println(cnt);
                //cnt++;
                try {
                    str = str.trim();
                    if (str.isEmpty()) {
                        continue;
                    }
                    String arr[] = str.split("\t");
                    id = arr[0];
                    String date = arr[1];
                    String tweetText = arr[2];
                    
                    String classlab = arr[3];
                    Double classConf = Double.parseDouble(arr[4]);
                    if(Analysis.notNeededClass.contains(classlab.trim())){
                        
                        continue;
                    }
                    //if(classConf>=conf){
                        tweet = new Tweet();
                        tweet.setTweetID(id);
                        tweet.setCreatedAt(date);
                        tweet.setMessage(tweetText);
                        tweet.setLabelAuto(classlab);
                        tweet.setClassLabelConf(classConf);
                        data.put(id, tweet);
                    //}
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error in parsing a line, skipping.");
                }
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            logger.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            logger.error("Please check your file.");
        }
        return data;
    }

    private static HashMap<String, ArrayList<String>> filterTweet(HashMap<String, Tweet> data) {
        HashMap<String, ArrayList<String>> dataNew = new HashMap();
        ArrayList<String> tmp = null;
        int cnt=0;
        for (Map.Entry<String, Tweet> entry : data.entrySet()) {
            if(cnt%1000==0) System.out.println(cnt);
                cnt++;
            String id = entry.getKey();
            Tweet tweet = entry.getValue();
            String msg = tweet.getMessage().trim();
            if(dataNew.containsKey(msg)){
                //boolean bool = duplicates(data, msg);
                //if(!bool) 
                dataNew.get(msg).add(id);
            }else{
                tmp = new ArrayList<String>();
                //boolean bool = duplicates2(dataNew, msg);
                //if (!bool) {
                    tmp.add(id);
                    dataNew.put(msg, tmp);
                //}
            }
        }
        return dataNew;
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new Analysis();
        String inFileName = "/Users/firojalam/QCRI/crisis_2017_data_analysis/hurricane_maria_2017_prediction.txt";
        String outFileName = "/Users/firojalam/QCRI/crisis_2017_data_analysis/out/hurricane_maria_2017_analysis.csv";
        double conf = 0.4;
        HashMap<String, Tweet> data=readData(inFileName, conf);
        System.out.println("Original size: "+data.size());
        HashMap<String, ArrayList<String>> dataNew=filterTweet(data);
        //data=checkDuplicates(data);
        System.out.println("Filtered size: "+dataNew.size());
        TreeMap<String, Integer> classFreq = new TreeMap();
        TreeMap<String, TreeMap<Double,ArrayList<Tweet>>> dataInst = new TreeMap();
        TreeMap<Double,ArrayList<Tweet>> tweetMap = null;
        ArrayList<Tweet> tweetListNew = null;
        
        for (Map.Entry<String, ArrayList<String>> entry : dataNew.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> tweetList = entry.getValue();
            Random randomizer = new Random();
            String tweetID = tweetList.get(randomizer.nextInt(tweetList.size()));
            Tweet tweet = data.get(tweetID);
            String clsLab = tweet.getLabelAuto();
            Double clsLabConf = tweet.getClassLabelConf();
            if(classFreq.containsKey(clsLab)){
                int cnt = classFreq.get(clsLab)+1;
                classFreq.put(clsLab, cnt);
            }else{
                classFreq.put(clsLab, 1);
            }            
            
            if(dataInst.containsKey(clsLab)){
                tweetMap=dataInst.get(clsLab);
                if(tweetMap.containsKey(clsLabConf)){
                    tweetListNew = tweetMap.get(clsLabConf); 
                    tweetListNew.add(tweet);
                    tweetMap.put(clsLabConf, tweetListNew);  
                } else {
                    tweetListNew = new ArrayList();
                    tweetListNew.add(tweet);
                    tweetMap.put(clsLabConf, tweetListNew);                
                }
            }else{
                tweetMap = new TreeMap(Collections.reverseOrder());
                tweetListNew = new ArrayList();
                tweetListNew.add(tweet);
                tweetMap.put(clsLabConf, tweetListNew);
                dataInst.put(clsLab, tweetMap);
            }            
        }


        int numOfItemToSelect = 2000;//400/classFreq.size();
        System.out.println(numOfItemToSelect);
        
        for (Map.Entry<String, Integer> entry : classFreq.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key+"\t"+value);
        }
        HashMap<String, Tweet> filterTweet = new HashMap<String, Tweet>();
        for (Map.Entry<String, TreeMap<Double,ArrayList<Tweet>>> entry : dataInst.entrySet()) {
            String key = entry.getKey();
            TreeMap<Double,ArrayList<Tweet>> value = entry.getValue();
            int count=0;
            for (Map.Entry<Double, ArrayList<Tweet>> entry1 : value.entrySet()) {
                Double confValue = entry1.getKey();
                ArrayList<Tweet> sortedTweets = entry1.getValue();                
                for (Iterator<Tweet> iterator = sortedTweets.iterator(); iterator.hasNext();) {
                    Tweet twt = iterator.next();
                    if(count<numOfItemToSelect){
                        filterTweet.put(twt.getTweetID(), twt);
                    }
                    count++;
                }
            }
        }

        Write2File wrt = new Write2File();
        wrt.openFile(outFileName);
        HashMap<String, Tweet> filterTweetNew = new HashMap<String, Tweet>();
                
        for (Map.Entry<String, Tweet> entry : filterTweet.entrySet()) {
            String key = entry.getKey();
            Tweet twt = entry.getValue();
            String txt = twt.getMessage();
            boolean bool = Analysis.duplicates(filterTweetNew, txt);
            if(bool) continue;
            filterTweetNew.put(key, twt);
        }
        int cnt=0;
        for (Map.Entry<String, Tweet> entry : filterTweetNew.entrySet()) {
            String key = entry.getKey();
            Tweet twt = entry.getValue();
            wrt.write2File(twt.getTweetID()+"\t"+twt.getCreatedAt()+"\t"+twt.getMessage()+"\t"+twt.getLabelAuto()+"\t"+twt.getClassLabelConf()+"\n");                    
            if(cnt%100==0) System.out.println(cnt);
            cnt++;
        }
        //
        wrt.closeFile();
    }

   

    
}
