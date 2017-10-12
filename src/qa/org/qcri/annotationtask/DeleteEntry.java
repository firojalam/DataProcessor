/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.annotationtask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import qa.org.qcri.suffolk.ReadFileList;
import qa.org.qcri.suffolk.Tweet;

/**
 *
 * @author firojalam
 */
public class DeleteEntry {
 
    private void deleteImageswTweet(SQLiteDatabase database,  ArrayList<String> fileDict) {
        Connection conn = database.connect();        
        String sql = "DELETE FROM image WHERE imageId = ?";
        for (Iterator<String> iterator = fileDict.iterator(); iterator.hasNext();) {
            int id = Integer.parseInt(iterator.next());            
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }            
            id++;
        }
       
        database.closeConnect();
    }
    public static void main(String args[]) {
        String dbName = "/Applications/XAMPP/xamppfiles/htdocs/assessDamage/damage.db";
        SQLiteDatabase database = new SQLiteDatabase(dbName);
        String inputFileName = "/Users/firojalam/QCRI/PEIC/image_entry_to_delete.txt";
        ReadFileList readList = new ReadFileList();
        ArrayList<String> fileDict = readList.readEventList(inputFileName);
        
        DeleteEntry deleteEntry = new DeleteEntry();
        deleteEntry.deleteImageswTweet(database, fileDict);
     
    }
}
