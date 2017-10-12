
/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.annotationtask;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import qa.org.qcri.suffolk.JSONParser;
import qa.org.qcri.suffolk.ReadFileList;
import qa.org.qcri.suffolk.Tweet;

/**
 *
 * @author firojalam
 */
public class DataPrepare {

    public void insertCat(SQLiteDatabase database, ArrayList<String> catList) {

        System.out.println("");
        Connection conn = database.connect();
        for (int i = 0; i < catList.size(); i++) {
            String str = catList.get(i);
            String arr[] = str.split("\t");
            String cat = arr[0];
            String maincat = arr[1];
            String sql = "INSERT INTO damage(damageTypeId,damageType,mainType) VALUES(?,?,?)";
            int catID = i + 1;
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, catID);
                pstmt.setString(2, cat);
                pstmt.setString(3, maincat);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        database.closeConnect();

    }

    private void insertImages(SQLiteDatabase database, ArrayList<String> imageList) {
        Connection conn = database.connect();
        for (int i = 0; i < imageList.size(); i++) {
            int id = i + 1;
            String image = "images/" + imageList.get(i);
            String sql = "INSERT INTO image(imageId,image,metaData) VALUES(?,?,?)";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, image);
                pstmt.setString(3, "");
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        database.closeConnect();
    }

    private void insertImageswTweet(SQLiteDatabase database, HashMap<String, Tweet> tweetDict) {
        Connection conn = database.connect();
        int id = 1;
        for (Map.Entry<String, Tweet> entry : tweetDict.entrySet()) {
            String imagePath = entry.getKey();
            Tweet tweet = entry.getValue();
            String image = "images/" + imagePath;
            String sql = "INSERT INTO image(imageId,image,metaData,tweet) VALUES(?,?,?,?)";
            try {
                String tweetTxt = tweet.getMessage();
                tweetTxt = tweetTxt.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, image);
                pstmt.setString(3, "");
                
                pstmt.setString(4, tweetTxt);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }            
            id++;
        }
       
        database.closeConnect();
    }
    
    public static void mainOld(String args[]) {
        DataPrepare dataPrepare = new DataPrepare();
        String dbName = "damage.db";
        SQLiteDatabase database = new SQLiteDatabase(dbName);

        String catFile = "/Users/firojalam/QCRI/PEIC/categories.txt";
        String imageFile = "/Users/firojalam/QCRI/PEIC/relevant_image_evaluation/images/";

        DataReader reader = new DataReader();

//        ArrayList<String> catList = reader.readCategories(catFile);
//        dataPrepare.insertCat(database,catList);
        ArrayList<String> imageList = reader.getImageListfromPath(imageFile);
        dataPrepare.insertImages(database, imageList);
    }

    public void readJsons() {

    }

    public static void main(String args[]) {
        DataPrepare dataPrepare = new DataPrepare();
        String dbName = "/Applications/XAMPP/xamppfiles/htdocs/assessDamage/damage.db";
        SQLiteDatabase database = new SQLiteDatabase(dbName);

        String catFile = "/Users/firojalam/QCRI/PEIC/categories.txt";
        String imageFile = "/Users/firojalam/QCRI/PEIC/relevant_image_evaluation/images";

        DataReader reader = new DataReader();

//        ArrayList<String> catList = reader.readCategories(catFile);
//        dataPrepare.insertCat(database,catList);
        HashMap<String, String> imageList = reader.getImageList(imageFile);

        /**
         * Reads the JSONs
         */
        String inputFileName = "/Users/firojalam/QCRI/PEIC/file_list.txt";
        ReadFileList readList = new ReadFileList();
        ArrayList<String> fileDict = readList.readEventList(inputFileName);
        JSONParser parser = new JSONParser();
        HashMap<String, Tweet> tweetList = new HashMap();
        for (String fullPath : fileDict) {
            HashMap<String, Tweet> tweetListtmp = parser.readJSONFilestoDict(fullPath);
            tweetList.putAll(tweetListtmp);
        }///end of hashMap                       

        HashMap<String, Tweet> tweetDict = new HashMap();
        for (Map.Entry<String, String> entry : imageList.entrySet()) {
            String tweetID = entry.getKey();
            String imagePath = entry.getValue();
            tweetDict.put(imagePath, tweetList.get(tweetID));
        }

        dataPrepare.insertImageswTweet(database, tweetDict);
    }

}
