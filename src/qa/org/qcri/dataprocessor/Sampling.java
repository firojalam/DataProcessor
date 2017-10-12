/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor;

import info.debatty.java.stringsimilarity.Cosine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import qa.org.qcri.suffolk.Tweet;

/**
 *
 * @author firojalam
 */
public class Sampling {
    
    public static ArrayList<Tweet> getData(ArrayList<String> tweetListNew,HashMap<String, Tweet> tweetDict){
        Random generator = new Random(10000);        
        ArrayList<Tweet> tweetBucket = new ArrayList<Tweet>();
        Cosine cosine = new Cosine(2);
        for (int i = 0; i < 1000;) {
            int id = generator.nextInt(tweetListNew.size());
            Tweet tweet = tweetDict.get(tweetListNew.get(id));
            tweetListNew.remove(id);
            boolean flag = true;
            for (int j = 0; j < tweetBucket.size(); j++) {
                Tweet twt = tweetBucket.get(j);
                Map<String, Integer> profile1 = cosine.getProfile(tweet.getMessage());
                Map<String, Integer> profile2 = cosine.getProfile(twt.getMessage());
                double sim = cosine.similarity(profile1, profile2);
                if(sim>0.70){
                    flag = false;
                    break;
                }else flag = true;
            }
            if(flag){
                tweetBucket.add(tweet);
                i=i+1;
            }
        }
        return tweetBucket;
    }
}
