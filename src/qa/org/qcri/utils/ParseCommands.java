/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.utils;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Firoj Alam
 */
public class ParseCommands {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ParseCommands.class);
    /**
     * 
     * @param file a file to check its existence
     */
    public void checkFileExistance(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                LOG.info(f + " file does not exist. Please check the file.");
                System.exit(0);
            }
        } catch (Exception ex) {
            LOG.error("wav file reading problem.");
            System.exit(0);
        }
    }
    /**
     * Parse command line arguments for the <em>DataProcessor<em\> class
     * @param args command line arguments
     * @return dictionary containing the options
     */
    public CommandLine parseCommands(String[] args) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        
        // create the parser
        CommandLineParser parser = new GnuParser();
        // create the Options
        Option inputFile = OptionBuilder.withArgName("input-file")
                .hasArg()
                .withDescription("input file should contain three columns: 1. inst id, 2. text, 3. class.")
                .create("i");
        Option config = OptionBuilder.withArgName("config-file")
                .hasArg()
                .withDescription("please use the configuration file.")
                .create("c");
        Option outFile = OptionBuilder.withArgName("output-file")
                .hasArg()
                .withDescription("please use name of the output file.")
                .create("o");        
        Option outFileFormat = OptionBuilder.withArgName("file format[arff|csv|tsv]")
                .hasArg()
                .withDescription("option can be chosen for a particular file format, default is arff")
                .create("f");
        Option classType = OptionBuilder.withArgName("class-type")
                .hasArg()
                .withDescription("class type can be categorical or numeric[cat|numeric]")
                .create("C");
        
        Options options = new Options();
        options.addOption(inputFile);
        options.addOption(config);
        options.addOption(outFile);
        options.addOption(outFileFormat);
        options.addOption(classType);
        String usageString ="";
        CommandLine cmds = null;
        try {
            // parse the command line arguments
            cmds = parser.parse(options, args);
            usageString = "java -classpath <DataProcessor.jar> qa.org.qcri.DataProcessor.DataProcessor -i input.file -c config.txt -o output.arff";            
            if(cmds.hasOption("i") && cmds.hasOption("c") && cmds.hasOption("o") && cmds.hasOption("C")){
                this.checkFileExistance(cmds.getOptionValue("i"));                
                this.checkFileExistance(cmds.getOptionValue("c"));
            }else{
                formatter.printHelp(usageString, options);
                System.exit(0);            
            }
        } catch (ParseException exp) {
            // Something went wrong
            formatter.printHelp(usageString, options);
            LOG.error("Please check the options. " + exp.getMessage());
            System.exit(0);
        }
        return cmds;
    }                  

    /**
     * Parse command line arguments for the <em>DataProcessor<em\> class
     * @param args command line arguments
     * @return dictionary containing the options
     */
    public CommandLine parseCommandsSuffolk(String[] args) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        
        // create the parser
        CommandLineParser parser = new GnuParser();
        // create the Options
        Option inputFile = OptionBuilder.withArgName("input-file")
                .hasArg()
                .withDescription("input file should contain three columns: 1. inst id, 2. text, 3. class.")
                .create("i");
        Option outFile = OptionBuilder.withArgName("output-file")
                .hasArg()
                .withDescription("please use name of the output file.")
                .create("o");        
        Option outFileFormat = OptionBuilder.withArgName("file format[arff|csv|tsv]")
                .hasArg()
                .withDescription("option can be chosen for a particular file format, default is arff")
                .create("f");
        
        Options options = new Options();
        options.addOption(inputFile);
        options.addOption(outFile);
        options.addOption(outFileFormat);
        String usageString ="";
        CommandLine cmds = null;
        try {
            // parse the command line arguments
            cmds = parser.parse(options, args);
            usageString = "java -classpath <DataProcessor.jar> qa.org.qcri.DataProcessor.DataProcessor -i input.file -o output.arff -f tsv";
            if(cmds.hasOption("i") && cmds.hasOption("o") && cmds.hasOption("f")){
                this.checkFileExistance(cmds.getOptionValue("i"));                                
            }else{
                formatter.printHelp(usageString, options);
                System.exit(0);            
            }
        } catch (ParseException exp) {
            // Something went wrong
            formatter.printHelp(usageString, options);
            LOG.error("Please check the options. " + exp.getMessage());
            System.exit(0);
        }
        return cmds;
    }                  

    /**
     * Parse command line arguments for the <em>DataProcessor<em\> class
     * @param args command line arguments
     * @return dictionary containing the options
     */
    public CommandLine parseCommandsSuffolkOutliers(String[] args) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        
        // create the parser
        CommandLineParser parser = new GnuParser();
        // create the Options
        Option inputFile = OptionBuilder.withArgName("input-file")
                .hasArg()
                .withDescription("input file should contain three columns: 1. inst id, 2. text, 3. class.")
                .create("i");
        Option outFile = OptionBuilder.withArgName("output-file")
                .hasArg()
                .withDescription("please use name of the output file.")
                .create("o");        
        Option outFileFormat = OptionBuilder.withArgName("file for outlier list")
                .hasArg()
                .withDescription("outlier list")
                .create("s");
        
        Options options = new Options();
        options.addOption(inputFile);
        options.addOption(outFile);
        options.addOption(outFileFormat);
        String usageString ="";
        CommandLine cmds = null;
        try {
            // parse the command line arguments
            cmds = parser.parse(options, args);
            usageString = "java -classpath <DataProcessor.jar> qa.org.qcri.DataProcessor.suffolk.OutliersSimilarity -i input.file -o output.txt -s outlier.txt";
            if(cmds.hasOption("i") && cmds.hasOption("o") && cmds.hasOption("s")){
                this.checkFileExistance(cmds.getOptionValue("i"));                                
            }else{
                formatter.printHelp(usageString, options);
                System.exit(0);            
            }
        } catch (ParseException exp) {
            // Something went wrong
            formatter.printHelp(usageString, options);
            LOG.error("Please check the options. " + exp.getMessage());
            System.exit(0);
        }
        return cmds;
    }                  

    /**
     * Parse command line arguments for the <em>DataProcessor<em\> class
     * @param args command line arguments
     * @return dictionary containing the options
     */
    public CommandLine parseCommand(String[] args) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        
        // create the parser
        CommandLineParser parser = new GnuParser();
        // create the Options
        Option inputFile = OptionBuilder.withArgName("--input-file")
                .hasArg()
                .withDescription("please use a name of directory.")
                .create("i");
        Option outFile = OptionBuilder.withArgName("--output-file")
                .hasArg()
                .withDescription("please use name of the output file.")
                .create("o");        
        Option outFileFormat = OptionBuilder.withArgName("--event-name")
                .hasArg()
                .withDescription("file containing list of event name")
                .create("e");
        
        Options options = new Options();
        options.addOption(inputFile);
        options.addOption(outFile);
        options.addOption(outFileFormat);
        String usageString ="";
        CommandLine cmds = null;
        try {
            // parse the command line arguments
            cmds = parser.parse(options, args);
            usageString = "java -classpath <DataProcessor.jar> qa.org.qcri.dataprocessor.AIDRdata -i inputDir -o output.tsv -e events.txt";
            if(cmds.hasOption("i") && cmds.hasOption("o")&& cmds.hasOption("e")){
                this.checkFileExistance(cmds.getOptionValue("i"));
                this.checkFileExistance(cmds.getOptionValue("e"));
            }else{
                formatter.printHelp(usageString, options);
                System.exit(0);            
            }
        } catch (ParseException exp) {
            // Something went wrong
            formatter.printHelp(usageString, options);
            LOG.error("Please check the options. " + exp.getMessage());
            System.exit(0);
        }
        return cmds;
    }                  
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        ParseCommands obj = new ParseCommands();
        
    }
}
