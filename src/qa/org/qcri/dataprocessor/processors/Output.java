/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor.processors;

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
     *
     * @param event
     * @param fileOut
     */
    public void writeEvent(Event event, String fileOut) {
        Write2File wrt = new Write2File();
        wrt.openFile(fileOut);
        wrt.write2File("##id\ttext\tlabel\n");

        String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
        String dataInfo = source + "\t" + event.getEventSource() + "\t" + event.getYear() + "\t" + event.getRegion() + "\t" + event.getName() + "\t" + event.getLang();
        String str = "";
        ArrayList<Instance> instList = event.getInstList();
        instList = this.filterInst(instList);
        for (int i = 0; i < instList.size(); i++) {
            Instance inst = instList.get(i);
            String txt = inst.getText().trim();
            String[] arr = txt.split("\\s+");
            StringBuilder strTxt = new StringBuilder();

            for (int j = 0; j < arr.length; j++) {
                String token = arr[j];
                if (token.equalsIgnoreCase("rt") || token.equalsIgnoreCase("#UserID") || token.equalsIgnoreCase("#DIGIT") || token.equalsIgnoreCase("#URL")) {
                    continue;
                }
                strTxt.append(token).append(" ");
            }
//            txt = txt.replace("^rt\\s+", "");
//            txt = txt.replace("\\s+rt\\s+", "");
//            txt = txt.replace("#UserID", "");
//            txt = txt.replace("#DIGIT", "");
//            txt = txt.replace("#URL", "");
//            txt = txt.replace("#", "");
            txt = strTxt.toString().replace("#", "");
            wrt.write2File(inst.getId() + "\t" + txt.trim() + "\t" + inst.getLabel() + "\n");

        }
        wrt.closeFile();
    }

    /**
     *
     * @param eventList
     * @param fileOut
     */
    public void write(ArrayList<Event> eventList, String fileOut) {
        Write2File wrt = new Write2File();

        wrt.openFile(fileOut);
        wrt.write2File("##DataSource\tSource\tYear\tRegion\tCrisisType\tLang\tClass-freq\n");

        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
            String dataInfo = source + "\t" + event.getEventSource() + "\t" + event.getYear() + "\t" + event.getRegion() + "\t" + event.getName() + "\t" + event.getLang();
            String str = "";
            for (Map.Entry<String, Integer> entry1 : event.getClassDist().entrySet()) {
                String cls = entry1.getKey();
                Integer count = entry1.getValue();
                str = dataInfo + "\t" + cls + "\t" + count + "\n";
                System.out.println(cls + "\t" + count);
                wrt.write2File(str.trim() + "\n");
            }

        }
        wrt.closeFile();
    }

    /**
     * Writes class-wise distribution
     *
     * @param eventTable - class associated with their distribution in the event
     * file.
     * @param classSet - unique class list
     * @param fileOut - output name of the file.
     */
    public void writeEventTable(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet, String fileOut) {
        Write2File wrt = new Write2File();

        wrt.openFile(fileOut);
        String header = "##DataSource\tSource\tYear\tRegion\tCrisisType\tLang";

        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            header = header + "\t" + cls;
        }
        wrt.write2File(header + "\n");

        for (Map.Entry<Event, ArrayList<Integer>> entrySet : eventTable.entrySet()) {
            Event event = entrySet.getKey();
            ArrayList<Integer> freqTable = entrySet.getValue();
            String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
            String dataInfo = source + "\t" + event.getEventSource() + "\t" + event.getYear() + "\t" + event.getRegion() + "\t" + event.getName() + "\t" + event.getLang();
            String str = "";
            for (int i = 0; i < freqTable.size(); i++) {
                Integer count = freqTable.get(i);
                str = str + count + "\t";
            }
            wrt.write2File(dataInfo + "\t" + str.trim() + "\n");

        }

        wrt.closeFile();
    }

    /**
     * Writes the data of each class to the file named with the class-name
     *
     * @param eventTable
     * @param classSet
     * @param outputPath
     */
    public void writeDataClassWise(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet, String outputPath) {

        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            cls = cls.replaceAll("/", "_");
            cls = cls.replaceAll("\\s+", "_");
            Write2File wrt = new Write2File();
            wrt.openFile(outputPath + "/" + cls + ".tsv");
            String header = "##ID\tEvent\ttext\tLabel\n";
            wrt.write2File(header);
            for (Map.Entry<Event, ArrayList<Integer>> entry : eventTable.entrySet()) {
                Event event = entry.getKey();
                ArrayList<Instance> instList = event.getInstList();
                String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
                for (int i = 0; i < instList.size(); i++) {
                    Instance inst = instList.get(i);
                    if (inst.getLabel().equalsIgnoreCase(cls)) {
                        String line = inst.getId() + "\t" + source + "\t\"" + inst.getText() + "\"\t" + inst.getLabel() + "\n";
                        wrt.write2File(line.trim() + "\n");
                    }
                }
            }
            wrt.closeFile();
        }
    }

    /**
     * Writes all data in a single file for cluster analysis
     *
     * @param eventTable
     * @param classSet
     * @param fileoutCluster
     */
    public void writeDataCluster(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet, String fileoutCluster) {
        Write2File wrt = new Write2File();
        wrt.openFile(fileoutCluster);
        String header = "##ID\tEvent\ttext\tLabel\n";
        wrt.write2File(header);

        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            cls = cls.replaceAll("/", "_");
            cls = cls.replaceAll("\\s+", "_");

            for (Map.Entry<Event, ArrayList<Integer>> entry : eventTable.entrySet()) {
                Event event = entry.getKey();
                ArrayList<Instance> instList = event.getInstList();
                String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
                for (int i = 0; i < instList.size(); i++) {
                    Instance inst = instList.get(i);
                    if (inst.getLabel().equalsIgnoreCase(cls) && !inst.getText().isEmpty()) {
                        String line = inst.getId() + "\t" + source + "\t\"" + inst.getText() + "\"\t" + inst.getLabel() + "\n";
                        wrt.write2File(line.trim() + "\n");
                    }
                }
            }
        }
        wrt.closeFile();
    }

    /**
     *
     * @param eventTable
     * @param classSet
     * @param fileoutData
     * @param classGroup
     */
    public void writeAllData(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet, String fileoutData, HashMap<String, ClassGroup> classGroup) {
        Write2File wrt = new Write2File();
        wrt.openFile(fileoutData);
        String header = "##ID\tEvent\ttext\tLabel\n";
        wrt.write2File(header);

        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            cls = cls.replaceAll("/", "_");
            cls = cls.replaceAll("\\s+", "_");

            for (Map.Entry<Event, ArrayList<Integer>> entry : eventTable.entrySet()) {
                Event event = entry.getKey();
                if (!event.getLang().equalsIgnoreCase("en")) {
                    continue;
                }
                ArrayList<Instance> instList = event.getInstList();
                String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
                for (int i = 0; i < instList.size(); i++) {
                    Instance inst = instList.get(i);
                    String clsLab = "";
                    if (classGroup.containsKey(inst.getLabel())) {
                        clsLab = classGroup.get(inst.getLabel()).getGrpName();
                        if (inst.getLabel().equalsIgnoreCase(cls) && !inst.getText().isEmpty()) {
                            String line = inst.getId() + "\t" + source + "\t\"" + inst.getText() + "\"\t" + clsLab + "\n";
                            wrt.write2File(line.trim() + "\n");
                        }
                    } else {
                        System.err.println("Check the group and class name \t" + inst.getLabel());
                    }

                }
            }
        }
        wrt.closeFile();
    }

    /**
     *
     * @param classGroup
     * @param outfile
     * @param clessesToFilter
     */
    public void groupDescription(HashMap<String, ClassGroup> classGroup, String outfile, HashSet<String> clessesToFilter) {
        TreeMap<String, String> classDict = new TreeMap<String, String>();
        for (Map.Entry<String, ClassGroup> entry : classGroup.entrySet()) {
            String key = entry.getKey();
            if (clessesToFilter.contains(key)) {
                continue;
            }
            ClassGroup cls = entry.getValue();
            if (classDict.containsKey(cls.getGrpName())) {
                String desc = classDict.get(cls.getGrpName()) + " " + cls.getAnnotationDesc().trim();
                classDict.put(cls.getGrpName(), desc);
            } else {
                classDict.put(cls.getGrpName(), cls.getAnnotationDesc().trim());
            }
        }

        Write2File wrt = new Write2File();
        wrt.openFile(outfile);
        String header = "##class\tdescription\n";
        wrt.write2File(header);

        for (Map.Entry<String, String> entry : classDict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            wrt.write2File(key + "\t" + value + "\n");
        }
        wrt.closeFile();
    }

    private ArrayList<Instance> filterInst(ArrayList<Instance> instList) {
        ArrayList<Instance> instListNew=new ArrayList<Instance>();
        
        HashSet<String> textSet = new HashSet<String>();
        for (int i = 0; i < instList.size(); i++) {
            Instance inst = instList.get(i);
            String txt = inst.getText();
            if(textSet.contains(txt)){
                //do nothing
            }else{
                textSet.add(txt);
                instListNew.add(inst);
            }
        }
        return instListNew;
    }
}
