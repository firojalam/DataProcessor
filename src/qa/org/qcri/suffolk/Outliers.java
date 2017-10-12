/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.suffolk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.cli.CommandLine;
import org.slf4j.LoggerFactory;
import qa.org.qcri.utils.ParseCommands;
import qa.org.qcri.utils.Write2File;

/**
 *
 * @author firojalam
 */
public class Outliers {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Outliers.class);

    /**
     * Reads the outliers index from the file, which is obtained from clustering
     * algorithm
     *
     * @param fileName name of the file
     * @return
     */
    public ArrayList<String> readOutlier(String fileName) {
        ArrayList<String> outLierList = new ArrayList<String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            fileRead.readLine();
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                outLierList.add(str);
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return outLierList;
    }

    /**
     * Reads the outliers index from the file, which is obtained from clustering
     * algorithm
     *
     * @param fileName name of the file
     * @return
     */
    public HashMap<String, String> readTweets(String fileName) {
        HashMap<String, String> tweetDict = new HashMap<String, String>();
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String str = "";
            fileRead.readLine();
            int index = 0;
            while ((str = fileRead.readLine()) != null) {
                str = str.trim();
                tweetDict.put(index + "", str);
                index++;
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            LOG.error("Please check your file. Check that whether it is exist or not.");
        } catch (Exception ex) {
            LOG.error("Please check your file.");
        }
        return tweetDict;
    }

    public void writeOutlierTweet(ArrayList<String> outLierList, HashMap<String, String> tweetDict, String outputFileName) {
        Write2File wrt = new Write2File();
        wrt.openFile(outputFileName);
        for (int i = 0; i < outLierList.size(); i++) {
            String outlierID = outLierList.get(i);
            String txt = tweetDict.get(outlierID);
            wrt.write2File(outlierID+"\t"+txt+"\n");
        }
        wrt.closeFile();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String inputFileName = args[0];
//        String outlierFile = args[1];
//        String outputFileName = args[2];
        ParseCommands parseCmds = new ParseCommands();
        CommandLine cmds = parseCmds.parseCommandsSuffolkOutliers(args);
        String inputFileName = cmds.getOptionValue("i");
        String outlierFile = cmds.getOptionValue("s");
        String outputFileName = cmds.getOptionValue("o");
//        String inputFileName = args[0];
//        String outlierFile = args[1];
//        String outputFileName = args[2];

//        String inputFileName = "shuffolk_marathon_data.csv";
//        String outlierFile = "/Users/firojalam/QCRI/suffolk_marathon/outlier/shuffolk_marathon_tfidf_sim.outler.txt";
//        String outputFileName = "/Users/firojalam/QCRI/suffolk_marathon/outlier/shuffolk_marathon_data_outliers.csv";
        Outliers outliers = new Outliers();

        ArrayList<String> outLierList = outliers.readOutlier(outlierFile);
        HashMap<String, String> tweetDict = outliers.readTweets(inputFileName);
        outliers.writeOutlierTweet(outLierList, tweetDict, outputFileName);

    }
}
