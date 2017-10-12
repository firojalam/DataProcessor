/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.suffolk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import qa.org.qcri.dataprocessor.UnlabelledDataReader;
import qa.org.qcri.dataprocessor.processors.Instance;
import qa.org.qcri.dataprocessor.processors.TokenizeAndFilter;
import qa.org.qcri.utils.Write2File;

/**
 *
 * @author Firoj Alam
 */
public class ProcessToArff {

    private static Logger LOG = Logger.getLogger(ProcessToArff.class.getName());

    public void writeToArff(ArrayList<Tweet> articleList, String outFile) {

        Write2File wrt = new Write2File();
        wrt.openFile(outFile);
        /// get classList
        TreeSet clsSet = this.getClassSet(articleList);
        StringBuilder arffTxt = new StringBuilder();
        arffTxt.append("@relation AIDR\n\n");
        arffTxt.append("@attribute id string\n");
        arffTxt.append("@attribute text string\n");
        String str = "";
        for (Iterator it = clsSet.iterator(); it.hasNext();) {
            str = str + it.next().toString() + ",";

        }
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        arffTxt.append("@attribute class {").append(str).append("}\n");
        arffTxt.append("\n\n@data\n");
        wrt.write2File(arffTxt.toString());
        TokenizeAndFilter tokFilter = new TokenizeAndFilter();
        Pattern Whitespace = Pattern.compile("[\\s\\p{Zs}]+");
        for (int i = 0; i < articleList.size(); i++) {
            try {
                Tweet tweet = articleList.get(i);
                arffTxt = new StringBuilder();
                String lab = "?";
                if (tweet.getLabelManual() == null) {
                    lab = "?";
                }
                String text = tokFilter.tokenizeText(tweet.getMessage());
                //String orgText = Whitespace.matcher(tweet.getMessage()).replaceAll(" ").trim();
                arffTxt.append(tweet.getTweetID()).append("\t").append("\"").append(text).append("\"").append("\t").append(lab);
                wrt.write2File(arffTxt.toString() + "\n");
            } catch (Exception ex) {
                LOG.error("Error in writing data to file.");
            }
        }
        wrt.closeFile();
    }

    /**
     *
     * @param articleList
     * @param outFile
     */
    public void writeOrgToArff(ArrayList<Tweet> articleList, String outFile) {
        Write2File wrt = new Write2File();
        wrt.openFile(outFile);
        /// get classList
        StringBuilder arffTxt = new StringBuilder();
        Pattern Whitespace = Pattern.compile("[\\s\\p{Zs}]+");
        for (int i = 0; i < articleList.size(); i++) {
            try {
                Tweet tweet = articleList.get(i);
                arffTxt = new StringBuilder();
                String lab = "?";
                if (tweet.getLabelManual() == null) {
                    lab = "?";
                }
                //String text = tokFilter.tokenizeText(tweet.getMessage());
                String orgText = Whitespace.matcher(tweet.getMessage()).replaceAll(" ").trim();
                arffTxt.append(tweet.getTweetID()).append("\t").append("\"").append(orgText).append("\"").append("\t").append(lab);
                wrt.write2File(arffTxt.toString() + "\n");
            } catch (Exception ex) {
                LOG.error("Error in writing data to file.");
                ex.printStackTrace();
            }
        }
        wrt.closeFile();
    }

    /**
     *
     * @param articleList
     * @param wrt
     * @param outFile
     */
    public void writeToTSV(ArrayList<Tweet> articleList, Write2File wrt) {
        StringBuilder arffTxt = null;

        Pattern Whitespace = Pattern.compile("[\\s\\p{Zs}]+");
        for (int i = 0; i < articleList.size(); i++) {
            try {
                Tweet tweet = articleList.get(i);
                arffTxt = new StringBuilder();
                String orgText = Whitespace.matcher(tweet.getMessage()).replaceAll(" ").trim();
                //arffTxt.append(tweet.getTweetID()).append(",").append("\"").append(orgText).append("\"").append(",").append(tweet.getCrisisName()) ;
                arffTxt.append(tweet.getTweetID()).append("\t").append(orgText);
                wrt.write2File(arffTxt.toString() + "\n");
            } catch (Exception ex) {
                LOG.error("Error in writing data to file.");

            }
        }
        //wrt.closeFile();
    }

    public ArrayList<Tweet> filterInst(ArrayList<Tweet> instList) {
        ArrayList<Tweet> instListNew = new ArrayList<Tweet>();
        UnlabelledDataReader langFilter = new UnlabelledDataReader();
        HashSet<String> textSet = new HashSet<String>();
        for (int i = 0; i < instList.size(); i++) {
            try {
                Tweet inst = instList.get(i);
                String text = inst.getMessage();
                if (text.split("\\s+").length < 4) {
                    continue;
                }
                if (!langFilter.detect(text).equalsIgnoreCase("en") || langFilter.detect(text) == null) {
                    continue;
                }
                if (textSet.contains(text)) {
                    //do nothing
                } else {
                    textSet.add(text);
                    instListNew.add(inst);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instListNew;
    }

    /**
     *
     * @param articleList
     * @return
     */
    private TreeSet getClassSet(ArrayList<Tweet> articleList) {
        TreeSet clsSet = new TreeSet();
        for (int i = 0; i < articleList.size(); i++) {
            Tweet tweet = articleList.get(i);
            if (tweet.getLabelManual() == null || tweet.getLabelManual().isEmpty()) {
                clsSet.add("?");
            } else {
                clsSet.add(tweet.getLabelManual());
            }
        }
        return clsSet;
    }

}
