
package qa.org.qcri.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import qa.org.qcri.dataprocessor.processors.Instance;

/**
 *
 * @author firojalam
 */
public class DataDistribution {
    protected static final Logger logger = Logger.getLogger(DataDistribution.class);
 

    public static void main(String[] args) {
        try {
//            String dirName = "/Applications/XAMPP/xamppfiles/htdocs/aidr_simulator/images/";

            CommandLineParser parser = new BasicParser();
            Options options = new Options();

//            options.addOption("m", "max", true, "Max number of instances to select the class labels");
//            options.addOption("a", "mean", true, "Average number of instances to randomly select.");
            options.addOption("n", "numbin", false, "Number of Bin.");
            options.addOption("i", "input", true, "Input file.");
            options.addOption("o", "output", true, "Output file.");
//            options.addOption("c", "classdist", true, "Class distribution.");
            
            
            CommandLine commandLine = null;
            
            try {
                commandLine = parser.parse(options, args);
            } catch (ParseException ex) {
                logger.error("Please provide command line options.");
                logger.error(ex.getMessage());
                System.exit(0);
            }
            
//            double max = Double.parseDouble(commandLine.getOptionValue('m', "4128.0"));
//            int avgNumInst = Integer.parseInt(commandLine.getOptionValue('a', "1139"));


            int numberOfBin = Integer.parseInt(commandLine.getOptionValue('a', "10"));
            String inputFile = commandLine.getOptionValue('i', "/Users/firojalam/QCRI/crisis_semi_supervised/data/all_events_data_filtered_train.csv");
            String outputFile = commandLine.getOptionValue('o', "/Users/firojalam/QCRI/crisis_semi_supervised/data/all_events_data_filtered_us_train.csv");
            //String classDistFi = commandLine.getOptionValue('o', "/Users/firojalam/QCRI/crisis_semi_supervised/data/all_events_data_filtered_us_train.csv");
            
            Reader reader = new Reader();
            reader.readEventFile(inputFile);
            HashMap<String, Integer> classDist = reader.getClassDist();
            HashMap<String, ArrayList<Instance>> dataInstance = reader.getDataInstance();
            

            Outlier outlier = new Outlier(classDist);
            double max = outlier.getMax();
            int avgNumInst = (int)outlier.getMean();
                        
            Sampling sampling = new Sampling();
            ArrayList<String> classList = sampling.selectcClassLabel(classDist, (int) max);
            HashMap<String, ArrayList<Instance>> data = sampling.generateData(dataInstance, classList, numberOfBin, avgNumInst);
            Output output = new Output();
            output.writeDataClassWise(data, outputFile);
            
        } catch (Exception ex) {
            logger.error("Error in parsing");
        }
    }    
}
