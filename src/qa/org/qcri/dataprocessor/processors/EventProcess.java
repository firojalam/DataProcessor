/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.org.qcri.dataprocessor.processors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author firojalam
 */
public class EventProcess {

    /**
     * Removes the class with frequency one
     * @param eventList 
     */
    public ArrayList<Event> removeUnwanted(ArrayList<Event> eventList) {
        ArrayList<Event> eventListNew = new ArrayList<Event>();
        for (Event event : eventList) {
            
            for (Map.Entry<String, Integer> entrySet : event.getClassDist().entrySet()) {
                String cls = entrySet.getKey();
                Integer freq = entrySet.getValue();
                if(freq==1) event.getClassDist().remove(cls);
                eventListNew.add(event);
            }
        }
        return eventListNew;
    }
    
    /**
     * Designs a unique class set in alphabatic order using TreeSet. 
     * @param eventList
     * @return unique class set
     */
    public TreeSet<String> getClassList(ArrayList<Event> eventList){
        TreeSet<String> classSet = new TreeSet<String>();
        for (Event event : eventList) {
            for (Map.Entry<String, Integer> entrySet : event.getClassDist().entrySet()) {
                String cls = entrySet.getKey();
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 
     * @param eventList
     * @param clsSet
     * @return 
     */
    public HashMap<Event,ArrayList<Integer>> designEventClassDistTable(ArrayList<Event> eventList, TreeSet<String> clsSet) {
        HashMap<Event,ArrayList<Integer>> eventTable = new HashMap<Event,ArrayList<Integer>>();
        ArrayList<String> classSet = new ArrayList<String>(clsSet);
        
        
        for (Event event : eventList) {
            ArrayList<Integer> freqlist=new ArrayList<Integer>(Collections.nCopies(classSet.size(), 0));//new ArrayList<>(classSet.size());            
            for (Map.Entry<String, Integer> entrySet : event.getClassDist().entrySet()) {
                String cls = entrySet.getKey();
                Integer freq = entrySet.getValue();
                for (int i = 0; i < classSet.size(); i++) {
                    String clsStr = classSet.get(i);
                    if(cls.equalsIgnoreCase(clsStr)){
                        freqlist.set(i, freq);
                        continue;
                    }
                }
            }
            //System.out.println(" event created ..");
            eventTable.put(event, freqlist);
        }
        
        return eventTable;
    }

    /**
     * 
     * @param eventTable
     * @param cls
     * @return 
     */
    private int getSum(HashMap<Event, ArrayList<Integer>> eventTable, String cls) {
        int sum = 0;
        for (Map.Entry<Event, ArrayList<Integer>> entry : eventTable.entrySet()) {
            Event event = entry.getKey();
            for (Map.Entry<String, Integer> entrySet : event.getClassDist().entrySet()) {
                String cls2 = entrySet.getKey();
                Integer freq = entrySet.getValue();
                if (cls.equalsIgnoreCase(cls2)) {
                    sum = sum + freq;
                    break;
                }
            }
        }
        return sum;
    }
    /**
     * 
     * @param eventTable
     * @param classSet 
     */
    public void designClassWiseEventDist(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet) {
       
        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            int sum = this.getSum(eventTable, cls);
            String str = "";
            int numOfEvent = 0;
            for (Map.Entry<Event, ArrayList<Integer>> entry : eventTable.entrySet()) {
                Event event = entry.getKey();
                String source = event.getYear() + "_" + event.getRegion() + "_" + event.getName() + "_" + event.getLang();
                for (Map.Entry<String, Integer> entrySet : event.getClassDist().entrySet()) {
                    String cls2 = entrySet.getKey();
                    Integer freq = entrySet.getValue();
                    if (cls.equalsIgnoreCase(cls2)) {
                        double dist =  (double)freq/(double)sum*100;
                        dist = Double.parseDouble(new DecimalFormat("##.##").format(dist));
                        str = str+source+"\t"+dist+"\t"+freq+"\n";
                        numOfEvent++;
                        break;
                    }
                }
            }
            System.out.println(":::"+cls+":::"+numOfEvent+"\n"+str);
        }        
    }    
    /**
     * 
     * @param eventTable
     * @param classSet 
     */
    public void designClassWiseSum(HashMap<Event, ArrayList<Integer>> eventTable, TreeSet<String> classSet) {       
        for (Iterator<String> iterator = classSet.iterator(); iterator.hasNext();) {
            String cls = iterator.next();
            int sum = this.getSum(eventTable, cls);
            String str = "";
            System.out.println(cls+"\t"+sum);
        }        
    }    
}
