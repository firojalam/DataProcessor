/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
public class AIDRdata {
    
    private static Logger LOG = Logger.getLogger(AIDRdata.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Date d1 = new Date();
        ParseCommands parseCmds = new ParseCommands();
        CommandLine cmds = parseCmds.parseCommand(args);        
        String inputFileName = cmds.getOptionValue("i");
        String outputFileName = cmds.getOptionValue("o");
        String eventFileName = cmds.getOptionValue("e");        
        
//        String path1 = "/Users/firojalam/QCRI/JavaProjects/DataProcessor/";
//        String eventFileName = path1 + "/event_files.txt";
//        String outputFileName = "shuffolk_marathon_data.tsv";
//        String inputFileName ="/Users/firojalam/QCRI/suffolk_marathon/data/tweet";

        /**
         * Reads the JSONs
         */
        ReadFileList readList = new ReadFileList();
        ArrayList<String> eventList = readList.readEventList(eventFileName);
        ArrayList<String> fileList = new ArrayList<String>();
        readList.listFiles(inputFileName,fileList);
        fileList=readList.filterEvent(fileList);
        ArrayList<String> fileDict = readList.filter(eventList,fileList);
        
        JSONParser parser = new JSONParser();        
        ProcessToArff out = new ProcessToArff();
        
        String basename = new File(outputFileName).getName();
        String path = new File(new File(outputFileName).getAbsolutePath()).getParent();
        	
        if (basename != null && basename.contains(".")) {
            basename = basename.substring(0, basename.lastIndexOf('.'));
        }        
        int vol = 107;
        String outFile = path+"/"+basename+"_vol_"+vol+".tsv";  
        System.out.println(outFile);
        Write2File wrt = new Write2File();        
        wrt.openFile(outFile);     
        wrt.write2File("##ID\tText\tEvent\n");
        LOG.info("Number of json file: "+fileDict.size());
        int line = 0;
        long million = 1000000;
        //long million = 5000;
        for (String fullPath : fileDict) {
            LOG.info(fullPath);
            ArrayList<Tweet> tweetList = parser.readJSONFiles(fullPath);
            if(tweetList.isEmpty()) continue;
            line = line +tweetList.size();
            if(line >= million) {
                wrt.closeFile();
                vol++;
                line = 0;
                outFile = path+"/"+basename+"_vol_"+vol+".tsv";                  
                System.out.println(outFile);
                wrt = new Write2File();
                wrt.openFile(outFile);
                wrt.write2File("##ID\tText\tEvent\n");
            }
            out.writeToTSV(tweetList, wrt);  
        }///end of hashMap               
        wrt.closeFile();
        
    }    
}
