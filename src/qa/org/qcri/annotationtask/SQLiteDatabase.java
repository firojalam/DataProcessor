/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */
package qa.org.qcri.annotationtask;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author firojalam
 */
public class SQLiteDatabase {

    String dbName = null;
    String path = null;
    String url = null;
    Connection conn = null;
    public SQLiteDatabase(String dbpath) {
        this.dbName = dbpath;
        url = "jdbc:sqlite:" + dbName;
    }
    
    public Connection connect() {        
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());            
        }
        return conn;
    }
    
    public void closeConnect() {
        
        try {
            //Connection conn = this.connect();
            conn.close();
                
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }                 
    }    

    public void createNewDatabase(String fileName) {

        try {
            Connection conn = DriverManager.getConnection(url);

            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    
    public static void main(String args[]) {
        //app.insert("Raw Materials", 3000);
        
    }
}
