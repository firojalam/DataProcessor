/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.sampling;

import qa.org.qcri.dataprocessor.processors.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import qa.org.qcri.utils.Write2File;

/**
 *
 * @author firojalam
 */
public class Output {

    
    /**
     * Writes the data of each class to the file named with the class-name
     *
     * @param eventTable
     * @param outputPath
     */
    public void writeDataClassWise(HashMap<String, ArrayList<Instance>> eventTable, String outputPath) {

        Write2File wrt = new Write2File();
        wrt.openFile(outputPath);
        String header = "##ID\tEvent\ttext\tLabel\n";
        wrt.write2File(header);
        for (Map.Entry<String, ArrayList<Instance>> entry : eventTable.entrySet()) {
            String event = entry.getKey();
            ArrayList<Instance> instList = entry.getValue();
            for (int i = 0; i < instList.size(); i++) {
                Instance inst = instList.get(i);
                String line = inst.getId() + "\t" + inst.getEvent() + "\t" + inst.getText() + "\t" + inst.getLabel() + "\n";
                wrt.write2File(line.trim() + "\n");                
            }
        }
        wrt.closeFile();

    }
}
