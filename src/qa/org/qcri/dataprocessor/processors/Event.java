
package qa.org.qcri.dataprocessor.processors;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author firojalam
 */
public class Event {
    private String year = "";
    private String region = "";
    private String name = "";
    private String lang = "";
    private String eventSource = "";
    private ConcurrentHashMap<String, Integer> classDist = new ConcurrentHashMap<String, Integer>();
    private ArrayList<Instance> instList = new ArrayList<Instance>();

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String country) {
        this.region = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ConcurrentHashMap<String, Integer> getClassDist() {
        return classDist;
    }

    public void setClassDist(ConcurrentHashMap<String, Integer> classDist) {
        this.classDist = classDist;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public ArrayList<Instance> getInstList() {
        return instList;
    }

    public void setInstList(ArrayList<Instance> instList) {
        this.instList = instList;
    }
    
    
}
