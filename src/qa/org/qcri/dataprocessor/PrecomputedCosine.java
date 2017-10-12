/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor;
import info.debatty.java.stringsimilarity.*;
import java.util.Map;
/**
 *
 * @author firojalam
 */
public class PrecomputedCosine {
 
public static void main(String[] args) throws Exception {
        String s1 = "School Attack";
        String s2 = "School Attack";
        
        // Let's work with sequences of 2 characters...
        Cosine cosine = new Cosine(2);
        
        Map<String, Integer> profile1 = cosine.getProfile(s1);
        Map<String, Integer> profile2 = cosine.getProfile(s2);

        // Prints 0.516185
        System.out.println(cosine.similarity(profile1, profile2));

        
        // Prints 0.516185
        
    }    
}
