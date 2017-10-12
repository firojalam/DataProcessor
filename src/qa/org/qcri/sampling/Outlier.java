
package qa.org.qcri.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author firojalam
 */
public class Outlier {
    private double max = 0.0;
    private double mean = 0.0;

    public Outlier() {
    }

    public Outlier(HashMap<String, Integer> classDist) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Map.Entry<String, Integer> entry : classDist.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            stats.addValue(value);            
        }
        this.removeOutlier2(stats);
    }
    
    
    
    
    
    /**
     * General method to calculate the ourlier
     * @param stats
     * @return 
     */
    private ArrayList<Double> removeOutlier(DescriptiveStatistics stats){
        ArrayList<Double> outlierFiltered = new ArrayList<Double>();
        
        //double stddev = Math.sqrt(stats.getVariance());
        //double val = Math.abs(x - stats.getMean());
        double q1=stats.getPercentile(33);
        double q3=stats.getPercentile(66);
        double iqr=q3-q1;
        double low = q1-1.5*iqr;
        double high = q1+1.5*iqr;
        double[] values = stats.getValues();
        for (int i = 0; i < values.length; i++) {
            double value = values[i];
            if(value<low || value>high){
                continue;
            }
            outlierFiltered.add(value);
        }
        System.out.println("size: "+values.length);
        System.out.println("size: "+outlierFiltered.size());
        return outlierFiltered;
    }

    /**
     * General method to calculate the ourlier
     * @param stats
     * @return 
     */
    private void removeOutlier2(DescriptiveStatistics stats){
        
        double stddev = Math.sqrt(stats.getVariance());
        double mean = Math.sqrt(stats.getMean());
        double[] values = stats.getValues();   
        
        stats = new DescriptiveStatistics();
        for (int i = 0; i < values.length; i++) {
            double value = values[i];
            if((Math.abs(value - mean) < 3 * stddev)){
                //outlierFiltered.add(value);
                stats.addValue(value);
            }          
        }
        this.max = stats.getMax();
        this.mean = stats.getMean();
//        System.out.println("size: "+values.length);
//        System.out.println("size: "+outlierFiltered.size());
    }

    public double getMax() {
        return max;
    }

    public double getMean() {
        return mean;
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
        stats.addValue(1000000);
        stats.addValue(5000000);
        
        int numBin = 10;
        //Sampling sampling = new Sampling();
        //sampling.getBin(stats, numBin);
        Outlier outlier = new Outlier();
        outlier.removeOutlier2(stats);
                
        
    }
    
}
