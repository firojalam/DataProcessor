

package qa.org.qcri.sampling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import qa.org.qcri.dataprocessor.processors.Instance;

/**
 *
 * @author firojalam
 */
public class Reader {
    protected static final Logger logger = Logger.getLogger(Reader.class);
    private HashMap<String, Integer> classDist = new HashMap();
    private HashMap<String, ArrayList<Instance>> dataInstance = new HashMap();

    public HashMap<String, Integer> getClassDist() {
        return classDist;
    }

    public HashMap<String, ArrayList<Instance>> getDataInstance() {
        return dataInstance;
    }
    
    
    
    /**
     * Reads the file list from the file
     *
     * @param fileName name of the file
     */
    public void readEventFile(String fileName) {
        
        try {

            BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String[] arr = null;
            String str="";
            fileRead.readLine(); // considering the first character is header 
            Instance inst = null;
            ArrayList<Instance> instList = null;
            while ((str = fileRead.readLine()) != null) {
                try {
                    str = str.trim();
                    arr = str.split("\t");
                    String id = arr[0];
                    String event = arr[1];
                    String txt = arr[2];
                    String cls = arr[arr.length - 1];
                    inst = new Instance();
                    inst.setId(id);
                    inst.setText(txt);
                    inst.setEvent(event);
                    inst.setLabel(cls);
                    String tok[] = txt.split("\\s+");
                    inst.setNumOfWord(tok.length);
                    
                    if(cls.equalsIgnoreCase("terrorism_related_information")) continue;
                    
                    if (classDist.containsKey(cls)) {
                        int cnt = classDist.get(cls);
                        classDist.put(cls, cnt + 1);

                    } else {
                        classDist.put(cls, 1);
                    }

                    if (dataInstance.containsKey(cls)) {
                        dataInstance.get(cls).add(inst);
                    } else {
                        instList = new ArrayList<Instance>();
                        instList.add(inst);
                        dataInstance.put(cls, instList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }//end read file
            fileRead.close();
        } catch (FileNotFoundException ex) {
            logger.error("File is not available: " + fileName);
        } catch (Exception ex) {
            logger.error("Please check your file and check the stack-trace.", ex);
            ex.printStackTrace();
        }

    }//method read file list  
    
    
    
}
