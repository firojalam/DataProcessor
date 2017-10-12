package qa.org.qcri.dataprocessor;

import qa.org.qcri.dataprocessor.processors.Event;
import qa.org.qcri.dataprocessor.processors.Output;

/**
 *
 * @author Firoj Alam
 */
public class DataUnlabelled {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String fileName = "/Users/firojalam/QCRI/crisis_domain_adaptation/data/2015_Nepal_Earthquake_en.csv";
//        String fileout = "/Users/firojalam/QCRI/crisis_domain_adaptation/data/multi2binary/2015_Nepal_Earthquake_en.csv";
////        String enentName = "2013_Queensland_Floods_en";
//        String type = "multi2binary";
        
        String fileName = args[0].trim();
        String fileout = args[1].trim();
        String type = args[2].trim();
        
        Output output = new Output();
        Event event = null;
        UnlabelledDataReader reader = new UnlabelledDataReader();
        if (type.equalsIgnoreCase("2013_Queensland_Floods_en")) {
            event = reader.readEventCSVFileData(fileName, type);
        } else if(type.equalsIgnoreCase("2013_Alberta_Floods-ontopic_en")
                ||type.equalsIgnoreCase("2013_Boston_Bombings-ontopic_en")
                ||type.equalsIgnoreCase("2013_Oklahoma_Tornado-ontopic_en")                                
                ||type.equalsIgnoreCase("2012_Sandy_Hurricane-ontopic_en")){
            
           event = reader.readEventTSVFileData(fileName,type);

        }else if(type.equalsIgnoreCase("2014_India_Floods_en")
                ||type.equalsIgnoreCase("2014_Typhoon_Hagupit_en")
                ||type.equalsIgnoreCase("2015_Nepal_Earthquake_en")
                ||type.equalsIgnoreCase("2014_Mexico_Hurricane-Odile_en")
                ||type.equalsIgnoreCase("2014_Pakistan_Floods_en")
                ||type.equalsIgnoreCase("2015_Vanuatu_Cyclone_en_PAM")
                ||type.equalsIgnoreCase("2014_California_Earthquake_en")
                ){
           event = reader.readEventCSVFileDataIn(fileName,type);
        }else if(type.equalsIgnoreCase("2014_Chile_Earthquake_en")
                ||type.equalsIgnoreCase("2014_California_Earthquake_en")
                ){
           event = reader.readEventCSVFileDataCh(fileName,type);
        }else if(type.equalsIgnoreCase("2013_Pakistan_Earthquake_en")){
           event = reader.readEventJSONFileData(fileName,type);
        }else if(type.equalsIgnoreCase("multi2binary")){
            event = reader.readEventTSVFileMulti2Binary(fileName,type);
        }
        output.writeEvent(event, fileout);

        
        
    }

}
