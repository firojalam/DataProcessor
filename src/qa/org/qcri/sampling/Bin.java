/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.sampling;

import java.util.ArrayList;
import qa.org.qcri.dataprocessor.processors.Instance;

/**
 *
 * @author firojalam
 */
public class Bin {
    private double start = 0.0;
    private double end = 0.0;
    private ArrayList<Instance> instList = new ArrayList<Instance>();

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public ArrayList<Instance> getInstList() {
        return instList;
    }

    public void setInstList(ArrayList<Instance> instList) {
        this.instList = instList;
    }
    

}

