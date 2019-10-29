package org.djw.services;

import org.djw.db.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class DBResource extends ServerResource {
	final static Logger logger = Logger.getLogger(DBResource.class);
	// final static String JedisServer = GeneralUtils.getConfigPropValue("redis-server");
	@Get
	public Representation represent() throws JSONException {
		Representation rep = null;
		String Error = "";
		if (getRequest().getAttributes().get("Error") != null ) Error = (String) getRequest().getAttributes().get("Error");
		JSONObject jResponse = new JSONObject();
		JSONObject jBody = new JSONObject();
		if (Error.equals("")){
			String Action = "";
			String Country = "";
			if (getRequest().getAttributes().get("action") != null)
				Action = (String) getRequest().getAttributes().get("action");
			if (getRequest().getAttributes().get("country") != null)
				Country = (String) getRequest().getAttributes().get("country");
			try {
				Country = URLDecoder.decode(Country, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Country Selected: " + Country);
		    DBConfig dbConfig = new DBConfig();
			dbConfig = dbConfig.getDBConfig();
			JSONObject jResults = new JSONObject();
			if (!Action.equals("")){
				if (Action.equals("countryList")){
					jResults = this.getCountryList(dbConfig);
				}
				if (Action.equals("getCountry")){
					jResults = this.getCountry(dbConfig, Country);
				}
			} else {
				jResults = this.searchResult(dbConfig, Country);
			}
		    
            jBody.put("results",jResults);
			jResponse.put("StatusCode", 0);
			jResponse.put("body", jBody);
			
			
		} else {
			JSONObject errorJson = new JSONObject();
			int errorCode = 200;
			String errorMessage = "This is a test error message";
			JSONObject jError = errorJson.put("errorCode", errorMessage);
			jBody.put("ErrorCode", errorCode);
			jBody.put("ErrorMessage", "This is an error message");
			jResponse.put("StatusCode", 1);
			jResponse.put("Message", "This is an error status");
			jResponse.put("body",jError);
		}
		rep = new JsonRepresentation(jResponse);
		return rep;
	}
	
	public JSONObject searchResult(DBConfig dbConfig, String Country){
	    JSONObject jResults = new JSONObject();
	    // Jedis jedis = new Jedis(JedisServer);
	    try{
	        boolean keyFound = false;
	        if (keyFound){
	        } else {
	            jResults = this.getDBResults(dbConfig, Country);
	        }
	    } catch (Exception e){
	        logger.fatal(e);
	    } finally {
	    }
	    
	    return jResults;
	}
	
	public JSONObject getDBResults(DBConfig dbConfig, String Country){
	    JSONObject jResults = new JSONObject();
	    DBTable dbTable = new DBTable();
	    String url = dbConfig.getUrl();
	    String dbName = dbConfig.getDBName();
	    String driver = dbConfig.getDriver();
	    String userName = dbConfig.getUsername();
	    String password = dbConfig.getPassword();
        
        JSONArray cols = new JSONArray();
        cols.put("Name");
        cols.put("Population 2019");
        cols.put("GDP (IMF)");
        cols.put("GDP (UN 16)");
        cols.put("GDP Per Capita");
		
		String sqlStatement = "";
		String sqlSelect = "select name,format(population_2019,0),format(gdp_imf,0),format(gdp_un16,0),format(gdp_per_capita,0) ";
		String sqlFrom = "from countries ";
		String sqlWhere = "";
		String sqlOrder = "order by gdp_imf desc";

		if (Country.equals("All") || Country.equals("")){
			sqlStatement = sqlSelect + sqlFrom + sqlOrder;
		} else {
			sqlWhere = "where name = '" + Country + "' ";
			sqlStatement = sqlSelect + sqlFrom + sqlWhere + sqlOrder;
		}
	    dbTable = dbTable.getDBTableResults(url, dbName, driver, userName, password, sqlStatement);
        
        JSONArray jRows = new JSONArray();
        for (int r=0; r<dbTable.getRows().size(); r++){
            ArrayList<String> row = dbTable.getRows().get(r);
            jRows.put(row);
        }
        jResults.put("cols", cols);
	    jResults.put("rows",jRows);
	    return jResults;
	}

	public JSONObject getCountryList(DBConfig dbConfig){
	    JSONObject jResults = new JSONObject();
	    DBTable dbTable = new DBTable();
	    String url = dbConfig.getUrl();
	    String dbName = dbConfig.getDBName();
	    String driver = dbConfig.getDriver();
	    String userName = dbConfig.getUsername();
	    String password = dbConfig.getPassword();
	    String sqlStatement = "select name " +
            "from countries " +
            "order by name";
	    dbTable = dbTable.getDBTableResults(url, dbName, driver, userName, password, sqlStatement);        
        JSONArray jRows = new JSONArray();
        for (int r=0; r<dbTable.getRows().size(); r++){
            String row = dbTable.getRows().get(r).get(0);
            jRows.put(row);
        }
	    jResults.put("countries",jRows);
	    return jResults;
	}

	public JSONObject getCountry(DBConfig dbConfig, String Country){
	    JSONObject jResults = new JSONObject();
	    DBTable dbTable = new DBTable();
	    String url = dbConfig.getUrl();
	    String dbName = dbConfig.getDBName();
	    String driver = dbConfig.getDriver();
	    String userName = dbConfig.getUsername();
	    String password = dbConfig.getPassword();
        
        JSONArray cols = new JSONArray();
        cols.put("Name");
        cols.put("Population 2019");
        cols.put("GDP (IMF)");
        cols.put("GDP (UN 16)");
        cols.put("GDP Per Capita");

		String sqlStatement = "";
		String sqlSelect = "select name,format(population_2019,0),format(gdp_imf,0),format(gdp_un16,0),format(gdp_per_capita,0) ";
		String sqlFrom = "from countries ";
		String sqlWhere = "";
		String sqlOrder = "order by gdp_imf desc";

		if (Country.equals("All")){
			sqlStatement = sqlSelect + sqlFrom + sqlOrder;
		} else {
			sqlWhere = "where name = '" + Country + "' ";
			sqlStatement = sqlSelect + sqlFrom + sqlWhere + sqlOrder;
		}
	    dbTable = dbTable.getDBTableResults(url, dbName, driver, userName, password, sqlStatement);
        
        JSONArray jRows = new JSONArray();
        for (int r=0; r<dbTable.getRows().size(); r++){
            ArrayList<String> row = dbTable.getRows().get(r);
            jRows.put(row);
        }
        jResults.put("cols", cols);
	    jResults.put("rows",jRows);
	    return jResults;
	}	
}