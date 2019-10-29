package org.djw.db;

import org.apache.log4j.Logger;

public class DBConfig{
    final static Logger logger = Logger.getLogger(DBConfig.class);
    
    public DBConfig(){}
    
    public String url;
    public String dbName;
    public String driver;
    public String userName;
    public String password;
    public String db_descriptor;

	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	
	public void setDBName(String dbName) {
		this.dbName = dbName;
	}
	public String getDBName() {
		return dbName;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDriver() {
		return driver;
	}
    
	public void setUsername(String userName) {
		this.userName = userName;
	}	
	public String getUsername() {
		return userName;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getPassword() {
		return password;
	}

	public void setDBDescriptor(String db_descriptor) {
		this.db_descriptor = db_descriptor;
	}	
	public String getDBDescriptor() {
		return db_descriptor;
	}
 
    public DBConfig getDBConfig(){
        DBConfig dbConfig = new DBConfig();
        try {
            String p_url = "";
            String p_dbName = "";
            String p_driver = "";
            String p_userName = "";
            String p_password = "";
            String p_db_descriptor = "";
            
            if (System.getenv("url") != null) p_url = System.getenv("url");
            if (System.getenv("dbName") != null) p_dbName = System.getenv("dbName");
            if (System.getenv("driver") != null) p_driver = System.getenv("driver");
            if (System.getenv("userName") != null) p_userName = System.getenv("userName");
            if (System.getenv("password") != null) p_password = System.getenv("password");
            if (System.getenv("db_descriptor") != null) p_db_descriptor = System.getenv("db_descriptor");
            
            dbConfig.setUrl(p_url);
            dbConfig.setDBName(p_dbName);
            dbConfig.setDriver(p_driver);
            dbConfig.setUsername(p_userName);
            dbConfig.setPassword(p_password);
            dbConfig.setDBDescriptor(p_db_descriptor);
        } catch (Exception e){
            logger.fatal("Error: " + e);
        }

        return dbConfig;
    }
 
}