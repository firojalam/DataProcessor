/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import qa.org.qcri.suffolk.JSONParser;
import qa.org.qcri.suffolk.ProcessToArff;
import qa.org.qcri.suffolk.ReadFileList;
import qa.org.qcri.suffolk.Tweet;
import qa.org.qcri.utils.ParseCommands;
import qa.org.qcri.utils.Write2File;

/**
 *
 * @author firojalam
 */
public class AIDRdataPrepEvent {

    private static Logger LOG = Logger.getLogger(AIDRdataPrepEvent.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Date d1 = new Date();
//        ParseCommands parseCmds = new ParseCommands();
//        CommandLine cmds = parseCmds.parseCommand(args);        
//        String inputFileName = cmds.getOptionValue("i");
//        String outputFileName = cmds.getOptionValue("o");
        //String eventFileName = cmds.getOptionValue("e");        

//        String path1 = "/Users/firojalam/QCRI/JavaProjects/DataProcessor/";
//        String eventFileName = path1 + "/event_files.txt";
        String outputFileName = "/Users/firojalam/QCRI/PEIC/batch10/170404094915_attacks_on_education_batch10.tsv";
        String inputFileName = "/Users/firojalam/QCRI/PEIC/file_list.txt";
        String prevData = "/Users/firojalam/QCRI/PEIC/PEIC/cumulative_data/170404094915_attacks_on_education_day1_day2_day2_2_batch4_5_6_7_8_9.tsv";

        /**
         * Reads the JSONs
         */
        ReadFileList readList = new ReadFileList();
        ArrayList<String> fileDict = readList.readEventList(inputFileName);
        ArrayList<String> prevTweets = readList.readPrevTweet(prevData);
        //ArrayList<String> fileList = new ArrayList<String>();
        //readList.listFiles(inputFileName,fileList);
        //fileList=readList.filterEvent(fileList);
        //ArrayList<String> fileDict = readList.filter(eventList,fileList);

        JSONParser parser = new JSONParser();
        ProcessToArff out = new ProcessToArff();

        String basename = new File(outputFileName).getName();
        String path = new File(new File(outputFileName).getAbsolutePath()).getParent();

        if (basename != null && basename.contains(".")) {
            basename = basename.substring(0, basename.lastIndexOf('.'));
        }
        int vol = 107;
        //String outFile = path+"/"+basename+"_vol_"+vol+".tsv";  
        String outFile = path + "/" + basename + ".tsv";
        System.out.println(outFile);
        Write2File wrt = new Write2File();
        wrt.openFile(outFile);
        wrt.write2File("##ID\tText\n");
        LOG.info("Number of json file: " + fileDict.size());
        int line = 0;
        long million = 1000000;
        ArrayList<Tweet> tweetList = new ArrayList();
        for (String fullPath : fileDict) {
            LOG.info(fullPath);
            ArrayList<Tweet> tweetListtmp = parser.readJSONFiles(fullPath);
            tweetList.addAll(tweetListtmp);
        }///end of hashMap               
        ArrayList<Tweet> tweetListN = out.filterInst(tweetList);

        ArrayList<String> tweetListNew = new ArrayList();
        HashMap<String, Tweet> tweetDict = new HashMap();
        for (Iterator<Tweet> iterator = tweetListN.iterator(); iterator.hasNext();) {
            Tweet tweet = iterator.next();            
            if(!prevTweets.contains(tweet.getTweetID())){
                tweetDict.put(tweet.getTweetID(), tweet);
                tweetListNew.add(tweet.getTweetID());
            }
            
        }
        tweetListN = Sampling.getData(tweetListNew, tweetDict);
        out.writeToTSV(tweetListN, wrt);

        wrt.closeFile();

    }
}
