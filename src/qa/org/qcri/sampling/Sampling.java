package qa.org.qcri.sampling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import qa.org.qcri.dataprocessor.processors.Instance;

/**
 *
 * @author firojalam
 */
public class Sampling {

    /**
     * 
     * @param dataDist
     * @param max
     * @return 
     */
    public ArrayList<String> selectcClassLabel(HashMap<String, Integer> dataDist, int max) {
        ArrayList<String> classList = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : dataDist.entrySet()) {
            String classLabel = entry.getKey();
            int count = entry.getValue();
            if (count >= max) {
                classList.add(classLabel);
            }
        }
        return classList;
    }

    /**
     * 
     * @param stats
     * @param numBin
     * @return 
     */
    public ArrayList<Bin> getBin(DescriptiveStatistics stats, int numBin) {
        ArrayList<Bin> bins = new ArrayList<Bin>();
        double binStart = 0.0;
        double binEnd = 0.0;
        double binInterval = (double) numBin / 100;

        DecimalFormat fmt = new DecimalFormat("##.##");
        for (int i = 1; i <= numBin; i++) {
            Bin bin = new Bin();
            double val = Double.parseDouble(fmt.format(binStart));
            bin.setStart(val);
            binInterval = 100 / numBin * i;
            binEnd = stats.getPercentile(binInterval);
            val = Double.parseDouble(fmt.format(binEnd));
            bin.setEnd(val);
            binStart = binEnd;
            //System.out.println("ST: "+bin.getStart()+"\tEND: "+bin.getEnd());
            bins.add(bin);
        }
        return bins;
    }

    
    /**
     * Randomly select instances of majority class from the data
     * Maximum number of instances it will select is basically average number of instances across class labels
     * @param dataInstance - whole data instance
     * @param classList - class labels
     * @param numberOfBin number of bin
     * @param avgNumBin
     * @return 
     */
    public HashMap<String, ArrayList<Instance>> generateData(HashMap<String, ArrayList<Instance>> dataInstance,ArrayList<String> classList,int numberOfBin , int avgNumBin){
        //HashMap<String, ArrayList<Instance>> data = new HashMap();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < classList.size(); i++) {
            String classlab = classList.get(i);
            ArrayList<Instance> instList = dataInstance.get(classlab);
            for (int j = 0; j < instList.size(); j++) {
                Instance inst = instList.get(j);
                stats.addValue(inst.getNumOfWord());
            }
            ArrayList<Bin> binlist = this.getBin(stats, numberOfBin);
            for (int j = 0; j < instList.size(); j++) {
                Instance inst = instList.get(j);
                for (int k = 0; k < binlist.size(); k++) {
                    Bin bin = binlist.get(k);
                    if(inst.getNumOfWord()>=bin.getStart()&& inst.getNumOfWord()<bin.getEnd()){
                        bin.getInstList().add(inst);
                    }
                }
             }

            //ArrayList<Bin> binlist = this.getBin(stats, numberOfBin);
            ArrayList<Instance> newInstList = new ArrayList<Instance>();
            int numOfItemEachBin = avgNumBin/numberOfBin;
            for (int j = 0; j < binlist.size(); j++) {
                Bin bin = binlist.get(j);
                ArrayList<Instance> binInstList = bin.getInstList();
                Collections.shuffle(binInstList, new Random(100000));                
                if(numOfItemEachBin>binInstList.size()){
                    numOfItemEachBin = binInstList.size()-1;
                }
                for (int k = 0; k < numOfItemEachBin; k++) {
                    try {
                        Instance inst = binInstList.get(k);
                        newInstList.add(inst);
                        //binInstList.remove(k);
                        //k--;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
             }
            dataInstance.put(classlab,newInstList);
        }
        for (Map.Entry<String, ArrayList<Instance>> entry : dataInstance.entrySet()) {
            String key = entry.getKey();
            ArrayList<Instance> value = entry.getValue();
            System.out.println(key+"\t"+value.size());
        }
        return dataInstance;
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (int i = 0; i <= 1000; i++) {
            stats.addValue(i);
        }
        int numBin = 10;
        Sampling sampling = new Sampling();
        sampling.getBin(stats, numBin);
    }
}
