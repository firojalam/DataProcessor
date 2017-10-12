/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.suffolk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.cli.CommandLine;
import org.slf4j.LoggerFactory;
import qa.org.qcri.utils.ParseCommands;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Firoj Alam
 */
public class JSONPipeline {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(JSONPipeline.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Date d1 = new Date();
        ParseCommands parseCmds = new ParseCommands();
        CommandLine cmds = parseCmds.parseCommandsSuffolk(args);        
        String inputFileName = cmds.getOptionValue("i");
        String outputFileName = cmds.getOptionValue("o");        
//        String path = "/Users/firojalam/QCRI/suffolk_marathon/";
//        String inputFileName = path + "file_list/json_file_list.txt";
//        String outputFileName = path + "shuffolk_marathon_data.arff";

        /**
         * Reads the JSONs
         */
        ReadFileList readList = new ReadFileList();
        HashMap<String, String> fileDict = readList.readFileList(inputFileName);
        JSONParser parser = new JSONParser();
        ArrayList<Tweet> allTweetList = parser.processJSONs(fileDict);
        ProcessToArff out = new ProcessToArff();
        out.writeToArff(allTweetList, outputFileName);
        String name = new File(outputFileName).getName();
        String path1 = new File(outputFileName).getParent();
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf('.'));
        }
        String orgFileName = path1 + "/" + name + "_org.csv";
        System.out.println(orgFileName);
        out.writeOrgToArff(allTweetList, orgFileName);

        CSVSaver csvSave = new CSVSaver();
        csvSave.setFieldSeparator("\t");
        name = new File(outputFileName).getName();
        String path = new File(outputFileName).getParent();
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf('.'));
        }
        String outputFileNameCsv = path + "/" + name + ".csv";

        try {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(outputFileName);
            Instances data1 = source.getDataSet();
            csvSave.setFile(new File(outputFileNameCsv));
            csvSave.setInstances(data1);
            csvSave.writeBatch();
        } catch (IOException ex) {
            LOG.error("Error in IO", ex.getMessage());
        } catch (Exception ex) {
            LOG.error("Severe Error in converting to file format", ex.getMessage());
        }

//        if (cmds.hasOption("f")) {
//            String name = new File(outputFileName).getName();
//            String path = new File(outputFileName).getParent();
//            if (name.indexOf(".") > 0) {
//                name = name.substring(0, name.lastIndexOf("."));
//            }
//            String outputFileNameCsv = null;
//            
//            if (cmds.getOptionValue("f").equalsIgnoreCase("csv")) {
//                csvSave.setFieldSeparator(",");
//                outputFileNameCsv = path + "/" + name + ".csv";
//            } else if (cmds.getOptionValue("f").equalsIgnoreCase("tsv")) {
//                csvSave.setFieldSeparator("\t");
//                outputFileNameCsv = path + "/" + name + ".tsv";
//            }
//            try {
//                ConverterUtils.DataSource source = new ConverterUtils.DataSource(outputFileName);
//                Instances data1 = source.getDataSet();
//                csvSave.setFile(new File(outputFileNameCsv));
//                csvSave.setInstances(data1);
//                csvSave.writeBatch();
//            } catch (IOException ex) {
//                LOG.error("Error in IO", ex.getMessage());
//            } catch (Exception ex) {
//                LOG.error("Severe Error in converting to file format", ex.getMessage());
//            }
//        }        
    }

}
