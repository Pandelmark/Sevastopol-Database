package config;

/**
 *
 * @author Pandelmark
 */
public class DatabaseConfig {
           
    //SQL Query Modification
    public static final String TABLE_NAME = "residents";           //table name of your choosing
    public static final String DB_USER = "Ripley";                 //the username of the db user
    public static final String DB_PASS = "1234";                   //the passwordof the db user
    
    static final String MARIADB_SUB = "jdbc:mariadb:";      //subprotocol name for mariadb
    static final String DB_SERVER = "//localhost:3306/";    //the address and port of the db server
    static final String DB_NAME = "sevastopol_db";         //the name of the database
    public static final String DB_URL = MARIADB_SUB + DB_SERVER + DB_NAME;   //url if we use mariadb
    
    // Getter methods for properties
    public static String getTableName() {
        return TABLE_NAME;
    }
    
    public static String getDbUrl() {
        return DB_URL;
    }
    
    public static String getDbUser() {
        return DB_USER;
    }
    
    public static String getDbPass() {
        return DB_PASS;
    }
}
