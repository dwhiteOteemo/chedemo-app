package org.djw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class DBTable {
	final static Logger logger = Logger.getLogger(DBTable.class);
	public DBTable(){}
	
	String Name;
	String Query;
	ArrayList<DBColumn> Columns;
	ArrayList<ArrayList<String>> Rows;
	String DBType;
	public void setDBType(String dBType) {
		DBType = dBType;
	}
	public String getDBType() {
		return DBType;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getQuery() {
		return Query;
	}
	public void setQuery(String query) {
		Query = query;
	}
	public ArrayList<DBColumn> getColumns() {
		return Columns;
	}
	public void setColumns(ArrayList<DBColumn> columns) {
		Columns = columns;
	}
	public ArrayList<ArrayList<String>> getRows() {
		return Rows;
	}
	public void setRows(ArrayList<ArrayList<String>> rows) {
		Rows = rows;
	}

	public DBTable getDBTableResults(String url, String dbName, String driver, String userName, String password, String sqlStatement) {
		DBTable dbObject = new DBTable();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rResults = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement();
			rResults = stmt.executeQuery(sqlStatement);

			ResultSetMetaData rsMetaData = rResults.getMetaData();
			ArrayList<DBColumn> Columns = new ArrayList<DBColumn>();
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				String columnName = rsMetaData.getColumnName(i);
				String columnLabel = rsMetaData.getColumnLabel(i);
				String columnType = rsMetaData.getColumnTypeName(i);
				int columnPrecision = rsMetaData.getPrecision(i);
				int columnScale = rsMetaData.getScale(i);

				DBColumn myColumn = new DBColumn();
				myColumn.setColName(columnName);
				myColumn.setColLabel(columnLabel);
				myColumn.setColType(columnType);
				myColumn.setColPrecision(columnPrecision);
				myColumn.setColScale(columnScale);
				Columns.add(myColumn);
				
			}
			dbObject.setName("TestTable");
			dbObject.setQuery(sqlStatement);
			dbObject.setColumns(Columns);
			
			ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
			while (rResults.next()) {
				ArrayList<String> row = new ArrayList<String>();
				for (int i = 0; i < Columns.size(); i++) {
					String cName = Columns.get(i).getColName();
					// Use type later.
//					String cType = Columns.get(i).getColType();
					String colData = rResults.getString(cName);
					row.add(colData);
				}
				rows.add(row);
			}
			dbObject.setRows(rows);
		} catch (SQLException e) {
			logger.fatal(e);
		} catch (InstantiationException e) {
			logger.fatal(e);
		} catch (IllegalAccessException e) {
			logger.fatal(e);
		} catch (ClassNotFoundException e) {
			logger.fatal(e);
		} finally {
			try {
				rResults.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return dbObject;
	}

	public boolean execDML(String url, String dbName, String driver, String userName, String password, String sqlStatement) {
		boolean Success = true;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rResults = null;
		try {
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlStatement);
			Success = true;
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			DBConMgr.closeDBResources(conn, stmt, rResults);
		}
		return Success;
	}

	public String getSingleResult(String url, String dbName, String driver, String userName, String password, String sqlStatement) {
		String queryResult = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rResults = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement();
			rResults = stmt.executeQuery(sqlStatement);
			while (rResults.next()) {
				if (rResults.getString("num") != null)
					queryResult = rResults.getString("num");
			}
		} catch (SQLException e) {
			logger.fatal(e);;
		} catch (InstantiationException e) {
			logger.fatal(e);;
		} catch (IllegalAccessException e) {
			logger.fatal(e);;
		} catch (ClassNotFoundException e) {
			logger.fatal(e);;
		} finally {
			try {
				rResults.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return queryResult;
	}

	public String getSingleValueL(String url, String dbName, String driver, String userName, String password, String sqlStatement) {
		String queryResult = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rResults = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement();
			rResults = stmt.executeQuery(sqlStatement);
			while (rResults.next()) {
					queryResult = rResults.getString(1);
			}
		} catch (SQLException e) {
			logger.fatal(e);;
		} catch (InstantiationException e) {
			logger.fatal(e);;
		} catch (IllegalAccessException e) {
			logger.fatal(e);;
		} catch (ClassNotFoundException e) {
			logger.fatal(e);;
		} finally {
			try {
				rResults.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return queryResult;
	}

}
